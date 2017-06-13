package src.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import src.client.register.request.RegistrationService;
import src.shared.UserDto;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RegServlet extends RemoteServiceServlet implements
		RegistrationService {

	private static final int ITERATIONS = 1000;
	private static final int KEY_LENGTH = 128;
	// Clave con la que se generará una key única.
	private String password = "ThothPassword";
	private byte[] salt = new String("02062017").getBytes();
	private String db_user = null;

	public static DatastoreService ds = DatastoreServiceFactory
			.getDatastoreService();

	/*
	 * public UserDto getUser() { return (UserDto)
	 * getThreadLocalRequest().getSession().getAttribute( "current_user"); }
	 * 
	 * public void setUser(UserDto user) {
	 * getThreadLocalRequest().getSession().setAttribute( "current_user", user);
	 * 
	 * }
	 */
	public void userAction(String name, String email) {

		Entity userAction = new Entity("UserAction", db_user);
		Entity session = new Entity("Session", db_user);
		userAction.setProperty("Name", db_user);

		Calendar rightNow = Calendar.getInstance();
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH);
		int day = rightNow.get(Calendar.DATE);
		int hour = rightNow.get(Calendar.HOUR_OF_DAY);
		int minute = rightNow.get(Calendar.MINUTE);

		String lastAccess = hour + ":" + minute + " " + day + "/" + month + "/"
				+ year;
		userAction.setProperty("Last-Access", lastAccess);
		session.setProperty("Current-user", db_user);
		// userAction.setProperty("Grammar", tipo);
		ds.put(userAction);
	}

	@Override
	public UserDto authenticate(String email, String password) throws Exception {

		UserDto user2 = new UserDto();
		String db_email = null;
		String db_pass = null;
		String aux_key = null;
		String passDecrypted = null;
		SecretKeySpec secretKey;
		byte[] decodeKey;

		Query q = new Query("User");
		PreparedQuery pq = ds.prepare(q);
		for (Entity user : pq.asIterable()) {
			db_email = (String) user.getProperty("Email");
			db_pass = (String) user.getProperty("Password");
			aux_key = (String) user.getProperty("Salt");

			// Decodifico el salt a base 64
			decodeKey = base64Decode(aux_key);
			// paso de base 64 a tipo SecretKeySpec
			secretKey = new SecretKeySpec(decodeKey, "AES");
			// desencripto la clave por medio del salt guardado en la bd
			passDecrypted = decrypt(db_pass, secretKey);

			if (db_pass != null && password.equalsIgnoreCase(passDecrypted)
					&& db_email.equalsIgnoreCase(email)) {

				db_user = (String) user.getProperty("Name");
				userAction(db_user, db_email);

				storeUserInSession(db_user);
				System.out.println("USUARIO:  --> "
						+ getUserAlreadyFromSession());
				return user2;
			}
		}

		return user2 = null;
	}

	@Override
	public UserDto register(String name, String username, String email,
			String pass) throws Exception {

		SecretKeySpec key = createSecretKey(password.toCharArray(), salt,
				ITERATIONS, KEY_LENGTH);
		String encryptedPassword = encrypt(pass, key);

		String saltString = base64Encode(key.getEncoded());

		UserDto user2 = new UserDto();

		// Fecha de registro del usuario.
		Calendar rightNow = Calendar.getInstance();
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH);
		int day = rightNow.get(Calendar.DATE);
		int hour = rightNow.get(Calendar.HOUR_OF_DAY);
		int minute = rightNow.get(Calendar.MINUTE);

		String registerDate = hour + ":" + minute + " " + day + "/" + month
				+ "/" + year;

		Entity user = new Entity("User", email);

		if (isValidEmailAddress(email) && email.contains("@")) {

			user.setProperty("UserID", email);
			user.setProperty("Name", name);
			user.setProperty("RegisterDate", registerDate);
			user.setProperty("Password", encryptedPassword);
			user.setProperty("Salt", saltString);
			user.setProperty("Email", email);

			ds.put(user);

		} else {
			user2 = null;
		}
		return user2;
	}

	/**
	 * Funcion que verifica si el e-mail introducido es válido por medio del uso
	 * de java email package.
	 * 
	 * @param email
	 *            e-mail a comprobar
	 * @return boolean que determina si es válido o no.
	 */
	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	private static SecretKeySpec createSecretKey(char[] password, byte[] salt,
			int iterationCount, int keyLength) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		SecretKeyFactory keyFactory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA512");
		PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount,
				keyLength);
		SecretKey keyTmp = keyFactory.generateSecret(keySpec);
		return new SecretKeySpec(keyTmp.getEncoded(), "AES");
	}

	private static String encrypt(String property, SecretKeySpec key)
			throws GeneralSecurityException, UnsupportedEncodingException {
		Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		pbeCipher.init(Cipher.ENCRYPT_MODE, key);
		AlgorithmParameters parameters = pbeCipher.getParameters();
		IvParameterSpec ivParameterSpec = parameters
				.getParameterSpec(IvParameterSpec.class);
		byte[] cryptoText = pbeCipher.doFinal(property.getBytes("UTF-8"));
		byte[] iv = ivParameterSpec.getIV();
		return base64Encode(iv) + ":" + base64Encode(cryptoText);
	}

	private static String base64Encode(byte[] bytes) {
		return DatatypeConverter.printBase64Binary(bytes);
	}

	private static String decrypt(String string, SecretKeySpec key)
			throws GeneralSecurityException, IOException {
		String iv = string.split(":")[0];
		String property = string.split(":")[1];
		Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(
				base64Decode(iv)));
		return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
	}

	private static byte[] base64Decode(String property) throws IOException {
		return DatatypeConverter.parseBase64Binary(property);
	}

	
	 @Override 
	 public String loginFromSessionServer() { 
		 return	 getUserAlreadyFromSession(); 
	 }
	 

	@Override
	public void logout() {
		deleteUserFromSession();
	}

	protected String getUserAlreadyFromSession() {
		String user = null;
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		Object userObj = session.getAttribute("Current_user");
		if (userObj != null) {
			user = (String) userObj;
		}
		return user;
	}

	private void storeUserInSession(String user) {

		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();

		session.setAttribute("Current_user", user);
	}

	private void deleteUserFromSession() {
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		session.removeAttribute("Current_user");
	}
}
