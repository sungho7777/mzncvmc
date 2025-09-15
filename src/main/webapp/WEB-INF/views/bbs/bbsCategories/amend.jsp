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
                    <tr><td>ID</td><td><input type="text" name="categoryId" id="categoryId" value="0" /></td></tr>
                    <tr><td>카테고리명</td><td><input type="text" name="categoryName" id="categoryName" /></td></tr>
                    <tr><td>카테고리 코드</td><td><input type="text" name="categoryCode" id="categoryCode" /></td></tr>
                    <tr><td>카테고리 설명</td><td><input type="text" name="description" id="description" /></td></tr>
                    <tr><td>정렬 순서</td><td><input type="text" name="sortOrder" id="sortOrder" /></td></tr>
                    <tr><td>활성 여부</td><td><input type="text" name="isActive" id="isActive" /></td></tr>
                    <tr><td>익명 게시 허용</td><td><input type="text" name="allowAnonymous" id="allowAnonymous" /></td></tr>
                    <tr><td>파일 업로드 허용</td><td><input type="text" name="allowFileUpload" id="allowFileUpload" /></td></tr>
                    <tr><td>최대 파일 개수</td><td><input type="text" name="maxFileCount" id="maxFileCount" /></td></tr>
                    <tr><td>읽기 권한</td><td><input type="text" name="readPermission" id="readPermission" /></td></tr>
                    <tr><td>쓰기 권한</td><td><input type="text" name="writePermission" id="writePermission" /></td></tr>
                    <tr><td>생성일</td><td><input type="text" name="createdDate" id="createdDate" /></td></tr>
                    <tr><td>수정일</td><td><input type="text" name="updatedDate" id="updatedDate" /></td></tr>
                    <tr><td>생성자 ID</td><td><input type="text" name="createdBy" id="createdBy" /></td></tr>

                    </tbody>
                </table>
                <button type="button" class="btn btn-primary" onclick="goList('bbs/bbsCategories');">목록</button>
                <button type="button" class="btn btn-info" onclick="amendData();">${mapping eq 'POST' ? 'Create' : 'Amend'}</button>
            </div>
        </div>

    </form>
</main>

<script type="text/javascript">
    const ID = ${id};
    const MENU = "bbs/bbsCategories";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };

    const init = () => {

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
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
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

        $("#categoryId").val(data.categoryId);
        $("#categoryName").val(data.categoryName);
        $("#categoryCode").val(data.categoryCode);
        $("#description").val(data.description);
        $("#sortOrder").val(data.sortOrder);
        $("#isActive").val(data.isActive);
        $("#allowAnonymous").val(data.allowAnonymous);
        $("#allowFileUpload").val(data.allowFileUpload);
        $("#maxFileCount").val(data.maxFileCount);
        $("#readPermission").val(data.readPermission);
        $("#writePermission").val(data.writePermission);
        $("#createdDate").val(data.createdDate);
        $("#updatedDate").val(data.updatedDate);
        $("#createdBy").val(data.createdBy);

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

        const errors = validateData(form, data);
        if (errors.length > 0) {
            alert(errors.join("\n"));
            return;
        }

        $('#loading').show(); // 로딩 표시

        try {
            const res = await fetch(API_URL + `/` + data.categoryId, {
                method: data.mapping,
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
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
            $('#success-btn').data('menu', MENU);

            //alert(mappingType + " 완료");

            //goView(MENU, jsonData.data);
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

        if (!data.categoryName || data.categoryName.trim() === "") {
            errors.push("카테고리명을 입력하세요.");
        }

        return errors;
    };
</script>
