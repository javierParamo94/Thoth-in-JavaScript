package algorithms.regExp.derive;

import algorithms.tree.Node;

/**
 * <b>Descripci�n</b><br>
 * Representa cada uno de los nodos del �rbol sint�ctico usado para el m�todo de las
 * derivadas.
 * <p>
 * <b>Detalles</b><br>
 * Aumenta la funcionalidad del tipo abstracto de datos nodo.<br>
 * Acumula en cada nodo la expresi�n regular asociada al nodo.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Representaci�n del nodo del �rbol sint�ctico de Derive.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * @see DeriveTree
 * @see Node
 */public class DeriveNode extends Node {

    // Attributes --------------------------------------------------------------------
    
    /**
     * Expresi�n regular asociada al nodo del �rbol.
     */
    private String mRegExp;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor para un nodo hoja.
     * 
     * @param c Car�cter del nodo.
     */
    public DeriveNode(Character c) {
        super(c);
        mRegExp = c.toString();
        
    }//DeriveNode

    /**
     * Constructor completo para nodos.<br>
     * Como m�nimo se le debe pasar el hijo izquierdo, siendo el derecho opcional.
     * 
     * @param c Car�cter del nodo.
     * @param left Hijo izquierdo.
     * @param right Hijo derecho.
     */
    public DeriveNode(Character c, DeriveNode left, DeriveNode right) {
        super(c, left, right);
        
        switch(c){
            case '|':
                mRegExp = left.getRegExp() + c + right.getRegExp();
                break;
            case '.':
                if(left.isLeaf())
                    mRegExp = left.getRegExp();
                else
                    mRegExp = "(" + left.getRegExp() + ")";
                if(right.isLeaf())
                    mRegExp = mRegExp + right.getRegExp();
                else
                    mRegExp = mRegExp + "(" + right.getRegExp() + ")";
                break;
            case '*':
                if(left.isLeaf())
                    mRegExp = left.getRegExp() + c;
                else
                    mRegExp = "(" + left.getRegExp() + ")" + c;
                break;
            default:
                mRegExp = c.toString();
        } 
        
    }//DeriveNode
    
    /**
     * Devuelve la expresi�n regular del nodo.
     * 
     * @return Expresi�n regular asociada al nodo.
     */
    public String getRegExp () {
        
        return mRegExp;
    }//getRegExp

}//DeriveNode
