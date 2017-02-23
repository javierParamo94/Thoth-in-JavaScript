package core.grammar;

import java.util.Vector;

import core.Symbol;
import core.Terminal;
import core.TerminalEpsilon;
import core.NonTerminal;

/**
 * <b>Descripción</b><br>
 * Se encarga de proporcionar toda la funcionalidad de una gramática.<br> 
 * <p>
 * <b>Detalles</b><br>
 * Nos proporaciona variados métodos de acceso y de utilidad para gramáticas. 
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo abstracto de datos Gramática.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class Grammar implements TypeHandler {
    
    //  Attributes ------------------------------------------------------------------
    
    /**
     * Producciones de la gramática.
     */
    private Vector<Production> mProductions;
    
    /**
     * Axioma de la gramática.
     */
    private NonTerminal mAxiom;
    
    /**
     * Alfabeto de no terminales de la gramática.
     */
    private Vector<NonTerminal> mNonTerminals;
    
    /**
     * Alfabeto de terminales de la gramática.
     */
    private Vector<Terminal> mTerminals;
    
    /**
     * Número de producciones.
     */
    private int mSize;
    
    /**
     * Tipo de gramática.
     */
    private int mType;
    
    /**
     * Etiqueta de la gramática en XML.
     */
    public static String mLabelXML = "grammar"; 
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor por defecto.<br>
     * Por defecto el tipo de la gramática es de Chomsky.
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
     * Devuelve el vector de producciones de la gramática.
     * 
     * @return Producciones de la gramática.
     */
    public Vector<Production> getProductions () {
        
        return mProductions;
    }//getProductions
    
    /**
     * Devuelve el alfabeto de no terminales de la gramática.
     * 
     * @return No terminales de la gramática.
     */
    public Vector<NonTerminal> getNonTerminals () {
        
        return mNonTerminals;
    }//getNonTerminales
    
    /**
     * Devuelve el alfabeto de entrada de la gramática.
     * 
     * @return Terminales de la gramática.
     */
    public Vector<Terminal> getTerminals () {
        
        return mTerminals;
    }//getTerminals

    /**
     * Si la gramática tiene axioma le devuelve y sino devuelve null.
     * 
     * @return El axioma de la gramática o null si no existe.
     */
    public NonTerminal getAxiom () {
        
        return mAxiom;
    }//getAxiom
    
    /**
     * Establece el axioma de la gramática.<br>
     * Le introduce como primer no terminal de su listado.<br>
     * 
     * @param axiom Axioma de la gramática.
     */
    public void setAxiom (NonTerminal axiom) {
        if(mNonTerminals.contains(axiom))
            mNonTerminals.remove(axiom);
        mNonTerminals.add(0, axiom);
        mAxiom = axiom;
        
    }//setAxiom
    
    /**
     * Devuelve el número de producciones de la gramática.
     * 
     * @return Número de producciones de la gramática.
     */
    public int getSize () {
        
        return mSize;
    }//getSize
    
    /**
     * Devuelve el tipo de la gramática.
     * 
     * @return Tipo de la gramática.
     */
    public int getType () {
        
        return mType;
    }//getType

    /**
     * Establece el tipo de gramática.
     * 
     * @param type Tipo de gramática.
     */
    public void setType (int type) {
        mType = type;
        
    }//setType
    
    /**
     * Calcula qué tipo de gramática es y devuelve el tipo.
     * 
     * @return Tipo de gramática.
     */
    public int calculateType () {
        mType = REGULAR;
        
        for(Production prod : mProductions)
            switch(mType){
                case REGULAR:
                        //Si left sólo tiene un no terminal o el axioma
                    if(prod.getLeft().size() == 1){
                        if(prod.getLeft().firstElement().getClass().equals(NonTerminal.class) ||
                                prod.getLeft().firstElement().equals(mAxiom)){
                                //Si right sólo tiene un terminal y un no terminal
                            if(prod.getRight().size() == 2 &&
                                    containsUpper(prod.getRight()) &&
                                    containsLower(prod.getRight()))
                                break;
                                //Si right tiene sólo un terminal
                            if(prod.getRight().size() == 1 && containsLower(prod.getRight()))
                                break;
                        }
                            //Si la producción es Axioma -> épsilon
                        if(prod.getLeft().firstElement().equals(mAxiom) &&
                                containsEpsilon(prod.getRight()))
                            break;
                    }
                    mType = INDEPENDENT;
                case INDEPENDENT:
                        //Si left sólo tiene un no terminal o el axioma
                    if(prod.getLeft().size() == 1 &&
                            (prod.getLeft().firstElement().getClass().equals(NonTerminal.class) ||
                            prod.getLeft().firstElement().equals(mAxiom)))
                        break;
                    mType = DEPENDENT;
                case DEPENDENT:
                        //Si left contiene al menos un no terminal y right no sea épsilon 
                    if(containsUpper(prod.getLeft()) && !containsEpsilon(prod.getRight()))
                        break;
                        //Si la producción es Axioma -> épsilon
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
     * Crea una nueva producción especificando solo su parte izquierda, por
     * defecto el consecuente será épsilon.<br>
     * Si la producción ya existe nos devuelve dicha producción.
     * 
     * @param left Parte izquierda de la producción.
     * @return La producción creada.
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
     * Crea una nueva producción especificando todo su contenido.<br>
     * Si la producción ya existe nos devuelve dicha producción.
     * 
     * @param left Parte izquierda de la producción.
     * @param right Parte derecha de la producción.
     * @return La producción creada.
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
     * Elimina una producción.
     * 
     * @param prod Produccion a eliminar.
     */
    public void removeProduction (Production prod) {
        if(mProductions.remove(prod))
            mSize--;
            
    }//removeProduction

    
    /**
     * Añade al alfabeto de terminales y de no terminales los símbolos de la
     * producción que se le pasa. 
     * 
     * @param prod Producción de la que vamos a obtener sus terminales
     * y no terminales.
     */
    private void addSymbols (Production prod) {
        Vector<Symbol> left = prod.getLeft(), right = prod.getRight();
        
            //Añadimos los símbolos de la parte izquierda
        for(int i=0; i<left.size(); i++)
            if(left.elementAt(i).getClass().equals(NonTerminal.class)
                    && !mNonTerminals.contains(left.elementAt(i)))
                mNonTerminals.add((NonTerminal)left.elementAt(i));
            else
                if(left.elementAt(i).getClass().equals(Terminal.class)
                        && !mTerminals.contains(left.elementAt(i)))
                    mTerminals.add((Terminal)left.elementAt(i));
        
            //Añadimos los símbolos de la parte derecha
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
     * Elimina un no terminal de todas las producciones de la gramática.<br>
     * Si el antecedente ha quedado vacío eliminamos la producción.<br>
     * Si el consecuente de la producción queda vacío le asignamos épsilon.
     * 
     * @param s Símbolo de la gramática que deseamos eliminar.
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
     * Los no terminales se representarán con mayúsculas en nuestra sintáxis.
     * 
     * @param v Vector de símbolos en el que vamos a buscar no terminales.
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
     * Los terminales se representarán con minúsculas en nuestra sintaxis.
     * 
     * @param v Vector de símbolos en el que vamos a buscar terminales.
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
     * Comprueba si el primer elemento del vector es un épsilon.
     * 
     * @param v Vector de símbolos en el que vamos a buscar épsilon.
     * @return True si hay épsilon y false en caso contrario.
     */
    public boolean containsEpsilon (Vector<Symbol> v) {
        if(v.firstElement().getClass().equals(TerminalEpsilon.class))
            return true;
        
        return false;
    }//containsEpsilon
    
    /**
     * Comprueba si la gramática es recursiva directa a izquierdas y devuelve
     * las producciones que tienen recursividad.
     * 
     * @return Vector con las producciones que tienen recursividad directa
     * o un vector vacío si no tiene recursividad.
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
     * Comprueba si la gramática es recursiva directa a izquierdas.
     * 
     * @return True si es recursiva y false en caso contrario.
     */
    public boolean isDirectRecursive () {
        
        return !getDirectRecursive().isEmpty();
    }//isDirectRecursive
    
    /**
     * Comprueba si la gramática es recursiva indirecta a izquierdas.<br>
     * Si no tiene recursividad indirecta devuelve falso aunque exista recursividad
     * directa.
     * 
     * @return True si es recursiva indirecta a izquierdas y false en caso
     * contrario.
     */
    public boolean isIndirectRecursive (){
        Production prod;
        Vector<Symbol> vNonTerm= new Vector<Symbol>();
        
            //Este método no vale para gramáticas de Chomsky ni Dependientes del contexto
        if(mType == CHOMSKY || mType == DEPENDENT)
            return false;
        
        for(int i=0; i<mProductions.size(); i++){
            prod = mProductions.elementAt(i);
                //Si el primer símbolo del consecuente es un no terminal
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
     * Método recursivo que se encarga de comprobar si las producciones que
     * tienen como antecedente el no terminal antecedent pueden llegar a derivar el
     * no terminal search.
     * 
     * @param search Símbolo que buscamos en el consecuente. 
     * @param antecedent Símbolo que debe formar el antecente.
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
     * Devuelve un String con la información en XML de la gramática.
     * 
     * @return String con los datos en XML.
     */
    public String getXML () {
        String file;
        
            //Cabecera
        file = new String("<?xml version='1.0' encoding=\'UTF-16\'?>\n"+
                          "<!DOCTYPE grammar SYSTEM \"grammar.dtd\">\n\n"+
                          "<!--Created with THOTH Project v1.0 -->\n\n");
            //Datos de la gramática
        file = file.concat("<"+Grammar.mLabelXML+"\n"+ 
                           "\ttype=\""+((Integer)mType).toString()+"\"\n"+
                           "\taxiom=\""+mAxiom.toString()+"\"\n"+
                           "\tnumprod=\""+((Integer)mSize).toString()+"\">\n");
            //Información de las producciones
        for(int i=0; i<mSize; i++)
            file = file.concat(mProductions.elementAt(i).getProductionXML());
        
        return file.concat("</"+Grammar.mLabelXML+">\n");
    }//getXML
    
    /**
     * Compara si dos gramáticas tienen las mismas producciones
     * 
     * @param grammar Gramática a comparar.
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
     * Método de clonación.<br>
     * Se encarga de clonarse a sí mismo; se trata de una clonación en profundidad.
     * 
     * @return Clon de esta gramática.
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
     * Devuelve un String con el contenido de la gramática.<br>
     * La gramática queda representada con la sintaxis usada por la aplicación de tal
     * modo que puede ser parseada sin realizar conversión alguna.
     * 
     * @return String con la gramática.
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
     * Devuelve un String con todas las producciones de la gramática.<br>
     * Mientras el toString te agrupa las producciones que tengan el mismo antecedente,
     * éste no te las agrupa y queda una producción por cada línea.
     *  
     * @return String con la gramática.
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
