document.addEventListener('DOMContentLoaded', function() {
    const container = document.getElementById('husc-chatbot-container');
    if (!container) return;

    const toggleBtn = document.getElementById('husc-chatbot-toggle');
    const chatBox = document.getElementById('husc-chatbot-box');
    const closeBtn = document.getElementById('husc-chatbot-close');
    const clearBtn = document.getElementById('husc-chatbot-clear');
    const form = document.getElementById('husc-chatbot-form');
    const input = document.getElementById('husc-chatbot-input');
    const messagesArea = document.getElementById('husc-chatbot-messages');
    const typingIndicator = document.getElementById('husc-chatbot-typing');
    const contextPath = container.getAttribute('data-context-path') || '';
    const csrfToken = container.getAttribute('data-csrf-token') || '';

    function scrollToBottom() {
        if (messagesArea) {
            messagesArea.scrollTop = messagesArea.scrollHeight;
        }
    }

    function toggleChat(show) {
        if (!chatBox) return;
        if (show === undefined) {
            show = chatBox.style.display === 'none';
        }
        chatBox.style.display = show ? 'flex' : 'none';
        if (show && input) {
            input.focus();
            scrollToBottom();
        }
    }

    if (toggleBtn) toggleBtn.addEventListener('click', () => toggleChat());
    if (closeBtn) closeBtn.addEventListener('click', () => toggleChat(false));

    // Clear chat
    if (clearBtn) {
        clearBtn.addEventListener('click', function() {
            if (confirm('Bạn muốn bắt đầu lại cuộc trò chuyện mới?')) {
                const welcomeMsg = messagesArea ? messagesArea.firstElementChild : null;
                if (messagesArea) {
                    messagesArea.innerHTML = '';
                    if (welcomeMsg) messagesArea.appendChild(welcomeMsg);
                }
                attachQuickPrompts();
            }
        });
    }

    function formatMarkdown(text) {
        if (!text) return '';
        let cleaned = text.replace(/^#{1,6}\s*(.*)$/gm, '**$1**');

        let escaped = cleaned
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;');

        escaped = escaped.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');
        escaped = escaped.replace(/\*(.*?)\*/g, '<em>$1</em>');

        const lines = escaped.split('\n');
        let html = '';
        let inList = false;

        lines.forEach(line => {
            const trimmed = line.trim();
            if (trimmed.startsWith('- ') || trimmed.startsWith('* ')) {
                if (!inList) {
                    html += '<ul class="mb-1 ps-3">';
                    inList = true;
                }
                html += '<li>' + trimmed.substring(2) + '</li>';
            } else if (/^\d+\.\s/.test(trimmed)) {
                if (!inList) {
                    html += '<ol class="mb-1 ps-3">';
                    inList = true;
                }
                html += '<li>' + trimmed.replace(/^\d+\.\s/, '') + '</li>';
            } else {
                if (inList) {
                    html += '</ul>';
                    inList = false;
                }
                if (trimmed) {
                    html += '<p class="mb-1">' + line + '</p>';
                } else {
                    html += '<div style="height: 4px;"></div>';
                }
            }
        });
        if (inList) html += '</ul>';
        return html;
    }

    function appendUserMessage(text) {
        if (!messagesArea) return;
        const div = document.createElement('div');
        div.className = 'd-flex justify-content-end mb-3';
        div.innerHTML = '<div class="chatbot-bubble-user">' + 
            text.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;') + 
            '</div>';
        messagesArea.appendChild(div);
        scrollToBottom();
    }

    function appendBotMessage(html) {
        if (!messagesArea) return;
        const div = document.createElement('div');
        div.className = 'd-flex gap-2 mb-3';
        div.innerHTML = `
            <div class="rounded-circle bg-primary text-white d-flex align-items-center justify-content-center flex-shrink-0"
                 style="width: 30px; height: 30px; font-size: 0.85rem;">
                <i class="bi bi-robot"></i>
            </div>
            <div class="chatbot-bubble-bot">` + html + `</div>
        `;
        messagesArea.appendChild(div);
        scrollToBottom();
    }

    async function sendMessage(msgText) {
        const text = (msgText || (input ? input.value : '') || '').trim();
        if (!text) return;

        appendUserMessage(text);
        if (input) input.value = '';
        if (typingIndicator) typingIndicator.style.display = 'block';
        scrollToBottom();

        try {
            const headers = {
                'Content-Type': 'application/json;charset=UTF-8'
            };
            if (csrfToken) {
                headers['X-CSRF-Token'] = csrfToken;
            }

            const response = await fetch(contextPath + '/api/chatbot', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify({ message: text })
            });

            if (typingIndicator) typingIndicator.style.display = 'none';

            if (!response.ok) {
                if (response.status === 404) {
                    appendBotMessage('<span class="text-danger"><i class="bi bi-exclamation-triangle me-1"></i> Chưa tải được Servlet <strong>/api/chatbot</strong> (HTTP 404).<br><small class="text-dark mt-1 d-block">👉 Bạn vui lòng nhấn <strong>Clean and Build</strong> hoặc <strong>Run</strong> lại dự án trên NetBeans để Tomcat nhận Servlet mới nhé!</small></span>');
                } else {
                    const errText = await response.text();
                    appendBotMessage('<span class="text-danger"><i class="bi bi-exclamation-triangle me-1"></i> Máy chủ phản hồi lỗi HTTP ' + response.status + '</span>');
                }
                return;
            }

            const data = await response.json();

            if (data.status === 'success' && data.reply) {
                appendBotMessage(formatMarkdown(data.reply));
            } else if (data.message) {
                appendBotMessage('<span class="text-danger"><i class="bi bi-exclamation-triangle me-1"></i> ' + data.message + '</span>');
            } else {
                appendBotMessage('Không thể nhận phản hồi lúc này. Vui lòng thử lại sau.');
            }
        } catch (err) {
            if (typingIndicator) typingIndicator.style.display = 'none';
            appendBotMessage('<span class="text-danger"><i class="bi bi-wifi-off me-1"></i> Không thể kết nối tới máy chủ (HTTP 404 / Network). Vui lòng nhấn <strong>Clean and Build</strong> hoặc <strong>Restart Tomcat</strong> trên NetBeans để cập nhật Servlet mới nhé!</span>');
        }
    }

    function attachQuickPrompts() {
        document.querySelectorAll('.chatbot-quick-prompt').forEach(btn => {
            btn.onclick = function() {
                const promptText = this.innerText.replace(/^[^\w\s\u00C0-\u1EF9]+/u, '').trim();
                sendMessage(promptText);
            };
        });
    }

    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            sendMessage();
        });
    }

    attachQuickPrompts();
});
