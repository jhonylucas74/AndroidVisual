package com.parserVisual;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import parser.varworks.*;

import com.example.androidvisual.ConsoleActivity;
import com.example.androidvisual.Var;

import android.os.Handler;
import android.text.Html;
import android.text.Spanned;


public class TradutorLinhas extends Thread {
		
	private String algoritmoNome;
	private ArrayList<Var> listaVar;
	private BufferedReader br;
	private String algoritmo;
	private String textoChange;
	private String textoChange2;	
	private boolean comfirmaEntrada= false;
	public static boolean stop = false;
	
	public TradutorLinhas(String s){
		algoritmo =s;		
	    listaVar= new ArrayList<Var>();
	   
	}	

	public void run() {

		
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	    //	String into InputStream
		InputStream is = new ByteArrayInputStream(algoritmo.getBytes());
	 
		// read it with BufferedReader
		br = new BufferedReader(new InputStreamReader(is));
		String line="";
		
		// primeira parte: verificando linha Algoritmo
		try {
			line = br.readLine();
			
			if (line == null)
			return;
			
		} catch (IOException e1) { 
			e1.printStackTrace();
		}
		
		if(!procurarComandoE(line,"algoritmo")) {
		
			System.out.println(" Erro de sintax, não foi encontrado a palavra algoritmo no inicio do texto.");
			return;
		}
		
		// segunda parte: verificando linha Var e adicionando variáveis
		try {
			
			
			
			while(!procurarComandoE(line,"var")) {
				line = br.readLine();	
				
				if (line == null)
					return;
			}

			listaVar.clear();
			while ((line = br.readLine()) != null) {
				
				//Caso encontre a palavra reservada inicio parar o loop
				if(procurarComandoE(line, "inicio")){
					break;
				}	
					
				if (!procurarVar(line)){					
					return;
				}
				
			}
			
			for(Var v: listaVar){
				System.out.println(v.getNome()+" do tipo "+v.getTipo());
			}
			
		 // terceira parte: lendo e executando os comandos
			
		stop = false;
          while ((line = br.readLine()) != null) {
				
        	  if(stop==true){
        		 return; 
        	  }
        	  
				//Caso encontre a palavra reservada inicio parar o loop
				if(procurarComandoE(line, "fimalgoritmo")){
					System.out.println("terminou a execução do código.");
					break;									
				}
								
				if(!verificarVar(line)){
				 
				  if(!executarCodigo(line,br)){
					  return;
				  }
				}		
				
			}			
	
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		showErro("*** Fim da execução.");
		System.out.println(ConsoleActivity.textConsole.getText());	
	}
     		
	
	public boolean procurarComandoE(String linha,String comando){
		
		
		if(linha.contains(comando)){
			
			int pos = linha.indexOf("algoritmo")+9;
			int nomeInicio = -1;
			int nomeFim= -1;
			for(int i= pos; i<linha.length();i++){
				
				if( linha.charAt(i) == '\"'){
					if (nomeInicio == -1){
					 nomeInicio =i+1;
					 }else{
						 nomeFim = i;
					 }
				}									
			}
			
        	if ( nomeInicio != -1 && nomeInicio != nomeFim && nomeFim != -1){
				
				algoritmoNome = linha.substring(nomeInicio, nomeFim);				
				System.out.println(" Nome do algoritmo: "+algoritmoNome);
				return true;
			}
		}
		
		
		if(linha.contains("var")){
			
			return true;
		}
		
         if(linha.contains("inicio")){
			
			return true;
		}
		
		  if(linha.contains("fimalgoritmo")){
				
				return true;
			}
		
		return false;
	}

	public boolean verificarVar(String linha){
							
		if(linha.length()<1)
			return false;
		
		if(!linha.contains("<-"))			
			return false;		
		
		try {
			
		
		int inicio =-1;
		int fim =-1;
		String var ="";
				
		for(int i= 0; i<linha.length();i++){
			
			if (inicio == -1 && linha.charAt(i)!= ' '){				
				inicio= i;				
			}
			
			if (fim == -1 && inicio != -1 && (linha.charAt(i) == ' ' || (linha.charAt(i)== '<'))){				
				fim= i;				
				break;
			}
			
		}
		if (fim == -1)
			fim = linha.length();
		
		var = linha.substring(inicio,fim);
		
		//verificando se variável foi declarada no inicio do programa
		Var	variavel = varExists(var);
	        
		    if(variavel == null){
		    	
		    	showErro("Erro de sintaxe: Variável "+var+"não encontrada. ");												
			return false;
		    }
	
		
		
		
		//trabalhando com a variável
		if (variavel.getTipo().equals("inteiro"))
		{		 			
			 VarWork  method = new InteiroWork(); 
			 return method.executar(linha, this, variavel);
			
		}		
		
		/**		 
		 * Tipo Real
		 */
		if (variavel.getTipo().equals("real"))
		{
		
			 VarWork  method = new realWork(); 
			 return method.executar(linha, this, variavel);
			
		}
		
		/**		  
		 * Tipo lógico
		 */
		if (variavel.getTipo().equals("logico"))
		{
		 
			 VarWork  method = new logicoWork(); 
			 return method.executar(linha, this, variavel);
			
		}	
		
		/**		  
		 * Tipo caractere
		 */
		if (variavel.getTipo().equals("caractere"))
		{
			 VarWork  method = new caractereWork(); 
			 return method.executar(linha, this, variavel);
			
		}
		
		}catch(Exception e){
			showErro("Erro ao tentar atribuir valores.");
			return false;
		}
		
		return false;
		
	}
		
	public boolean executarCodigo(String linha,BufferedReader br){
		
		if(linha.contains("escreva(")){
			 escreva(linha);				
			}
		
		if(linha.contains("escreval(")){
		 escreval(linha);				
		}
		
		if(((linha.contains("se") && !linha.contains("senao"))|| linha.contains("entao") && (!linha.contains("escreva") || linha.contains("<-")))){
	 		
			if(!linha.contains("se")){
	 			showErro("Erro de sintaxe: Era esperado um 'se'");
	 			return false;
	 		}
	 		
	 		if(!linha.contains("entao")){
	 			showErro("Erro de sintaxe: Era esperado um 'entao'");
	 			return false;
	 		}
			
		   try{
			if(!condicaoSe(linha,br)){
				return false;
			}
		   }catch(Exception e){
			   showErro("Erro na função se");
		   }
		 }
		
		if(linha.contains("para") && linha.contains("faca")){
			try{
		  if(!loopPara(linha,br)){
			  return false;
		  }
		   }catch(Exception e){
			   showErro("Erro na função para");
		   }
		 }
		
		if(linha.contains("leia(")){
			try{
			 leia(linha);
			}catch(Exception e){
				   showErro("Erro na função leia");
			   }
		 }
		
		if((linha.contains("enquanto")  && (!linha.contains("escreva") || linha.contains("<-")))){
	 					
			if(!linha.contains("faca")){
	 			showErro("Erro de sintaxe: Era esperado um 'faca'");
	 			return false;
	 		}
	 		
		   try{
			if(!enquanto(linha,br)){
				return false;
			}
		   }catch(Exception e){
			   showErro("Erro na função enquanto");
		   }
		 }
		
		if((linha.contains("repita")  && (!linha.contains("escreva") || linha.contains("<-")))){
				
	 		
		   try{
			if(!repita(linha,br)){
				return false;
			}
		   }catch(Exception e){
			   showErro("Erro na função repita.");
		   }
		 }
		
		if((linha.contains("escolha")  && (!linha.contains("escreva") || linha.contains("<-")))){
			
	 		
			   try{
				if(!escolha(linha,br)){
					return false;
				}
			   }catch(Exception e){
				   showErro("Erro na função escolha.");
			   }
			 }
			
		if((linha.contains("interrompa")  && (!linha.contains("escreva") || linha.contains("<-")))){
		
				return false;
			 }
		
		return true;
	}
	
	private void leia(String linha){
		int inicio = linha.indexOf("leia(")+5;
		int fim = linha.lastIndexOf(")");
		String texto= linha.substring(inicio,fim);
		
		Var valorV =varExists(texto);
		if (valorV == null){
			System.out.println("Erro, variável não existe");
			return;
		}
		
		try{
		ConsoleActivity.buttonok.post(new Runnable() {
		    public void run() {
		    	ConsoleActivity.buttonok.setEnabled(true);
		    	ConsoleActivity.buttonok.setFocusable(true);
		        
		    } 
		});
		
		
		ConsoleActivity.textin.post(new Runnable() {
		    public void run() {
		    	ConsoleActivity.textin.setEnabled(true);
		        
		    } 
		});
				
		}
		catch(Exception e) { System.out.println(":p");}
	    comfirmaEntrada = false	;
	    //valorV.setValor("77");
	 while(!comfirmaEntrada){
		 //Espere
	 }
	   
	   String t = ConsoleActivity.textin.getText().toString();
	 
	   ConsoleActivity.textin.post(new Runnable() {
		    public void run() {
		    	ConsoleActivity.textin.setText("");
		        
		    } 
		});
	   
		if(valorV.getTipo().equals("caractere"))
	    valorV.setValor(t);
		
		try{
		if(valorV.getTipo().equals("inteiro"))
		valorV.setValor(t);
		System.out.println("deu certo:"+String.valueOf(Integer.parseInt(t)));
		}catch(Exception e){System.out.println("Erro ao converter");}
		
		try{
			if(valorV.getTipo().equals("real"))
			valorV.setValor(String.valueOf(Double.parseDouble(t)));
			}catch(Exception e){}
		
	}
	
 	private boolean condicaoSe(String linha,BufferedReader br){
		
 		
 	
 		
		int inicio = linha.indexOf("se")+2;
		int fim = linha.lastIndexOf("entao");
		String texto= linha.substring(inicio,fim);
	
		
		listaVar.add(new Var("return","logico"));			
		if(verificarVar("return <- "+texto)){
			
		texto= listaVar.get(listaVar.size()-1).getValor();
		listaVar.remove(listaVar.size()-1);
		
		
			
			
			String executarSe="";
			String executarSeNao= "";
			boolean escreverEmSeNao= false;
			int conteIf=0;
			try{
				
			boolean fimse= false;
			while((linha = br.readLine()) != null  ){
								
				
				if(linha.contains("fimse") ){
		
				if(conteIf==0){
				    fimse= true;
					break;			
				}
				else{
				  conteIf--;
				}
				
				}
				
				if(linha.contains("se") && linha.contains("entao") && conteIf== 0){
				//	System.out.println("c++");
					conteIf++;
				}
				
				if(linha.contains("senao") && conteIf==0){
				  escreverEmSeNao= true;
				}
				
				if(escreverEmSeNao == false){
				executarSe+= linha+"\n";
				}
				else{
				executarSeNao+= linha+"\n";
				}
			}
			if(!fimse){
				
		 		showErro("Erro de sintaxe: Era esperado um 'fimse'");		 		
				return false;
			}
			
			if(texto.equals("falso") && escreverEmSeNao==true ){	
			
			 /* trocando o código que seria executado caso se fosse verdadeiro, 
			 agora vai ser executado o código que estava em se não.*/ 
			 executarSe = executarSeNao;
			 texto = "verdadeiro";
			}
			
			if (texto.equals("verdadeiro")){
			
			  //	String into InputStream
			InputStream is = new ByteArrayInputStream(executarSe.getBytes());
		 
			// read it with BufferedReader
			BufferedReader bre = new BufferedReader(new InputStreamReader(is));
			String line="";
			
			System.out.println(executarSe);
			
	        while ((line = bre.readLine()) != null) {						
									
					if(!verificarVar(line)){
					 
						if (!executarCodigo(line,bre)){							
							return false;
						}
					 
					}
					
				}
	        
	        System.out.println("fim do Se para o while");
			
			}else{
				System.out.println(" se = falso");
				return true;
			} 
			
		}
			catch(Exception e){}
		
		}
		else{
			System.out.println("Erro de sintaxe no se entao");	
		}
		return true;
		
	}

 	private boolean loopPara(String linha,BufferedReader br){ 		
 		
 		
 		if( !linha.contains("de")){
 			showErro("Erro de sintaxe: Era esperando um 'de'"); 
 			return false;
 		}
 		
 		if( !linha.contains("ate")){
 			showErro("Erro de sintaxe: Era esperando um 'ate'"); 
 			return false;
 		}
 		
 		//Capiturando informações importante que estão na linha 		
 		int inicio = linha.indexOf("para")+4;
		int fim = linha.lastIndexOf("de");
		String varCont= linha.substring(inicio,fim);
		
 		 inicio = linha.indexOf("de")+2;
		 fim = linha.lastIndexOf("ate");
		 String varlorInicial= linha.substring(inicio,fim);
		 
		 
		 String varlorFinal= null;
		 String varlorPasso= "1";
		 
		 if (!linha.contains("passo") ){
			 		
		 inicio = linha.indexOf("ate")+3;
		 fim = linha.lastIndexOf("faca");
		 varlorFinal= linha.substring(inicio,fim);
		 
		 }else{
			 
			 inicio = linha.indexOf("ate")+3;
			 fim = linha.lastIndexOf("passo");
			 varlorFinal= linha.substring(inicio,fim);
			 
			 
			 inicio = linha.indexOf("passo")+5;
			 fim = linha.lastIndexOf("faca");
			 varlorPasso= linha.substring(inicio,fim);
			 
		 }
		 		 		 		 
		 
			System.out.println(varlorInicial);
			System.out.println(varlorFinal);
		 
			//tirando todos os espaços vazios
			varCont = varCont.replaceAll(" ", "");
					
		    //Validando o que foi coletado		 
			Var valorV = varExists(varCont);
			if (valorV== null){
						
			 //Retorna o erro para o usuario!
		     System.out.println("saindo");
		     showErro("Erro variável "+varCont+" não encontrada.");
			 return false;
			}
			
			String textoInterno="";						
			int conteFor=0;
						
			//Pegando todo o texto que está entre o para e fimpara
	   try{
				
		    boolean fimpara= false;
			while((linha = br.readLine()) != null  ){
												 
				
				if(linha.contains("fimpara") ){
		
				if(conteFor==0){
				
				fimpara= true;
				break;			
				}
				else{
					conteFor--;
				}
				
				}
				
				if(linha.contains("para") && linha.contains("faca") && conteFor== 0){
				//	System.out.println("c++");
					conteFor++;
				}
				
			
				textoInterno+= linha+"\n";
				
			}
			
			if(fimpara== false){
				showErro("Erro de sintaxe: Era esperado um 'fimpara'");
				return false;
			}
	   }catch(Exception e) {  } 
	
	   //Retirando os espaços em branco
	   varlorInicial = varlorInicial.replace(" ","");
	   varlorFinal = varlorFinal.replace(" ","");
	   varlorPasso = varlorPasso.replace(" ","");
	   System.out.println(Integer.parseInt(varlorInicial)+" : "+Integer.parseInt(varlorFinal));
 		
	      /*
	       * Iniciando a execução dos codigos que estavam dentro do for
	       */	     			
		int passo = Integer.parseInt(varlorPasso);
			
		 for(int i = Integer.parseInt(varlorInicial) ; i<= Integer.parseInt(varlorFinal); i+= passo  ){
			 try{
			 
				 if(stop==true){
	        		 return false; 
	        	  }
				 
				 valorV.setValor(String.valueOf(i));
				 
	            // String into InputStream
				InputStream is = new ByteArrayInputStream(textoInterno.getBytes());			 
				// Read it with BufferedReader
				BufferedReader bre = new BufferedReader(new InputStreamReader(is));
				String line="";						
				
		        while ((line = bre.readLine()) != null) {						
										
		        	   		        	   
		        	    
						if(!verificarVar(line)){
						   
							if(!executarCodigo(line,bre)){
								
								return true;
							}
						}
						
					}
		        
			 } catch(Exception e){
				 
				 System.out.println("aconteceu alguma coisa :(");
				 }
			 
			 
			 
		 }
	   
	   		return true;		 
 	}
 	 	  	
	private void escreval(String linha){		
		
		//validando entrada
		if(!linha.contains(")")){
			showErro("Erro de sintaxe: ')' não foi encontrado no final.");
			showErro("Erro na função escreval.");
			return;
		}
		
		int inicio = linha.indexOf("escreval(")+9;
		int fim = linha.lastIndexOf(")");	
		
		textoChange="";
		textoChange = linha.substring(inicio,fim);
				
		ArrayList<String> valores = new ArrayList<String>();
		
		// capturando todos os valores dentro das virgulas
		while(textoChange.contains(",")){
		   String temp= textoChange.substring(0,textoChange.indexOf(","));
		 				 
		 valores.add(temp);
		 textoChange = textoChange.substring(textoChange.indexOf(",")+1,textoChange.length());
		}
				
		 valores.add(textoChange);
		 
			for(int i=0; i<valores.size();i++){
				
				    //Caso a seguência capturas tenha aspas.
				   if(!valores.get(i).contains("\"")){
					   				   
					listaVar.add(new Var("return","real"));			
					
					try{
					if(!verificarVar("return <- "+valores.get(i))){
						showErro("Erro de sintaxe: verifique se as variáveis foram escritas corretamente. ");
						showErro("Erro na função escreval");
						listaVar.remove(listaVar.size()-1);	
						return;
					}
					}catch(Exception e){
						
						showErro("Erro de sintaxe: verifique se as variáveis foram escritas corretamente. ");
						showErro("Erro na função escreval");
						listaVar.remove(listaVar.size()-1);	
						return;	
					}
					
					valores.remove(i); 
					String valorReturn =listaVar.get(listaVar.size()-1).getValor();
					
					if( valorReturn.charAt(valorReturn.indexOf(".")+1) == '0' && valorReturn.length() == valorReturn.indexOf(".")+2){
						valorReturn = valorReturn.substring(0,valorReturn.indexOf("."));
					}
					valores.add(i, "\""+valorReturn+"\"");
					listaVar.remove(listaVar.size()-1);		
					}				   
				
			}
				
		  //concatenar todos os valores
		    for(int i=0; i<valores.size();i++ ){			 
		        
		    	if(i>0){
		             textoChange+=" + "+valores.get(i);
		    	}
		    	 if(i==0)
		    	{
		    		textoChange=valores.get(0);	
		    	}
			   		    							
		    
	    	}		
				   
		    
		
		listaVar.add(new Var("return","caractere"));			
		if(verificarVar("return <- "+textoChange)){
			
		textoChange= listaVar.get(listaVar.size()-1).getValor();
		listaVar.remove(listaVar.size()-1);
		
		ConsoleActivity.textConsole.post(new Runnable() {
		    public void run() {
		    	String text= textoChange;
		    	ConsoleActivity.textConsole.append(text+"\n");	
		        
		    } 
		});
		
		ConsoleActivity.scroller.post(new Runnable() {
		    public void run() {
		    
		    ConsoleActivity.scroller.smoothScrollTo(0, ConsoleActivity.textConsole.getBottom());	
		        
		    } 
		});
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else{
			System.out.println("Erro de sintaxe no escreval");	
		}
		
	}
			
    private void escreva(String linha){		
		
    	//validando entrada
    			if(!linha.contains(")")){
    				showErro("Erro de sintaxe: ')' não foi encontrado no final.");
    				showErro("Erro na função escreva.");
    				return;
    			}
    	
    	int inicio = linha.indexOf("escreva(")+8;
		int fim = linha.lastIndexOf(")");
		textoChange="";
		textoChange = linha.substring(inicio,fim);
				
		ArrayList<String> valores = new ArrayList<String>();
		
		while(textoChange.contains(",")){
		   String temp= textoChange.substring(0,textoChange.indexOf(","));
		 				 
		 valores.add(temp);
		 textoChange = textoChange.substring(textoChange.indexOf(",")+1,textoChange.length());
		}
				
		 valores.add(textoChange);
		 
			for(int i=0; i<valores.size();i++){
				
				   if(!valores.get(i).contains("\"")){
					   				   
					listaVar.add(new Var("return","real"));			
					
					if(!verificarVar("return <- "+valores.get(i))){
						showErro("Erro de sintaxe: verifique se as variáveis foram escritas corretamente. ");
						showErro("Erro na função escreva");
						listaVar.remove(listaVar.size()-1);
						return;
					}
					valores.remove(i); 
					valores.add(i, "\""+listaVar.get(listaVar.size()-1).getValor()+"\"");
					listaVar.remove(listaVar.size()-1);		
					}				   
				
			}
				
		  //concatenar todos os valores
		    for(int i=0; i<valores.size();i++ ){			 
		        
		    	if(i>0){
		             textoChange+=" + "+valores.get(i);
		    	}
		    	 if(i==0)
		    	{
		    		textoChange=valores.get(0);	
		    	}
			   		    							
		    
	    	}		
				   
		    
		
		listaVar.add(new Var("return","caractere"));			
		if(verificarVar("return <- "+textoChange)){
			
			textoChange= listaVar.get(listaVar.size()-1).getValor();
		listaVar.remove(listaVar.size()-1);
		
		ConsoleActivity.textConsole.post(new Runnable() {
		    public void run() {
		    	String text= textoChange;
		    	ConsoleActivity.textConsole.append(text);	
		        
		    } 
		});
		
		ConsoleActivity.scroller.post(new Runnable() {
		    public void run() {
		    
		    ConsoleActivity.scroller.smoothScrollTo(0, ConsoleActivity.textConsole.getBottom());	
		        
		    } 
		});
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else{
			System.out.println("Erro de sintaxe no escreval");	
		}
		
		
	}
    
	
	public Var varExists(String var){				
			
		
	 // Verificando se o nome da variável está se referindo a um vetor criado pelo usuário.
	 if(var.contains("[") && var.contains("]")){
		
			if( !var.substring(var.indexOf("["),var.indexOf("]")).contains(",") ){
				
				int start = var.indexOf("[")+1;
				int end   = var.indexOf("]");															
				String valor = var.substring(start,end);
				var = var.substring(0,start-1)+"§"+valor+"§";		
				
				 
				}
				else{
					

					int start = var.indexOf("[")+1;
					int end   = var.indexOf(",");															
					String valor_1 = var.substring(start,end);
					
					int start2 = var.indexOf(",")+1;
					end   = var.indexOf("]");	
					String valor_2 = var.substring(start2,end);
					
					var = var.substring(0,start-1)+"§"+valor_1+"§"+valor_2+"§";		
				}
			    
		 
	}
	 
	 
	 
	        for(Var v: listaVar){
			
			  if( var.equals(v.getNome())){		
			   return v;
			  }
	        }
				
		return null;
	}
	
	
	public boolean procurarVar(String linha){

		
		if(linha.length()<1){
		 
			return true;
		}
		
		if(linha.contains("//")){
			 // é comentario então retorna;
			return true;
		}
		
		if (!linha.contains(":")){
			showErro("Erro de sintaxe: Era esperado ':' ");	
			return false;
		}
		
		int inicio= -1;
		int fim = -1;
		String tipo ="";
		String nome ="";
		ArrayList<String> listaVarNome= new ArrayList<String>();
		
		if(linha.contains("real")){
			 tipo= "real" ; 
			}else if (linha.contains("inteiro")){
				tipo="inteiro";
			}else if (linha.contains("caractere")){
				tipo="caractere";
			}else if (linha.contains("literal")){
				tipo="caractere";
			}
			else if (linha.contains("logico")){
				tipo="logico";
			}
			
		if (tipo.length()<1){
			showErro("Erro de sintaxe: Era esperado o tipo da variável. ");
			return false;
		}
		
		if(linha.contains("[") && linha.contains("]")){
			if(!linha.contains("vetor")){
				showErro("Erro de sintaxe: Era esperado 'vetor' ");	
				return false;
			}
		}
		
		if (linha.contains("vetor")){
			
			if(!linha.contains("[")){
				showErro("Erro de sintaxe: Era esperado ']' ");	
				return false;
			}
			if(!linha.contains("]")){
				showErro("Erro de sintaxe: Era esperado ']' ");	
				return false;
			}
			if(!linha.contains("..")){
				showErro("Erro de sintaxe: Era esperado '..' ");	
				return false;
			}
			if(!linha.contains("de")){
				showErro("Erro de sintaxe: Era esperado um 'de' ");	
				return false;
			}
		}
		
		if (linha.contains("[")){
			if(!linha.contains("]")){
				showErro("Erro de sintaxe: Era esperado ']' ");	
				return false;
			}
		}
		
		for(int i= 0; i<linha.length();i++){
			
			
			if (fim == -1 && inicio != -1 && (linha.charAt(i) == ' ' || linha.charAt(i)== ':' || linha.charAt(i) == ',')){				
				fim= i;				
						
			}
								
			if (inicio == -1 && linha.charAt(i)!= ' '  && linha.charAt(i)!= ':' && linha.charAt(i)!= ','){				
				inicio= i;				
			}
																
			if( inicio != -1 && fim !=-1){
				
				if(linha.contains("[") && linha.contains("..") && linha.contains("]")  ){
					
				/*	if ( (!linha.contains("vetor") && !linha.contains("de")) || (linha.indexOf("vetor")>linha.indexOf("de") ) ){
						//Erro de Sintaxe
						return false;
					}*/
					
					
					int start=-1;
					int end= -1;
					
					String nomeVar= linha.substring(inicio,fim)+"§";
										
					if( !linha.substring(linha.indexOf("["),linha.indexOf("]")).contains(",") ){
					
					start = linha.indexOf("[")+1;
					end   = linha.indexOf("..");															
					String valor_1 = linha.substring(start,end);
					
					start = linha.indexOf("..")+2;
					end   = linha.indexOf("]");								
					String valor_2 = linha.substring(start,end);
					
					 if(Integer.parseInt(valor_1)<0 || Integer.parseInt(valor_2)<=Integer.parseInt(valor_1)){
						 //Erro de sintaxe
						 return false;
					 }
					 
					 for(int l=Integer.parseInt(valor_1); l<Integer.parseInt(valor_2);l++){
						 
						 listaVarNome.add(nomeVar+l+"§");
					 }
					
						if (linha.charAt(i)== ':' ) {					
							break;
						}
					 
					}
					else{
						

						start = linha.indexOf("[")+1;
						end   = linha.indexOf("..");															
						String valor_1 = linha.substring(start,end);					
						
						start = linha.indexOf("..")+2;
						end   = linha.lastIndexOf(",");								
						String valor_2 = linha.substring(start,end);
						
						start = linha.lastIndexOf(",")+1;
						end   = linha.lastIndexOf("..");								
						String valor_3 = linha.substring(start,end);
												
						
						start = linha.lastIndexOf("..")+2;
						end   = linha.indexOf("]");								
						String valor_4 = linha.substring(start,end);
												
						
						 if( Integer.parseInt(valor_1)<0 || Integer.parseInt(valor_2)<=Integer.parseInt(valor_1) ||
						     Integer.parseInt(valor_3)<0 || Integer.parseInt(valor_3)<=Integer.parseInt(valor_4) ){
							 //Erro de sintaxe
							// return false;
						 }
						 
						 for(int l=Integer.parseInt(valor_1); l<Integer.parseInt(valor_2);l++){
							 for( int k= Integer.parseInt(valor_3); k<Integer.parseInt(valor_4); k++){
								 
							 listaVarNome.add(nomeVar+l+"§"+k+"§");
							 }
						 }
						
							if (linha.charAt(i)== ':' ) {					
								break;
							}
					}
					
				}else{
									
				listaVarNome.add(linha.substring(inicio,fim));
				}
				
				inicio= -1;
				fim=-1;
			}
		}
		
									
		for( String s : listaVarNome ) {
								
		  listaVar.add(new Var(s,tipo));
		}			
		
		return true;
	}

		
	
    public boolean isComfirmaEntrada() {
		return comfirmaEntrada;
	}

	public void setComfirmaEntrada(boolean comfirmaEntrada) {
		this.comfirmaEntrada = comfirmaEntrada;
	}

    public void showErro(String textoErro){
    	
    	textoChange= textoErro;
    	
    	ConsoleActivity.textConsole.post(new Runnable() {
		    public void run() {

		    	Spanned sp= Html.fromHtml("<font color=\"#F2C598\">§ "+textoChange+"</font>");
		    	ConsoleActivity.textConsole.append(sp);
		    	ConsoleActivity.textConsole.append("\n");
		        
		    } 
		});
		
		ConsoleActivity.scroller.post(new Runnable() {
		    public void run() {
		    
		    ConsoleActivity.scroller.smoothScrollTo(0, ConsoleActivity.textConsole.getBottom());	
		        
		    } 
		});
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
	
    public boolean enquanto(String linha,BufferedReader br){
    	    	
 	
		int inicio = linha.indexOf("enquanto")+8;
		int fim = linha.lastIndexOf("faca");
		String texto= linha.substring(inicio,fim);
									
			String executarEquanto="";						
			int conteEnquanto=0;
			
			try{
				
			boolean fimenquanto= false;
			while((linha = br.readLine()) != null  ){
								
				
				if(linha.contains("fimenquanto") ){
		
				if(conteEnquanto==0){
					fimenquanto= true;
					break;			
				}
				else{
					conteEnquanto--;
				}
				
				}
				
				if(linha.contains("enquanto") && linha.contains("faca") && conteEnquanto== 0){
				//	System.out.println("c++");
					conteEnquanto++;
				}					
								
				executarEquanto+= linha+"\n";	
					
				
			}
						
			if(!fimenquanto){
				
		 		showErro("Erro de sintaxe: Era esperado um 'fimenquanto'");		 		
				return false;
			}
			
			
			listaVar.add(new Var("return","logico"));	
			
			if(!verificarVar("return <- "+texto)){
				
				listaVar.remove(listaVar.size()-1);
				return false;
			}			
			String logica= listaVar.get(listaVar.size()-1).getValor();
			
			listaVar.remove(listaVar.size()-1);
					
			while(logica.equals("verdadeiro")){
				
				 if(stop==true){
	        		 return false; 
	        	  }
				 
			  //	String into InputStream
			InputStream is = new ByteArrayInputStream(executarEquanto.getBytes());
		 
			// read it with BufferedReader
			BufferedReader bre = new BufferedReader(new InputStreamReader(is));
			String line="";
						
			
	        while ((line = bre.readLine()) != null) {						
									
					if(!verificarVar(line)){
					  
						if (!executarCodigo(line,bre)){
							return true;
						}
					 
					}
					
				}
	        
	        
	        
			listaVar.add(new Var("return","logico"));		
			if(!verificarVar("return <- "+texto)){
				
				listaVar.remove(listaVar.size()-1);
				return false;
			}			
			logica= listaVar.get(listaVar.size()-1).getValor();
			listaVar.remove(listaVar.size()-1);
			}
			
		}
		catch(Exception e){}				
		
		return true;
    	    	
    }
    
    public boolean repita(String linha,BufferedReader br){
    	
     	
    		
    									
    			String executarRepita="";						
    			int conteRepita=0;
    			String texto="";
    			
    			try{
    				
    			boolean ate= false;
    			while((linha = br.readLine()) != null  ){    					
    				
    				if(linha.contains("ate") ){
    		
    				if(conteRepita==0){
    					
    					int inicio = linha.indexOf("ate")+3;
    	        		int fim = linha.length();
    	        		texto= linha.substring(inicio,fim);
    					ate= true;
    					break;			
    				}
    				else{
    					conteRepita--;
    				}
    				
    				}
    				
    				if(linha.contains("repita") && !linha.contains("escreva") && !linha.contains("<-") && conteRepita== 0){
    				//	System.out.println("c++");
    					conteRepita++;
    				}					
    								
    				executarRepita+= linha+"\n";	
    					    				
    			}
    			
    			
    			if(!ate){
    				
    		 		showErro("Erro de sintaxe: Era esperado um 'ate'");		 		
    				return false;
    			}
    			
    		
        		
    		String logica="";	
    		
    					
    		do{
    			
    				 if(stop==true){
    	        		 return false; 
    	        	  }
    				 
    			  //	String into InputStream
    			InputStream is = new ByteArrayInputStream(executarRepita.getBytes());
    		 
    			// read it with BufferedReader
    			BufferedReader bre = new BufferedReader(new InputStreamReader(is));
    			String line="";
    						
    			
    	        while ((line = bre.readLine()) != null) {						
    									
    					if(!verificarVar(line)){
    					    if(!executarCodigo(line,bre)){
    					    	return true;
    					    }
    					 
    					}
    					
    				}
    	        
    	        
    	        
    			listaVar.add(new Var("return","logico"));		
    			if(!verificarVar("return <- "+texto)){
    				
    				listaVar.remove(listaVar.size()-1);
    				return false;
    			}			
    			logica= listaVar.get(listaVar.size()-1).getValor();
    			listaVar.remove(listaVar.size()-1);
    			
    			}while(!logica.equals("verdadeiro"));
    			
    		}
    		catch(Exception e){}				
    		
    		return true;
        	    	
        }

    public boolean escolha(String linha,BufferedReader br){
    	
    	/* Capturando a expressão de seleção que se encontra logo após a palavra escolha. */
    	int inicio= linha.indexOf("escolha")+7;
    	int fim= linha.length();
    	
    	String var=linha.substring(inicio,fim);
    	var = var.replaceAll(" ","");
    	// Verificando a existência dessa variável.
    	Var expressaoDeSelecao = varExists(var);
    	
    	if( expressaoDeSelecao==null){
    		showErro("Erro : variável "+var+" não encontrada.");
    		return false;
    	}
    	
    	//Comandos que serem executados
    	String comandos=""; 
    	
    	/* Quando for verdadeiro, o while a seguir irá capturar as 
    	linhas até encontrar um caso ou o final do escolha*/
    	boolean captura = false; 
    	
    	/* Array para guarda os valores de comparação que estão logo após todas as palavras caso.*/
    	ArrayList<String> valores = new ArrayList<String>();
    	String editable,temp;
    	
    	boolean fimCaptura = false;
    	boolean fimescolha = false;
    	
    	try {
			while((linha = br.readLine()) != null  ){
			   
				 
			if(captura==true && fimCaptura == false){
				if(linha.contains("caso") || linha.contains("outrocaso")){
					fimCaptura= true;
				}				
				
				comandos+= linha;					
			}
				
			if(linha.contains("outrocaso")){
				captura= true;
			}
			
			if(linha.contains("fimescolha")){
				fimescolha = true;
				break;
			}
			
			
			if(linha.contains("caso") && captura == false){
					
					editable=linha.substring(linha.indexOf("caso")+4,linha.length());
					valores.clear();
					
					while(editable.contains(",")){
												
						//Capturando o valor que está mais a esquerda até encontrar uma virgula
						temp= editable.substring(0,editable.indexOf(","));
						temp= temp.replaceAll(" ", "");
					    valores.add(temp);	
						//Atualizando editable, removando o valor capturado
						editable = editable.substring(editable.indexOf(",")+1,editable.length());						
					}
					// capturando o ultimo valor restante
					temp=editable.substring(0,editable.length());
					temp= temp.replaceAll(" ", "");
					valores.add(temp);
					
					for(String v : valores){						
						String valorOb = expressaoDeSelecao.getValor().replaceAll(" ", "");						
						if(valorOb.equals(v)){
														
							captura= true;
						}
					}
			}
							
				
		
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}    	
    	
    	if(!fimescolha){
    		showErro("Erro de sintaxe: Era esperado um 'fimescolha'");
    		return false;
    	}
    	
    	// Get byte de comandos
    	InputStream is = new ByteArrayInputStream(comandos.getBytes());		  
		BufferedReader bre = new BufferedReader(new InputStreamReader(is));
		String line="";
						
        try {
			while ((line = bre.readLine()) != null) {						
									
					if(!verificarVar(line))
					 executarCodigo(line,bre);
					
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	return true;
    }
}

