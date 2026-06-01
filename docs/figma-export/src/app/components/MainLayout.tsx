import { ReactNode } from 'react';
import { Sidebar, MenuItem } from './Sidebar';
import { Header } from './Header';

interface MainLayoutProps {
  children: ReactNode;
  breadcrumbs?: { label: string; onClick?: () => void }[];
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
  menuItems?: MenuItem[];
  onSearch?: (query: string) => void;
  onLogout?: () => void;
}

export function MainLayout({
  children,
  breadcrumbs,
  userProfile,
  activeCondominium,
  menuItems,
  onSearch,
  onLogout,
}: MainLayoutProps) {
  return (
    <div className="flex h-screen bg-kondo-gray-50">
      {/* Sidebar */}
      <Sidebar menuItems={menuItems} onLogout={onLogout} />

      {/* Main Content */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Header */}
        <Header
          breadcrumbs={breadcrumbs}
          userProfile={userProfile}
          activeCondominium={activeCondominium}
          onSearch={onSearch}
        />

        {/* Page Content */}
        <main className="flex-1 overflow-y-auto">
          {children}
        </main>
      </div>
    </div>
  );
}
