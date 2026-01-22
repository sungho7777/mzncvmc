<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Modal -->
<div class="modal fade" id="generateUserOtpModal" tabindex="-1" role="dialog" aria-labelledby="generateUserOtpModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="generateUserOtpModalLabel">Enter the OTP code to set up MFA.</h5>
                <button class="btn-close" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form>
                    <input id="generateUserOtp_username" type="hidden" />

                    <div class="mb-3">
                        <label class="small mb-1" for="generateUserOtp_otpCode">Otp Code</label>
                        <input class="form-control" id="generateUserOtp_otpCode" type="text" placeholder="Enter current Otp Code" />
                    </div>
                    <!-- Step Component Example -->
                    <div class="step mb-5">
                        <div class="step-item active">
                            <a class="step-item-link" href="#!">Step 1</a>
                        </div>
                        <div class="step-item">
                            <a class="step-item-link disabled" href="#!">Step 2</a>
                        </div>
                        <div class="step-item">
                            <a class="step-item-link disabled" href="#!">Step 3</a>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-bs-dismiss="modal">Close</button>
                <button id="generateUserOtp-btn" class="btn btn-primary" type="button">generateUserOtp</button>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function() {
        const generateUserOtpBtn = document.getElementById('generateUserOtp-btn');

        generateUserOtpBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            const otpCode = $("#generateUserOtp_otpCode").val();

            if(isValidUserOtp(otpCode)){
                generateUserOtp();
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


    const generateUserOtp = async() => {
        const username = document.getElementById("generateUserOtp_username").value;
        const code = document.getElementById("generateUserOtp_otpCode").value;

        const response = await fetch('/api/auth/generateUserOtp', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                otpCode: code
            })
        });

        const data = await response.json();

        $("#generateUserOtp_otpCode").val('');
        if(data.status == 'success'){
            // 성공
            $("#generateUserOtp_username").val('');
            $('#generateUserOtpModal').modal('hide');

            generateUserMfaModal();
        }else if(data.status == 'fail'){
            // 실패
            alert(data.message);
        }
    };
</script>