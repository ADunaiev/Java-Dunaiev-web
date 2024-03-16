<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>Реєстрація користувача</h1>

<form method="post" id="sign-up-form">
    <div class="row">
        <div class="col s12">
            <div class="row">

                <div class="input-field col s12 m6">
                    <input id="user-email" name="user-email" type="email" class="validate">
                    <label for="user-email">Email</label>
                    <span class="helper-text" data-error="wrong" data-success="right">Email</span>
                </div>

                <div class="input-field col s12 m6">
                    <input id="user-name" name="user-name" type="text" class="validate">
                    <label for="user-name">Ім'я</label>
                    <span class="helper-text" data-error="wrong" data-success="right">Використовувайте лише літери</span>
                </div>

                <div class="input-field col s12 m6">
                    <input id="user-password" name="user-password" type="password" class="validate">
                    <label for="user-password">Пароль</label>
                    <span class="helper-text" data-error="wrong" data-success="right">Пароль</span>
                </div>

                <div class="input-field col s12 m6">
                    <input id="user-repeat-password" name="user-repeat" type="password" class="validate">
                    <label for="user-repeat-password">Повторить пароль</label>
                    <span class="helper-text" data-error="wrong" data-success="right">Повторить пароль</span>
                </div>

                <div class="file-field input-field col s12 m6">
                    <div class="btn amber lighten-1">
                        <span>Аватар</span>
                        <input id="avatar-file" name="user-avatar" type="file">
                    </div>
                    <div class="file-path-wrapper">
                        <input  id="avatar-file-path" class="file-path validate" type="text">
                    </div>
                </div>

            </div>

        </div>

    </div>
    <div class="row">
        <div class="col s12 m12 center">
            <button type="button" class="btn amber lighten-1" id="signup-button">Реєстрація</button>
            <div id="auth-result"></div>
        </div>
    </div>
</form>
