
package fr.rouret.generators.generators;

import java.util.List;
import java.util.ArrayList;

import fr.rouret.KeyValue;
import fr.rouret.api.Application;
import fr.rouret.generators.Generator;

public class ListStringGenerator extends Generator<List<String>> {

    public ListStringGenerator() {
        super("ListStringGenerator", new KeyValue<>("lenght", Integer.class),new KeyValue<>("lengthList", Integer.class));
    }

    @Override
    public List<String> generate(String... args) {
        // int lenght =  Integer.parseInt(args[0]);
        String lenght = args[0];
        int n = Integer.parseInt(args[1]);
        List<String> list= new ArrayList<String>();
        for (int i = 0; i < n; i++) {
            list.add((String) Application.getInstance().getGeneratorHandler().getGenerator("StringGenerator").generate(lenght));
        }
        return list;
    }
}
