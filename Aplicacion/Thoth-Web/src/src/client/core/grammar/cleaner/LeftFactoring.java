package src.client.core.grammar.cleaner;

import java.util.Vector;

import src.client.core.Symbol;
import src.client.core.NonTerminal;
import src.client.core.TerminalEpsilon;
import src.client.core.grammar.*;

/**
 * <b>Descripción</b><br>
 * Se encarga de factorizar una gramática.
 * <p>
 * <b>Detalles</b><br>
 * Busca el prefijo mayor a un conjunto de producciones.<br>
 * La idea es que cuando en el análisis descendente no está claro cuál de dos 
 * producciones alternativas utilizar para ampliar un no terminal A, se
 * sobreeescriben las producciones de A para retrasar la decisión hasta haber
 * visto suficiente entrada.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Factoriza por la izquierda la gramática.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class LeftFactoring extends Cleaning {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Nos indica qué índice de los no terminales de la gramática nos toca analizar en cada paso.
     */
    private int mCount;
    
    /**
     * Nos indica las producciones que van a ser factorizadas. 
     */
    private Vector<Production> mNewProds;
    
    /**
     * Prefijo de las producciones factorizadas.
     */
    private Vector<Symbol> mPrefix;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico.
     * 
     * @param grammar Gramática a limpiar.
     */
    public LeftFactoring (Grammar grammar) {
        super(grammar);
        mNewGrammar = grammar.clone();
        mCount = 0;
        mNewProds = new Vector<Production>(5, 5);
        mPrefix = new Vector<Symbol>(5, 5);
        
    }//LeftFactoring
    
    /**
     * Devuelve las producciones factorizadas en cada paso.
     * 
     * @return Producciones factorizadas.
     */
    public Vector<Production> getNewProd () {
        
        return mNewProds;
    }//getNewProd
    
    /**
     * Devuelve el prefijo.
     * 
     * @return Prefijo de las producciones factorizadas.
     */
    public Vector<Symbol> getPrefix () {
        
        return mPrefix;
    }//getPrefix
    
    /**
     * Primer paso del algoritmo.<br>
     * Comprueba que la gramática sea regular o independiente del contexto para que
     * pueda ejecutarse el algoritmo.
     * 
     * @return Devuelve falso si el algoritmo ha fallado y no debe continuar.
     */
    public boolean firstStep () {
        
            //No se puede ejecutar el algoritmo si es de tipo 0 ó 1
        if(mOldGrammar.getType() == CHOMSKY || mOldGrammar.getType() == DEPENDENT)
            return false;
        
        return true;
    }//firstStep
    
    /**
     * Siguiente paso del algoritmo.<br>
     * Localiza el prefijo común a dos o más producciones de la gramática.<br>
     * Crea nuevas producciones y modifica la antigua.
     * 
     * @return Devuelve falso si no hay mas pasos que realizar.
     */
    public boolean nextStep () {
        Vector<Production> vProd;
        Vector<Symbol> prefix;
        int size = mNewGrammar.getSize();
        
        for( ; mCount<mNewGrammar.getNonTerminals().size(); mCount++){
            vProd = getProds(mNewGrammar.getNonTerminals().elementAt(mCount));
            mNewProds = getPrefixProds(vProd);
            if(!mNewProds.isEmpty()){
                prefix = getPrefix(mNewProds, 1);
                mPrefix = prefix;
                buildProductions(mNewProds, prefix);
                break;
            }
        }
            //Comprobamos si se ha creado alguna producción
        if(size != mNewGrammar.getSize())
            return true;
        
        return false;
    }//nextStep
    
    /**
     * Devuelve todas las producciones de un determinado no terminal.
     * 
     * @param s No terminal del que queremos obtener sus producciones.
     * @return Vector de producciones del no terminal pasado.
     */
    private Vector<Production> getProds (Symbol s) {
        Vector<Production> vProds = new Vector<Production>(3, 3);
        Production prod;
        
        for(int i=0; i<mNewGrammar.getSize(); i++){
            prod = mNewGrammar.getProductions().elementAt(i);
            if(prod.getLeft().firstElement().equals(s))
                vProds.add(prod);
        }
        
        return vProds;
    }//getProds
    
    /**
     * Obtiene las producciones que deben ser factorizadas.
     * 
     * @param vProds Producciones que van a ser analizadas.
     * @return Producciones que deben ser factorizadas.
     */
    private Vector<Production> getPrefixProds (Vector<Production> vProds) {
        Vector<Production> v = new Vector<Production>(5, 5);
        Vector<Symbol> vSymbols = new Vector<Symbol>();
        Symbol s = null;
        boolean flag = true;
        int pos;
        
        for(Production production : vProds)
            if(flag)
                if(vSymbols.contains(production.getRight().firstElement())){
                    pos = vSymbols.indexOf(production.getRight().firstElement());
                    v.add(vProds.elementAt(pos));
                    v.add(production);
                    s = production.getRight().firstElement();
                    flag = false;
                }
                else
                    vSymbols.add(production.getRight().firstElement());
            else
                if(s.equals(production.getRight().firstElement()))
                    v.add(production);
        
        return v;
    }//getPrefixProds
    
    /**
     * Función recursiva que calcula el prefijo que tienen en común
     * todas las producciones.<br>
     * Función recursiva añadiendo incrementando el índice.
     * 
     * @param vProds Producciones de las que queremos obtener el prefijo.
     * @param pos Posición a partir de la cual empieza a buscar el prefijo.
     * @return Prefijo de las producciones.
     */
    private Vector<Symbol> getPrefix (Vector<Production> vProds, int pos) {
        Vector<Production> vNewProds = new Vector<Production>(3, 3);
        Vector<Symbol> prefix = new Vector<Symbol>(3, 3);
        Symbol s;
        boolean flag = false;
        
        if(pos < vProds.firstElement().getRight().size()){
            flag = true;
            s = vProds.firstElement().getRight().elementAt(pos);
            for(int i=0; i<vProds.size(); i++)
                if(pos < vProds.elementAt(i).getRight().size())
                    if(!s.equals(vProds.elementAt(i).getRight().elementAt(pos))){
                        flag = false;
                    }
                    else
                        vNewProds.add(vProds.elementAt(i));
                else{
                    flag = false;
                }
        }
            //Calculamos el prefijo de las nuevas producciones
        if(flag && vNewProds.size() > 0)
            return getPrefix (vNewProds, pos+1);

        for(int i=0; i<pos; i++)
            prefix.add(vProds.firstElement().getRight().elementAt(i));
        
        return prefix;
    }//getPrefix
    
    /**
     * Modifica las producciones de acuerdo con el prefijo que tienen en común.<br>
     * Crea nuevas producciones factorizadas con el prefijo.
     * 
     * @param vProds Producciones que deben ser factorizadas.
     * @param prefix Prefijo que tienen todas las producciones en común.
     */
    private void buildProductions (Vector<Production> vProds, Vector<Symbol> prefix) {
        Vector<Symbol> left = new Vector<Symbol>(1, 0),
                       right = new Vector<Symbol>(3, 3);
        NonTerminal noTerm;
        
            //Creamos la producción A -> prefijo A'
        left.add(vProds.firstElement().getLeft().firstElement());
        right.addAll(prefix);
        noTerm = new NonTerminal(((NonTerminal)left.firstElement()).getTokens() + "'");
        while(mNewGrammar.getNonTerminals().contains(noTerm))
            noTerm.addToken("'");
        
        right.add(noTerm);
        mNewGrammar.createProduction(left, right);
            //Creamos las producciones A' -> beta1 | beta 2 | ...
        for(int i=0; i<vProds.size(); i++){
            vProds.elementAt(i).getLeft().set(0, noTerm);
            for(int j=0; j<prefix.size(); j++)
                vProds.elementAt(i).getRight().removeElementAt(0);
            if(vProds.elementAt(i).getRight().size() == 0){
                Vector<Symbol> temp = new Vector<Symbol>(1,0);
                temp.add(new TerminalEpsilon());
                vProds.elementAt(i).setRight(temp);
            }
        }
        
    }//buildProductions
    
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
    
}//LeftFactoring
