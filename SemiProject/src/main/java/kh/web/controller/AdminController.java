package kh.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kh.web.dao.MapAdminDAO;
import kh.web.dao.MemberAdminDAO;
import kh.web.dao.NoticeDAO;
import kh.web.dao.QnADAO;
import kh.web.dao.RecipeAdminDAO;
import kh.web.dao.ShareAdminDAO;
import kh.web.dao.WithAdminDAO;
import kh.web.dto.Map_DTO;
import kh.web.dto.MemberAdminDTO;
import kh.web.dto.NoticeDTO;
import kh.web.dto.NoticeReplyDTO;
import kh.web.dto.QnADTO;
import kh.web.dto.QnAReplyDTO;
import kh.web.dto.RecipeBoardDTO;
import kh.web.dto.RecipeBoardReplyDTO;
import kh.web.dto.ShareBoardDTO;
import kh.web.dto.ShareBoardReplyDTO;
import kh.web.dto.WithBoardDTO;
import kh.web.dto.WithBoardReplyDTO;
import kh.web.statics.Statics;


@WebServlet("*.admin")
public class AdminController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		String uri = request.getRequestURI();
		String ctxPath = request.getContextPath();
		String cmd = uri.substring(ctxPath.length());

		NoticeDAO dao = NoticeDAO.getInstance();
		QnADAO qdao = QnADAO.getInstance();
		MemberAdminDAO mdao = MemberAdminDAO.getInstance();
		MapAdminDAO mapdao = MapAdminDAO.getInstance();
		ShareAdminDAO shdao = ShareAdminDAO.getInstance();
		RecipeAdminDAO rcdao = RecipeAdminDAO.getInstance();
		WithAdminDAO wdao = WithAdminDAO.getInstance();
		
		try {
			if(cmd.equals("/notice.admin")) {
				int currentPage = Integer.parseInt(request.getParameter("cpage")); // 현재페이지 받아오기
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<NoticeDTO> list = dao.selectByBound(start,end); // 전체 목록 불러오기
	//			System.out.println("제목:"+list.get(0).getTitle());
				String navi = dao.getPageNaviAdmin(currentPage); // 페이지 네비게이터
				request.setAttribute("list",list); // 공지사항 게시글 목록
				request.setAttribute("navi",navi); // 페이지 네비게이터
				request.getRequestDispatcher("/admin/adminNotice.jsp").forward(request, response);
			}else if(cmd.equals("/mod.admin")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				NoticeDTO dto = dao.selectBySeq(seq);
				request.setAttribute("dto", dto);
				request.getRequestDispatcher("/admin/adminModify.jsp").forward(request, response);
			}else if(cmd.equals("/deleteArticle.admin")) {
				String[] ajaxSeq = request.getParameterValues("check");
				int size = ajaxSeq.length;
				for(int i=0; i<size; i++) {
					System.out.println("출력:"+Integer.parseInt(ajaxSeq[i]));
					int result = dao.delete(Integer.parseInt(ajaxSeq[i]));
					int resultCnt = dao.deleteCommentByPseq(Integer.parseInt(ajaxSeq[i]));
				}
				request.getRequestDispatcher("/notice.admin?cpage=1").forward(request, response);
			}else if(cmd.equals("/modify.admin")) { // 공지사항 수정내용 반영
				int seq = Integer.parseInt(request.getParameter("seq"));
				String title = request.getParameter("title");
				String contents = request.getParameter("contents");
				int result = dao.modify(seq,title,contents);
				if (result>0) {
					response.sendRedirect("/notice.admin?cpage=1");
				}
			}else if(cmd.equals("/write.admin")) { // 공지사항 작성하기
				response.sendRedirect("/admin/adminWrite.jsp");
			}else if(cmd.equals("/insert.admin")) { // 작성한 공지사항 db 저장 
				String writer = "관리자";//(String)request.getSession().getAttribute("loginID");
				String title = request.getParameter("title"); // 입력할 공지사항 제목
				String contents = request.getParameter("contents"); // 입력할 공지사항 내용
				int result = dao.insert(new NoticeDTO(0,writer,title,contents,null,0));
				if(result>0) {
					response.sendRedirect("/notice.admin?cpage=1");
				}
			}else if(cmd.equals("/searchNotice.admin")) {
				String cpage = request.getParameter("cpage");
				if(cpage == null)cpage = "1";
				
				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				String option = request.getParameter("option");
				String target = request.getParameter("target");
				
				int pageTotalCount = 0;
				pageTotalCount = dao.getPageTotalCountBySearch(option, target);
				
				if(currentPage < 1) {currentPage = 1;}
				if(currentPage > pageTotalCount) {currentPage = pageTotalCount;}
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage *Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<NoticeDTO> list = dao.selectBySearch(option, target, start, end);
//				System.out.println(list.get(0).getTitle());
				
				String navi = dao.getPageNaviAdminBySearch(currentPage, target, option);
				
				request.setAttribute("list", list);
				request.setAttribute("navi", navi);
				request.setAttribute("target", target);
				request.setAttribute("option", option);
				request.setAttribute("cpage", cpage);
				request.getRequestDispatcher("/admin/srcAdminNotice.jsp").forward(request, response);
			}else if(cmd.equals("/noticeReply.admin")) {
				int currentPage = Integer.parseInt(request.getParameter("cpage")); // 현재페이지 받아오기
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<NoticeReplyDTO> list = dao.selectReplyByBound(start,end); // 전체 목록 불러오기
	//			System.out.println("제목:"+list.get(0).getTitle());
				String navi = dao.getReplyPageNaviAdmin(currentPage); // 페이지 네비게이터
				request.setAttribute("list",list); // 공지사항 게시 댓글 목록
				request.setAttribute("navi",navi); // 페이지 네비게이터
				request.getRequestDispatcher("/admin/adminNoticeReply.jsp").forward(request, response);
			}else if(cmd.equals("/deleteCnt.admin")) {
				String[] ajaxSeq = request.getParameterValues("check");
				int size = ajaxSeq.length;
				for(int i=0; i<size; i++) {
					System.out.println("출력:"+Integer.parseInt(ajaxSeq[i]));
					int result = dao.deleteComment(Integer.parseInt(ajaxSeq[i]));
				}
				request.getRequestDispatcher("/noticeReply.admin?cpage=1").forward(request, response);
			}else if(cmd.equals("/searchNoticeReply.admin")) {
				String cpage = request.getParameter("cpage");
				if(cpage == null)cpage = "1";
				
				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				String option = request.getParameter("option");
				String target = request.getParameter("target");
				
				int pageTotalCount = 0;
				pageTotalCount = dao.getPageTotalCountBySearchReply(option, target);
				
				if(currentPage < 1) {currentPage = 1;}
				if(currentPage > pageTotalCount) {currentPage = pageTotalCount;}
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage *Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<NoticeReplyDTO> list = dao.selectBySearchReply(option, target, start, end);
//				System.out.println(list.get(0).getTitle());
				
				String navi = dao.getPageNaviAdminBySearchReply(currentPage, target, option);
				
				request.setAttribute("list", list);
				request.setAttribute("navi", navi);
				request.setAttribute("target", target);
				request.setAttribute("option", option);
				request.setAttribute("cpage", cpage);
				request.getRequestDispatcher("/admin/srcAdminNoticeReply.jsp").forward(request, response);
			}else if(cmd.equals("/qna.admin")) {
				int currentPage = Integer.parseInt(request.getParameter("cpage")); // 현재페이지 받아오기
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<QnADTO> list = qdao.selectByBound(start,end); // 전체 목록 불러오기
	//			System.out.println("제목:"+list.get(0).getTitle());
				String navi = qdao.getPageNaviAdmin(currentPage); // 페이지 네비게이터
				request.setAttribute("list",list); // 공지사항 게시글 목록
				request.setAttribute("navi",navi); // 페이지 네비게이터
				request.getRequestDispatcher("/admin/adminQnA.jsp").forward(request, response);
			}else if(cmd.equals("/modQnA.admin")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				QnADTO dto = qdao.selectBySeq(seq);
				request.setAttribute("dto", dto);
				request.getRequestDispatcher("/admin/adminModifyQnA.jsp").forward(request, response);
			}else if(cmd.equals("/modifyQnA.admin")) { // 공지사항 수정내용 반영
				int seq = Integer.parseInt(request.getParameter("seq"));
				String title = request.getParameter("title");
				String contents = request.getParameter("contents");
				int result = qdao.modify(seq,title,contents);
				if (result>0) {
					response.sendRedirect("/qna.admin?cpage=1");
				}
			}else if(cmd.equals("/deleteQnA.admin")) {
				String[] ajaxSeq = request.getParameterValues("check");
				int size = ajaxSeq.length;
				for(int i=0; i<size; i++) {
					System.out.println("출력:"+Integer.parseInt(ajaxSeq[i]));
					int result = qdao.delete(Integer.parseInt(ajaxSeq[i]));
					int resultCnt = qdao.deleteCommentByPseq(Integer.parseInt(ajaxSeq[i]));
				}
				request.getRequestDispatcher("/qna.admin?cpage=1").forward(request, response);
			}else if(cmd.equals("/searchQnA.admin")) {
				String cpage = request.getParameter("cpage");
				if(cpage == null)cpage = "1";
				
				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				String option = request.getParameter("option");
				String target = request.getParameter("target");
				
				int pageTotalCount = 0;
				pageTotalCount = qdao.getPageTotalCountBySearch(option, target);
				
				if(currentPage < 1) {currentPage = 1;}
				if(currentPage > pageTotalCount) {currentPage = pageTotalCount;}
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage *Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<QnADTO> list = qdao.selectBySearch(option, target, start, end);
//				System.out.println(list.get(0).getTitle());
				
				String navi = qdao.getPageNaviAdminBySearch(currentPage, target, option);
				
				request.setAttribute("list", list);
				request.setAttribute("navi", navi);
				request.setAttribute("target", target);
				request.setAttribute("option", option);
				request.setAttribute("cpage", cpage);
				request.getRequestDispatcher("/admin/srcAdminQnA.jsp").forward(request, response);
			}else if(cmd.equals("/qnaReply.admin")) {
				int currentPage = Integer.parseInt(request.getParameter("cpage")); // 현재페이지 받아오기
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<QnAReplyDTO> list = qdao.selectReplyByBound(start,end); // 전체 목록 불러오기
	//			System.out.println("제목:"+list.get(0).getTitle());
				String navi = qdao.getReplyPageNaviAdmin(currentPage); // 페이지 네비게이터
				request.setAttribute("list",list); // 공지사항 게시 댓글 목록
				request.setAttribute("navi",navi); // 페이지 네비게이터
				request.getRequestDispatcher("/admin/adminQnAReply.jsp").forward(request, response);
			}else if(cmd.equals("/deleteQnACnt.admin")) {
				String[] ajaxSeq = request.getParameterValues("check");
				int size = ajaxSeq.length;
				for(int i=0; i<size; i++) {
					System.out.println("출력:"+Integer.parseInt(ajaxSeq[i]));
					int result = qdao.deleteComment(Integer.parseInt(ajaxSeq[i]));
				}
				request.getRequestDispatcher("/qnaReply.admin?cpage=1").forward(request, response);
			}else if(cmd.equals("/searchQnAReply.admin")) {
				String cpage = request.getParameter("cpage");
				if(cpage == null)cpage = "1";
				
				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				String option = request.getParameter("option");
				String target = request.getParameter("target");
				
				int pageTotalCount = 0;
				pageTotalCount = qdao.getPageTotalCountBySearchReply(option, target);
				
				if(currentPage < 1) {currentPage = 1;}
				if(currentPage > pageTotalCount) {currentPage = pageTotalCount;}
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage *Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<QnAReplyDTO> list = qdao.selectBySearchReply(option, target, start, end);
//				System.out.println(list.get(0).getTitle());
				
				String navi = qdao.getPageNaviAdminBySearchReply(currentPage, target, option);
				
				request.setAttribute("list", list);
				request.setAttribute("navi", navi);
				request.setAttribute("target", target);
				request.setAttribute("option", option);
				request.setAttribute("cpage", cpage);
				request.getRequestDispatcher("/admin/srcAdminQnAReply.jsp").forward(request, response);
			}else if(cmd.equals("/list.admin")) {
				response.sendRedirect("/admin/adminSideBar.jsp");
			}else if(cmd.equals("/member.admin")) {
				int currentPage = Integer.parseInt(request.getParameter("cpage")); // 현재페이지 받아오기
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<MemberAdminDTO> list = mdao.selectByBound(start,end); // 전체 목록 불러오기
	//			System.out.println("제목:"+list.get(0).getTitle());
				String navi = mdao.getPageNaviAdmin(currentPage); // 페이지 네비게이터
				request.setAttribute("list",list); // 공지사항 게시글 목록
				request.setAttribute("navi",navi); // 페이지 네비게이터
				request.getRequestDispatcher("/admin/adminMember.jsp").forward(request, response);
			}else if(cmd.equals("/deleteMember.admin")) {
				String[] ajaxSeq = request.getParameterValues("check");
				int size = ajaxSeq.length;
				for(int i=0; i<size; i++) {
					System.out.println("출력:"+ajaxSeq[i]);
					int result = mdao.delete(ajaxSeq[i]);
				}
				request.getRequestDispatcher("/member.admin?cpage=1").forward(request, response);
			}else if(cmd.equals("/searchMember.admin")) {
				String cpage = request.getParameter("cpage");
				if(cpage == null)cpage = "1";
				
				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				String option = request.getParameter("option");
				String target = request.getParameter("target");
				System.out.println(cpage+option+target);
				int pageTotalCount = 0;
				pageTotalCount = mdao.getPageTotalCountBySearch(option, target);
				
				if(currentPage < 1) {currentPage = 1;}
				if(currentPage > pageTotalCount) {currentPage = pageTotalCount;}
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage *Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<MemberAdminDTO> list = mdao.selectBySearch(option, target, start, end);
				
				String navi = mdao.getPageNaviAdminBySearch(currentPage, target, option);
				
				request.setAttribute("list", list);
				request.setAttribute("navi", navi);
				request.setAttribute("target", target);
				request.setAttribute("option", option);
				request.setAttribute("cpage", cpage);
				request.getRequestDispatcher("/admin/srcAdminMember.jsp").forward(request, response);
			}else if(cmd.equals("/rest.admin")) { // 맛집 정보 관리 목록
				int currentPage = Integer.parseInt(request.getParameter("cpage")); // 현재페이지 받아오기
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<Map_DTO> list = mapdao.selectByBound(start,end); // 전체 목록 불러오기
	//			System.out.println("제목:"+list.get(0).getTitle());
				String navi = mapdao.getPageNaviAdmin(currentPage); // 페이지 네비게이터
				request.setAttribute("list",list); // 공지사항 게시글 목록
				request.setAttribute("navi",navi); // 페이지 네비게이터
				request.getRequestDispatcher("/admin/adminRest.jsp").forward(request, response);
			}else if(cmd.equals("/deleteRest.admin")) {
				String[] ajaxSeq = request.getParameterValues("check");
				int size = ajaxSeq.length;
				for(int i=0; i<size; i++) {
					System.out.println("출력:"+ajaxSeq[i]);
					int result = mapdao.delete(Integer.parseInt(ajaxSeq[i]));
				}
				request.getRequestDispatcher("/rest.admin?cpage=1").forward(request, response);
			}else if(cmd.equals("/searchRest.admin")) {
				String cpage = request.getParameter("cpage");
				if(cpage == null)cpage = "1";
				
				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				String option = request.getParameter("option");
				String target = request.getParameter("target");
				System.out.println(cpage+option+target);
				int pageTotalCount = 0;
				pageTotalCount = mapdao.getPageTotalCountBySearch(option, target);
				
				if(currentPage < 1) {currentPage = 1;}
				if(currentPage > pageTotalCount) {currentPage = pageTotalCount;}
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage *Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<Map_DTO> list = mapdao.selectBySearch(option, target, start, end);
				
				String navi = mapdao.getPageNaviAdminBySearch(currentPage, target, option);
				
				request.setAttribute("list", list);
				request.setAttribute("navi", navi);
				request.setAttribute("target", target);
				request.setAttribute("option", option);
				request.setAttribute("cpage", cpage);
				request.getRequestDispatcher("/admin/srcAdminRest.jsp").forward(request, response);
				
				//맛집공유 게시판 관리
			}else if(cmd.equals("/share.admin")) {
				int currentPage = Integer.parseInt(request.getParameter("cpage")); // 현재페이지 받아오기
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<ShareBoardDTO> list = shdao.selectByBound(start,end); // 전체 목록 불러오기
	//			System.out.println("제목:"+list.get(0).getTitle());
				String navi = shdao.getPageNaviAdmin(currentPage); // 페이지 네비게이터
				request.setAttribute("list",list); // 공지사항 게시글 목록
				request.setAttribute("navi",navi); // 페이지 네비게이터
				request.getRequestDispatcher("/admin/adminShare.jsp").forward(request, response);
			}else if(cmd.equals("/modShare.admin")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				ShareBoardDTO dto = shdao.selectBySeq(seq);
				request.setAttribute("dto", dto);
				request.getRequestDispatcher("/admin/adminModifyShare.jsp").forward(request, response);
			}else if(cmd.equals("/modifyShare.admin")) { // 맛집공유 수정내용 반영
				int seq = Integer.parseInt(request.getParameter("seq"));
				String title = request.getParameter("title");
				String contents = request.getParameter("contents");
				int result = shdao.modify(seq,title,contents);
				if (result>0) {
					response.sendRedirect("/share.admin?cpage=1");
				}
			}else if(cmd.equals("/deleteShare.admin")) {
				String[] ajaxSeq = request.getParameterValues("check");
				int size = ajaxSeq.length;
				for(int i=0; i<size; i++) {
					System.out.println("출력:"+Integer.parseInt(ajaxSeq[i]));
					int result = shdao.delete(Integer.parseInt(ajaxSeq[i]));
					int resultCnt = shdao.deleteCommentByPseq(Integer.parseInt(ajaxSeq[i]));
				}
				request.getRequestDispatcher("/share.admin?cpage=1").forward(request, response);
			}else if(cmd.equals("/searchShare.admin")) {
				String cpage = request.getParameter("cpage");
				if(cpage == null)cpage = "1";
				
				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				String option = request.getParameter("option");
				String target = request.getParameter("target");
				
				int pageTotalCount = 0;
				pageTotalCount = shdao.getPageTotalCountBySearch(option, target);
				
				if(currentPage < 1) {currentPage = 1;}
				if(currentPage > pageTotalCount) {currentPage = pageTotalCount;}
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage *Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<ShareBoardDTO> list = shdao.selectBySearch(option, target, start, end);
//				System.out.println(list.get(0).getTitle());
				
				String navi = shdao.getPageNaviAdminBySearch(currentPage, target, option);
				
				request.setAttribute("list", list);
				request.setAttribute("navi", navi);
				request.setAttribute("target", target);
				request.setAttribute("option", option);
				request.setAttribute("cpage", cpage);
				request.getRequestDispatcher("/admin/srcAdminShare.jsp").forward(request, response);
			}else if(cmd.equals("/shareReply.admin")) {
				int currentPage = Integer.parseInt(request.getParameter("cpage")); // 현재페이지 받아오기
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<ShareBoardReplyDTO> list = shdao.selectReplyByBound(start,end); // 전체 목록 불러오기
	//			System.out.println("제목:"+list.get(0).getTitle());
				String navi = shdao.getReplyPageNaviAdmin(currentPage); // 페이지 네비게이터
				request.setAttribute("list",list); // 공지사항 게시 댓글 목록
				request.setAttribute("navi",navi); // 페이지 네비게이터
				request.getRequestDispatcher("/admin/adminShareReply.jsp").forward(request, response);
			}else if(cmd.equals("/deleteShareCnt.admin")) {
				String[] ajaxSeq = request.getParameterValues("check");
				int size = ajaxSeq.length;
				for(int i=0; i<size; i++) {
					System.out.println("출력:"+Integer.parseInt(ajaxSeq[i]));
					int result = shdao.deleteComment(Integer.parseInt(ajaxSeq[i]));
				}
				request.getRequestDispatcher("/shareReply.admin?cpage=1").forward(request, response);
			}else if(cmd.equals("/searchShareReply.admin")) {
				String cpage = request.getParameter("cpage");
				if(cpage == null)cpage = "1";
				
				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				String option = request.getParameter("option");
				String target = request.getParameter("target");
				
				int pageTotalCount = 0;
				pageTotalCount = shdao.getPageTotalCountBySearchReply(option, target);
				
				if(currentPage < 1) {currentPage = 1;}
				if(currentPage > pageTotalCount) {currentPage = pageTotalCount;}
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage *Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<ShareBoardReplyDTO> list = shdao.selectBySearchReply(option, target, start, end);
//				System.out.println(list.get(0).getTitle());
				
				String navi = shdao.getPageNaviAdminBySearchReply(currentPage, target, option);
				
				request.setAttribute("list", list);
				request.setAttribute("navi", navi);
				request.setAttribute("target", target);
				request.setAttribute("option", option);
				request.setAttribute("cpage", cpage);
				request.getRequestDispatcher("/admin/srcAdminShareReply.jsp").forward(request, response);
				
				
			// 레시피 게시판 관리기능
			}else if(cmd.equals("/recipe.admin")) {
				int currentPage = Integer.parseInt(request.getParameter("cpage")); // 현재페이지 받아오기
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<RecipeBoardDTO> list = rcdao.selectByBound(start,end); // 전체 목록 불러오기
	//			System.out.println("제목:"+list.get(0).getTitle());
				String navi = rcdao.getPageNaviAdmin(currentPage); // 페이지 네비게이터
				request.setAttribute("list",list); // 공지사항 게시글 목록
				request.setAttribute("navi",navi); // 페이지 네비게이터
				request.getRequestDispatcher("/admin/adminRecipe.jsp").forward(request, response);
			}else if(cmd.equals("/modRecipe.admin")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				RecipeBoardDTO dto = rcdao.selectBySeq(seq);
				request.setAttribute("dto", dto);
				request.getRequestDispatcher("/admin/adminModifyRecipe.jsp").forward(request, response);
			}else if(cmd.equals("/modifyRecipe.admin")) { // 맛집공유 수정내용 반영
				int seq = Integer.parseInt(request.getParameter("seq"));
				String title = request.getParameter("title");
				String contents = request.getParameter("contents");
				int result = rcdao.modify(seq,title,contents);
				if (result>0) {
					response.sendRedirect("/recipe.admin?cpage=1");
				}
			}else if(cmd.equals("/deleteRecipe.admin")) {
				String[] ajaxSeq = request.getParameterValues("check");
				int size = ajaxSeq.length;
				for(int i=0; i<size; i++) {
					System.out.println("출력:"+Integer.parseInt(ajaxSeq[i]));
					int result = rcdao.delete(Integer.parseInt(ajaxSeq[i]));
					int resultCnt = rcdao.deleteCommentByPseq(Integer.parseInt(ajaxSeq[i]));
				}
				request.getRequestDispatcher("/recipe.admin?cpage=1").forward(request, response);
			}else if(cmd.equals("/searchRecipe.admin")) {
				String cpage = request.getParameter("cpage");
				if(cpage == null)cpage = "1";
				
				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				String option = request.getParameter("option");
				String target = request.getParameter("target");
				
				int pageTotalCount = 0;
				pageTotalCount = rcdao.getPageTotalCountBySearch(option, target);
				
				if(currentPage < 1) {currentPage = 1;}
				if(currentPage > pageTotalCount) {currentPage = pageTotalCount;}
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage *Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<RecipeBoardDTO> list = rcdao.selectBySearch(option, target, start, end);
//				System.out.println(list.get(0).getTitle());
				
				String navi = rcdao.getPageNaviAdminBySearch(currentPage, target, option);
				
				request.setAttribute("list", list);
				request.setAttribute("navi", navi);
				request.setAttribute("target", target);
				request.setAttribute("option", option);
				request.setAttribute("cpage", cpage);
				request.getRequestDispatcher("/admin/srcAdminRecipe.jsp").forward(request, response);
			}else if(cmd.equals("/recipeReply.admin")) {
				int currentPage = Integer.parseInt(request.getParameter("cpage")); // 현재페이지 받아오기
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<RecipeBoardReplyDTO> list = rcdao.selectReplyByBound(start,end); // 전체 목록 불러오기
	//			System.out.println("제목:"+list.get(0).getTitle());
				String navi = rcdao.getReplyPageNaviAdmin(currentPage); // 페이지 네비게이터
				request.setAttribute("list",list); // 공지사항 게시 댓글 목록
				request.setAttribute("navi",navi); // 페이지 네비게이터
				request.getRequestDispatcher("/admin/adminRecipeReply.jsp").forward(request, response);
			}else if(cmd.equals("/deleteRecipeCnt.admin")) {
				String[] ajaxSeq = request.getParameterValues("check");
				int size = ajaxSeq.length;
				for(int i=0; i<size; i++) {
					System.out.println("출력:"+Integer.parseInt(ajaxSeq[i]));
					int result = rcdao.deleteComment(Integer.parseInt(ajaxSeq[i]));
				}
				request.getRequestDispatcher("/recipeReply.admin?cpage=1").forward(request, response);
			}else if(cmd.equals("/searchRecipeReply.admin")) {
				String cpage = request.getParameter("cpage");
				if(cpage == null)cpage = "1";
				
				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				String option = request.getParameter("option");
				String target = request.getParameter("target");
				
				int pageTotalCount = 0;
				pageTotalCount = rcdao.getPageTotalCountBySearchReply(option, target);
				
				if(currentPage < 1) {currentPage = 1;}
				if(currentPage > pageTotalCount) {currentPage = pageTotalCount;}
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage *Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<RecipeBoardReplyDTO> list = rcdao.selectBySearchReply(option, target, start, end);
//				System.out.println(list.get(0).getTitle());
				
				String navi = rcdao.getPageNaviAdminBySearchReply(currentPage, target, option);
				
				request.setAttribute("list", list);
				request.setAttribute("navi", navi);
				request.setAttribute("target", target);
				request.setAttribute("option", option);
				request.setAttribute("cpage", cpage);
				request.getRequestDispatcher("/admin/srcAdminRecipeReply.jsp").forward(request, response);
			// 같이 먹을 사람 게시판 관리기능
			}else if(cmd.equals("/with.admin")) {
				int currentPage = Integer.parseInt(request.getParameter("cpage")); // 현재페이지 받아오기
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<WithBoardDTO> list = wdao.selectByBound(start,end); // 전체 목록 불러오기
	//			System.out.println("제목:"+list.get(0).getTitle());
				String navi = wdao.getPageNaviAdmin(currentPage); // 페이지 네비게이터
				request.setAttribute("list",list); // 공지사항 게시글 목록
				request.setAttribute("navi",navi); // 페이지 네비게이터
				request.getRequestDispatcher("/admin/adminWith.jsp").forward(request, response);
			}else if(cmd.equals("/modWith.admin")) {
				int seq = Integer.parseInt(request.getParameter("seq"));
				WithBoardDTO dto = wdao.selectBySeq(seq);
				request.setAttribute("dto", dto);
				request.getRequestDispatcher("/admin/adminModifyWith.jsp").forward(request, response);
			}else if(cmd.equals("/modifyWith.admin")) { // 맛집공유 수정내용 반영
				int seq = Integer.parseInt(request.getParameter("seq"));
				String title = request.getParameter("title");
				String contents = request.getParameter("contents");
				int result = wdao.modify(seq,title,contents);
				if (result>0) {
					response.sendRedirect("/with.admin?cpage=1");
				}
			}else if(cmd.equals("/deleteWith.admin")) {
				String[] ajaxSeq = request.getParameterValues("check");
				int size = ajaxSeq.length;
				for(int i=0; i<size; i++) {
					System.out.println("출력:"+Integer.parseInt(ajaxSeq[i]));
					int result = wdao.delete(Integer.parseInt(ajaxSeq[i]));
					int resultCnt = wdao.deleteCommentByPseq(Integer.parseInt(ajaxSeq[i]));
				}
				request.getRequestDispatcher("/with.admin?cpage=1").forward(request, response);
			}else if(cmd.equals("/searchWith.admin")) {
				String cpage = request.getParameter("cpage");
				if(cpage == null)cpage = "1";
				
				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				String option = request.getParameter("option");
				String target = request.getParameter("target");
				
				int pageTotalCount = 0;
				pageTotalCount = wdao.getPageTotalCountBySearch(option, target);
				
				if(currentPage < 1) {currentPage = 1;}
				if(currentPage > pageTotalCount) {currentPage = pageTotalCount;}
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage *Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<WithBoardDTO> list = wdao.selectBySearch(option, target, start, end);
//				System.out.println(list.get(0).getTitle());
				
				String navi = wdao.getPageNaviAdminBySearch(currentPage, target, option);
				
				request.setAttribute("list", list);
				request.setAttribute("navi", navi);
				request.setAttribute("target", target);
				request.setAttribute("option", option);
				request.setAttribute("cpage", cpage);
				request.getRequestDispatcher("/admin/srcAdminWith.jsp").forward(request, response);
			}else if(cmd.equals("/withReply.admin")) {
				int currentPage = Integer.parseInt(request.getParameter("cpage")); // 현재페이지 받아오기
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<WithBoardReplyDTO> list = wdao.selectReplyByBound(start,end); // 전체 목록 불러오기
	//			System.out.println("제목:"+list.get(0).getTitle());
				String navi = wdao.getReplyPageNaviAdmin(currentPage); // 페이지 네비게이터
				request.setAttribute("list",list); // 공지사항 게시 댓글 목록
				request.setAttribute("navi",navi); // 페이지 네비게이터
				request.getRequestDispatcher("/admin/adminWithReply.jsp").forward(request, response);
			}else if(cmd.equals("/deleteWithCnt.admin")) {
				String[] ajaxSeq = request.getParameterValues("check");
				int size = ajaxSeq.length;
				for(int i=0; i<size; i++) {
					System.out.println("출력:"+Integer.parseInt(ajaxSeq[i]));
					int result = wdao.deleteComment(Integer.parseInt(ajaxSeq[i]));
				}
				request.getRequestDispatcher("/withReply.admin?cpage=1").forward(request, response);
			}else if(cmd.equals("/searchWithReply.admin")) {
				String cpage = request.getParameter("cpage");
				if(cpage == null)cpage = "1";
				
				int currentPage = Integer.parseInt(request.getParameter("cpage"));
				String option = request.getParameter("option");
				String target = request.getParameter("target");
				
				int pageTotalCount = 0;
				pageTotalCount = wdao.getPageTotalCountBySearchReply(option, target);
				
				if(currentPage < 1) {currentPage = 1;}
				if(currentPage > pageTotalCount) {currentPage = pageTotalCount;}
				
				int start = currentPage * Statics.RECORD_COUNT_PER_PAGE_ADMIN - (Statics.RECORD_COUNT_PER_PAGE_ADMIN-1);
				int end = currentPage *Statics.RECORD_COUNT_PER_PAGE_ADMIN;
				
				List<WithBoardReplyDTO> list = wdao.selectBySearchReply(option, target, start, end);
//				System.out.println(list.get(0).getTitle());
				
				String navi = wdao.getPageNaviAdminBySearchReply(currentPage, target, option);
				
				request.setAttribute("list", list);
				request.setAttribute("navi", navi);
				request.setAttribute("target", target);
				request.setAttribute("option", option);
				request.setAttribute("cpage", cpage);
				request.getRequestDispatcher("/admin/srcAdminWithReply.jsp").forward(request, response);
			}else if(cmd.equals("/blacklist.admin")) {
				String[] member_id = request.getParameterValues("member_id");
				for(int j=0; j < member_id.length; j++) {
					String[] blackYn = request.getParameterValues("blacklist"+member_id[j]);
					for(int i=0; i < blackYn.length; i++) {
						//System.out.println(member_id.length + " : " + blackYn.length);
						System.out.println("id:"+member_id[j]);
						System.out.println("yn:"+blackYn[i]);
						int result = mdao.blacklist(member_id[j], blackYn[i]);
					}
				}
				request.getRequestDispatcher("/member.admin?cpage=1").forward(request, response);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
