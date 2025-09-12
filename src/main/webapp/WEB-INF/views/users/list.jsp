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

        getList();
    };
    const init = () => {

        console.log("init");
    }

    /**
     * R.데이터 목록 조회 (Read)
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

        try {
            const res = await fetch(API_URL + "?" + query.toString(), {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
                }
            });

            if (!res.ok) throw new Error('Network response was not ok');

            const result = await res.json();
            renderGrid(result.data.content, "grid");
            renderPagination(result.data);
            renderSummary(result.data);
        } catch (err) {
            console.error("에러:", err);
        } finally {
            setTimeout(() => $('#loading').hide(), 250);
        }

    };

    /**
     * D.데이터 삭제 (Delete)
     * @param {number} id 데이터 ID
     * @returns {Promise<void>}
     */
    const deleteData = async (id) => {
        await fetch(API_URL + `/` + id, {
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
            tr.innerHTML = `<td colspan="10" class="text-center">조회된 데이터가 없습니다.</td>`;
            tbody.appendChild(tr);
            return;
        }

        // 테이블 행 생성
        data.forEach((item, index) => {
            const tr = document.createElement("tr");
            tr.innerHTML = [
                '<td>' + (index + 1) + '</td>',
                '<td>' + item.companyType + ' ' + item.companyName + '</td>',
                '<td>' + item.fullName + '<br/>(' + item.userId + ':' + item.username + ')</td>',
                '<td>' + item.email + '</td>',
                '<td>' + item.phone + '</td>',
                '<td>' + item.role + '</td>',
                '<td>' + item.status + '</td>',
                '<td class="text-center">' + createActionButtons(item.userId) + '</td>'
            ].join('');

            tbody.appendChild(tr);
        });
    };

    /**
     * ETC.액션 버튼 HTML 생성 함수
     * @param {id} 데이터 ID
     * @returns {Grid} Grid
     */
    const createActionButtons = (id) => {
        const buttons = [
            { class: 'btn-info', icon: 'fas fa-info-circle', action: 'goView(\'' + (MENU) + '\', ' + id + ')' },
            { class: 'btn-warning', icon: 'fas fa-exclamation-triangle', action: 'goAmend(\'' + (MENU) + '\', ' + id + ', \'PUT\')' },
            { class: 'btn-danger', icon: 'fa fa-trash', action: 'goDelete(\'' + (MENU) + '\', ' + id + ')' }
        ];

        return buttons.map(btn =>
            '<a href="#" class="btn ' + btn.class + ' btn-circle btn-sm" style="margin-right: 5px;" onclick="' + btn.action + '">' +
            '<i class="' + btn.icon + '"></i>' +
            '</a>'
        ).join('');
    };


</script>