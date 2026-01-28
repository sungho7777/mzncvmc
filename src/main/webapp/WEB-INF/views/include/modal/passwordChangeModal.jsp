<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Modal -->
<div class="modal fade" id="passwordChangeModal" tabindex="-1" role="dialog" aria-labelledby="passwordChangeModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="passwordChangeModalLabel">Change Password Plz</h5>
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
                <button id="passwordChange-btn" class="btn btn-primary" type="button">Password Change</button>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function() {
        const passwordChangeBtn = document.getElementById('passwordChange-btn');

        passwordChangeBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            passwordChange();
        });
    });

    const passwordChange = async () => {
        const _pwNotifyDuration = localStorage.getItem('pwNotifyDuration');

        $('#loading').show();

        const response = await fetch('/api/password/change', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json"
                //, 'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
            },
            body: JSON.stringify({
                currentPassword: document.getElementById('currentPassword').value,
                newPassword: document.getElementById('newPassword').value,
                confirmPassword: document.getElementById('confirmPassword').value
            })
        });
        $('#loading').hide();

        const data = await response.json();

        if(data.status == 'success'){
            if(_pwNotifyDuration == '999'){
                alert(data.message + "\nPlease log in again.");
                main.goLogout();
                return;
            }
        }

        alert(data.message);
    };
</script>