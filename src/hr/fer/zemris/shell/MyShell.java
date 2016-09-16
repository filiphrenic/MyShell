package hr.fer.zemris.shell;

import hr.fer.zemris.shell.command.CatShellCommand;
import hr.fer.zemris.shell.command.CharsetsShellCommand;
import hr.fer.zemris.shell.command.CopyShellCommand;
import hr.fer.zemris.shell.command.DecryptShellCommand;
import hr.fer.zemris.shell.command.DeleteShellCommand;
import hr.fer.zemris.shell.command.EncryptShellCommand;
import hr.fer.zemris.shell.command.ExitShellCommand;
import hr.fer.zemris.shell.command.GetshaShellCommand;
import hr.fer.zemris.shell.command.HexdumpShellCommand;
import hr.fer.zemris.shell.command.LsShellCommand;
import hr.fer.zemris.shell.command.MkdirShellCommand;
import hr.fer.zemris.shell.command.ShellCommand;
import hr.fer.zemris.shell.command.SymbolShellCommand;
import hr.fer.zemris.shell.command.TreeShellCommand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides several commands.
 * <ul>
 * <li><b>cat arg1 [arg2]</b> (see {@link CatShellCommand} for more info)</li>
 * <li><b>charsets</b> (see {@link CharsetsShellCommand} for more info)</li>
 * <li><b>copy arg1 arg2</b> (see {@link CopyShellCommand} for more info)</li>
 * <li><b>getsha arg1</b> (see {@link GetshaShellCommand} for more info)</li>
 * <li><b>exit</b> (see {@link ExitShellCommand} for more info)</li>
 * <li><b>hexdump arg1</b> (see {@link HexdumpShellCommand} for more info)</li>
 * <li><b>ls</b> (see {@link LsShellCommand} for more info)</li>
 * <li><b>mkdir arg1</b> (see {@link MkdirShellCommand} for more info)</li>
 * <li><b>symbol arg1 [arg2]</b> (see {@link SymbolShellCommand} for more info)</li>
 * <li><b>tree arg1</b> (see {@link TreeShellCommand} for more info)</li>
 * </ul>
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class MyShell {

    public static void main(String[] args) {

        Map<String, ShellCommand> commands = new HashMap<>();
        commands.put("cat", new CatShellCommand());
        commands.put("charsets", new CharsetsShellCommand());
        commands.put("copy", new CopyShellCommand());
        commands.put("decrypt", new DecryptShellCommand()); // added
        commands.put("delete", new DeleteShellCommand()); // added
        commands.put("getsha", new GetshaShellCommand()); // added
        commands.put("encrypt", new EncryptShellCommand()); // added
        commands.put("exit", new ExitShellCommand());
        commands.put("hexdump", new HexdumpShellCommand());
        commands.put("ls", new LsShellCommand());
        commands.put("mkdir", new MkdirShellCommand());
        commands.put("symbol", new SymbolShellCommand());
        commands.put("tree", new TreeShellCommand());

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8));

        try {
            out.write("Welcome to MyShell v 1.0");
            out.newLine();
            out.flush();
        } catch (IOException ioe) {
            System.out.println("Error with output buffer.");
        }

        ShellStatus status = ShellStatus.CONTINUE;
        while (status == ShellStatus.CONTINUE) {

            // String[] arguments = readFromInput(in, out).split("\\s+");

            String[] arguments = mySplit(readFromInput(in, out)); // doesn't split paths with spaces in them

            ShellCommand command = commands.get(arguments[0].toLowerCase());

            if (command == null) {
                MyShell.error("Unknown command.", out);
            } else {
                status = command.executeCommand(in, out, Arrays.copyOfRange(arguments, 1, arguments.length));
            }

        }

        try {
            in.close();
            out.close();
        } catch (IOException ioe) {
            System.err.println("Error while closing files");
        }
    }

    /**
     * Reads lines from reader until it reaches a line that doesn't have a multiline
     * 
     * @param in input stream
     * @param out output stream
     * @return a string that was read
     */
    private static String readFromInput(BufferedReader in, BufferedWriter out) {
        StringBuilder input = new StringBuilder();

        try {
            out.write(Character.toString(ShellSymbol.getSymbol("PROMPT", out)) + " ");
            out.flush();

            String line;
            while (true) {
                line = in.readLine();
                if (line == null || !line.endsWith(Character.toString(ShellSymbol.getSymbol("MORELINES", out)))) {
                    break;
                }
                input.append(line.substring(0, line.length() - 1)).append(" ");
            }
            if (line != null) {
                input.append(line);
            }

        } catch (IOException ioe) {
            System.err.println("Error with input/output buffers.");
        }

        return input.toString();
    }

    /**
     * Prints a error message to the output stream.
     * 
     * @param message
     * @param out output stream
     * @return <code>CONTINUE</code> because the shell doesn't need to terminate
     */
    public static ShellStatus error(String message, BufferedWriter out) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException ioe) {
            System.out.println("Error with output buffer.");
        }
        return ShellStatus.CONTINUE;
    }

    /**
     * Custom splitting that works with paths that have quotation marks in them
     * 
     * @param str string to split
     * @return an array of strings from the parameter
     */
    private static String[] mySplit(String str) {

        int argsCapacity = 10;

        String s = str.trim().replaceAll("\\s+", " ");
        String[] result = new String[argsCapacity];
        int i = 0;
        StringBuilder buff = new StringBuilder();
        int qm = 0; // quotationmark

        for (Character c : s.toCharArray()) {
            if (c == '\"') {
                qm++;
            } else if (c == ' ') {
                if (qm % 2 == 0) {
                    if (i == argsCapacity - 2) {
                        argsCapacity *= 2;
                        result = Arrays.copyOf(result, argsCapacity);
                    }
                    result[i++] = buff.toString();
                    buff.setLength(0);
                } else {
                    // unutar navodnika
                    buff.append(' ');
                }
            } else {
                buff.append(c);
            }
        }
        result[i++] = buff.toString();
        return Arrays.copyOfRange(result, 0, i);
    }
}
