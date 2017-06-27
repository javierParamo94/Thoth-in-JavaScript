package core;

/**
 * <b>Descripción</b><br>
 * Terminal especial que representa el carácter inicial de la pila.
 * <p>
 * <b>Detalles</b><br>
 * Almacena un terminal inicial de la pila.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Encapsula el terminal inicial de la pila.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Saiz
 * @version 2.0
 * @see Terminal
 */
public class TerminalPush extends Terminal {

    //  Attributes ------------------------------------------------------------------
    
    /**
     * Caracter especial para designar el inicio de la pila.
     */
    public static final char PUSH_CHARACTER = 'Z';
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor por defecto.
     */
    public TerminalPush() {
        super(PUSH_CHARACTER);
        
    }//TerminalPush

}//TerminalPush

