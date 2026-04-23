const LIVEPOLL_API_BASE = 'http://localhost:8080';
const ACCESS_TOKEN_KEY = 'livepoll.accessToken';
const REFRESH_TOKEN_KEY = 'livepoll.refreshToken';
const USERNAME_KEY = 'livepoll.username';
const ROLES_KEY = 'livepoll.roles';

const api = axios.create({
  baseURL: LIVEPOLL_API_BASE,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000,
});

function readStoredSession() {
  const roles = JSON.parse(localStorage.getItem(ROLES_KEY) || '[]');

  return {
    accessToken: localStorage.getItem(ACCESS_TOKEN_KEY),
    refreshToken: localStorage.getItem(REFRESH_TOKEN_KEY),
    username: localStorage.getItem(USERNAME_KEY),
    roles,
  };
}

function storeSession({ accessToken, refreshToken, username, roles }) {
  if (accessToken) {
    localStorage.setItem(ACCESS_TOKEN_KEY, accessToken);
  }

  if (refreshToken) {
    localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
  }

  if (username) {
    localStorage.setItem(USERNAME_KEY, username);
  }

  if (roles) {
    localStorage.setItem(ROLES_KEY, JSON.stringify(roles));
  }
}

function clearSession() {
  localStorage.removeItem(ACCESS_TOKEN_KEY);
  localStorage.removeItem(REFRESH_TOKEN_KEY);
  localStorage.removeItem(USERNAME_KEY);
  localStorage.removeItem(ROLES_KEY);
}

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem(ACCESS_TOKEN_KEY);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && originalRequest && !originalRequest._retry) {
      originalRequest._retry = true;

      const refreshToken = localStorage.getItem(REFRESH_TOKEN_KEY);
      if (refreshToken) {
        try {
          const refreshResponse = await axios.post(`${LIVEPOLL_API_BASE}/auth/refresh`, {
            refreshToken,
          });

          const newAccessToken = refreshResponse.data.accessToken;
          localStorage.setItem(ACCESS_TOKEN_KEY, newAccessToken);
          originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
          return api(originalRequest);
        } catch (refreshError) {
          clearSession();
          console.warn('Session expired. Please log in again.');
          return Promise.reject(refreshError);
        }
      }
    }

    if (error.response?.data?.message) {
      error.userMessage = error.response.data.message;
    } else if (error.response?.status === 403) {
      error.userMessage = "You don't have permission to access this.";
    } else if (!error.response) {
      error.userMessage = 'Cannot connect to the backend server.';
    }

    return Promise.reject(error);
  }
);

async function loginWithPassword(username, password) {
  const response = await api.post('/auth/login', { username, password });
  storeSession(response.data);
  return response.data;
}

async function refreshSession() {
  const { refreshToken } = readStoredSession();
  if (!refreshToken) {
    throw new Error('No refresh token found.');
  }

  const response = await axios.post(`${LIVEPOLL_API_BASE}/auth/refresh`, { refreshToken });
  localStorage.setItem(ACCESS_TOKEN_KEY, response.data.accessToken);
  return response.data;
}

function startGoogleLogin() {
  window.open(
    `${LIVEPOLL_API_BASE}/oauth2/authorization/google`,
    'livepoll-google-login',
    'width=520,height=720'
  );
}

async function me() {
  const response = await api.get('/api/me');
  return response.data;
}

async function listPolls() {
  const response = await api.get('/api/polls');
  return response.data;
}

async function getPoll(pollId) {
  const response = await api.get(`/api/polls/${pollId}`);
  return response.data;
}

async function getPollResults(pollId) {
  const response = await api.get(`/api/polls/${pollId}/results`);
  return response.data;
}

async function createPoll(payload) {
  const response = await api.post('/api/polls', payload);
  return response.data;
}

async function vote(pollId, optionId) {
  const response = await api.post(`/api/polls/${pollId}/vote`, { optionId });
  return response.data;
}

async function deletePoll(pollId) {
  const response = await api.delete(`/api/polls/${pollId}`);
  return response.data;
}

window.livePollApi = {
  api,
  baseUrl: LIVEPOLL_API_BASE,
  readStoredSession,
  storeSession,
  clearSession,
  loginWithPassword,
  refreshSession,
  startGoogleLogin,
  me,
  listPolls,
  getPoll,
  getPollResults,
  createPoll,
  vote,
  deletePoll,
};