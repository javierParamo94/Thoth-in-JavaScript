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
 * <b>Descripci�n</b><br>
 * Construye un aut�mata finito determinista a partir de una expresi�n regular dada.<br>
 * El aut�mata finito resultante ser� m�nimo y completo, para que sea completo tendr� un
 * estado sumidero que se podr� eliminar sin cambiar el lenguaje reconocido  por el aut�mata.
 * <p>
 * <b>Detalles</b><br>
 * A partir de una expresi�n regular y aplicando el m�todo de las derivadas va obteniendo
 * un aut�mata finito determinista m�nimo paso a paso.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Desarrolla el m�todo de las derivadas para la construcci�n de un AFD.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * @see DeriveTree
 * @see DeriveNode
 */
public class Derive extends Algorithm {

    // Attributes --------------------------------------------------------------------
    
    /**
     * Expresi�n regular de la que queremos calcular su aut�mata representada en
     * forma de �rbol.
     */
    private DeriveTree mRegExp;
    
    /**
     * Conjunto de aut�matas creados a partir de su expresi�n regular.
     */
    private Vector<DeriveTree> mTreeGroup;
    
    /**
     * Alfabeto de terminales del aut�mata.
     */
    private Vector<Terminal> mAlphabet;
    
    /**
     * Contador para ir asignando nombres a los estados del aut�mata.
     */
    private Integer mStatesName;
    
    /**
     * Contador para saber por qu� estado vamos.
     */
    private int mMarkState;
    
    /**
     * Contador para saber por qu� token del alfabeto vamos.
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
     * @param regExp Expresi�n regular en forma de �rbol.
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
     * Devuelve el estado que representa la expresi�n regular que
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
     * Devuelve la derivada de estado actual respecto al s�mbolo actual.
     * 
     * @return Expresi�n regular resultado.
     */
    public DeriveNode getCurrentDerive () {
        
        return mCurrentDerive;
    }//getCurrentDerive
    
    /**
     * Comprueba que el �rbol de la expresi�n regular tenga un nodo.<br>
     * Crea el estado inicial del nuevo aut�mata.
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
     * Deriva la expresi�n regular que corresponde con un terminal del alfabeto; si la
     * expresi�n resultante ya exist�a crea una transici�n a ese estado y sino crea el
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
     * Deriva la expresi�n regular que se le pasa en funci�n del terminal ter.<br>
     * Sigue las reglas de derivaci�n del m�todo de las derivadas.
     * 
     * @param ter Terminal en funci�n del que se deriva root.
     * @param root Nodo que contiene la expresi�n regular a derivar.
     * @return Nodo que contiene la expresi�n regular derivada.
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
                        //Si en el lenguaje reconocido por el hijo izquierdo est� �psilon 
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
     * Comprueba si la expresi�n regular que se le pasa reconoce �psilon.
     * 
     * @param s Expresi�n regular.
     * @return True si la expresi�n regular reconoce �psilon y false en caso contrario.
     */
    private boolean recognizeEpsilon (String s) {
        String temp = ((Character)TerminalEpsilon.EPSILON_CHARACTER).toString();
        
        if(Pattern.matches(s, "") || Pattern.matches(s, temp))
            return true;
        
        return false;
    }//recognizeEpsilon
    
    /**
     * Simplifica la expresi�n regular que se le pasa.
     * 
     * @param root Expresi�n regular a simplificar.
     * @return Expresi�n regular simplificada.
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
     * Compara si el aut�mata que representa expReg ya le tenemos en nuestra lista.<br>
     * Con �sto conseguimos no crear estados que representen la misma expresi�n regular.<br>
     * Si el aut�mata es nuevo, creamos un estado y le asignamos como etiqueta la expresi�n
     * regular de �ste.
     * 
     * @param expReg Expresi�n regular en forma de �rbol.
     * @return Estado que representa la expresi�n regular.
     */
    private State equivalence (DeriveNode expReg) {
        EquivalenceFA equi;
        DeriveTree tempTree;
        State temp;
        
            //Si el �rbol es '$'
        if(expReg.getToken() == TerminalEmpty.EMPTY_CHARACTER){
            temp = mAutomaton.findState("Drain");
                //Comprobaci�n para no meter en mTreeGroup '$' mas de una vez
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
     * @return True si el aut�mata ha sido creado con �xito, falso en caso contrario.
     */
    public boolean allSteps (){
        if(firstStep())
            while(nextStep());
        
        if(mAutomaton != null)
            return true;
        
        return false;
    }//allSteps
    
    /**
     * Devuelve el nuevo automata determinista constru�do a partir de la expresi�n
     * regular.
     * 
     * @return El aut�mata creado.
     */
    public FiniteAutomaton getSolution(){

        return (FiniteAutomaton)mAutomaton;
    }//getSolution
    
}//Derive
