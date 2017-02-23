package core;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de almacenar los s�mbolos usados por el core.
 * <p>
 * <b>Detalles</b><br>
 * Almacena un s�mbolo y proporciona m�todos de acceso de utilidad.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Encapsula los s�mbolos y normaliza su acceso.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public abstract class Symbol {
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Compara si un s�mbolo es igual a otro en funci�n del token.
     * 
     * @param symbol S�mbolo con el que va a ser comparado.
     * @return True si son iguales y false en caso contrario.
     */
    public abstract boolean equals (Object symbol);
    
    /**
     * Devuelve un String con el s�mbolo.
     * 
     * @return String con el s�mbolo.
     */
    public abstract String toString ();
    
}//Symbol

