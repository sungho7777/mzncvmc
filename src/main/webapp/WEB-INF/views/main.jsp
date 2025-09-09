<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko" data-bs-theme="auto">
<head>
    <!-- Head -->
    <%@ include file="include/head.jsp" %> <!-- End of Head -->
</head>
<body id="page-top">
<div id="wrapper">
    <!-- Sidebar -->
    <%@ include file="include/sidebar.jsp" %> <!-- End of Sidebar -->
    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">
        <!-- Main Content -->
        <div id="content">
            <!-- Topbar -->
            <%@ include file="include/topbar.jsp" %> <!-- End of Topbar -->
            <!-- Begin Page Content -->
            <div id="" class="container-fluid">
                <!-- Lodding -->
                <%@ include file="include/lodding.jsp" %> <!-- End of Lodding -->

                <c:if test="${not empty contentPage}">
                    <jsp:include page="${contentPage}"/>
                </c:if>

                <c:if test="${empty contentPage}">
                    <h1 class="h3 mb-4 text-gray-800">Empty ContentPage....</h1>
                </c:if>






            </div> <!-- /.container-fluid -->


        </div> <!-- End of Main Content -->
        <!-- Footer -->
        <%@ include file="include/footer.jsp" %> <!-- End of Footer -->
    </div> <!-- End of Content Wrapper -->
</div> <!-- End of Page Wrapper -->

<!-- Scroll to Top Button-->
<%@ include file="include/scrolltotop.jsp" %>

<!-- Modal-->
<%@ include file="include/modal/logoutModal.jsp" %>
<%@ include file="include/modal/successModal.jsp" %>
<%@ include file="include/modal/deleteModal.jsp" %>
</body>

<script type="text/javascript">





    const goList = (menu) => {
        window.location.href = "/" + menu + "/list";
    };
    const goView = (menu, id) => {
        window.location.href = "/" + menu + "/view/" + id;
    };
    const goAmend = (menu, id, mapping) => {
        window.location.href = "/" + menu + "/amend/" + id + "?mapping=" + mapping;
    };
    const goDelete = (menu, id) => {
        // 모달 띄우기
        $('#deleteModal').modal('show');


        // 모달 안의 버튼에 id 저장
        $('#delete-btn').data('id', id);
    }
</script>
</html>
