import { HttpClient } from "@angular/common/http";
import { API_CONFIG } from "../../config/api.config";
import { Injectable } from "@angular/core";

@Injectable()
export class PedidoService {

    constructor(public http: HttpClient) {}

    findById(id: string) {
        return this.http.get(`${API_CONFIG.baseUrl}/pedidos/${id}`);
    }

}