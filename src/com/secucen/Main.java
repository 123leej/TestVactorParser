package com.secucen;

import name.fraser.neil.plaintext.diff_match_patch;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Main {
    static ArrayList<String> mode = new ArrayList<String>();
    static ArrayList<String> keyword = new ArrayList<String>();
    static ArrayList<String> files = new ArrayList<String>();
    static ArrayList<String> acase = new ArrayList<String>();

    public static void main(String[] args) throws IOException {

        String print;

        File original;
        File result_file;
        BufferedReader reader;
        BufferedWriter writer;

        Parser parser;

        argParser(args);



        switch (mode.get(0)) {
            case "multi":
                for (int i = 0; i < files.size(); i++) {
                    original = new File(".\\" + files.get(i));

                    result_file = new File(original.getParentFile().getAbsolutePath() + "\\" + original.getName().substring(0, original.getName().length() - 4) + "_parsed.txt");

                    reader = new BufferedReader(new FileReader(original));
                    writer = new BufferedWriter(new FileWriter(result_file));
                    parser = new Parser(reader, writer, Parser.CASE.valueOf(acase.get(0)));

                    parser.setKeyword(keyword);

                    parser.parseString();

                    reader.close();
                    writer.close();
                }
                break;
//            case "single":
//                ArrayList<File> target_files = new ArrayList<File>();
//
//                //target_files.get(0).getParentFile().getAbsolutePath() + "\\" + dmp.diff_cleanupSemantic(diff); + "_Compressed.txt"
//
//                reader = null;
//                writer = new BufferedWriter(new StringWriter());
//                parser = new Parser(reader, writer, Parser.CASE.valueOf(acase.get(0)));
//
//                parser.setKeyword(keyword);
//
//                for (int i = 0; i < files.size(); i++) {
//                    target_files.add(new File(".\\" + files.get(i)));
//                    //result_file = new File(util.getMatchPattern(target_files));
//                    result_file = new File("test.txt");
//                    parser.update_reader(new BufferedReader(new FileReader(files.get(i))));
//
//                    parser.mappingDataByPlainText();
//
//                    reader.close();
//                }
//                writer.close();
//                break;
            default:
                original = new File(".\\" + files.get(0));

                result_file = new File(original.getParentFile().getAbsolutePath() + "\\" + original.getName().substring(0, original.getName().length() - 4) + "_parsed.txt");

                reader = new BufferedReader(new FileReader(original));
                writer = new BufferedWriter(new FileWriter(result_file));
                parser = new Parser(reader, writer, Parser.CASE.valueOf(acase.get(0)));

                parser.setKeyword(keyword);

                parser.parseString();

                reader.close();
                writer.close();
                break;
        }
    }

    public static void argParser(String[] args){
        String temp = "";

        for(String arg : args) {

            if ((arg.equals("--m") || arg.equals("--f")) || (arg.equals("--k") || arg.equals("--c"))) {
                temp = arg;
            }

            switch (temp) {
                case "--m":
                    mode.add(arg);
                    break;
                case "--f":
                    files.add(arg);
                    break;
                case "--k":
                    keyword.add(arg);
                    break;
                case  "--c":
                    acase.add(arg);
                    break;
                default:
                    System.out.println("java -jar TestVactorMaker.jar --c [CASE] --m [mode] --k [keyword] --f [files]");
                    System.out.println("case : UPPER, LOWER");
                    System.out.println("mode :  multi, single (nullable)");
                    System.out.println("keyword: KEY:BYTE CTR:NONE PT:BYTE CT:BYTE]");
                    System.exit(0);
            }
        }

        if (keyword.isEmpty() || files.isEmpty() || acase.isEmpty()) {
            System.out.println("java -jar TestVactorMaker.jar --c [CASE] --m [mode] --k [keyword] --f [files]");
            System.out.println("case : UPPER, LOWER");
            System.out.println("mode :  multiple, single");
            System.out.println("keyword: A:BYTE|NONE B:BYTE|NONE C:BYTE|NONE");
            System.exit(0);
        }

        mode.remove(0);
        files.remove(0);
        keyword.remove(0);
        acase.remove(0);
    }
}
