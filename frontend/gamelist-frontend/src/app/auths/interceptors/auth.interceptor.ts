import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const AuthInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  //pegando token pelo AuthService
  const token = authService.getToken();

  //clona a request se tiver token
  let authReq = req;
  if (token){
    authReq = req.clone({
      setHeaders:{ Authorization:`Bearer ${token}`
    }
    });
  }

  //Envia a request e escuta a response
  return next(authReq).pipe(
    catchError(error => {

    console.group('❌ ERRO HTTP');
    console.log('Status:', error.status);
    console.log('URL:', error.url);
    console.log('Mensagem:', error.message);
    console.log('Erro completo:', error);
    console.groupEnd();

      //se o backend nao autorizar
    if (error.status === 401 || error.status === 403) {
      authService.logout();
      router.navigateByUrl('/login');
    }
      //repassa o erro
      return throwError(() => error);
    })
  );
}
