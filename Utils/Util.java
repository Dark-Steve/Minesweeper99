package Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Util {
    // Utility functions can be added here in the future
    public static byte[] longToBytes(long v) {
        return ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN).putLong(v).array();
    }

    public static long bytesToLong(byte[] b) {
        if (b == null || b.length != 8)
            throw new IllegalArgumentException("length != 8");
        return ByteBuffer.wrap(b).order(ByteOrder.BIG_ENDIAN).getLong();
    }

}