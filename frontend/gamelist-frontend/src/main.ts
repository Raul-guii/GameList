import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { appRouterProviders } from './app/app-routing.module';
import { importProvidersFrom } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';


bootstrapApplication(AppComponent, {
  providers: [
    appRouterProviders,
    importProvidersFrom(HttpClientModule)
  ]
})
  .catch(err => console.error(err));
