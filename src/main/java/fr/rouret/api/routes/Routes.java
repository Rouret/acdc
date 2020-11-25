package fr.rouret.api.routes;

import static spark.Spark.*;

import fr.rouret.api.controllers.*;
import spark.Request;
import spark.Response;

public class Routes {
   public static void load(){
      
      // get("/scripts/:name/result",(req, res) -> Web3jController.process());

      redirect.get("/scripts","/scripts/");
      get("/test",(request, response) -> {
         
         System.out.println(request.queryParams("test"));
         return "oui" ;
      });
      path("/scripts", () -> {
         get("/", (request, response) -> FileController.getAvailableScripts());
         path("/:name", () -> {
            get("/result", (request, response) -> Web3jController.getResultOfContract(request.params(":name"), request.queryMap()));
            get("", (request, response) -> Web3jController.getContractInfo(request.params(":name")));
         });
     });
     
     
   }
   public static void test(Request request, Response response){
      
   }
}
