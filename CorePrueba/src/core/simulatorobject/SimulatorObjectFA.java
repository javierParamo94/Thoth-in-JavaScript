package core.simulatorobject;

import java.util.ArrayList;

import core.*;

/**
 * <b>Descripción</b><br>
 * Contiene los datos que se van a proporcionar para simular la validación de una palabra en un autómata finito.
 * <p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class SimulatorObjectFA extends SimulatorObject implements Cloneable{
   	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor.
     * 
     * @param oState Estado.
     * @param oWordRead Cadena que se lleva leída.
     * @param oPrev Objeto de simulación que le precede.
     */
    public SimulatorObjectFA (State oState, ArrayList<Character> oWordRead, SimulatorObject oPrev, String oWord){
    	super(oState,oWordRead,oPrev, oWord);
    }//SimulatorObjectFA
        
    /**
     * Clona el objeto de simulación y lo devuelve.
     * 
     * @return Objeto de simulación clonado.
     */
    public SimulatorObject clone (){
    	ArrayList<Character> readWord;
    	if(wordRead!=null)
    		readWord=(ArrayList<Character>)wordRead.clone();
    	else
    		readWord=null;
    	SimulatorObject so=new SimulatorObjectFA(state,readWord,prev, word);
    	if(this.valid)
    		so.validTrue();
    	if(this.wrong)
    		so.wrongTrue();
    	return so;
    }//clone
    
}//SimulatorObjectFA

    