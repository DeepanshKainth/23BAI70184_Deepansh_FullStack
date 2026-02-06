const Logs = ({ logs }) => {
  const filteredArray = logs.filter((log) => {
    if (log.carbon >= 4) {
      return log;
    }
  });
  const Array = logs.filter((log) => {
    if (log.carbon < 4) {
      return log;
    }
  });
  return (
    <ul>
      <h2>High Carbon Activity</h2>
      {filteredArray.map((log) => (
        <li style={{ color: "red" }} key={log.id}>
          {log.activity} = {log.carbon} Kg
        </li>
      ))}
      <h2>Low Carbon Activity</h2>
      {Array.map((log) => (
        <li style={{ color: "green" }} key={log.id}>
          {log.activity} = {log.carbon} Kg
        </li>
      ))}
    </ul>
  );
};

export default Logs;

