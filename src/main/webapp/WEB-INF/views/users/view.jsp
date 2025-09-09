<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<main>
    <p>company : ${user.companyType} ${user.companyName}</p>
    <p>username : ${user.username}</p>
    <p>fullName : ${user.fullName}</p>
    <p>email : ${user.email}</p>
    <p>phone : ${user.phone}</p>


    <p>role : ${user.role}</p>
    <p>status : ${user.status}</p>

</main>