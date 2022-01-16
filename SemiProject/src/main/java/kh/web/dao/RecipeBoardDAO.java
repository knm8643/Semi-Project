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

import kh.web.dto.RecipeBoardDTO;
import kh.web.dto.RecipeBoardReplyDTO;
import kh.web.statics.Statics;

public class RecipeBoardDAO {
	private static RecipeBoardDAO instance = null;

	public static RecipeBoardDAO getInstance() {
		if (instance == null) {
			instance = new RecipeBoardDAO();
		}
		return instance;
	}

	public RecipeBoardDAO() {
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
				stat.executeUpdate("insert into recipeboard values(recipe_seq.nextval,'writer" + i + "','title" + i
						+ "','내용',sysdate,default,default)");
			}
			con.commit();
		}
	}

	// 1. 총 게시글 카운트
	private int getRecordCount() throws Exception {
		String sql = "select count(*) from recipeboard";
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
		if (recordTotalCount % Statics.BOARD_COUNT_PER_PAGE == 0) {
			pageTotalCount = recordTotalCount / Statics.BOARD_COUNT_PER_PAGE;
			// 페이지가 딱 떨어지지 않으면 1을 더해서 페이지를 하나 더 만들어라
		} else {
			pageTotalCount = recordTotalCount / Statics.BOARD_COUNT_PER_PAGE + 1;
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
		if (recordTotalCount % Statics.BOARD_COUNT_PER_PAGE == 0) {
			pageTotalCount = recordTotalCount / Statics.BOARD_COUNT_PER_PAGE;
			// 페이지가 딱 떨어지지 않으면 1을 더해서 페이지를 하나 더 만들어라
		} else {
			pageTotalCount = recordTotalCount / Statics.BOARD_COUNT_PER_PAGE + 1;
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
			pageNavi += "<a href='/list.recipe?cpage=" + (startNavi - 1) + "'> < </a>";
		}

		for (int i = startNavi; i <= endNavi; i++) {
			pageNavi += "<a href='/list.recipe?cpage=" + i+ "'>" +    i    + "  </a>";
		}

		if (needNext) {
			pageNavi += "<a href='/list.recipe?cpage=" + (endNavi + 1) + "'> > </a>";
		}
		return pageNavi;
	}

	// 2. 총 검색한 글 카운트
	private int getRecordCountBySearch(String keyword, String searchWord) throws Exception {
		String sql = "select count(*) from recipeBoard where " + keyword + " like ?";
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
		if (recordTotalCount % Statics.BOARD_COUNT_PER_PAGE == 0) {
			pageTotalCount = recordTotalCount / Statics.BOARD_COUNT_PER_PAGE;
		} else {
			pageTotalCount = recordTotalCount / Statics.BOARD_COUNT_PER_PAGE + 1;
		}
		return pageTotalCount;
	}

	// 2-2. 검색한 글의 페이지 네비게이터
	public String getPageNaviBySearch(int currentPage, String keyword, String searchWord) throws Exception {

		int recordTotalCount = this.getRecordCountBySearch(keyword, searchWord);
		int pageTotalCount = 0;

		if (recordTotalCount % Statics.BOARD_COUNT_PER_PAGE == 0) {
			pageTotalCount = recordTotalCount / Statics.BOARD_COUNT_PER_PAGE;
		} else {
			pageTotalCount = recordTotalCount / Statics.BOARD_COUNT_PER_PAGE + 1;
		}

		int startNavi = (currentPage - 1) / Statics.BNAVI_COUNT_PER_PAGE * Statics.BNAVI_COUNT_PER_PAGE + 1;
		int endNavi = startNavi + Statics.BNAVI_COUNT_PER_PAGE - 1;

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
			pageNavi += "<a href='/searchList.recipe?searchWord=" + searchWord + "&keyword=" + keyword + "&cpage="
					+ (startNavi - 1) + "'><</a> ";
		}
		for (int i = startNavi; i <= endNavi; i++) {
			pageNavi += "<a href='/search.recipe?searchWord=" + searchWord + "&keyword=" + keyword + "&cpage=" + i
					+ "'>" + i + "</a> ";
		}
		if (needNext) {
			pageNavi += "<a href='/search.recipe?searchWord=" + searchWord + "&keyword=" + keyword + "&cpage="
					+ (endNavi + 1) + "'>></a>";
		}

		return pageNavi;
	}

	public int insert(RecipeBoardDTO dto) throws Exception {
		String sql = "insert into recipeboard values(recipe_seq.nextval,?,?,?,sysdate,default)";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, dto.getRc_writer());
			pstat.setString(2, dto.getRc_title());
			pstat.setString(3, dto.getRc_contents());
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}

	public List<RecipeBoardDTO> selectAll() throws Exception {
		String sql = "select * from recipeboard order by seq desc";
		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);
				ResultSet rs = pstat.executeQuery();) {

			List<RecipeBoardDTO> list = new ArrayList();

			while (rs.next()) {

				int seq = rs.getInt("rc_seq");
				String writer = rs.getString("rc_writer");
				String title = rs.getString("rc_title");
				String content = rs.getString("rc_contents");
				Timestamp write_date = rs.getTimestamp("rc_writedate");
				int view_count = rs.getInt("rc_viewcount");

				RecipeBoardDTO dto = new RecipeBoardDTO(seq, writer, title, content, write_date, view_count);

				list.add(dto);
			}
			return list;
		}
	}

	// DB로부터 데이터 가져오기
	public RecipeBoardDTO selectBySeq(int seq) throws SQLException, Exception {
		String sql = "select * from recipeboard where rc_seq=?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, seq);
			try (ResultSet rs = pstat.executeQuery();) {
				if (rs.next()) {
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

	public List<RecipeBoardDTO> selectByBound(int start, int end) throws Exception {
		String sql = "select * from (select recipeboard.*,row_number() over(order by rc_seq desc) rn from recipeboard) where rn between ? and ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {

			pstat.setInt(1, start);
			pstat.setInt(2, end);
			try (ResultSet rs = pstat.executeQuery()) {
				List<RecipeBoardDTO> list = new ArrayList<>();
				while (rs.next()) {
					int seq = rs.getInt("rc_seq");
					String writer = rs.getString("rc_writer");
					String title = rs.getString("rc_title");
					String content = rs.getString("rc_contents");
					Timestamp write_date = rs.getTimestamp("rc_writedate");
					int view_count = rs.getInt("rc_viewcount");
					RecipeBoardDTO dto = new RecipeBoardDTO(seq, writer, title, content, write_date, view_count);
					list.add(dto);
				}
				return list;
			}
		}
	}

	public int addViewCount(int seq) throws Exception {
		String sql = "update recipeboard set rc_viewcount = rc_viewcount + 1 where rc_seq = ?";

		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;

		}
	}

	// DB에 저장된 글 삭제하기
	public int deleteBySeq(int seq) throws Exception {
		String sql = "delete from recipeboard where rc_seq = ? ";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;

		}
	}

	/// 수정한 글 DB에 저장.
	public int modify(int seq, String title, String contents) throws Exception {
		String sql = "update recipeboard set rc_title=?, rc_contents=? where rc_seq=?";
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
		String sql = "insert into rc_reply values(recipe_replyseq.nextval,?,?,?,sysdate)";
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
		String sql = "update rc_reply set rc_replytext=? where rc_replyseq=? and rc_replypseq=?";
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
		String sql = "delete from rc_reply where rc_replyseq = ? ";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, seq);
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}

	// 글 삭제 시 달려있던 댓글도 삭제
	public int deleteByPseq(int pseq) throws Exception { // 게시판 글 삭제시 같이 달려있던 댓글 삭제
		String sql = "delete from rc_reply where rc_replypseq=? ";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setInt(1, pseq);
			int result = pstat.executeUpdate();

			con.commit();
			return result;
		}
	}

	// 게시글에 딸린 댓글들 선택해서 뽑아주기
	public List<RecipeBoardReplyDTO> selectReplyByPseq(int pseq) throws Exception {
		String sql = "select * from rc_reply where rc_replypseq=? order by rc_replyseq desc";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setInt(1, pseq);
			try (ResultSet rs = pstat.executeQuery();) {
				List<RecipeBoardReplyDTO> list = new ArrayList<>();
				while (rs.next()) {
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

	// 검색 기능
	public List<RecipeBoardDTO> searchBoard(int start, int end, String keyword, String searchWord) throws Exception {
		String sql = "select * from(select recipeboard.*,row_number()over(order by rc_seq desc)rn from recipeboard where "
				+ keyword + " like ?) where rn between ? and ?";

		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {

			pstat.setString(1, "%" + searchWord + "%");
			pstat.setInt(2, start);
			pstat.setInt(3, end);

			try (ResultSet rs = pstat.executeQuery()) {
				List<RecipeBoardDTO> list = new ArrayList<>();
				while (rs.next()) {
					int seq = rs.getInt("rc_seq");
					String writer = rs.getString("rc_writer");
					String title = rs.getString("rc_title");
					String content = rs.getString("rc_contents");
					Timestamp write_date = rs.getTimestamp("rc_writedate");
					int view_count = rs.getInt("rc_viewcount");
					
					RecipeBoardDTO dto = new RecipeBoardDTO(seq, writer, title, content, write_date, view_count);
					list.add(dto);
				}
				return list;
			}
		}

	}

	// 게시글에 몇개의 댓글이 달려있는지 카운트 쿼리
	public int totalReplyCount(int pseq) throws Exception {
		String sql = "select count(rc_replyseq) replycount from rc_reply where rc_replypseq=?";
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
