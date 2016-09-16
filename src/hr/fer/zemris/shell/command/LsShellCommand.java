package hr.fer.zemris.shell.command;

import hr.fer.zemris.shell.MyShell;
import hr.fer.zemris.shell.ShellStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class is used in the {@link MyShell} class to list out the files contained in the directory.
 * File attributes listed are:
 * <ul>
 * <li>directory/readable/writeable/executable (i.e drw-)</li>
 * <li>size of the file/directory</li>
 * <li>time of creation</li>
 * <li>file/directory name</li>
 * </ul>
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class LsShellCommand implements ShellCommand {

    @Override
    public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] args) {

        if (args.length != 1) {
            return MyShell.error("Must provide directory path.", out);
        }

        File dir = Paths.get(args[0]).toFile();
        if (!dir.isDirectory()) {
            return MyShell.error("Provided file must be a direcltory.", out);
        }

        for (File file : dir.listFiles()) {
            printToOutput(String.format("%s %10d %s %s%n", getAttributes(file.toPath()), getSize(file),
                    getCreationTime(file.toPath(), out), file.getName()), out);
        }

        return ShellStatus.CONTINUE;
    }

    /**
     * Prints a string to the outputStream
     * 
     * @param format string to be printed
     * @param out output stream used for printing
     */
    private void printToOutput(String format, BufferedWriter out) {
        try {
            out.write(format);
            out.flush();
        } catch (IOException ioe) {
            MyShell.error("Error while writing to the output stream.", out);
        }

    }

    /**
     * Gets file attributes.
     * drwx (directory, readable, writeable, executable)
     * if the file isn't one of those, there's a '-' sign at that place, i.e. -rw-
     * 
     * @param filePath path to the file
     * @return attributes
     */
    private static String getAttributes(Path filePath) {
        char directory = '-';
        if (Files.isDirectory(filePath)) {
            directory = 'd';
        }

        char readable = '-';
        if (Files.isReadable(filePath)) {
            readable = 'r';
        }

        char writeable = '-';
        if (Files.isWritable(filePath)) {
            writeable = 'w';
        }

        char executable = '-';
        if (Files.isExecutable(filePath)) {
            executable = 'x';
        }

        return String.valueOf(directory) + readable + writeable + executable;
    }

    /**
     * Gets the size of the directory including all of its files and subfolders.
     * 
     * @param file file whose size we want to knows
     * @return size of the directory
     */
    private static long getSize(File file) {
        if (file.isFile()) {
            return file.length();
        }

        long size = 0;
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                size += f.length();
            } else {
                size += getSize(f);
            }
        }
        return size;
    }

    /**
     * Returns the time when the file was created.
     * 
     * @param path path to the file whose creation time we want to know
     * @param out output stream used to write an error if there is one
     * @return date and time of creation
     */
    private static String getCreationTime(Path path, BufferedWriter out) {

        String formattedDateTime = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            BasicFileAttributeView faView = Files.getFileAttributeView(path, BasicFileAttributeView.class,
                    LinkOption.NOFOLLOW_LINKS);

            BasicFileAttributes attributes = faView.readAttributes();
            FileTime fileTime = attributes.creationTime();
            formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
        } catch (IOException ioe) {
            MyShell.error("Error occurred while getting creation time of the file.", out);
        }
        return formattedDateTime;
    }
}
