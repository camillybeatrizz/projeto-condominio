import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { MeuContexto, MeuAcesso } from '../types/api';
import axiosClient from '../api/axios-client';

interface AuthContextType {
  user: MeuContexto | null;
  activeAcesso: MeuAcesso | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (token: string) => Promise<void>;
  logout: () => void;
  selectAcesso: (acesso: MeuAcesso) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<MeuContexto | null>(null);
  const [activeAcesso, setActiveAcesso] = useState<MeuAcesso | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const fetchUserContext = async () => {
    try {
      const response = await axiosClient.get<MeuContexto>('/meu-contexto');
      setUser(response.data);
      
      // Tentar recuperar acesso salvo ou selecionar o único disponível
      const savedAcessoId = localStorage.getItem('kondo_active_acesso_id');
      const foundAcesso = response.data.acessos.find(a => a.acessoId === savedAcessoId);
      
      if (foundAcesso) {
        setActiveAcesso(foundAcesso);
      } else if (response.data.acessos.length === 1) {
        const singleAcesso = response.data.acessos[0];
        setActiveAcesso(singleAcesso);
        localStorage.setItem('kondo_active_acesso_id', singleAcesso.acessoId);
      }
    } catch (error) {
      console.error('Erro ao carregar contexto do usuário:', error);
      logout();
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    const token = localStorage.getItem('kondo_token');
    if (token) {
      fetchUserContext();
    } else {
      setIsLoading(false);
    }
  }, []);

  const login = async (token: string) => {
    localStorage.setItem('kondo_token', token);
    setIsLoading(true);
    await fetchUserContext();
  };

  const logout = () => {
    localStorage.removeItem('kondo_token');
    localStorage.removeItem('kondo_active_acesso_id');
    setUser(null);
    setActiveAcesso(null);
    // Redirecionar para login externo se necessário
  };

  const selectAcesso = (acesso: MeuAcesso) => {
    setActiveAcesso(acesso);
    localStorage.setItem('kondo_active_acesso_id', acesso.acessoId);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        activeAcesso,
        isAuthenticated: !!user,
        isLoading,
        login,
        logout,
        selectAcesso,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth deve ser usado dentro de um AuthProvider');
  }
  return context;
}
