package fr.rouret.api.controllers;


import java.io.IOException;
import java.net.URISyntaxException;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fr.rouret.api.services.FileService;
import fr.rouret.api.services.Web3jService;

public class FileController extends Controller {
    public static String getAvailableScripts() throws URISyntaxException, IOException {
        FileService fileService = new FileService();

        Web3jService web3jService=new Web3jService();
        String[] listScripts = fileService.getAvailableScripts().stream().map(c->c.getSimpleName()).toArray(String[]::new);

        JsonObject json = new JsonObject();
        JsonArray arryJson = new JsonArray();
        for (String scriptName : listScripts) {
            try {
                JsonObject scriptJson = new JsonObject();
                scriptJson.addProperty("name", scriptName);
                scriptJson.add("params", web3jService.getContractInfo(scriptName).toJson());
                arryJson.add(scriptJson);
            } catch (Exception e) {
                //Ne rien mettre quand certain script ne sont pas fonctionnel et donc genere une erreur
            }
            
        }
        json.add("scripts",arryJson);
        return json.toString();
    }
}
