import { render, screen, act } from '@testing-library/react';
import AuthProvider from '../../context/AuthProvider';
import useAuth from '../useAuth';

// Test component that uses the hook
const TestComponent = () => {
  const { user, login, logout } = useAuth();
  return (
    <div>
      <div data-testid="user">{user ? 'logged in' : 'not logged in'}</div>
      <button onClick={() => login('test-token')}>Login</button>
      <button onClick={logout}>Logout</button>
    </div>
  );
};

describe('useAuth hook', () => {
  test('throws error when used outside AuthProvider', () => {
    // Mock console.error to avoid noise in test output
    const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});

    expect(() => render(<TestComponent />)).toThrow('User must logged in.');

    consoleSpy.mockRestore();
  });

  test('provides auth context when used inside AuthProvider', () => {
    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    expect(screen.getByTestId('user')).toHaveTextContent('not logged in');
  });

  test('login function updates user state', () => {
    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    const loginButton = screen.getByText('Login');
    act(() => {
      loginButton.click();
    });

    expect(screen.getByTestId('user')).toHaveTextContent('logged in');
  });

  test('logout function clears user state', () => {
    // Start with logged in state
    localStorage.setItem('token', 'test-token');

    render(
      <AuthProvider>
        <TestComponent />
      </AuthProvider>
    );

    expect(screen.getByTestId('user')).toHaveTextContent('logged in');

    const logoutButton = screen.getByText('Logout');
    act(() => {
      logoutButton.click();
    });

    expect(screen.getByTestId('user')).toHaveTextContent('not logged in');
  });
});