import { Routes, Route, Navigate } from 'react-router-dom';
import { ProtectedRoute } from './ProtectedRoute';
import { SelecaoContextoPage } from '../pages/SelecaoContextoPage';
import { DashboardGestaoPage } from '../pages/DashboardGestaoPage';
import { PortalMoradorPage } from '../pages/PortalMoradorPage';
import { MainLayout } from '../components/MainLayout';
import { Perfil } from '../types/api';

const PlaceholderPage = ({ title }: { title: string }) => (
  <MainLayout breadcrumbs={[{ label: title }]}>
    <div className="p-8 bg-white rounded-xl border border-kondo-gray-200 shadow-sm">
      <h1 className="text-2xl font-bold">{title}</h1>
      <p className="mt-4 text-kondo-gray-600">Esta página está em desenvolvimento.</p>
    </div>
  </MainLayout>
);

export function AppRoutes() {
  return (
    <Routes>
      {/* Rota Pública de Login (Mockada para o MVP) */}
      <Route path="/login" element={
        <div className="flex items-center justify-center h-screen bg-kondo-gray-50">
          <div className="bg-white p-8 rounded-2xl shadow-xl border border-kondo-gray-200 w-full max-w-md">
            <h1 className="text-2xl font-bold mb-6 text-center">Login Kondo</h1>
            <p className="text-kondo-gray-600 text-center mb-6">Em um cenário real, você seria redirecionado para o Keycloak.</p>
            <button 
              onClick={() => {
                localStorage.setItem('kondo_token', 'mock-token');
                window.location.href = '/selecionar-contexto';
              }}
              className="w-full py-3 bg-kondo-purple-600 text-white rounded-xl font-bold hover:bg-kondo-purple-700 transition-all"
            >
              Simular Login
            </button>
          </div>
        </div>
      } />

      {/* Rota de Seleção de Contexto (Protegida) - Layout Especial */}
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
            <DashboardGestaoPage />
          </ProtectedRoute>
        } 
      />

      {/* Portal do Morador */}
      <Route 
        path="/portal-morador" 
        element={
          <ProtectedRoute allowedRoles={[Perfil.MORADOR]}>
            <PortalMoradorPage />
          </ProtectedRoute>
        } 
      />

      {/* Rotas Administrativas Genéricas */}
      <Route path="/admin/*" element={
        <ProtectedRoute allowedRoles={[Perfil.ADMIN]}>
          <PlaceholderPage title="Área Administrativa" />
        </ProtectedRoute>
      } />

      {/* Rotas de Gestão Genéricas */}
      <Route path="/gestao/*" element={
        <ProtectedRoute allowedRoles={[Perfil.SINDICO]}>
          <PlaceholderPage title="Área de Gestão" />
        </ProtectedRoute>
      } />

      {/* Redirecionamento Padrão */}
      <Route path="/" element={<Navigate to="/dashboard" replace />} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
