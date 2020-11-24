package fr.rouret.generators.generators;

import fr.rouret.KeyValue;
import fr.rouret.generators.Generator;

public class NumericalGenerator extends Generator<Integer> {

    public NumericalGenerator() {
        super("NumericalGenerator", new KeyValue<>("max", Integer.class));
    }

    @Override
    public Integer generate(String... args) {
        int max =  Integer.parseInt(args[0]);
        return RANDOM.nextInt(max);
    }
}

