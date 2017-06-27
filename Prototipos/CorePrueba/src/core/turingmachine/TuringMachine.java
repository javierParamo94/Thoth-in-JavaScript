package core.turingmachine;

import java.util.Vector;

import core.Automaton;
import core.State;
import core.Terminal;
import core.Transition;

/**
 * <b>Descripción</b><br>
 * Se encarga de proporcionar toda la funcionalidad que debe tener una Máquina de Turing básica.
 * <p>
 * <b>Detalles</b><br>
 * Nos proporciona todas las operaciones que se pueden obtener de la máquina de
 * estados de una máquina de Turing básica.<br>
 * Proporciona métodos que servirán para algunas de las distintas maquinas de Turing como:<br>
 * <p>
 * <ul>
 * <li>Máquina de Turing "escribir o mover".
 * <li>Máquina de Turing "mover o permanecer".
 * <li>Máquina de Turing con cinta limitada en un sentido.
 * <li>Máquina de Turing binaria.
 * </ul>
 * Estas máquinas corresponden con las restricciones del modelo básico.<br>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Maquina de Turing.
 * </p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class TuringMachine extends Automaton implements Cloneable{
    
	// Attributes ------------------------------------------------------------------
    		
	/**
	 * Alfabeto de cinta compuesto por terminales.
	 */
	protected Vector<Terminal> mAlphabetTape;
	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor básico.
     * 
     * @param name Nombre de la máquina de turing a crear.
     */
    public TuringMachine (String name) {
        super(name);
        mAlphabetTape=new Vector<Terminal>(5, 5);

    }//TuringMachine   
    
    /**
     * Constructor con alfabeto de entrada.
     * 
     * @param name Nombre de la máquina de turing a crear.
     * @param alphabet Alfabeto de entrada de la máquina de turing.
     */
    public TuringMachine (String name, Vector<Terminal> alphabet){
        super(name, alphabet);
        mAlphabetTape=new Vector<Terminal>(5, 5);
        
    }//TuringMachine
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre de la máquina de turing a crear.
     * @param alphabet Alfabeto de entrada de la máquina de turing.
     * @param alphabetTape Alfabeto de cinta de la máquina de turing.
     */
    public TuringMachine (String name, Vector<Terminal> alphabet, Vector<Terminal> alphabetTape){
        super(name, alphabet);
        this.mAlphabetTape=alphabetTape;
        
    }//TuringMachine
    
    /**
     * Calcula el alfabeto del autómata y lo devuelve.
     * 
     * @return Alfabeto del autómata.
     */
    public Vector<Terminal> obtainAlphabet () {
        mAlphabet = new Vector<Terminal>(5, 5);
        
        for(State s : mStates)
            for(Transition tran : s.getTransitionsOut())
                addAlphabetToken(tran.getIn());
        
        return mAlphabet;
    }//obtainAlphabet
    
    /**
     * Calcula el alfabeto de cinta de la máquina de turing y lo devuelve.
     * 
     * @return Alfabeto de cinta de la máquina de turing.
     */
    public Vector<Terminal> obtainAlphabetTape () {
        mAlphabetTape.clear();
        
        for(State s : mStates){
            for(Transition tran : s.getTransitionsOut()){
            	Terminal t=((TransitionTM)tran).getWriteTape();
        		if(t!=null)
        			addAlphabetTape(t);
            }
        }
        return mAlphabetTape;
    }//obtainAlphabetTape
    
    /**
	 * Añade un terminal al alfabeto de cinta.<br>
	 * Si el terminal no se encuentra en el alfabeto lo introduce y si ya está no lo
	 * introduce.<br>
     * Insertará los terminales ordenados según el código ASCII.
	 * 
	 * @param terminal El terminal a añadir. 
	 */
	public void addAlphabetTape (Terminal terminal){
		if (!mAlphabetTape.contains(terminal)){
            for(int i=0; i<mAlphabetTape.size(); i++)
                if(mAlphabetTape.elementAt(i).getToken() > terminal.getToken()){
                    mAlphabetTape.add(i, terminal);
                    return;
                }
            mAlphabetTape.add(terminal);
		}
		
	}//addAlphabetTape
    
    /**
     * Obtiene los datos de la máquina de turing para mostrarlos en una tabla de transición.
     * <br>
     *<br>
     *<table border=2 width=40%>
	 *<tr align="center">
	 *<td>State/Token</td>
	 *<td><</td>
	 *<td>a</td>
	 *<td>b</td>
	 *<td>></td>
	 *<td></td>
	 *</tr>
	 *<tr align="center">
 	 *<td>>>p</td>
	 *<td>q..D;</td>
	 *<td>-</td>
	 *<td>-</td>
	 *<td>-</td>
	 *<td>-</td>
	 *</tr>
	 *<tr align="center">
	 *<td>q</td>
	 *<td>-</td>
	 *<td>q..D</td>
	 *<td>r..D</td>
	 *<td>-</td>
	 *<td>-</td>
	 *</tr>
	 *<tr align="center">
	 *<td>r</td>
	 *<td>-</td>
	 *<td>-</td>
	 *<td>-</td>
	 *<td>s..P</td>
	 *<td>-</td>
	 *</tr>
	 *<tr align="center">
	 *<td>-</td>
	 *<td>-</td>
	 *<td>-</td>
	 *<td>-</td>
	 *<td>-</td>
	 *<td>-</td>
	 *</tr>
	 *</table>
     * @return Array de dos dimensiones con la función de transición del autómata.
     */
    public Object[][] getData () {
        Object[][] data;
        String temp;
        State st;
        Terminal ter;  
        
        obtainAlphabet();
        obtainAlphabetTape();
        
        data = new String[mStates.size()+1][mAlphabet.size()+1];
        data[0][0] = "State \\ Token";
        
            //Estados
        for(int i=1; i<data.length; i++){
        	temp = "";
            if(mInitialState != null && mStates.elementAt(i-1).equals(mInitialState))
                temp = ">>";
            if(mStates.elementAt(i-1).isFinal())
                temp += " ( " + mStates.elementAt(i-1).getName() + " ) ";
            else
                temp += mStates.elementAt(i-1).getName();
            data[i][0] = temp;
        }
        
            //Tokens de entrada
        for(int j=1; j<data[0].length; j++){            
            data[0][j] = this.mAlphabet.elementAt(j-1).toString();
        }
        
            //Transiciones
        for(int j=1; j<data.length; j++){
            st = mStates.elementAt(j-1);
            for(int i=1; i<mAlphabet.size()+1; i++){
                ter = mAlphabet.elementAt(i-1);
                temp = "";
                for(Transition trans : st.getTransitionsOut()){
                	TransitionTM tt=(TransitionTM)trans;                
                    if(tt.getIn().equals(ter)){
                        temp += tt.getNextState();
                        if(tt.getWriteTape()!=null){
                        	Character tape=tt.getWriteTape().getToken();
                        	temp+="."+ tape;
                        }
                        if(tt.getMoveTape()!=null){
                        	Character move=tt.getMoveTape();
                        	temp+= "." + move;
                        }
                        temp+=" / ";
                    }
                }
                if(temp.endsWith(" / "))
                    temp = temp.substring(0, temp.length()-3);
                data[j][i] = temp;
            }
        }
        
        return data;
    }//getData
    
    /**
     * Crea un estado dentro de esta máquina de turing asignándole sólo el nombre.<br>
     * Si el estado no existe lo crea y lo devuelve; si el estado ya existe devuelve la
     * referencia a dicho estado.
     * 
     * @param name Nombre del nuevo estado.
     * @return El nuevo estado.
     */
    public State createState (String name){
        StateTM state = new StateTM (name);
        
        return addState(state);
    }//createState

    /**
     * Devuelve si la máquina de turing es determinista o no.<br>
     * Actualiza el valor de determinismo de la máquina de turing.<br>
     * Una máquina de turing es determinista cuando;<br>
     * Al menos un estado tiene por lo menos dos transiciones con el mismo terminal de lectura de cinta.<br>
     * Guarda los estados no deterministas en un vector. 
     * 
     * @param states Vector donde se guardan los estados no deterministas.
     * @return True si es determinista, falso en caso contrario.
     */
    public boolean isDeterministic (Vector<State> states){
    	
    	if(this.mStates.size()==0){
    		mDeterministic = false;
            return false;
    	}
        Vector<State> temp = getStates();
        Vector<Terminal> countTer = new Vector<Terminal>();
        Vector<Transition> transOut;
        Terminal ter;
        
            //Recorremos los estados
        for(int i=0; i<temp.size(); i++){
            transOut = temp.elementAt(i).getTransitionsOut();
                //Recorremos las transiciones
            for(int j=0; j<transOut.size(); j++){
                ter = transOut.elementAt(j).getIn();
                if(countTer.contains(ter)){
                	states.add(temp.elementAt(i));
                }
                countTer.add(ter);
            }
            countTer.clear();
        }//for
        
        if(states.size()==0){
        	mDeterministic = true;
        	return true;
        }else{
        	mDeterministic = false;
        	return false;
        }
    }//isDeterministic
    
    /**
     * Método de clonación.<br>
     * Se encarga de clonarse a sí mismo; se trata de una clonación en profundidad.
     * 
     *  @return Clon de esta maquina de turing.
     */
    public Automaton clone (){
        TuringMachine automaton = new TuringMachine(mName);
        
            //Clonamos sus estados
        for(int i=0; i<mStates.size(); i++)
            automaton.addState(((StateTM)(mStates.elementAt(i))).clone());
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
     * Muestra una cadena con toda la información de la máquina de turing.<br>
     * En ella se incluyen todos los estados que la forman y las transiciones
     * de estos.
     * 
     * @return Cadena con la información.
     */
    public String toString () {
        Vector<Transition> trans;
        String solution = "";
        
            //Dibujamos el alfabeto de estados
        for(int i=0; i<getStates().size(); i++){
            if(getStates().elementAt(i).equals(getInitialState()))
                solution  += (">>");
            if(getStates().elementAt(i).isFinal())
                solution  += ("("+getStates().elementAt(i).getName().toString()+") ");
            else
                solution  += (getStates().elementAt(i).getName().toString()+"  ");
        }
        solution += "\n";
            //Dibujamos las transiciones
        for(int i=0; i<getStates().size(); i++){
            trans = getStates().elementAt(i).getTransitionsOut();
            for(int j=0; j<trans.size(); j++){
            	TransitionTM tr=(TransitionTM)trans.elementAt(j);
                solution  += (tr.getPrevState().getName()+" - "+
                        tr.getIn().getToken()+" , "+
                        tr.getWriteTape()+" ; "+
                        tr.getMoveTape()+" -> ");
                if(trans.elementAt(j).getNextState().isFinal())
                    solution  += ("("+trans.elementAt(j).getNextState().getName()+")");
                else
                    solution  += (" "+trans.elementAt(j).getNextState().getName());
                solution  += "\n";
            }//for
        }//for
            
        return solution;
    }//toString

    /**
     * Devuelve el tipo de autómata que se esta manejando.
	 * 
	 * @return Tipo de autómata
     */
	public String getAutomatonType() {
		return Automaton.TURING_MACHINE;
	}//getAutomatonType
    
}//TuringMachine
