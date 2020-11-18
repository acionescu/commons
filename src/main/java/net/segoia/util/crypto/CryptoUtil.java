/**
 * commons - Various Java Utils
 * Copyright (C) 2009  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.util.crypto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtil {
    public static void main(String[] args) throws Exception {
	KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
	keyGen.initialize(1024);
	KeyPair keyPair = keyGen.genKeyPair();
	byte[] publicKey = keyPair.getPublic().getEncoded();
	StringBuffer retString = new StringBuffer();
	for (int i = 0; i < publicKey.length; ++i) {
	    retString.append(Integer.toHexString(0x0100 + (publicKey[i] & 0x00FF)).substring(1));
	}
	// System.out.println(retString);

	System.out.println(bytesToString(keyPair.getPrivate().getEncoded()));

	String message = "Hello crypto!";

	System.out.println(base64Encode(encrypt(keyPair.getPrivate(), message)));

	System.out.println(base64Encode(encrypt(keyPair.getPrivate(), message)));

	long startTs = System.currentTimeMillis();

	System.out.println(new String(decrypt(keyPair.getPublic(), encrypt(keyPair.getPrivate(), message))));
	System.out.println(System.currentTimeMillis() - startTs);

	System.out.println(new String(decrypt(keyPair.getPrivate(), encrypt(keyPair.getPublic(), message))));
	System.out.println(System.currentTimeMillis() - startTs);

	System.out.println(testSign(keyPair, message));

	System.out.println(testSign(keyPair, message));

	KeyGenerator aesKeyGen = KeyGenerator.getInstance("AES");

	SecretKey secretKey = aesKeyGen.generateKey();
	Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	cipher.init(Cipher.ENCRYPT_MODE, secretKey);

	byte[] encrypted = cipher.doFinal(message.getBytes("UTF-8"));
	String out = base64Encode(encrypted);
	System.out.println((out));
	System.out.println(base64Encode(cipher.getIV()));
	System.out.println(base64Encode(secretKey.getEncoded()));
	
	String keyId="prod_node_id";

	 KeyPair gkp = generateAndSaveKeyPair("RSA", 1024, "/tmp", keyId);
	
	 KeyPair lkp = loadKeys("RSA", "/tmp", keyId);
	
	 System.out.println(gkp.getPublic().equals(lkp.getPublic()));

//	System.out.println(Cipher.getInstance("RSA"));
//
//	System.out.println(Cipher.getInstance("RSA/ECB/PKCS1Padding"));
//
//	String u ="\u003d";
//	String e ="=";
//	System.out.println(Arrays.toString(base64Decode(base64Encode(u.getBytes("UTF-8")))));
//	System.out.println(Arrays.toString(base64Decode(base64Encode(e.getBytes("UTF-8")))));
//	
//	System.out.println(base64Encode(generateKeyPair("RSA", 1024).getPublic().getEncoded()));
	
	
    }

    public static String computeHash(String input, String algorithm) throws NoSuchAlgorithmException {
	MessageDigest digest = MessageDigest.getInstance(algorithm);
	byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
	return base64Encode(hash);
    }
    
    public static String sha256(String input){
	try {
	    return CryptoUtil.computeHash(input, "SHA-256");
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException("Failed to compute sha256",e);
	}
    }

    public static KeyPair loadKeys(String algorithm, String path, String name)
	    throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
	byte[] privateKeyBytes = loadKeyFromFile(new File(path, name).getAbsolutePath());
	byte[] publicKeyBytes = loadKeyFromFile(new File(path, name + ".pub").getAbsolutePath());

	KeyFactory kf = KeyFactory.getInstance(algorithm);
	PrivateKey privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
	PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

	return new KeyPair(publicKey, privateKey);

    }

    public static String encodePrivateKeyToBase64String(PrivateKey privateKey) {
	PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
	return base64Encode(pkcs8EncodedKeySpec.getEncoded());
    }

    public static PrivateKey getPrivateKeyFromBase64EncodedString(String encodedKey, String algorithm)
	    throws Exception {
	byte[] base64Decode = base64Decode(encodedKey);
	KeyFactory kf = KeyFactory.getInstance(algorithm);
	return kf.generatePrivate(new PKCS8EncodedKeySpec(base64Decode));
    }

    public static String encodePublicKeyToBase64String(PublicKey publicKey) {
	X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
	return base64Encode(x509EncodedKeySpec.getEncoded());

    }

    public static PublicKey getPublicKeyFromBase64EncodedString(String encodedKey, String algorithm) throws Exception {
	byte[] decodedKey = Base64.getDecoder().decode(encodedKey.getBytes("UTF-8"));
	KeyFactory kf = KeyFactory.getInstance(algorithm);
	return kf.generatePublic(new X509EncodedKeySpec(decodedKey));
    }

    public static byte[] loadKeyFromFile(String fileName) throws IOException {
	System.out.println("Loading key from file "+fileName);
	return Base64.getDecoder().decode(Files.readAllBytes(new File(fileName).toPath()));
    }

    public static void saveKeyPair(KeyPair keyPair, String path, String name) throws IOException {
	X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());

	PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());

	saveKey(pkcs8EncodedKeySpec.getEncoded(), new File(path, name).getAbsolutePath());
	saveKey(x509EncodedKeySpec.getEncoded(), new File(path, name + ".pub").getAbsolutePath());
    }

    public static boolean saveKey(byte[] bytes, String file) throws IOException {
	FileOutputStream fos = new FileOutputStream(file);
	fos.write(base64Encode(bytes).getBytes(Charset.forName("UTF-8")));
	fos.close();

	return true;
    }
    
    public static boolean saveKey(byte[] bytes, String file, boolean encodeToBase64) throws IOException {
	FileOutputStream fos = new FileOutputStream(file);
	
	byte[] bytesToWrite = bytes;
	
	if(encodeToBase64) {
	    bytesToWrite = base64Encode(bytes).getBytes(Charset.forName("UTF-8"));
	}
	
	fos.write(bytesToWrite);
	fos.close();

	return true;
    }

    public static SecretKey generateSecretkey(String algorithm, int keySize) throws NoSuchAlgorithmException {
	KeyGenerator generator = KeyGenerator.getInstance(algorithm);
	generator.init(keySize);
	return generator.generateKey();
    }

    public static KeyPair generateKeyPair(String algorithm, int keySize) throws NoSuchAlgorithmException {
	KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
	keyGen.initialize(keySize);
	KeyPair keyPair = keyGen.genKeyPair();
	return keyPair;
    }

    public static KeyPair generateAndSaveKeyPair(String algorithm, int keySize, String path, String name)
	    throws NoSuchAlgorithmException, IOException {
	KeyPair keyPair = generateKeyPair(algorithm, keySize);
	saveKeyPair(keyPair, path, name);
	return keyPair;
    }

    public static byte[] sign(PrivateKey privateKey, byte[] data, String hashingAlgorithm) throws Exception {
	Signature sig = Signature.getInstance(hashingAlgorithm);
	sig.initSign(privateKey);
	sig.update(data);
	return sig.sign();
    }

    private static boolean testSign(KeyPair keyPair, String message) throws Exception {
	Signature sig = Signature.getInstance("SHA256WithRSA");
	sig.initSign(keyPair.getPrivate());
	byte[] msgBytes = message.getBytes("UTF-8");
	sig.update(msgBytes);
	byte[] signatureBytes = sig.sign();
	System.out.println("Singature:" + Base64.getEncoder().encode(signatureBytes));

	sig.initVerify(keyPair.getPublic());
	sig.update(msgBytes);

	return sig.verify(signatureBytes);
    }

    public static boolean verifySignature(PublicKey publicKey, byte data[], byte[] signature, String algorithm)
	    throws Exception {
	Signature sig = Signature.getInstance(algorithm);
	sig.initVerify(publicKey);
	sig.update(data);
	return sig.verify(signature);
    }

    public static String base64Encode(byte[] input) {
	return new String(Base64.getEncoder().encode(input), Charset.forName("UTF-8"));
    }

    public static byte[] base64EncodeToBytes(byte[] input) {
	return Base64.getEncoder().encode(input);
    }

    public static String base64Decode(byte[] input) {
	return new String(Base64.getDecoder().decode(input), Charset.forName("UTF-8"));
    }

    public static byte[] base64DecodeToBytes(byte[] input) {
	return Base64.getDecoder().decode(input);
    }

    public static byte[] base64Decode(String input) {
	return Base64.getDecoder().decode(input.getBytes(Charset.forName("UTF-8")));
    }

    private static String bytesToString(byte[] input) {
	StringBuffer retString = new StringBuffer();
	for (int i = 0; i < input.length; ++i) {
	    retString.append(Integer.toHexString(0x0100 + (input[i] & 0x00FF)).substring(1));
	}
	return retString.toString();
    }

    public static byte[] encrypt(Key key, String message) throws Exception {
	Cipher cipher = Cipher.getInstance("RSA");
	cipher.init(Cipher.ENCRYPT_MODE, key);

	return cipher.doFinal(message.getBytes("UTF-8"));
    }

    public static byte[] encrypt(Key key, byte[] data, String transformation) throws Exception {
	Cipher cipher = Cipher.getInstance(transformation);
	cipher.init(Cipher.ENCRYPT_MODE, key);

	return cipher.doFinal(data);
    }

    public static byte[] decrypt(Key key, byte[] encrypted) throws Exception {
	Cipher cipher = Cipher.getInstance("RSA");

	cipher.init(Cipher.DECRYPT_MODE, key);

	return cipher.doFinal(encrypted);
    }

    public static byte[] decrypt(Key key, byte[] encrypted, String transformation) throws Exception {
	Cipher cipher = Cipher.getInstance(transformation);

	cipher.init(Cipher.DECRYPT_MODE, key);

	return cipher.doFinal(encrypted);
    }
    
    public static SecretKey pbkdf2ToKey(String password, byte[] salt, int iterations) throws Exception {
	/* Derive the key, given password and salt. */
	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 256);
	SecretKey tmp = factory.generateSecret(spec);
	SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
	
	return secret;
    }
    
    public static String generate6DigitsPasscode() {
	int c = 100000 + (int) Math.ceil(Math.random() * 899999);
	return "" + c;
    }
}