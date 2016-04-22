<#include "header.ftl">
    <ul>
        <#list list as team>
            <li>${team_index+1} ${team.name} with score = ${team.score}</li>
        </#list>
    </ul>
<#include "footer.ftl">