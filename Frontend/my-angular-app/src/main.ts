
import './polyfills'; // Your regular polyfills
import { Buffer } from 'buffer';
import process from 'process';

(window as any).global = window;
(window as any).process = process;
(window as any).Buffer = Buffer;


import { bootstrapApplication } from '@angular/platform-browser';
import { importProvidersFrom } from '@angular/core';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { AppComponent } from './app/app.component';
import { AppRoutingModule } from './app/app-routing.module';

bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(AppRoutingModule),
    provideHttpClient(withInterceptorsFromDi())
  ]
}).catch(err => console.error(err));
