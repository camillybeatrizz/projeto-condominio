import { forwardRef, InputHTMLAttributes } from 'react';

interface ToggleProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'type'> {
  label?: string;
  description?: string;
}

export const Toggle = forwardRef<HTMLInputElement, ToggleProps>(
  ({ label, description, className = '', id, ...props }, ref) => {
    const toggleId = id || `toggle-${Math.random().toString(36).substr(2, 9)}`;

    return (
      <div className="flex items-start justify-between gap-4">
        {(label || description) && (
          <div className="flex-1">
            {label && (
              <label
                htmlFor={toggleId}
                className="block text-sm font-medium text-kondo-gray-900 cursor-pointer select-none"
              >
                {label}
              </label>
            )}
            {description && (
              <p className="text-sm text-kondo-gray-600 mt-0.5">
                {description}
              </p>
            )}
          </div>
        )}

        <div className="flex items-center h-6">
          <div className="relative">
            <input
              ref={ref}
              type="checkbox"
              id={toggleId}
              className="peer sr-only"
              {...props}
            />
            <label
              htmlFor={toggleId}
              className={`
                w-11 h-6 rounded-full cursor-pointer
                flex items-center px-0.5
                transition-all duration-200
                peer-focus:ring-2 peer-focus:ring-kondo-purple-500 peer-focus:ring-offset-2
                peer-disabled:opacity-50 peer-disabled:cursor-not-allowed
                peer-checked:bg-kondo-purple-600
                bg-kondo-gray-300
                ${className}
              `}
            >
              <div className="
                w-5 h-5 bg-white rounded-full shadow-sm
                transition-transform duration-200
                peer-checked:translate-x-5
              " />
            </label>
          </div>
        </div>
      </div>
    );
  }
);

Toggle.displayName = 'Toggle';
