// menu.js
window.menu = {
  init() {
    //this.render();
    //this.bind();
  },
  render() {
    $("#accordionSidenav").html(`
  
      <div class="sidenav-menu-heading">Custom</div>
      <a class="nav-link active" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#collapsePages" aria-expanded="false" aria-controls="collapsePages">
          <div class="nav-link-icon "><i data-feather="grid"></i></div>
          Pages
          <div class="sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
      </a>
      <div class="collapse show" id="collapsePages" data-bs-parent="#accordionSidenav">
          <nav class="sidenav-menu-nested nav accordion" id="accordionSidenavPagesMenu">
              <a class="nav-link active" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#pagesCollapseAccount" aria-expanded="false" aria-controls="pagesCollapseAccount">
                  Account
                  <div class="sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
              </a>
              <div class="collapse show" id="pagesCollapseAccount" data-bs-parent="#accordionSidenavPagesMenu">
                  <nav class="sidenav-menu-nested nav">
                      <a class="nav-link" href="#" onclick="main.goList('dcs/dcsLog', null)">DCS Log</a>
                      <a class="nav-link active" href="#" onclick="main.goList('menu', null)">Admin Menu</a>
                      <a class="nav-link" href="#" onclick="main.goList('userMfa', null)">User Mfa</a>
                      <a class="nav-link" href="#" onclick="main.goList('users', null)">Users List</a>
                      <a class="nav-link" href="#" onclick="main.goList('company', null)">Company List</a>
                  </nav>
              </div>
              <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#pagesCollapseAuth" aria-expanded="false" aria-controls="pagesCollapseAuth">
                  Authentication
                  <div class="sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
              </a>
              <div class="collapse" id="pagesCollapseAuth" data-bs-parent="#accordionSidenavPagesMenu">
                  <nav class="sidenav-menu-nested nav accordion" id="accordionSidenavPagesAuth">
                      <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#pagesCollapseAuthBasic" aria-expanded="false" aria-controls="pagesCollapseAuthBasic">
                          Basic
                          <div class="sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                      </a>
                      <div class="collapse" id="pagesCollapseAuthBasic" data-bs-parent="#accordionSidenavPagesAuth">
                          <nav class="sidenav-menu-nested nav">
                              <a class="nav-link" href="auth-login-basic.html">Login</a>
                              <a class="nav-link" href="auth-register-basic.html">Register</a>
                              <a class="nav-link" href="auth-password-basic.html">Forgot Password</a>
                          </nav>
                      </div>
                      <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#pagesCollapseAuthSocial" aria-expanded="false" aria-controls="pagesCollapseAuthSocial">
                          Social
                          <div class="sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                      </a>
                      <div class="collapse" id="pagesCollapseAuthSocial" data-bs-parent="#accordionSidenavPagesAuth">
                          <nav class="sidenav-menu-nested nav">
                              <a class="nav-link" href="auth-login-social.html">Login</a>
                              <a class="nav-link" href="auth-register-social.html">Register</a>
                              <a class="nav-link" href="auth-password-social.html">Forgot Password</a>
                          </nav>
                      </div>
                  </nav>
              </div>
              <a class="nav-link collapsed" href="javascript:void(0);" data-bs-toggle="collapse" data-bs-target="#pagesCollapseError" aria-expanded="false" aria-controls="pagesCollapseError">
                  Error
                  <div class="sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
              </a>
              <div class="collapse" id="pagesCollapseError" data-bs-parent="#accordionSidenavPagesMenu">
                  <nav class="sidenav-menu-nested nav">
                      <a class="nav-link" href="error-400.html">400 Error</a>
                      <a class="nav-link" href="error-401.html">401 Error</a>
                      <a class="nav-link" href="error-403.html">403 Error</a>
                      <a class="nav-link" href="error-404-1.html">404 Error 1</a>
                      <a class="nav-link" href="error-404-2.html">404 Error 2</a>
                      <a class="nav-link" href="error-500.html">500 Error</a>
                      <a class="nav-link" href="error-503.html">503 Error</a>
                      <a class="nav-link" href="error-504.html">504 Error</a>
                  </nav>
              </div>
          </nav>
      </div>
    `);

    // 동적 HTML 삽입 후 아이콘 재렌더링
    if (window.feather) {
      //feather.replace();
    }
  },

  bind() {
    // 추후 메뉴 클릭 이벤트 필요하면 여기에
  }
};