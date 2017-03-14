package src.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import src.client.service.ExampleService;

public class ExamlpleServiceImpl extends RemoteServiceServlet implements ExampleService{

	@Override
	public String sayHello(String name) {
		String greeting = "Hello " + name ;
		return greeting;
	}

	@Override
	public int addNum(int num1, int num2) {
		int y = num1 + num2;
		return y;
	}

}
