<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko" data-bs-theme="auto">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Signin Template · Bootstrap v5.3</title>
    <link rel="shortcut icon" href="<c:url value='/favicon.ico'/>">

    <script src="/common/assets/js/color-modes.js"></script>
    <link href="/common/assets/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="/common/assets/sign-in.css" rel="stylesheet" />

</head>
<body class="d-flex align-items-center py-4 bg-body-tertiary">

<main class="form-signin w-100 m-auto">
    <form action="/login" method="post">
        <c:if test="${not empty errorMsg}">
            <h5><spring:message code="error.login"/></h5>
        </c:if>
        <h1 class="h3 mb-3 fw-normal">Please sign in</h1>
        <div class="form-floating">
            <input
                    type="email"
                    class="form-control"
                    id="userid"
                    name="userid"
                    placeholder="name@example.com"
            />
            <label for="floatingInput">Email address</label>
        </div>
        <div class="form-floating">
            <input
                    type="password"
                    class="form-control"
                    id="password"
                    name="password"
                    placeholder="Password"
            />
            <label for="floatingPassword">Password</label>
        </div>
        <div class="form-check text-start my-3">
            <input
                    class="form-check-input"
                    type="checkbox"
                    value="remember-me"
                    id="checkDefault"
            />
            <label class="form-check-label" for="checkDefault">
                Remember me
            </label>
        </div>
        <button class="btn btn-primary w-100 py-2" type="submit">
            Sign in
        </button>
        <p class="mt-5 mb-3 text-body-secondary">&copy; 2017–2025</p>
    </form>
</main>
<script
        src="/common/assets/dist/js/bootstrap.bundle.min.js"
        class="astro-vvvwv3sm"
></script>
</body>
</html>