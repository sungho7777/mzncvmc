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
                                        <input id="username" name="username" value="manager"
                                               class="form-control form-control-solid" type="text" placeholder="" aria-label="Email Address" aria-describedby="emailExample" />
                                    </div>
                                    <!-- Form Group (password)-->
                                    <div class="mb-3">
                                        <label class="text-gray-600 small" for="passwordExample">Password</label>
                                        <input id="password" name="password" value="1212"
                                               class="form-control form-control-solid" type="password" placeholder="" aria-label="Password" aria-describedby="passwordExample" />
                                    </div>
                                    <!-- Form Group (forgot password link)-->
                                    <div class="mb-3"><a class="small" href="auth-password-social.html">Forgot your password?</a></div>
                                    <!-- Form Group (login box)-->
                                    <div class="d-flex align-items-center justify-content-between mb-0">
                                        <div class="form-check">
                                            <input class="form-check-input" id="checkRememberPassword" type="checkbox" value="" />
                                            <label class="form-check-label" for="checkRememberPassword">Remember password</label>
                                        </div>
                                        <button class="btn btn-primary" type="submit">Login</button>
                                    </div>
                                </form>
                            </div>
                            <hr class="my-0" />
                            <div class="card-body px-5 py-4">
                                <div class="small text-center">
                                    New user?
                                    <a href="auth-register-social.html">Create an account!</a>
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
</div>
<script src="/common/sbadminpro/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
<script src="/common/sbadminpro/js/scripts.js"></script>


<script type="text/javascript">
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
                        localStorage.removeItem('accessToken');
                        localStorage.removeItem('refreshToken');
                        localStorage.removeItem('username');
                    }
                }

            } catch (error) {
                console.error('토큰 검증 중 오류:', error);
                // 네트워크 오류 등의 경우 토큰 제거
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                localStorage.removeItem('username');
            }
        }

        console.log('로그인 페이지 유지');

    });

    // 토큰 갱신 함수
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
            // 로그인 성공 - 토큰을 localStorage에 저장
            localStorage.setItem('accessToken', data.accessToken);
            localStorage.setItem('refreshToken', data.refreshToken);
            localStorage.setItem('userId', data.userId);
            localStorage.setItem('username', data.username);

            // 잠시 후 메인화면으로 이동
            setTimeout(() => {
                window.location.href = '/main';
            }, 150);

        } else {
            // 로그인 실패
            alert(data.error || '로그인에 실패했습니다.');
        }
    });
</script>
</body>
</html>
