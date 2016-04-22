<!DOCTYPE html>
<html>
<head>
    <title>Hello Friend!</title>
    <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css'>
    <link rel='stylesheet' href="/style.css">
</head>
<body>
<a href="/tasks">Задания</a>
<a href="/rating">Рейтинг</a>
<#if session??>
    <a href="/client">Профиль</a>
    Name:${name} score:${score} place:${place}
    <a href="/logout" >Выйти</a>
<#else> <a href="/login">Войти</a>
</#if>
