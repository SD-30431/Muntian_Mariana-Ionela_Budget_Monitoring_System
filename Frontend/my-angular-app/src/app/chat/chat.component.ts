import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ChatService } from '../services/chat.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  username: string = '';
  message: string = '';
  messages: { sender: string, content: string }[] = [];

  users: string[] = [];
  searchTerm: string = '';
  selectedUser: string = '';

  connectionStatus: string = 'Connecting to chat server...';

  constructor(
    private chatService: ChatService,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const storedUser = sessionStorage.getItem('user');
    if (storedUser) {
      const userObj = JSON.parse(storedUser);
      this.username = userObj.username;
    }

    this.userService.getAllUsers().subscribe({
      next: (data: any[]) => {
        this.users = data
          .map(user => user.username)
          .filter(username => username !== this.username);
      },
      error: (err) => {
        console.error('Error fetching users:', err);
      }
    });

    this.chatService.getMessages().subscribe((msg: { sender: string, content: string } | null) => {
      if (msg) {
        this.messages.push(msg);
      }
    });

    this.chatService.getConnectionStatus().subscribe((status: string) => {
      this.connectionStatus = status;
    });
  }

  filteredUsers(): string[] {
    const search = this.searchTerm.toLowerCase();
    return this.users.filter(u => u.toLowerCase().includes(search));
  }

  selectUser(user: string): void {
    this.selectedUser = user;
    this.messages = [];

    this.chatService.getChatHistory(this.username, this.selectedUser).subscribe({
      next: (history) => {
        this.messages = history.map((msg: any) => ({
          sender: msg.sender,
          content: msg.content
        }));
      },
      error: (err) => {
        if (err.status === 404) {
          console.warn('No previous chat history found, starting new conversation.');
          this.messages = [];
        } else {
          console.error('Error fetching chat history:', err);
        }
      }
    });
  }

  send(): void {
    const trimmedMessage = this.message.trim();

    if (!this.username || !this.selectedUser || !trimmedMessage) {
      console.warn('❌ Cannot send: missing sender, recipient, or message.');
      return;
    }

    this.chatService.sendMessage(this.username, this.selectedUser, trimmedMessage);
    this.messages.push({ sender: this.username, content: trimmedMessage });

    this.chatService.saveMessage(this.username, this.selectedUser, trimmedMessage).subscribe({
      next: () => console.log('✅ Message saved successfully'),
      error: (err) => console.error('❌ Error saving message:', err)
    });

    this.message = '';
  }

  navigateTo(route: string): void {
    const routesMap: { [key: string]: string } = {
      main: '/main-user',
      'manage-expenses': '/manage-expenses',
      'manage-cards': '/manage-cards',
      history: '/history',
      chart: '/chart-expenses',
      chat: '/chat',
      'send-money': '/send-money',
      'spending-trends': '/spending-trends',  
      dashboard: '/dashboard'
    };
    this.router.navigate([routesMap[route] || '/dashboard']);
  }
}
