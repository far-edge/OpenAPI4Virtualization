package ch.supsi.isteps.virtualfactory.realtodigitalsync.spike;

public class SpikeClassLoading {

//	public static void main(String[] args) throws Exception {
//		String pathToJar = "/home/gabriele/Desktop/component.jar";
//		JarFile jarFile = new JarFile(pathToJar);
//		Enumeration<JarEntry> e = jarFile.entries();
//		URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
//		URLClassLoader cl = URLClassLoader.newInstance(urls);
//
//		while (e.hasMoreElements()) {
//		    JarEntry je = e.nextElement();
//		    if(je.isDirectory() || !je.getName().endsWith(".class")){
//		        continue;
//		    }
//		    // -6 because of .class
//		    String className = je.getName().substring(0,je.getName().length()-6);
//		    className = className.replace('/', '.');
//		    if(className.contains("PlatformComponentFactory")) {
//		    	Class c = cl.loadClass(className);
//		    	AbstractComponentFactory prova = (AbstractComponentFactory)c.newInstance();
//		    	System.out.println(prova.create());
//		    }
//		}
//	}
}
