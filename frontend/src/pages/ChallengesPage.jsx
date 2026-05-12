import { useState, useEffect } from 'react'
import axios from 'axios'
import { useNavigate } from 'react-router-dom'

const DIFFICULTY_COLOR = { Easy: '#4caf50', Medium: '#ff9800', Hard: '#f44336' }

export default function ChallengesPage() {
  const [challenges, setChallenges] = useState([])
  const [error, setError] = useState(null)
  const navigate = useNavigate()

  useEffect(() => {
    axios.get('/api/challenges')
      .then(r => setChallenges(r.data))
      .catch(() => setError('Failed to load challenges'))
  }, [])

  const card = { background: 'var(--surface)', border: '1px solid var(--border)', borderRadius: 'var(--radius)', padding: '1.25rem', marginBottom: '1rem' }

  return (
    <div style={{ padding: '2rem', fontFamily: 'var(--font-body)', color: 'var(--text)', maxWidth: 800, margin: '0 auto' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h1 style={{ fontFamily: 'var(--font-display)' }}>Challenges</h1>
        <button onClick={() => navigate('/dashboard')}
          style={{ padding: '0.5rem 1rem', background: 'var(--border)', color: 'var(--text)', border: 'none', borderRadius: 8, cursor: 'pointer' }}>
          Back
        </button>
      </div>

      {error && <p style={{ color: 'var(--error)' }}>{error}</p>}

      {challenges.map(c => (
        <div key={c.id} style={card}>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
            <div>
              <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>#{c.id} · {c.category}</span>
              <h3 style={{ margin: '0.25rem 0' }}>{c.title}</h3>
              <code style={{ fontSize: '0.8rem', color: 'var(--accent)', background: '#1a1a24', padding: '2px 6px', borderRadius: 4 }}>{c.endpoint}</code>
            </div>
            <span style={{ fontSize: '0.75rem', fontWeight: 700, color: DIFFICULTY_COLOR[c.difficulty] ?? '#aaa', marginLeft: 12, whiteSpace: 'nowrap' }}>
              {c.difficulty}
            </span>
          </div>
          <p style={{ color: 'var(--text-muted)', fontSize: '0.85rem', marginTop: '0.75rem' }}><strong>Goal:</strong> {c.goal}</p>
          <details style={{ marginTop: '0.5rem' }}>
            <summary style={{ color: 'var(--text-muted)', fontSize: '0.8rem', cursor: 'pointer' }}>Show hint</summary>
            <p style={{ color: 'var(--text-muted)', fontSize: '0.82rem', marginTop: '0.4rem', paddingLeft: '1rem' }}>{c.hint}</p>
          </details>
        </div>
      ))}
    </div>
  )
}
