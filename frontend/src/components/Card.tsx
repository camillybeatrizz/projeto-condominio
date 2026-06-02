import { ReactNode } from 'react';
import { LucideIcon, TrendingUp, TrendingDown } from 'lucide-react';

interface CardProps {
  children: ReactNode;
  className?: string;
}

export function Card({ children, className = '' }: CardProps) {
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
  isLoading?: boolean;
}

export function FinancialCard({
  label,
  value,
  icon: Icon,
  iconColor = 'bg-kondo-purple-100 text-kondo-purple-600',
  trend,
  subtitle,
  isLoading
}: FinancialCardProps) {
  if (isLoading) {
    return (
      <Card className="p-6">
        <div className="animate-pulse space-y-4">
          <div className="flex justify-between">
            <div className="w-12 h-12 bg-kondo-gray-100 rounded-lg" />
            <div className="w-16 h-4 bg-kondo-gray-100 rounded" />
          </div>
          <div className="space-y-2">
            <div className="w-24 h-4 bg-kondo-gray-100 rounded" />
            <div className="w-32 h-8 bg-kondo-gray-100 rounded" />
          </div>
        </div>
      </Card>
    );
  }

  return (
    <Card className="p-6">
      <div className="flex items-start justify-between mb-4">
        <div className={`w-12 h-12 ${iconColor} rounded-lg flex items-center justify-center shadow-sm`}>
          <Icon className="w-6 h-6" strokeWidth={2.5} />
        </div>
        {trend && (
          <div className={`flex items-center gap-1 text-xs font-bold px-2 py-1 rounded-full ${
            trend.isPositive ? 'bg-kondo-green-50 text-kondo-green-600' : 'bg-kondo-red-50 text-kondo-red-600'
          }`}>
            {trend.isPositive ? <TrendingUp className="w-3 h-3" /> : <TrendingDown className="w-3 h-3" />}
            {trend.value}
          </div>
        )}
      </div>
      <div>
        <h3 className="text-sm font-medium text-kondo-gray-500 mb-1">{label}</h3>
        <p className="text-2xl font-bold text-kondo-gray-900 tracking-tight">{value}</p>
        {subtitle && <p className="text-xs text-kondo-gray-400 mt-1 font-medium">{subtitle}</p>}
      </div>
    </Card>
  );
}
