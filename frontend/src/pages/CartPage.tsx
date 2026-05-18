import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { api, type Product } from '../api/client'

function getCart(): Record<string, number> {
  const raw = localStorage.getItem('cart')
  if (!raw) return {}
  try { return JSON.parse(raw) } catch { return {} }
}

function setCart(cart: Record<string, number>) {
  localStorage.setItem('cart', JSON.stringify(cart))
}

export default function CartPage() {
  const [products, setProducts] = useState<Product[]>([])
  const [cart, setCartState] = useState<Record<string, number>>(getCart())
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const navigate = useNavigate()

  useEffect(() => {
    api.listProducts()
      .then(setProducts)
      .catch(err => setError(err instanceof Error ? err.message : 'Erro'))
      .finally(() => setLoading(false))
  }, [])

  const items = products.filter(p => cart[p.id] > 0).map(p => ({
    product: p,
    quantity: cart[p.id],
    subtotal: p.price * cart[p.id],
  }))

  const total = items.reduce((s, i) => s + i.subtotal, 0)

  function updateQty(productId: string, delta: number) {
    const next = { ...cart }
    next[productId] = (next[productId] || 0) + delta
    if (next[productId] <= 0) delete next[productId]
    setCart(next)
    setCartState(next)
  }

  function removeItem(productId: string) {
    const next = { ...cart }
    delete next[productId]
    setCart(next)
    setCartState(next)
  }

  async function checkout() {
    if (items.length === 0) return
    setSubmitting(true)
    setError('')
    try {
      const orderItems = items.map(i => ({ productId: i.product.id, quantity: i.quantity }))
      await api.createOrder(orderItems)
      localStorage.removeItem('cart')
      navigate('/orders')
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro ao criar pedido')
    } finally {
      setSubmitting(false)
    }
  }

  if (loading) return <div className="loading">Carregando carrinho...</div>

  return (
    <div>
      <h1>Carrinho</h1>
      {error && <div className="error">{error}</div>}
      {items.length === 0 ? (
        <div className="empty">Seu carrinho está vazio</div>
      ) : (
        <div style={{ display: 'grid', gridTemplateColumns: '2fr 1fr', gap: '24px' }}>
          <div>
            {items.map(item => (
              <div key={item.product.id} className="card">
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <div>
                    <h3 style={{ marginBottom: '4px' }}>{item.product.name}</h3>
                    <div style={{ fontSize: '13px', color: '#86868b' }}>{item.product.sku}</div>
                    <div style={{ marginTop: '8px', fontSize: '16px', fontWeight: 600 }}>
                      R$ {Number(item.product.price).toFixed(2).replace('.', ',')}
                    </div>
                  </div>
                  <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: '8px' }}>
                    <div className="qty-control">
                      <button onClick={() => updateQty(item.product.id, -1)}>−</button>
                      <span style={{ minWidth: '24px', textAlign: 'center' }}>{item.quantity}</span>
                      <button onClick={() => updateQty(item.product.id, 1)}>+</button>
                    </div>
                    <button
                      onClick={() => removeItem(item.product.id)}
                      className="btn btn-secondary"
                      style={{ padding: '4px 12px', fontSize: '12px' }}
                    >
                      Remover
                    </button>
                    <div style={{ fontSize: '16px', fontWeight: 700 }}>
                      R$ {item.subtotal.toFixed(2).replace('.', ',')}
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>

          <div className="cart-summary">
            <h2>Resumo</h2>
            {items.map(i => (
              <div key={i.product.id} className="cart-item">
                <span style={{ fontSize: '14px' }}>{i.quantity}× {i.product.name}</span>
                <span>R$ {i.subtotal.toFixed(2).replace('.', ',')}</span>
              </div>
            ))}
            <div className="cart-total">
              <span>Total</span>
              <span>R$ {total.toFixed(2).replace('.', ',')}</span>
            </div>
            <button
              onClick={checkout}
              disabled={submitting}
              className="btn"
              style={{ width: '100%', marginTop: '16px' }}
            >
              {submitting ? 'Processando...' : 'Finalizar pedido'}
            </button>
          </div>
        </div>
      )}
    </div>
  )
}
