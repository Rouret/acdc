package fr.rouret.api.controllers;

import java.math.BigInteger;
import java.util.HashMap;
import com.google.gson.JsonObject;

import fr.rouret.api.services.Web3jService;
import fr.rouret.api.Error;
import fr.rouret.generators.Generator;
import spark.QueryParamsMap;

public class Web3jController extends Controller {
   
    public static String getResultOfContract(String scriptName, QueryParamsMap queryParamsMap) {
        System.out.println("Script demandé:" + scriptName);
        Web3jService service = new Web3jService();
        HashMap<String, BigInteger> result = null;
        //TODO return
        try {
            result = service.process(scriptName,queryParamsMap) ;
        } catch (Exception e) {
            Error err=new Error(e);
            return err.toJson().toString();
        }
        
        return gson.toJson(result);
    }
    public static String getContractInfo(String scriptName){
        System.out.println("Script demandé:"+scriptName);

        Web3jService service=new Web3jService();
        
        Generator generator = null;
        try {
            generator = service.getContractInfo(scriptName);
        } catch (Exception e) {
            Error err=new Error(e);
            return err.toJson().toString();
        }

        //JSON SETUP
        JsonObject json = new JsonObject();
        json.addProperty("name", scriptName);
        json.add("params", generator.toJson());


        return json.toString();
    }

}
    
