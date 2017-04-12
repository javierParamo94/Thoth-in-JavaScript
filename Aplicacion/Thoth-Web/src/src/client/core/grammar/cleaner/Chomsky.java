package src.client.core.grammar.cleaner;

import java.util.Vector;

import src.client.core.Symbol;
import src.client.core.Terminal;
import src.client.core.NonTerminal;
import src.client.core.grammar.*;

/**
 * <b>Descripción</b><br>
 * Transforma una gramática independiente del contexto a la Forma Normal de Chomsky.
 * <p>
 * <b>Detalles</b><br>
 * Primero transforma todas las producciones: en el consecuente sólo puede haber un
 * terminal o varios no terminales.
 * Después tranforma las producciones para que sólo haya dos no terminales en cada
 * consecuente.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Transforma una GLC a forma normal de Chomsky.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class Chomsky extends Cleaning {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Contador usado para saber qué producción nos toca analizar en cada paso.
     */
    private int mProd;
    
    /**
     * Almacenará todas las cadenas que han cambiado su valor en el último paso del algoritmo.
     */
    private Vector<Production> mChanges;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico.
     * 
     * @param grammar Gramática a limpiar.
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
     * Comprueba que la gramática sea regular o independiente del contexto para que
     * pueda ejecutarse el algoritmo.<br>
     * No debe tener transiciones épsilon, ni símbolos anulables, ni símbolos no
     * alcanzables, ni producciones unitarias.<br>
     * Se encarga de que en la parte derecha de la producción sólo haya no terminales
     * o un sólo terminal.
     * 
     * @return Devuelve falso si el algoritmo ha fallado y no debe continuar.
     */
    public boolean firstStep () {
        Production prod;
        
            //No se puede ejecutar el algoritmo si es de tipo 0 ó 1
        if(mOldGrammar.getType() == CHOMSKY || mOldGrammar.getType() == DEPENDENT)
            return false;
        
        //No debe tener SNT, SNA, PNG ni producciones épsilon
        
            //En el consecuente sólo deben a parecer no terminales o un terminal
        for(int i=0; i<mNewGrammar.getProductions().size(); i++){
            prod = mNewGrammar.getProductions().elementAt(i);
                //Si hay más de un símbolo en el consecuente
            if(prod.getRight().size() > 1)
                for(int j=0; j<prod.getRight().size(); j++)
                    if(prod.getRight().elementAt(j).getClass().equals(Terminal.class))
                        buildProdTerm(prod.getRight().elementAt(j));
            else
                    //Si hay sólo un símbolo debe ser un terminal
                if(prod.getRight().size() == 1 &&
                        !prod.getRight().firstElement().getClass().equals(Terminal.class))
                    return false;
        }
        
        return true;
    }//firstStep
    
    /**
     * Crea un nuevo no terminal que va a sustituir al terminal que se le pasa.<br>
     * Recorre todas las producciones sustituyendo el terminal t por el nuevo no
     * terminal. Finalmente crea una producción que tiene como antecedente el nuevo
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
                //Para la parte gráfica
            if(flag)
                mChanges.add(mNewGrammar.getProductions().elementAt(i));
        }
            //Creamos la producción T_A -> a
        left = new Vector<Symbol>(1, 0);
        right = new Vector<Symbol>(1, 0);
        left.add(noTerm);
        right.add(t);
        mNewGrammar.createProduction(left, right);
            //Para la parte gráfica
        mChanges.add(mNewGrammar.getProductions().lastElement());
        
    }//buildProdTerm
    
    /**
     * Siguiente paso del algoritmo.<br>
     * Si el consecuente tiene más de dos no terminales lo separa en una nueva producción.
     * 
     * @return Devuelve falso si no hay mas pasos que realizar.
     */
    public boolean nextStep () {
        Vector<Symbol> right, newLeft = new Vector<Symbol>(3, 3),
                       newRight = new Vector<Symbol>(5, 5);
        NonTerminal s, oldRight, noTerm;
        
        for( ; mProd < mNewGrammar.getSize(); mProd++){
            right = mNewGrammar.getProductions().elementAt(mProd).getRight();
                //Si tiene más de dos no terminales transformamos
            if(right.size() > 2){
                mChanges = new Vector<Production> ();
                mChanges.add(mNewGrammar.getProductions().elementAt(mProd).clone());
                    //Creamos el antecedente de la nueva producción
                s = (NonTerminal) mNewGrammar.getProductions().elementAt(mProd).getLeft().firstElement();
                noTerm = new NonTerminal(s + "\'");
                    //Transformamos la producción antigua
                newRight.addAll(right.subList(1, right.size()));
                oldRight = (NonTerminal)right.firstElement();
                right.clear();
                right.add(oldRight);
                right.add(noTerm);
                    //Para la parte gráfica
                mChanges.add(mNewGrammar.getProductions().elementAt(mProd));
                    //Creamos la nueva producción
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
