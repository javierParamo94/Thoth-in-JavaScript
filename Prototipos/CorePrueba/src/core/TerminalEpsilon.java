package core;

/**
 * <b>Descripci�n</b><br>
 * Terminal especial que representa el car�cter �psilon.
 * <p>
 * <b>Detalles</b><br>
 * Almacena un terminal �psilon.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Encapsula el terminal �psilon.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * @see Terminal
 */
public class TerminalEpsilon extends Terminal {

    //  Attributes ------------------------------------------------------------------
    
    /**
     * Caracter especial para designar epsilon.
     */
    public static final char EPSILON_CHARACTER = '�';
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor por defecto.
     */
    public TerminalEpsilon() {
        super(EPSILON_CHARACTER);
        
    }//TerminalEpsilon
    
}//TerminalEpsilon
