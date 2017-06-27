package algorithms.equation;

import java.util.Vector;

import algorithms.regExp.derive.DeriveNode;

import core.TerminalEmpty;
import core.TerminalEpsilon;

/**
 * <b>Descripción</b><br>
 * Representa cada una de las ecuaciones que forman el método de las equaciones
 * características.
 * <p>
 * <b>Detalles</b><br>
 * Están formadas a su vez por expresiones regulares, éstas se representan mediante
 * nodos del árbol de derivación.<br>
 * Cada expresión regular tendrá asignada su correspodiente beta.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Equación.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class Equation {
    
    // Attributes ------------------------------------------------------------------
    
    /**
     * Parte izquierda de la ecuación.
     */
    private Beta mLeft;
    
    /**
     * Contiene todas las expresiones regulares de la parte derecha.
     */
    private Vector<DeriveNode> mExp;
    
    /**
     * Contiene las betas (índices) de la parte derecha.
     */
    private Vector<Beta> mDegree;
    
    /**
     * Número de expresiones regulares que tiene la ecuación característica.
     */
    private int mSize;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo de la ecuación.<br>
     * Inicializa la parte izquierda de la ecuación.
     * 
     * @param index Índice de la beta que va a ir a la parte izquierda de la ecuación.
     */
    public Equation (int index) {
        mLeft = new Beta(index);
        mExp = new Vector<DeriveNode>(3, 3);
        mDegree = new Vector<Beta>(3, 3);
        mSize = 0;
        
    }//Equation
    
    /**
     * Devuelve la parte izquierda de la ecuación característica.
     * 
     * @return Parte izquierda de la ecuación.
     */
    public Beta getLeft () {
        
        return mLeft;
    }//getLeft
    
    /**
     * Devuelve la expresión regular situada en el índice que se le pasa.<br>
     * Si el índice no es válido devolverá nulo.
     * 
     * @param i Índice de la expresión regular solicitada.
     * @return Expresión regular si existe en dicho índice y null en otro caso.
     */
    public DeriveNode getExpAt (int i) {
        if(i < mSize)
            return mExp.elementAt(i);
        
        return null;
    }//getExpAt
    
    /**
     * Establece una expresión regular de la ecuación.<br>
     * Si i es mayor que el número de expresiones regulares el método no tendrá efecto.
     * 
     * @param i Índice de la expresión regular a modificar.
     * @param node Nueva expresión regular situada en i.
     */
    public void setExp (int i, DeriveNode node) {
        if(i < mSize)
            mExp.set(i, node);
        
    }//setExp
    
    /**
     * Devuelve el grado de un sumando de la ecuación.
     * 
     * @param i Índice del sumando solicitado.
     * @return Sumando si existe en dicho índice y null en otro caso.
     */
    public Beta getBetaAt (int i) {
        if(i < mSize)
            return mDegree.elementAt(i);
        
        return null;
    }//getBetaAt
    
    /**
     * Establece el grado de un sumando de la ecuación.<br>
     * Si i es mayor que el número de sumandos el método no tendrá efecto.
     * 
     * @param i Índice del sumando a modificar.
     * @param beta Nuevo grado del sumando situado en i.
     */
    public void setBeta (int i, Beta beta) {
        if(i < mSize)
            mDegree.set(i, beta);
        
    }//setBeta
    
    /**
     * Devuelve el tamaño de la ecuación característica.<br>
     * Es decir, el número de expresiones regulares que tiene. 
     * 
     * @return Tamaño de la ecuación característica.
     */
    public int getSize () {
        
        return mSize;
    }//getSize
    
    /**
     * Devuelve true si la solución de la ecuación es el lenguaje vacío.
     * 
     * @return True si la parte derecha de la ecuación está vacía.
     */
    public boolean isEmpty () {
        
        return mExp.isEmpty();
    }//isEmpty
    
    /**
     * Aumenta la ecuación característica añadiéndola la expresión regular que se le
     * pasa con su correspondiente índice/grado.
     * 
     * @param node Expresión regular a añadir.
     * @param index Índice.
     */
    public void addExpression (DeriveNode node, int index) {
        mExp.add(node);
        mDegree.add(new Beta(index));
        mSize++;
        group();
        
    }//addExpresion
    
    /**
     * Borra una expresión regular.
     * 
     * @param index Índice de la expresión regular dentro de la ecuación.
     */
    public void removeExpression (int index) {
        mExp.remove(index);
        mDegree.remove(index);
        mSize = mExp.size();
        
    }//removeExpresion
    
    /**
     * Agrupa las expresiones que tengan el mismo grado (beta).
     */
    public void group () {
        DeriveNode temp;
        
        for(int i=0; i<mSize; i++)
            for(int j=i+1; j<mSize; j++){
                if(mDegree.elementAt(i).equals(mDegree.elementAt(j))){
                    temp = new DeriveNode('|', mExp.elementAt(i), mExp.elementAt(j));
                    temp = CharacteristicEquation.simplify(temp);
                    mExp.set(i, temp);
                    removeExpression(j);
                    j--;
                }
            }
        
    }//group
    
    /**
     * Aplica el lema de Arden a esta ecuación característica.<br>
     * Consiste en transformar una ecuación de la forma X = AX | B a su solución
     * única X = A*B.<br>
     * Si no se puede aplicar el lema este método no surtirá efecto.
     */
    public void arden () {
        DeriveNode temp, temp2;
            
        for(int i=0; i<mSize; i++)
            if(mDegree.elementAt(i).equals(mLeft)){
                    //Si B=vacío
                if(mSize == 1){
                    removeExpression(i);
                    addExpression(new DeriveNode(TerminalEmpty.EMPTY_CHARACTER), -2);
                }   //Si se puede aplicar Arden
                else{
                    temp = new DeriveNode('*', mExp.elementAt(i), null);
                    temp = CharacteristicEquation.simplify(temp);
                    removeExpression(i);
                    i--;
                    for(int j=0; j<mSize; j++){
                        if(mExp.elementAt(j).getToken() == TerminalEpsilon.EPSILON_CHARACTER)
                            temp2 = temp;
                        else
                            temp2 = new DeriveNode('.', temp, mExp.elementAt(j));
                        temp2 = CharacteristicEquation.simplify(temp2);
                        mExp.set(j, temp2);
                    }
                }
            }
        
    }//arden
    
    /**
     * Devuelve el contenido de la ecuación característica de forma que el usuario 
     * pueda enterdelo.
     * 
     * @return String con el contenido de la ecuación.
     */
    public String toString () {
        String solution;
        
        solution = mLeft.toString() + " = ";
        if(mSize>0)
            solution += mExp.firstElement().getRegExp() + mDegree.firstElement().toString();
        
        for(int i=1; i<mSize; i++)
            solution += " | " + mExp.elementAt(i).getRegExp() + mDegree.elementAt(i).toString();;
        
        return solution;
    }//toString
    
}//Equation
