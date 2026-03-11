import { render } from '@testing-library/react';
import Login from '../Login';

describe('Login component', () => {
  test('renders correctly', () => {
    const { container } = render(<Login />);
    expect(container.firstChild).toMatchSnapshot();
  });

  test('displays login message', () => {
    const { getByText } = render(<Login />);
    expect(getByText('You are not Logged in.')).toBeInTheDocument();
  });
});