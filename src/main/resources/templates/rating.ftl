<#include "header.ftl">
<div class="container">
    <div class="panel panel-default">

        <ul class="nav nav-tabs" role="tablist">
            <li role="presentation" class="active"><a href="#rating" aria-controls="rating" role="tab"
                                                      data-toggle="tab">Рейтинг 1 курса</a></li>
            <li role="presentation"><a href="#total_rating" aria-controls="total_rating" role="tab" data-toggle="tab">
                Общий рейтинг</a></li>
        </ul>

        <!-- Tab panes -->
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="rating">
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
            <div role="tabpanel" class="tab-pane" id="total_rating">
                <table class="table">
                    <tr>
                        <th>#</th>
                        <th>Название</th>
                        <th>Очки</th>
                    </tr>
                <#list totallist as team>
                    <tr>
                        <td>${team_index+1}</td>
                        <td><a href="/team/${team.name}">${team.name}</a></td>
                        <td>${team.score}</td>
                    </tr>
                </#list>
                </table>
            </div>
        </div>
    </div>
</div>
<#include "footer.ftl">