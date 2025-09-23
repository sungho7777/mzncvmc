<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="modal fade" id="successModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" >successfully saved!</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">Your current data has been successfully saved.<br/>Select "Confirm" below.</div>
            <div class="modal-footer">
                <a class="btn btn-danger" href="#" id="success-btn">Success</a>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function() {
        const successBtn = document.getElementById('success-btn');

        successBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            $('#successModal').modal('hide');

            const id = $(this).data('id');
            const categoryId = $(this).data('categoryId');
            const menu = $(this).data('menu');

            goView(menu, categoryId, id);
        });
    });
</script>