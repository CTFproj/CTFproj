<#include "header.ftl">
<div class="container">
    <form class="form-signin" action="${callbackUrl}" method="POST">
        <#if error??><div class="alert alert-danger" role="alert">
            <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
            <span class="sr-only">Error:</span>
            ${error}
        </div>

        </#if>
            <h2 class="form-signin-heading">Please sign in</h2>
            <label for="inputLogin" class="sr-only">Login</label>
            <input type="text" id="inputLogin" name="username" class="form-control" placeholder="Login" required="" autofocus="">
            <label for="inputPassword" class="sr-only">Password</label>
            <input type="password" id="inputPassword" name="password" class="form-control" placeholder="Password" required="">
            <button class="btn btn-lg btn-primary btn-block" type="submit" name="submit">Sign in</button>
    </form>
</div>
<#include "footer.ftl">
