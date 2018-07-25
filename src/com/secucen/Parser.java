package com.secucen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

public class Parser {

    private BufferedReader buf_reader;
    private BufferedWriter buf_writer;
    private Dictionary<String, GROUP> dict;
    CASE aCase;

    public enum GROUP {
        BYTE("BYTE"),
        NONE("NONE");

        private  String group;

        GROUP(String _group) {
            this.group = _group;
        }
    }

    public enum CASE {
        UPPER("UPPER"),
        LOWER("LOWER");

        private  String acase;

        CASE(String _case) {
            this.acase = _case;
        }
    }

    Parser(BufferedReader _buf_reader, BufferedWriter _buf_writer, CASE _case) throws IOException {
        this.buf_reader = _buf_reader;
        this.buf_writer = _buf_writer;
        this.aCase = _case;
        this.dict = null;
    }

    public void setKeyword(ArrayList<String> arr){
        Dictionary<String, GROUP> _dict = new Hashtable<String, GROUP>();
        String[] temp = {"", ""};

        while (arr.iterator().hasNext()) {
            temp = arr.get(0).split(":");
            _dict.put(temp[0], GROUP.valueOf(temp[1]));
            arr.remove(0);
        }

        this.dict = _dict;
    }
    public String mappingString() throws IOException {
        if (this.dict == null) {
            return "There is no Keyword";
        }

        Dictionary<String, String> KeyByData = new Hashtable<String, String>();
        Dictionary<String, Dictionary> mergedData = new Hashtable<String, Dictionary>();

        String  ret = "";
        String data = "";

        String enterDoubleTab = "\r\n\t\t";
        String enterTab = "\r\n\t";
        String enter = "\r\n";

    }

    public String parseString() throws IOException {
        if (this.dict == null) {
            return "There is no Keyword";
        }

        Dictionary<String, String> keyByData = new Hashtable<String, String>();

        String ret = "";
        String data = "";

        String enterDoubleTab = "\r\n\t\t";
        String enterTab = "\r\n\t";
        String enter = "\r\n";

        String temp1 = enterDoubleTab;

        ret += "{" + enter;

        while ((data = this.buf_reader.readLine()) != null) {

            String temp2 = "";

            if (!data.equals("")) {
                keyByData = this.parseByKeyword(data);
                if (keyByData.size() == 0) {
                    continue;
                }
                temp2 += this.groupValueBySetting(keyByData);
                temp2 += "," + enterDoubleTab;
                temp1 += temp2;
            }

            else {
                if (!temp1.equals(enterDoubleTab)) {
                    temp1 = temp1.substring(0, temp1.length() - 1);
                    ret += "\t" + wraping(temp1) + "," + enter;
                }
                temp1 = enterDoubleTab;
                continue;
            }
        }

        ret = ret.substring(0, ret.length() -3 );
        ret += enter + "}";

        if (this.aCase == CASE.LOWER) {
            ret = ret.toLowerCase();
        } else if (this.aCase == CASE.UPPER) {
            ret = ret.toUpperCase();
        }

        this.buf_writer.write(ret);

        return ret;
    }

    private String groupValueBySetting(Dictionary<String, String> input) throws IOException {
        String ret = "";
        String _keyword = input.keys().nextElement();

        GROUP flag = this.dict.get(_keyword);

        switch (flag) {
            case BYTE:
                ret = wraping(this.makeByteFormat(input.get(_keyword)));
                break;
            case NONE:
                ret = input.get(_keyword);
                break;
            default:
                break;
        }

        return ret;
    }

    private Dictionary<String, String> parseByKeyword(String input) {
        int keySize = input.indexOf("=") - 1;
        String Key = input.substring(0, keySize);
        Dictionary<String, String> ret = new Hashtable<String, String>();

        if (this.dict.get(Key) != null) {
            ret.put(Key, input.substring(keySize+3, input.length()));
        }

        return ret;
    }

    private String makeByteFormat(String input) throws IOException {

        char[]  temp1;
        int i = 0;
        String ret = "";

        temp1 = input.toCharArray();


        while (i < temp1.length) {
            ret += "0x" + temp1[i] + temp1[i+1];

            if (i % 20 == 18 ) {
                ret += "\r\n\t\t";
            }

            if (i != temp1.length -2 ) {
                ret += ",";
            }

            i += 2;
        }

        return ret;
    }

    private String wraping(String input) {
        return "{ " + input + " }";
    }
}
