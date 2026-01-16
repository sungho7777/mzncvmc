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
    <title>Login - SB Admin Pro</title>
    <link href="/common/sbadminpro/css/styles.css" rel="stylesheet" />
    <link rel="icon" type="image/x-icon" href="/common/sbadminpro/assets/img/favicon.png" />
    <script data-search-pseudo-elements defer src="/common/sbadminpro/js/all.min.js" crossorigin="anonymous"></script>
    <script src="/common/sbadminpro/js/feather.min.js" crossorigin="anonymous"></script>

    <script src="/common/sbadminpro/js/jquery-3.7.1.min.js"></script>

</head>
<style>

</style>
<body class="bg-cyan">
<div id="layoutAuthentication">
    <div id="layoutAuthentication_content">
        <main>
            <div class="container-xl px-4">
                <div class="row justify-content-center">
                    <div class="col-xl-5 col-lg-6 col-md-8 col-sm-11">
                        <!-- Social login form-->
                        <div class="card my-5">
                            <div class="card-body p-5 text-center">
                                <div class="h3 fw-light mb-3">Sign In</div>



                                <!-- Social login links-->
                                <button class="btn btn-red btn-icon" type="button" onclick="loginWithGoogle();">G </button>
                                <button class="btn btn-yellow btn-icon" type="button" onclick="loginWithKakao();">K</button>
                                <button class="btn btn-green btn-icon" type="button" onclick="loginWithNaver();">N</button>
                                <button class="btn btn-purple btn-icon" type="button" onclick="loginWithGithub();">G</button>
                            </div>
                            <hr class="my-0" />
                            <div class="card-body p-5">
                                <!-- Login form-->
                                <form id="loginForm" action="/api/auth/login" method="post">
                                    <!-- Form Group (email address)-->
                                    <div class="mb-3">
                                        <label class="text-gray-600 small" for="username">User Name</label>
                                        <input id="username" name="username" value=""
                                               class="form-control form-control-solid" type="text" placeholder="" aria-label="User Name" aria-describedby="username" />
                                    </div>
                                    <!-- Form Group (password)-->
                                    <div class="mb-3">
                                        <label class="text-gray-600 small" for="passwordExample">Password</label>
                                        <input id="password" name="password" value=""
                                               class="form-control form-control-solid" type="password" placeholder="" aria-label="Password" aria-describedby="password" />
                                    </div>
                                    <!-- Form Group (forgot password link)-->
                                    <div class="mb-3"><a class="small" href="/recovery">Forgot your password?</a></div>
                                    <!-- Form Group (login box)-->
                                    <div class="d-flex align-items-center justify-content-between mb-0">
                                        <div class="form-check">
                                            <input class="form-check-input" id="checkRememberUsername" type="checkbox" value="" />
                                            <label class="form-check-label" for="checkRememberUsername">Remember Username</label>
                                        </div>
                                        <button class="btn btn-primary" type="submit">Login</button>
                                    </div>
                                </form>
                            </div>
                            <hr class="my-0" />
                            <%--
                            <div class="card-body px-5 py-4">
                                <div class="small text-center">
                                    New user?
                                    <a href="#" onclick="resetPassword();">Reset Password an account!</a>
                                </div>
                            </div>
                            --%>
                        </div>
                    </div>
                </div>
            </div>
        </main>
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

    <!-- Modal -->
    <%@ include file="include/modal/generateUserOtpModal.jsp" %>
    <%@ include file="include/modal/generateUserMfaModal.jsp" %>

    <%@ include file="include/modal/loginUserOtpModal.jsp" %>
    <%@ include file="include/modal/loginUserMfaModal.jsp" %>
</div>
<script src="/common/sbadminpro/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
<script src="/common/sbadminpro/js/scripts.js"></script>

<!-- common.script -->
<script src="/common/sbadmin/js/domain/valided.js"></script>

<script type="text/javascript">
    document.getElementById("username").focus();
    window.addEventListener('load', async function() {

        const token = localStorage.getItem('accessToken');

        if (token) {
            console.log(token);
            console.log('토큰이 존재함, 유효성 검증 후 main 페이지로 이동');

            try {
                // 토큰 유효성 검증을 위해 보호된 API 호출
                const response = await fetch('/api/user/authorities', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                        , 'Authorization': 'Bearer ' + token
                    }
                });

                if (response.ok) {
                    console.log('유효한 토큰, 메인 페이지로 리다이렉트');

                    // 사용자에게 알림 (선택사항)
                    const username = localStorage.getItem('username');
                    if (username) {
                        console.log(`${username}님, 이미 로그인되어 있습니다.`);
                    }

                    // 메인 페이지로 리다이렉트
                    window.location.href = '/main';
                    return; // 함수 종료

                } else if (response.status === 401) {
                    console.log('토큰이 만료됨, 갱신 시도');

                    // 토큰 갱신 시도
                    const refreshSuccess = await tryRefreshToken();
                    if (refreshSuccess) {
                        console.log('토큰 갱신 성공, 메인 페이지로 리다이렉트');
                        window.location.href = '/main';
                        return;
                    } else {
                        console.log('토큰 갱신 실패, 로컬 스토리지 정리');
                        // 갱신 실패 시 토큰 정리
                        removeLocalStorage();
                    }
                }

            } catch (error) {
                console.error('토큰 검증 중 오류:', error);
                // 네트워크 오류 등의 경우 토큰 제거
                removeLocalStorage();
            }
        }

        console.log('로그인 페이지 유지');

        // 로그인 아이디 기억하기.
        const rememberUsername = localStorage.getItem('rememberUsername');
        if (rememberUsername.length > 0) {
            $('#username').val(rememberUsername);
            $('#checkRememberUsername').prop('checked',true);
            document.getElementById("password").focus();
        }
    });

    /**
     * localStorage 초기화
     *
     * @returns {boolean}
     */
    const removeLocalStorage = () =>{
        // 네트워크 오류 등의 경우 토큰 제거
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('userId');
        localStorage.removeItem('username');
        localStorage.removeItem('pwNotifyDuration');
    };

    /**
     * 토큰 갱신 함수
     *
     * @returns {boolean}
     */
    async function tryRefreshToken() {
        const refreshToken = localStorage.getItem('refreshToken');

        if (!refreshToken) {
            console.log('리프레시 토큰이 없음');
            return false;
        }

        try {
            const response = await fetch('/api/auth/refresh', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    refreshToken: refreshToken
                })
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('accessToken', data.accessToken);
                console.log('토큰 갱신 성공');
                return true;
            } else {
                console.log('토큰 갱신 실패:', response.status);
                return false;
            }
        } catch (error) {
            console.error('토큰 갱신 중 오류:', error);
            return false;
        }
    }

    /**
     * 로그인 시도
     *
     * @returns {boolean}
     */
    document.getElementById("loginForm").addEventListener("submit", async (e) => {
        e.preventDefault();

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        //$('#loading').show();
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        const data = await response.json();
        if(response.ok){

            //$('#loading').hide();
            if(data.statusLogin == 'success') {
                // 무조건 로그인.
                successLogin(data);
            }else if(data.statusLogin == 'fall'){
                // 실패
                alert(data.message);
            }else{

                if(data.statusLogin == 'generateUserOtp'){
                    // false → MFA 비활성화 또는 설정 정보 없음 -> 설정 시작(이메일 OTP, 구글 TOTP)
                    // 모달 띄우기
                    generateUserOtpModal();
                }else if(data.statusLogin == 'loginUserOtp'){
                    // 메일을 통한 OTP 로그인 모달 띄우기.
                    loginUserOtpModal();
                }else if(data.statusLogin == 'loginUserMfa'){
                    // 구글 TOTP을 통한 OTP 로그인 모달 띄우기.
                    loginUserMfaModal();
                }
            }
        }else{
            alert(data.error);
        }

        $("#password").val("");
        $("#password").focus();
    });

    // MFA 설정을 위한 메일 OTP 입력 모달 띄우기
    const generateUserOtpModal = () => {
        const username = document.getElementById("username").value;

        document.getElementById("generateUserOtp_username").value = username;
        const _modal = new bootstrap.Modal(document.getElementById('generateUserOtpModal'), {
            backdrop: 'static',
            keyboard: false
        });
        _modal.show();
    };

    // MFA 설정을 위한 구글 TOTP 입력 모달 띄우기
    const generateUserMfaModal = () => {
        const username = document.getElementById("username").value;

        document.getElementById("generateUserMfa_username").value = username;
        const _modal = new bootstrap.Modal(document.getElementById('generateUserMfaModal'), {
            backdrop: 'static',
            keyboard: false
        });
        _modal.show();

        generateQRCode();
    }
    // 메일 OTP 로그인 모달 띄우기.
    const loginUserOtpModal = () => {
        const username = document.getElementById("username").value;

        document.getElementById("loginUserOtp_username").value = username;
        const _modal = new bootstrap.Modal(document.getElementById('loginUserOtpModal'), {
            backdrop: 'static',
            keyboard: false
        });
        _modal.show();
    };
    // 구글 TOTP을 통한 OTP 로그인 모달 띄우기.
    const loginUserMfaModal = () => {
        const username = document.getElementById("username").value;

        document.getElementById("loginUserMfa_username").value = username;
        const _modal = new bootstrap.Modal(document.getElementById('loginUserMfaModal'), {
            backdrop: 'static',
            keyboard: false
        });
        _modal.show();
    };

    /**
     * 로그인 처리
     *
     * @returns {boolean}
     */
    const successLogin = (data) => {
        if(data == null){
            alert('잘못된 접근입니다.');
            return;
        }

        // 로그인 성공 - 토큰을 localStorage에 저장
        localStorage.setItem('rememberUsername', $('#checkRememberUsername').is(':checked') ? data.username : "");
        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('refreshToken', data.refreshToken);
        localStorage.setItem('userId', data.userId);
        localStorage.setItem('username', data.username);
        localStorage.setItem('pwNotifyDuration', data.pwNotifyDuration);

        // 잠시 후 메인화면으로 이동
        setTimeout(() => {
            //alert('login 성공');
            window.location.href = '/main';
        }, 150);
    };
    const loginWithGoogle = () => {
        console.log('loginWithGoogle');
        // /api/auth/login
        window.location.href = '/api/oauth/google';
    };
    const loginWithKakao = () => {
        console.log('loginWithGoogle');
    };
    const loginWithNaver = () => {
        console.log('loginWithGoogle');
    };
    const loginWithGithub = () => {
        console.log('loginWithGoogle');
    };
</script>
</body>
</html>
