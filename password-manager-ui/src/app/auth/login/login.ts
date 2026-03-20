import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { RouterModule } from '@angular/router';
import { Component, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.html',
   styleUrls: ['./login.css'],
encapsulation: ViewEncapsulation.None,
  imports: [CommonModule, FormsModule, ReactiveFormsModule,RouterModule]
})
export class LoginComponent {

  show2FAScreen = false;
  twoFACode = '';
showPassword = false;


togglePassword() {
  this.showPassword = !this.showPassword;
}

  form: any;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {

    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });

  }
ngOnInit() {

  const savedPassword = localStorage.getItem('generatedPassword');

  if (savedPassword && savedPassword.length >= 8) {
    this.form.patchValue({
      password: savedPassword
    });
  }

}
logout() {

  localStorage.removeItem('token');

  this.router.navigate(['/login']);
}
verifyLogin2FA() {

  this.auth.verify2FA(this.twoFACode)
    .subscribe((res: any) => {

      localStorage.setItem('token', res.token);
      this.router.navigate(['/dashboard']);

    });

}
login() {

   if (this.form.invalid) return;

   this.auth.login(this.form.value).subscribe({

     next: (res: any) => {

       console.log('TOKEN:', res.token);

       localStorage.setItem('token', res.token);
      localStorage.removeItem('generatedPassword');
       alert('Login Successful');

       this.router.navigate(['/dashboard']);
     },

     error: () => {
       alert('Invalid Credentials');
     }

   });

 }

}
