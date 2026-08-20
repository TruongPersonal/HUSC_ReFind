<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="pageTitle" value="Bảng điều khiển - Quản trị HUSC ReFind" scope="request" />
<c:set var="activeAdminNav" value="dashboard" scope="request" />
<jsp:include page="/inc/admin_header.jsp" />

<div class="mb-4">
    <h4 class="fw-bold mb-1 text-dark">Tổng quan hệ thống</h4>
    <div class="small text-muted">Thống kê dữ liệu đồ thất lạc và tình trạng xử lý tại Trường ĐH Khoa học — ĐH Huế</div>
</div>

<div class="row g-3 mb-4">
    <div class="col-sm-6 col-xl-3">
        <div class="card border rounded-3 p-3 bg-white shadow-xs">
            <div class="d-flex justify-content-between align-items-center mb-2">
                <span class="text-muted small fw-semibold">Tổng số tin</span>
                <i class="bi bi-collection text-primary fs-5"></i>
            </div>
            <div class="fs-3 fw-bold text-dark">${totalItems}</div>
            <div class="text-muted small">Toàn bộ dữ liệu hệ thống</div>
        </div>
    </div>

    <div class="col-sm-6 col-xl-3">
        <div class="card border rounded-3 p-3 bg-white shadow-xs" style="border-left: 3px solid #DC2626 !important;">
            <div class="d-flex justify-content-between align-items-center mb-2">
                <span class="text-muted small fw-semibold">Đang tìm</span>
                <i class="bi bi-search text-danger fs-5"></i>
            </div>
            <div class="fs-3 fw-bold text-danger">${pendingLost}</div>
            <div class="text-muted small">Tin sinh viên báo mất</div>
        </div>
    </div>

    <div class="col-sm-6 col-xl-3">
        <div class="card border rounded-3 p-3 bg-white shadow-xs" style="border-left: 3px solid #D97706 !important;">
            <div class="d-flex justify-content-between align-items-center mb-2">
                <span class="text-muted small fw-semibold">Đang giữ</span>
                <i class="bi bi-shield-check text-warning fs-5"></i>
            </div>
            <div class="fs-3 fw-bold text-warning">${holdingCabinet}</div>
            <div class="text-muted small">Lưu tại Phòng Bảo vệ</div>
        </div>
    </div>

    <div class="col-sm-6 col-xl-3">
        <div class="card border rounded-3 p-3 bg-white shadow-xs" style="border-left: 3px solid #059669 !important;">
            <div class="d-flex justify-content-between align-items-center mb-2">
                <span class="text-muted small fw-semibold">Đã trả</span>
                <i class="bi bi-check2-circle text-success fs-5"></i>
            </div>
            <div class="fs-3 fw-bold text-success">${returnedItems}</div>
            <div class="text-muted small">Đã trao trả thành công</div>
        </div>
    </div>
</div>

<div class="row g-4 mb-4">

    <div class="col-lg-5">
        <div class="card border rounded-3 bg-white p-4 h-100 shadow-xs">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h6 class="fw-bold mb-0 text-dark">
                    <i class="bi bi-pie-chart-fill text-primary me-2"></i>Tỷ lệ trạng thái
                </h6>
            </div>
            <div class="position-relative d-flex align-items-center justify-content-center" style="min-height: 260px;">
                <canvas id="statusChart" data-pending="${pendingLost}" data-holding="${holdingCabinet}" data-returned="${returnedItems}" data-total="${totalItems}"></canvas>
            </div>
        </div>
    </div>

    <div class="col-lg-7">
        <div class="card border rounded-3 bg-white p-4 h-100 shadow-xs">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h6 class="fw-bold mb-0 text-dark">
                    <i class="bi bi-bar-chart-fill text-primary me-2"></i>Phân bố theo Danh mục
                </h6>
                <span class="small text-muted">Số lượng</span>
            </div>
            <div class="position-relative" style="min-height: 260px;">
                <div id="categoryChartData" class="d-none">
                    <c:forEach items="${categoryStats}" var="entry">
                        <span data-label="${entry.key}" data-value="${entry.value}"></span>
                    </c:forEach>
                </div>
                <canvas id="categoryChart"></canvas>
            </div>
        </div>
    </div>
</div>

<div class="card border rounded-3 bg-white p-4 shadow-xs mb-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h6 class="fw-bold mb-0 text-dark">
            <i class="bi bi-geo-alt-fill text-danger me-2"></i>Mật độ đồ thất lạc theo Khu vực
        </h6>
        <span class="small text-muted">Các vị trí thường xuyên thất lạc</span>
    </div>
    <div class="position-relative" style="min-height: ${not empty locationStats && locationStats.size() > 6 ? (locationStats.size() * 32 + 60) : 340}px;">
        <div id="locationChartData" class="d-none">
            <c:forEach items="${locationStats}" var="entry">
                <span data-label="${entry.key}" data-value="${entry.value}"></span>
            </c:forEach>
        </div>
        <canvas id="locationChart"></canvas>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/dashboard.js"></script>
<jsp:include page="/inc/admin_footer.jsp" />
