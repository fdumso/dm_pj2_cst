package main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Proposition prop = null;
        ArrayList<Proposition> premiseList = new ArrayList<>();
        try {
            if (args.length == 1) {
                File inputFile = new File("input/"+args[0]);
                if (!inputFile.exists()) {
                    throw  new IOException();
                } else {
                    Scanner in = new Scanner(inputFile);
                    String propStr = in.nextLine();
                    prop = Parser.parse(propStr);
                    if (prop == null) {
                        throw new IOException();
                    } else {
                        int premiseNum = Integer.parseInt(in.nextLine());
                        for (int i = 0; i < premiseNum; i++) {
                            Proposition p = Parser.parse(in.nextLine());
                            if (p == null) {
                                throw new IOException();
                            } else {
                                premiseList.add(p);
                            }
                        }
                    }
                }
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            System.err.print("Invalid input file");
            System.exit(-1);
        }
        new CST(prop, premiseList).reduce();
    }
}
