import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { appRouterProviders } from './app/app-routing.module';
import { importProvidersFrom } from '@angular/core';
import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';
import { AuthInterceptor } from './app/auths/interceptors/auth.interceptor';


bootstrapApplication(AppComponent, {
  providers: [
    appRouterProviders,
    provideHttpClient(withInterceptors([AuthInterceptor]))
  ]
})
  .catch(err => console.error(err));
