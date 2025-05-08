public class NBody {
    private static int number;
    private static double radius;
    public static double readRadius(String args) {
        In in = new In(args);
        in.readInt();
        radius = in.readDouble();
        return radius;
    }
    public static Planet[] readPlanets(String filename) {
        In in = new In(filename);
        number = in.readInt();
        in.readDouble();
        Planet[] planets = new Planet[number];
        for (int i = 0; i < number; i++) {
            planets[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
        }
        return planets;
    }

    private static String imageToDraw = "./images/starfield.jpg";
    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];
        readRadius(filename);
        Planet[] planets = readPlanets(filename);
        StdDraw.enableDoubleBuffering();
        StdDraw.setScale(-radius, radius);
        StdDraw.clear();
        StdDraw.picture(0, 0, imageToDraw);
        StdDraw.show();
        for (int i = 0; i < number; i++) {
            planets[i].draw();
        }
        StdDraw.show();
        double[] xforces = new double[planets.length];
        double[] yforces = new double[planets.length];
        for (int i = 0; i < T; i += dt) {
            for (int j = 0; j < number; j++) {
                xforces[j] = planets[j].calcNetForceExertedByX(planets);
                yforces[j] = planets[j].calcNetForceExertedByY(planets);
            }
            for (int j = 0; j < number; j++) {
                planets[j].update(dt, xforces[j], yforces[j]);
            }
            StdDraw.picture(0, 0, imageToDraw);
            for (int j = 0; j < number; j++) {
                planets[j].draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
            StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                    planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                    planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
        }
    }
}
