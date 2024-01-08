<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pageBody = (String) request.getAttribute("page-body");
    String context = request.getContextPath();
    String contextCulture = context + "/";
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
         <div class="nav-wrapper cyan darken-3">

         <a class="auth-trigger modal-trigger right" href="#auth-modal">
             <i class="material-icons">login</i>
         </a>
         <a class="profile-trigger right" style="margin-right: 20px;" href="<%=contextCulture%>product">
             <i class="material-icons" style="font-size: 2vw;">account_circle</i>
         </a>



         <a href="<%=context%>" class="site-logo right">Market</a>
         <ul id="nav-mobile">
             <li class="profile-li" <%= pageBody.equals("cart.jsp") ? "class='active'" : "" %>>
                 <a href="<%=contextCulture%>cart">Your cart</a>
             </li>
             <li <%= pageBody.equals("signup.jsp") ? "class='active'" : "" %>>
                 <a href="<%=contextCulture%>signup">Products</a>
                </li>
         </ul>
         </div>
    </nav>
    <div class="container">
        <jsp:include page="<%=pageBody%>"/>
    </div>

    <footer class="page-footer cyan darken-3">
        <div class="container">
            <div class="row">
                <div class="col l6 s12">
                    <h5 class="white-text">Market place company ltd. </h5>
                    <p class="grey-text text-lighten-4">
                        All rights reserved. 2023. Market place company ltd.
                        To contact us please use the following email: <a href="mailto:klementievdaniil@gmail.com">noreply@marketify.com</a>
                    </p>
                </div>
                <div class="col l4 offset-l2 s12">
                    <h5 class="white-text">Social networks</h5>
                    <ul>
                        <li><a class="grey-text text-lighten-3" href="https://www.facebook.com/">Facebook</a></li>
                        <li><a class="grey-text text-lighten-3" href="https://www.instagram.com/">Instagram</a></li>
                        <li><a class="grey-text text-lighten-3" href="https://twitter.com/">Twitter</a></li>
                        <li><a class="grey-text text-lighten-3" href="https://www.youtube.com/">Youtube</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="footer-copyright">
            <div class="container">
                © 2023 Market place company ltd. All rights reserved
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
    const isAuth = window.localStorage.getItem('token');
    if(isAuth){
        document.querySelector('.auth-trigger').style.display = 'none';
        document.querySelector('.profile-trigger').style.display = 'block';
    }else{
        document.querySelector('.auth-trigger').style.display = 'block';
        document.querySelector('.profile-trigger').style.display = 'none';
    }
</script>
