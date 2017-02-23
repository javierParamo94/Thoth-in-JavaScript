package core;

/**
 * <b>Descripción</b><br>
 * Terminal especial que representa el carácter épsilon.
 * <p>
 * <b>Detalles</b><br>
 * Almacena un terminal épsilon.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Encapsula el terminal épsilon.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * @see Terminal
 */
public class TerminalEmpty extends Terminal {
    
    // Attributes ------------------------------------------------------------------
    
    /**
     * Caracter especial para designar el conjunto vacío.
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
