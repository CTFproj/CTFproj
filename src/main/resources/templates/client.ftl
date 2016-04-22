<#include "header.ftl">
<div class="container">
    <div class="panel panel-primary">
        <div class="panel-heading">Profile</div>
        <div class="panel-body">
            <p>Team id: ${id}<br>
            Team name: ${name}<br>
            Team score: ${score}<br>
            Team place: ${place}</p>
        </div>
        <div class="panel-footer">
            <form action="logout" method="POST">
                <input id="btn_login" name="btn_login" type="submit" class="btn btn-danger" value="Logout"/>
            </form>
        </div>
    </div>
</div>
<#include "footer.ftl">