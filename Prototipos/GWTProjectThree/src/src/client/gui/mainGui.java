package src.client.gui;

import src.client.service.ExampleServiceClientImpl;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class mainGui extends Composite {

	private VerticalPanel vPanel = new VerticalPanel();
	private TextBox txt1;
	private Label result;
	
	private ExampleServiceClientImpl serviceImpl;
	
	public mainGui(ExampleServiceClientImpl serviceImpl){
		initWidget(this.vPanel);
		this.serviceImpl = serviceImpl;
		
		this.txt1 = new TextBox();
		this.vPanel.add(txt1);
		
		Button btn1 = new Button("Say hello");
		btn1.addClickHandler(new Btn1ClickHandler());
		this.vPanel.add(btn1);
		
		this.result = new Label ("Result"); 
		this.vPanel.add(result);
		
	}
	
	public void updateLabel (String greeting){
		this.result.setText(greeting);
	}
	
	private class Btn1ClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			String name = txt1.getText();
			serviceImpl.sayHello(name);
			
		}
		
	}
}
