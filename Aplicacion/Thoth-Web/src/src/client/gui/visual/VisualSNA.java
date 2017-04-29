package src.client.gui.visual;


import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

import src.client.GrammarServiceClientImp;
import src.client.core.grammar.Grammar;
import src.client.gui.MessageMessages;
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
    
	public RichTextArea mNew = new RichTextArea();
	public RichTextArea mOld = new RichTextArea();
	private HorizontalPanel hPanel = new HorizontalPanel();

	public VerticalPanel vPanel = new VerticalPanel();
	public GrammarServiceClientImp  serviceImp;
	
	/**
	 * Variable para la internacionalización de los textos
	 */
	private MessageMessages sms = GWT.create(MessageMessages.class);
	
    /**
     * Mediador asociado al panel
     */
    public MediatorSNA mMediator;
    
    /**
     * Indica si debe mostrarse o no el panel.
     */
    public boolean mVisible;
	/**
	 * Panel vertical que engloba el área de la nueva gramática
	 */
	public VerticalPanel vPanelNew = new VerticalPanel();
	/**
	 * Panel vertical que engloba el área de la vieja gramática
	 */
	public VerticalPanel vPanelOld = new VerticalPanel();
    
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

		mOld.setPixelSize(500, 400);
		mOld.setEnabled(false);
		mOld.setText(grammar.completeToString());

		mNew.setPixelSize(500, 400);
		mNew.setEnabled(false);
		
		mVisible = true;
		mMediator = new MediatorSNA(this, grammar);

		buildPanels();
		setVisible(mVisible);
		
		}// VisualElimNonDeterministic

    		public void buildPanels() {
    			

    			DockPanel dockPanel = new DockPanel();
    			dockPanel.setStyleName("dockpanel");
    			dockPanel.setSpacing(4);
    			dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);

    			vPanelNew.add(new HTML(sms.newgrammar()));
    			vPanelNew.setSpacing(20);
    			vPanelNew.add(mNew);
    			vPanelNew.setStyleName("gwt-Big-Text");

    			vPanelOld.add(new HTML(sms.oldgrammar()));
    			vPanelOld.setSpacing(20);
    			vPanelOld.add(mOld);
    			vPanelOld.setStyleName("gwt-Big-Text");
    			
    			// Botones
    			hPanel.add(btnCancel);
    			hPanel.add(btnOneStep);
    			hPanel.add(btnAllSteps);
    			hPanel.add(btnAcept);
    			buildListeners();

    			// Add text all around
    			dockPanel.add(new HTML(sms.snaalgorithm()),
    					DockPanel.NORTH);
    			dockPanel.add(hPanel, DockPanel.SOUTH);
    			dockPanel.add(vPanelNew, DockPanel.EAST);
    			dockPanel.add(vPanelOld, DockPanel.WEST);

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
