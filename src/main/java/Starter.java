import javax.crypto.spec.SecretKeySpec;

public class Starter {

	public static void main(String[] args) throws Exception {
		String superSecretText = "Meine Lieblingsfarbe ist Blau!";
		Crypt crypt = new Crypt();
		SecretKeySpec encryptionKey = crypt.createKey("password".toCharArray(), crypt.generateSalt());

		String ciphertext = crypt.encrypt(superSecretText, encryptionKey);
		System.out.println("Ciphertext: " + ciphertext);
		
		String plainText = crypt.decrypt(ciphertext, encryptionKey);
		System.out.println("Plainttext: " + plainText);
	}

}
