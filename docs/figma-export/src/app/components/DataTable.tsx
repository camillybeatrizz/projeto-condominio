import { useState, ReactNode } from 'react';
import { ChevronUp, ChevronDown, ChevronsUpDown, Eye, Pencil, Trash2, Download, ChevronLeft, ChevronRight } from 'lucide-react';

export interface Column<T> {
  key: string;
  label: string;
  sortable?: boolean;
  render?: (row: T) => ReactNode;
  width?: string;
}

export interface DataTableProps<T> {
  columns: Column<T>[];
  data: T[];
  keyExtractor: (row: T) => string | number;
  onRowClick?: (row: T) => void;
  actions?: {
    onView?: (row: T) => void;
    onEdit?: (row: T) => void;
    onDelete?: (row: T) => void;
    onDownload?: (row: T) => void;
  };
  zebraStripe?: boolean;
  pagination?: {
    currentPage: number;
    totalPages: number;
    pageSize: number;
    totalItems: number;
    onPageChange: (page: number) => void;
  };
}

export function DataTable<T extends Record<string, any>>({
  columns,
  data,
  keyExtractor,
  onRowClick,
  actions,
  zebraStripe = true,
  pagination,
}: DataTableProps<T>) {
  const [sortConfig, setSortConfig] = useState<{
    key: string;
    direction: 'asc' | 'desc';
  } | null>(null);

  const handleSort = (columnKey: string) => {
    let direction: 'asc' | 'desc' = 'asc';
    if (sortConfig && sortConfig.key === columnKey && sortConfig.direction === 'asc') {
      direction = 'desc';
    }
    setSortConfig({ key: columnKey, direction });
  };

  const sortedData = [...data].sort((a, b) => {
    if (!sortConfig) return 0;

    const aValue = a[sortConfig.key];
    const bValue = b[sortConfig.key];

    if (aValue < bValue) return sortConfig.direction === 'asc' ? -1 : 1;
    if (aValue > bValue) return sortConfig.direction === 'asc' ? 1 : -1;
    return 0;
  });

  const hasActions = actions && (actions.onView || actions.onEdit || actions.onDelete || actions.onDownload);

  return (
    <div className="bg-white rounded-xl shadow-sm border border-kondo-gray-200 overflow-hidden">
      {/* Table */}
      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="bg-kondo-gray-50 border-b border-kondo-gray-200">
            <tr>
              {columns.map((column) => (
                <th
                  key={column.key}
                  className={`px-6 py-4 text-left text-xs font-semibold text-kondo-gray-700 uppercase tracking-wider ${
                    column.sortable ? 'cursor-pointer select-none hover:bg-kondo-gray-100' : ''
                  }`}
                  onClick={() => column.sortable && handleSort(column.key)}
                  style={{ width: column.width }}
                >
                  <div className="flex items-center gap-2">
                    {column.label}
                    {column.sortable && (
                      <span className="text-kondo-gray-400">
                        {sortConfig?.key === column.key ? (
                          sortConfig.direction === 'asc' ? (
                            <ChevronUp className="w-4 h-4" />
                          ) : (
                            <ChevronDown className="w-4 h-4" />
                          )
                        ) : (
                          <ChevronsUpDown className="w-4 h-4" />
                        )}
                      </span>
                    )}
                  </div>
                </th>
              ))}
              {hasActions && (
                <th className="px-6 py-4 text-right text-xs font-semibold text-kondo-gray-700 uppercase tracking-wider">
                  Ações
                </th>
              )}
            </tr>
          </thead>
          <tbody className="divide-y divide-kondo-gray-200">
            {sortedData.map((row, index) => (
              <tr
                key={keyExtractor(row)}
                className={`
                  transition-colors
                  ${zebraStripe && index % 2 === 1 ? 'bg-kondo-gray-50/50' : 'bg-white'}
                  ${onRowClick ? 'cursor-pointer hover:bg-kondo-purple-50/30' : ''}
                `}
                onClick={() => onRowClick?.(row)}
              >
                {columns.map((column) => (
                  <td key={column.key} className="px-6 py-4 text-sm text-kondo-gray-700">
                    {column.render ? column.render(row) : row[column.key]}
                  </td>
                ))}
                {hasActions && (
                  <td className="px-6 py-4 text-right">
                    <div className="flex items-center justify-end gap-2">
                      {actions.onView && (
                        <button
                          onClick={(e) => {
                            e.stopPropagation();
                            actions.onView!(row);
                          }}
                          className="p-1.5 text-kondo-gray-600 hover:text-kondo-purple-600 hover:bg-kondo-purple-50 rounded-lg transition-colors"
                          title="Visualizar"
                        >
                          <Eye className="w-4 h-4" />
                        </button>
                      )}
                      {actions.onEdit && (
                        <button
                          onClick={(e) => {
                            e.stopPropagation();
                            actions.onEdit!(row);
                          }}
                          className="p-1.5 text-kondo-gray-600 hover:text-kondo-purple-600 hover:bg-kondo-purple-50 rounded-lg transition-colors"
                          title="Editar"
                        >
                          <Pencil className="w-4 h-4" />
                        </button>
                      )}
                      {actions.onDownload && (
                        <button
                          onClick={(e) => {
                            e.stopPropagation();
                            actions.onDownload!(row);
                          }}
                          className="p-1.5 text-kondo-gray-600 hover:text-kondo-green-600 hover:bg-kondo-green-50 rounded-lg transition-colors"
                          title="Download"
                        >
                          <Download className="w-4 h-4" />
                        </button>
                      )}
                      {actions.onDelete && (
                        <button
                          onClick={(e) => {
                            e.stopPropagation();
                            actions.onDelete!(row);
                          }}
                          className="p-1.5 text-kondo-gray-600 hover:text-kondo-red-600 hover:bg-kondo-red-50 rounded-lg transition-colors"
                          title="Deletar"
                        >
                          <Trash2 className="w-4 h-4" />
                        </button>
                      )}
                    </div>
                  </td>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      {pagination && (
        <div className="px-6 py-4 border-t border-kondo-gray-200 bg-kondo-gray-50/50">
          <div className="flex items-center justify-between">
            <div className="text-sm text-kondo-gray-600">
              Mostrando{' '}
              <span className="font-medium text-kondo-gray-900">
                {(pagination.currentPage - 1) * pagination.pageSize + 1}
              </span>{' '}
              a{' '}
              <span className="font-medium text-kondo-gray-900">
                {Math.min(pagination.currentPage * pagination.pageSize, pagination.totalItems)}
              </span>{' '}
              de{' '}
              <span className="font-medium text-kondo-gray-900">{pagination.totalItems}</span>{' '}
              resultados
            </div>

            <div className="flex items-center gap-2">
              <button
                onClick={() => pagination.onPageChange(pagination.currentPage - 1)}
                disabled={pagination.currentPage === 1}
                className="p-2 text-kondo-gray-600 hover:text-kondo-purple-600 hover:bg-white rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:text-kondo-gray-600 disabled:hover:bg-transparent"
              >
                <ChevronLeft className="w-5 h-5" />
              </button>

              <div className="flex items-center gap-1">
                {Array.from({ length: pagination.totalPages }, (_, i) => i + 1)
                  .filter((page) => {
                    const current = pagination.currentPage;
                    return (
                      page === 1 ||
                      page === pagination.totalPages ||
                      (page >= current - 1 && page <= current + 1)
                    );
                  })
                  .map((page, index, array) => {
                    const showEllipsis = index > 0 && page - array[index - 1] > 1;
                    return (
                      <div key={page} className="flex items-center gap-1">
                        {showEllipsis && (
                          <span className="px-2 text-kondo-gray-400">...</span>
                        )}
                        <button
                          onClick={() => pagination.onPageChange(page)}
                          className={`min-w-[2.5rem] px-3 py-2 text-sm font-medium rounded-lg transition-colors ${
                            page === pagination.currentPage
                              ? 'bg-kondo-purple-600 text-white'
                              : 'text-kondo-gray-700 hover:bg-white hover:text-kondo-purple-600'
                          }`}
                        >
                          {page}
                        </button>
                      </div>
                    );
                  })}
              </div>

              <button
                onClick={() => pagination.onPageChange(pagination.currentPage + 1)}
                disabled={pagination.currentPage === pagination.totalPages}
                className="p-2 text-kondo-gray-600 hover:text-kondo-purple-600 hover:bg-white rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:text-kondo-gray-600 disabled:hover:bg-transparent"
              >
                <ChevronRight className="w-5 h-5" />
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
