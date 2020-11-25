package fr.rouret.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Error {
    private String description;
    private Exception error;


    public Error(Exception e){
        this.description=e.getMessage();
        this.error=e;
    }
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        JsonObject info = new JsonObject();
        info.addProperty("name", this.error.getLocalizedMessage());
        info.addProperty("type", this.error.getClass().getName());
        info.addProperty("description", this.description);
        json.add("error", info);
        return json;
    }
}
