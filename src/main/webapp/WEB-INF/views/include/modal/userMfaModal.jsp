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
.qr-blur {
    filter: blur(10px);
    opacity: 0.4;
    pointer-events: none; /* 스캔 방지 보조 */
    transition: filter 0.2s ease, opacity 0.2s ease;
}

.qr-clear {
    filter: blur(0);
    opacity: 1;
    pointer-events: auto;
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
                        <div class="form-check mb-2">
                            <input class="form-check-input" type="checkbox" id="revealQrCheckbox">
                            <label class="form-check-label" for="revealQrCheckbox">
                                QR Code 표시
                            </label>
                        </div>

                        <div class="qr-container">
                            <div class="loading" id="qrLoading">QR 코드 생성 중...</div>
                            <img id="qrCode" src="" alt="QR Code" class="qr-blur">
                        </div>
                    </div>
                    <div class="mb-3">
                        <div style="display:flex; gap:16px;">
                            <div class="form-check">
                                <input class="form-check-input" id="mfaCodeRadio" type="radio" name="flexRadioDefault" checked>
                                <label class="form-check-label" for="mfaCodeRadio">Mfa Code</label>
                            </div>

                            <div class="form-check">
                                <input class="form-check-input" id="backupCodeRadio" type="radio" name="flexRadioDefault">
                                <label class="form-check-label" for="backupCodeRadio">Backup Code</label>
                            </div>
                        </div>

                        <input class="form-control mt-2" id="mfaCode" type="text" placeholder="Enter current Code" />
                        <%--
                        <input class="form-control mt-2" id="backupCode" type="text"
                               placeholder="Enter current Backup Code" style="display:none;" />
                        --%>
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

    document.addEventListener('DOMContentLoaded', function () {
        const checkbox = document.getElementById('revealQrCheckbox');
        const qrImage = document.getElementById('qrCode');

        checkbox.addEventListener('change', function () {
            if (this.checked) {
                qrImage.classList.remove('qr-blur');
                qrImage.classList.add('qr-clear');
            } else {
                qrImage.classList.remove('qr-clear');
                qrImage.classList.add('qr-blur');
            }
        });
    });
    document.addEventListener('DOMContentLoaded', function () {
        /*
        const mfaRadio = document.getElementById('mfaCodeRadio');
        const backupRadio = document.getElementById('backupCodeRadio');
        const mfaInput = document.getElementById('mfaCode');
        const backupInput = document.getElementById('backupCode');

        function toggleInputs() {
            if (mfaRadio.checked) {
                mfaInput.style.display = 'block';
                backupInput.style.display = 'none';
                backupInput.value = '';
            } else {
                mfaInput.style.display = 'none';
                backupInput.style.display = 'block';
                mfaInput.value = '';
            }
        }

        mfaRadio.addEventListener('change', toggleInputs);
        backupRadio.addEventListener('change', toggleInputs);
        */
    });
    document.addEventListener('DOMContentLoaded', function() {
        const verifyUserMfaBtn = document.getElementById('verifyUserMfa-btn');

        verifyUserMfaBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            //const mfaCode = $("#mfaCode").val();
            verifyUserMfa();


            /*
            if(isValidUserMfa(mfaCode)){
                verifyUserMfa();
            }else{
                alert('다시 작성하시요..');
                $("#mfaCode").val('');
            }
            */
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
        const flexRadioDefault = document.querySelector('input[name="flexRadioDefault"]:checked').id;

        const response = await fetch('/api/auth/verifyUserMfa', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: mfaUsername,
                mfaCode: mfaCode,
                mfaType: flexRadioDefault
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
                alert(data.message);
            }
        } else {
            // 로그인 실패
            alert(data.error || '실패. 다시 작성해보시요!!');
            $("#mfaCode").val('');
        }
    };
</script>