package classes;

import java.io.Serializable;

//Classe responsável por gerenciar as frequências de determinado dado.
public class Frequency<T extends Comparable<T>> implements Comparable<Frequency<T>> , Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private T value;
	private int frequency;

	//Não há construtor padrão, pois precisa forçar que seja fornecido um dado.
	
	//Construtor com dado.
	//Pré-condição: c não pode ser null.
	//Pós-condição: nenhuma.
	public Frequency(T c)  {
		this.value = c;
		this.frequency = 0;
	}
	
	//Construtor com dado e frequência.
	//Pré-condição: c não pode ser null e frequency deve ser >= 0.
	//Pós-condição: nenhuma.
	public Frequency(T c, int frequency) {
		this.value = c;
		this.frequency = frequency;
	}

	//Método get para obter o valor.
	//Pré-condição: nenhuma. 
	//Pós-condição: retorna o valor do dado.
	public T getValue() {
		return value;
	}

	//Método get para obter a frequência.
	//Pré-condição: nenhuma.
	//Pós-condição: retorna um int com a frequência do dado.
	public int getFrequency() {
		return frequency;
	}
	
	//Incrementa a frequência do dado.
	//Pré-condição: nenhuma. 
	//Pós-condição: nenhuma.
	public void incFrequency() {
		frequency++;
	}
	
	//Override método equals de Object.
	//Pré-condição: obj deve ser um Frequency. 
	//Pós-condição: return true se Frequency é igual a Obj, caso contrário retorna false.
	@Override
	public boolean equals(Object obj) {
		Frequency<T> f = (Frequency<T>) obj;
		return this.getValue().equals(f.getValue());
	}
	
	
	//Override do método toString de Object.
	//Pré-condição:nenhuma. 
	//Pós-condição: retorna uma string represenntando a Frequência.
	@Override
	public String toString() {
		return "[c=" + value + ", frequecy=" + frequency + "]";
	}


	//Override do método compareTo de Comparable.
	//Pré-condição:o diferente de null. 
	//Pós-condição:return -1 se this < o, return 1 se maior ou return 0 se iguais.
	@Override
	public int compareTo(Frequency<T> o) {
		if(this.frequency < o.frequency)
			return -1;
		else if(this.frequency > o.frequency)
			return 1;
		return 0;
	}








	


	


}
