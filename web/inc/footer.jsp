<%@page contentType="text/html" pageEncoding="UTF-8"%>
</main>

<footer class="pt-4 pb-2 mt-auto" style="background: var(--husc-light); border-top: 1px solid var(--husc-border);">
    <div class="container px-3 px-lg-4">
        <div class="row gy-3">

            <div class="col-md-4">
                <a href="${pageContext.request.contextPath}/" class="d-inline-flex align-items-center gap-2 text-decoration-none mb-2">
                    <img src="${pageContext.request.contextPath}/assets/img/logo.jpg" alt="HUSC ReFind" width="28" height="28" style="border-radius: 6px; object-fit: cover;">
                    <span style="font-family: var(--font-display); font-weight: 700; font-size: 1.05rem; color: var(--husc-dark);">HUSC ReFind</span>
                </a>
                <p class="mb-0 text-muted" style="font-size: 0.85rem; max-width: 300px; line-height: 1.5;">
                    Cổng quản lý và trao trả đồ thất lạc — Trường Đại học Khoa học, Đại học Huế.
                </p>
            </div>

            <div class="col-md-4">
                <h6 class="fw-bold mb-2 text-uppercase small text-muted" style="letter-spacing: 0.04em;">Điều hướng</h6>
                <div class="d-flex flex-column gap-1">
                    <a href="${pageContext.request.contextPath}/home" class="text-decoration-none text-dark small">
                        <i class="bi bi-chevron-right me-1 text-muted"></i> Bảng tin
                    </a>
                    <a href="${pageContext.request.contextPath}/about" class="text-decoration-none text-dark small">
                        <i class="bi bi-chevron-right me-1 text-muted"></i> Quy trình
                    </a>
                    <a href="${pageContext.request.contextPath}/contact" class="text-decoration-none text-dark small">
                        <i class="bi bi-chevron-right me-1 text-muted"></i> Liên hệ
                    </a>
                </div>
            </div>

            <div class="col-md-4">
                <h6 class="fw-bold mb-2 text-uppercase small text-muted" style="letter-spacing: 0.04em;">Phòng trực</h6>
                <div class="d-flex flex-column gap-1 small">
                    <div class="d-flex align-items-center gap-2 text-dark">
                        <i class="bi bi-geo-alt text-danger"></i>
                        <span>77 Nguyễn Huệ, TP. Huế</span>
                    </div>
                    <div class="d-flex align-items-center gap-2 text-dark">
                        <i class="bi bi-telephone text-primary"></i>
                        <a href="tel:02343823290" class="fw-semibold text-dark">(0234) 3823290</a>
                    </div>
                    <div class="d-flex align-items-center gap-2 text-muted">
                        <i class="bi bi-clock text-warning"></i>
                        <span>07:00 – 17:30 (Thứ 2 – Thứ 7)</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="border-top mt-3 pt-2 text-center text-muted small" style="font-size: 0.8rem;">
            &copy; 2026 All Right Reserved
        </div>
    </div>
</footer>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script src="${pageContext.request.contextPath}/assets/js/main.js?v=20260819_v10"></script>
</body>
</html>
