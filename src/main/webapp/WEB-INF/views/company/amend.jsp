<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
    <form id="amendForm">
        <button type="button" onclick="testData();">TestData</button><br>
        <br>
        <input type="text" name="mapping" value="${mapping}"><br>
        <input type="text" name="companyId" value="${empty company.companyId ? 0 : company.companyId}"><br>

        <br>
        <label for="companyName">회사명</label>
        <input type="text" name="companyName" value="${company.companyName}" /><br>


        <label for="status">상태</label>
        <input type="text" name="status" value="${company.status}" /><br>

        <button type="button" onclick="amendData();">${empty company.companyId ? 'Create' : 'Amend'}</button>
    </form>
</main>

<script type="text/javascript">
    const MENU = "company";
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
            const res = await fetch(API_URL + `/` + data.companyId, {
                method: data.mapping,
                headers: { "Content-Type": "application/json" },
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

        if (!data.companyName || data.companyName.trim() === "") {
            errors.push("회사 이름을 입력하세요.");
        }

        return errors;
    };
</script>
