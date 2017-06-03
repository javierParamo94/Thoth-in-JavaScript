package src.client.gui.utils;

/**
 * <b>Descripción</b><br>
 * Clase para la conversión de un texto en del tipo String a otro texto en formato HTML del tipo String
 * <p>
 * <b>Detalles</b><br>
 * Con ello se puede tratar como un texto HTML con funcionalidades propias de ese formato.<br>
 * </p>
 * <p>
 * <b>Funcionalidad</b><br>
 * Con ello se puede dar color y forma al estilo HTML de un texto que se le pase.
 * </p>
 * 
 * 
 * @author Francisco Javier Páramo Arnaiz
 * @version 1.0
 */
public class HTMLConverter {
	/**
	 * Funcion que recibe un string y devuelve otro con características HTML
	 * 
	 * @param txt	Texto a convertir en HTML.
	 * @return	String con características HTML.
	 */
	public static String toHTML(String txt) {

		String result = "<html>" + "<style>"
		// Establece un tipo de subrayado de color verde.
				+ "mark.green { " + "background-color:  #33CC00;}"
				// Establece un tipo de subrayado de color rojo.
				+ "mark.red {" + "background-color: #FF3333;}"

				+ "</style>" + "<body>";
		// convierte los retornos de carro o finales de line en el final del
		// linea de tipo HTML
		for (String s : txt.split("\n")) {
			result += s + "<br />";
		}
		result += "</body>" + "</html>";

		return result;
	}//toHTML
}//HTMLConverter
