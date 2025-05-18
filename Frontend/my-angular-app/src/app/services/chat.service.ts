import { Injectable } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Subject, BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private client: Client;
  private messageSubject = new Subject<{ sender: string; content: string }>();
  private connectionStatusSubject = new BehaviorSubject<string>('Connecting...');

  constructor(private http: HttpClient) {
    this.client = new Client({
      webSocketFactory: () => new SockJS('/ws'),
      reconnectDelay: 5000,
      debug: (str) => console.log('[WebSocket Debug]:', str),
    });

    this.client.onConnect = () => {
      console.log('✅ Connected to WebSocket');
      this.connectionStatusSubject.next('Connected ✅');
      this.client.subscribe('/topic/messages', (message: Message) => {
        if (message.body) {
          const parsed = JSON.parse(message.body);
          this.messageSubject.next(parsed);
        }
      });
    };

    this.client.onDisconnect = () => {
      console.warn('❌ Disconnected');
      this.connectionStatusSubject.next('Disconnected, reconnecting...');
    };

    this.client.activate();
  }

  sendMessage(sender: string, recipient: string, content: string): void {
    if (this.client.connected) {
      this.client.publish({
        destination: '/app/send',
        body: JSON.stringify({ sender, recipient, content }),
      });
    } else {
      console.warn('WebSocket not connected yet!');
    }
  }

  saveMessage(sender: string, recipient: string, content: string): Observable<any> {
    return this.http.post('http://localhost:8080/api/messages', { sender, recipient, content });
  }

  getMessages(): Observable<{ sender: string; content: string }> {
    return this.messageSubject.asObservable();
  }

  getConnectionStatus(): Observable<string> {
    return this.connectionStatusSubject.asObservable();
  }

  getChatHistory(sender: string, recipient: string): Observable<any[]> {
    return this.http.get<any[]>(
      `http://localhost:8080/api/messages/chat/history?sender=${sender}&recipient=${recipient}`
    );
  }
}
