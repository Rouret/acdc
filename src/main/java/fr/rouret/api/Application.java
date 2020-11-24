package fr.rouret.api;

import static spark.Spark.*;


import java.io.File;
import java.io.IOException;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;

import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.evm.Configuration;
import org.web3j.evm.EmbeddedWeb3jService;
import org.web3j.evm.PassthroughTracer;
import org.web3j.protocol.Web3j;
import org.hyperledger.besu.ethereum.vm.OperationTracer;
import fr.rouret.api.routes.Routes;
import fr.rouret.generators.GeneratorHandler;
import fr.rouret.scripts.ScriptHandler;

public class Application {
    private static Application instance;
    private static Web3j web3j;
    private static boolean isRunning=false;
    private static HashMap<String, String> confSolidityType;
    private Credentials credentials;
    private Configuration configuration;
    private OperationTracer operationTracer;
    private ScriptHandler scriptHandler;
    private GeneratorHandler generatorHandler;
    //Constantes
    public final static String SOLIDITY_FOLDER_NAME = "solidity";
    public final static int PORT = 8080;
    public final static String RESPONSE_TYPE="application/json";
    public final static String DEFAULT_METHOD_START="run";

    public Application() {
        this.instance = this;
        // SETUP PORT
        System.out.println("Setup API ...");
        port(Application.PORT);
        
        // SETUP API
        before((request, response) -> {
            response.type(Application.RESPONSE_TYPE);
            if(!this.isRunning) halt(401, "The api is starting ...");
            
        });
        System.out.println("ok");
        // INIT
        try {
            //solidity type
            System.out.println(">Setup Solidity type ...");
            Application.confSolidityType = this.loadConfType();
            System.out.println("OK");

            //WEB3J
            System.out.println(">Setup Web3j ...");
            this.credentials=this.loadCredential("Password123", "resources/demo-wallet.json");
            this.configuration= this.loadConfiguration();
            this.operationTracer= this.loadOperationTracer();
            Application.web3j = Web3j.build(new EmbeddedWeb3jService(this.configuration, this.operationTracer));

            System.out.println("OK");

            //TEST pour un fichier conf
            // System.out.println("TEST");
            // ArrayList<String> data = new ArrayList<String>();
            // InputStream confStream = this.getClass().getClassLoader().getResourceAsStream("conf/solidity_type.conf");
            // BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(confStream));
            // bufferedReader.lines().forEach(conf->data.add(conf));
            // System.out.println(data.toString());
            // System.out.println("ok");

            System.out.println(">Load scripts ...");
            
            URL scriptsURL = this.getClass().getClassLoader().getResource(Application.SOLIDITY_FOLDER_NAME);
            if (scriptsURL == null)
                throw new IllegalArgumentException("file not found!");

            scriptHandler = new ScriptHandler(new File(scriptsURL.toURI()));
            System.out.println("OK");

            System.out.println(">Load generators ...");
            generatorHandler = new GeneratorHandler();
            System.out.println("OK");

            System.out.println(">TEST");
            // Generator<?> generator = generatorHandler.getGenerator("ArrayStringGenerator");
            // System.out.println(generator.getName());
            // System.out.println("Args : ");
            // for (KeyValue<String, Class> arg : generator.getArgs())
            //     System.out.println(arg.getKey() + " as " + arg.getValue());
            
            // Object generate = generator.generate(8,50);
            // System.out.println(generate);
            System.out.println(">FIN TEST");

            this.isRunning=true;

            Routes.load();
            System.out.println("===========");
            System.out.println("API is UP ");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
            // TODO: handle exception
        } catch (CipherException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(0);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(0);
        }catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        
    }
    public static Application getInstance() {
        return instance;
    }
    public Web3j getWeb3J() {
        return Application.web3j;
    }
    public GeneratorHandler getGeneratorHandler(){
        return this.generatorHandler;
    }
    public ScriptHandler getScriptHandler(){
        return this.scriptHandler;
    }
    public Credentials getCredentials(){
        return this.credentials;
    }
    public HashMap<String,String> getConfSolidityType(){
        return Application.confSolidityType;
    }
    private OperationTracer loadOperationTracer(){
        return new PassthroughTracer();
    }
    private Configuration loadConfiguration(){
        return new Configuration(new Address(this.getCredentials().getAddress()), 1000000000);
    }

    private Credentials loadCredential(String s, String ressourceName) throws IOException, CipherException {
        return WalletUtils.loadCredentials("Password123", "resources/demo-wallet.json");
    }
    private HashMap<String, String> loadConfType(){
        HashMap<String, String> confSolidityType = new HashMap<String, String>();
        //normal
        confSolidityType.put("int", "IntegerGenerator"); //negat et positif
        confSolidityType.put("uint", "NumericalGenerator"); //positif
        // confSolidityType.put("bool", ""); 
        confSolidityType.put("fixed", "NumericalDoubleGenerator");  //float positif
        confSolidityType.put("ufixed", "DoubleGenerator"); //float positif et nagtif
        confSolidityType.put("string","StringGenerator");
        //list
        confSolidityType.put("uint[]", "ListNumericalGenerator");
        confSolidityType.put("int[]", "ListIntegerGenerator");
        // confSolidityType.put("bool[]", ""); 
        confSolidityType.put("fixed[]", "ListNumericalDoubleGenerator");  //float positif
        confSolidityType.put("ufixed[]", "ListDoubleGenerator"); //float positif et negatif
        confSolidityType.put("string[]","ListStringGenerator");
        return confSolidityType;
    }
    @Override
    public String toString() {
        return "* Application *\n" + "Application" + (Application.isRunning ? "is" : "is not") + "running\n"+"Port:"+Application.PORT+"\nResponse Type:"+Application.RESPONSE_TYPE;
    }
}