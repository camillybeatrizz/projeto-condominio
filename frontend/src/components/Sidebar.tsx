import { NavLink } from 'react-router-dom';
import { 
  Building2, 
  LayoutDashboard, 
  Home, 
  CreditCard, 
  Wallet, 
  MessageSquare, 
  Headphones, 
  LogOut, 
  Users,
  Settings,
  ShieldCheck,
  FileText
} from 'lucide-react';
import { useAuth } from '../providers/AuthProvider';
import { Perfil } from '../types/api';

interface NavItem {
  icon: any;
  label: string;
  path: string;
  badge?: number;
}

export function Sidebar() {
  const { activeAcesso, logout } = useAuth();

  const getMenuItems = (): NavItem[] => {
    const perfil = activeAcesso?.perfil;

    if (perfil === Perfil.ADMIN) {
      return [
        { icon: LayoutDashboard, label: 'Dashboard', path: '/dashboard' },
        { icon: Building2, label: 'Condomínios', path: '/admin/condominios' },
        { icon: Users, label: 'Usuários', path: '/admin/usuarios' },
        { icon: ShieldCheck, label: 'Auditoria', path: '/admin/auditoria' },
        { icon: Settings, label: 'Configurações', path: '/admin/settings' },
      ];
    }

    if (perfil === Perfil.SINDICO) {
      return [
        { icon: LayoutDashboard, label: 'Dashboard', path: '/dashboard' },
        { icon: Building2, label: 'Estrutura', path: '/gestao/estrutura' },
        { icon: CreditCard, label: 'Cobranças', path: '/financeiro/cobrancas', badge: 3 },
        { icon: Wallet, label: 'Despesas', path: '/financeiro/despesas' },
        { icon: FileText, label: 'Contratos', path: '/gestao/contratos' },
        { icon: Headphones, label: 'Chamados', path: '/gestao/chamados', badge: 5 },
        { icon: MessageSquare, label: 'Comunicados', path: '/gestao/comunicados' },
        { icon: Users, label: 'Moradores', path: '/gestao/moradores' },
      ];
    }

    if (perfil === Perfil.MORADOR) {
      return [
        { icon: LayoutDashboard, label: 'Início', path: '/portal-morador' },
        { icon: CreditCard, label: 'Minhas Cobranças', path: '/morador/cobrancas' },
        { icon: Headphones, label: 'Meus Chamados', path: '/morador/chamados' },
        { icon: Building2, label: 'Áreas Comuns', path: '/morador/areas-comuns' },
        { icon: MessageSquare, label: 'Comunicados', path: '/morador/comunicados' },
      ];
    }

    return [];
  };

  const menuItems = getMenuItems();

  return (
    <aside className="w-64 bg-gradient-to-b from-kondo-purple-700 to-kondo-purple-800 text-white flex flex-col h-screen sticky top-0 flex-shrink-0 z-20">
      {/* Logo */}
      <div className="p-6 border-b border-kondo-purple-600/50">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-white/20 backdrop-blur-md rounded-xl flex items-center justify-center shadow-lg border border-white/10">
            <Building2 className="w-6 h-6 text-white" strokeWidth={2.5} />
          </div>
          <div>
            <h1 className="text-xl font-bold tracking-tight">KONDO</h1>
            <p className="text-[10px] text-kondo-purple-200 uppercase font-bold tracking-widest">
              Management
            </p>
          </div>
        </div>
      </div>

      {/* Navigation */}
      <nav className="flex-1 p-4 space-y-1 overflow-y-auto scrollbar-hide">
        {menuItems.map((item, index) => (
          <NavLink
            key={index}
            to={item.path}
            className={({ isActive }) => `
              group flex items-center gap-3 px-4 py-3 rounded-xl
              text-sm font-medium transition-all duration-200
              ${isActive 
                ? 'bg-white text-kondo-purple-700 shadow-xl' 
                : 'text-white/80 hover:bg-white/10 hover:text-white'}
            `}
          >
            <item.icon className={`w-5 h-5 flex-shrink-0 transition-transform group-hover:scale-110`} strokeWidth={2} />
            <span className="flex-1">{item.label}</span>
            {item.badge && (
              <span className={`
                px-2 py-0.5 rounded-full text-[10px] font-bold shadow-sm
                ${index % 2 === 0 ? 'bg-kondo-orange-500 text-white' : 'bg-white text-kondo-purple-700'}
              `}>
                {item.badge}
              </span>
            )}
          </NavLink>
        ))}
      </nav>

      {/* Footer / Logout */}
      <div className="p-4 border-t border-kondo-purple-600/50">
        <button
          onClick={logout}
          className="w-full flex items-center gap-3 px-4 py-3 rounded-xl text-sm font-medium text-white/80 hover:bg-white/10 hover:text-white hover:text-kondo-red-200 transition-all duration-200 group"
        >
          <LogOut className="w-5 h-5 group-hover:-translate-x-1 transition-transform" strokeWidth={2} />
          <span>Sair do Sistema</span>
        </button>
      </div>
    </aside>
  );
}
