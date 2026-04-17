// === auth.js: Хранение токенов с уникальными ключами ===

// Сохранить токен для пользователя
function storeUserToken(userId, role, token) {
    const key = `jwt_token_${userId}_${role}`;
    localStorage.setItem(key, token);

    // Запоминаем, кто сейчас в этой вкладке
    sessionStorage.setItem('current_user_id', userId);
    sessionStorage.setItem('current_user_role', role);
}
document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault(); // ← КРИТИЧНО! Блокирует стандартную отправку формы
    // ... ваш код fetch
});

// Получить токен для текущей вкладки
function getCurrentToken() {
    const userId = sessionStorage.getItem('current_user_id');
    const role = sessionStorage.getItem('current_user_role');

    if (userId && role) {
        return localStorage.getItem(`jwt_token_${userId}_${role}`);
    }
    return null;
}

// Очистить токен текущего пользователя
function clearCurrentToken() {
    const userId = sessionStorage.getItem('current_user_id');
    const role = sessionStorage.getItem('current_user_role');

    if (userId && role) {
        localStorage.removeItem(`jwt_token_${userId}_${role}`);
        sessionStorage.removeItem('current_user_id');
        sessionStorage.removeItem('current_user_role');
    }
}

// Проверить, авторизован ли пользователь в этой вкладке
function isAuthenticated() {
    return getCurrentToken() !== null;
}

// Сделать авторизованный fetch-запрос
async function apiFetch(url, options = {}) {
    const token = getCurrentToken();

    const headers = {
        'Content-Type': 'application/json',
        ...(token && { 'Authorization': `Bearer ${token}` }),
        ...options.headers
    };

    const response = await fetch(url, { ...options, headers });

    if (response.status === 401) {
        clearCurrentToken();
        window.location.href = '/login';
        return null;
    }

    return response;
}

