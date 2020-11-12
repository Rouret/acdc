package org.web3j.api.routes;

import static spark.Spark.*;

import java.io.File;
import java.net.URL;

import org.web3j.api.controllers.*;
public class Routes {
   public static void load(){
      get("/scripts",(req, res) -> FileController.getAvailableScript());
   }

}
