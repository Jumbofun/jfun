import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;

class MatchResult {
	String newRoot;
	int file_length;
	String[] results;
	public String getNewRoot() {
		return newRoot;
	}
	public void setNewRoot(String newRoot) {
		this.newRoot = newRoot;
	}
	public int getFile_length() {
		return file_length;
	}
	public void setFile_length(int file_length) {
		this.file_length = file_length;
	}
	public String[] getResults() {
		return results;
	}
	public void setResults(String[] results) {
		this.results = results;
	}
	public MatchResult(String newRoot, int file_length, String[] results) {
		super();
		this.newRoot = newRoot;
		this.file_length = file_length;
		this.results = results;
	}
	public MatchResult() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}

public class DatabaseObject {
	public static final String driverName = "com.mysql.jdbc.Driver";
	public static final String dbURL = "jdbc:mysql://localhost:3306/jfun?useUnicode=true&characterEncoding=GBK";
	public static final String userName = "root";
	public static final String userPwd = "admin";
	private String dirOfPhotos = "F:\\photo_backup\\";
//	private String dirOfPhotos = "D:\\apache-tomcat-7.0.47\\webapps\\jfun\\photo\\";
	private String dirOfRPhotos = "D:\\apache-tomcat-7.0.47\\webapps\\jfun\\route_photos\\";
	private String reg = "(\\d+\\.([jJ][pP][gG]|[pP][nN][gG]|[gG][iI][fF]))+?";
	private Connection dbConn = null;
		
	public Connection getDbConn() {
		return dbConn;
	}

	public void setDbConn(Connection dbConn) {
		this.dbConn = dbConn;
	}

	public boolean connect(){		
		try{
			Class.forName(driverName);
//			com.mysql.jdbc.Driver driver = new com.mysql.jdbc.Driver();
			dbConn = DriverManager.getConnection(dbURL, userName, userPwd);
			System.out.println("CONNECTION SUCCEEDED");
			return true;
		}catch(SQLException se){
			se.printStackTrace();
			System.out.println("JBDC FAILED");
			return false;
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("CONNECTION FAILED");
			return false;
		}
	}
	
	public boolean disconnect(){
		try{
			dbConn.close();
			return true;
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	private MatchResult getPhotoPath(String pid, String root){
		int i,k=0;
		MatchResult matchResult = new MatchResult();
		String photo_dir;
		if(pid.equals("0")){
			photo_dir = root;
		}
		else{
			photo_dir = root+pid+"\\";
		}
		matchResult.newRoot = photo_dir;
		File file = new File(photo_dir);
		File[] files = file.listFiles();
		for(i=0; i<files.length; i++){
			if(!files[i].isDirectory()){
				k++;
			}
		}
		matchResult.setFile_length(k);
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher;
		matchResult.results = new String[k]; 
//		matchResult = new MatchResult(photo_dir, files.length, );
		for(int j = 0; j<k; j++){
			if(!files[j].isDirectory()){
				String filename = files[j].toString();
				matcher = pattern.matcher(filename);
				if(matcher.find()){
					matchResult.results[j] = matcher.group();
				}
			}
		}
		return matchResult;
	}
	
	public int register(String pid, String username, String password, String email, String phone, String sex){
		try{
			int count = 0;
			Statement stmt=dbConn.createStatement();
			String registerCheck="SELECT COUNT(*) FROM users WHERE email='"+email+"'";
			if(stmt.execute(registerCheck)){
				ResultSet rs = stmt.getResultSet();
				if(rs.next()){
					count = rs.getInt(1);
				}
			}
			if(count == 0){
				String register="INSERT INTO users (`pid`, `username`, `password`, `email`, `phone`, `sex`) VALUES ('"+pid+"', '"+username+"', sha("+password+"), '"+email+"', '"+phone+"', '"+sex+"');";
				stmt.execute(register);
				return 0;	// succeed
			}else{
				return 1;	// already exist
			}
		}catch(Exception e){
			return 2;
		}
	}
	
	public UserInfo login(String email, String password){
		try{
			int uid = 0;
			String url = null;
			String username = null;
			String phone = null;
			String sex = null;
			
			Statement stmt = dbConn.createStatement();			
			String check = "select uid, p.url, username, phone, sex  from users u, user_photos p where email='"+email+"' and password=sha("+password+") and u.pid=p.pid";
			if(stmt.execute(check)){
				ResultSet rs = stmt.getResultSet();
				if(rs.next()){
					uid = rs.getInt(1);
					url = rs.getString(2);
					username = rs.getString(3);
					phone = rs.getString(4);
					sex = rs.getString(5);
				}
				UserInfo userInfo = new UserInfo(uid, url, username, password, email, phone, sex);
				return userInfo;
			}else{
				return null;
			}
		}catch (SQLException e){
			return null;
		}
	}
	
	public ArrayList<String> tip(){
		try{
			ArrayList<String> contents = new ArrayList<String>();
			Statement stmt = dbConn.createStatement();
			String check = "SELECT content FROM tips ORDER BY RAND() LIMIT 1";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				if (rs.next()) {
					String content = rs.getString(1);
					contents.add(content);
				}
				return contents;
			}else{
				return null;
			}
		} catch (SQLException e) {
			return null;
		}
	}
	
	public int good(String rid){
		try{
			Statement stmt=dbConn.createStatement();
			String check="UPDATE routes SET good = good+1 WHERE rid="+rid;
			stmt.execute(check);
			return 0;	// succeed
		}catch(Exception e){
			return 1;	// fail
		}
	}
	
	public int bad(String rid){
		try{
			Statement stmt=dbConn.createStatement();
			String check="UPDATE routes SET bad = bad+1 WHERE rid='"+rid+"'";
			stmt.execute(check);
			return 0;	// succeed
		}catch(Exception e){
			return 1;	// fail
		}
	}
	
	public int pointCollection(String id, String uid, String timeslot){
		try{
			Statement stmt=dbConn.createStatement();
			Statement stmt1=dbConn.createStatement();
			String check;
			String check1 = "select sid from pointconnect where pcid='"+id+"' and category='"+timeslot+"'"; 
			if(stmt1.execute(check1)){
				ResultSet rs1 = stmt1.getResultSet();
				while(rs1.next()){
					int new_id = rs1.getInt(1);
					check = "INSERT INTO pointCollection (`id`, `uid`, `recordtime`) VALUES ('"+new_id+"', '"+uid+"', Current_Timestamp())";
					if(stmt.executeUpdate(check)==0){
						return 1;//fail
					}
				}
			}
			return 0;	// succeed
		}catch(Exception e){
			return 1;	// fail
		}
	}
	
//	public 
	
	public int routeCollection(String rid1, String rid2, String uid){
		try{
			Statement stmt=dbConn.createStatement();
			String check="INSERT INTO routeCollection (`rid1`, `rid2`, `uid`, `recordtime`) VALUES ('"+rid1+"', '"+rid2+"', '"+uid+"', Current_Timestamp())";
			stmt.execute(check);
			return 0;	// succeed
		}catch(Exception e){
			return 1;	// fail
		}
	}
	
	public int history(String rid1, String rid2, String uid){
		try{
			Statement stmt=dbConn.createStatement();
			String check="INSERT INTO history (`rid1`, `rid2`, `uid`, `recordtime`) VALUES ('"+rid1+"', '"+rid2+"', '"+uid+"', Current_Timestamp())";
			stmt.execute(check);
			return 0;	// succeed
		}catch(Exception e){
			return 1;	// fail
		}
	}
	
	public void selectBreakfastId(String style, String location, Statement stmt, ArrayList<InfoListId> results){
		try{
			String check = "SELECT id, category FROM breakfast WHERE style = "+ style +" AND location = '"+ location +"' ORDER BY RAND()";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					String category = rs.getString(2);
					InfoListId infoListId = new InfoListId(id, category, "breakfast");
					results.add(infoListId);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void selectBreakfastId(String style, String location, String category, Statement stmt, ArrayList<InfoListId> results){
		try{
			String check = "SELECT id FROM breakfast WHERE style = "+ style +" AND location = '"+ location +"' AND category = '"+ category +"' ORDER BY RAND()";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					InfoListId infoListId = new InfoListId(id, category, "breakfast");
					results.add(infoListId);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void selectMorningId(String style, String location, Statement stmt, ArrayList<InfoListId> results){
		try{
			String check = "SELECT id, category FROM morning WHERE style = "+ style +" AND location = '"+ location +"' ORDER BY RAND()";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					String category = rs.getString(2);
					InfoListId infoListId = new InfoListId(id, category, "morning");
					results.add(infoListId);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void selectMorningId(String style, String location, String category, Statement stmt, ArrayList<InfoListId> results){
		try{
			String check = "SELECT id FROM morning WHERE style = "+ style +" AND location = '"+ location +"' AND category = '"+ category +"' ORDER BY RAND()";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					InfoListId infoListId = new InfoListId(id, category, "morning");
					results.add(infoListId);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void selectLunchId(String style, String location, Statement stmt, ArrayList<InfoListId> results){
		try{
			String check = "SELECT id, category FROM lunch WHERE style = "+ style +" AND location = '"+ location +"' ORDER BY RAND()";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					String category = rs.getString(2);
					InfoListId infoListId = new InfoListId(id, category, "lunch");
					results.add(infoListId);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void selectLunchId(String style, String location, String category, Statement stmt, ArrayList<InfoListId> results){
		try{
			String check = "SELECT id FROM lunch WHERE style = "+ style +" AND location = '"+ location +"' AND category = '"+ category +"' ORDER BY RAND()";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					InfoListId infoListId = new InfoListId(id, category, "lunch");
					results.add(infoListId);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void selectAfternoonId(String style, String location, Statement stmt, ArrayList<InfoListId> results){
		try{
			String check = "SELECT id, category FROM afternoon WHERE style = "+ style +" AND location = '"+ location +"' ORDER BY RAND()";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					String category = rs.getString(2);
					InfoListId infoListId = new InfoListId(id, category, "afternoon");
					results.add(infoListId);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void selectAfternoonId(String style, String location, String category, Statement stmt, ArrayList<InfoListId> results){
		try{
			String check = "SELECT id FROM afternoon WHERE style = "+ style +" AND location = '"+ location +"' AND category = '"+ category +"' ORDER BY RAND()";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					InfoListId infoListId = new InfoListId(id, category, "afternoon");
					results.add(infoListId);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void selectDinnerId(String style, String location, Statement stmt, ArrayList<InfoListId> results){
		try{
			String check = "SELECT id, category FROM dinner WHERE style = "+ style +" AND location = '"+ location +"' ORDER BY RAND()";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					String category = rs.getString(2);
					InfoListId infoListId = new InfoListId(id, category, "dinner");
					results.add(infoListId);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void selectDinnerId(String style, String location, String category, Statement stmt, ArrayList<InfoListId> results){
		try{
			String check = "SELECT id FROM dinner WHERE style = "+ style +" AND location = '"+ location +"' AND category = '"+ category +"' ORDER BY RAND()";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					InfoListId infoListId = new InfoListId(id, category, "dinner");
					results.add(infoListId);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void selectNightId(String style, String location, Statement stmt, ArrayList<InfoListId> results){
		try{
			String check = "SELECT id, category FROM night WHERE style = "+ style +" AND location = '"+ location +"' ORDER BY RAND()";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					String category = rs.getString(2);
					InfoListId infoListId = new InfoListId(id, category, "night");
					results.add(infoListId);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void selectNightId(String style, String location, String category, Statement stmt, ArrayList<InfoListId> results){
		try{
			String check = "SELECT id FROM night WHERE style = "+ style +" AND location = '"+ location +"' AND category = '"+ category +"' ORDER BY RAND()";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					InfoListId infoListId = new InfoListId(id, category, "night");
					results.add(infoListId);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void selectMidnightId(String style, String location, Statement stmt, ArrayList<InfoListId> results){
		try{
			String check = "SELECT id, category FROM midnight WHERE style = "+ style +" AND location = '"+ location +"' ORDER BY RAND()";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					String category = rs.getString(2);
					InfoListId infoListId = new InfoListId(id, category, "midnight");
					results.add(infoListId);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void selectMidnightId(String style, String location, String category, Statement stmt, ArrayList<InfoListId> results){
		try{
			String check = "SELECT id FROM midnight WHERE style = "+ style +" AND location = '"+ location +"' AND category = '"+ category +"' ORDER BY RAND()";		
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					InfoListId infoListId = new InfoListId(id, category, "midnight");
					results.add(infoListId);
				}
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	public void selectBreakfast(String id, Statement stmt, ArrayList<InfoList> results){
		String check = "SELECT name, address, phone, price, description, p.url, l.url, rating, category, pid1, pid2, pid3, lid1, b.pid FROM breakfast b, photos p, links l WHERE id = "+ id +" AND b.pid = p.pid AND b.lid = l.lid";
		try {
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				if (rs.next()) {
					String name = rs.getString(1);
					String address = rs.getString(2);
					String phone = rs.getString(3);
					int price = rs.getInt(4);
					String description = rs.getString(5);
					String plink_root = rs.getString(6);
					String link_root = rs.getString(7);
					
					//match photos
					String pid = rs.getString(14);
//					String photo_dir = "F:\\photo_backup\\"+pid+"\\";
					String photo_dir = dirOfPhotos+pid+"\\";
					File file = new File(photo_dir);
					File[] files = file.listFiles();
					Pattern pattern = Pattern.compile(reg);
					Matcher matcher;
					String[] pid_results = new String[files.length]; 
					for(int j = 0; j<files.length; j++){
						if(!files[j].isDirectory()){
							String filename = files[j].toString();
							matcher = pattern.matcher(filename);
							if(matcher.find()){
								pid_results[j] = matcher.group();
							}
						}
					}
					
					
					float rating = rs.getFloat(8);
					String category = rs.getString(9);
					String lid1 = rs.getString(13);
					String[] plink = new String[files.length];
					String[] link = new String[1];
					for(int i = 0; i<files.length; i++){
						plink[i] = plink_root+pid_results[i];
					}
					link[0] = link_root;
					if (name != null && name.length() > 0) {
						InfoList infoList = new InfoList(name, address, phone, price, description, plink, link, lid1, rating, category);
						results.add(infoList);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void selectMorning(String id, Statement stmt, ArrayList<InfoList> results){
		String check = "SELECT name, address, phone, price, description, p.url, l.url, rating, category, pid1, pid2, pid3, lid1, b.pid FROM morning b, photos p, links l WHERE id = "+ id +" AND b.pid = p.pid AND b.lid = l.lid";
		try {
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				if (rs.next()) {
					String name = rs.getString(1);
					String address = rs.getString(2);
					String phone = rs.getString(3);
					int price = rs.getInt(4);
					String description = rs.getString(5);
					String plink_root = rs.getString(6);
					String link_root = rs.getString(7);
					
					//match photos
					String pid = rs.getString(14);
//					String photo_dir = "F:\\photo_backup\\"+pid+"\\";
					String photo_dir = dirOfPhotos+pid+"\\";
					File file = new File(photo_dir);
					File[] files = file.listFiles();
					Pattern pattern = Pattern.compile(reg);
					Matcher matcher;
					String[] pid_results = new String[files.length]; 
					for(int j = 0; j<files.length; j++){
						if(!files[j].isDirectory()){
							String filename = files[j].toString();
							matcher = pattern.matcher(filename);
							if(matcher.find()){
								pid_results[j] = matcher.group();
							}
						}
					}
					
					
					float rating = rs.getFloat(8);
					String category = rs.getString(9);
					String lid1 = rs.getString(13);
					String[] plink = new String[files.length];
					String[] link = new String[1];
					for(int i = 0; i<files.length; i++){
						plink[i] = plink_root+pid_results[i];
					}
					link[0] = link_root;
					if (name != null && name.length() > 0) {
						InfoList infoList = new InfoList(name, address, phone, price, description, plink, link, lid1, rating, category);
						results.add(infoList);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void selectLunch(String id, Statement stmt, ArrayList<InfoList> results){
		String check = "SELECT name, address, phone, price, description, p.url, l.url, rating, category, pid1, pid2, pid3, lid1, b.pid FROM lunch b, photos p, links l WHERE id = "+ id +" AND b.pid = p.pid AND b.lid = l.lid";
		try {
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				if (rs.next()) {
					String name = rs.getString(1);
					String address = rs.getString(2);
					String phone = rs.getString(3);
					int price = rs.getInt(4);
					String description = rs.getString(5);
					String plink_root = rs.getString(6);
					String link_root = rs.getString(7);
					
					//match photos
					String pid = rs.getString(14);
//					String photo_dir = "F:\\photo_backup\\"+pid+"\\";
					String photo_dir = dirOfPhotos+pid+"\\";
					File file = new File(photo_dir);
					File[] files = file.listFiles();
					Pattern pattern = Pattern.compile(reg);
					Matcher matcher;
					String[] pid_results = new String[files.length]; 
					for(int j = 0; j<files.length; j++){
						if(!files[j].isDirectory()){
							String filename = files[j].toString();
							matcher = pattern.matcher(filename);
							if(matcher.find()){
								pid_results[j] = matcher.group();
							}
						}
					}
					
					
					float rating = rs.getFloat(8);
					String category = rs.getString(9);
					String lid1 = rs.getString(13);
					String[] plink = new String[files.length];
					String[] link = new String[1];
					for(int i = 0; i<files.length; i++){
						plink[i] = plink_root+pid_results[i];
					}
					link[0] = link_root;
					if (name != null && name.length() > 0) {
						InfoList infoList = new InfoList(name, address, phone, price, description, plink, link, lid1, rating, category);
						results.add(infoList);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void selectAfternoon(String id, Statement stmt, ArrayList<InfoList> results){
		String check = "SELECT name, address, phone, price, description, p.url, l.url, rating, category, pid1, pid2, pid3, lid1, b.pid FROM afternoon b, photos p, links l WHERE id = "+ id +" AND b.pid = p.pid AND b.lid = l.lid";
		try {
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				if (rs.next()) {
					String name = rs.getString(1);
					String address = rs.getString(2);
					String phone = rs.getString(3);
					int price = rs.getInt(4);
					String description = rs.getString(5);
					String plink_root = rs.getString(6);
					String link_root = rs.getString(7);
					
					//match photos
					String pid = rs.getString(14);
//					String photo_dir = "F:\\photo_backup\\"+pid+"\\";
					String photo_dir = dirOfPhotos+pid+"\\";
					File file = new File(photo_dir);
					File[] files = file.listFiles();
					Pattern pattern = Pattern.compile(reg);
					Matcher matcher;
					String[] pid_results = new String[files.length]; 
					for(int j = 0; j<files.length; j++){
						if(!files[j].isDirectory()){
							String filename = files[j].toString();
							matcher = pattern.matcher(filename);
							if(matcher.find()){
								pid_results[j] = matcher.group();
							}
						}
					}
					
					
					float rating = rs.getFloat(8);
					String category = rs.getString(9);
					String lid1 = rs.getString(13);
					String[] plink = new String[files.length];
					String[] link = new String[1];
					for(int i = 0; i<files.length; i++){
						plink[i] = plink_root+pid_results[i];
					}
					link[0] = link_root;
					if (name != null && name.length() > 0) {
						InfoList infoList = new InfoList(name, address, phone, price, description, plink, link, lid1, rating, category);
						results.add(infoList);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void selectDinner(String id, Statement stmt, ArrayList<InfoList> results){
		String check = "SELECT name, address, phone, price, description, p.url, l.url, rating, category, pid1, pid2, pid3, lid1, b.pid FROM dinner b, photos p, links l WHERE id = "+ id +" AND b.pid = p.pid AND b.lid = l.lid";
		try {
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				if (rs.next()) {
					String name = rs.getString(1);
					String address = rs.getString(2);
					String phone = rs.getString(3);
					int price = rs.getInt(4);
					String description = rs.getString(5);
					String plink_root = rs.getString(6);
					String link_root = rs.getString(7);
					
					//match photos
					String pid = rs.getString(14);
//					String photo_dir = "F:\\photo_backup\\"+pid+"\\";
					String photo_dir = dirOfPhotos+pid+"\\";
					File file = new File(photo_dir);
					File[] files = file.listFiles();
					Pattern pattern = Pattern.compile(reg);
					Matcher matcher;
					String[] pid_results = new String[files.length]; 
					for(int j = 0; j<files.length; j++){
						if(!files[j].isDirectory()){
							String filename = files[j].toString();
							matcher = pattern.matcher(filename);
							if(matcher.find()){
								pid_results[j] = matcher.group();
							}
						}
					}
					
					
					float rating = rs.getFloat(8);
					String category = rs.getString(9);
					String lid1 = rs.getString(13);
					String[] plink = new String[files.length];
					String[] link = new String[1];
					for(int i = 0; i<files.length; i++){
						plink[i] = plink_root+pid_results[i];
					}
					link[0] = link_root;
					if (name != null && name.length() > 0) {
						InfoList infoList = new InfoList(name, address, phone, price, description, plink, link, lid1, rating, category);
						results.add(infoList);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void selectNight(String id, Statement stmt, ArrayList<InfoList> results){
		String check = "SELECT name, address, phone, price, description, p.url, l.url, rating, category, pid1, pid2, pid3, lid1, b.pid FROM night b, photos p, links l WHERE id = "+ id +" AND b.pid = p.pid AND b.lid = l.lid";
		try {
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				if (rs.next()) {
					String name = rs.getString(1);
					String address = rs.getString(2);
					String phone = rs.getString(3);
					int price = rs.getInt(4);
					String description = rs.getString(5);
					String plink_root = rs.getString(6);
					String link_root = rs.getString(7);
					
					//match photos
					String pid = rs.getString(14);
//					String photo_dir = "F:\\photo_backup\\"+pid+"\\";
					String photo_dir = dirOfPhotos+pid+"\\";
					File file = new File(photo_dir);
					File[] files = file.listFiles();
					Pattern pattern = Pattern.compile(reg);
					Matcher matcher;
					String[] pid_results = new String[files.length]; 
					for(int j = 0; j<files.length; j++){
						if(!files[j].isDirectory()){
							String filename = files[j].toString();
							matcher = pattern.matcher(filename);
							if(matcher.find()){
								pid_results[j] = matcher.group();
							}
						}
					}
					
					
					float rating = rs.getFloat(8);
					String category = rs.getString(9);
					String lid1 = rs.getString(13);
					String[] plink = new String[files.length];
					String[] link = new String[1];
					for(int i = 0; i<files.length; i++){
						plink[i] = plink_root+pid_results[i];
					}
					link[0] = link_root;
					if (name != null && name.length() > 0) {
						InfoList infoList = new InfoList(name, address, phone, price, description, plink, link, lid1, rating, category);
						results.add(infoList);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void selectMidnight(String id, Statement stmt, ArrayList<InfoList> results){
		String check = "SELECT name, address, phone, price, description, p.url, l.url, rating, category, pid1, pid2, pid3, lid1, b.pid FROM midnight b, photos p, links l WHERE id = "+ id +" AND b.pid = p.pid AND b.lid = l.lid";
		try {
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				if (rs.next()) {
					String name = rs.getString(1);
					String address = rs.getString(2);
					String phone = rs.getString(3);
					int price = rs.getInt(4);
					String description = rs.getString(5);
					String plink_root = rs.getString(6);
					String link_root = rs.getString(7);
					
					//match photos
					String pid = rs.getString(14);
//					String photo_dir = "F:\\photo_backup\\"+pid+"\\";
					String photo_dir = dirOfPhotos+pid+"\\";
					File file = new File(photo_dir);
					File[] files = file.listFiles();
					Pattern pattern = Pattern.compile(reg);
					Matcher matcher;
					String[] pid_results = new String[files.length]; 
					for(int j = 0; j<files.length; j++){
						if(!files[j].isDirectory()){
							String filename = files[j].toString();
							matcher = pattern.matcher(filename);
							if(matcher.find()){
								pid_results[j] = matcher.group();
							}
						}
					}
					
					
					float rating = rs.getFloat(8);
					String category = rs.getString(9);
					String lid1 = rs.getString(13);
					String[] plink = new String[files.length];
					String[] link = new String[1];
					for(int i = 0; i<files.length; i++){
						plink[i] = plink_root+pid_results[i];
					}
					link[0] = link_root;
					if (name != null && name.length() > 0) {
						InfoList infoList = new InfoList(name, address, phone, price, description, plink, link, lid1, rating, category);
						results.add(infoList);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getLocations(){
		try{
			ArrayList<String> results = new ArrayList<String>();
			Statement stmt = dbConn.createStatement();
			String check = "SELECT location FROM locations";	
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					String location = rs.getString(1);
					results.add(location);
				}
			}
			return results;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public ArrayList<Route> getRouteListId(){
		try{
			ArrayList<Route> results = new ArrayList<Route>();
			Statement stmt = getDbConn().createStatement();
//			Statement stmt1 = getDbConn().createStatement();
			String check = "SELECT r.rid, id1, id2, id3, id4, id5, id6, id7, id8, id9, id10, night, good, bad, style, description, title, hot, p.url FROM routes r, route_photos p ORDER BY RAND() LIMIT 20";
//			String check1;
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()){
					int rid = rs.getInt(1);
					int[] id = new int[10];
					id[0] = rs.getInt(2);
					id[1] = rs.getInt(3);
					id[2] = rs.getInt(4);
					id[3] = rs.getInt(5);
					id[4] = rs.getInt(6);
					id[5] = rs.getInt(7);
					id[6] = rs.getInt(8);
					id[7] = rs.getInt(9);
					id[8] = rs.getInt(10);
					id[9] = rs.getInt(11);
					int night = rs.getInt(12);
					float good = rs.getFloat(13);
					float bad = rs.getFloat(14);
					String style = rs.getString(15);
					String description = rs.getString(16);
					String title = rs.getString(17);
					float hot_float = rs.getFloat(18);
					String hot;
					if(hot_float>0.75){
						hot = "1";
					}
					else{
						hot = "0";
					}
					//match photos
					String plink_root = rs.getString(19);
//					String photo_dir = "F:\\photo_backup\\"+pid+"\\";
					String photo_dir = dirOfRPhotos+rid+"\\";
					File file = new File(photo_dir);
					File[] files = file.listFiles();
					Pattern pattern = Pattern.compile(reg);
					Matcher matcher;
					String[] pid_results = new String[files.length]; 
					for(int j = 0; j<files.length; j++){
						if(!files[j].isDirectory()){
							String filename = files[j].toString();
							matcher = pattern.matcher(filename);
							if(matcher.find()){
								pid_results[j] = matcher.group();
							}
						}
					}
					
					String[] plink = new String[files.length];
					for(int i = 0; i<files.length; i++){
						plink[i] = plink_root+pid_results[i];
					}
					
					
					
					Route route = new Route(rid, id, night, good, bad, description, style, title, hot, plink);
					results.add(route);
				}
			}
			return results;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Point getPoint(String id){
		try{
			Point point = null;
			Statement stmt = dbConn.createStatement();
			String check = "SELECT style, name, address, phone, price, description, location, timestart, timeend, o.url, l.url, p.pid1, p.pid2, p.pid3, category, rating, id, p.pid FROM points p, links l, photos o WHERE id="+id+" and p.pid=o.pid and p.lid=l.lid";	
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int style = rs.getInt(1);
					String name = rs.getString(2);
					String address = rs.getString(3);
					String phone = rs.getString(4);
					int price = rs.getInt(5);
					String description = rs.getString(6);
					String location = rs.getString(7);
					int timestart = rs.getInt(8);
					int timeend = rs.getInt(9);
					String plink_root = rs.getString(10);
					String link_root = rs.getString(11);
					
					//match photos
					String pid = rs.getString(18);
					String[] results;
					MatchResult matchResult = getPhotoPath(pid, dirOfPhotos);
//					String photo_dir = "F:\\photo_backup\\"+pid+"\\";
//					String photo_dir = dirOfPhotos+pid+"\\";
//					File file = new File(photo_dir);
//					File[] files = file.listFiles();
//					Pattern pattern = Pattern.compile(reg);
//					Matcher matcher;
//					String[] pid_results = new String[files.length]; 
//					for(int j = 0; j<files.length; j++){
//						if(!files[j].isDirectory()){
//							String filename = files[j].toString();
//							matcher = pattern.matcher(filename);
//							if(matcher.find()){
//								pid_results[j] = matcher.group();
//							}
//						}
//					}
					String[] plink = new String[matchResult.file_length];
					String[] link = new String[1];
					for(int i = 0; i<matchResult.file_length; i++){
						plink[i] = plink_root+matchResult.results[i];
					}
					link[0] = link_root;
					
					String smallPhoto_dir = matchResult.newRoot +"small\\";
					MatchResult MResult_small = null;
					MResult_small = getPhotoPath("0",smallPhoto_dir);
					
					String[] plinkSmall = new String[MResult_small.file_length];
					for(int i=0; i<MResult_small.file_length; i++){
						plinkSmall[i] = plink_root+"small/"+MResult_small.results[i];
					}
					
					String category = rs.getString(15);
					String rating = rs.getString(16);
					int pointid = rs.getInt(17);
					
					
					point = new Point(pointid, style, name, address, phone, price, description, location, timestart, timeend, plink, plinkSmall, link, category,rating);
				}
			}
			return point;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public ArrayList<Activity> getActivity(){
		try{
			ArrayList<Activity> results = new ArrayList<Activity>();
			Statement stmt = dbConn.createStatement();
			String check = "SELECT id, name, description, address, lowestprice, highestprice, startdate, enddate, starttime, endtime, location, category, weight, p.url, l.url, a.pid FROM activity a, links l, photos p WHERE enddate-current_date()>=0 and enddate-current_date()<8 and a.pid=p.pid and a.lid=l.lid order by enddate-current_date() desc";	
			if (stmt.execute(check)) {
				ResultSet rs = stmt.getResultSet();
				while (rs.next()) {
					int id = rs.getInt(1);
					String name = rs.getString(2);
					String description = rs.getString(3);
					String address = rs.getString(4);
					int lowestprice = rs.getInt(5);
					int hightestprice = rs.getInt(6);
					String startdate = rs.getString(7);
					String enddate = rs.getString(8);
					int starttime = rs.getInt(9);
					int endtime = rs.getInt(10);
					String plink_root = rs.getString(14);
					String link_root = rs.getString(15);
					
					//match photos
					String pid = rs.getString(16);
//					String photo_dir = "F:\\photo_backup\\"+pid+"\\";
					String photo_dir = dirOfPhotos+pid+"\\";
					File file = new File(photo_dir);
					File[] files = file.listFiles();
					Pattern pattern = Pattern.compile(reg);
					Matcher matcher;
					String[] pid_results = new String[files.length]; 
					for(int j = 0; j<files.length; j++){
						if(!files[j].isDirectory()){
							String filename = files[j].toString();
							matcher = pattern.matcher(filename);
							if(matcher.find()){
								pid_results[j] = matcher.group();
							}
						}
					}
					
					String[] plink = new String[files.length];
					String[] link = new String[1];
					for(int i = 0; i<files.length; i++){
						plink[i] = plink_root+pid_results[i];
					}
					link[0] = link_root;
					
					String location = rs.getString(11);
					String category = rs.getString(12);
					float weight = rs.getFloat(13);
					
					
					Activity activity = new Activity(id, name, description, address, lowestprice, hightestprice, startdate, enddate, starttime, endtime, location, category, weight, plink, link);
					results.add(activity);
				}
			}
			return results;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public ArrayList<InfoListId> getInfoListId(String style, String location, String time_start, String time_end, String category){
		try {
			ArrayList<InfoListId> results = new ArrayList<InfoListId>();
			Statement stmt = dbConn.createStatement();
			int time_s = Integer.valueOf(time_start);
			int time_e = Integer.valueOf(time_end);
			if(time_e > 0 && time_e <= 5){
				time_e += 24;
			}
			
			if(category.equals("default")){
				if(time_s < 9){
					selectBreakfastId(style, location, stmt, results);
					
					if(time_e > 9){
						selectMorningId(style, location, stmt, results);
					}
					if(time_e > 11){
						selectLunchId(style, location, stmt, results);
					}
					if(time_e > 14){
						selectAfternoonId(style, location, stmt, results);
					}
					if(time_e > 17){
						selectDinnerId(style, location, stmt, results);
					}
					if(time_e > 20){
						selectNightId(style, location, stmt, results);
					}
					if(time_e > 23){
						selectMidnightId(style, location, stmt, results);
					}
				} else if(time_s < 11){
					selectMorningId(style, location, stmt, results);
					
					if(time_e > 11){
						selectLunchId(style, location, stmt, results);
					}
					if(time_e > 14){
						selectAfternoonId(style, location, stmt, results);
					}
					if(time_e > 17){
						selectDinnerId(style, location, stmt, results);
					}
					if(time_e > 20){
						selectNightId(style, location, stmt, results);
					}
					if(time_e > 23){
						selectMidnightId(style, location, stmt, results);
					}
				} else if(time_s < 14){
					selectLunchId(style, location, stmt, results);
					
					if (time_e > 14) {
						selectAfternoonId(style, location, stmt, results);
					}
					if (time_e > 17) {
						selectDinnerId(style, location, stmt, results);
					}
					if (time_e > 20) {
						selectNightId(style, location, stmt, results);
					}
					if (time_e > 23) {
						selectMidnightId(style, location, stmt, results);
					}
				} else if(time_s < 17){
					selectAfternoonId(style, location, stmt, results);

					if (time_e > 17) {
						selectDinnerId(style, location, stmt, results);
					}
					if (time_e > 20) {
						selectNightId(style, location, stmt, results);
					}
					if (time_e > 23) {
						selectMidnightId(style, location, stmt, results);
					}
				} else if(time_s < 20){
					selectDinnerId(style, location, stmt, results);

					if (time_e > 20) {
						selectNightId(style, location, stmt, results);
					}
					if (time_e > 23) {
						selectMidnightId(style, location, stmt, results);
					}
				} else if(time_s < 23){
					selectNightId(style, location, stmt, results);

					if (time_e > 23) {
						selectMidnightId(style, location, stmt, results);
					}
				} else {
					selectMidnightId(style, location, stmt, results);
				}
			} else {
				if(time_s < 9){
					selectBreakfastId(style, location, category, stmt, results);
					
					if(time_e > 9){
						selectMorningId(style, location, category, stmt, results);
					}
					if(time_e > 11){
						selectLunchId(style, location, category, stmt, results);
					}
					if(time_e > 14){
						selectAfternoonId(style, location, category, stmt, results);
					}
					if(time_e > 17){
						selectDinnerId(style, location, category, stmt, results);
					}
					if(time_e > 20){
						selectNightId(style, location, category, stmt, results);
					}
					if(time_e > 23){
						selectMidnightId(style, location, category, stmt, results);
					}
				} else if(time_s < 11){
					selectMorningId(style, location, category, stmt, results);
					
					if(time_e > 11){
						selectLunchId(style, location, category, stmt, results);
					}
					if(time_e > 14){
						selectAfternoonId(style, location, category, stmt, results);
					}
					if(time_e > 17){
						selectDinnerId(style, location, category, stmt, results);
					}
					if(time_e > 20){
						selectNightId(style, location, category, stmt, results);
					}
					if(time_e > 23){
						selectMidnightId(style, location, category, stmt, results);
					}
				} else if(time_s < 14){
					selectLunchId(style, location, category, stmt, results);
					
					if (time_e > 14) {
						selectAfternoonId(style, location, category, stmt, results);
					}
					if (time_e > 17) {
						selectDinnerId(style, location, category, stmt, results);
					}
					if (time_e > 20) {
						selectNightId(style, location, category, stmt, results);
					}
					if (time_e > 23) {
						selectMidnightId(style, location, category, stmt, results);
					}
				} else if(time_s < 17){
					selectAfternoonId(style, location, category, stmt, results);

					if (time_e > 17) {
						selectDinnerId(style, location, category, stmt, results);
					}
					if (time_e > 20) {
						selectNightId(style, location, category, stmt, results);
					}
					if (time_e > 23) {
						selectMidnightId(style, location, category, stmt, results);
					}
				} else if(time_s < 20){
					selectDinnerId(style, location, category, stmt, results);

					if (time_e > 20) {
						selectNightId(style, location, category, stmt, results);
					}
					if (time_e > 23) {
						selectMidnightId(style, location, category, stmt, results);
					}
				} else if(time_s < 23){
					selectNightId(style, location, category, stmt, results);

					if (time_e > 23) {
						selectMidnightId(style, location, category, stmt, results);
					}
				} else {
					selectMidnightId(style, location, category, stmt, results);
				}
			}
			return results;
		} catch (SQLException e) {
			return null;
		}
	}
	
	public ArrayList<InfoList> getInfoList(String id1, String id2, String id3, String timeslot){
		try {
			ArrayList<InfoList> results = new ArrayList<InfoList>();
			Statement stmt = dbConn.createStatement();
			
			if(timeslot.equals("breakfast")){
				if(!id1.equals("")){
					selectBreakfast(id1, stmt, results);
				}
				if(!id2.equals("")){
					selectBreakfast(id2, stmt, results);
				}
				if(!id3.equals("")){
					selectBreakfast(id3, stmt, results);
				}
			} 
			if(timeslot.equals("morning")){
				if(!id1.equals("")){
					selectMorning(id1, stmt, results);
				}
				if(!id2.equals("")){
					selectMorning(id2, stmt, results);
				}
				if(!id3.equals("")){
					selectMorning(id3, stmt, results);
				}
			}
			if(timeslot.equals("lunch")){
				if(!id1.equals("")){
					selectLunch(id1, stmt, results);
				}
				if(!id2.equals("")){
					selectLunch(id2, stmt, results);
				}
				if(!id3.equals("")){
					selectLunch(id3, stmt, results);
				}
			}
			if(timeslot.equals("afternoon")){
				if(!id1.equals("")){
					selectAfternoon(id1, stmt, results);
				}
				if(!id2.equals("")){
					selectAfternoon(id2, stmt, results);
				}
				if(!id3.equals("")){
					selectAfternoon(id3, stmt, results);
				}
			}
			if(timeslot.equals("dinner")){
				if(!id1.equals("")){
					selectDinner(id1, stmt, results);
				}
				if(!id2.equals("")){
					selectDinner(id2, stmt, results);
				}
				if(!id3.equals("")){
					selectDinner(id3, stmt, results);
				}
			}
			if(timeslot.equals("night")){
				if(!id1.equals("")){
					selectNight(id1, stmt, results);
				}
				if(!id2.equals("")){
					selectNight(id2, stmt, results);
				}
				if(!id3.equals("")){
					selectNight(id3, stmt, results);
				}
			}
			if(timeslot.equals("midnight")){
				if(!id1.equals("")){
					selectMidnight(id1, stmt, results);
				}
				if(!id2.equals("")){
					selectMidnight(id2, stmt, results);
				}
				if(!id3.equals("")){
					selectMidnight(id3, stmt, results);
				}
			}			
			return results;
		} catch (SQLException e) {
			return null;
		}
	}
	
}


