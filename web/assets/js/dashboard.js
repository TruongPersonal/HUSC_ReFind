document.addEventListener('DOMContentLoaded', () => {
    const statusCtx = document.getElementById('statusChart');
    if (statusCtx && typeof Chart !== 'undefined') {
        const pending = parseInt(statusCtx.dataset.pending || '0', 10);
        const holding = parseInt(statusCtx.dataset.holding || '0', 10);
        const returned = parseInt(statusCtx.dataset.returned || '0', 10);
        const total = parseInt(statusCtx.dataset.total || '0', 10) || (pending + holding + returned) || 1;

        new Chart(statusCtx, {
            type: 'doughnut',
            data: {
                labels: ['Đang tìm', 'Đang giữ', 'Đã trả'],
                datasets: [{
                    data: [pending, holding, returned],
                    backgroundColor: ['#DC2626', '#D97706', '#059669'],
                    borderWidth: 2,
                    borderColor: '#FFFFFF'
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            font: { family: 'Be Vietnam Pro', size: 13, weight: '500' },
                            padding: 16,
                            usePointStyle: true
                        }
                    },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                const value = context.raw || 0;
                                const percentage = Math.round((value / total) * 100);
                                return ' ' + context.label + ': ' + value + ' tin (' + percentage + '%)';
                            }
                        }
                    }
                },
                cutout: '65%'
            }
        });
    }

    const categoryCtx = document.getElementById('categoryChart');
    const catDataContainer = document.getElementById('categoryChartData');
    if (categoryCtx && catDataContainer && typeof Chart !== 'undefined') {
        const catLabels = [];
        const catValues = [];
        catDataContainer.querySelectorAll('span').forEach(el => {
            catLabels.push(el.dataset.label || '');
            catValues.push(parseInt(el.dataset.value || '0', 10));
        });

        new Chart(categoryCtx, {
            type: 'bar',
            data: {
                labels: catLabels,
                datasets: [{
                    label: 'Số lượng',
                    data: catValues,
                    backgroundColor: '#1E3A8A',
                    borderRadius: 6,
                    maxBarThickness: 42
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: { stepSize: 1, precision: 0 },
                        grid: { color: '#E2E8F0' }
                    },
                    x: {
                        grid: { display: false },
                        ticks: { font: { family: 'Be Vietnam Pro', size: 12 } }
                    }
                }
            }
        });
    }

    const locationCtx = document.getElementById('locationChart');
    const locDataContainer = document.getElementById('locationChartData');
    if (locationCtx && locDataContainer && typeof Chart !== 'undefined') {
        const locLabels = [];
        const locValues = [];
        locDataContainer.querySelectorAll('span').forEach(el => {
            locLabels.push(el.dataset.label || '');
            locValues.push(parseInt(el.dataset.value || '0', 10));
        });

        new Chart(locationCtx, {
            type: 'bar',
            data: {
                labels: locLabels,
                datasets: [{
                    label: 'Số lượng',
                    data: locValues,
                    backgroundColor: '#2563EB',
                    borderRadius: 4,
                    maxBarThickness: 24
                }]
            },
            options: {
                indexAxis: 'y',
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: { display: false },
                    tooltip: {
                        callbacks: {
                            label: function(context) {
                                return ' Số lượng: ' + context.raw + ' tin';
                            }
                        }
                    }
                },
                scales: {
                    x: {
                        beginAtZero: true,
                        ticks: { stepSize: 1, precision: 0 },
                        grid: { color: '#E2E8F0' }
                    },
                    y: {
                        grid: { display: false },
                        ticks: {
                            font: { family: 'Be Vietnam Pro', size: 12, weight: '500' },
                            color: '#1E293B',
                            autoSkip: false
                        }
                    }
                }
            }
        });
    }
});
