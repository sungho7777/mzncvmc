<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Modal -->
<style>
.qr-container {
    display: flex;
    flex-direction: column;
    align-items: center;     /* 가로 가운데 */
    justify-content: center; /* 세로 가운데 */
}

</style>
<div class="modal fade" id="userMfaModal" tabindex="-1" role="dialog" aria-labelledby="userMfaModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="userMfaModalLabel">Mfa Code Plz</h5>
                <button class="btn-close" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form>
                    <input id="mfaUsername" type="hidden" />

                    <div class="qr-container">
                        <div class="loading" id="qrLoading">QR 코드 생성 중...</div>
                        <img id="qrCode" style="display:none;" alt="QR Code">
                    </div>
                    <div class="mb-3">
                        <label class="small mb-1" for="mfaCode">Mfa Code</label>
                        <input class="form-control" id="mfaCode" type="text" placeholder="Enter current Mfa Code" />
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-bs-dismiss="modal">Close</button>
                <button id="verifyUserMfa-btn" class="btn btn-primary" type="button">verifyUserMfa</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    const generateQRCode = async() => {
        const mfaUsername = document.getElementById("mfaUsername").value;

        const response = await fetch('/api/auth/generateQR', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: mfaUsername
            })
        });

        const data = await response.json();

        if (response.ok) {
            $('#qrLoading').text('신규 사용자는 QR 코드를 사용하여 MFA 인증을 완료하세요.');
            $('#qrCode').attr('src', data.qrCode).show();
        }
    };

    document.addEventListener('DOMContentLoaded', function() {
        const verifyUserMfaBtn = document.getElementById('verifyUserMfa-btn');

        verifyUserMfaBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            const mfaCode = $("#mfaCode").val();
            if(isValidUserMfa(mfaCode)){
                verifyUserMfa();
            }else{
                alert('다시 작성하시요..');
                $("#mfaCode").val('');
            }
        });
    });

    const isValidUserMfa = (mfaCode) => {
        // 문자열로 처리 (앞자리 0 보존)
        const mfaRegex = /^\d{6}$/;
        return mfaRegex.test(mfaCode);
    };

    const verifyUserMfa = async() => {
        const mfaUsername = document.getElementById("mfaUsername").value;
        const mfaCode = document.getElementById("mfaCode").value;

        const response = await fetch('/api/auth/verifyUserMfa', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: mfaUsername,
                mfaCode: mfaCode
            })
        });

        const data = await response.json();

        if (response.ok) {
            $("#mfaCode").val('');

            if(data.statusLogin == 'success'){
                // 모달 닫기
                $("#mfaUsername").val('');
                $('#userMfaModal').modal('hide');

                successLogin(data);
            }else if(data.statusLogin == 'fall'){
                alert('코드 검증에 실패하였습니다. 다시 작성해보시요!!');
            }
        } else {
            // 로그인 실패
            alert(data.error || '실패. 다시 작성해보시요!!');
            $("#mfaCode").val('');
        }
    };
</script>