

package fr.rouret.generators.generators;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import fr.rouret.KeyValue;
import fr.rouret.api.Application;
import fr.rouret.generators.Generator;

public class ListNumericalDoubleGenerator extends Generator<List<Double>> {

    public ListNumericalDoubleGenerator() {
        super("ListNumericalDoubleGenerator", new KeyValue<>("max", Double.class), new KeyValue<>("n", Integer.class));
    }

    @Override
    public List<Double> generate(String... args) {
        // double max = ((Double) args[0]);
        String max = args[0];
        int n = Integer.parseInt(args[1]);
        List<Double> list= new ArrayList<Double>();
        for (int i = 0; i < n; i++) {
            list.add((Double) Application.getInstance().getGeneratorHandler().getGenerator("NumericalDoubleGenerator").generate(max));
        }
        return list;
    }
}
