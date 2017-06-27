package core.simulatorobject;

import java.util.Vector;

import core.*;

/**
 * <b>Descripci�n</b><br>
 * Contiene los datos que se van a proporcionar para simular la validaci�n de una palabra en una m�quina de Turing multipista.
 * <p>
 * 
 * @author Javier Jimeno Visitaci�n, I�igo Mediavilla S�iz
 * @version 2.0
 */
public class SimulatorObjectMTRTM extends SimulatorObjectTM implements Cloneable{
   	
	//Attributes ------------------------------------------------------------------
    
    /**
     * Estado de las cintas.
     */
    protected Vector<Vector<Terminal>> mTRTape;
	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor.
     * 
     * @param oState Estado.
     * @param oPrev Objeto de simulaci�n que le precede.
     * @param oTRTape Estado de las cintas.
     * @param oHead Posici�n del cabezal.
     */
	public SimulatorObjectMTRTM (State oState, SimulatorObject oPrev,Vector<Vector<Terminal>> oMTRTape,int oHead, String oWord){
    	super(oState,oPrev,null,oHead, oWord);
    	this.mTRTape=oMTRTape;
    }//SimulatorObjectMTRTM
    
    /**
     * M�todo de acceso al estado de las cintas.
     * 
     * @return El estado de las cintas.
     */
    public Vector<Vector<Terminal>> getMTRTape () {
    	return this.mTRTape;
    }//getMTRTape
    
    /**
     * M�todo que modifica el estado de las cintas.
     * 
     * @param tp Nuevo estado de las cintas.
     */
    public void setMTRTape (Vector<Vector<Terminal>> tp) {
    	this.mTRTape=tp;
    }//setMTRTape
    
    /**
     * Clona el objeto de simulaci�n y lo devuelve.
     * 
     * @return Objeto de simulaci�n clonado.
     */
    public SimulatorObject clone (){
    	Vector<Vector<Terminal>> tp=(Vector<Vector<Terminal>>)mTRTape.clone();    	
    	SimulatorObject so=new SimulatorObjectMTRTM(state,prev,tp,head,word);
    	if(this.valid)
    		so.validTrue();
    	if(this.wrong)
    		so.wrongTrue();
    	return so;
    }//clone
    
}//SimulatorObjectMTRTM

    