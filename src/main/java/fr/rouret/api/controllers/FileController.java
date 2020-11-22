package fr.rouret.api.controllers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import fr.rouret.api.services.FileService;

public class FileController extends Controller {
    public static String getAvailableScripts() throws URISyntaxException, IOException {
        FileService service = new FileService();
        return gson.toJson(service.getAvailableScripts().stream().map(c->c.getSimpleName()).toArray(String[]::new));
    }
}
