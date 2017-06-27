package algorithms.equation;

import java.util.Vector;

import algorithms.regExp.derive.DeriveNode;

import core.TerminalEmpty;
import core.TerminalEpsilon;

/**
 * <b>Descripci�n</b><br>
 * Representa cada una de las ecuaciones que forman el m�todo de las equaciones
 * caracter�sticas.
 * <p>
 * <b>Detalles</b><br>
 * Est�n formadas a su vez por expresiones regulares, �stas se representan mediante
 * nodos del �rbol de derivaci�n.<br>
 * Cada expresi�n regular tendr� asignada su correspodiente beta.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Equaci�n.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class Equation {
    
    // Attributes ------------------------------------------------------------------
    
    /**
     * Parte izquierda de la ecuaci�n.
     */
    private Beta mLeft;
    
    /**
     * Contiene todas las expresiones regulares de la parte derecha.
     */
    private Vector<DeriveNode> mExp;
    
    /**
     * Contiene las betas (�ndices) de la parte derecha.
     */
    private Vector<Beta> mDegree;
    
    /**
     * N�mero de expresiones regulares que tiene la ecuaci�n caracter�stica.
     */
    private int mSize;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo de la ecuaci�n.<br>
     * Inicializa la parte izquierda de la ecuaci�n.
     * 
     * @param index �ndice de la beta que va a ir a la parte izquierda de la ecuaci�n.
     */
    public Equation (int index) {
        mLeft = new Beta(index);
        mExp = new Vector<DeriveNode>(3, 3);
        mDegree = new Vector<Beta>(3, 3);
        mSize = 0;
        
    }//Equation
    
    /**
     * Devuelve la parte izquierda de la ecuaci�n caracter�stica.
     * 
     * @return Parte izquierda de la ecuaci�n.
     */
    public Beta getLeft () {
        
        return mLeft;
    }//getLeft
    
    /**
     * Devuelve la expresi�n regular situada en el �ndice que se le pasa.<br>
     * Si el �ndice no es v�lido devolver� nulo.
     * 
     * @param i �ndice de la expresi�n regular solicitada.
     * @return Expresi�n regular si existe en dicho �ndice y null en otro caso.
     */
    public DeriveNode getExpAt (int i) {
        if(i < mSize)
            return mExp.elementAt(i);
        
        return null;
    }//getExpAt
    
    /**
     * Establece una expresi�n regular de la ecuaci�n.<br>
     * Si i es mayor que el n�mero de expresiones regulares el m�todo no tendr� efecto.
     * 
     * @param i �ndice de la expresi�n regular a modificar.
     * @param node Nueva expresi�n regular situada en i.
     */
    public void setExp (int i, DeriveNode node) {
        if(i < mSize)
            mExp.set(i, node);
        
    }//setExp
    
    /**
     * Devuelve el grado de un sumando de la ecuaci�n.
     * 
     * @param i �ndice del sumando solicitado.
     * @return Sumando si existe en dicho �ndice y null en otro caso.
     */
    public Beta getBetaAt (int i) {
        if(i < mSize)
            return mDegree.elementAt(i);
        
        return null;
    }//getBetaAt
    
    /**
     * Establece el grado de un sumando de la ecuaci�n.<br>
     * Si i es mayor que el n�mero de sumandos el m�todo no tendr� efecto.
     * 
     * @param i �ndice del sumando a modificar.
     * @param beta Nuevo grado del sumando situado en i.
     */
    public void setBeta (int i, Beta beta) {
        if(i < mSize)
            mDegree.set(i, beta);
        
    }//setBeta
    
    /**
     * Devuelve el tama�o de la ecuaci�n caracter�stica.<br>
     * Es decir, el n�mero de expresiones regulares que tiene. 
     * 
     * @return Tama�o de la ecuaci�n caracter�stica.
     */
    public int getSize () {
        
        return mSize;
    }//getSize
    
    /**
     * Devuelve true si la soluci�n de la ecuaci�n es el lenguaje vac�o.
     * 
     * @return True si la parte derecha de la ecuaci�n est� vac�a.
     */
    public boolean isEmpty () {
        
        return mExp.isEmpty();
    }//isEmpty
    
    /**
     * Aumenta la ecuaci�n caracter�stica a�adi�ndola la expresi�n regular que se le
     * pasa con su correspondiente �ndice/grado.
     * 
     * @param node Expresi�n regular a a�adir.
     * @param index �ndice.
     */
    public void addExpression (DeriveNode node, int index) {
        mExp.add(node);
        mDegree.add(new Beta(index));
        mSize++;
        group();
        
    }//addExpresion
    
    /**
     * Borra una expresi�n regular.
     * 
     * @param index �ndice de la expresi�n regular dentro de la ecuaci�n.
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
     * Aplica el lema de Arden a esta ecuaci�n caracter�stica.<br>
     * Consiste en transformar una ecuaci�n de la forma X = AX | B a su soluci�n
     * �nica X = A*B.<br>
     * Si no se puede aplicar el lema este m�todo no surtir� efecto.
     */
    public void arden () {
        DeriveNode temp, temp2;
            
        for(int i=0; i<mSize; i++)
            if(mDegree.elementAt(i).equals(mLeft)){
                    //Si B=vac�o
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
     * Devuelve el contenido de la ecuaci�n caracter�stica de forma que el usuario 
     * pueda enterdelo.
     * 
     * @return String con el contenido de la ecuaci�n.
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
