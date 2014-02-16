
public class InfoListId {
	private int id;
	private String category;
	private String timeslot;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTimeslot() {
		return timeslot;
	}
	public void setTimeslot(String timeslot) {
		this.timeslot = timeslot;
	}
	
	public InfoListId(int id, String category, String timeslot) {
		super();
		this.id = id;
		this.category = category;
		this.timeslot = timeslot;
	}
	@Override
	public String toString() {
		return "InfoListId [id=" + id + ", category=" + category
				+ ", timeslot=" + timeslot + "]";
	}	
}
