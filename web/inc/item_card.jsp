<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<div class="item-card">
    <div class="item-card-img-wrapper">
        <c:choose>
            <c:when test="${not empty item.image}">
                <img src="${pageContext.request.contextPath}/assets/uploads/items/${item.image}"
                     alt="${item.title}" class="item-card-img" loading="lazy"
                     onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                <div class="img-placeholder-svg" style="display: none;">
                    <i class="bi bi-image"></i>
                </div>
            </c:when>
            <c:otherwise>
                <div class="img-placeholder-svg">
                    <i class="bi bi-image"></i>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="position-absolute" style="top: 8px; left: 8px; z-index: 5;">
            <c:choose>
                <c:when test="${item.status == 1}"><span class="badge-status badge-lost"><i class="bi bi-search"></i> Đang tìm</span></c:when>
                <c:when test="${item.status == 2}"><span class="badge-status badge-holding"><i class="bi bi-shield-check"></i> Đang giữ</span></c:when>
                <c:when test="${item.status == 0}"><span class="badge-status badge-returned"><i class="bi bi-check2-circle"></i> Đã trả</span></c:when>
            </c:choose>
        </div>

        <c:if test="${showBookmark}">
            <button type="button"
                class="btn-bookmark btn-bookmark-ajax ${savedItemIds.contains(item.id) ? 'active' : ''}"
                data-item-id="${item.id}"
                data-context-path="${pageContext.request.contextPath}"
                aria-label="Lưu tin">
                <i class="bi ${savedItemIds.contains(item.id) ? 'bi-heart-fill text-danger' : 'bi-heart'}"></i>
            </button>
        </c:if>
    </div>

    <div class="item-card-body">
        <a href="${pageContext.request.contextPath}/item-detail?id=${item.id}"
            class="item-card-title text-decoration-none">
            ${item.title}
        </a>

        <div class="item-card-meta">
            <div class="d-flex align-items-center gap-1">
                <span class="badge bg-light text-dark fw-normal border">${item.categoryName}</span>
            </div>
            <div class="d-flex align-items-center gap-1 text-muted small mt-1">
                <i class="bi bi-geo-alt text-danger"></i>
                <span class="text-truncate">${item.locationName}</span>
            </div>
            <c:if test="${not empty item.adminNote}">
                <div class="d-flex align-items-center gap-1 small mt-1 text-warning fw-semibold">
                    <i class="bi bi-info-circle"></i>
                    <span class="text-truncate">${item.adminNote}</span>
                </div>
            </c:if>
            <c:if test="${showTimestamp}">
                <div class="text-muted small mt-1" style="font-size: 0.78rem;">
                    <i class="bi bi-clock me-1"></i><fmt:formatDate value="${item.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                </div>
            </c:if>
        </div>
    </div>
</div>
