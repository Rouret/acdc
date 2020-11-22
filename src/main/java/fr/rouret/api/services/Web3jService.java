package fr.rouret.api.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import static org.joor.Reflect.*;
import org.hyperledger.besu.ethereum.vm.OperationTracer;
import org.joor.ReflectException;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.evm.Configuration;
import org.web3j.evm.PassthroughTracer;
import fr.rouret.generator.ArrayGenerator;
import org.web3j.evm.EmbeddedWeb3jService;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.tx.gas.DefaultGasProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fr.rouret.api.Application;
import fr.rouret.api.services.FileService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Web3jService {
    
    public Web3jService() {
        
    }
    public void process(String scriptName, String[] params) {
        try {
            FileService fileService=new FileService();
            Class c = fileService.getClassOfScript(scriptName);
            File scripFile =Application.getInstance().getScriptHandler().getScript(scriptName.toLowerCase()+".abi").getFile();
            AbiDefinition[] inputs=this.getInput(scripFile);
            for (int i = 1; i < 10; i++) {
                BigInteger gasUsed = onClass(c.getName())
                        .call("deploy", Application.getInstance().getWeb3J(), Application.getInstance().getCredentials(), new DefaultGasProvider()).call("send")
                        .call("sort", ArrayGenerator.generateNumericalArray(200, i)).call("send").call("getGasUsed")
                        .get();
                System.out.println("gasUsed:" + gasUsed);
            }
            
        } catch (ReflectException e) {  
            System.err.println("Script inconnu");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private AbiDefinition[] getInput(File scriptFile) throws Exception {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        if(scriptFile==null) throw new Exception("null script file");
        AbiDefinition[] abiDefinition = objectMapper.readValue(scriptFile, AbiDefinition[].class);
        
        for(AbiDefinition abi : abiDefinition) {
            System.out.println(abi.getName());
        }
        return abiDefinition;
    }
   
}
