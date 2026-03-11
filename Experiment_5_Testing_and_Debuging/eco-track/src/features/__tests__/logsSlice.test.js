import logsReducer, { fetchLogs } from '../logsSlice';

describe('logsSlice', () => {
  const initialState = {
    data: [],
    status: 'idle',
    error: null,
  };

  test('should return the initial state', () => {
    expect(logsReducer(undefined, { type: undefined })).toEqual(initialState);
  });

  test('should handle fetchLogs.pending', () => {
    const action = { type: fetchLogs.pending.type };
    const state = logsReducer(initialState, action);
    expect(state.status).toBe('loading');
  });

  test('should handle fetchLogs.fulfilled', () => {
    const mockData = [
      { id: 1, activity: 'Car Travel', carbon: 4 },
      { id: 2, activity: 'Electricity Usage', carbon: 6 },
    ];
    const action = { type: fetchLogs.fulfilled.type, payload: mockData };
    const state = logsReducer({ ...initialState, status: 'loading' }, action);
    expect(state.status).toBe('success');
    expect(state.data).toEqual(mockData);
  });

  test('should handle fetchLogs.rejected', () => {
    const errorMessage = 'Failed to fetch';
    const action = { type: fetchLogs.rejected.type, error: { message: errorMessage } };
    const state = logsReducer({ ...initialState, status: 'loading' }, action);
    expect(state.status).toBe('failed');
    expect(state.error).toBe(errorMessage);
  });

  describe('fetchLogs thunk', () => {
    test('should fetch logs successfully', async () => {
      const dispatch = jest.fn();
      const thunk = fetchLogs();

      await thunk(dispatch, () => ({}), undefined);

      const pendingAction = dispatch.mock.calls[0][0];
      expect(pendingAction.type).toBe(fetchLogs.pending.type);

      const fulfilledAction = dispatch.mock.calls[1][0];
      expect(fulfilledAction.type).toBe(fetchLogs.fulfilled.type);
      expect(fulfilledAction.payload).toEqual([
        { id: 1, activity: 'Car Travel', carbon: 4 },
        { id: 2, activity: 'Electricity Usage', carbon: 6 },
        { id: 3, activity: 'Cycling', carbon: 0 }
      ]);
    });
  });
});