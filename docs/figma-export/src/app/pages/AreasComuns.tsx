import { useState } from 'react';
import { ChevronLeft, ChevronRight, Calendar as CalendarIcon, Clock, User, MapPin, Plus, X } from 'lucide-react';
import { Button } from '../components/Button';
import { Select } from '../components/Select';
import { Input } from '../components/Input';

interface Reservation {
  id: string;
  area: string;
  date: string;
  startTime: string;
  endTime: string;
  user: string;
  unit: string;
  status: 'confirmed' | 'pending' | 'cancelled';
}

const areas = [
  { id: 'salao', name: 'Salão de Festas', capacity: 80, color: 'bg-kondo-purple-500' },
  { id: 'churrasqueira', name: 'Churrasqueira', capacity: 20, color: 'bg-kondo-orange-500' },
  { id: 'piscina', name: 'Piscina', capacity: 30, color: 'bg-kondo-teal-500' },
  { id: 'academia', name: 'Academia', capacity: 15, color: 'bg-kondo-green-500' },
  { id: 'quadra', name: 'Quadra de Esportes', capacity: 12, color: 'bg-kondo-red-500' },
];

const mockReservations: Reservation[] = [
  { id: '1', area: 'salao', date: '2026-06-05', startTime: '18:00', endTime: '23:00', user: 'Maria Santos', unit: 'Apt 504', status: 'confirmed' },
  { id: '2', area: 'churrasqueira', date: '2026-06-06', startTime: '12:00', endTime: '16:00', user: 'João Silva', unit: 'Apt 301', status: 'confirmed' },
  { id: '3', area: 'piscina', date: '2026-06-07', startTime: '14:00', endTime: '18:00', user: 'Pedro Almeida', unit: 'Apt 101', status: 'pending' },
  { id: '4', area: 'academia', date: '2026-06-08', startTime: '07:00', endTime: '08:00', user: 'Lucia Ferreira', unit: 'Apt 102', status: 'confirmed' },
  { id: '5', area: 'salao', date: '2026-06-12', startTime: '19:00', endTime: '23:00', user: 'Roberto Souza', unit: 'Apt 201', status: 'confirmed' },
  { id: '6', area: 'churrasqueira', date: '2026-06-13', startTime: '11:00', endTime: '15:00', user: 'Fernanda Lima', unit: 'Apt 202', status: 'pending' },
];

export function AreasComuns() {
  const [currentDate, setCurrentDate] = useState(new Date(2026, 5, 1)); // June 2026
  const [selectedArea, setSelectedArea] = useState('all');
  const [showNewReservation, setShowNewReservation] = useState(false);
  const [selectedDate, setSelectedDate] = useState<string | null>(null);

  const getDaysInMonth = (date: Date) => {
    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startingDayOfWeek = firstDay.getDay();

    return { daysInMonth, startingDayOfWeek };
  };

  const { daysInMonth, startingDayOfWeek } = getDaysInMonth(currentDate);

  const previousMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1));
  };

  const nextMonth = () => {
    setCurrentDate(new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1));
  };

  const getReservationsForDay = (day: number) => {
    const dateStr = `${currentDate.getFullYear()}-${String(currentDate.getMonth() + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
    return mockReservations.filter(r => {
      const matchesDate = r.date === dateStr;
      const matchesArea = selectedArea === 'all' || r.area === selectedArea;
      return matchesDate && matchesArea;
    });
  };

  const monthNames = ['Janeiro', 'Fevereiro', 'Março', 'Abril', 'Maio', 'Junho', 'Julho', 'Agosto', 'Setembro', 'Outubro', 'Novembro', 'Dezembro'];
  const dayNames = ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'];

  const filteredReservations = mockReservations.filter(r => selectedArea === 'all' || r.area === selectedArea);

  const upcomingReservations = filteredReservations
    .filter(r => new Date(r.date) >= new Date())
    .sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime())
    .slice(0, 5);

  return (
    <div className="p-8 space-y-8">
      {/* Page Header */}
      <div className="flex items-start justify-between">
        <div>
          <h1 className="text-3xl font-bold text-kondo-gray-900 mb-2">Áreas Comuns</h1>
          <p className="text-kondo-gray-600">Gerencie reservas de espaços do condomínio</p>
        </div>
        <Button variant="primary" size="lg" onClick={() => setShowNewReservation(true)}>
          <Plus className="w-5 h-5 mr-2" />
          Nova Reserva
        </Button>
      </div>

      {/* Stats */}
      <div className="grid grid-cols-2 md:grid-cols-5 gap-4">
        {areas.map((area) => (
          <button
            key={area.id}
            onClick={() => setSelectedArea(selectedArea === area.id ? 'all' : area.id)}
            className={`p-4 rounded-xl border-2 transition-all ${
              selectedArea === area.id
                ? `${area.color} text-white border-transparent shadow-lg`
                : 'bg-white border-kondo-gray-200 hover:border-kondo-purple-300'
            }`}
          >
            <div className={`w-10 h-10 ${selectedArea === area.id ? 'bg-white/20' : area.color} rounded-lg flex items-center justify-center mx-auto mb-2`}>
              <MapPin className={`w-5 h-5 ${selectedArea === area.id ? 'text-white' : 'text-white'}`} />
            </div>
            <p className={`text-sm font-semibold ${selectedArea === area.id ? 'text-white' : 'text-kondo-gray-900'}`}>
              {area.name}
            </p>
            <p className={`text-xs ${selectedArea === area.id ? 'text-white/80' : 'text-kondo-gray-600'}`}>
              {mockReservations.filter(r => r.area === area.id).length} reservas
            </p>
          </button>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Calendar */}
        <div className="lg:col-span-2">
          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 overflow-hidden">
            {/* Calendar Header */}
            <div className="bg-gradient-to-r from-kondo-purple-600 to-kondo-purple-700 p-6 text-white">
              <div className="flex items-center justify-between mb-4">
                <button
                  onClick={previousMonth}
                  className="p-2 hover:bg-white/20 rounded-lg transition-colors"
                >
                  <ChevronLeft className="w-5 h-5" />
                </button>
                <h2 className="text-xl font-bold">
                  {monthNames[currentDate.getMonth()]} {currentDate.getFullYear()}
                </h2>
                <button
                  onClick={nextMonth}
                  className="p-2 hover:bg-white/20 rounded-lg transition-colors"
                >
                  <ChevronRight className="w-5 h-5" />
                </button>
              </div>
              {selectedArea !== 'all' && (
                <div className="flex items-center justify-center gap-2">
                  <span className="text-sm text-kondo-purple-100">Filtrando por:</span>
                  <span className="px-3 py-1 bg-white/20 rounded-full text-sm font-medium">
                    {areas.find(a => a.id === selectedArea)?.name}
                  </span>
                  <button
                    onClick={() => setSelectedArea('all')}
                    className="p-1 hover:bg-white/20 rounded transition-colors"
                  >
                    <X className="w-4 h-4" />
                  </button>
                </div>
              )}
            </div>

            {/* Day Names */}
            <div className="grid grid-cols-7 border-b border-kondo-gray-200 bg-kondo-gray-50">
              {dayNames.map((day) => (
                <div key={day} className="p-3 text-center">
                  <span className="text-xs font-semibold text-kondo-gray-600">{day}</span>
                </div>
              ))}
            </div>

            {/* Calendar Grid */}
            <div className="grid grid-cols-7">
              {/* Empty cells for days before month starts */}
              {Array.from({ length: startingDayOfWeek }).map((_, i) => (
                <div key={`empty-${i}`} className="aspect-square border-r border-b border-kondo-gray-200 bg-kondo-gray-50/50"></div>
              ))}

              {/* Days of the month */}
              {Array.from({ length: daysInMonth }).map((_, i) => {
                const day = i + 1;
                const reservations = getReservationsForDay(day);
                const isToday = new Date().getDate() === day &&
                  new Date().getMonth() === currentDate.getMonth() &&
                  new Date().getFullYear() === currentDate.getFullYear();

                return (
                  <div
                    key={day}
                    className={`aspect-square border-r border-b border-kondo-gray-200 p-2 hover:bg-kondo-gray-50 cursor-pointer transition-colors ${
                      isToday ? 'bg-kondo-purple-50' : ''
                    }`}
                    onClick={() => setSelectedDate(`${currentDate.getFullYear()}-${String(currentDate.getMonth() + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`)}
                  >
                    <div className={`text-sm font-semibold mb-1 ${
                      isToday ? 'text-kondo-purple-600' : 'text-kondo-gray-900'
                    }`}>
                      {day}
                    </div>
                    <div className="space-y-1">
                      {reservations.slice(0, 2).map((reservation) => {
                        const area = areas.find(a => a.id === reservation.area);
                        return (
                          <div
                            key={reservation.id}
                            className={`text-xs px-1 py-0.5 ${area?.color} text-white rounded truncate`}
                            title={`${area?.name} - ${reservation.startTime}`}
                          >
                            {reservation.startTime}
                          </div>
                        );
                      })}
                      {reservations.length > 2 && (
                        <div className="text-xs text-kondo-gray-600 font-medium">
                          +{reservations.length - 2}
                        </div>
                      )}
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        </div>

        {/* Sidebar - Upcoming Reservations */}
        <div className="space-y-6">
          <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 p-6">
            <div className="flex items-center gap-2 mb-4">
              <CalendarIcon className="w-5 h-5 text-kondo-purple-600" />
              <h3 className="font-semibold text-kondo-gray-900">Próximas Reservas</h3>
            </div>

            <div className="space-y-3">
              {upcomingReservations.length === 0 ? (
                <p className="text-sm text-kondo-gray-500 text-center py-8">
                  Nenhuma reserva encontrada
                </p>
              ) : (
                upcomingReservations.map((reservation) => {
                  const area = areas.find(a => a.id === reservation.area);
                  const date = new Date(reservation.date + 'T00:00:00');
                  const formattedDate = date.toLocaleDateString('pt-BR', { day: '2-digit', month: 'short' });

                  return (
                    <div
                      key={reservation.id}
                      className="p-3 bg-kondo-gray-50 rounded-lg hover:bg-kondo-gray-100 transition-colors"
                    >
                      <div className="flex items-start gap-3">
                        <div className={`w-10 h-10 ${area?.color} rounded-lg flex items-center justify-center flex-shrink-0`}>
                          <MapPin className="w-5 h-5 text-white" />
                        </div>
                        <div className="flex-1 min-w-0">
                          <p className="text-sm font-semibold text-kondo-gray-900 truncate">
                            {area?.name}
                          </p>
                          <div className="flex items-center gap-2 mt-1 text-xs text-kondo-gray-600">
                            <CalendarIcon className="w-3 h-3" />
                            <span>{formattedDate}</span>
                            <Clock className="w-3 h-3 ml-1" />
                            <span>{reservation.startTime}</span>
                          </div>
                          <div className="flex items-center gap-1 mt-1 text-xs text-kondo-gray-600">
                            <User className="w-3 h-3" />
                            <span>{reservation.user}</span>
                          </div>
                          <span className={`inline-block mt-2 px-2 py-0.5 text-xs rounded-full ${
                            reservation.status === 'confirmed'
                              ? 'bg-kondo-green-100 text-kondo-green-700'
                              : 'bg-kondo-orange-100 text-kondo-orange-700'
                          }`}>
                            {reservation.status === 'confirmed' ? 'Confirmada' : 'Pendente'}
                          </span>
                        </div>
                      </div>
                    </div>
                  );
                })
              )}
            </div>

            {upcomingReservations.length > 0 && (
              <button className="w-full mt-4 px-4 py-2 text-sm font-medium text-kondo-purple-600 hover:text-kondo-purple-700 transition-colors">
                Ver todas as reservas
              </button>
            )}
          </div>

          {/* Availability Info */}
          <div className="bg-gradient-to-br from-kondo-teal-600 to-kondo-teal-700 rounded-xl p-6 text-white shadow-lg">
            <h3 className="font-semibold mb-3">Regras de Reserva</h3>
            <ul className="space-y-2 text-sm text-kondo-teal-50">
              <li className="flex items-start gap-2">
                <span className="text-white mt-0.5">•</span>
                <span>Reservas com até 30 dias de antecedência</span>
              </li>
              <li className="flex items-start gap-2">
                <span className="text-white mt-0.5">•</span>
                <span>Máximo de 2 reservas por mês</span>
              </li>
              <li className="flex items-start gap-2">
                <span className="text-white mt-0.5">•</span>
                <span>Salão de Festas: taxa de limpeza R$ 150</span>
              </li>
              <li className="flex items-start gap-2">
                <span className="text-white mt-0.5">•</span>
                <span>Cancelamento até 48h antes sem custo</span>
              </li>
            </ul>
          </div>
        </div>
      </div>

      {/* New Reservation Modal */}
      {showNewReservation && (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
          <div className="bg-white rounded-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
            <div className="sticky top-0 bg-white border-b border-kondo-gray-200 px-6 py-4 flex items-center justify-between">
              <h2 className="text-xl font-bold text-kondo-gray-900">Nova Reserva</h2>
              <button
                onClick={() => setShowNewReservation(false)}
                className="p-2 hover:bg-kondo-gray-100 rounded-lg transition-colors"
              >
                <X className="w-5 h-5" />
              </button>
            </div>
            <div className="p-6 space-y-6">
              <Select
                label="Área Comum"
                placeholder="Selecione a área"
                options={areas.map(a => ({ value: a.id, label: a.name }))}
                required
              />
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <Input
                  label="Data"
                  type="date"
                  leftIcon={<CalendarIcon className="w-5 h-5" />}
                  required
                />
                <Input
                  label="Horário de Início"
                  type="time"
                  leftIcon={<Clock className="w-5 h-5" />}
                  required
                />
              </div>
              <Input
                label="Horário de Término"
                type="time"
                leftIcon={<Clock className="w-5 h-5" />}
                required
              />
              <Input
                label="Número de Convidados"
                type="number"
                placeholder="Ex: 30"
                required
              />
              <div className="flex gap-3 pt-4">
                <Button variant="primary" className="flex-1">
                  Confirmar Reserva
                </Button>
                <Button
                  variant="secondary"
                  onClick={() => setShowNewReservation(false)}
                >
                  Cancelar
                </Button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
