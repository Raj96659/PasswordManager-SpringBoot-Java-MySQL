import { HttpInterceptorFn } from '@angular/common/http';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {


const isPublic =
    req.url.includes('/api/otp') ||
    req.url.includes('/api/forgot');

  if (isPublic) {
    return next(req);   // no token attached
  }
  const token = localStorage.getItem('token');

  if (token) {

    const cloned = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    console.log('TOKEN SENT:', token);

    return next(cloned);
  }

  return next(req);
};
