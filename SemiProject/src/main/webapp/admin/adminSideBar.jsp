<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>대동맛지도 관리자 페이지</title>
<!-- include libraries(jQuery, bootstrap) -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

<!-- include summernote css/js -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet"> 
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
<script src="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.2/summernote.js"></script>
<script src=" https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/lang/summernote-ko-KR.min.js"></script>
<style>
	 [name="left side-menu"] {
	    top: 0px;
	    left: 0px;
	    width: 45px;
	    z-index: 10;
	    background: wheat;
	    border-right: 1px solid rgba(0, 0, 0, 0.07);
	    bottom: 50px;
	    height: 100%;
	    margin-bottom: -70px;
	    margin-top: 0px;
	    padding-bottom: 70px;
	    position: fixed;
	    box-shadow: 0 0px 24px 0 rgb(0 0 0 / 6%), 0 1px 0px 0 rgb(0 0 0 / 2%);
	}
	
	/* 사이드 메뉴 */
	[name="left_sub_menu"] {
	    position: fixed;
	    top: 0px;
	    width: 200px;
	    z-index: 10;
	    left: 45px;
	    background: white;
	    border-right: 1px solid rgba(0, 0, 0, 0.07);
	    bottom: 50px;
	    height: 100%;
	    margin-bottom: -70px;
	    margin-top: 0px;
	    padding-bottom: 0px;
	    box-shadow: 0 0px 24px 0 rgb(0 0 0 / 6%), 0 1px 0px 0 rgb(0 0 0 / 2%);
	    color: black;
	}
	
	[name="left_sub_menu"] li:hover {
	    color: ff5858;
	    background-color: #e1e1e1;
	}
	
	[name="left_sub_menu"] li {
	    color: #333;
	    font-size: 17px;
	    font-weight: 600;
	    padding: 20px 0px 8px 14px;
	    border-bottom: 1px solid #e1e1e1;
	    text-align:center;
	}
	
	[name="left_sub_menu"] h2 {
	    padding-bottom: 4px;
	    border-bottom: 3px solid #797979;
	    margin-top: 30px;
	    font-size: 21px;
	    font-weight: 600;
	    color: #333;
	    margin-left: 10px;
	    margin-right: 10px;
	    font-family: 'NotoKrB';
	    line-height: 35px;
	    text-align:center;
	}
	
	[name="left_sub_menu"]>.big_menu>.small_menu li {
	    color: #333;
	    font-size: 14px;
	    font-weight: 600;
	    border-bottom: 0px solid #e1e1e1;
	    margin-left: 0px;
	    padding-top: 8px;
	    padding-left: 0px;
	}
	
	.big_menu>li {
		cursor: pointer;
		padding-left:0px;
	}
	
	ul,li {
	    padding-inline-start: 0px;
	}
	
	a {
	    color: #797979;
	    text-decoration: none;
	    background-color: transparent;
	}
	
	ul {
	    list-style: none;
	}

</style>
</head>
<body>
	<div id="wrapper" class="container">
	   <div class="row" name="topbar" style="position: absolute; top:0;">
	     <!-- 왼쪽 메뉴 -->
	     <div class="col-xs-12" name="left side-menu">
	     </div>
	     <!-- 왼쪽 서브 메뉴 -->
	     <div class="col-xs-12" name="left_sub_menu">
             <h2>관리자 메뉴</h2>
             <ul class="big_menu">
                 <li>회원 관리</li>
                 <ul class="small_menu">
                     <li><a href="/member.admin?cpage=1">회원정보</a></li>
                 </ul>
             </ul>
             <ul class="big_menu">
                 <li>맛집 정보 관리</li>
                 <ul class="small_menu">
                     <li><a href="/rest.admin?cpage=1">맛집 정보</a></li>
                 </ul>
             </ul>
             <ul class="big_menu">
                 <li>게시글 관리</li>
                 <ul class="small_menu">
                 	 <li><a href="/share.admin?cpage=1">맛집 공유</a></li>
                 	 <li><a href="/recipe.admin?cpage=1">레시피</a></li>
                     <li><a href="/with.admin?cpage=1">같이 먹을 사람</a></li>
                     <li><a href="/notice.admin?cpage=1">공지 사항</a></li>
                     <li><a href="/qna.admin?cpage=1">고객 문의</a></li>
                 </ul>
             </ul>
             	<ul class="big_menu">
                 <li>댓글 관리</li>
                 <ul class="small_menu">
                 	 <li><a href="/shareReply.admin?cpage=1">맛집 공유</a></li>
                 	 <li><a href="/recipeReply.admin?cpage=1">레시피</a></li>
                     <li><a href="/withReply.admin?cpage=1">같이 먹을 사람</a></li>
                     <li><a href="/noticeReply.admin?cpage=1">공지 사항</a></li>
                     <li><a href="/qnaReply.admin?cpage=1">고객 문의</a></li>
                 </ul>
             </ul>
             <ul class="big_menu">
                 <li><a href="/Main.jsp">메인 페이지</a></li>
             </ul>
	     </div>
	     <!-- <div class="overlay"></div> -->
	 </div>
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
	    $(function () {
	        // 왼쪽메뉴 드롭다운
	        $(".small_menu").hide();
	        $(".big_menu").click(function () {
	            $("ul", this).slideToggle(300);
	        });
	        // 외부 클릭 시 좌측 사이드 메뉴 숨기기
	        $('[name="left side-menu"]').on('click', function () {
	            $('[name="left_sub_menu"]').fadeToggle();
	        });
	    });
	</script>
</body>
</html>