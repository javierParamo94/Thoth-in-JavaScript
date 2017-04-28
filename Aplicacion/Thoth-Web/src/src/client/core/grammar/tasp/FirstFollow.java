package src.client.core.grammar.tasp;

import java.util.Vector;

import src.client.core.*;
import src.client.core.grammar.*;

/**
 * <b>Descripción</b><br>
 * Se encarga de calcular el First y el Follow de una gramática.
 * <p>
 * <b>Detalles</b><br>
 * Para calcularlo utilizará las reglas definidas por la teoría de
 * autómatas finitos y gramáticas.<br>
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Cálculo del first y el follow.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class FirstFollow implements TypeHandler{

    // Attributes ------------------------------------------------------------------

    /**
     * Gramática sobre la que se va a calcular el first.
     */
    private Grammar mGrammar;
    
    /**
     * Lista con los terminales que componen el first para cada no terminal.
     */
    private Vector<Vector<Terminal>> mFirst;
    
    /**
     * Discriminador para indicar el first.
     */
    private static final int FIRST = 1;
    
    /**
     * Lista con los terminales que componen el follow para cada no terminal.
     */
    private Vector<Vector<Terminal>> mFollow;
    
    /**
     * Discriminador para indicar el follow.
     */
    private static final int FOLLOW = 2;
    
    /**
     * Lista con los no terminales de la gramática.
     */
    private Vector<NonTerminal> mNonTerminals;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico para el first.
     */
    public FirstFollow (Grammar grammar) {
        mGrammar = grammar;
        mNonTerminals = mGrammar.getNonTerminals();
        mFirst = new Vector<Vector<Terminal>>(mNonTerminals.size(), 0);
        mFollow = new Vector<Vector<Terminal>>(mNonTerminals.size(), 0);
            //Inicializamos First y Follow
        for(int i=0; i<mNonTerminals.size(); i++){
            mFirst.add(new Vector<Terminal>(3, 3));
            mFollow.add(new Vector<Terminal>(3, 3));
        }
    }//First
    
    /**
     * Calcula el first de la gramática y le almacena.
     * 
     * @return Devuelve falso si no es LL(k).
     */
    public boolean calculateFirst () {
        int size;
            //Comprobamos el tipo
        if(mGrammar.getType() == CHOMSKY || mGrammar.getType() == DEPENDENT)
            return false;
        do{
            size = calculateSize(FIRST);
            
            for(int i=0; i<mGrammar.getSize(); i++)
                obtainsFirst(mGrammar.getProductions().elementAt(i));
                
        }while(size != calculateSize(FIRST));
        
        if(mFirst.firstElement().isEmpty())
            return false;
        
        return true;
    }//FirstStep
    
    /**
     * Calcula el Follow de la gramática y le almacena.
     * 
     * @return Devuelve falso si no se ha calculado el first antes.
     */
    public boolean calculateFollow () {
        int size;
        
        if(mFirst.firstElement().isEmpty())
            return false;
        
        do{
            size = calculateSize(FOLLOW);
            
            for(int i=0; i<mGrammar.getSize(); i++)
                obtainsFollow(mGrammar.getProductions().elementAt(i));
            
        }while(size != calculateSize(FOLLOW));
        
        return true;
    }//FirstStep

    /**
     * Calcula el número de terminales que conforman el first o el follow.<br>
     * 
     * @param flag Si queremos el número de terminales del first: "FIRST"<br>
     *             Si queremos el número de terminales del follow: "FOLLOW". 
     * @return Numero de terminales.
     */
    private int calculateSize (int flag) {
        int size = 0;
        
        switch(flag){
            case FIRST:
                for(int i=0; i<mFirst.size(); i++)
                    size += mFirst.elementAt(i).size();
                break;
            case FOLLOW:
                for(int i=0; i<mFollow.size(); i++)
                    size += mFollow.elementAt(i).size();
                break;
        }
        
        return size;
    }//calculateSize
    
    /**
     * Calcula el first para una determinada producción.<br>
     * Aplica las reglas del first segun corresponda.
     * 
     * @param prod Producción sobre la cuál se aplican las reglas del first.
     */
    private void obtainsFirst (Production prod) {
        Vector<Symbol> right= prod.getRight(); 
        Symbol left = prod.getLeft().firstElement();
        int position = mNonTerminals.indexOf(left);
        TerminalEpsilon eps = new TerminalEpsilon();
        
            //Si X->terminal => FIRST(X)={terminal}
        if(right.firstElement().getClass().equals(Terminal.class)){
            mFirst.elementAt(position).remove((Terminal)right.firstElement());
            mFirst.elementAt(position).add((Terminal)right.firstElement());
            return;
        }
            //Si X->epsilon => FIRST(X)={epsilon}
        if(right.firstElement().getClass().equals(TerminalEpsilon.class)){
            mFirst.elementAt(position).remove((TerminalEpsilon)right.firstElement());
            mFirst.elementAt(position).add((TerminalEpsilon)right.firstElement());
            return;
        }
            //Si X->Y1 Y2 ...
        for(int i=0, pos; i<right.size(); i++){
            pos = mNonTerminals.indexOf(right.elementAt(i));
                //Regla del first 3a
            if(right.elementAt(i).getClass().equals(Terminal.class))
                if(!right.elementAt(i).getClass().equals(TerminalEpsilon.class)){
                    mFirst.elementAt(position).remove((Terminal)right.elementAt(i));
                    mFirst.elementAt(position).add((Terminal)right.elementAt(i));
                    return;
                }
            if(pos >= 0) {
                mFirst.elementAt(position).removeAll(mFirst.elementAt(pos));
                mFirst.elementAt(position).addAll(mFirst.elementAt(pos));
                if(mFirst.elementAt(pos).contains(eps))
                    mFirst.elementAt(position).remove(eps);
                    //Regla del first 3b
                if(!mFirst.elementAt(pos).contains(eps))
                    return;
            }
        }
            //Regla del first 3c
        mFirst.elementAt(position).add(eps);
        
    }//obtainsFirst
    
    /**
     * Calcula el follow para una determinada producción.<br>
     * Aplica las reglas del follow segun corresponda.
     * 
     * @param Producción sobre la cuál se aplican las reglas de follow.
     */
    private void obtainsFollow (Production prod) {
        Vector<Symbol> right= prod.getRight(); 
        Symbol left = prod.getLeft().firstElement();
        int posBeta, posicion, posA = mNonTerminals.indexOf(left);
        TerminalEpsilon eps = new TerminalEpsilon();
        
            //Si el antecedente es el axioma => $ € FOLLOW(S)
        if(left.equals(mGrammar.getAxiom()))
            if(!mFollow.elementAt(posA).contains(new TerminalEnd()))
                mFollow.elementAt(posA).add(new TerminalEnd());
        
            //Si A->alfa B beta
        for(int i=0, posB; i<right.size(); i++){
                //Encontramos el primer no terminal
            if(mNonTerminals.contains(right.elementAt(i))){
                posB = mNonTerminals.indexOf(right.elementAt(i));
                for(int j=i+1; j<right.size(); j++)
                        //Encontramos un terminal
                    if(right.elementAt(j).getClass().equals(Terminal.class)){
                        mFollow.elementAt(posB).remove(right.elementAt(j));
                        mFollow.elementAt(posB).add((Terminal)right.elementAt(j));
                        break;
                    }
                    else
                            //Encontramos un no terminal
                        if(mNonTerminals.contains(right.elementAt(j))){
                            posBeta = mNonTerminals.indexOf(right.elementAt(j));
                            mFollow.elementAt(posB).removeAll(mFirst.elementAt(posBeta));
                            mFollow.elementAt(posB).addAll(mFirst.elementAt(posBeta));
                                //Si el first de beta tiene épsilon le borramos y seguimos
                            if(mFirst.elementAt(posBeta).contains(eps))
                                mFollow.elementAt(posB).remove(eps);
                            else
                                break;
                        }
            }//if
        }
            //Si A->alfa B ó A->alfa B beta con epsilon € FIRST(beta)
        posBeta = right.size();
        do{
            posBeta--;
            if(posBeta < 0)
                break;
            if(mNonTerminals.contains(right.elementAt(posBeta))){
                posicion = mNonTerminals.indexOf(right.elementAt(posBeta));
                if(posicion != posA){
                    mFollow.elementAt(posicion).removeAll(mFollow.elementAt(posA));
                    mFollow.elementAt(posicion).addAll(mFollow.elementAt(posA));
                }
            }
            else
                return;
        }while(mFirst.elementAt(posicion).contains(eps));
        
    }//obtainsFollow

    /**
     * Devuelve la lista con los terminales.
     * 
     * @return Array que compone el first de cada producción.
     */
    public Object[][] getFirstTable () {
        Object[][] solution;
        int maxSize = 0;
        
        for(Vector<Terminal> vector: mFirst)
            if(vector.size() > maxSize)
                maxSize = vector.size();
        solution = new Object[maxSize][mFirst.size()];
        
        for(int i=0; i<mFirst.size(); i++)
            for(int j=0; j<mFirst.elementAt(i).size(); j++)
                solution[j][i] = mFirst.elementAt(i).elementAt(j);
        
        return solution;
    }//getFirst
    
    /**
     * Devuelve la lista con los terminales.
     * 
     * @return Array que compone el follow de cada producción.
     */
    public Object[][] getFollowTable () {
        Object[][] solution;
        int maxSize = 0;
        
        for(Vector<Terminal> vector: mFollow)
            if(vector.size() > maxSize)
                maxSize = vector.size();
        solution = new Object[maxSize][mFollow.size()];
        
        for(int i=0; i<mFollow.size(); i++)
            for(int j=0; j<mFollow.elementAt(i).size(); j++)
                solution[j][i] = mFollow.elementAt(i).elementAt(j);
        
        return solution;
    }//getFollow
    
    /**
     * Devuelve el first.
     * 
     * @return Vector de vectores con los terminales del first.
     */
    public Vector<Vector<Terminal>> getFirst () {
        
        return mFirst;
    }//getFirst
    
    /**
     * Devuelve el follow.
     * 
     * @return Vector de vectores con los terminales del follow.
     */
    public Vector<Vector<Terminal>> getFollow () {
        
        return mFollow;
    }//getFollow
    
    /**
     * Devuelve la gramática sobre la que se ha calculado el first y el follow.
     * 
     * @return Gramática del algoritmo.
     */
    public Grammar getGrammar () {
        
        return mGrammar;
    }//getGrammar
    
}//FirstFollow
