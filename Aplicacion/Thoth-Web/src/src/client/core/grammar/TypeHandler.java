package src.client.core.grammar;

/**
 * <b>Descripción</b><br>
 * Establece la interfaz para los tipos de gramáticas.
 * <p>
 * <b>Detalles</b><br>
 * Especifica los cuatro tipos de gramática que existen:<br>
 * - Chomsky<br>
 * - Sensibles al contexto<br>
 * - Independientes del contexto<br>
 * - Regulares
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Establece la interfaz para saber que manejador utilizar.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public interface TypeHandler {
    
    /**
     * Identifica una gramática de Chomsky o de tipo 0.
     */
    public static final int CHOMSKY = 0;
    
    /**
     * Identifica una gramática sensible al contexto o de tipo 1.
     */
    public static final int DEPENDENT = 1;
    
    /**
     * Identifica una gramática independiente del contexto o de tipo 2.
     */
    public static final int INDEPENDENT = 2;
    
    /**
     * Identifica una gramática regular o de tipo 3.
     */
    public static final int REGULAR = 3;

}//TypeHandler
