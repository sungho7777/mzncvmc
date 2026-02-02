<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
    .menu-tree {
        border: 1px solid #dee2e6;
        border-radius: 4px;
        padding: 15px;
        background-color: #f8f9fa;
        max-height: 600px;
        overflow-y: auto;
    }
    .menu-item {
        padding: 8px 12px;
        margin: 4px 0;
        background-color: white;
        border: 1px solid #dee2e6;
        border-radius: 4px;
        cursor: pointer;
        transition: all 0.2s;
    }
    .menu-item:hover {
        background-color: #e9ecef;
        border-color: #0d6efd;
    }
    .menu-item.active {
        background-color: #0d6efd;
        color: white;
        border-color: #0d6efd;
    }
    .menu-level-1 {
        margin-left: 0;
    }
    .menu-level-2 {
        margin-left: 20px;
    }
    .menu-level-3 {
        margin-left: 40px;
    }
    .form-section {
        background-color: #f8f9fa;
        padding: 20px;
        border-radius: 4px;
        border: 1px solid #dee2e6;
    }
</style>

<div class="container-fluid mt-4">
    <div class="row">
        <div class="col-12">
            <h2 class="mb-4">
                <i class="fa fa-bars"></i> 메뉴 관리
            </h2>
        </div>
    </div>

    <div class="row">
        <!-- 메뉴 트리 영역 -->
        <div class="col-md-5">
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">메뉴 목록</h5>
                    <button type="button" class="btn btn-sm btn-primary" onclick="menuManager.newMenu()">
                        <i class="fa fa-plus"></i> 신규
                    </button>
                </div>
                <div class="card-body">
                    <div id="menuTree" class="menu-tree">
                        <!-- 메뉴 트리가 여기에 동적으로 생성됩니다 -->
                    </div>
                </div>
            </div>
        </div>

        <!-- 메뉴 입력 폼 영역 -->
        <div class="col-md-7">
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">메뉴 상세정보</h5>
                </div>
                <div class="card-body">
                    <form id="menuForm" class="form-section">
                        <input type="hidden" id="menuId" name="menuId">

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="menuName" class="form-label">메뉴명 <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="menuName" name="menuName" required>
                            </div>
                            <div class="col-md-6">
                                <label for="menuUrl" class="form-label">메뉴 URL</label>
                                <input type="text" class="form-control" id="menuUrl" name="menuUrl" placeholder="/admin/example">
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="parentId" class="form-label">상위 메뉴</label>
                                <select class="form-select" id="parentId" name="parentId">
                                    <option value="">최상위 메뉴</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label for="iconClass" class="form-label">아이콘 클래스</label>
                                <input type="text" class="form-control" id="iconClass" name="iconClass" placeholder="fa fa-home">
                                <small class="text-muted">예: fa fa-home, feather activity</small>
                            </div>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-4">
                                <label for="menuLevel" class="form-label">메뉴 레벨 <span class="text-danger">*</span></label>
                                <select class="form-select" id="menuLevel" name="menuLevel" required>
                                    <option value="1">1단계</option>
                                    <option value="2">2단계</option>
                                    <option value="3">3단계</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label for="menuOrder" class="form-label">정렬 순서 <span class="text-danger">*</span></label>
                                <input type="number" class="form-control" id="menuOrder" name="menuOrder" value="1" required>
                            </div>
                            <div class="col-md-4">
                                <label for="useYn" class="form-label">사용 여부 <span class="text-danger">*</span></label>
                                <select class="form-select" id="useYn" name="useYn" required>
                                    <option value="Y">사용</option>
                                    <option value="N">미사용</option>
                                </select>
                            </div>
                        </div>

                        <div class="row mt-4">
                            <div class="col-12 text-end">
                                <button type="button" class="btn btn-secondary" onclick="menuManager.resetForm()">
                                    <i class="fa fa-refresh"></i> 초기화
                                </button>
                                <button type="button" class="btn btn-danger" onclick="menuManager.deleteMenu()" id="deleteBtn" style="display:none;">
                                    <i class="fa fa-trash"></i> 삭제
                                </button>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fa fa-save"></i> 저장
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    const MENU = "menu";
    const API_URL = "/api/" + MENU;

    // 페이지 로드 시 초기화
    $(document).ready(function() {
        menuManager.init();
    });

    const menuManager = {
        currentMenuId: null,

        // 페이지 로드 시 초기화
        init: function() {
            this.loadMenuTree();
            this.loadParentMenuList();
            this.bindEvents();
        },

        // 이벤트 바인딩
        bindEvents: function() {
            $('#menuForm').on('submit', function(e) {
                e.preventDefault();
                menuManager.saveMenu();
            });

            $('#parentId').on('change', function() {
                const parentId = $(this).val();
                if (parentId) {
                    // 부모 메뉴 레벨 + 1로 자동 설정
                    const parentOption = $(this).find('option:selected');
                    const parentLevel = parseInt(parentOption.data('level') || 0);
                    $('#menuLevel').val(parentLevel + 1);
                } else {
                    $('#menuLevel').val(1);
                }
            });
        },

        // 메뉴 트리 로드
        loadMenuTree: function() {
            $.ajax({
                url: API_URL + '/tree',
                method: 'GET',
                success: function(data) {
                    menuManager.renderMenuTree(data);
                },
                error: function() {
                    alert('메뉴 목록을 불러오는데 실패했습니다.');
                }
            });
        },

        // 메뉴 트리 렌더링
        renderMenuTree: function(menus, parentEl) {
            const container = parentEl || $('#menuTree');
            if (!parentEl) {
                container.empty();
            }

            menus.forEach(function(menu) {
                const menuItem = $('<div>')
                    .addClass('menu-item menu-level-' + menu.menuLevel)
                    .attr('data-menu-id', menu.menuId)
                    .html(
                        '<i class="' + (menu.iconClass || 'fa fa-circle-o') + '"></i> ' +
                        '<strong>' + menu.menuName + '</strong>' +
                        (menu.menuUrl ? ' <small class="text-muted">(' + menu.menuUrl + ')</small>' : '')
                    )
                    .on('click', function(e) {
                        e.stopPropagation();
                        menuManager.selectMenu(menu.menuId);
                    });

                container.append(menuItem);

                if (menu.children && menu.children.length > 0) {
                    menuManager.renderMenuTree(menu.children, container);
                }
            });
        },

        // 부모 메뉴 목록 로드
        loadParentMenuList: function() {
            $.ajax({
                url: API_URL + '/parent-list',
                method: 'GET',
                success: function(data) {
                    const select = $('#parentId');
                    select.find('option:not(:first)').remove();

                    data.forEach(function(menu) {
                        const indent = '　'.repeat(menu.menuLevel - 1);
                        select.append(
                            $('<option>')
                                .val(menu.menuId)
                                .data('level', menu.menuLevel)
                                .text(indent + menu.menuName)
                        );
                    });
                }
            });
        },

        // 메뉴 선택
        selectMenu: function(menuId) {
            $('.menu-item').removeClass('active');
            $('.menu-item[data-menu-id="' + menuId + '"]').addClass('active');

            $.ajax({
                url: API_URL + '/' + menuId,
                method: 'GET',
                success: function(data) {
                    menuManager.currentMenuId = menuId;
                    $('#menuId').val(data.menuId);
                    $('#menuName').val(data.menuName);
                    $('#menuUrl').val(data.menuUrl);
                    $('#parentId').val(data.parentId || '');
                    $('#menuLevel').val(data.menuLevel);
                    $('#menuOrder').val(data.menuOrder);
                    $('#useYn').val(data.useYn);
                    $('#iconClass').val(data.iconClass);
                    $('#deleteBtn').show();
                }
            });
        },

        // 신규 메뉴
        newMenu: function() {
            this.resetForm();
        },

        // 폼 초기화
        resetForm: function() {
            this.currentMenuId = null;
            $('#menuForm')[0].reset();
            $('#menuId').val('');
            $('#menuLevel').val('1');
            $('#menuOrder').val('1');
            $('#useYn').val('Y');
            $('#deleteBtn').hide();
            $('.menu-item').removeClass('active');
        },

        // 메뉴 저장
        saveMenu: function() {
            const formData = {
                menuId: $('#menuId').val() || null,
                menuName: $('#menuName').val(),
                menuUrl: $('#menuUrl').val(),
                parentId: $('#parentId').val() || null,
                menuLevel: parseInt($('#menuLevel').val()),
                menuOrder: parseInt($('#menuOrder').val()),
                useYn: $('#useYn').val(),
                iconClass: $('#iconClass').val()
            };

            $.ajax({
                url: API_URL + '/save',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function(result) {
                    if (result.success) {
                        alert(result.message);
                        menuManager.loadMenuTree();
                        menuManager.loadParentMenuList();
                        menuManager.resetForm();
                    } else {
                        alert(result.message);
                    }
                },
                error: function() {
                    alert('메뉴 저장 중 오류가 발생했습니다.');
                }
            });
        },

        // 메뉴 삭제
        deleteMenu: function() {
            if (!this.currentMenuId) {
                alert('삭제할 메뉴를 선택해주세요.');
                return;
            }

            if (!confirm('선택한 메뉴를 삭제하시겠습니까?')) {
                return;
            }

            $.ajax({
                url: API_URL + '/' + this.currentMenuId,
                method: 'DELETE',
                success: function(result) {
                    if (result.success) {
                        alert(result.message);
                        menuManager.loadMenuTree();
                        menuManager.loadParentMenuList();
                        menuManager.resetForm();
                    } else {
                        alert(result.message);
                    }
                },
                error: function() {
                    alert('메뉴 삭제 중 오류가 발생했습니다.');
                }
            });
        }
    };

</script>