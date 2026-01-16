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
                    <a id="goListBtn" class="btn btn-sm btn-light text-primary" href="#">
                        <i className="me-1" data-feather="arrow-left"></i>
                        <label id="goListBtnLabel">-</label>
                    </a>
                    <a id="goAmendBtn" class="btn btn-sm btn-light text-warning" href="#">
                        <label id="goAmendBtnLabel">-</label>
                    </a>
                    <a id="goDeleteBtn" class="btn btn-sm btn-light text-danger" href="#">
                        <label id="goDeleteBtnLabel">-</label>
                    </a>
                </div>
            </div>
        </div>
    </div>
</header>

<!-- Main page content-->
<div class="container-fluid px-4 mt-4">
    <!-- Account page navigation-->
    <nav class="nav nav-borders" id="accountTab" role="tablist">
        <a class="nav-link active ms-0"
           data-bs-toggle="tab"
           href="#tab-profile"
           role="tab">Profile</a>

        <a class="nav-link"
           data-bs-toggle="tab"
           href="#tab-security"
           role="tab">Security</a>

        <a class="nav-link"
           data-bs-toggle="tab"
           href="#tab-notifications"
           role="tab">Notifications</a>
    </nav>
    <hr class="mt-0 mb-4" />

    <div class="tab-content mt-4">
        <!-- Profile -->
        <div class="tab-pane fade show active" id="tab-profile" role="tabpanel">
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
                                <div class="row gx-3 mb-3">
                                    <div class="col-md-6">
                                        <label class="small mb-1" for="email">Email address</label>
                                        <div id="email" class="bg-light p-4 small"></div>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="small mb-1" for="phone">OAuth2.0</label>
                                        <div id="providerId" class="bg-light p-4 small"></div>
                                    </div>
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

                                <button type="button" class="btn btn-warning" onclick="main.goAmend('users', null, ${id}, 'PUT');">Amend</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Security -->
        <div class="tab-pane fade" id="tab-security" role="tabpanel">

            <div class="row">
                <div class="col-lg-8">
                    <!-- Change password card-->
                    <div class="card mb-4">
                        <div class="card-header">Change Password</div>
                        <div class="card-body">
                            <form>
                                <!-- Form Group (current password)-->
                                <div class="mb-3">
                                    <label class="small mb-1" for="currentPassword">Current Password</label>
                                    <input class="form-control" id="currentPassword" type="password" placeholder="Enter current password" />
                                </div>
                                <!-- Form Group (new password)-->
                                <div class="mb-3">
                                    <label class="small mb-1" for="newPassword">New Password</label>
                                    <input class="form-control" id="newPassword" type="password" placeholder="Enter new password" />
                                </div>
                                <!-- Form Group (confirm password)-->
                                <div class="mb-3">
                                    <label class="small mb-1" for="confirmPassword">Confirm Password</label>
                                    <input class="form-control" id="confirmPassword" type="password" placeholder="Confirm new password" />
                                </div>
                                <button type="button" class="btn btn-primary" onclick="_changePassword();">Password Change</button>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4">
                    <!-- Delete account card-->
                    <div class="card mb-4">
                        <div class="card-header">Delete Account</div>
                        <div class="card-body">
                            <p>Deleting your account is a permanent action and cannot be undone. If you are sure you want to delete your account, select the button below.</p>
                            <button class="btn btn-danger-soft text-danger" type="button">I understand, delete my account</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Notifications -->
        <div class="tab-pane fade" id="tab-notifications" role="tabpanel">
            <div class="card">
                <div class="card-header">Notifications</div>
                <div class="card-body">
                    Notifications 설정 화면
                </div>
            </div>
        </div>

    </div>

</div>

<script type="text/javascript">
    const ID = ${id};
    const MENU = "users";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };
    const init = () => {
        if(!auth.accessTokenCheck()) return false;


        getView();

        $("#goListBtnLabel").text("Back to All User List");
        $("#goAmendBtnLabel").text("Amend User");
        $("#goDeleteBtnLabel").text("Delete User");

        $("#goListBtn").attr("onclick", "main.goList('users', null);");
        $("#goAmendBtn").attr("onclick", "main.goAmend('"+MENU+"', null, '"+ID+"', 'POST');");
        $("#goDeleteBtn").attr("onclick", "main.goDelete('"+MENU+"', null, '"+ID+"');");


        console.log("view init");
    }

    /**
     * R.해당 데이터 단일조회 (Read One)
     * @returns {Promise<Data>} 단일 데이터
     */
    const getView = async () => {
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
        $("#providerId").text(data.providerId);
        $("#phone").text(data.phone);
        $("#role").text(data.role);
        $("#status").text(data.status);

        $("#companyView").attr("onclick", "main.goView('company', null, " + data.companyId + ");");
    };

    const _changePassword = async () => {
        const _pwNotifyDuration = localStorage.getItem('pwNotifyDuration');

        $('#loading').show();

        const response = await fetch('/api/account/changePassword', {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
            },
            body: JSON.stringify({
                currentPassword: document.getElementById('currentPassword').value,
                newPassword: document.getElementById('newPassword').value,
                confirmPassword: document.getElementById('confirmPassword').value
            })
        });
        $('#loading').hide();

        const data = await response.json();

        alert(data.message);

        if(data.status == 'success'){

            
        }

/*

        await fetch("/api/account/changePassword", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
            },
            body: JSON.stringify({
                currentPassword: document.getElementById('currentPassword').value,
                newPassword: document.getElementById('newPassword').value,
                confirmPassword: document.getElementById('confirmPassword').value
            })
        })
            .then(res => {
                if (!res.ok) throw new Error('비밀번호 변경 실패');

                localStorage.setItem('pwNotifyDuration', '10');
                if(_pwNotifyDuration == '999'){
                    alert('비밀번호가 변경되었습니다. 다시 로그인하세요.');
                    main.goLogout();
                }
                alert('비밀번호가 변경되었습니다.');
            })
            .then(result => {

                //renderTable(result.data);
            })
            .finally(() => {
                setTimeout(() => $('#loading').hide(), 250);
            })
            .catch(err => console.error("에러:", err));
*/

    };


</script>