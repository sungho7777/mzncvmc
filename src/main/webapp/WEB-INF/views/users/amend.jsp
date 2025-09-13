<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
    <form id="amendForm">
        <input type="hidden" name="mapping" value="${mapping}">
        <input type="hidden" name="userId" value="${empty user.userId ? 0 : user.userId}">
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
                    <tr><td>회사</td><td><input type="text" name="companyId" value="${user.companyId}" /></td></tr>
                    <tr><td>아이디</td><td><input type="text" name="username" value="${user.username}" /></td></tr>
                    <tr><td>이름</td><td><input type="text" name="fullName" value="${user.fullName}" /></td></tr>
                    <tr><td>이메일</td><td><input type="text" name="email" value="${user.email}" /></td></tr>
                    <tr><td>전화번호</td><td><input type="text" name="phone" value="${user.phone}" /></td></tr>
                    <tr><td>룰</td><td><input type="text" name="role" value="${user.role}" /></td></tr>
                    <tr><td>상태</td><td><input type="text" name="status" value="${user.status}" /></td></tr>
                    </tbody>
                </table>
            </div>
        </div>
        <button type="button" onclick="amendData();">${empty user.userId ? 'Create' : 'Amend'}</button>

    </form>
</main>

<script type="text/javascript">
    const MENU = "users";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };

    const init = () => {

        console.log("amend init");
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
