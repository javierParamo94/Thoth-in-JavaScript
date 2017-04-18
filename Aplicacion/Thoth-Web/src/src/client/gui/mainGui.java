package src.client.gui;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.Grammar;
import src.client.core.grammar.TypeHandler;
import src.client.gui.mediator.MediatorRecursive;
import src.client.gui.utils.ShowDialog;
import src.client.gui.visual.VisualChomsky;
import src.client.gui.visual.VisualDirectRecursive;
import src.client.gui.visual.VisualIndirectRecursive;
import src.client.gui.visual.VisualLeftFactoring;
import src.client.gui.visual.VisualPNG;
import src.client.gui.visual.VisualSA;
import src.client.gui.visual.VisualSNA;
import src.client.gui.visual.VisualSNT;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;

public class mainGui extends Composite {

	private VerticalPanel vPanel = new VerticalPanel();
	private VerticalPanel vPanel1 = new VerticalPanel();
	private VerticalPanel vPanel2 = new VerticalPanel();
	private VerticalPanel vPanel3 = new VerticalPanel();
	private VerticalPanel vPanel4 = new VerticalPanel();
	private HorizontalPanel hPanel = new HorizontalPanel();
	private HorizontalPanel hPanel3 = new HorizontalPanel();
	private TextBox txt1, txt2, txt3, txt4, txt5;
	private FlexTable stocksFlexTable = new FlexTable();
	
	private TabBar bar = new TabBar();
	
	TabPanel tabPanel = new TabPanel();
	
	DockPanel dockPanel = new DockPanel();

	private TextArea ta;
	public Grammar mGrammar;

	private GrammarServiceClientImp serviceImp;
	private Composite currentPage;

	public mainGui(GrammarServiceClientImp serviceImp) {

		  Label label1 = new Label("This is contents of TAB 1");
	      label1.setHeight("200");
	      Label label2 = new Label("This is contents of TAB 2");
	      label2.setHeight("200");
	      Label label3 = new Label("This is contents of TAB 3");
	      label3.setHeight("200");
	      
	      //create titles for tabs
	      String tab1Title = "TAB 1";
	      String tab2Title = "TAB 2";
	      String tab3Title = "TAB 3";

	      //create tabs 
	      tabPanel.add(vPanel4, tab1Title);
	      tabPanel.add(label2, tab2Title);
	      tabPanel.add(label3, tab3Title);
	      //tabPanel.add(vPanel4,);

	      //select first tab
	      tabPanel.selectTab(0);

	      //set width if tabpanel
	      tabPanel.setWidth("400");

	      // Add the widgets to the root panel.
	     
	      
		buildMenuBar();
		buildBar();
		buildPanel(serviceImp);
		

		// Add the widgets to the root panel.
		vPanel4.add(hPanel);
		vPanel4.add(vPanel);
		RootPanel.get().add(tabPanel);
		//RootPanel.get().add(hPanel);
		//RootPanel.get().add(bar);
		//RootPanel.get().add(vPanel);
	}

	private void buildPanel(GrammarServiceClientImp serviceImp) {

		this.serviceImp = serviceImp;

		DockPanel dockPanel = new DockPanel();
		dockPanel.setStyleName("dockpanel");
		dockPanel.setSpacing(4);
		dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		dockPanel.setVerticalAlignment(DockPanel.ALIGN_TOP);


		ta = new TextArea();
		ta.setCharacterWidth(80);
		ta.setVisibleLines(20);
		ta.setReadOnly(false);

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

		txt1 = new TextBox();
		txt2 = new TextBox();
		txt3 = new TextBox();
		txt4 = new TextBox();
		txt5 = new TextBox();

		txt1.setReadOnly(true);
		txt2.setReadOnly(true);
		txt3.setReadOnly(true);
		txt4.setReadOnly(true);
		txt5.setReadOnly(true);
		txt1.setSize("250px", "20px");

		this.vPanel1.add(ta);
		this.vPanel3.add(txt1);
		this.vPanel3.add(txt2);
		this.vPanel3.add(txt3);
		this.vPanel3.add(txt4);
		this.vPanel3.add(txt5);

		PushButton btn1 = new PushButton(new Image("images/checkGrammar.png"));
		PushButton btn2 = new PushButton(new Image("images/renameSymbol.png"));

		btn1.addClickHandler(new Btn1ClickHandler());
		this.vPanel2.add(btn1);
		this.vPanel2.add(btn2);

		txt1.setText("Tipo de Gramática");
		txt2.setText("Número de producciones");
		txt3.setText("Axiomas");
		txt4.setText("Terminales");
		txt5.setText("No terminales");

		this.hPanel3.add(stocksFlexTable);
		hPanel3.add(vPanel3);

		dockPanel.add(new HTML("Definición de Gramática."), DockPanel.NORTH);
		dockPanel.add(hPanel3, DockPanel.SOUTH);
		dockPanel.add(vPanel1, DockPanel.EAST);
		dockPanel.add(vPanel2, DockPanel.WEST);
		// dockPanel.add(new HTML("center"));

		vPanel.add(dockPanel);
	}

	private class Btn1ClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			String textcheck = ta.getText();
			serviceImp.checkContent(textcheck);
		}

	}

	public void updateLabel(Grammar grammar) {
		String term = grammar.getTerminals().toString(), noTerm = grammar
				.getNonTerminals().toString();

		term = term.substring(1, term.length() - 1);
		noTerm = noTerm.substring(1, noTerm.length() - 1);

		mGrammar = grammar;
		txt2.setText(((Integer) grammar.getProductions().size()).toString());
		txt3.setText(grammar.getAxiom().toString());
		txt4.setText(term);
		txt5.setText(noTerm);
		switch (grammar.getType()) {
		case TypeHandler.CHOMSKY:
			txt1.setText("Chomsky");
			break;
		case TypeHandler.DEPENDENT:
			txt1.setText("dependent");
			break;
		case TypeHandler.INDEPENDENT:
			txt1.setText("Independiente del contexto");
			break;
		case TypeHandler.REGULAR:
			txt1.setText("Regular");
			break;
		}

	}
	private void buildBar() {
		bar.addTab("1");
	    bar.addTab("bar");
	    bar.addTab("baz");

 	    bar.addTab("+");

	    bar.addSelectionHandler(new SelectionHandler<Integer>() {
	    	public void onSelection(SelectionEvent<Integer> event) {
	    		if (event.getSelectedItem() == 1) {                 
	    			bar.addTab("Nueva");

	    			}
	    		
	    		}
	    	});


	    // Just for fun, let's disallow selection of 'bar'.
	    bar.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {
	      public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
	        if (event.getItem().intValue() == 1) {
	          event.cancel();
	        }
	      }
	    });

	}
	    // Add it to the root panel.
	    

	private void buildMenuBar() {

		Command cmd = new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");
			}
		};

		Command eliminate_SNT = new Command() {
			public void execute() {
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				openSNT();
			}
		};

		Command eliminate_SNA = new Command() {
			public void execute() {
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openSNA();
			}
		};

		Command eliminate_SA = new Command() {
			public void execute() {
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openSA();
			}
		};

		Command eliminate_PNG = new Command() {
			public void execute() {
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openPNG();
			}
		};

		Command direct_recursion = new Command() {
			public void execute() {
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openDR();
			}
		};

		Command indirect_recursion = new Command() {
			public void execute() {
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openIR();
			}
		};

		Command recursion = new Command() {
			public void execute() {
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openR();
			}
		};
		
		Command left_factoring = new Command() {
			public void execute() {
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openLF();
			}
		};
		
		Command chomsky = new Command() {
			public void execute() {
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openChomsky();
			}
		};
		MenuBar fooMenu = new MenuBar(true);
		fooMenu.addItem("ejemplo1", cmd);
		fooMenu.addItem("ejemplo2", cmd);
		fooMenu.addItem("ejemplo3", cmd);

		MenuBar grammarMenu = new MenuBar(true);

		grammarMenu.addItem("Comprobar", cmd);
		grammarMenu.addItem("Renombrar símbolo", cmd);

		
		MenuBar algorithmMenu = new MenuBar(true);
		algorithmMenu.addItem("Eliminar símbolos no terminables.", eliminate_SNT);
		algorithmMenu.addItem("Eliminar símbolos no alcanzables.", eliminate_SNA);
		algorithmMenu.addItem("Eliminar simbolos anulables.", eliminate_SA);
		algorithmMenu.addItem("Eliminar preduciones no generativas.", eliminate_PNG);
		algorithmMenu.addItem("Eliminar recursividad directa.",	direct_recursion);
		algorithmMenu.addItem("Eliminar recursividad indirecta.", indirect_recursion);
		algorithmMenu.addItem("Eliminar recursividad.", recursion);
		algorithmMenu.addItem("Factorización por la izquierda.", left_factoring);
		algorithmMenu.addItem("Forma normal de Chomsky.", chomsky);

		MenuBar toolsMenu = new MenuBar(true);
		toolsMenu.addItem("Idioma.", cmd);
		toolsMenu.addItem("Preferencias..", cmd);

		MenuBar menu = new MenuBar();
		menu.addItem("Archivo", fooMenu);
		menu.addItem("Editar", fooMenu);
		menu.addItem("Gramática", grammarMenu);
		menu.addItem("Algoritmos", algorithmMenu);
		menu.addItem("Herramientas", toolsMenu);
		menu.addItem("Ayuda", fooMenu);

		hPanel.add(menu);
	}// buildMenuBar

	
	
	//Elimina Símbolos no Terminales
	public void openSNT() {
		this.vPanel.clear();
		hPanel.clear();
		this.currentPage = new VisualSNT(mGrammar);
		this.vPanel.add(this.currentPage);
	}

	//Elimina Símbolos no Alcanzables
	public void openSNA() {
		this.vPanel.clear();
		hPanel.clear();
		this.currentPage = new VisualSNA(mGrammar);
		this.vPanel.add(this.currentPage);
	}

	//Elimina Símbolos Anulables
	public void openSA() {
		this.vPanel.clear();
		hPanel.clear();
		this.currentPage = new VisualSA(mGrammar);
		this.vPanel.add(this.currentPage);
	}

	//Elimina Producciones no Generativas
	public void openPNG() {
		this.vPanel.clear();
		hPanel.clear();
		this.currentPage = new VisualPNG(mGrammar);
		this.vPanel.add(this.currentPage);
	}

	//Eliminar Recursividad Directa
	public void openDR() {
		this.vPanel.clear();
		hPanel.clear();
		this.currentPage = new VisualDirectRecursive(mGrammar);
		this.vPanel.add(this.currentPage);
	}

	//Eliminar Recursividad Indirecta
	public void openIR() {
		this.vPanel.clear();
		hPanel.clear();
		this.currentPage = new VisualIndirectRecursive(mGrammar);
		this.vPanel.add(this.currentPage);
	}

	//Eliminar Recursividad
	public void openR() {
		new MediatorRecursive(mGrammar);

	}
	
	//Factorización por la izquierda
	public void openLF() {
		this.vPanel.clear();
		hPanel.clear();
		this.currentPage = new VisualLeftFactoring(mGrammar);
		this.vPanel.add(this.currentPage);
	}
	
	//Chomsky
	public void openChomsky() {
		this.vPanel.clear();
		hPanel.clear();
		this.currentPage = new VisualChomsky(mGrammar);
		this.vPanel.add(this.currentPage);
	}
}//mainGui
