import { ReactNode } from 'react';
import { Sidebar } from './Sidebar';
import { Header } from './Header';

interface MainLayoutProps {
  children: ReactNode;
  breadcrumbs?: { label: string; onClick?: () => void }[];
}

export function MainLayout({ children, breadcrumbs }: MainLayoutProps) {
  return (
    <div className="flex min-h-screen bg-kondo-gray-50">
      {/* Barra Lateral fixa */}
      <Sidebar />

      <div className="flex-1 flex flex-col min-w-0">
        {/* Cabeçalho fixo no topo */}
        <Header breadcrumbs={breadcrumbs} />

        {/* Área de Conteúdo principal com scroll */}
        <main className="flex-1 p-8 overflow-y-auto">
          <div className="max-w-7xl mx-auto animate-in fade-in slide-in-from-bottom-4 duration-500">
            {children}
          </div>
        </main>
      </div>
    </div>
  );
}
