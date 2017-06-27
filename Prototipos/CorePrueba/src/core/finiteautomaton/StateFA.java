package core.finiteautomaton;

import java.util.Vector;

import core.*;

/**
 * <b>Descripción</b><br>
 * Proporciona la funcionalidad que deben tener los estados de Autómata Finito.
 * <p>
 * <b>Detalles</b><br>
 * Implementa los estados abstractos heredados de State y aumenta con nuevos
 * métodos la funcionalidad de éstos.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Estado de Autómata Finito.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Saiz
 * @version 2.0
 */
public class StateFA extends State implements Cloneable{
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico.
     * 
     * @param name Nombre del estado del autómata finito.
     */
    public StateFA (String name){
        super(name);

    }//StateFA
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre del estado del autómata finito.
     * @param fin Es final o no.
     */
    public StateFA (String name, boolean fin){
        super(name, fin);

    }//StateFA
    
    /**
     * Crea una nueva transición de entrada y la añade.<br>
     * El terminal de la transición será por defecto epsilon.
     * 
     * @param prevState Estado origen de la transición.
     * @return La transición creada.
     */
    public Transition createTransitionIn (State prevState){
        
        return new TransitionFA(prevState, this);
    }//createTransitionIn
    
    /**
     * Crea una nueva transición de entrada y la añade.
     * 
     * @param in Terminal de entrada.
     * @param prevState Estado origen de la transición.
     * @return La transición creada.
     */
    public Transition createTransitionIn (Terminal in, State prevState){
        
        return new TransitionFA(in, prevState, this);
    }//createTransitionIn    
    
    /**
     * Crea una nueva transición de salida y la añade.<br>
     * El terminal de la transición por defecto será épsilon.
     * 
     * @param nextState Estado destino de la transición.
     * @return La transición creada.
     */
    public Transition createTransitionOut (State nextState){
        
        return new TransitionFA(this, nextState);     
    }//createTransitionOut
    
    /**
     * Crea una nueva transición de salida y la añade.
     * 
     * @param in Terminal de entrada.
     * @param nextState Estado destino de la transición.
     * @return La transición creada.
     */
    public Transition createTransitionOut (Terminal in, State nextState){
        
        return new TransitionFA(in, this, nextState);
    }//createTransitionOut
    
    /**
     * Crea una nueva transición de salida y la añade.
     * 
     * @param in Terminal de entrada.
     * @param nextState Estado destino de la transición.
     * @param id Identificador de la transición.
     * @return La transición creada.
     */
    public Transition createTransitionOut (Terminal in, State nextState, int id){
        
        return new TransitionFA(in, this, nextState, id);
    }//createTransitionOut    
    
    /**
     * Devuelve un vector con los posibles siguientes estados según la entrada.<br>
     * Si el autómata finito es determinista tan sólo devolverá un estado dentro del
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
            //Si la entrada está vacía, devolvemos el cierre de este estado.
        if(in == null)
            return lockState;
        
            //2.- Consumir la entrada
        for(int i=0; i<lockState.size(); i++){
            tempState = lockState.elementAt(i);
                //Recorremos las transiciones de salida del estado lockState(i) = tempState
            for(int j=0; j<tempState.getTransitionsOut().size(); j++)
                    //Si la transición de salida de lockState(i) es igual a in.
                if(tempState.getTransitionsOut().elementAt(j).getIn().equals(in)){
                    tempOut = tempState.getTransitionsOut().elementAt(j).getNextState();
                        // Si ya hemos pasado por ese estado no le almacenamos.
                    if(!targetStates.contains(tempOut))
                        targetStates.add(tempOut);
                }//if
        }//for
        
            //3.- Hacemos la cerradura epsilon después de consumir
        for(int i=0; i<targetStates.size(); i++){
            StateFA st=(StateFA) targetStates.elementAt(i);
            st.lockEpsilon(targetStates);
        }
            
        
            //4.- Devolvemos el vector con los estados siguientes.
        return targetStates;
    }//nextStates
    
    /**
     * Método recursivo que almacena la cerradura epsilon de este estado.
     * 
     * @param list Vector de estados que utiliza para evitar bucles y repeticiones
     * de estados.
     */
    public void lockEpsilon (Vector<State> list){
        StateFA tempOut;
        
        if(!list.contains(this))
            list.add(this);
        
            //Recorremos las transiciones de salida comprobando si son épsilon
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
     * Clona las transiciones del estado que se le pasa por parámetro.
     * 
     * @param state Estado del que queremos clonar sus transiciones.
     * @param vState Vector de estados del autómata clonado.
     */
    public void cloneTransitions (State state, Vector<State> vState){
        Transition temp;
        
        for(Transition tran : state.getTransitionsOut()){
            temp = ((TransitionFA)tran).clone();
            createTransitionOut(temp.getIn(), vState.elementAt(vState.indexOf(temp.getNextState())));
        }
        
    }//cloneTransitions

    /**
     * Devuelve un String con el tipo de la transición. Debe concordar con uno de los
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
