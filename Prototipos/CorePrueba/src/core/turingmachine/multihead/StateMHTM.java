package core.turingmachine.multihead;

import java.util.Vector;

import core.Automaton;
import core.State;
import core.Terminal;
import core.Transition;
import core.turingmachine.StateTM;


/**
 * <b>Descripci�n</b><br>
 * Proporciona la funcionalidad que deben tener los estados de una maquina de turing multicabeza.
 * <p>
 * <b>Detalles</b><br>
 * Implementa los estados abstractos heredados de State y aumenta con nuevos
 * m�todos la funcionalidad de �stos.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Estado de Maquina de Turing multicabeza.
 * </p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public class StateMHTM extends StateTM implements Cloneable{


    //  Attributes ---------------------------------------------------------------------
    
    /**
     * N�mero de cabezas de la m�quina.
     */
    private int numHeads;
    
    /**
     * Tipo de modificaci�n.<br>
     * Si es 1 la modificaci�n ser� secuencial.<br>
     * Si es 2 la modificaci�n ser� paralela.
     */
    private int modification; 
	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param name Nombre del estado de la maquina de turing multicabeza.
     */
    public StateMHTM (String name){
        super(name);
        numHeads=0;
        modification=0;

    }//StateMHTM
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre del estado de la maquina de turing multicabeza.
     * @param fin Es final o no.
     */
    public StateMHTM (String name, boolean fin){
        super(name, fin);

    }//StateMTTM
    
    /**
     * Crea una nueva transici�n de entrada y la a�ade.
     * 
     * @param in Vector de terminales de lectura de cinta.
     * @param prevState Estado origen de la transici�n.
     * @param write Vector de terminales de escritura en cinta.
     * @param move Vector de caracteres que indican el movimiento de los cabezales de las cintas.
     * @return La transici�n creada.
     */
    public Transition createTransitionIn (Vector<Terminal> in, State prevState, Vector<Terminal> write, Vector<Character> move){
        
        return new TransitionMHTM(in, prevState, write, this, move);
    }//createTransitionIn
    
    /**
     * Crea una nueva transici�n de entrada y la a�ade.
     * 
     * @param in Vector de terminales de lectura de cinta.
     * @param prevState Estado origen de la transici�n.
     * @param write Vector de terminales de escritura en cinta.
     * @param move Vector de caracteres que indican el movimiento de los cabezales de las cintas.
     * @param id Identificador de la transici�n.
     * @return La transici�n creada.
     */
    public Transition createTransitionIn (Vector<Terminal> in, State prevState, Vector<Terminal> write, Vector<Character> move,int id){
        
        return new TransitionMHTM(in, prevState, write, this, move, id);
    }//createTransitionIn
    
    /**
     * Crea una nueva transici�n de salida y la a�ade.
     * 
     * @param in Vector de terminales de lectura de cinta.
     * @param nextState Estado destino de la transici�n.
     * @param write Vector de terminales de escritura en cinta.
     * @param move Vector de caracteres que indican el movimiento de los cabezales de las cintas.
     * @return La transici�n creada.
     */
    public Transition createTransitionOut (Vector<Terminal> in, State nextState, Vector<Terminal> write, Vector<Character> move){
        return new TransitionMHTM(in, this, write, nextState, move);
    }//createTransitionOut
    
    /**
     * Crea una nueva transici�n de salida y la a�ade.
     * 
     * @param in Vector de terminales de lectura de cinta.
     * @param nextState Estado destino de la transici�n.
     * @param write Vector de terminales de escritura en cinta.
     * @param move Vector de caracteres que indican el movimiento de los cabezales de las cintas.
     * @param id Identificador de la transici�n.
     * @return La transici�n creada.
     */
    public Transition createTransitionOut (Vector<Terminal> in, State nextState, Vector<Terminal> write, Vector<Character> move, int id){
        return new TransitionMHTM(in, this, write, nextState, move, id);
    }//createTransitionOut
    
    /**
     * Indica el n�mero de cabezas.
     * 
     * @param num N�mero de cabezas.
     */
    public void setHeads(int num){
    	numHeads=num; 
    }//setHeads
    
    /**
     * Devuelve el n�mero de cabezas.
     * 
     * @return El n�mero de cabezas.
     */
    public int getHeads(){
    	return numHeads; 
    }//getHeads
    
    /**
     * Indica el tipo de modificaci�n.
     * 
     * @param mod Tipo de modificaci�n.
     */
    public void setMod(int mod){
    	modification=mod; 
    }//setMod
    
    /**
     * Devuelve el tipo de modificaci�n.
     * 
     * @return Tipo de modificaci�n.
     */
    public int getMod(){
    	return modification; 
    }//getMod
    
    /**
     * Clona el estado y lo devuelve.
     * 
     * @return Estado clonado.
     */
    public State clone (){
        StateMHTM state;
        
        state = new StateMHTM(mName, mFinal);
        state.setLabel(mLabel);
        state.setInitial(this.mInitial);
        
        return state;
    }//clone    
    
    /**
     * Clona las transiciones del estado que se le pasa por par�metro.
     * 
     * @param state Estado del que queremos clonar sus transiciones.
     * @param vState Vector de estados de la maquina de turing multicabeza clonada.
     */
    public void cloneTransitions (State state, Vector<State> vState){
        TransitionMHTM temp;
        
        for(Transition tran : state.getTransitionsOut()){
            temp = ((TransitionMHTM)tran).clone();
            createTransitionOut(temp.getReadings(), vState.elementAt(vState.indexOf(temp.getNextState())),temp.getWritings(),temp.getMovements());
        }
        
    }//cloneTransitions
    
    /**
     * Devuelve un String con el tipo de la Estado. Debe concordar con uno de los
     * tipos definidos en la clase Automaton.
     * 
     * @see core.Automaton
     * 
     * @return Tipo del estado
     */ 
	public String getStateType() {
		return Automaton.MULTI_HEAD_TURING_MACHINE;
	}//getStateType
    
}//StateMHTM
