import React, { useState, useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';

function SimpleChatComponent() {
    const [messages, setMessages] = useState([]);
    const [message, setMessage] = useState('');
    const [connected, setConnected] = useState(false);
    const clientRef = useRef(null);

    useEffect(() => {
        const connectWebSocket = () => {
            const stompClient = new Client({
                brokerURL: 'ws://localhost:8080/ws',  // WebSocket 서버 URL
                connectHeaders: {
                    login: 'user',
                    passcode: 'password'
                },
                debug: (str) => {
                    console.log('STOMP Debug:', str);
                },
                reconnectDelay: 5000,
                heartbeatIncoming: 4000,
                heartbeatOutgoing: 4000,
                onConnect: (frame) => {
                    console.log('STOMP Connected:', frame);
                    setConnected(true);

                    // 구독
                    stompClient.subscribe('/topic/public', (message) => {
                        try {
                            const receivedMessage = JSON.parse(message.body);
                            setMessages(prev => [...prev, receivedMessage]);
                        } catch (error) {
                            console.error('Error parsing message:', error);
                        }
                    });

                    // 입장 메시지
                    stompClient.publish({
                        destination: '/app/chat.addUser',
                        body: JSON.stringify({
                            sender: "User",
                            type: 'JOIN',
                            response: '입장하셨습니다.'
                        })
                    });
                },
                onDisconnect: () => {
                    console.log('STOMP Disconnected');
                    setConnected(false);
                },
                onStompError: (frame) => {
                    console.error('STOMP Error:', frame);
                }
            });

            clientRef.current = stompClient;

            try {
                console.log('Activating STOMP client...');
                stompClient.activate();
            } catch (error) {
                console.error('Error activating STOMP client:', error);
            }
        };

        connectWebSocket();

        return () => {
            if (clientRef.current) {
                if (clientRef.current.connected) {
                    clientRef.current.publish({
                        destination: '/app/chat.addUser',
                        body: JSON.stringify({
                            sender: "User",
                            type: 'LEAVE',
                            response: 'has left the chat'
                        })
                    });
                }
                clientRef.current.deactivate();
            }
        };
    }, ["User"]);

    const sendMessage = (event) => {
        event.preventDefault();
        if (message.trim() && clientRef.current?.connected) {
            try {
                clientRef.current.publish({
                    destination: '/app/chat.sendMessage',
                    body: JSON.stringify({
                        sender: "User",
                        response: message,
                        type: 'CHAT'
                    })
                });
                setMessage('');
            } catch (error) {
                console.error('Error sending message:', error);
            }
        }
    };

    return (
        <div className="chat-container">
            <div className="connection-status">
                <h3>AI Chat</h3>
                <p>Connected: {connected ? '✅ Online' : '❌ Offline'}</p>
                <p>Username: {"User"}</p>
                <button onClick={() => {
                    if (clientRef.current) {
                        clientRef.current.deactivate();
                        setTimeout(() => clientRef.current.activate(), 1000);
                    }
                }} disabled={connected}>Reconnect</button>
            </div>

            <div className="messages">
                {messages.map((msg, index) => (
                    <div key={index}>
                        <strong>{msg.sender}:</strong> <p dangerouslySetInnerHTML={{ __html: msg.response }}></p>
                    </div>
                ))}
            </div>

            <form onSubmit={sendMessage}>
                <input type="text" value={message} onChange={(e) => setMessage(e.target.value)} placeholder="Type your message..." disabled={!connected} />
                <button type="submit" disabled={!connected}>Send</button>
            </form>
        </div>
    );
}

export default SimpleChatComponent;