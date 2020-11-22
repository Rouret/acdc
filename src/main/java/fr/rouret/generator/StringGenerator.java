package fr.rouret.generator;

import java.util.Random;

public class StringGenerator {
    //33-47 & 58-64 & 91-96 & 123-125 = caracteres spéciaux
    //48-57 = chiffres
    //65-90 = lettres maj
    //97-122 = lettres
    public static int ASCII_INDEX_MIN=33;
    public static int ASCII_INDEX_MAX=126;
    //Pour tester
    public static void main(String[] args) {
        System.out.println(StringGenerator.generate(100));
    }
    public static String generate(int length){
        return new Random().ints(StringGenerator.ASCII_INDEX_MIN, StringGenerator.ASCII_INDEX_MAX + 1)
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

    }
}
