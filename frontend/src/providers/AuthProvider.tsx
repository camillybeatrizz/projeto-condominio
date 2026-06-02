import { useState, useEffect, ReactNode, useCallback } from 'react';
import { MeuContexto, MeuAcesso, Perfil } from '../types/api';
import axiosClient from '../api/axios-client';
import { AuthContext } from './auth-context';

const DEMO_CONDOMINIO_ID = '660e8400-e29b-41d4-a716-446655440001';
const DEMO_UNIDADE_ID = '770e8400-e29b-41d4-a716-446655440002';

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<MeuContexto | null>(null);
  const [activeAcesso, setActiveAcesso] = useState<MeuAcesso | null>(null);
  const [isLoading, setIsLoading] = useState(() => !!localStorage.getItem('kondo_token'));

  const handleAcessoSelection = useCallback((acessos: MeuAcesso[]) => {
    const savedAcessoId = localStorage.getItem('kondo_active_acesso_id');
    const foundAcesso = acessos.find(a => a.acessoId === savedAcessoId);
    
    if (foundAcesso) {
      setActiveAcesso(foundAcesso);
    } else if (acessos.length === 1) {
      const singleAcesso = acessos[0];
      setActiveAcesso(singleAcesso);
      localStorage.setItem('kondo_active_acesso_id', singleAcesso.acessoId);
    }
  }, []);

  const fetchUserContext = useCallback(async () => {
    try {
      const response = await axiosClient.get<MeuContexto>('/meu-contexto');
      setUser(response.data);
      handleAcessoSelection(response.data.acessos);
    } catch {
      console.warn('Backend offline ou erro na API. Ativando Modo de Simulação para testes visuais.');
      
      const mockUser: MeuContexto = {
        usuarioId: '550e8400-e29b-41d4-a716-446655440000',
        nome: 'João da Silva (Simulado)',
        email: 'joao@kondo.com',
        ativo: true,
        acessos: [
          {
            acessoId: 'cc0e8400-e29b-41d4-a716-446655440007',
            perfil: Perfil.SINDICO,
            condominioId: DEMO_CONDOMINIO_ID,
            condominioNome: 'Residencial Aurora',
          },
          {
            acessoId: 'cc0e8400-e29b-41d4-a716-446655440008',
            perfil: Perfil.MORADOR,
            condominioId: DEMO_CONDOMINIO_ID,
            condominioNome: 'Residencial Aurora',
            unidadeId: DEMO_UNIDADE_ID,
            unidadeNumero: '101 Bloco A',
          },
          {
            acessoId: 'cc0e8400-e29b-41d4-a716-446655440009',
            perfil: Perfil.ADMIN,
            condominioId: DEMO_CONDOMINIO_ID,
            condominioNome: 'Administração Kondo',
          }
        ]
      };
      
      setUser(mockUser);
      handleAcessoSelection(mockUser.acessos);
    } finally {
      setIsLoading(false);
    }
  }, [handleAcessoSelection]);

  useEffect(() => {
    const token = localStorage.getItem('kondo_token');
    if (token) {
      const timeoutId = window.setTimeout(() => {
        void fetchUserContext();
      }, 0);
      return () => window.clearTimeout(timeoutId);
    }
  }, [fetchUserContext]);

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
