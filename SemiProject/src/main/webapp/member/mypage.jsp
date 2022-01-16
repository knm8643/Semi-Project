<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MyPage</title>
<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<script
	src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
	var confirmId = ""; // 중복확인 후 전역변수에 확인한 ID 대입.
	
	var updateYn = "${updateYn}";
	<%
	session.setAttribute("updateYn", "N");
	%>
	window.onpageshow = function (event) {
	    if (event.persisted || (window.performance && window.performance.navigation.type == 2)) {
	        // 뒤로가기 했을 때 처리할 이벤트
	    }else{
	    	
			if(updateYn == "Y"){
				alert("수정되었습니다.");
				updateYn = "";
			}
	    }
	}

	function validation() {
		/* if ($("#id").val() == '') {
			alert("ID 를 입력하세요.");
			$("#id").focus();
			return false;
		}
		
		// 전역 변화 값과 입력값 같으면 pass
		if (confirmId != $("#id").val()){
			alert("ID 중복확인 해주세요.");
			return false;
		}
		
		  var idReg = /^[a-z]+[a-z0-9]{5,11}$/g;
		    if( !idReg.test( $("input[name=id]").val() ) ) {
		        alert("아이디는 영문자로 시작하는 6~12자 영문자 또는 숫자이어야 합니다.");
		        return false;
		    } */
		/* 	if ($("#ps").val() == '') {
		 alert("비밀번호를 입력하세요.");
		 $("#ps").focus();
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
		
		
		 } */
		if ($("#name").val() == '') {
			alert("이름을 입력하세요.");
			$("#name").focus();
			return false;
		}
		if ($("#ssn1").val() == '') {
			alert("주민번호 앞자리를 확인해주세요.");
			$("#ssn1").focus();
			return false;
		}
		if ($("#ssn2").val() == '') {
			alert("주민번호 뒷자리를 확인해주세요.");
			$("#ssn2").focus();
			return false;
		}

		if ($("#nickname").val() == '') {
			alert("사용할 닉네임을 확인해주세요.");
			$("#nickname").focus();
			return false;
		}
		if ($("#p1").val() == '') {
			alert("휴대폰 번호를 확인해주세요.");
			$("#p1").focus();
			return false;
		}
		if ($("#p2").val() == '') {
			alert("휴대폰 번호를 확인해주세요.");
			$("#p2").focus();
			return false;
		}
		if ($("#p3").val() == '') {
			alert("휴대폰 번호를 확인해주세요.");
			$("#p3").focus();
			return false;

		}
		if ($("#email").val() == '') {
			alert("이메일을 확인해주세요.");
			$("#email").focus();
			return false;
		}
		if ($("#ad").val() == '') {
			alert("우편번호를 확인해주세요.");
			$("#ad").focus();
			return false;
		}
		if ($("#ad1").val() == '') {
			alert("주소를 확인해주세요.");
			$("#ad1").focus();
			return false;
		}
		if ($("#ad2").val() == '') {
			alert("주소를 확인해주세요.");
			$("#ad2").focus();
			return false;
		}

		var answer=confirm("수정 하시겠습니까?");
		if (answer==false){
			return false
		}
		return true;

		}
	function searchEditPw() {
		window.open("/member/editPwPopup.jsp", "a", "width=820, height=400, left=100, top=50"); 
}
</script>
</head>
<body>
	<h1 align="center">
		<a href="/Main.jsp"><img src="/img/daedonglogo.png" alt="로고" align="top"></a>
	</h1>
	<form action="updateProc.mem" method="post"
		onsubmit="return validation()">

		<div align="center">
			<div id="contents"
				style="width: 800px; margin: 95px auto 120px; overflow: hidden;">
				<p style="font-size: 26px; font-weight: bold;">회원가입정보</p>
				<p
					style="width: 100%; height: 1px; background: #d8d8d8; margin: 40px 0;"></p>
				<p>
					<img src="/img/login_icon.png"
						style="width: 200px; background: black">
				</p>

				<table
					style="width: 800px; margin-top: 65px; border-collapse: collapse;">

					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #ccc; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">아이디</th>

						<td
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;"><span>${dto.id}</span>
							<input type="hidden" name=id id="id" value="${dto.id }">

							<a onclick="searchEditPw()">
								<button type=button id="btnEditPw"
									style="background: #03114f; color: #fff; text-align: center; cursor: pointer;">비밀번호
									변경</button>
						</a></td>
					</tr>
					<!-- 				<tr>
					<td align="right">비밀번호 :
					<td><input type="password" name=pw id="ps">
				</tr>
				<tr>
					<td align="right">비밀번호 확인 :
					<td><input type="password" id="reps">
				</tr> -->
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">이름
						</th>

						<td><span>${dto.name }</span> <input type="hidden" name=name
							id="name" value="${dto.name }"></td>
					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">전화번호</th>

						<td><input type=text name=phone value="${dto.phone }">
					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">이메일
						</th>
						<td><input type="text" name=email id="email"
							value="${dto.email }">
					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">우편번호
						</th>
						<td><input type="text" id=zipcode name=zipcode readonly value="${dto.zipcode}"> 
							<input type="button" value="찾기" id="ad">
							<script>
								//카카오 api 우편번호 검색 
								document.getElementById("ad").onclick = function(){
							      new daum.Postcode({
							          oncomplete: function(data) {
							               // 우편번호와 주소 정보를 해당 필드에 넣는다.
							               document.getElementById('zipcode').value = data.zonecode;
							               document.getElementById("ad1").value = data.roadAddress;
							              
							               }
							           }).open();
						       	}
							</script>
					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">주소
						</th>
						<td><input type="text" name=address1 id="ad1" readonly
							value="${dto.address1 }">
					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">상세주소
						</th>
						<td><input type="text" name=address2 id="ad2"
							value="${dto.address2 }">
					</tr>
					<tr>
						<td colspan=2 align="center"
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;"><input
							type="submit"
							style="width: 100px; height: 40px; background: #03114f; color: #fff; border: none; cursor: pointer;"
							value="수정완료" id="regi" /> <!-- <td colspan=2 align="center"><input type="submit" value="수정"
							id="regi"> <a href="javaScript:history.go(-1)"><img
								src="img/237FEE45557D517D08.png" style="width: 18px;"></a></td> -->
					</tr>

				</table>
			</div>
		</div>
	</form>
</body>
</html>