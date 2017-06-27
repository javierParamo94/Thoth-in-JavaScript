package core.pushdownautomaton;

import java.util.Vector;

import core.*;


/**
 * <b>Descripci�n</b><br>
 * Se encarga de proporcionar toda la funcionalidad que debe tener un aut�mata de pila.
 * <p>
 * <b>Detalles</b><br>
 * Nos proporciona todas las operaiones que se pueden obtener de la m�quina de
 * estados de un aut�mata de pila.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Implementa el tipo de datos Aut�mata de Pila.
 * </p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public class PushDownAutomaton extends Automaton implements Cloneable{
    
	// Attributes ------------------------------------------------------------------
    		
	/**
	 * Alfabeto de pila compuesto por cadenas.
	 */
	protected Vector<String> mAlphabetPush;
	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor b�sico.
     * 
     * @param name Nombre del aut�mata de pila a crear.
     */
    public PushDownAutomaton (String name) {
        super(name);

    }//FiniteAutomaton    
    
    /**
     * Constructor con alfabeto de entrada.
     * 
     * @param name Nombre del aut�mata de pila a crear.
     * @param alphabet Alfabeto de entrada del aut�mata de pila.
     */
    public PushDownAutomaton (String name, Vector<Terminal> alphabet){
        super(name, alphabet);
        
    }//FiniteAutomaton
    
    /**
     * Constructor completo.
     * 
     * @param name Nombre del aut�mata de pila a crear.
     * @param alphabet Alfabeto de entrada del aut�mata de pila.
     * @param alphabetPush Alfabeto de pila del aut�mata de pila.
     */
    public PushDownAutomaton (String name, Vector<Terminal> alphabet, Vector<String> alphabetPush){
        super(name, alphabet);
        this.mAlphabetPush=alphabetPush;
        
    }//FiniteAutomaton
    
    /**
     * Calcula el alfabeto del aut�mata y lo devuelve.
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
     * Calcula el alfabeto de pila del aut�mata y lo devuelve.
     * 
     * @return Alfabeto de pila del aut�mata.
     */
    public Vector<String> obtainAlphabetPush () {
        mAlphabetPush = new Vector<String>(5, 5);
        
        for(State s : mStates)
            for(Transition tran : s.getTransitionsOut())
                addAlphabetPush(((TransitionPDA)tran).getInPush());
        
        return mAlphabetPush;
    }//obtainAlphabet
    
    /**
	 * A�ade una cadena al alfabeto de pila.<br>
	 * Si la cadena no se encuentra en el alfabeto de pila la introduce.
     * Insertar� las cadenas ordenadas seg�n el c�digo ASCII.
	 * 
	 * @param string Cadena a a�adir. 
	 */
	private void addAlphabetPush (String string){
		if (!mAlphabetPush.contains(string)){
            for(int i=0; i<mAlphabetPush.size(); i++)
                if(mAlphabetPush.elementAt(i).compareTo(string)>0){
                    mAlphabetPush.add(i, string);
                    return;
                }
            mAlphabetPush.add(string);
		}
		
	}//addAlphabetString
	
	/**
	 * A�ade una cadena al alfabeto de pila.<br>
	 * Si la cadena no se encuentra en el alfabeto de pila la introduce.
     * Insertar� las cadenas ordenadas seg�n el c�digo ASCII.
	 * 
	 * @param string Cadena a a�adir. 
	 */
	private Object[][] obtainInAndPush() {
		//Inicializar la matriz con todas las posibilidades de combinaci�n
		// de los terminales de entrada y las cadenas de lectura de pila.
				
		if(this.mAlphabet.size()>0 && this.mAlphabetPush.size()>0){
			int row=this.mAlphabet.size()*this.mAlphabetPush.size(); 
			Object[][] and=new Object[row][3];
			int i=0;
			for(Terminal t : mAlphabet){
				for(String s: mAlphabetPush){				
					and[i][0]=t;
					and[i][1]=s;
					i++;
				}
			}
		
			//Se�alar las combinaciones que est�n presentes en al menos alguna transici�n.
			for(State s : mStates){
				for(Transition tran : s.getTransitionsOut()){
					for(i=0; i<and.length;i++){
						if(((TransitionPDA)tran).getIn().equals(and[i][0])){
							if(((TransitionPDA)tran).getInPush().equals(and[i][1])){
								and[i][2]="y";
							}else{
								and[i][2]="";
							}
						}else{
							i+=mAlphabetPush.size()-1;
						}
					}
				}
			}
			
			//Inicializar y rellenar la matriz de los pares de terminales y 
			//las cadenas de lectura de pila que est�n en el automata.
			int cont=0;
			for(i=0;i<and.length;i++){
				if(and[i][2].equals("y"))
					cont++;
			}
			Object[][] inAndPush=new Object[cont][2];
			int j=0;
			for(i=0;i<and.length;i++){
				if(and[i][2].equals("y")){
					inAndPush[j][0]=and[i][0];
					inAndPush[j][1]=and[i][1];
					j++;
				}
			}			
			return inAndPush;
		}
		return new Object[0][0];
		
	}//obtainInAndPush
    
    /**
     * Obtiene los datos del aut�mata de pila para mostrarlos en una tabla de transici�n.
     * Obtiene los datos del aut�mata finito para mostrarlos en una tabla de transici�n.
     * La disposici�n de la tabla es:
     *<br>
     *<br>
     *<table border=2 width=40%>
	 *<tr align="center">
	 *<td>Token,Stack/State</td>
	 *<td>>>0</td>
	 *<td>1</td>
	 *<td>(2)</td>
	 *</tr>
	 *<tr align="center">
 	 *<td>a,Z</td>
	 *<td>A;1<br>�;2</td>
	 *<td>-</td>
	 *<td>-</td>
	 *</tr>
	 *<tr align="center">
	 *<td>a,A</td>
	 *<td>-</td>
	 *<td>A;1</td>
	 *<td>�;2</td>
	 *</tr>
	 *<tr align="center">
	 *<td>�,Z</td>
	 *<td>�;2</td>
	 *<td>-</td>
	 *<td>-</td>
	 *</tr>
	 *</table>
     * @return Array de dos dimensiones con la funci�n de transici�n del aut�mata.
     */
    public Object[][] getData () {
        Object[][] data;
        Object[][] inAndPush;
        String temp;
        State st;
        Terminal ter;
        String str;
        
        obtainAlphabet();
        obtainAlphabetPush();
        inAndPush=obtainInAndPush();
        
        int tam=mStates.size();
        data = new String[inAndPush.length+1][tam+1];
    	data[0][0] = "Token,Stack \\ State";
        
    	//Estados
        for(int j=1; j<=tam; j++){
            temp = "";
            if(mInitialState != null && mStates.elementAt(j-1).equals(mInitialState))
                temp = ">>";
            if(mStates.elementAt(j-1).isFinal())
                temp += " ( " + mStates.elementAt(j-1).getName() + " ) ";
            else
                temp += mStates.elementAt(j-1).getName();
            data[0][j] = temp;
        }
        
        if(inAndPush != null){
                //Tokens y lecturas de pila
            for(int i=1; i<data.length; i++){
            	String s=inAndPush[i-1][0].toString()+","+inAndPush[i-1][1];
                data[i][0] = s;
            }
                
            
                //Transiciones
            for(int k=1; k<tam+1; k++){
                st = mStates.elementAt(k-1);
                for(int i=1; i<=inAndPush.length; i++){
                    ter = (Terminal)inAndPush[i-1][0];
                	str = (String)inAndPush[i-1][1];
                    temp = "";
                    for(Transition trans : st.getTransitionsOut()){                    	
                        if(trans.getIn().equals(ter) && ((TransitionPDA)trans).getInPush().equals(str))
                            temp += trans.getNextState() + ","+((TransitionPDA)trans).getOutPush()+"  /  ";                        	
                    }
                    if(temp.endsWith("  /  "))
                        temp = temp.substring(0, temp.length()-5);
                    data[i][k] = temp;
                    
                }
            }
            return data;

        }else{
            return data;
        }

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
        StatePDA state = new StatePDA (name);
        
        return addState(state);
    }//createState
    
    /**
     * Devuelve si el aut�mata es determinista o no.<br>
     * Actualiza el valor de determinismo del aut�mata.<br>
     * Un aut�mata es determinista cuando:<br>
     * 1. Tiene una transici�n con el terminal epsilon, la cadena de lectura de pila epsilon y la cadena de escritura en pila epsilon.<br>
     * 2. Tiene una transici�n con el terminal epsilon y la cadena de lectura de pila epsilon y ese estado tiene m�s transiciones.<br>
     * 3. Dos transiciones del mismo estado tienen el mismo terminal y la misma lectura de pila o el terminal epsilon.<br>
     * 4. Dos transiciones tienen la misma cadena de lectura de pila y al menos una de las transiciones tiene el terminal epsilon.<br>
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
        Vector<String> countStr = new Vector<String>();
        Vector<Transition> transOut;
        TerminalEpsilon epsilon = new TerminalEpsilon();
        Terminal ter;
        String str, str2;
        
            //Recorremos los estados
        for(int i=0; i<temp.size(); i++){
            transOut = temp.elementAt(i).getTransitionsOut();
                //Recorremos las transiciones
            for(int j=0; j<transOut.size(); j++){
                ter = transOut.elementAt(j).getIn();
                str =((TransitionPDA)transOut.elementAt(j)).getInPush();
                str2 =((TransitionPDA)transOut.elementAt(j)).getOutPush();
                if(ter.equals(epsilon)&& str.equals(epsilon.toString())){//si el terminal y la cadena son epsilon
                	if(transOut.size()>1 || str2.equals(epsilon.toString())){ //caso 2 y caso 1
                		states.add(temp.elementAt(i));
                	}
                }else{
                	if(countTer.contains(ter)){
                		int k=countTer.indexOf(ter);
                		if(countStr.get(k).equals(str) || countStr.get(k).equals(epsilon.toString())){//si el terminal ya est� dentro y la cadena es epsilon o la misma que otra transici�n
                			states.add(temp.elementAt(i));
                		}
                	}
                	if(countTer.contains(epsilon) || ter.equals(epsilon)){
                		if(countStr.contains(str) || countTer.contains(epsilon)){
                			states.add(temp.elementAt(i));
                		}
                	}
                }
                countTer.add(ter);
                countStr.add(str);
            }
            countTer.clear();
            countStr.clear();
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
     * M�todo de clonaci�n.<br>
     * Se encarga de clonarse a s� mismo; se trata de una clonaci�n en profundidad.
     * 
     *  @return Clon de �ste aut�mata.
     */
    public Automaton clone (){
        PushDownAutomaton automaton = new PushDownAutomaton(mName);
        
            //Clonamos sus estados
        for(int i=0; i<mStates.size(); i++)
            automaton.addState(((StatePDA)(mStates.elementAt(i))).clone());
            //Clonamos las transiciones de los estados
        for(int i=0; i<mStates.size(); i++)
            automaton.getStates().elementAt(i).cloneTransitions(mStates.elementAt(i), automaton.getStates());
            //Asignamos el estado inicial
        if(mInitialState != null)
            automaton.setInitialState(automaton.findState(getInitialState().getName()));
            //Actualizamos el alfabeto del clon
        for(int i=0; i<mAlphabet.size(); i++){
            automaton.addAlphabetToken(mAlphabet.elementAt(i));
            automaton.addAlphabetPush(mAlphabetPush.elementAt(i));
        }
        
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
            	TransitionPDA tr=(TransitionPDA)trans.elementAt(j);
                solution  += (tr.getPrevState().getName()+" - "+
                        tr.getIn().getToken()+" , "+
                        tr.getInPush()+" ; "+
                        tr.getOutPush()+" -> ");
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
		return Automaton.PUSH_DOWN_AUTOMATON;
	}//getAutomatonType
    
}//PushDownAutomaton
