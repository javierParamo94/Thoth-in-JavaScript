package core;

import java.util.Vector;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de establecer la interfaz de todos los aut�matas.
 * <p>
 * <b>Detalles</b><br>
 * Define la estructura a seguir por todos los aut�matas. Tambi�n implementa los 
 * m�todos comunes a todos ellos.<br>
 * Sirve de superclase a aut�matas finitos, aut�matas de pila y m�quinas de Turing. 
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Aut�mata.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla Saiz
 * @version 2.0
 */
public abstract class Automaton{
    
	// Constants -------------------------------------------------------------------
	
	/**
	 * Constantes que definen los tipos de automatas
	 */

	/**
	 * Automata finito
	 */	
	public static String FINITE_AUTOMATON = "FA";
	
	/**
	 * Automata de Pila
	 */
	public static String PUSH_DOWN_AUTOMATON = "PDA";
	
	/**
	 * Maquina de Turing
	 */	
	public static String TURING_MACHINE = "TM";	
	
	/**
	 * Maquina de Turing Escribir o Mover
	 */	
	public static String WRITE_MOVE_TURING_MACHINE = "WMTM";	
	
	/**
	 * Maquina de Turing con cinta limitada en un sentido
	 */	
	public static String LIMITED_TAPE_TURING_MACHINE = "LTTM";	
	
	/**
	 * Maquina de Turing binaria
	 */	
	public static String BINARY_TURING_MACHINE = "BTM";	
	
	/**
	 * Maquina de Turing multicabeza
	 */	
	public static String MULTI_HEAD_TURING_MACHINE = "MHTM";
	
	/**
	 * Maquina de Turing multipista
	 */	
	public static String MULTI_TRACK_TURING_MACHINE = "MTRTM";	
	
	/**
	 * Maquina de Turing multicinta
	 */	
	public static String MULTI_TAPE_TURING_MACHINE = "MTTM";
	
    // Attributes ------------------------------------------------------------------
    
	/**
	 * Nombre e identificador del aut�mata. 
	 */
	protected String mName;
	
	/**
	 * Estado inicial en el que comienza el aut�mata.
	 */
	protected State mInitialState;
	
	/**
	 * Alfabeto de entrada compuesto por terminales.
	 */
	protected Vector<Terminal> mAlphabet;

	/**
	 * Estado/s actual/es en el/los que se encuentra el aut�mata actualmente.
	 */
	protected Vector<State> mCurrentStates;
	
	/**
	 * Estados del aut�mata.
	 */
	protected Vector<State> mStates;
    
    /**
     * Indica si el aut�mata es determinista o no.
     */
    protected boolean mDeterministic;
    
    /**
     * Etiqueta del aut�mata en XML.
     */
    public static String mLabelXML = "automaton";
	
	// Methods ---------------------------------------------------------------------
	
	/**
	 * Constructor b�sico.
	 * 
	 * @param name Nombre del aut�mata.
	 */
	public Automaton (String name){
		mName = name;
        mInitialState = null;
        mDeterministic = false;
		mAlphabet = new Vector<Terminal>(5,5);
		mCurrentStates = new Vector<State>(5,5);
		mStates = new Vector<State>(10,5);
		
	}//Automaton
	
	/**
	 * Constructor completo.
	 * 
	 * @param name Nombre del aut�mata.
	 * @param alphabet Alfabeto de entrada.
	 */
	public Automaton (String name, Vector<Terminal> alphabet){
		mName = name;
        mInitialState = null;
        mDeterministic = false;
		mAlphabet = alphabet;
		mCurrentStates = new Vector<State>();
		mStates = new Vector<State>();
		
	}//Automaton
	
    /**
     * Inicializa el estado actual al estado inicial del aut�mata.
     *
     * @return Devuelve true si el estado actual ha podido ser inicializado. En
     * caso contrario devuelve false.
     */
    public boolean initialize (){
        if(mInitialState != null){
            mCurrentStates.clear();
            mCurrentStates.add(getInitialState());
        }//if
        
        return hasInitial();
    }//initialize
    
    /**
     * Devuelve true si existe estado inicial en el aut�mata.
     * 
     * @return True si existe estado inicial, false en caso contrario.
     */
    public boolean hasInitial (){
        if(mInitialState != null)
            return true;
        
        return false;
    }//hasInitial
    
    /**
     * Devuelve true si el aut�mata tiene al menos un estado final.
     * 
     * @return True si existe un estado inicial, false en caso contrario.
     */
    public boolean hasFinal (){
        for(int i=0; i<mStates.size(); i++)
            if(mStates.elementAt(i).isFinal())
                return true;
        
        return false;
    }//hasFinal
    
	/**
	 * Devuelve el nombre del aut�mata.
	 * 
	 * @return El nombre del aut�mata.
	 */
	public String getName (){
		
		return mName;
	}//getName
	
	/**
	 * Cambia el nombre del aut�mata.
	 * 
	 * @param name El nombre del aut�mata
	 */
	public void setName (String name){
		mName = name;
		
	}//setName
	
	/**
	 * Devuelve el estado inicial del aut�mata.
	 * 
	 * @return El estado inicial.
	 */
	public State getInitialState (){
		
		return mInitialState;
	}//getInitialState
	
	/**
	 * Cambia el estado inicial del aut�mata.<br>
     * Comprueba que el estado inicial ya pertenezca al automata, si no es as�
     * le agrega al aut�mata y se le indica que el estado es inicial.
	 * 
	 * @param initial Estado inicial.
	 */
	public void setInitialState (State initial){
        if(!mStates.contains(initial) && initial != null)
            addState(initial);
        for (State st : mStates)
        	st.setInitial(false);
        if(initial!=null)
        	initial.setInitial(true);
        mInitialState = initial;
        initialize();
		
	}//setInitialState
	
    /**
     * Devuelve el alfabeto de estados del aut�mata.
     * 
     * @return Devuelve el vector con todos los estados.
     */
    public Vector<State> getStates (){
        
        return mStates;
    }//getStates
    
    /**
	 * Devuelve el alfabeto de entrada del aut�mata.
	 * 
	 * @return El alfabeto de entrada.
	 */
    
	public Vector<Terminal> getAlphabet (){
		
		return mAlphabet;
	}//getAlphabet
	
	/**
	 * Cambia el alfabeto de entrada del aut�mata.
	 * 
	 * @param alphabet El nuevo alfabeto.
	 */
	public void setAlphabet (Vector<Terminal> alphabet){
		mAlphabet = alphabet;
		
	}//setAlphabet
	
	/**
	 * A�ade un terminal al alfabeto.<br>
	 * Si el terminal no se encuentra en el alfabeto le introduce y si ya est� no le
	 * introduce.<br>
     * Insertar� los terminales ordenados seg�n el c�digo ASCII.
	 * 
	 * @param terminal El terminal a a�adir. 
	 */
	public void addAlphabetToken (Terminal terminal){
		if (!mAlphabet.contains(terminal)){
            for(int i=0; i<mAlphabet.size(); i++)
                if(mAlphabet.elementAt(i).getToken() > terminal.getToken()){
                    mAlphabet.add(i, terminal);
                    return;
                }
            mAlphabet.add(terminal);
		}
		
	}//addAlphabetToken
	
	/**
	 * Elimina un terminal del alfabeto.<br>
	 * 
	 * @param terminal El terminal a eliminar.
	 */
	public void removeAlphabetToken (Terminal terminal){
		mAlphabet.remove(terminal);
		
	}//removeAlphabetToken
	
	/**
	 * Devuelve los estados en los que se encuentra.
	 * 
	 * @return Los estados en los que se encuentra.
	 */
	public Vector<State> getCurrentStates (){
		
		return mCurrentStates;
	}//getCurrentState
	    
    /**
     * Cambia los estados actuales.  
     * 
     * @param currentStates Lista de estados actuales.
     */
    public void setCurrentStates (Vector<State> currentStates){
        mCurrentStates = currentStates;
        
    }//setCurrentStates

    /**
     * A�ade un nuevo estado a la lista de estados actuales.<br>
     * Si el estado no se encuentra en la lista de estados actuales le a�ade.
     * 
     * @param currentState Estados actual.
     */
    public void addCurrentState (State currentState){
        if(!mCurrentStates.contains(currentState)){
            mCurrentStates.add(currentState);
        }
        
    }//addCurrentStates
    
    /**
     * Elimina un estado concreto de la lista de estados actuales.
     * 
     * @param currentState Estado que queremos eliminar.
     */
    public void removeCurrentState (State currentState){
        mCurrentStates.remove(currentState);
        
    }//removeCurrentStates
    
    /**
     * Devuelve true si el aut�mata es determinista, o false si no lo es.
     * 
     * @return Si es determinista o no.
     */
    public boolean getDeterministic (){
        
        return mDeterministic;
    }//getDeterministic
    
    /**
     * Determina si el aut�mata tiene estado inicial y al menos un estado final
     * 
     * @return False si no cumple las condiciones, true en caso contrario.
     */
    public boolean checkInitialFinal (){
        
        if(mInitialState != null)
            for(State state : mStates)
                if(state.isFinal())
                    return true;

        return false;
    }//checkInitialFinal
    
    /**
     * Obtiene los datos del aut�mata para mostrarlos en una tabla de transici�n.
     * 
     * @return Array de dos dimensiones con los datos del aut�mata.
     */
    public abstract Object[][] getData ();
    
    /**
     * Crea un estado dentro de este aut�mata asign�ndole s�lo el nombre.<br>
     * Si el estado no existe lo crea y lo devuelve; si el estado ya existe devuelve la
     * referencia a dicho estado.
     * 
     * @param name Nombre del nuevo estado.
     * @return El nuevo estado.
     */
    public abstract State createState (String name);
    
    /**
     * Crea un estado dentro de este aut�mata asign�ndole el nombre y si es o no final.<br>
     * Si el estado no existe le crea y le devuelve; si el estado ya existe devuelve la
     * referencia a dicho estado cambi�ndole �nicamente si es o no final.
     * 
     * @param name Nombre del nuevo estado.
     * @param fin Si es o no final.
     * @return El nuevo estado.
     */
    public State createState (String name, boolean fin){
        State temp;
        
        temp = createState(name);
        temp.setFinal(fin);
        
        return temp;
    }//createState

    /**
     * A�ade un nuevo estado al aut�mata.<br>
     * Si el estado ya existe no le a�ade a la lista de estado del aut�mata si,
     * por el contrario no exist�a le a�ade y pasa a formar parte del aut�mata. 
     * 
     * @param state El estado a a�adir.
     * @return El estado que se ha a�adido, o el estado que ya exist�a.
     */
    public State addState (State state){
    	State stRet;
        if (!mStates.contains(state)){
            mStates.add(state);            
            stRet=state;
        }
        else{
            stRet=findState(state.getName());
        }
        if(state.isInitial()){
        	for (State st : mStates)
        		st.setInitial(false);
        	state.setInitial(true);
        	this.setInitialState(state);
        }
        return stRet;
    }//addState    
    
	/**
	 * Elimina el estado especificado del aut�mata.<br>
     * Si el estado que borramos es el estado inicial, aut�mata deja de
     * tener un estado inicial.
	 * 
	 * @param name Nombre del estado que se desea eliminar.
	 */
	public void removeState (String name){
		State temp = findState(name);
		
		if (temp != null) {
                //Si borramos el estado inicial el automata se queda sin �l. 
            if(temp == mInitialState)
                mInitialState = null;
            temp.destroyTransitions();
			mStates.remove(temp);
		}
		
	}//removeState
	
	/**
	 * Busca un estado dentro del aut�mata.<br>
	 * Busca el estado entre todos los estados del aut�mata. Si el estado con �ste 
	 * nombre no existe en este aut�mata, devolver� una referencia nula.
	 * 
	 * @param name Nombre del estado.
	 * @return El estado con el nombre indicado, o null si no existe en �ste aut�mata.
	 */
	public State findState (String name){
		State temp;
		
		for (int i=0; i<mStates.size(); i++){
			temp = mStates.elementAt(i);
			if (temp.getName().equals(name)){
				return temp;
			}
		}//for
		
		return null;
	}//findState
    
    /**
     * Busca una transici�n dentro del aut�mata.<br>
     * Busca la transici�n que tenga el identificador que se le pasa, devolver�
     * una referencia nula.
     * 
     * @param id Identificador de la transici�n.
     * @return La transici�n que se buscaba o null si no existe una transici�n con
     * dicho identificador devuelve null.
     */
    public Transition findTransition (int id) {
        for(State st : mStates)
            for(Transition tr : st.getTransitionsOut())
                if(tr.getID() == id)
                    return tr;
        
        return null;
    }//findTransition
    
    /**
     * Devuelve si es determinista el aut�mata o no.
     * Guarda los estados no deterministas en un vector. 
     * 
     * @param states Vector donde se guardan los estados no deterministas.
     * @return True si es determinista, falso en caso contrario.
     */
    public abstract boolean isDeterministic (Vector<State> states);    

    /**
     * M�todo de clonaci�n.<br>
     * Se encarga de clonarse a s� mismo; se trata de una clonaci�n en profundidad.
     * 
     *  @return Clon de �ste aut�mata.
     */
    public abstract Automaton clone ();
    
    /**
     * Devuelve el tipo de aut�mata que se esta manejando. Los tipos de aut�mata
     * posibles bienen definidos por las constantes:
     * 
	 * Automata finito : FINITE_AUTOMATON 
	 * Automata de pila : PUSH_DOWN_AUTOMATON
	 * M�quina de Turing : TURING_MACHINE
	 * M�quina de Turing "escribir o mover" : WRITE_MOVE_TURING_MACHINE
	 * M�quina de Turing con cinta limitada : LIMITED_TAPE_TURING_MACHINE
	 * M�quina de Turing binaria : BINARY_TURING_MACHINE
	 * M�quina de Turing multicabezal : MULTI_HEAD_TURING_MACHINE
	 * M�quina de Turing multicinta : MULTI_TAPE_TURING_MACHINE
	 * M�quina de Turing multipista : MULTI_TRACK_TURING_MACHINE
	 * 
	 * @return Tipo de aut�mata
     */    
    public abstract String getAutomatonType();
    
    /**
     * Comprueba si existe un estado con nombre en el aut�mata.
     * 
     * @param name Nombre de estado.
     * @return Si existe un estado en el aut�mata con el nombre dado.
     */
    public boolean containsState(String name){
    	for(State s: mStates){
			if(s.getName().equals(name))
				return true;
		}
    	return false;
    }   
    
}//Automaton
