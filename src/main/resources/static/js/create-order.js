function incrementQty(id) {
    var input = document.getElementById('qty-' + id);
    input.value = parseInt(input.value) + 1;
    updateCart();
}

function decrementQty(id) {
    var input = document.getElementById('qty-' + id);
    if (parseInt(input.value) > 0) {
        input.value = parseInt(input.value) - 1;
        updateCart();
    }
}

function updateCart() {
    var inputs = document.querySelectorAll('input[id^="qty-"]');
    var cartList = document.getElementById('cartList');
    var total = 0;
    var html = '';
    var hasItems = false;

    inputs.forEach(function(input) {
        var qty = parseInt(input.value);
        if (qty > 0) {
            hasItems = true;
            var price = parseFloat(input.dataset.price);
            var name = input.dataset.name;
            var sum = qty * price;
            total += sum;
            html += '<li class="list-group-item d-flex justify-content-between">' +
                    '<span>' + name + ' x' + qty + '</span>' +
                    '<span>' + sum + ' ₽</span></li>';
        }
    });

    if (!hasItems) {
        html = '<li class="list-group-item text-muted text-center" id="emptyCart">Корзина пуста</li>';
    }

    cartList.innerHTML = html;
    document.getElementById('totalPrice').textContent = total + ' ₽';
}

document.getElementById('orderForm').addEventListener('submit', function(e) {
    var inputs = document.querySelectorAll('input[id^="qty-"]');
    var cart = {};
    var hasItems = false;

    inputs.forEach(function(input) {
        var qty = parseInt(input.value);
        if (qty > 0) {
            hasItems = true;
            var id = input.id.replace('qty-', '');
            cart[id] = qty;
        }
    });

    if (!hasItems) {
        e.preventDefault();
        alert('Добавьте хотя бы одно блюдо в заказ');
        return;
    }

    document.getElementById('cartData').value = JSON.stringify(cart);
});

document.getElementById('pickupCheck').addEventListener('change', function() {
    var addressBlock = document.getElementById('addressBlock');
    if (this.checked) {
        addressBlock.style.display = 'none';
    } else {
        addressBlock.style.display = 'block';
    }
});
