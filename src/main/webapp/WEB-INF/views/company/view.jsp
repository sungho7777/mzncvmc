<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>

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
                    <tr><td>ID</td><td id="companyId"></td></tr>
                    <tr><td>회사명</td><td id="companyName"></td></tr>
                    <tr><td>영문 회사명</td><td id="companyEngName"></td></tr>
                    <tr><td>사업자 등록번호</td><td id="businessNumber"></td></tr>
                    <tr><td>대표자명</td><td id="ceoName"></td></tr>
                    <tr><td>설립일</td><td id="establishedDate"></td></tr>
                    <tr><td>회사 형태</td><td id="companyType"></td></tr>
                    <tr><td>업종</td><td id="industry"></td></tr>
                    <tr><td>대표 전화번호</td><td id="phone"></td></tr>
                    <tr><td>팩스 번호</td><td id="fax"></td></tr>
                    <tr><td>대표 이메일</td><td id="email"></td></tr>
                    <tr><td>홈페이지</td><td id="website"></td></tr>
                    <tr><td>우편번호</td><td id="postalCode"></td></tr>
                    <tr><td>주소</td><td id="address"></td></tr>
                    <tr><td>상세 주소</td><td id="addressDetail"></td></tr>
                    <tr><td>상태</td><td id="status"></td></tr>
                </tbody>
            </table>
            <button id="btnGoList" type="button" class="btn btn-primary" onclick="goList('company', null);">목록</button>
            <button id="btnGoAmend" type="button" class="btn btn-warning" onclick="goAmend('company', null, ${id}, 'PUT');">수정</button>
        </div>
    </div>
</main>
<script type="text/javascript">
    const ID = ${id};
    const MENU = "company";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };
    const init = () => {

        getView();
        console.log("view init");
    }
    /**
     * R.해당 데이터 단일조회 (Read One)
     * @returns {Promise<Data>} 단일 데이터
     */
    const getView = async() => {
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

        $("#companyId").text(data.companyId);
        $("#companyName").text(data.companyName);
        $("#companyEngName").text(data.companyEngName);
        $("#businessNumber").text(data.businessNumber);
        $("#ceoName").text(data.ceoName);
        $("#establishedDate").text(data.establishedDate);
        $("#companyType").text(data.companyType);
        $("#industry").text(data.industry);
        $("#phone").text(data.phone);
        $("#fax").text(data.fax);
        $("#email").text(data.email);
        $("#website").text(data.website);
        $("#postalCode").text(data.postalCode);
        $("#address").text(data.address);
        $("#addressDetail").text(data.addressDetail);
        $("#status").text(data.status);
    };

</script>