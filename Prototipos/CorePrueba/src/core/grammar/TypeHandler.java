package core.grammar;

/**
 * <b>Descripci�n</b><br>
 * Establece la interfaz para los tipos de gram�ticas.
 * <p>
 * <b>Detalles</b><br>
 * Especifica los cuatro tipos de gram�tica que existen:<br>
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
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public interface TypeHandler {
    
    /**
     * Identifica una gram�tica de Chomsky o de tipo 0.
     */
    public static final int CHOMSKY = 0;
    
    /**
     * Identifica una gram�tica sensible al contexto o de tipo 1.
     */
    public static final int DEPENDENT = 1;
    
    /**
     * Identifica una gram�tica independiente del contexto o de tipo 2.
     */
    public static final int INDEPENDENT = 2;
    
    /**
     * Identifica una gram�tica regular o de tipo 3.
     */
    public static final int REGULAR = 3;

}//TypeHandler
