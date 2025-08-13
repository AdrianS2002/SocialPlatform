import { Injectable } from '@angular/core';
import { Firestore, doc, setDoc, updateDoc, getDoc } from '@angular/fire/firestore';

@Injectable({ providedIn: 'root' })
export class UserFirestoreService {
  constructor(private firestore: Firestore) {}

  async saveUserProfile(userId: string, data: any): Promise<void> {
    const userRef = doc(this.firestore, 'users', String(userId));

    await setDoc(userRef, data, { merge: true });
  }

  async getUserProfile(userId: string): Promise<any> {
    const userRef = doc(this.firestore, 'users', String(userId));
    const snapshot = await getDoc(userRef);
    return snapshot.exists() ? snapshot.data() : null;
  }
}
