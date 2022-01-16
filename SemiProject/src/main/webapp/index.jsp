<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>대동맛지도 로그인</title>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<script src="https://developers.kakao.com/sdk/js/kakao.js"></script>
</head>
<body>
	<h1 class="logo" align="center">
		<a href="/Main.jsp"><img
			src="img/daedonglogo.png" alt="로고" align="top"></a>

	</h1>
	<!-- logo e -->
	<c:choose>
		<c:when test="${loginID != null}">
			<table border=1 align=center>
				<tr>
					<th colspan=5>${loginID }님안녕하세요.
				</tr>
				<tr>
					<td><button id="toBoard">To Board</button>
					<td><button id="MyMap">MyMap</button>       <!--  이충재 추가 (상명님 하신 것 아님)  -->
					<td><button id="myPage">MyPage</button>
					<td><button id="logout">logout</button>
					<td><button id="leave">Leave</button>
				</tr>
			</table>


		</c:when>
		<c:otherwise>
			<form action="login.mem" method="post">
				<div id="contents"
					style="margin: 95px auto 0px; width: 1000px; height: 600px">

					<p class="name" style="font-size: 26px; font-weight: bold;">로그인</p>
					<p class="line"
						style="width: 100%; height: 1px; background: #d8d8d9; margin: 40px 0 120px;"></p>

					<div class="cont_left"
						style="float: left; width: 436px; height: 225px;">
						<img src="img/login_visual.jpg" alt="login">
					</div>
					<!-- cont_left e -->
					
					<div class="cont_right"
						style="float: right; width: 450px; height: 200px; position: relative; margin-right: 40px;">
						<input type="text" id="id" name=id placeholder="아이디"style="height: 43px; width: 326px;">
							
						
						<p>
							<input type="password" id="pass" name=pw placeholder="비밀번호"
								style="height: 43px; width: 326px;">
						</p>

						<p class="idsave" style="color: red">
							<c:if test="${loginFailID != null}">
							아이디 또는 비밀번호가 잘못되었습니다.
							</c:if>
						</p>
						<tr>
							<td colspan=2 align="center"><input type="submit" id="login"
								value="로그인"
								style="background: #03114f; color: #fff; text-align: center;">
								<a id="find" href="signup.mem"style="text-decoration: none;">
									<button type=button
										style="background: #03114f; color: #fff; text-align: center;">회원가입</button>
								</a>
								<a id="find" href="javascript:searchPw();"style="color: black;text-decoration: none;font-size: 12px;">비밀번호 찾기</a>
								
								<p>
									<a href="javascript:kakaoLogin()"><img
										src="img/kakao_login_medium_wide.png"> </a>
								</p>
						</tr>
						</table>
					</div>
			</form>
			<!-- 카카오 로그인 처리 -->
			<form action="kakaoLogin.mem" method="post" id="kakaoLogin">
				<input type="hidden" name=kakaoid id="kakaoid"> <input
					type="hidden" name=nickname id="nickname"> <input
					type="hidden" name=avgage id="avgage"> <input type="hidden"
					name=email id="email">
			</form>
		</c:otherwise>
	</c:choose>
</body>
<script>
	Kakao.init('280f4b845b98a0adf26878c0048c5ade'); //발급받은 키 중 javascript키를 사용해준다.
	console.log(Kakao.isInitialized()); // sdk초기화여부판단
	
	if("${loginFailID}"!=""){
		$("#id").val("${loginFailID}");
	}
	

	$("#myPage").on("click", function() {
		location.href = "myPage.mem";
	})

	$("#toBoard").on("click", function() {
		location.href = "/list.board?cpage=1";
	})
	
	$("#MyMap").on("click", function() {  // (이충재가 추가 맵페이지에 값 전달)  
		location.href = "sendmap.mem";
	})
	
	
	/* $("#logout").on("click", function() {
		if (confirm("정말 로그아웃 하시겠습니까?")) {
			if (Kakao.Auth.getAccessToken()) {
				Kakao.API.request({
					url : '/v1/user/unlink',
					success : function(response) {
						location.href = "logout.mem"; //카카오api 결과값을 받은후 로그아웃 처리
					},
					fail : function(error) {
						location.href = "logout.mem";
					},
				})
				Kakao.Auth.setAccessToken(undefined)
			} else {
				location.href = "logout.mem"; //카카오 api를 사용한 로그인이 아니고 일반 로그인인 경우
			}

		}
	})
 */
	$("#leave").on("click", function() {
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
	})

	function kakaoLogin() {
		Kakao.Auth.login({
			success : function(response) {
				Kakao.API.request({
					url : '/v2/user/me',
					success : function(response) {
						console.log(response);
						//response는 카카오 api로 부터 return 받은 로그인 정보
						// 값을 form에 세팅
						$("#avgage").val(response.kakao_account.age_range);
						$("#nickname").val(response.properties.nickname);
						$("#email").val(response.kakao_account.email);
						$("#kakaoid").val(response.id);
						//폼을 전송(대동맛지도 로그인 처리)
						$("#kakaoLogin").submit();

					},
					fail : function(error) {
						console.log(error)
					},
				})
			},
			fail : function(error) {
				console.log(error)
			},
		})
	}
	//카카오로그아웃  
	function kakaoLogout() {
		if (Kakao.Auth.getAccessToken()) {
			Kakao.API.request({
				url : '/v1/user/unlink',
				success : function(response) {
					console.log(response)
				},
				fail : function(error) {
					console.log(error)
				},
			})
			Kakao.Auth.setAccessToken(undefined)
		}
	}
	var ingSearchPw = false;
	function searchPw() {
		if(ingSearchPw){
			alert("전송중입니다. 잠시만 기다려주세요.")
			return false;
		}
		ingSearchPw = true;
		if ($("#id").val() == '') {
			alert("ID 를 입력하세요.");
			$("#id").focus();
			return false;
		}
		$.ajax({
			url : "searchPw.mem",
			data : {
				id : $("#id").val()
			}
		}).done(function(resp) {
			if (resp == "true") {
				alert("임시비밀번호가 전송되었습니다.")
			} else {
				alert("전송에 실패했습니다 아아디를 확인해주세요.")
			}
			ingSearchPw = false;
		})
	}
</script>
</html>
