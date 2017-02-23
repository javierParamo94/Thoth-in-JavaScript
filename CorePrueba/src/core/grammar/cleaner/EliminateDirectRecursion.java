package core.grammar.cleaner;

import java.util.Vector;

import core.Symbol;
import core.NonTerminal;
import core.TerminalEpsilon;
import core.grammar.*;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de eliminar la recursividad directa de la gram�tica
 * <p>
 * <b>Detalles</b><br>
 * Elimina la recursividad directa a izquierda de la gram�tica. 
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Elimina la recursividad directa izquierda.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class EliminateDirectRecursion extends Cleaning {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Vector de producciones donde almacenamos las producciones que tienen 
     * recursi�n.
     */
    private Vector<Production> mRecursive;
    
    /**
     * Vector de produccciones donde almacenamos las producciones que acabamos de
     * analizar.
     */
    private Vector<Production> mRecursiveOld;
    
    /**
     * Vector de nuevas producciones creadas en la gram�tica.
     */
    private Vector<Production> mNewProductions;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param grammar Gram�tica a limpiar.
     */
    public EliminateDirectRecursion (Grammar grammar) {
        super(grammar);
        mNewGrammar = grammar.clone();
        
    }//EliminateDirectRecursion
    
    /**
     * Devuelve las producciones recursivas de la gram�tica que acabamos de
     * analizar.
     * 
     * @return Producciones recursivas.
     */
    public Vector<Production> getRecProductions () {
        
        return mRecursiveOld;
    }//getRecProductions
    
    /**
     * Devuelve las nuevas producciones de la gram�tica que acabamos de 
     * construir.
     * 
     * @return Producciones que evitan la recursividad.
     */
    public Vector<Production> getNewProductions () {
        
        return mNewProductions;
    }//getNewProductions
    
    
    /**
     * Primer paso del algoritmo.<br>
     * Comprueba que la gram�tica sea regular o independiente del contexto para que
     * pueda ejecutarse el algoritmo.<br>
     * Comprueba que la gram�tica sea recursiva directa.
     * 
     * @return Devuelve falso si el algoritmo ha fallado y no debe continuar.
     */
    public boolean firstStep () {
            //No se puede ejecutar el algoritmo si es de tipo 0 � 1
        if(mOldGrammar.getType() == CHOMSKY || mOldGrammar.getType() == DEPENDENT)
            return false;
        
            //Calculamos si es recursiva directa
        mRecursive = mNewGrammar.getDirectRecursive();
        mRecursiveOld = mRecursive;
        
            //Si no tiene recursi�n finaliza el algoritmo
        if(mRecursive.isEmpty())
            return false;
        
        return true;
    }//firstStep
    
    /**
     * Siguiente paso del algoritmo.<br>
     * Obtiene las nuevas producciones que sustituir�n a las anteriores.<br>
     * Creamos las producciones y borramos las antiguas que ya no nos hagan falta.
     * 
     * @return Devuelve falso si no hay mas pasos que realizar.
     */
    public boolean nextStep () {
        Vector<Production> vRec, vProd, vNew;
        Production temp;
        Symbol oldLeft;
        
        oldLeft = mRecursive.firstElement().getLeft().firstElement();
        vRec = getProds(oldLeft, mRecursive);
        vProd = getProds(oldLeft, mNewGrammar.getProductions());
        vProd.removeAll(vRec);
        
        mRecursiveOld = vRec;
        
        if(vProd.isEmpty()){
            for(int i=0; i<vRec.size(); i++)
                mNewGrammar.removeProduction(vRec.elementAt(i));
            mNewProductions = new Vector<Production>(0,0);
        }
        else{
                //Obtenemos las nuevas producciones a crear en mNewGrammar
            vNew = getNewProductions(vProd, vRec);
            mNewProductions = vNew;
                //Creamos las nuevas producciones
            for(int i=0; i<vNew.size(); i++){
                temp = vNew.elementAt(i);
                mNewGrammar.createProduction(temp.getLeft(), temp.getRight());
            }
                //Eliminamos las producciones que provocaban la recursividad
            for(int i=0; i<vRec.size(); i++)
                mNewGrammar.removeProduction(vRec.elementAt(i));
            for(int i=0; i<vProd.size(); i++)
                mNewGrammar.removeProduction(vProd.elementAt(i));
        }
            //Comprobamos si debemos parar el algoritmo
        mRecursive.removeAll(vRec);
        
        if(mRecursive.isEmpty())
            return false;
        
        return true;
    }//nextStep
    
    /**
     * Devuelve las producciones necesarias en funci�n de los vectores de
     * producciones que se la pasan.<br>
     * Cambia las producciones de la pinta: "A -> A alpha | beta" a producciones
     * de esta forma: "A -> beta A'" , "A' -> alfa A'" y "A' -> epsilon".
     * 
     * @param vProd Producciones que no son recursivas.
     * @param vRec Producciones que son recursivas
     * @return Vector con las nuevas producciones a crear en la gram�tica.
     */
    protected static Vector<Production> getNewProductions (Vector<Production> vProd, Vector<Production> vRec){
        Vector<Production> vNewProds = new Vector<Production>();
        Vector<Symbol> oldLeft = new Vector<Symbol>(1, 0),
                        newLeft = new Vector<Symbol>(1, 0), newRight;
        NonTerminal newSymbol;
        
        oldLeft.add(vProd.firstElement().getLeft().firstElement());
        newSymbol = ((NonTerminal)oldLeft.firstElement()).clone();
        newSymbol.addToken("\'");
        newLeft.add(newSymbol);
            //Creamos las producciones A -> beta A'
        for(int i=0; i<vProd.size(); i++){
            newRight = new Vector<Symbol>(3, 3);
            	//Para no crear producciones A -> epsilon A'
            if(!vProd.elementAt(i).getRight().contains(new TerminalEpsilon()))
            	newRight.addAll(vProd.elementAt(i).getRight());
            newRight.add(newSymbol);
                //A�adimos la producci�n
            vNewProds.add(new Production(oldLeft, newRight));
        }
            //Creamos las producciones A' -> alfa A'
        for(int i=0; i<vRec.size(); i++){
            newRight = new Vector<Symbol>(3, 3);
            newRight.addAll(vRec.elementAt(i).getRight());
            newRight.remove(0);
            newRight.add(newSymbol);
                //A�adimos la producci�n
            vNewProds.add(new Production(newLeft, newRight));
        }
            //Creamos la producci�n A' -> epsilon
        vNewProds.add(new Production(newLeft));
        
        return vNewProds;
    }//getNewProductions
    
    /**
     * Devuelve todas las producciones de un determinado no terminal.
     * 
     * @param s No terminal del que queremos obtener sus producciones.
     * @param vProductions Vector de producciones donde va a buscar.
     * @return Vector de producciones del no terminal pasado.
     */
    private Vector<Production> getProds (Symbol s, Vector<Production> vProductions) {
        Vector<Production> vProds = new Vector<Production>(3, 3);
        Production prod;
        
        for(int i=0; i<vProductions.size(); i++){
            prod = vProductions.elementAt(i);
            if(prod.getLeft().firstElement().equals(s))
                vProds.add(prod);
        }
        
        return vProds;
    }//getProds
    
    /**
     * Realiza todos los pasos del algoritmo.
     * 
     * @return Devuelve falso cuando el algoritmo ha fallado.
     */
    public boolean allSteps () {
        if(!firstStep())
            return false;
        while(nextStep());
        
        return !mNewGrammar.isDirectRecursive();
    }//allSteps
    
}//EliminateDirectRecursion
