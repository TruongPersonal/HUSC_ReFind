<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Quy trình — HUSC ReFind" scope="request" />
<c:set var="activeNav" value="about" scope="request" />
<jsp:include page="/inc/header.jsp" />

<div class="container px-3 px-lg-4 py-4">

    <div class="mb-4">
        <h2 class="fw-bold mb-1">Quy trình xử lý</h2>
        <p class="text-muted small mb-0">Hướng dẫn tiếp nhận và nhận lại tài sản tại Trường ĐH Khoa học — ĐH Huế</p>
    </div>

    <div class="row g-4 mb-5">

        <div class="col-lg-6">
            <div class="p-4 border rounded-3 bg-white h-100 shadow-xs">
                <div class="d-flex align-items-center gap-2 mb-3 text-primary">
                    <i class="bi bi-box-arrow-in-down fs-4"></i>
                    <h5 class="fw-bold mb-0 text-dark">1. Khi nhặt được đồ thất lạc</h5>
                </div>
                <ul class="list-unstyled d-flex flex-column gap-3 text-muted small mb-0" style="line-height: 1.6;">
                    <li class="d-flex gap-2">
                        <i class="bi bi-check-circle text-primary flex-shrink-0 mt-1"></i>
                        <span><strong>Bàn giao:</strong> Mang đồ vật đến nộp tại <strong>Phòng trực (77 Nguyễn Huệ)</strong>.</span>
                    </li>
                    <li class="d-flex gap-2">
                        <i class="bi bi-check-circle text-primary flex-shrink-0 mt-1"></i>
                        <span><strong>Ghi nhận:</strong> Bảo vệ kiểm tra tình trạng, tiếp nhận và cập nhật thông tin lên hệ thống.</span>
                    </li>
                    <li class="d-flex gap-2">
                        <i class="bi bi-check-circle text-primary flex-shrink-0 mt-1"></i>
                        <span><strong>Lưu giữ:</strong> Đồ vật được giữ an toàn cho đến khi có sinh viên đến nhận.</span>
                    </li>
                </ul>
            </div>
        </div>

        <div class="col-lg-6">
            <div class="p-4 border rounded-3 bg-white h-100 shadow-xs">
                <div class="d-flex align-items-center gap-2 mb-3 text-success">
                    <i class="bi bi-shield-check fs-4"></i>
                    <h5 class="fw-bold mb-0 text-dark">2. Khi đến nhận lại đồ thất lạc</h5>
                </div>
                <ul class="list-unstyled d-flex flex-column gap-3 text-muted small mb-0" style="line-height: 1.6;">
                    <li class="d-flex gap-2">
                        <i class="bi bi-check-circle text-success flex-shrink-0 mt-1"></i>
                        <span><strong>Tra cứu:</strong> Kiểm tra mục <strong>Đang giữ</strong> trên Bảng tin để xem món đồ đang gửi chưa.</span>
                    </li>
                    <li class="d-flex gap-2">
                        <i class="bi bi-check-circle text-success flex-shrink-0 mt-1"></i>
                        <span><strong>Giấy tờ cần mang:</strong> Xuất trình <strong>Thẻ sinh viên</strong> hoặc <strong>CCCD</strong> để xác minh danh tính.</span>
                    </li>
                    <li class="d-flex gap-2">
                        <i class="bi bi-check-circle text-success flex-shrink-0 mt-1"></i>
                        <span><strong>Đối chiếu:</strong> Mô tả chi tiết đặc điểm nhận dạng và nhận lại đồ.</span>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <div class="p-4 border rounded-3 bg-light">
        <h5 class="fw-bold mb-3"><i class="bi bi-question-circle text-primary me-2"></i>Câu hỏi thường gặp</h5>

        <div class="accordion accordion-flush" id="faqAccordion">
            <div class="accordion-item bg-transparent border-bottom">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed bg-transparent fw-semibold text-dark shadow-none px-0" type="button" data-bs-toggle="collapse" data-bs-target="#faq1">
                        Ai có thể đăng tin báo mất đồ trên hệ thống?
                    </button>
                </h2>
                <div id="faq1" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                    <div class="accordion-body text-muted small px-0">
                        Tất cả sinh viên Trường ĐH Khoa học — ĐH Huế đều có thể đăng ký tài khoản bằng MSV để đăng tin báo mất đồ.
                    </div>
                </div>
            </div>

            <div class="accordion-item bg-transparent border-bottom">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed bg-transparent fw-semibold text-dark shadow-none px-0" type="button" data-bs-toggle="collapse" data-bs-target="#faq2">
                        Đồ thất lạc được lưu giữ tại đâu?
                    </button>
                </h2>
                <div id="faq2" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                    <div class="accordion-body text-muted small px-0">
                        Đồ nhặt được được tiếp nhận và lưu giữ trực tiếp tại Phòng trực (77 Nguyễn Huệ, TP. Huế).
                    </div>
                </div>
            </div>

            <div class="accordion-item bg-transparent">
                <h2 class="accordion-header">
                    <button class="accordion-button collapsed bg-transparent fw-semibold text-dark shadow-none px-0" type="button" data-bs-toggle="collapse" data-bs-target="#faq3">
                        Sau khi nhận lại đồ, tôi cần làm gì?
                    </button>
                </h2>
                <div id="faq3" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                    <div class="accordion-body text-muted small px-0">
                        Bạn vào mục <strong>Tin đã đăng</strong> và bấm <strong>"Đã nhận"</strong> để cập nhật trạng thái tin bài thành <strong>Đã trả</strong>, giúp hệ thống luôn chính xác.
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/inc/footer.jsp" />
