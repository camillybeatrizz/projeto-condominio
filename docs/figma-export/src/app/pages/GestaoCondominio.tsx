import { Plus, Search, Filter, Building2, Home } from 'lucide-react';
import { useState } from 'react';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import { Select } from '../components/Select';
import { UnitCard } from '../components/Card';

const unitsData = [
  { id: '1', unit: '101', block: 'A', owner: 'Pedro Almeida', type: 'Apartamento', status: 'occupied' as const, phone: '(83) 98999-1111', email: 'pedro@email.com' },
  { id: '2', unit: '102', block: 'A', owner: 'Lucia Ferreira', type: 'Apartamento', status: 'occupied' as const, phone: '(83) 98999-2222' },
  { id: '3', unit: '103', block: 'A', owner: '—', type: 'Apartamento', status: 'vacant' as const },
  { id: '4', unit: '201', block: 'A', owner: 'Roberto Souza', type: 'Apartamento', status: 'rented' as const, phone: '(83) 98999-3333' },
  { id: '5', unit: '202', block: 'A', owner: 'Fernanda Lima', type: 'Apartamento', status: 'occupied' as const, phone: '(83) 98999-4444' },
  { id: '6', unit: '301', block: 'A', owner: 'João Silva', type: 'Apartamento', status: 'occupied' as const, phone: '(83) 98999-5555' },
  { id: '7', unit: '302', block: 'A', owner: 'Sandra Dias', type: 'Apartamento', status: 'occupied' as const, phone: '(83) 98999-6666' },
  { id: '8', unit: '401', block: 'A', owner: 'Marcos Paulo', type: 'Apartamento', status: 'occupied' as const, phone: '(83) 98999-7777' },
  { id: '9', unit: '501', block: 'B', owner: 'Antonio Carlos', type: 'Apartamento', status: 'occupied' as const, phone: '(83) 98999-8888' },
  { id: '10', unit: '502', block: 'B', owner: '—', type: 'Apartamento', status: 'vacant' as const },
  { id: '11', unit: '503', block: 'B', owner: 'Maria Santos', type: 'Apartamento', status: 'rented' as const, phone: '(83) 98999-9999' },
  { id: '12', unit: '504', block: 'B', owner: 'Carlos Oliveira', type: 'Apartamento', status: 'occupied' as const, phone: '(83) 98888-1111' },
];

export function GestaoCondominio() {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedBlock, setSelectedBlock] = useState('all');
  const [selectedStatus, setSelectedStatus] = useState('all');

  const filteredUnits = unitsData.filter((unit) => {
    const matchesSearch =
      unit.unit.toLowerCase().includes(searchQuery.toLowerCase()) ||
      unit.owner.toLowerCase().includes(searchQuery.toLowerCase());

    const matchesBlock = selectedBlock === 'all' || unit.block === selectedBlock;
    const matchesStatus = selectedStatus === 'all' || unit.status === selectedStatus;

    return matchesSearch && matchesBlock && matchesStatus;
  });

  const stats = {
    total: unitsData.length,
    occupied: unitsData.filter(u => u.status === 'occupied').length,
    vacant: unitsData.filter(u => u.status === 'vacant').length,
    rented: unitsData.filter(u => u.status === 'rented').length,
  };

  return (
    <div className="p-8 space-y-8">
      {/* Page Header */}
      <div className="flex items-start justify-between">
        <div>
          <h1 className="text-3xl font-bold text-kondo-gray-900 mb-2">Gestão de Unidades</h1>
          <p className="text-kondo-gray-600">Visualize e gerencie todas as unidades do condomínio</p>
        </div>
        <Button variant="primary" size="lg">
          <Plus className="w-5 h-5 mr-2" />
          Adicionar Unidade
        </Button>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
          <div className="flex items-center gap-3 mb-3">
            <div className="w-10 h-10 bg-kondo-purple-100 rounded-lg flex items-center justify-center">
              <Building2 className="w-5 h-5 text-kondo-purple-600" />
            </div>
            <div>
              <p className="text-sm text-kondo-gray-600">Total de Unidades</p>
              <p className="text-2xl font-bold text-kondo-gray-900">{stats.total}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
          <div className="flex items-center gap-3 mb-3">
            <div className="w-10 h-10 bg-kondo-teal-100 rounded-lg flex items-center justify-center">
              <Home className="w-5 h-5 text-kondo-teal-600" />
            </div>
            <div>
              <p className="text-sm text-kondo-gray-600">Ocupadas</p>
              <p className="text-2xl font-bold text-kondo-teal-600">{stats.occupied}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
          <div className="flex items-center gap-3 mb-3">
            <div className="w-10 h-10 bg-kondo-gray-100 rounded-lg flex items-center justify-center">
              <Home className="w-5 h-5 text-kondo-gray-600" />
            </div>
            <div>
              <p className="text-sm text-kondo-gray-600">Vagas</p>
              <p className="text-2xl font-bold text-kondo-gray-600">{stats.vacant}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
          <div className="flex items-center gap-3 mb-3">
            <div className="w-10 h-10 bg-kondo-purple-100 rounded-lg flex items-center justify-center">
              <Home className="w-5 h-5 text-kondo-purple-600" />
            </div>
            <div>
              <p className="text-sm text-kondo-gray-600">Alugadas</p>
              <p className="text-2xl font-bold text-kondo-purple-600">{stats.rented}</p>
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
              placeholder="Buscar por unidade ou morador..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>

          <Select
            placeholder="Todos os blocos"
            options={[
              { value: 'all', label: 'Todos os Blocos' },
              { value: 'A', label: 'Bloco A' },
              { value: 'B', label: 'Bloco B' },
            ]}
            value={selectedBlock}
            onChange={(e) => setSelectedBlock(e.target.value)}
          />

          <Select
            placeholder="Todos os status"
            options={[
              { value: 'all', label: 'Todos os Status' },
              { value: 'occupied', label: 'Ocupadas' },
              { value: 'vacant', label: 'Vagas' },
              { value: 'rented', label: 'Alugadas' },
            ]}
            value={selectedStatus}
            onChange={(e) => setSelectedStatus(e.target.value)}
          />
        </div>

        {(searchQuery || selectedBlock !== 'all' || selectedStatus !== 'all') && (
          <div className="mt-4 flex items-center justify-between">
            <p className="text-sm text-kondo-gray-600">
              Mostrando <span className="font-semibold text-kondo-gray-900">{filteredUnits.length}</span> de {unitsData.length} unidades
            </p>
            <button
              onClick={() => {
                setSearchQuery('');
                setSelectedBlock('all');
                setSelectedStatus('all');
              }}
              className="text-sm font-medium text-kondo-purple-600 hover:text-kondo-purple-700"
            >
              Limpar filtros
            </button>
          </div>
        )}
      </div>

      {/* Units Grid */}
      <div>
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-xl font-semibold text-kondo-gray-900">
            {selectedBlock !== 'all' ? `Bloco ${selectedBlock}` : 'Todas as Unidades'}
          </h2>
          <div className="flex items-center gap-2">
            <button className="px-3 py-2 text-sm font-medium text-kondo-gray-700 bg-white border border-kondo-gray-300 rounded-lg hover:bg-kondo-gray-50">
              Grade
            </button>
            <button className="px-3 py-2 text-sm font-medium text-kondo-gray-700 bg-white border border-kondo-gray-300 rounded-lg hover:bg-kondo-gray-50">
              Lista
            </button>
          </div>
        </div>

        {filteredUnits.length === 0 ? (
          <div className="bg-white rounded-xl p-12 shadow-sm border border-kondo-gray-200 text-center">
            <Building2 className="w-12 h-12 text-kondo-gray-400 mx-auto mb-4" />
            <h3 className="text-lg font-semibold text-kondo-gray-900 mb-2">Nenhuma unidade encontrada</h3>
            <p className="text-kondo-gray-600">Tente ajustar os filtros ou limpar a busca.</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {filteredUnits.map((unit) => (
              <UnitCard
                key={unit.id}
                unit={unit.unit}
                block={unit.block}
                owner={unit.owner}
                type={unit.type}
                status={unit.status}
                phone={unit.phone}
                email={unit.email}
                onView={() => alert(`Ver detalhes da unidade ${unit.unit}`)}
                onEdit={() => alert(`Editar unidade ${unit.unit}`)}
              />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
