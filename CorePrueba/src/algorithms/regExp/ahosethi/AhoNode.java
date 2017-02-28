package algorithms.regExp.ahosethi;

import java.util.Vector;

import algorithms.tree.Node;

import core.TerminalEpsilon;

/**
 * <b>Descripción</b><br>
 * Representa cada uno de los nodos del árbol sintáctico de Aho-Sethi-Ullman.
 * <p>
 * <b>Detalles</b><br>
 * Aumenta la funcionalidad del tipo abstracto de datos nodo calculando la
 * anulabilidad, la primera-pos y la última-pos de los nodos.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Representación del nodo del árbol sintáctico de Aho-Sethi-Ullman
 * Calcula la anulabilidad, primera-pos y última-pos.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * @see AhoTree
 */
public class AhoNode extends Node {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Indica la primera posición del nodo.
     */
    private Vector<Integer> mFirstPos;
    
    /**
     * Indica la última posición del nodo.
     */
    private Vector<Integer> mLastPos;
    
    /**
     * Indica si el nodo es anulable.
     */
    private boolean mCancel;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico de Nodo.<br>
     * Si el terminal del nodo es épsilon el valor de cancelación será verdadero.
     * 
     * @param c Indica el caracter que irá asignado al nodo.
     */
    public AhoNode (Character c) {
        super(c);
        mFirstPos = new Vector<Integer>(3, 3);
        mLastPos = new Vector<Integer>(3, 3);
        mCancel = false;
            //Si es épsilon -> mCancel=true
        if(mToken == TerminalEpsilon.EPSILON_CHARACTER)
            mCancel = true;
        else
            mCancel = false;
        
    }//AhoNode
    
    /**
     * Constructor para un nodo hoja.<br>
     * Sus hijos derecho e izquierdo serán nulos y le debemos asignar la primera
     * posición y la última posición ya que no va a cambiar.<br>
     * 
     * @param c Indica el caracter que irá asignado al nodo. 
     * @param pos Primera y última posición del nodo(ambas son iguales en los hoja).
     */
    public AhoNode (Character c, Integer pos) {
        super(c);
        mFirstPos = new Vector<Integer>(1, 0);
        mLastPos = new Vector<Integer>(1, 0);
        mFirstPos.add (0,pos);
        mLastPos.add (0,pos);
        
    }//AhoNode
    
    /**
     * Constructor del nodo especificando sus hijos.
     * 
     * @param c Indica el carácter que irá asignado al nodo.
     * @param left Hijo izquierdo del nodo.
     * @param right Hijo derecho del nodo.
     */
    public AhoNode (Character c, AhoNode left, AhoNode right){
        super(c, left, right);
        mFirstPos = new Vector<Integer>(3, 3);
        mLastPos = new Vector<Integer>(3, 3);
        
    }//AhoNode
    
    /**
     * Devuelve el vector de primera-pos del nodo.<br>
     * Si no ha sido calculada la primera-pos la calcula primero.
     * 
     * @return La primera-pos del nodo.
     */
    public Vector<Integer> getFirst (){
        if(mFirstPos.isEmpty())
            calculateFirst();
    
        return mFirstPos;
    }//getFirst
  
    /**
     * Calcula la primera-pos del nodo en función de sus hijos.<br>
     * Es una función recursiva invocando a la primera-pos de sus hijos.
     */
    private void calculateFirst (){
        AhoNode left = (AhoNode)mLeft;
        AhoNode right = (AhoNode)mRight;
        
        if(!mLeaf)
            switch(mToken){
            case '|':
                left.calculateFirst();
                right.calculateFirst();
                mFirstPos.addAll(left.getFirst());
                mFirstPos.removeAll(right.getFirst());
                mFirstPos.addAll(right.getFirst());
                break;
            case '.':
                left.calculateFirst();
                right.calculateFirst();
                if(left.isCancel()){
                    mFirstPos.addAll(left.getFirst());
                    mFirstPos.removeAll(right.getFirst());
                    mFirstPos.addAll(right.getFirst());
                }
                else
                    mFirstPos.addAll(left.getFirst());
                break;
            case '*':
                left.calculateFirst();
                mFirstPos.addAll(left.getFirst());
                break;
            }
        
    }//calculateFirst
    
    /**
     * Devuelve el vector de última pos del nodo.<br>
     * Antes calcula su última pos.
     * 
     * @return Las últimas pos del nodo.
     */
    public Vector<Integer> getLast (){
        if(mLastPos.isEmpty())
            calculateLast();
        
        return mLastPos;
    }//getLast
  
    /**
     * Calcula las últimas posiciones del nodo en función de sus hijos.<br>
     * Es una función recursiva invocando a la última-pos de sus hijos.
     */
    private void calculateLast (){
        AhoNode left = (AhoNode)mLeft;
        AhoNode right = (AhoNode)mRight;

        if(!mLeaf)
            switch(mToken){
                case '|':
                    left.calculateLast();
                    right.calculateLast();
                    mLastPos.addAll(left.getLast());
                    mLastPos.removeAll(right.getLast());
                    mLastPos.addAll(right.getLast());
                    break;
                case '.':
                    left.calculateLast();
                    right.calculateLast();
                    if(right.isCancel()){
                        mLastPos.addAll(left.getLast());
                        mLastPos.removeAll(right.getLast());
                        mLastPos.addAll(right.getLast());
                    }
                    else
                        mLastPos.addAll(right.getLast());
                    break;
                case '*':
                    left.calculateLast();
                    mLastPos.addAll(left.getLast());
                    break;
            }
        
    }//calculateLast
    
    /**
     * Calcula si es cancelable o no.<br>
     * También acutaliza el valor de cancelación del nodo.<br>
     * Funcion recursiva calculando si sus hijos son cancelables.
     * 
     * @return True si es cancelable y false en caso contrario
     */
    public boolean isCancel (){
        AhoNode left = (AhoNode)mLeft;
        AhoNode right = (AhoNode)mRight;
        
        switch(mToken){
            case '*':
            case TerminalEpsilon.EPSILON_CHARACTER:
                mCancel = true; 
                break;
            case '|':
                if(left.isCancel() || right.isCancel())
                    mCancel = true;
                break;
            case '.':
                if(left.isCancel() && right.isCancel())
                    mCancel = true;
                break;
            default:
                mCancel = false;
        }
        
        return mCancel;
    }//isCancel
 
}//AhoNode
