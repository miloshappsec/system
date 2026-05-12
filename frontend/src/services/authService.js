import axios from 'axios'

// Uses relative paths — proxied by Vite to the correct backend service
const authClient = axios.create({
  headers: { 'Content-Type': 'application/json' },
})

export async function login(username, password) {
  const response = await authClient.post('/api/auth/login', { username, password })
  return response.data
}

export async function register(user) {
  const response = await authClient.post('/api/auth/register', user)
  return response.data
}

export default authClient
