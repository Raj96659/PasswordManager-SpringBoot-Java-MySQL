import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';
import { LandingComponent } from './landing/landing';
import { LayoutComponent } from './auth/layout/layout';

export const routes: Routes = [

  // ================= LANDING PAGE =================
  {
    path: '',
    component: LandingComponent
  },

  // ================= AUTH PAGES =================

  {
    path: 'login',
    loadComponent: () =>
      import('./auth/login/login')
        .then(m => m.LoginComponent)
  },

  {
    path: 'register',
    loadComponent: () =>
      import('./auth/register/register')
        .then(m => m.RegisterComponent)
  },

  {
    path: 'forgot-password',
    loadComponent: () =>
      import('./auth/forgot-password/forgot-password')
        .then(m => m.ForgotPasswordComponent)
  },

  // ================= MAIN APP =================

  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [

      { path: 'dashboard', loadComponent: () => import('./dashboard/dashboard').then(m => m.DashboardComponent) },
      { path: 'vault', loadComponent: () => import('./vault/vault').then(m => m.VaultComponent) },
      { path: 'profile', loadComponent: () => import('./profile/profile').then(m => m.ProfileComponent) },
      { path: 'generator', loadComponent: () => import('./generator/generator').then(m => m.GeneratorComponent) }

    ]
  }

];
