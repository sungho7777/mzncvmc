// auth.js
const ACCESS_TOKEN_AUTH = window.auth.accessToken;

window.auth = {

  init() {},
  // 유효한 토크 체크
  async accessTokenCheck(){

    return true;
    /*
    if (ACCESS_TOKEN_AUTH) {
      // 토큰 유효성 검증을 위해 보호된 API 호출
      const response = await fetch('/api/user/authorities', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
          , 'Authorization': 'Bearer ' + ACCESS_TOKEN_AUTH
        }
      });
      if (response.ok) {

        return true;
      }
    }
    alert('잘못된 토큰 정보입니다. 로그아웃처리 됩니다.');

    main.goLogout();

    return false;
    */
  },
  showAlert() {}
};