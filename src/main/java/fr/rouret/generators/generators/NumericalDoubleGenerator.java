package fr.rouret.generators.generators;

import fr.rouret.KeyValue;
import fr.rouret.generators.Generator;

public class NumericalDoubleGenerator extends Generator<Double> {

    public NumericalDoubleGenerator() {
        super("NumericalDoubleGenerator", new KeyValue<>("max", Double.class));
    }

    @Override
    public Double generate(String... args) {
        double max = Double.parseDouble(args[0]);
        return max  * RANDOM.nextDouble();
    }
}
