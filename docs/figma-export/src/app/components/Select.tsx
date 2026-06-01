import { forwardRef, SelectHTMLAttributes } from 'react';
import { ChevronDown, AlertCircle } from 'lucide-react';

interface SelectOption {
  value: string;
  label: string;
  disabled?: boolean;
}

interface SelectProps extends SelectHTMLAttributes<HTMLSelectElement> {
  label?: string;
  error?: string;
  helperText?: string;
  options: SelectOption[];
  placeholder?: string;
}

export const Select = forwardRef<HTMLSelectElement, SelectProps>(
  ({ label, error, helperText, options, placeholder, className = '', ...props }, ref) => {
    const hasError = !!error;

    return (
      <div className="w-full">
        {label && (
          <label className="block text-sm font-medium text-kondo-gray-700 mb-2">
            {label}
          </label>
        )}

        <div className="relative">
          {/* Select Field */}
          <select
            ref={ref}
            className={`
              w-full px-4 py-2.5 bg-kondo-gray-50 border rounded-lg
              text-kondo-gray-900
              appearance-none cursor-pointer
              transition-all duration-200
              focus:outline-none focus:ring-2 focus:border-transparent
              disabled:opacity-50 disabled:cursor-not-allowed disabled:bg-kondo-gray-100
              ${hasError
                ? 'border-kondo-red-500 focus:ring-kondo-red-500'
                : 'border-kondo-gray-300 focus:ring-kondo-purple-600'
              }
              ${hasError ? 'pr-16' : 'pr-10'}
              ${className}
            `}
            {...props}
          >
            {placeholder && (
              <option value="" disabled>
                {placeholder}
              </option>
            )}
            {options.map((option) => (
              <option
                key={option.value}
                value={option.value}
                disabled={option.disabled}
              >
                {option.label}
              </option>
            ))}
          </select>

          {/* Chevron Icon */}
          <div className={`absolute top-1/2 -translate-y-1/2 text-kondo-gray-400 pointer-events-none ${hasError ? 'right-9' : 'right-3'}`}>
            <ChevronDown className="w-5 h-5" />
          </div>

          {/* Error Icon */}
          {hasError && (
            <div className="absolute right-3 top-1/2 -translate-y-1/2 text-kondo-red-500 pointer-events-none">
              <AlertCircle className="w-5 h-5" />
            </div>
          )}
        </div>

        {/* Helper Text or Error Message */}
        {(error || helperText) && (
          <p className={`mt-1.5 text-sm ${hasError ? 'text-kondo-red-600' : 'text-kondo-gray-500'}`}>
            {error || helperText}
          </p>
        )}
      </div>
    );
  }
);

Select.displayName = 'Select';
