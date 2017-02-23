package core.grammar.cleaner;

import java.util.Vector;

import core.Symbol;
import core.Terminal;
import core.NonTerminal;
import core.grammar.*;

/**
 * <b>Descripci�n</b><br>
 * Transforma una gram�tica independiente del contexto a la Forma Normal de Chomsky.
 * <p>
 * <b>Detalles</b><br>
 * Primero transforma todas las producciones: en el consecuente s�lo puede haber un
 * terminal o varios no terminales.
 * Despu�s tranforma las producciones para que s�lo haya dos no terminales en cada
 * consecuente.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Transforma una GLC a forma normal de Chomsky.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class Chomsky extends Cleaning {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Contador usado para saber qu� producci�n nos toca analizar en cada paso.
     */
    private int mProd;
    
    /**
     * Almacenar� todas las cadenas que han cambiado su valor en el �ltimo paso del algoritmo.
     */
    private Vector<Production> mChanges;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param grammar Gram�tica a limpiar.
     */
    public Chomsky (Grammar grammar) {
        super(grammar);
        mNewGrammar = grammar.clone();
        mProd = 0;
        mChanges = new Vector<Production>();
        
    }//Chomsky
    
    /**
     * Devuelve las producciones que han cambiado.
     */
    public Vector<Production> getChanges () {
        
        return mChanges;
    }//getChanges
    
    /**
     * Primer paso del algoritmo.<br>
     * Comprueba que la gram�tica sea regular o independiente del contexto para que
     * pueda ejecutarse el algoritmo.<br>
     * No debe tener transiciones �psilon, ni s�mbolos anulables, ni s�mbolos no
     * alcanzables, ni producciones unitarias.<br>
     * Se encarga de que en la parte derecha de la producci�n s�lo haya no terminales
     * o un s�lo terminal.
     * 
     * @return Devuelve falso si el algoritmo ha fallado y no debe continuar.
     */
    public boolean firstStep () {
        Production prod;
        
            //No se puede ejecutar el algoritmo si es de tipo 0 � 1
        if(mOldGrammar.getType() == CHOMSKY || mOldGrammar.getType() == DEPENDENT)
            return false;
        
        //No debe tener SNT, SNA, PNG ni producciones �psilon
        
            //En el consecuente s�lo deben a parecer no terminales o un terminal
        for(int i=0; i<mNewGrammar.getProductions().size(); i++){
            prod = mNewGrammar.getProductions().elementAt(i);
                //Si hay m�s de un s�mbolo en el consecuente
            if(prod.getRight().size() > 1)
                for(int j=0; j<prod.getRight().size(); j++)
                    if(prod.getRight().elementAt(j).getClass().equals(Terminal.class))
                        buildProdTerm(prod.getRight().elementAt(j));
            else
                    //Si hay s�lo un s�mbolo debe ser un terminal
                if(prod.getRight().size() == 1 &&
                        !prod.getRight().firstElement().getClass().equals(Terminal.class))
                    return false;
        }
        
        return true;
    }//firstStep
    
    /**
     * Crea un nuevo no terminal que va a sustituir al terminal que se le pasa.<br>
     * Recorre todas las producciones sustituyendo el terminal t por el nuevo no
     * terminal. Finalmente crea una producci�n que tiene como antecedente el nuevo
     * no terminal y como consecuente el terminal que se le pasa.
     *    
     * @param t Terminal a sustituir.
     */
    private void buildProdTerm (Symbol t) {
        Vector<Symbol> right, left;
        NonTerminal noTerm;
        boolean flag = false;
        
            //Creamos el nuevo no terminal 'T_A'
        noTerm = new NonTerminal(new String("T_"+((Terminal)t).getToken()));
        while(mOldGrammar.getNonTerminals().contains(noTerm))
            noTerm.addToken("\'");
        
            //Cambiamos t por el nuevo no terminal
        for(int i=0; i<mNewGrammar.getSize(); i++){
            flag = false;
            right = mNewGrammar.getProductions().elementAt(i).getRight();
            if(right.size() > 1)
                for(int j=0; j<right.size(); j++)
                    if(right.elementAt(j).equals(t)){
                        right.set(j, noTerm);
                        flag = true;
                    }
                //Para la parte gr�fica
            if(flag)
                mChanges.add(mNewGrammar.getProductions().elementAt(i));
        }
            //Creamos la producci�n T_A -> a
        left = new Vector<Symbol>(1, 0);
        right = new Vector<Symbol>(1, 0);
        left.add(noTerm);
        right.add(t);
        mNewGrammar.createProduction(left, right);
            //Para la parte gr�fica
        mChanges.add(mNewGrammar.getProductions().lastElement());
        
    }//buildProdTerm
    
    /**
     * Siguiente paso del algoritmo.<br>
     * Si el consecuente tiene m�s de dos no terminales lo separa en una nueva producci�n.
     * 
     * @return Devuelve falso si no hay mas pasos que realizar.
     */
    public boolean nextStep () {
        Vector<Symbol> right, newLeft = new Vector<Symbol>(3, 3),
                       newRight = new Vector<Symbol>(5, 5);
        NonTerminal s, oldRight, noTerm;
        
        for( ; mProd < mNewGrammar.getSize(); mProd++){
            right = mNewGrammar.getProductions().elementAt(mProd).getRight();
                //Si tiene m�s de dos no terminales transformamos
            if(right.size() > 2){
                mChanges = new Vector<Production> ();
                mChanges.add(mNewGrammar.getProductions().elementAt(mProd).clone());
                    //Creamos el antecedente de la nueva producci�n
                s = (NonTerminal) mNewGrammar.getProductions().elementAt(mProd).getLeft().firstElement();
                noTerm = new NonTerminal(s + "\'");
                    //Transformamos la producci�n antigua
                newRight.addAll(right.subList(1, right.size()));
                oldRight = (NonTerminal)right.firstElement();
                right.clear();
                right.add(oldRight);
                right.add(noTerm);
                    //Para la parte gr�fica
                mChanges.add(mNewGrammar.getProductions().elementAt(mProd));
                    //Creamos la nueva producci�n
                newLeft.add(noTerm);
                mNewGrammar.createProduction(newLeft , newRight);
                mChanges.add(mNewGrammar.getProductions().lastElement());
                return true;
            }
        }
        
        if(mProd < mNewGrammar.getSize())
            return true;
        
        return false;
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

}//Chomsky
