import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

public class InfoListIdServlet extends HttpServlet {
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String style = request.getParameter("style");
		String location = new String(request.getParameter("location").getBytes(
				"GBK"), "UTF-8");
		String time_start = request.getParameter("time_start");
		String time_end = request.getParameter("time_end");
		String category = request.getParameter("category");

		// System.out.println(style);
		// System.out.println(location);
		// System.out.println(category);

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		DatabaseObject db = new DatabaseObject();
		db.connect();
		ArrayList<InfoListId> temp = null;
		temp = db.getInfoListId(style, location, time_start, time_end, category);

		if (temp != null) {
			JSONArray jsArray = JSONArray.fromObject(temp);
			out.write(jsArray.toString());
		} else {
			out.write("0");
		}
		out.flush();
		out.close();
		db.disconnect();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
