package av.parserVisual;

import java.util.ArrayList;

public class ParserMath {
		
	public String mathString(String text){
		
		ArrayList<String> list = new ArrayList<String>();
		
		int inicio = -1;
		String valor="";
		int fim = -1;		
		int contPareteses = 0;
		
		if (text.length()<1)
			return null;
		
		System.out.println("iniciando método "+text);
		
		//separando e indentificando valores e operadores
        for( int i = 0; i<text.length();i++){			       
        	
			if (inicio == -1 &&
					text.charAt(i) != ' ' && text.charAt(i) != '!' && text.charAt(i) != '|' && text.charAt(i) != '%' &&
					text.charAt(i) != '&' && text.charAt(i) != '=' && text.charAt(i) != '<' && text.charAt(i) != '>' &&
				    text.charAt(i) != '+' && text.charAt(i) != '-' && text.charAt(i) != '*' && text.charAt(i) != '/' &&
					text.charAt(i) != '(' && text.charAt(i) != ')' && text.charAt(i) != '$' && text.charAt(i) != '#' &&
					text.charAt(i) != '@' && text.charAt(i) != '!' && text.charAt(i) != '?' && text.charAt(i) != '^'){				
				
				inicio= i;					 	
			}
			
			if (fim == -1 && inicio != -1 &&
				   (text.charAt(i) == ' ' || (text.charAt(i) == '!' || text.charAt(i) == '|'  || text.charAt(i) == '%' ||
					text.charAt(i) == '&' || text.charAt(i) == '='  || text.charAt(i) == '<'  || text.charAt(i) == '>' ||
					text.charAt(i) == '+' || text.charAt(i) == '-'  || text.charAt(i) == '*'  || text.charAt(i) == '/' ||
					text.charAt(i) == '(' || text.charAt(i) == ')'  || text.charAt(i) == '$'  || text.charAt(i) == '#' ||
					text.charAt(i) == '@' || text.charAt(i) == '!'  || text.charAt(i) == '?'  || text.charAt(i) == '^'))){												
				
				fim= i;											
			}
			
			if (fim == -1 && inicio != -1 &&  i+1 == text.length()){												
				fim= i+1;											
			}
			
			
			if(inicio!= -1 && fim != -1){
				valor = text.substring(inicio,fim);				
				
				if(valor.length()>0)
					list.add(valor);
				
				inicio = -1;
				fim    = -1;				
											
			}
			
			if(     text.charAt(i)== '!' || text.charAt(i)== '|' || text.charAt(i)== '&' || text.charAt(i)== '=' ||
					text.charAt(i)== '>' ||	text.charAt(i)== '<' || text.charAt(i)== '+' || text.charAt(i)== '-' ||
					text.charAt(i)== '/' ||	text.charAt(i)== '*' || text.charAt(i)== '(' || text.charAt(i)== ')' ||
					text.charAt(i)== '$' ||	text.charAt(i)== '#' || text.charAt(i)== '@' || text.charAt(i)== '!' ||
					text.charAt(i)== '?' || text.charAt(i)== '%' || text.charAt(i)== '^')			
				
				list.add(String.valueOf(text.charAt(i))); 			
		}
        
     
        //contando os parênteses
        inicio= -1;        
        
        for( int i = 0; i<text.length();i++){
        	
        	if (inicio == -1 && (text.charAt(i) == '('))
        	inicio = 1;
        	
        	if (inicio ==  1 &&  (text.charAt(i) == ')')){
        	 inicio =-1; 
        	 contPareteses++;
        	}
        }
        
        
        //encontrar parênteses
        System.out.println("texto de entrada: "+text);
        
        
        while(contPareteses>0){
        	
        	
        	//ConsoleActivity.textConsole.append("entrei");
        	System.out.println("entrei no while, o texto contém parênteses");
        	int prts = 0; //numeros de abri parênteses
        	int start= -1;
        	int end= -1;
        	
        	
        	
        	for(int i=0 ;i<list.size();i++){
        		
        		if(list.get(i).toString().equals("(")){
        			
        			if(prts == 0){
        			   start= i;
        			   prts++;
        			}
        			else
        			  prts++;        			        			        			
        		}
        		
        		
        		if(list.get(i).toString().equals(")")){
        			
        			if(prts == 1)
        			  end= i;
        			else
        			  prts--;        			        			        			
        		}
        		
        	}
        	
        	//texto capturado entre os parênteses
        	String inPrts ="";
        	if (start != end){        		        	
        	for(int i=start+1;i<=end-1;i++){
        	  inPrts+= list.get(i).toString()+" ";
        	}
        	}
        	 
        	String result = mathString(inPrts);
                        
        	
        	if (inPrts.length()>0)
        	System.out.println(result);
        	else
        	System.out.println("erro result");
        	
        	//ConsoleActivity.textConsole.append(result+"\n");
        	
        	for(int i=0; i<= end-start ; i++ ){
        		list.remove(start);
        	}
        	
        	list.add(start,result);
        	contPareteses--;
        	
        	System.out.println("finalizou o while com sucesso.");
       }
        
        
       
        
        System.out.println("iniciando calculo.");
        //calculando de fato
        String cal= "";
        for(int i= 0 ; i<list.size();i++){
        	
        	if(!(i-1<0) && !(i >= list.size())){        		 
        		 if (list.get(i).equals("*")){  
        			 System.out.println("emtrei no menos");
        			 double a = Double.parseDouble(list.get(i-1).toString());
        			 double b = Double.parseDouble(list.get(i+1).toString());
        			 double resu =a*b;
        			 cal = String.valueOf(resu);

        			 list.remove(i-1);
        			 list.remove(i-1);
        			 list.remove(i-1);
        			 list.add(i-1,cal);
        			i=0;
        			System.out.println("multiplicação relizada");
        		 }
        		 
        		}        	
        }
        
        for(int i= 0 ; i<list.size();i++){
        	
        	if(     list.get(i).equals("=") || list.get(i).equals(">") || list.get(i).equals("<") || list.get(i).equals("+") ||
        			list.get(i).equals("-") || list.get(i).equals("*") || list.get(i).equals("/") || list.get(i).equals("$") ||
        			list.get(i).equals("#") || list.get(i).equals("@") || list.get(i).equals("!") || list.get(i).equals("?") ||
        			list.get(i).equals("%") || list.get(i).equals("^")){
        		
        		if(!(i-1<0) && !(i >= list.size())){
        		 
        		 if (list.get(i).equals("+")){        			
        			 double a = Double.parseDouble(list.get(i-1).toString());
        			 double b = Double.parseDouble(list.get(i+1).toString());
        			 double resu =a+b;
        			 cal = String.valueOf(resu);

        			 list.remove(i-1);
        			 list.remove(i-1);
        			 list.remove(i-1);
        			 list.add(i-1,cal);
        			 i=0;
        			 System.out.println("soma relizada");
        		 }
        		}
        		
        		if(!(i-1<0) && !(i >= list.size())){        		 
        		 if (list.get(i).equals("-")){  
        			 System.out.println("emtrei no menos");
        			 double a = Double.parseDouble(list.get(i-1).toString());
        			 double b = Double.parseDouble(list.get(i+1).toString());
        			 double resu =a-b;
        			 cal = String.valueOf(resu);

        			 list.remove(i-1);
        			 list.remove(i-1);
        			 list.remove(i-1);
        			 list.add(i-1,cal);
        			 i=0;
        			 
        			 System.out.println("subtração relizada");
        		 }
        		}
        			         	
        			
          		if(!(i-1<0) && !(i >= list.size())){        		 
            		 if (list.get(i).equals("/")){  
            			 System.out.println("entrei no menos");
            			 double a = Double.parseDouble(list.get(i-1).toString());
            			 double b = Double.parseDouble(list.get(i+1).toString());
            			 double resu =a/b;
            			 cal = String.valueOf(resu);

            			 list.remove(i-1);
            			 list.remove(i-1);
            			 list.remove(i-1);
            			 list.add(i-1,cal);
            			 i=0;
            			 
            			 System.out.println("divisão relizada");
            		 }
             	}
        	
          		if(!(i-1<0) && !(i >= list.size())){        		 
           		 if (list.get(i).equals("%")){  
           			 System.out.println("entrei no menos");
           			 double a = Double.parseDouble(list.get(i-1).toString());
           			 double b = Double.parseDouble(list.get(i+1).toString());
           			 int ai= (int) a;
           			 int bi= (int) b;
           			 double resu = ai % bi;
           			 cal = String.valueOf(resu);

           			 list.remove(i-1);
           			 list.remove(i-1);
           			 list.remove(i-1);
           			 list.add(i-1,cal);
           			 i=0;
           			 
           			 System.out.println("divisão relizada");
           		 }
            	}
          		
          		if(!(i-1<0) && !(i >= list.size())){        		 
              		 if (list.get(i).equals("^")){  
              			 System.out.println("entrei no menos");
              			 double a = Double.parseDouble(list.get(i-1).toString());
              			 double b = Double.parseDouble(list.get(i+1).toString());
              			 double resu =  Math.pow(a,b);
              			 cal = String.valueOf(resu);

              			 list.remove(i-1);
              			 list.remove(i-1);
              			 list.remove(i-1);
              			 list.add(i-1,cal);
              			 i=0;
              			 
              			 System.out.println("divisão relizada");
              		 }
               	}
          		
        		if(!(i-1<0) && !(i >= list.size())){
           		 
           		 if (list.get(i).equals(">")){        
           			 
           			System.out.println("entrei  >>>> .");
           			
           			 double a = Double.parseDouble(list.get(i-1).toString());
           			 double b = Double.parseDouble(list.get(i+1).toString());
           			 if(a>b)              			 	
              			 cal = "1";
              			 else
              			 cal = "0";	 

           			 list.remove(i-1);
           			 list.remove(i-1);
           			 list.remove(i-1);
           			 list.add(i-1,cal);
           			 i=0;
           			 
           		 }
           		}
        		
        		if(!(i-1<0) && !(i >= list.size())){
              		 
              		 if (list.get(i).equals("<")){        			
              			 double a = Double.parseDouble(list.get(i-1).toString());
              			 double b = Double.parseDouble(list.get(i+1).toString());
              			 
              			 if(a<b)              			 	
              			 cal = "1";
              			 else
              			 cal = "0";	 

              			 list.remove(i-1);
              			 list.remove(i-1);
              			 list.remove(i-1);
              			 list.add(i-1,cal);
              			 i=0;
              			 
              		 }
              		}
        		
        		if(!(i-1<0) && !(i >= list.size())){
              		 
              		 if (list.get(i).equals("=")){        			
              			 double a = Double.parseDouble(list.get(i-1).toString());
              			 double b = Double.parseDouble(list.get(i+1).toString());
              			 if(a == b)              			 	
                  			 cal = "1";
                  			 else
                  			 cal = "0";	 

              			 list.remove(i-1);
              			 list.remove(i-1);
              			 list.remove(i-1);
              			 list.add(i-1,cal);
              			 i=0;
              			 
              		 }
              		}
        		
        		
        		if(!(i-1<0) && !(i >= list.size())){
             		 
             		 if (list.get(i).equals("$")){        			
             			 double a = Double.parseDouble(list.get(i-1).toString());
             			 double b = Double.parseDouble(list.get(i+1).toString());
             			 if(a >= b)              			 	
                 			 cal = "1";
                 			 else
                 			 cal = "0";	 

             			 list.remove(i-1);
             			 list.remove(i-1);
             			 list.remove(i-1);
             			 list.add(i-1,cal);
             			 i=0;
             			 
             		 }
             		}
        		
        		if(!(i-1<0) && !(i >= list.size())){
            		 
            		 if (list.get(i).equals("#")){        			
            			 double a = Double.parseDouble(list.get(i-1).toString());
            			 double b = Double.parseDouble(list.get(i+1).toString());
            			 if(a <= b)              			 	
                			 cal = "1";
                			 else
                			 cal = "0";	 

            			 list.remove(i-1);
            			 list.remove(i-1);
            			 list.remove(i-1);
            			 list.add(i-1,cal);
            			 i=0;
            			 
            		 }
            		}
          		
        		if(!(i-1<0) && !(i >= list.size())){
           		 
           		 if (list.get(i).equals("@")){        			
           			 double a = Double.parseDouble(list.get(i-1).toString());
           			 double b = Double.parseDouble(list.get(i+1).toString());
           			 if(a != b)              			 	
               			 cal = "1";
               			 else
               			 cal = "0";	 

           			 list.remove(i-1);
           			 list.remove(i-1);
           			 list.remove(i-1);
           			 list.add(i-1,cal);
           			 i=0;
           			 
           		 }
           		}
        }
        	
        }
        
      for(int i= 0 ; i<list.size();i++){
        	
  		if(!(i-1<0) && !(i >= list.size())){
    		 
    		 if (list.get(i).equals("&")){        			
    			 double a = Double.parseDouble(list.get(i-1).toString());
    			 double b = Double.parseDouble(list.get(i+1).toString());
    			 if(a == 1 && b == 1)              			 	
        			 cal = "1";
        			 else
        			 cal = "0";	 

    			 list.remove(i-1);
    			 list.remove(i-1);
    			 list.remove(i-1);
    			 list.add(i-1,cal);
    			 i=0;
    			 
    		 }
    		}
		
  		
		if(!(i-1<0) && !(i >= list.size())){
   		 
   		 if (list.get(i).equals("|")){        			
   			 double a = Double.parseDouble(list.get(i-1).toString());
   			 double b = Double.parseDouble(list.get(i+1).toString());
   			 if(a == 1 || b == 1)              			 	
       			 cal = "1";
       			 else
       			 cal = "0";	 

   			 list.remove(i-1);
   			 list.remove(i-1);
   			 list.remove(i-1);
   			 list.add(i-1,cal);
   			 i=0;
   			 
   		 }
   		}
		
		if(!(i-1<0) && !(i >= list.size())){
	   		 
	   		 if (list.get(i).equals("?")){        			
	   			 double a = Double.parseDouble(list.get(i-1).toString());
	   			 double b = Double.parseDouble(list.get(i+1).toString());
	   			 if((a == 1 && b == 0) || (a == 0 && b==1) )              			 	
	       			 cal = "1";
	       			 else
	       			 cal = "0";	 

	   			 list.remove(i-1);
	   			 list.remove(i-1);
	   			 list.remove(i-1);
	   			 list.add(i-1,cal);
	   			 i=0;
	   			 
	   		 }
	   		}
		
		
    }
        
      for(int i= 0 ; i<list.size();i++){
      	
  		if(!(i >= list.size())){
    		 
    		 if (list.get(i).equals("!")){        			    			 
    			 double b = Double.parseDouble(list.get(i+1).toString());
    			
    			 if( b == 0)              			 	
        			 cal = "1";
        			 else
        			 cal = "0";	 

    			 list.remove(i);
    			 list.remove(i);    			 
    			 list.add(i,cal);
    			 i=0;
    			 
    		
    			 
    		 }
    		}
      }
        
        String out="";
		for(String s: list)
		 out += s.toString()+" ";        			
		
		return out;
	}
	
}
