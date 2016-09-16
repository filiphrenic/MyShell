package hr.fer.zemris.shell;

import java.io.BufferedWriter;

/**
 * Used in {@link MyShell} class as symbol representation.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class ShellSymbol {

    /**
     * Used on the start of every shell line that expects prompt.
     */
    private static char prompt = '>';

    /**
     * Used when user wants to input something in more than one line.
     */
    private static char morelines = '\\';

    /**
     * Used on the start of every shell line that is part of a multilne input.
     */
    private static char multilne = '|';

    /**
     * Returns the current value that is set on symbol defined by name paramter.
     * 
     * @param name which symbol
     * @return symbol
     */
    public static char getSymbol(String name, BufferedWriter out) {
        if (name.equalsIgnoreCase("prompt")) {
            return prompt;
        } else if (name.equalsIgnoreCase("morelines")) {
            return morelines;
        } else if (name.equalsIgnoreCase("multilne")) {
            return multilne;
        } else {
            MyShell.error("Unknown symbol name: " + name, out);
            return ' ';
        }
    }

    /**
     * Returns the current value that is set on symbol defined by name paramter.
     * 
     * @param name which symbol
     */
    public static void setSymbol(String name, char symbol, BufferedWriter out) {
        if (name.equalsIgnoreCase("prompt")) {
            prompt = symbol;
        } else if (name.equalsIgnoreCase("morelines")) {
            morelines = symbol;
        } else if (name.equalsIgnoreCase("multilne")) {
            multilne = symbol;
        } else {
            MyShell.error("Unknown symbol name: " + name, out);
        }
    }

}
