<#include "header.ftl">
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading">Rating</div>
        <table class="table">
            <tr>
                <th>#</th>
                <th>Name</th>
                <th>Score</th>
            </tr>
        <#list list as team>
            <tr>
                <td>${team_index+1}</td>
                <td>${team.name}</td>
                <td>${team.score}</td>
            </tr>
        </#list>
        </table>
    </div>
</div>
<#include "footer.ftl">