import axios from 'axios'

const authClient = axios.create({
  baseURL: 'http://auth-service',
  headers: {
    'Content-Type': 'application/json',
  },
})

/**
 * Login a user with username and password.
 * POST http://auth-service/auth/login/
 * @param {string} username
 * @param {string} password
 * @returns {Promise<{token: string, user: object}>}
 */
export async function login(username, password) {
  const response = await authClient.post('/auth/login/', { username, password })
  return response.data
}

export default authClient
