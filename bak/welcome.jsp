<style>
    #slides{
      display: none;
      margin-bottom:50px;
    }
	
    .slidesjs-navigation {
      margin-top:3px;
    }

    .slidesjs-previous {
      margin-right: 5px;
      float: left;
    }

    .slidesjs-next {
      margin-right: 5px;
      float: left;
    }

    .slidesjs-pagination {
      margin: 6px 0 0;
      float: right;
      list-style: none;
    }

    .slidesjs-pagination li {
      float: left;
      margin: 0 1px;
    }

    .slidesjs-pagination li a {
      display: block;
      width: 13px;
      height: 0;
      padding-top: 13px;
      background-image: url(${pageContext.request.contextPath }/application/welcome/img/pagination.png);
      background-position: 0 0;
      float: left;
      overflow: hidden;
    }

    .slidesjs-pagination li a.active,
    .slidesjs-pagination li a:hover.active {
      background-position: 0 -13px
    }

    .slidesjs-pagination li a:hover {
      background-position: 0 -26px
    }

    a:link,
    a:visited {
      color: #333
    }

    a:hover,
    a:active {
      color: #9e2020
    }

    .navbar {
      overflow: hidden
    }
  </style>

<div id="slides">
      <img src="${pageContext.request.contextPath }/images/welcome-1.jpg">
      <img src="${pageContext.request.contextPath }/images/welcome-2.jpg">
      <img src="${pageContext.request.contextPath }/images/welcome-3.jpg">
      <img src="${pageContext.request.contextPath }/images/welcome-4.jpg">
</div>
  <script src="${pageContext.request.contextPath }/application/welcome/js/jquery.slides.min.js"></script>
  <script>
    $(function() {
      $('#slides').slidesjs({
        width: 940,
        height: 528,
        navigation: false
      });
    });
  </script>