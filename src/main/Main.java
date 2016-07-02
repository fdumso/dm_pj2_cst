package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Proposition prop = null;
        ArrayList<Proposition> premiseList = new ArrayList<>();
        try {
            if (args.length == 1) {
                File inputFile = new File(args[0]);
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
            System.err.println("Invalid input file");
            System.exit(-1);
        }
        CST cst = new CST(prop, premiseList);

        String result = cst.getOutput();
        String counterExample;
        if (!(counterExample = cst.getCounterExample()).equals("")) {
            result += "\n\nCounter Example:\n"+counterExample+"All the other proposition letter unlisted can be T or F";
        } else {
            result += "\n\nThere does not exist any counter example.";
        }

        // output to file
        FileOutputStream fos = new FileOutputStream("result_"+args[0]);
        fos.write(result.getBytes());
    }
}
