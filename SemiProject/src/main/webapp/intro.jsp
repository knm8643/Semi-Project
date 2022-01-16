<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
    <!-- CSS only -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <!-- JavaScript Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
</head>
<style>
        #imgtext {
            padding: 5px 10px;
        }

        #imgtext2 {
            padding: 5px 10px;
        }

        .navi {
            height: 100px;
        }

        #thm {
        	top:25px;
            line-height: 200%;
            position: relative;
            text-align: center;
            background-image: url("배경.png");
            height: 180px;
        }

        #thm2 {
            background-color: black;
            text-align: center;
            height: 500px;
        }

        #intro {
       		position:relrative;
            text-align: center;
        }

        #introtext {
            position: absolute;
            top:900px;
            height: 150px;
            line-height: 300%;
        }

        #num1 {
            font-size: 30px;
        }

        #imgbox:hover {
            cursor: pointer;
            transform: scale(0.9);
            transition: all 0.2s linear;
        }
        .bottom{
        position:relative;
        }
        
         @media screen and (max-width: 1500px ) {
        #introtext{top:820px;}
        }
        
        @media screen and (max-width: 1200px ) {
        #introtext{top:760px;}
        }
        
        @media screen and (max-width: 1110px ) {
        #introtext{top:650px;}
        }
        @media screen and (max-width: 800px ) {
        #introtext{top:580px;}
        }
        @media screen and (max-width: 510px ) {
        #introtext{top:530px;}
        }
        
    </style>
<body>
       <jsp:include page="Navi.jsp"></jsp:include>
 <div class="main">
        <div class="body">
            <div id="thm">
                <div id="imgtext">
                    <h1>우리의 사이트를 소개합니다</h1>
                </div>
                <span>신입개발자들에게 잊지 못할 모험을 선사하는</span><br>
                <span> 자가혁신적인 대동맛지도</span>
            </div>
                    <div id="carouselExampleControls" class="carousel slide" data-bs-ride="carousel" >
                        <div class="carousel-inner">
                            <div class="carousel-item active">
                                <img src="img/img1.png"class="d-block w-100" alt="...">
                            </div>
                            <div class="carousel-item">
                                <img src="img/img2.png" class="d-block w-100" alt="...">
                            </div>
                            <div class="carousel-item">
                                <img src="img/img3.png" class="d-block w-100" alt="...">
                            </div>
                        </div>
                        <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleControls"
                            data-bs-slide="prev">
                            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Previous</span>
                        </button>
                        <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleControls"
                            data-bs-slide="next">
                            <span class="carousel-control-next-icon" aria-hidden="true"></span>
                            <span class="visually-hidden">Next</span>
                        </button>
                    </div>
                </div>
                </div>
            </div>
        </div>

            <div id="intro">
                <div id="introtext">
                    <span id="num1"><b>우리 이야기</b></span><br>
                    <span id="num2"><b>이것저것 다양하게 만져보자!</b></span><br>
                    <span id="num3">좌충우돌 총체적 어려움을 겪는 이세계의 중고 신입들은 맛집지도를</span><br>
                    <span id="num3">개발하려고 한다. 과연 놀라운 기능을 경험하게 만들 수 있을지 그들의 도전이 시작됩니다!</span>
                    <div id="imgbox">
                        <img src="/메인 테마.jpg" style="width: 400px; height: 300px;">
                    </div>
                    <span id="num2"><b>우리에겐 일상으로 느껴졌던 한끼가 어떤 이에겐 소중한 한끼..</b></span><br>
                    <span id="num3">아직도 우리 주변에는 미각을 포기한채 배만 채우며 하루하루 생활하고 있는</span><br>
                    <span id="num3">지인들이 너무나도 많습니다. 이런이들에게 즐거움을 선사하고자 차세대 혁신 플랫폼을 제공하는 곳이 있습니다</span><br>
                    <span id="num4">바로 <b>대동맛지도</b> 입니다.</span><br>

        <div class="bottom">
            <img src="img/footer.png" style="width: 100%; height: auto;">
        </div>
    </div>
</body>
</html>