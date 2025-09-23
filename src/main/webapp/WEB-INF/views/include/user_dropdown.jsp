<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<li class="nav-item dropdown no-caret dropdown-user me-3 me-lg-4">
    <a class="btn btn-icon btn-transparent-dark dropdown-toggle" id="navbarDropdownUserImage" href="javascript:void(0);" role="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><img class="img-fluid" src="/common/sbadminpro/assets/img/illustrations/profiles/profile-1.png" /></a>
    <div class="dropdown-menu dropdown-menu-end border-0 shadow animated--fade-in-up" aria-labelledby="navbarDropdownUserImage">
        <h6 class="dropdown-header d-flex align-items-center">
            <img class="dropdown-user-img" src="/common/sbadminpro/assets/img/illustrations/profiles/profile-1.png" />
            <div class="dropdown-user-details">
                <div id="user-dropdown-username" class="dropdown-user-details-name">Valerie Luna</div>
                <div id="user-dropdown-email" class="dropdown-user-details-email">vluna@aol.com</div>
            </div>
        </h6>
        <div class="dropdown-divider"></div>
        <a id="user-dropdown-profile" class="dropdown-item" href="#" onclick="goView();">
            <div class="dropdown-item-icon"><i data-feather="settings"></i></div>
            Account
        </a>
        <a class="dropdown-item" href="#" data-bs-toggle="modal" data-bs-target="#logoutModal">
            <div class="dropdown-item-icon"><i data-feather="log-out"></i></div>
            Logout
        </a>
    </div>
</li>
<script>
    document.addEventListener("DOMContentLoaded", () => {
        const userId = localStorage.getItem('userId');
        const username = localStorage.getItem('username');
        if (userId && username) {
            $("#user-dropdown-username").text(username);
            $("#user-dropdown-email").text("mzncvmc@gmail.com");
            $("#user-dropdown-profile").attr("onclick", "goView('users', null, " + userId + ");");
        }
    });
</script>