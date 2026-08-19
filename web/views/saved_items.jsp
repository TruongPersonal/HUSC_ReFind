<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="pageTitle" value="Tin đã lưu — HUSC ReFind" scope="request" />
<jsp:include page="/inc/header.jsp" />

<div class="container px-3 px-lg-4 py-4">

    <div class="mb-4">
        <h2 class="fw-bold mb-1">Tin đã lưu</h2>
        <p class="text-muted small mb-0">Danh sách các đồ thất lạc bạn đã đánh dấu theo dõi</p>
    </div>

    <c:choose>
        <c:when test="${not empty items}">
            <div class="row g-3 g-lg-4">
                <c:forEach items="${items}" var="item">
                    <div class="col-xl-3 col-lg-4 col-md-6 col-sm-6 saved-item-card-wrapper">
                        <c:set var="item" value="${item}" scope="request" />
                        <c:set var="showBookmark" value="${true}" scope="request" />
                        <c:set var="showTimestamp" value="${false}" scope="request" />
                        <jsp:include page="/inc/item_card.jsp" />
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <div class="text-center py-5 border rounded-3 bg-light my-3">
                <i class="bi bi-heart fs-1 text-muted d-block mb-2"></i>
                <h5 class="fw-bold mb-0">Chưa có tin nào được lưu</h5>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/inc/footer.jsp" />
