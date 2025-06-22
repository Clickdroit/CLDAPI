package fr.clickdroit.api.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public final class Reflection {
    private static final Map<String, Class<?>> classCache = new HashMap<>();

    // Constructeurs
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes) throws NoSuchMethodException {
        Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveTypes)) {
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
        constructor.setAccessible(true);
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
        Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName) &&
                    DataType.compare(DataType.getPrimitive(method.getParameterTypes()), primitiveTypes)) {
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

    public static <T> T callMethod(Method method, Object instance, Object... parameters) {
        if (method == null) {
            throw new RuntimeException("No such method");
        }
        method.setAccessible(true);
        try {
            return (T) method.invoke(instance, parameters);
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

    // Fields
    public static Field getField(Class<?> clazz, boolean declared, String fieldName) throws NoSuchFieldException, SecurityException {
        Field field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static Field makeField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
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
        field.setAccessible(true);
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
        field.setAccessible(true);
        try {
            field.set(instance, value);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object getValue(Object instance, Class<?> clazz, boolean declared, String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        return getField(clazz, declared, fieldName).get(instance);
    }

    public static void setValue(Object instance, boolean declared, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        getField(instance.getClass(), declared, fieldName).set(instance, value);
    }

    public static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

    // Classes
    public static Class<?> getClass(String name) {
        try {
            if (classCache.containsKey(name)) {
                return classCache.get(name);
            }
            Class<?> clazz = Class.forName(name);
            classCache.put(name, clazz);
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

    // Enum pour les types de données
    public enum DataType {
        BYTE(byte.class, Byte.class),
        SHORT(short.class, Short.class),
        INTEGER(int.class, Integer.class),
        LONG(long.class, Long.class),
        CHARACTER(char.class, Character.class),
        FLOAT(float.class, Float.class),
        DOUBLE(double.class, Double.class),
        BOOLEAN(boolean.class, Boolean.class);

        private static final Map<Class<?>, DataType> CLASS_MAP = new HashMap<>();

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
            return type == null ? clazz : type.getPrimitive();
        }

        public static Class<?> getReference(Class<?> clazz) {
            DataType type = fromClass(clazz);
            return type == null ? clazz : type.getReference();
        }

        public static Class<?>[] getPrimitive(Class<?>[] classes) {
            int length = classes == null ? 0 : classes.length;
            Class<?>[] types = new Class[length];
            for (int index = 0; index < length; index++) {
                types[index] = getPrimitive(classes[index]);
            }
            return types;
        }

        public static Class<?>[] getReference(Class<?>[] classes) {
            int length = classes == null ? 0 : classes.length;
            Class<?>[] types = new Class[length];
            for (int index = 0; index < length; index++) {
                types[index] = getReference(classes[index]);
            }
            return types;
        }

        public static Class<?>[] getPrimitive(Object[] objects) {
            int length = objects == null ? 0 : objects.length;
            Class<?>[] types = new Class[length];
            for (int index = 0; index < length; index++) {
                types[index] = getPrimitive(objects[index].getClass());
            }
            return types;
        }

        public static Class<?>[] getReference(Object[] objects) {
            int length = objects == null ? 0 : objects.length;
            Class<?>[] types = new Class[length];
            for (int index = 0; index < length; index++) {
                types[index] = getReference(objects[index].getClass());
            }
            return types;
        }

        public static boolean compare(Class<?>[] primary, Class<?>[] secondary) {
            if (primary == null || secondary == null || primary.length != secondary.length) {
                return false;
            }
            for (int index = 0; index < primary.length; index++) {
                Class<?> primaryClass = primary[index];
                Class<?> secondaryClass = secondary[index];
                if (!primaryClass.equals(secondaryClass) && !primaryClass.isAssignableFrom(secondaryClass)) {
                    return false;
                }
            }
            return true;
        }
    }

    // Enum pour les types de packages
    public enum PackageType {
        MINECRAFT_SERVER("net.minecraft.server." + getServerVersion()),
        CRAFTBUKKIT("org.bukkit.craftbukkit." + getServerVersion()),
        CRAFTBUKKIT_BLOCK(CRAFTBUKKIT, "block"),
        CRAFTBUKKIT_CHUNKIO(CRAFTBUKKIT, "chunkio"),
        CRAFTBUKKIT_COMMAND(CRAFTBUKKIT, "command"),
        CRAFTBUKKIT_CONVERSATIONS(CRAFTBUKKIT, "conversations"),
        CRAFTBUKKIT_ENCHANTMENTS(CRAFTBUKKIT, "enchantments"),
        CRAFTBUKKIT_ENTITY(CRAFTBUKKIT, "entity"),
        CRAFTBUKKIT_EVENT(CRAFTBUKKIT, "event"),
        CRAFTBUKKIT_GENERATOR(CRAFTBUKKIT, "generator"),
        CRAFTBUKKIT_HELP(CRAFTBUKKIT, "help"),
        CRAFTBUKKIT_INVENTORY(CRAFTBUKKIT, "inventory"),
        CRAFTBUKKIT_MAP(CRAFTBUKKIT, "map"),
        CRAFTBUKKIT_METADATA(CRAFTBUKKIT, "metadata"),
        CRAFTBUKKIT_POTION(CRAFTBUKKIT, "potion"),
        CRAFTBUKKIT_PROJECTILES(CRAFTBUKKIT, "projectiles"),
        CRAFTBUKKIT_SCHEDULER(CRAFTBUKKIT, "scheduler"),
        CRAFTBUKKIT_SCOREBOARD(CRAFTBUKKIT, "scoreboard"),
        CRAFTBUKKIT_UPDATER(CRAFTBUKKIT, "updater"),
        CRAFTBUKKIT_UTIL(CRAFTBUKKIT, "util");

        private final String path;

        PackageType(String path) {
            this.path = path;
        }

        PackageType(PackageType parent, String suffix) {
            this.path = parent.path + "." + suffix;
        }

        public String getPath() {
            return path;
        }

        public Class<?> getClass(String className) throws ClassNotFoundException {
            return Class.forName(this + "." + className);
        }

        @Override
        public String toString() {
            return path;
        }

        private static String getServerVersion() {
            return org.bukkit.Bukkit.getServer().getClass().getPackage().getName().substring(23);
        }
    }
}