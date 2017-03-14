package src.client.service;

import src.client.gui.mainGui;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;


public class ExampleServiceClientImpl   implements ExampleServiceInt{

	private ExampleServiceAsync service;
	private mainGui mangui;
	
	public ExampleServiceClientImpl(String url) {
		System.out.println(url);
		this.service = GWT.create(ExampleService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) this.service;
		endpoint.setServiceEntryPoint(url);
		
		this.mangui = new mainGui(this);
		
	}
	
	@Override
	public void sayHello(String name) {
		this.service.sayHello(name, new DefaultCallback());
	}

	@Override
	public void addNum(int num1, int num2) {
		this.service.addNum(num1, num2, new DefaultCallback());
		
	}
	
	public mainGui getManGUI(){
		return this.mangui;
	}

	private class DefaultCallback implements AsyncCallback {

		@Override
		public void onFailure(Throwable caught) {
			System.out.println("Ha ocurrido un error");
			
		}

		@Override
		public void onSuccess(Object result) {
			if (result instanceof String){
				String greeting = (String) result;
				mangui.updateLabel(greeting);
			}
			
		}
		
		
	}
	
}
