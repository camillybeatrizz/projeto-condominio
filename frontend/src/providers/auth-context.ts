import { createContext, useContext } from 'react';
import { MeuAcesso, MeuContexto } from '../types/api';

export interface AuthContextType {
  user: MeuContexto | null;
  activeAcesso: MeuAcesso | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (token: string) => Promise<void>;
  logout: () => void;
  selectAcesso: (acesso: MeuAcesso) => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth deve ser usado dentro de um AuthProvider');
  }
  return context;
}
