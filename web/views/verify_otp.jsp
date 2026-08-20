<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Xác thực OTP Email — HUSC ReFind" scope="request" />
<jsp:include page="/inc/header.jsp" />

<div class="container py-5 my-auto d-flex flex-column justify-content-center" style="min-height: calc(100vh - 64px - 140px);">
    <div class="row justify-content-center">
        <div class="col-md-5 col-lg-4">
            <div class="card border rounded-3 p-4 bg-white shadow-xs text-center">
                <div class="d-inline-flex align-items-center justify-content-center rounded-circle mb-3 mx-auto text-primary bg-light"
                     style="width: 52px; height: 52px; font-size: 1.5rem;">
                    <i class="bi bi-shield-check"></i>
                </div>

                <h4 class="fw-bold mb-1" style="color: var(--husc-dark);">Xác thực OTP</h4>
                <p class="small text-muted mb-4">
                    Mã 6 chữ số đã gửi tới email:<br>
                    <strong class="text-primary">${sessionScope.pendingRegistration.email}</strong>
                </p>

                <form action="${pageContext.request.contextPath}/verify-otp" method="POST" class="mb-3">
                    <input type="hidden" name="action" value="verify">
                    <div class="mb-4">
                        <input type="text" class="form-control form-control-custom text-center fw-bold fs-4"
                               id="otp" name="otp" maxlength="6" pattern="\d{6}" placeholder="------" required autofocus
                               style="letter-spacing: 0.35em;">
                        <div class="small mt-2 text-muted">Mã có hiệu lực trong vòng 5 phút</div>
                    </div>

                    <div class="d-grid">
                        <button type="submit" class="btn btn-husc-primary py-2 d-flex align-items-center justify-content-center gap-2">
                            <i class="bi bi-check2-circle"></i>
                            <span>Kích hoạt</span>
                        </button>
                    </div>
                </form>

                <div class="d-flex justify-content-between align-items-center small pt-2 border-top text-muted">
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-sm btn-link text-muted p-0 text-decoration-none" title="Quay lại">
                        <i class="bi bi-arrow-left fs-5"></i>
                    </a>
                    <form action="${pageContext.request.contextPath}/verify-otp" method="POST" class="d-inline m-0 p-0" id="resendOtpForm">
                        <input type="hidden" name="action" value="resend">
                        <button type="submit" id="resendOtpBtn" data-cooldown="${cooldownSeconds != null ? cooldownSeconds : 0}" class="btn btn-link fw-bold p-0 border-0 align-baseline text-primary" style="font-size: 0.85rem;">
                            <span>Gửi lại</span> <span id="cooldownTimer"></span>
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/inc/footer.jsp" />
