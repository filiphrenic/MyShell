package hr.fer.zemris.crypto;

import hr.fer.zemris.util.Utility;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class is used for calculating SHA-1 file digest.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class ShaChecker {

    private static final int BUFFER_CAPACITY = 4096;

    private MessageDigest shaDigest;
    private byte[] digest;
    private final String fileName;

    /**
     * Creates a new ShaChecker that calculates SHA-1 file digest of given file.
     * 
     * @param fileName
     */
    public ShaChecker(String fileName) {
        this.fileName = fileName;

        try {
            shaDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException nsae) {
            // ignoring it, because there is a SHA-1 algorithm
        }
    }

    /**
     * Calculates the digest that {@link MessageDigest} digested.
     */
    public void calculateDigest() {
        if (this.readFile()) {
            digest = shaDigest.digest();
        }
    }

    /**
     * Returns the calculated digest.
     * 
     * @return string representation of the digest
     */
    public String getDigest() {
        return Utility.byteToHex(digest);
    }

    /**
     * Checks if the two digests are the same.
     * 
     * @param keyText text being compared to
     * @return <code>true</code> if they are the same, <code>false</code> otherwise
     */
    public boolean checkIfAreTheSame(String keyText) {
        return Utility.byteToHex(digest).equals(keyText);
    }

    /**
     * Tries to read a file.
     * 
     * @return <code>true</code> if reading succeded, <code>false</code> otherwise
     */
    private boolean readFile() {
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(fileName));
            byte[] buffer = new byte[BUFFER_CAPACITY]; // 4kB buffer

            while (true) {
                int numOfBytes = is.read(buffer);
                if (numOfBytes < 1) {
                    break;
                }
                shaDigest.update(buffer, 0, numOfBytes);
            }
            is.close();
            return true; // reading succeded

        } catch (IOException e) {
            System.err.print("Error occured while trying to read the file: " + fileName);
            return false;
        }
    }
}
