import { Component, OnInit } from '@angular/core';
import { ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { VaultService } from '../core/services/vault.service';
import { ProfileService } from '../core/services/profile.service';
@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  standalone: true,
   styleUrls: ['./dashboard.css'],
  imports: [CommonModule, FormsModule]
})

export class DashboardComponent implements OnInit {


  user: any;
  lastAccount: any;

  totalAccounts = 0;
  strongPasswords = 0;
  weakPasswords = 0;
vaults: any[] = [];
  vault: any = {
    accountName: '',
    website: '',
    username: '',
    password: '',
    category: '',
    notes: '',
    favorite: false
  };
constructor(
  private vaultService: VaultService,
  private profileService: ProfileService,
  private router: Router,
  private cd: ChangeDetectorRef
) {}

ngOnInit() {

  this.loadProfile();
  this.loadVaultSummary();
  this.loadLastAccount();
  this.loadVaults();


}

  // ================= PROFILE =================



loadProfile(): void {

  this.profileService.getProfile().subscribe({
    next: (res: any) => {
      this.user = res;
      this.cd.detectChanges();   // ⭐ ADD
    },
    error: (err: any) => console.error(err)
  });

}
  // ================= SUMMARY =================
loadVaultSummary() {

  this.vaultService.getAll().subscribe({

    next: (res: any) => {

      const list = res || [];

      console.log("Vault List:", list);   // DEBUG

      this.totalAccounts = list.length;

      this.strongPasswords =
        list.filter((v: any) => this.isStrong(v.password)).length;

      this.weakPasswords =
        list.filter((v: any) => !this.isStrong(v.password)).length;
              this.cd.detectChanges();   // ⭐ ADD HERE


    },

    error: (err: any) => console.error(err)

  });

}
loadVaults() {

  this.vaultService.getAll().subscribe({

    next: (res: any[]) => {

      console.log("Dashboard Vaults:", res);

      this.vaults = res || [];
      this.cd.detectChanges();   // ⭐ ADD

    },

    error: (err: any) => console.error(err)

  });

}


  isStrong(password: string): boolean {

    if (!password) return false;

    let score = 0;

    if (password.length >= 8) score++;
    if (/[A-Z]/.test(password)) score++;
    if (/[0-9]/.test(password)) score++;
    if (/[^A-Za-z0-9]/.test(password)) score++;

    return score >= 3;
  }

  // ================= ADD ACCOUNT =================
addVault() {

  this.vaultService.create(this.vault).subscribe({

    next: (res: any) => {

      alert('Account Added Successfully');

      // clear form
      this.vault = {
        accountName: '',
        website: '',
        username: '',
        password: '',
        category: '',
        notes: '',
        favorite: false
      };

     this.loadVaultSummary();
           this.loadLastAccount();
      this.cd.detectChanges();   // ⭐ ADD

    },

    error: (err: any) => console.error(err)

  });

}

  // ================= LAST ACCOUNT =================
loadLastAccount() {

  this.vaultService.getLast().subscribe({

    next: (res: any) => {

      console.log("Last Account:", res);

this.lastAccount = res;
this.cd.detectChanges();
    },

    error: (err: any) => console.error(err)

  });

}

  // ================= PASSWORD GENERATOR =================

  generatePassword(): void {

    const chars =
      'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%';

    let pass = '';

    for (let i = 0; i < 12; i++) {
      pass += chars.charAt(Math.floor(Math.random() * chars.length));
    }

    this.vault.password = pass;
  }
get securityScore(): number {

     const total = Number(this.totalAccounts) || 0;
     const strong = Number(this.strongPasswords) || 0;

     if (total === 0) return 0;

     return Math.round((strong / total) * 100);
   }
  // ================= NAVIGATION =================

  goVault(): void {
    this.router.navigate(['/vault']);
  }

  goProfile(): void {
    this.router.navigate(['/profile']);
  }

}
