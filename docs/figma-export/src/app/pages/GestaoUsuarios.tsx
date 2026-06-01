import { Plus, Search, Filter, Mail, Phone, Shield, Building2, User, MoreVertical } from 'lucide-react';
import { useState } from 'react';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import { Select } from '../components/Select';
import { DataTable, Column } from '../components/DataTable';

interface UserData {
  id: string;
  name: string;
  email: string;
  phone: string;
  role: 'admin' | 'sindico' | 'morador' | 'funcionario';
  status: 'active' | 'inactive';
  unit?: string;
  block?: string;
  joinDate: string;
}

const usersData: UserData[] = [
  { id: '1', name: 'João Silva', email: 'joao.silva@email.com', phone: '(83) 98999-1111', role: 'admin', status: 'active', joinDate: '15/01/2024' },
  { id: '2', name: 'Maria Santos', email: 'maria.santos@email.com', phone: '(83) 98999-2222', role: 'sindico', status: 'active', unit: '504', block: 'B', joinDate: '20/02/2024' },
  { id: '3', name: 'Pedro Almeida', email: 'pedro.almeida@email.com', phone: '(83) 98999-3333', role: 'morador', status: 'active', unit: '101', block: 'A', joinDate: '10/03/2024' },
  { id: '4', name: 'Lucia Ferreira', email: 'lucia.ferreira@email.com', phone: '(83) 98999-4444', role: 'morador', status: 'active', unit: '102', block: 'A', joinDate: '15/03/2024' },
  { id: '5', name: 'Roberto Souza', email: 'roberto.souza@email.com', phone: '(83) 98999-5555', role: 'morador', status: 'active', unit: '201', block: 'A', joinDate: '20/03/2024' },
  { id: '6', name: 'Carlos Oliveira', email: 'carlos.oliveira@email.com', phone: '(83) 98999-6666', role: 'funcionario', status: 'active', joinDate: '01/04/2024' },
  { id: '7', name: 'Sandra Dias', email: 'sandra.dias@email.com', phone: '(83) 98999-7777', role: 'morador', status: 'active', unit: '302', block: 'A', joinDate: '10/04/2024' },
  { id: '8', name: 'Marcos Paulo', email: 'marcos.paulo@email.com', phone: '(83) 98999-8888', role: 'morador', status: 'inactive', unit: '401', block: 'A', joinDate: '15/04/2024' },
  { id: '9', name: 'Fernanda Lima', email: 'fernanda.lima@email.com', phone: '(83) 98999-9999', role: 'morador', status: 'active', unit: '202', block: 'A', joinDate: '20/04/2024' },
  { id: '10', name: 'Antonio Carlos', email: 'antonio.carlos@email.com', phone: '(83) 98888-1111', role: 'funcionario', status: 'active', joinDate: '01/05/2024' },
];

export function GestaoUsuarios() {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedRole, setSelectedRole] = useState('all');
  const [selectedStatus, setSelectedStatus] = useState('all');
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 8;

  const filteredUsers = usersData.filter((user) => {
    const matchesSearch =
      user.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      user.email.toLowerCase().includes(searchQuery.toLowerCase());

    const matchesRole = selectedRole === 'all' || user.role === selectedRole;
    const matchesStatus = selectedStatus === 'all' || user.status === selectedStatus;

    return matchesSearch && matchesRole && matchesStatus;
  });

  const totalPages = Math.ceil(filteredUsers.length / pageSize);
  const paginatedUsers = filteredUsers.slice(
    (currentPage - 1) * pageSize,
    currentPage * pageSize
  );

  const roleConfig = {
    admin: { label: 'Administrador', color: 'bg-kondo-purple-100 text-kondo-purple-700', icon: Shield },
    sindico: { label: 'Síndico', color: 'bg-kondo-orange-100 text-kondo-orange-700', icon: Building2 },
    morador: { label: 'Morador', color: 'bg-kondo-teal-100 text-kondo-teal-700', icon: User },
    funcionario: { label: 'Funcionário', color: 'bg-kondo-gray-100 text-kondo-gray-700', icon: User },
  };

  const stats = {
    total: usersData.length,
    active: usersData.filter(u => u.status === 'active').length,
    admins: usersData.filter(u => u.role === 'admin').length,
    moradores: usersData.filter(u => u.role === 'morador').length,
  };

  const columns: Column<UserData>[] = [
    {
      key: 'name',
      label: 'Nome',
      sortable: true,
      width: '20%',
      render: (row) => (
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-gradient-to-br from-kondo-purple-600 to-kondo-purple-700 rounded-full flex items-center justify-center text-white font-semibold text-sm flex-shrink-0">
            {row.name.charAt(0)}
          </div>
          <div>
            <p className="font-medium text-kondo-gray-900">{row.name}</p>
            {row.unit && (
              <p className="text-xs text-kondo-gray-500">Bloco {row.block} - Apt {row.unit}</p>
            )}
          </div>
        </div>
      ),
    },
    {
      key: 'email',
      label: 'Email',
      width: '20%',
      render: (row) => (
        <div className="flex items-center gap-2 text-sm text-kondo-gray-700">
          <Mail className="w-4 h-4 text-kondo-gray-400" />
          {row.email}
        </div>
      ),
    },
    {
      key: 'phone',
      label: 'Telefone',
      width: '15%',
      render: (row) => (
        <div className="flex items-center gap-2 text-sm text-kondo-gray-700">
          <Phone className="w-4 h-4 text-kondo-gray-400" />
          {row.phone}
        </div>
      ),
    },
    {
      key: 'role',
      label: 'Perfil',
      width: '15%',
      render: (row) => {
        const config = roleConfig[row.role];
        const Icon = config.icon;
        return (
          <div className="flex items-center gap-2">
            <span className={`px-3 py-1 ${config.color} rounded-full text-xs font-medium inline-flex items-center gap-1.5`}>
              <Icon className="w-3.5 h-3.5" />
              {config.label}
            </span>
          </div>
        );
      },
    },
    {
      key: 'status',
      label: 'Status',
      width: '12%',
      render: (row) => (
        <span className={`px-3 py-1 rounded-full text-xs font-medium ${
          row.status === 'active'
            ? 'bg-kondo-green-100 text-kondo-green-700'
            : 'bg-kondo-gray-100 text-kondo-gray-600'
        }`}>
          {row.status === 'active' ? 'Ativo' : 'Inativo'}
        </span>
      ),
    },
    {
      key: 'joinDate',
      label: 'Data de Cadastro',
      sortable: true,
      width: '13%',
    },
  ];

  return (
    <div className="p-8 space-y-8">
      {/* Page Header */}
      <div className="flex items-start justify-between">
        <div>
          <h1 className="text-3xl font-bold text-kondo-gray-900 mb-2">Gestão de Usuários</h1>
          <p className="text-kondo-gray-600">Gerencie moradores, funcionários e administradores</p>
        </div>
        <Button variant="primary" size="lg">
          <Plus className="w-5 h-5 mr-2" />
          Adicionar Usuário
        </Button>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-kondo-purple-100 rounded-lg flex items-center justify-center">
              <User className="w-5 h-5 text-kondo-purple-600" />
            </div>
            <div>
              <p className="text-sm text-kondo-gray-600">Total de Usuários</p>
              <p className="text-2xl font-bold text-kondo-gray-900">{stats.total}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-kondo-green-100 rounded-lg flex items-center justify-center">
              <User className="w-5 h-5 text-kondo-green-600" />
            </div>
            <div>
              <p className="text-sm text-kondo-gray-600">Ativos</p>
              <p className="text-2xl font-bold text-kondo-green-600">{stats.active}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-kondo-teal-100 rounded-lg flex items-center justify-center">
              <User className="w-5 h-5 text-kondo-teal-600" />
            </div>
            <div>
              <p className="text-sm text-kondo-gray-600">Moradores</p>
              <p className="text-2xl font-bold text-kondo-teal-600">{stats.moradores}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-kondo-orange-100 rounded-lg flex items-center justify-center">
              <Shield className="w-5 h-5 text-kondo-orange-600" />
            </div>
            <div>
              <p className="text-sm text-kondo-gray-600">Administradores</p>
              <p className="text-2xl font-bold text-kondo-orange-600">{stats.admins}</p>
            </div>
          </div>
        </div>
      </div>

      {/* Filters */}
      <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
        <div className="flex items-center gap-2 mb-4">
          <Filter className="w-5 h-5 text-kondo-gray-600" />
          <h2 className="text-lg font-semibold text-kondo-gray-900">Filtros</h2>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div className="md:col-span-2">
            <Input
              variant="search"
              placeholder="Buscar por nome ou email..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>

          <Select
            placeholder="Todos os perfis"
            options={[
              { value: 'all', label: 'Todos os Perfis' },
              { value: 'admin', label: 'Administrador' },
              { value: 'sindico', label: 'Síndico' },
              { value: 'morador', label: 'Morador' },
              { value: 'funcionario', label: 'Funcionário' },
            ]}
            value={selectedRole}
            onChange={(e) => setSelectedRole(e.target.value)}
          />

          <Select
            placeholder="Todos os status"
            options={[
              { value: 'all', label: 'Todos os Status' },
              { value: 'active', label: 'Ativos' },
              { value: 'inactive', label: 'Inativos' },
            ]}
            value={selectedStatus}
            onChange={(e) => setSelectedStatus(e.target.value)}
          />
        </div>

        {(searchQuery || selectedRole !== 'all' || selectedStatus !== 'all') && (
          <div className="mt-4 flex items-center justify-between">
            <p className="text-sm text-kondo-gray-600">
              Mostrando <span className="font-semibold text-kondo-gray-900">{filteredUsers.length}</span> de {usersData.length} usuários
            </p>
            <button
              onClick={() => {
                setSearchQuery('');
                setSelectedRole('all');
                setSelectedStatus('all');
                setCurrentPage(1);
              }}
              className="text-sm font-medium text-kondo-purple-600 hover:text-kondo-purple-700"
            >
              Limpar filtros
            </button>
          </div>
        )}
      </div>

      {/* Users Table */}
      <DataTable
        columns={columns}
        data={paginatedUsers}
        keyExtractor={(row) => row.id}
        onRowClick={(row) => alert(`Ver detalhes de ${row.name}`)}
        actions={{
          onView: (row) => alert(`Visualizar ${row.name}`),
          onEdit: (row) => alert(`Editar ${row.name}`),
          onDelete: (row) => {
            if (confirm(`Deseja realmente remover ${row.name}?`)) {
              alert(`Usuário ${row.name} removido`);
            }
          },
        }}
        pagination={{
          currentPage,
          totalPages,
          pageSize,
          totalItems: filteredUsers.length,
          onPageChange: setCurrentPage,
        }}
      />
    </div>
  );
}
