<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Liên hệ — HUSC ReFind" scope="request" />
<c:set var="activeNav" value="contact" scope="request" />
<jsp:include page="/inc/header.jsp" />

<div class="container px-3 px-lg-4 py-4">

    <div class="mb-4">
        <h2 class="fw-bold mb-1">Thông tin liên hệ</h2>
        <p class="text-muted small mb-0">Địa chỉ tiếp nhận và hỗ trợ tìm kiếm đồ thất lạc tại trường</p>
    </div>

    <div class="row g-4 mb-4">

        <div class="col-md-6">
            <div class="p-4 border rounded-3 bg-white h-100 shadow-xs">
                <div class="d-flex align-items-center gap-2 mb-3 text-danger">
                    <i class="bi bi-shield-shaded fs-3"></i>
                    <h5 class="fw-bold mb-0 text-dark">Phòng Bảo vệ</h5>
                </div>
                <div class="d-flex flex-column gap-2 text-muted small" style="line-height: 1.6;">
                    <div><i class="bi bi-geo-alt text-danger me-2"></i><strong>Địa chỉ:</strong> 77 Nguyễn Huệ, TP. Huế</div>
                    <div><i class="bi bi-telephone text-primary me-2"></i><strong>Điện thoại:</strong> <a href="tel:02343823290" class="text-dark fw-semibold">(0234) 3823290</a></div>
                    <div><i class="bi bi-clock text-warning me-2"></i><strong>Thời gian trực:</strong> 07:00 – 17:30 (Thứ 2 – Thứ 7)</div>
                    <div class="mt-2 text-dark bg-light p-2 rounded">
                        <i class="bi bi-info-circle me-1 text-primary"></i> Tiếp nhận và bàn giao đồ thất lạc trực tiếp cho sinh viên.
                    </div>
                </div>
            </div>
        </div>

        <div class="col-md-6">
            <div class="p-4 border rounded-3 bg-white h-100 shadow-xs">
                <div class="d-flex align-items-center gap-2 mb-3 text-primary">
                    <i class="bi bi-building fs-3"></i>
                    <h5 class="fw-bold mb-0 text-dark">Phòng Công tác SV</h5>
                </div>
                <div class="d-flex flex-column gap-2 text-muted small" style="line-height: 1.6;">
                    <div><i class="bi bi-geo-alt text-danger me-2"></i><strong>Vị trí:</strong> Tầng 1 — Dãy nhà A (77 Nguyễn Huệ)</div>
                    <div><i class="bi bi-envelope text-primary me-2"></i><strong>Email:</strong> <a href="mailto:ctsv@husc.edu.vn" class="text-dark">ctsv@husc.edu.vn</a></div>
                    <div><i class="bi bi-clock text-warning me-2"></i><strong>Giờ hành chính:</strong> 07:30 – 11:30 | 13:30 – 17:00</div>
                    <div class="mt-2 text-dark bg-light p-2 rounded">
                        <i class="bi bi-info-circle me-1 text-primary"></i> Hỗ trợ xác nhận giấy tờ tùy thân, thẻ sinh viên bị thất lạc.
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="border rounded-3 overflow-hidden bg-white shadow-xs">
        <div class="p-3 border-bottom bg-light d-flex align-items-center gap-2">
            <i class="bi bi-map text-primary"></i>
            <span class="fw-semibold small text-dark">Vị trí Trường Đại học Khoa học — Đại học Huế</span>
        </div>
        <div style="height: 320px; width: 100%;">
            <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3826.3775438848777!2d107.58788487588386!3d16.456345984280597!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3141a13e2f9d5059%3A0xb36ef207d57df4a3!2zVHLGsOG7nW5nIMSQ4bqhaSBo4buNYyBLaG9hIGjhu41jIC0gxJDhuqFpIGjhu41jIEh14bq_!5e0!3m2!1svi!2s!4v1718000000000!5m2!1svi!2s"
                    width="100%" height="100%" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade">
            </iframe>
        </div>
    </div>
</div>

<jsp:include page="/inc/footer.jsp" />
