package hr.fer.zemris.shell.command;

import hr.fer.zemris.crypto.CryptMode;
import hr.fer.zemris.crypto.FileCrypter;
import hr.fer.zemris.shell.MyShell;
import hr.fer.zemris.shell.ShellStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Helper class used by {@link EncryptShellCommand} and {@link DecryptShellCommand} classes.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class CryptShellCommand implements ShellCommand {

    private final CryptMode mode;

    public CryptShellCommand(CryptMode mode) {
        this.mode = mode;
    }

    /**
     * Encrypts/decrypts the file provided as first argument and stores the result in the second argument.
     */
    @Override
    public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] args) {
        if (args.length != 2) {
            return MyShell.error("Must provide two paths, source and destination.", out);
        }

        if (!Files.exists(Paths.get(args[0])) || Files.isDirectory(Paths.get(args[0]))) {
            return MyShell.error("There is no such file as " + args[0], out);
        }

        String encryptionKey;
        String initializationVector;

        try {
            out.write("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");
            out.flush();
            encryptionKey = in.readLine();

            out.write("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
            out.flush();
            initializationVector = in.readLine();

            if (encryptionKey == null || initializationVector == null) {
                throw new IOException("Error while reading from input.");
            }

            FileCrypter crypter = new FileCrypter(args[0], args[1], encryptionKey, initializationVector, mode);
            if (crypter.cryptIt()) {
                // these aren't errors, but works good.
                return MyShell
                        .error((mode == CryptMode.ENCRYPT ? "En" : "De") + "crypting completed. Generated "
                                + Paths.get(args[1]).getFileName() + " based on " + Paths.get(args[0]).getFileName()
                                + ".", out);
            } else {
                return MyShell.error((mode == CryptMode.ENCRYPT ? "En" : "De") + "crypting failed.", out);
            }

        } catch (IOException ioe) {
            return MyShell.error("Error with I/O stream.", out);
        }
    }
}
