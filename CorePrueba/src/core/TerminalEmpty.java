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
public class TerminalEmpty extends Terminal {
    
    // Attributes ------------------------------------------------------------------
    
    /**
     * Caracter especial para designar el conjunto vac�o.
     */
    public static final char EMPTY_CHARACTER = 216;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor por defecto.
     */
    public TerminalEmpty() {
        super(EMPTY_CHARACTER);
        
    }//TerminalEmpty
    
}//TerminalEmpty
