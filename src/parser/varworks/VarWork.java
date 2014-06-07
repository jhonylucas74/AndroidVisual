package parser.varworks;

import av.androidvisual.Var;
import av.parserVisual.TradutorLinhas;

/*
 * Interface para auxiliar a manipula��o das varias formas de se trabalhar com 
 * as v�riaveis no visual G  
 */

public interface VarWork {

  public boolean executar(String linha,TradutorLinhas tradutor,Var variavel);
  
}
