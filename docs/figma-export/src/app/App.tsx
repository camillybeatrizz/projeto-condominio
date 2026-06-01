import { useState } from 'react';
import { Building2, LayoutDashboard, Home as HomeIcon, Users, CreditCard, Wallet, Headphones, Calendar } from 'lucide-react';
import { ContextSelector } from './components/ContextSelector';
import { MainLayout } from './components/MainLayout';
import { DashboardGestao } from './pages/DashboardGestao';
import { PortalMorador } from './pages/PortalMorador';
import { GestaoCondominio } from './pages/GestaoCondominio';
import { GestaoUsuarios } from './pages/GestaoUsuarios';
import { FinanceiroCobrancas } from './pages/FinanceiroCobrancas';
import { FinanceiroDespesas } from './pages/FinanceiroDespesas';
import { OperacionalChamados } from './pages/OperacionalChamados';
import { AreasComuns } from './pages/AreasComuns';

type View = 'context-selector' | 'dashboard-gestao' | 'portal-morador' | 'gestao-condominio' | 'gestao-usuarios' | 'financeiro-cobrancas' | 'financeiro-despesas' | 'operacional-chamados' | 'areas-comuns';

export default function App() {
  const [currentView, setCurrentView] = useState<View>('context-selector');
  const [userRole, setUserRole] = useState<'admin' | 'sindico' | 'morador'>('sindico');

  // Context Selector View
  if (currentView === 'context-selector') {
    return (
      <ContextSelector
        userName="João Silva"
        contexts={[
          {
            id: 'admin',
            title: 'Administração Global',
            subtitle: 'Acesso total ao sistema',
            role: 'admin',
            onClick: () => {
              setUserRole('admin');
              setCurrentView('dashboard-gestao');
            },
          },
          {
            id: 'aurora',
            title: 'Edifício Aurora',
            subtitle: 'Gestão do condomínio',
            role: 'sindico',
            onClick: () => {
              setUserRole('sindico');
              setCurrentView('dashboard-gestao');
            },
          },
          {
            id: 'sunset',
            title: 'Residencial Sunset',
            subtitle: 'Acesso às informações pessoais',
            role: 'morador',
            block: 'A',
            unit: '301',
            onClick: () => {
              setUserRole('morador');
              setCurrentView('portal-morador');
            },
          },
          {
            id: 'vista-verde',
            title: 'Condomínio Vista Verde',
            subtitle: 'Acesso às informações pessoais',
            role: 'morador',
            block: 'B',
            unit: '102',
            onClick: () => {
              setUserRole('morador');
              setCurrentView('portal-morador');
            },
          },
        ]}
        onLogout={() => alert('Logout clicked')}
      />
    );
  }

  // Morador Portal (Different Layout)
  if (currentView === 'portal-morador') {
    return (
      <MainLayout
        breadcrumbs={[{ label: 'Meu Portal' }]}
        userProfile={{
          name: 'João Silva',
          role: 'Morador',
        }}
        activeCondominium={{
          name: 'Residencial Sunset',
          options: [
            { id: 'sunset', name: 'Residencial Sunset' },
            { id: 'vista-verde', name: 'Vista Verde' },
          ],
          onChange: (id) => {
            if (id === 'aurora') {
              setUserRole('sindico');
              setCurrentView('dashboard-gestao');
            }
          },
        }}
        onSearch={(query) => console.log('Search:', query)}
        onLogout={() => setCurrentView('context-selector')}
      >
        <PortalMorador />
      </MainLayout>
    );
  }

  // Admin/Sindico Views
  const getBreadcrumbs = () => {
    switch (currentView) {
      case 'dashboard-gestao':
        return [{ label: 'Dashboard' }];
      case 'gestao-condominio':
        return [
          { label: 'Dashboard', onClick: () => setCurrentView('dashboard-gestao') },
          { label: 'Gestão de Unidades' },
        ];
      case 'gestao-usuarios':
        return [
          { label: 'Dashboard', onClick: () => setCurrentView('dashboard-gestao') },
          { label: 'Gestão de Usuários' },
        ];
      case 'financeiro-cobrancas':
        return [
          { label: 'Dashboard', onClick: () => setCurrentView('dashboard-gestao') },
          { label: 'Financeiro' },
          { label: 'Cobranças' },
        ];
      case 'financeiro-despesas':
        return [
          { label: 'Dashboard', onClick: () => setCurrentView('dashboard-gestao') },
          { label: 'Financeiro' },
          { label: 'Despesas' },
        ];
      case 'operacional-chamados':
        return [
          { label: 'Dashboard', onClick: () => setCurrentView('dashboard-gestao') },
          { label: 'Chamados' },
          { label: '#1247' },
        ];
      case 'areas-comuns':
        return [
          { label: 'Dashboard', onClick: () => setCurrentView('dashboard-gestao') },
          { label: 'Áreas Comuns' },
        ];
      default:
        return [{ label: 'Dashboard' }];
    }
  };

  const renderContent = () => {
    switch (currentView) {
      case 'dashboard-gestao':
        return <DashboardGestao />;
      case 'gestao-condominio':
        return <GestaoCondominio />;
      case 'gestao-usuarios':
        return <GestaoUsuarios />;
      case 'financeiro-cobrancas':
        return <FinanceiroCobrancas />;
      case 'financeiro-despesas':
        return <FinanceiroDespesas />;
      case 'operacional-chamados':
        return <OperacionalChamados />;
      case 'areas-comuns':
        return <AreasComuns />;
      default:
        return <DashboardGestao />;
    }
  };

  // Custom menu items based on current view
  const menuItems = [
    {
      icon: LayoutDashboard,
      label: 'Dashboard',
      active: currentView === 'dashboard-gestao',
      onClick: () => setCurrentView('dashboard-gestao'),
    },
    {
      icon: Building2,
      label: 'Estrutura',
      active: currentView === 'gestao-condominio',
      onClick: () => setCurrentView('gestao-condominio'),
    },
    {
      icon: CreditCard,
      label: 'Cobranças',
      active: currentView === 'financeiro-cobrancas',
      onClick: () => setCurrentView('financeiro-cobrancas'),
      badge: 3,
    },
    {
      icon: Wallet,
      label: 'Despesas',
      active: currentView === 'financeiro-despesas',
      onClick: () => setCurrentView('financeiro-despesas'),
    },
    {
      icon: HomeIcon,
      label: 'Chamados',
      active: currentView === 'operacional-chamados',
      onClick: () => setCurrentView('operacional-chamados'),
      badge: 5,
    },
    {
      icon: Calendar,
      label: 'Áreas Comuns',
      active: currentView === 'areas-comuns',
      onClick: () => setCurrentView('areas-comuns'),
    },
    {
      icon: Users,
      label: 'Usuários',
      active: currentView === 'gestao-usuarios',
      onClick: () => setCurrentView('gestao-usuarios'),
    },
  ];

  return (
    <MainLayout
      breadcrumbs={getBreadcrumbs()}
      userProfile={{
        name: 'João Silva',
        role: userRole === 'admin' ? 'Administrador' : 'Síndico',
      }}
      activeCondominium={{
        name: 'Edifício Aurora',
        options: [
          { id: 'aurora', name: 'Edifício Aurora' },
          { id: 'sunset', name: 'Residencial Sunset (Morador)' },
          { id: 'vista-verde', name: 'Vista Verde (Morador)' },
        ],
        onChange: (id) => {
          if (id === 'sunset' || id === 'vista-verde') {
            setUserRole('morador');
            setCurrentView('portal-morador');
          }
        },
      }}
      menuItems={menuItems}
      onSearch={(query) => console.log('Search:', query)}
      onLogout={() => setCurrentView('context-selector')}
    >
      {renderContent()}
    </MainLayout>
  );
}
