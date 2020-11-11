package org.web3j.generator;
import java.util.Random;
public class IntegerGenerator {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(IntegerGenerator.generate(-5,5));
        }
    }
    public static Integer generate(int min,int max){
        return min + new Random().nextInt(max - min);
    }
}
