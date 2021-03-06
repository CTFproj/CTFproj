<#include "header.ftl">
<div class="panel panel-header panel-primary">
    <div class="panel-heading">
        <div class="container">Профиль</div>
    </div>
</div>
<div class="container">
<div class="panel panel-primary">
<div class="panel-body">
<#if id??><p>
    Название команды: ${name}<br>
    Очки команды: ${score}<br>
    Место: ${place}</p>
    <#if task??>
        <h2>Решенные задания</h2>
        <table class="table table-striped">
            <tr>
                <th>Задание</th>
                <th>Очки</th>
                <th>Категория</th>
            </tr>
            <#list task as solve>
                <tr>
                    <td><a href="/tasks#${solve.getName()}">${solve.getName()}</a></td>
                    <td>${solve.getScore()}</td>
                    <td>${solve.getCategory()}</td>
                </tr>
            </#list>
        </table>
    </#if>
</div>
    <div class="panel-footer">
        <form action="logout" method="POST">
            <input id="btn_login" name="btn_login" type="submit" class="btn btn-danger" value="Выйти"/>
        </form>
    </div>
</div>
</div>
</#if>
<#include "footer.ftl">