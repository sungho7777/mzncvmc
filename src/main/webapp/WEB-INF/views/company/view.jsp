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
                    <tr><td>ID</td><td>${company.companyId}</td></tr>
                    <tr><td>회사명</td><td>${company.companyName}</td></tr>
                    <tr><td>영문 회사명</td><td>${company.companyEngName}</td></tr>
                    <tr><td>사업자 등록번호</td><td>${company.businessNumber}</td></tr>
                    <tr><td>대표자명</td><td>${company.ceoName}</td></tr>
                    <tr><td>설립일</td><td>${company.establishedDate}</td></tr>
                    <tr><td>회사 형태</td><td>${company.companyType}</td></tr>
                    <tr><td>업종</td><td>${company.industry}</td></tr>
                    <tr><td>대표 전화번호</td><td>${company.phone}</td></tr>
                    <tr><td>팩스 번호</td><td>${company.fax}</td></tr>
                    <tr><td>대표 이메일</td><td>${company.email}</td></tr>
                    <tr><td>홈페이지</td><td>${company.website}</td></tr>
                    <tr><td>우편번호</td><td>${company.postalCode}</td></tr>
                    <tr><td>주소</td><td>${company.address}</td></tr>
                    <tr><td>상세 주소</td><td>${company.addressDetail}</td></tr>
                    <tr><td>상태</td><td>${company.status}</td></tr>
                </tbody>
            </table>
        </div>
    </div>
</main>
<script type="text/javascript">
    const MENU = "company";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };
    const init = () => {

        console.log("view init");
    }


</script>