package edu.slaxxx.beans;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import edu.slaxxx.crypt.Crypt;
import edu.slaxxx.entities.SecretEntity;

@Named
public class StupidDB {
	private HashMap<String, SecretEntity> stupidDB = new HashMap<>();

	public StupidDB() {
		SecretEntity entity = new SecretEntity();
		entity.value = "Test";
		store(entity, "test");
	}

	public void store(SecretEntity entity, String key) {
		stupidDB.put(key, entity);
	}

	public SecretEntity load(String key) {
		return stupidDB.get(key);
	}
}
