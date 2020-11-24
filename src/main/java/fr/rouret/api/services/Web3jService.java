package fr.rouret.api.services;

import java.math.BigInteger;

import static org.joor.Reflect.*;
import org.joor.ReflectException;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.protocol.core.methods.response.AbiDefinition;
import org.web3j.protocol.core.methods.response.AbiDefinition.NamedType;
import org.web3j.tx.gas.DefaultGasProvider;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import fr.rouret.KeyValue;
import fr.rouret.api.Application;
import fr.rouret.generators.Generator;
import fr.rouret.generators.generators.*;
import spark.QueryParamsMap;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Web3jService {


    public Web3jService() {
        //inutile
    }

    public Generator getContractInfo(String scriptName){
        try {
            System.out.println("===========");
            // Find abi (json file)
            File scripFile = Application.getInstance().getScriptHandler().getScript(scriptName.toLowerCase() + ".abi")
                    .getFile();
            // get inputs
            List<NamedType> inputs = this.getInput(scripFile);

            if(inputs.size()!=1) throw new Exception("Nombre d'input non prit en compte");

            for (NamedType ty : inputs) { //pas tres utile pour 1 tour

                String generatorName=this.getGeneratorNameFromInputType(ty);

                Generator<?> generator = Application.getInstance().getGeneratorHandler().getGenerator(generatorName);

                return generator;//pas propre, manque de temps
            }
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        return null; //^^
    }
    public HashMap<String, BigInteger> process(String scriptName, QueryParamsMap args) {
        try {
            if(!args.hasKey("selected") || !args.hasKey("numberOfLoop") || !args.hasKey("inc")) {
                throw new Exception("Missisng selected,numberOfLoop and inc");
            }
            System.out.println("===========");
            FileService fileService = new FileService();
            // La classe du script en fonction de son nom
            Class classOfScript = fileService.getClassOfScript(scriptName);
            // Find abi (json file)
            File scripFile = Application.getInstance().getScriptHandler().getScript(scriptName.toLowerCase() + ".abi")
                    .getFile();
            //Inputs de script solidity (qui est en java ici)
            List<NamedType> inputs = this.getInput(scripFile);

            System.out.println("Nombre de parametre du scripts demandé pas le script solidity: "+inputs.size());
            //Par manque de temps il n'y as pas la possibilité d'avoir plusieurs inputs pour un script
            if(inputs.size()!=1) throw new Exception("Nombre d'input non prit en compte");
            //La boucle est inutile mets prete pour ajouter la fonctionnalité du multi-inputs
            for (NamedType ty : inputs) {
                //Le nom du generateur en fonction du type
                String generatorName=this.getGeneratorNameFromInputType(ty);
                //Le generateur 
                Generator<?> generator = Application.getInstance().getGeneratorHandler().getGenerator(generatorName);

                System.out.println("===========");
                System.out.println("PROCESS...");

                //Les filtres
                int numberOfLoop = Integer.parseInt(args.get("numberOfLoop").value());
                int inc = Integer.parseInt(args.get("inc").value());
                String selected = args.get("selected").value();
                //Initiale les variables pour les traitement
                String[] generatorInput = null;
                HashMap<String,String> map=this.getGenerateurInput(generator, args);
                Object generatorOutput;
                HashMap<String,BigInteger> result=new HashMap<String,BigInteger>();
                int curso = 1;

                System.out.println("numberOfLoop: "+numberOfLoop);
                System.out.println("inc: "+inc);
                System.out.println("selected: "+selected);

                for (int i=0; i < numberOfLoop; i++) {
                    System.out.println("Loop");
                    //Input pour le generateur
                    generatorInput=map.values().toArray(new String[0]);
                    //Resultat du generateur
                    generatorOutput = generator.generate(generatorInput);

                    System.out.println("OUTPUT: "+generatorOutput);
                    //Get le gasUsed 
                    BigInteger gasUsed = onClass(classOfScript.getName())
                    .call("deploy", Application.getInstance().getWeb3J(),
                            Application.getInstance().getCredentials(), new DefaultGasProvider())
                    .call("send")
                    .call(Application.DEFAULT_METHOD_START, generatorOutput)
                    .call("send").call("getGasUsed").get();

                    System.out.println("gasUsed:" + gasUsed);
                    //Ajout le gasUsed au resultat total
                    result.put(map.get(selected),gasUsed);
                    //Met a jour la valeur de l'élément a incrémenter
                    map.put(selected, String.valueOf(curso));
                    
                    System.out.println("Map: "+map.get(selected));
                    //On augmente le curso en fonction de inc
                    curso+=inc;
                }
                return result;
            }
        } catch (ReflectException e) {
            e.printStackTrace();
        } catch (NullPointerException|NoSuchMethodException e) {
            System.err.println("Aucune method run trouvé");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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



    private HashMap<String,String> getGenerateurInput(Generator generator,QueryParamsMap args) throws Exception{
        //Arguments demandés du generateur
        KeyValue<String,Class>[] genArgs = generator.getArgs();
        //La map va stocker key/valeur des filtres demandé
        HashMap<String,String> map = new HashMap<String,String>();

        for (int i = 0; i < genArgs.length; i++) {
            KeyValue<String,Class> arg = genArgs[i];
            String key = arg.getKey()+"Generator";
            if (!args.hasKey(key) )
                throw new Exception("Missing argument in QueryParamArgs for key : " + key);
            map.put(key,args.get(key).value());
        }
        return map;
    }
}
