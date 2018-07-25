package com.secucen;

import name.fraser.neil.plaintext.diff_match_patch;

import java.io.File;
import java.util.*;
import java.util.ArrayList;

public class util {

    public static String getMatchPattern(ArrayList<File> target_files) {

        String path = target_files.get(0).getParentFile().getAbsolutePath() + "\\";
        ArrayList<String> file_name = new ArrayList<String>();
        diff_match_patch dmp = new diff_match_patch();
        String ret = "";


        for (File file : target_files) {
            file_name.add(file.getName());
        }

        LinkedList<diff_match_patch.Diff> diff = dmp.diff_main(file_name.get(0), file_name.get(1));
        dmp.diff_cleanupSemantic(diff);

        for (diff_match_patch.Diff d  : diff) {
            if (d.operation == diff_match_patch.Operation.EQUAL) {
                ret += d.text;
            }
        }

        return ret;
    }
}
