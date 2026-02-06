const Dashboard = ({ prop }) => {
  const Total = prop.reduce((acc, x) => {
    return acc + x.carbon;
  }, 0);

  return (
    <div>
      <p>Total Carbon Footprint: {Total} Kg</p>
      <ul>
        {prop.map((log) => (
          <li key={log.id}>
            {log.activity} = {log.carbon} KG
          </li>
        ))}
      </ul>
    </div>
  );
};

export default Dashboard;


