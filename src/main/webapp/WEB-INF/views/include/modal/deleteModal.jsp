<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Modal -->
<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="successModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="successModalLabel">Default Bootstrap Modal</h5>
                <button class="btn-close" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">To delete your current data, select "Delete" below.</div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-bs-dismiss="modal">Close</button>
                <button id="delete-btn" class="btn btn-primary" type="button">Delete changes</button>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function() {
        const deleteBtn = document.getElementById('delete-btn');

        deleteBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            $('#deleteModal').modal('hide');

            const id = $(this).data('id');
            const categoryId = $(this).data('categoryId');

            deleteData(id);
        });
    });
</script>