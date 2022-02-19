package algoritmo;

import controle.Constantes;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class Poupador extends ProgramaPoupador {

	private int visited = 6;
	private List<Point> memoryCoin = new ArrayList<>();
	private List<Point> memoryPower = new ArrayList<>();
	private int[][] map = new int[30][30];
	private int[][] movements = {
			{0, 0}, {0, -1}, {0, 1}, {1, 0}, {-1, 0}
	};
	private int[][] viewMove = {
			{-2, -2}, {-2, -1}, {-2, 0}, {-2, 1}, {-2, 2}, {-1, -2}, {-1, -1}, {-1, 0}, {-1, 1}, {-1, 2}, {0, -2}, {0, -1}, {0, 1}, {0, 2}, {1, -2}, {1, -1}, {1, 0}, {1, 1}, {1, 2}, {2, -2}, {2, -1}, {2, 0}, {2, 1}, {2, 2},
	};
	private final Collection<Integer> cima = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	private final Collection<Integer> direita = Arrays.asList(12, 13, 3, 4, 8, 9, 17, 18, 22, 23);
	private final Collection<Integer> baixo = Arrays.asList(14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
	private final Collection<Integer> esquerda = Arrays.asList(10, 11, 0, 1, 5, 6, 14, 15, 19, 20);
	
	public int acao() {
		memory();
		move();
		return (int) (Math.random() * 5);
	}

	private void memory() {
		Point pointNow = sensor.getPosicao();
		int[] see = sensor.getVisaoIdentificacao();
		map[pointNow.x][pointNow.y] = visited;
		for(int i = 0; i < see.length; i++) {
			Point pointNew = new Point(pointNow.x + viewMove[i][0],pointNow.y + viewMove[i][1]);
			if(see[i] == Constantes.numeroBanco && sensor.getNumeroDeMoedas() != 0){

			}
			if(see[i] == Constantes.numeroMoeda && !memoryCoin.contains(pointNew)){
				memoryCoin.add(pointNew);
			}
			if(map[pointNew.x][pointNew.y] != visited && see[i] != Constantes.semVisao){
				map[pointNew.x][pointNew.y] = see[i];
			}
		}
	}

	private int move() {
		Point pointNow = sensor.getPosicao();
		int[] see = sensor.getVisaoIdentificacao();
		List<Integer> ladroes = new ArrayList<>(Arrays.asList(Constantes.numeroLadrao01,Constantes.numeroLadrao02,Constantes.numeroLadrao03,Constantes.numeroLadrao04));
		for(int i = 0; i < see.length; i++){
			if(ladroes.contains(see[i])){
				if(cima.contains(i) && direita.contains(i)){
					int x = (Math.random() <= 0.5) ? 2 : 4;
					return x;
				}
				if(cima.contains(i) && esquerda.contains(i)){
					int x = (Math.random() <= 0.5) ? 2 : 3;
					return x;
				}
				if(baixo.contains(i) && direita.contains(i)){
					int x = (Math.random() <= 0.5) ? 1 : 4;
					return x;
				}
				if(baixo.contains(i) && esquerda.contains(i)){
					int x = (Math.random() <= 0.5) ? 1 : 3;
					return x;
				}
			}
		}
		return 0;
	}


}