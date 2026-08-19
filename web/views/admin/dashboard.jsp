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
                <canvas id="statusChart"></canvas>
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
    <div class="position-relative" style="min-height: 280px;">
        <canvas id="locationChart"></canvas>
    </div>
</div>

<script>
document.addEventListener('DOMContentLoaded', () => {
    // 1. Biểu đồ tròn: Trạng thái xử lý
    const statusCtx = document.getElementById('statusChart');
    if (statusCtx) {
        new Chart(statusCtx, {
            type: 'doughnut',
            data: {
                labels: ['Đang tìm', 'Đang giữ', 'Đã trả'],
                datasets: [{
                    data: [${pendingLost}, ${holdingCabinet}, ${returnedItems}],
                    backgroundColor: ['#DC2626', '#D97706', '#059669'],
                    borderWidth: 2,
                    borderColor: '#FFFFFF'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            font: { family: 'Be Vietnam Pro', size: 13, weight: '500' },
                            padding: 16,
                            usePointStyle: true
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const total = ${totalItems > 0 ? totalItems : 1};
                                const value = context.raw || 0;
                                const percentage = Math.round((value / total) * 100);
                                return ' ' + context.label + ': ' + value + ' tin (' + percentage + '%)';
                            }
                        }
                    }
                },
                cutout: '65%'
            }
        });
    }

    // 2. Biểu đồ cột: Danh mục
    const categoryCtx = document.getElementById('categoryChart');
    if (categoryCtx) {
        new Chart(categoryCtx, {
            type: 'bar',
            data: {
                labels: [
                    <c:forEach items="${categoryStats}" var="entry" varStatus="loop">
                        '${entry.key}'${!loop.last ? ',' : ''}
                    </c:forEach>
                ],
                datasets: [{
                    label: 'Số lượng',
                    data: [
                        <c:forEach items="${categoryStats}" var="entry" varStatus="loop">
                            ${entry.value}${!loop.last ? ',' : ''}
                        </c:forEach>
                    ],
                    backgroundColor: '#1E3A8A',
                    borderRadius: 6,
                    maxBarThickness: 42
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: { stepSize: 1, precision: 0 },
                        grid: { color: '#E2E8F0' }
                    },
                    x: {
                        grid: { display: false },
                        ticks: { font: { family: 'Be Vietnam Pro', size: 12 } }
                    }
                }
            }
        });
    }

    // 3. Biểu đồ cột: Khu vực
    const locationCtx = document.getElementById('locationChart');
    if (locationCtx) {
        new Chart(locationCtx, {
            type: 'bar',
            data: {
                labels: [
                    <c:forEach items="${locationStats}" var="entry" varStatus="loop">
                        '${entry.key}'${!loop.last ? ',' : ''}
                    </c:forEach>
                ],
                datasets: [{
                    label: 'Số lượng',
                    data: [
                        <c:forEach items="${locationStats}" var="entry" varStatus="loop">
                            ${entry.value}${!loop.last ? ',' : ''}
                        </c:forEach>
                    ],
                    backgroundColor: '#2563EB',
                    borderRadius: 6,
                    maxBarThickness: 48
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: { stepSize: 1, precision: 0 },
                        grid: { color: '#E2E8F0' }
                    },
                    x: {
                        grid: { display: false },
                        ticks: { font: { family: 'Be Vietnam Pro', size: 12 } }
                    }
                }
            }
        });
    }
});
</script>

<jsp:include page="/inc/admin_footer.jsp" />
