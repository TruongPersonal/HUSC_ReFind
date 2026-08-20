<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="csrf-token" content="${sessionScope.csrfToken}">
    <meta name="description" content="${not empty requestScope.pageDescription ? requestScope.pageDescription : 'Hệ thống tìm kiếm và quản lý đồ thất lạc chính thức dành cho sinh viên Trường Đại học Khoa học — Đại học Huế (HUSC ReFind).'}">
    <title>${not empty requestScope.pageTitle ? requestScope.pageTitle : (not empty pageTitle ? pageTitle : 'HUSC ReFind — Quản lý Đồ thất lạc')}</title>

    <link rel="icon" type="image/jpeg" href="${pageContext.request.contextPath}/assets/img/logo.jpg">
    <link rel="apple-touch-icon" href="${pageContext.request.contextPath}/assets/img/logo.jpg">

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Be+Vietnam+Pro:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

<div id="huscFlashData" style="display:none;"
     data-success="<c:out value='${not empty successMessage ? successMessage : sessionScope.successMessage}'/>"
     data-error="<c:out value='${not empty errorMessage ? errorMessage : sessionScope.errorMessage}'/>"
     data-info="<c:out value='${not empty infoMessage ? infoMessage : sessionScope.infoMessage}'/>"></div>
<c:remove var="successMessage" scope="session"/>
<c:remove var="errorMessage" scope="session"/>
<c:remove var="infoMessage" scope="session"/>

<div class="husc-toast-container"></div>

<nav class="navbar navbar-expand-lg navbar-husc">
    <div class="container px-3 px-lg-4">

        <a href="${pageContext.request.contextPath}/" class="navbar-brand-husc user-select-none text-decoration-none py-0">
            <img src="${pageContext.request.contextPath}/assets/img/brand_logo.jpg" alt="HUSC ReFind" class="navbar-brand-full-img">
        </a>

        <div class="d-flex align-items-center gap-2 order-lg-3">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">

                    <div class="dropdown">
                        <button class="btn btn-user-pill dropdown-toggle d-flex align-items-center gap-1 py-1 px-2" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                            <div class="user-avatar-badge" style="background: var(--husc-accent-soft); color: var(--husc-primary);">
                                <i class="bi bi-person-fill"></i>
                            </div>
                        </button>
                        <ul class="dropdown-menu dropdown-menu-end shadow-sm border mt-2" style="font-size: 0.9rem; min-width: 200px;">
                            <li class="px-3 py-2 border-bottom bg-light">
                                <div class="fw-bold text-dark text-truncate">${sessionScope.user.name}</div>
                                <div class="text-muted small">${sessionScope.user.code}</div>
                            </li>

                            <li>
                                <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/my-posts">
                                    <i class="bi bi-file-text me-2 text-muted"></i> Tin đã đăng
                                </a>
                            </li>
                            <li>
                                <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/saved-items">
                                    <i class="bi bi-heart me-2 text-danger"></i> Tin đã lưu
                                </a>
                            </li>
                            <li>
                                <a class="dropdown-item py-2" href="${pageContext.request.contextPath}/profile">
                                    <i class="bi bi-person me-2 text-muted"></i> Tài khoản
                                </a>
                            </li>
                            <li><hr class="dropdown-divider my-1"></li>
                            <li>
                                <form action="${pageContext.request.contextPath}/logout" method="POST" class="m-0">
                                    <button type="submit" class="dropdown-item py-2 text-danger">
                                        <i class="bi bi-box-arrow-right me-2"></i> Đăng xuất
                                    </button>
                                </form>
                            </li>
                        </ul>
                    </div>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-husc-primary btn-sm">
                        <i class="bi bi-box-arrow-in-right me-1"></i> Tham gia
                    </a>
                </c:otherwise>
            </c:choose>

            <button class="navbar-toggler border-0 p-2" type="button" data-bs-toggle="collapse" data-bs-target="#navbarMain" aria-label="Toggle navigation">
                <i class="bi bi-list fs-4"></i>
            </button>
        </div>

        <div class="collapse navbar-collapse order-lg-2" id="navbarMain">

            <ul class="navbar-nav mx-auto mb-2 mb-lg-0 gap-1">
                <li class="nav-item">
                    <a class="nav-link nav-link-custom ${activeNav == 'home' ? 'active' : ''}" href="${pageContext.request.contextPath}/home">
                        <i class="bi bi-grid-fill me-1"></i> Bảng tin
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link nav-link-custom ${activeNav == 'about' ? 'active' : ''}" href="${pageContext.request.contextPath}/about">
                        <i class="bi bi-journal-check me-1"></i> Quy trình
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link nav-link-custom ${activeNav == 'contact' ? 'active' : ''}" href="${pageContext.request.contextPath}/contact">
                        <i class="bi bi-telephone-fill me-1"></i> Liên hệ
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<c:if test="${not empty sessionScope.user}">
    <div class="modal fade modal-husc" id="quickPostModal" tabindex="-1" aria-labelledby="quickPostModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="quickPostModalLabel">
                        <i class="bi bi-plus-circle text-primary"></i> Báo mất đồ
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form action="${pageContext.request.contextPath}/post-item" method="POST" enctype="multipart/form-data">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label">Tên đồ vật <span class="text-danger">*</span></label>
                            <input type="text" class="form-control form-control-custom" name="title" placeholder="Ví dụ: Ví da màu nâu, Thẻ sinh viên..." required>
                        </div>

                        <div class="row g-3 mb-3">
                            <div class="col-md-6">
                                <label class="form-label">Danh mục <span class="text-danger">*</span></label>
                                <select class="form-select form-select-custom" name="category_id" required>
                                    <option value="" disabled selected>Chọn danh mục</option>
                                    <c:forEach items="${applicationScope.globalCategories != null ? applicationScope.globalCategories : categories}" var="cat">
                                        <option value="${cat.id}">${cat.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Khu vực <span class="text-danger">*</span></label>
                                <select class="form-select form-select-custom" name="location_id" required>
                                    <option value="" disabled selected>Chọn khu vực</option>
                                    <c:forEach items="${applicationScope.globalLocations != null ? applicationScope.globalLocations : locations}" var="loc">
                                        <option value="${loc.id}">${loc.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Mô tả</label>
                            <textarea class="form-control form-control-custom" name="description" rows="3" placeholder="Mô tả màu sắc, đặc điểm nhận dạng, thời gian rơi..."></textarea>
                        </div>

                        <div class="mb-2">
                            <label class="form-label">Ảnh <span class="text-danger">*</span></label>
                            <div class="image-dropzone">
                                <div class="dropzone-prompt">
                                    <i class="bi bi-camera fs-2 text-muted d-block mb-1"></i>
                                    <div class="fw-semibold small text-dark">Bấm hoặc kéo thả ảnh vào đây</div>
                                </div>
                                <input type="file" name="image" accept="image/*" class="d-none input-image-preview" required>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-husc-outline btn-sm" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-husc-primary btn-sm">
                            <i class="bi bi-send me-1"></i> Đăng tin
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</c:if>

<main class="main-content">
