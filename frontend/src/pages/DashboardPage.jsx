import { useAuth } from '../contexts/AuthContext'
import { useNavigate } from 'react-router-dom'

export default function DashboardPage() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => {
    logout()
    navigate('/login')
  }

  return (
    <div style={{ padding: '2rem', fontFamily: 'var(--font-body)', color: 'var(--text)' }}>
      <h1 style={{ fontFamily: 'var(--font-display)' }}>
        Hello, {user?.username ?? 'User'} 👋
      </h1>
      <p style={{ color: 'var(--text-muted)', marginTop: '0.5rem' }}>
        You're logged in. Build your dashboard here.
      </p>
      <button
        onClick={handleLogout}
        style={{
          marginTop: '1.5rem',
          padding: '0.6rem 1.2rem',
          background: 'var(--accent)',
          color: '#0a0a0f',
          border: 'none',
          borderRadius: '8px',
          fontWeight: '700',
          cursor: 'pointer',
        }}
      >
        Logout
      </button>
    </div>
  )
}
