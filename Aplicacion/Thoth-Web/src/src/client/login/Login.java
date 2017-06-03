package src.client.login;

import src.client.login.request.LoginService;
import src.client.login.request.LoginServiceAsync;
import src.client.login.view.LoginView;
import src.client.register.Registration;
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

/**
 * 
 * @author User
 *
 */
public class Login implements EntryPoint {

	private LoginView viewLog = GWT.create(LoginView.class);
	private Registration viewReg = GWT.create(Registration.class);
	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	/**
	 * 
	 */
	public void onModuleLoad() {

		showLogin();

	}

	public void showLogin() {

		SubmitHandler handler = new SubmitHandler();
		loginHandler handler2 = new loginHandler();

		viewReg.getSubmitButton().addClickHandler(handler);
		viewReg.getEmailBox().addKeyUpHandler(handler);
		viewReg.getPasswordBox().addKeyUpHandler(handler);

		viewLog.getSubmitButton().addClickHandler(handler2);
		viewLog.getEmailBox().addKeyUpHandler(handler2);
		viewLog.getPasswordBox().addKeyUpHandler(handler2);

		viewReg.getLoginLink().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				RootLayoutPanel.get().clear();
				RootLayoutPanel.get().add(viewLog);
			}
		});

		viewLog.getRegisterLink().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				RootLayoutPanel.get().clear();
				RootLayoutPanel.get().add(viewReg);
			}
		});
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(viewLog);
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
		disableHandler();
		loginService.authenticate(viewLog.getEmailValue(),
				viewLog.getPasswordValue(), new AsyncCallback<UserDto>() {

					public void onFailure(Throwable caught) {
						viewLog.getMessage()
								.setWidget(
										new HTML(
												"Authentication failed. Please try again."));
						enableHandler();
					}

					public void onSuccess(UserDto user) {

						if (user == null) {
							// pantalla de registro
							// showLogin();
							enableHandler();
							viewReg.getMessage().setWidget(
									new HTML("Tu correo electrónico o tu contraseña no son correctos."));//Please, sign in "));
						} else {

							Window.Location.assign("/gramaticacs/");

						}
					}
				});

	}

	public void registrate() {

		disableHandler();
		loginService.register(viewReg.getNameValue(),
				viewReg.getLastNameValue(), viewReg.getEmailValue(),
				viewReg.getPasswordValue(), new AsyncCallback<UserDto>() {
					public void onFailure(Throwable caught) {
						viewReg.getMessage()
								.setWidget(
										new HTML(
												"Authentication failed. Please try again."));
						enableHandler();
					}

					public void onSuccess(UserDto user) {

						if (user == null) {
							// pantalla de login
							Window.alert("Error, el e-mail no es correcto");
							showLogin();
							enableHandler();
						} else {
							viewReg.getMessage().setWidget(
									new HTML("Registration Success."));
							enableHandler();
						}
					}
				});

	}
	/**
	 * 
	 */
	public void disableHandler() {
		viewReg.getMessage().clear();
		viewLog.getMessage().clear();
		viewReg.getSubmitButton().setText("Checking...");
		viewLog.getSubmitButton().setText("Checking...");
		viewReg.getSubmitButton().setEnabled(false);
		viewLog.getSubmitButton().setEnabled(false);
		viewReg.getEmailBox().setEnabled(false);
		viewLog.getEmailBox().setEnabled(false);
		viewReg.getPasswordBox().setEnabled(false);
		viewLog.getPasswordBox().setEnabled(false);
		viewReg.getLoginLink().setEnabled(false);
		viewLog.getRegisterLink().setEnabled(false);
	}

	/**
	 * 
	 * @param login
	 */
	public void enableHandler() {
		viewLog.getSubmitButton().setText("Login");
		viewReg.getSubmitButton().setText("Register");
		
		viewReg.getSubmitButton().setEnabled(true);
		viewReg.getEmailBox().setEnabled(true);
		viewReg.getPasswordBox().setEnabled(true);
		viewReg.getLoginLink().setEnabled(true);
		
		viewLog.getSubmitButton().setEnabled(true);
		viewLog.getEmailBox().setEnabled(true);
		viewLog.getPasswordBox().setEnabled(true);
		viewLog.getRegisterLink().setEnabled(true);
	}
}