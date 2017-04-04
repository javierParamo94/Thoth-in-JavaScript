package src.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.Grammar;
import src.client.core.grammar.TypeHandler;
import view.application.Application;
import view.grammar.PanelGrammar;
import view.grammar.visual.VisualSNT;
import view.utils.ShowDialog;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI;

//import src.client.algorithms.eliminatenondeterministic.*;

public class mainGui extends Composite{
    private VerticalPanel vPanel = new VerticalPanel();
	private VerticalPanel vPanel1 = new VerticalPanel();
	private VerticalPanel vPanel2 = new VerticalPanel();
	private VerticalPanel vPanel3 = new VerticalPanel();
	private HorizontalPanel hPanel = new HorizontalPanel();
	private HorizontalPanel hPanel3 = new HorizontalPanel();
	private TextBox txt1, txt2, txt3, txt4, txt5;
	private FlexTable stocksFlexTable = new FlexTable();

	DockPanel dockPanel = new DockPanel();
    
	private TextArea ta;
    public Grammar mGrammar;
	
	private GrammarServiceClientImp serviceImp;
	private MyComposite currentPage;
	
	public mainGui(GrammarServiceClientImp  serviceImp){

	    dockPanel.setStyleName("dockpanel");
	    dockPanel.setSpacing(4);
	    dockPanel.setHorizontalAlignment(DockPanel.ALIGN_RIGHT);

	    Command cmd = new Command() {
	        public void execute() {
	          Window.alert("You selected a menu item!");
	        }
	      };
			


	      Command elimnarSimbolosNoTerminable = new Command() {
		        public void execute() {
		        	/*PanelGrammar panel = ((PanelGrammar) Application.getInstance().getCurrentTab());
	                
	                if(panel.checkContent())
	                    if(panel.mGrammar.getType() == TypeHandler.CHOMSKY ||
	                            panel.mGrammar.getType() == TypeHandler.DEPENDENT)
	                        ShowDialog.incorrectTypeGrammar();
	                    else
	                        new VisualSNT(Application.getInstance(), panel.mGrammar);
	           // }*/
		        	openElimNonDeterministic(Grammar grammar);
		        }
		      };
	    // Make some sub-menus that we will cascade from the top menu.
	    MenuBar fooMenu = new MenuBar(true);
	    fooMenu.addItem("ejemplo1", cmd);
	    fooMenu.addItem("ejemplo2", cmd);
	    fooMenu.addItem("ejemplo3", cmd);

	    MenuBar gramaticaMenu = new MenuBar(true);
	    gramaticaMenu.addItem("Comprobar", cmd);
	    gramaticaMenu.addItem("Renombrar símbolo", cmd);

	    MenuBar algorithmMenu = new MenuBar(true);
	    algorithmMenu.addItem("Eliminar símbolos no terminables.", elimnarSimbolosNoTerminable);
	    algorithmMenu.addItem("Eliminar símbolos no alcanzables.", cmd);
	    algorithmMenu.addItem("Eliminar simbolos anulables.", cmd);
	    algorithmMenu.addItem("Eliminar simbolos anulables.", cmd);
	    
	    MenuBar toolsMenu = new MenuBar(true);
	    toolsMenu.addItem("Idioma.", cmd);
	    toolsMenu.addItem("Preferencias..", cmd);


	    MenuBar menu = new MenuBar();
	    menu.addItem("Archivo", fooMenu);
	    menu.addItem("Editar", fooMenu);
	    menu.addItem("Gramática", gramaticaMenu);
	    menu.addItem("Algoritmos", algorithmMenu);
	    menu.addItem("Herramientas", fooMenu);
	    menu.addItem("Ayuda", fooMenu);

		this.serviceImp = serviceImp;
		
		this.ta = new TextArea();
	    ta.setCharacterWidth(80);
	    ta.setVisibleLines(20);
	    
	    stocksFlexTable.setText(0, 0, "Tipo de Gramática");
	    stocksFlexTable.setText(1, 0, "Número de producciones");
	    stocksFlexTable.setText(2, 0, "Axiomas");
	    stocksFlexTable.setText(3, 0, "Terminales");
	    stocksFlexTable.setText(4, 0, "No terminales");
	    
	    stocksFlexTable.getRowFormatter().addStyleName(0, "header");
	    stocksFlexTable.getRowFormatter().addStyleName(1, "header");
	    stocksFlexTable.getRowFormatter().addStyleName(2, "header");
	    stocksFlexTable.getRowFormatter().addStyleName(3, "header");
	    stocksFlexTable.getRowFormatter().addStyleName(4, "header");
	    stocksFlexTable.getCellSpacing();
	    
	    this.txt1 = new TextBox();
		this.txt2 = new TextBox();
		this.txt3 = new TextBox();
		this.txt4 = new TextBox();
		this.txt5 = new TextBox();
		
		ta.setReadOnly(false);
		txt1.setReadOnly(true);
		txt2.setReadOnly(true);	
		txt3.setReadOnly(true);	
		txt4.setReadOnly(true);	
		txt5.setReadOnly(true);	
		
		this.vPanel1.add(ta);
		//this.vPanel.setCellHorizontalAlignment(ta, HasHorizontalAlignment.ALIGN_CENTER);
		
		this.vPanel3.add(txt1);
	    this.vPanel3.add(txt2);
	    this.vPanel3.add(txt3);
	    this.vPanel3.add(txt4);
	    this.vPanel3.add(txt5);


		PushButton btn1 = new PushButton(new Image("images/checkGrammar.png"));
		PushButton btn2 = new PushButton(new Image("images/renameSymbol.png"));
		//PushButton btn3 = new PushButton(new Image("images/grammar.png"));
	        	
	
		btn1.addClickHandler(new Btn1ClickHandler()); 
		this.vPanel2.add(btn1);
		this.vPanel2.add(btn2);
		 
		txt1.setText("Tipo de Gramática");
		txt2.setText("Número de producciones");
		txt3.setText("Axiomas");
		txt4.setText("Terminales");
		txt5.setText("No terminales");
		
		//hPanel.add(btn3);
		this.hPanel3.add(stocksFlexTable);
		hPanel3.add(vPanel3);
		

		dockPanel.add(new HTML("Definición de Gramática."), DockPanel.NORTH);
		dockPanel.add(hPanel3, DockPanel.SOUTH);
		dockPanel.add(vPanel1, DockPanel.EAST);
		dockPanel.add(vPanel2, DockPanel.WEST);
		//dockPanel.add(new HTML("center"));

		hPanel.add(menu);
	    vPanel.add(dockPanel);

	    // Add the widgets to the root panel.
	    RootPanel.get().add(hPanel);
	    RootPanel.get().add(vPanel);
	}

	public void updateLabel (String gramatica){
		txt1.setText(gramatica);
	}

			      
	private class Btn1ClickHandler implements ClickHandler{

		@Override
		public void onClick(ClickEvent event) {
			String textcheck = ta.getText();
			serviceImp.checkContent(textcheck);
		}
		
	}
	
	 public void updateLabel (Grammar grammar) {
	        String term = grammar.getTerminals().toString(),
	               noTerm = grammar.getNonTerminals().toString();
	        
	        term = term.substring(1, term.length()-1);
	        noTerm = noTerm.substring(1, noTerm.length()-1);
	        
	        mGrammar = grammar;
	        txt2.setText(((Integer)grammar.getProductions().size()).toString());
	        txt3.setText(grammar.getAxiom().toString());
	        txt4.setText(term);
	        txt5.setText(noTerm);
	        switch(grammar.getType()){
	            case TypeHandler.CHOMSKY:
	                txt1.setText("Chomsky");
	                break;
	            case TypeHandler.DEPENDENT:
	                txt1.setText("dependent");
	                break;                
	            case TypeHandler.INDEPENDENT:
	                txt1.setText("INDEPENDENT");
	                break;
	            case TypeHandler.REGULAR:
	                txt1.setText("REGULAR");
	                break;
	        }
	        
	    }
		public void openElimNonDeterministic(Grammar grammar) {
			this.vPanel.clear();

			this.currentPage = new VisualElimNonDeterministic();
			this.vPanel.add(this.currentPage);
			//p.addEast(this.currentPage, 100);
			//this.vPanel.setCellHorizontalAlignment(this.currentPage, HasHorizontalAlignment.ALIGN_CENTER);
		}
}
