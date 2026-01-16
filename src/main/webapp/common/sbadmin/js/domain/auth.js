// auth.js
window.auth = {

  init() {},
  // 유효한 토크 체크
  async accessTokenCheck(){
    const accessToken = localStorage.getItem("accessToken");

    if (accessToken) {
      // 토큰 유효성 검증을 위해 보호된 API 호출
      const response = await fetch('/api/user/authorities', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
          , 'Authorization': 'Bearer ' + accessToken
        }
      });
      if (response.ok) {

        return true;
      }
    }
    alert('잘못된 토큰 정보입니다. 로그아웃처리 됩니다.');

    main.goLogout();

    return false;
  },
  showAlert() {}
};