package core.grammar.cleaner;

import java.util.Vector;

import core.Symbol;
import core.TerminalEpsilon;
import core.grammar.Grammar;
import core.grammar.Production;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de eliminar los s�mbolos anulables (�psilon).
 * <p>
 * <b>Detalles</b><br>
 * Elimina todo elemento anuble, es decir, todo aquel desde el cual se puede llegar
 * a derivar �psilon.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Elimina los s�mbolos anulables.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class EliminateSA extends Cleaning {

    // Attributes ------------------------------------------------------------------

    /**
     * Vector con los no terminales que no van a ser eliminados.
     */
    private Vector<Symbol> mNonTerm;
    
    /**
     * Vector con los no terminales que han sido eliminados de la gram�tica.
     */
    private Vector<Symbol> mCancel;
    
    /**
     * Indica que posici�n de los no terminales que no van a ser eliminados, va a estudiarse.
     */
    private int mCount;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param grammar Gram�tica a limpiar.
     */
    public EliminateSA (Grammar grammar) {
        super(grammar);
        mCount = 0;
        mNewGrammar = grammar.clone();
        mNonTerm = new Vector<Symbol>(5, 5);
        mCancel = new Vector<Symbol>(5, 5);
        
    }//EliminateSA
    
    /**
     * Devuelve el no terminal cancelable que est� siendo analizado.
     * 
     * @return No terminal analizado en este momento.
     */
    public Symbol currentCancel () {
        
        return mNonTerm.elementAt(mCount);
    }//currentCancel
    
    /**
     * Devuelve los no terminales que son anulables.
     * 
     * @return No terminales anulables.
     */
    public Vector<Symbol> getCancel () {
        
        return mCancel;
    }//getNonTerm
    
    /**
     * Primer paso del algoritmo.<br>
     * Comprueba que la gram�tica sea regular o independiente del contexto para que
     * pueda ejecutarse el algoritmo.<br>
     * Identifica las producciones epsilon de la gram�tica.
     * 
     * @return Devuelve falso si el algoritmo ha fallado y no debe continuar.
     */
    public boolean firstStep () {
            //No se puede ejecutar el algoritmo si es de tipo 0 � 1
        if(mNewGrammar.getType() == CHOMSKY || mNewGrammar.getType() == DEPENDENT)
            return false;
        
            //Buscamos las producciones epsilon
        searchEpsilon();
        
            //Si no hay producciones epsilon falla el algoritmo
        if(mNonTerm.isEmpty())
            return false;
            
        return true;
    }//firstStep
    
    /**
     * Busca producciones epsilon en la nueva gram�tica y a�ade el antecedente a la
     * lista de no terminales anulables.<br>
     * Si encuentra una producci�n �psilon la elimina de la nueva gram�tica.
     */
    private void searchEpsilon () {
        Vector<Symbol> left, right;
        
            //Por cada producci�n miramos su parte derecha
        for(int i=0; i<mNewGrammar.getSize(); i++){
            left = mNewGrammar.getProductions().elementAt(i).getLeft();
            right = mNewGrammar.getProductions().elementAt(i).getRight();
                //Si es produccion epsilon metemos el antecendente y la borramos
            if(right.firstElement().getClass().equals(TerminalEpsilon.class)){
                if(!mNonTerm.contains(left.firstElement())){
                    mNonTerm.add(left.firstElement());
                    if(!mCancel.contains(left.firstElement()))
                        mCancel.add(left.firstElement());
                }
                if(!left.firstElement().equals(mNewGrammar.getAxiom())){
                    mNewGrammar.removeProduction(mNewGrammar.getProductions().elementAt(i));
                    i--;
                }
            }        
        }//for
        
    }//searchEpsilon
    
    /**
     * Siguiente paso del algoritmo.<br>
     * Recorre las producciones buscando en el consecuente un no terminal de la 
     * lista de s�mbolos anulables.<br>
     * Si el no terminal s�lo tiene una producci�n y �sta es �psilon eliminaremos
     * la producci�n y el s�mbolo de todas las producciones de la gram�tica.<br>
     * Crea todas las posibles produciones considerando que el no terminal anulable
     * se puede eliminar o no.
     * 
     * @return Devuelve falso si no hay mas pasos que realizar.
     */
    public boolean nextStep () {
        Symbol temp;
        
        temp = mNonTerm.elementAt(mCount);
            //Si temp es un anulable absoluto
        if(isAbsoluteEpsilon(temp)){
            if(!temp.equals(mNewGrammar.getAxiom()))
                mNewGrammar.removeSymbol(temp);
        }
        else
                //Por cada producci�n miramos su parte derecha
            for(int i=0; i<mNewGrammar.getSize(); i++)
                if(mNewGrammar.getProductions().elementAt(i).getRight().contains(temp))
                    buildProductions(mNewGrammar.getProductions().elementAt(i), temp);
        
        mCount++;
            //Buscamos nuevos no terminales anulables
        searchEpsilon();
            //Si no quedan no terminales anulables devolvemos false
        if(mCount == mNonTerm.size())
            return false;
        else
            return true;
    }//nextStep
    
    /**
     * Determina si el s�mbolo solo puede derivar en la cadena vac�a,
     * es decir, todas sus producciones son epsilon.<br>
     * 
     * @param s S�mbolo a comprobar.
     * @return True si solo puede derivar en cadena vac�a.
     */
    private boolean isAbsoluteEpsilon (Symbol s) {
        Vector<Production> prods;
        
            //Obtenemos las producciones de s
        prods = getProdNonTerm(s);
        
            //Si el consecuente es distinto de epsilon retornamos false
        for(int i=0; i<prods.size(); i++)
            if(!mNewGrammar.containsEpsilon(prods.elementAt(i).getRight()))
                return false;
        
        return true;
    }//isAbsoluteEpsilon
    
    /**
     * Devuelve las producciones que tienen como antecedente al no terminal que se le pasa.
     * 
     * @param s No terminal del que queremos obtener las producciones.
     * @return Producciones del no terminal.
     */
    private Vector<Production> getProdNonTerm (Symbol s) {
        Vector<Production> v = new Vector<Production>(3, 3);
        Symbol left;
        
        for(int i=0; i<mNewGrammar.getSize(); i++){
            left = mNewGrammar.getProductions().elementAt(i).getLeft().firstElement();
                //Si el antecedente es s
            if(s.equals(left))
                v.add(mNewGrammar.getProductions().elementAt(i));
        }
        
        return v;
    }//getProdNonTerm
    
    /**
     * Transforma a binario el n�mero que se le pasa y le devuelve como un
     * array de caracteres.<br>
     * Completa con ceros a la izquierda hasta llegar al tama�o que le
     * pasamos.
     * 
     * @param num N�mero que queremos transformar a binario.
     * @param size Tama�o del array que vamos a devolver. 
     * @return Array con el n�mero en binario o null si el n�mero excede
     * el tama�o que se le pasa.
     */
    private char[] toBinary (int num, int size) {
        String binary = new String();
        int temp;
        
        if(num == 0)
            binary = "0";
        
        while(num > 0){
            binary = num % 2 + binary;
            num /= 2;
        }
            //Rellenamos con ceros
        if(binary.length() <= size){
            temp = size - binary.length();
            for(int i=0; i<temp; i++)
                binary = "0" + binary;
            return binary.toCharArray();
        }
        
        return null;
    }//toBinary
    
    /**
     * Construye a partir de una producci�n nuevas producciones para 
     * todas las combinaciones que se obtienen al considerar el s�mbolo
     * anulable que se le pasa. 
     * 
     * @param prod Producci�n de la que vamos a obtener las nuevas.
     * @param a S�mbolo anulable.
     */
    private void buildProductions (Production prod, Symbol a){
        Production newProd;
        Vector<Integer> location = new Vector<Integer>(3, 3);
        int size;
        char[] binary;
            
            //Buscamos los s�mbolos anulables en el consecuente
        for(int loc, i=0; i<prod.getRight().size(); i++){
            loc = prod.getRight().indexOf(a, i);
            if(loc != -1){
                location.add(loc);
                i = loc;
            }
        }
        
        size = (int)Math.pow(2, location.size()) - 1;
        for(int i=0; i<size; i++){
            newProd = prod.clone();
            binary = toBinary(i, location.size());
                //Constru�mos el consecuente
            for(int j=binary.length - 1; j>=0; j--)
                if(binary[j] == '0')
                    newProd.getRight().remove((int)location.elementAt(j));
                //Creamos la transici�n
            mNewGrammar.createProduction(newProd.getLeft(), newProd.getRight());
        }
        
    }//buildProductions
    
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
        
        if(mNewGrammar.getSize() > 0)
            return true;
                
        return false;
    }//allSteps
    
}//EliminateSA
