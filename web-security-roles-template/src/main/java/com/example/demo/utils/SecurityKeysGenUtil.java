package com.example.demo.utils;

import static com.example.demo.utils.ServiceConstants.PRIVATE;
import static com.example.demo.utils.ServiceConstants.PUBLIC;
import static com.example.demo.utils.ServiceConstants.RSA_ALGO;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class SecurityKeysGenUtil {

	/**
	 * Generate Public and Private keys for local testing RSA - asymmetric keys
	 * (PublicKey and PrivateKey) generated in their own formats like PKCS,X509 
	 * Use Base64 Encoder to encode these values into a string and save them onto disk
	 * While validating the date, publickey needs to be decode using Base64 decoder
	 * 
	 * @return
	 * @throws Exception
	 */
	static Map<String, Object> genRSAKeys() {
		Map<String, Object> keys = new HashMap<>();
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGO);
			keyPairGenerator.initialize(2048);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();
			keys.put(PRIVATE, privateKey);
			keys.put(PUBLIC, publicKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keys;
	}

	public static void genKeysAndStoreIntoFiles() {
		Map<String, Object> rsaKeys = new HashMap<>();
		Encoder encoder = Base64.getEncoder();
		try {
			rsaKeys = genRSAKeys();
			PublicKey publicKey = (PublicKey) rsaKeys.get(PUBLIC);
			PrivateKey privateKey = (PrivateKey) rsaKeys.get(PRIVATE);
			
			StringBuilder pubKey = new StringBuilder();
			pubKey.append("-----BEGIN RSA PUBLIC KEY-----\n");
			pubKey.append(encoder.encodeToString(publicKey.getEncoded()));
			pubKey.append("\n-----END RSA PUBLIC KEY-----\n");
			// System.err.println(pubKey);

			StringBuilder priKey = new StringBuilder();
			priKey.append("-----BEGIN RSA PRIVATE KEY-----\n");
			priKey.append(encoder.encodeToString(privateKey.getEncoded()));
			priKey.append("\n-----END RSA PRIVATE KEY-----\n");
			// System.err.println(priKey);
			Files.writeString(Paths.get("privatekeygen.key"), priKey, StandardOpenOption.CREATE_NEW);
			Files.writeString(Paths.get("publickeygen.pub"), pubKey, StandardOpenOption.CREATE_NEW);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static Map<String, Object> loadKeysFromFiles(String pubKeyPath, String privKeyPath) {
		Map<String, Object> keys = new HashMap<>();
		Decoder decoder = Base64.getDecoder();
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGO);
			String pubKey = new String(Files.readAllBytes(Paths.get(pubKeyPath)), Charset.defaultCharset());

			String publicKeyPEM = pubKey.replace("-----BEGIN RSA PUBLIC KEY-----", "")
					.replaceAll(System.lineSeparator(), "").replace("-----END RSA PUBLIC KEY-----", "");
			byte[] pubKeyBytes = decoder.decode(publicKeyPEM.trim());
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
			keys.put(PUBLIC, keyFactory.generatePublic (pubKeySpec));

			String privKey = new String(Files.readAllBytes(Paths.get(privKeyPath)), Charset.defaultCharset());
			String privateKeyPEM = privKey.replace("-----BEGIN RSA PRIVATE KEY-----", "")
					.replaceAll(System.lineSeparator(), "").replace("-----END RSA PRIVATE KEY-----", "");
			byte[]  privKeyBytes = decoder.decode(privateKeyPEM.trim());
			PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privKeyBytes);
			keys.put(PRIVATE, keyFactory.generatePrivate(privKeySpec));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keys;
	}
	
	public static void main(String[] args) throws Exception {
		genKeysAndStoreIntoFiles();	    
	}

}
