import { HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs/Rx";
import { StorageService } from "../services/storage_service";
import { AlertController } from "ionic-angular";
import { FieldMessage } from "../models/fieldmessage.dto";


@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

    constructor(
        public storage: StorageService,
        public alertController: AlertController){}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        console.log("Erro na request");
        return next.handle(req)
            .catch((error, caught) => {

                let errorObj = error;
                if (errorObj.error) {
                    errorObj = errorObj.error;
                }
                if (!errorObj.status) { // Tratamento para quando resposta HTTP volta em texto
                    errorObj = JSON.parse(errorObj); // Converte para JSON aqui
                }

                switch(errorObj.status) {
                    case 401: 
                        this.handle401(); // Tratamento para erros 401 usando um alerta na tela
                        break;

                    case 403:
                        this.handle403();
                        break;

                    case 422:
                        this.handle422(errorObj);
                        break;

                    default: 
                        this.handleDefaultError(errorObj);
                        break;
                }

                return Observable.throw(error);
            }) as any;
    }

    handle401() {
        let alert = this.alertController.create({
            title: 'Erro 401 - Falha de autenticação',
            message: 'E-mail ou senha incorretos',
            enableBackdropDismiss: false,
            buttons: [
                {
                    text: 'Ok'
                }
            ]
        });
        alert.present();
    }

    handle403() {
        this.storage.setLocalUser(null);
    }

    handle422(errorObj) {
        console.log("Erro 422");
        let alert = this.alertController.create({
            title: 'Erro 422: Validação',
            message: this.listErrors(errorObj.errors),
            enableBackdropDismiss: false,
            buttons: [
                {
                    text: 'Ok'
                }
            ]
        });
        alert.present();
    }

    handleDefaultError(errorObj) {
        let alert = this.alertController.create({
            title: 'Erro ' + errorObj.status + ': ' + errorObj.error,
            message: errorObj.message,
            enableBackdropDismiss: false,
            buttons: [
                {
                    text: 'Ok'
                }
            ]
        });
        alert.present();
    }

    listErrors(messages: FieldMessage[]) {
        let s : string = '';
        for (var i=0; i<messages.length; i++) {
            s = s  + '<p><strong>' + messages[i].fieldName + '</strong>: ' + messages[i].message + '</p>'
        }
        return s;
    }
}

export const ErrorInterceptionProvider = {
    provide: HTTP_INTERCEPTORS,
    useClass: ErrorInterceptor,
    multi: true,
};