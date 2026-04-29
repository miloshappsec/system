import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { login } from '../services/authService'
import { useAuth } from '../contexts/AuthContext'

export function useLogin() {
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const { saveSession } = useAuth()
  const navigate = useNavigate()

  const handleLogin = async (username, password) => {
    setLoading(true)
    setError(null)
    try {
      const data = await login(username, password)
      // Adjust these keys based on your actual API response shape
      saveSession(data.user ?? { username }, data.token ?? data.access)
      navigate('/dashboard')
    } catch (err) {
      const message =
        err?.response?.data?.detail ||
        err?.response?.data?.message ||
        'Invalid credentials. Please try again.'
      setError(message)
    } finally {
      setLoading(false)
    }
  }

  return { handleLogin, loading, error }
}
