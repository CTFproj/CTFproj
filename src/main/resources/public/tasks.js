    function gettasks() {
        $.getJSON("/admin/task", function (data) {
            tasks = $.parseJSON(JSON.stringify(data));
            var categories = [];
            $('.container-tasks').html("");
            $('.container-tasks').append($("<ul id='tasks-nav' class='nav nav-pills'></ul>"));
            $('#tasks-nav').append($("<li class='active' role='presentation'><a href='#' class='all'>all</a></li>"));
            for (var i = tasks.length-1; i >= 0; i--) {
                if ($.inArray(tasks[i]['category'], categories) == -1) {
                    var category = tasks[i]['category'];
                    categories.push(category);
                    var categoryid = category.replace(/ /g,"-");
                    $('#tasks-nav').append($("<li role='presentation'><a href='#' class='"+ categoryid + "'>"+ category +"</a></li>"));
                }
            };
            $('.container-tasks').append($("<div class='tasks'></div>"));
            for (var i = 0; i <= tasks.length-1; i++) {
                var taskinfo = tasks[i];
                var taskid = taskinfo.name.replace(/\s/g,"-");
                var taskcategoryid = taskinfo.category.replace(/\s/g,"-");
                $('.tasks').append($("<button data-id='id-"+ (i+1) +"' data-category='"+ taskcategoryid +"' class='task-button btn btn-primary btn-lg' value='"+ taskinfo.id +"' data-toggle='modal' data-target='#task-window'>" + taskinfo.name +" - "  + taskinfo.score + "</button>"));
            };
            $('body').on('click', 'button.task-button', function () {
                loadtask(this.value);
                console.log('call function loadtask by id - ' + this.value);
            });
            if (window.location.hash.length > 0){
                loadtaskbyname(window.location.hash.substring(1));
                $("#task-window").modal("show");
            }

            // get the action filter option item on page load
            var $filterCategory = $('#tasks-nav li.active a').attr('class');

            // get and assign the ourHolder element to the
            // $holder varible for use later
            var $holder = $('div.tasks');
            // clone all items within the pre-assigned $holder element
            var $data = $holder.clone();
            // attempt to call Quicksand when a filter option
            // item is clicked
            $('#tasks-nav li a').click(function(e) {
                // reset the active class on all the buttons
                $('#tasks-nav li').removeClass('active');

                // assign the class of the clicked filter option
                // element to our $filterType variable
                var $filterCategory = $(this).attr('class');
                $(this).parent().addClass('active');
                if ($filterCategory == 'all') {
                    // assign all li items to the $filteredData var when
                    // the 'All' filter option is clicked
                    var $filteredData = $data.find('button');
                }
                else {
                    // find all li elements that have our required $filterType
                    // values for the data-type element
                    var $filteredData = $data.find("button[data-category='" + $filterCategory + "']");
                }

                // call quicksand and assign transition parameters
                // $holder.quicksand($filteredData, {
                $holder.quicksand($filteredData);
            });
        });
    }
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
    }

    $('#task-window').on('hide.bs.modal', function (event) {
        document.location.hash = "";
    });

