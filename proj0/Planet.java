public class Planet {
    static final double G = 6.67e-11;
    public double xxPos;
    public double yyPos;
    public  double xxVel;
    public  double yyVel;
    public double mass;
    public String imgFileName;
    public Planet(double xP, double yP, double xV,
                  double yV, double m, String img){
        this.xxPos = xP;
        this.yyPos = yP;
        this.xxVel = xV;
        this.yyVel = yV;
        this.mass = m;
        this.imgFileName = img;
    }
    public Planet(Planet p){
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }
    public double calcDistance(Planet p){
        double dx = p.xxPos - this.xxPos;
        double dy = p.yyPos - this.yyPos;
        double dis = Math.sqrt(dx*dx + dy*dy);
        return dis;
    }
    public double calcForceExertedBy(Planet p){
        double force = G * this.mass * p.mass / (this.calcDistance(p) * this.calcDistance(p));
        return force;
    }
    public double calcForceExertedByX(Planet p){
        return calcForceExertedBy(p) * (p.xxPos - this.xxPos) / this.calcDistance(p);
    }
    public double calcForceExertedByY(Planet p){
        return calcForceExertedBy(p) * (p.yyPos - this.yyPos) / this.calcDistance(p);
    }
    public double calcNetForceExertedByX(Planet[] allPlanets){
        double forces = 0;
        for(int i = 0; i < allPlanets.length; i++){
            if(!this.equals(allPlanets[i])){
                forces += this.calcForceExertedByX(allPlanets[i]);
            }
        }
        return forces;
    }
    public double calcNetForceExertedByY(Planet[] allPlanets){
        double forces = 0;
        for(int i = 0; i < allPlanets.length; i++){
            if(!this.equals(allPlanets[i])){
                forces += this.calcForceExertedByY(allPlanets[i]);
            }
        }
        return forces;
    }
    public void update(double dt, double fx, double fy){
        double anetx = fx / this.mass;
        double anety = fy / this.mass;
        this.xxVel += dt * anetx;
        this.yyVel += dt * anety;
        this.xxPos += dt * this.xxVel;
        this.yyPos += dt * this.yyVel;
    }
    public void draw(){
        String imgFileName = "./images/" + this.imgFileName;
        StdDraw.picture(this.xxPos, this.yyPos, imgFileName);
    }
}
