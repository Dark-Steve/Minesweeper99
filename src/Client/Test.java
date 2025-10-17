package Client;

import Minesweeper.PlayerBoard;
import Utils.MagicNumbers;
import Utils.Util;

// Imma just use this for whatever
public class Test {
    public static void main(String[] args) {
        long test = 100;
        System.out.println(test);
        test = Util.bytesToLong(Util.longToBytes(test));
        System.out.println(test);
    }
}
