<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${dto.title }</title>
<!-- include libraries(jQuery, bootstrap) -->
<link href="https://stackpath.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>

<!-- include summernote css/js -->
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet"> 
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
<script src=" https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/lang/summernote-ko-KR.min.js"></script>

<script type="text/javascript">
	function sendFile(file, editor) { // summernote에서 이미지 업로드시 실행할 함수
	    data = new FormData(); // 파일 전송을 위한 폼생성
	    data.append("uploadFile", file);
	    $.ajax({ // ajax를 통해 파일 업로드 처리
	        data : data,
	        type : "POST",
	        url : "/uploadFile.notice",
	        cache : false,
	        contentType : false,
	        processData : false,
	        success: function(data){
	        	let url = JSON.parse(data); //컨트롤러에서 넘어온 url을 json으로 변환
	    	  	//console.log(url);
	    	  	$(editor).summernote('editor.insertImage', url); // 게시판에 사진 업로드
	        }
	    })
	}
</script>
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
	}
	#title{height:30px;}
	#write_date{height:30px;}
	#content{height:600px;}
	#btns{height:40px;}
	textarea{resize:none;}
</style>
</head>
<body>
	<jsp:include page="/Navi.jsp"></jsp:include>
	<form action="/modify.notice" method="post" id="frmDetail">
		<div class=container>
			<div class=row id=article>
				<input type=hidden value="${dto.seq}" name="seq">
				<div class=col-12 id=title><input type=text id=title name=title placeholder="글 제목을 입력하세요" size=100% value="${dto.title }"></div>
				<div class=col-12 id=write_date>${dto.write_date }</div>
				<div class=col-12 id=content>
					<textarea rows=25 cols=127 id="summernote" name=contents placeholder="글 내용을 입력하세요" readonly>${dto.contents }</textarea>
				</div>
				<div class=col-12 id=btns align=right>
					<button type=button id=modOk>수정완료</button>
					<button type=button id=modCancel>취소</button>
				</div>
			</div>
		</div>
	</form>
	
	<script>
		$("#del").on("click",function(){
			if(confirm("정말 삭제하시겠습니까?")){
				location.href="/delete.notice?seq=${dto.seq}";
			}
		})
		
		let bkTitle = $("#title").val();
		let bkContents = $("#contents").val();
		
		
		$("#modOk").on("click",function(){
			if(confirm("이대로 수정하시겠습니까?")){
				$("#frmDetail").submit();
			}
		});
		
		$("#modCancel").on("click",function(){
			if(confirm("정말 취소하시겠습니까?")){
				location.href="/detail.notice?seq=${dto.seq}";
			}
		});

		$("#submit").on("click",function(){
			if($("#title").val()==""){
				alert("제목을 입력해주세요.");
				return false;
			}else if($("#contents").val()==""){
				alert("내용을 입력해주세요.");
				return false;
			}
		})
		$(document).ready(function() {
			$('#summernote').summernote({
			  // 에디터 높이
			  height: 535,
			  disableResizeEditor: true,
			  // 에디터 한글 설정
			  lang: "ko-KR",
			  callbacks:{
				  	onImageUpload: function(files, editor, welEditable) {
		            	sendFile(files[0],this);}
			  		},
			  // 에디터에 커서 이동 (input창의 autofocus라고 생각하시면 됩니다.)
			  focus : true,
			  toolbar: [
				    // 글꼴 설정
				    ['fontname', ['fontname']],
				    // 글자 크기 설정
				    ['fontsize', ['fontsize']],
				    // 굵기, 기울임꼴, 밑줄,취소 선, 서식지우기
				    ['style', ['bold', 'italic', 'underline','strikethrough', 'clear']],
				    // 글자색
				    ['color', ['forecolor','color']],
				    // 표만들기
				    ['table', ['table']],
				    // 글머리 기호, 번호매기기, 문단정렬
				    ['para', ['ul', 'ol', 'paragraph']],
				    // 줄간격
				    ['height', ['height']],
				    // 그림첨부, 링크만들기, 동영상첨부
				    ['insert',['picture','link','video']],
				    // 코드보기, 확대해서보기, 도움말
				    ['view', ['codeview', 'help']]
				  ],
				  // 추가한 글꼴
				fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋음체','바탕체'],
				 // 추가한 폰트사이즈
				fontSizes: ['8','9','10','11','12','14','16','18','20','22','24','28','30','36','50','72']
				
			});
		});
	</script>
</body>
</html>