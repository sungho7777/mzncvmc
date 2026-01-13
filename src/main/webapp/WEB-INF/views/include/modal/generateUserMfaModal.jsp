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
<div class="modal fade" id="generateUserMfaModal" tabindex="-1" role="dialog" aria-labelledby="generateUserMfaModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="generateUserMfaModalLabel">Mfa Code Plz</h5>
                <button class="btn-close" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form>
                    <input id="generateUserMfa_username" type="hidden" />

                    <div class="qr-container">
                        <div class="qr-container">
                            <div class="loading" id="qrLoading">QR 코드 생성 중...</div>
                            <img id="qrCode" src="" alt="QR Code" class="qr-blur">
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="small mb-1" for="generateUserMfa_mfaCode">Mfa Code</label>
                        <input class="form-control" id="generateUserMfa_mfaCode" type="text" placeholder="Enter current Mfa Code" />
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-bs-dismiss="modal">Close</button>
                <button id="generateUserMfa-btn" class="btn btn-primary" type="button">generateUserMfa</button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    const generateQRCode = async() => {
        const username = document.getElementById("generateUserMfa_username").value;

        const response = await fetch('/api/auth/generateQR', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username
            })
        });

        const data = await response.json();

        console.log(data);

        if(data.status == 'success'){
            // 성공
            $('#qrLoading').text(data.message);
            $('#qrCode').attr('src', data.data.qrCode).show();
        }else if(data.status == 'fail'){
            // 실패
            alert(data.message);
        }
    };

    document.addEventListener('DOMContentLoaded', function() {
        const generateUserMfaBtn = document.getElementById('generateUserMfa-btn');

        generateUserMfaBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            const mfaCode = $("#generateUserMfa_mfaCode").val();

            if(isValidUserMfa(mfaCode)){
                generateUserMfa();
            }else{
                alert('다시 작성하시요..');
                $("#generateUserMfa_mfaCode").val('');
            }
        });
    });

    const isValidUserMfa = (mfaCode) => {
        // 문자열로 처리 (앞자리 0 보존)
        const mfaRegex = /^\d{6}$/;
        return mfaRegex.test(mfaCode);
    };

    const generateUserMfa = async() => {
        const username = document.getElementById("generateUserMfa_username").value;
        const mfaCode = document.getElementById("generateUserMfa_mfaCode").value;

        const response = await fetch('/api/auth/generateUserMfa', {
        //const response = await fetch('/api/auth/verifyUserMfa', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                mfaCode: mfaCode
            })
        });

        const data = await response.json();

        alert(data.message);
        $("#generateUserMfa_mfaCode").val('');
        if(data.status == 'success'){
            // 성공
            $("#generateUserMfa_username").val('');
            $('#generateUserMfaModal').modal('hide');
        }else if(data.status == 'fail'){
            // 실패
        }
    };
</script>