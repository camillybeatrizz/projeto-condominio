import { useState } from 'react';
import { Upload, X, FileText, Calendar, DollarSign, Tag, AlertCircle, CheckCircle } from 'lucide-react';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import { Select } from '../components/Select';

interface DespesaRecente {
  id: string;
  description: string;
  category: string;
  value: string;
  date: string;
  supplier: string;
  status: 'approved' | 'pending';
}

const despesasRecentes: DespesaRecente[] = [
  { id: '1', description: 'Manutenção de Elevadores', category: 'Manutenção', value: 'R$ 3.500,00', date: '28/05/2026', supplier: 'ElevaTech LTDA', status: 'approved' },
  { id: '2', description: 'Energia Elétrica - Maio', category: 'Utilidades', value: 'R$ 2.800,00', date: '25/05/2026', supplier: 'Companhia de Energia', status: 'approved' },
  { id: '3', description: 'Produtos de Limpeza', category: 'Material', value: 'R$ 450,00', date: '20/05/2026', supplier: 'CleanSupply', status: 'pending' },
];

export function FinanceiroDespesas() {
  const [uploadedFile, setUploadedFile] = useState<File | null>(null);
  const [formData, setFormData] = useState({
    description: '',
    category: '',
    value: '',
    date: '',
    supplier: '',
    paymentMethod: '',
    notes: '',
  });

  const handleFileUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setUploadedFile(file);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    alert('Despesa lançada com sucesso!');
    // Reset form
    setFormData({
      description: '',
      category: '',
      value: '',
      date: '',
      supplier: '',
      paymentMethod: '',
      notes: '',
    });
    setUploadedFile(null);
  };

  return (
    <div className="p-8 space-y-8">
      {/* Page Header */}
      <div>
        <h1 className="text-3xl font-bold text-kondo-gray-900 mb-2">Lançamento de Despesas</h1>
        <p className="text-kondo-gray-600">Registre e gerencie as despesas do condomínio</p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left - Form */}
        <div className="lg:col-span-2">
          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-8">
            <div className="flex items-center gap-3 mb-8">
              <div className="w-12 h-12 bg-kondo-purple-100 rounded-lg flex items-center justify-center">
                <DollarSign className="w-6 h-6 text-kondo-purple-600" />
              </div>
              <div>
                <h2 className="text-xl font-semibold text-kondo-gray-900">Nova Despesa</h2>
                <p className="text-sm text-kondo-gray-600">Preencha os dados abaixo</p>
              </div>
            </div>

            <form onSubmit={handleSubmit} className="space-y-6">
              {/* Description */}
              <Input
                label="Descrição da Despesa"
                placeholder="Ex: Manutenção preventiva do elevador"
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                required
              />

              {/* Category and Value */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <Select
                  label="Categoria"
                  placeholder="Selecione a categoria"
                  options={[
                    { value: 'manutencao', label: 'Manutenção' },
                    { value: 'utilidades', label: 'Utilidades' },
                    { value: 'material', label: 'Material' },
                    { value: 'limpeza', label: 'Limpeza' },
                    { value: 'seguranca', label: 'Segurança' },
                    { value: 'jardinagem', label: 'Jardinagem' },
                    { value: 'administrativo', label: 'Administrativo' },
                    { value: 'outros', label: 'Outros' },
                  ]}
                  value={formData.category}
                  onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                  required
                />

                <Input
                  label="Valor"
                  type="text"
                  placeholder="R$ 0,00"
                  leftIcon={<DollarSign className="w-5 h-5" />}
                  value={formData.value}
                  onChange={(e) => setFormData({ ...formData, value: e.target.value })}
                  required
                />
              </div>

              {/* Date and Supplier */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <Input
                  label="Data da Despesa"
                  type="date"
                  leftIcon={<Calendar className="w-5 h-5" />}
                  value={formData.date}
                  onChange={(e) => setFormData({ ...formData, date: e.target.value })}
                  required
                />

                <Input
                  label="Fornecedor"
                  placeholder="Nome do fornecedor"
                  value={formData.supplier}
                  onChange={(e) => setFormData({ ...formData, supplier: e.target.value })}
                  required
                />
              </div>

              {/* Payment Method */}
              <Select
                label="Método de Pagamento"
                placeholder="Selecione o método"
                options={[
                  { value: 'dinheiro', label: 'Dinheiro' },
                  { value: 'transferencia', label: 'Transferência Bancária' },
                  { value: 'pix', label: 'PIX' },
                  { value: 'boleto', label: 'Boleto' },
                  { value: 'cartao', label: 'Cartão' },
                  { value: 'cheque', label: 'Cheque' },
                ]}
                value={formData.paymentMethod}
                onChange={(e) => setFormData({ ...formData, paymentMethod: e.target.value })}
                required
              />

              {/* File Upload */}
              <div>
                <label className="block text-sm font-medium text-kondo-gray-700 mb-2">
                  Comprovante (Nota Fiscal/Recibo)
                </label>
                <div className="border-2 border-dashed border-kondo-gray-300 rounded-lg p-6 hover:border-kondo-purple-400 transition-colors">
                  {uploadedFile ? (
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-3">
                        <div className="w-12 h-12 bg-kondo-purple-100 rounded-lg flex items-center justify-center">
                          <FileText className="w-6 h-6 text-kondo-purple-600" />
                        </div>
                        <div>
                          <p className="text-sm font-medium text-kondo-gray-900">{uploadedFile.name}</p>
                          <p className="text-xs text-kondo-gray-500">
                            {(uploadedFile.size / 1024).toFixed(2)} KB
                          </p>
                        </div>
                      </div>
                      <button
                        type="button"
                        onClick={() => setUploadedFile(null)}
                        className="p-2 text-kondo-red-600 hover:bg-kondo-red-50 rounded-lg transition-colors"
                      >
                        <X className="w-5 h-5" />
                      </button>
                    </div>
                  ) : (
                    <label className="cursor-pointer block text-center">
                      <input
                        type="file"
                        className="hidden"
                        accept=".pdf,.jpg,.jpeg,.png"
                        onChange={handleFileUpload}
                      />
                      <Upload className="w-12 h-12 text-kondo-gray-400 mx-auto mb-3" />
                      <p className="text-sm font-medium text-kondo-gray-900 mb-1">
                        Clique para fazer upload
                      </p>
                      <p className="text-xs text-kondo-gray-500">
                        PDF, JPG ou PNG até 5MB
                      </p>
                    </label>
                  )}
                </div>
              </div>

              {/* Notes */}
              <div>
                <label className="block text-sm font-medium text-kondo-gray-700 mb-2">
                  Observações (Opcional)
                </label>
                <textarea
                  className="w-full px-4 py-2.5 bg-kondo-gray-50 border border-kondo-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-kondo-purple-600 focus:border-transparent resize-none"
                  rows={4}
                  placeholder="Adicione informações adicionais sobre esta despesa..."
                  value={formData.notes}
                  onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
                />
              </div>

              {/* Actions */}
              <div className="flex gap-3 pt-4 border-t border-kondo-gray-200">
                <Button variant="primary" type="submit" size="lg" className="flex-1">
                  Lançar Despesa
                </Button>
                <Button
                  variant="secondary"
                  type="button"
                  size="lg"
                  onClick={() => {
                    setFormData({
                      description: '',
                      category: '',
                      value: '',
                      date: '',
                      supplier: '',
                      paymentMethod: '',
                      notes: '',
                    });
                    setUploadedFile(null);
                  }}
                >
                  Limpar
                </Button>
              </div>
            </form>
          </div>
        </div>

        {/* Right - Info & Recent */}
        <div className="space-y-6">
          {/* Info Card */}
          <div className="bg-gradient-to-br from-kondo-teal-600 to-kondo-teal-700 rounded-xl p-6 text-white shadow-lg">
            <div className="flex items-center gap-3 mb-4">
              <div className="w-10 h-10 bg-white/20 backdrop-blur-sm rounded-lg flex items-center justify-center">
                <AlertCircle className="w-5 h-5" />
              </div>
              <h3 className="font-semibold">Dicas Importantes</h3>
            </div>
            <ul className="space-y-3 text-sm text-kondo-teal-50">
              <li className="flex items-start gap-2">
                <span className="text-white mt-0.5">•</span>
                <span>Sempre anexe o comprovante da despesa</span>
              </li>
              <li className="flex items-start gap-2">
                <span className="text-white mt-0.5">•</span>
                <span>Categorize corretamente para melhor controle</span>
              </li>
              <li className="flex items-start gap-2">
                <span className="text-white mt-0.5">•</span>
                <span>Descreva de forma clara e objetiva</span>
              </li>
              <li className="flex items-start gap-2">
                <span className="text-white mt-0.5">•</span>
                <span>Verifique os dados antes de salvar</span>
              </li>
            </ul>
          </div>

          {/* Recent Expenses */}
          <div className="bg-white rounded-xl p-6 shadow-sm border border-kondo-gray-200">
            <h3 className="font-semibold text-kondo-gray-900 mb-4">Despesas Recentes</h3>
            <div className="space-y-4">
              {despesasRecentes.map((despesa) => (
                <div
                  key={despesa.id}
                  className="p-4 bg-kondo-gray-50 rounded-lg hover:bg-kondo-gray-100 transition-colors"
                >
                  <div className="flex items-start justify-between mb-2">
                    <div className="flex-1">
                      <p className="text-sm font-semibold text-kondo-gray-900 mb-1">
                        {despesa.description}
                      </p>
                      <div className="flex items-center gap-2 flex-wrap">
                        <span className="px-2 py-0.5 bg-kondo-purple-100 text-kondo-purple-700 text-xs rounded-full">
                          {despesa.category}
                        </span>
                        <span className={`px-2 py-0.5 text-xs rounded-full ${
                          despesa.status === 'approved'
                            ? 'bg-kondo-green-100 text-kondo-green-700'
                            : 'bg-kondo-orange-100 text-kondo-orange-700'
                        }`}>
                          {despesa.status === 'approved' ? 'Aprovado' : 'Pendente'}
                        </span>
                      </div>
                    </div>
                  </div>
                  <div className="flex items-center justify-between text-xs text-kondo-gray-600">
                    <span>{despesa.date}</span>
                    <span className="font-semibold text-kondo-gray-900">{despesa.value}</span>
                  </div>
                </div>
              ))}
            </div>
            <button className="w-full mt-4 px-4 py-2 text-sm font-medium text-kondo-purple-600 hover:text-kondo-purple-700 transition-colors">
              Ver todas as despesas
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
