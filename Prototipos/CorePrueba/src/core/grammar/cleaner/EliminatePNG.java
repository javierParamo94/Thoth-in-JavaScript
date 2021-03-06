package core.grammar.cleaner;

import java.util.Vector;

import core.Symbol;
import core.grammar.*;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de eliminar las producciones no generativas, tambi�n llamadas unitarias.
 * <p>
 * <b>Detalles</b><br>
 * Elimina toda produccci�n no generativa, es decir, toda aquella producci�n cuyo
 * consecuente es un �nico no terminal.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Elimina las producciones no generativas.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class EliminatePNG extends Cleaning {

    // Attributes ------------------------------------------------------------------

    /**
     * Vector con los no terminales a los se que puede llegar siguiendo producciones
     * unitarias.
     */
    private Vector<Symbol> mUnited;
    
    /**
     * Vector con las producciones unitarias de la gram�tica.
     */
    private Vector<Production> mUnitedProd;
    
    /**
     * No terminales que ya hemos analizado.
     */
    private Vector<Symbol> mAnalyzed;
    
    /**
     * Contador que nos indica por qu� no terminal vamos en cada paso del algoritmo.
     */
    private int mCount;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.<br>
     * Copia la vieja gram�tica en la nueva e inicializa las variables necesarias en
     * el algoritmo.
     * 
     * @param grammar Gram�tica a limpiar.
     */
    public EliminatePNG (Grammar grammar) {
        super(grammar);
        mNewGrammar = grammar.clone();
        mUnited = new Vector<Symbol>(3, 3);
        mUnitedProd = new Vector<Production>(3, 3);
        mAnalyzed = new Vector<Symbol>(5, 5);
        mCount = 0;
        
    }//EliminatePNG
    
    /**
     * Para la parte gr�fica.<br>
     * Devuelve los unitarios en este momento.
     * 
     * @return S�mbolos a los que se llega solo con producciones unitarias.
     */
    public Vector<Symbol> getUnited () {
        
        return mUnited;
    }//getUnited
    
    /**
     * Para la parte gr�fica.<br>
     * Devuelve las producciones unitarias obtenidas tras el algoritmo.
     * 
     * @return Vector con las producciones unitarias.
     */
    public Vector<Production> getUnitedProductions() {
        
        return mUnitedProd;
    }//getUnitedProductions
    
    /**
     * Primer paso del algoritmo.<br>
     * Comprueba que la gram�tica sea regular o independiente del contexto para que
     * pueda ejecutarse el algoritmo.<br>
     * Comienza a calcular los no terminales a los que puede llegar desde el axioma
     * siguiendo �nicamente producciones no generativas.
     * 
     * @return Devuelve falso si el algoritmo ha fallado y no debe continuar.
     */
    public boolean firstStep () {
        Symbol a = mNewGrammar.getAxiom();
        
            //No se puede ejecutar el algoritmo si es de tipo 0 � 1
        if(mNewGrammar.getType() == CHOMSKY || mNewGrammar.getType() == DEPENDENT)
            return false;
       
            //A�adimos a mUnited el axioma y calculamos sus unitarios
        mAnalyzed.add(a);
        calculateUnited(a);
            //Si hay producciones unitarias para el axioma
        if(mUnited.size() > 1)
            buildProductions(a);
        
        return true;
    }//firstStep
    
    /**
     * Calcula los no terminales a los que se puede llegar con producciones no generativas
     * desde el no terminal que se le pasa.
     */
    private void calculateUnited (Symbol s) {
        Production prod;
        
        if(!mUnited.contains(s))
            mUnited.add(s);
        
        for(int i=0; i<mNewGrammar.getProductions().size(); i++){
            prod = mNewGrammar.getProductions().elementAt(i);
                //Si s es igual al antecedente de prod
            if(prod.getLeft().firstElement().equals(s))
                    //Si el consecuente s�lo tiene un no terminal le metemos y calculamos su unitario
                if(prod.getRight().size() == 1 && mNewGrammar.containsUpper(prod.getRight()))
                    if(!mUnited.contains(prod.getRight().firstElement())){
                        mUnited.add(prod.getRight().firstElement());
                        calculateUnited(prod.getRight().firstElement());
                            //Metemos las producciones unitarias en mUnitedProd
                        if(!mUnitedProd.contains(prod))
                            mUnitedProd.add(prod);
                    }
        }//for
        
    }//calculateUnited
    
    /**
     * Construye las producciones que sustituir�n a las producciones unitarias
     * por producciones generativas.<br>
     * Tendr�n como antecedente el no terminal que se le pasa.
     * 
     * @param s Antecedente de las nuevas producciones.
     */
    private void buildProductions (Symbol s) {
        Vector<Symbol> left, right, temp = new Vector<Symbol>(1, 0);
        
        temp.add(s);
        for(int i=0; i<mNewGrammar.getProductions().size(); i++){
            left = mNewGrammar.getProductions().elementAt(i).getLeft();
            right = mNewGrammar.getProductions().elementAt(i).getRight();
            if(left.size() == 1 && mUnited.containsAll(left))
                if(!mUnitedProd.contains(mNewGrammar.getProductions().elementAt(i)))
                    mNewGrammar.createProduction(temp, right);
        }
        
    }//buildProductions
    
    /**
     * Siguiente paso del algoritmo.<br>
     * Remplaza los no terminales unitarios a los que se puede llegar para un no
     * terminal determinado.<br>
     * En caso de que hayamos analizado todos los no terminales eliminamos de la
     * nueva gram�tica las producciones no generativas.
     * 
     * @return Devuelve falso si no hay mas pasos que realizar.
     */
    public boolean nextStep () {
        mUnited = new Vector<Symbol>(3, 3);
        Symbol s;
        
            //Si ya hemos analizado todos los no terminales
        if(mAnalyzed.size() == mNewGrammar.getNonTerminals().size()){
                //Eliminamos las producciones unitarias y return false
            for(int i=0; i<mUnitedProd.size(); i++)
                mNewGrammar.removeProduction(mUnitedProd.elementAt(i));
            return false;
        }
        
            //No terminal que analizaremos en esta iteraci�n
        s = mNewGrammar.getNonTerminals().elementAt(mCount);
            //Si es el axioma incrementamos mCount
        if(s.equals(mNewGrammar.getAxiom())){
            mCount++;
            s = mNewGrammar.getNonTerminals().elementAt(mCount);
        }
        
        mCount++;
        mAnalyzed.add(s);
        calculateUnited(s);
            //Si hay producciones unitarias para s
        if(mUnited.size() > 1)
            buildProductions(s);
        
        return true;
    }//nextStep
    
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
    
}//EliminatePNG
