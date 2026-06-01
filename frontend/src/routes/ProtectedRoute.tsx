import { ReactNode } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../providers/AuthProvider';
import { Perfil } from '../types/api';

interface ProtectedRouteProps {
  children: ReactNode;
  allowedRoles?: Perfil[];
}

export function ProtectedRoute({ children, allowedRoles }: ProtectedRouteProps) {
  const { isAuthenticated, activeAcesso, isLoading } = useAuth();
  const location = useLocation();

  if (isLoading) {
    return (
      <div className="flex items-center justify-center h-screen bg-kondo-gray-50">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-kondo-purple-600"></div>
      </div>
    );
  }

  if (!isAuthenticated) {
    // Para o MVP, assumimos que o login é externo. 
    // Em um cenário real, redirecionaríamos para o OIDC provider.
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // Se o usuário está logado mas não escolheu um contexto (e tem mais de um)
  if (!activeAcesso && location.pathname !== '/selecionar-contexto') {
    return <Navigate to="/selecionar-contexto" replace />;
  }

  // Verificar RBAC (Role-Based Access Control)
  if (allowedRoles && activeAcesso && !allowedRoles.includes(activeAcesso.perfil)) {
    return (
      <div className="flex flex-col items-center justify-center h-screen bg-kondo-gray-50 p-4 text-center">
        <h1 className="text-4xl font-bold text-kondo-red-600 mb-4">403</h1>
        <h2 className="text-xl font-semibold text-kondo-gray-900 mb-2">Acesso Negado</h2>
        <p className="text-kondo-gray-600 mb-6">Você não tem permissão para acessar esta área.</p>
        <button 
          onClick={() => window.history.back()}
          className="px-6 py-2 bg-kondo-purple-600 text-white rounded-lg font-medium hover:bg-kondo-purple-700 transition-colors"
        >
          Voltar
        </button>
      </div>
    );
  }

  return <>{children}</>;
}
