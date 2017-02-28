package algorithms.equation;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de representar el car�cter beta en el m�todo de las ecuaciones
 * caracter�sticas.
 * <p>
 * <b>Detalles</b><br>
 * Est�n representadas por un n�mero que indica el grado de alfa..
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Beta.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * @see Equation
 */
public class Beta {
    
    // Attributes ------------------------------------------------------------------
    
    /**
     * Entero que representa el sub�ndice.
     */
    private Integer mBeta;
    
    /**
     * S�mbolo beta.
     */
    public static final Character BETA = 223;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo.<br>
     * Se le pasa el �ndice.
     * 
     * @param index Sub�ndice de la beta.
     */
    public Beta(int index) {
        mBeta = new Integer(index);
        
    }//Beta
    
    /**
     * Devuelve el �ndice de la beta.
     * 
     * @return El �ndice de la beta.
     */
    public Integer getIndex () {
        
        return mBeta;
    }//getIndex
    
    /**
     * Compara dos betas en funci�n de su �ndice.
     * 
     * @return True si son iguales y false en caso contrario.
     */
    public boolean equals (Object o) {
        
        return ((Beta)o).getIndex().equals(mBeta);
    }//equals
    
    /**
     * Devuelve una cadena con la beta acompa�ada de su �ndice.<br>
     * Si el �ndice es menor de 0 devolver� la cadena vac�a.
     * 
     * @return La beta con su �ndice o la cadena vac�a si su �ndice es menor
     * de cero.
     */
    public String toString () {
        if(mBeta < 0)
            return "";
        
        return " " + BETA.toString() + mBeta;
    }//toString

}//Beta
