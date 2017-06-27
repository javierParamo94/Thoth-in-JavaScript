package core.grammar.cleaner;

import java.util.Vector;

import core.Symbol;
import core.NonTerminal;
import core.grammar.*;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de eliminar los s�mbolos no terminables.
 * <p>
 * <b>Detalles</b><br>
 * Elimina todo elemento que no puede llegar a convertirse en una cadena de
 * terminales.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Elimina los s�mbolos no terminables.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class EliminateSNT extends Cleaning {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Vector con los no terminales que no van a ser eliminados.
     */
    private Vector<Symbol> mNonTerm;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param grammar Gram�tica a limpiar.
     */
    public EliminateSNT (Grammar grammar) {
        super(grammar);
        mNonTerm = new Vector<Symbol>(5, 5);
        
    }//EliminateSNT
    
    /**
     * Primer paso del algoritmo.<br>
     * Comprueba que la gram�tica sea regular o independiente del contexto para que
     * pueda ejecutarse el algoritmo.<br>
     * Depu�s recorre la gram�tica buscando s�mbolos terminables, es decir, s�mbolos
     * que deriven directamente una cadena de terminales.<br>
     * A�adimos dichas producciones a la nueva gram�tica.
     * 
     * @return Devuelve falso si el algoritmo ha fallado y no debe continuar.
     */
    public boolean firstStep () {
            //No se puede ejecutar el algoritmo si es de tipo 0 � 1
        if(mOldGrammar.getType() == CHOMSKY || mOldGrammar.getType() == DEPENDENT)
            return false;
        
            //Por cada producci�n miramos su parte derecha
        for(Production prod : mOldGrammar.getProductions())
                //Si todo son no terminales a�adimos la produccion
            if(!mOldGrammar.containsUpper(prod.getRight()) || mOldGrammar.containsEpsilon(prod.getRight()))
                insert(prod.getLeft(), prod.getRight());
            
        return true;
    }//firstStep
    
    /**
     * Siguiente paso del algoritmo.<br>
     * Recorre las producciones buscando que la parte derecha est� formada por
     * terminales y no terminales que puedan derivar en una cadena de terminales.<br>
     * A�adimos dicha producci�n a la nueva gram�tica.
     * 
     * @return Devuelve falso si no hay mas pasos que realizar.
     */
    public boolean nextStep () {
        Vector<Symbol> left, right;
        boolean flag;
        int size = mNewGrammar.getSize();
        
            //Por cada producci�n miramos su parte derecha
        for(int i=0; i<mOldGrammar.getSize(); i++){
            left = mOldGrammar.getProductions().elementAt(i).getLeft();
            right = mOldGrammar.getProductions().elementAt(i).getRight();
            flag = true;
                //Recorremos cada s�mbolo de la parte derecha
            for(int j=0; j<right.size() && flag; j++)
                if(right.elementAt(j).getClass().equals(NonTerminal.class))
                    if(!mNonTerm.contains(right.elementAt(j)))
                        flag = false;
            
                //Si introducimos una producci�n salimos de la funci�n
            if(flag)
                if(insert(left, right))
                    return true;
        }//for
        
            //Cuando no hayamos metido ninguna producci�n hemos terminado
        if(size == mNewGrammar.getSize())
            return false;

        return true;
    }//nextStep

    /**
     * Se encarga de crear la nueva produccion e introducir el antecedente en
     * la lista de terminables.<br>
     * No insertar� repetidos en la lista.<br>
     * Si el antecedente es el axioma, le introduciremos como tal en la nueva
     * gram�tica.
     * 
     * @param left Parte izquierda de la producci�n.
     * @param right Parte derecha de la produccci�n.
     * @return True si la producci�n no exist�a, false en caso contrario.
     */
    private boolean insert (Vector<Symbol> left, Vector<Symbol> right){
        int size = mNewGrammar.getSize();
        
            //Si no estaba el no terminal le a�adimos
        if(!mNonTerm.contains(left.firstElement())){
            mNonTerm.add(left.firstElement());
                //Si es el axioma se le introduce como tal
            if(left.firstElement().equals(mOldGrammar.getAxiom()))
                mNewGrammar.setAxiom((NonTerminal)left.firstElement());
        }
            //Creamos la producci�n
        mNewGrammar.createProduction(left, right);
            //Si ha sido insertada devolvemos true
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
        if(firstStep())
            while(nextStep());
        else
            return false;
        
        return true;
    }//allSteps
    
}//EliminateSNT
