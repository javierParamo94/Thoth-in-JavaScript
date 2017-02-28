package algorithms;

/**
 * <b>Descripci�n</b><br>
 * Indica que un aut�mata no tiene estado inicial.
 * <p>
 * <b>Detalles</b><br>
 * Esta excepci�n es lanzada cuando el aut�mata no tiene estado inicial. Debe ser
 * recogida por el entorno gr�fico y mostrar mensaje de error.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evitar fallos en tiempo de ejecuci�n en los algoritmos.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class InitializeException extends Exception {

    /**
     * Mensaje de error de la excepci�n si no tiene ni estado inicial ni final.
     */
    public static final String mError = "El aut�mata no tiene estado inicial ni uno final.";
    
    /**
     * Mensaje de error de la excepci�n si no tiene estado inicial.
     */
    public static final String mInitialError = "El aut�mata no tiene estado inicial.";
    
    /**
     * Mensaje de error de la excepci�n si no tiene estado final.
     */
    public static final String mFinalError = "El aut�mata no tiene un estado final.";
    
    /**
     * Constructor por defecto. 
     */
    public InitializeException (String message) {
        super(message);

    }//InitializeException

}//InitializeException
