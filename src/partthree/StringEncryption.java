package partthree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.io.File;



/**
 * Utility class for encrypting/decrypting files. Stolen from here: 
 * http://www.macs.hw.ac.uk/~ml355/lore/FileEncryption.java
 * Described here: http://www.macs.hw.ac.uk/~ml355/lore/pkencryption.htm
 * Heavily modifed so that in encrypts strings to byte arrays, and decrypts bytearrays to strings
 * @author Cameron Cross 7193432
 * @since 14 April 2014
 */
public class StringEncryption {

	public static final int AES_Key_Size = 256;

	Cipher aesCipher;
	byte[] aesKey;
	SecretKeySpec aeskeySpec;

	/**
	 * Constructor: creates ciphers
	 */
	public StringEncryption() throws GeneralSecurityException {
		// create AES shared key cipher
		aesCipher = Cipher.getInstance("AES");
	}

	/**
	 * Creates a new AES key
	 */
	public void makeKey() throws NoSuchAlgorithmException {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(AES_Key_Size);
		SecretKey key = kgen.generateKey();
		aesKey = key.getEncoded();
		aeskeySpec = new SecretKeySpec(aesKey, "AES");
	}

	/**
	 * Loads the AES key in a raw format
	 * @param in - file the key is stored in
	 */
	public void loadKey(File in) throws GeneralSecurityException, IOException {
		// read AES key
		aesKey = new byte[AES_Key_Size/8];
		FileInputStream is = new FileInputStream(in);
		is.read(aesKey);
		is.close();
		aeskeySpec = new SecretKeySpec(aesKey, "AES");
	}

	/**
	 * Saves the AES key in a raw format
	 */
	public void saveKey(File out) throws IOException, GeneralSecurityException {
		// write AES key
		FileOutputStream os = new FileOutputStream(out);
		os.write(aesKey);
		os.close();
	}

/**
 * Encrypts string and returns byte array
 * @param in String to be encrypted
 * @return ByteAray containing the encrypted data
 * @throws IOException
 * @throws InvalidKeyException
 */
	public byte[] encrypt(String in) throws IOException, InvalidKeyException {
		aesCipher.init(Cipher.ENCRYPT_MODE, aeskeySpec);

		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		CipherOutputStream os = new CipherOutputStream(stream, aesCipher);
		os.write(in.getBytes("UTF-8"));
		
		os.flush();
		os.close();

		return stream.toByteArray();
	}
/**
 * Decryots byte array and returns a string
 * @param in byte array containing encrypted data
 * @return String containing the decrypted text
 * @throws IOException
 * @throws InvalidKeyException
 */
	public String decrypt(byte[] in) throws IOException, InvalidKeyException {
		
		int i;
		byte[] b = new byte[1024];
		
		aesCipher.init(Cipher.DECRYPT_MODE, aeskeySpec);

		InputStream stream = new ByteArrayInputStream(in);

		CipherInputStream is = new CipherInputStream(stream, aesCipher);
		ByteArrayOutputStream os = new ByteArrayOutputStream();


		while((i=is.read(b))!=-1) {
			os.write(b, 0, i);
		}

		is.close();
		os.close();
		
		return os.toString("UTF-8");
	}
}