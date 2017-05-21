package src.client.login.request;

import src.shared.UserDto;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("loginservice")
public interface LoginService extends RemoteService{

	UserDto authenticate(String email, String password);
	void logout();
	UserDto register(String name, String username, String email,String pass);
}
