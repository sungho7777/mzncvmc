<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko" data-bs-theme="auto">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>대시보드</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background-color: #f8f9fa;
            color: #333;
        }

        .navbar {
            background: white;
            padding: 1rem 2rem;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .navbar h1 {
            color: #667eea;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 1rem;
        }

        .logout-btn {
            background: #dc3545;
            color: white;
            border: none;
            padding: 0.5rem 1rem;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .logout-btn:hover {
            background: #c82333;
        }

        .container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 2rem;
        }

        .dashboard-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 2rem;
            margin-bottom: 2rem;
        }

        .card {
            background: white;
            padding: 2rem;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }

        .card h3 {
            margin-bottom: 1rem;
            color: #333;
        }

        .api-test {
            margin-top: 2rem;
        }

        .btn {
            background: #667eea;
            color: white;
            border: none;
            padding: 0.75rem 1.5rem;
            border-radius: 5px;
            cursor: pointer;
            margin: 0.5rem 0.5rem 0.5rem 0;
            transition: background-color 0.3s;
        }

        .btn:hover {
            background: #5a67d8;
        }

        .btn.secondary {
            background: #6c757d;
        }

        .btn.secondary:hover {
            background: #5a6268;
        }

        .response-area {
            background: #f8f9fa;
            padding: 1rem;
            border-radius: 5px;
            margin-top: 1rem;
            font-family: 'Courier New', monospace;
            font-size: 0.9rem;
            max-height: 300px;
            overflow-y: auto;
        }


        .user-profile {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .user-profile h3 {
            color: white;
        }

        @media (max-width: 768px) {
            .navbar {
                flex-direction: column;
                gap: 1rem;
            }

            .container {
                padding: 0 1rem;
            }

            .dashboard-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
<nav class="navbar">
    <h1>대시보드</h1>
    <div class="user-info">
        <span id="welcomeMessage">환영합니다!</span>
        <button class="logout-btn" onclick="logout()">로그아웃</button>
    </div>
</nav>

<div class="container">
    <div class="dashboard-grid">
        <div class="card user-profile">
            <h3>사용자 정보</h3>
            <div id="userProfile">
                <p>로딩 중...</p>
            </div>
        </div>

        <div class="card">
            <h3>빠른 작업</h3>
            <p>여기에 주요 기능들을 배치할 수 있습니다.</p>
            <button class="btn" onclick="refreshToken()">토큰 갱신</button>
        </div>
    </div>

    <div class="card api-test">
        <h3>API 테스트</h3>
        <p>다양한 API 엔드포인트를 테스트해보세요.</p>

        <button class="btn" onclick="testPublicAPI()">공개 API 테스트</button>
        <button class="btn" onclick="testPrivateAPI()">보호된 API 테스트</button>
        <button class="btn secondary" onclick="testInvalidAPI()">인증 실패 테스트</button>

        <div class="response-area" id="apiResponse">
            API 응답이 여기에 표시됩니다.
        </div>
    </div>
</div>

<script>
    // 페이지 로드 시 인증 확인 및 사용자 정보 로드
    window.addEventListener('load', async function() {
        const token = localStorage.getItem('accessToken');
        const username = localStorage.getItem('username');
        console.log(token);
        console.log(username);
        if (!token) {
            alert('로그인이 필요합니다.');
            window.location.href = '/login';
            return;
        }

        // 토큰 유효성 검증
        try {
            const response = await fetch('/api/user/authorities', {
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            });

            if (!response.ok) {
                throw new Error('토큰 유효하지 않음');
            }

            // 환영 메시지 설정
            if (username) {
                document.getElementById('welcomeMessage').textContent = "환영합니다, " + (username) + "님!";
            }

            // 사용자 프로필 정보 로드
            await loadUserProfile();

        } catch (error) {
            console.error('인증 확인 오류:', error);
            alert('인증이 만료되었습니다. 다시 로그인해주세요.');
            localStorage.clear();
            window.location.href = '/login';
        }
    });

    async function loadUserProfile() {
        try {
            const response = await fetchWithAuth('/api/user/authorities');

            if (response.ok) {
                const data = await response.json();
                const innerHtml = "<p><strong>사용자명 : </strong>" + data.username + "</p><p><strong>권한 : </strong>" + data.authorities + "</p><p><strong>상태 : </strong>활성</p>" ;

                document.getElementById('userProfile').innerHTML = innerHtml;
            } else {
                throw new Error('프로필 로드 실패');
            }
        } catch (error) {
            console.error('프로필 로드 오류:', error);
            document.getElementById('userProfile').innerHTML = '<p>프로필 로드 실패</p>';

            // 토큰이 만료되었을 가능성이 있으므로 로그인 페이지로 이동
            if (error.message.includes('401')) {
                localStorage.clear();
                window.location.href = '/login';
            }
        }
    }

    async function fetchWithAuth(url, options = {}) {
        const token = localStorage.getItem('accessToken');

        const defaultOptions = {
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json',
                ...options.headers
            }
        };

        const response = await fetch(url, {...options, ...defaultOptions});

        // 토큰이 만료된 경우 갱신 시도
        if (response.status === 401) {
            const refreshSuccess = await tryRefreshToken();
            if (refreshSuccess) {
                // 새 토큰으로 다시 시도
                const newToken = localStorage.getItem('accessToken');
                defaultOptions.headers['Authorization'] = 'Bearer ' + newToken;
                return await fetch(url, {...options, ...defaultOptions});
            } else {
                // 갱신 실패 시 로그인 페이지로 이동
                localStorage.clear();
                window.location.href = '/login';
                throw new Error('인증 실패');
            }
        }

        return response;
    }

    async function tryRefreshToken() {
        const refreshToken = localStorage.getItem('refreshToken');

        if (!refreshToken) {
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
                //localStorage.setItem('accessToken', data.accessToken);
                return true;
            } else {
                return false;
            }
        } catch (error) {
            console.error('토큰 갱신 오류:', error);
            return false;
        }
    }

    async function refreshToken() {
        const success = await tryRefreshToken();

        if (success) {
            alert('토큰이 성공적으로 갱신되었습니다.', 'success');
        } else {
            alert('토큰 갱신에 실패했습니다.', 'error');
        }
    }

    async function testPublicAPI() {
        try {
            const response = await fetch('/api/public/hello');
            const data = await response.json();
            displayAPIResponse('공개 API', response.status, data);
        } catch (error) {
            displayAPIResponse('공개 API', 'ERROR', {error: error.message});
        }
    }

    async function testPrivateAPI() {
        try {
            const response = await fetchWithAuth('/api/private/hello');
            const data = await response.json();
            displayAPIResponse('보호된 API', response.status, data);
        } catch (error) {
            displayAPIResponse('보호된 API', 'ERROR', {error: error.message});
        }
    }

    async function testInvalidAPI() {
        try {
            const response = await fetch('/api/private/hello', {
                headers: {
                    'Authorization': 'Bearer invalid_token'
                }
            });
            const data = await response.json();
            displayAPIResponse('잘못된 토큰 테스트', response.status, data);
        } catch (error) {
            displayAPIResponse('잘못된 토큰 테스트', 'ERROR', {error: error.message});
        }
    }

    function displayAPIResponse(testName, status, data) {
        const responseArea = document.getElementById('apiResponse');
        const timestamp = new Date().toLocaleTimeString();

        const innerHtml = "<strong>" + timestamp + " " + testName + "</strong><br><strong>상태:</strong>" + status + "<br><strong>응답:</strong><br>" + JSON.stringify(data, null, 2);

        responseArea.innerHTML = innerHtml;
    }

    function logout() {
        if (confirm('정말 로그아웃 하시겠습니까?')) {
            // 로컬 스토리지에서 토큰 제거
            localStorage.removeItem('accessToken');
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('username');

            // 서버에 로그아웃 요청 (선택사항)
            fetch('/api/auth/logout', {
                method: 'POST'
            }).finally(() => {
                window.location.href = '/login';
            });
        }
    }

</script>
</body>
</html>