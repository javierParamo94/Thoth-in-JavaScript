package src.client.gui.utils;

/**
 * 
 * @author User
 *
 */
public class HTMLConverter {
     public static String toHTML (String txt) {
    	
    			String result="<html>"
    			+ "<style>"
    					
    			+ "mark.green { "
    			+ "background-color:  #33CC00;}"
    			
    			+ "mark.red {"
    			+ "background-color: #FF3333;}"
    			
    			+ "</style>"
    			+ "<body>";//+txt+"</body>"+ "</html>";
    			
    			for(String s : txt.split("\n")){
    				//result += "<p>"+ s + "</p>";
    				result += s + "<br />";
    				//Window.alert("HTML: "+result);
    			}
    				result +="</body>"
    			+ "</html>";
    			
    			return result;
    }
}
