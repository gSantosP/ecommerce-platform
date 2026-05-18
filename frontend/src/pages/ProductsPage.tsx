import { useEffect, useState } from 'react'
import { api, type Product } from '../api/client'
import { useAuth } from '../context/AuthContext'

// Cart helpers (localStorage)
function getCart(): Record<string, number> {
  const raw = localStorage.getItem('cart')
  if (!raw) return {}
  try { return JSON.parse(raw) } catch { return {} }
}

function setCart(cart: Record<string, number>) {
  localStorage.setItem('cart', JSON.stringify(cart))
}

export default function ProductsPage() {
  const [products, setProducts] = useState<Product[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [addedSku, setAddedSku] = useState<string | null>(null)
  const { user } = useAuth()

  useEffect(() => {
    api.listProducts()
      .then(setProducts)
      .catch(err => setError(err instanceof Error ? err.message : 'Erro ao carregar produtos'))
      .finally(() => setLoading(false))
  }, [])

  function addToCart(product: Product) {
    if (!user) {
      window.location.href = '/login'
      return
    }
    const cart = getCart()
    cart[product.id] = (cart[product.id] || 0) + 1
    setCart(cart)
    setAddedSku(product.sku)
    setTimeout(() => setAddedSku(null), 1500)
  }

  if (loading) return <div className="loading">Carregando produtos...</div>
  if (error) return <div className="error">{error}</div>

  return (
    <div>
      <h1>Produtos</h1>
      {products.length === 0 ? (
        <div className="empty">Nenhum produto disponível</div>
      ) : (
        <div className="products-grid">
          {products.map(product => (
            <div key={product.id} className="product-card">
              <div className="sku">{product.sku}</div>
              <h3>{product.name}</h3>
              <div className="description">{product.description}</div>
              <div className="price">
                R$ {Number(product.price).toFixed(2).replace('.', ',')}
              </div>
              <div className="stock">
                {product.stockQuantity > 0
                  ? `${product.stockQuantity} em estoque`
                  : 'Esgotado'}
              </div>
              <button
                className="btn"
                onClick={() => addToCart(product)}
                disabled={product.stockQuantity === 0}
              >
                {addedSku === product.sku ? '✓ Adicionado!' : 'Adicionar ao carrinho'}
              </button>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
