package src.client.gui.visual;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.Grammar;
import src.client.gui.mediator.MediatorPNG;


/**
 * <b>Descripción</b><br>
 * Visualización de la eliminación de producciones no generativas.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza paso a paso la eliminación de producciones no generativas.<br>
 * Está formada por tres paneles de texto, uno contiene la vieja gramática, otro la
 * nueva y el último y más pequeño el conjunto UNITARIO(A), es decir, los terminales
 * a los que se pueden llegar siguiendo sólo producciones unitarias.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Visualiza la eliminación de PNGs.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class VisualPNG extends Composite {
    
    // Attributes --------------------------------------------------------------------
    
	
	public TextArea mNew = new TextArea();
	public TextArea mOld = new TextArea();
	public TextArea mAux = new TextArea();
	private HorizontalPanel hPanel = new HorizontalPanel();

	public VerticalPanel vPanel = new VerticalPanel();
	public GrammarServiceClientImp  serviceImp;
	private Composite currentPage;
	
    /**
     * Mediador de limpieza asociado al panel.
     */
    public MediatorPNG mMediator;
    
    /**
     * Indica si debe mostrarse o no el panel.
     */
    public boolean mVisible;
    
	public PushButton btnCancel = new PushButton(new Image(
			"images/cancelAlgorithm.png"));
	public PushButton btnOneStep = new PushButton(new Image(
			"images/oneStep.png"));
	public PushButton btnAllSteps = new PushButton(new Image(
			"images/allSteps.png"));
	public PushButton btnAcept = new PushButton(new Image(
			"images/acceptAlgorithm.png"));
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del algoritmo visual de limpieza de producciones no generativas
     * 
     * @param frame Frame al que está asociado el diálogo.
     * @param grammar Gramática a limpiar.
     */
    public VisualPNG(Grammar grammar) {
    	
		mOld.setCharacterWidth(80);
		mOld.setVisibleLines(20);
		mOld.setEnabled(false);
		mOld.setText(grammar.completeToString());

		mNew.setCharacterWidth(80);
		mNew.setVisibleLines(20);
		mNew.setEnabled(false);

		mAux.setReadOnly(true);
		mAux.setSize("250px", "20px");

		mVisible = true;
		mMediator = new MediatorPNG(this, grammar);

		buildPanels();
		setVisible(mVisible);

        
    }//VisualPNG
    
    /**
     * Construye los paneles y botones.<br>
     * Asigna la funcionalidad de los botones.
     */
    private void buildPanels () {
		DockPanel dockPanel = new DockPanel();
		dockPanel.setStyleName("dockpanel");
		dockPanel.setSpacing(4);
		dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

		// Botones
		hPanel.add(btnCancel);
		hPanel.add(btnOneStep);
		hPanel.add(btnAllSteps);
		hPanel.add(btnAcept);
		buildListeners();

		// Add text all around
		dockPanel.add(new HTML("This is the first north component."),
				DockPanel.NORTH);
		dockPanel.add(hPanel, DockPanel.SOUTH);
		dockPanel.add(mNew, DockPanel.EAST);
		dockPanel.add(mOld, DockPanel.WEST);
		dockPanel.add(new HTML("This is the second north component."),
				DockPanel.NORTH);
		dockPanel.add(mAux, DockPanel.SOUTH);

		vPanel.add(dockPanel);

		RootPanel.get().add(vPanel);
                
    }//buildPanels
    
    /**
     * Asigna la funcionalidad de los botones.
     */
    public void buildListeners () {
    	// Pulsar sobre Cancelar
    			btnCancel.addClickHandler(new ClickHandler() {
    				@Override
    				public void onClick(ClickEvent event) {
    					mMediator.exit();

    				}
    			});
    			// Pulsar sobre Siguiente
    			btnOneStep.addClickHandler(new ClickHandler() {
    				@Override
    				public void onClick(ClickEvent event) {
    					mMediator.next();

    				}
    			});
    			// Pulsar sobre Todos los pasos
    			btnAllSteps.addClickHandler(new ClickHandler() {
    				@Override
    				public void onClick(ClickEvent event) {
    					mMediator.all();

    				}
    			});
    			// Pulsar sobre Aceptar
    			btnAcept.addClickHandler(new ClickHandler() {
    				@Override
    				public void onClick(ClickEvent event) {
    					mMediator.accept();
    				}
    			});
        
    }//buildListeners
}//VisualPNG