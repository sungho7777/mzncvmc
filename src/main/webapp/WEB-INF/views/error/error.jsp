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
    <!-- Content Wrapper -->
    <div id="content-wrapper" class="d-flex flex-column">
        <!-- Main Content -->
        <div id="content">
            <!-- Begin Page Content -->
            <div class="container-fluid">




                <!-- 404 Error Text -->
                <div class="text-center">
                    <div class="error mx-auto" data-text="Error">Error</div>
                    <p class="lead text-gray-800 mb-5">Page Not Found</p>
                    <p class="text-gray-500 mb-0">It looks like you found a glitch in the matrix...</p>
                    <a href="/main">&larr; Back to Main Page...</a>
                </div>




            </div> <!-- /.container-fluid -->


        </div> <!-- End of Main Content -->
    </div> <!-- End of Content Wrapper -->
</div> <!-- End of Page Wrapper -->
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
