package fr.rouret.generators;

import fr.rouret.KeyValue;

import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public abstract class Generator<T> {

    protected static  final Random RANDOM = new Random();

    protected final String name;
    protected final KeyValue<String, Class>[] args;

    public Generator(String name, KeyValue<String, Class>... args) {
        this.name = name;
        this.args = new KeyValue[args.length];
        for (int i = 0; i < args.length; i++)
            this.args[i] = args[i];

    }

    public abstract T generate(String... args);

    public String getName() {
        return name;
    }

    public KeyValue<String, Class>[] getArgs() {
        return args;
    }
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("generatorName", name);
        JsonArray args = new JsonArray();
        for (KeyValue<String, Class> arg : this.args) {
            JsonObject argument = new JsonObject();
            argument.addProperty("name", arg.getKey()+"Generator");
            argument.addProperty("type", arg.getValue().getSimpleName());
            args.add(argument);
        }
        json.add("arguments", args);
        return json;
    }


}
