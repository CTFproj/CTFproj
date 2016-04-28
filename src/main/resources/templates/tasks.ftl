<#include "header.ftl">
<div class="container">
    <div id="task-nav" class="container-tasks"></div>
    <!-- Modal -->
    <div class="modal fade" id="task-window" tabindex="-1" role="dialog" aria-labelledby="task-windowLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="task-windowLabel"></h4>
                </div>
                <div class="modal-body">
                    <p>score - <span class="score"></span></p>
                    <p>cat - <span class="cat"></span> </p>
                    <p>desc - <span class="desc"></span></p>
                    <div class="input-group"></div>
                    <div id="incorrect-key" class="alert alert-danger alert-dismissable" role="alert">
                        <strong>Неправильно</strong>
                    </div>
                    <div id="correct-key" class="alert alert-success alert-dismissable" role="alert">
                        <strong>Правильно</strong>
                    </div>
                    <div id="task-solved" class="alert alert-success alert-dismissable" role="alert">
                        <strong>Решено</strong>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="/isotope.pkgd.min.js"></script>
<script src="/tasks.js"></script>
<#include "footer.ftl">