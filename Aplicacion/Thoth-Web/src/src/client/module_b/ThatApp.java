package src.client.module_b;

import src.client.login.request.LoginService;
import src.client.login.request.LoginServiceAsync;
import src.client.module_b.view.ThatAppShell;
import src.shared.UserDto;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class ThatApp implements EntryPoint{
	ThatAppShell shell = GWT.create(ThatAppShell.class);
	LoginServiceAsync loginService = GWT.create(LoginService.class);

	public void onModuleLoad(){
		//SubmitHandler handler = new SubmitHandler();
		//shell.getSubmitButton().addClickHandler(handler);
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(shell);
		
		if (shell.getEmailValue()==null)
			shell.getMessage().setWidget(new HTML("Wrong! try Email: empty"));
		

		if (shell.getPasswordValue()==null)
			shell.getMessage().setWidget(new HTML("the passwords are differents"));


		}
		

		
	
}
