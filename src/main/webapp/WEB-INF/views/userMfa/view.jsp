<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<header class="page-header page-header-compact page-header-light border-bottom bg-white mb-4">
    <div class="container-fluid px-4">
        <div class="page-header-content">
            <div class="row align-items-center justify-content-between pt-3">
                <div class="col-auto mb-3">
                    <h1 class="page-header-title">
                        <div class="page-header-icon"><i data-feather="user"></i></div>
                        userMfa - View
                    </h1>
                </div>
                <div class="col-12 col-xl-auto mb-3">
                    <a id="goListBtn" class="btn btn-sm btn-light text-primary" href="#">
                        <i className="me-1" data-feather="arrow-left"></i>
                        <label id="goListBtnLabel">-</label>
                    </a>
                </div>
            </div>
        </div>
    </div>
</header>

<!-- Main page content-->
<div class="container-fluid px-4 mt-4">
    <input type="text" name="id" id="id"  />

    <div class="row gx-4">
        <div class="col-lg-12">
            <div class="card mb-4">
                <div class="card-header">User Id</div>
                <div class="card-body">
                    <div id="userId" class="bg-light p-4 small"></div>
                </div>
            </div>
            <div class="card mb-4">
                <div class="card-header">Mfa Enabled</div>
                <div class="card-body">
                    <div id="mfaEnabled" class="bg-light p-4 small"></div>
                </div>
            </div>
            <div class="card mb-4">
                <div class="card-header">Mfa Secret</div>
                <div class="card-body">
                    <div id="mfaSecret" class="bg-light p-4 small"></div>
                </div>
            </div>
            <div class="card mb-4">
                <div class="card-header">Mfa Verified</div>
                <div class="card-body">
                    <div id="mfaVerified" class="bg-light p-4 small"></div>
                </div>
            </div>


        </div>
    </div>
</div>

<script type="text/javascript">
    const ID = ${id};
    const MENU = "userMfa";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };
    const init = () => {
        if(!auth.accessTokenCheck()) return false;


        getView();

        $("#goListBtnLabel").text("Back to All UserMfa List");
        $("#goDeleteBtnLabel").text("Delete UserMfa");

        $("#goListBtn").attr("onclick", "main.goList('userMfa', null);");
        $("#goDeleteBtn").attr("onclick", "main.goDelete('"+MENU+"', null, '"+ID+"');");


        console.log("view init");
    }

    /**
     * R.해당 데이터 단일조회 (Read One)
     * @returns {Promise<Data>} 단일 데이터
     */
    const getView = async () => {
        $('#loading').show();

        await fetch(API_URL + "/" + ID, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
            }
        })
            .then(res => res.json())
            .then(result => {

                renderTable(result.data);
            })
            .finally(() => {
                setTimeout(() => $('#loading').hide(), 250);
            })
            .catch(err => console.error("에러:", err));
    };

    /**
     * ETC.그리드(테이블) 생성 함수
     * @param {number} data, tbodyId 데이터 ID
     * @returns {Grid} Grid
     */
    const renderTable = (data) => {

        $("#id").val(data.id);
        $("#userId").text(data.userId);
        $("#mfaEnabled").text(data.mfaEnabled);
        $("#mfaSecret").text(data.mfaSecret);
        $("#mfaVerified").text(data.mfaVerified);

    };
</script>