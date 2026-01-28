<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko" data-bs-theme="auto">
    <head>
        <!-- Head -->
        <%@ include file="include/head.jsp" %> <!-- End of Head -->
    </head>
    <body class="nav-fixed">
        <nav id="sidenavAccordion"
             class="topnav navbar navbar-expand shadow justify-content-between justify-content-sm-start navbar-light bg-white" >
            <!-- Sidenav Toggle Button -->
            <%@ include file="include/sidenav_toggle_button.jsp" %> <!-- End of Sidenav Toggle Button -->
            <!-- Navbar Brand-->
            <%@ include file="include/navbar_brand.jsp" %> <!-- End of Navbar Brand -->
            <!-- Navbar Search Input -->
            <%@ include file="include/navbar_search_input.jsp" %> <!-- End of Navbar Search Input -->
            <!-- Navbar Items-->
            <ul class="navbar-nav align-items-center ms-auto">
                <!-- Documentation Dropdown -->
                <%@ include file="include/documentation_dropdown.jsp" %> <!-- End of Documentation Dropdown -->
                <!-- Navbar Search Dropdown-->
                <%@ include file="include/navbar_search_dropdown.jsp" %> <!-- End of Documentation Navbar Search Dropdown -->
                <!-- Alerts Dropdown-->
                <%@ include file="include/alerts_dropdown.jsp" %> <!-- End of Alerts Dropdown -->
                <!-- Messages Dropdown-->
                <%@ include file="include/messages_dropdown.jsp" %> <!-- End of Messages Dropdown -->
                <!-- User Dropdown-->
                <%@ include file="include/user_dropdown.jsp" %> <!-- End of User Dropdown -->
            </ul>
        </nav>
        <div id="layoutSidenav">
            <div id="layoutSidenav_nav">
                <nav class="sidenav shadow-right sidenav-light">
                    <!-- Sidenav Nav -->
                    <%@ include file="include/sidenav_nav.jsp" %> <!-- End of Sidenav Nav -->
                    <!-- Sidenav Footer -->
                    <%@ include file="include/sidenav_footer.jsp" %> <!-- End of Sidenav Footer -->
                </nav>
            </div>
            <div id="layoutSidenav_content">
                <!-- Lodding -->
                <%@ include file="include/lodding.jsp" %> <!-- End of Lodding -->
                <main>
                    <!-- Main Header -->
                    <!--< % @ include file="include/main_header.jsp" %>  End of Main Header -->

                    <!-- Content Page -->
                    <c:if test="${not empty contentPage}">
                        <jsp:include page="${contentPage}"/>
                    </c:if>

                    <!-- Content Not Page -->
                    <c:if test="${empty contentPage}">
                        <!-- Main page content-->
                        <div class="container-fluid px-4">
                            <div class="card bg-gradient-primary-to-secondary mb-4">
                                <div class="card-body">
                                    <div class="d-flex align-items-center justify-content-between">
                                        <div class="me-3">
                                            <div class="small text-white-50">Organization Name:</div>
                                            <div class="h1 text-white">Start Bootstrap</div>
                                        </div>
                                        <div class="text-white">20 Member(s)</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <script type="text/javascript">

                        </script>


                    </c:if>
                </main>

                <!-- Footer -->
                <%@ include file="include/footer.jsp" %> <!-- End of Footer -->
            </div>
            <!-- Modal-->
            <%@ include file="include/modal/logoutModal.jsp" %>
            <%@ include file="include/modal/successModal.jsp" %>
            <%@ include file="include/modal/deleteModal.jsp" %>
            <%@ include file="include/modal/loginDetailsModal.jsp" %>
            <%@ include file="include/modal/changePasswordModal.jsp" %>
        </div>

        <script src="/common/sbadminpro/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="/common/sbadminpro/js/scripts.js"></script>

        <!-- common.script -->
        <script>
            window.auth = {
                accessToken: "${fn:escapeXml(sessionScope.accessToken)}"
                , pwNotifyDuration: "${fn:escapeXml(sessionScope.loginUser.pwNotifyDuration)}"
            };
        </script>
        <script src="/common/sbadmin/js/domain/main.js"></script>
        <script src="/common/sbadmin/js/domain/file.js"></script>
        <script src="/common/sbadmin/js/domain/auth.js"></script>
        <script src="/common/sbadmin/js/domain/board.js"></script>
        <script src="/common/sbadmin/js/domain/valided.js"></script>

    </body>
</html>
