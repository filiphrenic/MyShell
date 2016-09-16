package hr.fer.zemris.shell.command;

import hr.fer.zemris.crypto.CryptMode;
import hr.fer.zemris.shell.MyShell;
import hr.fer.zemris.shell.ShellStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Used in {@link MyShell} class for encrypting files.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class EncryptShellCommand implements ShellCommand {

    /**
     * Enrypts the first file and stores it into the second file.
     */
    @Override
    public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] args) {
        return new CryptShellCommand(CryptMode.ENCRYPT).executeCommand(in, out, args);
    }

}
