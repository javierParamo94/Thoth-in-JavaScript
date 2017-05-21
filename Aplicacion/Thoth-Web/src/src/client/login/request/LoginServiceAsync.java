package src.client.login.request;

import src.shared.UserDto;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {

	void authenticate(String email, String password, AsyncCallback<UserDto> callback);
	void logout(AsyncCallback callback);
	void register(String name, String username, String email, String pass,  AsyncCallback<UserDto> callback);
}
