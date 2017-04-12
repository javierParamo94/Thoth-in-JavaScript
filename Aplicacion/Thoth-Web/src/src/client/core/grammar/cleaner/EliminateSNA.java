package src.client.core.grammar.cleaner;

import java.util.Vector;

import src.client.core.Symbol;
import src.client.core.NonTerminal;
import src.client.core.grammar.*;

/**
 * <b>Descripción</b><br>
 * Se encarga de eliminar los símbolos no alcanzables.
 * <p>
 * <b>Detalles</b><br>
 * Elimina todo elemento al que no es posible llegar partiendo del axioma de la
 * gramática. 
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Elimina los símbolos no alcanzables.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class EliminateSNA extends Cleaning {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Vector con los no terminales que no van a ser eliminados.
     */
    private Vector<Symbol> mNonTerm;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico.
     * 
     * @param grammar Gramática a limpiar.
     */
    public EliminateSNA (Grammar grammar) {
        super(grammar);
        mNonTerm = new Vector<Symbol>(5, 5);
        
    }//EliminateSNA
    
    /**
     * Primer paso del algoritmo.<br>
     * Comprueba que la gramática sea regular o independiente del contexto para que
     * pueda ejecutarse el algoritmo.<br>
     * Añade el axioma a la lista de símbolos alcanzables y las producciones que
     * tengan como antecedente al axioma de la gramática.
     * 
     * @return Devuelve falso si el algoritmo ha fallado y no debe continuar.
     */
    public boolean firstStep () {
        Vector<Symbol> left, right;
        
            //No se puede ejecutar el algoritmo si es de tipo 0 ó 1
        if(mOldGrammar.getType() == CHOMSKY || mOldGrammar.getType() == DEPENDENT)
            return false;
        
            //Introducimos el axioma como símbolo alcanzable
        mNonTerm.add(mOldGrammar.getAxiom());
        mNewGrammar.setAxiom(mOldGrammar.getAxiom() );
            //Por cada producción miramos su parte derecha
        for(Production prod : mOldGrammar.getProductions()){
            left = prod.getLeft();
            right = prod.getRight();
                //Si la parte izquierda es el axioma le insertamos
            if(left.firstElement().equals(mOldGrammar.getAxiom()))
                insert(left, right);
        }
        
        return true;
    }//firstStep
    
    /**
     * Siguiente paso del algoritmo.<br>
     * Recorre las producciones buscando que la parte izquierda sea un símbolo 
     * alcanzable partiendo del axioma.<br>
     * Añadimos dicha producción a la nueva gramática y los no terminales del
     * consecuente a la lista de símbolos alcanzables.
     * 
     * @return Devuelve falso si no hay mas pasos que realizar.
     */
    public boolean nextStep () {
        Vector<Symbol> left, right;
        int size = mNewGrammar.getSize();
        
            //Por cada producción miramos su parte izquierda
        for(Production prod : mOldGrammar.getProductions()){
            left = prod.getLeft();
            right = prod.getRight();
                //Si el simbolo de la parte izquierda es alcanzable le insertamos
            if(mNonTerm.contains(left.firstElement()))
                if(insert(left, right))
                    return true;
        }//for
        
            //Cuando no hayamos metido ninguna producción hemos terminado
        if(size == mNewGrammar.getSize())
            return false;
        
        return true;
    }//nextStep

    /**
     * Inserta los no terminales del consecuente en la lista de símbolos alcanzables.
     * 
     * @param left Parte izquierda de la producción.
     * @param right Parte derecha de la produccción.
     * @return True si no existía la producción en la nueva gramática, 
     * false en caso contrario.
     */
    private boolean insert (Vector<Symbol> left, Vector<Symbol> right){
        int size = mNewGrammar.getSize();
        
            //Insertamos sólo aquellos no terminales del consecuente
        for(int i=0; i<right.size(); i++)
            if(right.elementAt(i).getClass().equals(NonTerminal.class))
                    //Si no estaba el no terminal le añadimos
                if(!mNonTerm.contains(right.elementAt(i)))
                    mNonTerm.add(right.elementAt(i));
                    
            //Si no está la producción la añadimos a la gramatica
        mNewGrammar.createProduction(left, right);
        if(size < mNewGrammar.getSize())
            return true;
        
        return false;
    }//insert
    
    /**
     * Realiza todos los pasos del algoritmo.
     * 
     * @return Devuelve falso cuando el algoritmo ha fallado.
     */
    public boolean allSteps () {
        if(!firstStep())
            return false;
        
        while(nextStep());
        
        return true;
    }//allSteps
    
}//EliminateSNA
