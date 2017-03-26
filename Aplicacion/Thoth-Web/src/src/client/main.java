package src.client;

import src.client.GrammarServiceClientImp;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class main implements EntryPoint {

	public void onModuleLoad() {

		GrammarServiceClientImp clientImp = new GrammarServiceClientImp (GWT.getModuleBaseURL() + "grammarservice");
		RootPanel.get().add(clientImp.getManGUI());

	}
	
}
