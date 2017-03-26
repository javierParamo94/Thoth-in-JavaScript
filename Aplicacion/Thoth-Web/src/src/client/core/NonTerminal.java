package src.client.core;

import src.client.core.grammar.Symbol;

/**
 * <b>Descripción</b><br>
 * Especifica un no terminal.
 * <p>
 * <b>Detalles</b><br>
 * Almacena un no terminal.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Encapsula los no terminales.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * @see Symbol
 */
public class NonTerminal extends Symbol {
    
    // Attributes ------------------------------------------------------------------
    
    /**
     * No terminal.
     */
    private String mTokens;
    
    /**
     * Número de símbolos que contiene el no terminal.
     */
    private int mSize;
    
    // Methods ---------------------------------------------------------------------
    
    public NonTerminal () {
    	// TODO
    }
    
    /**
     * Constructor al que se le pasa el contenido del no terminal.
     * 
     * @param s Carácter de entrada, formado por varios caracteres.
     */
    public NonTerminal (String s) {
        mTokens = s;
        mSize = s.length();
        
    }//NonTerminal
    
    /**
     * Devuelve el no terminal.
     * 
     * @return No terminal.
     */
    public String getTokens () {
        
        return mTokens;
    }//getTokens
    
    /**
     * Devuelve el número de símbolos que contiene el no terminal.
     * 
     * @return Número de símbolos del no terminal
     */
    public int getSize () {
        
        return mSize;
    }//getSize
    
    /**
     * Añade símbolos al no terminal.
     * 
     * @param s Símbolos a añadir.
     */
    public void addToken (String s) {
        mTokens = mTokens + s;
        mSize = mTokens.length();
        
    }//addToken
    
    /**
     * Compara si un no terminal es igual a otro.
     * 
     * @param symbol No terminal con el que va a ser comparado.
     * @return True si son iguales y false en caso contrario.
     */
    public boolean equals (Object symbol) {
        try{
            if(mSize != ((NonTerminal)symbol).getSize())
                return false;
            
            return mTokens.equals(((NonTerminal)symbol).getTokens());
        }catch(ClassCastException e){
            return false;
        }
    }//equals
    
    /**
     * Método de clonación.<br>
     * Se encarga de clonarse a sí mismo.
     * 
     * @return Clon de este no terminal.
     */
    public NonTerminal clone () {
        String s = new String(mTokens);
        
        return new NonTerminal(s);
    }//clone
    
    /**
     * Devuelve un String con el no terminal.
     * 
     * @return String con el no terminal.
     */
    public String toString () {
        
        return mTokens.toString();
    }//toString
    
}//NonTerminal
