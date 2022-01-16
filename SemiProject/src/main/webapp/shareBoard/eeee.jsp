<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Index</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- include libraries(jQuery, bootstrap) -->
<link href="http://netdna.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.css" rel="stylesheet">
<script src="http://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.js"></script>
<script src="http://netdna.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.js"></script>
<!-- include summernote css/js-->
<link href="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.8/summernote.css" rel="stylesheet">
<script src="http://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.8/summernote.js"></script>

<link rel="stylesheet " href="/Css.css " type="text /css ">
<link rel="preconnect " href="https: //fonts.googleapis.com ">
<script src="https: //kit.fontawesome.com /b06cee0c6f.js " crossorigin="anonymous ">
</script>
<script src="/toggle.js " defer></script>
<script>

</script>
<style type="text/css">
* {
	font-family: 'Nanum Gothic', sans-serif;
	box-sizing: border-box;
}

a {
	text-decoration: none
}


</style>
</head>

<body>

	<!-- 네비바  -->
	<jsp:include page="/Navi.jsp"></jsp:include>

	<!-------------------------------  게시판 코드 시작 ---------------------------->

	<form action="/shareProc.board" method="post">
		<div class="container" style="padding: 10px;">
			<!-- 네비바 하단 게시판 이미지 넣는 부분 -->
			<div class="row">
				<div class="col-12 p-3" style="text-align: center;">
					<img src="/img/shareboard.JPG" style="display: block; margin: 0px auto; max-width: 100%; height: auto;">
				</div>
			</div>

			<!-- 글 작성 부분-->

			<input type="text" name="title" id="title" placeholder="제목을 입력하세요." style="margin: 10px; width: 100%; height:50px; border: none; font-size: 19px;";>

			<!--  summernote API -->
			<div class="row">
				<div class="col-12">
					<textarea id="summernote" class="content" name="content"></textarea>
				</div>
				<!-- 등록 / 취소 버튼  -->
				<div class="col-12" >
					<input type="submit"  class="btn btn-default" id="subbtn" value="등록" style="float: right;" >
					<input type="button"class="btn btn-default" id="canclebtn" value="취소" style="float: right">
				</div>
			</div>
		</div>
	</form>
	<script>
	$(document).ready(function() {
		$('#summernote').summernote({
		  // 에디터 높이
		  height: 500,
		  disableResizeEditor: true,
		  // 에디터 한글 설정
		  lang: "ko-KR",
		  callbacks:{
			  	onImageUpload: function(files,editor,welEditable) {
	            	sendFile(files[0],this);}
		  		},
		  toolbar: [
			    // 글꼴 설정
			    ['fontname', ['fontname']],
			    // 글자 크기 설정
			    ['fontsize', ['fontsize']],
			    // 굵기, 기울임꼴, 밑줄,취소 선, 서식지우기
			    ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
			    // 글자색
			    ['color', ['forecolor','color']],
			    // 글머리 기호, 번호매기기, 문단정렬
			    ['para', ['ul', 'ol', 'paragraph']],
			    // 줄간격
			    ['height', ['height']],
			    // 그림첨부, 링크만들기, 동영상첨부
			    ['insert',['picture','link','video']],
			    // 코드보기, 확대해서보기, 도움말
			    ['view', ['codeview', 'help']]
			  ],
			  // 이미지 업로드 후 사이즈,정렬 조정
				 image: [
					  ['imagesize', ['imageSize100', 'imageSize50', 'imageSize25']],
					  ['float', ['floatLeft', 'floatRight', 'floatNone']],
					  ['remove', ['removeMedia']],
					  ['custom', ['imageAttributes']],
				  ],
			  // 추가한 글꼴
			fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체'],
			 // 추가한 폰트사이즈
			fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72']	
		});
	});
	
	
	function sendFile(file, editor) { // summernote에서 이미지 업로드시 실행할 함수
	    data = new FormData(); // 파일 전송을 위한 폼생성
	    data.append("uploadFile", file);
	    $.ajax({ // ajax를 통해 파일 업로드 처리
	        data : data,
	        type : "POST",
	        url : "/uploadFile.board",
	        cache : false,
	        contentType : false,
	        enctype : "multipart/form-data",
	        processData : false,
	        success: function(data){
	        	let url = JSON.parse(data); //컨트롤러에서 넘어온 url을 json으로 변환
	    	  	console.log("jsp에서 받은 data : " + url);
	    	  	$(editor).summernote('insertImage', url); // 게시판에 사진 업로드
	        }
	    })
	}
	
		/* 취소 버튼*/
		$("#canclebtn").on("click", function() {
			if (confirm("작성중인 글쓰기를 종료하겠습니까?")) {
				location.href = "/list.board?cpage=1";
			}
		})

		/*제목 혹은 내용이 빈칸일 경우 알림창을 띄운다.*/
		$("#subbtn").on("click", function() {
			if (!$("#title").val()) {
				alert("제목을 입력해주세요.");
				$("#title").focus();
				return false;
			} else if (!$("#summernote").val()) {
				alert("내용을 입력해주세요.");
				$("#summernote").focus();
				return false;
			}
		})
	</script>

</body>

</html>