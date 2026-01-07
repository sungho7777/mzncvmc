// board.js
window.board = {

  init() {},

  // 게시판 페이징 처리
  renderPagination(pageData){
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
  },
  // 게시판 건수 렌더링
  renderSummary(pageData){
    const total = pageData.totalElements;        // 전체 건수
    const currentCount = pageData.numberOfElements; // 현재 페이지 조회 건수
    const page = pageData.pageable.pageNumber + 1; // 1-based 페이지
    //const size = pageData.pageable.pageSize;

    // Showing 1 to 10 of 100 entries
    //$('#summary').text("Showing " + page + " to " + pageData.totalPages + " of " + total + " entries");
    $('#summary').text("Total " + total + "건 중 " + currentCount + "건 조회 (Page " + page + " / " + pageData.totalPages + ")");
  },
  // ETC.액션 버튼 HTML 생성 함수
  createActionButtons(id, categoryId){
    const buttons = [
      { class: 'bg-green-soft text-green', label:"View", action: 'main.goView(\'' + (MENU) + '\', ' + categoryId + ', ' + id + ')' },
      { class: 'bg-yellow-soft text-yellow', label:"Amend", action: 'main.goAmend(\'' + (MENU) + '\', ' + categoryId + ', ' + id + ', \'PUT\')' },
      { class: 'bg-red-soft text-red', label:"Delete", action: 'main.goDelete(\'' + (MENU) + '\', ' + categoryId + ', ' + id + ')' }

    ];
    return buttons.map(btn =>
        '   <a href="#" onclick="' + btn.action + '">' +
        '       <span class="badge '+btn.class+'">' + btn.label +
        '       </span> ' +
        '   </a>'
    ).join('');
  },

  showAlert() {}
};