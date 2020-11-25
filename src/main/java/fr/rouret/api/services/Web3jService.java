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

    public Generator getContractInfo(String scriptName) throws Exception {
     
        System.out.println("===========");
        // Find abi (json file)
        File scripFile = Application.getInstance().getScriptHandler().getScript(scriptName.toLowerCase() + ".abi")
                .getFile();
        // get inputs
        List<NamedType> inputs = this.getInput(scripFile);

        if(inputs.size()!=1) throw new Exception("Le script demandé demande un nombre de paramètres non supportés");

        for (NamedType ty : inputs) { //pas tres utile pour 1 tour

            String generatorName=this.getGeneratorNameFromInputType(ty);

            Generator<?> generator = Application.getInstance().getGeneratorHandler().getGenerator(generatorName);

            return generator;//pas propre, manque de temps
        }
        
        return null; //^^
    }
    public HashMap<String, BigInteger> process(String scriptName, QueryParamsMap args) throws Exception {
        
        if(!args.hasKey("selected") || !args.hasKey("numberOfLoop") || !args.hasKey("inc")) {
            throw new Exception("Il manque des parametres: selected ET/OU numberOfLoop ET/OU inc");
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
        if(inputs.size()!=1) throw new Exception("Le script demandé demande un nombre de paramètres non supportés");
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

            System.out.println("*****INIT*****");
            System.out.println("numberOfLoop: "+numberOfLoop);

            System.out.println("selected: "+selected);

            for (int i=0; i < numberOfLoop; i++) {
                System.out.println("*****");
                System.out.println("Valeur param: "+map.get(selected));
                System.out.println("i: "+i);

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
                int newValue=Integer.parseInt(map.get(selected)) + inc;
                map.put(selected, String.valueOf(newValue));
                
                System.out.println("Map: "+map.get(selected));
               
            }
            return result;
        }
        return null;
    }

    private List<NamedType> getInput(File scriptFile) throws Exception {
        ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
        if(scriptFile==null) throw new Exception("Le script demandé n'a pas été trouvé");
        AbiDefinition[] abiDefinition = objectMapper.readValue(scriptFile, AbiDefinition[].class);
        //le findFirst renvoie un NullPointerException si le filter n'a rien trouvé, c'est comme ça qu'on peut vérifier qu'il il y a une méthod
        Optional<AbiDefinition> runMethod =Arrays.stream(abiDefinition).filter(abi->abi.getName().equalsIgnoreCase(Application.DEFAULT_METHOD_START)).findFirst();
        //Double check
        if(!runMethod.isPresent()) throw new NullPointerException("Aucune méthode '"+Application.DEFAULT_METHOD_START+"' trouvé dans le script solidity");
        return runMethod.get().getInputs();
    }

    private String getGeneratorNameFromInputType(NamedType typeName) throws Exception{
        // remove number
        String type = typeName.getType().replaceAll("\\d", "");
        String generatorName = Application.getInstance().getConfSolidityType().get(type);
        System.out.println("Generateur en question :"+generatorName);
        // throw error if type not find
        if (generatorName == null)
            throw new Exception("Le générateur '"+ generatorName + "' n'a pas été trouvé");
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
                throw new Exception("Il manque le paramete : " + key);
            map.put(key,args.get(key).value());
        }
        return map;
    }
}
