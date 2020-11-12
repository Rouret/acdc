package org.web3j.api.controllers;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.web3j.api.services.FileService;

public class FileController extends Controller {
    public static String getAvailableScript() {
        FileService service = new FileService();
        File[] allFileNames;
        final Map<String, String[]> response = new HashMap<String, String[]>();
        try {
            allFileNames = service.getAvailableScript();
            String[] fileNames = new String[allFileNames.length];
            for (int i = 0; i < allFileNames.length; i++) {
                fileNames[i] = allFileNames[i].getName().split("\\.")[0];
            }
            response.put("scripts",fileNames);
        } catch (URISyntaxException e) {
            //TODO gestion dérreur
        }
        return gson.toJson(response);
    }
}
