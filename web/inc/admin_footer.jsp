<%@page contentType="text/html" pageEncoding="UTF-8"%>
        </main>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

<script src="${pageContext.request.contextPath}/assets/js/main.js?v=20260819_v6"></script>

<script>
    // Toggle mobile admin sidebar
    document.addEventListener('DOMContentLoaded', () => {
        const toggleBtn = document.getElementById('toggleAdminSidebar');
        const sidebar = document.getElementById('adminSidebar');
        const backdrop = document.getElementById('adminSidebarBackdrop');

        if (toggleBtn && sidebar && backdrop) {
            toggleBtn.addEventListener('click', () => {
                sidebar.classList.toggle('show');
                backdrop.classList.toggle('show');
            });
            backdrop.addEventListener('click', () => {
                sidebar.classList.remove('show');
                backdrop.classList.remove('show');
            });
        }
    });
</script>

</body>
</html>
