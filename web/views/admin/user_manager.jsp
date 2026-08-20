<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="pageTitle" value="Tài khoản — Quản trị HUSC ReFind" scope="request" />
<c:set var="activeAdminNav" value="users" scope="request" />
<jsp:include page="/inc/admin_header.jsp" />

<div class="d-flex justify-content-between align-items-center mb-4 flex-wrap gap-3">
    <div>
        <h4 class="fw-bold mb-1 text-dark">Quản lý Tài khoản</h4>
        <div class="small text-muted">Kiểm soát danh sách tài khoản sinh viên và quản trị viên</div>
    </div>
    <button type="button" class="btn btn-husc-primary btn-sm" data-bs-toggle="modal" data-bs-target="#addUserModal">
        <i class="bi bi-person-plus-fill me-1"></i> Thêm người dùng
    </button>
</div>

<div class="card border rounded-3 p-3 bg-white mb-4 shadow-xs">
    <div class="row g-3 align-items-center justify-content-between">

        <div class="col-lg-7">
            <div class="d-flex align-items-center gap-2 flex-wrap">
                <span class="small fw-semibold text-muted me-1">Vai trò:</span>
                <a href="${pageContext.request.contextPath}/admin/users?role=ALL<c:if test='${not empty keyword}'>&keyword=<c:out value='${keyword}'/></c:if>"
                   class="btn btn-sm ${selectedRole == 'ALL' ? 'btn-dark' : 'btn-light border'}">
                    Tất cả (${users.size()})
                </a>
                <a href="${pageContext.request.contextPath}/admin/users?role=student<c:if test='${not empty keyword}'>&keyword=<c:out value='${keyword}'/></c:if>"
                   class="btn btn-sm ${selectedRole == 'student' ? 'btn-dark' : 'btn-light border'}">
                    <i class="bi bi-person me-1"></i> Sinh viên
                </a>
                <a href="${pageContext.request.contextPath}/admin/users?role=admin<c:if test='${not empty keyword}'>&keyword=<c:out value='${keyword}'/></c:if>"
                   class="btn btn-sm ${selectedRole == 'admin' ? 'btn-dark' : 'btn-light border'}">
                    <i class="bi bi-shield-check me-1"></i> Quản trị viên
                </a>
            </div>
        </div>

        <div class="col-lg-5">
            <form action="${pageContext.request.contextPath}/admin/users" method="GET" class="d-flex gap-2">
                <input type="hidden" name="role" value="${selectedRole}">
                <input type="text" name="keyword" class="form-control form-control-custom"
                       placeholder="Tìm theo tên, mã, sđt, email" value="<c:out value='${keyword}'/>">
                <button type="submit" class="btn btn-husc-primary btn-sm px-3 flex-shrink-0">Tìm</button>
                <c:if test="${not empty keyword || selectedRole != 'ALL'}">
                    <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-husc-outline btn-sm px-2 flex-shrink-0">
                        <i class="bi bi-x-lg"></i>
                    </a>
                </c:if>
            </form>
        </div>
    </div>
</div>

<div class="card border rounded-3 bg-white overflow-hidden shadow-xs w-100">
    <div class="table-responsive">
        <table class="table-modern mb-0 w-100" style="min-width: 900px;">
            <thead>
                <tr>
                    <th class="ps-4" style="min-width: 220px;">Họ tên</th>
                    <th style="min-width: 170px;">Mã người dùng</th>
                    <th style="min-width: 200px;">Email</th>
                    <th style="min-width: 150px;">SĐT</th>
                    <th class="text-end pe-4" style="width: 120px; min-width: 120px;">Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty users}">
                        <c:forEach items="${users}" var="u">
                            <tr>
                                <td class="ps-4">
                                    <div class="d-flex align-items-center gap-3">

                                        <div class="user-avatar-card position-relative rounded-2 overflow-hidden flex-shrink-0 d-flex flex-column align-items-center justify-content-center border"
                                             style="width: 74px; height: 56px; background: ${u.admin ? 'linear-gradient(135deg, #002B7F 0%, #2563EB 100%)' : 'linear-gradient(135deg, #334155 0%, #475569 100%)'}; color: #ffffff;">
                                            <div class="d-flex align-items-center justify-content-center" style="margin-top: -10px;">
                                                <c:choose>
                                                    <c:when test="${u.admin}">
                                                        <i class="bi bi-shield-lock-fill" style="font-size: 1.25rem; text-shadow: 0 1px 2px rgba(0,0,0,0.25);"></i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="bi bi-person-fill" style="font-size: 1.35rem; text-shadow: 0 1px 2px rgba(0,0,0,0.25);"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="w-100 text-center position-absolute bottom-0 start-0 end-0 py-1"
                                                 style="background: rgba(15, 23, 42, 0.75); backdrop-filter: blur(2px);">
                                                <span class="text-white fw-semibold" style="font-size: 0.56rem; letter-spacing: -0.2px; line-height: 1; display: block;">
                                                    ${u.admin ? 'Quản trị viên' : 'Sinh viên'}
                                                </span>
                                            </div>
                                        </div>

                                        <div>
                                            <div class="fw-semibold text-dark">${u.name}</div>
                                            <div class="d-flex align-items-center gap-1 mt-1 flex-wrap">
                                                <c:if test="${sessionScope.user.id == u.id}">
                                                    <span class="badge bg-dark text-white" style="font-size: 0.65rem;">Bạn</span>
                                                </c:if>
                                                <c:choose>
                                                    <c:when test="${u.status == 1}">
                                                        <span class="badge bg-success-subtle text-success border border-success-subtle" style="font-size: 0.65rem;">Hoạt động</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-danger-subtle text-danger border border-danger-subtle" style="font-size: 0.65rem;">Đã khóa</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </td>
                                <td>
                                    <span class="fw-medium text-dark">${u.code}</span>
                                </td>
                                <td>
                                    <span class="small text-muted">${not empty u.email ? u.email : '—'}</span>
                                </td>
                                <td>
                                    <span class="small text-muted">${not empty u.phone ? u.phone : '—'}</span>
                                </td>
                                <td class="text-end pe-4">
                                    <div class="d-inline-flex align-items-center justify-content-end gap-1">

                                        <button type="button" class="btn btn-outline-primary btn-icon-action"
                                                data-bs-toggle="modal" data-bs-target="#editUserModal${u.id}">
                                            <i class="bi bi-pencil"></i>
                                        </button>

                                        <c:choose>
                                            <c:when test="${u.admin}">
                                                <c:if test="${sessionScope.user.id != u.id}">
                                                    <form action="${pageContext.request.contextPath}/admin/users" method="POST" onsubmit="return confirm('Bạn có chắc chắn muốn XÓA vĩnh viễn tài khoản quản trị [${u.name}]?');" class="d-inline m-0 p-0">
                                                        <input type="hidden" name="action" value="delete">
                                                        <input type="hidden" name="id" value="${u.id}">
                                                        <button type="submit" class="btn btn-outline-danger btn-icon-action">
                                                            <i class="bi bi-trash"></i>
                                                        </button>
                                                    </form>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <c:choose>
                                                    <c:when test="${u.status == 1}">
                                                        <form action="${pageContext.request.contextPath}/admin/users" method="POST" onsubmit="return confirm('Bạn có chắc chắn muốn KHÓA tài khoản của sinh viên [${u.name}]?');" class="d-inline m-0 p-0">
                                                            <input type="hidden" name="action" value="toggle_status">
                                                            <input type="hidden" name="id" value="${u.id}">
                                                            <input type="hidden" name="status" value="0">
                                                            <button type="submit" class="btn btn-outline-warning btn-icon-action">
                                                                <i class="bi bi-lock-fill"></i>
                                                            </button>
                                                        </form>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <form action="${pageContext.request.contextPath}/admin/users" method="POST" onsubmit="return confirm('Xác nhận MỞ KHÓA cho tài khoản sinh viên [${u.name}]?');" class="d-inline m-0 p-0">
                                                            <input type="hidden" name="action" value="toggle_status">
                                                            <input type="hidden" name="id" value="${u.id}">
                                                            <input type="hidden" name="status" value="1">
                                                            <button type="submit" class="btn btn-outline-success btn-icon-action">
                                                                <i class="bi bi-unlock-fill"></i>
                                                            </button>
                                                        </form>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="5" class="text-center py-5 text-muted">
                                <i class="bi bi-people fs-2 d-block mb-2"></i>
                                Không tìm thấy người dùng nào phù hợp.
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<c:if test="${not empty users}">
    <c:forEach items="${users}" var="u">
        <div class="modal fade modal-husc text-start" id="editUserModal${u.id}" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title"><i class="bi bi-person-gear text-primary"></i> Sửa người dùng</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <form action="${pageContext.request.contextPath}/admin/users" method="POST">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="${u.id}">

                        <div class="modal-body">
                            <div class="mb-3">
                                <label class="form-label">Họ tên <span class="text-danger">*</span></label>
                                <input type="text" class="form-control form-control-custom" name="name" value="${u.name}" required>
                            </div>

                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">Mã người dùng <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control form-control-custom" name="code" value="${u.code}" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Vai trò <span class="text-danger">*</span></label>
                                    <select class="form-select form-select-custom" name="role" required>
                                        <option value="student" ${u.role == 'student' ? 'selected' : ''}>Sinh viên</option>
                                        <option value="admin" ${u.role == 'admin' ? 'selected' : ''}>Quản trị viên</option>
                                    </select>
                                </div>
                            </div>

                            <div class="row g-3 mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">Email</label>
                                    <input type="email" class="form-control form-control-custom" name="email" value="${u.email}">
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">SĐT</label>
                                    <input type="tel" class="form-control form-control-custom" name="phone" value="${u.phone}">
                                </div>
                            </div>

                            <div class="mb-2">
                                <label class="form-label">Mật khẩu mới</label>
                                <input type="password" class="form-control form-control-custom" name="password" placeholder="Để trống nếu không muốn đổi mật khẩu">
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
    </c:forEach>
</c:if>

<div class="modal fade modal-husc text-start" id="addUserModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><i class="bi bi-person-plus-fill text-primary"></i> Thêm người dùng mới</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>

            <form action="${pageContext.request.contextPath}/admin/users" method="POST">
                <input type="hidden" name="action" value="add">

                <div class="modal-body">
                    <div class="mb-3">
                        <label class="form-label">Họ tên <span class="text-danger">*</span></label>
                        <input type="text" class="form-control form-control-custom" name="name" placeholder="Ví dụ: Nguyễn Văn An" required>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label">Mã người dùng <span class="text-danger">*</span></label>
                            <input type="text" class="form-control form-control-custom" name="code" placeholder="Ví dụ: 23t1020000" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Vai trò <span class="text-danger">*</span></label>
                            <select class="form-select form-select-custom" name="role" required>
                                <option value="student" selected>Sinh viên</option>
                                <option value="admin">Quản trị viên</option>
                            </select>
                        </div>
                    </div>

                    <div class="row g-3 mb-3">
                        <div class="col-md-6">
                            <label class="form-label">Email</label>
                            <input type="email" class="form-control form-control-custom" name="email" placeholder="email@husc.edu.vn">
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">SĐT</label>
                            <input type="tel" class="form-control form-control-custom" name="phone" placeholder="09xxxxxxxxx">
                        </div>
                    </div>

                    <div class="mb-2">
                        <label class="form-label">Mật khẩu khởi tạo <span class="text-danger">*</span></label>
                        <input type="password" class="form-control form-control-custom" name="password" placeholder="Tối thiểu 6 ký tự" required>
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
