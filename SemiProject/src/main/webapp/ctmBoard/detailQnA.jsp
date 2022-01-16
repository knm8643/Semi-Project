<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${dto.title }</title>
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
		position:absolute;
		top:400px;
		left:50%;
		transform:translateX(-50%);	
		width:60%;
	}
	#article{
		height:700px;
		margin:auto;
	}
	#title{height:30px;border:1px solid black;}
	#write_date{height:30px;}
	#content{height:600px; overflow:scroll;}
	#btns{height:40px;}
	#reply{margin:0px;padding:0px;}
	#content{border:1px solid black;}
	
	textarea{width:100%;resize:none;overflow:auto;}
</style>
</head>
<body>
	<jsp:include page="/Navi.jsp"></jsp:include>
	<div class=container>
		<div class=row id=article>
			<input type=hidden value="${dto.seq}" name="seq" id="seq">
			<div class=col-12 id=title name=title placeholder="글 제목을 입력하세요" size=100%  readonly>${dto.title }</div>
			<div class=col-12 id=write_date>${dto.formedDate }</div>
			<div class=col-12 id=content name=contents>${dto.contents }</div>
			<div class=col-12 id=btns align=right style="padding:0px;">
				<a href="/list.qna?cpage=1"><input type=button value="목록으로"></a>
				<c:if test="${dto.writer == loginID }">
					<button type=submit id=mod>수정하기</button>
					<button type=button id=del>삭제하기</button>
				</c:if>
			</div>
		</div>

			<!-- <input type=hidden value="${dto.seq}" name="seq"> -->
			<div class=row id=reply>
				<div class=col-12 id=reply><span name=writer>${loginID }</span><br>
					<!-- <input type=text name="cmtCnt" id="cmtCnt" placeholder="여러분의 소중한 댓글을 입력해주세요"> -->
					<textarea name="cmtCnt" id="cmtCnt" placeholder="여러분의 소중한 댓글을 입력해주세요"onKeyup="var m=50;var s=this.scrollHeight;if(s>=m)this.style.pixelHeight=s+4"></textarea>
					<div class="test_cnt">(0 / 100)</div>
				</div>
				<c:if test="${loginID != null && blacklist != 'Y'}">
				<div class=col-12 style="padding:0px;">
					<input type=submit id="cmtBtn" value=댓글달기>
				</div>
				</c:if>
			</div>

		<c:forEach var="rdto" items="${list}">
			<div class=row id=comments>
				<div class=col-12 id=repTitle>${rdto.writer } ( ${rdto.formedDate } )</div>
				<div class=col-12 name=rep>
					<input type=hidden value="${rdto.seq}" name="repSeq">
					<input type=hidden value="${rdto.parentSeq}" name="repPseq">
					<!-- <input type=text class=repContent name=repContent readonly value=${rdto.contents }> -->
					<textarea type=text class=repContent name=repContent readonly>${rdto.contents }</textarea>
					<div class="rep_cnt" style="display:none;">(0 / 100)</div>
				</div>
				<c:if test="${rdto.writer == loginID }">
				<div class=col-12 align=right>
					<input type=button class=modCnt value=수정>
					<input type=button class=delCnt value=삭제>
					<input type=button class=modOk style="display:none" value=수정하기>
					<input type=button class=modCancel style="display:none" value=수정취소>
				</div>
				</c:if>
			</div>		
		</c:forEach>
	</div>
	
	<script>
		$("#del").on("click",function(){ <!--댓글 삭제 버튼 클릭-->
			if(confirm("정말 삭제하시겠습니까?")){
				location.href="/delete.qna?seq=${dto.seq}";
			}
		})
		
		$("#mod").on("click",function(){ <!--댓글 수정 버튼 클릭-->	
			location.href="/mod.qna?seq=${dto.seq}";
		});
		
		$("#cmtBtn").on("click",function(){   <!-- 댓글달기 버튼 기능 -->
			if(!$("#cmtCnt").val()){ <!--댓글달기 버튼 클릭 시 내용 없으면 alert창 띄움-->
				alert("댓글 내용을 입력해주세요.");
				$("#cmtCnt").focus();
				return false;
			}
			$.ajax({		<!-- 댓글 달고 성공 시 댓글 추가되면서 페이지 리로드 -->
				url:"/commentIn.qna",
				dataType:"post",
				data:{
					pseq:$("#seq").val(),
					writer:"writer",
					contents:$("#cmtCnt").val(),
					success: function(data) {location.reload();}
				}
			})
		})
		
		$(".delCnt").on("click",function(){  <!--댓글 삭제 버튼 클릭시-->
			if(confirm("댓글을 정말 삭제하시겠습니까?")){
				$.ajax({
					url:"/deleteCnt.qna",
					dataType:"post",
					data:{seq:$(this).parent().prev().children("input[name=repSeq]").val()},
				})
				location.reload();
			}
		});
		
		let bkContents = "";
		
		$(".modCnt").on("click",function(){  <!--댓글 수정버튼 클릭시-->
			
			bkContents = $(".repContent").val();
			
			$(this).parent().prev().children(".repContent").removeAttr("readonly");
			$(this).parent().prev().children(".repContent").focus();
			$(this).parent().prev().children(".rep_cnt").css("display","inline");
			$(this).css("display","none");
			$(this).siblings(".delCnt").css("display","none");
			$(this).siblings(".modOk").css("display","inline");
			$(this).siblings(".modCancel").css("display","inline");
		});
		
		$(".modCancel").on("click",function(){ <!--댓글 수정취소 버튼 클릭시-->
			
			$(this).parent().prev().children(".repContent").val(bkContents);
			$(this).parent().prev().children(".repContent").attr("readonly","readonly");
			$(this).parent().prev().children(".rep_cnt").css("display","none");
			$(this).siblings(".modCnt").css("display","inline");
			$(this).siblings(".delCnt").css("display","inline");
			$(this).siblings(".modOk").css("display","none");
			$(this).css("display","none");
		});
		
		$(".modOk").on("click",function(){ <!--댓글 수정하기 버튼 클릭시-->
			if(confirm("댓글을 수정하시겠습니까?")){
				$(this).parent().prev().children(".repContent").attr("readonly","readonly");
				$(this).parent().prev().children(".rep_cnt").css("display","none");
				$(this).siblings(".modCnt").css("display","inline");
				$(this).siblings(".delCnt").css("display","inline");
				$(this).css("display","none");
				$(this).siblings(".modCancel").css("display","none");
				$.ajax({
					url:"/modCnt.qna",
					dataType:"post",
					data:{
						seq:$(this).parent().prev().children("input[name=repSeq]").val(),
						pseq:$(this).parent().prev().children("input[name=repPseq]").val(),
						writer:"writer",
						content:$(this).parent().prev().children(".repContent").val()},
					success: function(data) {
						location.reload();
					}
				})
			}
		}); 

		$(document).ready(function() { //댓글 100글자 입력 제한 코드
		    $('#cmtCnt').on('keyup', function() { //댓글 100글자 입력 제한 코드
		        $('.test_cnt').html("("+$(this).val().length+" / 100)");
		 
		        if($(this).val().length > 100) {
		            $(this).val($(this).val().substring(0, 100));
		            $('.test_cnt').html("(100 / 100)");
		        }
		    });
		});
		    $('.repContent').on('keyup', function() {
		        $(this).next().html("("+$(this).val().length+" / 100)");
		 
		        if($(this).val().length > 100) {
		            $(this).val($(this).val().substring(0, 100));
		            $('.repContent').next().html("(100 / 100)");
		        }
		    });
		
	</script>
</body>
</html>