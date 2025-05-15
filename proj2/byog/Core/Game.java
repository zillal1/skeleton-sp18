package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private static final int TILE_SIZE = 16;

    private World world;

    private long SEED = 0L;
    private String COMMAND;

    private boolean isPlaying = false;
    private boolean hasCommand = false;
    private boolean needToLoad = false;

    private StringBuilder inputHistory = new StringBuilder();
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        displayMainMenu();
        stringAnalysis(keyboardInput());

        if (needToLoad) {
            load();
        } else {
            world = new World(WIDTH, HEIGHT, SEED);
            isPlaying = true;
        }
        playing();
    }
    public void playing() {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        while (isPlaying) {
            ter.renderFrame(world.getTeTiles());
            displayGameUI();
            movePlayer();
        }
        if (!isPlaying) {
            StdDraw.clear();
            StdDraw.show();
            System.exit(0);
        }
    }
    public void displayGameUI() {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(10, HEIGHT - 2, 20, 1);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(10, HEIGHT - 2, cursorPointing(world.getTeTiles()));
        StdDraw.show();
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 20));
    }
    public void movePlayer() {
        world.move(keyboardMove());
    }
    public void movePlayer(Toward toward) {
        if (world == null) {
            throw new RuntimeException(new NullPointerException("world is null"));
        } else {
            world.move(toward);
        }
    }
    private void renderInputHistory() {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(20, HEIGHT - 1, 20, 1);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(10, HEIGHT - 1.5, "输入记录：" + inputHistory.toString());
        StdDraw.show();
    }
    public Toward keyboardMove() {
        char input;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                input = StdDraw.nextKeyTyped();
                inputHistory.append((char) input);
                System.out.println("接收到输入：" + input);
                renderInputHistory();
                switch (input) {
                    case 'W':
                    case 'w':
                        return Toward.W;
                    case 'S':
                    case 's':
                        return Toward.S;
                    case 'A':
                    case 'a':
                        return Toward.A;
                    case 'D':
                    case 'd':
                        return Toward.D;
                    case ':':
                        while (isPlaying) {
                            if (StdDraw.hasNextKeyTyped()) {
                                input = StdDraw.nextKeyTyped();
                                if (input == 'Q' || input == 'q') {
                                    System.out.println("成功保存退出");
                                    quitAndSaving();
                                }
                            }
                        }
                        return Toward.STAY;
                    default:
                        return Toward.STAY;
                }
                //renderInputHistory();
                //StdDraw.pause(20);
            }
            StdDraw.pause(20);
            return Toward.STAY;
        }

    }
    public void quitAndSaving() {
        try {
            java.io.ObjectOutputStream out =
                    new java.io.ObjectOutputStream(new java.io.FileOutputStream("savefile.txt"));
            out.writeObject(world);
            out.close();
            isPlaying = false;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
    private String cursorPointing(TETile[][] teTiles) {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        int xPos = (int) x;
        int yPos = (int) y;
        if (xPos < 0 || xPos >= WIDTH || yPos < 0 || yPos >= HEIGHT) {
            return "Out of bounds! You are at " + "x: " + x + " y: " + y;
        }
        return teTiles[xPos][yPos].description();
    }
    private String cursorPointing() {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        return "x: " + x + " y: " + y;
    }
    public void load() {
        try {
            java.io.ObjectInputStream in =
                    new java.io.ObjectInputStream(new java.io.FileInputStream("savefile.txt"));
            world = (World) in.readObject();
            world.resetLoad();
            isPlaying = true;
            in.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private String keyboardInput() {
        StringBuilder command = new StringBuilder();
        char input;
        while (true) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            input = StdDraw.nextKeyTyped();
            inputHistory.append(input);
            System.out.println("接收到输入：" + input);
            command.append(input);

            if (command.charAt(command.length() - 1) == 'l' || command.charAt(command.length() - 1) == 'L') {
                isPlaying = true;
                break;
            }
            if (command.charAt(command.length() - 1) == 's' || command.charAt(command.length() - 1) == 'S') {
                break;
            }
            renderInputHistory();
            StdDraw.pause(20);
        }

        System.out.println(command);
        return command.toString();
    }
    private void displayMainMenu() {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.text(midWidth, midHeight + 10, "CS61B: THE GAME");

        Font smallFont = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(midWidth, midHeight, "New Game (N)");
        StdDraw.text(midWidth, midHeight - 2, "Load Game (L)");
        StdDraw.text(midWidth, midHeight - 4, "Quit (Q)");
        StdDraw.show();
    }
    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        stringAnalysis(input);
        if (needToLoad) {
            load();
        } else {
            world = new World(WIDTH, HEIGHT, SEED);
            isPlaying = true;
        }
        if (hasCommand) {
            useCommand();
        }
        return world.getTeTiles();
    }
    public void useCommand() {
        for (int i = 0; i < COMMAND.length(); i ++) {
            if (COMMAND.charAt(i) == 'Q') {
                quitAndSaving();
            }
            switch (COMMAND.charAt(i)) {
                case 'W':
                case 'w':
                    movePlayer(Toward.W);
                    break;
                case 'S':
                case 's':
                    movePlayer(Toward.S);
                    break;
                case 'A':
                case 'a':
                    movePlayer(Toward.A);
                    break;
                case 'D':
                case 'd':
                    movePlayer(Toward.D);
                    break;
                default:
                    movePlayer(Toward.STAY);
                    break;
            }
        }
    }
    private void stringAnalysis(String input) {
        //long seed = -1;
        StringBuilder seedAndCommand = new StringBuilder();
        AtomicInteger index = new AtomicInteger(0);
        if (input.toLowerCase().contains("l")) {
            needToLoad = true;
            if (input.length() > 1) {
                index.addAndGet(1);
                COMMAND = commandAnalysis(input, index).toString();
            }
            return;
        }

        seedAndCommand.append(seedAnalysis(input, index));
        seedAndCommand.append(commandAnalysis(input, index));

        SEED = Long.parseLong(seedAndCommand.toString().split(" ")[0]);

        if (hasCommand) {
            COMMAND = seedAndCommand.toString().split(" ")[1];
        }
    }
    private StringBuilder commandAnalysis(String input, AtomicInteger index) {
        StringBuilder commandBuilder = new StringBuilder();
        while (index.get() < input.length()) {
            char command = input.charAt(index.get());
            hasCommand = true;

            switch (command) {
                case 'W':
                case 'w':
                    commandBuilder.append("W");
                    break;
                case 'S':
                case 's':
                    commandBuilder.append("S");
                    break;
                case 'A':
                case 'a':
                    commandBuilder.append("A");
                    break;
                case 'D':
                case 'd':
                    commandBuilder.append("D");
                    break;
                case ':':
                    if (index.get() + 1 < input.length()
                            && (input.charAt(index.get() + 1) == 'Q'
                            || input.charAt(index.get() + 1) == 'q')) {
                        commandBuilder.append("Q");
                    }
                    break;
                default:
                    index.addAndGet(1);
                    break;
            }
            index.addAndGet(1);
        }
        return commandBuilder;
    }
    private StringBuilder seedAnalysis(String input, AtomicInteger index) {
        StringBuilder seedBuilder = new StringBuilder();
        index.addAndGet(1);
        if (input.charAt(0) == 'N' || input.charAt(0) == 'n') {
            while (index.get() < input.length() && Character.isDigit(input.charAt(index.get()))) {
                seedBuilder.append(input.charAt(index.get()));
                index.addAndGet(1);
            }

            if (index.get() < input.length()
                && (input.charAt(index.get()) == 'S' || input.charAt(index.get()) == 's')) {
                index.addAndGet(1);
                seedBuilder.append(" ");
            }
        }
        return seedBuilder;
    }
}
