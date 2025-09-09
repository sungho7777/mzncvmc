<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko" data-bs-theme="auto">
<head>
    <%@ include file="layout/head.jsp" %>
</head>
<body class="bg-body-tertiary">
    <%@ include file="layout/nav.jsp" %>
    <%@ include file="layout/secondnav.jsp" %>







    <main class="container">
        <div class="d-flex align-items-center p-3 my-3 text-white bg-purple rounded shadow-sm" >
            <img class="me-3" src="/common/assets/brand/bootstrap-logo-white.svg" alt="" width="48" height="38" />
            <div class="lh-1">
                <h1 class="h6 mb-0 text-white lh-1">Bootstrap</h1>
                <small>Since 2011</small>
            </div>
        </div>
        <div class="my-3 p-3 bg-body rounded shadow-sm">
            <h6 class="border-bottom pb-2 mb-0">Sample...</h6>


        </div>




        <%@ include file="layout/footer.jsp" %>
    </main>



</body>
</html>
