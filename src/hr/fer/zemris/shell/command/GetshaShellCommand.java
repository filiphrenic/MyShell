package hr.fer.zemris.shell.command;

import hr.fer.zemris.crypto.ShaChecker;
import hr.fer.zemris.shell.MyShell;
import hr.fer.zemris.shell.ShellStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Used in {@link MyShell} for checking the sha signature.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class GetshaShellCommand implements ShellCommand {

    /**
     * Compares sha signature of the provided file through arguments with signature from user input.
     */
    @Override
    public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] args) {

        if (args.length != 1) {
            return MyShell.error("Must provide path of the file you want to check.", out);
        }

        Path filePath = Paths.get(args[0]);

        if (!Files.exists(filePath)) {
            return MyShell.error("File provided doesn't exist.", out);
        }

        if (Files.isDirectory(filePath)) {
            return MyShell.error("Don't know how to calculate signature of directory.", out);
        }

        ShaChecker check = new ShaChecker(args[0]);
        check.calculateDigest();

        try {
            out.write("Digesting " + filePath.getFileName() + "...");
            out.newLine();
            out.flush();

            out.write("Digesting completed. Sha signature of " + filePath.getFileName() + " is:\n" + check.getDigest());
            out.newLine();
            out.flush();

        } catch (IOException ioe) {
            return MyShell.error("Error" + " with I/O buffers.", out);
        }

        return ShellStatus.CONTINUE;
    }
}
