<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title} | CTFproj</title>
    <link rel="icon" type="image/png" href="/img/msun.png" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel="stylesheet" href="//fonts.googleapis.com/css?family=PT+Sans%3A400italic%2C700%2C300%2C600%2C400&amp;subset=latin%2Ccyrillic&amp;ver=4.5.1" type="text/css" media="all">
    <link rel="stylesheet" href="/css/font-awesome.min.css">
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/animate.css">
    <link rel="stylesheet" href="/css/background.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.2/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>
<body>
<nav class="navbar navbar-fixed-top navbar-default">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/"><img class="img-responsive" src="/img/ctfproj.png"></a>
        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse">
            <ul class="nav navbar-nav">
                <li><a href="/tasks">Задания</a></li>
                <li><a href="/rating">Рейтинг</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
            <#if session??>
                <li><a href="/client">Профиль</a>
                <li class="navbar-text">Name:${name} score:${score} place:${place}</li>
                <li><a href="/logout" >Выйти</a></li>
            <#else> <li><a href="/login">Войти</a></li>
            </#if>
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-->
</nav>
