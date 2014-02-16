import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

public class LoginServlet extends HttpServlet{
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		DatabaseObject db = new DatabaseObject();
		db.connect();
		UserInfo temp = null;
		temp = db.login(email, password);		
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
