import { Bell, Search, User, ChevronDown, LogOut } from 'lucide-react';
import { useAuth } from '../providers/AuthProvider';
import { Perfil } from '../types/api';

interface HeaderProps {
  breadcrumbs?: { label: string; onClick?: () => void }[];
}

export function Header({ breadcrumbs }: HeaderProps) {
  const { user, activeAcesso, logout } = useAuth();

  const getPerfilLabel = (perfil?: Perfil) => {
    switch (perfil) {
      case Perfil.ADMIN: return 'Administrador';
      case Perfil.SINDICO: return 'Síndico';
      case Perfil.MORADOR: return 'Morador';
      default: return '';
    }
  };

  const getPerfilColor = (perfil?: Perfil) => {
    switch (perfil) {
      case Perfil.ADMIN: return 'bg-kondo-purple-100 text-kondo-purple-700';
      case Perfil.SINDICO: return 'bg-kondo-orange-100 text-kondo-orange-700';
      case Perfil.MORADOR: return 'bg-kondo-teal-100 text-kondo-teal-700';
      default: return 'bg-kondo-gray-100 text-kondo-gray-700';
    }
  };

  return (
    <header className="h-20 bg-white border-b border-kondo-gray-200 flex items-center justify-between px-8 sticky top-0 z-10">
      {/* Left: Breadcrumbs & Search */}
      <div className="flex items-center gap-8 flex-1">
        {breadcrumbs && (
          <nav className="hidden lg:flex items-center gap-2 text-sm">
            {breadcrumbs.map((crumb, index) => (
              <div key={index} className="flex items-center gap-2">
                {index > 0 && <span className="text-kondo-gray-400">/</span>}
                <button
                  onClick={crumb.onClick}
                  disabled={!crumb.onClick}
                  className={`${crumb.onClick ? 'text-kondo-gray-600 hover:text-kondo-purple-600' : 'text-kondo-gray-900 font-semibold'} transition-colors`}
                >
                  {crumb.label}
                </button>
              </div>
            ))}
          </nav>
        )}

        <div className="max-w-md w-full relative group hidden md:block">
          <Search className="w-5 h-5 text-kondo-gray-400 absolute left-3 top-1/2 -translate-y-1/2 group-focus-within:text-kondo-purple-500 transition-colors" />
          <input
            type="text"
            placeholder="Pesquisar..."
            className="w-full bg-kondo-gray-50 border-transparent focus:bg-white focus:border-kondo-purple-300 rounded-lg py-2.5 pl-10 pr-4 text-sm transition-all outline-none"
          />
        </div>
      </div>

      {/* Right: User Profile & Actions */}
      <div className="flex items-center gap-6">
        <button className="relative p-2 text-kondo-gray-500 hover:text-kondo-purple-600 hover:bg-kondo-purple-50 rounded-lg transition-all">
          <Bell className="w-6 h-6" />
          <span className="absolute top-1.5 right-1.5 w-2.5 h-2.5 bg-kondo-red-500 border-2 border-white rounded-full"></span>
        </button>

        <div className="h-10 w-px bg-kondo-gray-200 hidden sm:block"></div>

        <div className="flex items-center gap-4">
          <div className="text-right hidden sm:block">
            <p className="text-sm font-bold text-kondo-gray-900 leading-tight">
              {user?.nome}
            </p>
            <div className="flex items-center justify-end gap-2 mt-0.5">
              <span className={`text-[10px] uppercase font-bold px-1.5 py-0.5 rounded ${getPerfilColor(activeAcesso?.perfil)}`}>
                {getPerfilLabel(activeAcesso?.perfil)}
              </span>
              <p className="text-[11px] text-kondo-gray-500 font-medium truncate max-w-[120px]">
                {activeAcesso?.condominioNome || 'Gestão Global'}
              </p>
            </div>
          </div>

          <div className="relative group">
            <button className="flex items-center gap-2 p-1 hover:bg-kondo-gray-50 rounded-lg transition-all">
              <div className="w-10 h-10 bg-gradient-to-br from-kondo-purple-500 to-kondo-purple-700 rounded-lg flex items-center justify-center text-white font-bold shadow-md">
                {user?.nome.charAt(0)}
              </div>
              <ChevronDown className="w-4 h-4 text-kondo-gray-400 group-hover:text-kondo-gray-600 transition-all" />
            </button>

            {/* Simple Dropdown Mock */}
            <div className="absolute right-0 mt-2 w-48 bg-white border border-kondo-gray-200 rounded-xl shadow-xl py-2 opacity-0 invisible group-hover:opacity-100 group-hover:visible transition-all duration-200 z-20">
              <button className="w-full px-4 py-2 text-left text-sm text-kondo-gray-700 hover:bg-kondo-gray-50 flex items-center gap-2 transition-colors">
                <User className="w-4 h-4" /> Perfil
              </button>
              <button 
                onClick={logout}
                className="w-full px-4 py-2 text-left text-sm text-kondo-red-600 hover:bg-kondo-red-50 flex items-center gap-2 transition-colors border-t border-kondo-gray-100 mt-1"
              >
                <LogOut className="w-4 h-4" /> Sair
              </button>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
}
