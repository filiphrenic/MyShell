package hr.fer.zemris.shell.command;

import hr.fer.zemris.shell.MyShell;
import hr.fer.zemris.shell.ShellStatus;
import hr.fer.zemris.shell.ShellSymbol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Used in {@link MyShell} for previewing or changing symbols for prompt, multiline, morelines.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class SymbolShellCommand implements ShellCommand {

    /**
     * Previews or changes symbol for prompt, multiline or morelines.
     * If only one argument is provided, symbol specified by the name is previewed.
     * If two arguments are provided, symbol specified by the name is changed to the second argument.
     */
    @Override
    public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] args) {

        if (args.length != 1 && args.length != 2) {
            return MyShell.error("Must provide atleast one argument.", out);
        }

        if (args.length == 1) {
            try {
                out.write("Symbol for " + args[0] + " is '" + ShellSymbol.getSymbol(args[0], out) + "'.");
                out.newLine();
                out.flush();
            } catch (IOException ioe) {
                return MyShell.error("Error with output stream.", out);
            }
        } else {
            try {
                char symbolBefore = ShellSymbol.getSymbol(args[0], out);
                char symbolAfter = args[1].charAt(0);

                ShellSymbol.setSymbol(args[0], symbolAfter, out);

                out.write("Symbol for " + args[0] + " changed from '" + symbolBefore + "' to '" + symbolAfter + "'.");
                out.newLine();
                out.flush();
            } catch (IOException ioe) {

            }
        }

        return ShellStatus.CONTINUE;
    }
}
