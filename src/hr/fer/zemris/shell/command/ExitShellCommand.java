package hr.fer.zemris.shell.command;

import hr.fer.zemris.shell.ShellStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Terminates the shell.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class ExitShellCommand implements ShellCommand {

    /**
     * Only thing this command does it's that it terminates the shell.
     */
    @Override
    public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] args) {
        return ShellStatus.TERMINATE;
    }

}
