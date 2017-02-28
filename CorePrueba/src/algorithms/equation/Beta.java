package algorithms.equation;

/**
 * <b>Descripción</b><br>
 * Se encarga de representar el carácter beta en el método de las ecuaciones
 * características.
 * <p>
 * <b>Detalles</b><br>
 * Están representadas por un número que indica el grado de alfa..
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Beta.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * @see Equation
 */
public class Beta {
    
    // Attributes ------------------------------------------------------------------
    
    /**
     * Entero que representa el subíndice.
     */
    private Integer mBeta;
    
    /**
     * Símbolo beta.
     */
    public static final Character BETA = 223;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo.<br>
     * Se le pasa el índice.
     * 
     * @param index Subíndice de la beta.
     */
    public Beta(int index) {
        mBeta = new Integer(index);
        
    }//Beta
    
    /**
     * Devuelve el índice de la beta.
     * 
     * @return El índice de la beta.
     */
    public Integer getIndex () {
        
        return mBeta;
    }//getIndex
    
    /**
     * Compara dos betas en función de su índice.
     * 
     * @return True si son iguales y false en caso contrario.
     */
    public boolean equals (Object o) {
        
        return ((Beta)o).getIndex().equals(mBeta);
    }//equals
    
    /**
     * Devuelve una cadena con la beta acompañada de su índice.<br>
     * Si el índice es menor de 0 devolverá la cadena vacía.
     * 
     * @return La beta con su índice o la cadena vacía si su índice es menor
     * de cero.
     */
    public String toString () {
        if(mBeta < 0)
            return "";
        
        return " " + BETA.toString() + mBeta;
    }//toString

}//Beta
