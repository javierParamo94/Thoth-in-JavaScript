package core.grammar;

import java.util.Vector;

import core.Symbol;
import core.Terminal;
import core.TerminalEpsilon;
import core.NonTerminal;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de proporcionar toda la funcionalidad de una gram�tica.<br> 
 * <p>
 * <b>Detalles</b><br>
 * Nos proporaciona variados m�todos de acceso y de utilidad para gram�ticas. 
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo abstracto de datos Gram�tica.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 */
public class Grammar implements TypeHandler {
    
    //  Attributes ------------------------------------------------------------------
    
    /**
     * Producciones de la gram�tica.
     */
    private Vector<Production> mProductions;
    
    /**
     * Axioma de la gram�tica.
     */
    private NonTerminal mAxiom;
    
    /**
     * Alfabeto de no terminales de la gram�tica.
     */
    private Vector<NonTerminal> mNonTerminals;
    
    /**
     * Alfabeto de terminales de la gram�tica.
     */
    private Vector<Terminal> mTerminals;
    
    /**
     * N�mero de producciones.
     */
    private int mSize;
    
    /**
     * Tipo de gram�tica.
     */
    private int mType;
    
    /**
     * Etiqueta de la gram�tica en XML.
     */
    public static String mLabelXML = "grammar"; 
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor por defecto.<br>
     * Por defecto el tipo de la gram�tica es de Chomsky.
     */
    public Grammar() {
        mProductions = new Vector<Production>(5, 5);
        mNonTerminals = new Vector<NonTerminal>(10, 5);
        mTerminals = new Vector<Terminal>(5, 5);
        mAxiom = null;
        mSize = 0;
        mType = CHOMSKY;
        
    }//Grammar
    
    /**
     * Devuelve el vector de producciones de la gram�tica.
     * 
     * @return Producciones de la gram�tica.
     */
    public Vector<Production> getProductions () {
        
        return mProductions;
    }//getProductions
    
    /**
     * Devuelve el alfabeto de no terminales de la gram�tica.
     * 
     * @return No terminales de la gram�tica.
     */
    public Vector<NonTerminal> getNonTerminals () {
        
        return mNonTerminals;
    }//getNonTerminales
    
    /**
     * Devuelve el alfabeto de entrada de la gram�tica.
     * 
     * @return Terminales de la gram�tica.
     */
    public Vector<Terminal> getTerminals () {
        
        return mTerminals;
    }//getTerminals

    /**
     * Si la gram�tica tiene axioma le devuelve y sino devuelve null.
     * 
     * @return El axioma de la gram�tica o null si no existe.
     */
    public NonTerminal getAxiom () {
        
        return mAxiom;
    }//getAxiom
    
    /**
     * Establece el axioma de la gram�tica.<br>
     * Le introduce como primer no terminal de su listado.<br>
     * 
     * @param axiom Axioma de la gram�tica.
     */
    public void setAxiom (NonTerminal axiom) {
        if(mNonTerminals.contains(axiom))
            mNonTerminals.remove(axiom);
        mNonTerminals.add(0, axiom);
        mAxiom = axiom;
        
    }//setAxiom
    
    /**
     * Devuelve el n�mero de producciones de la gram�tica.
     * 
     * @return N�mero de producciones de la gram�tica.
     */
    public int getSize () {
        
        return mSize;
    }//getSize
    
    /**
     * Devuelve el tipo de la gram�tica.
     * 
     * @return Tipo de la gram�tica.
     */
    public int getType () {
        
        return mType;
    }//getType

    /**
     * Establece el tipo de gram�tica.
     * 
     * @param type Tipo de gram�tica.
     */
    public void setType (int type) {
        mType = type;
        
    }//setType
    
    /**
     * Calcula qu� tipo de gram�tica es y devuelve el tipo.
     * 
     * @return Tipo de gram�tica.
     */
    public int calculateType () {
        mType = REGULAR;
        
        for(Production prod : mProductions)
            switch(mType){
                case REGULAR:
                        //Si left s�lo tiene un no terminal o el axioma
                    if(prod.getLeft().size() == 1){
                        if(prod.getLeft().firstElement().getClass().equals(NonTerminal.class) ||
                                prod.getLeft().firstElement().equals(mAxiom)){
                                //Si right s�lo tiene un terminal y un no terminal
                            if(prod.getRight().size() == 2 &&
                                    containsUpper(prod.getRight()) &&
                                    containsLower(prod.getRight()))
                                break;
                                //Si right tiene s�lo un terminal
                            if(prod.getRight().size() == 1 && containsLower(prod.getRight()))
                                break;
                        }
                            //Si la producci�n es Axioma -> �psilon
                        if(prod.getLeft().firstElement().equals(mAxiom) &&
                                containsEpsilon(prod.getRight()))
                            break;
                    }
                    mType = INDEPENDENT;
                case INDEPENDENT:
                        //Si left s�lo tiene un no terminal o el axioma
                    if(prod.getLeft().size() == 1 &&
                            (prod.getLeft().firstElement().getClass().equals(NonTerminal.class) ||
                            prod.getLeft().firstElement().equals(mAxiom)))
                        break;
                    mType = DEPENDENT;
                case DEPENDENT:
                        //Si left contiene al menos un no terminal y right no sea �psilon 
                    if(containsUpper(prod.getLeft()) && !containsEpsilon(prod.getRight()))
                        break;
                        //Si la producci�n es Axioma -> �psilon
                    if(prod.getLeft().firstElement().equals(mAxiom) &&
                            containsEpsilon(prod.getRight()))
                        break;
                    mType = CHOMSKY;
                case CHOMSKY:
                        //Si es de Chomsky no seguimos comprobando
                    return CHOMSKY;
            }//switch
        
        return mType;
    }//calculateType
    
    /**
     * Crea una nueva producci�n especificando solo su parte izquierda, por
     * defecto el consecuente ser� �psilon.<br>
     * Si la producci�n ya existe nos devuelve dicha producci�n.
     * 
     * @param left Parte izquierda de la producci�n.
     * @return La producci�n creada.
     */
    public Production createProduction (Vector<Symbol> left) {
        Production prod = new Production(left);
        int val = mProductions.indexOf(prod);
        
        if(val == -1){
            mProductions.add(prod);
            addSymbols(prod);
            mSize++;
            return prod;
        }
        
        return mProductions.elementAt(val);
    }//createProduction
    
    /**
     * Crea una nueva producci�n especificando todo su contenido.<br>
     * Si la producci�n ya existe nos devuelve dicha producci�n.
     * 
     * @param left Parte izquierda de la producci�n.
     * @param right Parte derecha de la producci�n.
     * @return La producci�n creada.
     */
    public Production createProduction (Vector<Symbol> left, Vector<Symbol> right) {
        Production prod = new Production(left, right);
        int val = mProductions.indexOf(prod);
        
        if(val == -1){
            mProductions.add(prod);
            addSymbols(prod);
            mSize++;
            return prod;
        }
        
        return mProductions.elementAt(val);
    }//createProduction
    
    /**
     * Elimina una producci�n.
     * 
     * @param prod Produccion a eliminar.
     */
    public void removeProduction (Production prod) {
        if(mProductions.remove(prod))
            mSize--;
            
    }//removeProduction

    
    /**
     * A�ade al alfabeto de terminales y de no terminales los s�mbolos de la
     * producci�n que se le pasa. 
     * 
     * @param prod Producci�n de la que vamos a obtener sus terminales
     * y no terminales.
     */
    private void addSymbols (Production prod) {
        Vector<Symbol> left = prod.getLeft(), right = prod.getRight();
        
            //A�adimos los s�mbolos de la parte izquierda
        for(int i=0; i<left.size(); i++)
            if(left.elementAt(i).getClass().equals(NonTerminal.class)
                    && !mNonTerminals.contains(left.elementAt(i)))
                mNonTerminals.add((NonTerminal)left.elementAt(i));
            else
                if(left.elementAt(i).getClass().equals(Terminal.class)
                        && !mTerminals.contains(left.elementAt(i)))
                    mTerminals.add((Terminal)left.elementAt(i));
        
            //A�adimos los s�mbolos de la parte derecha
        for(int i=0; i<right.size(); i++){
            if(right.elementAt(i).getClass().equals(TerminalEpsilon.class)
                    && !mTerminals.contains(right.elementAt(i))){
                mTerminals.add((Terminal)right.elementAt(i));
                return;
            }
            if(right.elementAt(i).getClass().equals(NonTerminal.class)
                    && !mNonTerminals.contains(right.elementAt(i)))
                mNonTerminals.add((NonTerminal)right.elementAt(i));
            else
                if(right.elementAt(i).getClass().equals(Terminal.class)
                        && !mTerminals.contains(right.elementAt(i)))
                    mTerminals.add((Terminal)right.elementAt(i));
        }
        
    }//addSymbols

    /**
     * Elimina un no terminal de todas las producciones de la gram�tica.<br>
     * Si el antecedente ha quedado vac�o eliminamos la producci�n.<br>
     * Si el consecuente de la producci�n queda vac�o le asignamos �psilon.
     * 
     * @param s S�mbolo de la gram�tica que deseamos eliminar.
     */
    public void removeSymbol (Symbol s) {
        Vector<Symbol> left, right, temp = new Vector<Symbol>(1, 0);
        temp.add(s);
        
        for(int i=0; i<mSize; i++){
            left = mProductions.elementAt(i).getLeft();
            right = mProductions.elementAt(i).getRight();
            right.removeAll(temp);
            left.removeAll(temp);
            if(right.isEmpty())
                right.add(new TerminalEpsilon());
            if(left.isEmpty())
                removeProduction(mProductions.elementAt(i));
        }//for
        mNonTerminals.remove(s);
        mTerminals.remove(s);
        
    }//removeSymbol

    /**
     * Comprueba si existen no terminales en el vector que se le pasa.<br>
     * Los no terminales se representar�n con may�sculas en nuestra sint�xis.
     * 
     * @param v Vector de s�mbolos en el que vamos a buscar no terminales.
     * @return True si hay no terminales y false en caso contrario.
     */
    public boolean containsUpper (Vector<Symbol> v) {
        for(int i=0; i<v.size(); i++)
                //Si hay un no terminal o un axioma
            if(v.elementAt(i).getClass().equals(NonTerminal.class))
                return true;
        
        return false;
    }//containsUpper
    
    /**
     * Comprueba si existen terminales en el vector que se le pasa.<br>
     * Los terminales se representar�n con min�sculas en nuestra sintaxis.
     * 
     * @param v Vector de s�mbolos en el que vamos a buscar terminales.
     * @return True si hay terminales y false en caso contrario.
     */
    public boolean containsLower (Vector<Symbol> v) {
        for(int i=0; i<v.size(); i++)
                //Si hay terminales
            if(v.elementAt(i).getClass().equals(Terminal.class))
                return true;
        
        return false;
    }//containsLower
    
    /**
     * Comprueba si el primer elemento del vector es un �psilon.
     * 
     * @param v Vector de s�mbolos en el que vamos a buscar �psilon.
     * @return True si hay �psilon y false en caso contrario.
     */
    public boolean containsEpsilon (Vector<Symbol> v) {
        if(v.firstElement().getClass().equals(TerminalEpsilon.class))
            return true;
        
        return false;
    }//containsEpsilon
    
    /**
     * Comprueba si la gram�tica es recursiva directa a izquierdas y devuelve
     * las producciones que tienen recursividad.
     * 
     * @return Vector con las producciones que tienen recursividad directa
     * o un vector vac�o si no tiene recursividad.
     */
    public Vector<Production> getDirectRecursive () {
        Vector<Production> temp =  new Vector<Production>(3, 3);
        Production prod;
        
        for(int i=0; i<mProductions.size(); i++){
            prod = mProductions.elementAt(i);
            if(prod.getLeft().size() == 1
                    && prod.getLeft().firstElement().equals(prod.getRight().firstElement()))
                temp.add(prod);
        }
        
        return temp;
    }//getDirectRecursive

    /**
     * Comprueba si la gram�tica es recursiva directa a izquierdas.
     * 
     * @return True si es recursiva y false en caso contrario.
     */
    public boolean isDirectRecursive () {
        
        return !getDirectRecursive().isEmpty();
    }//isDirectRecursive
    
    /**
     * Comprueba si la gram�tica es recursiva indirecta a izquierdas.<br>
     * Si no tiene recursividad indirecta devuelve falso aunque exista recursividad
     * directa.
     * 
     * @return True si es recursiva indirecta a izquierdas y false en caso
     * contrario.
     */
    public boolean isIndirectRecursive (){
        Production prod;
        Vector<Symbol> vNonTerm= new Vector<Symbol>();
        
            //Este m�todo no vale para gram�ticas de Chomsky ni Dependientes del contexto
        if(mType == CHOMSKY || mType == DEPENDENT)
            return false;
        
        for(int i=0; i<mProductions.size(); i++){
            prod = mProductions.elementAt(i);
                //Si el primer s�mbolo del consecuente es un no terminal
            if(prod.getRight().firstElement().getClass().equals(NonTerminal.class)){
                vNonTerm.add(prod.getRight().firstElement());
                	//Si no tiene recursividad directa miramos si tiene indirecta
                if(!prod.getLeft().firstElement().equals(prod.getRight().firstElement()))
                	if(isIndirectRecursive(prod.getLeft().firstElement(), prod.getRight().firstElement(),
                        (Vector<Symbol>) vNonTerm.clone()))
                		return true;
            }
        }
        
        return false;
    }//isIndirectRecursive
    
    /**
     * M�todo recursivo que se encarga de comprobar si las producciones que
     * tienen como antecedente el no terminal antecedent pueden llegar a derivar el
     * no terminal search.
     * 
     * @param search S�mbolo que buscamos en el consecuente. 
     * @param antecedent S�mbolo que debe formar el antecente.
     * @param vNonTerm Lista con los estados que ya se han comprobado.<br>
     * Se utiliza para evitar repeticiones y ciclos.
     * @return True si es recursivo y false en caso contrario.
     */
    private boolean isIndirectRecursive (Symbol search, Symbol antecedent, Vector<Symbol> vNonTerm) {
        Production prod;
        
        for(int i=0; i<mProductions.size(); i++){
            prod = mProductions.elementAt(i);
            if((prod.getLeft().firstElement().equals(antecedent))){
                    //Si le hemos encontrado
                if(prod.getRight().firstElement().equals(search))
                    return true;
                    //Si debemos seguir buscando
                if(prod.getRight().firstElement().getClass().equals(NonTerminal.class )
                        || prod.getRight().firstElement().equals(mAxiom))
                    if(!vNonTerm.contains(prod.getRight().firstElement())){
                        vNonTerm.add(prod.getRight().firstElement());
                        if(isIndirectRecursive(search, prod.getRight().firstElement(),
                                (Vector<Symbol>) vNonTerm.clone()))
                            return true;
                    } 
            }
        }
        
        return false;
    }//isIndirectRecursive
    
    /**
     * Devuelve un String con la informaci�n en XML de la gram�tica.
     * 
     * @return String con los datos en XML.
     */
    public String getXML () {
        String file;
        
            //Cabecera
        file = new String("<?xml version='1.0' encoding=\'UTF-16\'?>\n"+
                          "<!DOCTYPE grammar SYSTEM \"grammar.dtd\">\n\n"+
                          "<!--Created with THOTH Project v1.0 -->\n\n");
            //Datos de la gram�tica
        file = file.concat("<"+Grammar.mLabelXML+"\n"+ 
                           "\ttype=\""+((Integer)mType).toString()+"\"\n"+
                           "\taxiom=\""+mAxiom.toString()+"\"\n"+
                           "\tnumprod=\""+((Integer)mSize).toString()+"\">\n");
            //Informaci�n de las producciones
        for(int i=0; i<mSize; i++)
            file = file.concat(mProductions.elementAt(i).getProductionXML());
        
        return file.concat("</"+Grammar.mLabelXML+">\n");
    }//getXML
    
    /**
     * Compara si dos gram�ticas tienen las mismas producciones
     * 
     * @param grammar Gram�tica a comparar.
     * @return True si son iguales, false en caso contrario.
     */
    public boolean equals (Grammar grammar) {
        if(!mAxiom.equals(grammar.getAxiom()))
            return false;
        
        if(mProductions.containsAll(grammar.getProductions()) && 
                grammar.getProductions().containsAll(mProductions))
            return true;
        
        return false;    
    }//equals
    
    /**
     * M�todo de clonaci�n.<br>
     * Se encarga de clonarse a s� mismo; se trata de una clonaci�n en profundidad.
     * 
     * @return Clon de esta gram�tica.
     */
    public Grammar clone (){
        Grammar gra = new Grammar();
        Production prod;
            //Clonamos sus producciones
        for(int i=0; i<mSize; i++){
            prod = mProductions.elementAt(i).clone();
            gra.createProduction(prod.getLeft(), prod.getRight());
        }
        gra.setAxiom(mAxiom);
        gra.setType(mType);
        
        return gra;
    }//clone
    
    /**
     * Devuelve un String con el contenido de la gram�tica.<br>
     * La gram�tica queda representada con la sintaxis usada por la aplicaci�n de tal
     * modo que puede ser parseada sin realizar conversi�n alguna.
     * 
     * @return String con la gram�tica.
     */
    public String toString () {
        String string, temp;
        
        if(mAxiom != null)
            string = "% start " + mAxiom + "\n%%\n\n";
        else
            string = "% start \n%%\n\n";
        
            //Producciones independientes del contexto o regulares
        for(NonTerminal noTerm : mNonTerminals){
            temp = noTerm + " : ";
            for(Production prod : mProductions)
                if(prod.getLeft().size() == 1 &&
                        prod.getLeft().firstElement().equals(noTerm)){
                    for(Symbol s : prod.getRight())
                        temp += s + " ";
                    temp += "| ";
                }
            
            if(temp.endsWith("| "))
                temp = temp.subSequence(0, temp.length()-3).toString();
            if(!temp.endsWith(": "))
                string = string + temp + " ;\n";
        }
            //Producciones dependientes del contexto o de Chomsky
        for(Production prod : mProductions)
            if(prod.getLeft().size() > 1){
                temp = new String();
                for(Symbol s : prod.getLeft())
                    temp += s + " ";
                temp += ": ";
                for(Symbol s : prod.getRight())
                    temp += s + " ";
                temp += ";\n";
                string += temp;
            }
        
        return string + "\n%%";
    }//toString
    
    /**
     * Devuelve un String con todas las producciones de la gram�tica.<br>
     * Mientras el toString te agrupa las producciones que tengan el mismo antecedente,
     * �ste no te las agrupa y queda una producci�n por cada l�nea.
     *  
     * @return String con la gram�tica.
     */
    public String completeToString () {
        String temp = new String();
        
        if(mAxiom != null)
            temp = "% start " + mAxiom + "\n%%\n\n";
        else
            temp = "% start \n%%\n\n";
        
        for(int i=0; i<mSize; i++)
            temp = temp + mProductions.elementAt(i).toString();
        
        temp = temp + "\n%%";
        
        return temp;
    }//completeToString
    
}//Grammar
