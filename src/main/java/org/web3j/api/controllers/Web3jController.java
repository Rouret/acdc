package org.web3j.api.controllers;

import org.web3j.api.services.Web3jService;
public class Web3jController extends Controller {
    public static String process(String scriptName, String[] params) {
        System.out.println("Script demandé:"+scriptName);
        Web3jService service=new Web3jService();
        service.process(scriptName,params);
        return "oui oui";
    }

}
    
