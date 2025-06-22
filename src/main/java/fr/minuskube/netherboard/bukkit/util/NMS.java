package fr.minuskube.netherboard.bukkit.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NMS {
    private static final Logger LOGGER = LoggerFactory.getLogger(NMS.class);

    private static final String OLD_PACKAGE_NAME;

    private static final Version VERSION;

    public static final Field PLAYER_SCORES;

    public static final Constructor<?> PACKET_SCORE_REMOVE;

    public static final Constructor<?> PACKET_SCORE;

    public static final Object ENUM_SCORE_ACTION_CHANGE;

    public static final Object ENUM_SCORE_ACTION_REMOVE;

    public static final Constructor<?> SB_SCORE;

    public static final Method SB_SCORE_SET;

    public static final Constructor<?> PACKET_OBJ;

    public static final Constructor<?> PACKET_DISPLAY;

    public static final Field PLAYER_CONNECTION;

    public static final Method SEND_PACKET;

    private static final Map<Class<?>, Method> HANDLES;

    static {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String ver = name.substring(name.lastIndexOf('.') + 1);
        VERSION = new Version(ver);
        OLD_PACKAGE_NAME = "net.minecraft.server." + ver;
        Field playerScores = null;
        Constructor<?> packetScoreRemove = null;
        Constructor<?> packetScore = null;
        Object enumScoreActionChange = null;
        Object enumScoreActionRemove = null;
        Constructor<?> sbScore = null;
        Method sbScoreSet = null;
        Constructor<?> packetObj = null;
        Constructor<?> packetDisplay = null;
        Field playerConnection = null;
        Method sendPacket = null;
        try {
            Class<?> scoreActionClass, packetScoreClass = getClass("net.minecraft.network.protocol.game", "PacketPlayOutScoreboardScore");
            Class<?> packetDisplayClass = getClass("net.minecraft.network.protocol.game", "PacketPlayOutScoreboardDisplayObjective");
            Class<?> packetObjClass = getClass("net.minecraft.network.protocol.game", "PacketPlayOutScoreboardObjective");
            Class<?> scoreClass = getClass("net.minecraft.world.scores", "ScoreboardScore");
            Class<?> sbClass = getClass("net.minecraft.world.scores", "Scoreboard");
            Class<?> objClass = getClass("net.minecraft.world.scores", "ScoreboardObjective");
            Class<?> playerClass = getClass("net.minecraft.server.level", "EntityPlayer");
            Class<?> playerConnectionClass = getClass("net.minecraft.server.network", "PlayerConnection");
            Class<?> packetClass = getClass("net.minecraft.network.protocol", "Packet");
            sbScore = scoreClass.getConstructor(new Class[] { sbClass, objClass, String.class });
            sbScoreSet = scoreClass.getMethod("setScore", new Class[] { int.class });
            packetObj = packetObjClass.getConstructor(new Class[] { objClass, int.class });
            packetDisplay = packetDisplayClass.getConstructor(new Class[] { int.class, objClass });
            sendPacket = playerConnectionClass.getMethod("sendPacket", new Class[] { packetClass });
            if (VERSION.isBelow1_17()) {
                playerScores = sbClass.getDeclaredField("playerScores");
                playerScores.setAccessible(true);
                playerConnection = playerClass.getField("playerConnection");
            } else {
                playerScores = sbClass.getDeclaredField("j");
                playerScores.setAccessible(true);
                playerConnection = playerClass.getField("b");
            }
            switch (VERSION.getMajor()) {
                case "1.7":
                    packetScore = packetScoreClass.getConstructor(new Class[] { scoreClass, int.class });
                    break;
                case "1.8":
                case "1.9":
                case "1.10":
                case "1.11":
                case "1.12":
                    packetScore = packetScoreClass.getConstructor(new Class[] { scoreClass });
                    packetScoreRemove = packetScoreClass.getConstructor(new Class[] { String.class, objClass });
                    break;
                default:
                    scoreActionClass = getClass("net.minecraft.server", "ScoreboardServer$Action");
                    packetScore = packetScoreClass.getConstructor(new Class[] { scoreActionClass, String.class, String.class, int.class });
                    enumScoreActionChange = scoreActionClass.getEnumConstants()[0];
                    enumScoreActionRemove = scoreActionClass.getEnumConstants()[1];
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("Error while loading NMS methods. (Unsupported Minecraft version?)", e);
        }
        PLAYER_SCORES = playerScores;
        PACKET_SCORE_REMOVE = packetScoreRemove;
        PACKET_SCORE = packetScore;
        ENUM_SCORE_ACTION_CHANGE = enumScoreActionChange;
        ENUM_SCORE_ACTION_REMOVE = enumScoreActionRemove;
        SB_SCORE = sbScore;
        SB_SCORE_SET = sbScoreSet;
        PACKET_OBJ = packetObj;
        PACKET_DISPLAY = packetDisplay;
        PLAYER_CONNECTION = playerConnection;
        SEND_PACKET = sendPacket;
        HANDLES = new HashMap<>();
    }

    public static Version getVersion() {
        return VERSION;
    }

    public static Class<?> getClass(String realPackageName, String name) throws ClassNotFoundException {
        String packageName = VERSION.isBelow1_17() ? OLD_PACKAGE_NAME : realPackageName;
        return Class.forName(packageName + "." + name);
    }

    public static Object getHandle(Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = obj.getClass();
        if (!HANDLES.containsKey(clazz)) {
            Method method = clazz.getDeclaredMethod("getHandle", new Class[0]);
            if (!method.isAccessible())
                method.setAccessible(true);
            HANDLES.put(clazz, method);
        }
        return ((Method)HANDLES.get(clazz)).invoke(obj, new Object[0]);
    }

    public static void sendPacket(Object packet, Player... players) {
        for (Player p : players) {
            try {
                Object playerConnection = PLAYER_CONNECTION.get(getHandle(p));
                SEND_PACKET.invoke(playerConnection, new Object[] { packet });
            } catch (IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
                LOGGER.error("Error while sending packet to player. (Unsupported Minecraft version?)", e);
            }
        }
    }

    public static class Version {
        private final String name;

        private final String major;

        private final String minor;

        Version(String name) {
            this.name = name;
            String[] splitName = name.split("_");
            this.major = splitName[0].substring(1) + "." + splitName[1];
            this.minor = splitName[2].substring(1);
        }

        public String getName() {
            return this.name;
        }

        public String getMajor() {
            return this.major;
        }

        public String getMinor() {
            return this.minor;
        }

        public boolean isBelow1_17() {
            return (this.major.equals("1.7") || this.major
                    .equals("1.8") || this.major
                    .equals("1.9") || this.major
                    .equals("1.10") || this.major
                    .equals("1.11") || this.major
                    .equals("1.12") || this.major
                    .equals("1.13") || this.major
                    .equals("1.14") || this.major
                    .equals("1.15") || this.major
                    .equals("1.16"));
        }
    }
}
