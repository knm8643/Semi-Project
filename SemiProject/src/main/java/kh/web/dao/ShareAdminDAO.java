package kh.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import kh.web.dto.NoticeReplyDTO;
import kh.web.dto.ShareBoardDTO;
import kh.web.dto.ShareBoardReplyDTO;
import kh.web.statics.Statics;

public class ShareAdminDAO {
	private static ShareAdminDAO instance = null;
	public static ShareAdminDAO getInstance() {
		if(instance == null) {
			instance = new ShareAdminDAO();
		}
		return instance;
	}
	private ShareAdminDAO() {}
	//Singleton 관련 코드
	
	private Connection getConnection() throws Exception{
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/oracle");
		return ds.getConnection();
	}
	 // JNDI 관련 코드
	
	public List<ShareBoardDTO> selectByBound(int start, int end) throws Exception{  // DB에 저장된 공지사항 글 5개씩 조회
		String sql = "select * from (select shareboard.*, row_number() over(order by sh_seq desc) rn from shareboard) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			
			pstat.setInt(1, start);
			pstat.setInt(2, end);
			try(ResultSet rs = pstat.executeQuery();){
				List<ShareBoardDTO> list = new ArrayList<>();
				while(rs.next()) {
					ShareBoardDTO dto = new ShareBoardDTO();
					dto.setSh_seq(rs.getInt("sh_seq"));
					dto.setSh_writer(rs.getString("sh_writer"));
					dto.setSh_title(rs.getString("sh_title"));
					dto.setSh_contents(rs.getString("sh_contents"));
					dto.setSh_writedate(rs.getTimestamp("sh_writedate"));
					dto.setSh_viewcount(rs.getInt("sh_viewcount"));
					list.add(dto);
				}
				return list;
			}
		}
	}
	
	private int getRecordCount() throws Exception{ // DB에 저장된 게시글 수 구하는 기능. 우리만 쓰는 기능이라 정보은닉
	String sql = "select count(*) from shareboard";
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
	if(needPrev) {pageNavi += "<a href='/share.admin?cpage="+(startNavi-1)+"'><</a> ";}
	for(int i = startNavi;i <= endNavi; i++) {
		pageNavi+="<a href='/share.admin?cpage="+i+"'>" + i+"</a> ";
	}
	if(needNext) {pageNavi += "<a href='/share.admin?cpage="+(endNavi+1)+"'>></a>";}
	return pageNavi;
}
	
	public ShareBoardDTO selectBySeq(int seq) throws Exception{  // DB에 저장된 공지사항 글 seq로 검색
		String sql = "select * from shareboard where sh_seq=?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setInt(1, seq);
			try(ResultSet rs = pstat.executeQuery();){
				if(rs.next()) {
					ShareBoardDTO dto = new ShareBoardDTO();
					dto.setSh_seq(rs.getInt("sh_seq"));
					dto.setSh_writer(rs.getString("sh_writer"));
					dto.setSh_title(rs.getString("sh_title"));
					dto.setSh_contents(rs.getString("sh_contents"));
					dto.setSh_writedate(rs.getTimestamp("sh_writedate"));
					dto.setSh_viewcount(rs.getInt("sh_viewcount"));
					return dto;
				}
			}
		return null;
		}
	}
	public int modify(int seq, String title, String contents) throws Exception{  // 공지사항 글 수정
	String sql = "update shareboard set sh_title = ?, sh_contents = ? where sh_seq = ?";
	try(Connection con = this.getConnection();
		PreparedStatement pstat = con.prepareStatement(sql)){
		pstat.setString(1, title);
		pstat.setString(2, contents);
		pstat.setInt(3, seq);
		int result = pstat.executeUpdate();
		con.commit();
		return result;
	}
}
	
	public int delete(int seq) throws Exception{  // DB에 저장된 공지사항 글 삭제
	String sql = "delete from shareboard where sh_seq = ?";
	try(Connection con = this.getConnection();
		PreparedStatement pstat = con.prepareStatement(sql)){
		pstat.setInt(1, seq);
		int result = pstat.executeUpdate();
		con.commit();
		return result;
	}
}
	
	public int deleteCommentByPseq(int pseq) throws Exception{  // 게시판 글 삭제시 같이 달려있던 댓글 삭제
	String sql = "delete from sh_reply where sh_replypseq = ?";
	try(Connection con = this.getConnection();
		PreparedStatement pstat = con.prepareStatement(sql)){
		pstat.setInt(1, pseq);
		int result = pstat.executeUpdate();
		System.out.println(result);
		con.commit();
		return result;
	}
}
	private int getRecordCountBySearch(String option, String target) throws Exception{
	String sql = "select count(*) from shareboard where "+option+ " like ?";
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
		if(recordTotalCount % Statics.RECORD_COUNT_PER_PAGE == 0) {
			pageTotalCount = recordTotalCount / Statics.RECORD_COUNT_PER_PAGE;
		}else {
			pageTotalCount = recordTotalCount / Statics.RECORD_COUNT_PER_PAGE + 1;
		}
		return pageTotalCount;
	}	
	
	public List<ShareBoardDTO> selectBySearch(String option, String target, int start, int end) throws Exception{
		String sql = "select * from (select shareboard.*, row_number() over(order by sh_seq desc) rn from shareboard where "+option+" like ?) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, "%"+target+"%");
			pstat.setInt(2, start);
			pstat.setInt(3, end);
			try(ResultSet rs = pstat.executeQuery()){
				List<ShareBoardDTO> list = new ArrayList<>();
				while(rs.next()) {
					ShareBoardDTO dto = new ShareBoardDTO();
					dto.setSh_seq(rs.getInt("sh_seq"));
					dto.setSh_writer(rs.getString("sh_writer"));
					dto.setSh_title(rs.getString("sh_title"));
					dto.setSh_contents(rs.getString("sh_contents"));
					dto.setSh_writedate(rs.getTimestamp("sh_writedate"));
					dto.setSh_viewcount(rs.getInt("sh_viewcount"));
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
		if(needPrev) {pageNavi += "<a href='/searchShare.admin?target="+target+"&option="+option+"&cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/searchShare.admin?target="+target+"&option="+option+"&cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/searchShare.admin?target="+target+"&option="+option+"&cpage="+(endNavi+1)+"'>></a>";}
		
		return pageNavi;
	}
	
	public List<ShareBoardReplyDTO> selectReplyByBound(int start, int end) throws Exception{  // DB에 저장된 공지사항 글 5개씩 조회
		String sql = "select * from (select sh_reply.*, row_number() over(order by sh_replyseq desc) rn from sh_reply) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			
			pstat.setInt(1, start);
			pstat.setInt(2, end);
			try(ResultSet rs = pstat.executeQuery();){
				List<ShareBoardReplyDTO> list = new ArrayList<>();
				while(rs.next()) {
					ShareBoardReplyDTO dto = new ShareBoardReplyDTO();
					dto.setSh_replyseq(rs.getInt("sh_replyseq"));
					dto.setSh_replypseq(rs.getInt("sh_replypseq"));
					dto.setSh_replyer(rs.getString("sh_replyer"));
					dto.setSh_replytext(rs.getString("sh_replytext"));
					dto.setSh_replydate(rs.getTimestamp("sh_replydate"));
					list.add(dto);
				}
				return list;
			}
		}
	}
	
	private int getReplyRecordCount() throws Exception{ // DB에 저장된 게시글 수 구하는 기능. 우리만 쓰는 기능이라 정보은닉
		String sql = "select count(*) from sh_reply";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);
			ResultSet rs = pstat.executeQuery();){
			rs.next();
			return rs.getInt(1);
		}
	}
	public String getReplyPageNaviAdmin(int currentPage) throws Exception{
		int recordTotalCount = this.getReplyRecordCount(); // 총 몇개의 레코드(게시글)을 가지고 있는지
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
		if(needPrev) {pageNavi += "<a href='/shareReply.admin?cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/shareReply.admin?cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/shareReply.admin?cpage="+(endNavi+1)+"'>></a>";}
		return pageNavi;
	}
	
	public int deleteComment(int seq) throws Exception{  // DB에 저장된 공지사항 글 삭제
		String sql = "delete from sh_reply where sh_replyseq = ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setInt(1, seq);
			int result = pstat.executeUpdate();
			System.out.println(result);
			con.commit();
			return result;
		}
	}
	private int getRecordCountBySearchReply(String option, String target) throws Exception{
		String sql = "select count(*) from sh_reply where "+option+ " like ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, "%"+target+"%");
			try(ResultSet rs = pstat.executeQuery();){
				rs.next();
				return rs.getInt(1);
			}
		}
	}
	
	public int getPageTotalCountBySearchReply(String option, String title) throws Exception{
	
		// 총 몇개의 레코드(게시글)를 가지고 있는지
		int recordTotalCount = this.getRecordCountBySearchReply(option, title);
		
		int pageTotalCount = 0;
		// 총 몇개의 페이지가 만들어질 것인지
		if(recordTotalCount % Statics.RECORD_COUNT_PER_PAGE == 0) {
			pageTotalCount = recordTotalCount / Statics.RECORD_COUNT_PER_PAGE;
		}else {
			pageTotalCount = recordTotalCount / Statics.RECORD_COUNT_PER_PAGE + 1;
		}
		return pageTotalCount;
	}
	
	public List<ShareBoardReplyDTO> selectBySearchReply(String option, String target, int start, int end) throws Exception{
		String sql = "select * from (select sh_reply.*, row_number() over(order by sh_replyseq desc) rn from sh_reply where "+option+" like ?) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, "%"+target+"%");
			pstat.setInt(2, start);
			pstat.setInt(3, end);
			try(ResultSet rs = pstat.executeQuery()){
				List<ShareBoardReplyDTO> list = new ArrayList<>();
				while(rs.next()) {
					ShareBoardReplyDTO dto = new ShareBoardReplyDTO();
					dto.setSh_replyseq(rs.getInt("sh_replyseq"));
					dto.setSh_replypseq(rs.getInt("sh_replypseq"));
					dto.setSh_replyer(rs.getString("sh_replyer"));
					dto.setSh_replytext(rs.getString("sh_replytext"));
					dto.setSh_replydate(rs.getTimestamp("sh_replydate"));
					list.add(dto);
				}
				return list;
			}
		}	
	}
	
	public String getPageNaviAdminBySearchReply(int currentPage, String target, String option) throws Exception{
		int recordTotalCount = this.getRecordCountBySearchReply(option, target); // 총 몇개의 레코드(게시글)을 가지고 있는지
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
		if(needPrev) {pageNavi += "<a href='/searchShareReply.admin?target="+target+"&option="+option+"&cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/searchShareReply.admin?target="+target+"&option="+option+"&cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/searchShareReply.admin?target="+target+"&option="+option+"&cpage="+(endNavi+1)+"'>></a>";}
		
		return pageNavi;
	}
}
