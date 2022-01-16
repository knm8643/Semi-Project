package kh.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kh.web.dao.Map_DAO;
import kh.web.dto.Map_DTO;

@WebServlet("*.map")
public class Map_controller extends HttpServlet {


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf8"); // 한글패치

		String requestURI = request.getRequestURI();
		String ctxPath = request.getContextPath();
		String cmd = requestURI.substring(ctxPath.length());

		System.out.println("(Map_Controller)클라이언트가 입력한 값 : " + cmd);
		Map_DAO dao_map = Map_DAO.getIntance();
		Map_DTO dto_map = new Map_DTO();

		try {
			if(cmd.equals("/My_MapPage.map")) {	// 나만의 맛집 페이지로 이동 
				// String id = request.getQueryString(); id를 가지고 오는 코드 같은데 null값이 떠서 아래 두 코드로 변경합니다.
				HttpSession session = request.getSession();
				String id = (String)request.getSession().getAttribute("loginID");
				System.out.println("사용자의 아이디값 : " + id);
				
				List<Map_DTO> list = dao_map.selectAll(id);
				request.setAttribute("list", list);
				request.getRequestDispatcher("/my_map/My_MapPage.jsp").forward(request, response);

			}else if(cmd.equals("/like_here.map")) {  // 사용자가 map에서 등록을 눌렀을 때 값을 자동 저장 
				for(int i=1; i<16; i++) {  // 리스트별로 출력 
					String map = request.getParameter("here_map_" + i);
					if(map == null) continue;  // 사용자가 장소등록을 누른것외에 나머지는 다 null 값이기에 null 값을 제외한 값만 출력 
					String place_name = request.getParameter("place_name_" + i);
					String address_name = request.getParameter("address_name_" + i);
					String tel = request.getParameter("tel_" + i);
					String user = request.getParameter("user");

					System.out.println("(like_here)클라이언트가 누른정보 Number "+ i +":" + map + ":" + place_name + ":" + address_name + ":" +tel + ":" + user);	
					dao_map.insert(new Map_DTO(0,place_name,address_name,tel,user));  // 가게 전화번호가 없는 경우가 있는데 제대로 저장을 하려면 sql에서 테이블에 디폴트값 기입해야함
					//					List<Map_DTO> list = dao_map.selectAll(user);
					//								        request.setAttribute("list", list);
					//										request.getRequestDispatcher("/my_map/My_MapPage.map").forward(request, response);

				}
				dao_map.getRecord(); // 관리자 계정에 사용자가 등록한 점보담기
				response.sendRedirect("/my_map/kakaomap.jsp");  //장소등록을 누르면 원래 페이지로 이동

			}else if(cmd.equals("/del.map")) {				
				for(int i=1; i<100; i++) {
					String num = request.getParameter("this_click_"+ i );
					if(num == null) continue;					
					System.out.println("사용자가 입력한 번호 :" + i);
					dao_map.delete(i);
				}
				response.sendRedirect("/My_MapPage.map");

			} else if(cmd.equals("/local.map")) {
				String local_name = request.getParameter("local_map");     // 도시선택 
				String local_name2 = request.getParameter("local_map2");   // 서울
				String local_name3 = request.getParameter("local_map3");   // 경기
				String local_name4 = request.getParameter("local_map4");   // 대구 				
				System.out.println("넘어온 값 : "+ local_name + local_name2 + local_name3 + local_name4);

				// 사용자가 선택한 도시별로 if문 재생 
				if(local_name2.equals("none") && local_name3.equals("none") && local_name4.equals("none")) {
					List<Map_DTO> list_map = dao_map.map_select(local_name);
					request.setAttribute("list_map", list_map);
					request.getRequestDispatcher("/My_MapPage.map").forward(request, response);

				} else if(local_name3.equals("none") && local_name4.equals("none")) {
					System.out.println("서울지역이 검색 됐습니다.");
					String local_sum = local_name + " " + local_name2;

					List<Map_DTO> list_map = dao_map.map_select(local_sum);
					request.setAttribute("list_map", list_map);
					request.getRequestDispatcher("/My_MapPage.map").forward(request, response);


				} else if(local_name2.equals("none") && local_name4.equals("none")) {
					System.out.println("경기지역이 검색 됐습니다.");
					String local_sum = local_name + " " + local_name3;

					List<Map_DTO> list_map = dao_map.map_select(local_sum);
					request.setAttribute("list_map",list_map);
					request.getRequestDispatcher("/My_MapPage.map").forward(request, response);		


				}else if(local_name2.equals("none") && local_name3.equals("none")) {
					System.out.println("대구지역이 검색 됐습니다.");
					String local_sum = local_name + " " + local_name4;

					List<Map_DTO> list_map = dao_map.map_select(local_sum);
					request.setAttribute("list_map",list_map);
					request.getRequestDispatcher("/My_MapPage.map").forward(request, response);
				}
			} else if(cmd.equals("/like_here.map")) {
				response.sendRedirect("/like_here.map");
			}

		} catch(Exception e) {
			System.out.println("(Map_Controller) try문에서 오류확인바람 ");
			e.printStackTrace();
			response.sendRedirect("/my_map/kakaomap.jsp");
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
