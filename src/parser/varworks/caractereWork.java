package parser.varworks;

import av.androidvisual.Var;
import av.parserVisual.TradutorLinhas;

public class caractereWork implements VarWork{

	@Override
	public boolean executar(String linha, TradutorLinhas tradutor, Var variavel) {
				
		String conta = "" ;
			
		int j= linha.lastIndexOf("<-")+2;
		int inicio = -1;
		int fim = -1;
		String valor; 
		Var valorV;
		boolean aspas = false;
		
		//separando e indentificando valores e operadores
        for( int i = j; i<linha.length();i++){			       
        	
        	if (aspas == true &&fim == -1 && inicio != -1 && linha.charAt(i) == '"' ){												
				
				fim= i;
				 
			}
        	
			if (inicio == -1 &&  ((linha.charAt(i) != ' ' && (linha.charAt(i) != '+') || linha.charAt(i)=='"') )){				
				inicio= i;
				
				if( linha.charAt(i)=='"'){
					aspas = true;
				 
				}
			}
			
			if (aspas == false &&fim == -1 && inicio != -1 && (linha.charAt(i) == ' ' || (linha.charAt(i) == '+' ))){												
				fim= i;											
			}
			
			
			
			if (fim == -1 && inicio != -1 &&  i+1 == linha.length()){												
				fim= i+1;											
			}
			
			
			if(inicio!= -1 && fim != -1){															
								
				if ( aspas == false) {
				valor = linha.substring(inicio,fim);
				valorV = tradutor.varExists(valor);
				
				if (valorV != null){
				/*	 if(!valorV.getTipo().equals("caractere")){
						  System.out.println("erro o tipo da variável não é válido.");								
						  return false;
						   }	*/
				
				 conta+= valorV.getValor() ;					 				 				
				}
				
				}else{
				 valor = linha.substring(inicio+1,fim);
				 conta+=valor;  //listaInteiros.add(Integer.parseInt(valor))
				 aspas= false;
				}
				
				inicio = -1;
				fim    = -1;
				
											
			}
			
			//if(linha.charAt(i)== '+' || linha.charAt(i)== '-' || linha.charAt(i)== '/' || linha.charAt(i) == '*' || linha.charAt(i) == '(' || linha.charAt(i) == ')')			
		    //conta+=String.valueOf(linha.charAt(i)); //operadores.add(String.valueOf(linha.charAt(i)));
			
		}
        
        //Fazendo os cálculos 
       //  variavel.setValor(String.valueOf(realizarCalculo(listaInteiros,operadores)));             	   
	    
	     variavel.setValor(conta);
       //  System.out.println(variavel.getNome()+" = "+variavel.getValor());
         return true;
	}

}
