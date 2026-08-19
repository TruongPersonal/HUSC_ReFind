document.addEventListener('DOMContentLoaded', () => {
  initCsrfProtection();
  initToastContainer();
  initFlashMessages();
  initGlobalSearchShortcut();
  initImagePreviews();
  initBookmarkButtons();
  initPasswordToggles();
  initNavbarScroll();
});

function initCsrfProtection() {
  const token = document.querySelector('meta[name="csrf-token"]')?.content;
  if (!token) return;
  document.querySelectorAll('form[method="POST"], form[method="post"]').forEach((form) => {
    if (!form.querySelector('input[name="csrf_token"]')) {
      const input = document.createElement('input');
      input.type = 'hidden';
      input.name = 'csrf_token';
      input.value = token;
      form.appendChild(input);
    }
  });
}

function initNavbarScroll() {
  const navbar = document.querySelector('.navbar-husc');
  if (!navbar) return;

  const onScroll = () => {
    if (window.scrollY > 20) {
      navbar.classList.add('scrolled');
    } else {
      navbar.classList.remove('scrolled');
    }
  };

  window.addEventListener('scroll', onScroll, { passive: true });
  onScroll();
}

function initToastContainer() {
  if (!document.querySelector('.husc-toast-container')) {
    const container = document.createElement('div');
    container.className = 'husc-toast-container';
    document.body.appendChild(container);
  }
}

function showToast(message, type = 'info', duration = 3000) {
  const container = document.querySelector('.husc-toast-container');
  if (!container || !message) return;

  const toast = document.createElement('div');
  toast.className = `husc-toast ${type}`;

  let icon = 'bi-info-circle-fill text-primary';
  if (type === 'success') icon = 'bi-check-circle-fill text-success';
  if (type === 'error') icon = 'bi-exclamation-triangle-fill text-danger';

  toast.innerHTML = `
    <i class="bi ${icon} fs-5 mt-1"></i>
    <div class="flex-grow-1" style="font-size: 0.88rem; font-weight: 600; color: var(--husc-dark); line-height: 1.4;">
      ${message}
    </div>
    <button type="button" class="btn-close ms-auto" style="font-size: 0.7rem;" aria-label="Close"></button>
    <div class="toast-progress" style="animation-duration: ${duration}ms;"></div>
  `;

  const closeBtn = toast.querySelector('.btn-close');
  closeBtn.addEventListener('click', () => {
    dismissToast(toast);
  });

  container.appendChild(toast);

  let startTime = Date.now();
  let remaining = duration;
  let timerId = null;

  const startTimer = () => {
    startTime = Date.now();
    timerId = setTimeout(() => {
      dismissToast(toast);
    }, remaining);
  };

  startTimer();

  toast.addEventListener('mouseenter', () => {
    const progress = toast.querySelector('.toast-progress');
    if (progress) progress.style.animationPlayState = 'paused';
    clearTimeout(timerId);
    remaining -= Date.now() - startTime;
    if (remaining < 600) remaining = 600;
  });

  toast.addEventListener('mouseleave', () => {
    const progress = toast.querySelector('.toast-progress');
    if (progress) progress.style.animationPlayState = 'running';
    startTimer();
  });
}

function dismissToast(toast) {
  if (!toast || toast.dataset.dismissing === 'true') return;
  toast.dataset.dismissing = 'true';
  toast.style.transition = 'all 0.3s cubic-bezier(0.16, 1, 0.3, 1)';
  toast.style.opacity = '0';
  toast.style.transform = 'translateX(100%) scale(0.9)';
  setTimeout(() => {
    if (toast.parentNode) toast.remove();
  }, 300);
}

window.showToast = showToast;

function initFlashMessages() {
  const flashData = document.getElementById('huscFlashData');
  if (flashData) {
    const successMsg = flashData.dataset.success;
    const errorMsg = flashData.dataset.error;
    const infoMsg = flashData.dataset.info;

    if (successMsg) showToast(successMsg, 'success');
    if (errorMsg) showToast(errorMsg, 'error');
    if (infoMsg) showToast(infoMsg, 'info');
  }
}

function initGlobalSearchShortcut() {
  const searchInput = document.getElementById('navbarSearchInput');
  if (!searchInput) return;

  document.addEventListener('keydown', (e) => {

    const activeEl = document.activeElement;
    const isTyping = activeEl && (
      activeEl.tagName === 'INPUT' ||
      activeEl.tagName === 'TEXTAREA' ||
      activeEl.isContentEditable
    );

    if (e.key === '/' && !isTyping) {
      e.preventDefault();
      searchInput.focus();
      searchInput.select();
    }
  });
}

function initImagePreviews() {
  const fileInputs = document.querySelectorAll('.input-image-preview');

  fileInputs.forEach((input) => {
    const dropzone = input.closest('.image-dropzone');

    input.addEventListener('change', (e) => {
      const file = e.target.files[0];
      if (file) {
        if (!file.type.startsWith('image/')) {
          showToast('Vui lòng chọn file hình ảnh hợp lệ (.jpg, .png, .webp)', 'error');
          input.value = '';
          if (dropzone) {
            dropzone.classList.remove('has-image');
            const previewWrapper = dropzone.querySelector('.dropzone-preview-wrapper');
            if (previewWrapper) previewWrapper.remove();
          }
          return;
        }

        if (file.size > 10 * 1024 * 1024) {
          showToast('Ảnh quá lớn. Vui lòng chọn ảnh có dung lượng dưới 10MB.', 'error');
          input.value = '';
          if (dropzone) {
            dropzone.classList.remove('has-image');
            const previewWrapper = dropzone.querySelector('.dropzone-preview-wrapper');
            if (previewWrapper) previewWrapper.remove();
          }
          return;
        }

        const reader = new FileReader();
        reader.onload = (event) => {
          if (dropzone) {
            dropzone.classList.add('has-image');
            let previewWrapper = dropzone.querySelector('.dropzone-preview-wrapper');
            if (!previewWrapper) {
              previewWrapper = document.createElement('div');
              previewWrapper.className = 'dropzone-preview-wrapper';
              dropzone.appendChild(previewWrapper);
            }
            previewWrapper.innerHTML = `
              <img src="${event.target.result}" alt="Xem trước ảnh" class="dropzone-preview-img">
              <div class="dropzone-overlay-badge">
                <i class="bi bi-arrow-repeat"></i> Đổi ảnh
              </div>
            `;
          }
        };
        reader.readAsDataURL(file);
      }
    });
  });

  const dropzones = document.querySelectorAll('.image-dropzone');
  dropzones.forEach((dropzone) => {
    const input = dropzone.querySelector('input[type="file"]');
    if (!input) return;

    dropzone.addEventListener('click', (e) => {
      if (e.target !== input) {
        input.click();
      }
    });

    ['dragenter', 'dragover'].forEach((eventName) => {
      dropzone.addEventListener(eventName, (e) => {
        e.preventDefault();
        e.stopPropagation();
        dropzone.classList.add('dragover');
      });
    });

    ['dragleave', 'drop'].forEach((eventName) => {
      dropzone.addEventListener(eventName, (e) => {
        e.preventDefault();
        e.stopPropagation();
        dropzone.classList.remove('dragover');
      });
    });

    dropzone.addEventListener('drop', (e) => {
      const dt = e.dataTransfer;
      const files = dt.files;
      if (files.length > 0) {
        input.files = files;
        input.dispatchEvent(new Event('change'));
      }
    });
  });
}

function initBookmarkButtons() {
  document.addEventListener('click', async (e) => {
    const btn = e.target.closest('.btn-bookmark-ajax');
    if (!btn) return;

    e.preventDefault();
    e.stopPropagation();

    const itemId = btn.dataset.itemId;
    const contextPath = btn.dataset.contextPath || '';

    if (!itemId) return;

    try {
      const response = await fetch(`${contextPath}/bookmark`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          'X-Requested-With': 'XMLHttpRequest',
          'X-CSRF-Token': document.querySelector('meta[name="csrf-token"]')?.content || ''
        },
        body: new URLSearchParams({ item_id: itemId })
      });

      if (response.status === 401) {
        window.location.href = `${contextPath}/login`;
        return;
      }

      const data = await response.json();
      if (data.success) {
        const icon = btn.querySelector('i');
        const textSpan = btn.querySelector('span');

        if (data.saved) {
          btn.classList.add('active');
          if (icon) icon.className = 'bi bi-heart-fill text-danger';
          if (textSpan) textSpan.textContent = 'Đã lưu tin';
          showToast('Đã lưu đồ vật vào mục Đồ đã lưu', 'success');
        } else {
          btn.classList.remove('active');
          if (icon) icon.className = 'bi bi-heart';
          if (textSpan) textSpan.textContent = 'Lưu theo dõi';
          showToast('Đã bỏ lưu bài viết', 'info');

          const savedCard = btn.closest('.saved-item-card-wrapper');
          if (savedCard) {
            savedCard.style.transition = 'all 0.3s cubic-bezier(0.16, 1, 0.3, 1)';
            savedCard.style.opacity = '0';
            savedCard.style.transform = 'scale(0.9)';
            setTimeout(() => savedCard.remove(), 300);
          }
        }
      } else {
        showToast(data.message || 'Có lỗi xảy ra', 'error');
      }
    } catch (err) {
      showToast('Không thể kết nối đến máy chủ', 'error');
    }
  });
}

function initPasswordToggles() {
  const toggleBtns = document.querySelectorAll('.btn-toggle-password');
  toggleBtns.forEach((btn) => {
    btn.addEventListener('click', () => {
      const targetId = btn.dataset.target;
      const input = document.getElementById(targetId);
      const icon = btn.querySelector('i');
      if (input) {
        if (input.type === 'password') {
          input.type = 'text';
          if (icon) icon.className = 'bi bi-eye-slash';
        } else {
          input.type = 'password';
          if (icon) icon.className = 'bi bi-eye';
        }
      }
    });
  });
}
