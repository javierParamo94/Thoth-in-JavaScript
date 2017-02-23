package core.grammar.cleaner;

import core.grammar.*;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de limpiar una gram�tica
 * <p>
 * <b>Detalles</b><br>
 * Ejecuta todos los algoritmos de limpieza de gram�ticas:<br>
 * <ul>
 *  <li>Eliminaci�n de s�mbolos anulables.</li>
 *  <li>Eliminaci�n de s�mbolos no terminables.</li>
 *  <li>Eliminaci�n de s�mbolos no alcanzables.</li>
 *  <li>Eliminaci�n de producciones no generativas.</li>
 * </lu>
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Limpia una gram�tica.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class Cleaner {

    // Attributes ------------------------------------------------------------------
        
    /**
     * Gram�tica limpia.
     */
    private Grammar mNewGrammar;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param grammar Gram�tica que va a ser limpiada.
     */
    public Cleaner (Grammar grammar) {
        mNewGrammar = grammar.clone();
        
    }//Cleaner
    
    /**
     * Limpia la gram�tica ejecutando todos los algoritmos de limpieza.
     * 
     * @return True si se ha podido ejecutar todos los algoritmos.
     */
    public boolean toClean () {
        EliminateSA sa = new EliminateSA(mNewGrammar);
        
        if(sa.allSteps()){
            mNewGrammar = sa.getSolution();
            if(mNewGrammar.getAxiom() == null)
                return false;
            mNewGrammar.calculateType();
        }
        
        EliminateSNT snt = new EliminateSNT(mNewGrammar);;
        
        if(snt.allSteps()){
            mNewGrammar = snt.getSolution();
            if(mNewGrammar.getAxiom() == null)
                return false;
            mNewGrammar.calculateType();
        }
        
        EliminateSNA sna = new EliminateSNA(mNewGrammar);
        
        if(sna.allSteps()){
            mNewGrammar = sna.getSolution();
            if(mNewGrammar.getAxiom() == null)
                return false;
            mNewGrammar.calculateType();
        }
        
        EliminatePNG png = new EliminatePNG(mNewGrammar);
        
        if(png.allSteps()){
            mNewGrammar = png.getSolution();
            if(mNewGrammar.getAxiom() == null)
                return false;
            mNewGrammar.calculateType();
        }
        
        return true;
    }//toClean
    
    /**
     * Devuelve la gram�tica limpia.
     * 
     * @return Gram�tica adecuada.
     */
    public Grammar getSolution () {
        
        return mNewGrammar;
    }//getSolution

}//Cleaner
