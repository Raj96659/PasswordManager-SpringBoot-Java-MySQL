import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProfileService } from '../core/services/profile.service';
import { VaultService } from '../core/services/vault.service';
import { ChangeDetectorRef } from '@angular/core';
import Swal from 'sweetalert2';
@Component({
  selector: 'app-profile',
  standalone: true,
  templateUrl: './profile.html',
  styleUrls: ['./profile.css'],
  imports: [CommonModule, FormsModule]
})
export class ProfileComponent implements OnInit {

  // ================= USER =================
  user: any = {};

  questions: any[] = [];   // ✅ ADD THIS LINE


  // ================= PASSWORD MODEL =================
  password = {
    current: '',
    new: '',
    confirm: ''
  };

  // ================= MESSAGE =================
  message = '';

 constructor(
   private profileService: ProfileService,
    private cd: ChangeDetectorRef
 ) {}

  ngOnInit() {
    this.loadProfile();


  }

  // ================= LOAD PROFILE =================

  loadProfile() {

    this.profileService.getProfile()
      .subscribe((res: any) => {
        this.user = res;
              this.cd.detectChanges();   // ⭐ IMPORTANT

         this.loadQuestions();
      });

  }

  // ================= LOAD QUESTIONS =================
loadQuestions() {

  this.profileService.getQuestions()
    .subscribe({

      next: (res: any) => {

        console.log("QUESTIONS:", res);

        this.questions = res || [];
        this.cd.detectChanges();   // ⭐ FIX HERE

      },

      error: err => console.error(err)

    });

}
 // ================= UPDATE PROFILE =================

updateProfile() {

  this.profileService.updateProfile(this.user)
    .subscribe(() => {
Swal.fire({
  icon: 'success',
  title: 'Success',
  text: 'Profile Updated successfully'
});      this.loadProfile();   // ✅ IMPORTANT
    });

}

  // ================= UPDATE QUESTIONS =================

updateQuestions() {

  const payload = this.questions.map(q => ({
    questionId: q.questionId,
    answer: q.answer
  }));

  this.profileService.updateQuestions(payload)
    .subscribe(() => {

Swal.fire({
  icon: 'success',
  title: 'Success',
  text: 'Question Updated successfully'
});
      this.loadQuestions(); // reload
      this.cd.detectChanges();   // ⭐ optional but good

    });

}
 // ✅ ADD THIS METHOD HERE
  trackById(index: number, item: any) {
    return item.questionId;
  }
vaults: any[] = [];



  // ================= CHANGE PASSWORD =================

  changePassword() {

    if (this.password.new !== this.password.confirm) {
     Swal.fire('Password mismatch');
      return;
    }

    this.profileService.changePassword({
      currentPassword: this.password.current,
      newPassword: this.password.new,
      confirmPassword: this.password.confirm
    }).subscribe({

      next: () => {
Swal.fire({
  icon: 'success',
  title: 'Success',
  text: 'Password Updated successfully'
});      },

      error: (err) => {

        console.error(err);

  Swal.fire('Error', 'Current password is incorrect', 'error');

      }

    });

  }
  // ================= TOGGLE 2FA =================

//   toggle2FA() {
//
//     this.profileService.update2FA(!this.user.twoFactorEnabled)
//       .subscribe({
//
//         next: () => {
//
//           // Toggle local state
//           this.user.twoFactorEnabled = !this.user.twoFactorEnabled;
//
//         },
//
//         error: () => {
//           Swal.fire('Failed to update 2FA');
//         }
//
//       });
//
//   }
toggle2FA() {
  this.user.twoFactorEnabled =
    !this.user.twoFactorEnabled;

}
}
