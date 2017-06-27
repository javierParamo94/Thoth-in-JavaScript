package core.finiteautomaton;

import java.util.Vector;

import core.*;

/**
 * <b>Descripci�n</b><br>
 * Se encarga de proporcionar toda la funcionalidad que debe tener un aut�mata finito.
 * <p>
 * <b>Detalles</b><br>
 * Nos proporciona todas las operaiones que se pueden obtener de la m�quina de
 * estados de un aut�mta finito.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Aut�mata Finito.
 * </p>
 * 
 * @author �lvar Arn�iz Gonz�lez, Andr�s Arn�iz Moreno
 * @version 1.0
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla Saiz
 * @version 2.0
 */
public class FiniteAutomaton extends Automaton implements Cloneable{
    
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param name Nombre del aut�mata finito a crear.
     */
    public FiniteAutomaton (String name) {
        super(name);

    }//FiniteAutomaton    
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre del aut�mata finito a crear.
     * @param alphabet Alfabeto de entrada del aut�mata finito.
     */
    public FiniteAutomaton (String name, Vector<Terminal> alphabet){
        super(name, alphabet);
        
    }//FiniteAutomaton
    
    /**
     * Constructor completo a partir de la tabla de transici�n.
     * 
     * @param table Matriz con los datos de la tabla de transici�n.
     */
    public FiniteAutomaton (String name, Object[][] table){
        super(name);
        buildAutomaton(table);
        
    }//FiniteAutomaton
    
    /**
     * Construye los estados y las transiciones del aut�mata a partir de los
     * datos contenidos en la matriz que se le pasa.<br>
     * Primero crea todos los estados, una vez a�adidos al aut�mata se crean todas
     * las transiciones que existan entre ellos.
     * 
     * @param table Tabla de transici�n
     */
    private void buildAutomaton (Object[][] table) {
        Vector<State> vState;
        State state;
        String name;
        Character c;
            
            //Creamos todos los estados
        for(int i=1; i<table.length; i++){
            name = (String)table[i][2];
            if(!name.equals("")){
                state = createState(name, (Boolean)table[i][1]);
                if((Boolean)table[i][0])
                    setInitialState(state);
            }
        }
            //Creamos las transiciones
        for(int i=1; i<table.length; i++)
            if(!table[i][2].equals("")){
                state = findState((String)table[i][2]);
                for(int j=3; j<table[0].length; j++){
                        //Si no est� vac�a la casilla creamos el terminal
                    if(!((String)table[0][j]).equals("")){
                        c = ((String)table[0][j]).charAt(0);
                        vState = obtainStates((String)table[i][j]);
                            //Creamos las transiciones epsilon
                        if(c == TerminalEpsilon.EPSILON_CHARACTER)
                            for(State st:vState)
                                ((StateFA)state).createTransitionOut(st);
                            //Creamos transiciones con terminales
                        else
                            for(State st:vState)
                            	((StateFA)state).createTransitionOut(new Terminal(c), st);
                    }
                }
            }
        
    }//buildAutomaton
    
    /**
     * Obtiene los estados que aparecen en el string.
     * 
     * @param s Cadena con uno o m�s nombres de estados.
     * @return Estados encontrados en esa cadena.
     */
    private Vector<State> obtainStates (String s) {
        Vector<State> v = new Vector<State>(2, 4);
        String temp = new String();
        
        while(s.length() > 0){
            if(s.charAt(0) != ',')
                    //Concatena en temp el primer caracter de la cadena
                temp = temp.concat(s.substring(0, 1));
            else
                if(!temp.equals("")){
                    //A�ade el estado que ya hab�amos creado o uno nuevo con ese nombre
                    v.add(createState(temp));
                    temp = new String();
                }
            s = s.substring(1);
        }
        if(temp.length() > 0)
            v.add(createState(temp));
        
        return v;
    }//obtainStates
    
    /**
     * Calcula el alfabeto del aut�mata y le devuelve.
     * 
     * @return Alfabeto del aut�mata.
     */
    public Vector<Terminal> obtainAlphabet () {
        mAlphabet = new Vector<Terminal>(5, 5);
        
        for(State s : mStates)
            for(Transition tran : s.getTransitionsOut())
                addAlphabetToken(tran.getIn());
        
        return mAlphabet;
    }//obtainAlphabet
    
    /**
     * Obtiene los datos del aut�mata finito para mostrarlos en una tabla de transici�n.
     * La disposici�n de la tabla es:
     *<br>
     *<br>
     *<table border=2 width=50%>
	 *<tr align="center">
	 *<td>Token/State</td>
	 *<td>>>0</td>
	 *<td>1</td>
	 *<td>(2)</td>
	 *</tr>
	 *<tr align="center">
 	 *<td>a</td>
	 *<td>1,2</td>
	 *<td>2</td>
	 *<td>-</td>
	 *</tr>
	 *<tr align="center">
	 *<td>�</td>
	 *<td>2</td>
	 *<td>-</td>
	 *<td>-</td>
	 *</tr>
	 *</table>
     * @return Array de dos dimensiones con la funci�n de transici�n del aut�mata.
     */
    public Object[][] getData () {
        Object[][] data;
        String temp;
        State st;
        Terminal ter;
        
        obtainAlphabet();
        
        data = new String[mAlphabet.size()+1][mStates.size()+1];
        data[0][0] = "Token \\ State";
            //Tokens
        for(int i=1; i<data.length; i++)
            data[i][0] = mAlphabet.elementAt(i-1).toString();
            //Estados
        for(int j=1; j<data[0].length; j++){
            temp = "";
            if(mInitialState != null && mStates.elementAt(j-1).equals(mInitialState))
                temp = ">>";
            if(mStates.elementAt(j-1).isFinal())
                temp += " ( " + mStates.elementAt(j-1).getName() + " ) ";
            else
                temp += mStates.elementAt(j-1).getName();
            data[0][j] = temp;
        }
            //Transiciones
        for(int j=1; j<data[0].length; j++){
            st = mStates.elementAt(j-1);
            for(int i=1; i<mAlphabet.size()+1; i++){
                ter = mAlphabet.elementAt(i-1);
                temp = "";
                for(Transition trans : st.getTransitionsOut())
                    if(trans.getIn().equals(ter))
                        temp += trans.getNextState() + ",";
                if(temp.endsWith(","))
                    temp = temp.substring(0, temp.length()-1);
                data[i][j] = temp;
            }
        }
        
        return data;
    }//getData
    
    /**
     * Crea un estado dentro de este aut�mata asign�ndole s�lo el nombre.<br>
     * Si el estado no existe lo crea y lo devuelve; si el estado ya existe devuelve la
     * referencia a dicho estado.
     * 
     * @param name Nombre del nuevo estado.
     * @return El nuevo estado.
     */
    public State createState (String name){
        StateFA state = new StateFA (name);
        
        return addState(state);
    }//createState
    
    /**
     * Devuelve el/los siguiente/s estado/s.<br>
     * Busca entre sus transiciones una que coincida con la entrada que se le es
     * pasada y nos devuelve el/los siguiente/s estado/s posibles.
     * Almacena en los estados actuales los nuevos estados que hemos obtenido.<br>
     * Si el aut�mata es determinista devolver� un �nico estado, si por el
     * contrario no lo es, devolvera una vector de siguientes estados.
     * 
     * @param in Terminal de entrada.
     * @return Vector con los posibles siguientes estados.
     */
    public Vector<State> nextStates (Terminal in){
        Vector<State> vState = new Vector<State>();
        Vector<State> temp;
        
        for (int i=0; i<mCurrentStates.size(); i++){
        	StateFA st=(StateFA)mCurrentStates.elementAt(i);
            temp = st.nextStates(in);
            if(!vState.containsAll(temp)){
                vState.removeAll(temp);
                vState.addAll(temp);
            }
        }//for
        
        return vState;
    }//nextStates

    /**
     * Devuelve si el aut�mata es determinista o no.<br>
     * Actualiza el valor de determinismo del aut�mata.<br>
     * Un aut�mata es determinista cuando el terminal epsilon no forma
     * parte de su alfabeto y cuando en una determinado estado consumiendo
     * una entrada determinada existe una sola posibilidad de transici�n.<br>
     * Guarda los estados no deterministas en un vector. 
     * 
     * @param states Vector donde se guardan los estados no deterministas.
     * 
     * @return True si es determinista, falso en caso contrario.
     */
    public boolean isDeterministic (Vector<State> states){
    	
    	if(this.mStates.size()==0){
    		mDeterministic = false;
            return false;
    	}
        Vector<State> temp = getStates();
        Vector<Terminal> count = new Vector<Terminal>();
        Vector<Transition> transOut;
        TerminalEpsilon epsilon = new TerminalEpsilon();
        Terminal ter;
        
            //Recorremos los estados
        for(int i=0; i<temp.size(); i++){
            transOut = temp.elementAt(i).getTransitionsOut();
                //Recorremos las transiciones
            for(int j=0; j<transOut.size(); j++){
                ter = transOut.elementAt(j).getIn();
                    //Comprobamos que no haya transiciones �psil�n o con el mismo terminal
                if(ter.equals(epsilon) || count.contains(ter))                
                    states.add(temp.elementAt(i));
                else
                	count.add(ter);
            }
            count.clear();
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
     * Completa el aut�mata.<br>
     * Crea un estado sumidero y hace que todos los estados del aut�mata tengan
     * transiciones de salida para todos los terminales del alfabeto. Creando 
     * transiciones hacia dicho estado si no las ten�an.<br>
     * Tambi�n completa el nuevo estado creando transiciones a s� mismo.
     * Si el aut�mata ya era completo no hace nada.
     * 
     * @return True si el aut�mata ha cambiado, false si ya era completo.
     */
    public boolean completeAutomaton (){
        Vector<Transition> transOut;
        Vector<Terminal> alphabet = mAlphabet;
        State newState = new StateFA("Drain");
        boolean flag;
        
            //Recorremos todos los estados del aut�mata
        for(int i=0; i<mStates.size(); i++){
                //Almacenamos las transiciones de cada estado
            transOut = mStates.elementAt(i).getTransitionsOut();
                //Para cada elemento del alfabeto -> Nos aseguramos de que haya transici�n
            for(int j=0; j<alphabet.size(); j++){
                flag = true;
                for(int k=0; k<transOut.size() && flag==true; k++)
                    if(transOut.elementAt(k).getIn().equals(alphabet.elementAt(j)))
                        flag = false;
                    //Si no hay transici�n para ese car�cter la creamos
                if(flag)
                    ((StateFA)newState).createTransitionIn(alphabet.elementAt(j), 
                            mStates.elementAt(i));
            }//for
        }//for
        
            //Si hemos creado transiciones al estado sumidero -> Le a�adimos
        if(newState.getTransitionsIn().size()>0){
                //Creamos todas las transiciones del sumidero a s� mismo.
            for(int i=0; i<alphabet.size(); i++)
            	((StateFA)newState).createTransitionIn(alphabet.elementAt(i), newState);
            addState(newState);
            return false;
        }//if
        
        return true;
    }//completeAutomaton
    
    /**
     * M�todo de clonaci�n.<br>
     * Se encarga de clonarse a s� mismo; se trata de una clonaci�n en profundidad.
     * 
     *  @return Clon de �ste aut�mata.
     */
    public FiniteAutomaton clone (){
        FiniteAutomaton automaton = new FiniteAutomaton(mName);
        
            //Clonamos sus estados
        for(int i=0; i<mStates.size(); i++)
            automaton.addState(((StateFA)(mStates.elementAt(i))).clone());
            //Clonamos las transiciones de los estados
        for(int i=0; i<mStates.size(); i++)
        	((StateFA)automaton.getStates().elementAt(i)).cloneTransitions(mStates.elementAt(i), automaton.getStates());
            //Asignamos el estado inicial
        if(mInitialState != null)
            automaton.setInitialState(automaton.findState(getInitialState().getName()));
            //Actualizamos el alfabeto del clon
        for(int i=0; i<mAlphabet.size(); i++)
            automaton.addAlphabetToken(mAlphabet.elementAt(i));
        
        return automaton;
    }//clone
    
    /**
     * Muestra una cadena con toda la informaci�n del aut�mata.<br>
     * En ella se incluyen todos los estados que la forman y las transiciones
     * de estos.
     * 
     * @return Cadena con la informaci�n.
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
                solution  += (trans.elementAt(j).getPrevState().getName()+" - "+
                        trans.elementAt(j).getIn().getToken()+" -> ");
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
     * Devuelve el tipo de aut�mata que se esta manejando.
	 * 
	 * @return Tipo de aut�mata
     */
	public String getAutomatonType() {
		return Automaton.FINITE_AUTOMATON;
	}//getAutomatonType
    
}//FiniteAutomaton
