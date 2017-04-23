package src.client.gui.utils;

public class ToHTML {
     public static String toHTML (String txt) {
    	return (
    			"<html>"
    			+ "<style>"
    					
    			+ "mark { "
    			+ "background-color:  #33CC00;}"
    			
    			+ "mark2 {"
    			+ "background-color:  	#FF3333;}"
    			
    			+ "</style>"
    			+ "<body>" + txt + "</body>"
    			
    			+ "</html>");//.replaceAll("\n", "<br>");
    }
}
