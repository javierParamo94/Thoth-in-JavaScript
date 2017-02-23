package core;

/**
 * <b>Descripción</b><br>
 * Terminal especial que representa el carácter especial de pila de una maquina de turing.
 * <p>
 * <b>Detalles</b><br>
 * Almacena un terminal de maquinas de turing.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Encapsula el terminal de maquinas de turing.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Saiz
 * @version 2.0
 * @see Terminal
 */
public class TerminalTuring extends Terminal {

    //  Attributes ------------------------------------------------------------------
    
    /**
     * Caracter especial para designar el caracter especial de pila de una maquina de turing.
     */
    public static final char TURING_CHARACTER = '';
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor por defecto.
     */
    public TerminalTuring() {
        super(TURING_CHARACTER);
        
    }//TerminalTuring
    
}//TerminalTuring
