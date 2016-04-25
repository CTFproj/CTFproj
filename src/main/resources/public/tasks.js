function gettasks() {
    $.getJSON("/admin/task", function (data) {
        tasks = $.parseJSON(JSON.stringify(data));
        var categories = [];
        for (var i = tasks.length-1; i >= 0; i--) {
            if ($.inArray(tasks[i]['category'], categories) == -1) {
                var category = tasks[i]['category'];
                categories.push(category)
            }
        };
        $('.container-tasks').html("");
        $('.container-tasks').append($("<div class='tasks'></div>"));
        // console.log(categories);
        for (var i = 0; i <= tasks.length-1; i++) {
            var taskinfo = tasks[i];
            var taskid = taskinfo.name.replace(/\s/g,"-");
            $('.tasks').append($("<button class='task-button btn btn-primary btn-lg " + taskid + "' value='"+ taskinfo.id +"' data-toggle='modal' data-target='#task-window'>" + taskinfo.name +" - "  + taskinfo.score + "</button>"));
        };

        $('.task-button').click(function (e) {
            loadtask(this.value);
        });
        if (window.location.hash.length > 0){
            loadtaskbyname(window.location.hash.substring(1));
            $("#task-window").modal("show");
        }
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
    chal.find('.desc').html(obj.des);
}

$('#task-window').on('hide.bs.modal', function (event) {
    document.location.hash = "";
});