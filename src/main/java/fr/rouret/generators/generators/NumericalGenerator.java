package fr.rouret.generators.generators;

import java.math.BigInteger;

import fr.rouret.KeyValue;
import fr.rouret.generators.Generator;

public class NumericalGenerator extends Generator<BigInteger> {

  
    public NumericalGenerator() {
        super("NumericalGenerator", new KeyValue<>("lengthNumber", Integer.class));
    }

    @Override
    public BigInteger generate(String... args) {
        int length = Integer.parseInt(args[0]);
        int max = Integer.valueOf((int) (Math.pow(10,length) - 1));
        return BigInteger.valueOf(RANDOM.nextInt(max));
    }
}

