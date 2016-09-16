package hr.fer.zemris.shell.command;

import hr.fer.zemris.shell.MyShell;

import hr.fer.zemris.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Used in {@link MyShell} class to create a hex dump a file and to write it to the output stream.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class HexdumpShellCommand implements ShellCommand {

    /**
     * How many bytes per one row.
     */
    private static final int BYTES_IN_ROW = 16;

    /**
     * Creates a hex-dump of the file provided in arguments.
     */
    @Override
    public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] args) {
        if (args.length != 1) {
            return MyShell.error("Must proide a path to the file", out);
        }

        Path src = Paths.get(args[0]);

        if (!Files.exists(src)) {
            return MyShell.error("Provided path is not valid", out);
        }

        try {
            InputStream is = new BufferedInputStream(new FileInputStream(src.toFile()));

            byte[] buff = new byte[BYTES_IN_ROW];
            StringBuilder s = new StringBuilder();
            int numOfPrints = 0;

            while (true) {
                int numOfBytes = is.read(buff);
                if (numOfBytes < 1) {
                    break;
                }
                s.append(String.format("%08x: ", numOfPrints));
                s.append(byteToHex(buff, numOfBytes));
                s.append(makeLetters(buff, numOfBytes));

                numOfPrints += BYTES_IN_ROW;

                out.write(s.toString());
                out.newLine();
                out.flush();
                s.setLength(0);

            }

            is.close();

        } catch (IOException ioe) {
            return MyShell.error("Error with file/buffer.", out);
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Returns a string representation of the byte array. Bytes that aren't in <code>[32,127]</code> interval are
     * represented as a <code>'.'<code>.
     * 
     * @param bytes bytes to represent
     * @param len how many of them are there
     * @return string representation
     */
    private static String makeLetters(byte[] bytes, int len) {

        StringBuilder buff = new StringBuilder();

        for (int i = 0; i < len; i++) {
            int value = bytes[i] & 0xff;
            if (value < 32 || value > 127) {
                buff.append('.');
            } else {
                buff.append((char) value);
            }
        }

        return buff.toString();
    }

    /**
     * Returns string representation of a byte array.
     * 
     * @param bytes bytes to represent
     * @param len how many bytes are there
     * @return string representation
     */
    private static String byteToHex(byte[] bytes, int len) {
        StringBuilder buff = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {

            if (i < len) {
                if ((bytes[i] & 0xff) < 0x10) {
                    buff.append('0');
                }
                buff.append(Integer.toHexString(bytes[i] & 0xff).toUpperCase());
            } else {
                buff.append(" ");
            }

            if (i == bytes.length / 2 - 1) {
                buff.append("|");
            } else {
                buff.append(" ");
            }

        }
        return buff.append("| ").toString();
    }

}
