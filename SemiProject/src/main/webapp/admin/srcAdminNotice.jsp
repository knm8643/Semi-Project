<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 관리 - 공지사항</title>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>
	.container{
		position:absolute;
		top:100px;
		left:300px;
		background-color:white;
		align:center;
		font-family: 굴림;
	}
	.container>div{
		padding-top:2px;
		padding-bottom:2px;
	}
	.container>.row>div{
		padding:0px;
		text-align:center;
		overflow:hidden;
		text-overflow:ellipsis;
		white-space: nowrap;
	}
</style>
</head>
<body>
<jsp:include page="adminSideBar.jsp"></jsp:include>
	<div class="container">
	<form action="/searchNotice.admin?cpage=1" method=post>
		<div class="row">
			<div class="col-xs-12 col-sm-1" style="text-align:left;padding:0px;">
				<select style="width:100%;height:100%;" name=option>
					<option value="noti_seq">번호</option>
					<option value="noti_title">제목</option>
					<option value="noti_writer">작성자</option>
				</select>
			</div>
			<div class="col-xs-12 col-sm-11" style="text-align:left;padding:0px;">
				<input type=text placeholder="검색할 내용을 입력하세요" style="width:30%;" name=target>
				<input type=submit value="검색">
			</div>
		</div>
	</form>
		<div class="row" style="height:50px;"></div>
		<div class="row" style="background-color:#e1e1e1;border-top:1px solid black;border-bottom:1px solid black;">
			<div class="col-xs-12 col-sm-1"><input type=checkbox name="checkAll"></div>
			<div class="col-xs-12 col-sm-1">번호</div>
			<div class="col-xs-12 col-sm-1">게시판</div>
			<div class="col-xs-12 col-sm-3">제목</div>
			<div class="col-xs-12 col-sm-1">작성자</div>
			<div class="col-xs-12 col-sm-2">작성일</div>
			<div class="col-xs-12 col-sm-1">조회수</div>
			<div class="col-xs-12 col-sm-2">기능</div>
		</div>
		<c:choose>
		<c:when test="${empty list }" >
			<div style="text-align: center; line-height: 100px;">검색 결과가 없습니다.</div>
		</c:when>
		<c:otherwise>
		<form action="/deleteArticle.admin" method=post id="frm">
		<c:forEach var="dto" items="${list}">
			<div class=row style="text-align:center;">
				<div class="col-xs-12 col-sm-1"><input type=checkbox name="check" value="${dto.seq }"></div>
				<div class="col-xs-12 col-sm-1" id=seq>${dto.seq }</div>
				<div class="col-xs-12 col-sm-1">공지사항</div>
				<div class="col-xs-12 col-sm-3" id=title>${dto.title }</div>
				<div class="col-xs-12 col-sm-1" id=writer>${dto.writer }</div>
				<div class="col-xs-12 col-sm-2" id=write_date>${dto.write_date }</div>
				<div class="col-xs-12 col-sm-1" id=view_count>${dto.view_count }</div>
		   		<div class="col-xs-12 col-sm-2">
		   			<a href='/detail.notice?seq=${dto.seq}'><input type=button value=보기></a>
		   			<a href='/mod.admin?seq=${dto.seq}'><input type=button value=수정 ></a>
		   		</div>
			</div>
		</c:forEach>
		</c:otherwise>
		</c:choose>
		<div class=row style="height:50px;border-top:1px solid black">
			<div class="col-sm-2" style="text-align:left;padding-left:10px;">
				<input type=button value=선택삭제 id=delete>
				<a href='/write.admin'><input type=button value="글 작성"></a>
			</div>
			<div class="col-sm-10" id=pNavi style="text-align:center; margin-left:-50px;">${navi }</div>
		</div>
		</form>
	 </div>
	 <script>
		let chkObj = document.getElementsByName("check");
 		let rowCnt = chkObj.length;
 		
		$("#delete").on("click",function(){
			console.log($("input[name='check']:checked").length);
 			if($("input[name='check']:checked").length == 0){
 				alert("선택된 글이 없습니다.");
 				return false;
 			}else{
 				if(!confirm("정말 삭제하시겠습니까?")){
 					return false;
 				}else{
 					$("#frm").submit();
 					alert("삭제완료!");
 				}
 			}
 		})
	 	$(function(){
	 		
	 		$("input[name='checkAll']").click(function(){
	 			let chk_listArr = $("input[name='check']");
	 			for(let i = 0; i<chk_listArr.length; i++){
	 				chk_listArr[i].checked = this.checked;
	 			}
	 		});
	 		$("input[name='check']").click(function(){
	 			if($("input[name='check']:checked").length == rowCnt){
	 				$("input[name='checkAll']")[0].checked = true;
	 			}else{
	 				$("input[name='checkAll']")[0].checked = false;
	 			}
	 		});	
	 	});
	 </script>
</body>
</html>