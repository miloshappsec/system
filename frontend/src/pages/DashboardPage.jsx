import { useAuth } from '../contexts/AuthContext'
import { useNavigate } from 'react-router-dom'
import { useState, useEffect } from 'react'
import axios from 'axios'

export default function DashboardPage() {
  const { user, token, logout } = useAuth()
  const navigate = useNavigate()

  const [balance, setBalance]         = useState(user?.balance ?? '—')
  const [transactions, setTransactions] = useState([])
  const [txError, setTxError]         = useState(null)

  // Transfer form
  const [fromId, setFromId]   = useState(user?.id ?? '')
  const [toId, setToId]       = useState('')
  const [amount, setAmount]   = useState('')
  const [txResult, setTxResult] = useState(null)

  // Profile update form
  const [profileId, setProfileId]       = useState(user?.id ?? '')
  const [profileUsername, setProfileUsername] = useState(user?.username ?? '')
  const [profileRole, setProfileRole]   = useState(user?.role ?? 'user')
  const [profileBalance, setProfileBalance] = useState(user?.balance ?? '')
  const [profileResult, setProfileResult] = useState(null)

  // Fetch balance for this user (IDOR: change the ID in the input to see others)
  const fetchBalance = async (id) => {
    try {
      const res = await axios.get(`/data/users/id/${id}`)
      setBalance(res.data.balance)
    } catch (e) {
      setBalance('Error')
    }
  }

  // Fetch transaction history (IDOR: change userId to see anyone's history)
  const fetchTransactions = async (id) => {
    try {
      const res = await axios.get(`/data/transactions/${id}`)
      setTransactions(res.data)
      setTxError(null)
    } catch (e) {
      setTxError('Failed to load transactions')
    }
  }

  useEffect(() => {
    if (user?.id) {
      fetchBalance(user.id)
      fetchTransactions(user.id)
    }
  }, [])

  const handleTransfer = async (e) => {
    e.preventDefault()
    try {
      const res = await axios.post('/data/transactions/transfer', {
        fromId: Number(fromId),
        toId:   Number(toId),
        amount: Number(amount),
      })
      setTxResult(`Transfer successful! TX id: ${res.data.id}`)
      fetchBalance(user.id)
      fetchTransactions(user.id)
    } catch (e) {
      setTxResult('Transfer failed: ' + (e.response?.data || e.message))
    }
  }

  const handleProfileUpdate = async (e) => {
    e.preventDefault()
    try {
      const res = await axios.put(`/data/users/${profileId}`, {
        id:       Number(profileId),
        username: profileUsername,
        role:     profileRole,       // mass assignment — change to "admin"
        balance:  Number(profileBalance),
        password: user?.password,
        email:    user?.email,
        bankNumber: user?.bankNumber,
      })
      setProfileResult(`Updated! New role: ${res.data.role}, Balance: ${res.data.balance}`)
    } catch (e) {
      setProfileResult('Update failed: ' + (e.response?.data || e.message))
    }
  }

  const handleLogout = () => { logout(); navigate('/login') }

  const card = { background: 'var(--surface)', border: '1px solid var(--border)', borderRadius: 'var(--radius)', padding: '1.5rem', marginBottom: '1.5rem' }
  const input = { background: '#1a1a24', border: '1px solid var(--border)', borderRadius: 8, color: 'var(--text)', padding: '0.5rem 0.75rem', width: '100%', marginTop: 4 }
  const btn = { padding: '0.5rem 1.2rem', background: 'var(--accent)', color: '#0a0a0f', border: 'none', borderRadius: 8, fontWeight: 700, cursor: 'pointer', marginTop: '0.75rem' }
  const label = { display: 'block', color: 'var(--text-muted)', fontSize: '0.8rem', marginTop: '0.75rem' }

  return (
    <div style={{ padding: '2rem', fontFamily: 'var(--font-body)', color: 'var(--text)', maxWidth: 800, margin: '0 auto' }}>

      {/* Header */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <div>
          <h1 style={{ fontFamily: 'var(--font-display)', marginBottom: 4 }}>Hello, {user?.username ?? 'User'}</h1>
          <p style={{ color: 'var(--text-muted)' }}>Role: <strong style={{ color: 'var(--accent)' }}>{user?.role ?? '—'}</strong></p>
        </div>
        <button onClick={handleLogout} style={{ ...btn, background: '#333' }}>Logout</button>
      </div>

      {/* Balance */}
      <div style={card}>
        <h2 style={{ marginBottom: '0.75rem' }}>Account Balance</h2>
        <p style={{ fontSize: '2rem', color: 'var(--accent)', fontWeight: 700 }}>${balance}</p>
        <div style={{ display: 'flex', gap: 8, marginTop: '0.75rem' }}>
          <span style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>User ID:</span>
          {/* IDOR hint: change this ID to view another user's balance */}
          <input value={fromId} onChange={e => setFromId(e.target.value)} style={{ ...input, width: 80, marginTop: 0 }} />
          <button onClick={() => { fetchBalance(fromId); fetchTransactions(fromId) }} style={{ ...btn, marginTop: 0, padding: '0.3rem 0.8rem' }}>Load</button>
        </div>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.75rem', marginTop: 4 }}>
          Tip: Change the User ID above and click Load to view another user's data.
        </p>
      </div>

      {/* Transfer Money */}
      <div style={card}>
        <h2 style={{ marginBottom: '0.75rem' }}>Transfer Money</h2>
        <form onSubmit={handleTransfer}>
          <label style={label}>From Account ID
            <input style={input} value={fromId} onChange={e => setFromId(e.target.value)} placeholder="Your ID" />
          </label>
          <label style={label}>To Account ID
            <input style={input} value={toId} onChange={e => setToId(e.target.value)} placeholder="Recipient ID" />
          </label>
          <label style={label}>Amount (try a negative value)
            <input style={input} type="number" value={amount} onChange={e => setAmount(e.target.value)} placeholder="100.00" />
          </label>
          <button type="submit" style={btn}>Transfer</button>
          {txResult && <p style={{ marginTop: 8, color: 'var(--accent)', fontSize: '0.85rem' }}>{txResult}</p>}
        </form>
      </div>

      {/* Transaction History */}
      <div style={card}>
        <h2 style={{ marginBottom: '0.75rem' }}>Transaction History</h2>
        {txError && <p style={{ color: 'var(--error)' }}>{txError}</p>}
        {transactions.length === 0
          ? <p style={{ color: 'var(--text-muted)' }}>No transactions found.</p>
          : (
            <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '0.85rem' }}>
              <thead>
                <tr style={{ color: 'var(--text-muted)', textAlign: 'left' }}>
                  <th style={{ padding: '0.4rem' }}>ID</th>
                  <th style={{ padding: '0.4rem' }}>From</th>
                  <th style={{ padding: '0.4rem' }}>To</th>
                  <th style={{ padding: '0.4rem' }}>Amount</th>
                  <th style={{ padding: '0.4rem' }}>Time</th>
                </tr>
              </thead>
              <tbody>
                {transactions.map(tx => (
                  <tr key={tx.id} style={{ borderTop: '1px solid var(--border)' }}>
                    <td style={{ padding: '0.4rem' }}>{tx.id}</td>
                    <td style={{ padding: '0.4rem' }}>{tx.senderId}</td>
                    <td style={{ padding: '0.4rem' }}>{tx.receiverId}</td>
                    <td style={{ padding: '0.4rem', color: tx.amount < 0 ? 'var(--error)' : 'var(--accent)' }}>${tx.amount}</td>
                    <td style={{ padding: '0.4rem' }}>{tx.timestamp ? new Date(tx.timestamp).toLocaleString() : '—'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )
        }
      </div>

      {/* Profile Update (mass assignment) */}
      <div style={card}>
        <h2 style={{ marginBottom: '0.5rem' }}>Update Profile</h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.8rem', marginBottom: '0.75rem' }}>
          Note: All fields are sent to the server — including <code>role</code> and <code>balance</code>.
        </p>
        <form onSubmit={handleProfileUpdate}>
          <label style={label}>User ID (change to update another account — IDOR)
            <input style={input} value={profileId} onChange={e => setProfileId(e.target.value)} />
          </label>
          <label style={label}>Username
            <input style={input} value={profileUsername} onChange={e => setProfileUsername(e.target.value)} />
          </label>
          <label style={label}>Role (try: admin)
            <input style={input} value={profileRole} onChange={e => setProfileRole(e.target.value)} />
          </label>
          <label style={label}>Balance
            <input style={input} type="number" value={profileBalance} onChange={e => setProfileBalance(e.target.value)} />
          </label>
          <button type="submit" style={btn}>Update Profile</button>
          {profileResult && <p style={{ marginTop: 8, color: 'var(--accent)', fontSize: '0.85rem' }}>{profileResult}</p>}
        </form>
      </div>

      {/* Admin Panel link */}
      <div style={card}>
        <h2 style={{ marginBottom: '0.5rem' }}>Admin Panel</h2>
        <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>
          Visible to all users. Access is controlled only by the JWT role claim — which is not signature-verified.
        </p>
        <button
          style={{ ...btn, marginTop: '0.75rem' }}
          onClick={async () => {
            try {
              const res = await axios.get('/api/admin/users', {
                headers: { Authorization: `Bearer ${token}` },
              })
              alert(JSON.stringify(res.data, null, 2))
            } catch (e) {
              alert('Access denied (403) — try forging your JWT role claim to "admin"')
            }
          }}
        >
          List All Users (Admin Only)
        </button>
        <button
          style={{ ...btn, marginTop: '0.75rem', marginLeft: 8, background: '#555' }}
          onClick={() => navigate('/challenges')}
        >
          View Challenges
        </button>
      </div>

    </div>
  )
}

