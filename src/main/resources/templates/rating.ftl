<#include "header.ftl">
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">Рейтинг команд</div>
        <table class="table">
            <tr>
                <th>#</th>
                <th>Название</th>
                <th>Очки</th>
            </tr>
        <#list list as team>
            <tr>
                <td>${team_index+1}</td>
                <td><a href="/team/${team.name}">${team.name}</a></td>
                <td>${team.score}</td>
            </tr>
        </#list>
        </table>
    </div>
</div>
<#include "footer.ftl">