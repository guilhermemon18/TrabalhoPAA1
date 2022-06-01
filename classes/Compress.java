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

//Classe respons�vel por gerenciar a compacta��o e descompacta��o dos arquivos pela t�cnica de caracter e string.
//Classe Est�tica utili�t�ria, construtor privado n�o pode ser instanciada, pois n�o guarda informa��es.
public class Compress {
	//Construtor privado, classe static utilit�ria, impede de instanciar a classe.
	//pr�-requisitos: nenhum.
	//p�s-requisitos: nenhum.
	private Compress() {
		
	}

	//Compacta��o por carateres
	//Recebe o nome do arquivo a ser compactado (src) e gera sua compacta��o (dest)
	//pr�-requisitos: src deve ser arquivo texto(.txt), dest deve ser arquivo bin�rio(.bin)
	//p�s-requisitos: nenhum
	public static void CharacterZip(String src, String dest) throws IOException, ClassNotFoundException {
		BinaryTree<Frequency<Character>> tree = HUFFMAN(readCharacters(src));
		File arq = new File(dest);//cria um novo arquivo bin�rio com o nome dest.
		arq.delete();
		arq.createNewFile();
		ObjectOutputStream objOutput = new ObjectOutputStream(new FileOutputStream(arq));
		objOutput.writeObject(tree);//escreve a �rvore bin�ria no arquivo.
		BitSet aux = getBitsChar(src,tree);
		objOutput.write(aux.toByteArray());//escreve os bits codificados no arquivo
		objOutput.close();
	}

	//Compacta��o por Palavra
	//Recebe o nome do arquivo a ser compactado (src) e gera sua compacta��o (dest)
	//pr�-requisitos: src deve ser arquivo texto(.txt), dest deve ser arquivo bin�rio(.bin)
	//p�s-requisitos: nenhum
	public static void StringZip(String src, String dest) throws IOException, ClassNotFoundException {
		BinaryTree<Frequency<String>> tree = HUFFMAN(readStrings(src));
		File arq = new File(dest);//cria um novo arquivo bin�rio com o nome dest.
		arq.delete();
		arq.createNewFile();
		ObjectOutputStream objOutput = new ObjectOutputStream(new FileOutputStream(arq));
		objOutput.writeObject(tree);//escreve a �rvore bin�ria no arquivo.
		BitSet aux = getBitsString(src,tree);
		objOutput.write(aux.toByteArray());//escreve os bits codificados no arquivo
		objOutput.close();

	}

	//Descompacta o arquivo src(.bin) para o arquivo texto dest(.txt)
	//pr�-condi��es: src � nome de arquivo bin�rio(.bin) de compacta��o por caracter v�lido e dest � nome de arquivo .txt v�lido.
	//p�s-condi��o: nenhuma.
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
	//pr�-condi��es: src � nome de arquivo bin�rio(.bin) de compacta��o por caracter v�lido e dest � nome de arquivo .txt v�lido.
	//p�s-condi��o: nenhuma.
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

	//Gera um BitSet com os bits a serem gravados no arquivo para codifica��o por caractere.
	//pr�-condi��o: src � arquivo v�lido de texto e tree � �rvore de huffman n�o nula.
	//p�s-condi�ao: retorna o bitset com os bits setados.
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

	//Gera um BitSet com os bits a serem gravados no arquivo para codifica��o por palavra.
	//pr�-condi��o: src � arquivo v�lido de texto(.txt) e tree � �rvore de huffman n�o nula.
	//p�s-condi�ao: retorna o bitset com os bits setados.
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


	//Huffman: Gera a �rvore de Huffman para a fila de ra�zes com as palavras ou caracteres e suas frequ�ncias.
	//Pr�-condi��es: queue n�o pode ser null.
	//P�s-condi��es: Retorna uma Binarytree de Huffman.
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


	//M�todo privado para ler os caracteres no arquivo.
	//Pr�-condi��o: String n�o nula com o nome do arquivo.
	//P�s-condi��o: Retorna uma fila organizada crescente com as ra�zes de �rvore bin�ria cada qual com sua contagem.
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
		Collections.sort((List<BinaryTree<Frequency<Character>>>) charactersAux);//organiza a fila de acordo com as frequ�ncias.
		buffRead.close();
		return charactersAux;
	}

	//M�todo privado para ler as strings  no arquivo.
	//Pr�-condi��o: String n�o nula com o nome do arquivo.
	//P�s-condi��o: Retorna uma fila organizada crescente com as ra�zes de �rvore bin�ria cada qual com sua contagem.
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




	//M�todos relacionados � �rvore de Huffman para obten��o de informa��es relevantes para a compacta��o, tanto por Character como por String.

	//SetCodifications: Seta a hashTable com as codifica��es como chave e seus respectivos valores de char ou string como valores associados.
	//Pr�-condi��o: Hash n�o pode ser null, s inicia como vazia "", root n�o pode ser null, este m�todo � auxiliar, n�o � recomendado us�-lo.
	//T deve ser String se se desejar obter codifica��es por palavra, Character se se desejar obter codifica��es por char.
	//P�s-condi��o: nenhuma.
	private static <T extends Comparable<T>> void setCodifications(BinaryTree<Frequency<T>> root, String s,Hashtable<String,T> hash) {
		if (root.getEsq() == null && root.getDir() == null && root.getInfo() != null) {//encntrou um caracter v�lido
			hash.put(s, root.getInfo().getValue());
		}else {
			setCodifications(root.getEsq(), s + "0",hash);
			setCodifications(root.getDir(), s + "1",hash);
		}
	}

	//getCodifications: Retorna as codifica��es e seus respectivos valores numa hashTable.
	//Pr�-condi��es: root deve ser diferente de null.
	//P�s-Condi��o: Retorna uma hashTable contendo as chaves Strings representando os bin�rios associados aos valores das palavras ou caracteres.
	private static <T extends Comparable<T>> Hashtable<String,T> getCodifications(BinaryTree<Frequency<T>> root){
		Hashtable<String,T> hash = new Hashtable<String,T>();
		setCodifications(root, "", hash);
		return hash;
	}


	//setValues: Seta a hashTable com as codifica��es como chave e seus respectivos valores de char ou string como valores associados.
	//Pr�-condi��o: Hash n�o pode ser null, s inicia como vazia "", root n�o pode ser null, este m�todo � auxiliar, n�o � recomendado us�-lo.
	//T deve ser String se se desejar obter codifica��es por palavra, Character se se desejar obter codifica��es por char.
	//P�s-condi��o: nenhuma.
	private static <T extends Comparable<T>> void setValues(BinaryTree<Frequency<T>> root, String s,Hashtable<T,String> hash) {
		if (root.getEsq() == null && root.getDir() == null && root.getInfo() != null) {//encntrou um caracter v�lido
			hash.put(root.getInfo().getValue(),s);
		}else {
			setValues(root.getEsq(), s + "0",hash);
			setValues(root.getDir(), s + "1",hash);
		}
	}

	//getValues: Retorna os valores como chave e suas respectivas codifica��es como valor numa hashTable.
	//Pr�-condi��es: root deve ser diferente de null, 
	//P�s-Condi��o: Retorna uma hashTable contendo os valores como chave representando os (caracteres ou strings) e suas codifica��es associadas, como valor.
	private static <T extends Comparable<T>> Hashtable<T,String> getValues(BinaryTree<Frequency<T>> root){
		Hashtable<T,String> hash = new Hashtable<T,String>();
		setValues(root, "", hash);
		return hash;
	}
	
	//Escreve um s�mbolo percorrendo o bitSet b a partir de i  pela �rvore bin�ria.
	//Pr�-condi��es: root n�o pode ser null, b n�o pode ser null, arq n�o pode ser null, i deve ser maior que 0;
	//P�s-condi��es: returna o i atualizado at� a posi��o que foi percorrida no bitSet.
	private static <T1 extends Comparable<T1>>int writeBitsAux(BinaryTree<Frequency<T1>> root,BitSet b, OutputStreamWriter arq, int i) throws IOException {
		if (root.getDir() == null && root.getEsq() == null && root.getInfo() != null) {//encntrou um caracter v�lido
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
	//Escreve as palavras ou caracteres correspondentes � decodifica��o da �rvore de Huffman do bitSet
	//Pr�-Condi��es: root != null, b != null, arq != null.
	//P�s-condi��es: nenhuma.
	private static <T1 extends Comparable<T1>>void writeBits(BinaryTree<Frequency<T1>> root,BitSet b, OutputStreamWriter arq ) throws IOException {
		int i = 0;
		while(i < (b.length() - 1)) {
			i = writeBitsAux(root,b,arq,i);
		}
	}


}
