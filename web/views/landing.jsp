<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="pageTitle" value="HUSC ReFind — Quản lý Đồ thất lạc HUSC" scope="request" />
<c:set var="activeNav" value="landing" scope="request" />
<jsp:include page="/inc/header.jsp" />

<section class="py-5 border-bottom" style="background: var(--husc-light);">
    <div class="container px-3 px-lg-4">
        <div class="row align-items-center g-4 g-lg-5">

            <div class="col-lg-6">
                <div class="d-inline-flex align-items-center gap-2 mb-3 px-3 py-1 bg-white border rounded-pill shadow-xs">
                    <i class="bi bi-mortarboard-fill text-primary"></i>
                    <span class="small fw-semibold text-dark">Trường ĐH Khoa học — ĐH Huế</span>
                </div>

                <h1 class="fw-bold mb-3" style="color: var(--husc-dark); line-height: 1.25;">
                    HUSC ReFind — Tìm & Trao trả Đồ thất lạc
                </h1>

                <p class="mb-4 text-muted" style="font-size: 1rem; line-height: 1.6; max-width: 500px;">
                    Kênh thông tin kết nối sinh viên và Phòng trực tại 77 Nguyễn Huệ — hỗ trợ tra cứu, tiếp nhận và nhận lại tài sản nhanh chóng.
                </p>

                <div class="d-flex flex-wrap gap-2 mb-4">
                    <a href="${pageContext.request.contextPath}/home?tab=LOST" class="btn btn-husc-primary">
                        <i class="bi bi-search"></i> Đồ báo mất
                    </a>
                    <a href="${pageContext.request.contextPath}/home?tab=FOUND" class="btn btn-husc-outline">
                        <i class="bi bi-shield-check text-warning"></i> Đồ đang giữ
                    </a>
                </div>

                <div class="d-flex align-items-center gap-4 pt-3 border-top">
                    <div>
                        <div class="fw-bold fs-4 text-dark">${totalItems > 0 ? totalItems : 0}</div>
                        <div class="small text-muted">Tổng tin báo</div>
                    </div>
                    <div style="width: 1px; height: 32px; background: var(--husc-border);"></div>
                    <div>
                        <div class="fw-bold fs-4 text-warning">${holdingItems > 0 ? holdingItems : 0}</div>
                        <div class="small text-muted">Đang giữ</div>
                    </div>
                    <div style="width: 1px; height: 32px; background: var(--husc-border);"></div>
                    <div>
                        <div class="fw-bold fs-4 text-success">${returnedItems > 0 ? returnedItems : 0}</div>
                        <div class="small text-muted">Đã trả</div>
                    </div>
                </div>
            </div>

            <div class="col-lg-6">
                <div class="position-relative border rounded-3 overflow-hidden shadow-sm bg-white">
                    <img src="${pageContext.request.contextPath}/assets/img/hero-campus.jpg"
                         alt="Sinh viên HUSC" class="img-fluid w-100" style="object-fit: cover; aspect-ratio: 4/3;">
                </div>
            </div>
        </div>
    </div>
</section>

<section class="py-5 bg-white">
    <div class="container px-3 px-lg-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold mb-1">
                    <i class="bi bi-clock-history text-primary me-2"></i>Bảng tin gần đây
                </h3>
                <p class="text-muted small mb-0">Các đồ thất lạc mới được cập nhật trên hệ thống</p>
            </div>
            <a href="${pageContext.request.contextPath}/home" class="btn btn-husc-outline btn-sm">
                Xem tất cả <i class="bi bi-arrow-right ms-1"></i>
            </a>
        </div>

        <c:choose>
            <c:when test="${not empty recentItems}">
                <div class="row g-3 g-lg-4">
                    <c:forEach items="${recentItems}" var="item">
                        <div class="col-xl-3 col-lg-4 col-md-6 col-sm-6">
                            <c:set var="item" value="${item}" scope="request" />
                            <c:set var="showBookmark" value="${false}" scope="request" />
                            <c:set var="showTimestamp" value="${true}" scope="request" />
                            <jsp:include page="/inc/item_card.jsp" />
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-center py-5 text-muted">
                    <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                    Chưa có đồ thất lạc nào gần đây.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<jsp:include page="/inc/footer.jsp" />
