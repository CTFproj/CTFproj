<#include "header.ftl">
<div class="container">
    <div class="panel panel-primary">
        <div class="panel-heading">Profile</div>
        <div class="panel-body">
            <#if id??><p>Team id: <br>
            Team name: ${name!}<br>
            Team score: ${score}<br>
            Team place: ${place}</p>
            <#if task??>
                <h2>Solved tasks</h2>
                <table class="table table-striped">
                    <tr>
                        <th>Task</th>
                        <th>Score</th>
                        <th>Category</th>
                    </tr>
                    <#list task as solve>
                        <tr>
                            <td><a href="/tasks#${solve.getNameWOS()}">${solve.getName()}</a></td>
                            <td>${solve.getScore()}</td>
                            <td>${solve.getCategory()}</td>
                        </tr>
                    </#list>
                </table>
            </#if>
        </div>
        <div class="panel-footer">
            <form action="logout" method="POST">
                <input id="btn_login" name="btn_login" type="submit" class="btn btn-danger" value="Logout"/>
            </form>
        </div>
    </div>
</div>
</#if>
<#include "footer.ftl">