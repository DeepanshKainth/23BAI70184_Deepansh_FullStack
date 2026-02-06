import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchLogs } from "../store/logsSlice";

const Logs = () => {
  const dispatch = useDispatch();
  const { data: logs, status, error } = useSelector((state) => state.logs);

  useEffect(() => {
    if (status === "idle") {
      dispatch(fetchLogs());
    }
  }, [status, dispatch]);

  if (status === "loading") {
    return <div>Loading...</div>;
  }

  if (status === "failed") {
    return <div>Error: {error}</div>;
  }
  return (
    <div style={{ padding: "20px" }}>
      <h3>Activity Logs(Redux)</h3>
      <button onClick={() => dispatch(fetchLogs())} style={{ marginBottom: "20px", padding: "10px 20px", cursor: "pointer" }}>
        Refresh Logs
      </button>
      <ul>
        {logs.map((log) => (
          <li
            key={log.id}>{log.activity} = {log.carbon} Kg</li>
        ))}
      </ul>
    </div>
  )
}



// const Logs = ({ logs }) => {
//   const filteredArray = logs.filter((log) => {
//     if (log.carbon >= 4) {
//       return log;
//     }
//   });
//   const Array = logs.filter((log) => {
//     if (log.carbon < 4) {
//       return log;
//     }
//   });
//   return (
//     <ul>
//       <h2>High Carbon Activity</h2>
//       {filteredArray.map((log) => (
//         <li style={{ color: "red" }} key={log.id}>
//           {log.activity} = {log.carbon} Kg
//         </li>
//       ))}
//       <h2>Low Carbon Activity</h2>
//       {Array.map((log) => (
//         <li style={{ color: "green" }} key={log.id}>
//           {log.activity} = {log.carbon} Kg
//         </li>
//       ))}
//     </ul>
//   );
// };

export default Logs;

