<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="page-header page-header-compact page-header-light border-bottom bg-white mb-4">
    <div class="container-fluid px-4">
        <div class="page-header-content">
            <div class="row align-items-center justify-content-between pt-3">
                <div class="col-auto mb-3">
                    <h1 class="page-header-title">
                        <div class="page-header-icon"><i data-feather="globe"></i></div>
                        <label id="page-header-title">-</label>
                    </h1>
                </div>
                <div class="col-12 col-xl-auto mb-3">
                    <a id="goAmendBtn" class="btn btn-sm btn-light text-primary" href="#">
                        <label id="goAmendBtnLabel">-</label>
                    </a>
                    <a id="goExcelBtn" class="btn btn-sm btn-light text-teal" href="#">
                        <label id="goExcelBtnLabel">Excel Download</label>
                    </a>
                    <a id="goExcelBtn" class="btn btn-sm btn-light text-success" href="#">
                        <label id="goCsvBtnLabel">CSV Download</label>
                    </a>
                </div>
            </div>
        </div>
    </div>
</header>

<!-- Main page content-->
<div class="container-fluid px-4">
    <div class="card">
        <div class="card-body">
            <div class="datatable-wrapper datatable-loading no-footer sortable searchable fixed-columns">
                <div class="datatable-top">
                    <div class="datatable-dropdown">
                        <label>
                            <select id="pageSize" class="datatable-selector">
                                <option value="5">5</option>
                                <option value="10" selected>10</option>
                                <option value="25">25</option>
                                <option value="50">50</option>
                                <option value="100">100</option>
                            </select>
                            entries per page
                        </label>
                    </div>

                    <div class="datatable-search">
                        <div class="input-group">
                            <input id="searchBox"
                                   class="datatable-input" type="search" placeholder="Search..." title="Search within table" aria-controls="datatablesSimple" />
                            <button class="btn btn-primary" type="button" onclick="getList();">Search</button>
                        </div>
                    </div>
                </div>
                <div class="datatable-container">
                    <table id="datatablesSimple" class="datatable-table">
                        <thead>
                        <tr>
                            <th style="width: 25%">Title</th>
                            <th style="width: 64%">Content</th>
                            <th style="width: 11%">Actions</th>
                        </tr>
                        </thead>
                        <tbody id="grid" />
                        <tr>
                            <td colspan="3" class="text-center">The data you searched for does not exist.</td>
                        </tr>
                        </tbody>
                    </table>
                </div>


                <div id="" class="datatable-bottom">

                    <div id="summary" class="datatable-info"></div><!-- Total count -->
                    <div id="pagination" class="pagination"></div><!-- Page item -->
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    const CATEGORY_ID = ${categoryId};
    const MENU = "bbs/bbsPosts";
    const API_URL = "/api/" + MENU;
    window.onload = function() {

        init();
    };
    const init = () => {
        if(!accessTokenCheck()) return false;

        getList();

        $("#page-header-title").text("Ai Inquiry & Answer");
        $("#goAmendBtn").text("Add New Ai Bbs");
        $("#goExcelBtnLabel").text("Excel Download");
        $("#goCsvBtnLabel").text("CSV Download");

        $("#viewTitle").text(CATEGORY_ID);
        $("#goAmendBtn").attr("onclick", "goAmend('"+MENU+"', " + CATEGORY_ID + ", '0', 'POST');");
        console.log("list init");
    }

    /**
     * R.데이터 목록 조회 (Read)
     * @returns {Promise<Array>} 리스트 데이터
     */
    const getList = async (page = 0, size = 5) => {
        size = $("#pageSize").val();
        const search = $('#searchBox').val();

        // 쿼리스트링 만들기
        const query = new URLSearchParams({
            search: search || "",
            page: page,   // 몇 번째 페이지 (0부터 시작)
            size: size    // 페이지당 데이터 개수
        });

        $('#loading').show();

        await fetch(API_URL + "?" + query.toString() + "&categoryId=" + CATEGORY_ID, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
            }
        })
            .then(res => res.json())
            .then(result => {
                console.log(result.data.content);
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
     * D.데이터 삭제 (Delete)
     * @param {number} id 데이터 ID
     * @returns {Promise<void>}
     */
    const deleteData = async (id) => {
        await fetch(API_URL + `/` + id + "?categoryId=" + CATEGORY_ID, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
            }
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

    /**
     * ETC.그리드(테이블) 생성 함수
     * @param {number} data, tbodyId 데이터 ID
     * @returns {Grid} Grid
     */
    const renderGrid = (data, tbodyId) => {
        const tbody = document.getElementById(tbodyId);
        tbody.innerHTML = ""; // 기존 내용 초기화

        if (!data || data.length === 0) {
            // 데이터 없으면 안내 메시지 표시
            const tr = document.createElement("tr");
            tr.innerHTML = `<td colspan="3" class="text-center">The data you searched for does not exist.</td>`;
            tbody.appendChild(tr);
            return;
        }

        // 테이블 행 생성
        data.forEach((item, index) => {
            const tr = document.createElement("tr");
            tr.innerHTML = [
                '<td>' + item.title.substring(0, 32) +  '</td>',
                '<td>' + item.bbsContent.substring(0, 80) +  '</td>',
                '<td class="text-center">' + createActionButtons(item.postId, item.categoryId) + '</td>'
            ].join('');

            tbody.appendChild(tr);
        });
    };
</script>