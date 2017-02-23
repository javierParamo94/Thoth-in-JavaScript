package core;

/**
 * <b>Descripción</b><br>
 * Especifica un símbolo terminal negado.
 * <p>
 * <b>Detalles</b><br>
 * Almacena un terminal negado, el cual puede ser cualquiera de los siguienter caracteres:<br>
 * [ a - z , 0 - 9 , ( , ) , + ,  - , · , * ] Estos caracteres pueden ser cambiados de
 * acuerdo a las especificaciones de gramática de lenguajes.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Encapsula los terminales negados.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Íñigo Mediavilla Saiz
 * @version 2.0
 * @see Terminal
 */
public class TerminalExc extends Terminal {    
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor por defecto.
     * 
     * @param token Carácter de entrada.
     */
    public TerminalExc (char token) {
        super(token);
        
    }//TerminalExc

    /**
     * Devuelve un String con el símbolo.
     * 
     * @return String con el símbolo.
     */
    public String toString (){
        
        return "!"+new Character(mToken).toString();
    }//toString
        
}//TerminalExc
