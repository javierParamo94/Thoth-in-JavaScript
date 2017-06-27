package core.simulatorobject;

import java.util.Vector;

import core.*;

/**
 * <b>Descripción</b><br>
 * Contiene los datos que se van a proporcionar para simular la validación de una palabra en una máquina de turing multicinta.
 * <p>
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Sáiz
 * @version 2.0
 */
public class SimulatorObjectMTTM extends SimulatorObjectTM{
   	
	//Attributes ------------------------------------------------------------------
    
    /**
     * Estado de las cintas.
     */
    protected Vector<Vector<Terminal>> mTTape;
    
    /**
     * Posiciones de los cabezales.
     */
    protected int[] mHeads;
	
    //  Methods ---------------------------------------------------------------------
    
    /**
     * Constructor completo.
     * 
     * @param oState Estado.
     * @param oPrev Objeto de simulación que le precede.
     * @param oTRTape Estado de las cintas.
     * @param oHead Posiciones de los cabezales.
     */
	public SimulatorObjectMTTM (State oState, SimulatorObject oPrev,Vector<Vector<Terminal>> oMTRTape,int[] oHeads, String oWord){
    	super(oState,oPrev,null,0,oWord);
    	this.mTTape=oMTRTape;
    	this.mHeads=oHeads;
    	
    }//SimulatorObjectMTTM
    
    /**
     * Método de acceso al estado de las cintas.
     * 
     * @return El estado de las cintas.
     */
    public Vector<Vector<Terminal>> getMTTape () {
    	return this.mTTape;    	
    }//getMTTape
    
    /**
     * Método que modifica el estado de las cintas.
     * 
     * @param tp Nuevo estado de las cintas.
     */
    public void setMTTape (Vector<Vector<Terminal>> tp) {
    	this.mTTape=tp;    	
    }//setMTTape
    
    /**
     * Método de acceso a las posiciones de los cabezales.
     * 
     * @return Las posiciones de los cabezales.
     */
    public int[] getHeads () {
    	return this.mHeads;
    }//getHeads
    
    /**
     * Método que modifica las posiciones de los cabezales.
     * 
     * @param heads Nuevas posiciones de los cabezales.
     */
    public void setHeads (int[] heads) {
    	this.mHeads=heads;
    }//setHeads
    
    /**
     * Clona el objeto de simulación y lo devuelve.
     * 
     * @return Objeto de simulación clonado.
     */
    public SimulatorObject clone (){
    	Vector<Vector<Terminal>> tp=(Vector<Vector<Terminal>>)mTTape.clone();    	
    	SimulatorObject so=new SimulatorObjectMTTM(state,prev,tp,mHeads,word);
    	if(this.valid)
    		so.validTrue();
    	if(this.wrong)
    		so.wrongTrue();
    	return so;
    }//clone
    
}//SimulatorObjectMTTM

    