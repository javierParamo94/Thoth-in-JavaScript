package core.simulatorobject;

import java.util.ArrayList;

import core.*;

/**
 * <b>Descripci�n</b><br>
 * Contiene los datos que se van a proporcionar para simular la validaci�n de una palabra en un aut�mata finito.
 * <p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public class SimulatorObjectFA extends SimulatorObject implements Cloneable{
   	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor.
     * 
     * @param oState Estado.
     * @param oWordRead Cadena que se lleva le�da.
     * @param oPrev Objeto de simulaci�n que le precede.
     */
    public SimulatorObjectFA (State oState, ArrayList<Character> oWordRead, SimulatorObject oPrev, String oWord){
    	super(oState,oWordRead,oPrev, oWord);
    }//SimulatorObjectFA
        
    /**
     * Clona el objeto de simulaci�n y lo devuelve.
     * 
     * @return Objeto de simulaci�n clonado.
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

    