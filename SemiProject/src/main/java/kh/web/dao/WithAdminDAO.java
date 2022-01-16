package kh.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import kh.web.dto.RecipeBoardReplyDTO;
import kh.web.dto.WithBoardDTO;
import kh.web.dto.WithBoardReplyDTO;
import kh.web.statics.Statics;

public class WithAdminDAO {
	private static WithAdminDAO instance = null;
	public static WithAdminDAO getInstance() {
		if(instance == null) {
			instance = new WithAdminDAO();
		}
		return instance;
	}
	private WithAdminDAO() {}
	//Singleton 관련 코드
	
	private Connection getConnection() throws Exception{
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/oracle");
		return ds.getConnection();
	}
	 // JNDI 관련 코드
	
	public List<WithBoardDTO> selectByBound(int start, int end) throws Exception{  // DB에 저장된 공지사항 글 5개씩 조회
		String sql = "select * from (select withboard.*, row_number() over(order by with_seq desc) rn from withboard) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			
			pstat.setInt(1, start);
			pstat.setInt(2, end);
			try(ResultSet rs = pstat.executeQuery();){
				List<WithBoardDTO> list = new ArrayList<>();
				while(rs.next()) {
					WithBoardDTO dto = new WithBoardDTO();
					dto.setWith_seq(rs.getInt("with_seq"));
					dto.setWith_writer(rs.getString("with_writer"));
					dto.setWith_title(rs.getString("with_title"));
					dto.setWith_contents(rs.getString("with_contents"));
					dto.setWith_writedate(rs.getTimestamp("with_writedate"));
					dto.setWith_viewcount(rs.getInt("with_viewcount"));
					list.add(dto);
				}
				return list;
			}
		}
	}
	
	private int getRecordCount() throws Exception{ // DB에 저장된 게시글 수 구하는 기능. 우리만 쓰는 기능이라 정보은닉
	String sql = "select count(*) from withboard";
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
		if(needPrev) {pageNavi += "<a href='/with.admin?cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/with.admin?cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/with.admin?cpage="+(endNavi+1)+"'>></a>";}
		return pageNavi;
	}
	
	public WithBoardDTO selectBySeq(int seq) throws Exception{  // DB에 저장된 공지사항 글 seq로 검색
		String sql = "select * from withboard where with_seq=?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setInt(1, seq);
			try(ResultSet rs = pstat.executeQuery();){
				if(rs.next()) {
					WithBoardDTO dto = new WithBoardDTO();
					dto.setWith_seq(rs.getInt("with_seq"));
					dto.setWith_writer(rs.getString("with_writer"));
					dto.setWith_title(rs.getString("with_title"));
					dto.setWith_contents(rs.getString("with_contents"));
					dto.setWith_writedate(rs.getTimestamp("with_writedate"));
					dto.setWith_viewcount(rs.getInt("with_viewcount"));
					return dto;
				}
			}
		return null;
		}
	}
	public int modify(int seq, String title, String contents) throws Exception{  // 공지사항 글 수정
	String sql = "update withboard set with_title = ?, with_contents = ? where with_seq = ?";
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
	String sql = "delete from withboard where with_seq = ?";
	try(Connection con = this.getConnection();
		PreparedStatement pstat = con.prepareStatement(sql)){
		pstat.setInt(1, seq);
		int result = pstat.executeUpdate();
		con.commit();
		return result;
	}
}
	
	public int deleteCommentByPseq(int pseq) throws Exception{  // 게시판 글 삭제시 같이 달려있던 댓글 삭제
	String sql = "delete from with_reply where with_replypseq = ?";
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
	String sql = "select count(*) from withboard where "+option+ " like ?";
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
	
	public List<WithBoardDTO> selectBySearch(String option, String target, int start, int end) throws Exception{
		String sql = "select * from (select withboard.*, row_number() over(order by with_seq desc) rn from withboard where "+option+" like ?) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, "%"+target+"%");
			pstat.setInt(2, start);
			pstat.setInt(3, end);
			try(ResultSet rs = pstat.executeQuery()){
				List<WithBoardDTO> list = new ArrayList<>();
				while(rs.next()) {
					WithBoardDTO dto = new WithBoardDTO();
					dto.setWith_seq(rs.getInt("with_seq"));
					dto.setWith_writer(rs.getString("with_writer"));
					dto.setWith_title(rs.getString("with_title"));
					dto.setWith_contents(rs.getString("with_contents"));
					dto.setWith_writedate(rs.getTimestamp("with_writedate"));
					dto.setWith_viewcount(rs.getInt("with_viewcount"));
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
		if(needPrev) {pageNavi += "<a href='/searchWith.admin?target="+target+"&option="+option+"&cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/searchWith.admin?target="+target+"&option="+option+"&cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/searchWith.admin?target="+target+"&option="+option+"&cpage="+(endNavi+1)+"'>></a>";}
		
		return pageNavi;
	}
	
	public List<WithBoardReplyDTO> selectReplyByBound(int start, int end) throws Exception{  // DB에 저장된 공지사항 글 5개씩 조회
		String sql = "select * from (select with_reply.*, row_number() over(order by with_replyseq desc) rn from with_reply) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			
			pstat.setInt(1, start);
			pstat.setInt(2, end);
			try(ResultSet rs = pstat.executeQuery();){
				List<WithBoardReplyDTO> list = new ArrayList<>();
				while(rs.next()) {
					WithBoardReplyDTO dto = new WithBoardReplyDTO();
					dto.setWith_replyseq(rs.getInt("with_replyseq"));
					dto.setWith_replypseq(rs.getInt("with_replypseq"));
					dto.setWith_replyer(rs.getString("with_replyer"));
					dto.setWith_replytext(rs.getString("with_replytext"));
					dto.setWith_replydate(rs.getTimestamp("with_replydate"));
					list.add(dto);
				}
				return list;
			}
		}
	}
	
	private int getReplyRecordCount() throws Exception{ // DB에 저장된 게시글 수 구하는 기능. 우리만 쓰는 기능이라 정보은닉
		String sql = "select count(*) from with_reply";
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
		if(needPrev) {pageNavi += "<a href='/withReply.admin?cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/withReply.admin?cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/withReply.admin?cpage="+(endNavi+1)+"'>></a>";}
		return pageNavi;
	}
	
	public int deleteComment(int seq) throws Exception{  // DB에 저장된 공지사항 글 삭제
		String sql = "delete from with_reply where with_replyseq = ?";
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
		String sql = "select count(*) from with_reply where "+option+ " like ?";
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
	
	public List<WithBoardReplyDTO> selectBySearchReply(String option, String target, int start, int end) throws Exception{
		String sql = "select * from (select with_reply.*, row_number() over(order by with_replyseq desc) rn from with_reply where "+option+" like ?) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, "%"+target+"%");
			pstat.setInt(2, start);
			pstat.setInt(3, end);
			try(ResultSet rs = pstat.executeQuery()){
				List<WithBoardReplyDTO> list = new ArrayList<>();
				while(rs.next()) {
					WithBoardReplyDTO dto = new WithBoardReplyDTO();
					dto.setWith_replyseq(rs.getInt("with_replyseq"));
					dto.setWith_replypseq(rs.getInt("with_replypseq"));
					dto.setWith_replyer(rs.getString("with_replyer"));
					dto.setWith_replytext(rs.getString("with_replytext"));
					dto.setWith_replydate(rs.getTimestamp("with_replydate"));
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
		if(needPrev) {pageNavi += "<a href='/withRecipeReply.admin?target="+target+"&option="+option+"&cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/withRecipeReply.admin?target="+target+"&option="+option+"&cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/withRecipeReply.admin?target="+target+"&option="+option+"&cpage="+(endNavi+1)+"'>></a>";}
		
		return pageNavi;
	}
}
