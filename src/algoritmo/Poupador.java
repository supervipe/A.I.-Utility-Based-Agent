package algoritmo;

import controle.Constantes;

import java.awt.*;
import java.util.*;
import java.util.List;


public class Poupador extends ProgramaPoupador {

	private int visited = 6;
	private List<Point> memoryCoin = new ArrayList<>();
	private List<Point> memoryPower = new ArrayList<>();
	private int[][] map = new int[30][30];
	private int[][] movements = {
			{0, 0}, {0, -1}, {0, 1}, {1, 0}, {-1, 0}
	};
	private int[][] viewMove = { {-2, -2}, {-1, -2}, {0, -2}, {1, -2}, {2, -2}, {-2, -1}, {-1, -1}, {0, -1}, {1, -1}, {2, -1}, {-2, 0}, {-1, 0}, {1, 0}, {2, 0}, {-2, 1}, {-1, 1}, {0, 1}, {1, 1}, {2, 1}, {-2, 2}, {-1, 2}, {0, 2}, {1, 2}, {2, 2},	};
	private final int[] up = {7, 6, 8};
	private final int[] right = {12, 8, 17};
	private final int[] down = {16, 15, 17};
	private final int[] left = {11, 6, 15};
	private int fear = 0;
	
	public int acao() {
		memory();
		System.out.println("Fear " + fear);
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
				if (see[i] == Constantes.numeroBanco && sensor.getNumeroDeMoedas() != 0) {

				}
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
			}
		}
	}

	private int move() {
		Point pointNow = sensor.getPosicao();
		int[] see = sensor.getVisaoIdentificacao();
		List<Integer> ladroes = new ArrayList<>(Arrays.asList(Constantes.numeroLadrao01,Constantes.numeroLadrao02,Constantes.numeroLadrao03,Constantes.numeroLadrao04));
		List<Integer> poupadores = new ArrayList<>(Arrays.asList(Constantes.numeroPoupador01, Constantes.numeroPoupador02));
		for(int i = 0; i < up.length; i++){
				if(ladroes.contains(see[up[i]])){
					fear *= 2;
					if(see[16] == Constantes.foraAmbiene || see[16] == Constantes.numeroParede || ladroes.contains(see[16]) || poupadores.contains(see[16])){
						if(see[11] == Constantes.foraAmbiene || see[11] == Constantes.numeroParede || ladroes.contains(see[11]) || poupadores.contains(see[11])){
							return 3;
						}
						return 4;
					}
					return 2;
				}
				if(ladroes.contains(see[left[i]])){
					fear *= 2;
					if(see[12] == Constantes.foraAmbiene || see[12] == Constantes.numeroParede || ladroes.contains(see[12]) || poupadores.contains(see[12])){
						if(see[7] == Constantes.foraAmbiene || see[7] == Constantes.numeroParede || ladroes.contains(see[7]) || poupadores.contains(see[7])){
							return 2;
						}
						return 1;
					}
					return 3;
				}
				if(ladroes.contains(see[right[i]])){
					fear *= 2;
					if(see[11] == Constantes.foraAmbiene || see[11] == Constantes.numeroParede || ladroes.contains(see[11]) || poupadores.contains(see[11])){
						if(see[7] == Constantes.foraAmbiene || see[7] == Constantes.numeroParede || ladroes.contains(see[7]) || poupadores.contains(see[7])){
							return 2;
						}
						return 1;
					}
					return 4;
				}
				if(ladroes.contains(see[down[i]])){
					fear *= 2;
					if(see[7] == Constantes.foraAmbiene || see[7] == Constantes.numeroParede || ladroes.contains(see[7]) || poupadores.contains(see[7])){
						if(see[11] == Constantes.foraAmbiene || see[11] == Constantes.numeroParede || ladroes.contains(see[11]) || poupadores.contains(see[11])){
							return 3;
						}
						return 4;
					}
					return 1;
				}
		}
		return (int) (Math.random() * 5);
	}

	private int coin(){
		int[] see = sensor.getVisaoIdentificacao();

		int x = (int) (Math.random() * 1000);
		if(x < 100 + fear) {
			System.out.println("MOVE");
			return move();
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
				return (int) (Math.random() * 5);
			}
		}

	}

}