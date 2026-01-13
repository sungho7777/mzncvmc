<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Modal -->
<div class="modal fade" id="loginUserMfaModal" tabindex="-1" role="dialog" aria-labelledby="loginUserMfaModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="loginUserMfaModalLabel">Google TOTP Code Plz</h5>
                <button class="btn-close" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form>
                    <input id="loginUserMfa_username" type="hidden" />

                    <div class="mb-3">

                        <div style="display:flex; gap:16px;">
                            <div class="form-check">
                                <input class="form-check-input" id="mfaCodeRadio" type="radio" name="flexRadioDefault" checked>
                                <label class="form-check-label" for="mfaCodeRadio">TOTP Code</label>
                            </div>

                            <div class="form-check">
                                <input class="form-check-input" id="backupCodeRadio" type="radio" name="flexRadioDefault">
                                <label class="form-check-label" for="backupCodeRadio">Backup Code</label>
                            </div>
                        </div>

                        <input class="form-control mt-2" id="loginUserMfa_mfaCode" type="text" placeholder="Enter current Code" />
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" type="button" data-bs-dismiss="modal">Close</button>
                <button id="verifyLoginUserMfa-btn" class="btn btn-primary" type="button">Mfa Login</button>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">

    document.addEventListener('DOMContentLoaded', function() {
        const verifyLoginUserMfaBtn = document.getElementById('verifyLoginUserMfa-btn');

        verifyLoginUserMfaBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            verifyUserMfa();
        });
    });


    const verifyUserMfa = async() => {
        const username = document.getElementById("loginUserMfa_username").value;
        const mfaCode = document.getElementById("loginUserMfa_mfaCode").value;
        const flexRadioDefault = document.querySelector('input[name="flexRadioDefault"]:checked').id;

        const response = await fetch('/api/auth/verifyUserMfa', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                mfaCode: mfaCode,
                mfaType: flexRadioDefault
            })
        });

        const data = await response.json();

        $("#loginUserMfa_mfaCode").val('');
        if(data.statusLogin == 'success'){
            // 성공
            $("#loginUserMfa_username").val('');
            $('#loginUserMfaModal').modal('hide');

            successLogin(data);
        }else if(data.statusLogin == 'fall'){
            // 실패
            alert(data.message);
        }
    };
</script>