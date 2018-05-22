<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>
    <title>后台主页</title>
   <%@include file="../header.jsp" %>
	<script type="text/javascript" src='${pageContext.request.contextPath }/jquery-easyui-1.4.3/outlook2.js'> </script>
    <script type="text/javascript">
	 $(function(){
		 getUserMenusData();
	});
	 
	 function getUserMenusData(){
		$.ajax({
			type: "post",
			url:contextPath+"menu/usermenus.do",
			contentType:"application/x-www-form-urlencoded;charset=UTF-8",
			dataType:"json",
			success: function(resp, textStatus){
				if(resp.code==0){
					var data = resp.data;
					initMenus(data);
					initTab();
				}
			}
		});
	 }
	 function initMenus(data){
		_menus = {"menus":data}
		InitLeftMenu();
		tabClose();
		tabCloseEven();
		openPwd();
		
	 }
	 
	 function initTab(){
		 $('#tabs').tabs({
	        onSelect: function (title) {
	            var currTab = $('#tabs').tabs('getTab', title);
	            var iframe = $(currTab.panel('options').content);

				var src = iframe.attr('src');
				if(src){
					$('#tabs').tabs('update', { 
						tab: currTab, 
						options: { content: createFrame(src)} 
					});
				}
	        }
	    });
	 }
    </script>
<style>
#west .panel-body{
	font-size: 24px;
} 
#nav ul{
	list-style-type:none;
}
#nav ul li{
	margin:20px 0 ;
	/* border: solid 1px; */
	font-weight: bold;
}
</style>
</head>
<body class="easyui-layout" style="overflow-y: hidden"  scroll="no">

    <div region="north" split="true" border="false" style="overflow: hidden; height: 30px;
        background:#7f99be repeat-x center 50%;
        line-height: 20px;color: #fff; font-family: Verdana, 微软雅黑,黑体">
        <span style="float:right; padding-right:20px;" class="head">欢迎 ${sessionScope.userInfo.username } 
        <a href="${pageContext.request.contextPath }/auth/logout.do" id="loginOut">安全退出</a></span>
        <span style="padding-left:10px; font-size: 16px; ">
        米赢天下 会员 后台管理系统
        </span>
    </div>
    <div region="west" hide="true" split="true" title="导航菜单" style="width:180px;" id="west">
		<div id="nav" class="easyui-accordion" fit="true" border="false">
				<!--  导航内容 -->
		</div>
    </div>
    <div id="mainPanle" region="center" style="background: #eee; overflow-y:hidden">
        <div id="tabs" class="easyui-tabs"  fit="true" border="false" >
			<div title="欢迎使用" style="padding:20px;overflow:hidden; color:red;">
				<div class="banner" style="width:85%;height:100%;text-align: center;">
					<%@include file="welcome.jsp" %>
				</div>
			</div>
		</div>
    </div>

	<div id="mm" class="easyui-menu" style="width:150px;">
		<div id="mm-tabupdate">刷新</div>
		<div class="menu-sep"></div>
		<div id="mm-tabclose">关闭</div>
		<div id="mm-tabcloseall">全部关闭</div>
		<div id="mm-tabcloseother">除此之外全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-tabcloseright">当前页右侧全部关闭</div>
		<div id="mm-tabcloseleft">当前页左侧全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-exit">退出</div>
	</div>


	<!--二级登录窗口-->
    <div id="secondLoginWin" class="easyui-window" title="二级登录" collapsible="false" minimizable="false"
        maximizable="false" icon="icon-save"  style="width: 300px; height: 200px; padding: 2px;
        background: #fafafa;">
        <div class="easyui-layout" fit="true">
            <div region="center" border="false" style="padding: 10px; background: #fff; border: 1px solid #ccc;">
                <table cellpadding=3>
                    <tr>
                        <td width="80px;">用户名：</td>
                        <td>${sessionScope.userInfo.username }</td>
                    </tr>
                    <tr>
                        <td>二级密码：</td>
                        <td><input id="towPassword" type="Password" class="txt01" /></td>
                    </tr>
                </table>
            </div>
            <div region="south" border="false" style="text-align: right; height: 30px; line-height: 30px;">
                <a id="btnEp" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="login2();">登录</a> 
            </div>
        </div>
    </div>
<%@include file="qq/qq.jsp" %>
</body>
</html>