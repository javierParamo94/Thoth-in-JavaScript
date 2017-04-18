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
import com.google.gwt.user.client.ui.VerticalPanel;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.Grammar;
import src.client.gui.mediator.MediatorSNA;

/**
 * <b>Descripción</b><br>
 * Visualización de la eliminación de símbolos no alcanzables.
 * <p>
 * <b>Detalles</b><br>
 * Visualiza paso a paso la eliminación de los símbolos no alcanzables.<br>
 * Está formada por dos paneles de texto, uno contiene la vieja gramática y otro la
 * nueva; la nueva gramática va siendo construída desde el axioma hasta finalizar.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Visualiza la tranformación a FNC.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class VisualSNA extends Composite {
    
    // Attributes --------------------------------------------------------------------
    
	public TextArea mNew = new TextArea();
	public TextArea mOld = new TextArea();
	private HorizontalPanel hPanel = new HorizontalPanel();

	public VerticalPanel vPanel = new VerticalPanel();
	public GrammarServiceClientImp  serviceImp;
	private Composite currentPage;
	
    /**
     * Mediador asociado al panel
     */
    public MediatorSNA mMediator;
    
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
     * Constructor completo del algoritmo visual de limpieza de símbolos no alcanzables.
     * 
     * @param frame Frame al que está asociado el diálogo.
     * @param grammar Gramática a limpiar.
     */
    public VisualSNA(Grammar grammar) {
    	//setLocationRelativeTo(frame);
    			//setVisible(mVisible);

    		    mOld.setCharacterWidth(80);
    	        mOld.setVisibleLines(20);
    	        mOld.setEnabled(false);
    			mOld.setText(grammar.completeToString());

    	        mNew.setCharacterWidth(80);
    	        mNew.setVisibleLines(20);
    	        mNew.setEnabled(false);

    	        mVisible = true;
    	        mMediator = new MediatorSNA(this, grammar);

    	        buildPanels();
//    	        setLocationRelativeTo(frame);
//    	        setResizable(false);
    	        setVisible(mVisible);
    	        
    		}// VisualElimNonDeterministic

    		public void buildPanels() {
    			

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
    			dockPanel.add(new HTML("This is the second south component"),
    					DockPanel.SOUTH);


    			vPanel.add(dockPanel);

    			RootPanel.get().add(vPanel);
    		}

    
    /**
     * Asigna la funcionalidad de los botones.
     */
    public void buildListeners () {
            //Pulsar sobre Cancelar
		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.exit();

			}
		});
            //Pulsar sobre Siguiente
		btnOneStep.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
                mMediator.next();
                
            } 
        });
            //Pulsar sobre Todos los pasos
		btnAllSteps.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.all();
                
            } 
        });
            //Pulsar sobre Aceptar
		btnAcept.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mMediator.accept();
			}
        });
        
    }//buildListeners
    
}//VisualSNA
