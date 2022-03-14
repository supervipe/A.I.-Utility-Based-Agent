package algoritmo;

import algoritmo.path.Graph;
import controle.Constantes;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Poupador extends ProgramaPoupador {

	private int visited = 6;
	private List<Point> memoryCoin = new ArrayList<>();
	private List<Point> memoryPower = new ArrayList<>();
	private int[][] map = new int[30][30];
	private int[][] movements = { /// still, up, down, right, left
			{0, 0}, {0, -1}, {0, 1}, {1, 0}, {-1, 0}
	};
	private int[] graphMovements = {-5, 5, 1, -1};
	private int[][] viewMove = { {-2, -2}, {-1, -2}, {0, -2}, {1, -2}, {2, -2}, {-2, -1}, {-1, -1}, {0, -1}, {1, -1}, {2, -1}, {-2, 0}, {-1, 0}, {1, 0}, {2, 0}, {-2, 1}, {-1, 1}, {0, 1}, {1, 1}, {2, 1}, {-2, 2}, {-1, 2}, {0, 2}, {1, 2}, {2, 2},	};
	private final int[] up = {0, 1, 3, 4, 5, 6, 7, 8, 9};
	private final int[] right = {3, 4, 8, 9, 12, 13, 17, 18, 22, 23};
	private final int[] down = {14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
	private final int[] left = {0, 1, 5, 6, 10, 11, 14, 15, 19, 20};
	private final int[] around = {7, 16, 12, 11};
	private int fear = 0;
	Graph graph = new Graph(900);
	private Boolean canGoToBank = false;
	private Boolean goingToBank = false;

	public int acao() {
		memory();
		return coin();
	}

	private void memory() {
		Point pointNow = sensor.getPosicao();
		int[] see = sensor.getVisaoIdentificacao();
		map[pointNow.x][pointNow.y] = visited;
		if(sensor.getNumeroDeMoedas() == 0){
			fear = 0;
		}
		for(int i = 0; i < see.length; i++) {
			if (see[i] != Constantes.foraAmbiene) {
				Point pointNew = new Point(pointNow.x + viewMove[i][0], pointNow.y + viewMove[i][1]);
				if (see[i] == Constantes.numeroBanco && sensor.getNumeroDeMoedas() != 0) {}
				if (see[i] == Constantes.numeroMoeda && !memoryCoin.contains(pointNew)) {
					memoryCoin.add(pointNew);
				}
				if(memoryCoin.contains(pointNow)){
					memoryCoin.remove(pointNow);
					if(fear == 0){
						fear = 2;
					} else {
						fear *= 2;
					}
				}
				if (map[pointNew.x][pointNew.y] != visited && see[i] != Constantes.semVisao) {
					map[pointNew.x][pointNew.y] = see[i];
				}

				if (see[i] != Constantes.semVisao && see[i] != Constantes.numeroParede) {
					for (int j = 1; j < 5; j++) {
						Point pointNewAdj = new Point(pointNew.x + movements[j][0], pointNew.y + movements[j][1]);
						int xAdj = pointNow.x - pointNewAdj.x; // -2 < x < 2
						int yAdj = pointNow.y - pointNewAdj.y; // -2 < y < 2
						if (xAdj <= 2 && xAdj >= -2 && yAdj <= 2 && yAdj >= -2) {
							if (see[i + graphMovements[j-1]] != Constantes.numeroParede && see[i + graphMovements[j-1]] != Constantes.foraAmbiene) {
								graph.addEdge2(pointNew.x, pointNew.y, pointNewAdj.x, pointNewAdj.y);
							}
						}
					}
				}
			}
		}
		if (graph.canRouteToBank(pointNow.x, pointNow.y)) canGoToBank = true;
	}

	private int run() {
		int[] see = sensor.getVisaoIdentificacao();
		List<Integer> ladroes = new ArrayList<>(Arrays.asList(Constantes.numeroLadrao01,Constantes.numeroLadrao02,Constantes.numeroLadrao03,Constantes.numeroLadrao04));
		List<Integer> poupadores = new ArrayList<>(Arrays.asList(Constantes.numeroPoupador01, Constantes.numeroPoupador02));
		for(int i = 0; i < up.length; i++){
			if(ladroes.contains(see[up[i]])){
				fear *= 2;
				if(see[16] == Constantes.foraAmbiene || see[16] == Constantes.numeroParede || ladroes.contains(see[16]) || ladroes.contains(see[21]) || ladroes.contains(see[17]) || poupadores.contains(see[16])){
					if(see[11] == Constantes.foraAmbiene || see[11] == Constantes.numeroParede || ladroes.contains(see[11]) || ladroes.contains(see[15]) || ladroes.contains(see[6]) || ladroes.contains(see[10]) ||poupadores.contains(see[11])){
						return 3;
					}
					return 4;
				}
				return 2;
			}
			if(ladroes.contains(see[left[i]])){
				fear *= 2;
				if(see[12] == Constantes.foraAmbiene || see[12] == Constantes.numeroParede || ladroes.contains(see[12]) || ladroes.contains(see[13]) || ladroes.contains(see[8]) || ladroes.contains(see[17]) || poupadores.contains(see[12])){
					if(see[7] == Constantes.foraAmbiene || see[7] == Constantes.numeroParede || ladroes.contains(see[7]) || ladroes.contains(see[2]) || ladroes.contains(see[6]) || poupadores.contains(see[7])){
						return 2;
					}
					return 1;
				}
				return 3;
			}
			if(ladroes.contains(see[right[i]])){
				fear *= 2;
				if(see[11] == Constantes.foraAmbiene || see[11] == Constantes.numeroParede || ladroes.contains(see[11]) || ladroes.contains(see[10]) || ladroes.contains(see[6]) || ladroes.contains(see[15]) || poupadores.contains(see[11])){
					if(see[7] == Constantes.foraAmbiene || see[7] == Constantes.numeroParede || ladroes.contains(see[7]) || ladroes.contains(see[2]) || ladroes.contains(see[8]) || poupadores.contains(see[7])){
						return 2;
					}
					return 1;
				}
				return 4;
			}
			if(ladroes.contains(see[down[i]])){
				fear *= 2;
				if(see[7] == Constantes.foraAmbiene || see[7] == Constantes.numeroParede || ladroes.contains(see[7]) || ladroes.contains(see[2]) || ladroes.contains(see[6]) || ladroes.contains(see[8]) || poupadores.contains(see[7])){
					if(see[11] == Constantes.foraAmbiene || see[11] == Constantes.numeroParede || ladroes.contains(see[11]) || ladroes.contains(see[10]) || ladroes.contains(see[15]) || poupadores.contains(see[11])){
						return 3;
					}
					return 4;
				}
				return 1;
			}
		}
		return 0;
	}

	private int coin() {
		int[] see = sensor.getVisaoIdentificacao();
		Point pointNow = sensor.getPosicao();
		int y = run();
		if(y != 0){
			goingToBank = false;
			return y;
		}
		if(goingToBank){
			moveToBank();
		}
		int x = (int) (Math.random() * 1000);
		System.out.println("Fear " + fear);
		if(x <= 100 + fear && canGoToBank) {
			System.out.println("Banco");
			goingToBank = true;
			return moveToBank();
		} else {
			if(see[7] == Constantes.numeroMoeda){
				return 1;
			} else if(see[11] == Constantes.numeroMoeda){
				return 4;
			} else if(see[12] == Constantes.numeroMoeda){
				return 3;
			} else if(see[16] == Constantes.numeroMoeda) {
				return 2;
			} else {
				int z = (int) (Math.random() * 100);
				int[] visitedArray = { 25, 50, 75, 100};
				int div = 1;
				for(int i = 0; i < 4; i++){
					if(z <= visitedArray[i]){
//						System.out.println(z);
						return (i + 1);
					}
				}
				return 0;
			}
		}
	}

	LinkedList<Point> pointsToGo = new LinkedList<>();
	private void routeToBank() {
		Point pointNow = sensor.getPosicao();

		if (canGoToBank) {
			LinkedList<Integer> shortestPathToBank = graph.shortestDistance(pointNow.x, pointNow.y, 8 ,8);

			for (int i = 2; i <= shortestPathToBank.size()-2; i=i+2) {
				Point nextPoint = new Point(shortestPathToBank.get(i), shortestPathToBank.get(i+1));
				pointsToGo.add(nextPoint);
			}
		}
	}

	private int moveToBank() {
		routeToBank();
		Point pointNow = sensor.getPosicao();
		int x, y;

		x = pointsToGo.get(0).x - pointNow.x;
		y = pointsToGo.get(0).y - pointNow.y;

		if (x == 1 && y == 0) {
			pointsToGo.removeFirst();
			return 3; // DIREITA
		} else if (x == 0 && y == 1) {
			pointsToGo.removeFirst();
			return 2; // BAIXO
		} else if (x == -1 && y == 0) {
			pointsToGo.removeFirst();
			return 4; // ESQUERDA
		} else if (x == 0 && y == -1) {
			pointsToGo.removeFirst();
			return 1; // CIMA
		} else {
			fear = 0;
			return coin();
		}
	}
}

//package algoritmo;
//
//import algoritmo.path.Graph;
//import controle.Constantes;
//import java.awt.*;
//import java.util.*;
//import java.util.List;
//
//public class Poupador extends ProgramaPoupador {
//
//	private int visited = 6;
//	private List<Point> memoryCoin = new ArrayList<>();
//	private List<Point> memoryPower = new ArrayList<>();
//	private int[][] map = new int[30][30];
//	private int[][] movements = {
//			{0, 0}, {0, -1}, {0, 1}, {1, 0}, {-1, 0}
//	};
//	private int[] graphMovements = {-5, 5, 1, -1};
//	private int[][] viewMove = {
//			{-2, -2}, {-1, -2}, {0, -2}, {1, -2}, {2, -2},
//			{-2, -1}, {-1, -1}, {0, -1}, {1, -1}, {2, -1},
//			{-2, 0},  {-1, 0},  {1, 0},  {2, 0}, {-2, 1},
//			{-1, 1},  {0, 1},   {1, 1},  {2, 1}, {-2, 2},
//			{-1, 2},  {0, 2},   {1, 2},  {2, 2},
//	};
//	private final int[] up = {7, 6, 8};
//	private final int[] right = {12, 8, 17};
//	private final int[] down = {16, 15, 17};
//	private final int[] left = {11, 6, 15};
//	private int fear = 0;
//	private Boolean canGoToBank = false;
//
//	Graph graph = new Graph(900);
//
//	public int acao() {
//		memory();
//		goToBank();
//		if (!canGoToBank) {
//			return coin();
//		} else {
//			return moveToBank();
//		}
//	}
//
//	private void memory() {
//		Point pointNow = sensor.getPosicao();
//		int[] see = sensor.getVisaoIdentificacao();
//		map[pointNow.x][pointNow.y] = visited;
//		if(sensor.getNumeroDeMoedas() == 0){
//			fear = 0;
//		}
//		for(int i = 0; i < see.length; i++) {
//			if (see[i] != Constantes.foraAmbiene) {
//				Point pointNew = new Point(pointNow.x + viewMove[i][0], pointNow.y + viewMove[i][1]);
//				if (see[i] == Constantes.numeroBanco && sensor.getNumeroDeMoedas() != 0) {}
//				if (see[i] == Constantes.numeroMoeda && !memoryCoin.contains(pointNew)) {
//					memoryCoin.add(pointNew);
//				}
//				if(memoryCoin.contains(pointNow)){
//					memoryCoin.remove(pointNow);
//					if(fear == 0){
//						fear = 2;
//					} else {
//						fear *= 2;
//					}
//				}
//				if (map[pointNew.x][pointNew.y] != visited && see[i] != Constantes.semVisao) {
//					map[pointNew.x][pointNew.y] = see[i];
//				}
//
//				if (see[i] != Constantes.semVisao && see[i] != Constantes.numeroParede) {
//					for (int j = 1; j < 5; j++) {
//						Point pointNewAdj = new Point(pointNew.x + movements[j][0], pointNew.y + movements[j][1]);
//						int xAdj = pointNow.x - pointNewAdj.x; // -2 < x < 2
//						int yAdj = pointNow.y - pointNewAdj.y; // -2 < y < 2
//						if (xAdj <= 2 && xAdj >= -2 && yAdj <= 2 && yAdj >= -2) {
//							if (see[i + graphMovements[j-1]] != Constantes.numeroParede && see[i + graphMovements[j-1]] != Constantes.foraAmbiene) {
//								graph.addEdge2(pointNew.x, pointNew.y, pointNewAdj.x, pointNewAdj.y);
//							}
//						}
//					}
//				}
//			}
//		}
//	}
//
//	private int move() {
//		Point pointNow = sensor.getPosicao();
//		int[] see = sensor.getVisaoIdentificacao();
//		List<Integer> ladroes = new ArrayList<>(Arrays.asList(Constantes.numeroLadrao01,Constantes.numeroLadrao02,Constantes.numeroLadrao03,Constantes.numeroLadrao04));
//		List<Integer> poupadores = new ArrayList<>(Arrays.asList(Constantes.numeroPoupador01, Constantes.numeroPoupador02));
//		for(int i = 0; i < up.length; i++){
//			if(ladroes.contains(see[up[i]])){
//				fear *= 2;
//				if(see[16] == Constantes.foraAmbiene || see[16] == Constantes.numeroParede || ladroes.contains(see[16]) || poupadores.contains(see[16])){
//					if(see[11] == Constantes.foraAmbiene || see[11] == Constantes.numeroParede || ladroes.contains(see[11]) || poupadores.contains(see[11])){
//						return 3;
//					}
//					return 4;
//				}
//				return 2;
//			}
//			if(ladroes.contains(see[left[i]])){
//				fear *= 2;
//				if(see[12] == Constantes.foraAmbiene || see[12] == Constantes.numeroParede || ladroes.contains(see[12]) || poupadores.contains(see[12])){
//					if(see[7] == Constantes.foraAmbiene || see[7] == Constantes.numeroParede || ladroes.contains(see[7]) || poupadores.contains(see[7])){
//						return 2;
//					}
//					return 1;
//				}
//				return 3;
//			}
//			if(ladroes.contains(see[right[i]])){
//				fear *= 2;
//				if(see[11] == Constantes.foraAmbiene || see[11] == Constantes.numeroParede || ladroes.contains(see[11]) || poupadores.contains(see[11])){
//					if(see[7] == Constantes.foraAmbiene || see[7] == Constantes.numeroParede || ladroes.contains(see[7]) || poupadores.contains(see[7])){
//						return 2;
//					}
//					return 1;
//				}
//				return 4;
//			}
//			if(ladroes.contains(see[down[i]])){
//				fear *= 2;
//				if(see[7] == Constantes.foraAmbiene || see[7] == Constantes.numeroParede || ladroes.contains(see[7]) || poupadores.contains(see[7])){
//					if(see[11] == Constantes.foraAmbiene || see[11] == Constantes.numeroParede || ladroes.contains(see[11]) || poupadores.contains(see[11])){
//						return 3;
//					}
//					return 4;
//				}
//				return 1;
//			}
//		}
//		return (int) (Math.random() * 5);
//	}
//
//	private int coin(){
//		int[] see = sensor.getVisaoIdentificacao();
//
//		int x = (int) (Math.random() * 1000);
//		if(x < 100 + fear) {
//			return move();
//		} else {
//			if(see[7] == Constantes.numeroMoeda){
//				return 1;
//			} else if(see[11] == Constantes.numeroMoeda){
//				return 4;
//			} else if(see[12] == Constantes.numeroMoeda){
//				return 3;
//			} else if(see[16] == Constantes.numeroMoeda) {
//				return 2;
//			} else {
//				return (int) (Math.random() * 5);
//			}
//		}
//
//	}
//
//	LinkedList<Point> pointsToGo = new LinkedList<>();
//	private void goToBank() {
//		Point pointNow = sensor.getPosicao();
//		if (graph.canRouteToBank(pointNow.x, pointNow.y)) canGoToBank = true;
//		if (canGoToBank) {
//			LinkedList<Integer> shortestPathToBank = graph.shortestDistance(pointNow.x, pointNow.y, 8 ,8);
//
//			for (int i = 2; i <= shortestPathToBank.size()-2; i=i+2) {
//				Point nextPoint = new Point(shortestPathToBank.get(i), shortestPathToBank.get(i+1));
//				pointsToGo.add(nextPoint);
//			}
//		}
//	}
//
//	private int moveToBank() {
//		Point pointNow = sensor.getPosicao();
//		int x, y;
//
//		x = pointsToGo.get(0).x - pointNow.x;
//		y = pointsToGo.get(0).y - pointNow.y;
//
//		if (x == 1 && y == 0) {
//			pointsToGo.removeFirst();
//			return 3; // DIREITA
//		} else if (x == 0 && y == 1) {
//			pointsToGo.removeFirst();
//			return 2; // BAIXO
//		} else if (x == -1 && y == 0) {
//			pointsToGo.removeFirst();
//			return 4; // ESQUERDA
//		} else if (x == 0 && y == -1) {
//			pointsToGo.removeFirst();
//			return 1; // CIMA
//		} else {
//			return coin();
//		}
//	}
//}