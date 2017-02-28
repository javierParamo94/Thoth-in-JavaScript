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
 * <b>Descripción</b><br>
 * Clase que hace de mediador con la clase física de derivadas.
 * <p>
 * <b>Detalles</b><br>
 * Se encarga de mediar entre la parte gráfica y la parte física.<br>
 * El algoritmo obtiene un autómata finito a partir de una expresión regular
 * mediante el método de las derivadas.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evita el acomplamiento entre la parte física y la parte visual.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class MediatorDerive implements MediatorAlgorithm{
    
    // Attributes -----------------------------------------------------------------
    
    /**
     * Algoritmo visual. Pantalla donde se muestra la ejecución del algoritmo.
     */
    private VisualDerive mVisual;
    
    /**
     * Algoritmo de creación de autómatas finitos mediante derivadas.
     */
    private Derive mAlgorithm;
    
    /**
     * Expresión regular.
     */
    private String mExpression;
    
    // Methods --------------------------------------------------------------------
    
    /**
     * Constructor del mediador.<br>
     * Se asegura de que el algoritmo se puede realizar y de la inicialización de todos
     * los parámetros y variables.
     * 
     * @param visualDer Algoritmo visual. Pantalla donde se va a mostrar el algoritmo.
     * @param regExp Expresión regular en forma de árbol. 
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
     * @param regExp Expresión regular.
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
     * Muestra el autómata tal y como está en cada momento y rellena las derivadas.
     * 
     * @return True si puede seguir ejecutando el algoritmo y false en caso contrario.
     */
    public boolean next () {
            //Último paso
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
     * Muestra información de utilidad sobre las derivadas.
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
     * Crea el autómata visual y cierra la ventana.<br>
     * Si la ventana seleccionada no contiene ningún autómata lo creará
     * en esa pestaña, si por el contrario ya tiene uno añadirá una nueva.
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
