package algorithms;

/**
 * <b>Descripción</b><br>
 * Indica que un autómata no tiene estado inicial.
 * <p>
 * <b>Detalles</b><br>
 * Esta excepción es lanzada cuando el autómata no tiene estado inicial. Debe ser
 * recogida por el entorno gráfico y mostrar mensaje de error.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Evitar fallos en tiempo de ejecución en los algoritmos.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class InitializeException extends Exception {

    /**
     * Mensaje de error de la excepción si no tiene ni estado inicial ni final.
     */
    public static final String mError = "El autómata no tiene estado inicial ni uno final.";
    
    /**
     * Mensaje de error de la excepción si no tiene estado inicial.
     */
    public static final String mInitialError = "El autómata no tiene estado inicial.";
    
    /**
     * Mensaje de error de la excepción si no tiene estado final.
     */
    public static final String mFinalError = "El autómata no tiene un estado final.";
    
    /**
     * Constructor por defecto. 
     */
    public InitializeException (String message) {
        super(message);

    }//InitializeException

}//InitializeException
