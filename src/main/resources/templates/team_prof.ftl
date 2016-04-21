<!DOCTYPE html>
<html>
<head>
    <title>Hello Friend!</title>
    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css'>
</head>
<body>
<h1>Team_info</h1>
<h1>${name} with score = ${score} </h1>
<#if name == "19.11"><h1>WE LOVE YOU</h1></#if>
<form action="/addscores" method="POST">
    <input class="form-control" id="score" name="score" placeholder="score" type="text" value="" />
    <input id="btn_login" name="btn_login" type="submit" class="btn btn-default" value="Add" />
</form>
<form action="/addscores" method="POST">
    <input class="form-control" id="password" name="password" placeholder="password" type="password" value="" />
    <input id="btn_login" name="btn_login" type="submit" class="btn btn-default" value="Add" />
</form>
</body>
</html>