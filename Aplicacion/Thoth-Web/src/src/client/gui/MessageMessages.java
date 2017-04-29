package src.client.gui;

import com.google.gwt.i18n.client.Constants;

public interface MessageMessages extends Constants {

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

	@DefaultStringValue("Factorizar por la izquierda")
	String factoring();

	@DefaultStringValue("Forma normal de Chomsky")
	String fnchomsky();

	@DefaultStringValue("Número de producciones:")
	String productionnumber();

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

	@DefaultStringValue("por:")
	String by();

	@DefaultStringValue("Cancelar")
	String cancel();

	@DefaultStringValue("Símbolos anulables:")
	String cancelsymbols();

	@DefaultStringValue("No terminales:")
	String nonterminals();

	@DefaultStringValue("Terminales")
	String tokens();

	@DefaultStringValue("Eliminación de Símbolos no Alcanzables (SNA)")
	String snaalgorithm();

	@DefaultStringValue("Eliminación de Símbolos no Terminables (SNT)")
	String sntalgorithm();

	@DefaultStringValue("Eliminación de Recursividad Indirecta")
	String indirectrecursive();

	@DefaultStringValue("Eliminación de Símbolos Anulables (SA)")
	String saalgorithm();

	@DefaultStringValue("Símbolos unitarios:")
	String unisymbols();

	@DefaultStringValue("Eliminación de Producciones no Generativas (PNG)")
	String pngalgorithm();

	@DefaultStringValue("Eliminación de Recursividad Directa")
	String directrecursive();

	@DefaultStringValue("Producciones recursivas:")
	String recprods();

	@DefaultStringValue("Producción recursiva:")
	String recprod();

	@DefaultStringValue("Producciones nuevas tras eliminar Rec. Indir:")
	String resultprods();

	@DefaultStringValue("Prefijo:")
	String factorsymbols();
	
	@DefaultStringValue("Los cambios no se aplicarán hasta que no reinicie el programa.")
	String restartchanges();			

	@DefaultStringValue("Producción a transformar:")
	String transprod();

	@DefaultStringValue("Convertir a Forma Normal de Chomsky")
	String chomskyalgorithm();

	@DefaultStringValue("Algoritmo de factorización")
	String factoralgorithm();

	@DefaultStringValue("Gramática")
	String grammar();

	@DefaultStringValue("Definición de Gramática")
	String grammardef();

	@DefaultStringValue("Propiedades de la gramática:")
	String grammarproperties();

	@DefaultStringValue("Tipo de Gramática:")
	String grammartype();

	@DefaultStringValue("Dependiente del contexto")
	String dependent();

	@DefaultStringValue("Independiente del contexto")
	String independent();

	@DefaultStringValue("Regular")
	String regular();

	@DefaultStringValue("Idioma")
	String language();

	@DefaultStringValue("Archivo")
	String file();

	@DefaultStringValue("Editar")
	String edit();

	@DefaultStringValue("Herramientas")
	String tools();

	@DefaultStringValue("Ayuda")
	String help();

	@DefaultStringValue("Limpiar gramática")
	String clear();

	@DefaultStringValue("Cálculo First y Follow")
	String calculateff();

	@DefaultStringValue("Reconocimiento con TASP")
	String tasp();

	@DefaultStringValue("Vieja gramática:")
	String oldgrammar();

	@DefaultStringValue("Nueva gramática:")
	String newgrammar();

	@DefaultStringValue("Necesita ejecutar Factorización y Eliminación de recursividad.")
	String questionfirstfollow();

	@DefaultStringValue("¿Desea continuar?")
	String ucontinue();

	@DefaultStringValue("No")
	String no();

	@DefaultStringValue("Si")
	String yes();

	@DefaultStringValue("Salir")
	String exit();

}