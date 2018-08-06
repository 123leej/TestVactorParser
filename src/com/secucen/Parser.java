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
        LEN("LEN"),
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

        for (String key : arr) {
            temp = key.split(":");
            _dict.put(temp[0], GROUP.valueOf(temp[1]));
        }

        this.dict = _dict;
    }

    public void update_reader(BufferedReader _buf_reader) {
        this.buf_reader = _buf_reader;
    }

//    public String mappingDataByPlainText() throws IOException {
//        if (this.dict == null) {
//            return "There is no Keyword";
//        }
//
//        Dictionary<String, String> keyByData = new Hashtable<String, String>();
//        Dictionary<String, Dictionary> mergedData = new Hashtable<String, Dictionary>();
//
//        String  ret = "";
//        String data = "";
//
//        while ((data = this.buf_reader.readLine()) != null) {
//            if (!data.equals("")) {
//                keyByData = this.parseByKeyword(data);
//                if (keyByData.size() == 0) {
//                    continue;
//                }
//
//                String id = keyByData.get("PT");
//                keyByData.remove("PT");
//                if (mergedData.get(id).isEmpty()) {
//                    mergedData.put(id, keyByData);
//                } else {
//                    this.matchDict(mergedData.get(id), keyByData);
//                }
//            }
//        }
//
//        System.out.println(mergedData.toString());
//        return ret;
//
//    }

//    public Dictionary<String, String> matchDict(Dictionary<String, String> _old, Dictionary<String, String> _new) {
//        Dictionary<String, String> ret = new Hashtable<String, String>();
//
//        while ( _old.keys().hasMoreElements() && _new.keys().hasMoreElements() ) {
//            String ok = _old.keys().nextElement();
//            String nk = _new.keys().nextElement();
//
//            if (ok.equals(nk)) {
//                if (_old.get(ok).equals(_new.get(nk))) {
//                    ret.put(ok, _old.get(ok));
//                } else {
//
//                }
//            }
//
//
//        }
//    }

    public String parseString() throws IOException {
        if (this.dict == null) {
            return "There is no Keyword";
        }

        Dictionary<String, String> keyByData = new Hashtable<String, String>();

        String ret = "";
        String data = "";
        String temp1 = "";
        String temp2 = "";

        while ((data = this.buf_reader.readLine()) != null) {

            if (!data.equals("")) {
                keyByData = this.parseByKeyword(data);
                if (keyByData.size() == 0) {
                    continue;
                }
                temp1 += this.groupValueBySetting(keyByData) + ",";
                temp2 += temp1;
            }

            else {
                if (!temp2.equals("")) {
                    temp2 = temp2.substring(0, temp2.length() - 1);
                    temp2 = wraping(temp2);
                    ret += temp2 + ",";
                    temp2 = "";
                } else {
                    continue;
                }
            }
            temp1 = "";
        }

        ret = ret.substring(0, ret.length() -1);
        ret = wraping(ret);

        if (this.aCase == CASE.LOWER) {
            ret = ret.toLowerCase();
        } else if (this.aCase == CASE.UPPER) {
            ret = ret.toUpperCase();
        }

        ret = this.indentString(ret);

        this.buf_writer.write(ret);

        return ret;
    }

    private String indentString(String _input) {
        String ret = "";
        String enter = "\r\n";
        String tab = "\t";
        int line_break_flag = 0;

        int depth = 0;
        int i;
        char[] input = _input.toCharArray();

        for (char chr : input){
            switch (chr){
                case '{':
                    if (depth < 2) {
                        ret += chr + enter;
                        depth++;
                        for (i = 0; i < depth; i++) {
                            ret += tab;
                        }
                    } else {
                        ret += chr;
                        depth++;
                        line_break_flag = 0;
                    }
                    break;

                case '}':
                    depth --;
                    if (depth < 2) {
                        ret += enter;
                        for (i = 0; i < depth; i++) {
                            ret += tab;
                        }
                        ret += chr;
                    } else {
                        ret += chr;
                    }
                    break;

                case ',':
                    if (depth < 3) {
                        ret += chr + enter;
                        for (i = 0; i < depth; i++) {
                            ret += tab;
                        }
                    } else {
                        ret += chr;
                        line_break_flag++;
                        if (line_break_flag % 10 == 0) {
                            ret += enter + tab + tab;
                        }
                    }
                    break;

                default:
                    ret += chr;
            }
        }

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
            case LEN:
                ret = String.valueOf(Integer.parseInt(input.get(_keyword))/8);
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

            if (i != temp1.length -2 ) {
                ret += ",";
            }
            i += 2;
        }

        return ret;
    }

    private String wraping(String input) {
        return "{" + input + "}";
    }
}
