import { useState } from 'react';
import { Download, QrCode, Filter, Search, Mail, Calendar, DollarSign, CheckCircle, Clock, XCircle } from 'lucide-react';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import { Select } from '../components/Select';
import { DataTable, Column } from '../components/DataTable';

interface Cobranca {
  id: string;
  unit: string;
  owner: string;
  reference: string;
  dueDate: string;
  value: string;
  status: 'paid' | 'pending' | 'overdue';
  paymentDate?: string;
  method?: string;
}

const cobrancasData: Cobranca[] = [
  { id: '001', unit: 'Apt 101', owner: 'Pedro Almeida', reference: 'Junho/2026', dueDate: '10/06/2026', value: 'R$ 1.850,00', status: 'pending' },
  { id: '002', unit: 'Apt 102', owner: 'Lucia Ferreira', reference: 'Junho/2026', dueDate: '10/06/2026', value: 'R$ 1.850,00', status: 'pending' },
  { id: '003', unit: 'Apt 201', owner: 'Roberto Souza', reference: 'Junho/2026', dueDate: '10/06/2026', value: 'R$ 1.850,00', status: 'paid', paymentDate: '08/06/2026', method: 'PIX' },
  { id: '004', unit: 'Apt 202', owner: 'Fernanda Lima', reference: 'Maio/2026', dueDate: '10/05/2026', value: 'R$ 1.850,00', status: 'overdue' },
  { id: '005', unit: 'Apt 301', owner: 'João Silva', reference: 'Maio/2026', dueDate: '10/05/2026', value: 'R$ 1.850,00', status: 'overdue' },
  { id: '006', unit: 'Apt 302', owner: 'Sandra Dias', reference: 'Junho/2026', dueDate: '10/06/2026', value: 'R$ 1.850,00', status: 'paid', paymentDate: '09/06/2026', method: 'Boleto' },
  { id: '007', unit: 'Apt 401', owner: 'Marcos Paulo', reference: 'Junho/2026', dueDate: '10/06/2026', value: 'R$ 1.850,00', status: 'paid', paymentDate: '07/06/2026', method: 'PIX' },
  { id: '008', unit: 'Apt 501', owner: 'Antonio Carlos', reference: 'Junho/2026', dueDate: '10/06/2026', value: 'R$ 1.850,00', status: 'pending' },
];

export function FinanceiroCobrancas() {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('all');
  const [selectedCobranca, setSelectedCobranca] = useState<Cobranca | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const pageSize = 6;

  const filteredCobrancas = cobrancasData.filter((cobranca) => {
    const matchesSearch =
      cobranca.unit.toLowerCase().includes(searchQuery.toLowerCase()) ||
      cobranca.owner.toLowerCase().includes(searchQuery.toLowerCase());

    const matchesStatus = selectedStatus === 'all' || cobranca.status === selectedStatus;

    return matchesSearch && matchesStatus;
  });

  const totalPages = Math.ceil(filteredCobrancas.length / pageSize);
  const paginatedData = filteredCobrancas.slice(
    (currentPage - 1) * pageSize,
    currentPage * pageSize
  );

  const stats = {
    total: cobrancasData.reduce((sum, c) => sum + parseFloat(c.value.replace('R$ ', '').replace('.', '').replace(',', '.')), 0),
    paid: cobrancasData.filter(c => c.status === 'paid').length,
    pending: cobrancasData.filter(c => c.status === 'pending').length,
    overdue: cobrancasData.filter(c => c.status === 'overdue').length,
  };

  const columns: Column<Cobranca>[] = [
    {
      key: 'unit',
      label: 'Unidade',
      sortable: true,
      width: '12%',
    },
    {
      key: 'owner',
      label: 'Morador',
      sortable: true,
      width: '18%',
    },
    {
      key: 'reference',
      label: 'Referência',
      width: '12%',
    },
    {
      key: 'dueDate',
      label: 'Vencimento',
      sortable: true,
      width: '12%',
      render: (row) => (
        <div className="flex items-center gap-2 text-sm">
          <Calendar className="w-4 h-4 text-kondo-gray-400" />
          {row.dueDate}
        </div>
      ),
    },
    {
      key: 'value',
      label: 'Valor',
      sortable: true,
      width: '12%',
      render: (row) => (
        <span className="font-semibold text-kondo-gray-900">{row.value}</span>
      ),
    },
    {
      key: 'status',
      label: 'Status',
      width: '15%',
      render: (row) => {
        const statusConfig = {
          paid: { label: 'Pago', color: 'bg-kondo-green-100 text-kondo-green-700', icon: CheckCircle },
          pending: { label: 'Pendente', color: 'bg-kondo-orange-100 text-kondo-orange-700', icon: Clock },
          overdue: { label: 'Atrasado', color: 'bg-kondo-red-100 text-kondo-red-700', icon: XCircle },
        };
        const config = statusConfig[row.status];
        const Icon = config.icon;
        return (
          <span className={`px-3 py-1 ${config.color} rounded-full text-xs font-medium inline-flex items-center gap-1.5`}>
            <Icon className="w-3.5 h-3.5" />
            {config.label}
          </span>
        );
      },
    },
    {
      key: 'paymentDate',
      label: 'Pagamento',
      width: '15%',
      render: (row) => (
        <div className="text-sm text-kondo-gray-600">
          {row.paymentDate ? (
            <div>
              <p className="font-medium text-kondo-gray-900">{row.paymentDate}</p>
              <p className="text-xs text-kondo-gray-500">{row.method}</p>
            </div>
          ) : (
            <span className="text-kondo-gray-400">—</span>
          )}
        </div>
      ),
    },
  ];

  return (
    <div className="p-8 space-y-8">
      {/* Page Header */}
      <div className="flex items-start justify-between">
        <div>
          <h1 className="text-3xl font-bold text-kondo-gray-900 mb-2">Cobranças</h1>
          <p className="text-kondo-gray-600">Gerencie boletos e pagamentos do condomínio</p>
        </div>
        <div className="flex gap-3">
          <Button variant="secondary">
            <Download className="w-5 h-5 mr-2" />
            Exportar
          </Button>
          <Button variant="primary">
            Gerar Cobranças
          </Button>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-kondo-purple-100 rounded-lg flex items-center justify-center">
              <DollarSign className="w-5 h-5 text-kondo-purple-600" />
            </div>
            <div>
              <p className="text-sm text-kondo-gray-600">Total a Receber</p>
              <p className="text-2xl font-bold text-kondo-gray-900">
                R$ {stats.total.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
              </p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-kondo-green-100 rounded-lg flex items-center justify-center">
              <CheckCircle className="w-5 h-5 text-kondo-green-600" />
            </div>
            <div>
              <p className="text-sm text-kondo-gray-600">Pagos</p>
              <p className="text-2xl font-bold text-kondo-green-600">{stats.paid}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-kondo-orange-100 rounded-lg flex items-center justify-center">
              <Clock className="w-5 h-5 text-kondo-orange-600" />
            </div>
            <div>
              <p className="text-sm text-kondo-gray-600">Pendentes</p>
              <p className="text-2xl font-bold text-kondo-orange-600">{stats.pending}</p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 bg-kondo-red-100 rounded-lg flex items-center justify-center">
              <XCircle className="w-5 h-5 text-kondo-red-600" />
            </div>
            <div>
              <p className="text-sm text-kondo-gray-600">Atrasados</p>
              <p className="text-2xl font-bold text-kondo-red-600">{stats.overdue}</p>
            </div>
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left - Table */}
        <div className="lg:col-span-2 space-y-6">
          {/* Filters */}
          <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              <div className="md:col-span-2">
                <Input
                  variant="search"
                  placeholder="Buscar por unidade ou morador..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                />
              </div>
              <Select
                placeholder="Todos os status"
                options={[
                  { value: 'all', label: 'Todos os Status' },
                  { value: 'paid', label: 'Pagos' },
                  { value: 'pending', label: 'Pendentes' },
                  { value: 'overdue', label: 'Atrasados' },
                ]}
                value={selectedStatus}
                onChange={(e) => setSelectedStatus(e.target.value)}
              />
            </div>
          </div>

          {/* Table */}
          <DataTable
            columns={columns}
            data={paginatedData}
            keyExtractor={(row) => row.id}
            onRowClick={(row) => setSelectedCobranca(row)}
            actions={{
              onView: (row) => setSelectedCobranca(row),
              onDownload: (row) => alert(`Download boleto ${row.id}`),
            }}
            pagination={{
              currentPage,
              totalPages,
              pageSize,
              totalItems: filteredCobrancas.length,
              onPageChange: setCurrentPage,
            }}
          />
        </div>

        {/* Right - QR Code PIX */}
        <div className="space-y-6">
          <div className="bg-gradient-to-br from-kondo-green-600 to-kondo-teal-600 rounded-xl p-6 text-white shadow-lg">
            <div className="flex items-center gap-3 mb-6">
              <div className="w-12 h-12 bg-white/20 backdrop-blur-sm rounded-lg flex items-center justify-center">
                <QrCode className="w-6 h-6 text-white" />
              </div>
              <div>
                <h2 className="text-xl font-bold">Gerar QR Code PIX</h2>
                <p className="text-sm text-kondo-green-100">Pagamento instantâneo</p>
              </div>
            </div>

            {selectedCobranca && selectedCobranca.status !== 'paid' ? (
              <div className="space-y-4">
                <div className="bg-white rounded-lg p-4">
                  <div className="aspect-square bg-kondo-gray-100 rounded-lg flex items-center justify-center mb-3">
                    {/* QR Code Placeholder */}
                    <div className="w-full h-full p-4">
                      <svg viewBox="0 0 100 100" className="w-full h-full">
                        {/* QR Code Pattern */}
                        <rect x="0" y="0" width="20" height="20" fill="#000" />
                        <rect x="5" y="5" width="10" height="10" fill="#fff" />
                        <rect x="80" y="0" width="20" height="20" fill="#000" />
                        <rect x="85" y="5" width="10" height="10" fill="#fff" />
                        <rect x="0" y="80" width="20" height="20" fill="#000" />
                        <rect x="5" y="85" width="10" height="10" fill="#fff" />
                        <rect x="30" y="10" width="5" height="5" fill="#000" />
                        <rect x="40" y="10" width="5" height="5" fill="#000" />
                        <rect x="50" y="10" width="5" height="5" fill="#000" />
                        <rect x="60" y="10" width="5" height="5" fill="#000" />
                        <rect x="30" y="20" width="5" height="5" fill="#000" />
                        <rect x="50" y="20" width="5" height="5" fill="#000" />
                        <rect x="30" y="30" width="5" height="5" fill="#000" />
                        <rect x="40" y="30" width="5" height="5" fill="#000" />
                        <rect x="60" y="30" width="5" height="5" fill="#000" />
                        <rect x="40" y="40" width="5" height="5" fill="#000" />
                        <rect x="50" y="40" width="5" height="5" fill="#000" />
                        <rect x="30" y="50" width="5" height="5" fill="#000" />
                        <rect x="60" y="50" width="5" height="5" fill="#000" />
                        <rect x="40" y="60" width="5" height="5" fill="#000" />
                        <rect x="50" y="60" width="5" height="5" fill="#000" />
                        <rect x="30" y="70" width="5" height="5" fill="#000" />
                        <rect x="60" y="70" width="5" height="5" fill="#000" />
                        <rect x="70" y="30" width="5" height="5" fill="#000" />
                        <rect x="80" y="30" width="5" height="5" fill="#000" />
                        <rect x="90" y="30" width="5" height="5" fill="#000" />
                        <rect x="70" y="40" width="5" height="5" fill="#000" />
                        <rect x="90" y="40" width="5" height="5" fill="#000" />
                        <rect x="70" y="50" width="5" height="5" fill="#000" />
                        <rect x="80" y="50" width="5" height="5" fill="#000" />
                        <rect x="90" y="50" width="5" height="5" fill="#000" />
                        <rect x="80" y="60" width="5" height="5" fill="#000" />
                        <rect x="70" y="70" width="5" height="5" fill="#000" />
                        <rect x="90" y="70" width="5" height="5" fill="#000" />
                        <rect x="30" y="80" width="5" height="5" fill="#000" />
                        <rect x="40" y="90" width="5" height="5" fill="#000" />
                        <rect x="50" y="80" width="5" height="5" fill="#000" />
                        <rect x="60" y="90" width="5" height="5" fill="#000" />
                      </svg>
                    </div>
                  </div>
                  <div className="text-center">
                    <p className="text-xs text-kondo-gray-600 mb-1">Valor</p>
                    <p className="text-lg font-bold text-kondo-gray-900">{selectedCobranca.value}</p>
                  </div>
                </div>

                <div className="space-y-3">
                  <div className="bg-white/20 backdrop-blur-sm rounded-lg p-3">
                    <p className="text-xs text-kondo-green-100 mb-1">Unidade</p>
                    <p className="font-semibold">{selectedCobranca.unit} - {selectedCobranca.owner}</p>
                  </div>
                  <div className="bg-white/20 backdrop-blur-sm rounded-lg p-3">
                    <p className="text-xs text-kondo-green-100 mb-1">Referência</p>
                    <p className="font-semibold">{selectedCobranca.reference}</p>
                  </div>
                  <div className="bg-white/20 backdrop-blur-sm rounded-lg p-3">
                    <p className="text-xs text-kondo-green-100 mb-1">Vencimento</p>
                    <p className="font-semibold">{selectedCobranca.dueDate}</p>
                  </div>
                </div>

                <Button variant="secondary" className="w-full" size="lg">
                  Copiar Código PIX
                </Button>
              </div>
            ) : (
              <div className="text-center py-8">
                <QrCode className="w-16 h-16 mx-auto mb-4 opacity-50" />
                <p className="text-kondo-green-100">
                  {selectedCobranca?.status === 'paid'
                    ? 'Esta cobrança já foi paga'
                    : 'Selecione uma cobrança na tabela para gerar o QR Code PIX'}
                </p>
              </div>
            )}
          </div>

          {/* Quick Actions */}
          <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
            <h3 className="font-semibold text-kondo-gray-900 mb-4">Ações Rápidas</h3>
            <div className="space-y-2">
              <button className="w-full px-4 py-3 text-left text-sm font-medium text-kondo-gray-700 bg-kondo-gray-50 hover:bg-kondo-gray-100 rounded-lg transition-colors flex items-center gap-3">
                <Mail className="w-4 h-4" />
                Enviar Boletos por Email
              </button>
              <button className="w-full px-4 py-3 text-left text-sm font-medium text-kondo-gray-700 bg-kondo-gray-50 hover:bg-kondo-gray-100 rounded-lg transition-colors flex items-center gap-3">
                <Download className="w-4 h-4" />
                Download em Lote
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
