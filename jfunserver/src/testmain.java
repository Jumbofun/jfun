import java.sql.SQLException;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;


public class testmain {
	public static void main(String arg[]){
		DatabaseObject db=new DatabaseObject();
		db.connect();
		
//		ArrayList<InfoListId> temp = null;
//		temp = db.getInfoListId("1", "Œ‰¡÷", "8", "10", "default");
		
//		ArrayList<InfoList> temp = null;
//		temp = db.getInfoList("1000001", "1000006", "", "breakfast");
		
//		ArrayList<String> temp = null;
//		temp = db.getLocations();
		
//		ArrayList<Route> temp = null;
//		temp = db.getRouteListId();
		
		Point temp = null;
		temp = db.getPoint("1");
//		ArrayList<String> temp = null;
//		temp = db.tip();
//		ArrayList<InfoList> temp = null;
//		temp = db.getInfoList("1000002", "1", "3", "breakfast");
		
//		int temp = 0;
//		temp = db.history("0","1","1");
//		System.out.println(temp);
//		ArrayList<Activity> temp = null;
//		temp = db.getActivity();
//		
		
		if(temp != null){
			JSONArray jsArray = JSONArray.fromObject(temp);
			System.out.println(jsArray);
		}
		
		db.disconnect();
//		for(int i=1;i<=43;i++){
//			System.out.println("(")
//		}
	}
}
