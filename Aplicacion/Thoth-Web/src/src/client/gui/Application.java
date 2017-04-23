package src.client.gui;



import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.Grammar;


import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * <b>Descripción</b><br>
 * Pantalla principal de la aplicación.
 * <p>
 * <b>Detalles</b><br>
 * Sobre ella se ejecuta toda la acción de la aplicación y es la contenedora de los
 * menús y botones a la vez que de las pestañas.<br>
 * Puede albergar paneles de gramática o de autómatas y en función de cuál esté en 
 * este momento seleccionado habilita o deshabilita los menús y botones correspondientes.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Panel principal.<br>
 * Soporta toda la funcionalidad de la aplicación.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Saiz
 * @version 2.0
 */
public class Application  {

    // Attributes -------------------------------------------------------------------
    
    /**
     * Única instancia de la clase.
     */
    private static Application instance;
    
    /**
     * Idioma seleccionado.
     */
    private Integer idiom;
    
    /**
     * Barra de herramientas.
     */
    //public JToolBar mToolBar;
    
    /**
     * Menú de la barra de herramientas: Edición.
     */
    //private MenuEdit mEdit;
    
    /**
     * Menú de la barra de herramientas: Autómata. 
     */
    //private MenuAutomaton mAutomaton;

    /**
     * Menú de la barra de herramientas: Gramática.
     */
    //private MenuGrammar mGrammar;
    
    /**
     * Menú de la barra de herramientas: Algoritmos Gramática.
     */
    //private MenuAlgorithmGrammar mAlgGra;
    
    /**
     * Menú de la barra de herramientas: Algoritmos Autómata.
     */
   // private MenuAlgorithmAutomaton mAlgFA;
    
    private static final String COMMENTS =  "###############################################################\n"+
                                            "#      Archivo de configuración del proyecto THOTH v2         #\n"+
                                            "#                                                             #\n"+
                                            "# Proyecto de final de carrera realizado por:                 #\n"+                                           
                                            "#                                                             #\n"+
                                            "#     THOTH v.1                                               #\n"+
                                            "#   Andrés Arnáiz Moreno                                      #\n"+
                                            "#   Álvar Arnáiz González                                     #\n"+                                           
                                            "#                                                             #\n"+
                                            "#     THOTH v.2                                               #\n"+
                                            "#   Íñigo Mediavilla Saiz                                     #\n"+
                                            "#   Javier Jimeno Visitación                                  #\n"+                                         
                                            "#                                                             #\n"+
                                            "#     Tutelado por:                                           #\n"+
                                            "#   Dr. César Ignacio García Osorio                           #\n"+
                                            "#   Área de lenguajes y sistemas informaticos                 #\n"+
                                            "#                                                             #\n"+
                                            "# Universidad de Burgos                                       #\n"+
                                            "# Ingeniería Técnica en Informática de Gestión                #\n"+
                                            "###############################################################\n";
   
	private VerticalPanel vPanel = new VerticalPanel();
	private VerticalPanel vPanel1 = new VerticalPanel();
	private VerticalPanel vPanel2 = new VerticalPanel();
	private VerticalPanel vPanel3 = new VerticalPanel();
	private HorizontalPanel hPanel = new HorizontalPanel();
	private HorizontalPanel hPanel3 = new HorizontalPanel();
	private TextBox txt1, txt2, txt3, txt4, txt5;
	private FlexTable stocksFlexTable = new FlexTable();

	private TextArea ta;

	//public Grammar mGrammar;

	private GrammarServiceClientImp serviceImp;
	private Composite currentPage;
    
    
    // Methods ----------------------------------------------------------------------
    
    /**
     * Devuelve la única instancia de la calse Application.
     * 
     * @return Instancia de Application.
     */
    public static Application getInstance () {
        if(instance == null)
            instance = new Application();
        
        return instance;
    }//getInstance
    
    /**
     * Constructor privado que se encarga de la inicialización de la aplicación.
     */
    
    private Application() {
        //super("THOTH v2.0");
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //first();
        //readConfig();
        buildPanels();
        buildMenuBar();
        buildToolBar();
        //setBackground(Color.white);
        
    }//Application
    
    /**
     * Cambia de lenguaje la aplicación, establece las dimensiones de la ventana, y
     * carga la decoración por defecto.<br>
     * Se encarga de que el botón cerrar pregunte si desea guardar o no.
     */
    private void first () {
    	
        
    }//first
    
   
    
    /**
     * Crea los paneles principales y les asigna los bordes.
     * 
     */
    private void buildPanels () {
    	
    }//buildComponents
    
    /**
     * Construye la barra de menús al iniciar la aplicación.
     */
    private void buildMenuBar () {
    	
		
        /*JMenuBar menu = new JMenuBar();
        JMenu file = new MenuFile(),
              tool = new MenuTool(),
              help = new MenuHelp();
        mEdit = new MenuEdit();
        mAutomaton = new MenuAutomaton();
        mGrammar = new MenuGrammar();
        mAlgFA = new MenuAlgorithmAutomaton();
        mAlgGra = new MenuAlgorithmGrammar();
        mEdit.setVisible(false);
        mAutomaton.setVisible(false);
        mAlgFA.setVisible(false);
        mGrammar.setVisible(false);
        mAlgGra.setVisible(false);
        
        menu.add(file);
        menu.add(mEdit);
        menu.add(mAutomaton);
        menu.add(mGrammar);
        menu.add(mAlgFA);
        menu.add(mAlgGra);
        menu.add(tool);
        menu.add(help);
        
        setJMenuBar(menu);*/

    }//buildMenuBar
    
    /**
     * Construye la barra de herramientas.
     */
    private void buildToolBar () {
        /*mToolBar = new JToolBar("AutomatonToolBar", JToolBar.HORIZONTAL);
        JButton newFA = new JButton(Actions.getCreateFAJGraph()),
        		newPDA = new JButton(Actions.getCreatePDAJGraph()),
        		newTM = new JButton(Actions.getCreateMenuTM()),
                newRegExp = new JButton(Actions.getCreateRegExp()),
                newGrammar = new JButton(Actions.getCreateGrammar()),
                open = new JButton(Actions.getOpen()),
                save = new JButton(Actions.getSave()),
                print = new JButton(Actions.getPrint());
        
        newRegExp.setToolTipText(Messages.NEW + " " + Messages.REG_EXP);
        newTM.setToolTipText(Messages.NEW + " " + Messages.TM);
        newFA.setToolTipText(Messages.NEW + " " +Messages.AUTOMATON);
        newPDA.setToolTipText(Messages.NEW + " " +Messages.PDA);
        newGrammar.setToolTipText(Messages.NEW + " " + Messages.GRAMMAR);
        open.setToolTipText(Messages.OPEN);
        print.setToolTipText(Messages.PRINT);
        save.setToolTipText(Messages.SAVE);
        
        
        mToolBar.add(newFA);
        mToolBar.add(newPDA);
        mToolBar.add(newTM);
        mToolBar.add(newRegExp);
        mToolBar.add(newGrammar);
        mToolBar.add(open);
        mToolBar.add(save);
        mToolBar.add(print);

        add(mToolBar, BorderLayout.PAGE_START);*/
    }//buildToolBar
    
    
    
    /**
     * Imprime el autómata o la gramática del panel que está seleccionado.
     *//*
    public void printTab () {
        
    		//Si no tenemos nada abierto
    	if(Application.getInstance().mMain.getTabCount() == 0){
            ShowDialog.nothingPrint();
            return;
        }else{
        	ApplicationTab tab = Application.getInstance().getCurrentTab();
            //Inicializamos
            PrinterJob printJob = PrinterJob.getPrinterJob();
            PageFormat pf = new PageFormat();
            if(tab.getTabType() != ApplicationTab.GRAMMAR_TAB)
            	pf.setOrientation(PageFormat.LANDSCAPE);
        	if(printJob.pageDialog(pf).equals(pf))
        		return;

        	printJob.setPrintable(tab.getPrintable());
            
            if (printJob.printDialog()) {
                try {
                    printJob.print();
                } catch (Exception ex) {ShowDialog.errorPrint();}
            }
        }
        
    }//printTab
    */
}//Application
