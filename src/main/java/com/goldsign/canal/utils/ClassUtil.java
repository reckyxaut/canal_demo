package com.goldsign.canal.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {
	private static final  String  	CLASS_FILE_SUFFIX = ".class";
	private static final  String  	FILE_JAR = "jar";
	private static ClassLoader   	my_classLoader = ClassUtil.class.getClassLoader();//默认使用的类加载器
	/**
	* @Description: 根据一个接口返回该接口的所有类
	* @param c 接口
	* @return List<Class>    实现接口的所有类
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Class> getAllClassByInterface(String packageName , Class c){
		List returnClassList = new ArrayList<Class>();
		//判断是不是接口,不是接口不作处理
		if(c.isInterface()){
//			String packageName = c.getPackage().getName();	//获得当前包名
			try {
				List<Class> allClass = getClasses(packageName);//获得当前包以及子包下的所有类
				
				//判断是否是一个接口
				for(int i = 0; i < allClass.size(); i++){
					if(c.isAssignableFrom(allClass.get(i))){
						if(!c.equals(allClass.get(i))){
							returnClassList.add(allClass.get(i));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return returnClassList;
	}
	
	/**
	 * 
	* @Description: 根据包名获得该包以及子包下的所有类不查找jar包中的
	* @param pageName 包名
	* @return List<Class>    包下所有类
	 */
	@SuppressWarnings("rawtypes")
	public static List<Class> getClasses(String packageName) throws ClassNotFoundException,IOException{
		List<Class>  classes = new ArrayList<>();
		Enumeration<URL> dirs;
		try {
			 String packageDirName = packageName.replace('.', '/');
			 dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			 System.out.println(packageName + ">>" + packageDirName);
			 while (dirs.hasMoreElements()){
				 URL url = dirs.nextElement();
				 String protocol = url.getProtocol();
				 if ("file".equals(protocol)) {
					 String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					 File basePackageFile = new File(filePath);
					 File[] childFiles = basePackageFile.listFiles();
					 for(File file:childFiles ){
						if(file.isDirectory()){
							classes.addAll(getClasses(packageName + "." + file.getName()));
						}else if(file.isFile() && file.getName().endsWith(CLASS_FILE_SUFFIX)){
							classes.add(Class.forName(packageName + "." + file.getName().split("\\.")[0]));
						}
					 }
				 }else if (FILE_JAR.equalsIgnoreCase(protocol)) {
					 	JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
					 	classes.addAll(getClassesFromJar2(jarFile,packageDirName));
					 	break;
			 }
		}}  catch (IOException e) {
			 e.printStackTrace();
			 throw new RuntimeException("class loader error.");
		}
		return classes;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<Class> getClassesFromJar2(JarFile jarFile , String path ) throws ClassNotFoundException, IOException{
		List<Class>  classes = new ArrayList<>();
		Enumeration<JarEntry> jarEntries = jarFile.entries();  
        while (jarEntries.hasMoreElements()) {  
            JarEntry jarEntry = jarEntries.nextElement();  
            String jarEntryName = jarEntry.getName();  
            if(jarEntryName.contains(path) && !jarEntryName.equals(path + "/" )){
	        	if(jarEntryName.endsWith(CLASS_FILE_SUFFIX)){  
	                 Class<?> clazz = null;  
	                 try {  
	                     clazz = my_classLoader.loadClass(jarEntry.getName().replace("/" , ".").replace(CLASS_FILE_SUFFIX , ""));  
	                     classes.add((Class<?>) clazz);  
	                 } catch (ClassNotFoundException e) {  
	                     e.printStackTrace();  
	                 }  
	           }else if(jarEntry.isDirectory()){
                	 getClassesFromJar2(jarFile,jarEntryName.substring(0, jarEntryName.lastIndexOf('/')));   
               }  
            }  
        } 
		return classes;
	}
	
	
	/**
	 * 从jar包扫描
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	@SuppressWarnings("rawtypes")
	public static List<Class> getClassesFromJar(String packName ) throws ClassNotFoundException, IOException{
		System.out.println("=================");
		System.out.println(packName);
		List<Class>  classes = new ArrayList<>();
//		String pathName = packName.replace(".", File.separator);  
		String pathName = packName.replace('.', '/'); 
        JarFile jarFile  = null;  
        try {  
            URL url = my_classLoader.getResource(pathName);  
            JarURLConnection jarURLConnection  = (JarURLConnection )url.openConnection();  
            jarFile = jarURLConnection.getJarFile();    
        } catch (IOException e) {  
        	e.printStackTrace();
            throw new RuntimeException("未找到策略资源");  
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new RuntimeException("策略资源加载失败");  
		} 
          
        Enumeration<JarEntry> jarEntries = jarFile.entries();  
        while (jarEntries.hasMoreElements()) {  
            JarEntry jarEntry = jarEntries.nextElement();  
            String jarEntryName = jarEntry.getName();  
              
            if(jarEntryName.contains(pathName) && !jarEntryName.equals(pathName + File.separator )){  
                //递归遍历子目录  
                if(jarEntry.isDirectory()){  
                    String clazzName = jarEntry.getName().replace(File.separator , ".");  
                    int endIndex = clazzName.lastIndexOf(".");   
                    String prefix = null;    
                    if (endIndex > 0) prefix = clazzName.substring(0, endIndex);    
                    getClassesFromJar(prefix);  
                }  
                
                if(jarEntry.getName().endsWith(CLASS_FILE_SUFFIX)){  
                    Class<?> clazz = null;  
                    try {  
                        clazz = my_classLoader.loadClass(jarEntry.getName().replace(File.separator , ".").replace(CLASS_FILE_SUFFIX , ""));  
                        classes.add((Class<?>) clazz);  
                    } catch (ClassNotFoundException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }  
        return classes;
    }  
	


	@SuppressWarnings({ "rawtypes", "unused" })
	private static  List<Class> findClass(File directory, String packageName) 
		throws ClassNotFoundException{
		List<Class> classes = new ArrayList<Class>();
		if(!directory.exists()){
			return classes;
		}
		File[] files = directory.listFiles();
		for(File file:files){
			if(file.isDirectory()){
				assert !file.getName().contains(".");
				classes.addAll(findClass(file, packageName+"."+file.getName()));
			}else if(file.getName().endsWith(".class")){
				classes.add(Class.forName(packageName + "." + file.getName().split("\\.")[0])); 
			}
		}
		return classes;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Class> getAllClassByAnnotation(String packageName, Class annotationClass){
		List returnClassList = new ArrayList<Class>();
		//判断是不是注解
		if(annotationClass.isAnnotation()){
//			String packageName = annotationClass.getPackage().getName();	//获得当前包名
			try {
				List<Class> allClass = getClasses(packageName);//获得当前包以及子包下的所有类
				
				for(int i = 0; i < allClass.size(); i++){
					if(allClass.get(i).isAnnotationPresent(annotationClass)){
						returnClassList.add(allClass.get(i));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return returnClassList;
	}
	
	public static List<String> packageNames = new ArrayList<>();
	
	/**
	 * 根据包路径扫描
	 * @param basePackage
	 * @throws UnsupportedEncodingException 
	 */
	public static void scanByPackage(String basePackage) throws UnsupportedEncodingException{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource(basePackage.replace(".", File.separator));
		String filePath = java.net.URLDecoder.decode(url.getPath(),"utf-8"); 
		File basePackageFile = new File(filePath);
		
		System.out.println("scan:" + basePackageFile );
		File[] childFiles = basePackageFile.listFiles();
		for(File file:childFiles ){
			if(file.isDirectory()){
				scanByPackage(basePackage + "." + file.getName());
			}else if(file.isFile()){
				packageNames.add(basePackage + "." + file.getName().split("\\.")[0]); 
			} 
		}
	}
	
	/*public static  Class<?> getReqParamClass(String packageName , String type, String code) throws ClassNotFoundException{
		StringBuffer className = new StringBuffer(packageName);
		if(MyConfig.hitype_gl.equals(type)){
			switch (code) {
			case "9001":
				className.append(".").append("ReqUser");
				break;
			case "9002":
				className.append(".").append("ReqRole");
				break;
			case "9003":
				className.append(".").append("ReqMenu");
				break;
			default:
				className.append(".bsi01.").append("Req").append(type).append(code);
				break;
			} 
		}else{
			className.append(".bsi").append(type).append(".Req").append(type).append(code);
		}
		return Class.forName(className.toString());
	}*/
}
