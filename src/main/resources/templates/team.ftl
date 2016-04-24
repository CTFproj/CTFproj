<#include "header.ftl">
<div class="container">
<div class="panel panel-primary">
    <div class="panel-heading">Profile</div>
        <div class="panel-body">
        <#if teamid??><p>Team id: ${teamid} <br>
            Team name: ${teamname}<br>
            Team score: ${teamscore}<br>
            Team place: ${teamplace}</p>
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
    </div>
</div>
</#if>
<#include "footer.ftl">