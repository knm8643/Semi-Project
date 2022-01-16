<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyPage</title>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<script>
	var confirmId = ""; // 중복확인 후 전역변수에 확인한 ID 대입.
	
	function searchEditPw() {
		if ($("#ordPs").val() == '') {
			alert("이전 비밀번호를 입력하세요.");
			$("#ordPs").focus();
			return false;
		}
		if ($("#ps").val() == '') {
			alert("비밀번호를 입력하세요.");
			$("#ps").focus();
			return false;
		}

		if ($("#reps").val() == '') {
			alert("비밀번호를 입력하세요.");
			$("#reps").focus();
			return false;
		}
		
		var pw = $("input[name=pw]").val()
		 var num = pw.search(/[0-9]/g);
		 var eng = pw.search(/[a-z]/ig);
		 var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

		 if(pw.length < 8 || pw.length > 20){

		  alert("8자리 ~ 20자리 이내로 입력해주세요.");
		  return false;
		 }else if(pw.search(/\s/) != -1){
		  alert("비밀번호는 공백 없이 입력해주세요.");
		  return false;
		 }else if(num < 0 || eng < 0 || spe < 0 ){
		  alert("영문,숫자, 특수문자를 혼합하여 입력해주세요.");
		  return false;
		 }
	
		if( $("#ps").val() != $("#reps").val() ) {
           alert("비밀번호불일치");
           return false;
         }
		
		// 변경 버튼 비횔성화 처리
		$("#edit").prop("disabled", true);

		
		$.ajax({
			url : "editPw.mem",
			data : {
				ps : $("#ps").val(),
				ordPs : $("#ordPs").val()
			}
		}).done(function(resp) {
			// 변경 버튼 횔성화 처리			
			$("#edit").prop("disabled", false);
			
			if (resp == "loginSucess") {
				alert("수정되었습니다.")
				window.close();
			} else if( resp == "loginFail"){
				alert("실패했습니다. 기존 비밀변호를 확인해주세요.")
			}
		})
	}
</script>
</head>
<body>
	<form action="updateProc.mem" method="post" onsubmit="return validation()">
		<div align="center">
			<div id="contents" style="width: 70%;overflow: hidden;">
				<p style="font-size: 26px; font-weight: bold;">비밀번호 변경</p>
				<p style="width: 100%; height: 1px; background: #d8d8d8; margin: 40px 0;"></p>

				<table style="width: 70%; margin-top: 65px; border-collapse: collapse;margin-bottom: 25px; ">
					<tr>
						<td align="right">이전 비밀번호 :
						<td><input type="password" name=ordPs id="ordPs">
					</tr>
					<tr>
						<td align="right">비밀번호 :
						<td><input type="password" name=pw id="ps">
					</tr>
					<tr>
						<td align="right">비밀번호 확인 :
						<td><input type="password" id="reps">
					</tr>

				</table>
				<a style="text-decoration: none;" id="edit" onclick="searchEditPw()">
					<button type=button style="background: #03114f; color: #fff; text-align: center;cursor: pointer;">변경</button>
				</a>
				<a style="text-decoration: none;"onclick="window.close();" >
						<button type=button	style="background: #03114f; color: #fff; text-align: center;cursor: pointer;">닫기</button>
				</a>
			</div>
		</div>
	</form>
</body>
</html>