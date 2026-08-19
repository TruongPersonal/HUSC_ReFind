<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib prefix="c" uri="jakarta.tags.core" %>
        <%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
            <c:set var="pageTitle" value="Bảng tin — HUSC ReFind" scope="request" />
            <c:set var="activeNav" value="home" scope="request" />
            <jsp:include page="/inc/header.jsp" />

            <div class="container px-3 px-lg-4 py-4">

                <div class="d-flex justify-content-between align-items-center flex-wrap gap-3 mb-4">
                    <div>
                        <h2 class="fw-bold mb-1">Bảng tin hệ thống</h2>
                        <p class="text-muted small mb-0">Tra cứu danh sách đồ báo mất và đồ đang được giữ tại Phòng trực
                        </p>
                    </div>
                    <c:if test="${not empty sessionScope.user}">
                        <button type="button" class="btn btn-husc-primary btn-sm" data-bs-toggle="modal"
                            data-bs-target="#quickPostModal">
                            <i class="bi bi-plus-circle me-1"></i> Báo mất đồ
                        </button>
                    </c:if>
                    <c:if test="${empty sessionScope.user}">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-husc-primary btn-sm">
                            <i class="bi bi-plus-circle me-1"></i> Báo mất đồ
                        </a>
                    </c:if>
                </div>

                <div class="mb-4">
                    <div class="d-flex flex-wrap align-items-center justify-content-between gap-3 mb-3">

                        <div class="feed-tabs-nav">
                            <a href="${pageContext.request.contextPath}/home?tab=LOST<c:if test='${selectedCategory > 0}'>&category_id=${selectedCategory}</c:if><c:if test='${selectedLocation > 0}'>&location_id=${selectedLocation}</c:if><c:if test='${not empty keyword}'>&keyword=${keyword}</c:if>"
                                class="feed-tab-btn ${currentTab == 'LOST' ? 'active' : ''}">
                                <i class="bi bi-search"></i>
                                <span>Đang tìm</span>
                                <span class="counter-pill">${lostCount}</span>
                            </a>
                            <a href="${pageContext.request.contextPath}/home?tab=FOUND<c:if test='${selectedCategory > 0}'>&category_id=${selectedCategory}</c:if><c:if test='${selectedLocation > 0}'>&location_id=${selectedLocation}</c:if><c:if test='${not empty keyword}'>&keyword=${keyword}</c:if>"
                                class="feed-tab-btn ${currentTab == 'FOUND' ? 'active' : ''}">
                                <i class="bi bi-shield-check"></i>
                                <span>Đang giữ</span>
                                <span class="counter-pill">${foundCount}</span>
                            </a>
                        </div>

                        <form action="${pageContext.request.contextPath}/home" method="GET"
                            class="d-flex flex-wrap align-items-center gap-2">
                            <input type="hidden" name="tab" value="${currentTab}">
                            <c:if test="${selectedCategory > 0}">
                                <input type="hidden" name="category_id" value="${selectedCategory}">
                            </c:if>

                            <div class="input-group" style="width: auto; min-width: 240px; max-width: 300px;">
                                <input type="text" name="keyword" class="form-control form-control-custom"
                                    placeholder="Tìm theo tiêu đề, mô tả" value="<c:out value='${keyword}'/>">
                                <button class="btn btn-husc-primary px-3" type="submit" aria-label="Tìm kiếm">
                                    <i class="bi bi-search"></i>
                                </button>
                            </div>

                            <select name="location_id" class="form-select form-select-custom"
                                onchange="this.form.submit()" style="width: auto; min-width: 150px;">
                                <option value="0">Tất cả khu vực</option>
                                <c:forEach items="${locations}" var="loc">
                                    <option value="${loc.id}" ${selectedLocation==loc.id ? 'selected' : '' }>${loc.name}
                                    </option>
                                </c:forEach>
                            </select>

                            <c:if test="${not empty keyword || selectedLocation > 0}">
                                <a href="${pageContext.request.contextPath}/home?tab=${currentTab}<c:if test='${selectedCategory > 0}'>&category_id=${selectedCategory}</c:if>"
                                    class="btn btn-outline-secondary d-flex align-items-center" title="Đặt lại tìm kiếm & khu vực">
                                    <i class="bi bi-x-lg"></i>
                                </a>
                            </c:if>
                        </form>
                    </div>

                    <div class="category-chips-scroll pb-1">
                        <a href="${pageContext.request.contextPath}/home?tab=${currentTab}<c:if test='${selectedLocation > 0}'>&location_id=${selectedLocation}</c:if><c:if test='${not empty keyword}'>&keyword=${keyword}</c:if>"
                            class="category-chip-item ${selectedCategory == 0 ? 'active' : ''}">
                            Tất cả danh mục
                        </a>

                        <c:forEach items="${categories}" var="cat">
                            <a href="${pageContext.request.contextPath}/home?tab=${currentTab}&category_id=${cat.id}<c:if test='${selectedLocation > 0}'>&location_id=${selectedLocation}</c:if><c:if test='${not empty keyword}'>&keyword=${keyword}</c:if>"
                                class="category-chip-item ${selectedCategory == cat.id ? 'active' : ''}">
                                ${cat.name}
                            </a>
                        </c:forEach>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${not empty items}">
                        <div class="row g-3 g-lg-4">
                            <c:forEach items="${items}" var="item">
                                <div class="col-xl-3 col-lg-4 col-md-6 col-sm-6">
                                    <c:set var="item" value="${item}" scope="request" />
                                    <c:set var="showBookmark" value="${true}" scope="request" />
                                    <c:set var="showTimestamp" value="${false}" scope="request" />
                                    <jsp:include page="/inc/item_card.jsp" />
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>

                        <div class="text-center py-5 border rounded-3 bg-light my-4">
                            <i class="bi bi-inbox fs-1 text-muted d-block mb-2"></i>
                            <h5 class="fw-bold mb-0">Không tìm thấy đồ thất lạc nào</h5>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <jsp:include page="/inc/footer.jsp" />
