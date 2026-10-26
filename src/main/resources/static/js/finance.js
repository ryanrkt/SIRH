document.addEventListener('DOMContentLoaded', () => {
    const csrfToken  = document.querySelector('meta[name="_csrf"]')?.content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;
    const tbody      = document.querySelector('.table-custom tbody');
    const emptyRow   = document.getElementById('empty-row');

    function toast(message, type = 'success') {
        const el = document.createElement('div');
        el.className = 'toast toast-' + type;
        el.textContent = message;
        document.getElementById('toast-container').appendChild(el);
        requestAnimationFrame(() => el.classList.add('show'));
        setTimeout(() => {
            el.classList.remove('show');
            setTimeout(() => el.remove(), 300);
        }, 3000);
    }

    function removeRow(row) {
        row.classList.add('row-removing');
        setTimeout(() => {
            row.remove();
            if (!tbody.querySelector('tr[data-id]')) {
                emptyRow.style.display = '';
            }
        }, 350);
    }

    document.querySelectorAll('.js-action-form').forEach(form => {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();

            const confirmMsg = form.dataset.confirm;
            if (confirmMsg && !confirm(confirmMsg)) return;

            const row = form.closest('tr');
            const buttons = row.querySelectorAll('button');
            buttons.forEach(b => b.disabled = true);

            try {
                const headers = { 'Accept': 'application/json' };
                if (csrfToken && csrfHeader) headers[csrfHeader] = csrfToken;

                const response = await fetch(form.action, { method: 'POST', headers });
                const data = await response.json().catch(() => ({}));

                if (response.ok) {
                    toast(data.message || 'Action effectuée.', 'success');
                    removeRow(row);
                } else {
                    toast(data.message || 'Une erreur est survenue.', 'error');
                    if (response.status === 409 || response.status === 404) {
                        removeRow(row); // déjà traitée ou introuvable
                    } else {
                        buttons.forEach(b => b.disabled = false);
                    }
                }
            } catch (err) {
                toast('Erreur réseau, veuillez réessayer.', 'error');
                buttons.forEach(b => b.disabled = false);
            }
        });
    });
});