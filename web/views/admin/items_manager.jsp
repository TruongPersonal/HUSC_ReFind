<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="pageTitle" value="Đồ thất lạc — Quản trị HUSC ReFind" scope="request" />
<c:set var="activeAdminNav" value="items" scope="request" />
<jsp:include page="/inc/admin_header.jsp" />

<div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
    <div>
        <h4 class="fw-bold mb-1 text-dark">Quản lý đồ thất lạc</h4>
        <div class="text-muted small">Kiểm soát tất cả đồ thất lạc và tình trạng tiếp nhận, trao trả tại trường</div>
    </div>
    <button type="button" class="btn btn-husc-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addFoundModal">
        <i class="bi bi-plus-circle me-1"></i> Tiếp nhận đồ
    </button>
</div>

<div class="card border rounded-3 p-3 bg-white mb-4 shadow-xs">
    <div class="d-flex flex-wrap align-items-center justify-content-between gap-3">

        <div class="d-flex align-items-center gap-2 flex-wrap">
            <span class="small fw-semibold text-muted me-1">Trạng thái:</span>
            <a href="${pageContext.request.contextPath}/admin/items?status=ALL<c:if test='${not empty keyword}'>&keyword=${keyword}</c:if>"
               class="btn btn-sm ${selectedStatus == 'ALL' ? 'btn-dark' : 'btn-light text-dark border'}">
                Tất cả (${items.size()})
            </a>
            <a href="${pageContext.request.contextPath}/admin/items?status=1<c:if test='${not empty keyword}'>&keyword=${keyword}</c:if>"
               class="btn btn-sm ${selectedStatus == '1' ? 'btn-danger' : 'btn-light text-danger border'}">
                <i class="bi bi-search me-1"></i> Đang tìm
            </a>
            <a href="${pageContext.request.contextPath}/admin/items?status=2<c:if test='${not empty keyword}'>&keyword=${keyword}</c:if>"
               class="btn btn-sm ${selectedStatus == '2' ? 'btn-warning text-white' : 'btn-light text-warning border'}">
                <i class="bi bi-shield-check me-1"></i> Đang giữ
            </a>
            <a href="${pageContext.request.contextPath}/admin/items?status=0<c:if test='${not empty keyword}'>&keyword=${keyword}</c:if>"
               class="btn btn-sm ${selectedStatus == '0' ? 'btn-success' : 'btn-light text-success border'}">
                <i class="bi bi-check2-circle me-1"></i> Đã trả
            </a>
        </div>

        <form action="${pageContext.request.contextPath}/admin/items" method="GET" class="d-flex gap-2">
            <input type="hidden" name="status" value="${selectedStatus}">
            <input type="text" name="keyword" class="form-control form-control-custom"
                   placeholder="Tìm theo thông tin, người đăng" value="<c:out value='${keyword}'/>" style="min-width: 240px;">
            <button type="submit" class="btn btn-husc-primary btn-sm px-3 flex-shrink-0">Tìm</button>
            <c:if test="${not empty keyword || selectedStatus != 'ALL'}">
                <a href="${pageContext.request.contextPath}/admin/items" class="btn btn-husc-outline btn-sm px-2 flex-shrink-0">
                    <i class="bi bi-x-lg"></i>
                </a>
            </c:if>
        </form>
    </div>
</div>

<div class="card border rounded-3 bg-white overflow-hidden shadow-xs">
    <div class="table-responsive">
        <table class="table-modern mb-0" style="min-width: 1050px;">
            <thead>
                <tr>
                    <th style="width: 90px; min-width: 90px;">Ảnh</th>
                    <th style="min-width: 300px;">Thông tin</th>
                    <th style="min-width: 160px;">Người đăng</th>
                    <th style="min-width: 130px;">Danh mục</th>
                    <th style="min-width: 130px;">Khu vực</th>
                    <th style="min-width: 180px;">Ghi chú</th>
                    <th class="text-end" style="width: 140px; min-width: 140px;">Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty items}">
                        <c:forEach items="${items}" var="item">
                            <tr>

                                <td>
                                    <div class="position-relative d-inline-block rounded-2 overflow-hidden shadow-xs border" style="width: 70px; height: 70px; background: #f1f5f9;">

                                        <div class="w-100 h-100 d-flex align-items-center justify-content-center text-muted position-absolute top-0 start-0">
                                            <i class="bi bi-image fs-4"></i>
                                        </div>

                                        <img src="${item.image.startsWith('http') ? item.image : pageContext.request.contextPath.concat('/assets/uploads/items/').concat(item.image)}"
                                             alt="${item.title}" class="position-relative w-100 h-100" style="object-fit: cover; z-index: 1;"
                                             onerror="this.style.display='none';">

                                        <div class="position-absolute bottom-0 start-0 end-0 p-1 text-center" style="z-index: 2; background: rgba(15, 23, 42, 0.75); backdrop-filter: blur(2px);">
                                            <c:choose>
                                                <c:when test="${item.status == 1}">
                                                    <span class="badge" style="background: #DC2626; color: #fff; font-size: 0.62rem; padding: 2px 4px; font-weight: 600; display: inline-block; line-height: 1;">
                                                        Đang tìm
                                                    </span>
                                                </c:when>
                                                <c:when test="${item.status == 2}">
                                                    <span class="badge" style="background: #D97706; color: #fff; font-size: 0.62rem; padding: 2px 4px; font-weight: 600; display: inline-block; line-height: 1;">
                                                        Đang giữ
                                                    </span>
                                                </c:when>
                                                <c:when test="${item.status == 0}">
                                                    <span class="badge" style="background: #059669; color: #fff; font-size: 0.62rem; padding: 2px 4px; font-weight: 600; display: inline-block; line-height: 1;">
                                                        Đã trả
                                                    </span>
                                                </c:when>
                                            </c:choose>
                                        </div>
                                    </div>
                                </td>

                                <td>
                                    <div class="fw-bold text-dark mb-1" style="font-size: 0.95rem;">
                                        ${item.title}
                                    </div>
                                    <c:if test="${not empty item.description}">
                                        <div class="text-muted small mb-1" style="line-height: 1.4; max-width: 320px; white-space: normal;">
                                            ${item.description}
                                        </div>
                                    </c:if>
                                    <div class="text-muted small" style="font-size: 0.78rem;">
                                        <i class="bi bi-clock me-1"></i><fmt:formatDate value="${item.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                                    </div>
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${item.studentPost}">
                                            <div class="small fw-semibold text-dark">${item.authorName}</div>
                                            <div class="text-muted small">${item.authorCode}</div>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="small text-muted">—</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td><span class="badge bg-light text-dark fw-normal border">${item.categoryName}</span></td>
                                <td><span class="small text-muted"><i class="bi bi-geo-alt text-danger me-1"></i>${item.locationName}</span></td>

                                <td>
                                    <c:choose>
                                        <c:when test="${item.status == 1}">
                                            <span class="small text-muted">—</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="small text-muted">${not empty item.adminNote ? item.adminNote : '—'}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td class="text-end">
                                    <div class="d-inline-flex align-items-center justify-content-end gap-1">

                                        <c:if test="${item.status == 1}">
                                            <button type="button" class="btn btn-outline-warning btn-icon-action"
                                                    data-bs-toggle="modal" data-bs-target="#holdModal${item.id}">
                                                <i class="bi bi-box-arrow-in-down"></i>
                                            </button>
                                        </c:if>

                                        <c:if test="${item.status != 0}">
                                            <form action="${pageContext.request.contextPath}/admin/items" method="POST" onsubmit="return confirm('Xác nhận đồ vật đã được trao trả xong?');" class="d-inline m-0 p-0">
                                                <input type="hidden" name="action" value="close">
                                                <input type="hidden" name="id" value="${item.id}">
                                                <button type="submit" class="btn btn-outline-success btn-icon-action">
                                                    <i class="bi bi-check2"></i>
                                                </button>
                                            </form>
                                        </c:if>

                                        <button type="button" class="btn btn-outline-primary btn-icon-action"
                                                data-bs-toggle="modal" data-bs-target="#editModal${item.id}">
                                            <i class="bi bi-pencil"></i>
                                        </button>

                                        <form action="${pageContext.request.contextPath}/admin/items" method="POST" onsubmit="return confirm('Xác nhận xóa đồ thất lạc này?');" class="d-inline m-0 p-0">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${item.id}">
                                            <button type="submit" class="btn btn-outline-danger btn-icon-action">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="7" class="text-center py-5 text-muted">
                                <i class="bi bi-inbox fs-2 d-block mb-2"></i>
                                Không tìm thấy dữ liệu đồ thất lạc.
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<c:if test="${not empty items}">
    <c:forEach items="${items}" var="item">
        <c:if test="${item.status == 1}">
            <div class="modal fade modal-husc text-start" id="holdModal${item.id}" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title"><i class="bi bi-box-arrow-in-down text-warning"></i> Tiếp nhận đồ</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <form action="${pageContext.request.contextPath}/admin/items" method="POST">
                            <input type="hidden" name="action" value="hold_in_cabinet">
                            <input type="hidden" name="id" value="${item.id}">
                            <div class="modal-body">
                                <p class="small text-muted mb-3">Đồ vật: <strong class="text-dark">${item.title}</strong> (Đăng bởi: ${item.authorName})</p>
                                <label class="form-label">Ghi chú <span class="text-danger">*</span></label>
                                <input type="text" class="form-control form-control-custom" name="admin_note"
                                       placeholder="Ví dụ: Phòng trực..." value="Phòng trực" required>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-husc-outline btn-sm" data-bs-dismiss="modal">Hủy</button>
                                <button type="submit" class="btn btn-warning btn-sm fw-semibold">Xác nhận</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </c:if>

        <div class="modal fade modal-husc text-start" id="editModal${item.id}" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title"><i class="bi bi-pencil text-primary"></i> Sửa thông tin đồ</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="${pageContext.request.contextPath}/admin/items" method="POST" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="update_item">
                        <input type="hidden" name="id" value="${item.id}">
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
                            <c:choose>
                                <c:when test="${item.status != 1}">
                                    <div class="mb-3">
                                        <label class="form-label">Ghi chú</label>
                                        <input type="text" class="form-control form-control-custom" name="admin_note" value="${not empty item.adminNote ? item.adminNote : 'Phòng trực'}">
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="admin_note" value="${item.adminNote}">
                                </c:otherwise>
                            </c:choose>
                            <div class="mb-2">
                                <label class="form-label">Ảnh mới</label>
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
    </c:forEach>
</c:if>

<div class="modal fade modal-husc" id="addFoundModal" tabindex="-1" aria-labelledby="addFoundModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addFoundModalLabel">
                    <i class="bi bi-plus-circle text-primary"></i> Tiếp nhận đồ mới
                </h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="${pageContext.request.contextPath}/admin/items" method="POST" enctype="multipart/form-data">
                <input type="hidden" name="action" value="create_found">
                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Tên đồ vật <span class="text-danger">*</span></label>
                        <input type="text" class="form-control form-control-custom" name="title" placeholder="Ví dụ: Thẻ sinh viên, Ví tiền..." required>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label">Danh mục <span class="text-danger">*</span></label>
                            <select class="form-select form-select-custom" name="category_id" required>
                                <option value="" disabled selected>Chọn danh mục</option>
                                <c:forEach items="${categories}" var="cat">
                                    <option value="${cat.id}">${cat.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Khu vực <span class="text-danger">*</span></label>
                            <select class="form-select form-select-custom" name="location_id" required>
                                <option value="" disabled selected>Chọn khu vực</option>
                                <c:forEach items="${locations}" var="loc">
                                    <option value="${loc.id}">${loc.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Ghi chú <span class="text-danger">*</span></label>
                        <input type="text" class="form-control form-control-custom" name="admin_note" value="Phòng trực" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Mô tả</label>
                        <textarea class="form-control form-control-custom" name="description" rows="3" placeholder="Mô tả màu sắc, đặc điểm nhận dạng..."></textarea>
                    </div>

                    <div class="mb-2">
                        <label class="form-label">Ảnh <span class="text-danger">*</span></label>
                        <div class="image-dropzone py-3">
                            <div class="dropzone-prompt text-center">
                                <i class="bi bi-camera fs-2 text-muted d-block mb-1"></i>
                                <div class="small fw-semibold text-dark">Bấm hoặc kéo thả ảnh vào đây</div>
                            </div>
                            <input type="file" name="image" accept="image/*" class="d-none input-image-preview" required>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-husc-outline btn-sm" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-husc-primary btn-sm">
                        <i class="bi bi-check2"></i> Xác nhận
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/inc/admin_footer.jsp" />
