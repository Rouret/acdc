
package fr.rouret.generators.generators;

import java.util.ArrayList;
import java.util.List;

import fr.rouret.KeyValue;
import fr.rouret.api.Application;
import fr.rouret.generators.Generator;

public class ListDoubleGenerator extends Generator<List<Double>> {

    public ListDoubleGenerator() {
        super("ListDoubleGenerator", new KeyValue<>("min", Double.class),new KeyValue<>("max", Double.class), new KeyValue<>("n", Integer.class));
    }

    @Override
    public List<Double> generate(String... args) {
        // double min = Double.parseDouble(args[0]);
        // double max = Double.parseDouble(args[1]);
        String min = args[0];
        String max = args[1];
        int n = Integer.parseInt(args[2]);
        List<Double> list= new ArrayList<Double>();
        for (int i = 0; i < n; i++) {
            list.add((Double) Application.getInstance().getGeneratorHandler().getGenerator("DoubleGenerator").generate(min,max));
        }
        return list;
    }
}
