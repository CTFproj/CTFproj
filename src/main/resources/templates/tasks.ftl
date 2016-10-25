<#include "header.ftl">
<div class="container-fluid">
    <div class="row">
        <div id="nav-tasks" class="col-sm-3 col-md-2 sidebar"></div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <div class="container-tasks"></div>
            <!-- Modal -->
            <div class="modal fade" id="task-window" tabindex="-1" role="dialog" aria-labelledby="task-windowLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="task-windowLabel"></h4>
                            <div class="modal-info">
                                <div><i class="fa fa-trophy" aria-hidden="true"></i><span class="score"></span></div>
                                <div><i class="fa fa-bookmark" aria-hidden="true"></i><span class="cat"></span></div>
                            </div>
                        </div>
                        <div class="modal-body">
                            <p class="desc"></p>
                            <div class="input-group"></div>
                            <div id="incorrect-key" class="alert alert-danger alert-dismissable" role="alert">
                                <strong>Неверный флаг</strong>
                            </div>
                            <div id="correct-key" class="alert alert-success alert-dismissable" role="alert">
                                <strong>Задание решено</strong>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="/js/isotope.pkgd.min.js"></script>
<script src="/js/tasks.js"></script>
<#include "footer.ftl">