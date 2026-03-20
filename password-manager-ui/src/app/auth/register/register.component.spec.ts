import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { AuthService } from '../../core/services/auth.service';
import { of, throwError } from 'rxjs';

describe('RegisterComponent', () => {

  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authServiceMock: any;

  beforeEach(async () => {

    authServiceMock = {
      register: jasmine.createSpy('register').and.returnValue(of({})),
      getQuestions: jasmine.createSpy('getQuestions').and.returnValue(of([]))
    };

    await TestBed.configureTestingModule({
      imports: [
        RegisterComponent,
        ReactiveFormsModule,
        FormsModule,
        RouterTestingModule
      ],
      providers: [
        { provide: AuthService, useValue: authServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create component', () => {
    expect(component).toBeTruthy();
  });

  it('should not call register if form invalid', () => {

    component.register();

    expect(authServiceMock.register).not.toHaveBeenCalled();
  });
it('should register successfully and navigate to login', () => {

  authServiceMock.register.and.returnValue(of({}));

  component.form.patchValue({
    username: 'shubhu',
    email: 'test@test.com',
    phone: '9999999999',
    password: '12345678'
  });


  spyOnProperty(component.form, 'invalid', 'get').and.returnValue(false);

  component.register();

  expect(authServiceMock.register).toHaveBeenCalled();
});
  it('should show error if registration fails', () => {

    authServiceMock.register.and.returnValue(
      throwError(() => new Error())
    );

    spyOn(window, 'alert');

    component.form.setValue({
      username: 'shubhu',
      email: 'test@test.com',
      phone: '9999999999',
      password: '123456'
    });

    component.register();

    expect(window.alert).toHaveBeenCalled();
  });

});
