<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>

    #pagination {
        margin-top: 15px;
        text-align: center;
    }

    #pagination .page-btn {
        margin: 0 3px;
        padding: 5px 10px;
        border: 1px solid #ccc;
        background: #f9f9f9;
        cursor: pointer;
    }

    #pagination .page-btn.active {
        font-weight: bold;
        background: #007bff;
        color: white;
    }


</style>
<main>
    <!-- Page Heading -->
    <h1 class="h3 mb-2 text-gray-800">Tables</h1>
    <p class="mb-4">DataTables is a third party plugin that is used to generate the demo table below.
        For more information about DataTables, please visit the <a target="_blank"
                                                                   href="https://datatables.net">official DataTables documentation</a>.</p>

    <!-- DataTales Example -->
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">DataTables Example</h6>
        </div>
        <div class="card-body">
            <div class="table-responsive">
                <div class="dataTables_wrapper dt-bootstrap4">
                    <div class="row">
                        <div class="col-sm-12 col-md-6">
                            <label class="d-flex align-items-center ">
                                <span class="mr-2">Search:</span>
                                <input type="search" id="searchBox" class="form-control form-control-sm" placeholder="" aria-controls="dataTable" style="width: 30%;">

                                <span class="mr-2">Status:</span>
                                <select id="status" class="form-control form-control-sm" style="width: 30%;">
                                    <option value="">-- 전체 --</option> <!-- 선택 안함일 경우 전체 조회 -->
                                    <option value="ACTIVE">ACTIVE</option>
                                    <option value="INACTIVE">INACTIVE</option>
                                </select>
                                <a href="#" onclick="getList();" class="btn btn-primary btn-icon-split" style="margin-left: 5px;">
                                    <span class="icon text-white-50">
                                        <i class="fas fa-search fa-sm"></i>
                                    </span>
                                </a>

                            </label>
                        </div>
                        <div class="col-sm-12 col-md-6">
                            <a href="#" onclick="goAmend('users', '0', 'POST');" class="btn btn-secondary btn-icon-split" style="margin-left: 5px;">
                                    <span class="icon text-white-50">
                                        New
                                    </span>
                            </a>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-12">
                            <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                                <thead>
                                <tr>
                                    <th>idx</th>
                                    <th>company</th>
                                    <th>fullName</th>
                                    <th>Email</th>
                                    <th>phone</th>
                                    <th>role</th>
                                    <th>status</th>
                                    <th>Btn</th>
                                </tr>
                                </thead>
                                <tbody id="grid" />
                                <tr>
                                    <td colspan="10" class="text-center">조회된 데이터가 없습니다.</td>
                                </tr>
                            </table>
                            <div id="pagination" class="pagination"></div>
                            <div id="summary" ></div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>

<script type="text/javascript">
    const MENU = "users";
    const API_URL = "/api/" + MENU;
    window.onload = function() {

        init();
        getList();
    };
    const init = () => {

        console.log("Users init");
    }

    /**
     * 생성 (Create)
     * @param {Object} data 데이터 정보 {id, name, email}
     * @returns {Promise<Object>} 생성된 데이터 정보
     */
    const createData = async (userId, data) => {
        await fetch(API_URL + `/` + userId, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok)
                    throw new Error("서버 에러 발생: " + response.status);
                return response.json(); // JSON 응답 처리
            })
            .then(jsonData => {
                getList();
                console.log("응답 createData JSON:", jsonData);
            })
            .catch(err => console.error("에러:", err));

    };
    /**
     * 데이터 단일 조회 (Read)
     * @returns {Promise<Object>} 단일 데이터
     */
    const getData = async (userId) => {
        await fetch(API_URL + `/` + userId, {
            method: "GET",
            headers: { "Content-Type": "application/json" }
        })
            .then(response => {
                if (!response.ok)
                    throw new Error("서버 에러 발생: " + response.status);
                return response.json(); // JSON 응답 처리
            })
            .then(jsonData => {
                console.log("응답 getData JSON:", jsonData);
            })
            .catch(err => console.error("에러:", err));
    };

    /**
     * 데이터 목록 조회 (Read)
     * @returns {Promise<Array>} 리스트 데이터
     */
    const getList = async (page = 0, size = 10) => {
        const search = $('#searchBox').val();
        const status = $('#status').val();

        // 쿼리스트링 만들기
        const query = new URLSearchParams({
            search: search || "",
            status: status || "",
            page: page,   // 몇 번째 페이지 (0부터 시작)
            size: size    // 페이지당 데이터 개수
            // sort: "userId,desc"  // 필요하면 정렬도 추가 가능
        });

        $('#loading').show();

        await fetch(API_URL + "?" + query.toString(), {
            method: "GET",
            headers: { "Content-Type": "application/json" }
        })
            .then(res => res.json())
            .then(result => {
                renderGrid(result.data.content, "grid");   // data.content → 실제 데이터
                renderPagination(result.data);             // 페이지네이션 UI 추가
                renderSummary(result.data);
            })
            .finally(() => {
                setTimeout(() => $('#loading').hide(), 250);
            })
            .catch(err => console.error("에러:", err));
    };

    /**
     * 데이터 삭제 (Delete)
     * @param {number} id 데이터 ID
     * @returns {Promise<void>}
     */
    const deleteData = async (userId) => {
        await fetch(API_URL + `/` + userId, {
            method: "DELETE",
            headers: { "Content-Type": "application/json" }
        })
            .then(response => {
                if (!response.ok)
                    throw new Error("서버 에러 발생: " + response.status);
                return response.json(); // JSON 응답 처리
            })
            .then(jsonData => {
                getList();
                console.log("응답 deleteData JSON:", jsonData);
            })
            .catch(err => console.error("에러:", err));
    };

    // 그리드(테이블) 생성 함수
    const renderGrid = (data, tbodyId) => {
        const tbody = document.getElementById(tbodyId);
        tbody.innerHTML = ""; // 기존 내용 초기화

        if (!data || data.length === 0) {
            // 데이터 없으면 안내 메시지 표시
            const tr = document.createElement("tr");
            tr.innerHTML = `<td colspan="10" class="text-center">조회된 데이터가 없습니다.</td>`;
            tbody.appendChild(tr);
            return;
        }

        data.forEach((item, index) => {
            const tr = document.createElement("tr");
            tr.innerHTML =
                "<td>" + (index + 1) + "</td>" +
                "<td>" + item.companyType + " " + item.companyName + "</td>" +
                "<td>" + item.fullName + "</br>(" + item.userId + ":" + item.username + ")" + "</td>" +
                "<td>" + item.email + "</td>" +
                "<td>" + item.phone + "</td>" +
                "<td>" + item.role + "</td>" +
                "<td>" + item.status + "</td>" +
                "<td class='text-center'>" +

                    "<a href='#' class='btn btn-info btn-circle btn-sm' style='margin-right: 5px;' onclick='goView(\"" + MENU + "\", \"" + item.userId + "\")'><i class='fas fa-info-circle' ></i></a>" +
                    "<a href='#' class='btn btn-warning btn-circle btn-sm' style='margin-right: 5px;' onclick='goAmend(\"" + MENU + "\", \"" + item.userId + "\", \"PUT\")'><i class='fas fa-exclamation-triangle' ></i></a>" +
                    "<a href='#' class='btn btn-danger btn-circle btn-sm' style='margin-right: 5px;' onclick='goDelete(\"" + MENU + "\", \"" + item.userId + "\")'><i class='fa fa-trash' ></i></a>" +
                "</td>";


            tbody.appendChild(tr);
        });
    };

    const renderPagination = (pageData) => {
        const pagination = $("#pagination"); // 페이지네이션 영역 (div or ul)
        pagination.empty();

        const totalPages = pageData.totalPages;
        const currentPage = pageData.number; // 0부터 시작
        const maxLinks = 5; // 하단에 표시할 최대 번호 개수

        if (totalPages <= 1) return; // 페이지가 1개뿐이면 안 그림

        // 이전 버튼
        if (currentPage > 0) {
            pagination.append(
                `<button class="page-btn" data-page="${currentPage - 1}">이전</button>`
            );
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
            pagination.append(
                `<button class="page-btn ${i == currentPage ? "active" : ""}" data-page="${i}">${i + 1}</button>`
            );
        }

        // 다음 버튼
        if (currentPage < totalPages - 1) {
            pagination.append(
                `<button class="page-btn" data-page="${currentPage + 1}">다음</button>`
            );
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

</script>