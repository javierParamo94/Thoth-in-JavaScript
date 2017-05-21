package src.client.module_b.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Francisco Javier Páramo Arnaiz
 *
 */
public class ThatAppShell extends Composite {

	private static ThatAppShellUiBinder uiBinder = GWT
			.create(ThatAppShellUiBinder.class);

	interface ThatAppShellUiBinder extends UiBinder<Widget, ThatAppShell> {
	}

	/**
	 * 
	 */
	@UiField SimplePanel message;
	@UiField TextBox fName;
	@UiField TextBox lName;
	@UiField TextBox email;
	@UiField PasswordTextBox password;
	@UiField Button submit_button;
	@UiField Anchor login_link;
	
	/**
	 * 
	 */
	public ThatAppShell() {
		initWidget(uiBinder.createAndBindUi(this));
		submit_button.setText("Register");
		login_link.setText("login");
	}
	//método que devuelve el valor del email.
	public String getEmailValue(){
		return email.getValue();
	}
	//método get que devuelve el valor del nombre
	public String getNameValue(){
		return fName.getValue();
	}
	//método get que devuelve el valor del apellido
	public String getLastNameValue(){
		return lName.getValue();
	}
	//método get que devuelve el valor del la contraseña
	public String getPasswordValue(){
		return password.getValue();
	}
	//
	public Button getSubmitButton(){
		return submit_button;
	}
	//método get que devuelve el link al login
	public Anchor getLoginLink(){
		return login_link;
	}
	//
	public TextBox getEmailBox(){
		return email;
	}
	//
	public PasswordTextBox getPasswordBox(){
		return password;
	}
	//método get que devuelve el mensaje 
	public SimplePanel getMessage(){
		return message;
	}
}