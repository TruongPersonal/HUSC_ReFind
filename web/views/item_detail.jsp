<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="pageTitle" value="${item.title} — HUSC ReFind" scope="request" />
<jsp:include page="/inc/header.jsp" />

<div class="container py-4">

    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-sm btn-husc-outline">
            <i class="bi bi-arrow-left me-1"></i> Quay lại
        </a>
    </div>

    <div class="row g-4">

        <div class="col-lg-5">
            <div class="detail-photo-frame h-100 d-flex flex-column border rounded-3 overflow-hidden bg-white shadow-xs">
                <div style="width: 100%; height: 320px; background-color: #0f172a; display: flex; align-items: center; justify-content: center; position: relative; overflow: hidden;">
                    <c:choose>
                        <c:when test="${not empty item.image}">
                            <img src="${item.image.startsWith('http') ? item.image : pageContext.request.contextPath.concat('/assets/uploads/items/').concat(item.image)}"
                                 alt="${item.title}" style="position: absolute; inset: 0; width: 100%; height: 100%; object-fit: contain;"
                                 onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                            <div style="display: none; color: var(--husc-muted); font-size: 3.5rem;">
                                <i class="bi bi-image"></i>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="color: var(--husc-muted); font-size: 3.5rem;">
                                <i class="bi bi-image"></i>
                            </div>
                        </c:otherwise>
                    </c:choose>

                    <div class="position-absolute" style="top: 12px; left: 12px; z-index: 2;">
                        <c:choose>
                            <c:when test="${item.status == 1}"><span class="badge-status badge-lost"><i class="bi bi-search"></i> Đang tìm</span></c:when>
                            <c:when test="${item.status == 2}"><span class="badge-status badge-holding"><i class="bi bi-shield-check"></i> Đang giữ</span></c:when>
                            <c:when test="${item.status == 0}"><span class="badge-status badge-returned"><i class="bi bi-check2-circle"></i> Đã nhận</span></c:when>
                        </c:choose>
                    </div>
                </div>
                <div class="p-3 bg-white flex-grow-1 border-top">
                    <h4 class="fw-bold mb-2 text-dark">${item.title}</h4>
                    <div class="text-muted small mb-1 fw-semibold">Mô tả:</div>
                    <div style="white-space: pre-line; color: var(--husc-body); line-height: 1.6; font-size: 0.92rem;">${not empty item.description ? item.description : 'Không có mô tả chi tiết.'}</div>
                </div>
            </div>
        </div>

        <div class="col-lg-7">
            <div class="card border rounded-3 p-4 bg-white h-100 d-flex flex-column shadow-xs">

                <div class="d-flex justify-content-between align-items-center mb-3">
                    <div class="small text-muted">
                        <i class="bi bi-clock me-1"></i> Đăng lúc <fmt:formatDate value="${item.createdAt}" pattern="HH:mm - dd/MM/yyyy" />
                    </div>
                    <button type="button" class="btn btn-sm btn-husc-outline btn-bookmark-ajax ${isSaved ? 'active' : ''}"
                            data-item-id="${item.id}" data-context-path="${pageContext.request.contextPath}">
                        <i class="bi ${isSaved ? 'bi-heart-fill text-danger' : 'bi-heart'}"></i>
                        <span class="ms-1">${isSaved ? 'Đã lưu' : 'Lưu tin'}</span>
                    </button>
                </div>

                <div class="d-flex flex-wrap align-items-center gap-2 mb-3">
                    <span class="badge bg-light text-dark fw-normal border px-2 py-1">
                        <i class="bi bi-tag me-1 text-primary"></i> ${item.categoryName}
                    </span>

                    <span class="badge bg-light text-dark fw-normal border px-2 py-1">
                        <i class="bi bi-geo-alt me-1 text-danger"></i> ${item.locationName}
                    </span>
                </div>

                <div class="d-flex flex-column gap-3 mb-4">
                    <c:if test="${item.status == 2 || not empty item.adminNote}">
                        <div class="detail-notice-box">
                            <div class="fw-bold mb-1" style="color: #92400e; font-size: 0.9rem;">
                                <i class="bi bi-info-circle-fill me-1"></i> Ghi chú: <span style="color: var(--husc-dark);">${not empty item.adminNote ? item.adminNote : 'Đang lưu tại Phòng trực'}</span>
                            </div>
                            <div class="small" style="color: #b45309;">
                                Sinh viên mang Thẻ SV hoặc CCCD đến đối chiếu đặc điểm để nhận lại đồ.
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${item.studentPost}">
                        <div class="p-3 border rounded-3 bg-light small">
                            <div class="fw-bold text-dark mb-2"><i class="bi bi-person me-1 text-primary"></i> Sinh viên báo mất:</div>
                            <div class="row g-2 text-muted">
                                <div class="col-sm-6">Họ tên: <strong class="text-dark">${item.authorName}</strong></div>
                                <c:if test="${not empty item.authorCode}">
                                    <div class="col-sm-6">MSV: <strong class="text-dark">${item.authorCode}</strong></div>
                                </c:if>
                                <c:if test="${not empty item.authorPhone}">
                                    <div class="col-sm-6">SĐT: <strong class="text-primary">${item.authorPhone}</strong></div>
                                </c:if>
                                <c:if test="${not empty item.authorEmail}">
                                    <div class="col-sm-6">Email: <strong class="text-dark">${item.authorEmail}</strong></div>
                                </c:if>
                            </div>
                        </div>
                    </c:if>
                </div>

                <div class="mt-auto pt-3 border-top d-flex gap-2 flex-wrap align-items-center">
                    <c:if test="${not empty sessionScope.user && sessionScope.user.id == item.userId && item.status == 1}">
                        <button type="button" class="btn btn-husc-outline btn-sm"
                                data-bs-toggle="modal" data-bs-target="#editItemDetailModal">
                            <i class="bi bi-pencil me-1"></i> Sửa tin
                        </button>
                    </c:if>

                    <c:if test="${not empty sessionScope.user && sessionScope.user.id == item.userId && item.status != 0}">
                        <form action="${pageContext.request.contextPath}/close-item" method="POST" onsubmit="return confirm('Xác nhận bạn đã nhận lại được đồ thất lạc này?');" style="display: inline;">
                            <input type="hidden" name="id" value="${item.id}">
                            <button type="submit" class="btn btn-success btn-sm fw-semibold">
                                <i class="bi bi-check2 me-1"></i> Đã nhận
                            </button>
                        </form>
                    </c:if>

                    <c:if test="${not empty sessionScope.user && sessionScope.user.id == item.userId}">
                        <form action="${pageContext.request.contextPath}/delete-item" method="POST" onsubmit="return confirm('Xác nhận xóa bài đăng này?');" style="display: inline;" class="ms-auto">
                            <input type="hidden" name="id" value="${item.id}">
                            <button type="submit" class="btn btn-outline-danger btn-sm">
                                <i class="bi bi-trash"></i> Xóa
                            </button>
                        </form>
                    </c:if>
                </div>

                <c:if test="${not empty sessionScope.user && sessionScope.user.id == item.userId && item.status == 1}">
                    <div class="modal fade modal-husc" id="editItemDetailModal" tabindex="-1" aria-hidden="true">
                        <div class="modal-dialog modal-dialog-centered">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title"><i class="bi bi-pencil text-primary"></i> Sửa bài đăng</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <form action="${pageContext.request.contextPath}/edit-item" method="POST" enctype="multipart/form-data">
                                    <input type="hidden" name="id" value="${item.id}">
                                    <input type="hidden" name="redirect_url" value="${pageContext.request.contextPath}/item-detail?id=${item.id}">

                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label class="form-label">Tên đồ vật <span class="text-danger">*</span></label>
                                            <input type="text" class="form-control form-control-custom" name="title" value="${item.title}" required>
                                        </div>

                                        <div class="row g-3 mb-3">
                                            <div class="col-md-6">
                                                <label class="form-label">Danh mục <span class="text-danger">*</span></label>
                                                <select class="form-select form-select-custom" name="category_id" required>
                                                    <c:forEach items="${categories}" var="cat">
                                                        <option value="${cat.id}" ${item.categoryId == cat.id ? 'selected' : ''}>${cat.name}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="col-md-6">
                                                <label class="form-label">Khu vực <span class="text-danger">*</span></label>
                                                <select class="form-select form-select-custom" name="location_id" required>
                                                    <c:forEach items="${locations}" var="loc">
                                                        <option value="${loc.id}" ${item.locationId == loc.id ? 'selected' : ''}>${loc.name}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="mb-3">
                                            <label class="form-label">Mô tả</label>
                                            <textarea class="form-control form-control-custom" name="description" rows="3">${item.description}</textarea>
                                        </div>

                                        <div class="mb-2">
                                            <label class="form-label">Ảnh</label>
                                            <div class="image-dropzone py-3">
                                                <div class="dropzone-prompt text-center">
                                                    <i class="bi bi-camera fs-3 text-muted d-block mb-1"></i>
                                                    <div class="small fw-semibold text-dark">Bấm hoặc kéo thả ảnh vào đây</div>
                                                </div>
                                                <input type="file" name="image" accept="image/*" class="d-none input-image-preview">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-husc-outline btn-sm" data-bs-dismiss="modal">Hủy</button>
                                        <button type="submit" class="btn btn-husc-primary btn-sm"><i class="bi bi-check2"></i> Xác nhận</button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <c:if test="${not empty matchingItems}">
        <div class="mt-5 pt-4 border-top">
            <h5 class="fw-bold mb-3">
                <i class="bi bi-grid-fill text-primary me-2"></i>Đồ vật tương tự theo khu vực & danh mục
            </h5>

            <div class="row g-3">
                <c:forEach items="${matchingItems}" var="matched">
                    <div class="col-md-4 col-sm-6">
                        <div class="p-3 border rounded-3 bg-white h-100 d-flex flex-column shadow-xs">
                            <div class="d-flex gap-3">
                                <div style="width: 60px; height: 60px; border-radius: var(--radius-sm); overflow: hidden; background-color: var(--husc-dark); flex-shrink: 0;">
                                    <img src="${matched.image.startsWith('http') ? matched.image : pageContext.request.contextPath.concat('/assets/uploads/items/').concat(matched.image)}"
                                         alt="${matched.title}" style="width: 100%; height: 100%; object-fit: cover;"
                                         onerror="this.onerror=null; this.style.display='none'; this.parentElement.innerHTML='<div style=\'display:flex;align-items:center;justify-content:center;width:100%;height:100%;color:var(--husc-muted);\'><i class=\'bi bi-image\'></i></div>';">
                                </div>
                                <div class="flex-grow-1 overflow-hidden">
                                    <span class="badge-status badge-holding mb-1" style="font-size: 0.68rem;">Đang giữ</span>
                                    <a href="${pageContext.request.contextPath}/item-detail?id=${matched.id}" class="fw-semibold d-block text-truncate small text-dark">
                                        ${matched.title}
                                    </a>
                                    <c:if test="${not empty matched.adminNote}">
                                        <div class="small text-warning fw-semibold mt-1 text-truncate" style="font-size: 0.78rem;">
                                            <i class="bi bi-info-circle"></i> ${matched.adminNote}
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                            <div class="mt-2 text-end mt-auto">
                                <a href="${pageContext.request.contextPath}/item-detail?id=${matched.id}" class="btn btn-sm btn-husc-outline py-0 px-2" style="font-size: 0.78rem;">
                                    Xem chi tiết <i class="bi bi-arrow-right"></i>
                                </a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:if>
</div>

<jsp:include page="/inc/footer.jsp" />
