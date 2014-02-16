
public class UserInfo {
	private int uid;
	private String url;
	private String username;
	private String password;
	private String email;
	private String phone;
	private String sex;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public UserInfo(int uid, String url, String username, String password,
			String email, String phone, String sex) {
		super();
		this.uid = uid;
		this.url = url;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phone = phone;
		this.sex = sex;
	}
	
	@Override
	public String toString() {
		return "UserInfo [uid=" + uid + ", url=" + url + ", username="
				+ username + ", password=" + password + ", email=" + email
				+ ", phone=" + phone + ", sex=" + sex + "]";
	}
}
