<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko" data-bs-theme="auto">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>SB Admin 2 - Login</title>
    <!-- Custom fonts for this template-->
    <link href="/common/sbadmin/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
    <!-- Custom styles for this template-->
    <link href="/common/sbadmin/css/sb-admin-2.min.css" rel="stylesheet">
</head>

<body class="bg-gradient-primary">

<div class="container">

    <!-- Outer Row -->
    <div class="row justify-content-center">

        <div class="col-xl-10 col-lg-12 col-md-9">

            <div class="card o-hidden border-0 shadow-lg my-5">
                <div class="card-body p-0">
                    <!-- Nested Row within Card Body -->
                    <div class="row">
                        <div class="col-lg-6 d-none d-lg-block bg-login-image"></div>
                        <div class="col-lg-6">
                            <div class="p-5">
                                <div class="text-center">
                                    <h1 class="h4 text-gray-900 mb-4">Welcome Back!</h1>
                                </div>
                                <form id="loginForm" class="user" action="/api/auth/login" method="post">
                                    <div class="form-group">
                                        <input type="text" class="form-control form-control-user"
                                               id="username" name="username" value="manager" aria-describedby="emailHelp"
                                               placeholder="Enter Email Address...">
                                    </div>
                                    <div class="form-group">
                                        <input type="password" class="form-control form-control-user"
                                               id="password" name="password" value="1212" placeholder="Password">
                                    </div>
                                    <div class="form-group">
                                        <div class="custom-control custom-checkbox small">
                                            <input type="checkbox" class="custom-control-input" id="customCheck">
                                            <label class="custom-control-label" for="customCheck">Remember
                                                Me</label>
                                        </div>
                                    </div>
                                    <button class="btn btn-primary w-100 py-2" type="submit">
                                        Login
                                    </button>
                                    <hr>
                                    <button class="btn btn-google w-100 py-2">
                                        <i class="fab fa-google fa-fw"></i> Login with Google
                                    </button>
                                    <button class="btn btn-facebook w-100 py-2">
                                        <i class="fab fa-facebook fa-fw"></i> Login with facebook
                                    </button>
                                </form>
                                <hr>
                                <div class="text-center">
                                    <a class="small" href="forgot-password.html">Forgot Password?</a>
                                </div>
                                <div class="text-center">
                                    <a class="small" href="register.html">Create an Account!</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>

    </div>

</div>

<!-- Bootstrap core JavaScript-->
<script src="/common/sbadmin/vendor/jquery/jquery.min.js"></script>
<script src="/common/sbadmin/vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

<!-- Core plugin JavaScript-->
<script src="/common/sbadmin/vendor/jquery-easing/jquery.easing.min.js"></script>

<!-- Custom scripts for all pages-->
<script src="/common/sbadmin/js/sb-admin-2.min.js"></script>

</body>

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
</html>
