package algorithms.regExp.derive;

import java.util.Vector;
import java.util.regex.Pattern;

import algorithms.Algorithm;
import algorithms.InitializeException;
import algorithms.equivalence.EquivalenceFA;

import core.*;
import core.finiteautomaton.FiniteAutomaton;
import core.finiteautomaton.StateFA;

/**
 * <b>Descripción</b><br>
 * Construye un autómata finito determinista a partir de una expresión regular dada.<br>
 * El autómata finito resultante será mínimo y completo, para que sea completo tendrá un
 * estado sumidero que se podrá eliminar sin cambiar el lenguaje reconocido  por el autómata.
 * <p>
 * <b>Detalles</b><br>
 * A partir de una expresión regular y aplicando el método de las derivadas va obteniendo
 * un autómata finito determinista mínimo paso a paso.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Desarrolla el método de las derivadas para la construcción de un AFD.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * @see DeriveTree
 * @see DeriveNode
 */
public class Derive extends Algorithm {

    // Attributes --------------------------------------------------------------------
    
    /**
     * Expresión regular de la que queremos calcular su autómata representada en
     * forma de árbol.
     */
    private DeriveTree mRegExp;
    
    /**
     * Conjunto de autómatas creados a partir de su expresión regular.
     */
    private Vector<DeriveTree> mTreeGroup;
    
    /**
     * Alfabeto de terminales del autómata.
     */
    private Vector<Terminal> mAlphabet;
    
    /**
     * Contador para ir asignando nombres a los estados del autómata.
     */
    private Integer mStatesName;
    
    /**
     * Contador para saber por qué estado vamos.
     */
    private int mMarkState;
    
    /**
     * Contador para saber por qué token del alfabeto vamos.
     */
    private int mMarkToken;
    
    /**
     * Resultado de la derivada actual.
     */
    private DeriveNode mCurrentDerive;
    
    // Methods -----------------------------------------------------------------------
    
    /**
     * Constructor completo del algoritmo.
     * 
     * @param regExp Expresión regular en forma de árbol.
     */
    public Derive (DeriveTree regExp) {
        super();
        mRegExp = regExp;
        mTreeGroup = new Vector<DeriveTree>();
        mAlphabet = regExp.getAlphabet();
        mStatesName = 0;
        mMarkState = 0;
        mMarkToken = -1;
        
    }//Derive
    
    /**
     * Devuelve el estado que representa la expresión regular que
     * estamos derivando en este momento.
     * 
     * @return Estado que estamos derivando.
     */
    public State getCurrentState () {
        
        return mAutomaton.getStates().elementAt(mMarkState);
    }//getCurrentState
    
    /**
     * Devuelve el terminal respecto al que estamos derivando.
     * 
     * @return Terminal con el que derivamos.
     */
    public Terminal getCurrentTerminal () {
        
        return mAlphabet.elementAt(mMarkToken);
    }//getCurrentTerminal
    
    /**
     * Devuelve la derivada de estado actual respecto al símbolo actual.
     * 
     * @return Expresión regular resultado.
     */
    public DeriveNode getCurrentDerive () {
        
        return mCurrentDerive;
    }//getCurrentDerive
    
    /**
     * Comprueba que el árbol de la expresión regular tenga un nodo.<br>
     * Crea el estado inicial del nuevo autómata.
     * 
     * @return True si el algoritmo debe continuar y false si no se puede ejecutar.
     */
    public boolean firstStep() {
        if(mRegExp.getRoot() == null)
            return false;
        
        mAutomaton = new FiniteAutomaton("Derive Automaton");
            //Creamos el estado inicial y le asignamos si es o no final
        mAutomaton.setInitialState(mAutomaton.createState(mStatesName.toString(),
                mRegExp.getAutomaton().getInitialState().isFinal()));
            //Asignamos la etiqueta
        mAutomaton.getInitialState().setLabel(mRegExp.getRoot().getRegExp());
        mTreeGroup.add(mRegExp);
        
        mStatesName++;
        return true;
    }//firstStep

    /**
     * Siguiente paso del algoritmo.<br>
     * Deriva la expresión regular que corresponde con un terminal del alfabeto; si la
     * expresión resultante ya existía crea una transición a ese estado y sino crea el
     * estado.
     * 
     * @return True si debe continuar el algoritmo y false si el algoritmo ha finalizado.
     */
    public boolean nextStep() {
        DeriveNode newExp;
        State state, newState;
        Terminal ter;
        
        mMarkToken++;
        
            //Si hemos analizado a donde va un estado con todo el alfabeto
        if(mMarkToken >= mAlphabet.size()){
            mMarkState++;
            mMarkToken = 0;
        }
            //Si hemos finalizado el algoritmo
        if(mMarkState >= mAutomaton.getStates().size())
            return false;
        
        state = mAutomaton.getStates().elementAt(mMarkState);
        ter = mAlphabet.elementAt(mMarkToken);
        
        newExp = derivative(ter, mTreeGroup.elementAt(mMarkState).getRoot());
        newState = equivalence(newExp);
        ((StateFA)state).createTransitionOut(ter, newState);
        
        mCurrentDerive = newExp;
        
        return true;
    }//nextStep
    
    /**
     * Deriva la expresión regular que se le pasa en función del terminal ter.<br>
     * Sigue las reglas de derivación del método de las derivadas.
     * 
     * @param ter Terminal en función del que se deriva root.
     * @param root Nodo que contiene la expresión regular a derivar.
     * @return Nodo que contiene la expresión regular derivada.
     */
    private DeriveNode derivative (Terminal ter, DeriveNode root) {
        DeriveNode derLeft, derRight, temp;
        
            //Si no es hoja
        if(!root.isLeaf()){
            switch(root.getToken()){
                case '|':
                    derLeft = derivative(ter, (DeriveNode)root.getLeft());
                    derRight = derivative(ter, (DeriveNode)root.getRight());
                    return simplify(new DeriveNode('|', derLeft, derRight));
                case '.':
                    derLeft = derivative(ter, (DeriveNode)root.getLeft());
                    temp = simplify(new DeriveNode('.', derLeft, (DeriveNode)root.getRight()));
                        //Si en el lenguaje reconocido por el hijo izquierdo está épsilon 
                    if(recognizeEpsilon(((DeriveNode)root.getLeft()).getRegExp())){
                        derRight = derivative(ter, (DeriveNode)root.getRight());
                        return simplify(new DeriveNode('|', temp, derRight));
                    }
                    return temp;
                case '*':
                    derLeft = derivative(ter, (DeriveNode)root.getLeft());
                    return simplify(new DeriveNode('.', derLeft, root));
            }
        }   //Si es hoja
        else
            if(root.getToken() == ter.getToken())
                return new DeriveNode(TerminalEpsilon.EPSILON_CHARACTER);
            else
                return new DeriveNode(TerminalEmpty.EMPTY_CHARACTER);
        
        return root;
    }//derivative
    
    /**
     * Comprueba si la expresión regular que se le pasa reconoce épsilon.
     * 
     * @param s Expresión regular.
     * @return True si la expresión regular reconoce épsilon y false en caso contrario.
     */
    private boolean recognizeEpsilon (String s) {
        String temp = ((Character)TerminalEpsilon.EPSILON_CHARACTER).toString();
        
        if(Pattern.matches(s, "") || Pattern.matches(s, temp))
            return true;
        
        return false;
    }//recognizeEpsilon
    
    /**
     * Simplifica la expresión regular que se le pasa.
     * 
     * @param root Expresión regular a simplificar.
     * @return Expresión regular simplificada.
     */
    private DeriveNode simplify (DeriveNode root) {
        char empty = TerminalEmpty.EMPTY_CHARACTER;
        char epsilon = TerminalEpsilon.EPSILON_CHARACTER;
        
        switch(root.getToken()){
            case '|':
                if(root.getLeft().getToken() == empty)
                    return (DeriveNode)root.getRight();
                if(root.getRight().getToken() == empty)
                    return (DeriveNode)root.getLeft();
                if(root.getRight() == root.getLeft())
                    return (DeriveNode)root.getRight();
                break;
            case '.':
                if(root.getLeft().getToken() == epsilon)
                    return (DeriveNode)root.getRight();
                if(root.getRight().getToken() == epsilon)
                    return (DeriveNode)root.getLeft();
                if(root.getRight().getToken() == empty || root.getLeft().getToken() == empty)
                    return new DeriveNode(empty);
                break;
            case '*':
                if(root.getLeft().getToken() == epsilon || root.getLeft().getToken() == empty)
                    return new DeriveNode(epsilon);
                if(root.getLeft().getToken() == '*')
                    return (DeriveNode)root.getLeft();
                break;
        }

        return root;
    }//simplify
    
    /**
     * Compara si el autómata que representa expReg ya le tenemos en nuestra lista.<br>
     * Con ésto conseguimos no crear estados que representen la misma expresión regular.<br>
     * Si el autómata es nuevo, creamos un estado y le asignamos como etiqueta la expresión
     * regular de éste.
     * 
     * @param expReg Expresión regular en forma de árbol.
     * @return Estado que representa la expresión regular.
     */
    private State equivalence (DeriveNode expReg) {
        EquivalenceFA equi;
        DeriveTree tempTree;
        State temp;
        
            //Si el árbol es '$'
        if(expReg.getToken() == TerminalEmpty.EMPTY_CHARACTER){
            temp = mAutomaton.findState("Drain");
                //Comprobación para no meter en mTreeGroup '$' mas de una vez
            if(temp == null){
                mTreeGroup.add(new DeriveTree(new DeriveNode(TerminalEnd.END_CHARACTER)));
                temp = mAutomaton.createState("Drain");
            }
            return temp;
        }
        
        DeriveTree tree = new DeriveTree(expReg);
        try{
            for(int i=0; i<mTreeGroup.size(); i++){
                tempTree = mTreeGroup.elementAt(i);
                
                if(!tempTree.getRoot().getToken().equals(TerminalEnd.END_CHARACTER)){
                    equi = new EquivalenceFA(tree.getAutomaton(), tempTree.getAutomaton());
                    if(equi.allSteps() && equi.getAccept())
                        return mAutomaton.getStates().elementAt(i);
                }
            }
        }catch(InitializeException ie){
            System.err.print(ie);
        }
            //Creamos el estado
        temp = mAutomaton.createState(mStatesName.toString(), 
                    tree.getAutomaton().getInitialState().isFinal());
        temp.setLabel(expReg.getRegExp().toString());
        mStatesName++;
        mTreeGroup.add(tree);
        
        return temp;
    }//equivalence

    /**
     * Ejecuta todos los pasos del algoritmo.
     * 
     * @return True si el autómata ha sido creado con éxito, falso en caso contrario.
     */
    public boolean allSteps (){
        if(firstStep())
            while(nextStep());
        
        if(mAutomaton != null)
            return true;
        
        return false;
    }//allSteps
    
    /**
     * Devuelve el nuevo automata determinista construído a partir de la expresión
     * regular.
     * 
     * @return El autómata creado.
     */
    public FiniteAutomaton getSolution(){

        return (FiniteAutomaton)mAutomaton;
    }//getSolution
    
}//Derive
