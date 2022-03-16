package algoritmo;//package algoritmo;

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
	private int[] graphMovements = {-5, 5, 1, -1};
	private int[][] viewMove = {
			{-2, -2}, {-1, -2}, {0, -2}, {1, -2}, {2, -2},
			{-2, -1}, {-1, -1}, {0, -1}, {1, -1}, {2, -1},
			{-2, 0},  {-1, 0},  {1, 0},  {2, 0}, {-2, 1},
			{-1, 1},  {0, 1},   {1, 1},  {2, 1}, {-2, 2},
			{-1, 2},  {0, 2},   {1, 2},  {2, 2},
	};
	private final int[] up = {7, 6, 8};
	private final int[] right = {12, 8, 17};
	private final int[] down = {16, 15, 17};
	private final int[] left = {11, 6, 15};
	private int fear = 0;
	private int multiplicador = 1;
	LinkedList<Point> pointsToGo = new LinkedList<>();

	Graph graph = new Graph(900);

	public int acao() {
		Point pointNow = sensor.getPosicao();
		memory(pointNow);
		System.out.println("FEAR " + fear);
		return move(pointNow);
	}

	private void memory(Point pointNow) {
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
					multiplicador = 1;
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
		if(sensor.getNumeroDeMoedas() == 0){
			multiplicador++;
		}
	}

	private int run() {
		int[] see = sensor.getVisaoIdentificacao();
		List<Integer> ladroes = new ArrayList<>(Arrays.asList(Constantes.numeroLadrao01,Constantes.numeroLadrao02,Constantes.numeroLadrao03,Constantes.numeroLadrao04));
		List<Integer> poupadores = new ArrayList<>(Arrays.asList(Constantes.numeroPoupador01, Constantes.numeroPoupador02));
		for(int i = 0; i < up.length; i++){
			if(ladroes.contains(see[up[i]])){
				fear *= 2;

				if(perigo(see,ladroes,poupadores,16,21,17)){
					if(perigo(see,ladroes,poupadores,11,15,6,10)){
						return 3;
					}
					return 4;
				}
				return 2;
			}
			if(ladroes.contains(see[left[i]])){
				fear *= 2;

				if(perigo(see,ladroes,poupadores,12,13,8,17)){
					if(perigo(see,ladroes,poupadores,7,2,6)){
						return 2;
					}
					return 1;
				}
				return 3;
			}
			if(ladroes.contains(see[right[i]])){
				fear *= 2;

				if(perigo(see,ladroes,poupadores,11,10,15,6)){
					if(perigo(see,ladroes,poupadores,7,2,8)){
						return 2;
					}
					return 1;
				}
				return 4;
			}
			if(ladroes.contains(see[down[i]])){
				fear *= 2;

				if(perigo(see,ladroes,poupadores,7,2,6,8)){
					if(perigo(see,ladroes,poupadores,11,10,15)){
						return 3;
					}
					return 4;
				}
				return 1;
			}
		}
		return -1;
	}

	private boolean perigo(int[] see, List<Integer> ladroes,List<Integer> poupadores, int i, int j, int z, int k){
		int cont = 0;
		if(see[i] == Constantes.foraAmbiene || see[i] == Constantes.numeroParede || ladroes.contains(see[i]) || ladroes.contains(see[j]) || ladroes.contains(see[z]) || ladroes.contains(see[k]) || poupadores.contains(see[i])){
			cont++;
		}
		if(see[i] == Constantes.numeroPastinhaPoder && sensor.getNumeroDeMoedas() < 5){
			cont++;
		}
		if(cont > 0){
			return true;
		} else {
			return false;
		}
	}
	private boolean perigo(int[] see, List<Integer> ladroes,List<Integer> poupadores, int i, int j, int k){
		int cont = 0;
		if(see[i] == Constantes.foraAmbiene || see[i] == Constantes.numeroParede || ladroes.contains(see[i]) || ladroes.contains(see[j])  || ladroes.contains(see[k]) || poupadores.contains(see[i])){
			cont++;
		}
		if(see[i] == Constantes.numeroPastinhaPoder && sensor.getNumeroDeMoedas() < 5){
			cont++;
		}
		if(cont > 0){
			return true;
		} else {
			return false;
		}
	}

	private int coin(){
		int[] see = sensor.getVisaoIdentificacao();
		if(see[7] == Constantes.numeroMoeda){
			return 1;
		} else if(see[11] == Constantes.numeroMoeda){
			return 4;
		} else if(see[12] == Constantes.numeroMoeda){
			return 3;
		} else if(see[16] == Constantes.numeroMoeda) {
			return 2;
		} else {
			return explore();
		}
	}

	private int move(Point pointNow) {
		int y = run();
		if(y != -1){
			return y;
		}
		if (graph.canRouteToBank(pointNow.x, pointNow.y) && sensor.getNumeroDeMoedas() > 3 && fear > 1000) {
			System.out.println("ENTROU");
			pointsToGo.addAll(graph.pointsToGo(pointNow));

			int point_x, point_y;
			point_x = pointsToGo.get(0).x - pointNow.x;
			point_y = pointsToGo.get(0).y - pointNow.y;

			if (point_x == 1 && point_y == 0) {
				pointsToGo.removeFirst();
				return 3; // DIREITA
			} else if (point_x == 0 && point_y == 1) {
				pointsToGo.removeFirst();
				return 2; // BAIXO
			} else if (point_x == -1 && point_y == 0) {
				pointsToGo.removeFirst();
				return 4; // ESQUERDA
			} else if (point_x == 0 && point_y == -1) {
				pointsToGo.removeFirst();
				return 1; // CIMA
			} else {
				fear = 0;
				pointsToGo.clear();
				return coin();
			}
		} else {
			pointsToGo.clear();
			return coin();
		}
	}

	private int explore(){
		Point pointNow = sensor.getPosicao();
		int[] see = sensor.getVisaoIdentificacao();
		int bancox = 8 - pointNow.x;
		int bancoy = 8 - pointNow.y;
		HashMap<Integer, Integer> positionsChance = new HashMap<Integer, Integer>();
		positionsChance.put(1, 100);
		positionsChance.put(2, 100);
		positionsChance.put(3, 100);
		positionsChance.put(4, 100);
		if(bancoy < 0){
			positionsChance.replace(1,(positionsChance.get(1)*5));
		}
		if(bancoy > 0){
			positionsChance.replace(2,(positionsChance.get(2)*5));
		}
		if(bancox < 0){
			positionsChance.replace(4,(positionsChance.get(4)*5));
		}
		if(bancox > 0){
			positionsChance.replace(3,(positionsChance.get(3)*5));
		}
		int[] around = new int[]{ 7, 16, 12, 11 };
		int soma = 0;
		LinkedList<Integer> chances = new LinkedList<Integer>();
		for(int i = 0; i < around.length; i++) {
			if (see[around[i]] == Constantes.numeroParede || see[around[i]] == Constantes.foraAmbiene) {
				positionsChance.replace((i+1), -1);
			} else {
				if(see[around[i]] == Constantes.numeroBanco && sensor.getNumeroDeMoedas() == 0){
					positionsChance.replace((i + 1), -1);
				} else {
					int mapX = pointNow.x + movements[i + 1][0];
					int mapY = pointNow.y + movements[i + 1][1];
					if (map[mapX][mapY] == visited) {
						positionsChance.replace((i + 1), (positionsChance.get(i + 1) / (5*multiplicador)));
					}
					if (see[around[i]] == Constantes.numeroPastinhaPoder && fear >= 4000) {
						positionsChance.replace((i + 1), (positionsChance.get(i + 1) * 3));
					}
					soma += positionsChance.get(i + 1);
				}
			}
			chances.add(positionsChance.get(i+1));
		}
		Collections.sort(chances);
		int ran = (int) (Math.random() * soma);
		soma = 0;
		for(int i = 0; i < around.length; i++){
			if(chances.get(i) != -1){
				soma += chances.get(i);
				if(ran < soma){
					for(int j = 1; j < 5; j++){
						if(Objects.equals(positionsChance.get(j), chances.get(i))){
							return j;
						}
					}
				}
			}
		}

		return (int) (Math.random() * 5);
	}
}

class Graph {
	private final int vertices_size;
	private final ArrayList<ArrayList<Integer>> adj;

	public Graph(int vertices_size) {
		this.vertices_size = vertices_size;

		// Adjacency list for storing which vertices are connected
		adj = new ArrayList<ArrayList<Integer>>(vertices_size);

		for (int i = 0; i < vertices_size; i++) {
			adj.add(new ArrayList<Integer>());
		}
	}

	public int mapPositions(int pos_x, int pos_y) {
		// pos_x = 29, pos_y = 29 -> 900
		// pos_x =  1, pos_y =  0 ->  30
		int count = 0;
		if(pos_y != 0){
			pos_y -=1;
		}
		for (int y = 0; y < pos_y*30; y++) {
			count++;
		}

		if (pos_x == 0) {
			return count;
		} else {
			for (int x = 0; x < pos_x; x++) {
				count++;
			}
		}
		return count;
	}

	public int[][] mapPositionsToPosition(int num_v) {
		// 900 -> pos_x = 29, pos_y = 29
		// 30 -> pos_x = 0, pos_y = 1
		int pos_x = ((num_v+1)%30) - 1;
		int pos_y = Math.abs((num_v+1)/30);

		if ((num_v+1)%30 == 0) {
			if(pos_y == 0) {
				pos_x = 0;
			} else {
				pos_x = 29;
			}
		}

		return new int[][]{{pos_x}, {pos_y}};
	}

	public void addEdge2(int pos_x, int pos_y, int pos_x2, int pos_y2) {
		int current_v = mapPositions(pos_x, pos_y);
		int linked_v = mapPositions(pos_x2, pos_y2);

		adj.get(current_v).add(linked_v);
		adj.get(linked_v).add(current_v);
	}

	public Boolean canRouteToBank(int pos_x, int pos_y) {
		int source_v = mapPositions(pos_x, pos_y);
		int destination_v = mapPositions(8, 8);

		int[] predecessor = new int[vertices_size];
		int[] distance = new int[vertices_size];

		return BFS(source_v, destination_v, vertices_size, predecessor, distance);
	}

	public LinkedList<Point> pointsToGo(Point pointNow) {
		LinkedList<Integer> shortestPathToBank = shortestDistance(pointNow.x, pointNow.y, 8 ,8);

		LinkedList<Point> pointsToGoList = new LinkedList<>();
		for (int i = 2; i <= shortestPathToBank.size()-2; i=i+2) {
			Point nextPoint = new Point(shortestPathToBank.get(i), shortestPathToBank.get(i+1));
			pointsToGoList.add(nextPoint);
		}
		return pointsToGoList;
	}

	public LinkedList<Integer> shortestDistance(int pos_x, int pos_y, int pos_x2, int pos_y2) {
		int source_v = mapPositions(pos_x, pos_y);
		int destination_v = mapPositions(pos_x2, pos_y2);

		int[] predecessor = new int[vertices_size];
		int[] distance = new int[vertices_size];

		if (!BFS(source_v, destination_v, vertices_size, predecessor, distance)) {

		}

		// LinkedList to store path
		LinkedList<Integer> path = new LinkedList<Integer>();
		int crawl = destination_v;
		path.add(crawl);
		while (predecessor[crawl] != -1) {
			path.add(predecessor[crawl]);
			crawl = predecessor[crawl];
		}

		LinkedList<Integer> result = new LinkedList<>();
		for (int i = path.size() - 1; i >= 0; i--) {
			result.add(mapPositionsToPosition(path.get(i))[0][0]);
			result.add(mapPositionsToPosition(path.get(i))[1][0]);
		}

		return result;
	}

	// a modified version of BFS that stores predecessor
	// of each vertex in array pred
	// and its distance from source in array dist
	boolean BFS(int src, int dest, int v, int[] pred, int[] dist) {
		// a queue to maintain queue of vertices whose
		// adjacency list is to be scanned as per normal
		// BFS algorithm using LinkedList of Integer type
		LinkedList<Integer> queue = new LinkedList<Integer>();

		// boolean array visited[] which stores the
		// information whether ith vertex is reached
		// at least once in the Breadth first search
		boolean[] visited = new boolean[v];

		// initially all vertices are unvisited
		// so v[i] for all i is false
		// and as no path is yet constructed
		// dist[i] for all i set to infinity
		for (int i = 0; i < v; i++) {
			visited[i] = false;
			dist[i] = Integer.MAX_VALUE;
			pred[i] = -1;
		}

		// now source is first to be visited and
		// distance from source to itself should be 0
		visited[src] = true;
		dist[src] = 0;
		queue.add(src);

		// bfs Algorithm
		while (!queue.isEmpty()) {
			int u = queue.remove();
			for (int i = 0; i < adj.get(u).size(); i++) {
				if (!visited[adj.get(u).get(i)]) {
					visited[adj.get(u).get(i)] = true;
					dist[adj.get(u).get(i)] = dist[u] + 1;
					pred[adj.get(u).get(i)] = u;
					queue.add(adj.get(u).get(i));

					// stopping condition (when we find
					// our destination)
					if (adj.get(u).get(i) == dest) return true;
				}
			}
		}
		return false;
	}
}