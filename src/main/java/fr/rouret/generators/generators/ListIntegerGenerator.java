package fr.rouret.generators.generators;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import fr.rouret.KeyValue;
import fr.rouret.api.Application;
import fr.rouret.generators.Generator;

public class ListIntegerGenerator extends Generator<List<BigInteger>> {

    public ListIntegerGenerator() {
        super("ListIntegerGenerator", new KeyValue<>("min", Integer.class),new KeyValue<>("max", Integer.class), new KeyValue<>("lengthList", Integer.class));
    }

    @Override
    public List<BigInteger> generate(String... args) {
        // int min = Integer.parseInt(args[0]);
        // int max = Integer.parseInt(args[1]);
        String min = args[0];
        String max = args[1];
        int n = Integer.parseInt(args[2]);
        List<BigInteger> list = new ArrayList<BigInteger>();
        for (int i = 0; i < n; i++) {
            list.add((BigInteger) Application.getInstance().getGeneratorHandler().getGenerator("IntegerGenerator").generate(min,max));
        }
        return list;
    }
}
