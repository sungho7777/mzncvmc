<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="page-header page-header-compact page-header-light border-bottom bg-white mb-4">
    <div class="container-fluid px-4">
        <div class="page-header-content">
            <div class="row align-items-center justify-content-between pt-3">
                <div class="col-auto mb-3">
                    <h1 class="page-header-title">
                        <div class="page-header-icon"><i data-feather="globe"></i></div>
                        Bbs Categories
                    </h1>
                </div>
                <div class="col-12 col-xl-auto mb-3">
                    <a class="btn btn-sm btn-light text-primary" href="#" onclick="main.goList('bbs/bbsCategories', null);">
                        <i class="me-1" data-feather="arrow-left"></i>
                        Back to All Bbs Categories
                    </a>
                </div>
            </div>
        </div>
    </div>
</header>

<div class="container-fluid px-4">
    <input type="hidden" name="categoryId" id="categoryId"  />
    <input type="hidden" name="createdDate" id="createdDate" />
    <input type="hidden" name="updatedDate" id="updatedDate" />

    <div class="row gx-4">
        <div class="col-lg-10">
            <div class="card mb-4">
                <div class="card-header">Category Title</div>
                <div class="card-body">
                    <div id="categoryName" class="bg-light p-4 small"></div>
                </div>
            </div>
            <div class="card card-header-actions mb-4">
                <div class="card-header">
                    Category Preview
                    <i class="text-muted" data-feather="info" data-bs-toggle="tooltip" data-bs-placement="left" title="The Category preview text shows below the post Category, and is the Category summary on blog pages."></i>
                </div>
                <div class="card-body">
                    <div id="description" class="bg-light p-4 small"></div>
                </div>
            </div>
            <div class="card mb-4">
                <div class="card-header">Category Setting</div>
                <div class="card-body">
                    <div class="row gx-3 mb-3">
                        <div class="col-md-6">
                            <label class="small mb-1">Category Code</label>
                            <div id="categoryCode" class="bg-light p-4 small"></div>
                        </div>
                        <div class="col-md-6">
                            <label class="small mb-1">Sort Order</label>
                            <div id="sortOrder" class="bg-light p-4 small"></div>
                        </div>
                    </div>
                    <div class="row gx-3 mb-3">
                        <div class="col-md-6">
                            <label class="small mb-1">활성 여부</label>
                            <div id="isActive" class="bg-light p-4 small"></div>
                        </div>
                        <div class="col-md-6">
                            <label class="small mb-1">익명 게시 허용</label>
                            <div id="allowAnonymous" class="bg-light p-4 small"></div>
                        </div>
                    </div>
                    <div class="row gx-3 mb-3">
                        <div class="col-md-6">
                            <label class="small mb-1">readPermission</label>
                            <div id="readPermission" class="bg-light p-4 small"></div>
                        </div>
                        <div class="col-md-6">
                            <label class="small mb-1">writePermission</label>
                            <div id="writePermission" class="bg-light p-4 small"></div>
                        </div>
                    </div>
                    <div class="row gx-3 mb-3">
                        <div class="col-md-6">
                            <label class="small mb-1">파일 업로드 허용</label>
                            <div id="allowFileUpload" class="bg-light p-4 small"></div>
                        </div>
                        <div class="col-md-6">
                            <label class="small mb-1">maxFileCount</label>
                            <div id="maxFileCount" class="bg-light p-4 small"></div>
                        </div>
                    </div>
                    <div class="row gx-3 mb-3">
                        <div class="col-md-6">
                            <label class="small mb-1">createdBy</label>
                            <div id="createdBy" class="bg-light p-4 small"></div>
                        </div>
                        <div class="col-md-6">
                        </div>
                    </div>
                </div>
            </div>


        </div>
        <div class="col-lg-2">
            <div class="card card-header-actions">
                <div class="card-header">
                    Publish
                    <i class="text-muted" data-feather="info" data-bs-toggle="tooltip" data-bs-placement="left" title="After submitting, your Category will be published once it is approved by a moderator."></i>
                </div>
                <div class="card-body">
                    <div class="d-grid">
                        <button class="fw-500 btn btn-warning" onclick="main.goAmend('bbs/bbsCategories', null, ${id}, 'PUT');">Amend</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    const ID = ${id};
    const MENU = "bbs/bbsCategories";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };
    const init = () => {
        if(!auth.accessTokenCheck()) return false;

        getView();
        console.log("view init");
    }

    /**
     * R.해당 데이터 단일조회 (Read One)
     * @returns {Promise<Data>} 단일 데이터
     */
    const getView = async() => {
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

        $("#categoryId").text(data.categoryId);
        $("#categoryName").text(data.categoryName);
        $("#categoryCode").text(data.categoryCode);
        $("#description").text(data.description);
        $("#sortOrder").text(data.sortOrder);
        $("#isActive").text(data.isActive == true ? "활성" : "비활성");
        $("#allowAnonymous").text(data.allowAnonymous == true ? "허용" : "허용 불가");
        $("#allowFileUpload").text(data.allowFileUpload == true ? "허용" : "허용 불가");
        $("#maxFileCount").text(data.maxFileCount + " 건");
        $("#readPermission").text(data.readPermission);
        $("#writePermission").text(data.writePermission);
        $("#createdDate").text(data.createdDate);
        $("#updatedDate").text(data.updatedDate);
        $("#createdBy").text(data.createdBy);
    };
</script>