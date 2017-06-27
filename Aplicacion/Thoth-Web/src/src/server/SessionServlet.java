package src.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 
 * <b>Descripción</b><br>
 * Clase que recibe peticiones y comprueba si hay una sesion activa o no y
 * dependiendo de si la hay redirige a un módulo o redirige a otro
 * 
 * @author Francisco Javier Páramo Arnaiz
 * @version 1.0
 */
public class SessionServlet extends RegServlet {

	/**
	 * Numero de versión que posee la clase.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Método que se ejecuta cuando se hace una petición a la clase y realiza
	 * una acción de respuesta, en este caso de redigir a otro módulo.
	 */
	@Override
	protected void doGet(HttpServletRequest reques, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");

		PrintWriter page = response.getWriter();

		if (((String) reques.getSession().getAttribute("Current_user")) == null) {

			page.append("<html><head>"
					+ "<link rel='shortcut icon' type='images/png' href='/images/thothIcon2.png'/>"
					+ "<title>Thoth</title><script type=\"text/javascript\" src=\"../login/login.nocache.js\"></script></head><body></body></html>");
		} else {

			page.append("<html><head>"
					+ "<link rel='shortcut icon' type='images/png' href='/images/thothIcon2.png'/>"
					+ "<title>Thoth</title><script type=\"text/javascript\" src=\"../gramaticacs/gramaticacs.nocache.js\"></script></head><body></body></html>");
		}
	}// doGet
}// SessionServlet
