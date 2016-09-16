package hr.fer.zemris.shell.command;

import hr.fer.zemris.shell.MyShell;
import hr.fer.zemris.shell.ShellStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Used in {@link MyShell} class to print out all of the charsets availible on the machine running the shell.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class CharsetsShellCommand implements ShellCommand {

    /**
     * Prints out availible charsets to the output stream. No arguments needed.
     */
    @Override
    public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] args) {

        if (args.length != 0) {
            return MyShell.error("'charsets' command doesn't need any additional arguments", out);
        }

        Map<String, Charset> map = Charset.availableCharsets();

        try {
            out.write("You can use one of this charsets: ");
            out.newLine();
            out.flush();

            for (String charset : map.keySet()) {
                out.write(charset);
                out.newLine();
                out.flush();
            }

            out.write(map.keySet().size() + " charsets availible.");
            out.newLine();
            out.flush();
        } catch (IOException ioe) {
            return MyShell.error("Error while writing to output.", out);
        }

        return ShellStatus.CONTINUE;
    }
}
