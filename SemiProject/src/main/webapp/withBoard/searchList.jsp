<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>같이먹을사람 게시판</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet " href="/Css.css " type="text /css ">
<link rel="preconnect " href="https: //fonts.googleapis.com ">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
<script src="https: //kit.fontawesome.com /b06cee0c6f.js " crossorigin="anonymous ">
	
</script>
<script src="/toggle.js " defer>
	
</script>
<style type="text/css">
* {
	box-sizing: border-box;
	font-family: 'Nanum Gothic', sans-serif;
}

a {
	text-decoration: none
}

body {
	padding: 0px;
}

#hover:hover {
	background-color: rgb(245, 241, 202);
}

a:hover {
	text-decoration: underline
}
.row>div{
	overflow:hidden;
	text-overflow:ellipsis;
	white-space: nowrap;
}
</style>
</head>

<body>
	<jsp:include page="/Navi.jsp"></jsp:include>
	<!-------------------------------  게시판 코드 시작 ---------------------------->

	<div class="container" style="padding: 10px;">
		<!-- 네비바 하단 게시판 이미지 넣는 부분 -->
		<div class="row">
			<div class="col p-3" style="text-align: center;">
				<img src="/img/withboard.JPG" style="display: block; margin: 0px auto; max-width: 100%; height: auto;">
			</div>
		</div>

		<div class="board" style="margin-bottom: 20px;">
			<div class="row" style="text-align: left; height: 50px; line-height: 50px; border-top: 2px solid grey; border-bottom: 1px solid grey;">
				<div class="col-1 p-0" style="float: left;"></div>
				<div class="col-5 p-0" style="float: left;">제목</div>
				<div class="col-2 p-0" style="float: left;">글쓴이</div>
				<div class="col-3 p-0" style="float: left;">작성일</div>
				<div class="col-1 p-0" style="float: left;">조회</div>
			</div>

			<c:choose>
				<c:when test="${empty list }">
					<div style="text-align: center; line-height: 100px;">검색된 글이 없습니다.</div>
				</c:when>
				<c:otherwise>

					<c:forEach var="dto" items="${list}">

						<div class="row" id="hover" style="height: 50px; line-height: 50px; border-bottom: 2px solid rgb(202, 200, 200); text-align: left;">
							<div class="col-1" style="float: left;">${dto.with_seq }</div>
							<div class="col-5" style="float: left;">
								<a href="/detail.with?seq=${dto.with_seq }">${dto.with_title }</a>
							</div>

							<div class="col-2" style="float: left;">${dto.with_writer }</div>
							<div class="col-3" style="float: left;">${dto.detailDate }</div>
							<div class="col-1" style="float: left;">${dto.with_viewcount }</div>
						</div>

					</c:forEach>
				</c:otherwise>
			</c:choose>
		</div>

		<div class="row" style="text-align: center; margin: 10px;">
			<div class="col-10 p-0">${navi }</div>
			<div class="col-2 p-0">
				<c:if test="${blacklist != 'Y'}">
				<input type="button" class="btn btn-outline-secondary" value=글쓰기 onclick="location.href='/write.with'">
				</c:if>
				<input type="button" class="btn btn-outline-secondary" value=목록으로 onclick="location.href='/list.with?cpage=1'">

			</div>
		</div>


		<!-- 검색 기능 -->
		<form action="/search.with?cpage=1" method="post" onkeypress="return event.keyCode!=13">
			<!-- 엔터 치면 페이지 넘어가는 것을 방지 -->
			<div class="row" style="text-align: center;">
				<div class="col">
					<select name="keyword">
						<option value="with_title">제목</option>
						<option value="with_contents">내용</option>
						<option value="with_writer">작성자</option>
					</select>
					<input id="search-input" name="searchWord" type="text" placeholder="검색어를 입력하세요">
					<input type="submit" class="btn btn-outline-secondary" value=검색하기>
				</div>
			</div>
		</form>
	</div>

</body>
</html>