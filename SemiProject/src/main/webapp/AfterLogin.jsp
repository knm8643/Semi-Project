<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
 	<link rel="stylesheet" href="Css.css" type="text/css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <script src="https://kit.fontawesome.com/b06cee0c6f.js" crossorigin="anonymous"></script>
    <script src="toggle.js" defer></script>
    <style>
    	#nav_login{
    	width:250px;
    	}
        .content {
            /*컨텐츠 넓이지정*/
            position: relative;
            top: 60px;
            width: 100%;
            margin: auto;
            max-width: 1530px;
        }

        .slides {
            /*슬라이드 아이템이 나올 뼈대 지정*/
            position: relative;
            width: 100%;
        }

        .slide_item {
            /*슬라이드 아이템을 absolute로 겹쳐놓고 투명하게 하기*/
            position: absolute;
            width: 100%;
            opacity: 0;
            transition: all 0.3s;
        }

        .ontheSlide {
            /*현재 아이템에 붙여줄 클래스*/
            opacity: 1;
            transition: all 0.3s;
        }

        .ontheThumbnail {
            /*현재 썸네일에 붙여줄 클래스*/
            border: 1px dashed red;
        }

        .nextButton,
        .prevButton {
            /*다음,이전 버튼을 드래그되지 않게 방지해주기*/
            -webkit-user-select: none;
            -ms-user-select: none;
            user-select: none;
            position: absolute;
            cursor: pointer;
            max-width: 64px;
            max-height: 64px;
            margin-top: 8%;
            width: 3%;
        }

        .nextButton {
            /*다음 버튼을 오른쪽으로 보내기*/
            right: 0;
        }

        .prevButton {
            /*왼쪽 버튼을 왼쪽으로 보내기*/
            left: 0;
        }

        .Thumbnail {
            /*썸네일 아이템을 감쌀 뼈대*/
            display: none;
            flex-flow: row wrap;
            width: 100%;
            height: 50px;
            position: absolute;
            bottom: 0px;
            justify-content: center;
        }

        .thumbnail_item {
            /*썸네일 아이템의 길이 설정*/
            width: 98px;
        }

        * {
            box-sizing: border-box
        }

        .mySlides {
            display: none
        }

        img {
            vertical-align: middle;
        }

        .imagebox1 {
            border: 5px solid black;
        }


        .board {
            position: absolute;
            text-align: left;
            /* float: left; */
            margin-right: 10%;
            top: 13%;
            width: 30%;
            height: 100%;
            border-top: 4px double red;
            font-size: 25px;
            background-color: white;
        }

        .board li {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            margin-left: 3%;
        }

        .board1 {
            position: absolute;
            text-align: left;
            left: 35%;
            top: 13%;
            width: 30%;
            border-top: 4px double red;
            font-size: 25px;
            background-color: white;
        }

        .board1 li {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            margin-left: 3%;
        }

        .board2 {
            position: absolute;
            text-align: left;
            float: left;
            margin-right: 10%;
            left: 70%;
            top: 13%;
            width: 30%;
            border-top: 4px double rgb(8, 207, 8);
            font-size: 25px;
            background-color: white;
        }

        .board2 li {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            margin-left: 3%;
        }

        .border {
            border-bottom: 1px dotted black;
            padding-bottom: 4px;
        }

        .bestwrite {
            font-size: 17px;
        }

        .bullet {
            color: red;
            font-size: 15px;
            padding-right: 10px;
        }

        .bullet1 {
            color: rgb(8, 207, 8);
            font-size: 15px;
            padding-right: 10px;
        }

        .board_header {
            position: absolute;
            top: -40px;
            border-top: 4px double red;
            font-size: 23px;
            width: 100%;
            text-align: center;
            background-color: white;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .board_header1 {
            position: absolute;
            top: -40px;
            border-top: 4px double rgb(8, 207, 8);
            font-size: 23px;
            width: 100%;
            text-align: center;
            background-color: white;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .sub {
            color: red;
            font-size: 21px;
        }

        .sub1 {
            color: rgb(8, 207, 8);
            font-size: 21px;
        }

        /* .line {
            position: relative;
            border: 2px solid orange;
            top: 24%;
        }

        .line1 {
            position: relative;
            border: 2px solid orange;
            top: 560px;
        }

        .line2 {
            position: relative;
            border: 2px solid orange;
            top: 530px;
            left: -188px;
            width: 1905px;
        } */

        .board_background {
            max-width: 1530px;
            position: relative;
            margin: auto;
            top: 370px;
            width: 100%;
            height: 14%;
        }

        .recipes {
            position: relative;
            top: 30px;
        }

        /* .recipes_img {
            position: absolute;
            top: 37%;
            left: 22.5%;
        }

        .recipes_img1 {
            position: absolute;
            top: 37%;
            left: 38.5%;
        }

        .recipes_img2 {
            position: absolute;
            top: 37%;
            left: 54.3%;
        }

        .recipes_img3 {
            position: absolute;
            top: 37%;
            left: 70.2%;
        } */

        .recipe_box1 {
            position: absolute;
            max-width: 250px;
            max-height: 250px;
            width: 14.5%;
            height: 14.5%;
            top: 40%;
            left: 22%;
        }

        .recipe_box2 {
            position: absolute;
            max-width: 250px;
            max-height: 250px;
            width: 14.5%;
            height: 14.5%;
            top: 40%;
            left: 38%;
        }

        .recipe_box3 {
            position: absolute;
            max-width: 250px;
            max-height: 250px;
            width: 14.5%;
            height: 14.5%;
            top: 40%;
            left: 54%;
        }

        .recipe_box4 {
            position: absolute;
            max-width: 250px;
            max-height: 250px;
            width: 14.5%;
            height: 14.5%;
            top: 40%;
            left: 70%;
        }

        .list_con {
            position: relative;
            margin: auto;
            max-width: 1560px;
            max-height: 900px;
            top: 380px;
        }

        /* .grid-image {
            position: absolute;
            left: 8%;
            flex-wrap: wrap;
            display: flex; 
            top: 25%;
            width: 100%;
            max-width: 1560px;
            height: 100%;
            align-items: flex-start;
        } */

        /* .list_img {
            position: relative;
            margin: 0 15px 15px 0;
        }

        .list_img img:nth-of-type(5n),
        .list_img img:last-child {
            margin-right: 0;
        } */

        .list_boxes {
            max-width: 250px;
            position:absolute;
            top: 28%;
            text-align: center;
            width: 30%;
            height: 30%; 
            max-width: 250px;
            max-height: 250px;
            font-size: 20px;
            font-style:italic;
        }
        #list_box1{
            left: 8%;
        }
        #list_box2{
            left:25%;
        }
        #list_box3{
            left:42%;
        }
        #list_box4{
            left:59%;
        }
        #list_box5{
            left:76%;
        }
        #list_box6{
            left:8%;
            top: 65%;
        }
        #list_box7{
            left:25%;
            top: 65%;
        }
        #list_box8{
            left:42%;
            top: 65%;
        }
        #list_box9{
            left:59%;
            top: 65%;
        }
        #list_box10{
            left:76%;
            top: 65%;
        }
        .footer {
            position: relative;
            width: 100%;
            height: 350px;
            bottom:0px;
        }
        .footer>img {
            position: absolute;
            width: 100%;
            height: 100%;
            max-height:100%;
        }
        /* @media screen and (max-width:640px){
        .grid-image img {
            width:calc(50% - 15px);
        }
        }
        @media screen and (max-width:480px){
        .grid-image img:nth-of-type(2n) {
            margin-right:0;
        }
        .grid-image img:nth-of-type(3n) {
            margin-right:15px;
        }
        }
        @media screen and (max-width: 1200px ) {
            .board_background {
            top: 17%;
            }
            .grid-image{
                top: 16%;
            }

        } */

        /* @media screen and (max-width: 1000px) {
            .board_background {
                top: 14%;
            }
            .list_con{
                top: 14%;
            }
            .grid-image{
                top: 14%;
            }

        }

        @media screen and (max-width: 800px) {
            .board_background {
                top: 12%;
            }
            .list_con{
                top: 12%;
            }
            .grid-image{
                top: 14%;
            }
        } */

       /* @media screen and (max-width: 605px) {
            .board_background {
                top: 10%;
            }
            .list_con{
                top: 10%;
            }
            .grid-image{
                top: 14%;
            }

        } */
    </style>
</head>
<body>
<style type="text/css">
        a {
            text-decoration: none
        }
    </style>
    <nav class="nav_con">
        <div id="nav_login">
            <div><a href="#" class="login"><b>로그아웃</b></a></div>
            <div><a href="#" class="member"><b>회원정보수정</b></a></div>
        </div>
        <div id="nav_logo"><a href="#"><img src=img/Logo.png style="height:90px;"></a></div>
        <ul id="nav_menu">
            <li><a href="#" class="menu1"><b>사이트 소개</b></a></li>
            <li><a href="#" class="menu2"><b>내주변 맛집</b></a></li>
            <li><a href="#" class="menu3"><b>나만의 맛집</b></a></li>
            <li><a href="#" class="menu4"><b>맛 담 화</b></a>
                <ul class="drop1">
                    <li><a href="#"><b>맛집 공유</b></a></li>
                    <li><a href="#"><b>레시피</b></a></li>
                    <li><a href="#"><b>같이 먹을 사람</b></a></li>
                </ul>
            </li>
            <li><a href="#" class="menu5"><b>고객 센터</b></a>
                <ul class="dorp2">
                    <li><a href="#"><b>공지 사항</b></a></li>
                    <li><a href="#"><b>고객 문의</b></a></li>
                </ul>
            </li>
        </ul>

        <a href="#" class="nav_toogle">
            <i class="fas fa-bars"></i>
        </a>
    </nav>
    <script>
   $("#menu2").click(function() {
      if(${loginID == null}){
         alert("로그인을 해주세요");
         return false;
         location.href="/login.mem";
      } 
   })
   
   $("#menu3").click(function() {
      if(${loginID == null}){
         alert("로그인을 해주세요");
         return false;
         location.href="/login.mem";
      } 
   })
    </script>
    <!-- <div class="line"></div> -->
    <!-- <div class="line1"></div> -->
    <div class="recipes"><img src="img/recipes_background.png" width="100%">
        <div class="recipe_box1">
            <div class="recipes_img"><a href="#"><img src="img/recipe.png" style="max-width: 100%; height: auto;"></a></div>
        </div>
        <div class="recipe_box2">
            <div class="recipes_img1"><a href="#"><img src="img/recipe1.png" style="max-width: 100%; height: auto;"></a>
            </div>
        </div>
        <div class="recipe_box3">
            <div class="recipes_img2"><a href="#"><img src="img/recipe2.png" style="max-width: 100%; height: auto;"></a>
            </div>
        </div>
        <div class="recipe_box4">
            <div class="recipes_img3"><a href="#"><img src="img/recipe3.png" style="max-width: 100%; height: auto;"></a>
            </div>
        </div>
    </div>
    <div class="content">
        <!--전체를 감쌀 뼈대-->
        <div class="slides">
            <!--슬라이드 아이템을 감쌀 뼈대-->
            <div class="slide_item"><img src="img/img1.png" style="max-width: 100%;"></div>
            <!--슬라이드될 아이템들 -->
            <div class="slide_item"><img src="img/img2.png" style="max-width: 100%;"></div>
            <div class="slide_item"><img src="img/img3.png" style="max-width: 100%;"></div>
        </div>
        <div class="nextButton"><img src="img/nextButton.png" style="max-width: 100%;"></div>
        <!--다음 버튼 -->
        <div class="prevButton"><img src="img/prevButton.png" style="max-width: 100%;"></div>
        <!--이전 버튼 -->
        <div class="Thumbnail">
            <!--썸네일을 감쌀 뼈대 -->
            <div class="thumbnail_item"></div>
            <!--썸네일 아이템들 -->
            <div class="thumbnail_item"></div>
            <div class="thumbnail_item"></div>
        </div>
    </div>
    <script>
        function sliderOn() {
            const slides = document.querySelector('.slides'); // 슬라이드뼈대 감지
            const Content = document.querySelector('.Thumbnail'); //썸네일 뼈대 감지
            const item = slides.getElementsByClassName('slide_item'); // 슬라이드 아이템 획득
            const thumbnail = Content.getElementsByClassName('thumbnail_item'); //썸네일 아이템 획득

            const firstEle = item[0]; // 첫번째 슬라이드 아이템
            const firstThumb = thumbnail[0]; // 첫번째 썸네일 아이템
            firstEle.classList.add('ontheSlide'); //첫번째 슬라이드 아이템에 ontheSlide 클래스 추가
            firstThumb.classList.add('ontheThumbnail'); // 첫번째 썸네일 아이템에 ontheThumbnail 클래스 추가
            const gogogo = setInterval(sliderGo, 4000); // 4초마다 함수 sliderGo 함수 발동시키기
            function sliderGo() {

                const currentItem = document.querySelector('.ontheSlide'); // 현재 활성화된 슬라이드 아이템 감지
                const currentThumb = document.querySelector('.ontheThumbnail'); // 현재 활성화된 썸네일 아이템 감지
                currentItem.classList.remove('ontheSlide') //현재 활성화된 슬라이드 아이템 비활성화
                currentThumb.classList.remove('ontheThumbnail') // 현재 활성화된 썸네일 아이템 비활성화

                if (!currentItem.nextElementSibling) { // 만약 마지막 슬라이드 아이템이라면
                    item[0].classList.add('ontheSlide') //첫번째 아이템을 활성화
                    thumbnail[0].classList.add('ontheThumbnail') // 첫번째 썸네일을 활성화
                }
                else { // 그 외의 경우
                    currentItem.nextElementSibling.classList.add('ontheSlide') //다음 엘리먼트를 활성화
                    currentThumb.nextElementSibling.classList.add('ontheThumbnail') //다음 썸네일을 활성화
                }
            }
            const nextButton = document.querySelector('.nextButton');//다음버튼 감지
            const prevButton = document.querySelector('.prevButton');//이전버튼 감지
            nextButton.addEventListener('click', function () {//다음 버튼을 누른다면
                // clearInterval(gogogo)//자동이동을 중지하고
                sliderGo();//수동으로 다음이동
            })
            prevButton.addEventListener('click', function () {//이전 버튼을 누른다면
                // clearInterval(gogogo)//자동이동을 중지하고
                sliderGo('1');//수동으로 이전 이동
            })
        }
        sliderOn();
    </script>
    <!-- <div class="banner"></div> -->
    <div class="board_background">
        <!-- <div class="line2"></div> -->
        <ul class="board">
            <div class="board_header"><span class="sub">Best</span> 맛집 공유 게시글</div>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 1</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 2</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 3</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 4</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 5</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 6</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 7</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 8</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 9</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 10</a></li>
        </ul>
        <ul class="board1">
            <div class="board_header"><span class="sub">Best</span> 레시피</div>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 1</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 2</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 3</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 4</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 5</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 6</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 7</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 8</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 9</a></li>
            <li class="border"><span class="bullet">Hot </span><a href="#" class="bestwrite">Best 게시글 10</a></li>
        </ul>
        <ul class="board2">
            <div class="board_header1"><span class="sub1">new</span> 같이 먹을 사람</div>
            <li class="border"><span class="bullet1">New </span><a href="#" class="bestwrite">Best 게시글 1</a></li>
            <li class="border"><span class="bullet1">New </span><a href="#" class="bestwrite">Best 게시글 2</a></li>
            <li class="border"><span class="bullet1">New </span><a href="#" class="bestwrite">Best 게시글 3</a></li>
            <li class="border"><span class="bullet1">New </span><a href="#" class="bestwrite">Best 게시글 4</a></li>
            <li class="border"><span class="bullet1">New </span><a href="#" class="bestwrite">Best 게시글 5</a></li>
            <li class="border"><span class="bullet1">New </span><a href="#" class="bestwrite">Best 게시글 6</a></li>
            <li class="border"><span class="bullet1">New </span><a href="#" class="bestwrite">Best 게시글 7</a></li>
            <li class="border"><span class="bullet1">New </span><a href="#" class="bestwrite">Best 게시글 8</a></li>
            <li class="border"><span class="bullet1">New </span><a href="#" class="bestwrite">Best 게시글 9</a></li>
            <li class="border"><span class="bullet1">New </span><a href="#" class="bestwrite">Best 게시글 10</a></li>
        </ul>
    </div>

    <!-- <ul class="board2">
            <div class="board_header1"><span class="sub1">News</span> 공지 사항</div>
            <li class="border"><span class="bullet1">News </span><a href="#" class="bestwrite">Best 게시글 1</a></li>
            <li class="border"><span class="bullet1">News </span><a href="#" class="bestwrite">Best 게시글 2</a></li>
            <li class="border"><span class="bullet1">News </span><a href="#" class="bestwrite">Best 게시글 3</a></li>
            <li class="border"><span class="bullet1">News </span><a href="#" class="bestwrite">Best 게시글 4</a></li>
            <li class="border"><span class="bullet1">News </span><a href="#" class="bestwrite">Best 게시글 5</a></li>
            <li class="border"><span class="bullet1">News </span><a href="#" class="bestwrite">Best 게시글 6</a></li>
            <li class="border"><span class="bullet1">News </span><a href="#" class="bestwrite">Best 게시글 7</a></li>
            <li class="border"><span class="bullet1">News </span><a href="#" class="bestwrite">Best 게시글 8</a></li>
            <li class="border"><span class="bullet1">News </span><a href="#" class="bestwrite">Best 게시글 9</a></li>
        <li class="border"><span class="bullet1">News </span><a href="#" class="bestwrite">Best 게시글 10</a></li>
    </ul> -->


    <div class="list_con"> <img src="img/background.png" width="100%">
            <div id="list_box1" class="list_boxes">
            <a href="https://allaprima.co.kr/" target='_blank'><img alt="" src="img/list_img1.png" class="list_img" style="max-width: 100%; height: auto;">알라프리마</a>
            </div>
            <div id="list_box2" class="list_boxes">
            <a href="https://www.instagram.com/sinsa_mirai/" target='_blank'><img alt="" src="img/list_img2.png" class="list_img" style="max-width: 100%; height: auto;">미라이</a>
            </div>
            <div id="list_box3" class="list_boxes">
            <a href="https://www.instagram.com/tapadito_seoul/" target='_blank'><img alt="" src="img/list_img3.png" class="list_img" style="max-width: 100%; height: auto;">따빠띠또</a>
            </div>
            <div id="list_box4" class="list_boxes">
            <a href="https://www.mangoplate.com/restaurants/LGXola8pgdAC" target='_blank'><img alt="" src="img/list_img4.png" class="ist_img" style="max-width: 100%; height: auto;">우미노미</a>
            </div>
            <div id="list_box5" class="list_boxes">
            <a href="https://www.instagram.com/chefkuna/" target='_blank'><img alt="" src="img/list_img5.png" class="list_img" style="max-width: 100%; height: auto;">쿠나</a>
            </div>
            <div id="list_box6" class="list_boxes">
            <a href="https://www.instagram.com/il_riso/" target='_blank'><img alt="" src="img/list_img6.png" class="list_img" style="max-width: 100%; height: auto;">일리조</a>
            </div>
            <div id="list_box7" class="list_boxes">
            <a href="https://www.mangoplate.com/restaurants/yy0VPDhV7mw7" target='_blank'><img alt="" src="img/list_img7.png" class="list_img" style="max-width: 100%; height: auto;">레브어</a>
            </div>
            <div id="list_box8" class="list_boxes">
            <a href="https://www.mangoplate.com/restaurants/2qFEx0pXrDdZ" target='_blank'><img alt="" src="img/list_img8.png" class="list_img" style="max-width: 100%; height: auto;">우오보파스타바</a>
            </div>
            <div id="list_box9" class="list_boxes">
            <a href="https://www.mangoplate.com/restaurants/jiQPoOdNDqQc" target='_blank'><img alt="" src="img/list_img9.png" class="list_img" style="max-width: 100%; height: auto;">와라야키 쿠이신보</a>
            </div>
            <div id="list_box10" class="list_boxes">
            <a href="https://www.mangoplate.com/restaurants/4hAcTcdILi" target='_blank'><img alt="" src="img/list_img10.png" class="list_img" style="max-width: 100%; height: auto;">맛짱조개</a>
    </div>
    <div class="footer"><img src="img/footer.png" style="max-width: 100%; height: auto;"></div>
</body>
</html>