package classes;

import java.io.Serializable;

//Classe respons�vel por gerenciar as frequ�ncias de determinado dado.
public class Frequency<T extends Comparable<T>> implements Comparable<Frequency<T>> , Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private T value;
	private int frequency;

	//N�o h� construtor padr�o, pois precisa for�ar que seja fornecido um dado.
	
	//Construtor com dado.
	//Pr�-condi��o: c n�o pode ser null.
	//P�s-condi��o: nenhuma.
	public Frequency(T c)  {
		this.value = c;
		this.frequency = 0;
	}
	
	//Construtor com dado e frequ�ncia.
	//Pr�-condi��o: c n�o pode ser null e frequency deve ser >= 0.
	//P�s-condi��o: nenhuma.
	public Frequency(T c, int frequency) {
		this.value = c;
		this.frequency = frequency;
	}

	//M�todo get para obter o valor.
	//Pr�-condi��o: nenhuma. 
	//P�s-condi��o: retorna o valor do dado.
	public T getValue() {
		return value;
	}

	//M�todo get para obter a frequ�ncia.
	//Pr�-condi��o: nenhuma.
	//P�s-condi��o: retorna um int com a frequ�ncia do dado.
	public int getFrequency() {
		return frequency;
	}
	
	//Incrementa a frequ�ncia do dado.
	//Pr�-condi��o: nenhuma. 
	//P�s-condi��o: nenhuma.
	public void incFrequency() {
		frequency++;
	}
	
	//Override m�todo equals de Object.
	//Pr�-condi��o: obj deve ser um Frequency. 
	//P�s-condi��o: return true se Frequency � igual a Obj, caso contr�rio retorna false.
	@Override
	public boolean equals(Object obj) {
		Frequency<T> f = (Frequency<T>) obj;
		return this.getValue().equals(f.getValue());
	}
	
	
	//Override do m�todo toString de Object.
	//Pr�-condi��o:nenhuma. 
	//P�s-condi��o: retorna uma string represenntando a Frequ�ncia.
	@Override
	public String toString() {
		return "[c=" + value + ", frequecy=" + frequency + "]";
	}


	//Override do m�todo compareTo de Comparable.
	//Pr�-condi��o:o diferente de null. 
	//P�s-condi��o:return -1 se this < o, return 1 se maior ou return 0 se iguais.
	@Override
	public int compareTo(Frequency<T> o) {
		if(this.frequency < o.frequency)
			return -1;
		else if(this.frequency > o.frequency)
			return 1;
		return 0;
	}








	


	


}
