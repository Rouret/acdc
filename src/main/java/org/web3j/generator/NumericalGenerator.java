package org.web3j.generator;

import java.util.Random;

public class NumericalGenerator {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(NumericalGenerator.generate(5));
        }
    }
    public static Integer generate(int max){
        return new Random().nextInt(max);
    }
}
