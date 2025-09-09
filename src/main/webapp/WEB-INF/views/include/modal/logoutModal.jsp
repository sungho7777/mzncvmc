<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="modal fade" id="logoutModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" >Ready to Leave?</h5>
                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">×</span>
                </button>
            </div>
            <div class="modal-body">Select "Logout" below if you are ready to end your current session.</div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-dismiss="modal">Cancel</button>
                <a class="btn btn-primary" href="#" id="logout-btn">Logout</a>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function() {
        const logoutBtn = document.getElementById('logout-btn');

        logoutBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            fetch('/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json' // 서버가 JSON을 예상할 경우
                }
            })
                .then(response => {
                    if (response.ok) {
                        // 상태 코드 200-299
                        //alert('로그아웃되었습니다.');
                        window.location.href = '/login';
                    } else {
                        // 다른 상태 코드
                        throw new Error('로그아웃 실패');
                    }
                })
                .catch(error => {
                    console.error('로그아웃 실패:', error);
                    alert('로그아웃에 실패했습니다. 다시 시도해 주세요.');
                });
        });
    });
</script>