<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Modal -->
<div class="modal fade" id="loginUserOtpModal" tabindex="-1" role="dialog" aria-labelledby="loginUserOtpModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="loginUserOtpModalLabel">Email Otp Code Plz</h5>
                <button class="btn-close" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form>
                    <input id="loginUserOtp_username" type="hidden" />

                    <div class="mb-3">
                        <label class="small mb-1" for="loginUserOtp_otpCode">Otp Code</label>
                        <input class="form-control" id="loginUserOtp_otpCode" type="text" placeholder="Enter current Otp Code" />
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-bs-dismiss="modal">Close</button>
                <button id="verifyLoginUserOtp-btn" class="btn btn-primary" type="button">OTP Login</button>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function() {
        const verifyLoginUserOtpBtn = document.getElementById('verifyLoginUserOtp-btn');

        verifyLoginUserOtpBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            const otpCode = $("#loginUserOtp_otpCode").val();

            if(isValidUserOtp_(otpCode)){
                verifyUserOtp();
            }else{
                alert('다시 작성하시요..');
                $("#otpCode").val('');
            }
        });
    });

    const isValidUserOtp_ = (otpCode) => {
        // 문자열로 처리 (앞자리 0 보존)
        const otpRegex = /^\d{6}$/;
        return otpRegex.test(otpCode);
    };


    const verifyUserOtp = async() => {
        const username = document.getElementById("loginUserOtp_username").value;
        const otpCode = document.getElementById("loginUserOtp_otpCode").value;

        const response = await fetch('/api/auth/verifyUserOtp', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                otpCode: otpCode
            })
        });

        const data = await response.json();

        $("#loginUserOtp_otpCode").val('');
        if(data.statusLogin == 'success'){
            // 성공
            $("#loginUserOtp_username").val('');
            $('#loginUserOtpModal').modal('hide');

            successLogin(data);
        }else if(data.statusLogin == 'fall'){
            // 실패
            alert(data.message);
        }
    };
</script>