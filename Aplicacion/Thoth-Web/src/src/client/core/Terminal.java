package src.client.core;

import src.client.core.grammar.Symbol;

/**
 * <b>Descripción</b><br>
 * Especifica un símbolo terminal.
 * <p>
 * <b>Detalles</b><br>
 * Almacena un terminal, el cual puede ser cualquiera de los siguienter caracteres:<br>
 * [ a - z , 0 - 9 , ( , ) , + ,  - , · , * ] Estos caracteres pueden ser cambiados de
 * acuerdo a las especificaciones de gramática de lenguajes.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Encapsula los terminales.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * @see Symbol
 */
public class Terminal extends Symbol {
    
    // Attributes ------------------------------------------------------------------
    
    /**
     * Token del terminal.
     */
    protected char mToken; 
    
    // Methods ---------------------------------------------------------------------
    
    public Terminal () {
    	mToken = ' ';
    }
    
    /**
     * Constructor al que se le pasa un carácter.
     * 
     * @param token Carácter de entrada.
     */
    public Terminal (char token) {
        mToken = token;
        
    }//Terminal
        
    /**
     * Devuelve el token del terminal.
     * 
     * @return Token del terminal.
     */
    public char getToken (){
        
        return mToken;
    }//getToken
    
    /**
     * Establece el nuevo token del terminal.
     * 
     * @param token Token del terminal.
     */
    public void setToken (char token){
        mToken = token;
        
    }//getToken
    
    /**
     * Compara si un terminal es igual a otro en función del token.
     * 
     * @param symbol Símbolo con el que va a ser comparado.
     * @return True si son iguales y false en caso contrario.
     */
    public boolean equals (Object symbol){
        try{
        	if(((Terminal)symbol).toString().length()!=
        		toString().length())
        			return mToken!=((Terminal)symbol).getToken();
        	else
        		return mToken==((Terminal)symbol).getToken();
            
        }catch(ClassCastException e){
            
            return false;
        }
    }//equals

    /**
     * Devuelve un String con el símbolo.
     * 
     * @return String con el símbolo.
     */
    public String toString (){
        
        return new Character(mToken).toString();
    }//toString
        
}//Terminal
