package hr.fer.zemris.shell.command;

import hr.fer.zemris.shell.MyShell;
import hr.fer.zemris.shell.ShellStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Used in {@link MyShell} class for file deleting.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class DeleteShellCommand implements ShellCommand {

    /**
     * Must provide only one argument, path to the file. File will be deleted if it's not in use and if you have the
     * privileges to delete it.
     */
    @Override
    public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] args) {

        if (args.length != 1) {
            return MyShell.error("Must provide only one parameter, path to the file.", out);
        }

        Path filePath = Paths.get(args[0]);

        if (!Files.exists(filePath)) {
            return MyShell.error("File " + filePath.getFileName() + " doesn't exist.", out);
        }

        try {
            Files.delete(filePath);
        } catch (IOException ioe) {
            return MyShell.error("Couldn't delete file. Check if the file is in use, or you "
                    + "might not have the privleges to delete it.", out);
        }

        return MyShell.error("File " + filePath.getFileName() + " deleted.", out); // isn't a error, but...
    }

}
