package core.finiteautomaton;

import java.util.Vector;

import core.*;

/**
 * <b>Descripci�n</b><br>
 * Proporciona la funcionalidad que deben tener los estados de Aut�mata Finito.
 * <p>
 * <b>Detalles</b><br>
 * Implementa los estados abstractos heredados de State y aumenta con nuevos
 * m�todos la funcionalidad de �stos.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Estado de Aut�mata Finito.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla Saiz
 * @version 2.0
 */
public class StateFA extends State implements Cloneable{
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param name Nombre del estado del aut�mata finito.
     */
    public StateFA (String name){
        super(name);

    }//StateFA
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre del estado del aut�mata finito.
     * @param fin Es final o no.
     */
    public StateFA (String name, boolean fin){
        super(name, fin);

    }//StateFA
    
    /**
     * Crea una nueva transici�n de entrada y la a�ade.<br>
     * El terminal de la transici�n ser� por defecto epsilon.
     * 
     * @param prevState Estado origen de la transici�n.
     * @return La transici�n creada.
     */
    public Transition createTransitionIn (State prevState){
        
        return new TransitionFA(prevState, this);
    }//createTransitionIn
    
    /**
     * Crea una nueva transici�n de entrada y la a�ade.
     * 
     * @param in Terminal de entrada.
     * @param prevState Estado origen de la transici�n.
     * @return La transici�n creada.
     */
    public Transition createTransitionIn (Terminal in, State prevState){
        
        return new TransitionFA(in, prevState, this);
    }//createTransitionIn    
    
    /**
     * Crea una nueva transici�n de salida y la a�ade.<br>
     * El terminal de la transici�n por defecto ser� �psilon.
     * 
     * @param nextState Estado destino de la transici�n.
     * @return La transici�n creada.
     */
    public Transition createTransitionOut (State nextState){
        
        return new TransitionFA(this, nextState);     
    }//createTransitionOut
    
    /**
     * Crea una nueva transici�n de salida y la a�ade.
     * 
     * @param in Terminal de entrada.
     * @param nextState Estado destino de la transici�n.
     * @return La transici�n creada.
     */
    public Transition createTransitionOut (Terminal in, State nextState){
        
        return new TransitionFA(in, this, nextState);
    }//createTransitionOut
    
    /**
     * Crea una nueva transici�n de salida y la a�ade.
     * 
     * @param in Terminal de entrada.
     * @param nextState Estado destino de la transici�n.
     * @param id Identificador de la transici�n.
     * @return La transici�n creada.
     */
    public Transition createTransitionOut (Terminal in, State nextState, int id){
        
        return new TransitionFA(in, this, nextState, id);
    }//createTransitionOut    
    
    /**
     * Devuelve un vector con los posibles siguientes estados seg�n la entrada.<br>
     * Si el aut�mata finito es determinista tan s�lo devolver� un estado dentro del
     * vector.
     * 
     * @param in Terminal de entrada.
     * @return Vector de siguientes estados.
     */
    public Vector<State> nextStates (Terminal in){
        Vector<State> lockState = new Vector<State>();
        Vector<State> targetStates = new Vector<State>();
        State tempState, tempOut;
        
            //1.- Obtenemos la cerradura epsilon de este estado.
        lockEpsilon(lockState);
            //Si la entrada est� vac�a, devolvemos el cierre de este estado.
        if(in == null)
            return lockState;
        
            //2.- Consumir la entrada
        for(int i=0; i<lockState.size(); i++){
            tempState = lockState.elementAt(i);
                //Recorremos las transiciones de salida del estado lockState(i) = tempState
            for(int j=0; j<tempState.getTransitionsOut().size(); j++)
                    //Si la transici�n de salida de lockState(i) es igual a in.
                if(tempState.getTransitionsOut().elementAt(j).getIn().equals(in)){
                    tempOut = tempState.getTransitionsOut().elementAt(j).getNextState();
                        // Si ya hemos pasado por ese estado no le almacenamos.
                    if(!targetStates.contains(tempOut))
                        targetStates.add(tempOut);
                }//if
        }//for
        
            //3.- Hacemos la cerradura epsilon despu�s de consumir
        for(int i=0; i<targetStates.size(); i++){
            StateFA st=(StateFA) targetStates.elementAt(i);
            st.lockEpsilon(targetStates);
        }
            
        
            //4.- Devolvemos el vector con los estados siguientes.
        return targetStates;
    }//nextStates
    
    /**
     * M�todo recursivo que almacena la cerradura epsilon de este estado.
     * 
     * @param list Vector de estados que utiliza para evitar bucles y repeticiones
     * de estados.
     */
    public void lockEpsilon (Vector<State> list){
        StateFA tempOut;
        
        if(!list.contains(this))
            list.add(this);
        
            //Recorremos las transiciones de salida comprobando si son �psilon
        for(int i=0; i<mTransitionsOut.size(); i++)
            if(mTransitionsOut.elementAt(i).getIn().equals(new TerminalEpsilon())){
                tempOut = (StateFA)mTransitionsOut.elementAt(i).getNextState();
                if(!list.contains(tempOut)){
                    list.add(tempOut);
                    tempOut.lockEpsilon(list);
                }
            }//if
        
    }//lockEpsilon
    
    /**
     * Clona el estado y lo devuelve.
     * 
     * @return Estado clonado.
     */
    public State clone (){
        StateFA state;
        
        state = new StateFA(mName, mFinal);
        state.setLabel(mLabel);
        state.setInitial(this.mInitial);
        
        return state;
    }//clone
    
    /**
     * Clona las transiciones del estado que se le pasa por par�metro.
     * 
     * @param state Estado del que queremos clonar sus transiciones.
     * @param vState Vector de estados del aut�mata clonado.
     */
    public void cloneTransitions (State state, Vector<State> vState){
        Transition temp;
        
        for(Transition tran : state.getTransitionsOut()){
            temp = ((TransitionFA)tran).clone();
            createTransitionOut(temp.getIn(), vState.elementAt(vState.indexOf(temp.getNextState())));
        }
        
    }//cloneTransitions

    /**
     * Devuelve un String con el tipo de la transici�n. Debe concordar con uno de los
     * tipos definidos en la clase Automaton.
     * 
     * @see core.Automaton
     * 
     * @return Tipo del estado
     */ 
	public String getStateType() {
		return Automaton.FINITE_AUTOMATON;
	}//getStateType

    
}//StateFA
