package src.server;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;

import src.client.login.request.LoginService;
import src.shared.UserDto;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AdvancedServlet extends RemoteServiceServlet implements
		LoginService {
	public static DatastoreService ds = DatastoreServiceFactory
			.getDatastoreService();
	public Key k;

	public UserDto getUser() {
		return (UserDto) getThreadLocalRequest().getSession().getAttribute(
				"current_user");
	}

	public void setUser(UserDto user) {
		getThreadLocalRequest().getSession();// .setAttribute("current_user",
												// user);

	}


	@Override
	public UserDto authenticate(String email, String password) {

		UserDto user2 = new UserDto();
		String mail = null;
		String pass = null;

		Query q = new Query("User");
		PreparedQuery pq = ds.prepare(q);
		for (Entity user : pq.asIterable()) {
			mail = (String) user.getProperty("Email");
			pass = (String) user.getProperty("Password");

			if (pass != null && pass.equalsIgnoreCase(password)
					&& mail.equalsIgnoreCase(email))
				return user2;
		}
		return user2 = null;
	}

	@Override
	public UserDto register(String name, String username, String email,
			String pass) {

		UserDto user2 = new UserDto();
		Entity user = new Entity("User", email);

		if (email.contains("@")) {

			user.setProperty("UserID", email);
			user.setProperty("Name", name);
			user.setProperty("LastName", username);
			user.setProperty("Password", pass);
			user.setProperty("Email", email);

			k = ds.put(user);

		} else {
			user2 = null;// force showLogin
		}
		return user2;
	}

	@Override
	public void logout() {
		setUser(null);

	}
}
