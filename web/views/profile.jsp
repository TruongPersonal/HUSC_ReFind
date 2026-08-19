<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Thông tin tài khoản — HUSC ReFind" scope="request" />
<jsp:include page="/inc/header.jsp" />

<div class="container px-3 px-lg-4 py-4">

    <div class="card border rounded-3 p-4 bg-white mb-4 shadow-xs">
        <div class="d-flex align-items-center gap-3">
            <div class="rounded-circle d-flex align-items-center justify-content-center flex-shrink-0"
                 style="width: 72px; height: 72px; background: var(--husc-accent-soft); color: var(--husc-primary); font-size: 2rem;">
                <i class="bi bi-person-fill"></i>
            </div>
            <div>
                <h4 class="fw-bold mb-1 text-dark">${sessionScope.user.name}</h4>
                <div class="text-muted small">
                    <span>Mã sinh viên:</span> <strong class="text-dark font-monospace">${sessionScope.user.code}</strong>
                </div>
            </div>
        </div>
    </div>

    <div class="row g-4">

        <div class="col-lg-6">
            <div class="card border rounded-3 p-4 bg-white h-100 shadow-xs">
                <div class="d-flex align-items-center gap-2 mb-3 text-primary">
                    <i class="bi bi-person-lines-fill fs-4"></i>
                    <h5 class="fw-bold mb-0 text-dark">Thông tin</h5>
                </div>

                <form action="${pageContext.request.contextPath}/profile" method="POST">
                    <input type="hidden" name="action" value="update_info">

                    <div class="mb-3">
                        <label class="form-label">Họ và tên <span class="text-danger">*</span></label>
                        <input type="text" class="form-control form-control-custom" name="name"
                               value="${sessionScope.user.name}" placeholder="Ví dụ: Nguyễn Văn An" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Địa chỉ Email</label>
                        <input type="email" class="form-control form-control-custom" name="email"
                               value="${sessionScope.user.email}" placeholder="Ví dụ: nguyenvana@husc.edu.vn">
                    </div>

                    <div class="mb-4">
                        <label class="form-label">Số điện thoại liên hệ</label>
                        <input type="tel" class="form-control form-control-custom" name="phone"
                               pattern="0[0-9]{9}" maxlength="10" title="Số điện thoại gồm 10 chữ số và bắt đầu bằng số 0"
                               value="${sessionScope.user.phone}" placeholder="Ví dụ: 0905123456">
                    </div>

                    <button type="submit" class="btn btn-husc-primary btn-sm">
                        <i class="bi bi-check2 me-1"></i> Lưu mới
                    </button>
                </form>
            </div>
        </div>

        <div class="col-lg-6">
            <div class="card border rounded-3 p-4 bg-white h-100 shadow-xs">
                <div class="d-flex align-items-center gap-2 mb-3 text-warning">
                    <i class="bi bi-shield-lock-fill fs-4"></i>
                    <h5 class="fw-bold mb-0 text-dark">Mật khẩu</h5>
                </div>

                <form action="${pageContext.request.contextPath}/profile" method="POST">
                    <input type="hidden" name="action" value="change_password">

                    <div class="mb-3">
                        <label class="form-label">Mật khẩu hiện tại <span class="text-danger">*</span></label>
                        <div class="input-group">
                            <input type="password" class="form-control form-control-custom" id="current_password" name="current_password" required>
                            <button class="btn btn-outline-secondary btn-toggle-password" type="button" data-target="current_password">
                                <i class="bi bi-eye"></i>
                            </button>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Mật khẩu mới <span class="text-danger">*</span></label>
                        <div class="input-group">
                            <input type="password" class="form-control form-control-custom" id="new_password" name="new_password" placeholder="Tối thiểu 6 ký tự" required>
                            <button class="btn btn-outline-secondary btn-toggle-password" type="button" data-target="new_password">
                                <i class="bi bi-eye"></i>
                            </button>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label class="form-label">Xác nhận mật khẩu mới <span class="text-danger">*</span></label>
                        <div class="input-group">
                            <input type="password" class="form-control form-control-custom" id="confirm_password" name="confirm_password" placeholder="Nhập lại mật khẩu mới" required>
                            <button class="btn btn-outline-secondary btn-toggle-password" type="button" data-target="confirm_password">
                                <i class="bi bi-eye"></i>
                            </button>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-husc-outline btn-sm">
                        <i class="bi bi-key me-1"></i> Cập nhật
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/inc/footer.jsp" />
