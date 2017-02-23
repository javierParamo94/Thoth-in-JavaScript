package core;

/**
 * <b>Descripci�n</b><br>
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
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
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
     * N�mero de s�mbolos que contiene el no terminal.
     */
    private int mSize;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor al que se le pasa el contenido del no terminal.
     * 
     * @param s Car�cter de entrada, formado por varios caracteres.
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
     * Devuelve el n�mero de s�mbolos que contiene el no terminal.
     * 
     * @return N�mero de s�mbolos del no terminal
     */
    public int getSize () {
        
        return mSize;
    }//getSize
    
    /**
     * A�ade s�mbolos al no terminal.
     * 
     * @param s S�mbolos a a�adir.
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
     * M�todo de clonaci�n.<br>
     * Se encarga de clonarse a s� mismo.
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
