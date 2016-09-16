package hr.fer.zemris.shell.command;

import hr.fer.zemris.shell.MyShell;
import hr.fer.zemris.shell.ShellStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Used in {@link MyShell} class for creating directories.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class MkdirShellCommand implements ShellCommand {

    /**
     * Creates a directory with name provided through arguments.
     */
    @Override
    public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] args) {

        if (args.length != 1) {
            return MyShell.error("Must provide a path of the wanted directory.", out);
        }

        // creating directory
        try {
            Files.createDirectory(Paths.get(args[0]));
        } catch (IOException ioe) {
            return MyShell.error("Error while creating directory", out);
        }

        try {
            out.write("Directory with name " + args[0] + " successfully created.");
            out.newLine();
            out.flush();
        } catch (IOException ioe) {
            return MyShell.error("Error with output buffer.", out);
        }

        return ShellStatus.CONTINUE;
    }
}
