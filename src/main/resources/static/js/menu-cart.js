document.addEventListener('DOMContentLoaded', updateCartCount);

async function updateCartCount() {
    try {
        const response = await fetch('/client/cart/count');
        const data = await response.json();
        const el = document.getElementById('cartCount');
        el.textContent = data.count;
        el.style.display = data.count > 0 ? 'inline' : 'none';
    } catch (e) { console.error(e); }
}

async function addToCart(btn) {
    const id = btn.getAttribute('data-dish-id');
    btn.disabled = true;
    btn.innerHTML = '<i class="bi bi-hourglass-split"></i>...';

    try {
        const response = await fetch('/client/cart/add/' + id, { method: 'POST' });
        const data = await response.json();

        if (data.success) {
            const el = document.getElementById('cartCount');
            el.textContent = data.cartCount;
            el.style.display = data.cartCount > 0 ? 'inline' : 'none';
            el.classList.add('pulse');
            setTimeout(() => el.classList.remove('pulse'), 300);

            showToast(data.message);
            btn.classList.add('added');
            btn.innerHTML = '<i class="bi bi-check"></i> Добавлено';

            setTimeout(() => {
                btn.classList.remove('added');
                btn.innerHTML = '<i class="bi bi-cart-plus"></i> В корзину';
            }, 1500);
        }
    } catch (e) {
        console.error(e);
        showToast('Ошибка', true);
    } finally {
        btn.disabled = false;
    }
}

function showToast(msg, isError) {
    const el = document.getElementById('cartToast');
    const msgEl = document.getElementById('toastMessage');
    const header = el.querySelector('.toast-header');
    msgEl.textContent = msg;
    header.className = isError ? 'toast-header bg-danger text-white' : 'toast-header bg-success text-white';
    new bootstrap.Toast(el).show();
}
