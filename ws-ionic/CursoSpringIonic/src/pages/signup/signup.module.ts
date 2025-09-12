import { NgModule } from '@angular/core';
import { IonicPageModule } from 'ionic-angular';
import { SignupPage } from './signup';
import { CidadeService } from '../../services/domain/cidade.service';
import { EstadoService } from '../../services/domain/estado.service';

@NgModule({
  declarations: [
    SignupPage,
  ],
  imports: [
    IonicPageModule.forChild(SignupPage),
  ],
  providers: [
    CidadeService, // Service da pagina de categorias declarado localmente para ser usado pela pagina de signup
    EstadoService // Service da pagina de categorias declarado localmente para ser usado pela pagina de signup
  ]
})
export class SignupPageModule {}
