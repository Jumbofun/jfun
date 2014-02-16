import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;


public class LocationListServlet extends HttpServlet{
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		DatabaseObject db = new DatabaseObject();
		db.connect();
		ArrayList<String> temp = null;
		temp = db.getLocations();
		
		if(temp != null){
			JSONArray jsArray = JSONArray.fromObject(temp);
			out.write(jsArray.toString());
		}
		else
		{
			out.write("0");
		}		
		out.flush();
		out.close();
		db.disconnect();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		doPost(request, response);
	}
}
