package src.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import src.shared.UserDto;

public class ModuleBServlet  extends AdvancedServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		RequestDispatcher dispacher;
		boolean flag;
		String userid=req.getParameter("userID");
		String name=req.getParameter("ename");
		String username=req.getParameter("userName");
		String password=req.getParameter("pass");
		String email=req.getParameter("email");
		
		//flag=User.c
		
		//set up response
		res.setContentType("text/html");
		res.setCharacterEncoding("UTF-8");
		
		//set up our response
		//if the user is already authenticated => append appropriate module js
		PrintWriter page = res.getWriter();
		//flag = AdvancedServlet.createUpdateUser(userid, name, username, password, email);
		//if (flag){
			
		//}
		// user helper from AdvancedServlet to get local current_user if it exists
		//if(((UserDto)req.getSession().getAttribute("current_user"))==null){
			//this guy needs to login..
			//respond with the login page (login.js)
			//page.append("<html><head><script type=\"text/javascript\" src=\"../login/login.nocache.js\"></script></head><body></body></html>");
			//page.append("<html><head><script type=\"text/javascript\" src=\"../module_b/module_b.nocache.js\"></script></head><body></body></html>");
		//}else{
			//the user is authenticated
			//respond with the host page (ModuleA.js) for module a
			page.append("<html><head><script type=\"text/javascript\" src=\"../gramaticacs/gramaticacs.nocache.js\"></script></head><body></body></html>");
		//	page.append("<html><head><script type=\"text/javascript\" src=\"../login/login.nocache.js\"></script></head><body></body></html>");
		//	
		//}
	}
}
