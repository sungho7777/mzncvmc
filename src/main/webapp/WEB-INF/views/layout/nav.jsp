<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<nav class="navbar navbar-expand-lg fixed-top navbar-dark bg-dark" aria-label="Main navigation" >
    <div class="container-fluid">
        <a class="navbar-brand" href="#">Offcanvas</a>
        <button
                class="navbar-toggler p-0 border-0"
                type="button"
                id="navbarSideCollapse"
                aria-label="Toggle navigation"
        >
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="navbar-collapse offcanvas-collapse" id="navbarsExampleDefault">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="/main">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/neo/list">NStringInfo</a>
                </li>
                <li class="nav-item"><a class="nav-link" href="#">Profile</a></li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Switch account</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown" aria-expanded="false" >Account</a>
                    <ul class="dropdown-menu">
                        <li><a class="dropdown-item" href="/system/project">New project...</a></li>
                        <li><a class="dropdown-item" href="/system/settings">Settings</a></li>
                        <li><a class="dropdown-item" href="/user/profile">Profile</a></li>
                        <li><a class="dropdown-item" href="/system/space">Space</a>
                        <li><a class="dropdown-item" href="#" id="logout-btn">Sign out</a>
                    </ul>
                </li>
            </ul>


            <form class="d-flex" role="search">
                <input
                        class="form-control me-2"
                        type="search"
                        placeholder="Search"
                        aria-label="Search"
                />
                <button class="btn btn-outline-success" type="submit">
                    Search
                </button>
            </form>


        </div>
    </div>
</nav>

<script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function() {
        const logoutBtn = document.getElementById('logout-btn');

        logoutBtn.addEventListener('click', function(e) {
            e.preventDefault(); // 기본 링크 동작을 막음

            fetch('/logout', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json' // 서버가 JSON을 예상할 경우
                }
            })
                .then(response => {
                    if (response.ok) {
                        // 상태 코드 200-299
                        alert('로그아웃되었습니다.');
                        window.location.href = '/login';
                    } else {
                        // 다른 상태 코드
                        throw new Error('로그아웃 실패');
                    }
                })
                .catch(error => {
                    console.error('로그아웃 실패:', error);
                    alert('로그아웃에 실패했습니다. 다시 시도해 주세요.');
                });
        });
    });
</script>