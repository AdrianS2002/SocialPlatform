import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';

import { provideFirebaseApp, initializeApp } from '@angular/fire/app';
import { provideStorage, getStorage } from '@angular/fire/storage';
import { provideFirestore, getFirestore } from '@angular/fire/firestore';
import { authInterceptor } from '../auth.interceptor';


const firebaseConfig = {
  apiKey: "AIzaSyAXiODNa-wufgdhGkp4EQO3jo-eotVfObs",
  authDomain: "socialplatform-c9638.firebaseapp.com",
  projectId: "socialplatform-c9638",
  storageBucket: "socialplatform-c9638.firebasestorage.app",
  messagingSenderId: "967566931420",
  appId: "1:967566931420:web:d001b7aab7368b68620b19",
  measurementId: "G-ZNM9477JZM"
};


export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideHttpClient( withInterceptors([authInterceptor])),
    provideRouter(routes),
    provideFirebaseApp(() => initializeApp(firebaseConfig)),
    provideStorage(() => getStorage()),
    provideFirestore(() => getFirestore())
  ]
};
