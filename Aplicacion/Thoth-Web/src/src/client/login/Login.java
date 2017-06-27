package src.client.login;

import src.client.login.view.LoginView;
import src.client.register.Registration;
import src.client.register.request.RegistrationService;
import src.client.register.request.RegistrationServiceAsync;
import src.shared.UserDto;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Cookies;
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

	private LoginView mViewLog = GWT.create(LoginView.class);
	private Registration mViewReg = GWT.create(Registration.class);
	private final RegistrationServiceAsync regService = GWT
			.create(RegistrationService.class);

	/**
	 * Función principal del módulo Login.
	 */
	public void onModuleLoad() {

		String sessionID = Cookies.getCookie("sid");
		if (sessionID == null) {
			showLogin();
		} else {
			checkSession(sessionID);
		}
	}//onModuleLoad

	/**
	 * Comprobar si la sesión del usuario esta activa o no.
	 * 
	 * @param sessionID
	 *            id de la sesión.
	 */
	private void checkSession(String sessionID) {
		regService.loginFromSessionServer(new AsyncCallback<Object>() {
			// En caso de fallo, muestra el login
			@Override
			public void onFailure(Throwable caught) {
				showLogin();
			}

			// En caso de exito, le redirige a editar una gramática.
			@Override
			public void onSuccess(Object result) {
				if (result == null) {
					showLogin();
				} else {
					Window.Location.assign("/gramaticacs/");
				}
			}
		});
	}//checkSession

	/**
	 * Muestra los alementos del login y del registro. Incluye los botones.
	 */
	public void showLogin() {

		SubmitHandler handlerReg = new SubmitHandler();
		LoginHandler handlerLog = new LoginHandler();

		mViewReg.getSubmitButton().addClickHandler(handlerReg);
		mViewReg.getEmailBox().addKeyUpHandler(handlerReg);
		mViewReg.getPasswordBox().addKeyUpHandler(handlerReg);

		mViewLog.getSubmitButton().addClickHandler(handlerLog);
		mViewLog.getEmailBox().addKeyUpHandler(handlerLog);
		mViewLog.getPasswordBox().addKeyUpHandler(handlerLog);

		/**
		 * Login
		 */
		mViewReg.getLoginLink().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				RootLayoutPanel.get().clear();
				RootLayoutPanel.get().add(mViewLog);
			}
		});

		/**
		 * Registro
		 */
		mViewLog.getRegisterLink().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				RootLayoutPanel.get().clear();
				RootLayoutPanel.get().add(mViewReg);
			}
		});
		// Por defecto login
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(mViewLog);
	}//showLogin

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
	}//SubmitHandler

	// Al pulsar el boton se hace la autenticacion
	class LoginHandler implements ClickHandler, KeyUpHandler {
		public void onClick(ClickEvent event) {
			authenticate();
		}

		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				authenticate();
			}
		}
	}//LoginHandler

	/**
	 * Autentificación. Comunica con el servidor.
	 */
	public void authenticate() {
		disableHandler();
		regService.authenticate(mViewLog.getEmailValue(),
				mViewLog.getPasswordValue(), new AsyncCallback<UserDto>() {
					//En caso de fallo, mensaje de error.
					public void onFailure(Throwable caught) {
						mViewLog.getMessage()
								.setWidget(
										new HTML(
												"Authentication failed. Please try again."));
						enableHandler();
					}
					//En caso de exito, vista de gramática.
					public void onSuccess(UserDto user) {
						if (user != null) {
							Window.Location.assign("/gramaticacs/");
						} else
							Window.alert("Access Denied. Check your user-name and password.");
						enableHandler();
					}
				});
	}//authenticate
	
	/**
	 * Registro del usuario, se comunica con el servidor.
	 */
	public void registrate() {

		disableHandler();
		regService.register(mViewReg.getNameValue(), mViewReg.getEmailValue(),
				mViewReg.getPasswordValue(), new AsyncCallback<UserDto>() {
					//Fallo, mensaje de error.
					public void onFailure(Throwable caught) {
						mViewReg.getMessage()
								.setWidget(
										new HTML(
												"Authentication failed. Please try again."));
						enableHandler();
					}
					//Exito
					public void onSuccess(UserDto user) {
						//Comprueba que el registro sea correcto.
						if (user == null) {
							// pantalla de login
							mViewReg.getMessage().setWidget(
									new HTML("Wrong field"));
							enableHandler();
						} else {
							//Exito en el registro
							mViewReg.getMessage().setWidget(
									new HTML("Registration Success."));
							enableHandler();
						}
					}
				});
	}//registrate

	/**
	 * Desactivar los botones.
	 */
	public void disableHandler() {
		mViewReg.getMessage().clear();
		mViewLog.getMessage().clear();
		mViewReg.getSubmitButton().setText("Checking...");
		mViewLog.getSubmitButton().setText("Checking...");
		mViewReg.getSubmitButton().setEnabled(false);
		mViewLog.getSubmitButton().setEnabled(false);
		mViewReg.getEmailBox().setEnabled(false);
		mViewLog.getEmailBox().setEnabled(false);
		mViewReg.getPasswordBox().setEnabled(false);
		mViewLog.getPasswordBox().setEnabled(false);
		mViewReg.getLoginLink().setEnabled(false);
		mViewLog.getRegisterLink().setEnabled(false);
	}//disableHandler

	/**
	 * Habilitar botones
	 */
	public void enableHandler() {
		mViewLog.getSubmitButton().setText("Login");
		mViewReg.getSubmitButton().setText("Register");

		mViewReg.getSubmitButton().setEnabled(true);
		mViewReg.getEmailBox().setEnabled(true);
		mViewReg.getPasswordBox().setEnabled(true);
		mViewReg.getLoginLink().setEnabled(true);

		mViewLog.getSubmitButton().setEnabled(true);
		mViewLog.getEmailBox().setEnabled(true);
		mViewLog.getPasswordBox().setEnabled(true);
		mViewLog.getRegisterLink().setEnabled(true);
	}//enableHandler
}//Login