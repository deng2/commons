package com.greenbee.commons.format.test;

import java.util.Date;

public class PrintFormat {

    //%[argument_index$][flags][width][.precision]conversion
    //%[argument_index$][flags][width]conversion
    //%[flags][width]conversion
    /*
     * flag:
     *  -
     *  #
     *  ,
     *  +
     */
    /*
     * conversion:
     * d
     * o
     * x
     * f
     * e
     * c: character
     * s
     * tM
     * tD
     */
    public static void main(String[] args) {
        //decimal 
        System.out.println(String.format("|%1$-,12d|", 100000));
        //octal
        System.out.println(String.format("|%1$-#10o|", 100));
        //hex
        System.out.println(String.format("|%1$#10x|", 100));
        //decimal float
        System.out.println(String.format("|%1$10.2f|", 101110.111));
        //scientific floating
        System.out.println(String.format("|%1$10.2e|", 10110.111));
        //special
        System.out.println(String.format("|%10%|%n|"));
        //character
        System.out.println(String.format("|%1$10c|", '\t'));
        //string
        System.out.println(String.format("|%1$10s|", "YYY"));
        System.out.println(String.format("|%1$10s|", new Date()));
        //date time
        System.out.println(String.format("|%1$10tM|", new Date()));

        //maximum output
        System.out.println(String.format("|%1$3.5s|", "YYYttt"));
    }
}
