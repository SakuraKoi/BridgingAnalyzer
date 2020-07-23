package sakura.kooi.BridgingAnalyzer.utils;

import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {
    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveTypes))
                return constructor;
        }
        throw new NoSuchMethodException(
                "There is no such constructor in this class with the specified parameter types");
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes)
            throws NoSuchMethodException {
        Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
        for (Method method : clazz.getMethods()) {
            if (method.getName().equals(methodName)
                    && DataType.compare(DataType.getPrimitive(method.getParameterTypes()), primitiveTypes))
                return method;
        }
        throw new NoSuchMethodException(
                "There is no such method in this class with the specified name and parameter types");
    }

    public static Method getMethod(String className, PackageType packageType, String methodName,
                                   Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
        return getMethod(packageType.getClass(className), methodName, parameterTypes);
    }

    public static Field getField(Class<?> clazz, boolean declared, String fieldName)
            throws NoSuchFieldException, SecurityException {
        Field field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static Field getField(String className, PackageType packageType, boolean declared, String fieldName)
            throws NoSuchFieldException, SecurityException, ClassNotFoundException {
        return getField(packageType.getClass(className), declared, fieldName);
    }

    public static void setValue(Object instance, Class<?> clazz, boolean declared, String fieldName, Object value)
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        getField(clazz, declared, fieldName).set(instance, value);
    }

    public static void setValue(Object instance, String className, PackageType packageType, boolean declared,
                                String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException,
            NoSuchFieldException, SecurityException, ClassNotFoundException {
        setValue(instance, packageType.getClass(className), declared, fieldName, value);
    }

    public static void setValue(Object instance, boolean declared, String fieldName, Object value)
            throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        setValue(instance, instance.getClass(), declared, fieldName, value);
    }

    public enum PackageType {
        MINECRAFT_SERVER("net.minecraft.server." + getServerVersion()), CRAFTBUKKIT("org.bukkit.craftbukkit."
                + getServerVersion()), CRAFTBUKKIT_BLOCK(CRAFTBUKKIT, "block"), CRAFTBUKKIT_CHUNKIO(CRAFTBUKKIT,
                "chunkio"), CRAFTBUKKIT_COMMAND(CRAFTBUKKIT, "command"), CRAFTBUKKIT_CONVERSATIONS(CRAFTBUKKIT,
                "conversations"), CRAFTBUKKIT_ENCHANTMENS(CRAFTBUKKIT,
                "enchantments"), CRAFTBUKKIT_ENTITY(CRAFTBUKKIT, "entity"), CRAFTBUKKIT_EVENT(
                CRAFTBUKKIT, "event"), CRAFTBUKKIT_GENERATOR(CRAFTBUKKIT,
                "generator"), CRAFTBUKKIT_HELP(CRAFTBUKKIT,
                "help"), CRAFTBUKKIT_INVENTORY(CRAFTBUKKIT,
                "inventory"), CRAFTBUKKIT_MAP(CRAFTBUKKIT,
                "map"), CRAFTBUKKIT_METADATA(
                CRAFTBUKKIT,
                "metadata"), CRAFTBUKKIT_POTION(
                CRAFTBUKKIT,
                "potion"), CRAFTBUKKIT_PROJECTILES(
                CRAFTBUKKIT,
                "projectiles"), CRAFTBUKKIT_SCHEDULER(
                CRAFTBUKKIT,
                "scheduler"), CRAFTBUKKIT_SCOREBOARD(
                CRAFTBUKKIT,
                "scoreboard"), CRAFTBUKKIT_UPDATER(
                CRAFTBUKKIT,
                "updater"), CRAFTBUKKIT_UTIL(
                CRAFTBUKKIT,
                "util");

        private String path;

        PackageType(String path) {
            this.path = path;
        }

        PackageType(PackageType parent, String path) {
            this(parent + "." + path);
        }

        public static String getServerVersion() {
            return Bukkit.getServer().getClass().getPackage().getName().substring(23);
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
    }

    public enum DataType {
        BYTE(Byte.TYPE, Byte.class), SHORT(Short.TYPE, Short.class), INTEGER(Integer.TYPE, Integer.class), LONG(
                Long.TYPE, Long.class), CHARACTER(Character.TYPE, Character.class), FLOAT(Float.TYPE,
                Float.class), DOUBLE(Double.TYPE, Double.class), BOOLEAN(Boolean.TYPE, Boolean.class);

        private static Map<Class<?>, DataType> CLASS_MAP;

        static {
            CLASS_MAP = new HashMap<>();
            for (DataType type : values()) {
                CLASS_MAP.put(type.primitive, type);
                CLASS_MAP.put(type.reference, type);
            }
        }

        private Class<?> primitive;
        private Class<?> reference;

        DataType(Class<?> primitive, Class<?> reference) {
            this.primitive = primitive;
            this.reference = reference;
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
            if (primary == null || secondary == null || primary.length != secondary.length)
                return false;
            for (int index = 0; index < primary.length; index++) {
                Class<?> primaryClass = primary[index];
                Class<?> secondaryClass = secondary[index];
                if (!primaryClass.equals(secondaryClass) && !primaryClass.isAssignableFrom(secondaryClass))
                    return false;
            }
            return true;
        }

        public Class<?> getPrimitive() {
            return primitive;
        }

        public Class<?> getReference() {
            return reference;
        }
    }
}
