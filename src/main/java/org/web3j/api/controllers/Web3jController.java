package org.web3j.api.controllers;

import org.web3j.api.services.Web3jService;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Web3jController extends Controller {
    public static String process(String scriptName, String[] params) {
        System.out.println("Script demandé:"+scriptName);
        try {
            //org.web3j.regreeter

            Class[] classes =Web3jController.getClasses("org.web3j");
            Boolean isFind=false;
            int i =0;
            while(!isFind && i<classes.length){
                final String className=classes[i].getSimpleName();
                if(className.equals(scriptName)){
                    System.out.println("Find");
                    isFind=true;
                }else{
                    i++;
                }
            }
            Web3jService service = new Web3jService();
            service.process(classes[i]);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "oui oui";
    }

    public static Object createObject(String name, String[] arguments) throws NoSuchMethodException, SecurityException,
            ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        // Class<?> clazz;
        // clazz = Class.forName(name);
        // Constructor<?> ctor = clazz.getConstructor(String.class);
        // Object object = ctor.newInstance(new Object[] { arguments });
        Class componentClass = Class.forName(name);
        return componentClass.newInstance();
    }

    private static Class[] getClasses(String packageName)
        throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
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
    
