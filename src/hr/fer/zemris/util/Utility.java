package hr.fer.zemris.util;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

final public class Utility {

    private Utility() {
    }

    /**
     * Returns byte representation of hex-encoded {@link String}.
     * 
     * @param s string to decode
     * @return byte representation
     */
    public static byte[] hexToByte(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Returns string representation of a byte array.
     * 
     * @param bytes bytes to represent
     * @return string representation
     */
    public static String byteToHex(byte[] bytes) {
        StringBuilder buff = new StringBuilder();
        for (byte aByte : bytes) {
            buff.append(Integer.toHexString(aByte & 0xff));
        }
        return buff.toString();
    }

    /**
     * Reads a line from standard input.
     * 
     * @return read line
     */
    @SuppressWarnings("resource")
    public static String readLine() {
        String ret;
        Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        ret = scanner.nextLine();
        return ret;
    }
}
