package fr.rouret.generators.generators;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import fr.rouret.KeyValue;
import fr.rouret.api.Application;
import fr.rouret.generators.Generator;

public class ListNumericalGenerator extends Generator<List<BigInteger>> {

    public ListNumericalGenerator() {
        super("ListNumericalGenerator",new KeyValue<>("max", Integer.class), new KeyValue<>("n", Integer.class));
    }

    @Override
    public List<BigInteger> generate(String... args) {
        // int max = ((Integer) args[0]);
        // int n = ((Integer) args[1]);
        String max = args[0];
        int n = Integer.parseInt(args[1]);
        List<BigInteger> list= new ArrayList<BigInteger>();
        for (int i = 0; i < n; i++) {
            list.add(BigInteger.valueOf((Integer) Application.getInstance().getGeneratorHandler().getGenerator("NumericalGenerator").generate(max)));
        }
        return list;
    }
}
