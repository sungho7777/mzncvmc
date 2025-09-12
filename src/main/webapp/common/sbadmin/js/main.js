
// dashboard 화면이동
const goDashboard = (menu) => {
    if(!accessTokenCheck) return false;
    window.location.href = "/m/" + menu + "/dashboard";
};
// 목록 화면이동
const goList = (menu) => {
    if(!accessTokenCheck) return false;
    window.location.href = "/m/" + menu + "/list";
};
// 상세보기 화면이동
const goView = (menu, id) => {
    if(!accessTokenCheck) return false;
    window.location.href = "/m/" + menu + "/view/" + id;
};
// 생성&수정 화면이동
const goAmend = (menu, id, mapping) => {
    if(!accessTokenCheck) return false;
    window.location.href = "/m/" + menu + "/amend/" + id + "?mapping=" + mapping;
};
// 삭제 모달띄우기
const goDelete = (menu, id) => {
    // 모달 띄우기
    $('#deleteModal').modal('show');

    // 모달 안의 버튼에 id 저장
    $('#delete-btn').data('id', id);
}
// 로그아웃
const goLogout = () => {

    fetch('/api/auth/logout', {
        method: 'POST'
    })
        .then(response => {
            if (response.ok) {
                // 로컬 스토리지에서 토큰 제거
                localStorage.removeItem('accessToken');
                localStorage.removeItem('refreshToken');
                localStorage.removeItem('username');

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
}

const accessTokenCheck = async () => {
    const accessToken = localStorage.getItem("accessToken");
    console.log("accessToken", accessToken);

    if (accessToken) {
        // 토큰 유효성 검증을 위해 보호된 API 호출
        const response = await fetch('/api/user/authorities', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + accessToken,
                'Content-Type': 'application/json'
            }
        });
        if (response.ok) {
            return true;
        }
    }
    alert('잘못된 토큰 정보입니다. 로그아웃처리 됩니다.');
    goLogout();
    return false;
}

const renderPagination = (pageData) => {
    const pagination = $("#pagination"); // 페이지네이션 영역 (div or ul)
    pagination.empty();

    const totalPages = pageData.totalPages;
    const currentPage = pageData.number; // 0부터 시작
    const maxLinks = 5; // 하단에 표시할 최대 번호 개수

    if (totalPages <= 1) return; // 페이지가 1개뿐이면 안 그림

    // 이전 버튼
    if (currentPage > 0) {
        const pageBtn = $('<button>')
            .addClass('page-btn')
            .attr('data-page', (currentPage - 1))
            .text('이전');
        pagination.append(pageBtn);
    }

    // 번호 버튼 계산 (ex: 1~5, 6~10)
    let startPage = Math.max(0, currentPage - Math.floor(maxLinks / 2));
    let endPage = Math.min(totalPages - 1, startPage + maxLinks - 1);

    // startPage 보정
    if (endPage - startPage < maxLinks - 1) {
        startPage = Math.max(0, endPage - maxLinks + 1);
    }

    // 번호 버튼 추가
    for (let i = startPage; i <= endPage; i++) {
        // 페이지 번호 버튼
        const pageBtn = $('<button>')
            .addClass('page-btn')
            .addClass(i == currentPage ? 'active' : '')
            .attr('data-page', i)
            .text(i + 1);
        pagination.append(pageBtn);
    }

    // 다음 버튼
    if (currentPage < totalPages - 1) {
        const nextBtn = $('<button>')
            .addClass('page-btn')
            .attr('data-page', currentPage + 1)
            .text('다음');
        pagination.append(nextBtn);
    }

    // 클릭 이벤트 바인딩
    $(".page-btn").off("click").on("click", function () {
        const page = $(this).data("page");
        getList(page, pageData.size); // getList(page, size) 호출
    });
}
const renderSummary = (pageData) => {
    const total = pageData.totalElements;        // 전체 건수
    const currentCount = pageData.numberOfElements; // 현재 페이지 조회 건수
    const page = pageData.pageable.pageNumber + 1; // 1-based 페이지
    //const size = pageData.pageable.pageSize;

    $('#summary').text("총 " + total + "건 중 " + currentCount + "건 조회 (페이지 " + page + " / " + pageData.totalPages + ")");
};