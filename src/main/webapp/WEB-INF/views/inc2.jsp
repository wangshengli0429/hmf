<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
    
<script type="text/javascript" src="<%=path%>/console/js/jquery/jquery-1.8.3.min.js"></script>

<!-- 引入项目css js文件 -->
<link  href="<%=request.getContextPath()%>/console/css/base.css" rel="stylesheet" >
<link  href="<%=request.getContextPath()%>/console/css/index.css" rel="stylesheet" >
<script type="text/javascript" src="<%=request.getContextPath()%>/console/js/zankai.js"></script>
<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/console/js/validation.js"></script> --%>

<!-- 引入bootstrap的css js -->
<link  href="<%=request.getContextPath()%>/jslib/bootstrap/css/bootstrap.min.css" rel="stylesheet" >
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/bootstrap/js/bootstrap.min.js"></script>

<!-- 引入bootstrap-addTabs的css js-->
<link  href="<%=request.getContextPath()%>/jslib/addTabs/addTabs.css" rel="stylesheet" >
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/addTabs/addTabs.js"></script>

<!-- 引入bootstrap-table的css js-->
<link  href="<%=request.getContextPath()%>/jslib/table/bootstrap-table.min.css" rel="stylesheet" >
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/table/bootstrap-table.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/table/locale/bootstrap-table-zh-CN.min.js"></script>

<!-- 引入my97 -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/my97/WdatePicker.js" charset="utf-8"></script>

<!-- 引入 artDialog-->
<!-- artDialog弹出层信息 -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/jslib/artDialog-master/css/ui-dialog.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/artDialog-master/dist/dialog-min.js" charset="utf-8"></script>

<!-- 引入bootbox.js -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/bootbox/bootbox.js"></script>

<!-- 引入Highcharts -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/Highcharts-5.0.12/code/highcharts.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/Highcharts-5.0.12/code/modules/exporting.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/Highcharts-5.0.12/syExtHighcharts.js" charset="utf-8"></script>

<!-- 引入百度ueditor富文本编辑器 -->
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/jslib/ueditor1_4_3_3-utf8-jsp/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/jslib/ueditor1_4_3_3-utf8-jsp/ueditor.all.min.js"> </script>
<!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
<!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
<script type="text/javascript" charset="utf-8" src="<%=request.getContextPath()%>/jslib/ueditor1_4_3_3-utf8-jsp/lang/zh-cn/zh-cn2.js"></script>

<!-- validate表单验证 -->
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/jquery-validation-1.14.0/dist/jquery.validate.min.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/jslib/jquery-validation-1.14.0/dist/localization/messages_zh.min.js" charset="utf-8"></script>





