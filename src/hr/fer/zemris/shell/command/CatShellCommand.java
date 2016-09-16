package hr.fer.zemris.shell.command;

import hr.fer.zemris.shell.MyShell;
import hr.fer.zemris.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

/**
 * This class is used to print out the contet of a file, using a specified charset (if none is provided, default is
 * used) in {@link MyShell} class.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class CatShellCommand implements ShellCommand {

    /**
     * Default buffer capacity used when reading a file.
     */
    private static final int BUFFER_CAPACITY = 4096;

    /**
     * Prints the file to the output stream. First argument must be the file which you want to print. Second argument is
     * used charset. If charset isn't provided, the default one is used.
     */
    @Override
    public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] args) {

        if (args.length != 1 && args.length != 2) {
            return MyShell.error("Must provide unleast one argument.", out);
        }

        Charset charset = Charset.defaultCharset();

        // setting charset
        if (args.length == 2) {
            try {
                charset = Charset.forName(args[1]);
            } catch (IllegalCharsetNameException icne) {
                return MyShell.error("Unknown charset " + args[1], out);
            } catch (UnsupportedCharsetException uce) {
                return MyShell.error("Provided charset " + args[1] + " not supported.", out);
            }
        }

        // reading from file and printing it out
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(
                    new FileInputStream(args[0])), charset));

            char[] cbuf = new char[BUFFER_CAPACITY];
            while (true) {
                int numOfChars = fileReader.read(cbuf);
                if (numOfChars < 1) {
                    break; // end of stream
                }
                out.write(cbuf, 0, numOfChars);
            }
            out.newLine();
            out.flush();
            fileReader.close();

        } catch (FileNotFoundException fnfe) {
            return MyShell.error("File " + args[0] + " doesn't exist.", out);
        } catch (IOException ioe) {
            return MyShell.error("Error occured while reading file.", out);
        }

        return ShellStatus.CONTINUE;
    }
}
