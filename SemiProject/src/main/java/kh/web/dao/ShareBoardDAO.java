package kh.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import kh.web.dto.ShareBoardDTO;
import kh.web.dto.ShareBoardReplyDTO;
import kh.web.statics.Statics;

public class ShareBoardDAO {
	private static ShareBoardDAO instance = null;

	public static ShareBoardDAO getInstance() {
		if (instance == null) {
			instance = new ShareBoardDAO();
		}
		return instance;
		
	}

	private ShareBoardDAO() {
	}

	private Connection getConnection() throws Exception {
		Context ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/oracle"); // 경로(java:comp/env/) + 자원의
																				// 이름(jdbc/oracle)
		return ds.getConnection();
	}

	// 대량 데이터 만들기 위한 것
	public void insertDummy() throws Exception {
		try (Connection con = this.getConnection(); Statement stat = con.createStatement()) {

			for (int i = 1; i < 147; i++) {
				stat.executeUpdate("insert into shareboard values(share_seq.nextval,'writer" + i + "','title" + i
						+ "','내용',sysdate,default)");
			}
			con.commit();
		}
	}

	// 1. 총 게시글 카운트
	private int getRecordCount() throws Exception {
		String sql = "select count(*) from shareboard";
		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);
				ResultSet rs = pstat.executeQuery();) {
			rs.next();
			return rs.getInt(1);
		}

	}

	// 1-1. 몇개의 페이지를 만들어야 하는지
	public int getPageTotalCount() throws Exception {
		// 총 몇개의 레코드(글)을 가지고 있는지
		int recordTotalCount = this.getRecordCount();
		// 총 페이지의 개수
		int pageTotalCount = 0;
		// 페이지가 딱 떨어지면 페이지 추가할 필요 없음 ex)100개 글 나누기 10 = 10개의 페이지
		if (recordTotalCount % Statics.BOARD_COUNT_PER_PAGE== 0) {
			pageTotalCount = recordTotalCount / Statics.BOARD_COUNT_PER_PAGE;
			// 페이지가 딱 떨어지지 않으면 1을 더해서 페이지를 하나 더 만들어라
		} else {
			pageTotalCount = recordTotalCount / Statics.BOARD_COUNT_PER_PAGE+ 1;
		}
		return pageTotalCount;
	}

	// 1-2. 페이지 네비게이터
	public String getPageNavi(int currentPage) throws Exception {

		// 총 몇개의 레코드(글)을 가지고 있는지
		int recordTotalCount = this.getRecordCount();

		// 총 페이지의 개수
		int pageTotalCount = 0;

		// 페이지가 딱 떨어지면 페이지 추가할 필요 없음 ex)100개 글 나누기 10 = 10개의 페이지
		if (recordTotalCount % Statics.BOARD_COUNT_PER_PAGE== 0) {
			pageTotalCount = recordTotalCount / Statics.BOARD_COUNT_PER_PAGE;
			// 페이지가 딱 떨어지지 않으면 1을 더해서 페이지를 하나 더 만들어라
		} else {
			pageTotalCount = recordTotalCount / Statics.BOARD_COUNT_PER_PAGE+ 1;
		}

		// 시작 페이지 구하는 공식!!!!!!
		int startNavi = (currentPage - 1) / Statics.BNAVI_COUNT_PER_PAGE * Statics.BNAVI_COUNT_PER_PAGE + 1;
		int endNavi = startNavi + Statics.BNAVI_COUNT_PER_PAGE - 1;

		// 공식에 의해 발생한endnavi값이 실제 페이지 전체 개수보다 클경우
		if (endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		}

		boolean needPrev = true;
		boolean needNext = true;

		if (startNavi == 1) {
			needPrev = false;
		}

		if (endNavi == pageTotalCount) {
			needNext = false;
		}

		String pageNavi = "";

		if (needPrev) {
			pageNavi += "<a href='/list.board?cpage=" + (startNavi - 1) + "'><</a>";
		}

		for (int i = startNavi; i <= endNavi; i++) {
			pageNavi += "<a href='/list.board?cpage=" + i + "'>" + i + " </a>";
		}

		if (needNext) {
			pageNavi += "<a href='/list.board?cpage=" + (endNavi + 1) + "'>></a>";
		}
		return pageNavi;
	}

	// 2. 총 검색한 글 카운트
	private int getRecordCountBySearch(String keyword, String searchWord) throws Exception {
		String sql = "select count(*) from shareBoard where " + keyword + " like ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, "%" + searchWord + "%");
			try (ResultSet rs = pstat.executeQuery();) {
				rs.next();
				return rs.getInt(1); // 결과값 첫번째를 Int에 담아 리턴해라.
			}
		}
	}

	// 2-1. 검색을 통해 나온 데이터를 위해 총 몇페이지 만들어야 하는지
	public int getPageTotalCountBySearch(String option, String title) throws Exception {
		// 총 몇개의 레코드(게시글)를 가지고 있는지
		int recordTotalCount = this.getRecordCountBySearch(option, title);

		int pageTotalCount = 0;
		// 총 몇개의 페이지가 만들어질 것인지
		if (recordTotalCount % Statics.BOARD_COUNT_PER_PAGE== 0) {
			pageTotalCount = recordTotalCount/Statics.RECORD_COUNT_PER_PAGE;
		} else {
			pageTotalCount = recordTotalCount/Statics.RECORD_COUNT_PER_PAGE+ 1;
		}
		return pageTotalCount;
	}

	// 2-2. 검색한 글의 페이지 네비게이터
	public String getPageNaviBySearch(int currentPage, String keyword, String searchWord) throws Exception {

		int recordTotalCount = this.getRecordCountBySearch(keyword, searchWord);
		int pageTotalCount = 0;
		
		if (recordTotalCount %  Statics.BOARD_COUNT_PER_PAGE== 0) {
			pageTotalCount = recordTotalCount / Statics.BOARD_COUNT_PER_PAGE;
		} else {
			pageTotalCount = recordTotalCount /  Statics.BOARD_COUNT_PER_PAGE+ 1;
		}

		int startNavi = (currentPage - 1) / Statics.BNAVI_COUNT_PER_PAGE  * Statics.BNAVI_COUNT_PER_PAGE  + 1;
		int endNavi = startNavi + Statics.BNAVI_COUNT_PER_PAGE  - 1;

		// 공식에 의해 발생한 endNavi 값이 실제 페이지 전체 개수보다 클 경우
		if (endNavi > pageTotalCount) {
			endNavi = pageTotalCount;
		}

		boolean needPrev = true;
		boolean needNext = true;

		if (startNavi == 1) {
			needPrev = false;
		}

		if (endNavi == pageTotalCount) {
			needNext = false;
		}

		String pageNavi = "";
		if (needPrev) {
			pageNavi += "<a href='/searchList.board?searchWord=" + searchWord + "&keyword=" + keyword + "&cpage=" + (startNavi - 1)
					+ "'><</a> ";
		}
		for (int i = startNavi; i <= endNavi; i++) {
			pageNavi += "<a href='/searchList.board?searchWord=" + searchWord + "&keyword=" + keyword + "&cpage=" + i + "'>" + i
					+ "</a> ";
		}
		if (needNext) {
			pageNavi += "<a href='/searchList.board?searchWord=" + searchWord + "&keyword=" + keyword + "&cpage=" + (endNavi + 1)
					+ "'>></a>";
		}

		return pageNavi;
	}


	public int insert(ShareBoardDTO dto) throws Exception {
		String sql = "insert into shareboard values(share_seq.nextval,?,?,?,sysdate,default)";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, dto.getSh_writer());
			pstat.setString(2, dto.getSh_title());
			pstat.setString(3, dto.getSh_contents());

			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}

	public List<ShareBoardDTO> selectAll() throws Exception {
		String sql = "select * from shareboard order by seq desc";
		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);
				ResultSet rs = pstat.executeQuery();) {

			List<ShareBoardDTO> list = new ArrayList();

			while (rs.next()) {

				int seq = rs.getInt("sh_seq");
				String writer = rs.getString("sh_writer");
				String title = rs.getString("sh_title");
				String content = rs.getString("sh_contents");
				Timestamp write_date = rs.getTimestamp("sh_writedate");
				int view_count = rs.getInt("sh_viewcount");

				ShareBoardDTO dto = new ShareBoardDTO(seq, writer, title, content, write_date, view_count);

				list.add(dto);
			}
			return list;
		}
	}

	// DB로부터 데이터 가져오기
	public ShareBoardDTO selectBySeq(int seq) throws SQLException, Exception {
		String sql = "select * from shareboard where sh_seq=?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, seq);
			try (ResultSet rs = pstat.executeQuery();) {
				if (rs.next()) {
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

	public List<ShareBoardDTO> selectByBound(int start, int end) throws Exception {
		String sql = "select * from (select shareboard.*,row_number() over(order by sh_seq desc) rn from shareboard) where rn between ? and ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {

			pstat.setInt(1, start);
			pstat.setInt(2, end);
			try (ResultSet rs = pstat.executeQuery()) {
				List<ShareBoardDTO> list = new ArrayList<>();
				while (rs.next()) {
					int seq = rs.getInt("sh_seq");
					String writer = rs.getString("sh_writer");
					String title = rs.getString("sh_title");
					String content = rs.getString("sh_contents");
					Timestamp write_date = rs.getTimestamp("sh_writedate");
					int view_count = rs.getInt("sh_viewcount");
					ShareBoardDTO dto = new ShareBoardDTO(seq, writer, title, content, write_date, view_count);
					list.add(dto);
				}
				return list;
			}
		}
	}

	public int addViewCount(int seq) throws Exception {
		String sql = "update shareboard set sh_viewcount = sh_viewcount + 1 where sh_seq = ?";

		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;

		}
	}

	// DB에 저장된 글 삭제하기
	public int deleteBySeq(int seq) throws Exception {
		String sql = "delete from shareboard where sh_seq = ? ";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;

		}
	}
	
	
	/// 수정한 글 DB에 저장.
	public int modify(int seq, String title, String contents) throws Exception {
		String sql = "update shareboard set sh_title=?, sh_contents=? where sh_seq=?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, title);
			pstat.setString(2, contents);
			pstat.setInt(3, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;

		}
	}

	////// DB에 댓글 저장
	public int insertReply(int pseq, String writer, String reply) throws Exception {
		String sql = "insert into sh_reply values(share_replyseq.nextval,?,?,?,sysdate)";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, pseq);
			pstat.setString(2, writer);
			pstat.setString(3, reply);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}

	/// 댓글 수정
	public int modifyReply(int seq, int pseq, String reply) throws Exception {
		String sql = "update sh_reply set sh_replytext=? where sh_replyseq=? and sh_replypseq=?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, reply);
			pstat.setInt(2, seq);
			pstat.setInt(3, pseq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;

		}
	}

	/// 댓글 삭제
	public int deleteReplyBySeq(int seq) throws Exception {
		String sql = "delete from sh_reply where sh_replyseq = ? ";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}
	
	// 글 삭제 시 달려있던 댓글도 삭제
	public int deleteByPseq(int pseq) throws Exception{  // 게시판 글 삭제시 같이 달려있던 댓글 삭제
		String sql = "delete from sh_reply where sh_replypseq=? ";
		try(Connection con = this.getConnection();
			PreparedStatement pstat = con.prepareStatement(sql)){
			pstat.setInt(1, pseq);
			int result = pstat.executeUpdate();
	
			con.commit();
			if(result > 0) {
				
			}
			return result;
		}
	}

	// 게시글에 딸린 댓글들 선택해서 뽑아주기
	public List<ShareBoardReplyDTO> selectReplyByPseq(int pseq) throws Exception {
		String sql = "select * from sh_reply where sh_replypseq=? order by sh_replyseq desc";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setInt(1, pseq);
			try (ResultSet rs = pstat.executeQuery();) {
				List<ShareBoardReplyDTO> list = new ArrayList<>();
				while (rs.next()) {
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

	// 검색 기능
	public List<ShareBoardDTO> searchBoard(int start, int end, String keyword, String searchWord) throws Exception {
		String sql = "select * from(select shareboard.*,row_number()over(order by sh_seq desc)rn from shareboard where "
				+ keyword + " like ?) where rn between ? and ?";

		

		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {

			pstat.setString(1, "%" + searchWord + "%");
			pstat.setInt(2, start);
			pstat.setInt(3, end);

			try (ResultSet rs = pstat.executeQuery()) {
				List<ShareBoardDTO> list = new ArrayList<>();
				while (rs.next()) {
					int seq = rs.getInt("sh_seq");
					String writer = rs.getString("sh_writer");
					String title = rs.getString("sh_title");
					String content = rs.getString("sh_contents");
					Timestamp write_date = rs.getTimestamp("sh_writedate");
					int view_count = rs.getInt("sh_viewcount");
					ShareBoardDTO dto = new ShareBoardDTO(seq, writer, title, content, write_date, view_count);
					list.add(dto);
				}
				return list;
			}
		}
	}
	// 게시글에 몇개의 댓글이 달려있는지 카운트 쿼리
	public int totalReplyCount(int pseq) throws Exception {
		String sql = "select count(sh_replyseq) replycount from sh_reply where sh_replypseq=?";
		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setInt(1, pseq);
			try (ResultSet rs = pstat.executeQuery()) {
				rs.next();
				return rs.getInt(1);
			}
			
		}
	}
}
