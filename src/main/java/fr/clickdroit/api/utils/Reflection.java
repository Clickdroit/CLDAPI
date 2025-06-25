package fr.clickdroit.api.utils;

import org.bukkit.Bukkit;

import java.lang.reflect.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Arrays;

public final class Reflection {
    // Caches thread-safe pour éviter les recherches répétées
    private static final ConcurrentMap<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>(256);
    private static final ConcurrentMap<String, Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<>(128);
    private static final ConcurrentMap<String, Method> METHOD_CACHE = new ConcurrentHashMap<>(512);
    private static final ConcurrentMap<String, Field> FIELD_CACHE = new ConcurrentHashMap<>(256);

    // Cache pour la version du serveur (appelée fréquemment)
    private static volatile String serverVersion;

    // Cache pour Unsafe
    private static volatile sun.misc.Unsafe unsafeInstance;

    // Constructeurs
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        String key = buildConstructorKey(clazz, parameterTypes);
        Constructor<?> cached = CONSTRUCTOR_CACHE.get(key);
        if (cached != null) {
            return cached;
        }

        Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        Constructor<?>[] constructors = clazz.getConstructors();

        for (Constructor<?> constructor : constructors) {
            if (DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveTypes)) {
                CONSTRUCTOR_CACHE.put(key, constructor);
                return constructor;
            }
        }
        throw new NoSuchMethodException("There is no such constructor in this class with the specified parameter types");
    }

    public static Constructor<?> makeConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        try {
            return getConstructor(clazz, parameterTypes);
        } catch (NoSuchMethodException ex) {
            return null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T callConstructor(Constructor<T> constructor, Object... parameters) {
        if (constructor == null) {
            throw new RuntimeException("No such constructor");
        }

        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }

        try {
            return constructor.newInstance(parameters);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex.getCause());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object instantiateObject(Class<?> clazz, Object... arguments) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return getConstructor(clazz, DataType.getPrimitive(arguments)).newInstance(arguments);
    }

    // Méthodes
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        String key = buildMethodKey(clazz, methodName, parameterTypes);
        Method cached = METHOD_CACHE.get(key);
        if (cached != null) {
            return cached;
        }

        Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName) &&
                    DataType.compare(DataType.getPrimitive(method.getParameterTypes()), primitiveTypes)) {
                METHOD_CACHE.put(key, method);
                return method;
            }
        }
        throw new NoSuchMethodException("There is no such method in this class with the specified name and parameter types");
    }

    public static Method makeMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return getMethod(clazz, name, parameterTypes);
        } catch (NoSuchMethodException ex) {
            return null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object callMethod(Method method, Object instance, Object... parameters) {
        if (method == null) {
            throw new RuntimeException("No such method");
        }

        if (!method.isAccessible()) {
            method.setAccessible(true);
        }

        try {
            return method.invoke(instance, parameters);
        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex.getCause());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object invokeMethod(Object instance, String methodName, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return getMethod(instance.getClass(), methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
    }

    public static Object invokeMethod(Object instance, Class<?> clazz, String methodName, Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        return getMethod(clazz, methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
    }

    // Fields avec cache
    public static Field makeField(Class<?> clazz, String name) {
        String key = clazz.getName() + "#" + name;
        Field cached = FIELD_CACHE.get(key);
        if (cached != null) {
            return cached;
        }

        try {
            Field field = clazz.getDeclaredField(name);
            FIELD_CACHE.put(key, field);
            return field;
        } catch (NoSuchFieldException ex) {
            return null;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static <T> T getField(Field field, Object instance) {
        if (field == null) {
            throw new RuntimeException("No such field");
        }

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        try {
            return (T) field.get(instance);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void setField(Field field, Object instance, Object value) {
        if (field == null) {
            throw new RuntimeException("No such field");
        }

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        try {
            field.set(instance, value);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object getValue(Object instance, Class<?> clazz, boolean declared, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        String key = clazz.getName() + "#" + fieldName + "#" + declared;
        Field field = FIELD_CACHE.get(key);

        if (field == null) {
            field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
            field.setAccessible(true);
            FIELD_CACHE.put(key, field);
        }

        return field.get(instance);
    }

    public static Field getField(Class<?> clazz, boolean declared, String fieldName) throws NoSuchFieldException {
        String key = clazz.getName() + "#" + fieldName + "#" + declared;
        Field cached = FIELD_CACHE.get(key);

        if (cached != null) {
            return cached;
        }

        Field field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
        field.setAccessible(true);
        FIELD_CACHE.put(key, field);
        return field;
    }

    public static void setValue(Object instance, Class<?> clazz, boolean declared, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(clazz, declared, fieldName);
        field.set(instance, value);
    }

    // Optimisation de setFinalStatic avec lazy loading d'Unsafe
    public static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        try {
            // Essayer la méthode normale d'abord
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, newValue);
            return;
        } catch (NoSuchFieldException | IllegalAccessException | SecurityException e) {
            // La méthode normale a échoué, essayer avec Unsafe
        }

        // Méthode alternative avec Unsafe (lazy loading)
        try {
            sun.misc.Unsafe unsafe = getUnsafe();
            Object staticFieldBase = unsafe.staticFieldBase(field);
            long staticFieldOffset = unsafe.staticFieldOffset(field);
            unsafe.putObject(staticFieldBase, staticFieldOffset, newValue);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field " + field.getName() + " using both reflection and Unsafe", e);
        }
    }

    // Lazy loading pour Unsafe
    private static sun.misc.Unsafe getUnsafe() throws Exception {
        if (unsafeInstance == null) {
            synchronized (Reflection.class) {
                if (unsafeInstance == null) {
                    Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                    unsafeField.setAccessible(true);
                    unsafeInstance = (sun.misc.Unsafe) unsafeField.get(null);
                }
            }
        }
        return unsafeInstance;
    }

    // Classes avec cache amélioré
    public static Class<?> getClass(String name) {
        Class<?> cached = CLASS_CACHE.get(name);
        if (cached != null) {
            return cached;
        }

        try {
            Class<?> clazz = Class.forName(name);
            CLASS_CACHE.put(name, clazz);
            return clazz;
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    public static <T> Class<? extends T> getClass(String name, Class<T> superClass) {
        try {
            return Class.forName(name).asSubclass(superClass);
        } catch (ClassNotFoundException | ClassCastException ex) {
            return null;
        }
    }

    // Méthodes utilitaires pour construire les clés de cache
    private static String buildConstructorKey(Class<?> clazz, Class<?>... parameterTypes) {
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append("#<init>#");
        if (parameterTypes.length > 0) {
            for (int i = 0; i < parameterTypes.length; i++) {
                if (i > 0) sb.append(',');
                sb.append(parameterTypes[i].getName());
            }
        }
        return sb.toString();
    }

    private static String buildMethodKey(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        StringBuilder sb = new StringBuilder(clazz.getName());
        sb.append('#').append(methodName).append('#');
        if (parameterTypes.length > 0) {
            for (int i = 0; i < parameterTypes.length; i++) {
                if (i > 0) sb.append(',');
                sb.append(parameterTypes[i].getName());
            }
        }
        return sb.toString();
    }

    // Méthode pour nettoyer les caches si nécessaire
    public static void clearCaches() {
        CLASS_CACHE.clear();
        CONSTRUCTOR_CACHE.clear();
        METHOD_CACHE.clear();
        FIELD_CACHE.clear();
    }

    // Enum pour les types de données (optimisé)
    public enum DataType {
        BYTE(byte.class, Byte.class),
        SHORT(short.class, Short.class),
        INTEGER(int.class, Integer.class),
        LONG(long.class, Long.class),
        CHARACTER(char.class, Character.class),
        FLOAT(float.class, Float.class),
        DOUBLE(double.class, Double.class),
        BOOLEAN(boolean.class, Boolean.class);

        private static final ConcurrentMap<Class<?>, DataType> CLASS_MAP = new ConcurrentHashMap<>();

        private final Class<?> primitive;
        private final Class<?> reference;

        static {
            for (DataType type : values()) {
                CLASS_MAP.put(type.primitive, type);
                CLASS_MAP.put(type.reference, type);
            }
        }

        DataType(Class<?> primitive, Class<?> reference) {
            this.primitive = primitive;
            this.reference = reference;
        }

        public Class<?> getPrimitive() {
            return primitive;
        }

        public Class<?> getReference() {
            return reference;
        }

        public static DataType fromClass(Class<?> clazz) {
            return CLASS_MAP.get(clazz);
        }

        public static Class<?> getPrimitive(Class<?> clazz) {
            DataType type = fromClass(clazz);
            return type == null ? clazz : type.primitive;
        }

        public static Class<?> getReference(Class<?> clazz) {
            DataType type = fromClass(clazz);
            return type == null ? clazz : type.reference;
        }

        public static Class<?>[] getPrimitive(Class<?>[] classes) {
            if (classes == null || classes.length == 0) {
                return new Class[0];
            }

            Class<?>[] types = new Class[classes.length];
            for (int i = 0; i < classes.length; i++) {
                types[i] = getPrimitive(classes[i]);
            }
            return types;
        }

        public static Class<?>[] getReference(Class<?>[] classes) {
            if (classes == null || classes.length == 0) {
                return new Class[0];
            }

            Class<?>[] types = new Class[classes.length];
            for (int i = 0; i < classes.length; i++) {
                types[i] = getReference(classes[i]);
            }
            return types;
        }

        public static Class<?>[] getPrimitive(Object[] objects) {
            if (objects == null || objects.length == 0) {
                return new Class[0];
            }

            Class<?>[] types = new Class[objects.length];
            for (int i = 0; i < objects.length; i++) {
                types[i] = getPrimitive(objects[i].getClass());
            }
            return types;
        }

        public static Class<?>[] getReference(Object[] objects) {
            if (objects == null || objects.length == 0) {
                return new Class[0];
            }

            Class<?>[] types = new Class[objects.length];
            for (int i = 0; i < objects.length; i++) {
                types[i] = getReference(objects[i].getClass());
            }
            return types;
        }

        // Optimisation de la méthode compare
        public static boolean compare(Class<?>[] primary, Class<?>[] secondary) {
            if (primary == secondary) return true;
            if (primary == null || secondary == null || primary.length != secondary.length) {
                return false;
            }

            for (int i = 0; i < primary.length; i++) {
                Class<?> primaryClass = primary[i];
                Class<?> secondaryClass = secondary[i];
                if (primaryClass != secondaryClass && !primaryClass.isAssignableFrom(secondaryClass)) {
                    return false;
                }
            }
            return true;
        }
    }

    // Enum pour les types de packages (optimisé avec lazy loading)
    public enum PackageType {
        MINECRAFT_SERVER("net.minecraft.server."),
        CRAFTBUKKIT("org.bukkit.craftbukkit."),
        BUKKIT("org.bukkit"),
        NMS("net.minecraft.server.");

        private final String basePath;
        private volatile String fullPath;

        PackageType(String basePath) {
            this.basePath = basePath;
        }

        public String getPath() {
            if (fullPath == null) {
                synchronized (this) {
                    if (fullPath == null) {
                        if (this == BUKKIT) {
                            fullPath = basePath;
                        } else {
                            fullPath = basePath + getServerVersion();
                        }
                    }
                }
            }
            return fullPath;
        }

        public Class<?> getClass(String className) throws ClassNotFoundException {
            return Class.forName(getPath() + "." + className);
        }

        @Override
        public String toString() {
            return getPath();
        }

        // Lazy loading pour la version du serveur
        public static String getServerVersion() {
            if (serverVersion == null) {
                synchronized (PackageType.class) {
                    if (serverVersion == null) {
                        serverVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
                    }
                }
            }
            return serverVersion;
        }
    }
}