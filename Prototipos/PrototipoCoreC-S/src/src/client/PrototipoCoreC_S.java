package src.client;


import src.client.service.CoreServiceClientImp;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class PrototipoCoreC_S implements EntryPoint {

	public void onModuleLoad() {
		CoreServiceClientImp clientImp = new CoreServiceClientImp(GWT.getModuleBaseURL() + "coreservice");
		RootPanel.get().add(clientImp.getManGUI());
	}
}
