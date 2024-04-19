
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String pageBody = (String) request.getAttribute("page-body");
    if (pageBody == null) {
        pageBody = "home";
    }
    String bodyFile = "/WEB-INF/" + pageBody + ".jsp";

    String context = request.getContextPath();
%>
<html>
    <!--Import Google Icon Font-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons" >

    <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <link rel="stylesheet" href="<%=context%>/css/site.css">
    <link rel="icon" href="<%=context%>/img/java.png">
<head>
    <title>My Java Site</title>
</head>
<body>
    <header>
        <nav class=" amber lighten-1">
            <div class="nav-wrapper">
                <a href="<%=context%>/" class="brand-logo">My First Java web</a>
                <ul id="nav-mobile" class="right hide-on-med-and-down">
                    <li><a href="<%= context %>/cart"><i class="material-icons">shopping_cart</i></a></li>
                    <li><a href="<%= context %>/shop"><i class="material-icons">shop</i></a></li>
                    <li><a href="<%= context %>/product"><i class="material-icons">add</i></a></li>
                    <li><a href="<%= context %>/action"><i class="material-icons">local_atm</i></a></li>
                    <li data-auth="avatar"><a href="#auth-modal" class="modal-trigger"><i class="material-icons">key</i></a></li>
                </ul>
            </div>
        </nav>
    </header>
    <main class="<%= request.getAttribute("skip-container") == null ? "container" : ""%>%>">
        <jsp:include page="<%= bodyFile %>" />
    </main>

    <footer class="page-footer amber lighten-1">
        <div class="container">
            <div class="row">
                <div class="col l6 s12">
                    <h5 class="white-text">Footer Content</h5>
                    <p class="grey-text text-lighten-4">You can use rows and columns here to organize your footer content.</p>
                </div>
                <div class="col l4 offset-l2 s12">
                    <h5 class="white-text">Links</h5>
                    <ul>
                        <li><a class="grey-text text-lighten-3" href="#!">Link 1</a></li>
                        <li><a class="grey-text text-lighten-3" href="#!">Link 2</a></li>
                        <li><a class="grey-text text-lighten-3" href="#!">Link 3</a></li>
                        <li><a class="grey-text text-lighten-3" href="#!">Link 4</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="footer-copyright">
            <div class="container">
                © 2024 Copyright Text
                <a class="grey-text text-lighten-4 right" href="#!">More Links</a>
            </div>
        </div>
    </footer>



    <!-- Modal Structure -->
    <div id="auth-modal" class="modal">
        <form class="modal-content" id="auth-modal-form">
            <h5>Authorization</h5>
            <div class="row">
                <div class="col s12">
                    <div class="row">
                        <div class="input-field col s12">
                            <i class="material-icons prefix amber-text text-lighten-1">email</i>
                            <input id="sign-in-email" name="sign-in-email" type="email" class="validate">
                            <label for="sign-in-email">Email</label>
                            <span class="helper-text" data-error="wrong" data-success="right">Ваш email</span>
                        </div>
                        <div class="input-field col s12">
                            <i class="material-icons prefix amber-text text-lighten-1">password</i>
                            <input id="sign-in-password" name="sign-in-password" type="password" class="validate">
                            <label for="sign-in-password">Password</label>
                            <span class="helper-text" data-error="wrong" data-success="right">Пароль</span>
                        </div>
                    </div>
                </div>
            </div>
            <span id="modal-auth-message"></span>
            <div class="row center-align">
                <div class="col s12">
                    <!--<a href="#!" style="width:100%;" class="modal-close waves-effect blue btn">Sign in</a> -->
                    <button style="width:100%;" class="waves-effect amber lighten-1 btn" type="button" id="auth-button">Sign in</button>
                </div>
            </div>
            <div class="row">
                <div class="col s12">
                    <a href="<%=context%>/signup" style="width:100%;" class="modal-close waves-effect waves-green btn">Sign Up</a>
                </div>
            </div>
        </form>
    </div>

    <!-- Compiled and minified JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
    <script src="<%=context%>/js/site.js"></script>
</body>

</html>
