<#include "header.ftl">
<div class="container">
    <div class="container-tasks"></div>
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
                    <p>desc - <span class="desc"></span></p>
                </div>
            </div>
        </div>
    </div>
<script src="/tasks.js"></script>
</div>
<#include "footer.ftl">