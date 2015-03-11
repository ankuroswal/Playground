public class Main
{
	public static void main(String[] args)
	{
		Maze m = new Maze(15,15);
		m.createMaze(1, 1);
		m.shortestPath(1,1, 13, 13);
		m.print();
	}
}
