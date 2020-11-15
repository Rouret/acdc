package org.web3j.api.routes;

import static spark.Spark.*;

import java.io.File;
import java.net.URL;

import org.web3j.api.controllers.*;
public class Routes {
   public static void load(){
      
      // get("/scripts/:name/result",(req, res) -> Web3jController.process());

      redirect.get("/scripts","/scripts/");

      path("/scripts", () -> {
         get("/", (request, response) -> FileController.getAvailableScripts());
         
         path("/:name", () -> {
            get("/result", (request, response) -> Web3jController.process(request.params(":name"), null));
         });
     });
   }

}
