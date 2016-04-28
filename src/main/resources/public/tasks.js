    function gettasks() {
        $.getJSON("/admin/task", function (data) {
            tasks = $.parseJSON(JSON.stringify(data));
            var categories = [];
            $('.container-tasks').html("");
            $('.container-tasks').append($("<div id='tasks-nav-cat' class='button-group' data-filter-group='category'><h3>Category</h3></div>"));
            $('#tasks-nav-cat').append($("<button class='btn btn-default is-checked' data-filter=''>all cat</button>"));
            for (var i = tasks.length-1; i >= 0; i--) {
                if ($.inArray(tasks[i]['category'], categories) == -1) {
                    var category = tasks[i]['category'];
                    categories.push(category);
                    var categoryid = category.replace(/ /g,"-");
                    $('#tasks-nav-cat').append($("<button class='btn btn-default' data-filter='."+ categoryid + "'>"+ category +"</button>"));
                }
            };
            $('.container-tasks').append($("<div id='tasks-nav-sol' class='button-group' data-filter-group='solution'><h3>Solved?</h3></div>"));
            $('#tasks-nav-sol').append($("<button class='btn btn-default is-checked' data-filter=''>all tasks</button>"));
            $('#tasks-nav-sol').append($("<button class='btn btn-success' data-filter='.btn-success'>solv</button>"));
            $('#tasks-nav-sol').append($("<button class='btn btn-primary' data-filter='.btn-primary'>not solv</button>"));
            $('.container-tasks').append($("<div class='tasks'></div>"));
            for (var i = 0; i <= tasks.length-1; i++) {
                var taskinfo = tasks[i];
                var taskcategoryid = taskinfo.category.replace(/\s/g,"-");
                $('.tasks').append($("<div class='task-button "+ taskcategoryid + " " + taskinfo.id +" btn btn-lg' data-value='"+ taskinfo.id +"' data-toggle='modal' data-target='#task-window'>" + taskinfo.name +" - "  + taskinfo.score + "</div>"));
                checktask(taskinfo.id);
            };
            $('body').on('click', 'div.task-button', function () {
                loadtask($(this).data('value'));
            });
            if (window.location.hash.length > 0){
                loadtaskbyname(window.location.hash.substring(1));
                $("#task-window").modal("show");
            }
            var $container = $('.tasks').isotope({
                itemSelector: '.task-button',
                layoutMode: 'fitRows'
            });
            var filters = {};
            $('#task-nav').on( 'click', 'button', function() {
                var $this = $(this);
                var $buttonGroup = $this.parents('.button-group');
                var filterGroup = $buttonGroup.attr('data-filter-group');
                filters[ filterGroup ] = $this.attr('data-filter');
                var filterValue = concatValues( filters );
                $container.isotope({ filter: filterValue });
            });
            $('.button-group').each( function( i, buttonGroup ) {
                var $buttonGroup = $( buttonGroup );
                $buttonGroup.on( 'click', 'button', function() {
                    $buttonGroup.find('.is-checked').removeClass('is-checked');
                    $( this ).addClass('is-checked');
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
                $('.' + id + '').removeClass('btn-primary').addClass('btn-success');
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
    function update(){
        gettasks()
    }
    $(function() {
        gettasks();
    });
    setInterval(update, 300000);
    function loadtask(id) {
        obj = $.grep(tasks, function (e) {
            return e.id == id;
        })[0];
        updateTaskWindow(obj);
    }
    function loadtaskbyname(taskname) {
        obj = $.grep(tasks, function (e) {
            return e.name == taskname;
        })[0];
        updateTaskWindow(obj);
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
                $('.input-group').append($("<div class='input-group-btn'><button id='task-submit' type='submit' class='btn btn-default' name='submit'>Submit</button></div>"));
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
                $('.' + id + '').removeClass('btn-success').addClass('btn-primary');
            }
            else if (data == 1){ // Challenge Solved
                $('.' + id + '').removeClass('btn-primary').addClass('btn-success');
            }
        });
    }