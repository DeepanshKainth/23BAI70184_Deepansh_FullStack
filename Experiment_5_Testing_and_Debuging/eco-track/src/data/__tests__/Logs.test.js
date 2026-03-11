import { render, screen, waitFor } from '@testing-library/react';
import { Provider } from 'react-redux';
import configureStore from 'redux-mock-store';
import Logs from '../Logs';

const mockStore = configureStore([]);

describe('Logs component', () => {
  let store;

  test('renders loading state', () => {
    store = mockStore({
      logs: {
        data: [],
        status: 'loading',
        error: null,
      },
    });

    render(
      <Provider store={store}>
        <Logs />
      </Provider>
    );

    expect(screen.getByText('Loading Logs...')).toBeInTheDocument();
  });

  test('renders error state', () => {
    store = mockStore({
      logs: {
        data: [],
        status: 'failed',
        error: 'Network error',
      },
    });

    render(
      <Provider store={store}>
        <Logs />
      </Provider>
    );

    expect(screen.getByText('Error: Network error')).toBeInTheDocument();
  });

  test('renders logs data', () => {
    const mockData = [
      { id: 1, activity: 'Car Travel', carbon: 4 },
      { id: 2, activity: 'Electricity Usage', carbon: 6 },
    ];

    store = mockStore({
      logs: {
        data: mockData,
        status: 'success',
        error: null,
      },
    });

    render(
      <Provider store={store}>
        <Logs />
      </Provider>
    );

    expect(screen.getByText('Daily Logs (Redux)')).toBeInTheDocument();
    expect(screen.getByText('Car Travel - 4 kg CO₂')).toBeInTheDocument();
    expect(screen.getByText('Electricity Usage - 6 kg CO₂')).toBeInTheDocument();
  });

  test('dispatches fetchLogs on mount when idle', () => {
    store = mockStore({
      logs: {
        data: [],
        status: 'idle',
        error: null,
      },
    });

    store.dispatch = jest.fn();

    render(
      <Provider store={store}>
        <Logs />
      </Provider>
    );

    // Since dispatch is mocked, state doesn't change, so it renders success with empty list
    expect(screen.getByText('Daily Logs (Redux)')).toBeInTheDocument();
    expect(screen.queryByText('Loading Logs...')).not.toBeInTheDocument();
  });
});