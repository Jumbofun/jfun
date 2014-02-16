import java.util.Arrays;


public class Route {
	private int rid;
	private int[] id;
	private int night;
	private float good;
	private float bad;
	private String description;
	private String style;
	private String title;
	private String hot;
	private String[] plink;
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public int[] getId() {
		return id;
	}
	public void setId(int[] id) {
		this.id = id;
	}
	public int getNight() {
		return night;
	}
	public void setNight(int night) {
		this.night = night;
	}
	public float getGood() {
		return good;
	}
	public void setGood(float good) {
		this.good = good;
	}
	public float getBad() {
		return bad;
	}
	public void setBad(float bad) {
		this.bad = bad;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getHot() {
		return hot;
	}
	public void setHot(String hot) {
		this.hot = hot;
	}
	public String[] getPlink() {
		return plink;
	}
	public void setPlink(String[] plink) {
		this.plink = plink;
	}
	public Route(int rid, int[] id, int night, float good, float bad,
			String description, String style, String title, String hot,
			String[] plink) {
		super();
		this.rid = rid;
		this.id = id;
		this.night = night;
		this.good = good;
		this.bad = bad;
		this.description = description;
		this.style = style;
		this.title = title;
		this.hot = hot;
		this.plink = plink;
	}
	@Override
	public String toString() {
		return "Route [rid=" + rid + ", id=" + Arrays.toString(id) + ", night="
				+ night + ", good=" + good + ", bad=" + bad + ", description="
				+ description + ", style=" + style + ", title=" + title
				+ ", hot=" + hot + ", plink=" + Arrays.toString(plink) + "]";
	}
	
	
}
