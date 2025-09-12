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
        <li class="nav-item ${sidebar eq 'neo' ? 'active' : ''}">
            <a class="nav-link ${sidebar eq 'neo' ? '' : 'collapsed'}" href="#" data-toggle="collapse" data-target="#collapseNeo"
               aria-expanded="true" aria-controls="collapseNeo">
                <i class="fas fa-fw fa-cog"></i>
                <span>NStringIn</span>
            </a>
            <div id="collapseNeo" class="collapse ${sidebar eq 'neo' ? 'show' : ''}" aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Custom NStringIn:</h6>
                    <a class="collapse-item ${sidebar eq 'neo' ? 'active' : ''}" href="#" onclick="goList('neo')">List</a>
                    <a class="collapse-item" href="cards.html">Cards</a>
                </div>
            </div>
        </li>


        <!-- Nav Item - Pages Collapse Menu -->
        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseTwo"
               aria-expanded="true" aria-controls="collapseTwo">
                <i class="fas fa-fw fa-cog"></i>
                <span>Components</span>
            </a>
            <div id="collapseTwo" class="collapse " aria-labelledby="headingTwo" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Custom Components:</h6>
                    <a class="collapse-item" href="buttons.html">Buttons</a>
                    <a class="collapse-item" href="cards.html">Cards</a>
                </div>
            </div>
        </li>

        <!-- Nav Item - Utilities Collapse Menu -->
        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapseUtilities"
               aria-expanded="true" aria-controls="collapseUtilities">
                <i class="fas fa-fw fa-wrench"></i>
                <span>Utilities</span>
            </a>
            <div id="collapseUtilities" class="collapse" aria-labelledby="headingUtilities"
                 data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Custom Utilities:</h6>
                    <a class="collapse-item" href="utilities-color.html">Colors</a>
                    <a class="collapse-item" href="utilities-border.html">Borders</a>
                    <a class="collapse-item" href="utilities-animation.html">Animations</a>
                    <a class="collapse-item" href="utilities-other.html">Other</a>
                </div>
            </div>
        </li>

        <!-- Divider -->
        <hr class="sidebar-divider">

        <!-- Heading -->
        <div class="sidebar-heading">
            Addons
        </div>

        <!-- Nav Item - Pages Collapse Menu -->
        <li class="nav-item">
            <a class="nav-link collapsed" href="#" data-toggle="collapse" data-target="#collapsePages"
               aria-expanded="true" aria-controls="collapsePages">
                <i class="fas fa-fw fa-folder"></i>
                <span>Pages</span>
            </a>
            <div id="collapsePages" class="collapse ${sidebar eq 'blank' ? 'show' : ''}" aria-labelledby="headingPages" data-parent="#accordionSidebar">
                <div class="bg-white py-2 collapse-inner rounded">
                    <h6 class="collapse-header">Login Screens:</h6>
                    <a class="collapse-item" href="login.html">Login</a>
                    <a class="collapse-item" href="register.html">Register</a>
                    <a class="collapse-item" href="forgot-password.html">Forgot Password</a>
                    <div class="collapse-divider"></div>
                    <h6 class="collapse-header">Other Pages:</h6>
                    <a class="collapse-item" href="404.html">404 Page</a>
                    <a class="collapse-item ${sidebar eq 'blank' ? 'active' : ''}" href="/sbadmin/blank">Blank Page</a>



                </div>
            </div>
        </li>

        <!-- Nav Item - Charts -->
        <li class="nav-item">
            <a class="nav-link" href="charts.html">
                <i class="fas fa-fw fa-chart-area"></i>
                <span>Charts</span></a>
        </li>

        <!-- Nav Item - User -->
        <li class="nav-item ${sidebar eq 'users' ? 'active' : ''}">
            <a class="nav-link" href="#" onclick="goList('users')">
                <i class="fas fa-user fa-chart-area"></i>
                <span>Users</span></a>
        </li>

        <!-- Nav Item - Company -->
        <li class="nav-item ${sidebar eq 'companys' ? 'active' : ''}">
            <a class="nav-link" href="#" onclick="goList('company')">
                <i class="fas fa-company fa-chart-area"></i>
                <span>Company</span></a>
        </li>

        <!-- Nav Item - files -->
        <li class="nav-item ${sidebar eq 'files' ? 'active' : ''}">
            <a class="nav-link" href="#" onclick="goList('files')">
                <i class="fas fa-company fa-chart-area"></i>
                <span>File Upload</span></a>
        </li>

        <!-- Nav Item - bbs -->
        <li class="nav-item ${sidebar eq 'bbs' ? 'active' : ''}">
            <a class="nav-link" href="#" onclick="goList('bbs')">
                <i class="fas fa-company fa-chart-area"></i>
                <span>Bbs</span></a>
        </li>

        <!-- Nav Item - Tables -->
        <li class="nav-item ${sidebar eq 'tables' ? 'active' : ''}">
            <a class="nav-link" href="#">
                <i class="fas fa-fw fa-table"></i>
                <span>Tables</span></a>
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