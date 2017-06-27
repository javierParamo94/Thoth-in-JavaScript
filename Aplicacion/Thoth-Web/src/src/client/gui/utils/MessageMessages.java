package src.client.gui.utils;

import com.google.gwt.i18n.client.Constants;

/**
 * <b>Descripción</b><br>
 * Todos los textos internacionalizables.
 * <p>
 * <b>Detalles</b><br>
 * Es obligatorio poner un valor por defecto de cada mensaje.<br>
 * En este caso los mensajes esta por defecto en español.
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Por medio de una funcion determina el mensaje a mostrar.
 * </p>
 * 
 * 
 * @author Francisco Javier Páramo Arnaiz
 * @version 1.0
 */
public interface MessageMessages extends Constants {
	// A
	@DefaultStringValue("Acerca de...")
	String about();

	@DefaultStringValue("Aceptar")
	String accept();

	@DefaultStringValue("La gramática no tiene producciones unitarias o no generativas.")
	String allpng();

	@DefaultStringValue("La gramática no tiene símbolos no alcanzables.")
	String allsna();

	@DefaultStringValue("La gramática no tiene símbolos terminables.")
	String allsnt();

	@DefaultStringValue("Algoritmos")
	String algorithms();

	@DefaultStringValue("La gramática no tiene producciones épsilon.")
	String allsa();

	@DefaultStringValue("Todos los pasos")
	String allsteps();

	@DefaultStringValue("La gramática es ambigua")
	String ambiguousgrammar();

	@DefaultStringValue("en la línea")
	String atline();

	@DefaultStringValue("Autómata")
	String automaton();

	@DefaultStringValue("Axioma:")
	String axiom();

	@DefaultStringValue("El axioma no tiene ninguna producción.")
	String axiomerror();

	// B
	@DefaultStringValue("por:")
	String by();

	// C
	@DefaultStringValue("Cancelar")
	String cancel();

	@DefaultStringValue("Cálculo First y Follow")
	String calculateff();

	@DefaultStringValue("Símbolos anulables:")
	String cancelsymbols();

	@DefaultStringValue("No se han detectado cambios en la gramática.")
	String changeerror();

	@DefaultStringValue("Carácter")
	String chara();

	@DefaultStringValue("No se ha podido convertir a FNC.")
	String chomskyerror();

	@DefaultStringValue("Convertir a Forma Normal de Chomsky")
	String chomskyalgorithm();

	@DefaultStringValue("No se han podido ejecutar todos los algoritmos de limpieza.")
	String cleanerrora();

	@DefaultStringValue("Por favor ejecutelos por separado.")
	String cleanerrorb();

	@DefaultStringValue("Limpiar gramática")
	String clear();

	// D
	@DefaultStringValue("Dependiente del contexto")
	String dependent();

	@DefaultStringValue("Eliminación de Recursividad Directa")
	String directrecursive();

	// E
	@DefaultStringValue("Editar")
	String edit();

	@DefaultStringValue("Eliminar símbolos no terminables")
	String eliminatesnt();

	@DefaultStringValue("Eliminar símbolos no alcanzables")
	String eliminatesna();

	@DefaultStringValue("Eliminar símbolos anulables")
	String eliminatesa();

	@DefaultStringValue("Eliminar recursividad")
	String eliminaterecursion();

	@DefaultStringValue("Eliminar producciones no generativas")
	String eliminatepng();

	@DefaultStringValue("Eliminar recursividad directa")
	String eliminatedirectrecursion();

	@DefaultStringValue("Eliminar recursividad indirecta")
	String eliminateindirectrecursion();

	@DefaultStringValue("Salir")
	String exit();

	// F
	@DefaultStringValue("Factorizar por la izquierda")
	String factoring();

	@DefaultStringValue("Algoritmo de factorización")
	String factoralgorithm();

	@DefaultStringValue("Prefijo:")
	String factorsymbols();

	@DefaultStringValue("Sustituir:")
	String find();

	@DefaultStringValue("El first calculado está vacío.")
	String firstempty();

	@DefaultStringValue("Archivo")
	String file();

	@DefaultStringValue("Forma normal de Chomsky")
	String fnchomsky();

	// G
	@DefaultStringValue("Gramática")
	String grammar();

	@DefaultStringValue("Definición de Gramática")
	String grammardef();

	@DefaultStringValue("Propiedades de la gramática:")
	String grammarproperties();

	@DefaultStringValue("Tipo de Gramática:")
	String grammartype();

	// H
	@DefaultStringValue("Ayuda")
	String help();

	// I
	@DefaultStringValue("Eliminación de Recursividad Indirecta")
	String indirectrecursive();

	@DefaultStringValue("No es necesario ejecutar este algoritmo.")
	String innecesaryalgorithm();

	@DefaultStringValue("Terminal incorrecto.")
	String invalidterminal();

	@DefaultStringValue("Independiente del contexto")
	String independent();

	@DefaultStringValue("Entrada")
	String in();

	// L
	@DefaultStringValue("Idioma")
	String language();

	// N
	@DefaultStringValue("No terminales:")
	String nonterminals();

	@DefaultStringValue("La gramática no tiene recursividad a izquierdas.")
	String norecursive();

	@DefaultStringValue("La gramática no tiene recursividad directa.")
	String nodirectrecursive();

	@DefaultStringValue("La gramática no tiene recursividad indirecta.")
	String noindirectrecursive();

	@DefaultStringValue("La gramática no se puede factorizar.")
	String nofactor();

	@DefaultStringValue("Siguiente paso")
	String nextstep();

	@DefaultStringValue("Palabra NO reconocida.")
	String nonrecognizedword();

	@DefaultStringValue("No")
	String no();

	@DefaultStringValue("Gramática resultante:")
	String newgrammar();

	// O
	@DefaultStringValue("Salida")
	String out();

	@DefaultStringValue("Antigua gramática:")
	String oldgrammar();

	// P
	@DefaultStringValue("Número de producciones:")
	String productionnumber();

	@DefaultStringValue("Eliminación de Producciones no Generativas (PNG)")
	String pngalgorithm();

	// Q
	@DefaultStringValue("Necesita ejecutar Factorización y Eliminación de recursividad.")
	String questionfirstfollow();

	// R
	@DefaultStringValue("Producciones recursivas:")
	String recprods();

	@DefaultStringValue("Producción recursiva:")
	String recprod();

	@DefaultStringValue("Producciones nuevas tras eliminar Rec. Indir:")
	String resultprods();

	@DefaultStringValue("Debe introducir un símbolo válido.")
	String renameerror();

	@DefaultStringValue("Buscar y renombrar:")
	String renamesymbol();

	@DefaultStringValue("Palabra RECONOCIDA.")
	String recognizedword();

	@DefaultStringValue("Los cambios no se aplicarán hasta que no reinicie el programa.")
	String restartchanges();

	@DefaultStringValue("Regular")
	String regular();

	// S
	@DefaultStringValue("Pila")
	String stack();

	@DefaultStringValue("Eliminación de Símbolos no Alcanzables (SNA)")
	String snaalgorithm();

	@DefaultStringValue("Eliminación de Símbolos no Terminables (SNT)")
	String sntalgorithm();

	@DefaultStringValue("Eliminación de Símbolos Anulables (SA)")
	String saalgorithm();

	@DefaultStringValue("Lo sentimos, no ha podido salir de la sesión.")
	String sessionerr();
	// T
	@DefaultStringValue("Terminales")
	String tokens();

	@DefaultStringValue("Herramientas")
	String tools();

	@DefaultStringValue("Reconocimiento con TASP")
	String tasp();

	@DefaultStringValue("Tope")
	String top();

	@DefaultStringValue("Producción a transformar:")
	String transprod();

	@DefaultStringValue("Traza de reconocimiento:")
	String trace();

	// U
	@DefaultStringValue("¿Desea continuar?")
	String ucontinue();

	@DefaultStringValue("Símbolos unitarios:")
	String unisymbols();

	// W
	@DefaultStringValue("Palabra:")
	String word();

	// Y
	@DefaultStringValue("Si")
	String yes();

}