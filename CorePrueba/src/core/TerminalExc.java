package core;

/**
 * <b>Descripci�n</b><br>
 * Especifica un s�mbolo terminal negado.
 * <p>
 * <b>Detalles</b><br>
 * Almacena un terminal negado, el cual puede ser cualquiera de los siguienter caracteres:<br>
 * [ a - z , 0 - 9 , ( , ) , + ,  - , � , * ] Estos caracteres pueden ser cambiados de
 * acuerdo a las especificaciones de gram�tica de lenguajes.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Encapsula los terminales negados.
 * </p>
 * 
 * @author Javier Jimeno Visitaci�n, ��igo Mediavilla Saiz
 * @version 2.0
 * @see Terminal
 */
public class TerminalExc extends Terminal {    
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor por defecto.
     * 
     * @param token Car�cter de entrada.
     */
    public TerminalExc (char token) {
        super(token);
        
    }//TerminalExc

    /**
     * Devuelve un String con el s�mbolo.
     * 
     * @return String con el s�mbolo.
     */
    public String toString (){
        
        return "!"+new Character(mToken).toString();
    }//toString
        
}//TerminalExc
