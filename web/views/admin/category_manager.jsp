<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="pageTitle" value="Danh mục — Quản trị HUSC ReFind" scope="request" />
<c:set var="activeAdminNav" value="categories" scope="request" />
<jsp:include page="/inc/admin_header.jsp" />

<div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
    <div>
        <h4 class="fw-bold mb-1 text-dark">Quản lý Danh mục</h4>
        <div class="small text-muted">Cấu hình các phân loại đồ thất lạc trên toàn hệ thống</div>
    </div>
    <button type="button" class="btn btn-husc-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addCategoryModal">
        <i class="bi bi-plus-lg me-1"></i> Thêm danh mục
    </button>
</div>

<div class="card border rounded-3 bg-white overflow-hidden shadow-xs w-100">
    <div class="table-responsive">
        <table class="table-modern mb-0 w-100">
            <thead>
                <tr>
                    <th class="ps-4">Tên danh mục</th>
                    <th class="text-end pe-4" style="width: 140px;">Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty categories}">
                        <c:forEach items="${categories}" var="cat">
                            <tr>
                                <td class="ps-4 fw-semibold text-dark">
                                    <i class="bi bi-tag-fill text-primary me-2"></i> ${cat.name}
                                </td>
                                <td class="text-end pe-4">
                                    <div class="d-inline-flex align-items-center justify-content-end gap-1">
                                        <button type="button" class="btn btn-outline-primary btn-icon-action" data-bs-toggle="modal" data-bs-target="#editCatModal${cat.id}">
                                            <i class="bi bi-pencil"></i>
                                        </button>
                                        <form action="${pageContext.request.contextPath}/admin/categories" method="POST" onsubmit="return confirm('Bạn có chắc muốn xóa danh mục này?');" class="d-inline m-0 p-0">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="${cat.id}">
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
                            <td colspan="2" class="text-center py-4 text-muted">Chưa có danh mục nào.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<c:if test="${not empty categories}">
    <c:forEach items="${categories}" var="cat">
        <div class="modal fade modal-husc text-start" id="editCatModal${cat.id}" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title"><i class="bi bi-pencil text-primary"></i> Sửa danh mục</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <form action="${pageContext.request.contextPath}/admin/categories" method="POST">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="${cat.id}">
                        <div class="modal-body">
                            <label class="form-label">Tên danh mục <span class="text-danger">*</span></label>
                            <input type="text" class="form-control form-control-custom" name="name" value="${cat.name}" required>
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

<div class="modal fade modal-husc" id="addCategoryModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><i class="bi bi-plus-circle text-primary"></i> Thêm danh mục mới</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="${pageContext.request.contextPath}/admin/categories" method="POST">
                <input type="hidden" name="action" value="add">
                <div class="modal-body">
                    <label class="form-label">Tên danh mục <span class="text-danger">*</span></label>
                    <input type="text" class="form-control form-control-custom" name="name" placeholder="Ví dụ: Giấy tờ & Thẻ, Thiết bị điện tử..." required autofocus>
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
