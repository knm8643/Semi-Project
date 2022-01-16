package kh.web.dto;

public class Map_DTO {
	
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
	public String getAddress_name() {
		return address_name;
	}
	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	private int id;
    private String name;
    private String address_name;
    private String tel;
	private String user_name;
    
	public Map_DTO() {}
	public Map_DTO(int id, String name, String address_name, String tel, String user_name) {
		super();
		this.id = id;
		this.name = name;
		this.address_name = address_name;
		this.tel = tel;
		this.user_name = user_name;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
}
