import java.util.*;

public class Maze
{
    // helper class to encapuslate a point on a graph
    private class Point implements Comparable<Point>
    {
        int x;
        int y;
        Integer weight = 0;

        public Point(int x, int y){ this.x = x; this.y = y; }
        public Point(int x, int y, int weight){ this.x = x; this.y = y; this.weight = weight; }

        public int getHash() { return ((x + y)*(x + y + 1)/2) + y; }

        public int compareTo(Point other) { return weight.compareTo(other.weight); }
    }


    // constant variables 
    private static final char WALL = '#';
    private static final char OPEN = ' ';
    private static final char PATH = '+';
    private static final Integer[][] DIR = {{1,0}, {-1,0}, {0,1}, {0, -1}};

    // grid properties
    private char[][] grid;
    private int width;
    private int height;

    public Maze(int height, int width)
    {
        grid = new char[height][width];
        this.height = height;
        this.width = width;
        initialize();
    }

    // shortest class API 
    // returns the length of the path
    public int shortestPath(int x1, int y1, int x2, int y2)
    {
        Point end = new Point(x2, y2);
        Point start = new Point(x1, y1);

        if (!valid(end) || !valid(start))
        {
            System.out.println("You have entered an invalid position!");
            return 0;
        }

        if (isWall(end) || isWall(start))
        {
            System.out.println("The position you have entered is a wall!");
            return 0;
        }

        ArrayList<Point> path = computeShortestPath(end, start);

        for (Point pp : path)
        {
            grid[pp.y][pp.x] = PATH;
        }
        grid[start.y][start.x] = 'S';
        grid[end.y][end.x] = 'E';
        return path.size();
    }

    // computes the shortest path using BFS
    private ArrayList<Point> computeShortestPath(Point end, Point start)
    {
        PriorityQueue<Point> queue = new PriorityQueue<Point>();
        Integer[][] visited = new Integer[grid.length][grid[0].length];

        for (int i = 0; i < visited.length; i++)
            for (int j = 0;  j < visited[i].length; j++)
                visited[i][j] = -1;

        queue.add(start);

        while(!queue.isEmpty())
        {
            Point p = queue.poll();
            if (visited[p.x][p.y] == -1 && grid[p.x][p.y] == OPEN) 
            {
                visited[p.x][p.y] = p.weight;

                // found the path build it!
                if (p.x == end.x && p.y == end.y)
                {
                    return buildPath(end, visited);
                }   
                for (Integer[] dir : DIR )
                    queue.add(new Point(dir[0] + p.x, dir[1] + p.y, p.weight + 1));
            }
        }
        return new ArrayList<Point>();
    }

    // backwards traversal of the graph by weight to get the shortest path
    private ArrayList<Point> buildPath(Point end, Integer[][] visited)
    {
        Point current = end;
        int weight = visited[end.y][end.x];
        current.weight = weight;
        ArrayList<Point> path = new ArrayList<Point>();
        while(weight > 0)
        {
            for (Integer[] dir : DIR)
            {
                Point tmp = new Point(dir[0] + current.x, dir[1] + current.y);
                tmp.weight = visited[tmp.y][tmp.x];

                if (tmp.weight == current.weight - 1)
                {
                    path.add(tmp);
                    current = new Point(tmp.x, tmp.y, tmp.weight);
                    break;
                }
            }
            weight--;
        }
        return path;
    }


    // creates the maze with a random DFS by checking if there there is 
    // adjacent walls around the current node.
    public void createMaze(int x, int y)
    {
        Stack<Point> stack = new Stack<Point>();
        HashSet<Integer> visited = new HashSet<Integer>();

        Point start = new Point(x, y);
        stack.push(start);

        while (!stack.isEmpty())
        {
            Point p = stack.pop();

            if (valid(p) && !visited.contains(p.getHash()) && isWall(p) && isFilled(p, 3))
            {
                grid[p.y][p.x] = OPEN;
                visited.add(p.getHash());

                LinkedList<Point> list = new LinkedList<Point>();

                for (Integer[] dir : DIR )
                    list.add(new Point(dir[0] + p.x, dir[1] + p.y));

                Collections.shuffle(list);

                for (Point n : list)
                    stack.push(n);
 
            }
        }
    }

    public void print()
    {
        for (char[] layer : grid)
        {
            for (char c : layer)
            {
                System.out.print(c + " ");
            }
            System.out.println("");
        }
    }

    public void initialize()
    {
        for (int i = 0; i < grid.length; i++)
        {
            for (int j = 0;  j < grid[i].length; j++)
            {
                grid[i][j] = WALL;
            }
        }
    }

    // checks to see if the point is on the graph
    public Boolean valid(Point p)
    {
        int i = p.y;
        int j = p.x;
        return i >= 0 && j >= 0 
            && i < grid.length && j < grid[i].length;
    }

    // verifies that there a certain "count" or amount surrounding walls around the point
    public Boolean isFilled(Point p, int count)
    {
        for (Integer[] dir : DIR )
        {
            if (isWall(new Point(dir[0] + p.x, dir[1] + p.y)))
                count--;
        }

        return count <= 0;
    }

    // checks to see if the the point is a node
    public Boolean isWall(Point p)
    {
        return valid(p) && grid[p.y][p.x] == WALL;
    }

}