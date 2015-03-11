public class Main
{
	public static void main(String[] args)
	{
		Maze m = new Maze(50,50);
		m.createMaze(1, 1);
		m.shortestPath(1,1, 35, 35);
		m.print();
	}
}
