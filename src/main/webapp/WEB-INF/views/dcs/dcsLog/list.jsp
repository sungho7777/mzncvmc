<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="page-header page-header-compact page-header-light border-bottom bg-white mb-4">
    <div class="container-fluid px-4">
        <div class="page-header-content">
            <div class="row align-items-center justify-content-between pt-3">
                <div class="col-auto mb-3">
                    <h1 class="page-header-title">
                        <div class="page-header-icon"><i data-feather="globe"></i></div>
                        <label id="page-header-title">-</label>
                    </h1>
                </div>
                <div class="col-12 col-xl-auto mb-3">
                    <a id="goAmendBtn" class="btn btn-sm btn-light text-primary" href="#">
                        <label id="goAmendBtnLabel">-</label>
                    </a>
                    <a id="goExcelBtn" class="btn btn-sm btn-light text-teal" href="#">
                        <label id="goExcelBtnLabel">Excel Download</label>
                    </a>
                    <a id="goExcelBtn" class="btn btn-sm btn-light text-success" href="#">
                        <label id="goCsvBtnLabel">CSV Download</label>
                    </a>
                </div>
            </div>
        </div>
    </div>
</header>

<!-- Main page content-->
<div class="container-fluid px-4">
    <div class="card">
        <div class="card-body">
            <div class="datatable-wrapper datatable-loading no-footer sortable searchable fixed-columns">
                <div class="datatable-top">
                </div>
                <div class="datatable-top">
                    <div class="datatable-dropdown">
                        <label>
                            <select id="pageSize" class="datatable-selector">
                                <option value="5">5</option>
                                <option value="10" selected>10</option>
                                <option value="25">25</option>
                                <option value="50">50</option>
                                <option value="100">100</option>
                            </select>
                            entries per page
                        </label>
                    </div>

                    <div class="datatable-search">
                        <div class="input-group">
                            <input id="searchBox"
                                   class="datatable-input" type="search" placeholder="Search..." title="Search within table" aria-controls="datatablesSimple" />
                            <button class="btn btn-primary" type="button" onclick="getList();">Search</button>
                        </div>
                    </div>
                </div>
                <div class="datatable-container">
                    <table id="datatablesSimple" class="datatable-table">
                        <thead>
                        <tr>
                            <th>순서</th>
                            <th>user_id</th>
                            <th>mfa_enabled</th>
                            <th>mfa_secret</th>
                            <th>mfa_verified</th>
                            <th>failed_Attempts</th>
                            <th>비고</th>
                        </tr>
                        </thead>
                        <tbody id="grid" />
                        <tr>
                            <td colspan="7" class="text-center">The data you searched for does not exist.</td>
                        </tr>
                        </tbody>
                    </table>
                </div>


                <div id="" class="datatable-bottom">

                    <div id="summary" class="datatable-info"></div><!-- Total count -->
                    <div id="pagination" class="pagination"></div><!-- Page item -->
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    const MENU = "dcs/dcsLog";
    const API_URL = "/api/" + MENU;
    window.onload = function() {

        init();
    };
    const init = () => {
        if(!auth.accessTokenCheck()) return false;


        $("#page-header-title").text("dcsLog list");
        $("#goAmendBtn").text("Add New DCS LogAnalyze");
        $("#goExcelBtnLabel").text("Excel Download");
        $("#goCsvBtnLabel").text("CSV Download");

        //
        $("#goAmendBtn").attr("onclick", "goLogAnalyze('1', '20260114');");
        console.log("init");
    }

    const goLogAnalyze = async(serverNo, logDate) => {
        console.log(serverNo);
        console.log(logDate);

        $('#loading').show();
        const response = await fetch('/api/dcs/dcsLog/dcsLogAnalyze', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
            },
            body: JSON.stringify({
                serverNo: serverNo,
                logDate: logDate
            })
        });

        const data = await response.json();

        console.log(data);


        setTimeout(() => $('#loading').hide(), 250);
    };


</script>