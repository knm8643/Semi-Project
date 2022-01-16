package kh.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sun.mail.iap.Response;

import kh.web.dao.MemberDAO;
import kh.web.dto.MemberDTO;
import kh.web.utils.EncrpytionUtils;

@WebServlet("*.mem")
public class MemberController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf8");
		
		String requestURI = request.getRequestURI(); // /member/login.mem
		String ctxPath = request.getContextPath();

		String cmd = requestURI.substring(ctxPath.length());

		System.out.println(cmd);

		MemberDAO dao = MemberDAO.getInstance();

		try {
			if(cmd.equals("/signup.mem")) { // 회원가입 폼으로 이동

				response.sendRedirect("member/signup.jsp");

			}else if(cmd.equals("/member/idCheck.mem")) { // 아이디 중복 체크 기능
				String id = request.getParameter("id");
				try {
					boolean result = dao.isIdExist(id);
					response.getWriter().append(String.valueOf(result));
				}catch(Exception e) {
					e.printStackTrace();
					response.sendRedirect("/member/signup.jsp");
//					response.sendRedirect("error.jsp");
				}
			}else if(cmd.equals("/member/signupProc.mem")) {
				String id = request.getParameter("id");
				String pw = EncrpytionUtils.getSHA512(request.getParameter("pw"));
				String name = request.getParameter("name");

				String phone1 = request.getParameter("phone1");
				String phone2 = request.getParameter("phone2");
				String phone3 = request.getParameter("phone3");
				String phone = phone1 + phone2 + phone3;

				String email = request.getParameter("email")+"@"+ request.getParameter("emailweb");
				String zipcode = request.getParameter("zipcode");
				String address1 = request.getParameter("address1");
				String address2 = request.getParameter("address2");
				String ssn = request.getParameter("ssn1") + request.getParameter("ssn2");
				String nickname = request.getParameter("nickname");
				String avgage = null;
				System.out.println(id + " : " + pw + " : " + name + " : " + phone  + " : " + email + " : " + zipcode  + " : " + address1  + " : " + address2);


				// member id key 채번 후 insert
				
				dao.insert(new MemberDTO(id,pw,name,phone,email,zipcode,address1,address2,ssn,nickname,avgage));
				request.getSession().removeAttribute("loginID");
				response.sendRedirect("/index.jsp");
			
				//회원 정보 수정
			}else if(cmd.equals("/updateProc.mem")) {
				String id = request.getParameter("id");
				String pw = EncrpytionUtils.getSHA512(request.getParameter("pw"));
				String name = request.getParameter("name");

				String phone = request.getParameter("phone");

				String email = request.getParameter("email");
				String zipcode = request.getParameter("zipcode");
				String address1 = request.getParameter("address1");
				String address2 = request.getParameter("address2");
				String ssn = request.getParameter("ssn1") + request.getParameter("ssn2");
				String nickname = request.getParameter("nickname");
				String avgage = null; //TODO
				System.out.println(id + " : " + pw + " : " + name + " : " + phone  + " : " + email + " : " + zipcode  + " : " + address1  + " : " + address2);


				// member id key 채번 후 insert
				
				dao.update(new MemberDTO(id,null,name,phone,email,zipcode,address1,address2,ssn,nickname,avgage));
				HttpSession session = request.getSession(); // 서버쪽 세션 금고에
				session.setAttribute("updateYn", "Y");
				response.sendRedirect("/myPage.mem");
				
			}else if(cmd.equals("/login.mem")) {
				
				String id = request.getParameter("id");
				String pw = EncrpytionUtils.getSHA512(request.getParameter("pw"));
				
				boolean result = dao.isLoginAllowed(id,pw);	
				HttpSession session = request.getSession();// 서버쪽 세션 금고에
				String blacklist = dao.blackYn(id);
				System.out.println("blacklist");
				if(result) { // 로그인에 성공했을 경우
					
					session.setAttribute("loginID", id); // loginID라는 키값으로 사용자 ID를 저장
					session.setAttribute("loginFailID", null);//로그인에 성공했을때 아이디를 지워준다
					session.setAttribute("blacklist", blacklist);
					System.out.println("로그인에 성공했습니다.");
					response.sendRedirect("/Main.jsp");
				}else {
					session.setAttribute("loginFailID", id);//로그인에 실패했을때 실패한 아이디를 넣어준다.
					response.sendRedirect("/index.jsp");
				}
				
			}else if(cmd.equals("/kakaoLogin.mem")) {
				//index.jsp에서 전송한 form(parameter)
				String id = request.getParameter("kakaoid");
				String nickname = request.getParameter("nickname");
				String email = request.getParameter("email");
				String avg_age = request.getParameter("avgage");
				String blacklist = dao.blackYn(id);
				
				//아이디 존재 유무
				boolean result = dao.isIdExist(id);
				if(!result) {
					//아이디가 없으면 카카오로그인후 대동맛지도 회원가입 처리
					dao.kakaoInsert(new MemberDTO(id,email,nickname,avg_age)); //카카오 Insert 
				}
				HttpSession session = request.getSession(); // 서버쪽 세션 금고에
				session.setAttribute("loginFailID", null);//로그인에 성공했을때 아이디를 지워준다
				session.setAttribute("loginID", id);// loginID라는 키값으로 사용자 ID를 저장
				session.setAttribute("blacklist", blacklist);
				System.out.println("로그인에 성공했습니다.");
				response.sendRedirect("/Main.jsp");
				
			}else if(cmd.equals("/logout.mem")) {
				// 접속한 사용자의 key값으로 저장되어 있는 모든 내용을 전부 삭제.
				//request.getSession().invalidate();
				request.getSession().removeAttribute("loginID");
				response.sendRedirect("/Main.jsp");
				
				
			}else if(cmd.equals("/leave.mem")) {
				String id = (String)request.getSession().getAttribute("loginID");
				
				int result = dao.delete(id); // DB에서 삭제
				request.getSession().removeAttribute("loginID"); //세션 정리 삭제
				request.getSession().invalidate(); // 세션에서 삭제
				response.sendRedirect("/Main.jsp"); // 메인페이지로 복귀
			}else if(cmd.equals("/myPage.mem")) {
				
				String id = (String)request.getSession().getAttribute("loginID");
				MemberDTO dto = dao.selectById(id);
				request.setAttribute("dto", dto);
				request.getRequestDispatcher("/member/mypage.jsp").forward(request, response);
			}else if(cmd.equals("id_overlap.mem")) {
				System.out.println("");
				/*	
				 * String id = (String)request.getSession().getAttribute("loginID"); MemberDTO
				 * dto = dao.selectById(id); request.setAttribute("dto", dto);
				 * request.getRequestDispatcher("/member/mypage.jsp").forward(request,
				 * response);
				 */
			}else if(cmd.equals("/searchPw.mem")) { // 아이디 중복 체크 기능
				String id = request.getParameter("id");
				try {
					boolean result = dao.searchPw(id);
					response.getWriter().append(String.valueOf(result));
				}catch(Exception e) {
					e.printStackTrace();
					System.out.println("오류났습니다.");
					response.sendRedirect("/index.jsp");
//					response.sendRedirect("error.jsp");
				}
			} else if(cmd.equals("/sendmap.mem")) {
				response.sendRedirect("like_here.map");
				
			} else if(cmd.equals("/member/editPw.mem")) { // 비밀번호 수정
				String id 	 = (String)request.getSession().getAttribute("loginID");
				String ps 	 = request.getParameter("ps");
				String ordPs = EncrpytionUtils.getSHA512(request.getParameter("ordPs"));
				try {
					String resultMsg = "";
					Boolean result = dao.isLoginAllowed(id,ordPs);// 현재 비밀 번호 확인
					if(result) {
						resultMsg = "loginSucess";
						dao.editPw(ps,id); // pw 변경 처리
					}else {
						resultMsg = "loginFail";
					}
					response.getWriter().append(String.valueOf(resultMsg));
				}catch(Exception e) {
					e.printStackTrace();
					response.sendRedirect("error.jsp");
				}
			}
			 
		}catch(Exception e) {
			e.printStackTrace();
			response.sendRedirect("/index.jsp");
//			response.sendRedirect("/BoardProject/error.html");
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
