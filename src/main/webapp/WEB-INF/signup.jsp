<%@ page import="step.learning.dto.models.SignupFormModel" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String regData = request.getAttribute("reg-data") != null ? (String) request.getAttribute("reg-data") : "";
    SignupFormModel formModel = (SignupFormModel) request.getAttribute("reg-model");
    Map<String, String> validationErrors = request.getAttribute("validationErrors") == null ?
            new HashMap<String, String>() : (Map<String, String>) request.getAttribute("validationErrors");
    String loginClass = ( regData.isEmpty() ? "validate" : ( validationErrors.containsKey("login") ? "invalid" : "valid" ) );
    String nameClass = ( regData.isEmpty() ? "validate" : ( validationErrors.containsKey("name") ? "invalid" : "valid" ) );
%>
<h2>User registration</h2>
<p>
    <%=request.getAttribute("culture")%>
</p>
<p>
<%--    <%if(validationErrors.containsKey("name")){   %>--%>
<%--    <span class="error"><strong><%=validationErrors.get("name")%></strong></span>--%>
<%--    <% } %>--%>
<%--    <br>--%>
<%--    <%if(validationErrors.containsKey("password")){   %>--%>
<%--    <span class="error"><strong><%=validationErrors.get("password")%></strong></span>--%>
<%--    <% } %>--%>
<%--    <br>--%>
<%--    <%if(validationErrors.containsKey("repeat")){   %>--%>
<%--    <span class="error"><strong><%=validationErrors.get("repeat")%></strong></span>--%>
<%--    <% } %>--%>
<%--    <br>--%>
<%--    <%if(validationErrors.containsKey("email")){   %>--%>
<%--    <span class="error"><strong><%=validationErrors.get("email")%></strong></span>--%>
<%--    <% } %>--%>
<%--    <br>--%>
<%--    <%if(validationErrors.containsKey("agree")){   %>--%>
<%--    <span class="error"><strong><%=validationErrors.get("agree")%></strong></span>--%>
<%--    <% } %>--%>
<%--    <br>--%>
<%--    <%if(validationErrors.containsKey("birthdate")){   %>--%>
<%--    <span class="error"><strong><%=validationErrors.get("birthdate")%></strong></span>--%>
<%--    <% } %>--%>
</p>
<div class="row">
    <form class="col s12" method="POST" enctype="multipart/form-data">
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">face</i>
                <input id="reg-login"
                       name="reg-login"
                       type="text"
                       class="<%=loginClass%>"
                       value="<%=formModel == null ? "" : formModel.getLogin()%>">
                <label for="reg-login">Login</label>
                <% if(validationErrors.containsKey("login")) {%>
                    <span class="helper-text" data-error="<%=validationErrors.get("login")%>"></span>
                <%} %>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">badge</i>
                <input id="reg-name" name="reg-name" type="text" class="validate" value="Experienced user">
                <label for="reg-name">Real name</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">alternate_email</i>
                <input id="reg-email" name="reg-email" type="email" class="validate" value="user@email.co">
                <label for="reg-email">Email</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">calendar_month</i>
                <input id="reg-birthday" name="reg-birthday" type="date" class="validate" value="2000-10-20">
                <label for="reg-birthday">Birth day</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s6">
                <i class="material-icons prefix">password</i>
                <input id="reg-password" name="reg-password" type="password" class="validate" value="123">
                <label for="reg-password">Password</label>
            </div>
            <div class="input-field col s6">
                <i class="material-icons prefix">password</i>
                <input id="reg-repeat" name="reg-repeat" type="password" class="validate" value="123">
                <label for="reg-repeat">Repeat password</label>
            </div>
        </div>


        <div class="row">
            <div class="input-field col s6">
                <label>
                    <input type="checkbox" name="reg-agree" class="validate deep-purple">
                    <span>I agree with privacy policy</span>
                </label>
            </div>
            <div class="col s6">
                <div class="file-field input-field">
                    <div class="btn deep-purple lighter-2">
                        <span><i class="material-icons">photo_library</i></span>
                        <input type="file" name="reg-avatar">
                    </div>
                    <div class="file-path-wrapper">
                        <input class="file-path validate" type="text" placeholder="Photo avatar">
                    </div>
                </div>
            </div>
        </div>
        <div class="input-field row align-right">
            <button class="waves-effect btn-large deep-purple lighter-2">
                <i class="material-icons right">input</i>
                button
            </button>
        </div>
    </form>
</div>
