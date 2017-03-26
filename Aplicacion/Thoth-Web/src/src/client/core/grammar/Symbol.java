package src.client.core.grammar;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * <b>Descripción</b><br>
 * Se encarga de almacenar los símbolos usados por el core.
 * <p>
 * <b>Detalles</b><br>
 * Almacena un símbolo y proporciona métodos de acceso de utilidad.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Encapsula los símbolos y normaliza su acceso.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 */
public abstract class Symbol implements IsSerializable {
    
    // Methods ---------------------------------------------------------------------
    
    /**
     * Compara si un símbolo es igual a otro en función del token.
     * 
     * @param symbol Símbolo con el que va a ser comparado.
     * @return True si son iguales y false en caso contrario.
     */
    public abstract boolean equals (Object symbol);
    
    /**
     * Devuelve un String con el símbolo.
     * 
     * @return String con el símbolo.
     */
    public abstract String toString ();
    
}//Symbol

