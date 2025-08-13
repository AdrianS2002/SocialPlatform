import { Injectable } from '@angular/core';
import { GoogleGenerativeAI } from '@google/generative-ai';

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  private genAI: GoogleGenerativeAI;

  constructor() {
    const apiKey = 'AIzaSyCydKM2z8PYVKVmN6BfWpTqrGwJ2pPiVFs';
    this.genAI = new GoogleGenerativeAI(apiKey);
  }

  async sendMessage(message: string): Promise<string> {
    const model = this.genAI.getGenerativeModel({ model: 'gemini-2.0-flash' });
    const result = await model.generateContent(message);
    const response = result.response;
    const reply = response.candidates?.[0]?.content?.parts?.[0]?.text || 'Sorry, I could not understand that.';
    return reply;
  }
}
