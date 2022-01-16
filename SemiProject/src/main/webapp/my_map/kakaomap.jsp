<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>지도 페이지</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- 카카오 Api 사용을 위한 인증키입니다. (작업과 무관 하지만 수정하지마세요) -->
<script type="text/javascript"
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=7a00e839ba07cfb660f1cfc019bdd08b&libraries=services"></script>
<!-- kakao Map Api 사용을 위한 css 입니다 -->
<link href="/project/map.css" rel="stylesheet">  <!-- 경로값수정하였음!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 경로값 -->
</head>

<body>
	<div class="main" style="height: 100%; width: 100%;">
		<jsp:include page="/Navi.jsp"></jsp:include>
		<div id="section_contents">
			<div id="back"></div>
			<div id="inner">
				<div id="wrap_service">
					<form action="/My_MapPage.map" method="get">
						<div id="mylocal" style="height: 50px;">
							<input type="text" value="${loginID}" name="user" hidden>
							<b>${loginID }님 내 주변 맛집을 검색해보세요!</b>
						</div>
					</form>
					<div id="srevice_container">
						<div id="wrap_card">
							<div id="menu_wrap" class="bg_white">
								<div class="option" style="height: 100px;">
									<div style="line-height: 20%;">
										<form onsubmit="searchPlaces(); return false;">
											<!-- input text 및 검색버튼 (여기만 수정가능) -->
											<input type="text" value="" id="keyword" placeholder="맛집을 검색하세요"
												style="width: 300px; height: 20px;">
											<!-- value 값을 바꿔주세요.  -->
											<button type="submit" style="width: 80px; height: 26px;">검색하기</button>
										</form>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div id="map text">
						<div class="map_wrap" style="height: 500px; ">
							<div id="map" style="height: 100%;"></div>
						</div>
					</div>
					<div id="plabox">
						<hr>
						<ul id="placesList"></ul>
						<div id="pagination" style="height: 100px; font-size: 25px"></div>
					</div>
				</div>
			</div>
		</div>
		<div id="wrap_footer" style="height: 200px">
			<b>열심히하겠습니다 감사합니다.<br>
				대표 : 이충재</b>
		</div>
	</div>







	<!--  --------------------------body와 script 구분선--------------------------- -->
	<script type="text/javascript">
		window.onload = function () {
			let loginId = '${loginID}'
			console.log(loginId, '페이지 진입')
			if (!loginId) {
				alert("로그인을 해주세요")
				location.href = "/index.jsp";
			}
		};
	</script>

	<script>
		// 진입시 사용자 정보 없을시 막기

		// 카카오맵 사용을 위한 API 시작 부분입니다.(일부분 제외하고 수정불가 중간에 주석으로 구분해놨음)
		// --------  쓸모없는 설명은 공간사용을 위해 지웠습니다. ---------------- 
		var markers = [];
		var mapContainer = document.getElementById('map'), mapOption = {
			center: new kakao.maps.LatLng(37.566826, 126.9786567),
			level: 3
		};
		var map = new kakao.maps.Map(mapContainer, mapOption);
		var ps = new kakao.maps.services.Places();
		var infowindow = new kakao.maps.InfoWindow({
			zIndex: 1
		});
		searchPlaces();
		function searchPlaces() {
			var keyword = document.getElementById('keyword').value;
			if (!keyword.replace(/^\s+|\s+$/g, '')) {
				//alert('키워드를 입력해주세요!');
				return false;
			}
			ps.keywordSearch(keyword, placesSearchCB);
		}
		function placesSearchCB(data, status, pagination) {
			if (status === kakao.maps.services.Status.OK) {
				displayPlaces(data);
				displayPagination(pagination);
			} else if (status === kakao.maps.services.Status.ZERO_RESULT) {
				alert('검색 결과가 존재하지 않습니다.');
				return;
			} else if (status === kakao.maps.services.Status.ERROR) {
				alert('검색 결과 중 오류가 발생했습니다.');
				return;
			}
		}

		function displayPlaces(places) {
			var listEl = document.getElementById('placesList'), menuEl = document
				.getElementById('menu_wrap'), fragment = document
					.createDocumentFragment(), bounds = new kakao.maps.LatLngBounds(), listStr = '';
			removeAllChildNods(listEl);
			removeMarker();
			for (var i = 0; i < places.length; i++) {
				var placePosition = new kakao.maps.LatLng(places[i].y,
					places[i].x), marker = addMarker(placePosition, i), itemEl = getListItem(
						i, places[i]);
				bounds.extend(placePosition);

				(function (marker, title) {
					kakao.maps.event.addListener(marker, 'mouseover',
						function () {
							displayInfowindow(marker, title);
						});
					kakao.maps.event.addListener(marker, 'mouseout',
						function () {
							infowindow.close();
						});
					itemEl.onmouseover = function () {
						displayInfowindow(marker, title);
					};
					itemEl.onmouseout = function () {
						infowindow.close();
					};
				})(marker, places[i].place_name);
				fragment.appendChild(itemEl);
			}
			listEl.appendChild(fragment);
			menuEl.scrollTop = 0;
			map.setBounds(bounds);
		}

		// 카카오 Api의 일부이나 DB저장에 쓰일 부분으로 가장 중요합니다.(맛집 정보가 담겨있음)
		// 수정가능한 부분입니다.(근데 왠만해선 건들지마세요.... 그냥 건들지마세요!! )
		function getListItem(index, places) {
			var el = document.createElement('li'), itemStr = '<form action="/like_here.map" method="get"><span class="markerbg marker_'
				+ (index + 1)
				+ '"></span>'
				+ '<div class="info">'
				+ '   <h5 value="' + places.place_name + '"><input type="text" name="place_name_'
				+ (index + 1)
				+ '" value="'
				+ places.place_name
				+ '" hidden> ' + places.place_name + '</h5>';
			if (places.road_address_name) {
				itemStr += '    <span value="' + places.road_address_name + '"><input type="text" name="address_name_'
					+ (index + 1)
					+ '" value="'
					+ places.road_address_name
					+ '" hidden>'
					+ places.road_address_name
					+ '</span>'
					+ '   <span class="jibun gray">'
					+ places.address_name
					+ '</span>';
			} else {
				itemStr += '    <span>' + places.address_name + '</span>';
			}

			itemStr += '  <span class="tel" value=' + places.phone + '><input type="text" name="tel_'
				+ (index + 1)
				+ '" value="'
				+ places.phone
				+ '" hidden><input type="text" name="user" value ="${loginID }" hidden>"'
				+ places.phone
				+ '</span>'
				+ '<span><input type="submit" onclick="check_'
				+ (index + 1)
				+ '()" value="즐겨찾기" class="here_click" name="here_map_'
				+ (index + 1) + '"></span>'
			'</div></form>';

			el.innerHTML = itemStr;
			el.className = 'item';
			return el;
			// ------------ 구 분 선--------------
		}

		function addMarker(position, idx, title) {
			var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', imageSize = new kakao.maps.Size(
				36, 37), imgOptions = {
					spriteSize: new kakao.maps.Size(36, 691),
					spriteOrigin: new kakao.maps.Point(0, (idx * 46) + 10),
					offset: new kakao.maps.Point(13, 37)
				}, markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize,
					imgOptions), marker = new kakao.maps.Marker({
						position: position,
						image: markerImage
					});
			marker.setMap(map);
			markers.push(marker);
			return marker;
		}

		function removeMarker() {
			for (var i = 0; i < markers.length; i++) {
				markers[i].setMap(null);
			}
			markers = [];
		}
		function displayPagination(pagination) {
			var paginationEl = document.getElementById('pagination'), fragment = document
				.createDocumentFragment(), i;
			while (paginationEl.hasChildNodes()) {
				paginationEl.removeChild(paginationEl.lastChild);
			}
			for (i = 1; i <= pagination.last; i++) {
				var el = document.createElement('a');
				el.href = "#";
				el.innerHTML = i;

				if (i === pagination.current) {
					el.className = 'on';
				} else {
					el.onclick = (function (i) {
						return function () {
							pagination.gotoPage(i);
						}
					})(i);
				}
				fragment.appendChild(el);
			}
			paginationEl.appendChild(fragment);
		}
		function displayInfowindow(marker, title) {
			var content = '<div style="padding:5px;z-index:1;">' + title
				+ '</div>';
			infowindow.setContent(content);
			infowindow.open(map, marker);
		}

		function removeAllChildNods(el) {
			while (el.hasChildNodes()) {
				el.removeChild(el.lastChild);
			}
		}
		// 카카오 API 마지막 입니다 -------------------------------------------------------------------
		function check_1() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_2() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_3() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_4() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_5() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_6() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_7() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_8() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_9() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_10() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_11() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_12() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_13() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_14() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_15() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
		function check_16() {
			alert("등록이 완료됐습니다.")
			location.href = "/like.here.map";
		}
	</script>
</body>
</html>