package kh.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import kh.web.dto.RecipeBoardDTO;
import kh.web.dto.RecipeBoardReplyDTO;
import kh.web.dto.ShareBoardReplyDTO;
import kh.web.statics.Statics;

public class RecipeAdminDAO {
	private static RecipeAdminDAO instance = null;
	public static RecipeAdminDAO getInstance() {
		if(instance == null) {
			instance = new RecipeAdminDAO();
		}
		return instance;
	}
	private RecipeAdminDAO() {}
	//Singleton 관련 코드
	
	private Connection getConnection() throws Exception{
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/oracle");
		return ds.getConnection();
	}
	 // JNDI 관련 코드
	
	public List<RecipeBoardDTO> selectByBound(int start, int end) throws Exception{  // DB에 저장된 공지사항 글 5개씩 조회
		String sql = "select * from (select recipeboard.*, row_number() over(order by rc_seq desc) rn from recipeboard) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			
			pstat.setInt(1, start);
			pstat.setInt(2, end);
			try(ResultSet rs = pstat.executeQuery();){
				List<RecipeBoardDTO> list = new ArrayList<>();
				while(rs.next()) {
					RecipeBoardDTO dto = new RecipeBoardDTO();
					dto.setRc_seq(rs.getInt("rc_seq"));
					dto.setRc_writer(rs.getString("rc_writer"));
					dto.setRc_title(rs.getString("rc_title"));
					dto.setRc_contents(rs.getString("rc_contents"));
					dto.setRc_writedate(rs.getTimestamp("rc_writedate"));
					dto.setRc_viewcount(rs.getInt("rc_viewcount"));
					list.add(dto);
				}
				return list;
			}
		}
	}
	
	private int getRecordCount() throws Exception{ // DB에 저장된 게시글 수 구하는 기능. 우리만 쓰는 기능이라 정보은닉
	String sql = "select count(*) from recipeboard";
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
		if(needPrev) {pageNavi += "<a href='/recipe.admin?cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/recipe.admin?cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/recipe.admin?cpage="+(endNavi+1)+"'>></a>";}
		return pageNavi;
	}
	
	public RecipeBoardDTO selectBySeq(int seq) throws Exception{  // DB에 저장된 공지사항 글 seq로 검색
		String sql = "select * from recipeboard where rc_seq=?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setInt(1, seq);
			try(ResultSet rs = pstat.executeQuery();){
				if(rs.next()) {
					RecipeBoardDTO dto = new RecipeBoardDTO();
					dto.setRc_seq(rs.getInt("rc_seq"));
					dto.setRc_writer(rs.getString("rc_writer"));
					dto.setRc_title(rs.getString("rc_title"));
					dto.setRc_contents(rs.getString("rc_contents"));
					dto.setRc_writedate(rs.getTimestamp("rc_writedate"));
					dto.setRc_viewcount(rs.getInt("rc_viewcount"));
					return dto;
				}
			}
		return null;
		}
	}
	public int modify(int seq, String title, String contents) throws Exception{  // 공지사항 글 수정
	String sql = "update recipeboard set rc_title = ?, rc_contents = ? where rc_seq = ?";
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
	String sql = "delete from recipeboard where rc_seq = ?";
	try(Connection con = this.getConnection();
		PreparedStatement pstat = con.prepareStatement(sql)){
		pstat.setInt(1, seq);
		int result = pstat.executeUpdate();
		con.commit();
		return result;
	}
}
	
	public int deleteCommentByPseq(int pseq) throws Exception{  // 게시판 글 삭제시 같이 달려있던 댓글 삭제
	String sql = "delete from rc_reply where rc_replypseq = ?";
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
	String sql = "select count(*) from recipeboard where "+option+ " like ?";
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
	
	public List<RecipeBoardDTO> selectBySearch(String option, String target, int start, int end) throws Exception{
		String sql = "select * from (select recipeboard.*, row_number() over(order by rc_seq desc) rn from recipeboard where "+option+" like ?) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, "%"+target+"%");
			pstat.setInt(2, start);
			pstat.setInt(3, end);
			try(ResultSet rs = pstat.executeQuery()){
				List<RecipeBoardDTO> list = new ArrayList<>();
				while(rs.next()) {
					RecipeBoardDTO dto = new RecipeBoardDTO();
					dto.setRc_seq(rs.getInt("rc_seq"));
					dto.setRc_writer(rs.getString("rc_writer"));
					dto.setRc_title(rs.getString("rc_title"));
					dto.setRc_contents(rs.getString("rc_contents"));
					dto.setRc_writedate(rs.getTimestamp("rc_writedate"));
					dto.setRc_viewcount(rs.getInt("rc_viewcount"));
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
		if(needPrev) {pageNavi += "<a href='/searchRecipe.admin?target="+target+"&option="+option+"&cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/searchRecipe.admin?target="+target+"&option="+option+"&cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/searchRecipe.admin?target="+target+"&option="+option+"&cpage="+(endNavi+1)+"'>></a>";}
		
		return pageNavi;
	}
	
	public List<RecipeBoardReplyDTO> selectReplyByBound(int start, int end) throws Exception{  // DB에 저장된 공지사항 글 5개씩 조회
		String sql = "select * from (select rc_reply.*, row_number() over(order by rc_replyseq desc) rn from rc_reply) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			
			pstat.setInt(1, start);
			pstat.setInt(2, end);
			try(ResultSet rs = pstat.executeQuery();){
				List<RecipeBoardReplyDTO> list = new ArrayList<>();
				while(rs.next()) {
					RecipeBoardReplyDTO dto = new RecipeBoardReplyDTO();
					dto.setRc_replyseq(rs.getInt("rc_replyseq"));
					dto.setRc_replypseq(rs.getInt("rc_replypseq"));
					dto.setRc_replyer(rs.getString("rc_replyer"));
					dto.setRc_replytext(rs.getString("rc_replytext"));
					dto.setRc_replydate(rs.getTimestamp("rc_replydate"));
					list.add(dto);
				}
				return list;
			}
		}
	}
	
	private int getReplyRecordCount() throws Exception{ // DB에 저장된 게시글 수 구하는 기능. 우리만 쓰는 기능이라 정보은닉
		String sql = "select count(*) from rc_reply";
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
		if(needPrev) {pageNavi += "<a href='/recipeReply.admin?cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/recipeReply.admin?cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/recipeReply.admin?cpage="+(endNavi+1)+"'>></a>";}
		return pageNavi;
	}
	
	public int deleteComment(int seq) throws Exception{  // DB에 저장된 공지사항 글 삭제
		String sql = "delete from rc_reply where rc_replyseq = ?";
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
		String sql = "select count(*) from rc_reply where "+option+ " like ?";
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
	
	public List<RecipeBoardReplyDTO> selectBySearchReply(String option, String target, int start, int end) throws Exception{
		String sql = "select * from (select rc_reply.*, row_number() over(order by rc_replyseq desc) rn from rc_reply where "+option+" like ?) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, "%"+target+"%");
			pstat.setInt(2, start);
			pstat.setInt(3, end);
			try(ResultSet rs = pstat.executeQuery()){
				List<RecipeBoardReplyDTO> list = new ArrayList<>();
				while(rs.next()) {
					RecipeBoardReplyDTO dto = new RecipeBoardReplyDTO();
					dto.setRc_replyseq(rs.getInt("rc_replyseq"));
					dto.setRc_replypseq(rs.getInt("rc_replypseq"));
					dto.setRc_replyer(rs.getString("rc_replyer"));
					dto.setRc_replytext(rs.getString("rc_replytext"));
					dto.setRc_replydate(rs.getTimestamp("rc_replydate"));
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
		if(needPrev) {pageNavi += "<a href='/searchRecipeReply.admin?target="+target+"&option="+option+"&cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/searchRecipeReply.admin?target="+target+"&option="+option+"&cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/searchRecipeReply.admin?target="+target+"&option="+option+"&cpage="+(endNavi+1)+"'>></a>";}
		
		return pageNavi;
	}
}
