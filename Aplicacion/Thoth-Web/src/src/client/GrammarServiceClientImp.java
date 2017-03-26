package src.client;

import src.client.core.grammar.Grammar;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class GrammarServiceClientImp implements GrammarServicetInt{

	
	
	private GrammarServiceAsync service;
	private mainGui mangui;

	public GrammarServiceClientImp (String url){
		System.out.println(url);
		this.service = GWT.create(GrammarService.class);
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
	
	public void checkContent(String grammar){
		this.service.checkContent(grammar, new AsyncCallback(){

			@Override
			public void onFailure(Throwable caught) {
				System.out.println("Ha ocurrido un error");
				
			}

			@Override
			public void onSuccess(Object result) {
				System.out.println ("La llamada fue exitosa y devolvió: " + result);
				if (result instanceof Grammar){
					String grammar = ((Grammar)result).toString();
					mangui.updateLabel(grammar);
				}
				
				if (result instanceof Exception) {
					mangui.updateLabel(((Exception)result).getMessage());
				}
			}
			
		});
	}
	
}

