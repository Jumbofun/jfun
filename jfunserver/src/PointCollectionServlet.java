import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PointCollectionServlet extends HttpServlet{
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		String id = request.getParameter("id");
		String uid = request.getParameter("uid");
		String timeslot = request.getParameter("timeslot");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		DatabaseObject db = new DatabaseObject();
		db.connect();
		int temp = db.pointCollection(id, uid, timeslot);		
		if(temp == 0){
			out.write("0");
		}
		else
		{
			out.write("1");
		}
		out.flush();
		out.close();
		db.disconnect();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		doPost(request, response);
	}
}
