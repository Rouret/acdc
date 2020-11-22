package fr.rouret.generator;


import java.math.BigInteger;
import java.util.ArrayList;


public class ArrayGenerator {
    //Pour tester
    public static void main(String[] args) {
        System.out.println(ArrayGenerator.generateIntArray(-100,10,100).toString());
    }
    public static ArrayList<BigInteger> generateIntArray(int min,int max,int n){
        ArrayList<BigInteger> list= new ArrayList<BigInteger>();
        for (int i = 0; i < n; i++) {
            //La valeur attendu par nextInt est le max Exclu
            int temp_int = IntegerGenerator.generate(min, max);
            //Gestion du minimun
            if(temp_int<min){
                temp_int=+min;
            }
            list.add(BigInteger.valueOf(temp_int));
        }
        return list;
    }
    public static ArrayList<BigInteger> generateNumericalArray(int max,int n){
        ArrayList<BigInteger> list= new ArrayList<BigInteger>();
        for (int i = 0; i < n; i++) {
            list.add(BigInteger.valueOf(NumericalGenerator.generate(max)));
        }
        return list;
    }
}
