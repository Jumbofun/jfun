import java.util.Arrays;


public class InfoList {
	private String name;	
	private String address;
	private String phone;
	private int price;
	private String description;
//	private String photo1;
	private String[] plink;
	private String[] link;
	private String lid1;
	private float rating;
	private String category;
	
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
//	public String getPhoto1() {
//		return photo1;
//	}
//	public void setPhoto1(String photo1) {
//		this.photo1 = photo1;
//	}
	
	public String[] getLink() {
		return link;
	}
	public void setLink(String[] link) {
		this.link = link;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String[] getPlink() {
		return plink;
	}
	public void setPlink(String[] plink) {
		this.plink = plink;
	}
	public String getLid1() {
		return lid1;
	}
	public void setLid1(String lid1) {
		this.lid1 = lid1;
	}
	public InfoList(String name, String address, String phone, int price,
			String description, String[] plink, String[] link,
			String lid1, float rating, String category) {
		super();
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.price = price;
		this.description = description;
//		this.photo1 = photo1;
		this.plink = plink;
		this.link = link;
		this.lid1 = lid1;
		this.rating = rating;
		this.category = category;
	}
	@Override
	public String toString() {
		return "InfoList [name=" + name + ", address=" + address + ", phone="
				+ phone + ", price=" + price + ", description=" + description
				+ ", plink=" + Arrays.toString(plink) + ", link="
				+ Arrays.toString(link) + ", lid1=" + lid1 + ", rating="
				+ rating + ", category=" + category + "]";
	}
	
	
	
	
}
