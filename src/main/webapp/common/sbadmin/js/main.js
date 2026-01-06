//setInterval(() => {
    //if(!accessTokenCheck()) return false;



//}, "3000");



// dashboard 화면이동
const goDashboard = (menu) => {
    if(!accessTokenCheck()) return false;

    window.location.href = "/m/" + menu + "/dashboard";
};
// 목록 화면이동
const goList = (menu, categoryId) => {
    if(!accessTokenCheck()) return false;

    window.location.href = "/m/" + menu + "/list" + (categoryId === undefined || categoryId == null ? "" : "?categoryId=" + categoryId);
};
// 상세보기 화면이동
const goView = (menu, categoryId, id, tab) => {
    if(!accessTokenCheck()) return false;

    window.location.href = "/m/" + menu + "/view/" + id + (categoryId === undefined || categoryId == null ? "" : "?categoryId=" + categoryId);
};
// 생성&수정 화면이동
const goAmend = (menu, categoryId, id, mapping) => {
    if(!accessTokenCheck()) return false;

    window.location.href = "/m/" + menu + "/amend/" + id + "?mapping=" + mapping + (categoryId === undefined || categoryId == null ? "" : "&categoryId=" + categoryId);
};
// 삭제 모달띄우기
const goDelete = (menu, categoryId, id) => {
    if(!accessTokenCheck()) return false;

    // 모달 띄우기
    $('#deleteModal').modal('show');

    // 모달 안의 버튼에 id 저장
    $('#delete-btn').data('id', id);
    $('#delete-btn').data('categoryId', categoryId);
}
// 로그아웃
const goLogout = () => {
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
                removeLocalStorage();

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

const removeLocalStorage = () =>{
    // 네트워크 오류 등의 경우 토큰 제거
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('userId');
    localStorage.removeItem('username');
    localStorage.removeItem('pwNotifyDuration');
};

const accessTokenCheck = async () => {
    const accessToken = localStorage.getItem("accessToken");
    const pwNotifyDuration = localStorage.getItem("pwNotifyDuration");

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
            console.log(response);

            if(pwNotifyDuration == '999'){
                alert('비밀번호를 변경하세요.');
            }

            return true;
        }
    }
    alert('잘못된 토큰 정보입니다. 로그아웃처리 됩니다.');
    goLogout();
    return false;
}
const renderPagination = (pageData) => {
    const pagination = $("#pagination"); // 페이지네이션 영역
    pagination.empty();

    const totalPages = pageData.totalPages;
    const currentPage = pageData.number; // 0부터 시작
    const maxLinks = 5; // 하단에 표시할 최대 번호 개수

    if (totalPages <= 1) return; // 페이지가 1개뿐이면 안 그림

    // nav 요소 생성
    const nav = $('<nav>').addClass('datatable-pagination');
    const ul = $('<ul>').addClass('datatable-pagination-list');

    // 이전 버튼
    const prevLi = $('<li>').addClass('datatable-pagination-list-item');
    if (currentPage <= 0) {
        prevLi.addClass('datatable-hidden datatable-disabled');
    }
    const prevLink = $('<a>')
        .addClass('datatable-pagination-list-item-link')
        .attr('data-page', Math.max(0, currentPage - 1))
        .text('<');
    prevLi.append(prevLink);
    ul.append(prevLi);

    // 번호 버튼 계산
    let startPage = Math.max(0, currentPage - Math.floor(maxLinks / 2));
    let endPage = Math.min(totalPages - 1, startPage + maxLinks - 1);

    // startPage 보정
    if (endPage - startPage < maxLinks - 1) {
        startPage = Math.max(0, endPage - maxLinks + 1);
    }

    // 번호 버튼 추가
    for (let i = startPage; i <= endPage; i++) {
        const pageLi = $('<li>').addClass('datatable-pagination-list-item');
        if (i === currentPage) {
            pageLi.addClass('datatable-active');
        }
        const pageLink = $('<a>')
            .addClass('datatable-pagination-list-item-link')
            .attr('data-page', i)
            .text(i + 1);
        pageLi.append(pageLink);
        ul.append(pageLi);
    }

    // 다음 버튼
    const nextLi = $('<li>').addClass('datatable-pagination-list-item');
    if (currentPage >= totalPages - 1) {
        nextLi.addClass('datatable-hidden datatable-disabled');
    }
    const nextLink = $('<a>')
        .addClass('datatable-pagination-list-item-link')
        .attr('data-page', Math.min(totalPages - 1, currentPage + 1))
        .text('>');
    nextLi.append(nextLink);
    ul.append(nextLi);

    nav.append(ul);
    pagination.append(nav);

    // 클릭 이벤트 바인딩
    $(".datatable-pagination-list-item-link").off("click").on("click", function (e) {
        e.preventDefault();
        const $parent = $(this).parent();

        // disabled 상태면 클릭 무시
        if ($parent.hasClass('datatable-disabled')) {
            return;
        }

        const page = parseInt($(this).data("page"));
        getList(page, pageData.size); // getList(page, size) 호출
    });
}

const renderPagination__ = (pageData) => {
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

    // Showing 1 to 10 of 100 entries
    //$('#summary').text("Showing " + page + " to " + pageData.totalPages + " of " + total + " entries");
    $('#summary').text("Total " + total + "건 중 " + currentCount + "건 조회 (Page " + page + " / " + pageData.totalPages + ")");
};


/**
 * ETC.액션 버튼 HTML 생성 함수
 * @param {id} 데이터 ID
 * @returns {Grid} Grid
 */
const createActionButtons = (id, categoryId) => {
    const buttons = [
        { class: 'bg-green-soft text-green', label:"View", action: 'goView(\'' + (MENU) + '\', ' + categoryId + ', ' + id + ')' },
        { class: 'bg-yellow-soft text-yellow', label:"Amend", action: 'goAmend(\'' + (MENU) + '\', ' + categoryId + ', ' + id + ', \'PUT\')' },
        { class: 'bg-red-soft text-red', label:"Delete", action: 'goDelete(\'' + (MENU) + '\', ' + categoryId + ', ' + id + ')' }

    ];
    return buttons.map(btn =>
        '   <a href="#" onclick="' + btn.action + '">' +
        '       <span class="badge '+btn.class+'">' + btn.label +
        '       </span> ' +
        '   </a>'
    ).join('');
};
