package hr.fer.zemris.shell.command;

import hr.fer.zemris.shell.MyShell;
import hr.fer.zemris.shell.ShellStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * Command interface in {@link MyShell} class. It only provides method to execute a command.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public interface ShellCommand {
    /**
     * Executes a shell command.
     * 
     * @param in Reader used to read additional input if required.
     * @param out Writer used for writing results of shell command.
     * @param args arguments provided by user
     * @return <code>CONTINUE</code> if shell should continue running, <code>TERMINATE</code> otherwise
     */
    ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] args);
}
