package algorithms.regExp.thompson;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import java.awt.geom.Point2D;

import org.jgraph.graph.DefaultGraphModel;


import core.State;
import core.Terminal;
import core.TerminalEpsilon;
import core.Transition;
import core.finiteautomaton.FiniteAutomaton;
import core.finiteautomaton.StateFA;
import core.finiteautomaton.TransitionFA;
import view.application.Application;
import view.application.AutomatonSplitPane;
import view2.controller.StateProperties;
import view2.controller.TransitionGroup;
import view2.controller.TransitionProperties;
import view2.controller.graphcontroller.LogicVisualConverter;
import view2.graph.ThothCellViewFactory;

/**
 * <b>Descripci�n</b><br>
 * Esta clase se encargar� de dibujar por pantalla el algoritmo resultante de 
 * ejecutar el m�todo de Thompson a partir de una expresi�n regular.
 * <p>
 * <b>Detalles</b><br>
 * Para la construcci�n del aut�mata utilizar� el m�todo de Thompson, que a partir
 * de un �rbol previamente construido con la expresi�n regular, construir� un nuevo
 * aut�mata.<br>
 * Esta clase utilizar� ese aut�mata para ir construyendo un aut�mata visual
 * asign�ndole unas posiciones (seg�n el criterio del dibujante) para poder
 * mostrarlo correctamente por pantalla.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el m�todo de Thompson.<br>
 * Crea un nuevo aut�mata visual.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class MediatorThompson{
	
    // Attributes -----------------------------------------------------------------
    
    /**
     * Algoritmo de construcci�n de aut�matas mediante el m�todo de Thompson.
     */
    protected Thompson mAlgorithm;
    
    /**
     * �rbol que contiene la expresi�n regular a partir de la cual se construye
     * el aut�mata.
     */
    protected ThompsonTree mTree;
    
    /**
     * Aut�mata visual que va a crearse.
     */
    protected FiniteAutomaton mAutomaton;
    
    /**
     * Nodo actual del �rbol de Thompson.
     */
    protected ThompsonNode mNode;
    
    /**
     * Separaci�n horizontal entre cada nodo
     */
    protected final int mWidth = 80;
    
    /**
     * Separaci�n vertical entre cada nodo
     */
    protected final int mHeight = 50;
    
    /**
     * Expresi�n regular.
     */
    protected String mExpression;

	protected StateProperties sPos;

	protected HashMap<TransitionGroup, Integer> cPointsYMove;

	private DefaultGraphModel model;
    
    // Methods --------------------------------------------------------------------
    
    /**
     * Constructor del mediador.<br>
     * Inicicalizar� el algoritmo de Thompson, creando un nuevo aut�mata visual.<br>
     * Invoca a buildAutomaton para asignar a cada estado y a cada transici�n su posici�n.
     * 
     * @param tree �rbol de thompson que servir� para construir un nuevo aut�mata.
     * @param expression Expresi�n regular introducida por el usuario.
     */
    public MediatorThompson (ThompsonTree tree, String expression) {
        mTree = tree;
        mAlgorithm = new Thompson(mTree);
        mAlgorithm.firstStep();
        mAutomaton = new FiniteAutomaton(mAlgorithm.getSolution().getName());
        mNode = mAlgorithm.mCurrentNode;
        mExpression = expression;
        sPos = new StateProperties();
        cPointsYMove = new HashMap<TransitionGroup,Integer>();
        buildAutomaton();
        LogicVisualConverter conv = new LogicVisualConverter();
		TransitionProperties tProp = buildControlPoints();
		//Se normalizan los estados y los puntos de control a la vez
		if(!sPos.isNormal() || tProp.isNormal()){
			double despX = Math.max(sPos.getXOffset(), tProp.getXOffset());
			double despY = Math.max(sPos.getYOffset(), tProp.getYOffset());
			sPos.translate(despX, despY);
			tProp.translate(despX, despY);
		}
		model = conv.logicToVisual(mAutomaton,sPos,tProp);
    }//MediatorThompson
    
    /**
     * Construye un aut�mata nuevo a partir del algoritmo de Thompson.
     */
    public void buildAutomaton () {
        boolean flag = true;
        int y;
        
        while(flag){
            flag = mAlgorithm.nextStep();
            switch(mNode.getToken()){
                case '.':
                    buildConcat();
                    break;
                case '|':
                    buildSelect();
                    break;
                case '*':
                    buildAsterisk();
                    break;
                default:
                    buildTerminal(mNode.getToken());
                    break;
            }
            mNode = mAlgorithm.mCurrentNode;
        }
            //Asignamos estado inicial y final
        mAutomaton.setInitialState(mAutomaton.findState(mNode.getInitial()));
        mAutomaton.findState(mNode.getFinal()).setFinal(true);
        
    }//buildAutomaton
    
    /**
     * Construye las concatenaciones.<br>
     * Borra el estado sobrante al realizar la concatenaci�n, tambi�n mueve la parte
     * derecha para que vaya seguida de la izquierda.
     */
    public void buildConcat () {
        State end = mAutomaton.findState(((ThompsonNode)mNode.getLeft()).getFinal());
        State ini = mAutomaton.findState(((ThompsonNode)mNode.getRight()).getInitial());
        Transition temp;
            //Obtenemos 
        int x = (int)(sPos.getStatePosition(ini).getX() - sPos.getStatePosition(end).getX());
        int y = (int)(sPos.getStatePosition(ini).getY() - sPos.getStatePosition(end).getY());
        moveStates((StateFA) ini, x, y);
        
            //Borramos el estado que va a desaparecer al concatenar
            //Antes copiamos sus transiciones de entrada al estado que lo reemplazar�
        for(int i=0; i<end.getTransitionsIn().size(); i++){
            temp = end.getTransitionsIn().elementAt(i);
            TransitionFA trans = new TransitionFA(temp.getIn(),temp.getPrevState(),ini);
            ini.addTransitionIn(trans);
            TransitionGroup old = new TransitionGroup(temp.getPrevState(),temp.getNextState());
            Integer up = cPointsYMove.get(old);
            if(up!=null){
            	cPointsYMove.put(new TransitionGroup(temp.getPrevState(),ini), up);
            	cPointsYMove.remove(old);
            }
        }
        mAutomaton.removeState(end.getName());
        
    }//buildConcat
    
    /**
     * Construye las selecciones.<br>
     * Construye dos nuevos estado y sus transiciones correspondientes.<br>
     * Desplaza la parte izquierda hacia arriba y la derecha hacia abajo situando
     * el nuevo estado inicial creado entre ambos.
     */
    public void buildSelect () {
    	State nodeInitial = mAutomaton.createState(mNode.getInitial());
    	State nodeFinal = mAutomaton.createState(mNode.getFinal());
    	sPos.addStatePosition(nodeInitial, new Point2D.Float(mWidth,mHeight));
    	sPos.addStatePosition(nodeFinal, new Point2D.Float(mWidth*mNode.getSize()[0], mHeight));

        State end, ini;
        int x, y, size;
        float flo;
        
        size = ((ThompsonNode)mNode.getLeft()).getSize()[0];
        if(((ThompsonNode)mNode.getRight()).getSize()[0] > size)
            size = ((ThompsonNode)mNode.getRight()).getSize()[0];
        
        end = mAutomaton.findState(((ThompsonNode)mNode.getLeft()).getFinal());
        ini = mAutomaton.findState(((ThompsonNode)mNode.getLeft()).getInitial());
        Point2D pointEnd = sPos.getStatePosition(end);
        Point2D pointIni = sPos.getStatePosition(ini);
        flo = (float)(size-((ThompsonNode)mNode.getLeft()).getSize()[0])/2;
        x = (int)(pointIni.getX() - mWidth * (float)(2 + flo));
        y = (int)(mHeight*((int)(((ThompsonNode)mNode.getLeft()).getSize()[1]/2) + 2) - pointIni.getY());
        
        moveStates(ini, x, y);
        TransitionFA trans = new TransitionFA(new Terminal(TerminalEpsilon.EPSILON_CHARACTER),nodeInitial,ini);
    	nodeInitial.addTransitionOut(trans);
        trans = new TransitionFA(new Terminal(TerminalEpsilon.EPSILON_CHARACTER),end,nodeFinal);
        end.addTransitionOut(trans);
        
        end = mAutomaton.findState(((ThompsonNode)mNode.getRight()).getFinal());
        ini = mAutomaton.findState(((ThompsonNode)mNode.getRight()).getInitial());
        pointEnd = sPos.getStatePosition(end);
        pointIni = sPos.getStatePosition(ini);
        flo = (float)(size-((ThompsonNode)mNode.getRight()).getSize()[0])/2;
        x = (int)(pointIni.getX() - mWidth * (float)(2 + flo));
        y = (int)(pointIni.getY() - mHeight * ((int)(((ThompsonNode)mNode.getRight()).getSize()[1]/2) + 2));
        
        moveStates(ini, x, y);
        trans = new TransitionFA(new Terminal(TerminalEpsilon.EPSILON_CHARACTER),nodeInitial,ini);
    	nodeInitial.addTransitionOut(trans);
        trans = new TransitionFA(new Terminal(TerminalEpsilon.EPSILON_CHARACTER),end,nodeFinal);
        end.addTransitionOut(trans);
        
    }//buildSelect
    
    /**
     * Construye los nodos con asterisco.<br>
     * Crea dos nuevos estados y sus correspondientes transiciones, adem�s desplaza esas
     * transiciones por arriba y por abajo para que se muestren correctamente al usuario.
     */
    public void buildAsterisk () {
    	State nodeInitial = mAutomaton.createState(mNode.getInitial());
    	State nodeFinal = mAutomaton.createState(mNode.getFinal());
    	sPos.addStatePosition(nodeInitial, new Point2D.Float(mWidth,mHeight));
    	sPos.addStatePosition(nodeFinal, new Point2D.Float(mWidth*mNode.getSize()[0], mHeight));
        State end, ini;
        int x, y;
        
        ini = mAutomaton.findState(((ThompsonNode)mNode.getLeft()).getInitial());
        end = mAutomaton.findState(((ThompsonNode)mNode.getLeft()).getFinal());
        Point2D pointEnd = sPos.getStatePosition(end);
        Point2D pointIni = sPos.getStatePosition(ini);
        x = (int)(pointIni.getX() - mWidth * 2);
        y = (int)(mHeight - pointIni.getY());
        
        moveStates(ini, x, y);
        y = mHeight * (1 + ((ThompsonNode)mNode.getLeft()).getSize()[1] / 2);
        //nodeInitial a ini
        Transition trans = new TransitionFA(new Terminal(TerminalEpsilon.EPSILON_CHARACTER),nodeInitial,ini);
    	nodeInitial.addTransitionOut(trans);
        //nodeInitial a nodeFinal
    	trans = new TransitionFA(new Terminal(TerminalEpsilon.EPSILON_CHARACTER),nodeInitial,nodeFinal);
    	nodeInitial.addTransitionOut(trans);
    	this.cPointsYMove.put(new TransitionGroup(nodeInitial,nodeFinal), y);
        //end a nodefinal
        trans = new TransitionFA(new Terminal(TerminalEpsilon.EPSILON_CHARACTER),end,nodeFinal);
    	end.addTransitionOut(trans);
    	//end a ini
        trans = new TransitionFA(new Terminal(TerminalEpsilon.EPSILON_CHARACTER),end,ini);
    	end.addTransitionOut(trans);
    	this.cPointsYMove.put(new TransitionGroup(end,ini), -y);
    	
    }//buildAsterisk
    
    /**
     * Construye dos estado y la transici�n con el caracter que se le pasa entre ellos.
     * 
     * @param c Terminal que formar� la transici�n.
     */
    public void buildTerminal (char c) {
    	State nodeInitial = mAutomaton.createState(mNode.getInitial());
    	State nodeFinal = mAutomaton.createState(mNode.getFinal());
    	sPos.addStatePosition(nodeInitial, new Point2D.Float(mWidth,mHeight));
    	sPos.addStatePosition(nodeFinal, new Point2D.Float(mWidth*2, mHeight));
    	TransitionFA trans = new TransitionFA(new Terminal(c),nodeInitial,nodeFinal);
    	nodeInitial.addTransitionOut(trans);
        
    }//buildTerminal
    
    /**
     * Desplaza todos los estados que esten enlazados entre s� mediante
     * transiciones.
     * 
     * @param state Estado visual del que cuelgan todos los dem�s.
     * @param x Desplazamiento horizontal de los estados.
     * @param y Desplazamiento vertical de los estados. 
     */
    protected void moveStates (State state, int x, int y) {
        Vector<State> vector = new Vector<State>();
        Transition trans;
        vector.add(state);
        
        for(int i=0; i<vector.size(); i++){
            for(int j=0; j <vector.elementAt(i).getTransitionsOut().size(); j++){
                trans = vector.elementAt(i).getTransitionsOut().elementAt(j);
                if(!vector.contains(trans.getNextState()))
                    vector.add(trans.getNextState());
            }
            State st =vector.elementAt(i);
            Point2D oldPos = sPos.getStatePosition(st);
            sPos.addStatePosition(st, new Point2D.Double(oldPos.getX()-x,oldPos.getY()-y));
        }
        
    }//moveStates
    
    /**
	 * Crea el aut�mata visual.<br>
	 * Si la ventana seleccionada no contiene ning�n aut�mata lo crear�
	 * en esa pesta�a, si por el contrario ya tiene uno, a�adir� una nueva.
	 *
	 **/
	public void accept(){
		AutomatonSplitPane pane = new AutomatonSplitPane(model,mAutomaton);
		Application app = Application.getInstance();
		app.createTab(mExpression, pane);
        app.getCurrentTab().setChanges(true);
        
    }//accept
	
	/**
	 * Crea un objeto que mapea estados y las posiciones que ocupar�n los
	 * v�rtices que contendr�n dichos estados en el grafo.
	 * 
	 * @return Posicionador de estados
	 */
	private TransitionProperties buildControlPoints() {
		TransitionProperties tProp = new TransitionProperties();
		Set<TransitionGroup> set =cPointsYMove.keySet();
		for(TransitionGroup tG:set){
			Point2D source = sPos.getStatePosition(tG.getSource());
			Point2D target = sPos.getStatePosition(tG.getTarget());
			double x = (target.getX()+source.getX())/2 + ThothCellViewFactory.INIT_DEFAULT_WIDTH/2;
			double y = source.getY()+cPointsYMove.get(tG);
			tProp.addGroupPoint(tG, new Point2D.Double(x,y));
		}
		return tProp;
	}  

}//MediatorThompson 
