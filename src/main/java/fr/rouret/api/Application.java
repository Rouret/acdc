package fr.rouret.api;

import static spark.Spark.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

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
import fr.rouret.scripts.ScriptHandler;

public class Application {
    private static Application instance;
    private static Web3j web3j;
    private static boolean isRunning=false;
    private Credentials credentials;
    private Configuration configuration;
    private OperationTracer operationTracer;
    private ScriptHandler scriptHandler;
    //Constantes
    private final static String SOLIDITY_FOLDER_NAME = "solidity";
    private final static int PORT = 8080;
    private final static String RESPONSE_TYPE="application/json";

    public Application() {
        this.instance = this;
        // SETUP PORT
        System.out.println("Setup API ...");
        port(Application.PORT);
        
        // SETUP API
        before((request, response) -> {
            response.type(Application.RESPONSE_TYPE);
        });
        System.out.println("ok");
        // INIT
        try {
            System.out.println("Setup Web3j ...");
            this.credentials=this.loadCredential("Password123", "resources/demo-wallet.json");
            this.configuration= this.loadConfiguration();
            this.operationTracer= this.loadOperationTracer();
            this.web3j = Web3j.build(new EmbeddedWeb3jService(this.configuration, this.operationTracer));
            System.out.println("ok");
            System.out.println("Load scripts ...");
            URL scriptsURL = this.getClass().getClassLoader().getResource(Application.SOLIDITY_FOLDER_NAME);
            if (scriptsURL == null)
                throw new IllegalArgumentException("file not found!");

            scriptHandler = new ScriptHandler(new File(scriptsURL.toURI()));
            System.out.println("OK");
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
        return this.web3j;
    }
    public ScriptHandler getScriptHandler(){
        return this.scriptHandler;
    }
    public Credentials getCredentials(){
        return this.credentials;
    }
    private OperationTracer loadOperationTracer(){
        return new PassthroughTracer();
    }
    private Configuration loadConfiguration(){
        return new Configuration(new Address(this.getCredentials().getAddress()), 10000);
    }

    private Credentials loadCredential(String s, String ressourceName) throws IOException, CipherException {
        return WalletUtils.loadCredentials("Password123", "resources/demo-wallet.json");
    }
    @Override
    public String toString() {
        return "* Application *\n" + "Application" + (Application.isRunning ? "is" : "is not") + "running\n"+"Port:"+Application.PORT+"\nResponse Type:"+Application.RESPONSE_TYPE;
    }
}