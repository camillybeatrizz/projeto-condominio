import axios from 'axios';

const axiosClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor: Injetar Bearer Token
axiosClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('kondo_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response Interceptor: Tratamento Global de Erros
axiosClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const { response } = error;

    if (response) {
      // 401: Unauthorized - Redirecionar para login (será tratado pelo AuthProvider futuramente)
      if (response.status === 401) {
        localStorage.removeItem('kondo_token');
        // window.location.href = '/login'; 
      }

      // 403: Forbidden - Acesso Negado
      if (response.status === 403) {
        console.error('Acesso Negado: Você não tem permissão para este recurso.');
      }

      // 422: Unprocessable Entity - Erro de Negócio (BusinessException do Backend)
      if (response.status === 422) {
        console.warn('Erro de Negócio:', response.data.message);
      }
    }

    return Promise.reject(error);
  }
);

export default axiosClient;
