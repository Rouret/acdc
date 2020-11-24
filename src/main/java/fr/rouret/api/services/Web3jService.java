package fr.rouret.api.services;

import java.math.BigInteger;

import static org.joor.Reflect.*;
import org.joor.ReflectException;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.protocol.core.methods.response.AbiDefinition.NamedType;
import org.web3j.tx.gas.DefaultGasProvider;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import fr.rouret.KeyValue;
import fr.rouret.api.Application;
import fr.rouret.generators.Generator;
import fr.rouret.generators.generators.*;
import spark.QueryParamsMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class Web3jService {


    public Web3jService() {
        //inutile
    }

    public Generator getContractInfo(String scriptName){
        try {
            System.out.println("===========");
            FileService fileService = new FileService();
            // get the class of the script name
            Class classOfScript = fileService.getClassOfScript(scriptName);
            // Find abi (json file)
            File scripFile = Application.getInstance().getScriptHandler().getScript(scriptName.toLowerCase() + ".abi")
                    .getFile();
            // get inputs
            List<NamedType> inputs = this.getInput(scripFile);
            for (NamedType ty : inputs) {
                System.out.println(inputs.get(0).getType());
                String generatorName=this.getGeneratorNameFromInputType(ty);

                Generator<?> generator = Application.getInstance().getGeneratorHandler().getGenerator(generatorName);

                return generator;
            }
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        return null; //^^
    }
    public void process(String scriptName, QueryParamsMap args) {
        try {
            
            System.out.println("===========");
            FileService fileService = new FileService();
            // get the class of the script name
            Class classOfScript = fileService.getClassOfScript(scriptName);
            // Find abi (json file)
            File scripFile = Application.getInstance().getScriptHandler().getScript(scriptName.toLowerCase() + ".abi")
                    .getFile();
            // get inputs
            List<NamedType> inputs = this.getInput(scripFile);
            System.out.println("Nombre de parametre du scripts demandé pas le script solidity: "+inputs.size());
            
            for (NamedType ty : inputs) {
                System.out.println(inputs.get(0).getType());
                String generatorName=this.getGeneratorNameFromInputType(ty);

                Generator<?> generator = Application.getInstance().getGeneratorHandler().getGenerator(generatorName);

                String[] generatorInput = this.getGenerateurInput(generator,args);

                Object generatorOutput = generator.generate(generatorInput);

                System.out.println("Type of generator result: "+ generatorOutput.getClass().getName());
                System.out.println("Generator Result: "+generatorOutput);

                System.out.println("===========");
                System.out.println("PROCESS...");

                

                BigInteger gasUsed = onClass(classOfScript.getName())
                        .call("deploy", Application.getInstance().getWeb3J(),
                                Application.getInstance().getCredentials(), new DefaultGasProvider())
                        .call("send")
                        .call(Application.DEFAULT_METHOD_START, generatorOutput)
                        .call("send").call("getGasUsed").get();
                System.out.println("gasUsed:" + gasUsed);

                System.out.println("===========");
            }
        } catch (ReflectException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Aucune method run trouvé");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<NamedType> getInput(File scriptFile) throws Exception {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        if(scriptFile==null) throw new Exception("null script file");
        AbiDefinition[] abiDefinition = objectMapper.readValue(scriptFile, AbiDefinition[].class);
        //le findFirst renvoie un NullPointerException si le filter n'a rien trouvé, c'est comme ça qu'on peut vérifier qu'il il y a une méthod
        Optional<AbiDefinition> runMethod =Arrays.stream(abiDefinition).filter(abi->abi.getName().equalsIgnoreCase(Application.DEFAULT_METHOD_START)).findFirst();
        //Double check
        if(!runMethod.isPresent()) throw new NullPointerException("no run  method find");
        return runMethod.get().getInputs();
    }
    private String getGeneratorNameFromInputType(NamedType typeName) throws Exception{
        // remove number
        String type = typeName.getType().replaceAll("\\d", "");
        String generatorName = Application.getInstance().getConfSolidityType().get(type);
        System.out.println("Generateur en question :"+generatorName);
        // throw error if type not find
        if (generatorName == null)
            throw new Exception("Type '" + type + "' unknow");
        // the return is like class.method
        return generatorName;
    }


    private String[] getGenerateurInput(Generator generator,QueryParamsMap args) throws Exception{
        KeyValue<String,Class>[] genArgs = generator.getArgs();
                
        Object generatorOutput = null;
        String[] generatorInput = new String[genArgs.length];

        for (int i = 0; i < genArgs.length; i++) {
            KeyValue<String,Class> arg = genArgs[i];
            String key = arg.getKey();
            if (!args.hasKey(key) )
                throw new Exception("Missing argument in QueryParamArgs for key : " + key);

            generatorInput[i] = args.get(key).value();
        }
        return generatorInput;
    }
}
