package fr.rouret.generators.generators;

import java.math.BigInteger;
import java.util.Random;

import fr.rouret.KeyValue;
import fr.rouret.generators.Generator;

/**
 * @author Baptiste MAQUET on 23/11/2020
 * @project tets
 */
public class IntegerGenerator extends Generator<BigInteger> {

    public IntegerGenerator() {
        super("IntegerGenerator", new KeyValue<>("lengthNumber", Integer.class));
    }

    @Override
    public BigInteger generate(String... args) {
        int length = Integer.parseInt(args[0]);
        int max = Integer.valueOf((int) (Math.pow(10,length) - 1));
        int min = -max;
        return BigInteger.valueOf(min + RANDOM.nextInt(max - min));
    }
}
