<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko" data-bs-theme="auto">
    <head>
        <meta charset="UTF-8">
        <title>Error Page</title>
        <link href="/common/assets/dist/css/bootstrap.min.css" rel="stylesheet" />
    </head>
    <body class="align-items-center" style="height:100vh;">


        <div class="my-5">
            <div class="p-5 text-center bg-body-tertiary">
                <div class="container py-5">
                    <h1 class="text-body-emphasis">This page does not exist.</h1>
                    <p class="col-lg-8 mx-auto lead">
                        This takes the basic jumbotron above and makes its background edge-to-edge with a <code>.container</code> inside to align content. Similar to above, it's been recreated with built-in grid and utility classes.
                        <br>
                        <br>
                        You will be redirected to the main page in <code>3 seconds.</code>
                        <br>
                        <br>
                        ${code} ${msg}
                        <br>
                        ${timestamp}
                    </p>
                </div>
            </div>
        </div>
        <script type="text/javascript">
            setTimeout(function() {
              window.location.href = '/main';
            }, 3000);
        </script>
    </body>
</html>
