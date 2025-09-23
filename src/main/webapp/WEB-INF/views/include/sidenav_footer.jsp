<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Sidenav Footer-->
<div class="sidenav-footer">
    <div class="sidenav-footer-content">
        <div class="sidenav-footer-subtitle">Logged in as:</div>
        <div id="sidenav-footer-username" class="sidenav-footer-title">Valerie Luna</div>
    </div>
</div>
<script>
    document.addEventListener("DOMContentLoaded", () => {
        const userId = localStorage.getItem('userId');
        const username = localStorage.getItem('username');
        if (userId && username) {
            $("#sidenav-footer-username").text(username);
        }
    });
</script>