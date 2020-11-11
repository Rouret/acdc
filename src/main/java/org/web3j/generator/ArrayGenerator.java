package org.web3j.generator;

import java.util.ArrayList;

import jnr.ffi.Struct.nlink_t;

public class ArrayGenerator {
    //Pour tester
    public static void main(String[] args) {
        System.out.println(ArrayGenerator.generate(-100,10,100).toString());
    }
    public static ArrayList<Integer> generate(int min,int max,int n){
        ArrayList<Integer> list= new ArrayList<Integer>();
        for (int i = 0; i < n; i++) {
            //La valeur attendu par nextInt est le max Exclu
            int temp_int=IntegerGenerator.generate(min, max);
            //Gestion du minimun
            if(temp_int<min){
                temp_int=+min;
            }
            list.add(temp_int);
        }
        return list;
    }
}
