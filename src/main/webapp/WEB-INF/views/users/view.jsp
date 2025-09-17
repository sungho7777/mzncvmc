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
                <tr><td>ID</td><td id="userId"></td></tr>
                <tr><td>회사</td><td id="companyName"></td></tr>
                <tr><td>아이디</td><td id="username"></td></tr>
                <tr><td>이름</td><td id="fullName"></td></tr>
                <tr><td>이메일</td><td id="email"></td></tr>
                <tr><td>전화번호</td><td id="phone"></td></tr>
                <tr><td>룰</td><td id="role"></td></tr>
                <tr><td>상태</td><td id="status"></td></tr>
                <tr>
                    <td>바로가기</td>
                    <td>

                        <a id="companyView" href="#" class="btn btn-info" style="margin-right: 5px;" >
                            <i class="fas fa-info-circle"></i>
                        </a>
                    </td>
                </tr>
                </tbody>
            </table>
            <button id="btnGoList" type="button" class="btn btn-primary" onclick="goList('users', null);">목록</button>
            <button id="btnGoAmend" type="button" class="btn btn-warning" onclick="goAmend('users', null, ${id}, 'PUT');">수정</button>
        </div>
    </div>
</main>
<script type="text/javascript">
    const ID = ${id};
    const MENU = "users";
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

        $("#userId").text(data.userId);
        $("#companyName").text(data.companyName);
        $("#username").text(data.username);
        $("#fullName").text(data.fullName);
        $("#email").text(data.email);
        $("#phone").text(data.phone);
        $("#role").text(data.role);
        $("#status").text(data.status);

        $("#companyView").attr("onclick", "goView('company', null, " + data.companyId + ");");
    };
</script>