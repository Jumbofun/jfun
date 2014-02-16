import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet{
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		String pid = request.getParameter("pid");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		String sex = request.getParameter("sex");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out=response.getWriter();
		DatabaseObject db=new DatabaseObject();
		db.connect();
		int temp = db.register(pid, username, password, email, phone, sex);
		if(temp == 0){
			out.write("0");
		}else if(temp == 1){
			out.write("1");
		}else{
			out.write("2");
		}
		out.flush();
		out.close();
		db.disconnect();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
		doPost(request,response);
	}
}
