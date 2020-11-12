package org.web3j.api;

import static spark.Spark.*;



import org.web3j.api.routes.Routes;

public class Application {
    public static void main(String[] args) {
        
        new Application();
    }
    public Application(){
        port(8080);
        before((request, response) -> {
            response.type("application/json");
        });
        Routes.load();
    }

}