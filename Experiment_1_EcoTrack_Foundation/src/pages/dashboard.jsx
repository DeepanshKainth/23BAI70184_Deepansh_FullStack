import logs from "..data/logs";

const Dashboard = () => {
    const totalCarbon = logs.reduce((total, log) => 
        total + log.carbon, 0);
};
return (
    <div>
        <h1>Dashboard</h1>
        <p>Total Carbon Footprint: {totalCarbon} kg CO2</p>

        <h2>Activity Logs</h2>
        <ul>
            {logs.map(log => (
                <li key={log.id}>
                    {log.activity}: {log.carbon} kg CO2
                </li>
            ))}
        </ul>
    </div>
)

export default Dashboard;
