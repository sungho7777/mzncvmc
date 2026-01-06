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
<body class="bg-primary">
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
                                <a class="btn btn-icon btn-facebook mx-1" href="#!"><i class="fab fa-facebook-f fa-fw fa-sm"></i></a>
                                <a class="btn btn-icon btn-github mx-1" href="#!"><i class="fab fa-github fa-fw fa-sm"></i></a>
                                <a class="btn btn-icon btn-google mx-1" href="#!"><i class="fab fa-google fa-fw fa-sm"></i></a>
                                <a class="btn btn-icon btn-twitter mx-1" href="#!"><i class="fab fa-twitter fa-fw fa-sm text-white"></i></a>
                            </div>
                            <hr class="my-0" />
                            <div class="card-body p-5">
                                <!-- Login form-->
                                <form id="loginForm" action="/api/auth/login" method="post">
                                    <!-- Form Group (email address)-->
                                    <div class="mb-3">
                                        <label class="text-gray-600 small" for="emailExample">Email address</label>
                                        <input id="username" name="username" value=""
                                               class="form-control form-control-solid" type="text" placeholder="" aria-label="Email Address" aria-describedby="emailExample" />
                                    </div>
                                    <!-- Form Group (password)-->
                                    <div class="mb-3">
                                        <label class="text-gray-600 small" for="passwordExample">Password</label>
                                        <input id="password" name="password" value=""
                                               class="form-control form-control-solid" type="password" placeholder="" aria-label="Password" aria-describedby="passwordExample" />
                                    </div>
                                    <!-- Form Group (forgot password link)-->
                                    <div class="mb-3"><a class="small" href="auth-password-social.html">Forgot your password?</a></div>
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
                            <div class="card-body px-5 py-4">
                                <div class="small text-center">
                                    New user?
                                    <a href="#" onclick="resetPassword();">Reset Password an account!</a>
                                </div>
                            </div>
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
                    <div class="col-md-6 small">Copyright &copy; Your Website 2021</div>
                    <div class="col-md-6 text-md-end small">
                        <a href="#!">Privacy Policy</a>
                        &middot;
                        <a href="#!">Terms &amp; Conditions</a>
                    </div>
                </div>
            </div>
        </footer>
    </div>


    <!-- Button trigger modal -->
    <button class="btn btn-primary" type="button" data-bs-toggle="modal" data-bs-target="#userOtpModal">User Otp Modal</button>
    <button class="btn btn-primary" type="button" data-bs-toggle="modal" data-bs-target="#userMfaModal">User Mfa Modal</button>

    <!-- Modal -->
    <%@ include file="include/modal/userOtpModal.jsp" %>
    <%@ include file="include/modal/userMfaModal.jsp" %>




</div>
<script src="/common/sbadminpro/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
<script src="/common/sbadminpro/js/scripts.js"></script>


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
                        'Authorization': 'Bearer ' + token,
                        'Content-Type': 'application/json'
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
        const savedUsername = localStorage.getItem("savedUsername");
        if (savedUsername) {
            document.getElementById("username").value = savedUsername;
            document.getElementById("checkRememberUsername").checked = true;
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

        if (response.ok) {

            if(data.statusLogin == 'sendUserOtpMail') {
                // 모달 띄우기
                document.getElementById("otpUsername").value = username;
                const userOtpModal = new bootstrap.Modal(document.getElementById('userOtpModal'), {
                    backdrop: 'static',
                    keyboard: false
                });
                userOtpModal.show();
            } else if(data.statusLogin == 'generateQRCode'){
                document.getElementById("mfaUsername").value = username;
                const userMfaModal = new bootstrap.Modal(document.getElementById('userMfaModal'), {
                    backdrop: 'static',
                    keyboard: false
                });
                userMfaModal.show();

                // 만약 mfa 가 등록 되어 있다면 mfaCode 입력 값만 받는다.
                // 만약 mfa 가 등록 되어 있지 않다면 QR 코드 이미지를 만들고 mfa_secret 값을 설정 한다.
                generateQRCode();

            }else if(data.statusLogin == 'success'){
                successLogin(data);
            }else{
                alert('fall');
            }
        } else {
            // 로그인 실패
            alert(data.error || '로그인에 실패했습니다.');
        }
    });


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
        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('refreshToken', data.refreshToken);
        localStorage.setItem('userId', data.userId);
        localStorage.setItem('username', data.username);
        localStorage.setItem('pwNotifyDuration', data.pwNotifyDuration);

        // 로그인 아이디 기억하기.
        const checkRememberUsername = document.getElementById("checkRememberUsername").checked;
        if (checkRememberUsername) {
            localStorage.setItem("savedUsername", username);
        } else {
            localStorage.removeItem("savedUsername");
        }

        // 잠시 후 메인화면으로 이동
        setTimeout(() => {
            window.location.href = '/main';
        }, 150);
    };
    /**
     * 패스워드 초기화
     *
     * @returns {boolean}
     */
    const resetPassword = async() => {

        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;

        const response = await fetch('/api/account/resetPassword', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password
            })
        });

        if (response.ok) {
            alert('비밀번호 초기화 성공했습니다.');
        } else {
            const data = await response.json();
            alert(data.error || '비밀번호 초기화 실패했습니다.');
        }
    };
</script>
</body>
</html>
