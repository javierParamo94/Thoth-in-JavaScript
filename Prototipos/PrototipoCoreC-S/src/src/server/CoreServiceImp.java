package src.server;

import src.client.service.CoreService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class CoreServiceImp extends RemoteServiceServlet implements CoreService{

	@Override
	public String diHola(String name) {
		String saludo;
		saludo = "Hola " + name;
		return saludo;
	}

}
