import { forwardRef, ButtonHTMLAttributes } from 'react';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'ghost' | 'danger' | 'success';
  size?: 'sm' | 'md' | 'lg';
  children: React.ReactNode;
}

export const Button = forwardRef<HTMLButtonElement, ButtonProps>(
  ({ variant = 'primary', size = 'md', children, className = '', disabled, ...props }, ref) => {
    const baseStyles = 'inline-flex items-center justify-center font-medium rounded-lg transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed disabled:pointer-events-none';

    const variants = {
      primary: 'bg-kondo-purple-600 text-white hover:bg-kondo-purple-700 focus:ring-kondo-purple-500 shadow-sm active:scale-95',
      secondary: 'bg-white text-kondo-gray-700 border border-kondo-gray-300 hover:bg-kondo-gray-50 focus:ring-kondo-purple-500 active:scale-95',
      ghost: 'bg-transparent text-kondo-purple-600 hover:bg-kondo-purple-50 focus:ring-kondo-purple-500 active:scale-95',
      danger: 'bg-kondo-red-600 text-white hover:bg-kondo-red-700 focus:ring-kondo-red-500 shadow-sm active:scale-95',
      success: 'bg-kondo-green-600 text-white hover:bg-kondo-green-700 focus:ring-kondo-green-500 shadow-sm active:scale-95',
    };

    const sizes = {
      sm: 'px-3 py-1.5 text-sm',
      md: 'px-4 py-2.5 text-base',
      lg: 'px-6 py-3 text-lg',
    };

    return (
      <button
        ref={ref}
        className={`${baseStyles} ${variants[variant]} ${sizes[size]} ${className}`}
        disabled={disabled}
        {...props}
      >
        {children}
      </button>
    );
  }
);

Button.displayName = 'Button';
