import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Album } from "../models/album-model";
import { ImageResponse } from "../models/image-model";

@Injectable({
  providedIn: 'root'
})
export class AlbumService {
  private readonly API_BASE = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  createAlbum(album: Album): Observable<void> {
    return this.http.post<void>(`${this.API_BASE}/album/create`, album);
  }

  getAlbumPhotos(albumId: number): Observable<ImageResponse[]> {
    const params = new HttpParams().set('albumId', albumId.toString());
    return this.http.get<ImageResponse[]>(`${this.API_BASE}/album/photos`, { params });
  }

  uploadImage(file: File, albumId: number): Observable<void> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('albumId', albumId.toString());
    return this.http.post<void>(`${this.API_BASE}/image/upload`, formData);
  }

  deleteImage(id: number): Observable<void> {
  const params = new HttpParams().set('id', id.toString());
  return this.http.delete<void>(`${this.API_BASE}/image/delete-photo`, { params });
}

  getUserAlbums(userId: number): Observable<Album[]> {
    const params = new HttpParams().set('userID', userId.toString());
    return this.http.get<Album[]>(`${this.API_BASE}/album/user-albums`, { params });
  }

  deleteAlbum(albumId: number): Observable<void> {
    const params = new HttpParams().set('albumId', albumId.toString());
    return this.http.delete<void>(`${this.API_BASE}/album/delete`, { params });
  }

  editAlbum(album: Album): Observable<Album> {
    return this.http.post<Album>(`${this.API_BASE}/album/edit`, album);
  }
}