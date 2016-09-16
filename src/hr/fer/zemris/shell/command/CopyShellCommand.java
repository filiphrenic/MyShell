package hr.fer.zemris.shell.command;

import hr.fer.zemris.shell.MyShell;
import hr.fer.zemris.shell.ShellStatus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Used in {@link MyShell} class for copying files.
 *
 * @author Filip Hrenić
 * @version 1.0
 */
public class CopyShellCommand implements ShellCommand {

    /**
     * Buffer capacity used for copying.
     */
    private static final int BUFFER_CAPACITY = 4096;

    /**
     * Needs two arguments to work properly. First argument is the path to the file being copied, second argument is the
     * path to the new file.
     */
    @Override
    public ShellStatus executeCommand(BufferedReader in, BufferedWriter out, String[] args) {
        if (args.length != 2) {
            return MyShell.error("Two paths needed to copy. Source and destination path.", out);
        }

        Path src = Paths.get(args[0]);
        Path dest = Paths.get(args[1]);

        if (!Files.exists(src)) {
            return MyShell.error("Provided source path doesn't exist.", out);
        }

        // copying
        try {

            out.write("Copying " + src.getFileName() + "...");
            out.newLine();
            out.flush();

            InputStream is = new BufferedInputStream(new FileInputStream(src.toFile()));
            OutputStream os = new BufferedOutputStream(new FileOutputStream(dest.toFile()));

            byte[] buff = new byte[BUFFER_CAPACITY];
            while (true) {
                int numOfBytes = is.read(buff);
                if (numOfBytes < 1) {
                    break;
                }
                os.write(buff, 0, numOfBytes);
                os.flush();
            }

            is.close();
            os.close();

            out.write("Copying done. Created " + dest.getFileName() + ".");
            out.newLine();
            out.flush();

        } catch (IOException ioe) {
            return MyShell.error("Error while copying file " + src.getFileName(), out);
        }

        return ShellStatus.CONTINUE;
    }
}
