import java.util.LinkedList;
import name.fraser.neil.plaintext.diff_match_patch;

public class hello {
    public static void main(String args[]) {
        diff_match_patch dmp = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> diff = dmp.diff_main("Hello World.", "Goodbye World.");
        dmp.diff_cleanupSemantic(diff);
        System.out.println(diff);
    }
}