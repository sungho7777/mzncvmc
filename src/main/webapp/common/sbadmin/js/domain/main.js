// main.js
window.main = {
    init() {},
    // dashboard 화면이동
    goDashboard(menu){
        if(!auth.accessTokenCheck()) return false;

        window.location.href = "/m/" + menu + "/dashboard";
    },
    // 목록 화면이동
    goList(menu, categoryId){
        if(!auth.accessTokenCheck()) return false;

        window.location.href = "/m/" + menu + "/list" + (categoryId === undefined || categoryId == null ? "" : "?categoryId=" + categoryId);
    },
    // 상세보기 화면이동
    goView(menu, categoryId, id, tab){
        if(!auth.accessTokenCheck()) return false;

        window.location.href = "/m/" + menu + "/view/" + id + (categoryId === undefined || categoryId == null ? "" : "?categoryId=" + categoryId);
    },
    // 생성&수정 화면이동
    goAmend(menu, categoryId, id, mapping){
        if(!auth.accessTokenCheck()) return false;

        window.location.href = "/m/" + menu + "/amend/" + id + "?mapping=" + mapping + (categoryId === undefined || categoryId == null ? "" : "&categoryId=" + categoryId);
    },
    // 삭제 모달띄우기
    goDelete(menu, categoryId, id){
        if(!auth.accessTokenCheck()) return false;

        // 모달 띄우기
        $('#deleteModal').modal('show');

        // 모달 안의 버튼에 id 저장
        $('#delete-btn').data('id', id);
        $('#delete-btn').data('categoryId', categoryId);
    },
    // 로그아웃
    goLogout(){
        const accessToken = localStorage.getItem("accessToken");
        console.log("accessToken", accessToken);

        fetch('/api/auth/logout', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + accessToken,
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    // 로컬 스토리지에서 토큰 제거
                    main.removeLocalStorage();

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
    },
    removeLocalStorage(){
        // 네트워크 오류 등의 경우 토큰 제거
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('userId');
        localStorage.removeItem('username');
        localStorage.removeItem('pwNotifyDuration');
    },
    showAlert() {}
};

setTimeout(() => {

    const pwNotifyDuration = localStorage.getItem("pwNotifyDuration");

    if(pwNotifyDuration == '999'){
        // 신규 사용자는 비밀번호를 변경하도록 유도한다.
        const changePasswordModal = new bootstrap.Modal(document.getElementById('changePasswordModal'), {
            backdrop: 'static',
            keyboard: false
        });
        changePasswordModal.show();
    }else{
/*
        const loginDetailsModal = new bootstrap.Modal(document.getElementById('loginDetailsModal'), {
            backdrop: 'static',
            keyboard: false
        });
        loginDetailsModal.show();*/
    }
}, "3000");








