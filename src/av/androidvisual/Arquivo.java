package av.androidvisual;
import java.io.Serializable;

public class Arquivo implements Serializable {

	String nome;
	String data;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}



