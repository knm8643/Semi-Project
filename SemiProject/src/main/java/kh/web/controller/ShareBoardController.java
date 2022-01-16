package kh.web.controller;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import kh.web.dao.ShareBoardDAO;
import kh.web.dto.ShareBoardDTO;
import kh.web.dto.ShareBoardReplyDTO;
import kh.web.statics.Statics;

@WebServlet("*.board")
public class ShareBoardController extends HttpServlet {

	private String test;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf8");

		String uri = request.getRequestURI();
		String ctxPath = request.getContextPath();
		String cmd = uri.substring(ctxPath.length());
		System.out.println(cmd);

		ShareBoardDAO dao = ShareBoardDAO.getInstance();

		// 대량 데이터
//		try {
//			dao.insertDummy();
//		}catch(Exception e1) {
//			e1.printStackTrace();
//		}

		try {
			if (cmd.equals("/list.board")) {
				String cpage = request.getParameter("cpage");
				HttpSession session = request.getSession();
				String writer = (String) request.getSession().getAttribute("loginID"); 
				if (cpage == null)
					cpage = "1";
				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				int pageTotalCount = dao.getPageTotalCount();
				int start = currentPage * Statics.BOARD_COUNT_PER_PAGE - (Statics.BOARD_COUNT_PER_PAGE - 1);
				int end = currentPage * Statics.BOARD_COUNT_PER_PAGE;
				
				
				List<ShareBoardDTO> list = dao.selectByBound(start, end);
				String navi = dao.getPageNavi(currentPage);
				request.setAttribute("navi", navi);
				request.setAttribute("list", list);
				request.getRequestDispatcher("/shareBoard/list.jsp").forward(request, response);

				// '맛집 공유 게시판'글쓰기 페이지로 넘어가기
			} else if (cmd.equals("/shareWrite.board")) {
				response.sendRedirect("/shareBoard/write.jsp");

				// '맛집 공유 게시판' 글 작성할때 db로 저장시키는 역할.
			} else if (cmd.equals("/shareProc.board")) {
				HttpSession session = request.getSession();
				String writer = (String) request.getSession().getAttribute("loginID"); 
				String title = request.getParameter("title");
				String content = request.getParameter("content");

				int result = dao.insert(new ShareBoardDTO(0, writer, title, content, null, 0));
				if (result > 0) {
					response.sendRedirect("/list.board?cpage=1");
				}

				// 게시글 클릭 시 보여주는 페이지
			} else if (cmd.equals("/detail.board")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				ShareBoardDTO dto = dao.selectBySeq(seq);
				int result = dao.addViewCount(seq);
				int replycount = dao.totalReplyCount(seq);
				
				List<ShareBoardReplyDTO> list = dao.selectReplyByPseq(seq);
				request.setAttribute("dto", dto);
				request.setAttribute("list", list);
				request.setAttribute("replycount", replycount);
				request.getRequestDispatcher("/shareBoard/detail.jsp").forward(request, response);

				// 게시글 삭제 기능
			} else if (cmd.equals("/delete.board")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				int result1 = dao.deleteBySeq(seq);  // 게시글 삭제
				int result2 = dao.deleteByPseq(seq); // 게시글의 댓글삭제
				response.sendRedirect("/list.board?cpage=1");

				// 게시글 수정 버튼 눌렀을 때 해당 seq 데이터 가지고 'summernote'수정페이지로 이동
			} else if (cmd.equals("/modify.board")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				ShareBoardDTO dto = dao.selectBySeq(seq);
				request.setAttribute("dto", dto);
				request.getRequestDispatcher("/shareBoard/modify.jsp").forward(request, response);

				// 'summernote'로 글 수정 하는 페이지
			} else if (cmd.equals("/modifyProc.board")) {
				String title = request.getParameter("title");
				String contents = request.getParameter("content");
				int seq = Integer.parseInt(request.getParameter("seq"));
				int result = dao.modify(seq, title, contents);
				response.sendRedirect("/detail.board?seq=" + seq);

				//// 댓글 등록
			} else if (cmd.equals("/reply.board")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				HttpSession session = request.getSession();
				String writer = (String)request.getSession().getAttribute("loginID");
				String reply = request.getParameter("reply");
				int result = dao.insertReply(seq, writer, reply);
				response.sendRedirect("/detail.board?seq=" + seq);

				/// 댓글 삭제
			} else if (cmd.equals("/deleteReply.board")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				int result = dao.deleteReplyBySeq(seq);
				response.sendRedirect("/detail.board?seq=" + seq);

				/// 댓글 수정
			} else if (cmd.equals("/modifyReply.board")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				int pseq = Integer.parseInt(request.getParameter("pseq"));
				String reply = request.getParameter("reply");
				int result = dao.modifyReply(seq, pseq, reply);
				response.sendRedirect("/detail.board?seq=" + seq);

				/// 게시글 검색 기능
			} else if (cmd.equals("/search.board")) {
				String cpage = request.getParameter("cpage");
				if (cpage == null)
					cpage = "1";

				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				String keyword = request.getParameter("keyword");
				String searchWord = request.getParameter("searchWord");

				int pageTotalCount = dao.getPageTotalCountBySearch(keyword, searchWord);

				if (currentPage < 1) {
					currentPage = 1;
				}
				if (currentPage > pageTotalCount) {
					currentPage = pageTotalCount;
				}

				int start = currentPage * 10 - (10 - 1);
				int end = currentPage * 10;

				List<ShareBoardDTO> list = dao.searchBoard(start, end, keyword, searchWord);
				String navi = dao.getPageNaviBySearch(currentPage, keyword, searchWord);
				request.setAttribute("navi", navi);
				request.setAttribute("list", list);
				request.getRequestDispatcher("/shareBoard/searchList.jsp").forward(request, response);
	
			// 파일 업로드
			} else if (cmd.equals("/uploadFile.board")) {
				String fileRoot = "C:\\Users\\이상명\\Desktop\\imguser"; // 이미지 업로드할 외부 경로
				int size = 10 * 1024 * 1024; // 업로드 사이즈 제한 10M 이하

				MultipartRequest multi = new MultipartRequest(request, fileRoot, size, "UTF8",new DefaultFileRenamePolicy()); // 파일 저장
				Enumeration files = multi.getFileNames();
				String file = (String) files.nextElement();
				String fileName = multi.getFilesystemName(file); // 저장되는 이름
				String targetPath = "/img/" + fileName; // ajax에 보낼 저장경로 url
				Gson g = new Gson();
				String url = g.toJson(targetPath); // 저장경로 url json 형태로 변환
				response.setContentType("text/html;charset=utf8;"); // 한글명 파일 처리
				response.getWriter().append(url); // ajax로 전송
				System.out.println("컨트롤러에서 보내는 url : " +  url);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("/error.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
