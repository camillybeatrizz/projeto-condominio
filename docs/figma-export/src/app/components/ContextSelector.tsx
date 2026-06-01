import { Building2, ChevronRight, Shield, User, Users } from 'lucide-react';

interface ContextOption {
  id: string;
  title: string;
  subtitle: string;
  role: 'admin' | 'sindico' | 'morador';
  block?: string;
  unit?: string;
  onClick?: () => void;
}

interface ContextSelectorProps {
  userName?: string;
  contexts: ContextOption[];
  onLogout?: () => void;
}

const roleConfig = {
  admin: {
    icon: Shield,
    label: 'Administrador',
    color: 'from-kondo-purple-600 to-kondo-purple-700',
    bgColor: 'bg-kondo-purple-100',
    textColor: 'text-kondo-purple-700',
  },
  sindico: {
    icon: Building2,
    label: 'Síndico',
    color: 'from-kondo-orange-600 to-kondo-orange-700',
    bgColor: 'bg-kondo-orange-100',
    textColor: 'text-kondo-orange-700',
  },
  morador: {
    icon: User,
    label: 'Morador',
    color: 'from-kondo-teal-600 to-kondo-teal-700',
    bgColor: 'bg-kondo-teal-100',
    textColor: 'text-kondo-teal-700',
  },
};

export function ContextSelector({
  userName = 'João Silva',
  contexts,
  onLogout,
}: ContextSelectorProps) {
  return (
    <div className="min-h-screen bg-gradient-to-br from-kondo-gray-50 to-kondo-purple-50/30 flex items-center justify-center p-6">
      <div className="w-full max-w-4xl">
        {/* Header */}
        <div className="text-center mb-12">
          <div className="w-20 h-20 bg-gradient-to-br from-kondo-purple-600 to-kondo-purple-700 rounded-2xl flex items-center justify-center mx-auto mb-6 shadow-xl">
            <Building2 className="w-11 h-11 text-white" strokeWidth={2.5} />
          </div>
          <h1 className="text-4xl font-bold text-kondo-gray-900 mb-2">
            Bem-vindo ao KONDO
          </h1>
          <p className="text-lg text-kondo-gray-600">
            Olá, <span className="font-semibold text-kondo-gray-900">{userName}</span>
          </p>
          <p className="text-kondo-gray-500 mt-1">
            Selecione o condomínio que deseja acessar
          </p>
        </div>

        {/* Context Cards */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
          {contexts.map((context) => {
            const config = roleConfig[context.role];
            const Icon = config.icon;

            return (
              <button
                key={context.id}
                onClick={context.onClick}
                className="bg-white rounded-2xl p-8 shadow-sm border border-kondo-gray-200 hover:shadow-xl hover:border-kondo-purple-300 transition-all duration-300 text-left group"
              >
                <div className="flex items-start justify-between mb-6">
                  <div className={`w-16 h-16 bg-gradient-to-br ${config.color} rounded-xl flex items-center justify-center shadow-lg`}>
                    <Icon className="w-9 h-9 text-white" strokeWidth={2} />
                  </div>
                  <ChevronRight className="w-6 h-6 text-kondo-gray-400 group-hover:text-kondo-purple-600 group-hover:translate-x-1 transition-all" />
                </div>

                <div className="mb-4">
                  <h2 className="text-2xl font-bold text-kondo-gray-900 mb-2">
                    {context.title}
                  </h2>
                  <p className="text-kondo-gray-600">{context.subtitle}</p>
                </div>

                <div className="flex items-center gap-2 flex-wrap">
                  <span className={`px-3 py-1 ${config.bgColor} ${config.textColor} rounded-full text-sm font-medium`}>
                    {config.label}
                  </span>
                  {context.block && (
                    <span className="px-3 py-1 bg-kondo-gray-100 text-kondo-gray-700 rounded-full text-sm font-medium">
                      Bloco {context.block}
                    </span>
                  )}
                  {context.unit && (
                    <span className="px-3 py-1 bg-kondo-gray-100 text-kondo-gray-700 rounded-full text-sm font-medium">
                      Apt {context.unit}
                    </span>
                  )}
                </div>
              </button>
            );
          })}
        </div>

        {/* Info Card */}
        <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
          <div className="flex items-start gap-4">
            <div className="w-12 h-12 bg-kondo-teal-100 rounded-lg flex items-center justify-center flex-shrink-0">
              <Users className="w-6 h-6 text-kondo-teal-600" />
            </div>
            <div className="flex-1">
              <h3 className="font-semibold text-kondo-gray-900 mb-1">
                Você possui {contexts.length} perfis de acesso
              </h3>
              <p className="text-sm text-kondo-gray-600">
                Contextos ordenados por nível de permissão. Você pode trocar de perfil a qualquer momento através do menu lateral.
              </p>
            </div>
          </div>
        </div>

        {/* Logout */}
        {onLogout && (
          <div className="text-center mt-8">
            <button
              onClick={onLogout}
              className="text-kondo-gray-600 hover:text-kondo-gray-900 text-sm font-medium transition-colors"
            >
              Não é você? Fazer logout
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
