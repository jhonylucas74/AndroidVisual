package parser.varworks;

import com.example.androidvisual.Var;
import com.parserVisual.ParserMath;
import com.parserVisual.TradutorLinhas;

public class realWork implements VarWork {

	@Override
	public boolean executar(String linha, TradutorLinhas tradutor, Var variavel) {
	
		
		String conta = "" ;
			
		int j= linha.lastIndexOf("<-")+2;
		int inicio = -1;
		int fim = -1;
		String valor; 
		Var valorV;
		
		//separando e indentificando valores e operadores
        for( int i = j; i<linha.length();i++){			       
        	
			if (inicio == -1 && 
			   (linha.charAt(i) != ' ' && (linha.charAt(i) != '+' && linha.charAt(i) != '-'  &&
				linha.charAt(i) != '*' &&  linha.charAt(i) != '/' && linha.charAt(i) != '('  &&
				linha.charAt(i) != ')' &&  linha.charAt(i) != '%' && linha.charAt(i) != '^'))){				
				
				inicio= i;						
			}
			
			if (fim == -1 && inicio != -1 && (
				 linha.charAt(i) == ' '  || (linha.charAt(i) == '+' || linha.charAt(i) == '-'  ||
				 linha.charAt(i) == '*'  ||  linha.charAt(i) == '/' || linha.charAt(i) == '(' ||
				 linha.charAt(i) == ')'  ||  linha.charAt(i) == '%' || linha.charAt(i) == '^'))){												
				fim= i;											
			}
			
			if (fim == -1 && inicio != -1 &&  i+1 == linha.length()){												
				fim= i+1;											
			}
			
			
			if(inicio!= -1 && fim != -1){
				valor = linha.substring(inicio,fim);								
								
				valorV = tradutor.varExists(valor);
				if (valorV == null){
				 conta+=valor;  //listaInteiros.add(Integer.parseInt(valor));
				}
				else{
				 conta+= valorV.getValor() ;	//listaInteiros.add(Integer.parseInt(valorV.getValor()));
				 
				 if(!valorV.getTipo().equals("inteiro") && !valorV.getTipo().equals("real")){
				  System.out.println("erro o tipo da variável não é válido.");								
				  return false;
				   }
				}
				
				inicio = -1;
				fim    = -1;
				
											
			}
			
			if(linha.charAt(i)== '+' || linha.charAt(i)== '-' || linha.charAt(i)== '/' || linha.charAt(i)== '*' ||
			   linha.charAt(i)== '(' || linha.charAt(i)== ')' || linha.charAt(i)== '%' || linha.charAt(i)== '^')			
				conta+=String.valueOf(linha.charAt(i)); //operadores.add(String.valueOf(linha.charAt(i)));
			
		}
        
        //Fazendo os cálculos 
       //  variavel.setValor(String.valueOf(realizarCalculo(listaInteiros,operadores)));
        
           	 
	    String text="";
	    try{
	    	
	    	ParserMath parser = new ParserMath();
	  	    text = parser.mathString(conta);	 
	     
	    }
	    catch(Exception e){ 
	    	System.out.println("Erro de sintaxe ao realizar os cálculos. ");
	    	return false;
	    }
	    
	    double d = Double.parseDouble(text);	  
	    
	     variavel.setValor(String.valueOf(d));
       //  System.out.println(variavel.getNome()+" = "+variavel.getValor());
         return true;
		
	}
}
