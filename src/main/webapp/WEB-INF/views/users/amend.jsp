<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
    <form id="amendForm">
        <input type="hidden" name="mapping" value="${mapping}">
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
                    <tr><td>ID</td><td><input type="text" name="userId" id="userId" value="0" /></td></tr>
                    <tr><td>회사</td><td><input type="text" name="companyId" id="companyId" /></td></tr>
                    <tr><td>아이디</td><td><input type="text" name="username" id="username" /></td></tr>
                    <tr><td>이름</td><td><input type="text" name="fullName" id="fullName" /></td></tr>
                    <tr><td>이메일</td><td><input type="text" name="email" id="email" /></td></tr>
                    <tr><td>전화번호</td><td><input type="text" name="phone" id="phone" /></td></tr>
                    <tr><td>룰</td><td><input type="text" name="role" id="role" /></td></tr>
                    <tr><td>상태</td><td><input type="text" name="status" id="status" /></td></tr>
                    </tbody>
                </table>
                <button id="btnGoList" type="button" class="btn btn-primary" onclick="goList('users', null);">목록</button>
                <button type="button" class="btn btn-info" onclick="amendData();">${mapping eq 'POST' ? 'Create' : 'Amend'}</button>
            </div>
        </div>

    </form>
</main>

<script type="text/javascript">
    const ID = ${id};
    const MENU = "users";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };

    const init = () => {
        if(!accessTokenCheck()) return false;

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

        $("#userId").val(data.userId);
        $("#companyId").val(data.companyId);
        $("#username").val(data.username);
        $("#fullName").val(data.fullName);
        $("#email").val(data.email);
        $("#phone").val(data.phone);
        $("#role").val(data.role);
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
            const res = await fetch(API_URL + `/` + data.userId, {
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
            $('#success-btn').data('categoryId', null);
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

        if (!data.username || data.username.trim() === "") {
            errors.push("사용자 ID를 입력하세요.");
        }

        if (!data.fullName || data.fullName.trim() === "") {
            errors.push("사용자 이름을 입력하세요.");
        }

        // 이메일 형식 체크
        if (data.email && !/^[\w.-]+@[\w.-]+\.\w+$/.test(data.email)) {
            errors.push("이메일 형식이 올바르지 않습니다.");
        }

        // 비밀번호는 8자 이상
        if (data.password && data.password.length < 8) {
            errors.push("비밀번호는 8자 이상이어야 합니다.");
        }

        return errors;
    };
</script>
