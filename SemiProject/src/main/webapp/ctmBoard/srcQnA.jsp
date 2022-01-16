<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>대동맛지도 고객문의</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>        
	*{
		box-sizing:border-box;
		margin:auto;
	}
	
	body{
		position:relative;
	}
	.container{
		width:50%;
		position:absolute;
		top:400px;
		left:50%;
		transform:translateX(-50%);
		font-family: 'Noto Sans KR', 'AppleSDGothicNeo-Regular', 'Nanum Gothic', Helvetica, Arial, sans-serif;
	}
	[name="title"]{height:15%;font-size:22px;}
	[name="content"]{
		padding-top:13px;
		height:150px;
		overflow:hidden;
		text-overflow:ellipsis;
		white-space: nowrap;
		font-size:18px;
	}
	[name="bottom"]{position:relative;height:15%;}
	.write_date{float:left;color: #999;}
	.view_count{float:right;color: #999;}
	#btn{text-align:right; padding:0px;}
	#pNavi{text-align:center;}
	
	.row>div{
		overflow:hidden;
		text-overflow:ellipsis;
		white-space: nowrap;
	}
</style>
</head>
<body>
	<jsp:include page="/Navi.jsp"></jsp:include>
	<div class=container>
		<div class="row" style= "text-align: center; height: 50px; line-height: 50px; border-top:2px solid grey; border-bottom: 1px solid grey;">
			<div class="col-1 p-0" style="float: left;">No.</div>
			<div class="col-5 p-0" style="float: left;">제목</div>
			<div class="col-2 p-0" style="float: left;">글쓴이</div>
			<div class="col-3 p-0" style="float: left;">작성일</div>
			<div class="col-1 p-0" style="float: left;">조회</div>
		</div>
		
		<c:choose>
	 	<c:when test="${empty list }" >
                   <div style="text-align: center; line-height: 100px;">등록된 글이 없습니다.</div>
            </c:when>
        <c:otherwise>
		<c:forEach var="dto" items="${list}">
			<div class=row name=article style="text-align: center; height:30px; line-height:30px; border-bottom: 2px solid rgb(202, 200, 200);">
					<div class="col-1 p-0" style="float: left;">${dto.seq }</div>
					<div class="col-5 p-0" style="float: left;"><a href="/detail.qna?seq=${dto.seq}">${dto.title }</a></div>
					<div class="col-2 p-0" style="float: left;">${dto.writer }</div>
					<div class="col-3 p-0" style="float: left;">${dto.formedDate }</div>
					<div class="col-1 p-0" style="float: left;">${dto.view_count }</div>
			</div>		
		</c:forEach>
		</c:otherwise>
		</c:choose>
		<c:if test="${loginID != null && blacklist != 'Y'}">
		<div class=row id=btn>
			<div class=col-12><input type=button value="글쓰기" onclick="location.href='/write.qna'"></div>
		</div>
		</c:if>
		<div class=row>
			<div class=col id=pNavi>${navi }</div>
		</div>
		<form action="/search.qna?cpage=1" method=post>
			<div class=row>
				<div class=col-12 align=center>
					<select name=option id=option>
						<option value="qna_title" selected>제목 
						<option value="qna_writer">작성자
						<option value="qna_contents">내용
					</select>
					<input type=text id=target name=target placeholder="검색">
					<input type=submit value=검색 id=search>
				</div>	
			</div>
		</form>

	</div>
	<script>
	</script>
</body>
</html>