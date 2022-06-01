package classes;

import java.io.Serializable;

//Classe que representa uma Árvore Binária que pode receber qualquer tipo de dados(Generics) e pode ser escrita em disco (Serializable).
public class BinaryTree<T extends Comparable<T>> implements Comparable<BinaryTree<T>>, Serializable {

	private static Integer i = 0;
	private static final long serialVersionUID = 1L;
	private T info;
	private BinaryTree<T> esq;
	private BinaryTree<T> dir;


	//Construtor padrão da classe
	//Pré-requisitos: nenhum.
	//Pós-requisitos: nenhum.
	public BinaryTree() {
		this.info = null;
		this.dir = null;
		this.esq = null;
	}

	//Construtor Com info
	//Pré-Requisitos: info é o valor inicial da árvore binária, se info == null, então o efeito é o mesmo do construtor padrão.
	//Pós-requisitos: nenhum.
	public BinaryTree(T info) {
		this.info = info;
		this.dir = null;
		this.esq = null;
	}
	//Construtor com nós.
	//Pré-Requisitos: info != null, esq ou dir podem ser null ou não, mas deve-se garantir que esq.info seja menor que dir.info.
	//Pós-requisitos: nenhum.
	public BinaryTree(T info,BinaryTree<T> esq, BinaryTree<T> dir ) {
		this.info = info;
		this.esq = esq;
		this.dir = dir;
	}

	//Método get de info.
	//Pré-Requisitos: nenhum.
	//Pós-requisitos: retorna info, pode retornar null se info == null;
	public T getInfo() {
		return info;
	}

	//Método get do nó equerdo.
	//Pré-Requisitos: nenhum.
	//Pós-requisitos: Retorna uma BinaryTree do lado esquerdo, pode retornar null se esq == null;
	public BinaryTree<T> getEsq() {
		return esq;
	}
	
	//Método get do nó direito.
	//Pré-Requisitos: nenhum.
	//Pós-requisitos: Retorna uma BinaryTree do lado direito, pode retornar null se dir == null;
	public BinaryTree<T> getDir() {
		return dir;
	}

	//Adiciona um elemento à BinaryTree.
	//Pré-Requisitos: nenhum.
	//Pós-requisitos: nenhum.
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
	
	//Verifica se o nó é nó folha.
	//Pré-condição: nenhuma.
	//Pós-condição: true se for nó folha, false se não for.
	public boolean isNoFolha() {
		return (this.esq == null && this.dir == null);
	}

	//Exibe a árvore binária in-ordem.
	//Pré-Requisitos: nenhum.
	//Pós-requisitos: nenhum.
	public void showInOrdem() {
		if(this.esq != null) this.esq.showInOrdem();
		System.out.println(this.info);
		if(this.dir!= null) this.dir.showInOrdem(); 

	}

	//Método main de teste para verificar e exemplificar o uso da árvore binária.
	//Pré-Requisitos: nenhum.
	//Pós-requisitos: nenhum.
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

/*REVER DEPOIS, N FOI NECESSÁRIO.
	//Método Equals sobreescrito para compararar duas árvores.
	//Pré-Requisitos: obj deve ser uma BinaryTree não nula.
	//Pós-requisitos: retorna se as duas são iguais
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
