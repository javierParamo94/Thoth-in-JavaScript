package core.grammar.cleaner;

import core.grammar.*;

/**
 * <b>Descripción</b><br>
 * Se encarga de limpiar una gramática
 * <p>
 * <b>Detalles</b><br>
 * Ejecuta todos los algoritmos de limpieza de gramáticas:<br>
 * <ul>
 *  <li>Eliminación de símbolos anulables.</li>
 *  <li>Eliminación de símbolos no terminables.</li>
 *  <li>Eliminación de símbolos no alcanzables.</li>
 *  <li>Eliminación de producciones no generativas.</li>
 * </lu>
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Limpia una gramática.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class Cleaner {

    // Attributes ------------------------------------------------------------------
        
    /**
     * Gramática limpia.
     */
    private Grammar mNewGrammar;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico.
     * 
     * @param grammar Gramática que va a ser limpiada.
     */
    public Cleaner (Grammar grammar) {
        mNewGrammar = grammar.clone();
        
    }//Cleaner
    
    /**
     * Limpia la gramática ejecutando todos los algoritmos de limpieza.
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
     * Devuelve la gramática limpia.
     * 
     * @return Gramática adecuada.
     */
    public Grammar getSolution () {
        
        return mNewGrammar;
    }//getSolution

}//Cleaner
