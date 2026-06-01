import { ChevronRight, Search, ChevronDown, Building2 } from 'lucide-react';

interface Breadcrumb {
  label: string;
  onClick?: () => void;
}

interface HeaderProps {
  breadcrumbs?: Breadcrumb[];
  userProfile?: {
    name: string;
    role: string;
    avatar?: string;
  };
  activeCondominium?: {
    name: string;
    options?: { id: string; name: string }[];
    onChange?: (id: string) => void;
  };
  onSearch?: (query: string) => void;
}

export function Header({
  breadcrumbs = [{ label: 'Dashboard' }],
  userProfile = {
    name: 'João Silva',
    role: 'Síndico',
  },
  activeCondominium,
  onSearch,
}: HeaderProps) {
  return (
    <header className="bg-white border-b border-kondo-gray-200 sticky top-0 z-40">
      <div className="px-6 py-4">
        <div className="flex items-center justify-between gap-6">
          {/* Left: Breadcrumbs */}
          <div className="flex items-center gap-2 text-sm">
            {breadcrumbs.map((crumb, index) => (
              <div key={index} className="flex items-center gap-2">
                {index > 0 && <ChevronRight className="w-4 h-4 text-kondo-gray-400" />}
                {crumb.onClick ? (
                  <button
                    onClick={crumb.onClick}
                    className="text-kondo-gray-600 hover:text-kondo-purple-600 font-medium transition-colors"
                  >
                    {crumb.label}
                  </button>
                ) : (
                  <span
                    className={
                      index === breadcrumbs.length - 1
                        ? 'text-kondo-gray-900 font-semibold'
                        : 'text-kondo-gray-600'
                    }
                  >
                    {crumb.label}
                  </span>
                )}
              </div>
            ))}
          </div>

          {/* Center: Search Bar */}
          {onSearch && (
            <div className="flex-1 max-w-md">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-kondo-gray-400" />
                <input
                  type="search"
                  placeholder="Buscar..."
                  className="w-full pl-10 pr-4 py-2 bg-kondo-gray-50 border border-kondo-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-kondo-purple-600 focus:border-transparent transition-all"
                  onChange={(e) => onSearch(e.target.value)}
                />
              </div>
            </div>
          )}

          {/* Right: Condominium Selector & User Profile */}
          <div className="flex items-center gap-4">
            {/* Condominium Selector */}
            {activeCondominium && (
              <div className="flex items-center gap-2 px-3 py-2 bg-kondo-gray-50 rounded-lg border border-kondo-gray-200">
                <Building2 className="w-4 h-4 text-kondo-purple-600" />
                <select
                  className="bg-transparent border-none text-sm font-medium text-kondo-gray-900 focus:outline-none cursor-pointer"
                  defaultValue={activeCondominium.options?.[0]?.id}
                  onChange={(e) => activeCondominium.onChange?.(e.target.value)}
                >
                  {activeCondominium.options?.map((option) => (
                    <option key={option.id} value={option.id}>
                      {option.name}
                    </option>
                  ))}
                </select>
              </div>
            )}

            {/* User Profile */}
            <div className="flex items-center gap-3">
              <div className="text-right">
                <p className="text-sm font-medium text-kondo-gray-900">{userProfile.name}</p>
                <div className="flex items-center gap-1.5 justify-end">
                  <span className="px-2 py-0.5 bg-kondo-purple-100 text-kondo-purple-700 text-xs font-medium rounded">
                    {userProfile.role}
                  </span>
                </div>
              </div>
              <button className="flex items-center gap-2 hover:bg-kondo-gray-50 p-2 rounded-lg transition-colors">
                <div className="w-10 h-10 bg-gradient-to-br from-kondo-purple-600 to-kondo-purple-700 rounded-full flex items-center justify-center text-white font-semibold text-sm">
                  {userProfile.avatar || userProfile.name.charAt(0)}
                </div>
                <ChevronDown className="w-4 h-4 text-kondo-gray-400" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
}
