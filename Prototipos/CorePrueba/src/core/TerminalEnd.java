package core;

/**
 * <b>Descripci�n</b><br>
 * Terminal especial que representa el car�cter de fin de entrada.
 * <p>
 * <b>Detalles</b><br>
 * Almacena un terminal de fin de entrada.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Encapsula el terminal fin de entrada.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * @see Terminal
 */
public class TerminalEnd extends Terminal {

    //  Attributes ------------------------------------------------------------------
    
    /**
     * Caracter especial para designar epsilon.
     */
    public static final char END_CHARACTER = '$';
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor por defecto.
     */
    public TerminalEnd() {
        super(END_CHARACTER);
        
    }//TerminalEnd

}//TerminalEpsilon

