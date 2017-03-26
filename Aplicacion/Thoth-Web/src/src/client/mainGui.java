package src.client;

import src.client.GrammarServiceClientImp;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class mainGui extends Composite{
	private VerticalPanel vPanel = new VerticalPanel();
	private TextBox txt1, txt2, txt3, txt4, txt5;
	private Label result;
	private TextArea ta;

	private GrammarServiceClientImp serviceImp;
	
	public mainGui(GrammarServiceClientImp  serviceImp){
		initWidget(this.vPanel);
		this.serviceImp = serviceImp;
		
		this.ta = new TextArea();
	    ta.setCharacterWidth(80);
	    ta.setVisibleLines(20);
		this.txt1 = new TextBox();
		this.txt2 = new TextBox();
		this.txt3 = new TextBox();
		this.txt4 = new TextBox();
		this.txt5 = new TextBox();
		
		
		this.vPanel.add(ta);
	    this.vPanel.add(txt1);
	    this.vPanel.add(txt2);
	    this.vPanel.add(txt3);
	    this.vPanel.add(txt4);
	    this.vPanel.add(txt5);
		
		
	    Image img = new Image("checkGrammar.png");
		Button btn1 = new Button();
		btn1.getElement().appendChild(img.getElement());
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
			//String name = txt1.getText();
			String name = ta.getText();
//			serviceImp.diHola(name);
			System.out.println ("Click...");
			serviceImp.checkContent(name);
		}
		
	}
}
