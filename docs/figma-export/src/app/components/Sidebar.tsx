import { ReactNode } from 'react';
import { Building2, LayoutDashboard, Home, CreditCard, Wallet, MessageSquare, Headphones, LogOut, LucideIcon, Users } from 'lucide-react';

export interface MenuItem {
  icon: LucideIcon;
  label: string;
  active?: boolean;
  onClick?: () => void;
  badge?: number;
}

interface SidebarProps {
  menuItems?: MenuItem[];
  onLogout?: () => void;
}

const defaultMenuItems: MenuItem[] = [
  { icon: LayoutDashboard, label: 'Dashboard', active: true },
  { icon: Building2, label: 'Estrutura' },
  { icon: CreditCard, label: 'Cobranças', badge: 3 },
  { icon: Wallet, label: 'Pagamentos' },
  { icon: MessageSquare, label: 'Comunicados', badge: 2 },
  { icon: Home, label: 'Chamados', badge: 5 },
  { icon: Users, label: 'Usuários' },
];

export function Sidebar({ menuItems = defaultMenuItems, onLogout }: SidebarProps) {
  return (
    <aside className="w-64 bg-gradient-to-b from-kondo-purple-700 to-kondo-purple-800 text-white flex flex-col h-screen sticky top-0">
      {/* Logo */}
      <div className="p-6 border-b border-kondo-purple-600">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-white/20 backdrop-blur-sm rounded-lg flex items-center justify-center">
            <Building2 className="w-6 h-6 text-white" strokeWidth={2.5} />
          </div>
          <div>
            <h1 className="text-xl font-bold">KONDO</h1>
            <p className="text-xs text-kondo-purple-200">Gestão de Condomínios</p>
          </div>
        </div>
      </div>

      {/* Navigation */}
      <nav className="flex-1 p-4 space-y-1 overflow-y-auto">
        {menuItems.map((item, index) => (
          <button
            key={index}
            onClick={item.onClick}
            className={`
              w-full flex items-center gap-3 px-4 py-3 rounded-lg
              text-sm font-medium transition-all duration-200
              ${
                item.active
                  ? 'bg-white text-kondo-purple-700 shadow-sm'
                  : 'text-white/90 hover:bg-white/10 hover:text-white'
              }
            `}
          >
            <item.icon className="w-5 h-5 flex-shrink-0" strokeWidth={2} />
            <span className="flex-1 text-left">{item.label}</span>
            {item.badge && (
              <span className="px-2 py-0.5 bg-kondo-orange-500 text-white text-xs font-semibold rounded-full">
                {item.badge}
              </span>
            )}
          </button>
        ))}
      </nav>

      {/* Logout */}
      <div className="p-4 border-t border-kondo-purple-600">
        <button
          onClick={onLogout}
          className="w-full flex items-center gap-3 px-4 py-3 rounded-lg text-sm font-medium text-white/90 hover:bg-white/10 hover:text-white transition-all duration-200"
        >
          <LogOut className="w-5 h-5" strokeWidth={2} />
          <span>Sair</span>
        </button>
      </div>
    </aside>
  );
}
