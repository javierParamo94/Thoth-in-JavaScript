package algorithms.equation;

import java.util.Vector;

import algorithms.Algorithm;
import algorithms.InitializeException;
import algorithms.regExp.derive.DeriveNode;

import core.*;

/**
 * <b>Descripción</b><br>
 * Implementa el método de obtención de expresión regular a partir de un autómata
 * finito.
 * <p>
 * <b>Detalles</b><br>
 * Inicializa las ecuaciones características, ejecuta el lema de Arden sobre ellas
 * y por último resuelve la del estado inicial.<br>
 * Para resolverlo tendrá que ir obtniendo el resultado de las ecuaciones que necesite.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el método de las ecuaciones características.<br>
 * Posibilita el paso a paso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public class CharacteristicEquation extends Algorithm {
    
    // Attributes ------------------------------------------------------------------
    
    /**
     * Lista de ecuaciones características obtenidas del autómata.
     */
    public Vector<Equation> mEquations;
    
    /**
     * Indica el primer paso del algoritmo
     */
    private boolean mFirstTime;
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo de las ecuaciones características.
     * 
     * @param auto Autómata del que queremos obtener su expresión regular
     * @throws InitializeException Si el autómata no tiene estado inicial o final.
     */
    public CharacteristicEquation (Automaton auto) throws InitializeException {
        super(auto.clone());
        mEquations = new Vector<Equation>(5, 5);
        mFirstTime = true;
        
    }//CharacteristicEquation
    
    /**
     * Devuelve el autómata sobre el que se va a aplicar el algoritmo.
     * 
     * @return Autómata del algoritmo.
     */
    public Automaton getAutomaton () {
        
        return mAutomaton;
    }//getAutomaton

    /**
     * Inicializa la lista de ecuaciones características sobre la que vamos a trabajar
     * a lo largo del algoritmo.<br>
     * Hace que el estado inicial sea el primero.
     * 
     * @return True si debe seguir el algoritmo y false en caso contrario.
     */
    public boolean firstStep(){
        State st;
        Equation eq;
        
        if(mAutomaton == null)
            return false;
        
            //Hacemos que el estado inicial sea el primero del vector de estados
        if(mAutomaton.getStates().size() > 1)
            for(int i=0; i<mAutomaton.getStates().size(); i++)
                if(mAutomaton.getStates().elementAt(i).equals(mAutomaton.getInitialState())){
                    st = mAutomaton.getStates().elementAt(
                            mAutomaton.getStates().indexOf(mAutomaton.getInitialState()));
                    mAutomaton.getStates().remove(st);
                    mAutomaton.getStates().insertElementAt(st, 0);
                    break;
                }
        
        for(int i=0; i<mAutomaton.getStates().size(); i++){
            st = mAutomaton.getStates().elementAt(i);
                //Si es el estado inicial
            if(st.equals(mAutomaton.getInitialState())){
                st.setLabel(((Integer)0).toString());
                eq = new Equation(0);
            }   //Los demás estados
            else{
                st.setLabel(new Integer(i).toString());
                eq = new Equation(i);
            }
                //Para cada transición
            for(Transition trans : st.getTransitionsOut())
                eq.addExpression(new DeriveNode(trans.getIn().getToken()),
                        mAutomaton.getStates().indexOf(trans.getNextState()));
                //Si es final añadimos épsilon
            if(st.isFinal())
                eq.addExpression(new DeriveNode(TerminalEpsilon.EPSILON_CHARACTER), -1);
            else    //Si no tiene transiciones ni es final
                if(st.getTransitionsOut().isEmpty())
                    eq.addExpression(new DeriveNode(TerminalEmpty.EMPTY_CHARACTER), -2);
            
                //Agregamos eq a mEquations
            mEquations.add(eq);
        }
        
        return true;
    }//firstStep
    
    /**
     * La primera vez que se ejecuta el paso aplica el lema de Arden.<br>
     * Al siguiente paso resuelve la ecuación característica.
     * 
     * @return True si debe seguir aplicandose el método y false si ya hemos
     * acabado.
     */
    public boolean nextStep() {
        int initial = 0;
        
        if(mFirstTime){
            for(Equation equation : mEquations)
                equation.arden();
            mFirstTime= false;
            return true;
        }
        
        solve(initial);
        
        return false;
    }//nextStep
    
    /**
     * Resuelve la ecuación característica que tiene el índice que se le pasa.
     * 
     * @param index Índice del beta que queremos resolver.
     * @return Ecuación resultante.
     */
    private Equation solve (int index) {
        Equation eqTemp = null, eq = null;
        
            //Buscamos la equación cuya parte izquierda sea igual a index
        for(int i=0; i<mEquations.size(); i++)
            if(mEquations.elementAt(i).getLeft().getIndex().equals(index)){
                eq = mEquations.elementAt(i);
                for(int j=0; j<eq.getSize(); j++){
                        //Para cada expresión regular con índice mayor que beta
                    if(eq.getBetaAt(j).getIndex() > index){
                        eqTemp = solve(eq.getBetaAt(j).getIndex());
                        replace(eqTemp);
                        eq.group();
                        eq.arden();
                        j--;
                    }
                    else if(eq.getBetaAt(j).getIndex() == index){
                        eq.group();
                        eq.arden();
                    }
                }
                return eq;
            }
        
        return eq;
    }//solve
    
    /**
     * Reemplaza un determinado beta por la ecuación que se le pasa.<br>
     * Reemplaza en todas las ecuaciones características.
     * 
     * @param eqSolve Solución a un determinado beta.
     */
    private void replace (Equation eqSolve) {
        DeriveNode old, nodeTemp;
        
        for(Equation eq : mEquations)
            for(int j=0; j<eq.getSize(); j++)
                if(eq.getBetaAt(j).equals(eqSolve.getLeft())){
                    old = eq.getExpAt(j);
                        //Si tenemos para sustituir el lenguaje vacío
                    if(eqSolve.getBetaAt(0).getIndex() == -2){
                        eq.removeExpression(j);
                        if(eq.isEmpty())
                            eq.addExpression(new DeriveNode(TerminalEmpty.EMPTY_CHARACTER), -2);
                        else
                            j--;
                    }
                    else
                        for(int k=0; k<eqSolve.getSize(); k++){
                            nodeTemp = new DeriveNode('.', old, eqSolve.getExpAt(k));
                            nodeTemp = simplify(nodeTemp);
                            if(k == 0){
                                eq.setExp(j, nodeTemp);
                                eq.setBeta(j, eqSolve.getBetaAt(k));
                            }
                            else
                                eq.addExpression(nodeTemp, eqSolve.getBetaAt(k).getIndex());
                        }
                }//if
        
    }//replace
    
    /**
     * Simplifica la expresión regular que se le pasa.
     * 
     * @param root Expresión regular a simplificar.
     * @return Expresión regular simplificada.
     */
    public static DeriveNode simplify (DeriveNode root) {
        char empty = TerminalEmpty.EMPTY_CHARACTER;
        char epsilon = TerminalEpsilon.EPSILON_CHARACTER;
        
        switch(root.getToken()){
            case '|':
                if(root.getLeft().getToken() == empty)
                    return (DeriveNode)root.getRight();
                if(root.getRight().getToken() == empty)
                    return (DeriveNode)root.getLeft();
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
                if(root.getLeft().getToken() == epsilon)
                    return (DeriveNode)root.getLeft();
                break;
        }

        return root;
    }//simplify
    
    /**
     * Ejecuta todos los estados del algoritmo.
     * 
     * @return True si se ha podido ejecutar el algoritmo y false en caso contario.
     */
    public boolean allSteps(){
        if(firstStep()){
            nextStep();
            nextStep();
            return true;
        }
        
        return false;
    }//allSteps
    
    /**
     * Obtiene la solución del algoritmo
     * 
     * @return Cadena con la expresión regular obtenida.
     */
    public String getSolution () {
        for(Equation temp : mEquations)
            if(temp.getLeft().getIndex() == 0)
                return temp.toString();
        
        return null;
    }//getSolution

}//CharacteristicEquation
