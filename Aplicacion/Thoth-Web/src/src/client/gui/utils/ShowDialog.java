package src.client.gui.utils;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;


/**
 * <b>Descripción</b><br>
 * Almacena todos los diálogos de error, advertencia e información de la aplicación.
 * <p>
 * <b>Detalles</b><br>
 * Cada uno de sus métodos estáticos muestra un mensaje específico.<br>
 * También ofrece la disponibilidad de mostrar mensajes que se le pasen al método.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Encapsula todos los mensajes de error en una única clase.
 * </p>
 * 
 * @author Álvar Arnáiz González, Andrés Arnáiz Moreno
 * @version 1.0
 * 
 * @author Javier Jimeno Visitación, Iñigo Mediavilla Saiz
 * @version 2.0
 * 
 * @author Francisco Javier Páramo Arnaiz
 * @version 3.0
 */
public class ShowDialog {
    
	private static MessageMessages sms = GWT.create(MessageMessages.class);
	
    // Methods -------------------------------------------------------------------
    
    /**
     * Lanza mensajes de error que indica que el tipo de la gramática es incorrecto
     * para el algoritmo seleccionado.
     */
    public static void incorrectTypeGrammar () {
        Window.alert("La gramática es incorrecta");
    }//incorrectTypeGrammar
    
    /**
     * Lanza un mensaje de advertencia si la gramática no necesita ejecutar ese algoritmo
     */
    public static void innecesaryAlgorithm () {
    	Window.alert(sms.innecesaryalgorithm());
    	
    }//innecesaryAlgorithm
    
    /**
     * Lanza un mensaje de advertencia si la gramática no ha podido ser limpiada.
     */
    public static void cleanError () {
    	Window.alert(sms.cleanerrora()+"\n"+sms.cleanerrorb());

    }//cleanError
    
    /**
     * Muestra un mensaje de error.
     * 
     * @param message Mensaje que se va a mostrar.
     */
    public static void messageError (String message) {
    	Window.alert(message);
    }//messageError
       
    /**
     * Muestra un mensaje de error cuando no se ha podido ejecutar el algoritmo
     * de chomsky.
     */
    public static void chomskyError () {
    	Window.alert(sms.chomskyerror());
    }//chomskyError
    
    /**
     * Muestra un mensaje de error por pantalla indicando que no hay recursividad
     * directa.
     */
    public static void nonRecursiveDir () {
    	Window.alert(sms.nodirectrecursive());
    }//nonRecursiveDir
    
    /**
     * Muestra un mensaje de error por pantalla indicando que no hay recursividad
     * indirecta
     */
    public static void nonRecursiveIndir () {
    	Window.alert(sms.noindirectrecursive());

    }//nonRecursiveIndir
    /**
     * Muestra un mensaje de error por pantalla cuando la gramática no tiene
     * recursión a izquierdas.
     */
    public static void nonRecursive () {
    	Window.alert(sms.norecursive());
    }//nonRecursive
    
    /**
     * Muestra un mensaje de error por pantalla indicando que la gramática no se puede
     * factorizar.
     */
    public static void nonFactorSymbols () {
    	Window.alert(sms.nofactor());
    }//nonFactorSymbols
    
    /**
     * Muestra un mensaje de error por pantalla indicando que no hay
     * producciones generativas.
     */
    public static void nonUniSymbols () {
    	Window.alert(sms.allpng());
    }//nonUniSymbols
    
    /**
     * Muestra un mensaje de error por pantalla indicando que no hay
     * producciones épsilon.
     */
    public static void nonCancelSymbols () {
    	Window.alert(sms.allsa());
    }//nonCancelSymbols
    
    /**
     * Muestra un mensaje de error por pantalla indicando que no hay  símbolos
     * alcanzables.
     */
    public static void nonReachableSymbols () {
    	Window.alert(sms.allsna());
    }//nonReachableSymbols
    
    /**
     * Muestra un mensaje de error por pantalla indicando que no hay  símbolos
     * terminables.
     */
    public static void nonTerminalSymbols () {
    	Window.alert(sms.allsnt());
    }//nonTerminalSymbols
        
    /**
     * Muestra un error producido en la ejecución del first o el follow.
     */
    public static void nonFirstFollow () {
    	Window.alert(sms.firstempty());
    }//nonFirstFollow
    
    /**
     * Muestra un error cuando la gramática es ambigua.
     */
    public static void grammarAmbiguous () {
    	Window.alert(sms.ambiguousgrammar());
    }//nonFirstFollow*/
    
    /**
     * Muestra la información de que ha sido reconocida la palabra.
     */
    public static void recognizedWord () {
    	Window.alert(sms.recognizedword());
    }//recognizedWord*/
    
    /**
     * Muestra la información de que no ha sido reconocida la palabra.
     */
    public static void nonRecognizedWord () {
    	Window.alert(sms.nonrecognizedword());

    }//nonRecognizedWord*/
    
    /**
     * Muestra un diálogo de error causado por no haber introducido un símbolo
     * válido para buscar y reemplazar en una gramática.
     */
    public static void nonSymbolRename () {
        Window.alert(sms.renameerror());
    }//nonSymbolRename
    
    /**
     * Muestra un diálogo informando que el buscar y reemplazar no ha provocado
     * cambios en la gramática.
     */
    public static void nonChangesRename () {
        Window.alert(sms.changeerror());
    }//nonChangesRename
    
    /**
     * Muestra un diálogo advirtiendo que el terminal introducido no es válido.<br>
     * Este error es debido a que el terminal escogido esta reservado para otras
     * funciones.
     */
    public static void invalidTerminal () {
    	Window.alert(sms.invalidterminal());
    }//invalidTerminal*/
    
    
}//ShowDialog
