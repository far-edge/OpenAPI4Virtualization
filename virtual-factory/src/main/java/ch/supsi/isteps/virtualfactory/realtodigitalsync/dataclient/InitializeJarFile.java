package ch.supsi.isteps.virtualfactory.realtodigitalsync.dataclient;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import ch.supsi.isteps.prototype.tools.AbstractComponentFactory;

public class InitializeJarFile {

	public static AbstractComponentFactory createFactory(String resourcePath) {
		try {
			File resourceFile = null;
			if (resourcePath == null || resourcePath.isEmpty()) return new NoComponentFactory();
			if(resourcePath.startsWith("file://")){
				resourceFile = new File(resourcePath.replace("file:/", ""));
			}else {
				resourceFile = retrieveFile(resourcePath);	
			}
			JarFile jarFile = new JarFile(resourceFile);
			Enumeration<JarEntry> entries = jarFile.entries();
			URL[] urls = { new URL("jar:file:" + resourcePath + "!/") };
			URLClassLoader classLoader = URLClassLoader.newInstance(urls, Thread.currentThread().getContextClassLoader());
			while (entries.hasMoreElements()) {
				JarEntry jarEntry = entries.nextElement();
				if (jarEntry.isDirectory() || !jarEntry.getName().endsWith(".class")) {
					continue;
				}
				String className = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
				className = className.replace('/', '.');
				if (className.contains("PlatformComponentFactory")) {
					Class c = classLoader.loadClass(className);
					jarFile.close();
					return (AbstractComponentFactory) c.newInstance();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static File retrieveFile(String resourcePath) {
		try {
			URL website = new URL(resourcePath);
			InputStream in = website.openStream();
			Path createTempFile = Files.createTempFile("", "");
			Files.copy(in, createTempFile, StandardCopyOption.REPLACE_EXISTING);
			return createTempFile.toFile();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}
}