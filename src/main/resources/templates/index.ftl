<#include "header.ftl">
<header style="background-image: url('/img/bg-header.jpg');">
    <div class="intro-content">
        <div class="brand">CTFProj - окунись в информационную безопасность</div>
    </div>
    <div class="scroll-down">
        <a class="btn page-scroll" href="#about"><i class="fa fa-angle-double-down fa-fw"></i></a>
    </div>
</header>
<section id="about">
    <div class="container-fluid wow fadeIn">
        <div class="row">
            <div class="col-md-6 about-img">
                <img src="/img/bg-about.jpg" class="img-responsive" alt="">
            </div>
            <div class="col-md-6 text-center">
                <h1>Привет, первокурсник!</h1>
                <h4>
                    Ты работаешь в фирме инженером по информационной безопасности, твоя контора подверглась взлому
                    злоумышленников, поэтому теперь тебе необходимо вычислить и поймать хакеров. Благо они любят
                    оставлять зацепки.
                </h4>
            </div>
        </div>
    </div>
</section>
<section id="rules" class="bg-gray">
    <div class="container-fluid">
        <div class="row text-center">
            <div class="col-lg-12 wow fadeIn">
                <h1>Правила</h1>
                <h4>Эта информация тебе определенно поможет.</h4>
            </div>
        </div>
        <div class="row text-center content-row">
            <div class="col-md-3 col-sm-6 wow fadeIn" data-wow-delay=".2s">
                <div class="rules-content">
                    <i class="fa fa-flag fa-4x"></i>
                    <h3>Формат флага</h3>
                    <p>flag{something}</p>
                </div>
            </div>
            <div class="col-md-3 col-sm-6 wow fadeIn" data-wow-delay=".4s">
                <div class="rules-content">
                    <i class="fa fa-rocket fa-4x"></i>
                    <h3>Фантазируй</h3>
                    <p>Пробуй неординарные подходы к заданиям.</p>
                </div>
            </div>
            <div class="col-md-3 col-sm-6 wow fadeIn" data-wow-delay=".6s">
                <div class="rules-content">
                    <i class="fa fa-diamond fa-4x"></i>
                    <h3>Зарабатывай очки</h3>
                    <p>Чем больше соберешь флагов, тем больше очков тебя ждет.</p>
                </div>
            </div>
            <div class="col-md-3 col-sm-6 wow fadeIn" data-wow-delay=".8s">
                <div class="rules-content">
                    <i class="fa fa-ban fa-4x"></i>
                    <h3>Не списывай</h3>
                    <p>Дружба дружбой, а задания заданиями</p>
                </div>
            </div>
        </div>
        <p>В случае вопросов</p>
    </div>
</section>
<section class="kza bg-dark">
    <div class="container text-center">
        <h3>Готов приступить к заданиям?</h3>
        <div class="row">
            <div class="col-lg-8 col-lg-offset-2">
                <a href="/tasks" class="btn btn-outline-inverse btn-lg">Приступить!</a>
            </div>
        </div>
    </div>
</section>
<aside class="raznr" style="background-image: url('/img/bg-kmb.jpg');">
    <div class="container">
        <div class="row">
            <div class="col-md-10 col-md-offset-1 wow fadeIn">
                <span class="quote">Не знаешь с чего начать?<br> Начни с <span
                        class="text-primary">курса молодого бойца</span>!</span>
                <a class="btn btn-outline-inverse btn-lg" href="http://kmb.ufoctf.ru/">Начать</a>
            </div>
        </div>
    </div>
</aside>
<script src="/js/jquery.easing.min.js"></script>
<script src="/js/core.js"></script>
<script src="/js/transition.js"></script>
<script src="/js/background.js"></script>
<script src="/js/wow.min.js"></script>
<script>
    (function ($) {
        "use strict";
        $('a.page-scroll').bind('click', function (event) {
            var $anchor = $(this);
            $('html, body').stop().animate({
                scrollTop: ($($anchor.attr('href')).offset().top - 50)
            }, 1250, 'easeInOutExpo');
            event.preventDefault();
        });
        $('.navbar-collapse ul li a').click(function () {
            $('.navbar-toggle:visible').click();
        });
    })(jQuery);
    var isPhoneDevice = "ontouchstart" in document.documentElement;
    $(document).ready(function () {
        if (isPhoneDevice) {
            //mobile
        } else {
            //desktop
            wow = new WOW({
                offset: 50
            });
            wow.init();
        }
    });
</script>
<#include "footer.ftl">