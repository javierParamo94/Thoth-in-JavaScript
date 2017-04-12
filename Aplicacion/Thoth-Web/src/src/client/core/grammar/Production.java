package src.client.core.grammar;

import java.util.Vector;

import src.client.core.Symbol;
import src.client.core.TerminalEpsilon;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * <b>Descripción</b><br>
 * Se encarga de proporcionar la funcionalidad de las producciones de una gramática.
 * <p>
 * <b>Detalles</b><br>
 * Nos proporciona métodos de acceso a los datos de la producción. 
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo abstracto de datos Producción de Gramática.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class Production implements IsSerializable {
    
    //  Attributes ------------------------------------------------------------------
    
    /**
     * Parte izquierda de la producción.
     */
    private Vector<Symbol> mLeft; 
    
    /**
     * Parte derecha de la producción.
     */
    private Vector<Symbol> mRight; 
    
    /**
     * Etiqueta de la producción en XML.
     */
    public static String mLabelXML = "production"; 
        
    // Methods ---------------------------------------------------------------------
    
    public Production () {
    	mLeft = new Vector<Symbol>(1, 0);
    	mRight = new Vector<Symbol>(1, 0);
    }
    /**
     * Constructor básico.<br>
     * Si sólo se le pasa la parte izquierda asume que la parte derecha es epsilon.
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
     * Si el consecuente de la producción está vacío, por defecto valdrá
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
     * Devuelve la parte izquierda de la producción.
     * 
     * @return Parte izquierda de la producción.
     */
    public Vector<Symbol> getLeft () {
        
        return mLeft;
    }//getLeft
    
    /**
     * Establece la parte izquierda de la producción.
     * 
     * @param left Parte izquierda que queremos almacenar.
     */
    public void setLeft (Vector<Symbol> left) {
        mLeft = left;
        
    }//setLeft
    
    /**
     * Devuelve la parte derecha de la producción.
     * 
     * @return Parte derecha de la producción.
     */
    public Vector<Symbol> getRight () {
        
        return mRight;
    }//getRight
    
    /**
     * Establece la parte derecha de la producción.
     * 
     * @param right Parte derecha que queremos almacenar.
     */
    public void setRight (Vector<Symbol> right) {
        mRight = right;
        
    }//setRight
    
    /**
     * Devuelve un String con la información de la producción en formato XML.
     * 
     * @return String Contiene la información de la producción.
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
     * Devuelve un String con el contenido del vector sin corchetes y sin ningún
     * tipo de separación.<br>
     * Es usado en el método getProductionXML.
     * 
     * @param v Vector de símbolos del que queremos obtener un String.
     * @return String con los símbolos del vector.
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
     * Transforma la producción a un String
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
