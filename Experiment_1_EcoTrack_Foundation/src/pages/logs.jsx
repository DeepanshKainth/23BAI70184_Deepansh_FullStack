import log from "../data/logs";

const logPage = () => {
    const highCarbonLogs = log.filter(log => log.carbon >= 4);
};

return (
    <div>
        <h1>High Carbon Logs</h1>
        <ul>
            {highCarbonLogs.map(log =>(
                <li key={log.id}>
                    {log.activity}: {log.carbon} kg CO2
                </li>
            ))}
        </ul>
    </div>
)

export default logPage;