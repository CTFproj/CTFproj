<!DOCTYPE html>
<html>
<head>
    <title>Hello Friend!</title>
    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css'>
</head>
<body>
    <ul>
        <#list list as team>
            <li>${team_index+1} ${team.name} with score = ${team.score}</li>
        </#list>
    </ul>
</body>
</html>