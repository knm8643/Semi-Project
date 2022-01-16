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

import kh.web.dao.QnADAO;
import kh.web.dto.QnADTO;
import kh.web.dto.QnAReplyDTO;
import kh.web.statics.Statics;


@WebServlet("*.qna")
public class QnAController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		String uri = request.getRequestURI();
		String ctxPath = request.getContextPath();
		String cmd = uri.substring(ctxPath.length());

		QnADAO dao = QnADAO.getInstance();
		
		try {
			if(cmd.equals("/list.qna")) { // 공지사항 목록
				int currentPage = Integer.parseInt(request.getParameter("cpage")); //DAO로부터 현재페이지 받아오기
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE - (Statics.RECORD_COUNT_PER_PAGE-1);
				int end = currentPage * Statics.RECORD_COUNT_PER_PAGE;
				
				List<QnADTO> list = dao.selectByBound(start,end); // 전체 목록 불러오기
//				System.out.println("제목:"+list.get(0).getTitle());
				String navi = dao.getPageNavi(currentPage); // 페이지 네비게이터
				request.setAttribute("list",list); // 공지사항 게시글 목록
				request.setAttribute("navi",navi); // 페이지 네비게이터
				request.getRequestDispatcher("/ctmBoard/listQnA.jsp").forward(request, response);
			}else if(cmd.equals("/write.qna")) { // 공지사항 작성하기
				response.sendRedirect("/ctmBoard/writeQnA.jsp");
			}else if(cmd.equals("/insert.qna")) { // 작성한 공지사항 db 저장
				HttpSession session = request.getSession();
				String writer = (String)request.getSession().getAttribute("loginID");
				String title = request.getParameter("title"); // 입력할 공지사항 제목
				String contents = request.getParameter("contents"); // 입력할 공지사항 내용
				int result = dao.insert(new QnADTO(0,writer,title,contents,null,0));
				if(result>0) {
					response.sendRedirect("/list.qna?cpage=1");
				}
			}else if(cmd.equals("/detail.qna")) { // 공지사항 글 자세히 보기
				int seq = Integer.parseInt(request.getParameter("seq"));
				QnADTO dto = dao.selectBySeq(seq);
				int result = dao.addViewCount(seq); // 글 조회수
				List<QnAReplyDTO> list = dao.selectCommentByPseq(seq);
				request.setAttribute("dto", dto);
				request.setAttribute("list", list);
				request.getRequestDispatcher("/ctmBoard/detailQnA.jsp").forward(request, response);
			}else if(cmd.equals("/delete.qna")) { // 공지사항 글 삭제
				int seq = Integer.parseInt(request.getParameter("seq"));
//				System.out.println(seq);
				int resultCnt = dao.deleteCommentByPseq(seq);
				int result = dao.delete(seq);
				response.sendRedirect("/list.qna?cpage=1");
			}else if(cmd.equals("/mod.qna")) { // 공지사항 수정화면으로 이동
				int seq = Integer.parseInt(request.getParameter("seq"));
				QnADTO dto = dao.selectBySeq(seq);
				request.setAttribute("dto", dto);
				request.getRequestDispatcher("/ctmBoard/modifyQnA.jsp").forward(request, response);
			}else if(cmd.equals("/modify.qna")) { // 공지사항 수정내용 반영
				int seq = Integer.parseInt(request.getParameter("seq"));
				String title = request.getParameter("title");
				String contents = request.getParameter("contents");
				int result = dao.modify(seq,title,contents);
				response.sendRedirect("/detail.qna?seq="+seq);
			}else if(cmd.equals("/commentIn.qna")) {
				int pseq = Integer.parseInt(request.getParameter("pseq"));
				HttpSession session = request.getSession();
				String writer = (String)request.getSession().getAttribute("loginID");
				String comment = request.getParameter("contents");
				int result = dao.insertComment(pseq, writer, comment);
				response.sendRedirect("/detail.qna?seq="+pseq);
			}else if(cmd.equals("/deleteCnt.qna")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				System.out.println(seq);
				int result = dao.deleteComment(seq);
//				request.setAttribute("result", result);
//				request.getRequestDispatcher("/detail.qna").forward(request, response);
//				response.sendRedirect("/detail.qna?seq="+seq);
			}else if(cmd.equals("/modCnt.qna")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				int pseq = Integer.parseInt(request.getParameter("pseq"));
				String content = request.getParameter("content");
//				System.out.println(seq+" : "+pseq+" : "+content);
				int result = dao.modCnt(seq,pseq,content);
				response.sendRedirect("/detail.qna?seq="+seq);
			}else if(cmd.equals("/uploadFile.qna")) {
				String uploadPath = "C:\\Users\\이상명\\Desktop\\imguser"; // 이미지 업로드할 외부 경로
			    int maxSize = 10 * 1024 * 1024;  // 업로드 사이즈 제한 10M 이하
			    System.out.println(uploadPath);
				MultipartRequest multi = new MultipartRequest(request, uploadPath, maxSize, "UTF8", new DefaultFileRenamePolicy()); //파일 저장
				Enumeration files = multi.getFileNames();
				String file = (String)files.nextElement(); 
				String sysName = multi.getFilesystemName(file); // 저장되는 이름
				String targetPath = "/img/"+sysName; // ajax에 보낼 저장경로 url
				Gson g = new Gson();
				String url = g.toJson(targetPath); // 저장경로 url json 형태로 변환
				System.out.println(targetPath);
				System.out.println(url);
				response.setContentType("text/html;charset=utf8;"); // 한글명 파일 처리
				response.getWriter().append(url); // ajax로 전송

			}else if(cmd.equals("/search.qna")) {
				String cpage = request.getParameter("cpage");
				if(cpage == null)cpage = "1";
				
				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				String option = request.getParameter("option");
				String target = request.getParameter("target");
				
				int pageTotalCount = 0;
				pageTotalCount = dao.getPageTotalCountBySearch(option, target);
				
				if(currentPage < 1) {currentPage = 1;}
				if(currentPage > pageTotalCount) {currentPage = pageTotalCount;}
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE - (Statics.RECORD_COUNT_PER_PAGE-1);
				int end = currentPage *Statics.RECORD_COUNT_PER_PAGE;
				
				List<QnADTO> list = dao.selectBySearch(option, target, start, end);
//				System.out.println(list.get(0).getTitle());
				
				String navi = dao.getPageNaviBySearch(currentPage, target, option);
				
				request.setAttribute("list", list);
				request.setAttribute("navi", navi);
				request.setAttribute("target", target);
				request.setAttribute("option", option);
				request.setAttribute("cpage", cpage);
				request.getRequestDispatcher("/ctmBoard/srcQnA.jsp").forward(request, response);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			response.sendRedirect("error.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
