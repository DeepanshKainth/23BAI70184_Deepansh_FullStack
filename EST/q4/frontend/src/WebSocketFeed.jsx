import { useEffect, useRef, useState } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

const WS_URL = "http://localhost:8080/ws";

export default function WebSocketFeed() {
  const [messages, setMessages] = useState([]);
  const [status, setStatus] = useState("Connecting...");
  const listRef = useRef(null);

  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS(WS_URL),
      reconnectDelay: 3000,
      debug: () => {}
    });

    client.onConnect = () => {
      setStatus("Connected");
      client.subscribe("/topic/messages", (frame) => {
        try {
          const payload = JSON.parse(frame.body);
          const text = payload.text ?? frame.body;
          const timestamp = payload.timestamp ?? new Date().toISOString();
          setMessages((prev) => [...prev, { text, timestamp }]);
        } catch {
          setMessages((prev) => [...prev, { text: frame.body, timestamp: new Date().toISOString() }]);
        }
      });
    };

    client.onStompError = (frame) => {
      setStatus(`Broker error: ${frame.headers.message || "unknown"}`);
    };

    client.onWebSocketClose = () => {
      setStatus("Disconnected");
    };

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  useEffect(() => {
    if (listRef.current) {
      listRef.current.scrollTop = listRef.current.scrollHeight;
    }
  }, [messages]);

  return (
    <section className="panel">
      <div className="panel-header">
        <h1>Server Messages</h1>
        <span className={`status ${status === "Connected" ? "ok" : "warn"}`}>{status}</span>
      </div>

      <div className="feed" ref={listRef}>
        {messages.length === 0 ? (
          <p className="empty">No messages yet...</p>
        ) : (
          messages.map((msg, idx) => (
            <article key={`${msg.timestamp}-${idx}`} className="message">
              <p>{msg.text}</p>
              <time>{new Date(msg.timestamp).toLocaleTimeString()}</time>
            </article>
          ))
        )}
      </div>
    </section>
  );
}
