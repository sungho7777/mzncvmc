<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko" data-bs-theme="auto">
<head>
    <!-- Head -->
    <%@ include file="../include/head.jsp" %> <!-- End of Head -->
</head>
<body id="page-top">
<div id="wrapper">
    <!-- Sidebar -->
    <%@ include file="../include/sidebar.jsp" %> <!-- End of Sidebar -->
    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">
        <!-- Main Content -->
        <div id="content">
            <!-- Topbar -->
            <%@ include file="../include/topbar.jsp" %> <!-- End of Topbar -->
            <!-- Begin Page Content -->
            <div class="container-fluid">




                <!-- Page Heading -->
                <h1 class="h3 mb-4 text-gray-800">Blank Page....</h1>




            </div> <!-- /.container-fluid -->


        </div> <!-- End of Main Content -->
        <!-- Footer -->
        <%@ include file="../include/footer.jsp" %> <!-- End of Footer -->
    </div> <!-- End of Content Wrapper -->
</div> <!-- End of Page Wrapper -->

<!-- Scroll to Top Button-->
<%@ include file="../include/scrolltotop.jsp" %>

<!-- Logout Modal-->
<%@ include file="../include/modal/logoutModal.jsp" %>
</body>

<script type="text/javascript">
    window.onload = function() {
        init();

    };

    const init = () => {

        console.log("init");
    }

</script>
</html>
