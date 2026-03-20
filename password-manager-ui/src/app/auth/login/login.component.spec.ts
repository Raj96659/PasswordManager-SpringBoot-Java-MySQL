import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthService } from '../../core/services/auth.service';
import { of, throwError } from 'rxjs';

describe('LoginComponent', () => {

  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authServiceMock: any;

  beforeEach(async () => {

    authServiceMock = {
      login: jasmine.createSpy('login').and.returnValue(of({ token: 'abc' })),
      verify2FA: jasmine.createSpy('verify2FA').and.returnValue(of({ token: 'abc' }))
    };

    await TestBed.configureTestingModule({
      imports: [
        LoginComponent,
        ReactiveFormsModule,
        FormsModule,
        RouterTestingModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('form should be invalid when empty', () => {
    expect(component.form.valid).toBeFalse();
  });

  it('form should be valid when values provided', () => {

    component.form.setValue({
      username: 'teju',
      password: '123456'
    });

    expect(component.form.valid).toBeTrue();
  });

  it('should login successfully', () => {

    spyOn(localStorage, 'setItem');

    component.form.setValue({
      username: 'teju',
      password: '123456'
    });

    component.login();

    expect(authServiceMock.login).toHaveBeenCalled();
    expect(localStorage.setItem).toHaveBeenCalled();
  });

  it('should show error on invalid credentials', () => {

    authServiceMock.login.and.returnValue(
      throwError(() => new Error())
    );

    spyOn(window, 'alert');

    component.form.setValue({
      username: 'teju',
      password: 'wrong'
    });

    component.login();

    expect(window.alert).toHaveBeenCalled();
  });

  it('should toggle password visibility', () => {

    expect(component.showPassword).toBeFalse();

    component.togglePassword();

    expect(component.showPassword).toBeTrue();
  });

});
