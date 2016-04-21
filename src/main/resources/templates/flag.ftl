<#if task??>
    <#list task as task1>
        <h1>${task1.getName()}</h1>
        <p>Score:${task1.getScore()} </p>
        <p>Category:${task1.getCategory()}</p>
        <p>Description:${task1.getDes()}</p>
         <#list not_solve_task as solve>
                <#if task1.getId()=solve.getId()>
                    <form action="/pass" method="POST">
                        <input type="text" name="${solve.getId()}" value="" />
                        <p />
                        <input type="submit" name="submit" value="Submit" />
                    </form>
                </#if>
            </#list>
    </#list>
</#if>