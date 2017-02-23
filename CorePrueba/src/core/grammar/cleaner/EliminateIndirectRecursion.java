package core.grammar.cleaner;

import java.util.Vector;

import core.Symbol;
import core.NonTerminal;
import core.grammar.*;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de eliminar la recursividad indirecta de la gram�tica
 * <p>
 * <b>Detalles</b><br>
 * Elimina la recursividad indirecta de la gram�tica.<br>
 * Para conseguirlo primero elimina la recursividad indirecta izquierda y 
 * despu�s elimina la recursividad directa. 
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Elimina la recursividad indirecta a la gram�tica.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class EliminateIndirectRecursion extends Cleaning {

    // Attributes ------------------------------------------------------------------
    
    /**
     * Contador que nos ayuda a llevar el orden en cada iteraci�n. 
     */
    private int mCountI;
    
    /**
     * Contador que nos ayuda a llevar el orden en cada iteraci�n.
     */
    private int mCountJ;
    
    /**
     * Producci�n que ocasionaba la recursividad indirecta.
     */
    private Production mOldProd;
    
    /**
     * Vector de producciones que han sido borradas.
     */
    private Vector<Production> mAllProds;
    
    /**
     * Vector de producciones nuevas creadas.
     */
    private Vector<Production> mRecDir;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param grammar Gram�tica a limpiar.
     */
    public EliminateIndirectRecursion (Grammar grammar) {
        super(grammar);
        mNewGrammar = grammar.clone();
        mCountI = 0;
        mCountJ = 0;
        
    }//EliminateIndirectRecursion
    
    /**
     * Proporciona la producci�n que se ha analizado en este paso.
     * 
     * @return Producci�n antigua.
     */
    public Production getOldProd () {
        
        return mOldProd;
    }//getOldProd
    
    /**
     * Proporciona las producciones que han sido creadas.
     * 
     * @return Producciones creadas.
     */
    public Vector<Production> getAllProds () {
        
        return mAllProds;
    }//getAllProds
    
    /**
     * Proporciona las producciones que han sido borradas.
     * 
     * @return Producciones nuevas.
     */
    public Vector<Production> getRecDirProds () {
        
        return mRecDir;
    }//getRecDirProds
    
    /**
     * Primer paso del algoritmo.<br>
     * Comprueba que la gram�tica sea regular o independiente del contexto para que
     * pueda ejecutarse el algoritmo.<br>
     * Comprueba que la gram�tica sea recursiva indirecta a izquierdas.
     * 
     * @return Devuelve falso si el algoritmo ha fallado y no debe continuar.
     */
    public boolean firstStep () {
            //No se puede ejecutar el algoritmo si es de tipo 0 � 1
        if(mOldGrammar.getType() == CHOMSKY || mOldGrammar.getType() == DEPENDENT)
            return false;
        
            //Calculamos si es recursiva indirecta
        if(!mNewGrammar.isIndirectRecursive())
            return false;

        return true;
    }//firstStep
    
    /**
     * Siguiente paso del algoritmo.<br>
     * Busca entre las producciones las que puedan dar problemas, es decir, las
     * que puedan tener recursividad y las sustituye por otras nuevas; eliminando
     * as� la recursividad indirecta.<br>
     * Finalmente para las nuevas transiciones elimina la recursividad directa
     * creada en el paso anterior.
     * 
     * @return Devuelve falso si no hay mas pasos que realizar.
     */
    public boolean nextStep () {
        Vector<Production> vProds;
        NonTerminal left, right;
        boolean flag = true;
        
            //Para la parte gr�fica
        mAllProds = new Vector<Production>(5, 5);
        mRecDir = new Vector<Production>(5, 5);
        
        do{
                //Comprobamos si debemos seguir o no
            if(mCountI == mCountJ)
                if(mCountI == mNewGrammar.getNonTerminals().size()-1)
                    return false;
                else{
                    mCountI++;
                    mCountJ = 0;
                }
                //Obtenemos el antecedente y el consecuente
            left = mNewGrammar.getNonTerminals().elementAt(mCountI);
            right = mNewGrammar.getNonTerminals().elementAt(mCountJ);
            vProds = getProds(left);
            for(Production prod : vProds)
                if(prod.getRight().firstElement().equals(right)){
                        //Para la parte gr�fica
                    mOldProd = prod;
                        //Creamos transiciones y eliminamos recursi�n directa
                    buildProductions(prod);
                    elimDirRecursion(left);
                    flag = false;
                }
            mCountJ++;
        }while(flag);
                
        return true;
    }//nextStep
    
    /**
     * Devuelve todas las producciones en las que el antecedente es el no terminal
     * que se le pasa.
     * 
     * @param s No terminal del que queremos obtener sus producciones.
     * @return Vector de producciones del no terminal pasado.
     */
    private Vector<Production> getProds (Symbol s) {
        Vector<Production> vProds = new Vector<Production>(3, 3);
        
        for(Production prod : mNewGrammar.getProductions())
            if(prod.getLeft().firstElement().equals(s))
                vProds.add(prod);
        
        return vProds;
    }//getProds
    
    /**
     * Sustituye la producci�n que se le pasa por todas las necesarias para
     * que no sea recursiva indirecta a izquierda.
     * 
     * @param prod Producci�n que ocasionaba la recursi�n indirecta.
     */
    private void buildProductions (Production prod) {
        Vector<Production> vProds = getProds(prod.getRight().firstElement());
        Vector<Symbol> newRight;
        
        for(int i=0; i<vProds.size(); i++){
            newRight = new Vector<Symbol>(5, 5);
            newRight.addAll(vProds.elementAt(i).getRight());
                //Quitamos el no terminal que provocaba la recursi�n
            newRight.addAll(prod.getRight().subList(1, prod.getRight().size()));
            mNewGrammar.createProduction(prod.getLeft(), newRight);
                //Para la parte gr�fica
            mAllProds.add(new Production(prod.getLeft(), newRight));
            mRecDir.add(new Production(prod.getLeft(), newRight));
        }
        mNewGrammar.removeProduction(prod);
        
    }//buildProductions
    
    /**
     * Elimina la recursi�n directa del no terminal left.
     * 
     * @param left No terminal del que queremos eliminar su recursividad directa. 
     */
    private void elimDirRecursion (Symbol left){
        Vector<Production> vProd, vNew, vRec =  new Vector<Production>(3, 3);
        
            //Introducimos en un vector las producciones con recursividad directa
        vRec = mNewGrammar.getDirectRecursive();
            //Si hay recursividad directa la quitamos
        if(!vRec.isEmpty()){
            vProd = getProds(left);
            vProd.removeAll(vRec);
                //Obtenemos las nuevas producciones a crear en mNewGrammar
            vNew = EliminateDirectRecursion.getNewProductions(vProd, vRec);
                //Creamos las nuevas producciones
            for(Production tempProd : vNew){
                mNewGrammar.createProduction(tempProd.getLeft(), tempProd.getRight());
                mRecDir.add(tempProd);
            }
                //Eliminamos las viejas
            for(Production tempProd : vProd)
                mNewGrammar.removeProduction(tempProd);
            for(Production tempProd : vRec)
                mNewGrammar.removeProduction(tempProd);
        }
        
    }//elimDirRecursion
    
    /**
     * Realiza todos los pasos del algoritmo.
     * 
     * @return Devuelve falso cuando el algoritmo ha fallado.
     */
    public boolean allSteps () {
        if(!firstStep())
            return false;
        while(nextStep());
        
        return !mNewGrammar.isIndirectRecursive();
    }//allSteps
    
}//EliminateIndirectRecursion
