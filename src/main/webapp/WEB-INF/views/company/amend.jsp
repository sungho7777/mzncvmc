<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<main>
    <form id="amendForm">
        <input type="text" name="mapping" value="${mapping}">

        <div class="card-body">
            <div class="table-responsive">
                <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                    <thead>
                    <tr>
                        <th style="width: 15%;">Entity</th>
                        <th>Content</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr><td>ID</td><td><input type="text" name="companyId" id="companyId" value="0" /></td></tr>
                    <tr><td>회사명</td><td><input type="text" name="companyName" id="companyName" /></td></tr>
                    <tr><td>영문 회사명</td><td><input type="text" name="companyEngName" id="companyEngName" /></td></tr>
                    <tr><td>사업자 등록번호</td><td><input type="text" name="businessNumber" id="businessNumber" /></td></tr>
                    <tr><td>대표자명</td><td><input type="text" name="ceoName" id="ceoName" /></td></tr>
                    <tr><td>설립일</td><td><input type="text" name="establishedDate" id="establishedDate" /></td></tr>
                    <tr><td>회사 형태</td><td><input type="text" name="companyType" id="companyType" /></td></tr>
                    <tr><td>업종</td><td><input type="text" name="industry" id="industry" /></td></tr>
                    <tr><td>대표 전화번호</td><td><input type="text" name="phone" id="phone" /></td></tr>
                    <tr><td>팩스 번호</td><td><input type="text" name="fax" id="fax" /></td></tr>
                    <tr><td>대표 이메일</td><td><input type="text" name="email" id="email" /></td></tr>
                    <tr><td>홈페이지</td><td><input type="text" name="website" id="website" /></td></tr>
                    <tr><td>우편번호</td><td><input type="text" name="postalCode" id="postalCode" /></td></tr>
                    <tr><td>주소</td><td><input type="text" name="address" id="address" /></td></tr>
                    <tr><td>상세 주소</td><td><input type="text" name="addressDetail" id="addressDetail" /></td></tr>
                    <tr><td>상태</td><td><input type="text" name="status" id="status" /></td></tr>

                    </tbody>
                </table>
                <button id="btnGoList" type="button" class="btn btn-primary" onclick="main.goList('company', null);">목록</button>
                <button type="button" class="btn btn-info" onclick="amendData();">${mapping eq 'POST' ? 'Create' : 'Amend'}</button>
            </div>
        </div>

    </form>
</main>

<script type="text/javascript">
    const ID = ${id};
    const MENU = "company";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };

    const init = () => {
        if(!auth.accessTokenCheck()) return false;

        if(Number(ID) > 0) getAmend();
        console.log("amend init");
    };

    /**
     * R.해당 데이터 단일조회 (Read One)
     * @returns {Promise<Data>} 단일 데이터
     */
    const getAmend = async() => {
        $('#loading').show();

        await fetch(API_URL + "/" + ID, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
                //, 'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
            }
        })
            .then(res => res.json())
            .then(result => {

                renderTable(result.data);
            })
            .finally(() => {
                setTimeout(() => $('#loading').hide(), 250);
            })
            .catch(err => console.error("에러:", err));
    };

    /**
     * ETC.그리드(테이블) 생성 함수
     * @param {number} data, tbodyId 데이터 ID
     * @returns {Grid} Grid
     */
    const renderTable = (data) => {

        $("#companyId").val(data.companyId);
        $("#companyName").val(data.companyName);
        $("#companyEngName").val(data.companyEngName);
        $("#businessNumber").val(data.businessNumber);
        $("#ceoName").val(data.ceoName);
        $("#establishedDate").val(data.establishedDate);
        $("#companyType").val(data.companyType);
        $("#industry").val(data.industry);
        $("#phone").val(data.phone);
        $("#fax").val(data.fax);
        $("#email").val(data.email);
        $("#website").val(data.website);
        $("#postalCode").val(data.postalCode);
        $("#address").val(data.address);
        $("#addressDetail").val(data.addressDetail);
        $("#status").val(data.status);
    };

    /**
     * CU.데이터 생성&수정 (Create&Update)
     * @param {number} id 데이터 ID
     * @param {Object} Data 수정할 데이터
     * @returns {Promise<Object>} 수정된 데이터 정보
     */
    const sleep = ms => new Promise(resolve => setTimeout(resolve, ms));

    const amendData = async () => {
        const form = document.getElementById("amendForm");
        const data = Object.fromEntries(new FormData(form).entries());
        const mappingType = data.mapping == "PUT" ? "UPDATE" :
            data.mapping == "POST" ? "NEW" : null;
        // 숫자 필드 변환 및 null 처리
        //data.age = data.age ? parseInt(data.age) : null;
        //data.salary = data.salary ? parseFloat(data.salary) : null;

        const errors = validateData(form, data);
        if (errors.length > 0) {
            alert(errors.join("\n"));
            return;
        }

        $('#loading').show(); // 로딩 표시

        try {
            const res = await fetch(API_URL + `/` + data.companyId, {
                method: data.mapping,
                headers: {
                    "Content-Type": "application/json"
                    //, 'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
                },
                body: JSON.stringify(data)
            });

            if (!res.ok) throw new Error("서버 에러 발생: " + res.status);

            const jsonData = await res.json();
            console.log("응답 updateData JSON:", jsonData);

            // 모달 띄우기
            $('#successModal').modal('show');
            // 모달 안의 버튼에 id 저장
            $('#success-btn').data('id', jsonData.data);
            $('#success-btn').data('categoryId', null);
            $('#success-btn').data('menu', MENU);
        } catch (err) {
            console.error("에러:", err);
            alert(mappingType + " 실패: " + err.message);
        } finally {
            await sleep(250); // 최소 0.25초 로딩 유지
            $('#loading').hide();
        }
    };

    /**
     * ETC.데이터 validate Data Check
     *
     * @returns {errors} 데이터 정보
     */
    const validateData = (form, data) => {
        const errors = [];

        if (!data.mapping || data.mapping.trim() === "") {
            errors.push("mapping 값을 입력하세요.");
        }

        if (!data.companyName || data.companyName.trim() === "") {
            errors.push("회사 이름을 입력하세요.");
        }

        return errors;
    };
</script>
