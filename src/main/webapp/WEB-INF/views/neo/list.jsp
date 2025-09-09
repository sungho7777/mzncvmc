<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>







<main class="container">
    <div class="d-flex align-items-center p-3 my-3 text-white bg-purple rounded shadow-sm" >
        <img class="me-3" src="/common/assets/brand/bootstrap-logo-white.svg" alt="" width="48" height="38" />
        <div class="lh-1">
            <h1 class="h6 mb-0 text-white lh-1">Example table</h1>
            <small>Since 2011</small>
        </div>
    </div>

    <p>And don't forget about tables in these posts:</p>
    <table class="table">
        <thead>
            <tr class="text-center">
                <th>idx</th>
                <th>Company</th>
                <th>Addr</th>
                <th>Tel</th>
                <th>Btn</th>
            </tr>
        </thead>
        <tbody id="grid"> </tbody>
        <tfoot>
            <tr>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
                <td class="text-center">Totals : 23/23</td>
            </tr>
        </tfoot>
    </table>
    <p>This is some additional paragraph placeholder content. It's a slightly shorter version of the other highly repetitive body text used throughout.</p> </article>


    <div class="my-3 p-3 bg-body rounded shadow-sm">
        <h6 class="border-bottom pb-2 mb-0">Suggestions</h6>
        <div class="d-flex text-body-secondary pt-3">
            <svg
                    aria-label="Placeholder: 32x32"
                    class="bd-placeholder-img flex-shrink-0 me-2 rounded"
                    height="32"
                    preserveAspectRatio="xMidYMid slice"
                    role="img"
                    width="32"
                    xmlns="http://www.w3.org/2000/svg"
            >
                <title>Placeholder</title>
                <rect width="100%" height="100%" fill="#007bff"></rect>
                <text x="50%" y="50%" fill="#007bff" dy=".3em">32x32</text>
            </svg>
            <div class="pb-3 mb-0 small lh-sm border-bottom w-100">
                <div class="d-flex justify-content-between">
                    <strong class="text-gray-dark">Full Name</strong>
                    <a href="#">Follow</a>
                </div>
                <span class="d-block">@username</span>
            </div>
        </div>
        <div class="d-flex text-body-secondary pt-3">
            <svg
                    aria-label="Placeholder: 32x32"
                    class="bd-placeholder-img flex-shrink-0 me-2 rounded"
                    height="32"
                    preserveAspectRatio="xMidYMid slice"
                    role="img"
                    width="32"
                    xmlns="http://www.w3.org/2000/svg"
            >
                <title>Placeholder</title>
                <rect width="100%" height="100%" fill="#007bff"></rect>
                <text x="50%" y="50%" fill="#007bff" dy=".3em">32x32</text>
            </svg>
            <div class="pb-3 mb-0 small lh-sm border-bottom w-100">
                <div class="d-flex justify-content-between">
                    <strong class="text-gray-dark">Full Name</strong>
                    <a href="#">Follow</a>
                </div>
                <span class="d-block">@username</span>
            </div>
        </div>
        <div class="d-flex text-body-secondary pt-3">
            <svg
                    aria-label="Placeholder: 32x32"
                    class="bd-placeholder-img flex-shrink-0 me-2 rounded"
                    height="32"
                    preserveAspectRatio="xMidYMid slice"
                    role="img"
                    width="32"
                    xmlns="http://www.w3.org/2000/svg"
            >
                <title>Placeholder</title>
                <rect width="100%" height="100%" fill="#007bff"></rect>
                <text x="50%" y="50%" fill="#007bff" dy=".3em">32x32</text>
            </svg>
            <div class="pb-3 mb-0 small lh-sm border-bottom w-100">
                <div class="d-flex justify-content-between">
                    <strong class="text-gray-dark">Full Name</strong>
                    <a href="#">Follow</a>
                </div>
                <span class="d-block">@username</span>
            </div>
        </div>
        <small class="d-block text-end mt-3">
            <a href="#">All suggestions</a>
        </small>
    </div>




</main>


<script type="text/javascript">
    const API_URL = "/api/neo";
    window.onload = function() {
        console.log("init");

        getList();
    };

    /**
     * 생성 (Create)
     * @param {Object} data 데이터 정보 {id, name, email}
     * @returns {Promise<Object>} 생성된 데이터 정보
     */
    /*
        // 사용자 추가
        createData({ company: "삼성전자", addr: "서울 성남구 9998", tel: "02-3434-5567" });
        createData({ company: "애플식품", addr: "충남 예성구 113", tel: "052-5555-3333" });
        createData({ company: "엘지전자", addr: "서울 성남구 2324", tel: "02-1212-5567" });
        createData({ company: "일등전자", addr: "서울 성남구 2324", tel: "02-1212-5567" });
     */
    const createData = async (data) => {
        await fetch(API_URL, {
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
    const getData = async (id) => {
        await fetch(API_URL + `/` + id, {
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
    const getList = async () => {
        await fetch(API_URL, {
            method: "GET",
            headers: { "Content-Type": "application/json" }
        })
            .then(response => {
                if (!response.ok)
                    throw new Error("서버 에러 발생: " + response.status);
                return response.json(); // JSON 응답 처리
            })
            .then(jsonData => {
                renderGrid(jsonData, "grid");
                console.log("응답 getList JSON:", jsonData);
            })
            .catch(err => console.error("에러:", err));
    };

    /**
     * 데이터 수정 (Update)
     * @param {number} id 데이터 ID
     * @param {Object} user 수정할 데이터 {name, email}
     * @returns {Promise<Object>} 수정된 데이터 정보
     */
    //
    /*
        updateData(3, { company: "삼성전자", addr: "제주도 서귀포시 3244", tel: "032-4545-2323" });

    */
    const updateData = async (id, data) => {
        await fetch(API_URL + `/` + id, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok)
                    throw new Error("서버 에러 발생: " + response.status);
                return response.json(); // JSON 응답 처리
            })
            .then(jsonData => {
                getList();
                console.log("응답 updateData JSON:", jsonData);
            })
            .catch(err => console.error("에러:", err));


    };

    /**
     * 데이터 삭제 (Delete)
     * @param {number} id 데이터 ID
     * @returns {Promise<void>}
     */
    const deleteData = async (id) => {
        // 사용자에게 확인
        const confirmed = confirm("정말 삭제하시겠습니까?");
        if (!confirmed) return; // 취소하면 종료

        await fetch(API_URL + `/` + id, {
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
            tr.innerHTML = `<td colspan="5" class="text-center">조회된 데이터가 없습니다.</td>`;
            tbody.appendChild(tr);
            return;
        }

        data.forEach(item => {
            const tr = document.createElement("tr");
            tr.innerHTML =
                "<td class='text-center'>" + item.id + "</td>" +
                "<td class='text-center'>" + item.company + "</td>" +
                "<td class='text-center'>" + item.addr + "</td>" +
                "<td class='text-center'>" + item.tel + "</td>" +
                "<td class='text-center'>" +
                "<button type='button' class='btn btn-info' onclick='goView(" + item.id + ")'>View</button>" +
                "<button type='button' class='btn btn-warning' onclick='goAmend(" + item.id + ")'>Amend</button>" +
                "<button type='button' class='btn btn-danger' onclick='deleteData(" + item.id + ")'>Del</button>" +
                "</td>";
            tbody.appendChild(tr);
        });
    };

    const goView = (id) => {
        window.location.href = "/neo/view/" + id;
    };
    const goAmend = (id) => {
        window.location.href = "/neo/amend/" + id;
    };
</script>
