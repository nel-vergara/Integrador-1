/**
 * MEDINA FRAGRANCES - CONSOLIDATED JAVASCRIPT
 */

// =========================================
// ESTADO GLOBAL
// =========================================
let cart = JSON.parse(localStorage.getItem('medinaCart')) || [];
let users = JSON.parse(localStorage.getItem('medinaUsers')) || [];
let currentUser = JSON.parse(localStorage.getItem('medinaCurrentUser')) || null;

// =========================================
// MÓDULO: CARRITO
// =========================================
const CartModule = {
  init() {
    this.updateCounter();
    this.renderOffcanvas();
    this.bindAddToCartButtons();
  },

  updateCounter() {
    const cartCount = cart.reduce((total, item) => total + item.quantity, 0);
    document.querySelectorAll('.cart-counter').forEach(counter => {
      counter.textContent = cartCount;
      counter.style.display = cartCount > 0 ? 'inline-block' : 'none';
    });
    const countText = document.querySelector('.cart-count-text');
    if (countText) countText.textContent = cartCount;
  },

  save() {
    localStorage.setItem('medinaCart', JSON.stringify(cart));
    this.updateCounter();
    this.renderOffcanvas();
  },

  add(product) {
    const productId = String(product.id);
    const existing = cart.find(item => String(item.id) === productId);
    if (existing) {
      existing.quantity += 1;
    } else {
      cart.push({ ...product, id: productId, quantity: 1 });
    }
    this.save();
    showNotification('Producto añadido al carrito', 'success');
    const offcanvasEl = document.getElementById('cartOffcanvas');
    if (offcanvasEl) {
      const offcanvas = new bootstrap.Offcanvas(offcanvasEl);
      offcanvas.show();
    }
  },

  remove(id) {
    const targetId = String(id);
    cart = cart.filter(item => String(item.id) !== targetId);
    this.save();
    showNotification('Producto eliminado', 'warning');
  },

  updateQty(id, change) {
    const item = cart.find(i => String(i.id) === String(id));
    if (item) {
      item.quantity += change;
      if (item.quantity <= 0) this.remove(id);
      else this.save();
    }
  },

  calcTotal() {
    return cart.reduce((total, item) => total + (item.price * item.quantity), 0);
  },

  renderOffcanvas() {
    const container = document.getElementById('offcanvas-cart-items');
    const summary = document.getElementById('offcanvas-cart-summary');
    if (!container) return;

    const totalItems = cart.reduce((acc, item) => acc + item.quantity, 0);
    const countText = document.querySelector('.cart-count-text');
    if (countText) countText.textContent = totalItems;

    if (cart.length === 0) {
      container.innerHTML = `
        <div class="text-center py-5 text-muted">
          <i class="bi bi-bag-x display-4 d-block mb-3"></i>
          <p class="mb-2">Tu carrito está vacío</p>
          <a href="/catalogo" class="btn btn-sm btn-outline-dark mt-2">
            <i class="bi bi-grid me-1"></i>IR AL CATÁLOGO
          </a>
        </div>`;
      if (summary) summary.style.display = 'none';
      return;
    }

    let html = '';
    cart.forEach(item => {
      html += `
        <div class="d-flex align-items-center mb-3 pb-3 border-bottom">
          <img src="${item.image}" class="rounded" style="width: 70px; height: 70px; object-fit: cover; background: #f8f9fa; padding: 5px;" alt="${item.name}">
          <div class="ms-3 flex-grow-1">
            <h6 class="mb-0 fw-bold" style="font-size: 0.9rem;">${item.name}</h6>
            <p class="text-muted small mb-2">${item.description || ''}</p>
            <div class="d-flex align-items-center gap-2">
              <button class="btn btn-sm btn-outline-secondary py-0 px-2" onclick="updateQuantity('${item.id}', -1)" style="font-size: 0.8rem;">-</button>
              <span class="fw-bold small">${item.quantity}</span>
              <button class="btn btn-sm btn-outline-secondary py-0 px-2" onclick="updateQuantity('${item.id}', 1)" style="font-size: 0.8rem;">+</button>
            </div>
          </div>
          <div class="text-end ms-2">
            <p class="fw-bold mb-1" style="font-size: 0.9rem;">S/. ${(item.price * item.quantity).toFixed(2)}</p>
            <button class="btn btn-link text-danger p-0 text-decoration-none" onclick="removeFromCart('${item.id}')" style="font-size: 0.75rem;">
              <i class="bi bi-trash3"></i> Quitar
            </button>
          </div>
        </div>`;
    });

    container.innerHTML = html;
    if (summary) summary.style.display = 'block';

    const subtotal = this.calcTotal();
    const shipping = subtotal >= 350 ? 0 : 20;
    const total = subtotal + shipping;

    const subtotalEl = document.getElementById('offcanvas-subtotal');
    const shippingEl = document.getElementById('offcanvas-shipping');
    const shippingMsg = document.getElementById('offcanvas-shipping-msg');
    const totalEl = document.getElementById('offcanvas-total');

    if (subtotalEl) subtotalEl.textContent = `S/. ${subtotal.toFixed(2)}`;
    if (shippingEl) {
      if (shipping === 0) {
        shippingEl.textContent = 'GRATIS';
        shippingEl.className = 'text-success fw-bold';
      } else {
        shippingEl.textContent = `S/. ${shipping.toFixed(2)}`;
        shippingEl.className = 'fw-bold';
      }
    }
    if (shippingMsg) {
      shippingMsg.textContent = shipping > 0 ? `¡Faltan S/. ${(350 - subtotal).toFixed(2)} para envío gratis!` : '¡Envío gratis aplicado!';
    }
    if (totalEl) totalEl.textContent = `S/. ${total.toFixed(2)}`;
  },

  checkout() {
    if (cart.length === 0) {
      showNotification('Tu carrito está vacío', 'warning');
      return;
    }
    window.location.href = '/checkout';
  },

  bindAddToCartButtons() {
    
    document.querySelectorAll('.btn-add-to-cart').forEach(btn => {
      btn.addEventListener('click', function(e) {
        e.preventDefault();
        const card = this.closest('.product-card, .decant-card');
        if (!card) return;
        const product = {
          id: card.dataset.id,
          name: card.dataset.name,
          price: parseFloat(card.dataset.price),
          image: card.dataset.image,
          description: card.dataset.description
        };
        CartModule.add(product);
      });
    });
  }
};

// =========================================
// UTILIDADES
// =========================================
function showNotification(message, type = 'success') {
  const bg = type === 'success' ? 'bg-dark' : type === 'warning' ? 'bg-warning text-dark' : 'bg-info';
  const icon = type === 'success' ? 'check-circle' : type === 'warning' ? 'exclamation-triangle' : 'info-circle';
  const note = document.createElement('div');
  note.className = `alert ${bg} text-white position-fixed top-0 end-0 m-3 z-3 shadow-lg`;
  note.style.cssText = 'z-index: 9999; animation: slideInRight 0.3s ease; min-width: 250px;';
  note.innerHTML = `<div class="d-flex align-items-center"><i class="bi bi-${icon} me-2"></i><span>${message}</span></div>`;
  document.body.appendChild(note);
  setTimeout(() => {
    note.style.animation = 'slideOutRight 0.3s ease';
    setTimeout(() => note.remove(), 300);
  }, 2000);
}

// =========================================
// MODAL DE PRODUCTO - CATALOGO
// =========================================
let currentCatalogProduct = null;

function productFromCard(card) {
  if (!card) return null;
  return {
    id: card.dataset.id,
    name: card.dataset.name,
    brand: card.dataset.brand || '',
    price: parseFloat(card.dataset.price || '0'),
    image: card.dataset.image,
    description: card.dataset.description || '',
    category: card.dataset.category || '',
    stock: parseInt(card.dataset.stock || '0', 10),
    notes: card.dataset.notes || 'Salida fresca, corazon especiado y fondo amaderado elegante.'
  };
}

function openProductModal(product) {
  const modal = document.getElementById('productModal');
  if (!modal || !product) return;
  currentCatalogProduct = product;

  const img = document.getElementById('productModalImg');
  const title = document.getElementById('productModalTitle');
  const brand = document.getElementById('productModalBrand');
  const desc = document.getElementById('productModalDesc');
  const notes = document.getElementById('productModalNotes');
  const price = document.getElementById('productModalPrice');
  const addBtn = document.getElementById('productModalAdd');

  if (img) {
    img.src = product.image;
    img.alt = product.name;
  }
  if (title) title.textContent = product.name;
  if (brand) brand.textContent = product.brand || product.category || 'MEDINA FRAGRANCES';
  if (desc) desc.textContent = product.description;
  if (notes) notes.textContent = product.notes;
  if (price) price.textContent = `S/. ${product.price.toFixed(2)}`;
  if (addBtn) addBtn.disabled = product.stock === 0;

  ['productModalThumb1', 'productModalThumb2', 'productModalThumb3'].forEach((id) => {
    const thumb = document.getElementById(id);
    if (thumb) thumb.src = product.image;
  });

  document.querySelectorAll('#productModal .size-option').forEach((btn, index) => {
    btn.classList.toggle('active', index === 0);
  });

  modal.classList.add('is-open');
  modal.setAttribute('aria-hidden', 'false');
  document.body.classList.add('modal-open-soft');
}

function closeProductModal() {
  const modal = document.getElementById('productModal');
  if (!modal) return;
  modal.classList.remove('is-open');
  modal.setAttribute('aria-hidden', 'true');
  document.body.classList.remove('modal-open-soft');
}

function addCatalogProductFromModal() {
  if (!currentCatalogProduct) return;
  CartModule.add({
    id: currentCatalogProduct.id,
    name: currentCatalogProduct.name,
    price: currentCatalogProduct.price,
    image: currentCatalogProduct.image,
    description: currentCatalogProduct.description
  });
  closeProductModal();
}

function bindCatalogModal() {
  document.querySelectorAll('.catalog-product-card').forEach(card => {
    card.addEventListener('click', (event) => {
      if (event.target.closest('.btn-add-to-cart')) return;
      openProductModal(productFromCard(card));
    });
  });

  document.querySelectorAll('.btn-view-product').forEach(btn => {
    btn.addEventListener('click', (event) => {
      const card = btn.closest('.product-card, .decant-card');
      if (!card || !card.classList.contains('catalog-product-card')) return;
      event.preventDefault();
      event.stopPropagation();
      openProductModal(productFromCard(card));
    });
  });

  document.querySelectorAll('[data-close-product-modal]').forEach(el => {
    el.addEventListener('click', closeProductModal);
  });

  const addBtn = document.getElementById('productModalAdd');
  if (addBtn) addBtn.addEventListener('click', addCatalogProductFromModal);

  document.querySelectorAll('#productModal .product-thumb').forEach(thumb => {
    thumb.addEventListener('click', () => {
      const image = thumb.querySelector('img');
      const mainImage = document.getElementById('productModalImg');
      if (image && mainImage) mainImage.src = image.src;
      document.querySelectorAll('#productModal .product-thumb').forEach(item => item.classList.remove('active'));
      thumb.classList.add('active');
    });
  });

  document.querySelectorAll('#productModal .size-option').forEach(btn => {
    btn.addEventListener('click', () => {
      document.querySelectorAll('#productModal .size-option').forEach(item => item.classList.remove('active'));
      btn.classList.add('active');
    });
  });
}

document.addEventListener('keydown', (event) => {
  if (event.key === 'Escape') closeProductModal();
});

function showLoginModal() {
  alert('Función de login - Conecta con tu backend');
}

if (!document.getElementById('anim-styles')) {
  const style = document.createElement('style');
  style.id = 'anim-styles';
  style.textContent = `
    @keyframes slideInRight { from { transform: translateX(100%); opacity: 0; } to { transform: translateX(0); opacity: 1; } }
    @keyframes slideOutRight { from { transform: translateX(0); opacity: 1; } to { transform: translateX(100%); opacity: 0; } }
  `;
  document.head.appendChild(style);
}

// =========================================
// LÓGICA DEL MODAL DE DECANTS
// =========================================
let currentDecantProduct = {};
let selectedSize = 5; // Por defecto 5ml
const sizeMultipliers = { 3: 0.65, 5: 1.0, 10: 1.9 }; // Factores de precio

function openDecantModal(id, title, basePrice, image, desc) {
  currentDecantProduct = { id, title, basePrice, image, desc };
  selectedSize = 5; // Resetear a 5ml al abrir

  // Actualizar contenido del modal
  document.getElementById('modalTitle').textContent = title;
  document.getElementById('modalBrand').textContent = title.split(' ')[0]; // Marca aproximada
  document.getElementById('modalImg').src = image;
  document.getElementById('modalDesc').textContent = desc;

  // Resetear botones de tamaño visualmente
  document.querySelectorAll('.size-option').forEach(btn => btn.classList.remove('active'));
  document.querySelector('.size-option:nth-child(2)').classList.add('active'); // Seleccionar 5ml visualmente

  updateModalPrice();

  // Mostrar Modal
  const modal = new bootstrap.Modal(document.getElementById('decantModal'));
  modal.show();
}

function selectDecantSize(size) {
  selectedSize = size;
  
  // Actualizar estilo visual de botones
  document.querySelectorAll('.size-option').forEach(btn => {
    btn.classList.remove('active');
    if(btn.textContent.trim() === size + 'ml') {
      btn.classList.add('active');
    }
  });

  updateModalPrice();
}

function updateModalPrice() {
  const finalPrice = (currentDecantProduct.basePrice * sizeMultipliers[selectedSize]).toFixed(2);
  document.getElementById('modalPrice').textContent = `S/. ${finalPrice}`;
}

function addToCartFromModal() {
  const finalPrice = currentDecantProduct.basePrice * sizeMultipliers[selectedSize];
  const productName = `${currentDecantProduct.title} (${selectedSize}ml)`;
  
  const item = {
    id: `${currentDecantProduct.id}-${selectedSize}`, 
    name: productName,
    price: finalPrice,
    image: currentDecantProduct.image,
    description: `Decant de ${selectedSize}ml`
  };

  CartModule.add(item);

  // Cerrar modal
  const modalEl = document.getElementById('decantModal');
  const modal = bootstrap.Modal.getInstance(modalEl);
  modal.hide();
}

function buyNowFromModal() {
  addToCartFromModal();
  setTimeout(() => CartModule.checkout(), 500);
}

function bindDecantCardModal() {
  document.querySelectorAll('.decant-card').forEach(card => {
    card.addEventListener('click', (event) => {
      if (event.target.closest('button, a')) return;
      openDecantModal(
        card.dataset.id,
        card.dataset.name,
        parseFloat(card.dataset.price || '0'),
        card.dataset.image,
        card.dataset.description || ''
      );
    });
  });
}

// =========================================
// INICIALIZACIÓN
// =========================================
document.addEventListener('DOMContentLoaded', () => {
  CartModule.init();
  bindCatalogModal();
  bindDecantCardModal();
});

// =========================================
// EXPORTS GLOBALES
// =========================================
window.addToCart = (p) => CartModule.add(p);
window.removeFromCart = (id) => CartModule.remove(id);
window.updateQuantity = (id, ch) => CartModule.updateQty(id, ch);
window.proceedToCheckout = () => CartModule.checkout();
window.showLoginModal = showLoginModal;
window.openProductModal = openProductModal;
window.closeProductModal = closeProductModal;

// Funciones globales para el modal de decants
window.openDecantModal = openDecantModal;
window.selectDecantSize = selectDecantSize;
window.addToCartFromModal = addToCartFromModal;
window.buyNowFromModal = buyNowFromModal;


// =========================================
// Login & Registo
// =========================================
document.addEventListener('DOMContentLoaded', () => {
    
    // 1. GESTIÓN DEL NAVBAR
    const nombreGuardado = localStorage.getItem('usuarioNombre');
    const iconoUsuario = document.querySelector('.bi-person');

    if (nombreGuardado && iconoUsuario) {
        const contenedor = iconoUsuario.parentElement;
        contenedor.innerHTML = `
            <div class="d-flex align-items-center">
                <span class="me-2 small fw-bold text-dark text-uppercase">HOLA, ${nombreGuardado}</span>
                <a href="#" id="btnLogout" class="text-danger small text-decoration-none ms-2">SALIR</a>
            </div>
        `;
        document.getElementById('btnLogout').addEventListener('click', (e) => {
            e.preventDefault();
            localStorage.removeItem('usuarioNombre');
            window.location.href = '/';
        });
    }

    // 2. REGISTRO
    const formReg = document.getElementById('formRegister');
    if (formReg) {
        formReg.addEventListener('submit', (e) => {
            e.preventDefault();
            
            const nombre = document.getElementById('regNombre').value;
            const email = document.getElementById('regEmail').value;
            const pass = document.getElementById('regPass').value;

            let usuarios = JSON.parse(localStorage.getItem('usuarios_registrados')) || [];
            
            usuarios.push({ nombre: nombre, email: email, pass: pass });
            localStorage.setItem('usuarios_registrados', JSON.stringify(usuarios));

            alert("Registro exitoso. Ahora puedes iniciar sesión.");
            window.location.href = '/login';
        });
    }

    // 3. LOGIN
    const formLog = document.getElementById('formLogin');
    if (formLog) {
        formLog.addEventListener('submit', (e) => {
            e.preventDefault();
            
            const emailIngresado = document.getElementById('loginEmail').value;
            const passIngresada = document.getElementById('loginPass').value;

            let usuarios = JSON.parse(localStorage.getItem('usuarios_registrados')) || [];

            const usuarioEncontrado = usuarios.find(u => u.email === emailIngresado && u.pass === passIngresada);

            if (usuarioEncontrado) {
                const primerNombre = usuarioEncontrado.nombre.trim().split(' ')[0];
                localStorage.setItem('usuarioNombre', primerNombre.toUpperCase());
                window.location.href = '/';
            } else {
                alert("Correo o contraseña incorrectos. ¿Ya te registraste?");
            }
        });
    }
});
