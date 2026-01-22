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
                <h5 class="modal-title" id="generateUserMfaModalLabel">Mfa TOTP Code.</h5>
                <button class="btn-close" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form>
                    <input id="generateUserMfa_username" type="hidden" />

                    <div class="qr-container">
                        <div id="qrContainer" class="qr-container" style="display: none;">
                            <div class="loading" id="qrLoading">.</div>
                            <img id="qrCode" src="" alt="QR Code" class="qr-blur">
                        </div>
                        <div id="storeQrContainer" class="qr-container">
                            <div class="loading" id="storeQrLoading">.</div>
                            <img id="storeQrCode" src="/api/auth/authenticatorStoreQr" alt="QR Code" class="qr-blur">
                        </div>
                    </div>


                    <div class="mb-3" style="text-align: center;">
                        <%--<label class="small mb-1" for="generateUserMfa_mfaCode">Mfa Code</label>--%>
                        <input class="form-control" id="generateUserMfa_mfaCode" type="text" placeholder="Enter current Mfa TOTP Code" />
                    </div>
                </form>
                <div class="mb-3">
                    <button class="btn btn-red btn-xs" onclick="authenticatorQr('AG');">Android Google</button>
                    <button class="btn btn-green btn-xs" onclick="authenticatorQr('AM');">Android Microsoft</button>
                    <button class="btn btn-blue btn-xs" onclick="authenticatorQr('IG');">ios Google</button>
                    <button class="btn btn-yellow btn-xs" onclick="authenticatorQr('IM');">ios Microsoft</button>
                    <button class="btn btn-cyan btn-xs" onclick="authenticatorQr('TC');">TOTP</button>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-bs-dismiss="modal">Close</button>
                <button id="generateUserMfa-btn" class="btn btn-primary" type="button">verify TOTP Code</button>
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
            $("#qrContainer").hide();
            $("#storeQrContainer").show();
            $('#qrLoading').text(data.message);
            $('#qrCode').attr('src', data.data.qrCode).show();
        }else if(data.status == 'fail'){
            // 실패
            alert(data.message);
        }

        authenticatorQr('AG');
    };

    const authenticatorQr = (appType) => {
        // Install the Android Google Authenticator app using the QR code.      -- AG (Android Google)
        // Install the iOS Google Authenticator app using the QR code.          -- IG (iOS Google)
        // Install the Android Microsoft Authenticator app using the QR code.   -- AM (Android Microsoft)
        // Install the Microsoft Authenticator app for iOS using the QR code.   -- IM (iOS Microsoft)
        $("#generateUserMfa_mfaCode").prop("disabled", true);
        $("#generateUserMfa_mfaCode").val("");
        if(appType == 'TC') {
            $("#qrContainer").show();
            $("#storeQrContainer").hide();
            $("#generateUserMfa_mfaCode").prop("disabled", false);
        }else{
            const storeQrMessage = "Install the Authenticator app using the QR code.";
            $("#qrContainer").hide();
            $("#storeQrContainer").show();
            $('#storeQrLoading').text(appType + " : " + storeQrMessage);
        }
    }
    document.addEventListener('DOMContentLoaded', function() {
        const generateUserMfaBtn = document.getElementById('generateUserMfa-btn');

        generateUserMfaBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            const mfaCode = $("#generateUserMfa_mfaCode").val();

            if(isValidUserMfa(mfaCode)){
                verifyAndEnableMFA();
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

    const verifyAndEnableMFA = async() => {
        const username = document.getElementById("generateUserMfa_username").value;
        const mfaCode = document.getElementById("generateUserMfa_mfaCode").value;

        const response = await fetch('/api/auth/verifyAndEnableMFA', {
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