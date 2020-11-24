package fr.rouret.generators.generators;

import fr.rouret.KeyValue;
import fr.rouret.generators.Generator;

public class DoubleGenerator extends Generator<Double> {

    public DoubleGenerator() {
        super("DoubleGenerator", new KeyValue<>("min", Double.class), new KeyValue<>("max", Double.class));
    }

    @Override
    public Double generate(String... args) {
        double min = Double.parseDouble(args[0]);
        double max = Double.parseDouble(args[1]);
        return min + (max - min) * RANDOM.nextDouble();
    }
}
