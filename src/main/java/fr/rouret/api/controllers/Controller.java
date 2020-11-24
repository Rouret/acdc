package fr.rouret.api.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Controller {
	public static Gson gson = new GsonBuilder()
	.setPrettyPrinting() // STATIC|TRANSIENT in the default configuration
	.create();
}