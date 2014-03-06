package org.sothis.core.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtils {
	/**
	 * 获得包下的类
	 * 
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Class<?>[] getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			String p = "";
			if (resource.getFile().indexOf("!") >= 0) {// 在其他的jar文件中
				p = resource.getFile().substring(0, resource.getFile().indexOf("!")).replaceAll("%20", "");
			} else {// 在classes目录中
				p = resource.getFile();
			}
			if (p.startsWith("file:/"))
				p = p.substring(5);
			if (p.toLowerCase().endsWith(".jar")) {
				JarFile jarFile = new JarFile(p);
				Enumeration<JarEntry> enums = jarFile.entries();
				while (enums.hasMoreElements()) {
					JarEntry entry = (JarEntry) enums.nextElement();
					String n = entry.getName();

					if (n.endsWith(".class")) {
						n = n.replaceAll("/", ".").substring(0, n.length() - 6);
						if (n.startsWith(packageName)) {

							classes.add(Class.forName(n));

						}
					}
				}
				jarFile.close();
			} else {
				dirs.add(new File(p));
			}

		}

		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * 查找一个文件夹下的文件
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

}
