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

import kh.web.dto.WithBoardDTO;
import kh.web.dto.WithBoardReplyDTO;
import kh.web.statics.Statics;

public class WithBoardDAO {
	private static WithBoardDAO instance = null;

	public static WithBoardDAO getInstance() {
		if (instance == null) {
			instance = new WithBoardDAO();
		}
		return instance;
	}

	public WithBoardDAO() {
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
				stat.executeUpdate("insert into withboard values(with_seq.nextval,'writer" + i + "','title" + i
						+ "','내용',sysdate,default)");
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
			pageNavi += "<a href='/list.with?cpage=" + (startNavi - 1) + "'> < </a>";
		}

		for (int i = startNavi; i <= endNavi; i++) {
			pageNavi += "<a href='/list.with?cpage=" + i+ "'>" +    i    + "  </a>";
		}

		if (needNext) {
			pageNavi += "<a href='/list.with?cpage=" + (endNavi + 1) + "'> > </a>";
		}
		return pageNavi;
	}

	// 2. 총 검색한 글 카운트
	private int getRecordCountBySearch(String keyword, String searchWord) throws Exception {
		String sql = "select count(*) from withBoard where " + keyword + " like ?";
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
			pageNavi += "<a href='/searchList.with?searchWord=" + searchWord + "&keyword=" + keyword + "&cpage="
					+ (startNavi - 1) + "'><</a> ";
		}
		for (int i = startNavi; i <= endNavi; i++) {
			pageNavi += "<a href='/search.with?searchWord=" + searchWord + "&keyword=" + keyword + "&cpage=" + i
					+ "'>" + i + "</a> ";
		}
		if (needNext) {
			pageNavi += "<a href='/search.with?searchWord=" + searchWord + "&keyword=" + keyword + "&cpage="
					+ (endNavi + 1) + "'>></a>";
		}

		return pageNavi;
	}

	public int insert(WithBoardDTO dto) throws Exception {
		String sql = "insert into withboard values(with_seq.nextval,?,?,?,sysdate,default)";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setString(1, dto.getWith_writer());
			pstat.setString(2, dto.getWith_title());
			pstat.setString(3, dto.getWith_contents());
			int result = pstat.executeUpdate();
			con.commit();
			return result;
		}
	}

	public List<WithBoardDTO> selectAll() throws Exception {
		String sql = "select * from withboard order by seq desc";
		try (Connection con = this.getConnection();
				PreparedStatement pstat = con.prepareStatement(sql);
				ResultSet rs = pstat.executeQuery();) {

			List<WithBoardDTO> list = new ArrayList();

			while (rs.next()) {

				int seq = rs.getInt("with_seq");
				String writer = rs.getString("with_writer");
				String title = rs.getString("with_title");
				String content = rs.getString("with_contents");
				Timestamp write_date = rs.getTimestamp("with_writedate");
				int view_count = rs.getInt("with_viewcount");

				WithBoardDTO dto = new WithBoardDTO(seq, writer, title, content, write_date, view_count);

				list.add(dto);
			}
			return list;
		}
	}

	// DB로부터 데이터 가져오기
	public WithBoardDTO selectBySeq(int seq) throws SQLException, Exception {
		String sql = "select * from withboard where with_seq=?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {
			pstat.setInt(1, seq);
			try (ResultSet rs = pstat.executeQuery();) {
				if (rs.next()) {
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

	public List<WithBoardDTO> selectByBound(int start, int end) throws Exception {
		String sql = "select * from (select withboard.*,row_number() over(order by with_seq desc) rn from withboard) where rn between ? and ?";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql);) {

			pstat.setInt(1, start);
			pstat.setInt(2, end);
			try (ResultSet rs = pstat.executeQuery()) {
				List<WithBoardDTO> list = new ArrayList<>();
				while (rs.next()) {
					int seq = rs.getInt("with_seq");
					String writer = rs.getString("with_writer");
					String title = rs.getString("with_title");
					String content = rs.getString("with_contents");
					Timestamp write_date = rs.getTimestamp("with_writedate");
					int view_count = rs.getInt("with_viewcount");
					WithBoardDTO dto = new WithBoardDTO(seq, writer, title, content, write_date, view_count);
					list.add(dto);
				}
				return list;
			}
		}
	}

	public int addViewCount(int seq) throws Exception {
		String sql = "update withboard set with_viewcount = with_viewcount + 1 where with_seq = ?";

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
		String sql = "update withboard set with_title=?, with_contents=? where with_seq=?";
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
		String sql = "insert into with_reply values(with_replyseq.nextval,?,?,?,sysdate)";
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
		String sql = "update with_reply set with_replytext=? where with_replyseq=? and with_replypseq=?";
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
	public List<WithBoardReplyDTO> selectReplyByPseq(int pseq) throws Exception {
		String sql = "select * from with_reply where with_replypseq=? order by with_replyseq desc";
		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {
			pstat.setInt(1, pseq);
			try (ResultSet rs = pstat.executeQuery();) {
				List<WithBoardReplyDTO> list = new ArrayList<>();
				while (rs.next()) {
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

	// 검색 기능
	public List<WithBoardDTO> searchBoard(int start, int end, String keyword, String searchWord) throws Exception {
		String sql = "select * from(select withboard.*,row_number()over(order by with_seq desc)rn from withboard where "
				+ keyword + " like ?) where rn between ? and ?";

		try (Connection con = this.getConnection(); PreparedStatement pstat = con.prepareStatement(sql)) {

			pstat.setString(1, "%" + searchWord + "%");
			pstat.setInt(2, start);
			pstat.setInt(3, end);

			try (ResultSet rs = pstat.executeQuery()) {
				List<WithBoardDTO> list = new ArrayList<>();
				while (rs.next()) {
					int seq = rs.getInt("with_seq");
					String writer = rs.getString("with_writer");
					String title = rs.getString("with_title");
					String content = rs.getString("with_contents");
					Timestamp write_date = rs.getTimestamp("with_writedate");
					int view_count = rs.getInt("with_viewcount");
					
					WithBoardDTO dto = new WithBoardDTO(seq, writer, title, content, write_date, view_count);
					list.add(dto);
				}
				return list;
			}
		}

	}

	// 게시글에 몇개의 댓글이 달려있는지 카운트 쿼리
	public int totalReplyCount(int pseq) throws Exception {
		String sql = "select count(with_replyseq) replycount from with_reply where with_replypseq=?";
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
