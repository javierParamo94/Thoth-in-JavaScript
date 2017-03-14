package src.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExampleServiceAsync {

	void sayHello (String name, AsyncCallback calback);
	void addNum (int num1, int num2, AsyncCallback callback);
}
