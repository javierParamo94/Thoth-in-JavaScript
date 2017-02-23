package core.grammar;

import java.util.Vector;
import core.*;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de proporcionar la funcionalidad de las producciones de una gram�tica.
 * <p>
 * <b>Detalles</b><br>
 * Nos proporciona m�todos de acceso a los datos de la producci�n. 
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo abstracto de datos Producci�n de Gram�tica.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class Production {
    
    //  Attributes ------------------------------------------------------------------
    
    /**
     * Parte izquierda de la producci�n.
     */
    private Vector<Symbol> mLeft; 
    
    /**
     * Parte derecha de la producci�n.
     */
    private Vector<Symbol> mRight; 
    
    /**
     * Etiqueta de la producci�n en XML.
     */
    public static String mLabelXML = "production"; 
        
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.<br>
     * Si s�lo se le pasa la parte izquierda asume que la parte derecha es epsilon.
     * 
     * @param left Indica la parte izquierda.
     */
    public Production (Vector<Symbol> left) {
        mLeft = left;
        mRight = new Vector<Symbol>(1, 0);
        mRight.add(new TerminalEpsilon());
        
    }//Production
    
    /**
     * Constructor completo.<br>
     * Si el consecuente de la producci�n est� vac�o, por defecto valdr�
     * epsilon.
     * 
     * @param left Indica la parte izquierda.
     * @param right Indica la parte derecha.
     */
    public Production (Vector<Symbol> left, Vector<Symbol> right) {
        mLeft = left;
        if(right.isEmpty()){
            mRight = new Vector<Symbol>(1, 0);
            mRight.add(new TerminalEpsilon());
        }
        else
            mRight = right;
        
    }//Production
    
    /**
     * Devuelve la parte izquierda de la producci�n.
     * 
     * @return Parte izquierda de la producci�n.
     */
    public Vector<Symbol> getLeft () {
        
        return mLeft;
    }//getLeft
    
    /**
     * Establece la parte izquierda de la producci�n.
     * 
     * @param left Parte izquierda que queremos almacenar.
     */
    public void setLeft (Vector<Symbol> left) {
        mLeft = left;
        
    }//setLeft
    
    /**
     * Devuelve la parte derecha de la producci�n.
     * 
     * @return Parte derecha de la producci�n.
     */
    public Vector<Symbol> getRight () {
        
        return mRight;
    }//getRight
    
    /**
     * Establece la parte derecha de la producci�n.
     * 
     * @param right Parte derecha que queremos almacenar.
     */
    public void setRight (Vector<Symbol> right) {
        mRight = right;
        
    }//setRight
    
    /**
     * Devuelve un String con la informaci�n de la producci�n en formato XML.
     * 
     * @return String Contiene la informaci�n de la producci�n.
     */
    public String getProductionXML (){
        String file;
         
        if(mRight.firstElement().getClass().equals(TerminalEpsilon.class))
            file = "\t<"+Production.mLabelXML+"\n"+
                    "\t\tleft=\""+getString(mLeft)+"\"/>\n";
        else
            file = "\t<"+Production.mLabelXML+"\n"+
                    "\t\tleft=\""+getString(mLeft)+"\"\n"+
                    "\t\tright=\""+getString(mRight)+"\"/>\n";
         
        return file;
    }//getProductionXML
    
    /**
     * Devuelve un String con el contenido del vector sin corchetes y sin ning�n
     * tipo de separaci�n.<br>
     * Es usado en el m�todo getProductionXML.
     * 
     * @param v Vector de s�mbolos del que queremos obtener un String.
     * @return String con los s�mbolos del vector.
     */
    private String getString (Vector<Symbol> v) {
        String tmp = new String();
        
        for(int i=0; i<v.size(); i++)
            tmp = tmp.concat(v.elementAt(i).toString()+" ");
         
        return tmp.substring(0, tmp.length()-1);
    }//getString
    
    /**
     * Compara si una produccion es igual a otra.<br>
     * Son iguales si y solo si cada una de las partes son iguales.
     * 
     * @param o Produccion a comparar.
     * @return true si son iguales, false en caso contrario.
     */
    public boolean equals (Object o) {        
        if(mLeft.equals(((Production)o).getLeft()) &&
                mRight.equals(((Production)o).getRight()))
            return true;
        
        return false;
    }//equals

    /**
     * Clona la produccion y la devuelve.
     * 
     * @return Produccion clonada.
     */
    public Production clone (){
        Vector<Symbol> left = new Vector<Symbol>(3, 3),
                       right = new Vector<Symbol>(3, 3);
        
        for(int i=0; i<mLeft.size(); i++)
            left.add(mLeft.elementAt(i));
        for(int i=0; i<mRight.size(); i++)
            right.add(mRight.elementAt(i));
        
        return new Production(left, right);
    }//clone

    /**
     * Transforma la producci�n a un String
     * 
     * @return Produccion en forma de cadena de caracteres.
     */
    public String toString () {
        String temp = new String();
        
        for(int i=0; i<mLeft.size(); i++)
            temp = temp.concat(mLeft.elementAt(i).toString()+" ");
        temp = temp.concat(" : ");
        for(int i=0; i<mRight.size(); i++)
            temp = temp.concat(mRight.elementAt(i).toString()+" ");
        temp = temp.concat(";\n");
        
        return temp;
    }//toString

}//Production
