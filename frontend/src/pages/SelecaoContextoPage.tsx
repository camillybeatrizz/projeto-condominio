import { useNavigate } from 'react-router-dom';
import { useAuth } from '../providers/AuthProvider';
import { ContextSelector, ContextOption } from '../components/ContextSelector';
import { Perfil, MeuAcesso } from '../types/api';

export function SelecaoContextoPage() {
  const { user, selectAcesso, logout } = useAuth();
  const navigate = useNavigate();

  if (!user) return null;

  const handleSelect = (acesso: MeuAcesso) => {
    selectAcesso(acesso);
    // Redirecionar baseado no perfil
    if (acesso.perfil === Perfil.MORADOR) {
      navigate('/portal-morador');
    } else {
      navigate('/dashboard');
    }
  };

  const contexts: ContextOption[] = user.acessos.map((acesso) => ({
    id: acesso.acessoId,
    title: acesso.perfil === Perfil.ADMIN ? 'Administração Global' : acesso.condominioNome,
    subtitle: acesso.perfil === Perfil.ADMIN 
      ? 'Gestão de todos os condomínios' 
      : `Gestão do condomínio ${acesso.condominioNome}`,
    role: acesso.perfil,
    block: acesso.unidadeId ? acesso.unidadeNumero?.split(' ')[0] : undefined, // Simplificação para o mock
    unit: acesso.unidadeNumero,
    onClick: () => handleSelect(acesso),
  }));

  return (
    <ContextSelector
      userName={user.nome}
      contexts={contexts}
      onLogout={logout}
    />
  );
}
