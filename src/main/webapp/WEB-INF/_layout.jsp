<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pageBody = (String) request.getAttribute("page-body");
    String context = request.getContextPath();
    String contextCulture = context + "/" + request.getAttribute("culture") + "/";
%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link rel="stylesheet" href="<%=context%>/css/site.css">
        <title>Java web</title>
    </head>
    <body>
    <nav>
         <div class="nav-wrapper deep-purple accent-2">

         <a class="auth-trigger modal-trigger right" href="#auth-modal">
             <i class="material-icons">login</i>
         </a>

             <a href="<%=context%>" class="site-logo right">Java Web</a>
         <ul id="nav-mobile">
            <li><a href="#">JSP</a></li>
            <li <%= pageBody.equals("filters.jsp") ? "class='active'" : "" %>
            ><a href="<%=contextCulture%>/filters">Filters</a></li>
             <li <%= pageBody.equals("ioc.jsp") ? "class='active'" : "" %>
             ><a href="<%=contextCulture%>/ioc">Ioc</a></li>
         </ul>
         </div>
    </nav>
    <div class="container">
        <jsp:include page="<%=pageBody%>"/>
    </div>


<footer>

</footer>
    <!-- Modal Structure -->
    <div id="auth-modal" class="modal">
        <div class="modal-content">
            <h4>Authentication</h4>
            <p>A bunch of text</p>
        </div>
        <div class="modal-footer">
            <a href="<%=context%>/signup" class="modal-close deep-purple lighten-2 btn-flat ">Registration</a>
            <a href="<%=context%>/singin" class=" waves-effect deep-purple lighten-3 btn-flat">Login</a>
        </div>
    </div>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <script src="<%=context%>/js/site.js"></script>

    </body>
</html>
