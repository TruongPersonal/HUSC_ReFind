<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Khu vực — Quản trị HUSC ReFind" scope="request" />
<c:set var="activeAdminNav" value="locations" scope="request" />
<jsp:include page="/inc/admin_header.jsp" />

<div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
    <div>
        <h4 class="fw-bold mb-1 text-dark">Quản lý Khu vực</h4>
        <div class="small text-muted">Cấu hình các vị trí, tòa nhà, giảng đường trong trường</div>
    </div>
    <button type="button" class="btn btn-husc-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addLocationModal">
        <i class="bi bi-plus-lg me-1"></i> Thêm khu vực
    </button>
</div>

<div class="card border rounded-3 bg-white overflow-hidden shadow-xs w-100">
    <div class="table-responsive">
        <table class="table-modern mb-0 w-100">
            <thead>
                <tr>
                    <th class="ps-4">Tên khu vực</th>
                    <th class="text-end pe-4" style="width: 140px;">Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty locations}">
                        <c:forEach items="${locations}" var="loc">
                            <tr>
                                <td class="ps-4 fw-semibold text-dark">
                                    <i class="bi bi-geo-alt-fill text-danger me-2"></i> ${loc.name}
                                </td>
                                <td class="text-end pe-4">
                                    <div class="d-inline-flex align-items-center justify-content-end gap-1">
                                        <button type="button" class="btn btn-outline-primary btn-icon-action" data-bs-toggle="modal" data-bs-target="#editLocModal${loc.id}">
                                            <i class="bi bi-pencil"></i>
                                        </button>
                                        <form action="${pageContext.request.contextPath}/admin/locations" method="POST" onsubmit="return confirm('Bạn có chắc muốn xóa khu vực này?');" class="d-inline m-0 p-0">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${loc.id}">
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
                            <td colspan="2" class="text-center py-4 text-muted">Chưa có khu vực nào.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<c:if test="${not empty locations}">
    <c:forEach items="${locations}" var="loc">
        <div class="modal fade modal-husc text-start" id="editLocModal${loc.id}" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title"><i class="bi bi-pencil text-primary"></i> Sửa khu vực</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="${pageContext.request.contextPath}/admin/locations" method="POST">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="${loc.id}">
                        <div class="modal-body">
                            <label class="form-label">Tên khu vực <span class="text-danger">*</span></label>
                            <input type="text" class="form-control form-control-custom" name="name" value="${loc.name}" required>
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

<div class="modal fade modal-husc" id="addLocationModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><i class="bi bi-plus-circle text-primary"></i> Thêm khu vực mới</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="${pageContext.request.contextPath}/admin/locations" method="POST">
                <input type="hidden" name="action" value="add">
                <div class="modal-body">
                    <label class="form-label">Tên khu vực <span class="text-danger">*</span></label>
                    <input type="text" class="form-control form-control-custom" name="name" placeholder="Ví dụ: Giảng đường Dãy D, Sân bóng, Thư viện..." required autofocus>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-husc-outline btn-sm" data-bs-dismiss="modal">Hủy</button>
                    <button type="submit" class="btn btn-husc-primary btn-sm"><i class="bi bi-check2"></i> Xác nhận</button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="/inc/admin_footer.jsp" />
