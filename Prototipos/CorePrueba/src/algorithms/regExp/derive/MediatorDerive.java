package algorithms.regExp.derive;

import java.awt.Dimension;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphModel;

import algorithms.MediatorAlgorithm;

import view.application.Application;
import view.application.AutomatonSplitPane;
import view.utils.Messages;
import view2.controller.graphcontroller.LogicVisualConverter;

/**
 * <b>Descripci�n</b><br>
 * Clase que hace de mediador con la clase f�sica de derivadas.
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de mediar entre la parte gr�fica y la parte f�sica.<br>
 * El algoritmo obtiene un aut�mata finito a partir de una expresi�n regular
 * mediante el m�todo de las derivadas.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evita el acomplamiento entre la parte f�sica y la parte visual.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class MediatorDerive implements MediatorAlgorithm{
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Algoritmo visual. Pantalla donde se muestra la ejecuci�n del algoritmo.
     */
    private VisualDerive mVisual;
    
    /**
     * Algoritmo de creaci�n de aut�matas finitos mediante derivadas.
     */
    private Derive mAlgorithm;
    
    /**
     * Expresi�n regular.
     */
    private String mExpression;
    
    // Methods --------------------------------------------------------------------
    
    /**
     * Constructor del mediador.<br>
     * Se asegura de que el algoritmo se puede realizar y de la inicializaci�n de todos
     * los par�metros y variables.
     * 
     * @param visualDer Algoritmo visual. Pantalla donde se va a mostrar el algoritmo.
     * @param regExp Expresi�n regular en forma de �rbol. 
     */
    public MediatorDerive (VisualDerive visualDer, DeriveTree regExp, String expression) {
        mVisual = visualDer;
        mAlgorithm = new Derive(regExp);
        mExpression = expression;
        if(!mAlgorithm.firstStep()){
            mVisual.setVisible(false);
            mVisual = null;
            return;
        }
            //Reiniciamos el algoritmo
        first(regExp);
        mVisual.mVisible = true;
        
    }//MediatorDerive
    
    /**
     * Primer paso del algoritmo.<br>
     * Inicializa el algoritmo y hace el primer paso.
     * 
     * @param regExp Expresi�n regular.
     */
    private void first (DeriveTree regExp) {
        mAlgorithm = new Derive(regExp);
        mAlgorithm.firstStep();
        updateShowing();
        
    }//first
    
    private void updateShowing() {
    	Dimension dim = mVisual.getShowingPaneSize();
    	//El 40 sirve para evitar extraLabels que se salen del panel
    	int height = (int) (dim.getHeight()-40< 250 ? 250 : Math.min(dim.getHeight()-40,700));
    	int width = (int) (dim.getWidth()-40< 650 ? 650 : Math.min(dim.getWidth()-40,1000));
        JGraph graph = LogicVisualConverter.createGraph(mAlgorithm.getSolution(),new Dimension(width,height));
        mVisual.setShowingPane(graph);
        
	}

	/**
     * Realiza cada paso del algoritmo.<br>
     * Muestra el aut�mata tal y como est� en cada momento y rellena las derivadas.
     * 
     * @return True si puede seguir ejecutando el algoritmo y false en caso contrario.
     */
    public boolean next () {
            //�ltimo paso
        if(!mAlgorithm.nextStep()){
            updateShowing();
            disableButtons();
            return false;
        }
        
        updateShowing();
        
        setInformation();
        
        return true;
    }//next
    
    /**
     * Muestra informaci�n de utilidad sobre las derivadas.
     */
    private void setInformation () {
        String old = mVisual.mInfo.getText();
        
        mVisual.mInfo.setText(old + Messages.DERIVEA + " \""
                + mAlgorithm.getCurrentState() + "\" "
                + Messages.DERIVEB + " \"" + mAlgorithm.getCurrentTerminal() + "\" = "
                + mAlgorithm.getCurrentDerive().getRegExp() + "\n");
        
    }//setInformation
    
    /**
     * Ejecuta todos los pasos del algoritmo.
     */
    public void all () {
        while(mAlgorithm.nextStep())
            setInformation();
        
        updateShowing();
        disableButtons();
        
    }//check
    
    /**
     * Crea el aut�mata visual y cierra la ventana.<br>
     * Si la ventana seleccionada no contiene ning�n aut�mata lo crear�
     * en esa pesta�a, si por el contrario ya tiene uno a�adir� una nueva.
     */
    public void accept () {
    	Application app = Application.getInstance();
        LogicVisualConverter con = new LogicVisualConverter();
        DefaultGraphModel model = con.logicToVisual(mAlgorithm.getSolution(),LogicVisualConverter.DEFAULT_SIZE,true);
        
        app.createTab(mAlgorithm.getSolution().getName(), new AutomatonSplitPane(model,mAlgorithm.getSolution()));
        app.getCurrentTab().setChanges(true);
        app.getCurrentTab().checkContent();
        
        exit();
    }//accept

    /**
     * Habilita o deshabilita los botones de siguiente paso y todos pasos.
     */
    private void disableButtons () {
        mVisual.mNext.setEnabled(false);
        mVisual.mAll.setEnabled(false);
        mVisual.mAccept.setEnabled(true);
        
    }//disableButtons
    
    /**
     * Desreferencia el mediador del visual y cirrar la pantalla visual.
     */
    public void exit () {
    	mVisual.liberate();
    }//exit
    
}//MediatorDerive
