package algorithms.regExp.ahosethi;

import java.util.Vector;

import algorithms.tree.Node;

import core.TerminalEpsilon;

/**
 * <b>Descripci�n</b><br>
 * Representa cada uno de los nodos del �rbol sint�ctico de Aho-Sethi-Ullman.
 * <p>
 * <b>Detalles</b><br>
 * Aumenta la funcionalidad del tipo abstracto de datos nodo calculando la
 * anulabilidad, la primera-pos y la �ltima-pos de los nodos.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Representaci�n del nodo del �rbol sint�ctico de Aho-Sethi-Ullman
 * Calcula la anulabilidad, primera-pos y �ltima-pos.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * @see AhoTree
 */
public class AhoNode extends Node {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Indica la primera posici�n del nodo.
     */
    private Vector<Integer> mFirstPos;
    
    /**
     * Indica la �ltima posici�n del nodo.
     */
    private Vector<Integer> mLastPos;
    
    /**
     * Indica si el nodo es anulable.
     */
    private boolean mCancel;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico de Nodo.<br>
     * Si el terminal del nodo es �psilon el valor de cancelaci�n ser� verdadero.
     * 
     * @param c Indica el caracter que ir� asignado al nodo.
     */
    public AhoNode (Character c) {
        super(c);
        mFirstPos = new Vector<Integer>(3, 3);
        mLastPos = new Vector<Integer>(3, 3);
        mCancel = false;
            //Si es �psilon -> mCancel=true
        if(mToken == TerminalEpsilon.EPSILON_CHARACTER)
            mCancel = true;
        else
            mCancel = false;
        
    }//AhoNode
    
    /**
     * Constructor para un nodo hoja.<br>
     * Sus hijos derecho e izquierdo ser�n nulos y le debemos asignar la primera
     * posici�n y la �ltima posici�n ya que no va a cambiar.<br>
     * 
     * @param c Indica el caracter que ir� asignado al nodo. 
     * @param pos Primera y �ltima posici�n del nodo(ambas son iguales en los hoja).
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
     * @param c Indica el car�cter que ir� asignado al nodo.
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
     * Calcula la primera-pos del nodo en funci�n de sus hijos.<br>
     * Es una funci�n recursiva invocando a la primera-pos de sus hijos.
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
     * Devuelve el vector de �ltima pos del nodo.<br>
     * Antes calcula su �ltima pos.
     * 
     * @return Las �ltimas pos del nodo.
     */
    public Vector<Integer> getLast (){
        if(mLastPos.isEmpty())
            calculateLast();
        
        return mLastPos;
    }//getLast
  
    /**
     * Calcula las �ltimas posiciones del nodo en funci�n de sus hijos.<br>
     * Es una funci�n recursiva invocando a la �ltima-pos de sus hijos.
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
     * Tambi�n acutaliza el valor de cancelaci�n del nodo.<br>
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
