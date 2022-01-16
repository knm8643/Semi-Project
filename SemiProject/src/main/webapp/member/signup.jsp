<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<script
	src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<script src="https://code.jquery.com/jquery-3.6.0.js"></script>
<script>
	var confirmId = ""; // 중복확인 후 전역변수에 확인한 ID 대입.
	$(function() {
		$("#check").on("click", function() {
			if ($("#id").val() == '') {
				alert("ID 를 입력하세요.");
				$("#id").focus();
				return false;
			}
			$.ajax({
				url : "idCheck.mem",
				data : {
					id : $("#id").val()
				}
			}).done(function(resp) {
				if (resp == "true") {
					$("#checkResult").css("color", "red")
					$("#checkResult").css("font-size", "12px")
					$("#checkResult").text("이미사용중인 아이디 입니다.")
					confirmId = ""; // 중복되면 id 제거
				} else {
					$("#checkResult").css("color", "#5daf5d")
					$("#checkResult").css("font-size", "12px")
					$("#checkResult").text("사용가능한 아이디 입니다.")
					confirmId = $("#id").val(); // 중복되지 않으면 id 저장. 
				}
				
			})
		});
	})
	
	//회원가입 시 필수 값 체크
	function validation() {
		if ($("#id").val() == '') {
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
	        }
		if ($("#ps").val() == '') {
			alert("비밀번호를 입력하세요.");
			$("#ps").focus();
			return false;
		}
		var pw = $("input[name=pw]").val()
		 var num = pw.search(/[0-9]/g);
		 var eng = pw.search(/[a-z]/ig);
		 var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);

		 if(pw.length < 8 || pw.length > 20){

		  alert("특수문자를 포함한 8자리 ~ 20자리 이내로 입력해주세요.");
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
		if ($("#ssn1").val().length != 6) {
			alert("주민번호 앞자리는 6자리입니다.");
			$("#ssn1").focus();
			
			return false;
		}
		if ($("#ssn2").val() == '') {
			alert("주민번호 뒷자리를 확인해주세요.");
			$("#ssn2").focus();
			return false;
		}
		if ($("#ssn2").val().length != 7) {
			alert("주민번호 뒷자리는 7자리입니다..");
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
		var answer=confirm("회원가입을 하시겠습니까?");
		if (answer==false){
			return false
		}
		return true;

	}
	function pwcheck(pwval){
		if(pwval.id == "ps") {
			
			var pw = $("input[name=pw]").val()
			var num = pw.search(/[0-9]/g);
			var eng = pw.search(/[a-z]/ig);
			var spe = pw.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
			if ($("#ps").val() == '') {
				$('#pwcheck1').text("비밀번호를 입력하세요.");
			}else if(pw.length < 8 || pw.length > 20){
				$('#pwcheck1').text("8자리 ~ 20자리 이내로 입력해주세요.");
			}else if(pw.search(/\s/) != -1){
				$('#pwcheck1').text("비밀번호는 공백 없이 입력해주세요.");
			}else if(num < 0 || eng < 0 || spe < 0 ){
				$('#pwcheck1').text("영문,숫자, 특수문자를 혼합하여 입력해주세요.");
			}else{
				$('#pwcheck1').text("");
			}
		}
	
		if(pwval.id == "reps") {
			if ($("#reps").val() == '') {
				$('#pwcheck2').text("비밀번호를 입력하세요.");
			}else if( $("#ps").val() != $("#reps").val() ) {
	            $('#pwcheck2').text("비밀번호불일치");
	        }else{
	        	$('#pwcheck2').text("");
	        }
		}
			
	}
</script>
</head>
<body>
	<h1  align="center">
		<a href="/Main.jsp"><img src="/img/daedonglogo.png" alt="로고"
			align="top"></a>
	</h1>
	<form action="signupProc.mem" method="post"
		onsubmit="return validation()">
		<div align="center">
			<div id="contents"
				style="width: 800px; margin: 95px auto 120px; overflow: hidden;">
				<p style="font-size: 26px; font-weight: bold;">회원가입</p>
				<p style="width: 100%; height: 1px; background: #d8d8d8; margin: 40px 0;"></p>
				<p>
					<img src="../img/join2.jpg" alt="join_img">
				</p>

				<table
					style="width: 800px; margin-top: 65px; border-collapse: collapse;">
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #ccc; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">아이디<span style="color:red;font-weight:bold;">*</span>
						</th>
						<td
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;"><input
							type="text" placeholder="아이디" name=id id="id"><input
							type="button" id="check" value="중복검사"
							style="background-color: -internal-light-dark(rgb(239, 239, 239), rgb(59, 59, 59)); color: -internal-light-dark(black, white); width: 62px; height: 20px; border: none; color: #000; font-size: 11px; margin-left: 20px">
							<span id="checkResult"></span></td>
					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">비밀번호<span style="color:red;font-weight:bold;">*</span></th>
						<td
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;"><input
							type="password" name=pw id="ps" placeholder="비밀번호" onblur="pwcheck(this);"> <span id="pwcheck1" style="color:red"></span></td>
					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">비밀번호
							확인<span style="color:red;font-weight:bold;">*</span>
						</th>
						<td
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;"><input
							type="password" id="reps"  placeholder="비밀번호 확인"  onblur="pwcheck(this);"> <span id="pwcheck2" style="color:red"></span> </td>
					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">이름<span style="color:red;font-weight:bold;">*</span></th>
						<td
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;"><input
							type="text" name=name id="name" maxlength="4">
					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">주민번호<span style="color:red;font-weight:bold;">*</span></th>
						<td
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;"><input
							type="text" name=ssn1 id="ssn1" maxlength="6" placeholder="앞 6자리"
							onKeyup="this.value=this.value.replace(/[^0-9]/g,'');">-<input
							type="password" name=ssn2 id="ssn2" maxlength="7"
							onKeyup="this.value=this.value.replace(/[^0-9]/g,'');">
					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">별명<span style="color:red;font-weight:bold;">*</span></th>
						<td
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;"><input
							type="text" name="nickname" id="nickname">
					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">연락처<span style="color:red;font-weight:bold;">*</span></th>
						<td
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;"><label>
								
						</label> <select size=1 id="p1" name=phone1>
								<option value="010">010
								<option value="011">011
								<option value="016">016
						</select>-<input type="text" name="phone2" id="p2" max="9999" maxlength="4"
							onKeyup="this.value=this.value.replace(/[^0-9]/g,'');">-<input
							type="text" name="phone3" id="p3" max="9999" maxlength="4"
							onKeyup="this.value=this.value.replace(/[^0-9]/g,'');">
					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">이메일<span style="color:red;font-weight:bold;">*</span>
						</th>
						<td
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;"><input
							type="text" name=email id="email"
							style="width: 124px; height: 20px; border: 1px solid #a9a9a9; margin-right: 6px;">@
							<select name="emailweb">
								<option value="etc">선택해주세요</option>
								<option value="naver.com">naver.com</option>
								<option value="hanmail.net">hanmail.net</option>
								<option value="gmail.com">gmail.com</option>
								<option value="yahoo.com">yahoo.com</option>
								<option value="hotmail.com">hotmail.com</option>
								<option value="nate.com">nate.com</option>
						</select></td>

					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">우편번호<span style="color:red;font-weight:bold;">*</span>
						</th>
						<td
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;">
							<input type="text" name=zipcode id=zipcode max="99999" maxlength="5" onKeyup="this.value=this.value.replace(/[^0-9]/g,'');"readonly> 
							<input type="button" value="우편번호찾기" id="ad"> 
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
						</td>
					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">주소1<span style="color:red;font-weight:bold;">*</span>
						</th>
						<td
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;">
							<input type="text" name=address1 id="ad1"readonly></td>
					</tr>
					<tr>
						<th
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">상세주소<span style="color:red;font-weight:bold;">*</span>
						</th>
						<td
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;"><input
							type="text" name=address2 id="ad2"></td>
					</tr>

					<tr>
						<th 
							style="width: 120px; height: 64px; background: #eee; border-top: 1px solid #fff; text-align: left; padding-left: 11px; box-sizing: border-box; font-size: 13px">이메일
							수신동의</th>
						<td
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;"><input
							type="checkbox">이메일 수신에 동의합니다. <input type="checkbox">SNS문자를
							수신합니다.</td>
					</tr>
					<tr>
						<td colspan=2 align="center"
							style="border-top: 1px solid #ccc; padding: 20px 0 20px 12px; box-sizing: border-box;"><input
							type="submit"
							style="width: 100px; height: 40px; background: #03114f; color: #fff; border: none; cursor: pointer;"
							value="회원가입" id="regi" /> <input type="reset" value="다시입력"
							
							style="width: 100px; height: 40px; background: #03114f; color: #fff; border: none; cursor: pointer;"
							id="re" /></td>
					</tr>
				</table>
			</div>
		</div>
	</form>
</body>
</html>