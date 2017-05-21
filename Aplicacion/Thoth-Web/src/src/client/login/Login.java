package src.client.login;

import src.client.login.request.LoginService;
import src.client.login.request.LoginServiceAsync;
import src.client.login.view.LoginView;
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
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Login implements EntryPoint {

	private LoginView viewLog = GWT.create(LoginView.class);
	private ThatAppShell viewReg = GWT.create(ThatAppShell.class);
	private boolean register = false;
	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	public void onModuleLoad() {

		showLogin();

	}

	public void showLogin() {

		SubmitHandler handler = new SubmitHandler();
		loginHandler handler2 = new loginHandler();

		viewReg.getSubmitButton().addClickHandler(handler);
		viewReg.getEmailBox().addKeyUpHandler(handler);
		/*
		 * viewReg.getEmailBox().addKeyUpHandler(handler);
		 * viewReg.getEmailBox().addKeyUpHandler(handler);
		 * viewReg.getEmailBox().addKeyUpHandler(handler);
		 */
		viewReg.getPasswordBox().addKeyUpHandler(handler);

		viewLog.getSubmitButton().addClickHandler(handler2);
		viewLog.getEmailBox().addKeyUpHandler(handler2);
		/*
		 * viewReg.getEmailBox().addKeyUpHandler(handler);
		 * viewReg.getEmailBox().addKeyUpHandler(handler);
		 * viewReg.getEmailBox().addKeyUpHandler(handler);
		 */
		viewLog.getPasswordBox().addKeyUpHandler(handler2);

		viewReg.getLoginLink().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				register = true;
				RootLayoutPanel.get().clear();
				RootLayoutPanel.get().add(viewLog);
			}
		});

		viewLog.getRegisterLink().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				register = true;
				RootLayoutPanel.get().clear();
				RootLayoutPanel.get().add(viewReg);
			}
		});
		register = false;
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(viewReg);
	}

	// Al pulsar el boton se hace la autenticacion
	class SubmitHandler implements ClickHandler, KeyUpHandler {
		public void onClick(ClickEvent event) {
			registrate();
		}

		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				registrate();
			}
		}
	}

	// Al pulsar el boton se hace la autenticacion
	class loginHandler implements ClickHandler, KeyUpHandler {
		public void onClick(ClickEvent event) {
			authenticate();
		}

		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				authenticate();
			}
		}
	}

	public void authenticate() {
		disableLogin();
		loginService.authenticate(viewLog.getEmailValue(),
				viewLog.getPasswordValue(), new AsyncCallback<UserDto>() {
					public void onFailure(Throwable caught) {
						viewLog.getMessage()
								.setWidget(
										new HTML(
												"Authentication failed. Please try again."));
						enableLogin();
					}

					public void onSuccess(UserDto user) {

						if (user == null) {
							// pantalla de registro
							//showLogin();
							enableLogin();
							viewReg.getMessage().setWidget(
									new HTML("Please, sign in "));
						} else {

								Window.Location.assign("/module_b/");

						}
					}
				});

	}

	public void registrate() {

		// disableLogin();
		loginService.register(viewReg.getNameValue(),
				viewReg.getLastNameValue(), viewReg.getEmailValue(),
				viewReg.getPasswordValue(), new AsyncCallback<UserDto>() {
					public void onFailure(Throwable caught) {
						viewReg.getMessage()
								.setWidget(
										new HTML(
												"Authentication failed. Please try again."));
						enableLogin();
					}

					public void onSuccess(UserDto user) {

						if (user == null) {
							// pantalla de login
							Window.alert(" Error, no funciono");
							showLogin();
						} else {
							viewReg.getMessage()
									.setWidget(
											new HTML(
													"Registration Success."));
						}
					}
				});

	}

	// helper to see if we are in module_b
	public boolean isModuleB() {
		return Window.Location.getHref().contains("/module_b/");
	}

	public void disableLogin() {
		viewReg.getMessage().clear();
		viewReg.getSubmitButton().setText("Checking...");
		viewReg.getSubmitButton().setEnabled(false);
		viewReg.getEmailBox().setEnabled(false);
		viewReg.getPasswordBox().setEnabled(false);
		viewReg.getLoginLink().setEnabled(false);
	}

	public void enableLogin() {
		//viewReg.getSubmitButton().setText("Login");
		viewReg.getSubmitButton().setEnabled(true);
		viewReg.getEmailBox().setEnabled(true);
		viewReg.getPasswordBox().setEnabled(true);
		viewReg.getLoginLink().setEnabled(true);
	}
}