import { useState } from 'react'
import { useLogin } from '../hooks/useLogin'
import styles from './LoginPage.module.css'

export default function LoginPage() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const { handleLogin, loading, error } = useLogin()

  const onSubmit = (e) => {
    e.preventDefault()
    if (username && password) handleLogin(username, password)
  }

  return (
    <div className={styles.root}>
      {/* Background grid decoration */}
      <div className={styles.grid} aria-hidden="true" />

      <div className={styles.card}>
        <div className={styles.header}>
          <div className={styles.logo}>
            <span className={styles.logoMark}>◆</span>
          </div>
          <h1 className={styles.title}>Welcome back</h1>
          <p className={styles.subtitle}>Sign in to continue</p>
        </div>

        <form className={styles.form} onSubmit={onSubmit} noValidate>
          <div className={styles.field}>
            <label className={styles.label} htmlFor="username">
              Username
            </label>
            <input
              id="username"
              className={styles.input}
              type="text"
              autoComplete="username"
              placeholder="your_username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              disabled={loading}
            />
          </div>

          <div className={styles.field}>
            <label className={styles.label} htmlFor="password">
              Password
            </label>
            <div className={styles.passwordWrapper}>
              <input
                id="password"
                className={styles.input}
                type={showPassword ? 'text' : 'password'}
                autoComplete="current-password"
                placeholder="••••••••"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                disabled={loading}
              />
              <button
                type="button"
                className={styles.toggle}
                onClick={() => setShowPassword((v) => !v)}
                aria-label={showPassword ? 'Hide password' : 'Show password'}
              >
                {showPassword ? '🙈' : '👁️'}
              </button>
            </div>
          </div>

          {error && (
            <div className={styles.error} role="alert">
              <span className={styles.errorIcon}>!</span>
              {error}
            </div>
          )}

          <button
            type="submit"
            className={styles.submit}
            disabled={loading || !username || !password}
          >
            {loading ? (
              <span className={styles.spinner} aria-hidden="true" />
            ) : (
              'Sign in'
            )}
          </button>
        </form>
      </div>
    </div>
  )
}