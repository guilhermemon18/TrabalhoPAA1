package classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.BitSet;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

//Classe responsável por gerenciar a compactação e descompactação dos arquivos pela técnica de caracter e string.
//Classe Estática utiliátária, construtor privado não pode ser instanciada, pois não guarda informações.
public class Compress {
	//Construtor privado, classe static utilitária, impede de instanciar a classe.
	//pré-requisitos: nenhum.
	//pós-requisitos: nenhum.
	private Compress() {
		
	}

	//Compactação por carateres
	//Recebe o nome do arquivo a ser compactado (src) e gera sua compactação (dest)
	//pré-requisitos: src deve ser arquivo texto(.txt), dest deve ser arquivo binário(.bin)
	//pós-requisitos: nenhum
	public static void CharacterZip(String src, String dest) throws IOException, ClassNotFoundException {
		BinaryTree<Frequency<Character>> tree = HUFFMAN(readCharacters(src));
		File arq = new File(dest);//cria um novo arquivo binário com o nome dest.
		arq.delete();
		arq.createNewFile();
		ObjectOutputStream objOutput = new ObjectOutputStream(new FileOutputStream(arq));
		objOutput.writeObject(tree);//escreve a árvore binária no arquivo.
		BitSet aux = getBitsChar(src,tree);
		objOutput.write(aux.toByteArray());//escreve os bits codificados no arquivo
		objOutput.close();
	}

	//Compactação por Palavra
	//Recebe o nome do arquivo a ser compactado (src) e gera sua compactação (dest)
	//pré-requisitos: src deve ser arquivo texto(.txt), dest deve ser arquivo binário(.bin)
	//pós-requisitos: nenhum
	public static void StringZip(String src, String dest) throws IOException, ClassNotFoundException {
		BinaryTree<Frequency<String>> tree = HUFFMAN(readStrings(src));
		File arq = new File(dest);//cria um novo arquivo binário com o nome dest.
		arq.delete();
		arq.createNewFile();
		ObjectOutputStream objOutput = new ObjectOutputStream(new FileOutputStream(arq));
		objOutput.writeObject(tree);//escreve a árvore binária no arquivo.
		BitSet aux = getBitsString(src,tree);
		objOutput.write(aux.toByteArray());//escreve os bits codificados no arquivo
		objOutput.close();

	}

	//Descompacta o arquivo src(.bin) para o arquivo texto dest(.txt)
	//pré-condições: src é nome de arquivo binário(.bin) de compactação por caracter válido e dest é nome de arquivo .txt válido.
	//pós-condição: nenhuma.
	public static void CharacterUnzip(String src, String dest) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(new File(src)));
		BinaryTree<Frequency<Character>> tree = (BinaryTree<Frequency<Character>>) objInput.readObject();
		BitSet b = BitSet.valueOf(objInput.readAllBytes());
		OutputStreamWriter arq = new OutputStreamWriter(new FileOutputStream(dest),"UTF-8");
		writeBits(tree,b,arq);
		/*StringBuilder s = new StringBuilder();
		long tempoInicial = System.currentTimeMillis();
		Hashtable<String,Character> hash = getCodifications(tree2);
		System.out.println("tempo para gerar a hashtable:"+( (float) (System.currentTimeMillis() - tempoInicial)/60000));

		tempoInicial = System.currentTimeMillis();
		for(int i = 0; i < (b.length() - 1); i++) {
			if(b.get(i) == true) {
				s.append('1');
			}else {
				s.append('0');
			}
			if(hash.get(s.toString()) != null) {
				arq.write(hash.get(s.toString()));
				s.setLength(0);
			}

		}*/
		arq.close();
		objInput.close();
	}


	//Descompacta o arquivo src(.bin) para o arquivo texto dest(.txt)
	//pré-condições: src é nome de arquivo binário(.bin) de compactação por caracter válido e dest é nome de arquivo .txt válido.
	//pós-condição: nenhuma.
	public static void StringUnzip(String src, String dest) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(new File(src)));
		BinaryTree<Frequency<String>> tree = (BinaryTree<Frequency<String>>) objInput.readObject();
		BitSet b = BitSet.valueOf(objInput.readAllBytes());
		objInput.close();
		StringBuilder s = new StringBuilder();
		OutputStreamWriter arq = new OutputStreamWriter(new FileOutputStream(dest),"UTF-8");
		writeBits(tree, b, arq);
		/*Hashtable<String,String> hash = getCodifications(tree);
		for(int i = 0; i < (b.length() - 1); i++) {
			if(b.get(i) == true) {
				s.append('1');
			}else {
				s.append('0');
			}
			if(hash.get(s.toString()) != null) {
				arq.write(hash.get(s.toString()));
				s.setLength(0);
			}

		}*/
		arq.close();
	}

	//Gera um BitSet com os bits a serem gravados no arquivo para codificação por caractere.
	//pré-condição: src é arquivo válido de texto e tree é árvore de huffman não nula.
	//pós-condiçao: retorna o bitset com os bits setados.
	private static BitSet getBitsChar(String src,BinaryTree<Frequency<Character>> tree ) throws IOException {
		BufferedReader buffRead = new BufferedReader(new InputStreamReader(new FileInputStream(src), "UTF-8"));
		BitSet aux = new BitSet();
		Hashtable<Character,String> hash = getValues(tree);
		int c = buffRead.read();
		int n = 0;

		while(c != -1) {
			String bits = hash.get((char) c);
			if(bits != null) {
				for(int j = 0; j < bits.length(); j++) {
					if(bits.charAt(j) == '1') {
						aux.set(n++,true);
					}else {
						aux.set(n++,false);
					}
				}
			}
			c = buffRead.read();
		}
		aux.set(n,true);
		buffRead.close();
		return aux;
	}

	//Gera um BitSet com os bits a serem gravados no arquivo para codificação por palavra.
	//pré-condição: src é arquivo válido de texto(.txt) e tree é árvore de huffman não nula.
	//pós-condiçao: retorna o bitset com os bits setados.
	private static BitSet getBitsString(String src,BinaryTree<Frequency<String>> tree ) throws IOException {
		BufferedReader buffRead = new BufferedReader(new InputStreamReader(new FileInputStream(src), "UTF-8"));
		BitSet aux = new BitSet();
		Hashtable<String,String> hash = getValues(tree);
		StringBuilder palavra  = new StringBuilder();
		int c = buffRead.read();
		int n = 0;

		while(c != -1) {
			if(Character.isLetterOrDigit(c)) {
				palavra.append((char) c);
			}else {
				String bits = hash.get(palavra.toString()) + hash.get(Character.valueOf((char) c).toString());
				if(bits != null) {
					for(int j = 0; j < bits.length(); j++) {
						if(bits.charAt(j) == '1') {
							aux.set(n++,true);
						}else {
							aux.set(n++,false);
						}
					}
				}
				palavra.setLength(0);
			}

			c = buffRead.read();
		}
		aux.set(n,true);
		buffRead.close();
		return aux;
	}


	//Huffman: Gera a árvore de Huffman para a fila de raízes com as palavras ou caracteres e suas frequências.
	//Pré-condições: queue não pode ser null.
	//Pós-condições: Retorna uma Binarytree de Huffman.
	private static <T extends Comparable<T>> BinaryTree<Frequency<T>> HUFFMAN(Queue<BinaryTree<Frequency<T>>> queue){
		int n = queue.size();
		for(int i = 1; i <= (n-1) ; i++) {
			BinaryTree<Frequency<T>> x = queue.poll();
			BinaryTree<Frequency<T>> y = queue.poll();
			BinaryTree<Frequency<T>> z = new BinaryTree<Frequency<T>>(new Frequency<T>(null, x.getInfo().getFrequency() + y.getInfo().getFrequency()), x, y);
			queue.add(z);
			Collections.sort((List<BinaryTree<Frequency<T>>>) queue);
		}
		return queue.poll();
	}


	//Método privado para ler os caracteres no arquivo.
	//Pré-condição: String não nula com o nome do arquivo.
	//Pós-condição: Retorna uma fila organizada crescente com as raízes de árvore binária cada qual com sua contagem.
	private static Queue<BinaryTree<Frequency<Character>>> readCharacters (String fileName) throws IOException{
		BufferedReader buffRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
		Queue<BinaryTree<Frequency<Character>>> charactersAux = new LinkedList<BinaryTree<Frequency<Character>>>();
		Hashtable<Character,Integer> hash = new Hashtable<Character,Integer>();

		int c =  buffRead.read();
		while(c!= -1 ) {
			if(hash.get((char) c) == null) {
				hash.put((char) c, 1);
			}else {
				hash.replace((char) c, hash.get((char) c) + 1);
			}
			c =  buffRead.read();
		}
		hash.forEach((key, value)->{charactersAux.add(new BinaryTree<Frequency<Character>>(new Frequency<Character>(key,value)));});
		Collections.sort((List<BinaryTree<Frequency<Character>>>) charactersAux);//organiza a fila de acordo com as frequências.
		buffRead.close();
		return charactersAux;
	}

	//Método privado para ler as strings  no arquivo.
	//Pré-condição: String não nula com o nome do arquivo.
	//Pós-condição: Retorna uma fila organizada crescente com as raízes de árvore binária cada qual com sua contagem.
	private static Queue<BinaryTree<Frequency<String>>> readStrings (String fileName) throws IOException{
		BufferedReader buffRead = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
		Queue<BinaryTree<Frequency<String>>> stringsAux = new LinkedList<BinaryTree<Frequency<String>>>();
		int c = buffRead.read();
		StringBuilder palavra = new StringBuilder();
		BinaryTree<Frequency<String>> b;
		Hashtable<String,Integer> hash = new Hashtable<String,Integer>();
		while(c!= -1 ) {
			if(Character.isLetterOrDigit(c)) {
				palavra.append((char)c);
			}else {
				if(hash.get(palavra.toString()) == null) {
					hash.put(palavra.toString(), 1);
				}else {
					hash.replace(palavra.toString(), hash.get(palavra.toString()) + 1);
				}
				palavra.setLength(0);
				palavra.append( (char) c);

				if(hash.get(palavra.toString()) == null) {
					hash.put(palavra.toString(), 1);
				}else {
					hash.replace(palavra.toString(), hash.get(palavra.toString()) + 1);
				}
				palavra.setLength(0);
			}
			c =  buffRead.read();

		}

		hash.forEach((key, value)->{stringsAux.add(new BinaryTree<Frequency<String>>(new Frequency<String>(key,value)));});

		Collections.sort((List<BinaryTree<Frequency<String>>>) stringsAux);
		buffRead.close();
		return stringsAux;
	}




	//Métodos relacionados à Árvore de Huffman para obtenção de informações relevantes para a compactação, tanto por Character como por String.

	//SetCodifications: Seta a hashTable com as codificações como chave e seus respectivos valores de char ou string como valores associados.
	//Pré-condição: Hash não pode ser null, s inicia como vazia "", root não pode ser null, este método é auxiliar, não é recomendado usá-lo.
	//T deve ser String se se desejar obter codificações por palavra, Character se se desejar obter codificações por char.
	//Pós-condição: nenhuma.
	private static <T extends Comparable<T>> void setCodifications(BinaryTree<Frequency<T>> root, String s,Hashtable<String,T> hash) {
		if (root.getEsq() == null && root.getDir() == null && root.getInfo() != null) {//encntrou um caracter válido
			hash.put(s, root.getInfo().getValue());
		}else {
			setCodifications(root.getEsq(), s + "0",hash);
			setCodifications(root.getDir(), s + "1",hash);
		}
	}

	//getCodifications: Retorna as codificações e seus respectivos valores numa hashTable.
	//Pré-condições: root deve ser diferente de null.
	//Pós-Condição: Retorna uma hashTable contendo as chaves Strings representando os binários associados aos valores das palavras ou caracteres.
	private static <T extends Comparable<T>> Hashtable<String,T> getCodifications(BinaryTree<Frequency<T>> root){
		Hashtable<String,T> hash = new Hashtable<String,T>();
		setCodifications(root, "", hash);
		return hash;
	}


	//setValues: Seta a hashTable com as codificações como chave e seus respectivos valores de char ou string como valores associados.
	//Pré-condição: Hash não pode ser null, s inicia como vazia "", root não pode ser null, este método é auxiliar, não é recomendado usá-lo.
	//T deve ser String se se desejar obter codificações por palavra, Character se se desejar obter codificações por char.
	//Pós-condição: nenhuma.
	private static <T extends Comparable<T>> void setValues(BinaryTree<Frequency<T>> root, String s,Hashtable<T,String> hash) {
		if (root.getEsq() == null && root.getDir() == null && root.getInfo() != null) {//encntrou um caracter válido
			hash.put(root.getInfo().getValue(),s);
		}else {
			setValues(root.getEsq(), s + "0",hash);
			setValues(root.getDir(), s + "1",hash);
		}
	}

	//getValues: Retorna os valores como chave e suas respectivas codificações como valor numa hashTable.
	//Pré-condições: root deve ser diferente de null, 
	//Pós-Condição: Retorna uma hashTable contendo os valores como chave representando os (caracteres ou strings) e suas codificações associadas, como valor.
	private static <T extends Comparable<T>> Hashtable<T,String> getValues(BinaryTree<Frequency<T>> root){
		Hashtable<T,String> hash = new Hashtable<T,String>();
		setValues(root, "", hash);
		return hash;
	}
	
	//Escreve um símbolo percorrendo o bitSet b a partir de i  pela árvore binária.
	//Pré-condições: root não pode ser null, b não pode ser null, arq não pode ser null, i deve ser maior que 0;
	//Pós-condições: returna o i atualizado até a posição que foi percorrida no bitSet.
	private static <T1 extends Comparable<T1>>int writeBitsAux(BinaryTree<Frequency<T1>> root,BitSet b, OutputStreamWriter arq, int i) throws IOException {
		if (root.getDir() == null && root.getEsq() == null && root.getInfo() != null) {//encntrou um caracter válido
			arq.write(root.getInfo().getValue().toString());
			return i;
		}else {
			
			if(i < (b.length() - 1)) {
				if(b.get(i) == true) {
					i++;
					return writeBitsAux(root.getDir(), b,arq,i);
				}
				else {
					i++;
					return writeBitsAux(root.getEsq(), b,arq,i);
				}
			}else {
				return i;	
			}
		}
	}
	//Escreve as palavras ou caracteres correspondentes à decodificação da árvore de Huffman do bitSet
	//Pré-Condições: root != null, b != null, arq != null.
	//Pós-condições: nenhuma.
	private static <T1 extends Comparable<T1>>void writeBits(BinaryTree<Frequency<T1>> root,BitSet b, OutputStreamWriter arq ) throws IOException {
		int i = 0;
		while(i < (b.length() - 1)) {
			i = writeBitsAux(root,b,arq,i);
		}
	}


}
