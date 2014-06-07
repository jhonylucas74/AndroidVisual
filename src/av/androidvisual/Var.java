package av.androidvisual;

public class Var {

	private String nome;
	private String valor;
	private String tipo;
	
	public Var(String n,String t){
		nome =n;
		tipo= t;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
