package algorithms.regExp.ahosethi;

import java.util.Vector;

import algorithms.Algorithm;
import algorithms.tree.Node;

import core.State;
import core.Terminal;
import core.finiteautomaton.FiniteAutomaton;
import core.finiteautomaton.StateFA;

/**
 * <b>Descripci�n</b><br>
 * Construye un aut�mata finito determinista a partir de una expresi�n regular
 * dada.<br>
 * El aut�mata obtenido ser� determinista pero no m�nimo.
 * <p>
 * <b>Detalles</b><br>
 * Primero comprueba que el �rbol sint�ctico sea el ampliado. Despu�s calcula las
 * funciones: anulable, primera-pos, �ltima-pos y siguiente-pos apoy�ndose en las
 * clases Tree y Node que complementan su funcionalidad.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Desarrolla el m�todo de Aho-Sethi-Ullman para la construcci�n de un AFD.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * @see AhoTree
 * @see AhoNode
 */
public class AhoSethiUllman extends Algorithm {
    
    // Attributes ------------------------------------------------------------------

    /**
     * Expresi�n regular de la que queremos calcular su aut�mata representada en
     * forma de �rbol.
     */
    private AhoTree mExpReg;
    
    /**
     * Contador para ir asignando nombres a los estados del aut�mata.
     */
    private Integer mStatesName;
    
    /**
     * �ndice para saber qu� pos hemos marcado en cada paso.
     */
    private Integer mMark;
    
    /**
     * Almacenamos las pos de cada estado.
     */
    private Vector<Vector<Integer>> mPosGroup;
    
    /**
     * Almacena una lista con los estados a los que el estado actual va a ir con cada
     * terminal del alfabeto.
     */
    public Vector<State> mStates;
    
    // Methods ---------------------------------------------------------------------

    /**
     * Constructor completo del algoritmo.
     * 
     * @param expReg Expresi�n regular en forma de �rbol.
     */
    public AhoSethiUllman (AhoTree expReg){
        super();
        mExpReg = expReg;
        mStatesName = 0;
        mMark = 0;
        mPosGroup = new Vector<Vector<Integer>>();
           
    }//AhoSethiUllman 
    
    /**
     * Primer paso del algoritmo.<br>
     * Se encarga de inicializar los nodos seg�n si son o no anulables y
     * tambi�n inicializa las primera-pos y �ltimas-pos de los nodos del �rbol.<br>
     * Por �ltimo crea el aut�mata y su estado inicial.
     * 
     * @return True en caso de que el algoritmo deba continuar, falso en caso
     * contrario.
     */
    public boolean firstStep (){
        String label;
        if(!mExpReg.getRoot().getToken().equals('.'))
            return false;
        
        calculateCancel();
        mExpReg.getRoot().getFirst();
        mExpReg.getRoot().getLast();
        
            //Creamos el aut�mata, el estado inicial, si es o no final  y su etiqueta
        mAutomaton = new FiniteAutomaton("Aho Sethi Ullman", mExpReg.getAlphabet());
        mAutomaton.setInitialState(mAutomaton.createState(mStatesName.toString()));
        label = mExpReg.getRoot().getFirst().toString();
        mAutomaton.getInitialState().setLabel(label.substring(1, label.length() - 1));
        mAutomaton.getInitialState().setFinal(isEnd(mExpReg.getRoot().getFirst()));
        mPosGroup.add(0, mExpReg.getRoot().getFirst());
        
        return true;
    }//firstStep

    /**
     * Recorre el �rbol calculando si los nodos son o no anulables.
     */
    private void calculateCancel (){
        Vector<Node> pre = mExpReg.preOrden();
        
        for (int i=0; i<pre.size(); i++)
            ((AhoNode)pre.elementAt(i)).isCancel();
        
    }//calculateCancel
    
    /**
     * Calcula si en el vector de pos est� contenida la pos del s�mbolo de
     * final de entrada.
     * 
     * @param u Vector de pos.
     * @return True si el nuevo estado creado va a ser final.
     */
    private boolean isEnd (Vector<Integer> u){
        boolean flag = false;
        
        for (int i=0; i<u.size() && !flag; i++)
            if(u.elementAt(i).equals(
                    ((AhoNode)mExpReg.getRoot().getRight()).getFirst().firstElement()))
                flag = true;
        
        return flag;
    }//isEnd
    
    /**
     * Siguiente paso del algoritmo.<br>
     * Crea las transiciones del estado actual creando, si son necesarios, nuevos
     * estados que son el destino de la transici�n.
     * 
     * @return True si el algoritmo no ha finalizado y debe continuar, falso en 
     * caso contrario.
     */
    public boolean nextStep (){
        Vector<Terminal> alphabet = mAutomaton.getAlphabet();
        Vector<Integer> u = new Vector<Integer>(3, 3);
        mStates = new Vector<State>(alphabet.size(), 0);
        boolean flag;
        Integer name;
        
            //Si nos quedan estados sin marcar en mPos
        if(mMark < mPosGroup.size()){
                //Para cada terminal del alfabeto
            for(int i=0; i<alphabet.size(); i++){
                    //u <- nextPos(alphabet[i])
                u = mExpReg.nextPosition(alphabet.elementAt(i).getToken(), mPosGroup.elementAt(mMark));
                    //Si u no est� en mPos y no es vac�o a�adimos sin marcar
                    // las pos de u que no est�n en mPos.
                flag = true;
                name = mStatesName;
                for(int j=0; j<mPosGroup.size(); j++)
                    if(mPosGroup.elementAt(j).containsAll(u) && 
                            u.containsAll(mPosGroup.elementAt(j))){
                        flag = false;
                        name = j;
                    }
                    //Si no existe el estado le creamos.
                if(flag){
                    buildState(alphabet.elementAt(i), u);
                    mStates.add(mAutomaton.findState(mStatesName.toString()));
                }
                    //Crear transicion
                else{
                	((StateFA)mAutomaton.findState(mMark.toString())).createTransitionOut(alphabet.elementAt(i), 
                                mAutomaton.findState(name.toString()));
                    mStates.add(mAutomaton.findState(name.toString()));
                }
            }
                //Marcamos t
            mMark++;
            return true;
        }//if
        
        return false;
    }//nextStep

    /**
     * Se encarga de construir un nuevo estado ya que no ha sido creado todav�a.<br>
     * Se crean las transiciones de los estados que han sido creados hacia �l.
     * Asigna la etiqueta con las pos que agrupa.
     * 
     * @param ter Terminal con la entrada de la transici�n.
     * @param next Pos que va a agrupar el estado. 
     */
    private void buildState(Terminal ter, Vector<Integer> next){
        mStatesName++;
            //Creamos el estado
        mAutomaton.createState(mStatesName.toString(), isEnd(next));
        mPosGroup.add(mStatesName, next);
            //Asignamos la etiqueta con el nombre de los estados que ha agrupado.
        mAutomaton.findState(mStatesName.toString()).setLabel(next.toString().substring(1, next.toString().length() - 1));
        ((StateFA)mAutomaton.findState(mMark.toString())).createTransitionOut(ter, 
                mAutomaton.findState(mStatesName.toString()));

    }//buildState

    /**
     * Ejecuta todos los pasos del algoritmo.<br>
     * 
     * @return True si el aut�mata ha sido creado con �xito, falso en caso contrario.
     */
    public boolean allSteps (){
        if(firstStep())
            while(nextStep());
        
        if(mAutomaton != null)
            return true;
        
        return false;
    }//allSteps
    
    /**
     * Devuelve el �bol del cual se va a obtener el aut�mata.
     * 
     * return AhoTree con el que se va a calcular el aut�mata.
     */
    public AhoTree getTree () {
        
        return mExpReg;
    }//getTree
    
    /**
     * Devuelve el nuevo automata determinista constru�do a partir de la expresi�n
     * regular.
     * 
     * @return El aut�mata creado.
     */
    public FiniteAutomaton getSolution(){

        return (FiniteAutomaton)mAutomaton;
    }//getSolution

}//AhoSethiUllman 
