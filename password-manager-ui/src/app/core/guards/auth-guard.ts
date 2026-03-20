import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authGuard: CanActivateFn = () => {

  const router = inject(Router);
  const token = localStorage.getItem('token');

  console.log('GUARD TOKEN:', token);

  if (!token) {
    alert('Please login first');
    router.navigate(['/login']);
    return false;
  }

  return true;
};
