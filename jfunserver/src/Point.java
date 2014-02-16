import java.util.Arrays;


public class Point {
	private int id;
	private int style;
	private String name;
	private String address;
	private String phone;
	private int price;
	private String description;
	private String location;
	private int timestart;
	private int timeend;
	private String[] plink;
	private String[] plinkSmall;
	private String[] link;
	private String category;
	private String rating;
	
	
	public String[] getPlinkSmall() {
		return plinkSmall;
	}
	public void setPlinkSmall(String[] plinkSmall) {
		this.plinkSmall = plinkSmall;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStyle() {
		return style;
	}
	public void setStyle(int style) {
		this.style = style;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getTimestart() {
		return timestart;
	}
	public void setTimestart(int timestart) {
		this.timestart = timestart;
	}
	public int getTimeend() {
		return timeend;
	}
	public void setTimeend(int timeend) {
		this.timeend = timeend;
	}
	public String[] getPlink() {
		return plink;
	}
	public void setPlink(String[] plink) {
		this.plink = plink;
	}
	public String[] getLink() {
		return link;
	}
	public void setLink(String[] link) {
		this.link = link;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public Point(int id, int style, String name, String address, String phone,
			int price, String description, String location, int timestart,
			int timeend, String[] plink, String[] plinkSmall, String[] link,
			String category, String rating) {
		super();
		this.id = id;
		this.style = style;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.price = price;
		this.description = description;
		this.location = location;
		this.timestart = timestart;
		this.timeend = timeend;
		this.plink = plink;
		this.plinkSmall = plinkSmall;
		this.link = link;
		this.category = category;
		this.rating = rating;
	}
	@Override
	public String toString() {
		return "Point [id=" + id + ", style=" + style + ", name=" + name
				+ ", address=" + address + ", phone=" + phone + ", price="
				+ price + ", description=" + description + ", location="
				+ location + ", timestart=" + timestart + ", timeend="
				+ timeend + ", plink=" + Arrays.toString(plink)
				+ ", plinkSmall=" + Arrays.toString(plinkSmall) + ", link="
				+ Arrays.toString(link) + ", category=" + category
				+ ", rating=" + rating + "]";
	}
	
	
	
	
	
	
	
}
