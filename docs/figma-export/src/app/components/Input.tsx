import { forwardRef, InputHTMLAttributes } from 'react';
import { Search, Eye, EyeOff, AlertCircle } from 'lucide-react';
import { useState } from 'react';

interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
  helperText?: string;
  leftIcon?: React.ReactNode;
  variant?: 'text' | 'password' | 'search';
}

export const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ label, error, helperText, leftIcon, variant = 'text', type, className = '', ...props }, ref) => {
    const [showPassword, setShowPassword] = useState(false);

    const inputType = variant === 'password'
      ? (showPassword ? 'text' : 'password')
      : variant === 'search'
        ? 'search'
        : type || 'text';

    const hasError = !!error;

    return (
      <div className="w-full">
        {label && (
          <label className="block text-sm font-medium text-kondo-gray-700 mb-2">
            {label}
          </label>
        )}

        <div className="relative">
          {/* Left Icon */}
          {(leftIcon || variant === 'search') && (
            <div className="absolute left-3 top-1/2 -translate-y-1/2 text-kondo-gray-400">
              {variant === 'search' ? <Search className="w-5 h-5" /> : leftIcon}
            </div>
          )}

          {/* Input Field */}
          <input
            ref={ref}
            type={inputType}
            className={`
              w-full px-4 py-2.5 bg-kondo-gray-50 border rounded-lg
              text-kondo-gray-900 placeholder:text-kondo-gray-400
              transition-all duration-200
              focus:outline-none focus:ring-2 focus:border-transparent
              disabled:opacity-50 disabled:cursor-not-allowed disabled:bg-kondo-gray-100
              ${hasError
                ? 'border-kondo-red-500 focus:ring-kondo-red-500'
                : 'border-kondo-gray-300 focus:ring-kondo-purple-600'
              }
              ${(leftIcon || variant === 'search') ? 'pl-10' : ''}
              ${variant === 'password' ? 'pr-10' : ''}
              ${className}
            `}
            {...props}
          />

          {/* Password Toggle */}
          {variant === 'password' && (
            <button
              type="button"
              onClick={() => setShowPassword(!showPassword)}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-kondo-gray-400 hover:text-kondo-gray-600 transition-colors"
            >
              {showPassword ? <EyeOff className="w-5 h-5" /> : <Eye className="w-5 h-5" />}
            </button>
          )}

          {/* Error Icon */}
          {hasError && variant !== 'password' && (
            <div className="absolute right-3 top-1/2 -translate-y-1/2 text-kondo-red-500">
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

Input.displayName = 'Input';
