<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
            <header class="py-3 mb-3 border-bottom">
                <div class="container-fluid d-grid gap-3 align-items-center" style="grid-template-columns: 1fr 2fr;">
                <div class="dropdown">
                    <a href="#" class="d-flex align-items-center col-lg-4 mb-2 mb-lg-0 link-body-emphasis text-decoration-none dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false" aria-label="Bootstrap menu">
                        <img src="/common/icons/wikipedia.svg" alt="Wikipedia" width="32" height="32">
                    </a>
                    <ul class="dropdown-menu text-small shadow">
                        <li>
                            <a class="dropdown-item active" href="/main" aria-current="page">Main v2</a>
                        </li>
                        <li>
                            <a class="dropdown-item" href="/neo/list" aria-current="page">NEOSTRINGINFO</a>
                        </li>
                        <li>
                            <a class="dropdown-item" href="#">Inventory</a>
                        </li>
                        <li>
                            <a class="dropdown-item" href="#">Customers</a>
                        </li>
                        <li>
                            <a class="dropdown-item" href="#">Products</a>
                        </li>
                        <li>
                            <hr class="dropdown-divider">
                        </li>
                        <li>
                            <a class="dropdown-item" href="#">Reports</a>
                        </li>
                        <li>
                            <a class="dropdown-item" href="/bootstrap/dashboard">Bootstrap</a>
                        </li>
                    </ul>
                </div>
                <div class="d-flex align-items-center">
                    <form class="w-100 me-3" role="search">
                        <input type="search" class="form-control" placeholder="Search..." aria-label="Search">
                    </form>
                    <div class="flex-shrink-0 dropdown">
                        <a href="#" class="d-block link-body-emphasis text-decoration-none dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                            <img src="/common/icons/person-workspace.svg" alt="person-workspace" width="32" height="32">
                        </a>
                        <ul class="dropdown-menu text-small shadow">
                            <li>
                                <a class="dropdown-item" href="/system/project">New project...</a>
                            </li>
                            <li>
                                <a class="dropdown-item" href="/system/settings">Settings</a>
                            </li>
                            <li>
                                <a class="dropdown-item" href="/user/profile">Profile</a>
                            </li>
                            <li>
                                <a class="dropdown-item" href="/system/space">Space</a>
                            </li>
                            <li>
                                <hr class="dropdown-divider">
                            </li>
                            <li>
                                <a class="dropdown-item" href="#" id="logout-btn">Sign out</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </header>
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