const state = { token: localStorage.getItem('edis-token'), userId: Number(localStorage.getItem('edis-user-id')) || null, registering: false };
const api = async (url, options = {}) => {
  const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
  if (state.token) headers.Authorization = `Bearer ${state.token}`;
  const response = await fetch(url, { ...options, headers });
  if (response.status === 204) return null;
  const data = await response.json().catch(() => ({}));
  if (!response.ok) throw new Error(data.detail || data.erro || `Erro ${response.status}`);
  return data;
};
const escapeHtml = (value) => String(value).replace(/[&<>"']/g, (char) => ({ '&':'&amp;', '<':'&lt;', '>':'&gt;', '"':'&quot;', "'":'&#39;' })[char]);

function refreshAuthState() {
  document.querySelector('#auth-open').classList.toggle('hidden', Boolean(state.token));
  document.querySelector('#logout').classList.toggle('hidden', !state.token);
  document.querySelector('#review-form').classList.toggle('hidden', !state.token);
  document.querySelector('#review-login-note').classList.toggle('hidden', Boolean(state.token));
}

async function loadProducts() {
  const products = await api('/api/produtos');
  const favorites = state.token ? await api('/api/favoritos').catch(() => []) : [];
  const favoriteIds = new Set(favorites.map((item) => item.id));
  document.querySelector('#products').innerHTML = products.map((product) => {
    const imageName = encodeURIComponent(product.modelo) + '.jpg';
    return `<article class="product-card"><img src="/img/${imageName}" alt="Piso ${escapeHtml(product.modelo)}" onerror="this.style.visibility='hidden'"><h3>${escapeHtml(product.modelo)}</h3><p>${escapeHtml(product.marca)}</p><button class="button button-outline favorite" data-id="${product.id}">${favoriteIds.has(product.id) ? '♥ Favorito' : '♡ Favoritar'}</button></article>`;
  }).join('') || '<p class="muted">Nenhum piso cadastrado ainda.</p>';
}

async function loadReviews() {
  const reviews = await api('/api/avaliacoes');
  document.querySelector('#reviews').innerHTML = reviews.map((review) => `<article class="review-card"><h3>${escapeHtml(review.nomeAutor)}</h3><p>${escapeHtml(review.texto)}</p>${state.token && review.idAutor === state.userId ? `<div class="review-actions"><button data-edit="${review.id}">Editar</button><button data-delete="${review.id}">Excluir</button></div>` : ''}</article>`).join('') || '<p class="muted">Seja a primeira pessoa a avaliar.</p>';
}

function showAuth(registering) {
  state.registering = registering;
  document.querySelector('#auth-title').textContent = registering ? 'Criar conta' : 'Entrar';
  document.querySelector('#auth-submit').textContent = registering ? 'Cadastrar' : 'Entrar';
  document.querySelector('#name-label').classList.toggle('hidden', !registering);
  document.querySelector('#name').required = registering;
  document.querySelector('#auth-switch').textContent = registering ? 'Já tenho uma conta' : 'Criar uma conta';
  document.querySelector('#password').autocomplete = registering ? 'new-password' : 'current-password';
  document.querySelector('#auth-message').textContent = '';
  document.querySelector('#auth-dialog').showModal();
}

document.querySelector('#auth-open').addEventListener('click', () => showAuth(false));
document.querySelector('#auth-switch').addEventListener('click', () => showAuth(!state.registering));
document.querySelector('#logout').addEventListener('click', () => {
  state.token = null; state.userId = null;
  localStorage.removeItem('edis-token'); localStorage.removeItem('edis-user-id');
  refreshAuthState(); loadProducts(); loadReviews();
});
document.querySelector('#auth-form').addEventListener('submit', async (event) => {
  event.preventDefault();
  const route = state.registering ? '/api/auth/cadastro' : '/api/auth/login';
  const body = { email: document.querySelector('#email').value, senha: document.querySelector('#password').value };
  if (state.registering) body.nome = document.querySelector('#name').value;
  try {
    const result = await api(route, { method: 'POST', body: JSON.stringify(body) });
    state.token = result.token; state.userId = result.usuarioId;
    localStorage.setItem('edis-token', state.token); localStorage.setItem('edis-user-id', state.userId);
    document.querySelector('#auth-dialog').close(); refreshAuthState(); await loadProducts(); await loadReviews();
  } catch (error) { document.querySelector('#auth-message').textContent = error.message; }
});
document.querySelector('#review-form').addEventListener('submit', async (event) => {
  event.preventDefault();
  try { await api('/api/avaliacoes', { method: 'POST', body: JSON.stringify({ texto: document.querySelector('#review-text').value }) }); document.querySelector('#review-text').value = ''; await loadReviews(); }
  catch (error) { alert(error.message); }
});
document.querySelector('#products').addEventListener('click', async (event) => {
  const button = event.target.closest('.favorite'); if (!button) return;
  if (!state.token) { showAuth(false); return; }
  const id = button.dataset.id; const isFavorite = button.textContent.includes('♥');
  try { await api(`/api/favoritos/${id}`, { method: isFavorite ? 'DELETE' : 'PUT' }); await loadProducts(); }
  catch (error) { alert(error.message); }
});
document.querySelector('#reviews').addEventListener('click', async (event) => {
  const edit = event.target.closest('[data-edit]'); const remove = event.target.closest('[data-delete]');
  try {
    if (edit) { const text = prompt('Edite sua avaliação:'); if (text?.trim()) await api(`/api/avaliacoes/${edit.dataset.edit}`, { method: 'PUT', body: JSON.stringify({ texto: text }) }); }
    if (remove && confirm('Excluir esta avaliação?')) await api(`/api/avaliacoes/${remove.dataset.delete}`, { method: 'DELETE' });
    if (edit || remove) await loadReviews();
  } catch (error) { alert(error.message); }
});

refreshAuthState();
loadProducts().catch((error) => { document.querySelector('#catalog-message').textContent = error.message; });
loadReviews().catch((error) => { document.querySelector('#reviews').textContent = error.message; });
