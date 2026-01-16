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
                        DCS - Log
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
    <div class="row gx-4">
        <div class="col-lg-12">
            <form id="amendForm">
                <div class="card mb-4">
                    <div class="card-header">Server No</div>
                    <div class="card-body">
                        <input class="form-control" id="serverNo" name="serverNo" type="text" placeholder="Enter server No" value="1" />
                    </div>
                </div>
                <div class="card mb-4">
                    <div class="card-header">Log Date</div>
                    <div class="card-body">
                        <input class="form-control" id="logDate" name="logDate" type="text" placeholder="Enter log Date" value="20260112" />
                    </div>
                </div>

                <button type="button" class="btn btn-primary" onclick="amendData();">${mapping eq 'POST' ? 'Create' : 'Amend'}</button>
            </form>

        </div>
    </div>
</div>

<script type="text/javascript">
    const ID = ${id};
    const MENU = "dcs/dcsLog";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };

    const init = () => {
        if(!auth.accessTokenCheck()) return false;

        //if(Number(ID) > 0) getAmend();

        $("#goListBtnLabel").text("Back to All Dcs Log List");

        $("#goListBtn").attr("onclick", "main.goList('dcs/dcsLog', null);");

        console.log("amend init");
    };


    /**
     * CU.데이터 생성&수정 (Create&Update)
     * @param {number} id 데이터 ID
     * @param {Object} Data 수정할 데이터
     * @returns {Promise<Object>} 수정된 데이터 정보
     */
    const sleep = ms => new Promise(resolve => setTimeout(resolve, ms));

    // 데이터 생성
    const amendData = async () => {
        const form = document.getElementById("amendForm");
        const data = Object.fromEntries(new FormData(form).entries());

        $('#loading').show(); // 로딩 표시

        try {
            const res = await fetch(API_URL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                    //, 'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
                },
                body: JSON.stringify(data)
            });

            if (!res.ok) throw new Error("서버 에러 발생: " + res.status);

            const jsonData = await res.json();
            console.log("응답 updateData JSON:", jsonData);


        } catch (err) {
            console.error("에러:");
        } finally {
            await sleep(250); // 최소 0.25초 로딩 유지
            $('#loading').hide();
        }
    };

</script>
