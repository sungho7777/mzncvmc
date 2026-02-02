<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="sidenav-menu">
    <div class="nav accordion" id="accordionSidenav">
        <!-- 메뉴가 여기에 동적으로 로드됩니다 -->
    </div>
</div>

<script type="text/javascript">
    document.addEventListener('DOMContentLoaded', function() {
        loadSideMenu();
    });

    function loadSideMenu() {
        fetch('/api/menu/tree')
            .then(response => response.json())
            .then(data => {
                renderSideMenu(data);
            })
            .catch(error => {
                console.error('메뉴 로드 실패:', error);
            });
    }

    function renderSideMenu(menus) {
        const container = document.getElementById('accordionSidenav');
        container.innerHTML = '';

        let currentHeading = null;

        console.log(menus);
        menus.forEach((menu, index) => {
            if (menu.menuLevel === 1) {
                // 1단계 메뉴
                if (menu.children && menu.children.length > 0) {
                    // 자식이 있는 경우 - collapse 구조
                    const collapseId = 'collapse' + menu.menuId;

                    // 헤딩이 있으면 추가
                    if (menu.menuName === 'Dashboard' || menu.menuName === 'Core') {
                        const heading = document.createElement('div');
                        heading.className = 'sidenav-menu-heading';
                        heading.textContent = menu.menuName;
                        container.appendChild(heading);
                        currentHeading = menu.menuName;
                    } else {
                        // 일반 collapse 메뉴
                        const link = createCollapseLink(menu, collapseId);
                        container.appendChild(link);

                        const collapseDiv = createCollapseDiv(menu, collapseId);
                        container.appendChild(collapseDiv);
                    }
                } else {
                    // 자식이 없는 경우 - 단순 링크
                    const link = createSimpleLink(menu);
                    container.appendChild(link);
                }
            }
        });
    }

    function createCollapseLink(menu, collapseId) {
        const a = document.createElement('a');
        a.className = 'nav-link collapsed';
        a.href = 'javascript:void(0);';
        a.setAttribute('data-bs-toggle', 'collapse');
        a.setAttribute('data-bs-target', '#' + collapseId);
        a.setAttribute('aria-expanded', 'false');
        a.setAttribute('aria-controls', collapseId);

        const iconDiv = document.createElement('div');
        iconDiv.className = 'nav-link-icon';
        const icon = document.createElement('i');
        icon.setAttribute('data-feather', getFeatherIcon(menu.iconClass));
        iconDiv.appendChild(icon);

        const text = document.createTextNode(' ' + menu.menuName + ' ');

        const arrowDiv = document.createElement('div');
        arrowDiv.className = 'sidenav-collapse-arrow';
        const arrowIcon = document.createElement('i');
        arrowIcon.className = 'fas fa-angle-down';
        arrowDiv.appendChild(arrowIcon);

        a.appendChild(iconDiv);
        a.appendChild(text);
        a.appendChild(arrowDiv);

        return a;
    }

    function createCollapseDiv(menu, collapseId) {
        const collapseDiv = document.createElement('div');
        collapseDiv.className = 'collapse';
        collapseDiv.id = collapseId;
        collapseDiv.setAttribute('data-bs-parent', '#accordionSidenav');

        const nav = document.createElement('nav');
        nav.className = 'sidenav-menu-nested nav accordion';
        nav.id = 'accordion' + menu.menuId;

        // 2단계 메뉴 렌더링
        menu.children.forEach(child => {
            if (child.children && child.children.length > 0) {
                // 3단계가 있는 경우
                const childCollapseId = 'collapse' + child.menuId;
                const childLink = createNestedCollapseLink(child, childCollapseId);
                nav.appendChild(childLink);

                const childCollapseDiv = createNestedCollapseDiv(child, childCollapseId, 'accordion' + menu.menuId);
                nav.appendChild(childCollapseDiv);
            } else {
                // 3단계가 없는 경우
                const childLink = createNestedSimpleLink(child);
                nav.appendChild(childLink);
            }
        });

        collapseDiv.appendChild(nav);
        return collapseDiv;
    }

    function createNestedCollapseLink(menu, collapseId) {
        const a = document.createElement('a');
        a.className = 'nav-link collapsed';
        a.href = 'javascript:void(0);';
        a.setAttribute('data-bs-toggle', 'collapse');
        a.setAttribute('data-bs-target', '#' + collapseId);
        a.setAttribute('aria-expanded', 'false');
        a.setAttribute('aria-controls', collapseId);

        const text = document.createTextNode(menu.menuName + ' ');

        const arrowDiv = document.createElement('div');
        arrowDiv.className = 'sidenav-collapse-arrow';
        const arrowIcon = document.createElement('i');
        arrowIcon.className = 'fas fa-angle-down';
        arrowDiv.appendChild(arrowIcon);

        a.appendChild(text);
        a.appendChild(arrowDiv);

        return a;
    }

    function createNestedCollapseDiv(menu, collapseId, parentId) {
        const collapseDiv = document.createElement('div');
        collapseDiv.className = 'collapse';
        collapseDiv.id = collapseId;
        collapseDiv.setAttribute('data-bs-parent', '#' + parentId);

        const nav = document.createElement('nav');
        nav.className = 'sidenav-menu-nested nav';

        // 3단계 메뉴 렌더링
        menu.children.forEach(child => {
            const childLink = createNestedSimpleLink(child);
            nav.appendChild(childLink);
        });

        collapseDiv.appendChild(nav);
        return collapseDiv;
    }

    function createSimpleLink(menu) {
        const a = document.createElement('a');
        a.className = 'nav-link';
        a.href = menu.menuUrl || 'javascript:void(0);';

        const iconDiv = document.createElement('div');
        iconDiv.className = 'nav-link-icon';
        const icon = document.createElement('i');
        icon.setAttribute('data-feather', getFeatherIcon(menu.iconClass));
        iconDiv.appendChild(icon);

        const text = document.createTextNode(' ' + menu.menuName);

        a.appendChild(iconDiv);
        a.appendChild(text);

        return a;
    }

    function createNestedSimpleLink(menu) {
        const a = document.createElement('a');
        a.className = 'nav-link';
        a.href = menu.menuUrl || 'javascript:void(0);';
        a.textContent = menu.menuName;

        return a;
    }

    function getFeatherIcon(iconClass) {
        if (!iconClass) return 'circle';

        // Font Awesome을 Feather 아이콘으로 매핑
        const iconMap = {
            'fa-dashboard': 'activity',
            'fa-cog': 'settings',
            'fa-users': 'users',
            'fa-file-text': 'file-text',
            'fa-bars': 'menu',
            'fa-code': 'code',
            'fa-lock': 'lock',
            'fa-user': 'user',
            'fa-history': 'clock',
            'fa-list': 'list',
            'fa-bullhorn': 'volume-2',
            'fa-question-circle': 'help-circle'
        };

        // iconClass에서 fa- 부분 추출
        const match = iconClass.match(/fa-([a-z-]+)/);
        if (match && iconMap[iconClass]) {
            return iconMap[iconClass];
        }

        // feather 아이콘이 직접 지정된 경우
        if (iconClass.includes('feather')) {
            return iconClass.replace('feather ', '');
        }

        return 'circle';
    }

    // Feather 아이콘 초기화 (페이지 로드 후 실행)
    window.addEventListener('load', function() {
        if (typeof feather !== 'undefined') {
            feather.replace();
        }
    });
</script>