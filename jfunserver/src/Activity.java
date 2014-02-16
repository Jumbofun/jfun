import java.util.Arrays;


public class Activity {
	private int id;	
	private String name;
	private String description;
	private String address;
	private int lowestprice;
	private int hightestprice;
	private String startdate;
	private String enddate;
	private int starttime;
	private int endtime;
	private String location;
	private String category;
	private float weight;
	private String[] plink;
	private String[] link;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getLowestprice() {
		return lowestprice;
	}
	public void setLowestprice(int lowestprice) {
		this.lowestprice = lowestprice;
	}
	public int getHightestprice() {
		return hightestprice;
	}
	public void setHightestprice(int hightestprice) {
		this.hightestprice = hightestprice;
	}
	public String getStartdate() {
		return startdate;
	}
	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public int getStarttime() {
		return starttime;
	}
	public void setStarttime(int starttime) {
		this.starttime = starttime;
	}
	public int getEndtime() {
		return endtime;
	}
	public void setEndtime(int endtime) {
		this.endtime = endtime;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public float getWeight() {
		return weight;
	}
	public void setWeight(float weight) {
		this.weight = weight;
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
	public Activity(int id, String name, String description, String address,
			int lowestprice, int hightestprice, String startdate,
			String enddate, int starttime, int endtime, String location,
			String category, float weight, String[] plink, String[] link) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.address = address;
		this.lowestprice = lowestprice;
		this.hightestprice = hightestprice;
		this.startdate = startdate;
		this.enddate = enddate;
		this.starttime = starttime;
		this.endtime = endtime;
		this.location = location;
		this.category = category;
		this.weight = weight;
		this.plink = plink;
		this.link = link;
	}
	@Override
	public String toString() {
		return "Activity [id=" + id + ", name=" + name + ", description="
				+ description + ", address=" + address + ", lowestprice="
				+ lowestprice + ", hightestprice=" + hightestprice
				+ ", startdate=" + startdate + ", enddate=" + enddate
				+ ", starttime=" + starttime + ", endtime=" + endtime
				+ ", location=" + location + ", category=" + category
				+ ", weight=" + weight + ", plink=" + Arrays.toString(plink)
				+ ", link=" + Arrays.toString(link) + "]";
	}
	
	
	
}
