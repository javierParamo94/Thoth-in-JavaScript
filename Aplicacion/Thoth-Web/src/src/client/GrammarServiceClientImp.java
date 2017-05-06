package src.client;

import src.client.core.grammar.Grammar;
import src.client.gui.mainGui;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class GrammarServiceClientImp {

	
	
	private GrammarServiceAsync service;
	private mainGui mainGUI;

	/**
	 * 
	 * @param url
	 */
	public GrammarServiceClientImp (String url){
		System.out.println(url);
		this.service = GWT.create(GrammarService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) this.service;
		endpoint.setServiceEntryPoint(url);
		
		this.mainGUI = new mainGui(this);
	}
	
	/**
	 * 
	 * @param url
	 * @param grammar
	 */
	public GrammarServiceClientImp (String url, Grammar grammar){
		System.out.println(url);
		this.service = GWT.create(GrammarService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) this.service;
		endpoint.setServiceEntryPoint(url);
		
		this.mainGUI = new mainGui(this, grammar);
	}
	
	public mainGui getManGUI(){
		return this.mainGUI;
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
				//mangui.updateLabel(saludo);
			}	
		}
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
					Grammar grammar = ((Grammar)result);
					mainGUI.updateLabel(grammar);
				}
				
				if (result instanceof Exception) {
					//mangui.updateLabel(((Exception)result).getMessage());
				}
			}
			
		});
	}
	
}

