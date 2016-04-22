<#include "header.ftl">
<div class="container">
    <#if task??>
        <#list task as task1>
        <div class="panel panel-default">
        <div class="panel-body">
            <h1>${task1.getName()}</h1>
            <p>Score:${task1.getScore()} </p>
            <p>Category:${task1.getCategory()}</p>
            <p>Description:${task1.getDes()}</p>
            <#assign x = true>
            <#list not_solve_task as solve>
                    <#if task1.getId()=solve.getId()>
                        <form action="/pass" method="POST">
                           <div class="row"><div class="col-md-4">
                            <div class="input-group">
                                <input type="text" class="form-control" name="${solve.getId()}" value="" >
                                <div class="input-group-btn">
                                    <button type="submit" class="btn btn-default" name="submit">Submit</button>
                                </div><!-- /btn-group -->
                            </div><!-- /input-group -->
                           </div></div>
                        </form>
                        <#assign x = false>
                    </#if>
            </#list>
            <#if x>
                <div class="row"><div class="col-md-4"><div class="alert alert-success" role="alert">Solved</div></div></div>
            </#if>
        </div>
        </div>
        </#list>
    </#if>
</div>
<#include "footer.ftl">