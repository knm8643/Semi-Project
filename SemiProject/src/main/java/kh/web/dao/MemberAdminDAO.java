package kh.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import kh.web.dto.MemberAdminDTO;
import kh.web.statics.Statics;

public class MemberAdminDAO {
	private static MemberAdminDAO instance = null;
	public static MemberAdminDAO getInstance() {
		if(instance == null) {
			instance = new MemberAdminDAO();
		}
		return instance;
	}
	private MemberAdminDAO() {}
	//Singleton 관련 코드
	
	private Connection getConnection() throws Exception{
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/oracle");
		return ds.getConnection();
	}
	 // JNDI 관련 코드
	
	public List<MemberAdminDTO> selectByBound(int start, int end) throws Exception{  // DB에 저장된 공지사항 글 5개씩 조회
		String sql = "select * from (select tbl_member.*, row_number() over(order by member_id desc) rn from tbl_member) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			
			pstat.setInt(1, start);
			pstat.setInt(2, end);
			try(ResultSet rs = pstat.executeQuery();){
				List<MemberAdminDTO> list = new ArrayList<>();
				while(rs.next()) {
					MemberAdminDTO dto = new MemberAdminDTO();
					dto.setMember_id(rs.getString("member_id"));
					dto.setMember_pw(rs.getString("member_pw"));
					dto.setLogin_id(rs.getString("login_id"));
					dto.setMember_name(rs.getString("member_name"));
					dto.setMember_nickname(rs.getString("member_nickname"));
					dto.setMember_zipcode(rs.getString("member_zipcode"));
					dto.setMember_address1(rs.getString("member_address1"));
					dto.setMember_address2(rs.getString("member_address2"));
					dto.setMember_ssn(rs.getString("member_ssn"));
					dto.setMember_phone(rs.getString("member_phone"));
					dto.setMember_email(rs.getString("member_email"));
					dto.setAdmin_yn(rs.getString("admin_yn"));
					dto.setAvgAge(rs.getString("Avg_age"));
					dto.setKakao_login_yn(rs.getString("kakao_login_yn"));
					dto.setBlacklist(rs.getString("blacklist"));
					list.add(dto);
				}
				return list;
			}
		}
	}
	
	private int getRecordCount() throws Exception{ // DB에 저장된 게시글 수 구하는 기능. 우리만 쓰는 기능이라 정보은닉
	String sql = "select count(*) from tbl_member";
	try(Connection con = this.getConnection();
		PreparedStatement pstat = con.prepareStatement(sql);
		ResultSet rs = pstat.executeQuery();){
		rs.next();
		return rs.getInt(1);
	}
}
	
	public String getPageNaviAdmin(int currentPage) throws Exception{
		int recordTotalCount = this.getRecordCount(); // 총 몇개의 레코드(게시글)을 가지고 있는지
		int recordCountPerPage = Statics.RECORD_COUNT_PER_PAGE_ADMIN; // 페이지 당 글의 개수
		int naviCountPerPage = Statics.NAVI_COUNT_PER_PAGE_ADMIN; // 페이지 네비게이터에 한 번에 표시될 페이지 수
		
		int pageTotalCount = 0; //페이지 수를 저장할 변수
		
		if(recordTotalCount % recordCountPerPage == 0) {
			pageTotalCount = recordTotalCount/recordCountPerPage; //  딱 떨어질 때
		}else {
			pageTotalCount = recordTotalCount/recordCountPerPage + 1; // 딱 떨어지지 않을 때
		}
		
		if(currentPage < 1) {
			currentPage = 1;
		} // 페이지 1보다 작을 때 1로 만들어주는 보완코드
		
		int startNavi = (currentPage - 1) / naviCountPerPage * naviCountPerPage + 1;
		int endNavi = startNavi + naviCountPerPage - 1;
		
		if(endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		} // 페이지 네비게이터 마지막 숫자가 총 페이지 숫자보다 클 때 맞춰주는 보완코드
		
		boolean needPrev = true;
		boolean needNext = true;
		
		if(startNavi == 1) {needPrev = false;}
		if(endNavi == pageTotalCount) {needNext = false;}
		
		String pageNavi = "";
		if(needPrev) {pageNavi += "<a href='/member.admin?cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/member.admin?cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/member.admin?cpage="+(endNavi+1)+"'>></a>";}
		return pageNavi;
	}
	
	public int delete(String member_id) throws Exception{  // DB에 저장된 공지사항 글 삭제
		String sql = "delete from tbl_member where member_id = ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setString(1, member_id);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
		
	private int getRecordCountBySearch(String option, String target) throws Exception{
		String sql = "select count(*) from tbl_member where "+option+ " like ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, "%"+target+"%");
			try(ResultSet rs = pstat.executeQuery();){
				rs.next();
				return rs.getInt(1);
			}
		}
	}
	
	public int getPageTotalCountBySearch(String option, String title) throws Exception{
		
		// 총 몇개의 레코드(게시글)를 가지고 있는지
		int recordTotalCount = this.getRecordCountBySearch(option, title);
	
		int pageTotalCount = 0;
		// 총 몇개의 페이지가 만들어질 것인지
		if(recordTotalCount % Statics.RECORD_COUNT_PER_PAGE_ADMIN == 0) {
			pageTotalCount = recordTotalCount / Statics.RECORD_COUNT_PER_PAGE_ADMIN;
		}else {
			pageTotalCount = recordTotalCount / Statics.RECORD_COUNT_PER_PAGE_ADMIN + 1;
		}
		return pageTotalCount;
	}
	
	public List<MemberAdminDTO> selectBySearch(String option, String target, int start, int end) throws Exception{
		String sql = "select * from (select tbl_member.*, row_number() over(order by member_id desc) rn from tbl_member where "+option+" like ?) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, "%"+target+"%");
			pstat.setInt(2, start);
			pstat.setInt(3, end);
			try(ResultSet rs = pstat.executeQuery()){
				List<MemberAdminDTO> list = new ArrayList<>();
				while(rs.next()) {
					MemberAdminDTO dto = new MemberAdminDTO();
					dto.setMember_id(rs.getString("member_id"));
					dto.setMember_pw(rs.getString("member_pw"));
					dto.setLogin_id(rs.getString("login_id"));
					dto.setMember_name(rs.getString("member_name"));
					dto.setMember_nickname(rs.getString("member_nickname"));
					dto.setMember_zipcode(rs.getString("member_zipcode"));
					dto.setMember_address1(rs.getString("member_address1"));
					dto.setMember_address2(rs.getString("member_address2"));
					dto.setMember_ssn(rs.getString("member_ssn"));
					dto.setMember_phone(rs.getString("member_phone"));
					dto.setMember_email(rs.getString("member_email"));
					dto.setAdmin_yn(rs.getString("admin_yn"));
					dto.setAvgAge(rs.getString("Avg_age"));
					dto.setKakao_login_yn(rs.getString("kakao_login_yn"));
					dto.setBlacklist(rs.getString("blacklist"));
					list.add(dto);
				}
				return list;
			}
		}
	}
		
	public String getPageNaviAdminBySearch(int currentPage, String target, String option) throws Exception{
		int recordTotalCount = this.getRecordCountBySearch(option, target); // 총 몇개의 레코드(게시글)을 가지고 있는지
		int recordCountPerPage = Statics.RECORD_COUNT_PER_PAGE_ADMIN; // 페이지 당 글의 개수
		int naviCountPerPage = Statics.NAVI_COUNT_PER_PAGE_ADMIN; // 페이지 네비게이터에 한 번에 표시될 페이지 수
		
		int pageTotalCount = 0; //페이지 수를 저장할 변수
		
		if(recordTotalCount % recordCountPerPage == 0) {
			pageTotalCount = recordTotalCount/recordCountPerPage; // 5로 딱 떨어질 때
		}else {
			pageTotalCount = recordTotalCount/recordCountPerPage + 1; // 5로 딱 떨어지지 않을 때
		}
		
		if(currentPage < 1) {
			currentPage = 1;
		} // 페이지 1보다 작을 때 1로 만들어주는 보완코드
		
		int startNavi = (currentPage - 1) / naviCountPerPage * naviCountPerPage + 1;
		int endNavi = startNavi + naviCountPerPage - 1;
		
		if(endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		} // 페이지 네비게이터 마지막 숫자가 총 페이지 숫자보다 클 때 맞춰주는 보완코드
		
		boolean needPrev = true;
		boolean needNext = true;
		
		if(startNavi == 1) {needPrev = false;}
		if(endNavi == pageTotalCount) {needNext = false;}
		
		String pageNavi = "";
		if(needPrev) {pageNavi += "<a href='/searchMember.admin?target="+target+"&option="+option+"&cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/searchMember.admin?target="+target+"&option="+option+"&cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/searchMember.admin?target="+target+"&option="+option+"&cpage="+(endNavi+1)+"'>></a>";}
		
		return pageNavi;
	}
	
	   public int blacklist(String member_id, String yn) throws Exception {
		      String sql = "update tbl_member set blacklist = ? where member_id = ?";

		      try (Connection con = this.getConnection(); 
		         PreparedStatement pstat = con.prepareStatement(sql);) {
		         pstat.setString(1, yn);
		         pstat.setString(2, member_id);
		         int result = pstat.executeUpdate();
		         System.out.println(result);
		         con.commit();
		         return result;
		      }
		   }
}
