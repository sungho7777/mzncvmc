<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<header class="page-header page-header-compact page-header-light border-bottom bg-white mb-4">
    <div class="container-fluid px-4">
        <div class="page-header-content">
            <div class="row align-items-center justify-content-between pt-3">
                <div class="col-auto mb-3">
                    <h1 class="page-header-title">
                        <div class="page-header-icon"><i data-feather="user"></i></div>
                        User - Profile
                    </h1>
                </div>
                <div class="col-12 col-xl-auto mb-3">
                    <a class="btn btn-sm btn-light text-primary" href="#" onclick="goList('users', null);">
                        <i class="me-1" data-feather="arrow-left"></i>
                        Back to All User List
                    </a>
                </div>
            </div>
        </div>
    </div>
</header>

<!-- Main page content-->
<div class="container-fluid px-4 mt-4">
    <!-- Account page navigation-->
    <nav class="nav nav-borders">
        <a class="nav-link active ms-0" href="#">Profile</a>
        <a class="nav-link" href="#">Billing</a>
        <a class="nav-link" href="#">Security</a>
        <a class="nav-link" href="#">Notifications</a>
    </nav>
    <hr class="mt-0 mb-4" />
    <div class="row">
        <div class="col-xl-4">
            <!-- Profile picture card-->
            <div class="card mb-4 mb-xl-0">
                <div class="card-header">Profile Picture</div>
                <div class="card-body text-center">
                    <!-- Profile picture image-->
                    <img class="img-account-profile rounded-circle mb-2" src="/common/sbadminpro/assets/img/illustrations/profiles/profile-1.png" alt="" />
                    <!-- Profile picture help block-->
                    <div class="small font-italic text-muted mb-4">JPG or PNG no larger than 5 MB</div>
                    <!-- Profile picture upload button-->
                    <button class="btn btn-primary" type="button">Upload new image</button>
                </div>
            </div>
        </div>
        <div class="col-xl-8">
            <!-- Account details card-->
            <div class="card mb-4">
                <div class="card-header">Account Details</div>
                <div class="card-body">
                    <form id="amendForm">
                        <input type="hidden" name="mapping" value="${mapping}">
                        <input type="hidden" name="userId" id="userId" value="0" />
                        <input type="hidden" name="companyId" id="companyId" value="0" />
                        <input type="hidden" name="status" id="status" value="ACTIVE" />

                        <div class="mb-3">
                            <label class="small mb-1" for="username">Username (how your name will appear to other users on the site)</label>
                            <div id="username" class="bg-light p-4 small"></div>
                        </div>
                        <!-- Form Row-->
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                <label class="small mb-1" for="companyId">Company</label>
                                <div id="companyName" class="bg-light p-4 small"></div>
                            </div>
                            <div class="col-md-6">
                                <label class="small mb-1" for="fullName">Full Name</label>
                                <div id="fullName" class="bg-light p-4 small"></div>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label class="small mb-1" for="email">Email address</label>
                            <div id="email" class="bg-light p-4 small"></div>
                        </div>
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                <label class="small mb-1" for="phone">Phone number</label>
                                <div id="phone" class="bg-light p-4 small"></div>
                            </div>
                            <div class="col-md-6">
                                <label class="small mb-1" for="role">Role</label>
                                <div id="role" class="bg-light p-4 small"></div>
                            </div>
                        </div>

                        <button type="button" class="btn btn-warning" onclick="goAmend('users', null, ${id}, 'PUT');">Amend</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<%--

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
--%>

<script type="text/javascript">
    const ID = ${id};
    const MENU = "users";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };
    const init = () => {
        if(!accessTokenCheck()) return false;


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