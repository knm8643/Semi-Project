package kh.web.dto;

public class MemberAdminDTO {
	private String member_id;
	private String member_pw;
	private String login_id;
	private String member_name;
	private String member_nickname;
	private String member_zipcode;
	private String member_address1;
	private String member_address2;
	private String member_ssn;
	private String member_phone;
	private String member_email;
	private String admin_yn;
	private String avgAge;
	private String kakao_login_yn;
	private String blacklist;
	public MemberAdminDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public MemberAdminDTO(String member_id, String member_pw, String login_id, String member_name,
			String member_nickname, String member_zipcode, String member_address1, String member_address2,
			String member_ssn, String member_phone, String member_email, String admin_yn, String avgAge,
			String kakao_login_yn, String blacklist) {
		super();
		this.member_id = member_id;
		this.member_pw = member_pw;
		this.login_id = login_id;
		this.member_name = member_name;
		this.member_nickname = member_nickname;
		this.member_zipcode = member_zipcode;
		this.member_address1 = member_address1;
		this.member_address2 = member_address2;
		this.member_ssn = member_ssn;
		this.member_phone = member_phone;
		this.member_email = member_email;
		this.admin_yn = admin_yn;
		this.avgAge = avgAge;
		this.kakao_login_yn = kakao_login_yn;
		this.blacklist = blacklist;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMember_pw() {
		return member_pw;
	}
	public void setMember_pw(String member_pw) {
		this.member_pw = member_pw;
	}
	public String getLogin_id() {
		return login_id;
	}
	public void setLogin_id(String login_id) {
		this.login_id = login_id;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public String getMember_nickname() {
		return member_nickname;
	}
	public void setMember_nickname(String member_nickname) {
		this.member_nickname = member_nickname;
	}
	public String getMember_zipcode() {
		return member_zipcode;
	}
	public void setMember_zipcode(String member_zipcode) {
		this.member_zipcode = member_zipcode;
	}
	public String getMember_address1() {
		return member_address1;
	}
	public void setMember_address1(String member_address1) {
		this.member_address1 = member_address1;
	}
	public String getMember_address2() {
		return member_address2;
	}
	public void setMember_address2(String member_address2) {
		this.member_address2 = member_address2;
	}
	public String getMember_ssn() {
		return member_ssn;
	}
	public void setMember_ssn(String member_ssn) {
		this.member_ssn = member_ssn;
	}
	public String getMember_phone() {
		return member_phone;
	}
	public void setMember_phone(String member_phone) {
		this.member_phone = member_phone;
	}
	public String getMember_email() {
		return member_email;
	}
	public void setMember_email(String member_email) {
		this.member_email = member_email;
	}
	public String getAdmin_yn() {
		return admin_yn;
	}
	public void setAdmin_yn(String admin_yn) {
		this.admin_yn = admin_yn;
	}
	public String getAvgAge() {
		return avgAge;
	}
	public void setAvgAge(String avgAge) {
		this.avgAge = avgAge;
	}
	public String getKakao_login_yn() {
		return kakao_login_yn;
	}
	public void setKakao_login_yn(String kakao_login_yn) {
		this.kakao_login_yn = kakao_login_yn;
	}
	public String getBlacklist() {
		return blacklist;
	}
	public void setBlacklist(String blacklist) {
		this.blacklist = blacklist;
	}
}
