package fr.rouret.generators;

import fr.rouret.generators.generators.*;


import java.util.HashMap;
import java.util.Map;


public class GeneratorHandler {

    private Map<String, Generator<?>> generators = new HashMap<>();

    public GeneratorHandler() {
        registerGenerator(new IntegerGenerator());
        registerGenerator(new StringGenerator());
        registerGenerator(new DoubleGenerator());
        registerGenerator(new NumericalGenerator());
        registerGenerator(new NumericalDoubleGenerator());
        registerGenerator(new ListIntegerGenerator());
        registerGenerator(new ListNumericalGenerator());
        registerGenerator(new ListDoubleGenerator());
        registerGenerator(new ListNumericalDoubleGenerator());
        registerGenerator(new ListStringGenerator());
    }

    public void registerGenerator(Generator<?> generator){
        System.out.println("Registering generator: "+generator.name);
        generators.put(generator.name, generator);
    }

    public Generator<?> getGenerator(String name){
        return generators.get(name);
    }
}
