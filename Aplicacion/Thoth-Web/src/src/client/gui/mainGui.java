package src.client.gui;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.Grammar;
import src.client.core.grammar.TypeHandler;
import src.client.gui.mediator.MediatorClear;
import src.client.gui.mediator.MediatorRecursive;
import src.client.gui.utils.AboutDialog;
import src.client.gui.utils.CommandLang;
import src.client.gui.utils.MessageMessages;
import src.client.gui.utils.RenameSymbolDialog;
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
import src.client.register.request.RegistrationService;
import src.client.register.request.RegistrationServiceAsync;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
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

/**
 * <b>Descripción</b><br>
 * Clase principal de la GUI de la gramática.
 * <p>
 * <b>Detalles</b><br>
 * Muestra un menú con funcionalidades, una area de texto donde ingtroducir la
 * gramática y botones para ejecutar las acciones asociadas a la gramatica asi
 * como un panel con las características .<br>
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Checkeo de la gramática y renombrado de nodos y funciones del menú.
 * </p>
 * 
 * @author Francisco Javier Páramo Arnaiz.
 * @version 1.0
 */
public class mainGui extends Composite {

	// Attributes
	// -----------------------------------------------------------------------

	private MenuBar menu = new MenuBar();
	/**
	 * Panel vertical donde se escribe la gramatica
	 */
	private VerticalPanel imputGrammarPanel = new VerticalPanel();
	/**
	 * Panel de los botones.
	 */
	private VerticalPanel buttonPanel = new VerticalPanel();
	/**
	 * Panel con la tabla de características.
	 */
	private VerticalPanel tablePanel = new VerticalPanel();
	/**
	 * Panel general donde se edita la gramática.
	 */
	public VerticalPanel editorGrammarPanel = new VerticalPanel();
	/**
	 * Panel de la barra de menú
	 */
	public HorizontalPanel barMenuPanel = new HorizontalPanel();
	/**
	 * Panel del usuario.
	 */
	public HorizontalPanel userPanel = new HorizontalPanel();
	/**
	 * Panel general de las características de la gramática.
	 */
	private HorizontalPanel GrammarPropetiesPanel = new HorizontalPanel();
	/**
	 * TextBox con cada una de las características de la gramática.
	 */
	private TextBox boxType, boxNumProd, boxAxiom, boxTokens, boxNonTerm;
	/**
	 * Tabla con las características.
	 */
	private FlexTable propertiesGramTable = new FlexTable();
	/**
	 * Variable que referencia a esta misma clase
	 */
	private mainGui mVisual = this;

	/**
	 * Variable para la comunicación RPC con el servidor
	 */
	private RegistrationServiceAsync regService = GWT
			.create(RegistrationService.class);
	/**
	 * Area de texto donde podremos escribir la gramática que queramos.
	 */
	public TextArea grammarArea;
	/**
	 * Variable que contendrá la gramática.
	 */
	public Grammar mGrammar;
	/**
	 * GrammarServiceClientImp Implementacion del servicio.
	 */
	private static GrammarServiceClientImp serviceImp;

	/**
	 * Variable para la internacionalización de los textos
	 */
	private MessageMessages mMsg = GWT.create(MessageMessages.class);

	/**
	 * Texto inicial del TextArea
	 */
	public static String INITIAL_TEXT = "% start --Axioma \n%%\n\n--Producciones\n\n%%\n";

	// Methods
	// -----------------------------------------------------------------------
	/**
	 * Constructor del panel de gramáticas al que se le pasa el servicio.
	 * 
	 * @param serviceImp
	 *            Implementacion del servicio.
	 */
	public mainGui(GrammarServiceClientImp serviceImp) {
		buildMenuBar();
		buildGrammarPanel(serviceImp);
		userPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		// Añadimos los elementos al panel raiz.
		RootPanel.get().add(barMenuPanel);
		RootPanel.get().add(userPanel);
		RootPanel.get().add(editorGrammarPanel);
	}// manGui

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
		grammarArea.setText(grammar.toString());
	}// mainGui

	/**
	 * Constructor del panel de gramáticas al que se le pasa la gramática en
	 * formato texto.
	 * 
	 * @param serviceImp
	 *            Implementacion del servicio.
	 * @param grammarText
	 *            Nueva gramática del panel.
	 */
	public mainGui(GrammarServiceClientImp serviceImp, String grammarText) {
		this(serviceImp);
		grammarArea.setText(grammarText);
	}// mainGui

	/**
	 * contruye los paneles para la edición del agramática.
	 * 
	 * @param serviceImp
	 *            Implementacion del servicio.
	 */
	private void buildGrammarPanel(GrammarServiceClientImp serviceImp) {

		mainGui.serviceImp = serviceImp;

		// Panel principal que esta compuesto de 4 parte, norte, sur, este y
		// oeste.
		DockPanel dockPanel = new DockPanel();
		dockPanel.setStyleName("dockpanel");
		dockPanel.setSpacing(4);
		dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		dockPanel.setVerticalAlignment(DockPanel.ALIGN_TOP);

		// TextArea donde se podrá escribir la gramática
		grammarArea = new TextArea();
		grammarArea.setCharacterWidth(150);
		grammarArea.setVisibleLines(18);
		grammarArea.setText(INITIAL_TEXT);
		grammarArea.setReadOnly(false);

		// Tabla con los tipos de características de una gramática.
		propertiesGramTable.setText(0, 0, mMsg.grammartype());
		propertiesGramTable.setText(1, 0, mMsg.productionnumber());
		propertiesGramTable.setText(2, 0, mMsg.axiom());
		propertiesGramTable.setText(3, 0, mMsg.tokens());
		propertiesGramTable.setText(4, 0, mMsg.nonterminals());
		propertiesGramTable.setHeight("230px");
		propertiesGramTable.setWidth("210px");

		propertiesGramTable.getRowFormatter().addStyleName(0, "header");
		propertiesGramTable.getRowFormatter().addStyleName(1, "header");
		propertiesGramTable.getRowFormatter().addStyleName(2, "header");
		propertiesGramTable.getRowFormatter().addStyleName(3, "header");
		propertiesGramTable.getRowFormatter().addStyleName(4, "header");

		// TextBox donde se escribe las características de la gramática.
		boxType = new TextBox();
		boxNumProd = new TextBox();
		boxAxiom = new TextBox();
		boxTokens = new TextBox();
		boxNonTerm = new TextBox();

		boxType.setReadOnly(true);
		boxNumProd.setReadOnly(true);
		boxAxiom.setReadOnly(true);
		boxTokens.setReadOnly(true);
		boxNonTerm.setReadOnly(true);

		this.imputGrammarPanel.add(grammarArea);
		this.tablePanel.add(boxType);
		this.tablePanel.add(boxNumProd);
		this.tablePanel.add(boxAxiom);
		this.tablePanel.add(boxTokens);
		this.tablePanel.add(boxNonTerm);

		// botones
		PushButton btnCheck = new PushButton(new Image(
				"/images/checkGrammar.png"));
		PushButton btnRename = new PushButton(new Image(
				"/images/renameSymbol.png"));

		btnCheck.addClickHandler(new BtnCheckHandler());
		btnRename.addClickHandler(new BtnRenameHandler());
		this.buttonPanel.add(btnCheck);
		this.buttonPanel.add(btnRename);

		boxType.setText(mMsg.grammartype());
		boxNumProd.setText(mMsg.productionnumber());
		boxAxiom.setText(mMsg.axiom());
		boxTokens.setText(mMsg.tokens());
		boxNonTerm.setText(mMsg.nonterminals());

		this.GrammarPropetiesPanel.add(propertiesGramTable);

		GrammarPropetiesPanel
				.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		GrammarPropetiesPanel.add(tablePanel);

		dockPanel.add(GrammarPropetiesPanel, DockPanel.SOUTH);
		dockPanel.add(imputGrammarPanel, DockPanel.EAST);
		dockPanel.add(buttonPanel, DockPanel.WEST);

		editorGrammarPanel.add(dockPanel);
	}// buildGrammarPanel

	/**
	 * Clase asociada al botón de checkear gramática. Ejecuta la acción de
	 * checkeo de la gramática
	 * 
	 * @author Francisco Javier Páramo Arnaiz.
	 *
	 */
	private class BtnCheckHandler implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			String textcheck = grammarArea.getText();
			serviceImp.checkContent(textcheck);

			regService.grammarReg(grammarArea.getText(),
					new AsyncCallback<Object>() {
						// Fallo, mensaje de error.
						public void onFailure(Throwable caught) {

						}

						// Éxito
						public void onSuccess(Object user) {
							;
						}
					});
		}
	}// BtnCheckHandler

	/**
	 * Renombrado de nodos. Tanto terminales como no terminales
	 * 
	 * @author Francisco Javier Páramo Arnaiz.
	 *
	 */
	private class BtnRenameHandler implements ClickHandler {

		public void onClick(ClickEvent event) {
			String textcheck = grammarArea.getText();
			serviceImp.checkContent(textcheck);
			new RenameSymbolDialog(mVisual, mGrammar).show();
		}
	}// BtnRenameHandler

	public int var = 0;

	/**
	 * Actualiza la gramática asociada al panel.<br>
	 * Actualiza las propiedades de la gramática que se muestran en la parte
	 * inferior.
	 * 
	 * @param grammar
	 *            Nueva gramática.
	 */
	public void updateLabel(Grammar grammar) {
		String term = grammar.getTerminals().toString(), noTerm = grammar
				.getNonTerminals().toString();

		term = term.substring(1, term.length() - 1);
		noTerm = noTerm.substring(1, noTerm.length() - 1);

		mGrammar = grammar;
		boxNumProd.setText(((Integer) grammar.getProductions().size())
				.toString());
		boxAxiom.setText(grammar.getAxiom().toString());
		boxTokens.setText(term);
		boxNonTerm.setText(noTerm);
		switch (grammar.getType()) {
		case TypeHandler.CHOMSKY:
			boxType.setText("Chomsky");
			break;
		case TypeHandler.DEPENDENT:
			boxType.setText(mMsg.dependent());
			break;
		case TypeHandler.INDEPENDENT:
			boxType.setText(mMsg.independent());
			break;
		case TypeHandler.REGULAR:
			boxType.setText(mMsg.regular());
			break;
		}
	}// updateLabel

	/**
	 * Construye todo el menú.
	 */
	private void buildMenuBar() {

		// Info del proyecto
		Command about = new Command() {
			public void execute() {
				new AboutDialog();
			}
		};

		// Cambio de idioma
		CommandLang spanish = new CommandLang("es");
		CommandLang deutschland = new CommandLang("de");
		CommandLang french = new CommandLang("fr");
		CommandLang english = new CommandLang("en");

		// comprueba la gramática para SNT
		Command eliminate_SNT = new Command() {
			public void execute() {
				serviceImp.checkContent(grammarArea.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT) {
					ShowDialog.incorrectTypeGrammar();
				} else {

					openSNT();
				}
			}
		};

		// comprueba la gramática para SNA
		Command eliminate_SNA = new Command() {
			public void execute() {
				serviceImp.checkContent(grammarArea.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openSNA();
			}
		};

		// comprueba la gramática para SA
		Command eliminate_SA = new Command() {
			public void execute() {
				serviceImp.checkContent(grammarArea.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openSA();
			}
		};

		// comprueba la gramática para PNG
		Command eliminate_PNG = new Command() {
			public void execute() {
				serviceImp.checkContent(grammarArea.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openPNG();
			}
		};

		// limpia la gramática o emite error.
		Command cleanGr = new Command() {
			public void execute() {
				serviceImp.checkContent(grammarArea.getText());
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
		// comprueba la gramática para la recursividad directa
		Command direct_recursion = new Command() {
			public void execute() {
				serviceImp.checkContent(grammarArea.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openDR();
			}
		};

		// comprueba la gramática para la recursividad indirecta
		Command indirect_recursion = new Command() {
			public void execute() {
				serviceImp.checkContent(grammarArea.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openIR();
			}
		};

		// comprueba la gramática para la limpieza de recursividad
		Command recursion = new Command() {
			public void execute() {
				serviceImp.checkContent(grammarArea.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openR();
			}
		};

		// comprueba la gramática para factorizar por la izq.
		Command left_factoring = new Command() {
			public void execute() {
				serviceImp.checkContent(grammarArea.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openLF();
			}
		};

		// comprueba la gramática para chomsky.
		Command chomsky = new Command() {
			public void execute() {
				serviceImp.checkContent(grammarArea.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();
				else
					openChomsky();
			}
		};

		// comprueba la gramática fifo y emite mensaje de continuacion
		Command fiFo = new Command() {
			public void execute() {
				serviceImp.checkContent(grammarArea.getText());
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

					Button yesBtn = new Button(mMsg.yes());
					yesBtn.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							deleteDialog.hide();
							openFiFo();
						}
					});

					Button noBtn = new Button(mMsg.no());
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
					deleteDialog.setText(mMsg.questionfirstfollow() + " "
							+ mMsg.ucontinue());
					deleteDialog.show();
				}
			}
		};

		// comprueba la gramática tasp y emite mensaje de continuacion
		Command tasp = new Command() {
			public void execute() {
				serviceImp.checkContent(grammarArea.getText());
				if (mGrammar.getType() == TypeHandler.CHOMSKY
						|| mGrammar.getType() == TypeHandler.DEPENDENT)
					ShowDialog.incorrectTypeGrammar();

				else {
					final DialogBox choiceDialog = new DialogBox();
					choiceDialog.setAnimationEnabled(true);
					choiceDialog.setGlassEnabled(true);

					HorizontalPanel buttonPane = new HorizontalPanel();
					buttonPane
							.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

					Button yesBtn = new Button(mMsg.yes());
					yesBtn.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							choiceDialog.hide();
							openTASP();
						}
					});

					Button noBtn = new Button(mMsg.no());
					noBtn.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							choiceDialog.hide();
						}
					});

					buttonPane.add(yesBtn);
					buttonPane.add(noBtn);

					choiceDialog.center();

					buttonPane.setWidth("75%");

					choiceDialog.add(buttonPane);
					choiceDialog.setText(mMsg.questionfirstfollow() + " "
							+ mMsg.ucontinue());
					choiceDialog.show();
				}
			}
		};

		// menu con los algoritmos
		MenuBar algorithmMenu = new MenuBar(true);
		algorithmMenu.addItem(mMsg.eliminatesnt(), eliminate_SNT);
		algorithmMenu.addItem(mMsg.eliminatesna(), eliminate_SNA);
		algorithmMenu.addItem(mMsg.eliminatesa(), eliminate_SA);
		algorithmMenu.addItem(mMsg.eliminatepng(), eliminate_PNG);
		algorithmMenu.addItem(mMsg.clear(), cleanGr);
		algorithmMenu.addSeparator();
		algorithmMenu
				.addItem(mMsg.eliminatedirectrecursion(), direct_recursion);
		algorithmMenu.addItem(mMsg.eliminateindirectrecursion(),
				indirect_recursion);
		algorithmMenu.addItem(mMsg.eliminaterecursion(), recursion);
		algorithmMenu.addSeparator();
		algorithmMenu.addItem(mMsg.factoring(), left_factoring);
		algorithmMenu.addItem(mMsg.fnchomsky(), chomsky);
		algorithmMenu.addSeparator();
		algorithmMenu.addItem(mMsg.calculateff(), fiFo);
		algorithmMenu.addItem(mMsg.tasp(), tasp);

		// menu de idiomas
		MenuBar selectIdiom = new MenuBar(true);
		selectIdiom.addItem("Castellano", spanish);
		selectIdiom.addItem("Deutsch", deutschland);
		selectIdiom.addItem("Français", french);
		selectIdiom.addItem("English", english);

		// menu de ayuda
		MenuBar helpMenu = new MenuBar(true);
		helpMenu.addItem(mMsg.about(), about);

		menu.addItem(mMsg.algorithms(), algorithmMenu);
		menu.addSeparator();
		menu.addItem(mMsg.language(), selectIdiom);
		menu.addSeparator();
		menu.addItem(mMsg.help(), helpMenu);

		// salida de sessión con nombre del usuario
		getUserName();
		// doble separador
		menu.addSeparator();

		barMenuPanel.add(menu);
	}// buildMenuBar

	/**
	 * Obtiene el nombre del usuario con sesión via RPC con el server
	 */
	public void getUserName() {

		regService.loginFromSessionServer(new AsyncCallback<Object>() {
			String user;

			@Override
			public void onFailure(Throwable caught) {
				user = "error";
			}

			@Override
			public void onSuccess(Object result) {
				if (result == null)
					user = "error";
				else
					user = (String) result;
				writeUserName(user);
			}
		});
	}// getUserName

	/**
	 * Escribe en el menu el nombre del usuario.
	 * 
	 * @param name
	 */
	public void writeUserName(String name) {

		MenuBar logoutMenu = new MenuBar(true);
		logoutMenu.addItem("Logout", logoutFunction);

		menu.addSeparator();
		menu.addItem(mMsg.user() + " " + name, logoutMenu);
	}// writeUserName

	/**
	 * Comando asociado al menú del logout
	 */
	Command logoutFunction = new Command() {
		@Override
		public void execute() {
			regService.logout(new AsyncCallback<Object>() {
				// en caso de fallo emite mensaje de error.
				public void onFailure(Throwable caught) {
					// Window.alert(mMsg.saalgorithm());
				}

				// en caso de exito redirige.
				@Override
				public void onSuccess(Object result) {
					Window.Location.assign("/gramaticacs/");
				}
			});
		}
	};

	// Elimina Símbolos no Terminales
	public void openSNT() {
		// checkOnAlgortihm();
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		new VisualSNT(mGrammar);
	}

	// Elimina Símbolos no Alcanzables
	public void openSNA() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		new VisualSNA(mGrammar);
	}

	// Elimina Símbolos Anulables
	public void openSA() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		new VisualSA(mGrammar);
	}

	// Elimina Producciones no Generativas
	public void openPNG() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		new VisualPNG(mGrammar);
	}

	// Eliminar Recursividad Directa
	public void openDR() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		new VisualDirectRecursive(mGrammar);
	}

	// Eliminar Recursividad Indirecta
	public void openIR() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		new VisualIndirectRecursive(mGrammar);
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
		new VisualLeftFactoring(mGrammar);
	}

	// Chomsky
	public void openChomsky() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		new VisualChomsky(mGrammar);
	}

	// First Follow
	public void openFiFo() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		new VisualFirstFollow(mGrammar);
	}

	// TASP
	public void openTASP() {
		editorGrammarPanel.clear();
		barMenuPanel.clear();
		new VisualTasp(mGrammar);
	}
}// mainGui
