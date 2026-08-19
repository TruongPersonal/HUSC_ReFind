<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Đăng ký tài khoản — HUSC ReFind" scope="request" />
<jsp:include page="/inc/header.jsp" />

<div class="container py-5 my-auto d-flex flex-column justify-content-center" style="min-height: calc(100vh - 64px - 140px);">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5 col-xl-4">

            <div class="text-center mb-4">
                <img src="${pageContext.request.contextPath}/assets/img/logo.jpg" alt="HUSC ReFind" width="48" height="48" class="rounded-3 shadow-xs mb-2">
                <h4 class="fw-bold mb-1" style="color: var(--husc-dark);">Đăng ký</h4>
                <p class="small text-muted mb-0">Hệ thống quản lý đồ thất lạc HUSC</p>
            </div>

            <div class="card border rounded-3 p-4 bg-white shadow-xs">

                <div class="d-flex p-1 rounded mb-4" style="background: var(--husc-light); border: 1px solid var(--husc-border);">
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-sm w-50 fw-semibold text-muted">
                        Đăng nhập
                    </a>
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-sm w-50 fw-bold text-dark bg-white shadow-xs">
                        Đăng ký
                    </a>
                </div>

                <form action="${pageContext.request.contextPath}/register" method="POST">
                    <div class="mb-3">
                        <label for="code" class="form-label">Mã sinh viên <span class="text-danger">*</span></label>
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0 text-muted"><i class="bi bi-person-badge"></i></span>
                            <input type="text" class="form-control form-control-custom border-start-0" id="code" name="code"
                                   value="<c:out value='${param_code}'/>" placeholder="Ví dụ: 23T1020573" required autofocus>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="name" class="form-label">Họ tên <span class="text-danger">*</span></label>
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0 text-muted"><i class="bi bi-person"></i></span>
                            <input type="text" class="form-control form-control-custom border-start-0" id="name" name="name"
                                   value="<c:out value='${param_name}'/>" placeholder="Ví dụ: Nguyễn Văn An" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="phone" class="form-label">Số điện thoại</label>
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0 text-muted"><i class="bi bi-telephone"></i></span>
                            <input type="tel" class="form-control form-control-custom border-start-0" id="phone" name="phone"
                                   pattern="0[0-9]{9}" maxlength="10" title="Số điện thoại gồm 10 chữ số và bắt đầu bằng số 0"
                                   value="<c:out value='${param_phone}'/>" placeholder="Ví dụ: 0905123456">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="password" class="form-label">Mật khẩu <span class="text-danger">*</span></label>
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0 text-muted"><i class="bi bi-lock"></i></span>
                            <input type="password" class="form-control form-control-custom border-start-0" id="password" name="password"
                                   placeholder="Tối thiểu 6 ký tự" required>
                            <button class="btn btn-outline-secondary btn-toggle-password" type="button" data-target="password">
                                <i class="bi bi-eye"></i>
                            </button>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label for="confirm_password" class="form-label">Xác nhận mật khẩu <span class="text-danger">*</span></label>
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0 text-muted"><i class="bi bi-shield-lock"></i></span>
                            <input type="password" class="form-control form-control-custom border-start-0" id="confirm_password" name="confirm_password"
                                   placeholder="Nhập lại mật khẩu" required>
                            <button class="btn btn-outline-secondary btn-toggle-password" type="button" data-target="confirm_password">
                                <i class="bi bi-eye"></i>
                            </button>
                        </div>
                    </div>

                    <div class="d-grid mb-3">
                        <button type="submit" class="btn btn-husc-primary py-2 d-flex align-items-center justify-content-center gap-2">
                            <i class="bi bi-send"></i>
                            <span>Gửi OTP</span>
                        </button>
                    </div>

                    <div class="text-center small text-muted">
                        Đã có tài khoản?
                        <a href="${pageContext.request.contextPath}/login" class="fw-semibold text-primary">Đăng nhập</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/inc/footer.jsp" />
