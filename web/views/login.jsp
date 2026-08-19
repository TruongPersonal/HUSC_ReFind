<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Đăng nhập — HUSC ReFind" scope="request" />
<jsp:include page="/inc/header.jsp" />

<div class="container py-5 my-auto d-flex flex-column justify-content-center" style="min-height: calc(100vh - 64px - 140px);">
    <div class="row justify-content-center">
        <div class="col-md-5 col-lg-4">

            <div class="text-center mb-4">
                <img src="${pageContext.request.contextPath}/assets/img/logo.jpg" alt="HUSC ReFind" width="48" height="48" class="rounded-3 shadow-xs mb-2">
                <h4 class="fw-bold mb-1" style="color: var(--husc-dark);">Đăng nhập</h4>
                <p class="small text-muted mb-0">Hệ thống quản lý đồ thất lạc HUSC</p>
            </div>

            <div class="card border rounded-3 p-4 bg-white shadow-xs">

                <div class="d-flex p-1 rounded mb-4" style="background: var(--husc-light); border: 1px solid var(--husc-border);">
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-sm w-50 fw-bold text-dark bg-white shadow-xs">
                        Đăng nhập
                    </a>
                    <a href="${pageContext.request.contextPath}/register" class="btn btn-sm w-50 fw-semibold text-muted">
                        Đăng ký
                    </a>
                </div>

                <form action="${pageContext.request.contextPath}/login" method="POST">
                    <div class="mb-3">
                        <label for="code" class="form-label">Mã người dùng <span class="text-danger">*</span></label>
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0 text-muted"><i class="bi bi-person"></i></span>
                            <input type="text" class="form-control form-control-custom border-start-0" id="code" name="code"
                                   value="<c:out value='${param_code}'/>" placeholder="Nhập mã người dùng" required autofocus>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label for="password" class="form-label">Mật khẩu <span class="text-danger">*</span></label>
                        <div class="input-group">
                            <span class="input-group-text bg-white border-end-0 text-muted"><i class="bi bi-lock"></i></span>
                            <input type="password" class="form-control form-control-custom border-start-0" id="password" name="password"
                                   placeholder="Nhập mật khẩu" required>
                            <button class="btn btn-outline-secondary btn-toggle-password" type="button" data-target="password">
                                <i class="bi bi-eye"></i>
                            </button>
                        </div>
                    </div>

                    <div class="d-grid mb-3">
                        <button type="submit" class="btn btn-husc-primary py-2 d-flex align-items-center justify-content-center gap-2">
                            <i class="bi bi-box-arrow-in-right"></i>
                            <span>Đăng nhập</span>
                        </button>
                    </div>

                    <div class="text-center small text-muted">
                        Chưa có tài khoản?
                        <a href="${pageContext.request.contextPath}/register" class="fw-semibold text-primary">Tạo mới ngay</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/inc/footer.jsp" />
