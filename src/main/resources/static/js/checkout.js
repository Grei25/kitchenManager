document.getElementById('pickupCheck').addEventListener('change', function() {
    var addressBlock = document.getElementById('addressBlock');
    var addressInput = addressBlock.querySelector('textarea');
    if (this.checked) {
        addressBlock.style.display = 'none';
        addressInput.removeAttribute('required');
    } else {
        addressBlock.style.display = 'block';
        addressInput.setAttribute('required', '');
    }
});
