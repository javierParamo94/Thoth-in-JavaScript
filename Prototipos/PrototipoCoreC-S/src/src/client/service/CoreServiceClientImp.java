package src.client.service;

import src.client.gui.mainGui;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;


public class CoreServiceClientImp implements CoreServiceInt{
	
	
	private CoreServiceAsync service;
	private mainGui mangui;

	public CoreServiceClientImp (String url){
		System.out.println(url);
		this.service = GWT.create(CoreService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) this.service;
		endpoint.setServiceEntryPoint(url);
		
		this.mangui = new mainGui(this);
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
				String saludo = (String) result;
				mangui.updateLabel(saludo);
			}	
		}
	}


	@Override
	public void diHola(String name) {
		this.service.diHola(name, new DefaultCallback());
	}
	
	
	
}
