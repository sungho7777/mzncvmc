<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Modal -->
<div class="modal fade" id="changePasswordModal" tabindex="-1" role="dialog" aria-labelledby="changePasswordModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="changePasswordModalLabel">Change Password Plz</h5>
                <button class="btn-close" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form>
                    <!-- Form Group (current password)-->
                    <div class="mb-3">
                        <label class="small mb-1" for="currentPassword">Current Password</label>
                        <input class="form-control" id="currentPassword" type="password" placeholder="Enter current password" />
                    </div>
                    <!-- Form Group (new password)-->
                    <div class="mb-3">
                        <label class="small mb-1" for="newPassword">New Password</label>
                        <input class="form-control" id="newPassword" type="password" placeholder="Enter new password" />
                    </div>
                    <!-- Form Group (confirm password)-->
                    <div class="mb-3">
                        <label class="small mb-1" for="confirmPassword">Confirm Password</label>
                        <input class="form-control" id="confirmPassword" type="password" placeholder="Confirm new password" />
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-bs-dismiss="modal">Close</button>
                <button id="changePassword-btn" class="btn btn-primary" type="button">change Password</button>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function() {
        const changePasswordBtn = document.getElementById('changePassword-btn');

        changePasswordBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            changePassword();
        });
    });

    const changePassword = async () => {
        const _pwNotifyDuration = localStorage.getItem('pwNotifyDuration');

        $('#loading').show();

        const response = await fetch('/api/account/changePassword', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
            },
            body: JSON.stringify({
                currentPassword: document.getElementById('currentPassword').value,
                newPassword: document.getElementById('newPassword').value,
                confirmPassword: document.getElementById('confirmPassword').value
            })
        });
        $('#loading').hide();

        const data = await response.json();

        alert(data.message);
        if(data.status == 'success'){
            if(_pwNotifyDuration == '999'){
                alert('신규 사용자입니다. 다시 로그인하세요.');
                main.goLogout();
            }
        }

    };
</script>