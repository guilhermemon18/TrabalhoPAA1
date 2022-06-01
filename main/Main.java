package main;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import classes.Compress;

//Classe Main com o método main onde se inicia o programa.

public class Main {

	//Método Estático main onde se inicia o programa
	//Pré-condição: nenhuma. 
	//Pós-condição:nenhuma.
	public static void main(String[] args) {
		String fileName;
		int option = 0;
		Scanner c = new Scanner(System.in);
		do {
			System.out.println("Escolha uma opção:");
			System.out.println("0.Compactar Arquivo (usando codificação por caractere)");
			System.out.println("1.Descompactar Arquivo (usando codificação por caractere)");
			System.out.println("2.Compactar Arquivo (usando codificação por palavra)");
			System.out.println("3.Descompactar Arquivo (usando codificação por palavra)");
			System.out.println("4.Sair");
			option = c.nextInt();
			c.nextLine();
			switch(option) {
			case 0:
				System.out.println("Digite o nome do arquivo para compactar(.txt)");
				fileName = c.nextLine();
				try {
					long tempoInicial = System.currentTimeMillis();
					Compress.CharacterZip(fileName, fileName.substring(0, fileName.indexOf('.')) + ".bin");
					System.out.println("tempo:"+( (float) (System.currentTimeMillis() - tempoInicial)/1000)+ " segundos");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Error to open file!");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 1:
				System.out.println("Digite o nome do arquivo para descompactar(.bin)");
				fileName = c.nextLine();
				try {
					long tempoInicial = System.currentTimeMillis();
					Compress.CharacterUnzip(fileName,fileName.substring(0, fileName.indexOf('.')) + "descompactado.txt");
					System.out.println("tempo:"+( (float) (System.currentTimeMillis() - tempoInicial)/1000)+ " segundos");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:
				System.out.println("Digite o nome do arquivo para compactar(.txt)");
				fileName = c.nextLine();
				try {
					long tempoInicial = System.currentTimeMillis();
					try {
						Compress.StringZip(fileName, fileName.substring(0, fileName.indexOf('.')) + ".bin");
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("tempo:"+( (float) (System.currentTimeMillis() - tempoInicial)/1000)+ " segundos");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 3:
				System.out.println("Digite o nome do arquivo para descompactar(.bin)");
				fileName = c.nextLine();
				try {
					long tempoInicial = System.currentTimeMillis();
					Compress.StringUnzip(fileName, fileName.substring(0, fileName.indexOf('.')) + "descompactado" + ".txt");
					System.out.println("tempo:"+( (float) (System.currentTimeMillis() - tempoInicial)/1000)+ " segundos");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("Escolha uma opção válida!");
			}

		}
		while(option != 4);
		System.out.println("Programa terminado");
		c.close();

	}

}
