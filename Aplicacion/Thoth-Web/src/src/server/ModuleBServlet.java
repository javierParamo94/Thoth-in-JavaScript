package src.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import src.shared.UserDto;

/**
 * 
 * @author User
 *
 */
public class ModuleBServlet  extends RegServlet{
	/**
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		res.setContentType("text/html");
		res.setCharacterEncoding("UTF-8");

		PrintWriter page = res.getWriter();

		if(((String)req.getSession().getAttribute("Current_user"))==null){

			page.append("<html><head><script type=\"text/javascript\" src=\"../login/login.nocache.js\"></script></head><body></body></html>");
		}else{

			page.append("<html><head><script type=\"text/javascript\" src=\"../gramaticacs/gramaticacs.nocache.js\"></script></head><body></body></html>");
		}
	}
}
