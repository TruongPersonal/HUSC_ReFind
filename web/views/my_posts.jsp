<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="jakarta.tags.core" %>
        <%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <c:set var="pageTitle" value="Tin đã đăng — HUSC ReFind" scope="request" />
            <jsp:include page="/inc/header.jsp" />

            <div class="container px-3 px-lg-4 py-4">

                <div class="d-flex justify-content-between align-items-center flex-wrap gap-3 mb-4">
                    <div>
                        <h2 class="fw-bold mb-1">Tin đã đăng</h2>
                        <p class="text-muted small mb-0">Quản lý các tin báo mất đồ bạn đã tạo trên hệ thống</p>
                    </div>
                    <button type="button" class="btn btn-husc-primary btn-sm" data-bs-toggle="modal"
                        data-bs-target="#quickPostModal">
                        <i class="bi bi-plus-circle"></i> Báo mất đồ
                    </button>
                </div>

                <div class="d-flex gap-2 flex-wrap mb-4 pb-3 border-bottom">
                    <a href="${pageContext.request.contextPath}/my-posts?status=ALL"
                        class="btn btn-sm ${selectedStatus == 'ALL' ? 'btn-dark' : 'btn-light text-dark border'}">
                        Tất cả (${items.size()})
                    </a>
                    <a href="${pageContext.request.contextPath}/my-posts?status=1"
                        class="btn btn-sm ${selectedStatus == '1' ? 'btn-danger' : 'btn-light text-danger border'}">
                        <i class="bi bi-search"></i> Đang tìm
                    </a>
                    <a href="${pageContext.request.contextPath}/my-posts?status=2"
                        class="btn btn-sm ${selectedStatus == '2' ? 'btn-warning text-white' : 'btn-light text-warning border'}">
                        <i class="bi bi-shield-check"></i> Đang giữ
                    </a>
                    <a href="${pageContext.request.contextPath}/my-posts?status=0"
                        class="btn btn-sm ${selectedStatus == '0' ? 'btn-success' : 'btn-light text-success border'}">
                        <i class="bi bi-check2-circle"></i> Đã trả
                    </a>
                </div>

                <c:choose>
                    <c:when test="${not empty items}">
                        <div class="d-flex flex-column gap-3">
                            <c:forEach items="${items}" var="item">
                                <div
                                    class="p-3 border rounded-3 bg-white d-flex flex-column flex-md-row align-items-md-center justify-content-between gap-3 shadow-xs">
                                    <div class="d-flex align-items-center gap-3">
                                        <div
                                            style="width: 120px; height: 120px; min-width: 120px; min-height: 120px; border-radius: var(--radius-md); overflow: hidden; background: var(--husc-light); flex-shrink: 0; position: relative; border: 1px solid var(--husc-border);">

                                            <div
                                                style="position: absolute; top:0; left:0; width:100%; height:100%; display:flex; align-items:center; justify-content:center; color:var(--husc-muted);">
                                                <i class="bi bi-image fs-2"></i>
                                            </div>
                                            <c:if test="${not empty item.image}">
                                                <img src="${item.image.startsWith('http') ? item.image : pageContext.request.contextPath.concat('/assets/uploads/items/').concat(item.image)}"
                                                    alt="${item.title}"
                                                    style="position: relative; z-index: 1; width: 100%; height: 100%; object-fit: cover;"
                                                    onerror="this.style.display='none';">
                                            </c:if>

                                            <div class="position-absolute" style="top: 8px; left: 8px; z-index: 2;">
                                                <c:choose>
                                                    <c:when test="${item.status == 1}"><span
                                                            class="badge-status badge-lost"
                                                            style="font-size: 0.72rem; padding: 2px 8px; white-space: nowrap;"><i
                                                                class="bi bi-search" style="font-size: 0.68rem;"></i>
                                                            Đang tìm</span></c:when>
                                                    <c:when test="${item.status == 2}"><span
                                                            class="badge-status badge-holding"
                                                            style="font-size: 0.72rem; padding: 2px 8px; white-space: nowrap;"><i
                                                                class="bi bi-shield-check"
                                                                style="font-size: 0.68rem;"></i> Đang giữ</span>
                                                    </c:when>
                                                    <c:when test="${item.status == 0}"><span
                                                            class="badge-status badge-returned"
                                                            style="font-size: 0.72rem; padding: 2px 8px; white-space: nowrap;"><i
                                                                class="bi bi-check2-circle"
                                                                style="font-size: 0.68rem;"></i> Đã trả</span></c:when>
                                                </c:choose>
                                            </div>
                                        </div>
                                        <div>
                                            <div class="text-muted small mb-1">
                                                <i class="bi bi-clock me-1"></i>
                                                <fmt:formatDate value="${item.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                                            </div>
                                            <a href="${pageContext.request.contextPath}/item-detail?id=${item.id}"
                                                class="fw-bold text-dark text-decoration-none d-block mb-1">
                                                ${item.title}
                                            </a>
                                            <div class="d-flex gap-2 flex-wrap small text-muted">
                                                <span><i class="bi bi-tag text-primary"></i> ${item.categoryName}</span>
                                                <span>&bull;</span>
                                                <span><i class="bi bi-geo-alt text-danger"></i>
                                                    ${item.locationName}</span>
                                            </div>
                                            <c:if test="${not empty item.adminNote}">
                                                <div class="small text-warning fw-semibold mt-1">
                                                    <i class="bi bi-info-circle"></i> Ghi chú: ${item.adminNote}
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>

                                    <div class="d-flex align-items-center gap-2 flex-wrap">
                                        <a href="${pageContext.request.contextPath}/item-detail?id=${item.id}"
                                            class="btn btn-husc-outline btn-sm">
                                            <i class="bi bi-eye me-1"></i> Xem
                                        </a>

                                        <c:if test="${item.status == 1}">
                                            <button type="button" class="btn btn-husc-outline btn-sm"
                                                data-bs-toggle="modal" data-bs-target="#editPostModal${item.id}">
                                                <i class="bi bi-pencil me-1"></i> Sửa
                                            </button>
                                        </c:if>

                                        <c:if test="${item.status != 0}">
                                            <form action="${pageContext.request.contextPath}/close-item" method="POST"
                                                onsubmit="return confirm('Xác nhận bạn đã nhận lại đồ?');"
                                                style="display: inline;">
                                                <input type="hidden" name="id" value="${item.id}">
                                                <button type="submit" class="btn btn-success btn-sm">
                                                    <i class="bi bi-check2 me-1"></i> Đã nhận
                                                </button>
                                            </form>
                                        </c:if>

                                        <form action="${pageContext.request.contextPath}/delete-item" method="POST"
                                            onsubmit="return confirm('Xác nhận xóa tin báo này?');"
                                            style="display: inline;">
                                            <input type="hidden" name="id" value="${item.id}">
                                            <button type="submit" class="btn btn-outline-danger btn-sm">
                                                <i class="bi bi-trash me-1"></i> Xóa
                                            </button>
                                        </form>
                                    </div>
                                </div>

                                <c:if test="${item.status == 1}">
                                    <div class="modal fade modal-husc text-start" id="editPostModal${item.id}"
                                        tabindex="-1" aria-hidden="true">
                                        <div class="modal-dialog modal-dialog-centered">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title"><i class="bi bi-pencil text-primary"></i>
                                                        Sửa tin</h5>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                        aria-label="Close"></button>
                                                </div>

                                                <form action="${pageContext.request.contextPath}/edit-item"
                                                    method="POST" enctype="multipart/form-data">
                                                    <input type="hidden" name="id" value="${item.id}">
                                                    <input type="hidden" name="redirect_url"
                                                        value="${pageContext.request.contextPath}/my-posts">

                                                    <div class="modal-body">
                                                        <div class="mb-3">
                                                            <label class="form-label">Tên đồ vật <span
                                                                    class="text-danger">*</span></label>
                                                            <input type="text" class="form-control form-control-custom"
                                                                name="title" value="${item.title}" required>
                                                        </div>

                                                        <div class="row g-3 mb-3">
                                                            <div class="col-md-6">
                                                                <label class="form-label">Danh mục <span
                                                                        class="text-danger">*</span></label>
                                                                <select class="form-select form-select-custom"
                                                                    name="category_id" required>
                                                                    <c:forEach items="${categories}" var="cat">
                                                                        <option value="${cat.id}"
                                                                            ${item.categoryId==cat.id ? 'selected' : ''
                                                                            }>${cat.name}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                            <div class="col-md-6">
                                                                <label class="form-label">Khu vực <span
                                                                        class="text-danger">*</span></label>
                                                                <select class="form-select form-select-custom"
                                                                    name="location_id" required>
                                                                    <c:forEach items="${locations}" var="loc">
                                                                        <option value="${loc.id}"
                                                                            ${item.locationId==loc.id ? 'selected' : ''
                                                                            }>${loc.name}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>

                                                        <div class="mb-3">
                                                            <label class="form-label">Mô tả</label>
                                                            <textarea class="form-control form-control-custom"
                                                                name="description"
                                                                rows="3">${item.description}</textarea>
                                                        </div>

                                                        <div class="mb-2">
                                                            <label class="form-label">Ảnh</label>
                                                            <div class="image-dropzone py-3">
                                                                <div class="dropzone-prompt text-center">
                                                                    <i
                                                                        class="bi bi-camera fs-3 text-muted d-block mb-1"></i>
                                                                    <div class="small fw-semibold text-dark">Bấm hoặc
                                                                        kéo thả ảnh vào đây</div>
                                                                </div>
                                                                <input type="file" name="image" accept="image/*"
                                                                    class="d-none input-image-preview">
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-husc-outline btn-sm"
                                                            data-bs-dismiss="modal">Hủy</button>
                                                        <button type="submit" class="btn btn-husc-primary btn-sm"><i
                                                                class="bi bi-check2"></i> Xác nhận</button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5 border rounded-3 bg-light my-3">
                            <i class="bi bi-file-earmark-text fs-1 text-muted d-block mb-2"></i>
                            <h5 class="fw-bold mb-0">Bạn chưa có tin báo mất nào</h5>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <jsp:include page="/inc/footer.jsp" />