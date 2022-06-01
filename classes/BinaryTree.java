package classes;

import java.io.Serializable;

//Classe que representa uma �rvore Bin�ria que pode receber qualquer tipo de dados(Generics) e pode ser escrita em disco (Serializable).
public class BinaryTree<T extends Comparable<T>> implements Comparable<BinaryTree<T>>, Serializable {

	private static Integer i = 0;
	private static final long serialVersionUID = 1L;
	private T info;
	private BinaryTree<T> esq;
	private BinaryTree<T> dir;


	//Construtor padr�o da classe
	//Pr�-requisitos: nenhum.
	//P�s-requisitos: nenhum.
	public BinaryTree() {
		this.info = null;
		this.dir = null;
		this.esq = null;
	}

	//Construtor Com info
	//Pr�-Requisitos: info � o valor inicial da �rvore bin�ria, se info == null, ent�o o efeito � o mesmo do construtor padr�o.
	//P�s-requisitos: nenhum.
	public BinaryTree(T info) {
		this.info = info;
		this.dir = null;
		this.esq = null;
	}
	//Construtor com n�s.
	//Pr�-Requisitos: info != null, esq ou dir podem ser null ou n�o, mas deve-se garantir que esq.info seja menor que dir.info.
	//P�s-requisitos: nenhum.
	public BinaryTree(T info,BinaryTree<T> esq, BinaryTree<T> dir ) {
		this.info = info;
		this.esq = esq;
		this.dir = dir;
	}

	//M�todo get de info.
	//Pr�-Requisitos: nenhum.
	//P�s-requisitos: retorna info, pode retornar null se info == null;
	public T getInfo() {
		return info;
	}

	//M�todo get do n� equerdo.
	//Pr�-Requisitos: nenhum.
	//P�s-requisitos: Retorna uma BinaryTree do lado esquerdo, pode retornar null se esq == null;
	public BinaryTree<T> getEsq() {
		return esq;
	}
	
	//M�todo get do n� direito.
	//Pr�-Requisitos: nenhum.
	//P�s-requisitos: Retorna uma BinaryTree do lado direito, pode retornar null se dir == null;
	public BinaryTree<T> getDir() {
		return dir;
	}

	//Adiciona um elemento � BinaryTree.
	//Pr�-Requisitos: nenhum.
	//P�s-requisitos: nenhum.
	public void add(T info) {
		if(this.info == null) {
			this.info = info;
		} else {
			if(this.info.compareTo(info) > 0) {
				if(this.esq == null) {
					this.esq = new BinaryTree<T>(info);
				}else {
					this.esq.add(info);
				}
			}else {
				if(this.dir == null) {
					this.dir = new BinaryTree<T>(info);
				}else {
					this.dir.add(info);
				}
			}
		}
	}
	
	//Verifica se o n� � n� folha.
	//Pr�-condi��o: nenhuma.
	//P�s-condi��o: true se for n� folha, false se n�o for.
	public boolean isNoFolha() {
		return (this.esq == null && this.dir == null);
	}

	//Exibe a �rvore bin�ria in-ordem.
	//Pr�-Requisitos: nenhum.
	//P�s-requisitos: nenhum.
	public void showInOrdem() {
		if(this.esq != null) this.esq.showInOrdem();
		System.out.println(this.info);
		if(this.dir!= null) this.dir.showInOrdem(); 

	}

	//M�todo main de teste para verificar e exemplificar o uso da �rvore bin�ria.
	//Pr�-Requisitos: nenhum.
	//P�s-requisitos: nenhum.
	public static void main(String[] args) {
		BinaryTree<Integer> r = new BinaryTree<Integer>();
		r.add(10);
		r.add(5);
		r.add(6);
		r.add(12);
		r.add(13);
		r.add(2);
		r.add(1);
		r.showInOrdem();

	}

/*REVER DEPOIS, N FOI NECESS�RIO.
	//M�todo Equals sobreescrito para compararar duas �rvores.
	//Pr�-Requisitos: obj deve ser uma BinaryTree n�o nula.
	//P�s-requisitos: retorna se as duas s�o iguais
	@Override
	public boolean equals(Object obj) {
		BinaryTree<T> b = (BinaryTree<T>) obj;
		return this.info.equals(b.info);
	}*/

	@Override
	public String toString() {
		return  info.toString();
	}

	@Override
	public int compareTo(BinaryTree<T> o) {
		// TODO Auto-generated method stub
		return this.info.compareTo(o.info);
	}
	

}
