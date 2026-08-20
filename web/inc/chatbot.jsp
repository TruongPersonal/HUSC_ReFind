<%@page contentType="text/html" pageEncoding="UTF-8"%>

<div id="husc-chatbot-container" data-context-path="${pageContext.request.contextPath}" data-csrf-token="${sessionScope.csrfToken}">
    <button id="husc-chatbot-toggle" type="button" class="btn shadow-lg" aria-label="Mở Trợ lý ảo HUSC ReFind">
        <div class="chatbot-toggle-icon">
            <i class="bi bi-robot"></i>
        </div>
        <span class="chatbot-online-pulse"></span>
    </button>

    <div id="husc-chatbot-box" class="card shadow-lg border-0" style="display: none;">
        <div class="card-header text-white d-flex align-items-center justify-content-between p-3"
             style="background: linear-gradient(135deg, #1e3a8a 0%, #2563eb 100%); border-radius: 16px 16px 0 0;">
            <div class="d-flex align-items-center gap-2">
                <div class="position-relative">
                    <div class="rounded-circle bg-white text-primary d-flex align-items-center justify-content-center"
                         style="width: 38px; height: 38px; font-size: 1.25rem; box-shadow: 0 2px 6px rgba(0,0,0,0.15);">
                        <i class="bi bi-robot"></i>
                    </div>
                    <span class="position-absolute bottom-0 end-0 bg-success border border-white rounded-circle"
                          style="width: 10px; height: 10px;"></span>
                </div>
                <div>
                    <div class="fw-bold mb-0 text-white" style="font-size: 0.95rem; line-height: 1.2;">Trợ lý ảo</div>
                    <div class="text-white-50" style="font-size: 0.75rem;">
                        <i class="bi bi-stars text-warning me-1"></i>HUSC ReFind 24/7
                    </div>
                </div>
            </div>
            <div class="d-flex align-items-center gap-1">
                <button type="button" class="btn btn-sm btn-link text-white text-decoration-none p-1" id="husc-chatbot-clear" title="Xóa đoạn chat">
                    <i class="bi bi-arrow-counterclockwise fs-6"></i>
                </button>
                <button type="button" class="btn btn-sm btn-link text-white text-decoration-none p-1" id="husc-chatbot-close" title="Đóng">
                    <i class="bi bi-x-lg fs-6"></i>
                </button>
            </div>
        </div>

        <div class="card-body p-3 overflow-y-auto" id="husc-chatbot-messages" style="height: 380px; background-color: #f8fafc;">
            <div class="d-flex gap-2 mb-3">
                <div class="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center flex-shrink-0"
                     style="width: 30px; height: 30px; font-size: 0.85rem;">
                    <i class="bi bi-robot"></i>
                </div>
                <div class="p-3 rounded-3 bg-white border shadow-xs text-dark" style="max-width: 85%; font-size: 0.88rem; line-height: 1.5; border-radius: 4px 16px 16px 16px !important;">
                    <p class="mb-1 fw-bold text-primary"><i class="bi bi-stars"></i> Chào bạn!</p>
                    <p class="mb-2">Mình là <strong>Trợ lý ảo HUSC ReFind</strong>. Mình có thể hỗ trợ bạn:</p>
                    <ul class="mb-2 ps-3 small text-muted">
                        <li>Tra cứu đồ thất lạc đang tìm / đang giữ tại trường.</li>
                        <li>Quy trình nhận lại đồ tại Phòng Bảo vệ (77 Nguyễn Huệ).</li>
                        <li>Cảnh báo an toàn, chống lừa đảo chuộc đồ.</li>
                    </ul>
                    <div class="fw-semibold small text-secondary mb-1">Gợi ý câu hỏi:</div>
                    <div class="d-flex flex-wrap gap-1 mt-1">
                        <button type="button" class="btn btn-xs btn-outline-primary chatbot-quick-prompt" style="font-size: 0.76rem; border-radius: 12px;">
                            🔍 Có ai nhặt được đồ gì ngày hôm nay không?
                        </button>
                        <button type="button" class="btn btn-xs btn-outline-primary chatbot-quick-prompt" style="font-size: 0.76rem; border-radius: 12px;">
                            🏢 Phòng bảo vệ đang giữ những gì?
                        </button>
                        <button type="button" class="btn btn-xs btn-outline-primary chatbot-quick-prompt" style="font-size: 0.76rem; border-radius: 12px;">
                            ❓ Bị mất đồ thì phải làm sao?
                        </button>
                        <button type="button" class="btn btn-xs btn-outline-danger chatbot-quick-prompt" style="font-size: 0.76rem; border-radius: 12px;">
                            🚨 Có người đòi tiền chuộc đồ thì làm gì?
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <div id="husc-chatbot-typing" class="px-3 py-1 bg-light border-top" style="display: none;">
            <div class="d-flex align-items-center gap-2 text-muted small">
                <div class="spinner-grow spinner-grow-sm text-primary" style="width: 0.6rem; height: 0.6rem;" role="status"></div>
                <span style="font-size: 0.8rem;">Trợ lý AI đang suy nghĩ...</span>
            </div>
        </div>

        <div class="card-footer bg-white p-2 border-top" style="border-radius: 0 0 16px 16px;">
            <form id="husc-chatbot-form" class="d-flex align-items-center gap-2">
                <input type="text" id="husc-chatbot-input" class="form-control form-control-sm border-0 bg-light px-3 py-2"
                       placeholder="Hỏi về hệ thống, đồ thất lạc..." autocomplete="off" style="border-radius: 20px; font-size: 0.88rem;">
                <button type="submit" id="husc-chatbot-send" class="btn btn-primary btn-sm rounded-circle d-flex align-items-center justify-content-center flex-shrink-0"
                        style="width: 36px; height: 36px;" aria-label="Gửi tin nhắn">
                    <i class="bi bi-send-fill" style="font-size: 0.85rem; margin-left: 2px;"></i>
                </button>
            </form>
        </div>
    </div>
</div>
