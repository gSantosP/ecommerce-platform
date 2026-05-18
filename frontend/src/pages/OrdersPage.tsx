import { useEffect, useState } from 'react'
import { api, type Order } from '../api/client'

export default function OrdersPage() {
  const [orders, setOrders] = useState<Order[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  useEffect(() => {
    api.listMyOrders()
      .then(setOrders)
      .catch(err => setError(err instanceof Error ? err.message : 'Erro'))
      .finally(() => setLoading(false))
  }, [])

  if (loading) return <div className="loading">Carregando pedidos...</div>
  if (error) return <div className="error">{error}</div>

  return (
    <div>
      <h1>Meus pedidos</h1>
      {orders.length === 0 ? (
        <div className="empty">Você ainda não fez pedidos</div>
      ) : (
        orders.map(order => (
          <div key={order.id} className="order-card">
            <div className="order-header">
              <div>
                <div style={{ fontSize: '13px', color: '#86868b' }}>Pedido</div>
                <div style={{ fontFamily: 'monospace', fontSize: '14px' }}>{order.id.slice(0, 8)}...</div>
              </div>
              <div className={`status-badge status-${order.status}`}>{order.status}</div>
            </div>
            <div>
              {order.items.map(item => (
                <div key={item.productId} style={{ display: 'flex', justifyContent: 'space-between', padding: '6px 0', fontSize: '14px' }}>
                  <span>{item.quantity}× {item.productName}</span>
                  <span>R$ {Number(item.subtotal).toFixed(2).replace('.', ',')}</span>
                </div>
              ))}
            </div>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '12px', paddingTop: '12px', borderTop: '1px solid #f0f0f0' }}>
              <span style={{ color: '#86868b', fontSize: '13px' }}>
                {new Date(order.createdAt).toLocaleString('pt-BR')}
              </span>
              <span style={{ fontWeight: 700 }}>
                Total: R$ {Number(order.totalAmount).toFixed(2).replace('.', ',')}
              </span>
            </div>
          </div>
        ))
      )}
    </div>
  )
}
