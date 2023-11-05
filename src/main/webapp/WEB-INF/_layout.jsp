<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pageBody = (String) request.getAttribute("page-body");
    String context = request.getContextPath();
    String contextCulture = context + "/" + request.getAttribute("culture") + "/";
    long time = new Date().getTime();
%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link rel="stylesheet" href="<%=context%>/css/site.css?<%=time%>">
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
            <li <%= pageBody.equals("about.jsp") ? "class='active'" : "" %>>
                <a href="<%=contextCulture%>about">JSP</a>
            </li>
            <li <%= pageBody.equals("filters.jsp") ? "class='active'" : "" %>>
                <a href="<%=contextCulture%>filters">Filters</a>
            </li>
             <li <%= pageBody.equals("ioc.jsp") ? "class='active'" : "" %>
                ><a href="<%=contextCulture%>ioc">Ioc</a>
             </li>
             <li <%= pageBody.equals("db.jsp") ? "class='active'" : "" %>
             ><a href="<%=contextCulture%>db">Database</a>
             </li>
             <li <%= pageBody.equals("spa.jsp") ? "class='active'" : "" %>
             ><a href="<%=contextCulture%>spa">SPA</a>
             </li>
             <li <%= pageBody.equals("ws.jsp") ? "class='active'" : "" %>
             ><a href="<%=contextCulture%>ws">Websocket</a>
             </li>
         </ul>
         </div>
    </nav>
    <div class="container">
        <jsp:include page="<%=pageBody%>"/>
    </div>

    <footer class="page-footer deep-purple">
        <div class="container">
            <div class="row">
                <div class="col l6 s12">
                    <h5 class="white-text">Hello from footer</h5>
                    <p class="grey-text text-lighten-4">Hello from Java Web Application JSP</p>
                </div>
                <div class="col l4 offset-l2 s12">
                    <h5 class="white-text">Links</h5>
                </div>
            </div>
        </div>
        <div class="footer-copyright">
            <div class="container">
                © 2023 Copyright Text
                <a class="grey-text text-lighten-4 right" href="#!">More Links</a>
            </div>
        </div>
    </footer>
    <!-- Modal Structure -->
    <div id="auth-modal" class="modal">
        <div class="modal-content">
            <h4>Authentication</h4>
            <div class="row">
                <div class="input-field col s6">
                    <i class="material-icons prefix">face</i>
                    <input id="auth-login" type="text" class="">
                    <label for="auth-login">Login</label>
                </div>
                <div class="input-field col s6">
                    <i class="material-icons prefix">password</i>
                    <input id="auth-password" type="password" class="">
                    <label for="auth-password">Password</label>
                </div>
            </div>

        </div>
        <div class="modal-footer">
            <b id="auth-message-container"></b>
            <a href="<%=context%>/signup" class="modal-close deep-purple lighten-2 btn-flat ">Registration</a>
            <button id="auth-modal-sign-in-button" class=" waves-effect deep-purple lighten-3 btn-flat">Login</button>
        </div>
    </div>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <script src="<%=context%>/js/site.js"></script>
    <script src="<%=context%>/js/auth.js?<%=time%>"></script>

    </body>
</html>

<script>
    window.addEventListener('load', checkAuth);
    function checkAuth(){
        const token = JSON.parse(atob(window.localStorage.getItem('token')));
        const exp = new Date(token.exp);
        if(exp < new Date()){
            window.localStorage.removeItem('token');
            window.location.reload();
            M.toast({html: 'Your token is expired'});
        }
        M.toast({html: 'Your token is valid'});
    }

</script>
