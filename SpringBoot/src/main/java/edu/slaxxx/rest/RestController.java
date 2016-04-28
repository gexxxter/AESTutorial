package edu.slaxxx.rest;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.slaxxx.beans.StupidDB;
import edu.slaxxx.crypt.Crypt;
import edu.slaxxx.entities.SecretEntity;

@org.springframework.web.bind.annotation.RestController
public class RestController {

	@Inject
	StupidDB db;

	@Inject
	Crypt crypt;

	static private String badPassword = "Really bad pass";

	@RequestMapping("/hello")
	public String hello() {
		return "hello";
	}

	@RequestMapping("/dbtest")
	public SecretEntity getTest() {
		SecretEntity entity = db.load("test");
		return entity;
	}

	@RequestMapping("/load")
	public SecretEntity load(@RequestParam(value = "key") String key) throws Exception {
		SecretEntity entity = db.load(key);
		SecretKeySpec secretKey = crypt.createKey(badPassword.toCharArray(), crypt.getSalt());
		entity.value = crypt.decrypt(entity.value, secretKey);
		return entity;
	}

	@RequestMapping("/store")
	public String store(@RequestParam(value = "key") String key, @RequestParam(value = "value") String value) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		SecretKeySpec secretKey = crypt.createKey(badPassword.toCharArray(), crypt.getSalt());
		SecretEntity entity = new SecretEntity();
		entity.value = crypt.encrypt(value, secretKey);
		db.store(entity, key);
		return "ok";
	}

}
