<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link href="/project/mypage.css" rel="stylesheet">
</head>
<body>
	<div class="con_mypage" style="width: 100%; height: 100%;">
		<jsp:include page="/Navi.jsp"></jsp:include>
		<div id="section_contents">
			<div id="back"></div>
			<div id="inner">
				<div id="wrap_service">
					<div id="mylocal" style="height: 100px;">
						<b>맛집킬러 ${loginID }님 내가 등록한 장소를 확인하세요.</b>
					</div>
					<div id="srevice_container">
						<div id="wrap_card">
							<div id="card_list">
								<c:forEach var="map_dto" items="${list }">
									<form action="/del.map" method="get">
										<div id="basic" style="line-height: 6pt">
											<div style="font-size: 15px" id="textbody"
												;
												onclick="location.href='https://search.daum.net/search?w=tot&DA=YZR&t__nil_searchbox=btn&sug=&sugo=&sq=&o=&q=${map_dto.address_name }'">
												<span id="map_num" name="${map_dto.id }" hidden>${map_dto.id}</span>
												<p>
													<b>${map_dto.name }</b>
												</p>
												<br>
												<p>${map_dto.address_name }</p>
												<br>
												<p id="text1">
													#<b>${map_dto.tel }</b>
												</p>
											</div>

											<div>
												<input type="submit" value="지우기" onclick="returns()"
													id="btnbox" name="this_click_${map_dto.id }">
											</div>
										</div>
									</form>
								</c:forEach>
								<div id="basic" style="line-height: 175pt;">
									<span style="font-size: 17pt" id="body_img2font"><a
										href="like_here.map"><b>내용을 입력하세요!</b></a></span>
								</div>

								<div id="basic2" style="line-height: 175pt;">
									<span style="font-size: 17pt" id="body_img2font"><a
										href="like_here.map"><b>내용을 입력하세요!</b></a></span>
								</div>

							</div>
						</div>
					</div>
				</div>
			</div>
			<div id="ad">
				<b>광고문의 / 010-5696-1909</b>
			</div>
			<div id="section_banner">
				<div id="product">
					<div id="top">
						<b>${loginID }님</b> 근처 맛집을 검색해보세요!
					</div>
					<div id="inner">
						<div id="wrap_service">
							<div style="height: 60px;" id="map_ser">
								<form action="/local.map" method="get">
									<select name="local_map" id="local_mapid">
										<option value="서울" selected>서울</option>
										<option value="경기">경기</option>
										<option value="대구">대구</option>
									</select> <select name="local_map2" id="local_mapid2">
										<option value="none" selected style="display: none"></option>
										<option value="동작구">동작구</option>
										<option value="영등포구">영등포구</option>
										<option value="관악구">관악구</option>
										<option value="용산구">용산구</option>
										<option value="종로구">종로구</option>

									</select> <select name="local_map3" id="local_mapid3"
										style="display: none">
										<option value="none" selected style="display: none"></option>
										<option value="성남시">성남시</option>
										<option value="파주시">파주시</option>
										<option value="안양시">안양시</option>

									</select> <select name="local_map4" id="local_mapid4"
										style="display: none">
										<option value="none" selected style="display: none"></option>
										<option value="수성구">수성구</option>
										<option value="중구">중구</option>
										<option value="달서구">달서구</option>
									</select>
									<button type="submit" onclick="send()">찾기</button>
								</form>
							</div>
							<div id="wrap_card">
								<div id="card_list">
									<c:forEach var="map2_dto" items="${list_map }" varStatus="vs">
										<div id="basic3">
											<div style="line-height: 4pt;" id="textbody2">
												<p>
													<b> <c:out value="${map2_dto.name }" />
													</b>
												</p>
												<br>
												<p>

													<c:out value="${map2_dto.address_name }" />
												</p>
												<br>
												<p>

													<c:out value="${map2_dto.tel }" />
												</p>
											</div>
										</div>
									</c:forEach>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div id="wrap_footer" style="height: 200px">
				<b>열심히하겠습니다 감사합니다.<br> 대표 : 이충재
				</b>
			</div>
		</div>

		<script type="text/javascript">
			window.onload = function() {
				let loginId = '${loginID}'
				console.log(loginId, '페이지 진입')
				if (!loginId) {
					alert("로그인을 해주세요")
					location.href = "/index.jsp";
				}
			};
		</script>

		<script>
			function returns() {
				location.href = "/del.map";
			}
			function send() {
				location.href = "/local.map";
			}

			$("#basic").show(function() {
				$("#basic2").hide();
			})

			$("#local_mapid").change(function() {
				let tests = $("#local_mapid option:selected").val();
				if (tests == "서울") {
					$("#local_mapid2").show();
					$("#local_mapid3").hide();
					$("#local_mapid4").hide();
					$("#here_font")
				} else if (tests == "경기") {
					$("#local_mapid2").hide();
					$("#local_mapid3").show();
					$("#local_mapid4").hide();
				} else if (tests == "대구") {
					$("#local_mapid2").hide();
					$("#local_mapid3").hide();
					$("#local_mapid4").show();
				}
			})

			$("#local_mapid").click(function() {
				$("#local_mapid2 option:eq(0)").prop("selected", true);
				$("#local_mapid3 option:eq(0)").prop("selected", true);
				$("#local_mapid4 option:eq(0)").prop("selected", true);
			})
		</script>
</body>

</html>