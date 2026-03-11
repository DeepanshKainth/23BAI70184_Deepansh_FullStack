import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchLogs } from "../features/logsSlice";
import React from "react" ;

const Logs = () => {
  const dispatch = useDispatch();
  const { data, status, error } = useSelector((state) => state.logs);

  useEffect(() => {
    if (status === "idle") {
      dispatch(fetchLogs());
    }
  }, [status, dispatch]);

  if (status === "loading") {
    return <p>Loading Logs...</p>;
  }

  if (status === "failed") {
    return <p>Error: {error}</p>;
  }

  return (
    <div className="flex items-center justify-center min-h-screen px-4 sm:px-6 md:px-10">
            <div className="flex items-center justify-center w-full max-w-xl min-h-52 rounded-xl bg-blue-300 ">
                <h3>Daily Logs (Redux)</h3>
                <ul>
                  {data.map((log) => (
                    <li key={log.id}>
                      {log.activity} - {log.carbon} kg CO₂
                    </li>
                  ))}
                </ul>
                
            </div>
            <button onClick={() => dispatch(fetchLogs())} className = "mt-1.5 border-2">Refresh</button>
        </div>
  );
};

export default Logs;
