import { useQuery } from '@tanstack/react-query';
import { Building2, Home, MapPin, Plus, Search } from 'lucide-react';
import { MainLayout } from '../components/MainLayout';
import { Card } from '../components/Card';
import { estruturaService } from '../services/operacional.service';
import { useAuth } from '../providers/AuthProvider';
import { useState } from 'react';

export function GestaoEstruturaPage() {
  const { activeAcesso } = useAuth();
  const condominioId = activeAcesso?.condominioId || '';
  const [selectedBlocoId, setSelectedBlocoId] = useState<string | null>(null);

  const { data: blocos, isLoading: loadingBlocos } = useQuery({
    queryKey: ['blocos', condominioId],
    queryFn: () => estruturaService.listBlocos(condominioId),
    enabled: !!condominioId,
  });

  const { data: unidades, isLoading: loadingUnidades } = useQuery({
    queryKey: ['unidades', selectedBlocoId],
    queryFn: () => estruturaService.listUnidades(selectedBlocoId!),
    enabled: !!selectedBlocoId,
  });

  return (
    <MainLayout breadcrumbs={[{ label: 'Estrutura' }]}>
      <div className="space-y-8">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold text-kondo-gray-900">Estrutura do Condomínio</h1>
            <p className="text-kondo-gray-500">Gerencie blocos e unidades do {activeAcesso?.condominioNome}</p>
          </div>
          <div className="flex items-center gap-3">
            <button className="flex items-center gap-2 px-4 py-2 text-sm font-semibold text-white bg-kondo-purple-600 rounded-lg hover:bg-kondo-purple-700 transition-all shadow-md">
              <Plus className="w-4 h-4" /> Novo Bloco
            </button>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
          {/* Blocos List */}
          <div className="lg:col-span-1 space-y-4">
            <h3 className="text-sm font-bold text-kondo-gray-500 uppercase tracking-wider px-1">Blocos</h3>
            <div className="space-y-2">
              {loadingBlocos ? (
                [1, 2, 3].map(i => <div key={i} className="h-14 bg-white rounded-xl animate-pulse border border-kondo-gray-100" />)
              ) : (
                blocos?.map((bloco) => (
                  <button
                    key={bloco.id}
                    onClick={() => setSelectedBlocoId(bloco.id)}
                    className={`w-full flex items-center justify-between p-4 rounded-xl border transition-all ${
                      selectedBlocoId === bloco.id
                        ? 'bg-kondo-purple-50 border-kondo-purple-200 text-kondo-purple-700 shadow-sm'
                        : 'bg-white border-kondo-gray-200 text-kondo-gray-700 hover:border-kondo-purple-200 hover:bg-kondo-purple-50/50'
                    }`}
                  >
                    <div className="flex items-center gap-3">
                      <Building2 className={`w-5 h-5 ${selectedBlocoId === bloco.id ? 'text-kondo-purple-600' : 'text-kondo-gray-400'}`} />
                      <span className="font-bold">{bloco.nome}</span>
                    </div>
                    <Search className="w-4 h-4 opacity-0 group-hover:opacity-100 transition-opacity" />
                  </button>
                ))
              )}
            </div>
          </div>

          {/* Unidades List */}
          <div className="lg:col-span-3 space-y-4">
            <div className="flex items-center justify-between px-1">
              <h3 className="text-sm font-bold text-kondo-gray-500 uppercase tracking-wider">
                {selectedBlocoId ? `Unidades do Bloco` : 'Selecione um bloco para ver as unidades'}
              </h3>
              {selectedBlocoId && (
                <button className="text-xs font-bold text-kondo-purple-600 hover:underline flex items-center gap-1">
                  <Plus className="w-3 h-3" /> Adicionar Unidade
                </button>
              )}
            </div>

            {!selectedBlocoId ? (
              <div className="h-64 flex flex-col items-center justify-center bg-white rounded-2xl border-2 border-dashed border-kondo-gray-200 text-kondo-gray-400">
                <MapPin className="w-12 h-12 mb-3 opacity-20" />
                <p className="font-medium text-sm">Escolha um bloco ao lado para gerenciar as unidades.</p>
              </div>
            ) : (
              <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-4">
                {loadingUnidades ? (
                  [1, 2, 3, 4, 5, 6].map(i => <div key={i} className="h-32 bg-white rounded-xl animate-pulse border border-kondo-gray-100" />)
                ) : (
                  unidades?.map((unidade) => (
                    <Card key={unidade.id} className="p-5 hover:shadow-md transition-shadow group">
                      <div className="flex items-start justify-between mb-4">
                        <div className="flex items-center gap-3">
                          <div className="w-10 h-10 bg-kondo-purple-50 text-kondo-purple-600 rounded-lg flex items-center justify-center font-bold">
                            {unidade.numero}
                          </div>
                          <div>
                            <p className="text-xs text-kondo-gray-500 font-medium">Andar {unidade.andar}</p>
                            <p className="text-[10px] font-bold text-kondo-gray-400 uppercase">{unidade.tipo}</p>
                          </div>
                        </div>
                        <span className={`px-2 py-0.5 rounded text-[10px] font-bold uppercase ${
                          unidade.moradorId ? 'bg-kondo-green-100 text-kondo-green-700' : 'bg-kondo-gray-100 text-kondo-gray-500'
                        }`}>
                          {unidade.moradorId ? 'Ocupada' : 'Vaga'}
                        </span>
                      </div>
                      <div className="flex items-center justify-between pt-4 border-t border-kondo-gray-50">
                        <button className="text-xs font-bold text-kondo-purple-600 hover:text-kondo-purple-700 transition-colors">
                          Ver Detalhes
                        </button>
                        <button className="p-1 text-kondo-gray-400 hover:text-kondo-gray-600 transition-colors">
                          <Plus className="w-4 h-4 rotate-45" />
                        </button>
                      </div>
                    </Card>
                  ))
                )}
                {unidades?.length === 0 && (
                  <div className="col-span-full py-12 text-center text-kondo-gray-400">
                    Nenhuma unidade cadastrada neste bloco.
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      </div>
    </MainLayout>
  );
}
