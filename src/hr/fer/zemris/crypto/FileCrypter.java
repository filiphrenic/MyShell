package hr.fer.zemris.crypto;

import hr.fer.zemris.util.Utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * This class is used to encrypt/decrypt files in {@link Crypto} class.
 * 
 * @author Filip Hrenić
 * @version 1.0
 */
public class FileCrypter {

    private static final int BUFFER_CAPACITY = 4096;

    /**
     * Cipher being used.
     */
    private Cipher cipher;
    /**
     * Path to the input file.
     */
    private final String srcFile;
    /**
     * Path to the output file.
     */
    private final String destFile;

    /**
     * @param inputFile
     * @param outputFile
     * @param encryptionKey
     * @param initializationVector
     * @param mode can be encrpyt or decrypt
     */
    public FileCrypter(String inputFile, String outputFile, String encryptionKey, String initializationVector,
            CryptMode mode) {

        int opmode = (mode == CryptMode.ENCRYPT ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE);

        SecretKeySpec keySpec = new SecretKeySpec(Utility.hexToByte(encryptionKey), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(Utility.hexToByte(initializationVector));

        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(opmode, keySpec, paramSpec);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | InvalidAlgorithmParameterException e) {
            System.err.println("Error while creating cipher.");
        }

        srcFile = inputFile;
        destFile = outputFile;

        try {
            File out = new File(outputFile);
            if (!out.exists()) {
                Files.createFile(Paths.get(destFile));
            }
        } catch (IOException e) {
            System.err.println("Error while creating file at " + destFile);
        }

    }

    /**
     * Encrypts/Decrypts the source file to destination file.
     * 
     * @return <code>true</code> if crypting succeded, <code>false</code> otherwise
     */
    public boolean cryptIt() {

        try {
            InputStream is = new BufferedInputStream(new FileInputStream(srcFile));
            OutputStream os = new BufferedOutputStream(new FileOutputStream(destFile));

            byte[] bufferIn = new byte[BUFFER_CAPACITY]; // 4kB buffer
            while (true) {
                int numOfBytes = is.read(bufferIn);
                if (numOfBytes < 1) {
                    break;
                }

                byte[] bufferOut = new byte[BUFFER_CAPACITY]; // 4kB buffer
                int numOfBytesOut = cipher.update(bufferIn, 0, numOfBytes, bufferOut);
                os.write(bufferOut, 0, numOfBytesOut);
            }

            cipher.doFinal();
            is.close();
            os.close();

        } catch (IOException | ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {
            return false;
        }

        return true;

    }

}
