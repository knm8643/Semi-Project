<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%> 
   <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<link rel="preconnect" href="https://fonts.googleapis.com">
<script src="https://kit.fontawesome.com/b06cee0c6f.js"
   crossorigin="anonymous"></script>
<script src="toggle.js" defer></script>
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>

</head>
<style>
body, ul, li {
   margin: 0;
   padding: 0;
   list-style: none;
}

a {
   color: inherit;
   color: inherit;
}

.nav_con {
   top: 0;
   width: 100%;
   height: 350px;
   padding: 0px 12px;
   position: relative;
   margin: auto;
   text-align: center;
   color: black;
   justify-content: space-between;
   z-index: 10;
   background-image: url("img/banner.png");
   background-size: 100% 100%;
}

.nav_con>ul {
   display: inline-block;
   padding: 0 10px;
   border-radius: 5px;
}

.nav_con>ul>li {
   display: inline-block;
   position: relative;
   font-size: 20px;
   width: 200px;
   text-align: center;
   z-index: 10;
   color: black;
}

.nav_con>ul>li>a {
   padding: 10px;
   display: block;
   line-height: 60px;
}

.nav_con>ul>li:hover>a {
   color: orange;
}

#nav_logo {
   text-align: center;
   display: block;
   padding-left: 20px;
}

.nav_con ul li a:hover {
   font-size: 20px;
   color: orange;
   display: block;
}

.nav_con>ul ul {
   display: none;
   position: absolute;
   top: 100%;
   left: 0%;
   width: 100%;
   z-index: 1;
}

.nav_con li:hover>ul {
   display: block;
}

.nav_con ul ul li {
   height: 50px;
   line-height: 49px;
}

.nav_h>a:hover {
   color: white;
}

#nav_b_login{
   position: absolute;
   font-size: 15px;
   z-index: 11;
   top: 15px;
   right: 20px;
   width: 200px;
}
.b_login{
    margin-right:30px;
}

#nav_login {
   position: absolute;
   font-size: 15px;
   z-index: 11;
   top: 15px;
   right: 20px;
   width: 250px;
}

.login {
   right: 10px;
   float: left;
   padding-left: 5%;
}
#logout{
	position:absolute;
	width:150px;
   	right: 330px;
}
#mypage_button{
	position:absolute;
	width:150px;
   	right: 200px;
}
#leave{
	position:absolute;
	width:150px;
   	right: 70px;
}
.member {
   padding-left: 5%;
}

.nav_toogle>i {
   position: static;
   display: none;
   font-size: 30px;
   right: 80px;
   /* color: orange; */
}




@media screen and (max-width: 1110px ) {
   .nav_con {
      flex-direction: column;
      align-items: flex-start;
      padding: 8px 24px;
      height: 250px;
   }
   #nav_menu {
      position: absolute;
      display: none;
      flex-direction: column;
      top: 160px;
      right: 10px;
      width: 220px;
      padding-top: 90px;
   }
   #nav_menu li {
      width: 100%;
      text-align: center;
      background-color:white;
      text-align:right;
      padding-right:30px;
      font-size:18px;
   }
   .sub_menu{
   	  font-size:5px;
   }
   .nav_con>ul ul {
      position: relative;
      color: black;
      background-color:white;
   }
   .dot {
      display: none;
   }
   #nav_login {
      position: absolute;
      width: 30%;
      right: 10px;
   }
   
   #nav_b_login{
   position: absolute;
   width:300px;
   }
   #login_button{
   position:absolute;
   right:14px;
   
   }
   #member_button{
   position:absolute;
   top:50px;
   right:35px;
   }
   #logout{
	position:absolute;
	width:150px;
   	right: 14px;
	}
	#mypage_button{
		position:absolute;
		top:50px;
		width:150px;
	   	right: 14px;
	}
	#leave{
		position:absolute;
		top:100px;
		width:150px;
	   	right: 14px;
	}
	  #nav_logo {
      display: block;
      height: 75px;
      padding-left: 10px;
      padding-top: 20px;
   }
   .nav_toogle>i {
      position: absolute;
      top: 200px;
      right:80px;
      display: block;
      color:black;
   }
   #nav_login.acctive, #nav_menu.acctive {
      display: block;
   }
}

</style>
<body>
<style type="text/css">
a {
   text-decoration: none
}
</style>
   <nav class="nav_con">
      <div id="nav_login">
      <c:choose> 
		<c:when test="${loginID != null}">
			<div>
	            <div><a href="javascript:logout();" class="login" id="logout"><button type="button" class="btn btn-dark">로그아웃</button></a></div>
	            <div><a href="/myPage.mem" class="member" id="mypage_button"><button type="button" class="btn btn-dark">마이페이지</button></a></div>
	            <div><a href="javascript:leave();" class="member" id="leave"><button type="button" class="btn btn-dark">회원탈퇴</button></a></div>
			</div>
        </c:when>
         <c:otherwise>
			<div id="nav_b_login">
                <span id="login_button"><a href="/index.jsp" class="b_login"><button type="button" class="btn btn-dark">로그인</button></a></span>
                <span id="member_button"><a href="/signup.mem" class="b_member"><button type="button" class="btn btn-dark">회원가입</a></span>
             </div>
      </c:otherwise>
      </c:choose>
      </div>
      <div id="nav_logo">
         <a href="/Main.jsp"><img src="/img/Logo.png"
            style="height: 90px;"></a>
      </div>
      <ul id="nav_menu">
         <li><a href="/intro.jsp" class="menu1"><b>사이트 소개</b></a></li>
         <li><a href="/like_here.map" class="menu2" id="menu2"><b>내주변
                  맛집</b></a></li>
         <!-- 수정했습니다(이충재) -->
         <li><a href="/My_MapPage.map" class="menu3" id="menu3"><b>나만의
                  맛집</b></a></li>
         <!-- 수정했습니다(이충재) -->
         <li><a href="#" class="menu4"><b>맛 담 화</b></a>
            <ul class="drop1">
               <li class="sub_menu"><a href="/list.board?cpage=1"><b>맛집 공유</b></a></li>
               <li class="sub_menu"><a href="/list.recipe?cpage=1"><b>레시피</b></a></li>
               <li class="sub_menu"><a href="/list.with?cpage=1"><b>같이 먹을 사람</b></a></li>
            </ul></li>
         <li><a href="#" class="menu5"><b>고객 센터</b></a>
            <ul class="drop2">
               <li class="sub_menu"><a href="/list.notice?cpage=1"><b>공지 사항</b></a></li>
               <li class="sub_menu"><a href="/list.qna?cpage=1"><b>고객 문의</b></a></li>
               <li class="sub_menu"><a href="/list.admin?cpage=1"><b>관리자 페이지</b></a></li>
            </ul></li>
      </ul>

      <a href="#" class="nav_toogle"> <i class="fas fa-bars"></i>
      </a>
   </nav>
   <script>
/*    $("#menu2").click(function() {
      if(${loginID == null}){
         alert("로그인을 해주세요");
         return false;
         location.href="/login.mem";
      } 
   })
   
   $("#menu3").click(function() {
      if(${loginID == null}){
         alert("로그인을 해주세요");
         return false;
         location.href="/login.mem";
      } 
   }) */
	Kakao.init('280f4b845b98a0adf26878c0048c5ade'); //발급받은 키 중 javascript키를 사용해준다.

   function logout() {
		if (confirm("정말 로그아웃 하시겠습니까?")) {
			if (Kakao.Auth.getAccessToken()) {
				Kakao.API.request({
					url : '/v1/user/unlink',
					success : function(response) {
						location.href = "/logout.mem"; //카카오api 결과값을 받은후 로그아웃 처리
					},
					fail : function(error) {
						location.href = "/logout.mem";
					},
				})
				Kakao.Auth.setAccessToken(undefined)
			} else {
				location.href = "/logout.mem"; //카카오 api를 사용한 로그인이 아니고 일반 로그인인 경우
			}

		}
	}
	
   function leave() {
		if (confirm("정말 탈퇴하시겠습니까?")) {
			if (Kakao.Auth.getAccessToken()) {
				Kakao.API.request({
					url : '/v1/user/unlink',
					success : function(response) {
						location.href = "leave.mem"; //카카오api 결과값을 받은후 로그아웃 처리
					},
					fail : function(error) {
						location.href = "leave.mem";
					},
				})
				Kakao.Auth.setAccessToken(undefined)
			} else {
				location.href = "leave.mem"; //카카오 api를 사용한 로그인이 아니고 일반 로그인인 경우
			}

		}
	}
    </script>
</body>