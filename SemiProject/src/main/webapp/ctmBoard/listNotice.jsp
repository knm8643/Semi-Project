<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>대동맛지도 공지사항</title>
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
	[name="article"]{
		padding-top:25px;
		padding-bottom:25px;
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
</style>
</head>
<body>
	<jsp:include page="/Navi.jsp"></jsp:include>
	<div class=container>
		<c:forEach var="dto" items="${list}">
			<div class=row name=article>
				<div class=col-12 name=title><a href="/detail.notice?seq=${dto.seq}"><b>${dto.title }</b></a></div>
				<div class=col-12 name=content>${dto.contents }</div>
				<div class=col-12 name=bottom>
					<div class=view_count>조회수 : ${dto.view_count }</div>
					<div class=write_date>${dto.formedDate }</div>
				</div>
			</div>		
		</c:forEach>
		<div class=row>
			<div class=col id=pNavi>${navi }</div>
		</div>
<!--		<form action="/search.notice?cpage=1" method=post>
 			<div class=row>
				<div class=col-12 align=center>
					<select name=option id=option>
						<option value="noti_title" selected>제목 
						<option value="noti_writer">작성자
						<option value="noti_contents">내용
					</select>
					<input type=text id=target name=target placeholder="검색">
					<input type=submit value=검색 id=search>
				</div>	
			</div>
		</form> -->
		<c:if test="${loginID == 'administrator'}">
		<div class=row id=btn>
			<div class=col-12><input type=button value="글쓰기" onclick="location.href='/write.notice'"></div>
		</div>
		</c:if>
	</div>
	<script>
	</script>
</body>
</html>