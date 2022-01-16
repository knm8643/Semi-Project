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
        * {
            box-sizing: border-box
        }

        .board_background {
            max-width: 1530px;
            position: relative;
            margin: auto;
            width: 100%;
            height: 14%;
            top: 20px;
        }
        .board {
            position: relative;
            text-align: left;
            margin-right: 10%;
            width: 30%;
            font-size: 25px;
            background-color: white;
        }

        .board li {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            top: 50px;
        }

        .board1 {
            position: absolute;
            text-align: left;
            left: 35%;
            top: 0;
            width: 30%;
            font-size: 25px;
            background-color: white;
        }

        .board1 li {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .board2 {
            position: absolute;
            text-align: left;
            float: left;
            margin-right: 10%;
            left: 70%;
            top: 0;
            width: 30%;
            font-size: 25px;
            background-color: white;
        }

        .board2 li {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
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
            position:relative;
            border-top: 4px double red;
            border-bottom: 4px double red;
            border-left:1px solid #e9e9e9;
            border-right:1px solid #e9e9e9;
            font-size: 23px;
            width: 100%;
            text-align: center;
            background-color: white;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .board_header1 {
            position: relative;
            border-top: 4px double rgb(8, 207, 8);
            border-bottom: 4px double rgb(8, 207, 8);
            border-left:1px solid #e9e9e9;
            border-right:1px solid #e9e9e9;
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
        .recipes {
            position: relative;
        }

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
            max-width: 1600px;
            text-align: center;
            /* white-space: nowrap; */
        }
        
        #list_box1{
            position: absolute;
            max-width: 250px;
            max-height: 250px;
            width: 14.5%;
            height: 14.5%;
            top: 26%;
            left: 2%;
        }
        #list_box2{
            position: absolute;
            max-width: 250px;
            max-height: 250px;
            width: 14.5%;
            height: 14.5%;
            top: 26%;
            left: 23%;
        }
        #list_box3{
            position: absolute;
            max-width: 250px;
            max-height: 250px;
            width: 14.5%;
            height: 14.5%;
            top: 26%;
            left: 43%;
        }
        #list_box4{
            position: absolute;
            max-width: 250px;
            max-height: 250px;
            width: 14.5%;
            height: 14.5%;
            top: 26%;
            left: 63%;
        }
        #list_box5{
            position: absolute;
            max-width: 250px;
            max-height: 250px;
            width: 14.5%;
            height: 14.5%;
            top: 26%;
            right: 2%;
        }
        #list_box6{
            position: absolute;
            max-width: 250px;
            max-height: 250px;
            width: 14.5%;
            height: 14.5%;
            top: 65%;
            left: 2%;
        }
        #list_box7{
            position: absolute;
            max-width: 250px;
            max-height: 250px;
            width: 14.5%;
            height: 14.5%;
            top: 65%;
            left: 23%;
        }
        #list_box8{
            position: absolute;
            max-width: 250px;
            max-height: 250px;
            width: 14.5%;
            height: 14.5%;
            top: 65%;
            left: 43%;
        }
        #list_box9{
            position: absolute;
            max-width: 250px;
            max-height: 250px;
            width: 14.5%;
            height: 14.5%;
            top: 65%;
            left: 63%;
        }
        #list_box10{
            position: absolute;
            max-width: 250px;
            max-height: 250px;
            width: 14.5%;
            height: 14.5%;
            top: 65%;
            right: 2%;
        } 
        @media (max-width:360px){.list_con{font-size:10px;}}

        @media (min-width:361px) and (max-width:399px){.list_con{font-size:11px;}}

        @media (min-width:400px) and (max-width:439px){.list_con{font-size:12px;}}

        @media (min-width:440px) and (max-width:479px){.list_con{font-size:13px;}}

        @media (min-width:480px) and (max-width:519px){.list_con{font-size:14px;}}

        @media (min-width:520px) and (max-width:559px){.list_con{font-size:15px;}}

        @media (min-width:560px) and (max-width:599px){.list_con{font-size:16px;}}

        @media (min-width:600px) and (max-width:639px){.list_con{font-size:17px;}}

        @media (min-width:640px) and (max-width:679px){.list_con{font-size:18px;}}

        @media (min-width:680px) and (max-width:719px){.list_con{font-size:19px;}}

        @media (min-width:720px) and (max-width:759px){.list_con{font-size:20px;}}

        @media (min-width:760px) and (max-width:799px){.list_con{font-size:21px;}}

        @media (min-width:800px) and (max-width:839px){.list_con{font-size:22px;}}

        @media (min-width:840px) and (max-width:879px){.list_con{font-size:23px;}}

        @media (min-width:880px){.list_con{font-size:24px;}}
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

        .footer{
            position: relative;
            text-align: center;
            margin: auto;
            top: 40px;
            max-hight:333px;
        }
    </style>
</head>
<body>

    <style type="text/css">
        a {
            text-decoration: none
        }
    </style>
    <jsp:include page="Navi.jsp"></jsp:include>
    <!-- <div class="line"></div> -->
    <!-- <div class="line1"></div> -->
    
    
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
    
    <div class="recipes"><img src="img/recipes_background.png" width="100%">
        <div class="recipe_box1">
            <div class="recipes_img"><a href="#"><img src="img/recipe.png" style="max-width: 100%; height: auto;"></a>
            </div>
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
    
    <div class="board_background">
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
        
        <div class="list_con"><img src="img/list_back.png" style="max-width: 100%; height: auto;">
            <div id="list_box1" class="list_boxes">
                <a href="https://allaprima.co.kr/" target='_blank'><img src="img/list_img1.png" style="max-width: 100%; height: auto;" >알라프리마</a>
                
            </div>
            <div id="list_box2" class="list_boxes">
                <a href="https://www.instagram.com/sinsa_mirai/" target='_blank'><img src="img/list_img2.png" style="max-width: 100%; height: auto;">미라이</a>
            </div>
            <div id="list_box3" class="list_boxes">
                <a href="https://www.instagram.com/tapadito_seoul/" target='_blank'><img src="img/list_img3.png" style="max-width: 100%; height: auto;">따빠띠또</a>
            </div>
            <div id="list_box4" class="list_boxes">
                <a href="https://www.mangoplate.com/restaurants/LGXola8pgdAC" target='_blank'><img src="img/list_img4.png" style="max-width: 100%; height: auto;">우미노미</a>
            </div>
            <div id="list_box5" class="list_boxes">
                <a href="https://www.instagram.com/chefkuna/" target='_blank'><img src="img/list_img5.png" style="max-width: 100%; height: auto;">쿠나</a>
            </div>
            <div id="list_box6" class="list_boxes">
                <a href="https://www.instagram.com/il_riso/" target='_blank'><img src="img/list_img6.png"style="max-width: 100%; height: auto;">일리조</a>
            </div>
            <div id="list_box7" class="list_boxes">
                <a href="https://www.mangoplate.com/restaurants/yy0VPDhV7mw7" target='_blank'><img src="img/list_img7.png" style="max-width: 100%; height: auto;">레브어</a>
            </div>
            <div id="list_box8" class="list_boxes">
                <a href="https://www.mangoplate.com/restaurants/2qFEx0pXrDdZ" target='_blank'><img src="img/list_img8.png" style="max-width: 100%; height: auto;">우오보</a>
            </div>
            <div id="list_box9" class="list_boxes">
                <a href="https://www.mangoplate.com/restaurants/jiQPoOdNDqQc" target='_blank'><img src="img/list_img9.png" style="max-width: 100%; height: auto;">와라야키</a>
            </div>
            <div id="list_box10" class="list_boxes">
                <a href="https://www.mangoplate.com/restaurants/4hAcTcdILi" target='_blank'><img src="img/list_img10.png" style="max-width: 100%; height: auto;">맛짱조개</a>
            </div>
        </div>
    </div>
    <footer class="footer"><img src="img/footer.png" style="max-width: 100%; height: auto"></footer>
</body>
</html>