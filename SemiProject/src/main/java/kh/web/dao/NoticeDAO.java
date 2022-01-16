package kh.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import kh.web.dto.NoticeDTO;
import kh.web.dto.NoticeReplyDTO;
import kh.web.statics.Statics;

public class NoticeDAO {
	private static NoticeDAO instance = null;
	public static NoticeDAO getInstance() {
		if(instance == null) {
			instance = new NoticeDAO();
		}
		return instance;
	}
	private NoticeDAO() {}
	//Singleton 관련 코드
	
	private Connection getConnection() throws Exception{
		Context ctx = new InitialContext();
		DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/oracle");
		return ds.getConnection();
	}
	 // JNDI 관련 코드
	
	public int insert(NoticeDTO dto) throws Exception{  // DB에 공지사항 글 저장
		String sql = "insert into notice values(notice_seq.nextval,?,?,?,default,default)";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, dto.getWriter());
			pstat.setString(2, dto.getTitle());
			pstat.setString(3, dto.getContents());
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	
	public List<NoticeDTO> selectAll() throws Exception{  // DB에 저장된 공지사항 글 전체 조회
		String sql = "select * from notice order by noti_seq desc";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);
			ResultSet rs = pstat.executeQuery();){
			
			List<NoticeDTO> list = new ArrayList<>();
			while(rs.next()) {
				NoticeDTO dto = new NoticeDTO();
				dto.setSeq(rs.getInt("noti_seq"));
				dto.setWriter(rs.getString("noti_writer"));
				dto.setTitle(rs.getString("noti_title"));
				dto.setContents(rs.getString("noti_contents"));
				dto.setWrite_date(rs.getTimestamp("noti_write_date"));
				dto.setView_count(rs.getInt("noti_view_count"));
				list.add(dto);
			}
			return list;
		}
	}
	
	public List<NoticeDTO> selectByBound(int start, int end) throws Exception{  // DB에 저장된 공지사항 글 5개씩 조회
		String sql = "select * from (select notice.*, row_number() over(order by noti_seq desc) rn from notice) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			
			pstat.setInt(1, start);
			pstat.setInt(2, end);
			try(ResultSet rs = pstat.executeQuery();){
				List<NoticeDTO> list = new ArrayList<>();
				while(rs.next()) {
					NoticeDTO dto = new NoticeDTO();
					dto.setSeq(rs.getInt("noti_seq"));
					dto.setWriter(rs.getString("noti_writer"));
					dto.setTitle(rs.getString("noti_title"));
					dto.setContents(rs.getString("noti_contents"));
					dto.setWrite_date(rs.getTimestamp("noti_write_date"));
					dto.setView_count(rs.getInt("noti_view_count"));
					list.add(dto);
				}
				return list;
			}
		}
	}
	
	public NoticeDTO selectBySeq(int seq) throws Exception{  // DB에 저장된 공지사항 글 seq로 검색
		String sql = "select * from notice where noti_seq=?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setInt(1, seq);
			try(ResultSet rs = pstat.executeQuery();){
				if(rs.next()) {
					NoticeDTO dto = new NoticeDTO();
					dto.setSeq(rs.getInt("noti_seq"));
					dto.setWriter(rs.getString("noti_writer"));
					dto.setTitle(rs.getString("noti_title"));
					dto.setContents(rs.getString("noti_contents"));
					dto.setWrite_date(rs.getTimestamp("noti_write_date"));
					dto.setView_count(rs.getInt("noti_view_count"));
					return dto;
				}
			}
		return null;
		}
	}
	public int addViewCount(int seq) throws Exception{  // 공지사항 글 조회할 때마다 조회 수 증가 기능
		String sql = "update notice set noti_view_count = noti_view_count + 1 where noti_seq = ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setInt(1, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	public int delete(int seq) throws Exception{  // DB에 저장된 공지사항 글 삭제
		String sql = "delete from notice where noti_seq = ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setInt(1, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	
	public int modify(int seq, String title, String contents) throws Exception{  // 공지사항 글 수정
		String sql = "update notice set noti_title = ?, noti_contents = ? where noti_seq = ?";
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
	
	private int getRecordCount() throws Exception{ // DB에 저장된 게시글 수 구하는 기능. 우리만 쓰는 기능이라 정보은닉
		String sql = "select count(*) from notice";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);
			ResultSet rs = pstat.executeQuery();){
			rs.next();
			return rs.getInt(1);
		}
	}
	
	public String getPageNavi(int currentPage) throws Exception{
		int recordTotalCount = this.getRecordCount(); // 총 몇개의 레코드(게시글)을 가지고 있는지
		
		int pageTotalCount = 0; //페이지 수를 저장할 변수
		
		if(recordTotalCount % Statics.RECORD_COUNT_PER_PAGE == 0) {
			pageTotalCount = recordTotalCount/Statics.RECORD_COUNT_PER_PAGE; // 5로 딱 떨어질 때
		}else {
			pageTotalCount = recordTotalCount/Statics.RECORD_COUNT_PER_PAGE + 1; // 5로 딱 떨어지지 않을 때
		}
		
		if(currentPage < 1) {
			currentPage = 1;
		} // 페이지 1보다 작을 때 1로 만들어주는 보완코드
		
		int startNavi = (currentPage - 1) / Statics.NAVI_COUNT_PER_PAGE * Statics.NAVI_COUNT_PER_PAGE + 1;
		int endNavi = startNavi + Statics.NAVI_COUNT_PER_PAGE - 1;
		
		if(endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		} // 페이지 네비게이터 마지막 숫자가 총 페이지 숫자보다 클 때 맞춰주는 보완코드
		
		boolean needPrev = true;
		boolean needNext = true;
		
		if(startNavi == 1) {needPrev = false;}
		if(endNavi == pageTotalCount) {needNext = false;}
		
		String pageNavi = "";
		if(needPrev) {pageNavi += "<a href='/list.notice?cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/list.notice?cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/list.notice?cpage="+(endNavi+1)+"'>></a>";}
		return pageNavi;
	}
	
	public int insertComment(int pseq, String writer, String comment) throws Exception{  // DB에 댓글 저장
		String sql = "insert into noticereply values(noticereply_seq.nextval,?,?,?,default)";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setInt(1, pseq);
			pstat.setString(2, writer);
			pstat.setString(3, comment);
			System.out.println(pseq+writer+comment);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	
	public List<NoticeReplyDTO> selectCommentByPseq(int pseq) throws Exception{  // DB에 저장된 댓글을 parent seq별로 조회
		String sql = "select * from noticereply where noti_reply_parentseq=? order by noti_reply_seq desc";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setInt(1, pseq);
			try(ResultSet rs = pstat.executeQuery();){
				List<NoticeReplyDTO> list = new ArrayList<>();
				while(rs.next()) {
					NoticeReplyDTO dto = new NoticeReplyDTO();
					dto.setSeq(rs.getInt("noti_reply_seq"));
					dto.setParentSeq(rs.getInt("noti_reply_parentSeq"));
					dto.setWriter(rs.getString("noti_reply_writer"));
					dto.setContents(rs.getString("noti_reply_contents"));
					dto.setWrite_date(rs.getTimestamp("noti_reply_write_date"));
					list.add(dto);
				}
				return list;
			}
		}
	}
	public int deleteComment(int seq) throws Exception{  // DB에 저장된 공지사항 글 삭제
		String sql = "delete from noticereply where noti_reply_seq = ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setInt(1, seq);
			int result = pstat.executeUpdate();
			System.out.println(result);
			con.commit();
			return result;
		}
	}
	public int modCnt(int seq, int pseq, String contents) throws Exception{  // 공지사항 글 수정
		String sql = "update noticereply set noti_reply_contents = ? where noti_reply_seq = ? and noti_reply_parentseq = ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setString(1, contents);
			pstat.setInt(2, seq);
			pstat.setInt(3, pseq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	public int deleteCommentByPseq(int pseq) throws Exception{  // 게시판 글 삭제시 같이 달려있던 댓글 삭제
		String sql = "delete from noticereply where noti_reply_parentseq = ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setInt(1, pseq);
			int result = pstat.executeUpdate();
			System.out.println(result);
			con.commit();
			return result;
		}
	}

	public List<NoticeDTO> searchTarget(String option, String target) throws Exception{  // DB에 저장된 공지사항 글 seq로 검색
		String sql = "select * from notice where "+option+" like ? order by noti_seq desc";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setString(1, "%"+target+"%");
			try(ResultSet rs = pstat.executeQuery();){
				List<NoticeDTO> list = new ArrayList<>();
				while(rs.next()) {
					NoticeDTO dto = new NoticeDTO();
					dto.setSeq(rs.getInt("noti_seq"));
					dto.setWriter(rs.getString("noti_writer"));
					dto.setTitle(rs.getString("noti_title"));
					dto.setContents(rs.getString("noti_contents"));
					dto.setWrite_date(rs.getTimestamp("noti_write_date"));
					dto.setView_count(rs.getInt("noti_view_count"));
					list.add(dto);
				}
			return list;
			}
		}
	}
	
	private int getRecordCountBySearch(String option, String target) throws Exception{
		String sql = "select count(*) from notice where "+option+ " like ?";
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
	
	public List<NoticeDTO> selectBySearch(String option, String target, int start, int end) throws Exception{
		String sql = "select * from (select notice.*, row_number() over(order by noti_seq desc) rn from notice where "+option+" like ?) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, "%"+target+"%");
			pstat.setInt(2, start);
			pstat.setInt(3, end);
			try(ResultSet rs = pstat.executeQuery()){
				List<NoticeDTO> list = new ArrayList<>();
				while(rs.next()) {
					int seq = rs.getInt("noti_seq");
					String writer = rs.getString("noti_writer");
					String title = rs.getString("noti_title");
					String contents = rs.getString("noti_contents");
					Timestamp write_date = rs.getTimestamp("noti_write_date");
					int view_count  = rs.getInt("noti_view_count");
					NoticeDTO dto = new NoticeDTO(seq, writer, title, contents, write_date, view_count);
					list.add(dto);
				}
				return list;
			}
		}
	}
	
	public String getPageNaviBySearch(int currentPage, String target, String option) throws Exception{
		
		// 총 몇개의 레코드(게시글)를 가지고 있는지
		int recordTotalCount = this.getRecordCountBySearch(option, target);
	
		int pageTotalCount = 0;
		// 총 몇개의 페이지가 만들어질 것인지
		if(recordTotalCount % Statics.RECORD_COUNT_PER_PAGE == 0) {
			pageTotalCount = recordTotalCount / Statics.RECORD_COUNT_PER_PAGE;
		}else {
			pageTotalCount = recordTotalCount / Statics.RECORD_COUNT_PER_PAGE + 1;
		}
		
		int startNavi = (currentPage - 1) / Statics.NAVI_COUNT_PER_PAGE * Statics.NAVI_COUNT_PER_PAGE + 1;
		int endNavi = startNavi + Statics.NAVI_COUNT_PER_PAGE - 1;
		
		// 공식에 의해 발생한 endNavi 값이 실제 페이지 전체 개수보다 클 경우
		if(endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		}
		
		boolean needPrev = true;
		boolean needNext = true;
		
		if(startNavi == 1) {needPrev = false;}
		
		if(endNavi == pageTotalCount) {needNext = false;}
		
		String pageNavi = "";
		if(needPrev) {pageNavi += "<a href='/search.notice?target="+target+"&option="+option+"&cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/search.notice?target="+target+"&option="+option+"&cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/search.notice?target="+target+"&option="+option+"&cpage="+(endNavi+1)+"'>></a>";}
		
		return pageNavi;
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
		if(needPrev) {pageNavi += "<a href='/notice.admin?cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/notice.admin?cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/notice.admin?cpage="+(endNavi+1)+"'>></a>";}
		return pageNavi;
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
		if(needPrev) {pageNavi += "<a href='/searchNotice.admin?target="+target+"&option="+option+"&cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/searchNotice.admin?target="+target+"&option="+option+"&cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/searchNotice.admin?target="+target+"&option="+option+"&cpage="+(endNavi+1)+"'>></a>";}
		
		return pageNavi;
	}
	
	public List<NoticeReplyDTO> selectReplyByBound(int start, int end) throws Exception{  // DB에 저장된 공지사항 글 5개씩 조회
		String sql = "select * from (select noticereply.*, row_number() over(order by noti_reply_seq desc) rn from noticereply) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			
			pstat.setInt(1, start);
			pstat.setInt(2, end);
			try(ResultSet rs = pstat.executeQuery();){
				List<NoticeReplyDTO> list = new ArrayList<>();
				while(rs.next()) {
					NoticeReplyDTO dto = new NoticeReplyDTO();
					dto.setSeq(rs.getInt("noti_reply_seq"));
					dto.setParentSeq(rs.getInt("noti_reply_parentseq"));
					dto.setWriter(rs.getString("noti_reply_writer"));
					dto.setContents(rs.getString("noti_reply_contents"));
					dto.setWrite_date(rs.getTimestamp("noti_reply_write_date"));
					list.add(dto);
				}
				return list;
			}
		}
	}
	
	private int getReplyRecordCount() throws Exception{ // DB에 저장된 게시글 수 구하는 기능. 우리만 쓰는 기능이라 정보은닉
		String sql = "select count(*) from noticereply";
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
		if(needPrev) {pageNavi += "<a href='/noticeReply.admin?cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/noticeReply.admin?cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/noticeReply.admin?cpage="+(endNavi+1)+"'>></a>";}
		return pageNavi;
	}
	private int getReplyRecordCountBySearch(String option, String target) throws Exception{
		String sql = "select count(*) from notice where "+option+ " like ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, "%"+target+"%");
			try(ResultSet rs = pstat.executeQuery();){
				rs.next();
				return rs.getInt(1);
			}
		}
	}
	
	public String getReplyPageNaviAdminBySearch(int currentPage, String target, String option) throws Exception{
		int recordTotalCount = this.getReplyRecordCountBySearch(option, target); // 총 몇개의 레코드(게시글)을 가지고 있는지
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
		if(needPrev) {pageNavi += "<a href='/noticeReply.admin?target="+target+"&option="+option+"&cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/noticeReply.admin?target="+target+"&option="+option+"&cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/noticeReply.admin?target="+target+"&option="+option+"&cpage="+(endNavi+1)+"'>></a>";}
		
		return pageNavi;
	}
	
	private int getRecordCountBySearchReply(String option, String target) throws Exception{
		String sql = "select count(*) from noticereply where "+option+ " like ?";
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
	public List<NoticeReplyDTO> selectBySearchReply(String option, String target, int start, int end) throws Exception{
		String sql = "select * from (select noticereply.*, row_number() over(order by noti_reply_seq desc) rn from noticereply where "+option+" like ?) where rn between ? and ?";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql);){
			pstat.setString(1, "%"+target+"%");
			pstat.setInt(2, start);
			pstat.setInt(3, end);
			try(ResultSet rs = pstat.executeQuery()){
				List<NoticeReplyDTO> list = new ArrayList<>();
				while(rs.next()) {
					int seq = rs.getInt("noti_reply_seq");
					int pseq = rs.getInt("noti_reply_parentseq");
					String writer = rs.getString("noti_reply_writer");
					String contents = rs.getString("noti_reply_contents");
					Timestamp write_date = rs.getTimestamp("noti_reply_write_date");
					NoticeReplyDTO dto = new NoticeReplyDTO(seq, pseq, writer, contents, write_date);
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
		if(needPrev) {pageNavi += "<a href='/searchNoticeReply.admin?target="+target+"&option="+option+"&cpage="+(startNavi-1)+"'><</a> ";}
		for(int i = startNavi;i <= endNavi; i++) {
			pageNavi+="<a href='/searchNoticeReply.admin?target="+target+"&option="+option+"&cpage="+i+"'>" + i+"</a> ";
		}
		if(needNext) {pageNavi += "<a href='/searchNoticeReply.admin?target="+target+"&option="+option+"&cpage="+(endNavi+1)+"'>></a>";}
		
		return pageNavi;
	}
}
