package src.client.register.request;

import src.shared.UserDto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("registerservice")
public interface RegistrationService extends RemoteService{
	
	String loginFromSessionServer();
	UserDto authenticate(String email, String password) throws Exception;
	void logout();
	UserDto register(String name, String username, String email,String pass) throws Exception;
	}
