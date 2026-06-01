import { ReactNode } from 'react';
import { TrendingUp, TrendingDown, LucideIcon } from 'lucide-react';

interface BaseCardProps {
  className?: string;
  children: ReactNode;
}

export function Card({ className = '', children }: BaseCardProps) {
  return (
    <div className={`bg-white rounded-xl shadow-sm border border-kondo-gray-200 ${className}`}>
      {children}
    </div>
  );
}

interface FinancialCardProps {
  label: string;
  value: string;
  icon: LucideIcon;
  iconColor?: string;
  trend?: {
    value: string;
    isPositive: boolean;
  };
  subtitle?: string;
}

export function FinancialCard({
  label,
  value,
  icon: Icon,
  iconColor = 'bg-kondo-purple-100 text-kondo-purple-600',
  trend,
  subtitle,
}: FinancialCardProps) {
  return (
    <Card className="p-6">
      <div className="flex items-start justify-between mb-4">
        <div className={`w-12 h-12 ${iconColor} rounded-lg flex items-center justify-center`}>
          <Icon className="w-6 h-6" strokeWidth={2} />
        </div>
        {trend && (
          <div className={`flex items-center gap-1 text-sm font-medium ${
            trend.isPositive ? 'text-kondo-green-600' : 'text-kondo-red-600'
          }`}>
            {trend.isPositive ? (
              <TrendingUp className="w-4 h-4" />
            ) : (
              <TrendingDown className="w-4 h-4" />
            )}
            {trend.value}
          </div>
        )}
      </div>
      <h3 className="text-sm font-medium text-kondo-gray-600 mb-1">{label}</h3>
      <p className="text-2xl font-bold text-kondo-gray-900 mb-1">{value}</p>
      {subtitle && <p className="text-sm text-kondo-gray-500">{subtitle}</p>}
    </Card>
  );
}

interface StatusCardProps {
  id: string;
  title: string;
  description: string;
  status: {
    label: string;
    color: 'green' | 'orange' | 'red' | 'purple' | 'teal' | 'gray';
  };
  category?: {
    label: string;
    color: string;
  };
  metadata?: {
    label: string;
    value: string;
  }[];
  date?: string;
}

export function StatusCard({ id, title, description, status, category, metadata, date }: StatusCardProps) {
  const statusColors = {
    green: 'bg-kondo-green-100 text-kondo-green-700',
    orange: 'bg-kondo-orange-100 text-kondo-orange-700',
    red: 'bg-kondo-red-100 text-kondo-red-700',
    purple: 'bg-kondo-purple-100 text-kondo-purple-700',
    teal: 'bg-kondo-teal-100 text-kondo-teal-700',
    gray: 'bg-kondo-gray-100 text-kondo-gray-700',
  };

  return (
    <Card className="p-6 hover:shadow-md transition-shadow">
      <div className="flex items-start justify-between mb-3">
        <div className="flex items-center gap-2">
          <span className="text-sm font-medium text-kondo-gray-500">#{id}</span>
          {category && (
            <span className={`px-2 py-0.5 ${category.color} rounded text-xs font-medium`}>
              {category.label}
            </span>
          )}
        </div>
        <span className={`px-3 py-1 ${statusColors[status.color]} rounded-full text-sm font-medium`}>
          {status.label}
        </span>
      </div>

      <h3 className="text-lg font-semibold text-kondo-gray-900 mb-2">{title}</h3>
      <p className="text-sm text-kondo-gray-600 mb-4">{description}</p>

      {metadata && metadata.length > 0 && (
        <div className="flex items-center gap-4 text-xs text-kondo-gray-500 border-t border-kondo-gray-100 pt-3">
          {metadata.map((item, index) => (
            <div key={index}>
              <span className="font-medium">{item.label}:</span> {item.value}
            </div>
          ))}
          {date && <div className="ml-auto">{date}</div>}
        </div>
      )}
    </Card>
  );
}

interface UnitCardProps {
  unit: string;
  block: string;
  owner: string;
  type: string;
  status: 'occupied' | 'vacant' | 'rented';
  phone?: string;
  email?: string;
  onEdit?: () => void;
  onView?: () => void;
}

export function UnitCard({ unit, block, owner, type, status, phone, email, onEdit, onView }: UnitCardProps) {
  const statusConfig = {
    occupied: { label: 'Ocupado', color: 'bg-kondo-teal-100 text-kondo-teal-700' },
    vacant: { label: 'Vago', color: 'bg-kondo-gray-100 text-kondo-gray-700' },
    rented: { label: 'Alugado', color: 'bg-kondo-purple-100 text-kondo-purple-700' },
  };

  return (
    <Card className="p-6 hover:shadow-md transition-shadow">
      <div className="flex items-start justify-between mb-4">
        <div>
          <div className="flex items-baseline gap-2 mb-1">
            <h3 className="text-xl font-bold text-kondo-gray-900">{unit}</h3>
            <span className="text-sm text-kondo-gray-500">Bloco {block}</span>
          </div>
          <p className="text-sm text-kondo-gray-600">{type}</p>
        </div>
        <span className={`px-3 py-1 ${statusConfig[status].color} rounded-full text-sm font-medium`}>
          {statusConfig[status].label}
        </span>
      </div>

      <div className="space-y-2 mb-4">
        <div>
          <p className="text-xs text-kondo-gray-500">Proprietário</p>
          <p className="text-sm font-medium text-kondo-gray-900">{owner || '—'}</p>
        </div>
        {phone && (
          <div>
            <p className="text-xs text-kondo-gray-500">Telefone</p>
            <p className="text-sm text-kondo-gray-700">{phone}</p>
          </div>
        )}
        {email && (
          <div>
            <p className="text-xs text-kondo-gray-500">Email</p>
            <p className="text-sm text-kondo-gray-700">{email}</p>
          </div>
        )}
      </div>

      {(onView || onEdit) && (
        <div className="flex gap-2 pt-4 border-t border-kondo-gray-100">
          {onView && (
            <button
              onClick={onView}
              className="flex-1 px-4 py-2 text-sm font-medium text-kondo-purple-600 bg-kondo-purple-50 rounded-lg hover:bg-kondo-purple-100 transition-colors"
            >
              Visualizar
            </button>
          )}
          {onEdit && (
            <button
              onClick={onEdit}
              className="flex-1 px-4 py-2 text-sm font-medium text-kondo-gray-700 bg-kondo-gray-100 rounded-lg hover:bg-kondo-gray-200 transition-colors"
            >
              Editar
            </button>
          )}
        </div>
      )}
    </Card>
  );
}
