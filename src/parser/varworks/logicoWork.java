package parser.varworks;

import av.androidvisual.ConsoleActivity;
import av.androidvisual.Var;
import av.parserVisual.ParserMath;
import av.parserVisual.TradutorLinhas;

public class logicoWork implements VarWork{

	String conta = "" ;
	
	@Override
	public boolean executar(String linha, TradutorLinhas tradutor, Var variavel) {
			
		
		
		/*
		 *  Substituindo as palavras verdadeiro e falso por 1 e 0 para que o a método possa comparar ele lógicamente.
		 *  Este método não faz a analise lógica em se, ele primeiramente só identifica o que foi passado pelo
		 *  usuário. Ele vai encontrar palavras e verificar se elas são variáveis declaras no inicio, e se são valores numéricos também.
		 *  Ao achar um valor lógico ele tranforma o mesmo em número. A função que é utilizada por esse método só é capaz de trabalhar 
		 *  com a lógica se a mesma for numérica. Exemplo: ( 1 = 1 ) o resultado é  1    
		 */
		
		linha = linha.replaceAll("verdadeiro", "1");
		linha = linha.replaceAll("falso", "0");
			
		int j= linha.lastIndexOf("<-")+2;
		int inicio = -1;
		int fim = -1;
		String valor; 
		Var valorV;
		
		/* 
		 * Percorendo toda a linha para indentificar todas as variáveis e valores
		 * 
		 */
        for( int i = j; i<linha.length();i++){			       
        	
			if (inicio == -1 && (linha.charAt(i) != ' ' && (linha.charAt(i) != '=' && linha.charAt(i) != '>'  &&
					              linha.charAt(i) != '<'   &&  linha.charAt(i) != '(' &&  linha.charAt(i) != ')'))){				
				inicio= i;						
			}
			
			if (fim == -1 && inicio != -1 && (linha.charAt(i) == ' ' || (linha.charAt(i) == '=' ||
					linha.charAt(i) == '<'  || linha.charAt(i) == '>'  ||  linha.charAt(i) == '(' ||  linha.charAt(i) == ')'))){												
				fim= i;											
			}
			
			if (fim == -1 && inicio != -1 &&  i+1 == linha.length()){												
				fim= i+1;											
			}
			
			
			if(inicio!= -1 && fim != -1){
				valor = linha.substring(inicio,fim);							
				
				// verificando se palavra capturada é uma variável declarada pelo usuário
				valorV = tradutor.varExists(valor);
				
				if (valorV== null){
				 
				 if (valor.equals("ou")){
					 conta+="|";
				 }else if(valor.equals("e")){
					 conta+="&";
				 }else if(valor.equals("nao")){
					 conta+="!";
				 }else if(valor.equals("xou")){
					 conta+="?";
				 }
				 else{					
				 conta+=valor; 
				 }
				}
				else{
					
			         if(valorV.getTipo().equals("logico")){
			        	 
			        	if (valorV.getValor().equals("verdadeiro")){
			        		conta+="1";
			        	}
			        	else {
			        		conta+="0";
			        	}
			         }
			         else{
					 conta+=valorV.getValor();
			         }
				 		 	
				}
				
				inicio = -1;
				fim    = -1;
				
											
			}
			
			if(linha.charAt(i)== '=' || linha.charAt(i)== '<' || linha.charAt(i)== '>' || linha.charAt(i)== '(' || linha.charAt(i) == ')'){			
					
				if( linha.charAt(i)== '>' && linha.charAt(i+1)== '=')  {
					
				    	conta+=String.valueOf("$"); // $ representa o valor de maior ou igual 
				    	
					
				}else if  (linha.charAt(i)== '<' && linha.charAt(i+1)== '='){
					
					   conta+=String.valueOf("#"); // $ representa o valor de menor ou igual 
					   
				}else if  (linha.charAt(i)== '=' && (linha.charAt(i-1)== '<' || linha.charAt(i-1)== '>')){
					
					   // Faça nada
				}
				else if  (linha.charAt(i)== '<' &&  linha.charAt(i+1)== '>'){
					
					    conta+=String.valueOf("@"); // @ representa  diferente <> 
				}
				else if  (linha.charAt(i)== '>' &&  linha.charAt(i-1)== '<'){
					
					   // Faça nada 
			}
				else{
					conta+=String.valueOf(linha.charAt(i)); 
				}
							
				
			}
		}
        
        /*
         * Trabalhando com os valores e variáveis indentificadas 
         * 
         */              
        
	    String text="";
	    try{
	    
	    // Realizando compração lógica com o método mathString
	    ParserMath parser = new ParserMath();
	    text = parser.mathString(conta);	    
	    }
	    catch(Exception e){ 
	    	System.out.println("Erro de sintaxe ao realizar os cálculos. ");
	    	return false;
	    }
	    
	    if (text.contains("1")){
	        variavel.setValor("verdadeiro");
	    }else{
	    	variavel.setValor("falso");
	    }
	    	
         System.out.println(variavel.getNome()+" = "+variavel.getValor());
         return true;
	}

}
