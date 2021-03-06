    function gettasks() {
        $.getJSON("/admin/task", function (data) {
            tasks = $.parseJSON(JSON.stringify(data));
            var categories = [];
            $('#nav-tasks').html("");
            $('#nav-tasks').append($("<h1>Категория</h1><ul id='tasks-nav-cat' class='nav nav-sidebar' data-filter-group='category'></ul>"));
            $('#tasks-nav-cat').append($("<li class='active' data-filter=''>все</li>"));
            for (var i = tasks.length-1; i >= 0; i--) {
                if ($.inArray(tasks[i]['category'], categories) == -1) {
                    var category = tasks[i]['category'];
                    categories.push(category);
                    var categoryid = category.replace(/ /g,"-");
                    $('#tasks-nav-cat').append($("<li data-filter='."+ categoryid + "'>"+ category +"</li>"));
                }
            };
            $('#nav-tasks').append($("<h1>Состояние</h1><ul id='tasks-nav-sol' class='nav nav-sidebar' data-filter-group='solution'></ul>"));
            $('#tasks-nav-sol').append($("<li class='active' data-filter=''>все</li>"));
            $('#tasks-nav-sol').append($("<li data-filter='.success'>решенные</li>"));
            $('#tasks-nav-sol').append($("<li data-filter='.primary'>нерешенные</li>"));

            $('.container-tasks').html("");
            for (var i = 0; i < tasks.length; i++) {
                var taskinfo = tasks[i];
                var taskcategoryid = taskinfo.category.replace(/\s/g,"-");
                $('.container-tasks').append($("<div class='task-button "+ taskcategoryid + " " + taskinfo.id +"' data-value='"+ taskinfo.id +"' data-toggle='modal' data-target='#task-window'><h3>"  + taskinfo.score + "</h3><p>" + taskinfo.name +"</p></div>"));
                checktask(taskinfo.id);
            };
            $('body').on('click', 'div.task-button', function () {
                loadtask($(this).data('value'));
            });
            if (window.location.hash.length > 0){
                loadtaskbyname(window.location.hash.substring(1));
            }
            var $container = $('.container-tasks').isotope({
                itemSelector: '.task-button',
                layoutMode: 'fitRows'
            });
            var filters = {};
            $('#nav-tasks').on( 'click', 'li', function() {
                var $this = $(this);
                var $navGroup = $this.parents('.nav-sidebar');
                var filterGroup = $navGroup.attr('data-filter-group');
                filters[ filterGroup ] = $this.attr('data-filter');
                var filterValue = concatValues( filters );
                $container.isotope({ filter: filterValue });
            });
            $('.nav-sidebar').each( function( i, navGroup ) {
                var $navGroup = $( navGroup );
                $navGroup.on( 'click', 'li', function() {
                    $navGroup.find('.active').removeClass('active');
                    $( this ).addClass('active');
                });
            });
            function concatValues( obj ) {
                var value = '';
                for ( var prop in obj ) {
                    value += obj[ prop ];
                }
                return value;
            }
        });
    }
    $('#task-window').on('keyup', '#task-input', function (event) {
        if(event.keyCode == 13){
            tasksubmit($('#task-id').val(), $('#task-input').val());
        }
    });
    function tasksubmit(id, flag) {
        $('#task-submit').addClass("disabled-button");
        $('#task-submit').prop('disabled', true);
        $('#task-input').prop('disabled', true);
        $.post("/pass", {
            taskid: id,
            flg: flag
        }, function (data) {
            if (data == 0){ // Incorrect key
                $("#incorrect-key").slideDown();
            }
            else if (data == 1){ // Challenge Solved
                $("#correct-key").slideDown();
                $('.' + id + '').removeClass('primary').addClass('success');
                $("#task-submit").remove();
                $("#task-input").remove();
            }
            setTimeout(function(){
                $('#incorrect-key').slideUp();
                $('#task-submit').removeClass("disabled-button");
                $('#task-submit').prop('disabled', false);
                $('#task-input').prop('disabled', false);
            }, 3000);
        })
    }
    $('#task-window').on('click', '#task-submit', function () {
        tasksubmit($('#task-id').val(), $('#task-input').val());
    });
    $(function() {
        gettasks();
    });
    function loadtask(id) {
        obj = $.grep(tasks, function (e) {
            return e.id == id;
        })[0];
        if(typeof obj != 'undefined') {
            updateTaskWindow(obj);
        }
    }
    function loadtaskbyname(taskname) {
        obj = $.grep(tasks, function (e) {
            return e.name == taskname;
        })[0];
        if(typeof obj != 'undefined') {
            updateTaskWindow(obj);
            $("#task-window").modal("show");
        }
    }
    function updateTaskWindow(obj) {
        window.location.hash = obj.name;
        var chal = $('#task-window');
        chal.find('.modal-title').text(obj.name);
        chal.find('.score').text(obj.score);
        chal.find('.cat').text(obj.category);
        chal.find('.desc').html(obj.des);
        checktaskwindow(obj.id);
    }
    $('#task-window').on('hide.bs.modal', function (event) {
        document.location.hash = "";
        $("#task-input").removeClass("wrong");
        $('#task-submit').removeClass("disabled-button");
        $('#task-submit').prop('disabled', false);
        $('#task-input').prop('disabled', false);
        $("#incorrect-key").slideUp();
        $("#correct-key").slideUp();
    });
    function checktaskwindow(id) {
        $.post("/checktask", {
            taskid: id
        }, function (data) {
            if (data == 0){ // Incorrect key
                $('.input-group').html("");
                $('.input-group').append($("<input id='task-id' type='hidden' value='"+ obj.id +"'>"));
                $('.input-group').append($("<input id='task-input' type='text' class='form-control' placeholder='flag{!s_HeRe}'>"));
                $('.input-group').append($("<div class='input-group-btn'><button id='task-submit' type='submit' class='btn btn-default' name='submit'>Отправить</button></div>"));
            }
            else if (data == 1){ // Challenge Solved
                $('#correct-key').show();
                $("#task-submit").remove();
                $("#task-input").remove();
            }
        });
    }
    function checktask(id) {
        $.post("/checktask", {
            taskid: id
        }, function (data) {
            if (data == 0){
                $('.' + id + '').removeClass('success').addClass('primary');
            }
            else if (data == 1){ // Challenge Solved
                $('.' + id + '').removeClass('primary').addClass('success');
            }
        });
    }