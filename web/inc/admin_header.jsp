<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="csrf-token" content="${sessionScope.csrfToken}">
    <meta name="description" content="Cổng quản trị hệ thống tiếp nhận và quản lý đồ thất lạc Trường Đại học Khoa học — Đại học Huế (HUSC ReFind).">
    <title>${not empty requestScope.pageTitle ? requestScope.pageTitle : (not empty pageTitle ? pageTitle : 'Quản trị hệ thống — HUSC ReFind')}</title>

    <link rel="icon" type="image/jpeg" href="${pageContext.request.contextPath}/assets/img/logo.jpg">
    <link rel="apple-touch-icon" href="${pageContext.request.contextPath}/assets/img/logo.jpg">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css?v=20260820_v6">
</head>
<body class="admin-page">

<div id="huscFlashData" style="display:none;"
     data-success="<c:out value='${not empty successMessage ? successMessage : sessionScope.successMessage}'/>"
     data-error="<c:out value='${not empty errorMessage ? errorMessage : sessionScope.errorMessage}'/>"
     data-info="<c:out value='${not empty infoMessage ? infoMessage : sessionScope.infoMessage}'/>"></div>
<c:remove var="successMessage" scope="session"/>
<c:remove var="errorMessage" scope="session"/>
<c:remove var="infoMessage" scope="session"/>

<div class="husc-toast-container"></div>

<div class="admin-sidebar-backdrop" id="adminSidebarBackdrop"></div>

<div class="admin-layout">

    <aside class="admin-sidebar" id="adminSidebar">

        <div class="admin-sidebar-brand" style="height: 88px; min-height: 88px; max-height: 88px; box-sizing: border-box; border-bottom: 1px solid var(--husc-border, #E2E8F0); display: flex; align-items: center; padding: 0 16px;">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="navbar-brand-husc user-select-none text-decoration-none py-0">
                <img src="${pageContext.request.contextPath}/assets/img/brand_logo.jpg" alt="HUSC ReFind" class="navbar-brand-full-img">
            </a>
        </div>

        <div class="admin-sidebar-menu">
            <div class="admin-nav-section-title">Tổng quan nghiệp vụ</div>
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="admin-nav-link ${activeAdminNav == 'dashboard' ? 'active' : ''}">
                <i class="bi bi-speedometer2"></i>
                <span>Bảng điều khiển</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/items" class="admin-nav-link ${activeAdminNav == 'items' ? 'active' : ''}">
                <i class="bi bi-box-seam"></i>
                <span>Đồ thất lạc</span>
            </a>

            <div class="admin-nav-section-title mt-2">Cấu hình hệ thống</div>
            <a href="${pageContext.request.contextPath}/admin/categories" class="admin-nav-link ${activeAdminNav == 'categories' ? 'active' : ''}">
                <i class="bi bi-tags"></i>
                <span>Danh mục</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/locations" class="admin-nav-link ${activeAdminNav == 'locations' ? 'active' : ''}">
                <i class="bi bi-geo-alt"></i>
                <span>Khu vực</span>
            </a>
            <a href="${pageContext.request.contextPath}/admin/users" class="admin-nav-link ${activeAdminNav == 'users' ? 'active' : ''}">
                <i class="bi bi-people"></i>
                <span>Tài khoản</span>
            </a>
        </div>

        <div class="admin-sidebar-footer">
            <div class="d-flex align-items-center justify-content-between p-2 rounded bg-light border">
                <div class="d-flex align-items-center gap-2 overflow-hidden">
                    <div class="user-avatar-badge d-flex align-items-center justify-content-center" style="background: linear-gradient(135deg, #002B7F, #2563EB); color: #fff;">
                        <i class="bi bi-shield-lock-fill"></i>
                    </div>
                    <div class="overflow-hidden">
                        <div class="small fw-bold text-dark text-truncate">${sessionScope.user.name}</div>
                        <div class="text-muted" style="font-size: 0.72rem;">${not empty sessionScope.user.code ? sessionScope.user.code : 'admin'}</div>
                    </div>
                </div>
                <form action="${pageContext.request.contextPath}/logout" method="POST" class="m-0">
                    <button type="submit" class="btn btn-link text-danger p-1 border-0">
                        <i class="bi bi-box-arrow-right fs-5"></i>
                    </button>
                </form>
            </div>
        </div>
    </aside>

    <div class="admin-main">

        <header class="admin-topbar d-flex align-items-center justify-content-between" style="height: 88px; min-height: 88px; max-height: 88px; box-sizing: border-box; border-bottom: 1px solid var(--husc-border, #E2E8F0); padding: 0 28px;">
            <div class="d-flex align-items-center gap-3">
                <button class="btn btn-sm btn-outline-secondary d-lg-none" type="button" id="toggleAdminSidebar" aria-label="Mở Menu">
                    <i class="bi bi-list fs-5"></i>
                </button>
                <div class="fw-bold fs-5 text-dark m-0 p-0" style="line-height: 1;">Quản trị hệ thống</div>
            </div>
        </header>

        <main class="admin-content">
