<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko" data-bs-theme="auto">
<head>
    <meta charset="utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <title>RECOVERY - SB Admin Pro</title>
    <link href="/common/sbadminpro/css/styles.css" rel="stylesheet" />
    <link rel="icon" type="image/x-icon" href="/common/sbadminpro/assets/img/favicon.png" />
    <script data-search-pseudo-elements defer src="/common/sbadminpro/js/all.min.js" crossorigin="anonymous"></script>
    <script src="/common/sbadminpro/js/feather.min.js" crossorigin="anonymous"></script>

    <script src="/common/sbadminpro/js/jquery-3.7.1.min.js"></script>

</head>
<body class="bg-orange">
<div id="layoutAuthentication">
    <div id="layoutAuthentication_content">
        <main>
            <div class="container-xl px-4">
                <div class="row justify-content-center">
                    <div class="col-lg-5">
                        <!-- Basic forgot password form-->
                        <div class="card shadow-lg border-0 rounded-lg mt-5">
                            <div class="card-header justify-content-center"><h3 class="fw-light my-4">Password Recovery</h3></div>
                            <div class="card-body">
                                <div class="small mb-3 text-muted">Enter your email address and we will send you a code to reset your password.</div>
                                <!-- Forgot password form-->
                                <form>
                                    <!-- Form Group (email address)-->
                                    <div class="mb-3">
                                        <label class="small mb-1" for="username">User Name</label>
                                        <input class="form-control" id="username" type="email" aria-describedby="usernameHelp" placeholder="Enter user name" />
                                    </div>
                                    <div class="mb-3">
                                        <label class="small mb-1" for="email">Email</label>
                                        <input class="form-control" id="email" type="email" aria-describedby="emailHelp" placeholder="Enter email address" />
                                    </div>
                                    <!-- Form Group (submit options)-->
                                    <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                                        <a class="small" href="/login">Return to login</a>
                                        <button id="RecoveryPassword-btn" class="btn btn-primary" type="button">Recovery Password</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>


        </main>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="recoveryCodeModal" tabindex="-1" role="dialog" aria-labelledby="recoveryCodeModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="recoveryCodeModalLabel">Enter the password Recovery code. Your password will be reset.</h5>
                    <button class="btn-close" type="button" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form>

                        <div class="mb-3">
                            <label class="small mb-1" for="recoveryCode">Recovery Code</label>
                            <input class="form-control" id="recoveryCode" type="text" placeholder="Enter Recovery Code" />
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-secondary" type="button" data-bs-dismiss="modal">Close</button>
                    <button id="ResetPassword-btn" class="btn btn-primary" type="button">Reset Password</button>
                </div>
            </div>
        </div>
    </div>

    <div id="layoutAuthentication_footer">
        <footer class="footer-admin mt-auto footer-dark">
            <div class="container-xl px-4">
                <div class="row">
                    <div class="col-md-6 small">Copyright &copy; Your Website 2026</div>
                    <div class="col-md-6 text-md-end small">
                        <a href="#!">Privacy Policy</a>
                        &middot;
                        <a href="#!">Terms &amp; Conditions</a>
                    </div>
                </div>
            </div>
        </footer>
    </div>

    <%@ include file="include/modal/loginUserOtpModal.jsp" %>
    <%@ include file="include/modal/loginUserMfaModal.jsp" %>
</div>
<script src="/common/sbadminpro/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
<script src="/common/sbadminpro/js/scripts.js"></script>

<script type="text/javascript">
    document.getElementById("username").focus();

    document.addEventListener('DOMContentLoaded', function() {
        const RecoveryPasswordBtn = document.getElementById('RecoveryPassword-btn');
        const ResetPasswordBtn = document.getElementById('ResetPassword-btn');

        // 비밀번호 복구 이메일 보냄.
        RecoveryPasswordBtn.addEventListener('click', function(e) {
            //e.preventDefault(); // 기본 링크 동작을 막음

            recoveryPassword();


        });
        // 복구 코드 검증 요청.
        ResetPasswordBtn.addEventListener('click', function(e) {
            //e.preventDefault(); // 기본 링크 동작을 막음

            resetPassword();
        });
    });

    /**
     * 복구코드 이메일 발송
     *
     * @returns {boolean}
     */
    const recoveryPassword = async() => {

        const username = $("#username").val();
        const email = $("#email").val();
        //const recoveryCode = $("#recoveryCode").val();

        const response = await fetch('/api/account/recoveryPassword', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                email: email
            })
        });

        const data = await response.json();

        console.log(data);

        if(data.status == 'success'){
            const _modal = new bootstrap.Modal(document.getElementById('recoveryCodeModal'), {
                backdrop: 'static',
                keyboard: false
            });
            _modal.show();

        }else if(data.status == 'fail'){
            // 실패
            alert(data.message);
        }
    };

    /**
     * 복구 코드 검증 요청 및 패스워드 초기화
     *
     * @returns {boolean}
     */
    const resetPassword = async() => {

        const username = $("#username").val();
        const email = $("#email").val();
        const recoveryCode = $("#recoveryCode").val();

        const response = await fetch('/api/account/resetPassword', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                email: email,
                recoveryCode: recoveryCode
            })
        });

        const data = await response.json();

        alert(data.message);
        if(data.status == 'success'){
            // 잠시 후 로그인 화면으로 이동
            setTimeout(() => {
                window.location.href = '/login';
            }, 150);
        }else if(data.status == 'fail'){
            // 실패
        }
    };
</script>