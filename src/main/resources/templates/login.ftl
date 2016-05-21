<#include "header.ftl">
<header style="background-image: url('/img/bg.jpg');">
    <div class="intro-content">
        <form class="form-signin" action="${callbackUrl}" method="POST">
        <#if error??>
            <div class="alert alert-danger">
                <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                <span class="sr-only">Ошибка:</span>
            ${error}
            </div>
        </#if>
            <h2 class="form-signin-heading">Авторизируйтесь</h2>
            <label for="inputLogin" class="sr-only">Логин</label>
            <input type="text" id="inputLogin" name="username" class="form-control" placeholder="Логин" required="" autofocus="">
            <label for="inputPassword" class="sr-only">Пароль</label>
            <input type="password" id="inputPassword" name="password" class="form-control" placeholder="Пароль" required="">
            <button class="btn btn-lg btn-default btn-block" type="submit" name="submit">Войти</button>
        </form>
    </div>
</header>

<#include "footer.ftl">
