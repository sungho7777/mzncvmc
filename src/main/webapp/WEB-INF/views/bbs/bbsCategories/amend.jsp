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

<!-- Main page content-->
<form id="amendForm">

    <div class="container-fluid px-4">
        <input type="hidden" name="mapping" value="${mapping}">
        <input type="hidden" name="categoryId" id="categoryId" value="0" />
        <input type="hidden" name="createdDate" id="createdDate" />
        <input type="hidden" name="updatedDate" id="updatedDate" />

        <div class="row gx-4">
            <div class="col-lg-10">
                <div class="card mb-4">
                    <div class="card-header">Category Title</div>
                    <div class="card-body">
                        <input name="categoryName" id="categoryName" class="form-control" type="text" placeholder="Enter your Category title..." />
                    </div>
                </div>
                <div class="card card-header-actions mb-4">
                    <div class="card-header">
                        Category Preview
                        <i class="text-muted" data-feather="info" data-bs-toggle="tooltip" data-bs-placement="left" title="The Category preview text shows below the post Category, and is the Category summary on blog pages."></i>
                    </div>
                    <div class="card-body">
                        <textarea name="description" id="description" class="lh-base form-control" type="text" placeholder="Enter your Category preview text..." rows="6"></textarea>
                    </div>
                </div>
                <div class="card mb-4">
                    <div class="card-header">Category Setting</div>
                    <div class="card-body">
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                <label class="small mb-1">Category Code</label>
                                <input name="categoryCode" id="categoryCode" value="CATEGORY_CODE"
                                       class="form-control" type="text" placeholder="Enter your Category Code title..." />
                            </div>
                            <div class="col-md-6">
                                <label class="small mb-1">Sort Order</label>
                                <input name="sortOrder" id="sortOrder" value="0"
                                       class="form-control" type="text" placeholder="Enter your Sort Order title..." />
                            </div>
                        </div>
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                <label class="small mb-1">활성 여부</label>
                                <select name="isActive" id="isActive" class="form-select" aria-label="Default select Active">
                                    <option value="true" selected>활성</option>
                                    <option value="false">비활성</option>
                                </select>
                            </div>
                            <div class="col-md-6">

                                <label class="small mb-1">익명 게시 허용</label>
                                <select name="allowAnonymous" id="allowAnonymous" class="form-select" aria-label="Default select Allow Anonymous">
                                    <option value="true">허용</option>
                                    <option value="false" selected>허용 불가</option>
                                </select>
                            </div>
                        </div>
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                <label class="small mb-1">readPermission</label>
                                <input name="readPermission" id="readPermission" value="ALL"
                                       class="form-control" type="text" placeholder="Enter your readPermission title..." />
                            </div>
                            <div class="col-md-6">
                                <label class="small mb-1">writePermission</label>
                                <input name="writePermission" id="writePermission" value="USER"
                                       class="form-control" type="text" placeholder="Enter your writePermission title..." />
                            </div>
                        </div>
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                <label class="small mb-1">파일 업로드 허용</label>
                                <select name="allowFileUpload" id="allowFileUpload" class="form-select" aria-label="Default select Allow File Upload">
                                    <option value="true" selected>허용</option>
                                    <option value="false">허용 불가</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="small mb-1">maxFileCount</label>
                                <input name="maxFileCount" id="maxFileCount" value="3"
                                       class="form-control" type="text" placeholder="Enter your maxFileCount title..." />
                            </div>
                        </div>
                        <div class="row gx-3 mb-3">
                            <div class="col-md-6">
                                <label class="small mb-1">createdBy</label>
                                <input name="createdBy" id="createdBy" class="form-control" type="text" placeholder="Enter your createdBy title..." />
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
                            <button class="fw-500 btn btn-primary" onclick="amendData();">Submit for Approval (${mapping eq 'POST' ? 'Create' : 'Amend'})</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>

<script type="text/javascript">
    const ID = ${id};
    const MENU = "bbs/bbsCategories";
    const API_URL = "/api/" + MENU;

    window.onload = function() {

        init();
    };

    const init = () => {
        if(!auth.accessTokenCheck()) return false;


        //const userId = localStorage.getItem('userId');
        //$("#createdBy").val(userId);

        if(Number(ID) > 0) getAmend();
        console.log("amend init");
    };

    /**
     * R.해당 데이터 단일조회 (Read One)
     * @returns {Promise<Data>} 단일 데이터
     */
    const getAmend = async() => {
        $('#loading').show();

        await fetch(API_URL + "/" + ID, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
                //, 'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
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

        $("#categoryId").val(data.categoryId);
        $("#categoryName").val(data.categoryName);
        $("#categoryCode").val(data.categoryCode);
        $("#description").val(data.description);
        $("#sortOrder").val(data.sortOrder);
        $("#isActive").val(data.isActive == true ? "true" : "false");
        $("#allowAnonymous").val(data.allowAnonymous == true ? "true" : "false");
        $("#allowFileUpload").val(data.allowFileUpload == true ? "true" : "false");
        $("#maxFileCount").val(data.maxFileCount);
        $("#readPermission").val(data.readPermission);
        $("#writePermission").val(data.writePermission);
        $("#createdDate").val(data.createdDate);
        $("#updatedDate").val(data.updatedDate);
        $("#createdBy").val(data.createdBy);

    };

    /**
     * CU.데이터 생성&수정 (Create&Update)
     * @param {number} id 데이터 ID
     * @param {Object} Data 수정할 데이터
     * @returns {Promise<Object>} 수정된 데이터 정보
     */
    const sleep = ms => new Promise(resolve => setTimeout(resolve, ms));

    const amendData = async () => {
        const form = document.getElementById("amendForm");
        const data = Object.fromEntries(new FormData(form).entries());
        const mappingType = data.mapping == "PUT" ? "UPDATE" :
                            data.mapping == "POST" ? "NEW" : null;

        const errors = validateData(form, data);
        if (errors.length > 0) {
            alert(errors.join("\n"));
            return;
        }

        $('#loading').show(); // 로딩 표시

        try {
            const res = await fetch(API_URL + `/` + data.categoryId, {
                method: data.mapping,
                headers: {
                    "Content-Type": "application/json"
                    //, 'Authorization': 'Bearer ' + localStorage.getItem('accessToken')
                },
                body: JSON.stringify(data)
            });

            if (!res.ok) throw new Error("서버 에러 발생: " + res.status);

            const jsonData = await res.json();
            console.log("응답 updateData JSON:", jsonData);

            goView(MENU, null, jsonData.data);
        } catch (err) {
            console.error("에러:", err);
            alert(mappingType + " 실패: " + err.message);
        } finally {
            await sleep(250); // 최소 0.25초 로딩 유지
            $('#loading').hide();
        }
    };

    /**
     * ETC.데이터 validate Data Check
     *
     * @returns {errors} 데이터 정보
     */
    const validateData = (form, data) => {
        const errors = [];

        if (!data.mapping || data.mapping.trim() === "") {
            errors.push("mapping 값을 입력하세요.");
        }

        if (!data.categoryName || data.categoryName.trim() === "") {
            errors.push("카테고리명을 입력하세요.");
        }

        return errors;
    };
</script>
