package fr.rouret.generators.generators;

import fr.rouret.KeyValue;
import fr.rouret.generators.Generator;

/**
 * @author Baptiste MAQUET on 23/11/2020
 * @project tets
 */
public class StringGenerator extends Generator<String> {
    //33-47 & 58-64 & 91-96 & 123-125 = caracteres spéciaux
    //48-57 = chiffres
    //65-90 = lettres maj
    //97-122 = lettres
    public static int ASCII_INDEX_MIN = 33;
    public static int ASCII_INDEX_MAX = 126;

    public StringGenerator() {
        super("StringGenerator", new KeyValue<>("lenght", Integer.class));
    }

    @Override
    public String generate(String... args) {
        int lenght = Integer.parseInt(args[0]);
        return RANDOM.ints(StringGenerator.ASCII_INDEX_MIN, StringGenerator.ASCII_INDEX_MAX + 1)
                .limit(lenght)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
