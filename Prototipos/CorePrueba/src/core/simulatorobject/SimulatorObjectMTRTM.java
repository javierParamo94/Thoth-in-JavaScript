package core.simulatorobject;

import java.util.Vector;

import core.*;

/**
 * <b>Descripción</b><br>
 * Contiene los datos que se van a proporcionar para simular la validación de una palabra en una máquina de Turing multipista.
 * <p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
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
     * @param oPrev Objeto de simulación que le precede.
     * @param oTRTape Estado de las cintas.
     * @param oHead Posición del cabezal.
     */
	public SimulatorObjectMTRTM (State oState, SimulatorObject oPrev,Vector<Vector<Terminal>> oMTRTape,int oHead, String oWord){
    	super(oState,oPrev,null,oHead, oWord);
    	this.mTRTape=oMTRTape;
    }//SimulatorObjectMTRTM
    
    /**
     * Método de acceso al estado de las cintas.
     * 
     * @return El estado de las cintas.
     */
    public Vector<Vector<Terminal>> getMTRTape () {
    	return this.mTRTape;
    }//getMTRTape
    
    /**
     * Método que modifica el estado de las cintas.
     * 
     * @param tp Nuevo estado de las cintas.
     */
    public void setMTRTape (Vector<Vector<Terminal>> tp) {
    	this.mTRTape=tp;
    }//setMTRTape
    
    /**
     * Clona el objeto de simulación y lo devuelve.
     * 
     * @return Objeto de simulación clonado.
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

    