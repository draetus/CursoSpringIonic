import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http'
import { ErrorHandler, NgModule } from '@angular/core';
import { IonicApp, IonicErrorHandler, IonicModule } from 'ionic-angular';

import { MyApp } from './app.component';

import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { CategoriaService } from '../services/domain/categoria.service';
import { ErrorInterceptionProvider } from '../interceptors/error-interceptor';
import { AuthService } from '../services/auth.service';
import { StorageService } from '../services/storage_service';
import { ClienteService } from '../services/domain/cliente.service';
import { AuthInterceptionProvider } from '../interceptors/auth-interceptor';
import { ProdutoService } from '../services/domain/produto.service';
import { CartService } from '../services/domain/cart.service';
import { PedidoService } from '../services/domain/pedido.service';

@NgModule({
  declarations: [
    MyApp
  ],
  imports: [
    BrowserModule,
    HttpClientModule, // O Client para realizar requisições http
    IonicModule.forRoot(MyApp),
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    CategoriaService, // Service da pagina de categorias declarado globalmente para uso em todas paginas
    AuthService, // Service de autenticação declarado globalmente para uso em todas paginas
    AuthInterceptionProvider, // Interceptador definido no projeto para tratar a autenticação, A ORDEM DECLARADA AQUI É A ORDEM DE EXECUÇÃO DOS INTERCEPTADORES
    ErrorInterceptionProvider, // Interceptador definido no projeto para tratar os erros de requisições http
    StorageService, // Service de guardar dados localmente
    ClienteService, // Service de resgatar clientes
    ProdutoService, // Service de resgatar produtos
    CartService, // Service para carrinho de compras
    PedidoService // Service para pedidos
  ]
})
export class AppModule {}
