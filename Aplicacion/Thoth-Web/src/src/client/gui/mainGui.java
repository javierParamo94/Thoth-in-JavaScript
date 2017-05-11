package src.client.gui;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.Grammar;
import src.client.core.grammar.TypeHandler;
import src.client.gui.mediator.MediatorClear;
import src.client.gui.mediator.MediatorRecursive;
import src.client.gui.utils.MessageMessages;
import src.client.gui.utils.ShowDialog;
import src.client.gui.visual.VisualChomsky;
import src.client.gui.visual.VisualDirectRecursive;
import src.client.gui.visual.VisualFirstFollow;
import src.client.gui.visual.VisualIndirectRecursive;
import src.client.gui.visual.VisualLeftFactoring;
import src.client.gui.visual.VisualPNG;
import src.client.gui.visual.VisualSA;
import src.client.gui.visual.VisualSNA;
import src.client.gui.visual.VisualSNT;
import src.client.gui.visual.VisualTasp;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;

public class mainGui extends Composite {

	private VerticalPanel vPanel = new VerticalPanel();
	private VerticalPanel vPanel1 = new VerticalPanel();
	private VerticalPanel vPanel2 = new VerticalPanel();
	private VerticalPanel vPanel3 = new VerticalPanel();
	private VerticalPanel editorGrammarPanel = new VerticalPanel();
	private HorizontalPanel barMenuPanel = new HorizontalPanel();
	private HorizontalPanel hPanel3 = new HorizontalPanel();
	private TextBox txt1, txt2, txt3, txt4, txt5;
	private FlexTable stocksFlexTable = new FlexTable();

	public int con = 1;

	DockPanel dockPanel = new DockPanel();

	private TextArea ta;
	public Grammar mGrammar;

	private static GrammarServiceClientImp serviceImp;
	private Composite currentPage;

	private MessageMessages sms = GWT.create(MessageMessages.class);

	/**
	 * Texto inicial del TextArea
	 */
	public static String INITIAL_TEXT = "% start \n%%\n\n\n\n%%\n";

	/**
	 * 
	 * @param serviceImp
	 */

	public mainGui(GrammarServiceClientImp serviceImp) {
		VerticalPanel mainVerticalPanel = new VerticalPanel();

		buildMenuBar();
		buildGrammarPanel(serviceImp);

		mainVerticalPanel.add(barMenuPanel);
		RootPanel.get().add(barMenuPanel);
		RootPanel.get().add(editorGrammarPanel);
	}//manGui

	/**
	 * Constructor del panel de gramáticas al que se le pasa la gramática.
	 * 
	 * @param serviceImp
	 *            Implementacion del servicio.
	 * @param grammar
	 *            Nueva gramática del panel.
	 */
	public mainGui(GrammarServiceClientImp serviceImp, Grammar grammar) {
		this(serviceImp);
		mGrammar = grammar;
		ta.setText(grammar.toString());
	}// mainGui

	/**
	 * 
	 * @param serviceImp
	 */
	private void buildGrammarPanel(GrammarServiceClientImp serviceImp) {

		mainGui.serviceImp = serviceImp;

		DockPanel dockPanel = new DockPanel();
		dockPanel.setStyleName("dockpanel");
		dockPanel.setSpacing(4);
		dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		dockPanel.setVerticalAlignment(DockPanel.ALIGN_TOP);

		ta = new TextArea();
		ta.setCharacterWidth(150);
		ta.setVisibleLines(18);
		ta.setText(INITIAL_TEXT);
		ta.setReadOnly(false);

		stocksFlexTable.setText(0, 0, sms.grammartype());
		stocksFlexTable.setText(1, 0, sms.productionnumber());
		stocksFlexTable.setText(2, 0, sms.axiom());
		stocksFlexTable.setText(3, 0, sms.tokens());
		stocksFlexTable.setText(4, 0, sms.nonterminals());
		stocksFlexTable.setHeight("230px");
		stocksFlexTable.setWidth("210px");

		stocksFlexTable.getRowFormatter().addStyleName(0, "header");
		stocksFlexTable.getRowFormatter().addStyleName(1, "header");
		stocksFlexTable.getRowFormatter().addStyleName(2, "header");
		stocksFlexTable.getRowFormatter().addStyleName(3, "header");
		stocksFlexTable.getRowFormatter().addStyleName(4, "header");

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

		this.vPanel1.add(ta);
		this.vPanel3.add(txt1);
		this.vPanel3.add(txt2);
		this.vPanel3.add(txt3);
		this.vPanel3.add(txt4);
		this.vPanel3.add(txt5);

		PushButton btn1 = new PushButton(new Image("images/checkGrammar.png"));
		PushButton btn2 = new PushButton(new Image("images/renameSymbol.png"));

		btn1.addClickHandler(new Btn1ClickHandler());
		btn2.addClickHandler(new Btn2ClickHandler());
		this.vPanel2.add(btn1);
		this.vPanel2.add(btn2);

		txt1.setText(sms.grammartype());
		txt2.setText(sms.productionnumber());
		txt3.setText(sms.axiom());
		txt4.setText(sms.tokens());
		txt5.setText(sms.nonterminals());

		this.hPanel3.add(stocksFlexTable);

		hPanel3.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		hPanel3.add(vPanel3);

		dockPanel.add(new HTML(sms.grammardef()), DockPanel.NORTH);
		dockPanel.add(hPanel3, DockPanel.SOUTH);
		dockPanel.add(vPanel1, DockPanel.EAST);
		dockPanel.add(vPanel2, DockPanel.WEST);

		editorGrammarPanel.add(dockPanel);
	}

	/**
	 * 
	 * @author User
	 *
	 */
	private class Btn1ClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			String textcheck = ta.getText();
			serviceImp.checkContent(textcheck);
		}

	}

	/**
	 * Renombrar
	 * 
	 * @author User
	 *
	 */
	private class Btn2ClickHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {

			serviceImp.checkContent(ta.getText());
			//new RenameSymbolDialog(Application.getInstance());
			// serviceImp.checkContent(textcheck);
		}
	}

	/**
	 * 
	 * @param grammar
	 */
	// @Override
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
			txt1.setText(sms.dependent());
			break;
		case TypeHandler.INDEPENDENT:
			txt1.setText(sms.independent());
			break;
		case TypeHandler.REGULAR:
			txt1.setText(sms.regular());
			break;
		}

	}

	/**
	 * 
	 */
	private void buildMenuBar() {

		Command cmd = new Command() {
			public void execute() {
				Window.alert("You selected a menu item!");

			}
		};

		Command spanish = new Command() {
			//private String locale;
			/*public void Command(String locale) {
				super();
				this.locale = locale;
			}*/
			public void execute() {
				Window.alert(sms.restartchanges() + " " + sms.ucontinue());
				UrlBuilder newUrl = Window.Location.createUrlBuilder();
				newUrl.setParameter("locale","es");// this.locale);
				Window.Location.assign(newUrl.buildString());
			}

		};
		Command deutschland = new Command() {
			public void execute() {
				Window.alert(sms.restartchanges() + " " + sms.ucontinue());
				UrlBuilder newUrl = Window.Location.createUrlBuilder();
				newUrl.setParameter("locale", "de");
				Window.Location.assign(newUrl.buildString());
			}
		};
		Command french = new Command() {
			public void execute() {
				Window.alert(sms.restartchanges() + " " + sms.ucontinue());
				UrlBuilder newUrl = Window.Location.createUrlBuilder();
				newUrl.setParameter("locale", "fr");
				Window.Location.assign(newUrl.buildString());
			}
		};
		Command english = new Command() {
			public void execute() {
				Window.alert(sms.restartchanges() + " " + sms.ucontinue());
				UrlBuilder newUrl = Window.Location.createUrlBuilder();
				newUrl.setParameter("locale", "en");
				Window.Location.assign(newUrl.buildString());
			}
		};

		Command eliminate_SNT = new Command() {
			public void execute() {
				serviceImp.checkContent(ta.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else {

					openSNT();
				}
			}
		};

		Command eliminate_SNA = new Command() {
			public void execute() {
				serviceImp.checkContent(ta.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openSNA();
			}
		};

		Command eliminate_SA = new Command() {
			public void execute() {
				serviceImp.checkContent(ta.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openSA();
			}
		};

		Command eliminate_PNG = new Command() {
			public void execute() {
				serviceImp.checkContent(ta.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openPNG();
			}
		};
		Command cleanGr = new Command() {
			public void execute() {
				serviceImp.checkContent(ta.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else {

					MediatorClear clean = new MediatorClear(mGrammar);
					switch (clean.doAll()) {
					case 0:
						ShowDialog.innecesaryAlgorithm();
						break;
					case 1:
						editorGrammarPanel.clear();
						barMenuPanel.clear();
						break;
					default:
						ShowDialog.cleanError();
					}
				}
			}
		};

		Command direct_recursion = new Command() {
			public void execute() {
				serviceImp.checkContent(ta.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openDR();
			}
		};

		Command indirect_recursion = new Command() {
			public void execute() {
				serviceImp.checkContent(ta.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openIR();
			}
		};

		Command recursion = new Command() {
			public void execute() {
				serviceImp.checkContent(ta.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openR();
			}
		};

		Command left_factoring = new Command() {
			public void execute() {
				serviceImp.checkContent(ta.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openLF();
			}
		};

		Command chomsky = new Command() {
			public void execute() {
				serviceImp.checkContent(ta.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openChomsky();
			}
		};

		Command fiFo = new Command() {
			public void execute() {
				serviceImp.checkContent(ta.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();

				else {
					final DialogBox deleteDialog = new DialogBox();
					deleteDialog.setAnimationEnabled(true);
					deleteDialog.setGlassEnabled(true);

					HorizontalPanel buttonPane = new HorizontalPanel();
					buttonPane
							.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

					Button yesBtn = new Button(sms.yes());
					yesBtn.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							deleteDialog.hide();
							openFiFo();
						}
					});

					Button noBtn = new Button(sms.no());
					noBtn.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							deleteDialog.hide();
						}
					});

					buttonPane.add(yesBtn);
					buttonPane.add(noBtn);

					deleteDialog.center();

					buttonPane.setWidth("75%");

					deleteDialog.add(buttonPane);
					deleteDialog.setText(sms.questionfirstfollow() + " "
							+ sms.ucontinue());
					deleteDialog.show();
				}
			}
		};

		Command tasp = new Command() {
			public void execute() {
				serviceImp.checkContent(ta.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();

				else {
					final DialogBox deleteDialog = new DialogBox();
					deleteDialog.setAnimationEnabled(true);
					deleteDialog.setGlassEnabled(true);

					HorizontalPanel buttonPane = new HorizontalPanel();
					buttonPane
							.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

					Button yesBtn = new Button(sms.yes());
					yesBtn.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							deleteDialog.hide();
							openTASP();
						}
					});

					Button noBtn = new Button(sms.no());
					noBtn.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							deleteDialog.hide();
						}
					});

					buttonPane.add(yesBtn);
					buttonPane.add(noBtn);

					deleteDialog.center();

					buttonPane.setWidth("75%");

					deleteDialog.add(buttonPane);
					deleteDialog.setText(sms.questionfirstfollow() + " "
							+ sms.ucontinue());
					deleteDialog.show();
				}
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
		algorithmMenu.addItem(sms.eliminatesnt(), eliminate_SNT);
		algorithmMenu.addItem(sms.eliminatesna(), eliminate_SNA);
		algorithmMenu.addItem(sms.eliminatesa(), eliminate_SA);
		algorithmMenu.addItem(sms.eliminatepng(), eliminate_PNG);
		algorithmMenu.addItem(sms.clear(), cleanGr);
		algorithmMenu.addSeparator();
		algorithmMenu.addItem(sms.eliminatedirectrecursion(), direct_recursion);
		algorithmMenu.addItem(sms.eliminateindirectrecursion(),
				indirect_recursion);
		algorithmMenu.addItem(sms.eliminaterecursion(), recursion);
		algorithmMenu.addSeparator();
		algorithmMenu.addItem(sms.factoring(), left_factoring);
		algorithmMenu.addItem(sms.fnchomsky(), chomsky);
		algorithmMenu.addSeparator();
		algorithmMenu.addItem(sms.calculateff(), fiFo);
		algorithmMenu.addItem(sms.tasp(), tasp);

		MenuBar selectIdiom = new MenuBar(true);
		selectIdiom.addItem("Castellano", spanish);

		selectIdiom.addItem("Deutsch", deutschland);

		selectIdiom.addItem("Français", french);

		selectIdiom.addItem("English", english);

		MenuBar toolsMenu = new MenuBar(true);
		toolsMenu.addItem(sms.language(), selectIdiom);

		MenuBar menu = new MenuBar();
		menu.addItem(sms.file(), fooMenu);
		menu.addSeparator();
		menu.addItem(sms.edit(), fooMenu);
		menu.addSeparator();
		menu.addItem(sms.grammar(), grammarMenu);
		menu.addSeparator();
		menu.addItem(sms.algorithms(), algorithmMenu);
		menu.addSeparator();
		menu.addItem(sms.tools(), toolsMenu);
		menu.addSeparator();
		menu.addItem(sms.help(), fooMenu);

		barMenuPanel.add(menu);
	}// buildMenuBar

	// Elimina Símbolos no Terminales
	public void openSNT() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		this.currentPage = new VisualSNT(mGrammar);
		this.vPanel.add(this.currentPage);
	}

	// Elimina Símbolos no Alcanzables
	public void openSNA() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		this.currentPage = new VisualSNA(mGrammar);
		this.vPanel.add(this.currentPage);
	}

	// Elimina Símbolos Anulables
	public void openSA() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		this.currentPage = new VisualSA(mGrammar);
		this.vPanel.add(this.currentPage);
	}

	// Elimina Producciones no Generativas
	public void openPNG() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		this.currentPage = new VisualPNG(mGrammar);
		this.vPanel.add(this.currentPage);
	}

	// Eliminar Recursividad Directa
	public void openDR() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		this.currentPage = new VisualDirectRecursive(mGrammar);
		this.vPanel.add(this.currentPage);
	}

	// Eliminar Recursividad Indirecta
	public void openIR() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		this.currentPage = new VisualIndirectRecursive(mGrammar);
		this.vPanel.add(this.currentPage);
	}

	// Eliminar Recursividad
	public void openR() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		new MediatorRecursive(mGrammar);

	}

	// Factorización por la izquierda
	public void openLF() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		this.currentPage = new VisualLeftFactoring(mGrammar);
		this.vPanel.add(this.currentPage);
	}

	// Chomsky
	public void openChomsky() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		this.currentPage = new VisualChomsky(mGrammar);
		this.vPanel.add(this.currentPage);
	}

	// First Follow
	public void openFiFo() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		this.currentPage = new VisualFirstFollow(mGrammar);
		this.vPanel.add(this.currentPage);
	}
	// TASP
	public void openTASP() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		this.currentPage = new VisualTasp(mGrammar);
		this.vPanel.add(this.currentPage);
	}
	
/*	private class CommandLang() extends Command {
		private String locale;
		public void Command(String locale) {
			super();
			this.locale = locale;
		}
		public void execute() {
			Window.alert(sms.restartchanges() + " " + sms.ucontinue());
			UrlBuilder newUrl = Window.Location.createUrlBuilder();
			newUrl.setParameter("locale", this.locale);
			Window.Location.assign(newUrl.buildString());
		}

	}*/
}// mainGui
