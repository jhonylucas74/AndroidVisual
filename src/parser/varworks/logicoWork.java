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
		 *  Substituindo as palavras verdadeiro e falso por 1 e 0 para que o a m�todo possa comparar ele l�gicamente.
		 *  Este m�todo n�o faz a analise l�gica em se, ele primeiramente s� identifica o que foi passado pelo
		 *  usu�rio. Ele vai encontrar palavras e verificar se elas s�o vari�veis declaras no inicio, e se s�o valores num�ricos tamb�m.
		 *  Ao achar um valor l�gico ele tranforma o mesmo em n�mero. A fun��o que � utilizada por esse m�todo s� � capaz de trabalhar 
		 *  com a l�gica se a mesma for num�rica. Exemplo: ( 1 = 1 ) o resultado �  1    
		 */
		
		linha = linha.replaceAll("verdadeiro", "1");
		linha = linha.replaceAll("falso", "0");
			
		int j= linha.lastIndexOf("<-")+2;
		int inicio = -1;
		int fim = -1;
		String valor; 
		Var valorV;
		
		/* 
		 * Percorendo toda a linha para indentificar todas as vari�veis e valores
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
				
				// verificando se palavra capturada � uma vari�vel declarada pelo usu�rio
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
					
					   // Fa�a nada
				}
				else if  (linha.charAt(i)== '<' &&  linha.charAt(i+1)== '>'){
					
					    conta+=String.valueOf("@"); // @ representa  diferente <> 
				}
				else if  (linha.charAt(i)== '>' &&  linha.charAt(i-1)== '<'){
					
					   // Fa�a nada 
			}
				else{
					conta+=String.valueOf(linha.charAt(i)); 
				}
							
				
			}
		}
        
        /*
         * Trabalhando com os valores e vari�veis indentificadas 
         * 
         */              
        
	    String text="";
	    try{
	    
	    // Realizando compra��o l�gica com o m�todo mathString
	    ParserMath parser = new ParserMath();
	    text = parser.mathString(conta);	    
	    }
	    catch(Exception e){ 
	    	System.out.println("Erro de sintaxe ao realizar os c�lculos. ");
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
