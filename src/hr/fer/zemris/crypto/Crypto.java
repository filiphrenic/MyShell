package hr.fer.zemris.crypto;

import hr.fer.zemris.util.Utility;

/**
 * This class allows user to encrypt/decrypt given file using AES crypto-algorithm and 128-bit encryption key or
 * calculate and check SHA-1 file digest
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class Crypto {

    /**
     * User must provide arguments for the program to know what to do. Arguments are provided through args parameter
     * Availible arguments are
     * <ul>
     * <li>encrypt - encrypts the document given through arguments</li>
     * <li>decrypt - decrypts the document given through arguments</li>
     * <li>checksha - checks SHA-1 file digest</li>
     * </ul>
     * 
     * @param args arguments given through command line
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Must provide arguments.");
        }

        if (args[0].equalsIgnoreCase("encrypt") || args[0].equalsIgnoreCase("decrypt")) {
            if (args.length != 3) {
                throw new IllegalArgumentException("Must provide 2 additional arguments in " + args[0] + " mode.");
            }
            crypt(args[1], args[2], (args[0].equalsIgnoreCase("encrypt") ? CryptMode.ENCRYPT : CryptMode.DECRYPT));

        } else if (args[0].equalsIgnoreCase("checksha")) {
            if (args.length != 2) {
                throw new IllegalArgumentException("Must provide 1 additional argument in " + args[0] + " mode.");
            }
            checksha(args[1]);

        } else {
            throw new UnsupportedOperationException(
                    "This class only supports 'encrypt', 'decrypt' and 'checksha' modes.");
        }
    }

    /**
     * Calculates and checks SHA-1 file digest.
     *
     * @param fileName file you want to check, provide it's file path
     */
    private static void checksha(String fileName) {

        ShaChecker sc = new ShaChecker(fileName);
        sc.calculateDigest();

        System.out.print("Please provide expected sha signature for " + fileName + ":\n> ");
        boolean b = sc.checkIfAreTheSame(Utility.readLine());

        System.out.println("Digesting completed. Digest of " + fileName + (b ? " matches " : " does not match the ")
                + "the expected digest." + (b ? "" : ("\nDigest was " + sc.getDigest())));

    }

    /**
     * Encrypts or decrypts given inputFile to outputFile.
     * 
     * @param inputFile
     * @param outputFile
     * @param mode encrypt/decrypt
     */
    private static void crypt(String inputFile, String outputFile, CryptMode mode) {

        String encryptionKey;
        String initializationVector;

        System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");
        encryptionKey = Utility.readLine();
        System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
        initializationVector = Utility.readLine();

        FileCrypter fileCrypter = new FileCrypter(inputFile, outputFile, encryptionKey, initializationVector, mode);

        if (fileCrypter.cryptIt()) {
            System.out.println((mode == CryptMode.ENCRYPT ? "En" : "De") + "cryption completed. Generated file "
                    + outputFile + " based on file " + inputFile);
        } else {
            System.out.println((mode == CryptMode.ENCRYPT ? "En" : "De") + "cryption failed.");
        }
    }
}
