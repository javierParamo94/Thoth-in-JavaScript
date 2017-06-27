package src.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	/**
	 * Numero de versión que posee la clase.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Variable para almacenar el usuario de la base de datos
	 */
	private String db_user = null;
	private static MessageDigest md;
	public static DatastoreService ds = DatastoreServiceFactory
			.getDatastoreService();

	/**
	 * 
	 * @param name
	 * @param email
	 */
	public void userAction(String name, String email) {

		Entity userAction = new Entity("UserAction", db_user);
		Entity session = new Entity("Session", db_user);
		userAction.setProperty("Name", db_user);

		String lastAccess = actualDate();
		userAction.setProperty("Last-Access", lastAccess);
		session.setProperty("Current-user", db_user);
		// userAction.setProperty("Grammar", tipo);
		ds.put(userAction);
	}

	/**
	 * 
	 */
	@Override
	public UserDto authenticate(String email, String password) throws Exception {

		UserDto user2 = new UserDto();
		String db_email = null;
		String db_pass = null;
		String aux_key = null;

		Query q = new Query("User");
		PreparedQuery pq = ds.prepare(q);
		for (Entity user : pq.asIterable()) {
			db_email = (String) user.getProperty("Email");
			db_pass = (String) user.getProperty("Password");
			aux_key = (String) user.getProperty("Salt");

			String encryptedPassword = cryptWithMD5(aux_key + password);

			if (db_pass != null && db_pass.equalsIgnoreCase(encryptedPassword)
					&& db_email.equalsIgnoreCase(email)) {

				db_user = (String) user.getProperty("Name");
				userAction(db_user, db_email);

				storeUserInSession(db_user);

				return user2;
			}
		}
		return user2 = null;
	}//authenticate

	@Override
	public UserDto register(String name, String email,
			String pass) throws Exception {

		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);

		String salt = base64Encode(bytes);
		//Encriptado
		String encryptedPassword = cryptWithMD5(salt + pass);
		// Fecha de registro del usuario.
		String registerDate = actualDate();
		
		UserDto userCorrect = new UserDto();

		Entity user = new Entity("User", email);

		if (isValidEmailAddress(email) && !name.isEmpty() && !pass.isEmpty()) {

			user.setProperty("UserID", email);
			user.setProperty("Name", name);
			user.setProperty("RegisterDate", registerDate);
			user.setProperty("Password", encryptedPassword);
			user.setProperty("Salt", salt);
			user.setProperty("Email", email);

			ds.put(user);

		} else {
			userCorrect = null;
		}
		return userCorrect;
	}//register

	/**
	 * Encriptado de la contraseña.
	 * 
	 * @param pass contaseña
	 * @return contraseña cifrada
	 */
	public static String cryptWithMD5(String pass) {
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] passBytes = pass.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digested.length; i++) {
				sb.append(Integer.toHexString(0xff & digested[i]));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(RegServlet.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return null;
	}//cryptWithMD5

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
			if (email.contains("@")){
				InternetAddress emailAddr = new InternetAddress(email);
				emailAddr.validate();
			}
			else result = false;
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}//isValidEmailAddress

	/**
	 * Paso de bytes a string.
	 * Ayuda en el cifrado
	 * 
	 * @param bytes salt a introducir
	 * @return salt en formato String
	 */
	private static String base64Encode(byte[] bytes) {
		return DatatypeConverter.printBase64Binary(bytes);
	}

	/**
	 * Función que se comunica con el cliente.
	 */
	@Override
	public String loginFromSessionServer() {
		return getUserAlreadyFromSession();
	}//loginFromSessionServer

	@Override
	public void logout() {
		deleteUserFromSession();
	}
	/**
	 * Obtener el nombre del usuario actual.
	 * 
	 * @return usuario en sesión
	 */
	public String getUserAlreadyFromSession() {
		String user = null;
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		Object userObj = session.getAttribute("Current_user");
		if (userObj != null) {
			user = (String) userObj;
		}
		return user;
	}//getUserAlreadyFromSession

	/**
	 * Establece una sesión del usuario.
	 * 
	 * @param user usuario.
	 */
	private void storeUserInSession(String user) {

		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();

		session.setAttribute("Current_user", user);
	}//storeUserInSession

	/**
	 * Borra la sesión actual.
	 */
	private void deleteUserFromSession() {

		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		session.removeAttribute("Current_user");
	}//deleteUserFromSession
	
	/**
	 * Registro de las gramáticas que se utilizan
	 */
	@Override
	public void grammarReg(String grammatica) {
		Entity grammarsUsed = new Entity("GrammarUsed", grammatica);
		grammarsUsed.setProperty("Name", db_user);
		grammarsUsed.setProperty("Grammar", grammatica);

		String grammarTime = actualDate();
		grammarsUsed.setProperty("Date_of_Grammar", grammarTime);
		ds.put(grammarsUsed);
	}//grammarReg
	
	/**
	 * Obtener la fecha hora actual
	 * 
	 * @return fecha y hora actual
	 */
	public String actualDate(){
		Calendar rightNow = Calendar.getInstance();
		int year = rightNow.get(Calendar.YEAR);
		int month = rightNow.get(Calendar.MONTH);
		int day = rightNow.get(Calendar.DATE);
		int hour = rightNow.get(Calendar.HOUR_OF_DAY);
		int minute = rightNow.get(Calendar.MINUTE);
		
		String date = hour + ":" + minute + " " + day + "/" + month + "/"
				+ year; 
		return date;
	}//actualDate
}//RegServlet
