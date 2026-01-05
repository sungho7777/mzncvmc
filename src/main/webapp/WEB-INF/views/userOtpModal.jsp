<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Modal -->
<div class="modal fade" id="userOtpModal" tabindex="-1" role="dialog" aria-labelledby="userOtpModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="userOtpModalLabel">Otp Code Plz</h5>
                <button class="btn-close" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form>
                    <input id="otpUsername" type="hidden" />

                    <div class="mb-3">
                        <label class="small mb-1" for="otpCode">Otp Code</label>
                        <input class="form-control" id="otpCode" type="text" placeholder="Enter current Otp Code" />
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-bs-dismiss="modal">Close</button>
                <button id="verifyUserOtp-btn" class="btn btn-primary" type="button">verifyUserOtp</button>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    document.getElementById("otpCode").focus();

    document.addEventListener('DOMContentLoaded', function() {
        const verifyUserOtpBtn = document.getElementById('verifyUserOtp-btn');

        verifyUserOtpBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            const otpCode = $("#otpCode").val();

            if(isValidUserOtp(otpCode)){
                verifyUserOtp();
            }else{
                alert('다시 작성하시요..');
                $("#otpCode").val('');
            }
        });
    });

    const isValidUserOtp = (otpCode) => {
        // 문자열로 처리 (앞자리 0 보존)
        const otpRegex = /^\d{6}$/;
        return otpRegex.test(otpCode);
    };


    const verifyUserOtp = async() => {
        const otpUsername = document.getElementById("otpUsername").value;
        const otpCode = document.getElementById("otpCode").value;

        const response = await fetch('/api/auth/login/verifyUserOtp', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: otpUsername,
                otp: otpCode
            })
        });

        const data = await response.json();

        if (response.ok) {
            // 모달 닫기
            $("#otpCode").val('');
            $("#otpUsername").val('');
            $('#userOtpModal').modal('hide');

            successLogin(data);
        } else {
            // 로그인 실패
            alert(data.error || '실패. 다시 작성해보시요!!');
            $("#otpCode").val('');
        }
    };
</script>