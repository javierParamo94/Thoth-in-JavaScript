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
public class TerminalEpsilon extends Terminal {

    //  Attributes ------------------------------------------------------------------
    
    /**
     * Caracter especial para designar epsilon.
     */
    public static final char EPSILON_CHARACTER = 'Ê';
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor por defecto.
     */
    public TerminalEpsilon() {
        super(EPSILON_CHARACTER);
        
    }//TerminalEpsilon
    
}//TerminalEpsilon
