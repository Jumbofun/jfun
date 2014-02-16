import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;


public class InfoListServlet extends HttpServlet{
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		String id1 = request.getParameter("id1");
		String id2 = request.getParameter("id2");
		String id3 = request.getParameter("id3");
		String timeslot = request.getParameter("timeslot");
		
//		System.out.println(style);
//		System.out.println(location);
//		System.out.println(category);
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		DatabaseObject db = new DatabaseObject();
		db.connect();
		ArrayList<InfoList> temp = null;
		temp = db.getInfoList(id1, id2, id3, timeslot);
		
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
