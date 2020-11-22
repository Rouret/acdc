package fr.rouret.generator;

import java.util.Random;

public class DoubleGenerator {
    public static void main(String[] args) {
        System.out.println(DoubleGenerator.generate(-2.3,2.4));
    }
    public static double generate(double min,double max){
        return min + (max - min) * new Random().nextDouble();
    }
}
