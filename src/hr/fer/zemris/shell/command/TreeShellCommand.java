package hr.fer.zemris.shell.command;

import hr.fer.zemris.shell.MyShell;
import hr.fer.zemris.shell.ShellStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Used in the {@link MyShell} class to print a tree like formation of the directory structure.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class TreeShellCommand implements ShellCommand {

    /**
     * Prints a tree structure of the directory given through arguments.
     */
    @Override
    public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] args) {
        if (args.length != 1) {
            return MyShell.error("Must provide a path to the directory.", out);
        }

        Path filePath = Paths.get(args[0]);
        if (!Files.exists(filePath)) {
            return MyShell.error("Provided path isn't a valid path.", out);
        }

        try {
            Files.walkFileTree(filePath, new DirectoryTree(out));
        } catch (IOException ioe) {
            return MyShell.error("Error while going through directory", out);
        }
        return ShellStatus.CONTINUE;
    }

    /**
     * Used for walking through the directory.
     * 
     * @author Filip Hrenić
     * @version 1.0
     */
    private static class DirectoryTree implements FileVisitor<Path> {

        private int indent = 0;
        private final BufferedWriter out;

        public DirectoryTree(BufferedWriter out) {
            this.out = out;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            printToOut(dir.getFileName().toString());
            indent += 2;
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            printToOut(file.getFileName().toString());
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            indent -= 2;
            return FileVisitResult.CONTINUE;
        }

        /**
         * Used for printing to output stream.
         * 
         * @param s string to be printed
         */
        private void printToOut(String s) {
            try {
                String spaces = (indent == 0 ? "" : String.format("%" + indent + "s", ""));
                out.write(spaces);
                out.write(s);
                out.newLine();
                out.flush();
            } catch (IOException ioe) {
                MyShell.error("Error while writing to output stream.", out);
            }
        }

    }

}
