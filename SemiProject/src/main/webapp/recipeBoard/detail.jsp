<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>레시피 게시판</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
<!-- include libraries(jQuery, bootstrap) -->
<!-- include summernote css/js-->
<link href="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.8/summernote.css" rel="stylesheet">
<script src="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.8/summernote.js"></script>
<link rel="stylesheet " href="Css.css " type="text /css ">
<link rel="preconnect " href="https: //fonts.googleapis.com ">
<script src="https://kit.fontawesome.com/b06cee0c6f.js" crossorigin="anonymous">
	
</script>
<script src="/toggle.js" defer>
	
</script>

<style type="text/css">
* {
	font-family: 'Nanum Gothic', sans-serif;
	box-sizing: border-box;
}

a {
	text-decoration: none
}

/*------------------------------- 게시판 CSS 시작-------------------------------*/
#subbtn {
	float: right;
	width: 100px;
	height: 50px;
	background-color: white-space;
	margin: 10px
}

#canclebtn {
	float: right;
	width: 100px;
	height: 50px;
	background-color: white-space;
	margin: 10px
}
</style>
</head>
<body>

	<!-- 네비바  -->
	<jsp:include page="/Navi.jsp"></jsp:include>

	<!-------------------------------  게시판 코드 시작 ---------------------------->
	<div class="container" style="padding: 10px;">
		<form action="/modify.recipe" method="post">

			<!-- 네비바 하단 게시판 이미지 넣는 부분 -->
			<div class="row">
				<div class="col-12 p-3" style="text-align: center; padding: 10px]">
					<img src="/img/recipeboard.JPG" style="display: block; margin: 0px auto; max-width: 100%; height: auto;">
				</div>
			</div>
			<input type="hidden" value="${dto.rc_seq }" name="seq">

			<!-- 게시글 제목-->
			<div class="row">
				<div class="col">
					<input type="text" id="title" name="title" placeholder="제목을 입력하세요." value="${dto.rc_title }" style="margin: 10px; width: 100%; border: none; font-size: 15px;">
				</div>
			</div>

			<!-- 게시글 내용 -->
			<div class="row" style="border: 1px solid gray;">
				<div class="col" id="content" name="content" style="overflow: auto; width: 100%; height: 500px;">${dto.rc_contents }</div>
			</div>


			<!--------------버튼 구역---------------->

			<div class="row" style="margin: 20px; text-align: right;">
				<div class="col" style="float: right;">
					<input type=button class="btn btn-outline-secondary" id="list" value="목록으로">
					<c:if test="${blacklist != 'Y'}">
					<input type=button class="btn btn-outline-secondary" id="write" value="글쓰기">
					</c:if>
					<c:if test="${dto.rc_writer == loginID }">
						<input type=button class="btn btn-outline-secondary" id=del value="삭제하기">
						<input type="submit" class="btn btn-outline-secondary" id=mod value="수정하기">
					</c:if>
				</div>
			</div>

		</form>
	</div>


	<!----------------------------- 댓글 목록 ------------------->
	<div class="container">
		<div style="height: 50px; line-height: 50px;">
			Comment <span id="replycount" style="color: red;">${replycount}</span>
		</div>

		<c:forEach var="dto" items="${list}">
			<table class="table table-striped">
				<tr>
					<td>
						<input type=hidden value="${dto.rc_replyseq}" name="seq" id=hiddenSeq>
						<input type=hidden value="${dto.rc_replypseq}" name="pseq" id=hiddenPseq>
						<span> ${dto.rc_replyer } (${dto.rc_replydate} )</span>

					</td>
				</tr>

				<tr>
					<td id="reply">
						<textarea id="replytext" maxlength="600" style="height: 60px; width: 100%; resize: none; border: none;" readonly="readonly">${dto.rc_replytext}</textarea>
						<div style="float: right;">
							<c:if test="${dto.rc_replyer == loginID }">
								<input type=button class="modReply btn btn-outline-secondary btn-sm" value=수정 style="margin: 0 0 50 0;">
								<input type=button class="delReply btn btn-outline-secondary btn-sm" value=삭제>
								<a href="javascript:subReply()" id="subReply" style="display: none;">등록</a>
								<a href="javascript:modCancle()" id="modCancle" style="display: none;">취소</a>
							</c:if>
						</div>
					</td>
				</tr>
			</table>
		</c:forEach>

		<!----------------------- 댓글 작성 ------------------->
		<table class="table table-striped" style="text-align: center;">
			<tr>
			<!-- 로그인해야지 댓글창 열립니다 -->
				<c:choose>
					<c:when test="${dto.rc_writer == null  }">
						<textarea placeholder="로그인을 하셔야 댓글에 글을 쓸 수 있습니다." class="nreply" readonly="readonly" name="nreply" maxlength="600" style="height: 150px; width: 100%; resize: none;"></textarea>
					</c:when>

					<c:otherwise>
						<input type=hidden value="${dto.rc_seq }" id="seq" name="seq">
						<td style="border-bottom: none; line-height: 100px;" valign="middle">
							<span name=writer>${loginID }</span>
						</td>

						<td>
							<textarea placeholder="상대방을 존중하는 댓글을 남깁시다" class="reply" name="reply" maxlength="600" style="height: 150px; width: 100%; resize: none;"></textarea>
							<br> <span id="counter">(0 / 최대 500자)</span>
						</td>
						<c:if test="${blacklist != 'Y'}">
						<td style="width: 100px; line-height: 150px;">
							<input type="button" id="subBtn" class="btn btn-outline-secondary" value="댓글 작성">
						</td>
						</c:if>
					</c:otherwise>
				</c:choose>
			</tr>
		</table>
	</div>




	<script>
		/*summernote 사용 위한 func*/
		$(document).ready(function() {
			$("#summernote").summernote();
		});
		/*summernote 글 작성부분 높이값,넓이값 등등 스타일 지정 부분*/
		$("#summernote").summernote({
			height : 300,
			minHeight : null,
			maxHeight : null,
			lang : 'ko-KR'
		});

		/* '삭제하기' 버튼 누르면 알림창 뜨고 -> 삭제할 시퀀스값 controller에게 넘기기*/
		$("#del").on("click", function() {
			if (confirm("정말 삭제하시겠습니까?")) {
				location.href = "/delete.recipe?seq=${dto.rc_seq}";
			}
		});

		/* '글쓰기' 버튼 누르면 글쓰기 페이지로 넘어가기*/
		$("#write").on("click", function() {
			location.href = "/write.recipe";
		});

		/* '목록으로' 버튼 누르면 목록으로 돌아가기*/
		$("#list").on("click", function() {
			location.href = "/list.recipe?cpage=1";
		})

		/* 댓글 등록 */

		$("#subBtn").on("click", function() {

			if (!$(".reply").val()) {
				alert("댓글을 입력해주세요.");
				$(".reply").focus();
				return false;

			} else if (confirm("댓글을 등록하시겠습니까?")) {
				$.ajax({
					url : "/reply.recipe",
					dataType : "post",
					data : {
						seq : $("#seq").val(),
						writer : "writer",
						reply : $(".reply").val()
					}
				}).done(function() {
				})
				location.reload();

			}
		});

		$('.reply').on("keyup", function() {
			var content = $(this).val();
			$('#counter').html("(" + content.length + " / 최대 500자)"); //글자수 실시간 카운팅

			if (content.length > 500) {
				alert("최대 500자까지 입력 가능합니다.");
				$(this).val(content.substring(0, 200));
				$('#counter').html("(200 / 최대 500자)");
			}
		});

		/*댓글 수정 버튼*/

		let bkContents = "";

		$(".modReply").on("click", function() {
			bkContents = $("#replytext").val();

			// 부로 요소로 찾아가기 
			//(반복문으로 댓글 리스트를 뽑아오기 때문에 모든 댓글의 선택자값이 같기 때문이다)
			var reply = $(this).parent().prev();
			console.log(reply);

			reply.removeAttr("readonly");
			reply.focus();

			$(this).css("display", "none");
			$(this).attr('disabled', true);
			$(this).siblings(".delReply").css("display", "none");
			$(this).siblings("#subReply").css("display", "inline");
			$(this).siblings("#modCancle").css("display", "inline");

			// 댓글 수정 시, 다른 댓글의 '수정','삭제'버튼 안보이기
			$(".modReply").css('display', "none");
			$(".delReply").css('display', "none");

		});

		/*수정된 내용을 등록*/
		function subReply() {
			if (confirm("댓글을 수정하시겠습니까?")) {
				$.ajax({
					url : "/modifyReply.recipe",
					dataType : "post",
					data : {
						seq : $("#hiddenSeq").val(),
						pseq : $("#hiddenPseq").val(),
						writer : "writer",
						reply : $("#replytext").val()
					}
				}).done(function() {
				})

				location.reload();
				$("#replytext").attr("readonly");
				$("#modReply").css("display", "inline");
				$("#delReply").css("display", "inline");
				$("#subReply").css("display", "none");
				$("#modCancel").css("display", "none");
			}
		};

		/* 댓글 삭제 */
		$(".delReply").on("click", function() {
			if (confirm("댓글을 삭제하시겠습니까?")) {
				$.ajax({
					url : "/deleteReply.board",
					dataType : "post",
					data : {
						seq : $("#hiddenSeq").val()
					}
				}).done(function() {
				})
				location.reload();
			}
		});

		function modCancle() {
			location.href = "/detail.recipe?seq=${dto.rc_seq}";
		}
	</script>
</body>

</html>



