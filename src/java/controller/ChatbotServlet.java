package controller;

import data.dao.Database;
import data.dao.ItemDao;
import data.utils.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Item;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "ChatbotServlet", urlPatterns = {"/api/chatbot", "/chatbot"})
public class ChatbotServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ChatbotServlet.class.getName());
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"status\":\"ok\",\"service\":\"HUSC ReFind AI Assistant\"}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String userMessage = null;

        String contentType = request.getContentType();
        if (contentType != null && contentType.contains("application/json")) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            String body = sb.toString();
            userMessage = extractJsonField(body, "message");
        }

        if (userMessage == null || userMessage.trim().isEmpty()) {
            userMessage = request.getParameter("message");
        }

        if (userMessage == null || userMessage.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"Nội dung tin nhắn không được để trống.\"}");
            return;
        }

        String apiKey = Constants.GEMINI_API_KEY;
        if (apiKey == null || apiKey.trim().isEmpty() || apiKey.contains("AIzaSyDummyKey")) {
            response.getWriter().write("{\"status\":\"success\",\"reply\":\"🤖 Trợ lý ảo HUSC ReFind chưa được cấu hình API Key từ Google AI Studio. Vui lòng cập nhật API Key hoặc liên hệ Quản trị viên.\"}");
            return;
        }

        try {
            String systemInstruction = buildSystemInstruction();
            String reply = callGeminiApi(apiKey, systemInstruction, userMessage.trim());

            if (reply == null || reply.trim().isEmpty()) {
                reply = "Xin lỗi bạn, mình chưa thể xử lý yêu cầu lúc này. Bạn vui lòng thử lại hoặc liên hệ trực tiếp Phòng Bảo vệ (Cổng 77 Nguyễn Huệ - ĐT: 0234 3823 290) nhé!";
            }

            response.getWriter().write("{\"status\":\"success\",\"reply\":\"" + escapeJson(reply) + "\"}");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi xử lý Chatbot: ", e);
            response.getWriter().write("{\"status\":\"error\",\"message\":\"" + escapeJson("Có lỗi xảy ra khi kết nối tới Trợ lý ảo: " + e.getMessage()) + "\"}");
        }
    }

    private String buildSystemInstruction() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm - EEEE, 'ngày' dd/MM/yyyy", new Locale("vi", "VN"));
        String currentTime = sdf.format(new Date());

        StringBuilder sb = new StringBuilder();
        sb.append("Bạn là 'Trợ lý ảo HUSC ReFind' — Trợ lý AI thông minh của Hệ thống hỗ trợ tìm kiếm và quản lý đồ thất lạc tại Trường Đại học Khoa học, Đại học Huế (HUSC).\n");
        sb.append("Địa chỉ cơ sở: 77 Nguyễn Huệ, TP. Huế. Hotline bảo vệ: 0234 3823 290.\n\n");

        sb.append("=== THỜI GIAN HỆ THỐNG HIỆN TẠI ===\n");
        sb.append(currentTime).append(" (Dùng mốc thời gian này để xác định chính xác các câu hỏi về hôm nay, hôm qua, tuần này, tuần trước, tháng này...)\n\n");

        sb.append("=== NGUYÊN TẮC NGHIỆP VỤ CỐT LÕI (BẮT BUỘC TUÂN THỦ) ===\n");
        sb.append("1. SINH VIÊN là NGƯỜI BỊ MẤT ĐỒ: Sinh viên đăng bài lên web là để 'Báo mất đồ' (Trạng thái: Đang tìm - Màu đỏ). Khi sinh viên đã nhận lại được đồ thì vào 'Tin đã đăng' bấm nút 'Đã nhận'.\n");
        sb.append("2. QUẢN TRỊ VIÊN / BẢO VỆ là NGƯỜI ĐANG GIỮ ĐỒ: Người nhặt được đồ sẽ đem đến nộp tại Phòng Bảo vệ (Cổng 77 Nguyễn Huệ). Quản trị viên/Bảo vệ tiếp nhận, lưu vào tủ và đăng tin/cập nhật trạng thái 'Đang giữ' (Màu vàng) kèm ghi chú vị trí tủ đồ. Quản trị viên đối chiếu thẻ SV/CCCD để trao trả cho sinh viên.\n");
        sb.append("3. CÁC TRẠNG THÁI BÀI ĐĂNG:\n");
        sb.append("   - Trạng thái 1: 'Đang tìm' (Sinh viên đăng tìm đồ mất).\n");
        sb.append("   - Trạng thái 2: 'Đang giữ' (Phòng bảo vệ đang cất giữ đồ trong tủ trực).\n");
        sb.append("   - Trạng thái 0: 'Đã nhận / Đã trả' (Đã trao trả hoàn tất cho chủ nhân).\n\n");

        sb.append("=== CẢNH BÁO AN TOÀN & CHỐNG LỪA ĐẢO ===\n");
        sb.append("- Nếu người dùng hỏi về việc có người lạ yêu cầu chuyển tiền cọc, tiền chuộc đồ, phí ship... -> CẢNH BÁO NGAY: Tuyệt đối KHÔNG chuyển tiền! Mọi hoạt động trao trả đồ tại HUSC ReFind qua Phòng bảo vệ là HOÀN TOÀN MIỄN PHÍ. Báo ngay cho Phòng Bảo vệ cổng chính nếu bị tống tiền/lừa đảo.\n\n");

        sb.append("=== QUY TẮC ĐỊNH DẠNG & PHONG CÁCH TRẢ LỜI (BẮT BUỘC) ===\n");
        sb.append("1. TRẢ LỜI NGẮN GỌN, SÚC TÍCH, TẬP TRUNG VÀO Ý CHÍNH: Đi thẳng vào trọng tâm câu trả lời, không chào hỏi rườm rà, không giải thích dài dòng lan man.\n");
        sb.append("2. TUYỆT ĐỐI KHÔNG DÙNG CÁC DẤU TIÊU ĐỀ MARKDOWN ('#', '##', '###', '####'): Không bao giờ xuất ra ký tự dấu thăng # trong câu trả lời.\n");
        sb.append("3. Dùng in đậm '**từ khóa**' cho các thông tin trọng tâm (tên món đồ, mã tin #ID, vị trí tủ đồ, hotline, địa điểm). Dùng gạch đầu dòng ('- ') khi cần liệt kê.\n");
        sb.append("4. Độ dài lý tưởng: Từ 2 đến 4 câu ngắn gọn, trực diện, dễ nhìn trên điện thoại.\n\n");

        sb.append("=== DANH SÁCH DỮ LIỆU ĐỒ THẤT LẠC THỜI GIAN THỰC TRONG DATABASE ===\n");
        try {
            ItemDao itemDao = Database.getItemDao();
            List<Item> items = itemDao.getAllItems(null, null);
            if (items == null || items.isEmpty()) {
                sb.append("(Hiện chưa có bài đăng đồ thất lạc nào trên hệ thống)\n");
            } else {
                SimpleDateFormat itemSdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                int count = 0;
                for (Item item : items) {
                    if (item.getStatus() != 0 || count < 30) {
                        String statusName = item.getStatus() == 1 ? "ĐANG TÌM (Sinh viên báo mất)" :
                                           item.getStatus() == 2 ? "ĐANG GIỮ (Tại Phòng bảo vệ)" : "ĐÃ NHẬN (Đã trao trả xong)";
                        sb.append("- Mã #").append(item.getId())
                          .append(" | Ngày đăng: ").append(item.getCreatedAt() != null ? itemSdf.format(item.getCreatedAt()) : "N/A")
                          .append(" | Tên đồ: ").append(item.getRawTitle())
                          .append(" | Danh mục: ").append(item.getRawCategoryName())
                          .append(" | Vị trí: ").append(item.getRawLocationName())
                          .append(" | Trạng thái: ").append(statusName);
                        if (item.getRawAdminNote() != null && !item.getRawAdminNote().trim().isEmpty()) {
                            sb.append(" | Ghi chú vị trí: ").append(item.getRawAdminNote().trim());
                        }
                        if (item.getRawDescription() != null && !item.getRawDescription().trim().isEmpty()) {
                            String shortDesc = item.getRawDescription().trim();
                            if (shortDesc.length() > 100) shortDesc = shortDesc.substring(0, 100) + "...";
                            sb.append(" | Mô tả: ").append(shortDesc.replace("\n", " "));
                        }
                        sb.append("\n");
                        count++;
                    }
                }
            }
        } catch (Exception ex) {
            sb.append("(Không thể tải danh sách dữ liệu thời gian thực: ").append(ex.getMessage()).append(")\n");
        }

        return sb.toString();
    }

    private String callGeminiApi(String apiKey, String systemInstruction, String userMessage) throws Exception {
        String cleanKey = apiKey != null ? apiKey.trim() : "";
        String encodedKey = java.net.URLEncoder.encode(cleanKey, StandardCharsets.UTF_8);

        String combinedPrompt = "[HƯỚNG DẪN HỆ THỐNG & DỮ LIỆU ĐỒ THẤT LẠC HUSC REFIND]:\n"
                + systemInstruction
                + "\n\n[CÂU HỎI CỦA NGƯỜI DÙNG]:\n"
                + userMessage;

        String payload = "{\"contents\":[{\"parts\":[{\"text\":\"" + escapeJson(combinedPrompt) + "\"}]}]}";

        String targetModel = (Constants.GEMINI_MODEL != null && !Constants.GEMINI_MODEL.trim().isEmpty())
                ? Constants.GEMINI_MODEL.trim()
                : "gemini-3.7-flash";

        List<String> modelsToTry = new java.util.ArrayList<>();
        modelsToTry.add(targetModel);
        if (!targetModel.equals("gemini-3.6-flash")) {
            modelsToTry.add("gemini-3.6-flash");
        }

        String lastErrorDetails = null;
        Exception lastException = null;

        for (String modelName : modelsToTry) {
            try {
                String endpoint = "https://generativelanguage.googleapis.com/v1beta/models/" + modelName + ":generateContent?key=" + encodedKey;

                HttpRequest httpRequest = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint))
                        .header("Content-Type", "application/json")
                        .header("x-goog-api-key", cleanKey)
                        .timeout(Duration.ofSeconds(25))
                        .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                        .build();

                HttpResponse<String> httpResponse = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
                int statusCode = httpResponse.statusCode();
                String resBody = httpResponse.body();

                if (statusCode >= 200 && statusCode < 300) {
                    String extractedText = extractGeminiReply(resBody);
                    if (extractedText != null && !extractedText.trim().isEmpty()) {
                        return extractedText;
                    }
                } else {
                    LOGGER.log(Level.WARNING, "Gemini API trả về status {0} với model {1}: {2}", new Object[]{statusCode, modelName, resBody});
                    String errorMsg = extractJsonField(resBody, "message");
                    if (errorMsg != null && !errorMsg.trim().isEmpty()) {
                        lastErrorDetails = errorMsg;
                    } else {
                        lastErrorDetails = "HTTP " + statusCode + " (" + resBody + ")";
                    }
                }
            } catch (Exception ex) {
                lastException = ex;
                LOGGER.log(Level.WARNING, "Lỗi khi gọi model {0}: {1}", new Object[]{modelName, ex.getMessage()});
            }
        }

        if (lastErrorDetails != null) {
            return "⚠️ Phản hồi từ Google AI Studio: " + lastErrorDetails;
        }

        if (lastException != null) {
            throw lastException;
        }
        return null;
    }

    private static String extractGeminiReply(String jsonResponse) {
        if (jsonResponse == null) return null;
        // Locate "text": "..."
        int textKeyIndex = jsonResponse.indexOf("\"text\": \"");
        if (textKeyIndex == -1) {
            textKeyIndex = jsonResponse.indexOf("\"text\":\"");
            if (textKeyIndex == -1) return null;
            textKeyIndex += 8;
        } else {
            textKeyIndex += 9;
        }

        StringBuilder sb = new StringBuilder();
        boolean escaped = false;
        for (int i = textKeyIndex; i < jsonResponse.length(); i++) {
            char c = jsonResponse.charAt(i);
            if (escaped) {
                switch (c) {
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    case 'b': sb.append('\b'); break;
                    case 'f': sb.append('\f'); break;
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    case 'u':
                        if (i + 4 < jsonResponse.length()) {
                            String hex = jsonResponse.substring(i + 1, i + 5);
                            try {
                                sb.append((char) Integer.parseInt(hex, 16));
                                i += 4;
                            } catch (NumberFormatException e) {
                                sb.append("\\u");
                            }
                        }
                        break;
                    default:
                        sb.append(c);
                        break;
                }
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String extractJsonField(String json, String fieldName) {
        if (json == null || fieldName == null) return null;
        String pattern = "\"" + fieldName + "\"";
        int idx = json.indexOf(pattern);
        if (idx == -1) return null;
        int colonIdx = json.indexOf(':', idx + pattern.length());
        if (colonIdx == -1) return null;
        int quoteStart = json.indexOf('"', colonIdx);
        if (quoteStart == -1) return null;

        StringBuilder sb = new StringBuilder();
        boolean escaped = false;
        for (int i = quoteStart + 1; i < json.length(); i++) {
            char c = json.charAt(i);
            if (escaped) {
                switch (c) {
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    case '"': sb.append('"'); break;
                    case '\\': sb.append('\\'); break;
                    default: sb.append(c); break;
                }
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                break;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String escapeJson(String raw) {
        if (raw == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < raw.length(); i++) {
            char ch = raw.charAt(i);
            switch (ch) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (ch < ' ') {
                        String t = "000" + Integer.toHexString(ch);
                        sb.append("\\u").append(t.substring(t.length() - 4));
                    } else {
                        sb.append(ch);
                    }
                    break;
            }
        }
        return sb.toString();
    }
}
