<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 관리 - 회원관리</title>
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
	.col{
		width:8.3%;
		overflow:hidden;
		text-overflow:ellipsis;
		white-space: nowrap;
		padding:0px;
	}
	[name=row]{
		width:100%;
	}
	.row>div{
		overflow:hidden;
		text-overflow:ellipsis;
		white-space: nowrap;
	}
</style>
</head>
<body>
<jsp:include page="adminSideBar.jsp"></jsp:include>
	<div class="container">
	<form action="/searchMember.admin?cpage=1" method=post>
		<div class="row">
			<div class="col-1" style="text-align:left;padding:0px;">
				<select style="width:100%;height:100%;" name=option>
					<option value="login_id">로그인ID</option>
					<option value="member_name">이름</option>
					<option value="blacklist">블랙리스트</option>
				</select>
			</div>
			<div class="col-xs-12 col-sm-11" style="text-align:left;padding:0px;">
				<input type=text placeholder="검색할 내용을 입력하세요" style="width:30%;" name=target>
				<input type=submit value="검색">
			</div>
		</div>
	</form>
		<div class="row" style="height:50px;"></div>
		<div class="row" name="row" style="background-color:#e1e1e1;border-top:1px solid black;margin:0px;border-bottom:1px dotted black;">
			<div class="col-1"><input type=checkbox name="checkAll"></div>
			<div class="col-2">ID</div>
			<div class="col-2">로그인ID</div>
			<div class="col-1">이름</div>
			<div class="col-1">별명</div>
			<div class="col-1">우편번호</div>
			<div class="col-2">주소</div>
			<div class="col-2">상세주소</div>
		</div>
		<div class="row" name="row" style="background-color:#e1e1e1;border-bottom:1px solid black;margin:0px;">
			<div class="col-1">주민번호</div>
			<div class="col-2">전화번호</div>
			<div class="col-2">이메일</div>
			<div class="col-1">관리자여부</div>
			<div class="col-1">나이대</div>
			<div class="col-1">카카오로그인</div>
			<div class="col-2">블랙리스트</div>
		</div>
		<c:choose>
		<c:when test="${empty list }" >
			<div style="text-align: center; line-height: 100px;">가입 회원이 없습니다.</div>
		</c:when>
		<c:otherwise>
		<form method=post id="frm">
		<c:forEach var="dto" items="${list}">
			<div class=row style="text-align:center;border-bottom:1px dotted black;margin:0px;">
				<c:choose>
					<c:when test="${dto.admin_yn == 'Y'}">
						<div class="col-1"></div>
					</c:when>
					<c:otherwise>
						<div class="col-1"><input type=checkbox name="check" value="${dto.member_id }"></div>
					</c:otherwise>
				</c:choose>
			<div class="col-2">${dto.member_id}</div>
			<div class="col-2">${dto.login_id}</div>
			<div class="col-1">${dto.member_name}</div>
			<div class="col-1">${dto.member_nickname}</div>
			<div class="col-1">${dto.member_zipcode}</div>
			<div class="col-2">${dto.member_address1}</div>
			<div class="col-2">${dto.member_address2}</div>
			</div>
			<div class=row style="text-align:center;border-bottom:1px solid black;margin:0px;">
				<c:set var = "ssn" value = "${dto.member_ssn}"/>
				<c:choose>
					<c:when test="${dto.member_ssn != null}">
						<div class="col-1">${fn:substring(ssn,1,7)}-${fn:substring(ssn,7,8)}******</div>
					</c:when>
					<c:otherwise>
						<div class="col-1"></div>
					</c:otherwise>
				</c:choose>
				<div class="col-2">${dto.member_phone}</div>
				<div class="col-2">${dto.member_email}</div>
				<div class="col-1">${dto.admin_yn}</div>
				<div class="col-1">${dto.avgAge}</div>
				<div class="col-1">${dto.kakao_login_yn}</div>
				<div class="col-2">	
				<input type=hidden name="member_id" value=${dto.member_id}>
				<c:choose>
					<c:when test="${dto.blacklist eq 'Y'}">
						Y<input type=radio name="blacklist${dto.member_id}" value='Y' class="input_check" checked>
						N<input type=radio name="blacklist${dto.member_id}" value='N' class="input_check">
					</c:when>
					<c:otherwise>
						Y<input type=radio name="blacklist${dto.member_id}" value='Y' class="input_check">
						N<input type=radio name="blacklist${dto.member_id}" value='N' class="input_check" checked>
					</c:otherwise>
				</c:choose>
				</div>
			</div>
		</c:forEach>
		</c:otherwise>
		</c:choose>
		<div class=row style="height:50px;">
			<div class="col-sm-2" style="text-align:left;padding-left:10px;">
				<input type=submit value=선택삭제 id=delete>
				<input type=button value=블랙리스트 id=blacklist>
			</div>
			<div class="col-sm-10" id=pNavi style="text-align:center; margin-left:-50px;">${navi }</div>
		</div>
		</form>
	 </div>
	 <script type="text/javascript">
    	window.onload = function() {
    		let loginId = '${loginID}'
    		console.log(loginId, '페이지 진입')
	    	if(loginId != 'administrator') {
			  alert("관리자 계정으로 로그인을 해주세요")
	          location.href = "/Main.jsp";
	         } 
  		};
	</script>
	 <script>
	 
 		
 		let chkObj = document.getElementsByName("check");
 		let rowCnt = chkObj.length;
 		
		$("#delete").on("click",function(){
			console.log($("input[name='check']:checked").length);
 			if($("input[name='check']:checked").length == 0){
 				alert("삭제할 대상을 선택해주세요");
 				return false;
 			}else{
 				if(!confirm("정말 삭제하시겠습니까?")){
 					return false;
 				}else{
 					$("#frm").attr("action","/deleteMember.admin");
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
		
	 	$("#blacklist").on("click",function(){
	 		// let chkYn = document.getElementsByName("blacklist");
	 		if(!confirm("블랙리스트를 업데이트 하시겠습니까?")){
					return false;
			}else{
				$("#frm").attr("action","/blacklist.admin");
				$("#frm").submit();
				alert("블랙리스트 업데이트가 완료되었습니다.");
			}
	 	})

	 </script>
</body>
</html>