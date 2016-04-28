package edu.slaxxx.crypt;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Named;

import org.apache.commons.lang.ArrayUtils;

@Named
public class Crypt {

	private int HASH_ITERATIONS = 100000;
	private int KEY_LENGTH = 256;
	SecureRandom secureRandom = new SecureRandom();
	private byte[] salt;

	public Crypt() {
		this.salt = generateSalt();
	}

	// IV 16 + Ciphertext n*16
	public String decrypt(String cipherTextBase64, Key secretKey) throws Exception {
		byte[] cipherAndIv = Base64.getDecoder().decode(cipherTextBase64);
		byte[] iv = Arrays.copyOfRange(cipherAndIv, 0, 16);
		byte[] cipherText = Arrays.copyOfRange(cipherAndIv, 16, cipherAndIv.length);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
		byte[] plainText = cipher.doFinal(cipherText);

		return new String(plainText);
	}

	public String encrypt(String plainText, Key secretKey) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
		byte[] cipherText = cipher.doFinal(plainText.getBytes());
		byte[] completeCipher = ArrayUtils.addAll(iv, cipherText);
		return Base64.getEncoder().encodeToString(completeCipher);
	}

	public byte[] generateSalt() {
		byte[] salt = new byte[25];
		secureRandom.nextBytes(salt);
		return salt;
	}

	public SecretKeySpec createKey(char[] password, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		KeySpec spec = new PBEKeySpec(password, salt, HASH_ITERATIONS, KEY_LENGTH);
		SecretKey generatedSecret = factory.generateSecret(spec);
		System.out.println("Key erzeugt");
		return new SecretKeySpec(generatedSecret.getEncoded(), "AES");
	}

	public byte[] getSalt() {
		return this.salt;
	}
}
