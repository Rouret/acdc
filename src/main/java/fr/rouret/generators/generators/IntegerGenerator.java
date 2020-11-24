package fr.rouret.generators.generators;

import java.util.Random;

import fr.rouret.KeyValue;
import fr.rouret.generators.Generator;

/**
 * @author Baptiste MAQUET on 23/11/2020
 * @project tets
 */
public class IntegerGenerator extends Generator<Integer> {

    public IntegerGenerator() {
        super("IntegerGenerator", new KeyValue<>("min", Integer.class), new KeyValue<>("max", Integer.class));
    }

    @Override
    public Integer generate(String... args) {
        int min = Integer.parseInt(args[0]);
        int max = Integer.parseInt(args[1]);
        return min + RANDOM.nextInt(max - min);
    }
}
