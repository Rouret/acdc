package fr.rouret.api.controllers;

import java.util.Set;

import com.google.gson.JsonObject;

import fr.rouret.api.services.Web3jService;
import spark.QueryParamsMap;
public class Web3jController extends Controller {
    public static String getResultOfContract(String scriptName, QueryParamsMap queryParamsMap) {
        System.out.println("Script demandé:"+scriptName);
        Web3jService service=new Web3jService();
        service.process(scriptName,queryParamsMap);
        //TODO return
        return "oui oui";
    }
    public static String getContractInfo(String scriptName){
        System.out.println("Script demandé:"+scriptName);

        Web3jService service=new Web3jService();

        JsonObject json = new JsonObject();
        json.addProperty("name", scriptName);
        json.add("params", service.getContractInfo(scriptName).toJson());
        
        return json.toString();
    }

}
    
