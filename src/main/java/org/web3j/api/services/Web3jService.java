package org.web3j.api.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import static org.joor.Reflect.*;
import org.hyperledger.besu.ethereum.vm.OperationTracer;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.evm.Configuration;
import org.web3j.evm.PassthroughTracer;
import org.web3j.generator.ArrayGenerator;
import org.web3j.evm.EmbeddedWeb3jService;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.regreeter.Regreeter;
import org.web3j.api.services.Web3jService;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
public class Web3jService {

    private Credentials credentials;
    private Configuration configuration;
    private OperationTracer operationTracer;
    private Web3j web3j;

    public Web3jService() {
        try {
            // credentials doit etre définit en 1er car configuration utilise credentials
            this.credentials = this.loadCredential("Password123", "resources/demo-wallet.json");
            this.configuration = this.loadConfiguration();
            this.operationTracer = this.loadOperationTracer();
            this.web3j = Web3j.build(new EmbeddedWeb3jService(this.configuration, this.operationTracer));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public void process(String scriptName, String[] params){
        try {
            Class[] classes =Web3jService.getClasses("org.web3j");
            Object[] filteredClass=Arrays.stream(classes).filter(className-> className.getSimpleName().equalsIgnoreCase(scriptName)).toArray();
            if(filteredClass.length==1){
                Class c= (Class) filteredClass[0];
                System.out.println("Name class:"+c.getName());
                for (int i = 1; i < 5; i++) {
                    BigInteger gasUsed = onClass(c)
                    .call("deploy", this.web3j, this.credentials, new DefaultGasProvider()) 
                    .call("send") 
                    .call("sort",ArrayGenerator.generateNumericalArray(200, i))  
                    .call("send")    
                    .call("getGasUsed")   
                    .get();
                    System.out.println("gasUsed:"+gasUsed);
                }
               
                
            }else{
                System.err.println("Not find");
            }
        } catch (Exception e) {
            System.err.println(e);
        }
       
        
    }
    private OperationTracer loadOperationTracer(){
        return new PassthroughTracer();
    }

    private Configuration loadConfiguration(){
        return new Configuration(new Address(this.credentials.getAddress()), 10000);
    }

    private Credentials loadCredential(String s, String ressourceName) throws IOException, CipherException {
        return WalletUtils.loadCredentials("Password123", "resources/demo-wallet.json");
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
