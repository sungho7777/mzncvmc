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
                <tr><td>ID</td><td>${user.userId}</td></tr>
                <tr><td>회사</td><td>${user.companyType} ${user.companyName}</td></tr>
                <tr><td>아이디</td><td>${user.username}</td></tr>
                <tr><td>이름</td><td>${user.fullName}</td></tr>
                <tr><td>이메일</td><td>${user.email}</td></tr>
                <tr><td>전화번호</td><td>${user.phone}</td></tr>
                <tr><td>룰</td><td>${user.role}</td></tr>
                <tr><td>상태</td><td>${user.status}</td></tr>
                </tbody>
            </table>
        </div>
    </div>
</main>
<script type="text/javascript">
    const MENU = "users";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };
    const init = () => {

        console.log("view init");
    }


</script>