package core.simulatorobject;

import java.util.ArrayList;

import core.*;

/**
 * <b>Descripci�n</b><br>
 * Contiene los datos que se van a proporcionar para simular la validaci�n de una palabra en una m�quina de turing multicabeza.
 * <p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public class SimulatorObjectMHTM extends SimulatorObjectTM{
   	
	//Attributes ------------------------------------------------------------------
        
    /**
     * Posiciones de los cabezales.
     */
    protected int[] mHeads;
	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo.
     * 
     * @param oState Estado.
     * @param oPrev Objeto de simulaci�n que le precede.
     * @param oTRTape Estado de las cintas.
     * @param oHead Posiciones de los cabezales.
     */
	public SimulatorObjectMHTM (State oState, SimulatorObject oPrev,ArrayList<Terminal> oTape,int[] oHeads, String oWord){
    	super(oState,oPrev,oTape,0,oWord);
    	this.mHeads=oHeads;
    	
    }//SimulatorObjectMHTM    
    
    /**
     * M�todo de acceso a las posiciones de los cabezales.
     * 
     * @return Las posiciones de los cabezales.
     */
    public int[] getHeads () {
    	return this.mHeads;
    }//getHeads
    
    /**
     * M�todo que modifica las posiciones de los cabezales.
     * 
     * @param heads Nuevas posiciones de los cabezales.
     */
    public void setHeads (int[] heads) {
    	this.mHeads=heads;
    }//setHeads
    
    /**
     * Clona el objeto de simulaci�n y lo devuelve.
     * 
     * @return Objeto de simulaci�n clonado.
     */
    public SimulatorObject clone (){
    	ArrayList<Terminal> tp=(ArrayList<Terminal>)tape.clone();    	
    	SimulatorObject so=new SimulatorObjectMHTM(state,prev,tp,mHeads,word);
    	if(this.valid)
    		so.validTrue();
    	if(this.wrong)
    		so.wrongTrue();
    	return so;
    }//clone
    
}//SimulatorObjectMHTM

    