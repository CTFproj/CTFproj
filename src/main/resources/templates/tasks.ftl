<#include "header.ftl">
<div class="container">
<#if task??>
    <#list task as task1>
        <!-- Button trigger modal -->
        <a class="btn btn-primary btn-lg ${task1.getName()}" href="#${task1.getName()}" >
            ${task1.getName()} - ${task1.getScore()}
        </a>
        <script>
            $(document).ready(function() {
                if(window.location.href.indexOf('#${task1.getName()}') != -1) {
                    $('#${task1.getName()}').modal('show');
                }
                $('.${task1.getName()}').click(function () {
                    $('#${task1.getName()}').modal('show');
                });
            });
        </script>
        <!-- Modal -->
        <div class="modal fade" id="${task1.getName()}" tabindex="-1" role="dialog" aria-labelledby="${task1.getName()}Label">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="${task1.getName()}Label">${task1.getName()}</h4>
                    </div>
                    <div class="modal-body">
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
                        <#elseif id?? && id == task1_index+1>
                            <div class="row"><div class="col-md-4"><div class="alert alert-danger" role="alert">Wrong flag</div></div></div>
                        </#if>
                    </div>
                </div>
            </div>
        </div>
    </#list>
</#if>
</div>
<#include "footer.ftl">