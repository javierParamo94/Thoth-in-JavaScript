package core.turingmachine.binary;

import java.util.Vector;

import core.Automaton;
import core.State;
import core.Terminal;
import core.turingmachine.TuringMachine;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de proporcionar toda la funcionalidad que debe tener una M�quina de Turing Binaria.
 * <p>
 * <b>Detalles</b><br>
 * Nos proporciona todas las operaciones que se pueden obtener de la m�quina de
 * estados de una m�quina de Turing binaria.<br>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Maquina de Turing binaria.
 * </p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public class BinaryTuringMachine extends TuringMachine implements Cloneable{
    	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param name Nombre de la m�quina de turing binaria a crear.
     */
    public BinaryTuringMachine (String name) {
        super(name);

    }//BinaryTuringMachine
    
    /**
     * Constructor con alfabeto de entrada.
     * 
     * @param name Nombre de la m�quina de turing binaria a crear.
     * @param alphabet Alfabeto de entrada de la m�quina de turing binaria.
     */
    public BinaryTuringMachine (String name, Vector<Terminal> alphabet){
        super(name, alphabet);
    }//BinaryTuringMachine
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre de la m�quina de turing binaria a crear.
     * @param alphabet Alfabeto de entrada de la m�quina de turing binaria.
     * @param alphabetTape Alfabeto de cinta de la m�quina de turing binaria.
     */
    public BinaryTuringMachine (String name, Vector<Terminal> alphabet, Vector<Terminal> alphabetTape){
        super(name, alphabet,alphabetTape);        
    }//BinaryTuringMachine
    
    /**
     * Crea un estado dentro de esta m�quina de turing binaria asign�ndole s�lo el nombre.<br>
     * Si el estado no existe lo crea y lo devuelve; si el estado ya existe devuelve la
     * referencia a dicho estado.
     * 
     * @param name Nombre del nuevo estado.
     * @return El nuevo estado.
     */
    public State createState (String name){
        StateBTM state = new StateBTM (name);
        
        return addState(state);
    }//createState
    
    /**
     * M�todo de clonaci�n.<br>
     * Se encarga de clonarse a s� mismo; se trata de una clonaci�n en profundidad.
     * 
     *  @return Clon de esta maquina de turing binaria.
     */
    public Automaton clone (){
        TuringMachine automaton = new BinaryTuringMachine(mName);
        
            //Clonamos sus estados
        for(int i=0; i<mStates.size(); i++)
            automaton.addState(((StateBTM)(mStates.elementAt(i))).clone());
            //Clonamos las transiciones de los estados
        for(int i=0; i<mStates.size(); i++)
            automaton.getStates().elementAt(i).cloneTransitions(mStates.elementAt(i), automaton.getStates());
            //Asignamos el estado inicial
        if(mInitialState != null)
            automaton.setInitialState(automaton.findState(getInitialState().getName()));
      //Actualizamos el alfabeto del clon
        for(int i=0; i<mAlphabet.size(); i++){
            automaton.addAlphabetToken(mAlphabet.elementAt(i));
            automaton.addAlphabetTape(mAlphabetTape.elementAt(i));
        }
        
        return automaton;
    }//clone

    /**
     * Devuelve el tipo de aut�mata que se esta manejando.
	 * 
	 * @return Tipo de aut�mata
     */
	public String getAutomatonType() {
		return Automaton.BINARY_TURING_MACHINE;
	}//getAutomatonType
    
}//BinaryTuringMachine
