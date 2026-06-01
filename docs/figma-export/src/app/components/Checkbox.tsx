import { forwardRef, InputHTMLAttributes } from 'react';
import { Check } from 'lucide-react';

interface CheckboxProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'type'> {
  label?: string;
  description?: string;
}

export const Checkbox = forwardRef<HTMLInputElement, CheckboxProps>(
  ({ label, description, className = '', id, ...props }, ref) => {
    const checkboxId = id || `checkbox-${Math.random().toString(36).substr(2, 9)}`;

    return (
      <div className="flex items-start gap-3">
        <div className="flex items-center h-6">
          <div className="relative">
            <input
              ref={ref}
              type="checkbox"
              id={checkboxId}
              className="peer sr-only"
              {...props}
            />
            <label
              htmlFor={checkboxId}
              className={`
                w-5 h-5 border-2 rounded-md cursor-pointer
                flex items-center justify-center
                transition-all duration-200
                peer-focus:ring-2 peer-focus:ring-kondo-purple-500 peer-focus:ring-offset-2
                peer-disabled:opacity-50 peer-disabled:cursor-not-allowed
                peer-checked:bg-kondo-purple-600 peer-checked:border-kondo-purple-600
                peer-hover:border-kondo-purple-500
                border-kondo-gray-300 bg-white
                ${className}
              `}
            >
              <Check
                className="w-3.5 h-3.5 text-white opacity-0 peer-checked:opacity-100 transition-opacity"
                strokeWidth={3}
              />
            </label>
          </div>
        </div>

        {(label || description) && (
          <div className="flex-1">
            {label && (
              <label
                htmlFor={checkboxId}
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
      </div>
    );
  }
);

Checkbox.displayName = 'Checkbox';
