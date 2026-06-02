import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { 
  QrCode, 
  Copy, 
  CheckCircle2, 
  Clock, 
  ExternalLink, 
  AlertCircle,
  CreditCard,
  ArrowRight
} from 'lucide-react';
import { MainLayout } from '../components/MainLayout';
import { Card, FinancialCard } from '../components/Card';
import { cobrancaService } from '../services/cobranca.service';
import { useAuth } from '../providers/auth-context';
import { StatusCobranca } from '../types/api';
import { formatCurrency, formatDateBr } from '../utils/formatters';

export function PortalMoradorPage() {
  const { activeAcesso } = useAuth();
  const [showPixModal, setShowPixModal] = useState(false);
  const [copied, setCopied] = useState(false);

  // 1. Buscar cobranças do morador
  const { data: cobrancasPage } = useQuery({
    queryKey: ['cobrancas-morador', activeAcesso?.unidadeId],
    queryFn: () => cobrancaService.list({
      condominioId: activeAcesso?.condominioId || '',
      unidadeId: activeAcesso?.unidadeId,
      size: 5
    }),
    enabled: !!activeAcesso?.unidadeId
  });

  const proximaCobranca = cobrancasPage?.content.find(c => c.status === StatusCobranca.ABERTA);

  // 2. Buscar dados do Pix se houver cobrança pendente
  const { data: pixData, isLoading: loadingPix } = useQuery({
    queryKey: ['pix', proximaCobranca?.id],
    queryFn: () => cobrancaService.getPix(proximaCobranca!.id),
    enabled: !!proximaCobranca?.id && showPixModal
  });

  const copyToClipboard = () => {
    if (pixData?.pixCopiaCola) {
      navigator.clipboard.writeText(pixData.pixCopiaCola);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    }
  };

  return (
    <MainLayout breadcrumbs={[{ label: 'Meu Portal' }]}>
      <div className="space-y-8">
        <div>
          <h1 className="text-2xl font-bold text-kondo-gray-900">Olá, Morador!</h1>
          <p className="text-kondo-gray-500">Unidade {activeAcesso?.unidadeNumero} - {activeAcesso?.condominioNome}</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Main Action: Proxima Fatura */}
          <div className="lg:col-span-2 space-y-6">
            <Card className="overflow-hidden border-none shadow-lg bg-gradient-to-br from-kondo-purple-600 to-kondo-purple-800 text-white p-8">
              <div className="flex flex-col md:flex-row md:items-center justify-between gap-6">
                <div>
                  <p className="text-kondo-purple-100 font-medium mb-1 uppercase tracking-wider text-xs">Próxima Fatura</p>
                  {proximaCobranca ? (
                    <>
                      <h2 className="text-4xl font-bold mb-2">{formatCurrency(proximaCobranca.valor)}</h2>
                      <p className="flex items-center gap-2 text-kondo-purple-100 text-sm">
                        <Clock className="w-4 h-4" /> Vence em {formatDateBr(proximaCobranca.vencimento)}
                      </p>
                    </>
                  ) : (
                    <h2 className="text-2xl font-bold">Tudo em dia! ✅</h2>
                  )}
                </div>
                {proximaCobranca && (
                  <button 
                    onClick={() => setShowPixModal(true)}
                    className="bg-white text-kondo-purple-700 px-8 py-4 rounded-2xl font-bold flex items-center gap-3 hover:bg-kondo-purple-50 transition-all shadow-xl group"
                  >
                    <QrCode className="w-6 h-6" /> Pagar com Pix
                    <ArrowRight className="w-5 h-5 group-hover:translate-x-1 transition-transform" />
                  </button>
                )}
              </div>
            </Card>

            {/* Historico Recente */}
            <Card>
              <div className="p-6 border-b border-kondo-gray-100">
                <h3 className="font-bold text-kondo-gray-900">Histórico de Cobranças</h3>
              </div>
              <div className="divide-y divide-kondo-gray-100">
                {cobrancasPage?.content.map((c) => (
                  <div key={c.id} className="p-6 flex items-center justify-between hover:bg-kondo-gray-50 transition-colors">
                    <div className="flex items-center gap-4">
                      <div className={`w-10 h-10 rounded-lg flex items-center justify-center ${
                        c.status === StatusCobranca.PAGA ? 'bg-kondo-green-100 text-kondo-green-600' : 'bg-kondo-gray-100 text-kondo-gray-500'
                      }`}>
                        <CreditCard className="w-5 h-5" />
                      </div>
                      <div>
                        <p className="text-sm font-bold text-kondo-gray-900">Competência {c.competencia}</p>
                        <p className="text-xs text-kondo-gray-500">Vencimento {formatDateBr(c.vencimento)}</p>
                      </div>
                    </div>
                    <div className="text-right">
                      <p className="text-sm font-bold text-kondo-gray-900">{formatCurrency(c.valor)}</p>
                      <span className={`text-[10px] font-bold uppercase px-2 py-0.5 rounded ${
                        c.status === StatusCobranca.PAGA ? 'bg-kondo-green-100 text-kondo-green-700' : 'bg-kondo-orange-100 text-kondo-orange-700'
                      }`}>
                        {c.status}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </Card>
          </div>

          {/* Sidebar Info */}
          <div className="space-y-6">
            <FinancialCard
              label="Total Pago no Ano"
              value={formatCurrency(12500)} // Mock por enquanto
              icon={CheckCircle2}
              iconColor="bg-kondo-green-100 text-kondo-green-600"
              subtitle="Obrigado pela pontualidade!"
            />
            
            <Card className="p-6 bg-kondo-teal-50 border-kondo-teal-100">
              <div className="flex items-start gap-4">
                <div className="bg-white p-2 rounded-lg shadow-sm">
                  <AlertCircle className="w-6 h-6 text-kondo-teal-600" />
                </div>
                <div>
                  <h4 className="font-bold text-kondo-gray-900 text-sm mb-1">Dúvida sobre o boleto?</h4>
                  <p className="text-xs text-kondo-gray-600 mb-3 leading-relaxed">
                    Entre em contato com a administração através da central de chamados.
                  </p>
                  <button className="text-xs font-bold text-kondo-teal-700 hover:underline">Abrir Chamado</button>
                </div>
              </div>
            </Card>
          </div>
        </div>
      </div>

      {/* Pix Modal Overlay */}
      {showPixModal && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-kondo-gray-900/60 backdrop-blur-sm animate-in fade-in duration-300">
          <Card className="w-full max-w-md overflow-hidden animate-in zoom-in-95 duration-300">
            <div className="p-6 border-b border-kondo-gray-100 flex items-center justify-between">
              <h3 className="font-bold text-kondo-gray-900">Pagamento com Pix</h3>
              <button onClick={() => setShowPixModal(false)} className="text-kondo-gray-400 hover:text-kondo-gray-600">
                <AlertCircle className="w-6 h-6 rotate-45" />
              </button>
            </div>
            
            <div className="p-8 flex flex-col items-center text-center">
              {loadingPix ? (
                <div className="w-48 h-48 bg-kondo-gray-100 rounded-2xl animate-pulse flex items-center justify-center">
                  <Clock className="w-10 h-10 text-kondo-gray-300 animate-spin" />
                </div>
              ) : (
                <>
                  <div className="bg-white p-4 rounded-3xl border-4 border-kondo-purple-100 shadow-inner mb-6">
                    <img 
                      src={`data:image/png;base64,${pixData?.pixQrCodeBase64}`} 
                      alt="QR Code Pix"
                      className="w-48 h-48"
                    />
                  </div>
                  
                  <div className="w-full space-y-4">
                    <div className="p-4 bg-kondo-gray-50 rounded-xl border border-kondo-gray-200 text-left">
                      <p className="text-[10px] text-kondo-gray-500 font-bold uppercase mb-1">Copia e Cola</p>
                      <div className="flex items-center gap-2">
                        <p className="text-xs font-mono text-kondo-gray-600 truncate flex-1">
                          {pixData?.pixCopiaCola}
                        </p>
                        <button 
                          onClick={copyToClipboard}
                          className={`p-2 rounded-lg transition-all ${copied ? 'bg-kondo-green-500 text-white' : 'bg-white border border-kondo-gray-200 text-kondo-purple-600 hover:bg-kondo-purple-50'}`}
                        >
                          {copied ? <CheckCircle2 className="w-4 h-4" /> : <Copy className="w-4 h-4" />}
                        </button>
                      </div>
                    </div>

                    <a 
                      href={pixData?.urlPagamentoExterno} 
                      target="_blank" 
                      rel="noreferrer"
                      className="flex items-center justify-center gap-2 text-sm font-bold text-kondo-gray-500 hover:text-kondo-gray-700 transition-colors py-2"
                    >
                      Pagar pelo site do banco <ExternalLink className="w-4 h-4" />
                    </a>
                  </div>

                  <div className="mt-8 flex items-center gap-2 px-4 py-2 bg-kondo-orange-50 text-kondo-orange-700 rounded-full text-[10px] font-bold uppercase">
                    <Clock className="w-4 h-4" /> 
                    Aguardando confirmação em tempo real...
                  </div>
                </>
              )}
            </div>
          </Card>
        </div>
      )}
    </MainLayout>
  );
}
