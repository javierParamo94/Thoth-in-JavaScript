package core.grammar.cleaner;

import core.grammar.*;

/**
 * <b>Descripción</b><br>
 * Se encarga de establecer la interfaz de los algoritmos de limpieza de gramáticas.
 * <p>
 * <b>Detalles</b><br>
 * Define la estructura a seguir por los algoritmos de limpieza.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public abstract class Cleaning implements TypeHandler {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Gramática que va a ser limpiada.
     */
    protected Grammar mOldGrammar;
    
    /**
     * Gramática limpiada.
     */
    protected Grammar mNewGrammar;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico de los algoritmos de limpieza.
     * 
     * @param grammar Gramática a limpiar.
     */
    public Cleaning (Grammar grammar) {
        mOldGrammar = grammar;
        mNewGrammar = new Grammar();
        
    }//Algorithm
    
    /**
     * Primer paso del algoritmo.<br>
     * 
     * @return Devuelve falso si el algoritmo ha fallado y no debe continuar.
     */
    public abstract boolean firstStep();
    
    /**
     * Siguiente paso del algoritmo.
     * 
     * @return Devuelve falso si no hay mas pasos que realizar.
     */
    public abstract boolean nextStep();
    
    /**
     * Realiza todos los pasos del algoritmo.
     * 
     * @return Devuelve falso cuando ya no puede seguir ejecutando el algoritmo.
     */
    public abstract boolean allSteps();
    
    /**
     * Devuelve la gramática limpiada.
     * 
     * @return La gramática limpia.
     */
    public Grammar getSolution () {
        
        return mNewGrammar;
    }//getSolution
    
}//Algorithm
