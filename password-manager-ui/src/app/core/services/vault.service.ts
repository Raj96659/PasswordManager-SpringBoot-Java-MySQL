import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class VaultService {

  private API = 'http://localhost:8080/api/vault';

  constructor(private http: HttpClient) {}

  getAll() {
    return this.http.get<any[]>(this.API);
  }

  create(data: any) {
    return this.http.post(this.API, data);
  }

 update(id: number, data: any) {
   return this.http.put(`http://localhost:8080/api/vault/${id}`, data);
 }

  delete(id: number) {
    return this.http.delete(`${this.API}/${id}`);
  }

  search(keyword: string) {
    return this.http.get<any[]>(`${this.API}/search?keyword=${keyword}`);
  }

  favorites() {
    return this.http.get<any[]>(`${this.API}/favorites`);
  }

  category(category: string) {
    return this.http.get<any[]>(`${this.API}/category?category=${category}`);
  }
getLast() {
  return this.http.get<any>('http://localhost:8080/api/vault/last');
}
}
