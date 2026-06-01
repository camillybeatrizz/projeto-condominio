import { Routes, Route, Navigate } from 'react-router-dom';
import { ProtectedRoute } from './ProtectedRoute';
import { SelecaoContextoPage } from '../pages/SelecaoContextoPage';
import { Perfil } from '../types/api';

// Mocks para as páginas que serão criadas nas próximas fases
const PlaceholderPage = ({ title }: { title: string }) => (
  <div className="p-8">
    <h1 className="text-2xl font-bold">{title}</h1>
    <p className="mt-4 text-kondo-gray-600">Esta página está em desenvolvimento.</p>
  </div>
);

export function AppRoutes() {
  return (
    <Routes>
      {/* Rota Pública de Login (Mockada para o MVP) */}
      <Route path="/login" element={<PlaceholderPage title="Página de Login (Simulada)" />} />

      {/* Rota de Seleção de Contexto (Protegida) */}
      <Route 
        path="/selecionar-contexto" 
        element={
          <ProtectedRoute>
            <SelecaoContextoPage />
          </ProtectedRoute>
        } 
      />

      {/* Dashboard (Síndico e Admin) */}
      <Route 
        path="/dashboard" 
        element={
          <ProtectedRoute allowedRoles={[Perfil.SINDICO, Perfil.ADMIN]}>
            <PlaceholderPage title="Dashboard de Gestão" />
          </ProtectedRoute>
        } 
      />

      {/* Portal do Morador */}
      <Route 
        path="/portal-morador" 
        element={
          <ProtectedRoute allowedRoles={[Perfil.MORADOR]}>
            <PlaceholderPage title="Portal do Morador" />
          </ProtectedRoute>
        } 
      />

      {/* Redirecionamento Padrão */}
      <Route path="/" element={<Navigate to="/dashboard" replace />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
