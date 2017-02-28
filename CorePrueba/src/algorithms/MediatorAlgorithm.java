package algorithms;

public interface MediatorAlgorithm {

	 /**
     * Realiza cada paso del algoritmo
     * 
     * @return True si puede seguir ejecutando el algoritmo y false en caso contrario.
     */
    public boolean next ();
    
    /**
     * Ejecuta todos los pasos del algoritmo.
     */
    public void all ();
    
    /**
     * Acepta la expresión regular y se la asigna al panel de autómata que
     * tengamos seleccionado.
     */
    public void accept ();
    
    /**
     * Desreferencia el mediador del visual y cierra la pantalla visual.
     */
    public void exit ();

}