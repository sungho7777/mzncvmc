<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <ul class="navbar-nav bg-gradient-primary sidebar sidebar-dark accordion" id="accordionSidebar">

        <!-- Sidebar - Brand -->
        <a class="sidebar-brand d-flex align-items-center justify-content-center" href="/main">
            <div class="sidebar-brand-icon rotate-n-15">
                <i class="fas fa-laugh-wink"></i>
            </div>
            <div class="sidebar-brand-text mx-3">SpringBoot <sup>7</sup></div>
        </a>

        <!-- Divider -->
        <hr class="sidebar-divider my-0">

        <!-- Nav Item - Dashboard -->
        <li class="nav-item ${sidebar eq 'dashboard' ? 'active' : ''}">
            <a class="nav-link" href="#" onclick="goDashboard('dashboard')">
                <i class="fas fa-fw fa-tachometer-alt"></i>
                <span>Dashboard </span></a>
        </li>

        <!-- Divider -->
        <hr class="sidebar-divider">

        <!-- Heading -->
        <div class="sidebar-heading">
            Interface
        </div>

        <!-- Nav Item - Pages Collapse Menu -->
        <li class="nav-item ${sidebar eq 'ai' ? 'active' : ''}">
            <a class="nav-link ${sidebar eq 'ai' ? '' : 'collapsed'}" href="#" data-toggle="collapse" data-target="#collapseAi"
               aria-expanded="true" aria-controls="collapseAi">
                <i class="fas fa-fw fa-cog"></i>
                <span>Ai</span>
            </a>
            <div id="collapseAi" class="collapse ${sidebar eq 'ai' ? 'show' : ''}" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Custom Components:</h6>
                    <a class="collapse-item ${sub_sidebar eq 'aiOllama' ? 'active' : ''}" href="#" onclick="goList('ai/aiOllama', null)">aiOllama</a>

                </div>
            </div>
        </li>

        <!-- Nav Item - Pages Collapse Menu -->
        <li class="nav-item ${sidebar eq 'bbs' ? 'active' : ''}">
            <a class="nav-link ${sidebar eq 'bbs' ? '' : 'collapsed'}" href="#" data-toggle="collapse" data-target="#collapseBbs"
               aria-expanded="true" aria-controls="collapseBbs">
                <i class="fas fa-fw fa-cog"></i>
                <span>Bbs</span>
            </a>
            <div id="collapseBbs" class="collapse ${sidebar eq 'bbs' ? 'show' : ''}" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Custom Components:</h6>
                    <a class="collapse-item ${sub_sidebar eq 'bbsCategories' ? 'active' : ''}" href="#" onclick="goList('bbs/bbsCategories', null)">게시판 설정</a>
                    <a class="collapse-item ${sub_sidebar eq 'bbsPosts1' ? 'active' : ''}" href="#" onclick="goList('bbs/bbsPosts', '1')">공지사항</a>
                    <a class="collapse-item ${sub_sidebar eq 'bbsPosts2' ? 'active' : ''}" href="#" onclick="goList('bbs/bbsPosts', '2')">자유게시판</a>
                    <a class="collapse-item ${sub_sidebar eq 'bbsPosts3' ? 'active' : ''}" href="#" onclick="goList('bbs/bbsPosts', '3')">질문답변</a>
                    <a class="collapse-item ${sub_sidebar eq 'bbsPosts4' ? 'active' : ''}" href="#" onclick="goList('bbs/bbsPosts', '4')">자료실</a>
                    <a class="collapse-item ${sub_sidebar eq 'bbsPosts5' ? 'active' : ''}" href="#" onclick="goList('bbs/bbsPosts', '5')">건의사항</a>
                </div>
            </div>
        </li>


        <!-- Divider -->
        <hr class="sidebar-divider">

        <!-- Heading -->
        <div class="sidebar-heading">
            Addons
        </div>


        <!-- Nav Item - User -->
        <li class="nav-item ${sidebar eq 'users' ? 'active' : ''}">
            <a class="nav-link" href="#" onclick="goList('users', null)">
                <i class="fas fa-user fa-chart-area"></i>
                <span>Users</span></a>
        </li>

        <!-- Nav Item - Company -->
        <li class="nav-item ${sidebar eq 'companys' ? 'active' : ''}">
            <a class="nav-link" href="#" onclick="goList('company', null)">
                <i class="fas fa-company fa-chart-area"></i>
                <span>Company</span></a>
        </li>

        <!-- Divider -->
        <hr class="sidebar-divider d-none d-md-block">

        <!-- Sidebar Toggler (Sidebar) -->
        <div class="text-center d-none d-md-inline">
            <button class="rounded-circle border-0" id="sidebarToggle"></button>
        </div>

        <!-- Sidebar Message -->
        <div class="sidebar-card d-none d-lg-flex">
            <img class="sidebar-card-illustration mb-2" src="/common/sbadmin/img/undraw_rocket.svg" alt="...">
            <p class="text-center mb-2"><strong>SB Admin Pro</strong> is packed with premium features, components, and more!</p>
            <a class="btn btn-success btn-sm" href="https://startbootstrap.com/theme/sb-admin-pro">Upgrade to Pro!</a>
        </div>

    </ul>