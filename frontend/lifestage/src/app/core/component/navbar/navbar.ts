import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LoginButton } from '../../../features/auth/login-button/login-button';
import { CommonModule } from '@angular/common';

@Component({
	selector: 'app-navbar',
	imports: [RouterModule, LoginButton, CommonModule],
	templateUrl: './navbar.html',
	styleUrl: './navbar.css',
})
export class Navbar {
	menuOpen: boolean = false;
}
