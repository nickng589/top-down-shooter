import javafx.geometry.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import java.util.*;
import javafx.util.Duration;
import javafx.animation.*;

//swarm has all the mobs and stuff
public class Swarm{
	
	//fields
	ArrayList<Mob> swarm = new ArrayList<Mob>();
	int numMobs;
	Timeline collisionCheck;
    Pane playground;
	
	//constructors
	
	/* There should eventually be a constructor that takes a list of mobs, so that each room can have a unique list
	 * of mobs to them
	*/
	public Swarm(int n){
		numMobs = n;
		collisionCheck = new Timeline(new KeyFrame(Duration.millis(10),ae -> checkCollisions()));
		collisionCheck.setCycleCount(Animation.INDEFINITE);
		collisionCheck.play();
	}
	
	//setters and getters
	public ArrayList<Mob> getSwarm(){return swarm;}
	public Mob getSwarm(int i){return swarm.get(i);}	
	
	//methods
	/*public void spawnSwarm(Pane main){
		playground = main;
		for(int i = 0; i < numMobs; i++){
			double spawnX = 0;
			double spawnY = 0;
			
			int spawnPos = (int)(Math.random()*4); //0=north, 1=east, 2=south, 3=west
			
			switch (spawnPos){
				case 0: spawnX = Math.random()*main.getPrefWidth();
						break;
				case 1: spawnY = Math.random()*main.getPrefHeight();
						break;
				case 2: spawnX = Math.random()*main.getPrefWidth();
						spawnY = main.getPrefHeight();
						break;
				case 3: spawnY = Math.random()*main.getPrefHeight();
						spawnX = main.getPrefWidth();
						break;
			}

			Mob temp = new ZombieMob();
			swarm.add(temp);
			main.getChildren().add(temp);
			temp.setLayoutX(spawnX);
			temp.setLayoutY(spawnY);
			
		}
		
	}
	*/
	public void spawnSwarm(Pane main){
		playground = main;
		for(int i = 0; i < 1; i++){
			double spawnX = 200;
			double spawnY = 200;
			
			int spawnPos = (int)(Math.random()*4); //0=north, 1=east, 2=south, 3=west
			
			switch (spawnPos){
				case 0: spawnX = Math.random()*(main.getPrefWidth()-200)+200;
						break;
				case 1: spawnY = Math.random()*(main.getPrefHeight()-200)+200;
						break;
				case 2: spawnX = Math.random()*(main.getPrefWidth()-200)+200;
						spawnY = main.getPrefHeight()-200;
						break;
				case 3: spawnY = Math.random()*(main.getPrefHeight()-200)+200;
						spawnX = main.getPrefWidth()-200;
						break;
			}

			Mob temp = new LaserMachine();
			swarm.add(temp);
			main.getChildren().add(temp);
			temp.setLayoutX(spawnX);
			temp.setLayoutY(spawnY);
			
		}
		
	}
	
	public void swarmPlayer(Player p){
		for(int i = 0; i < swarm.size(); i++){
			swarm.get(i).chase(p);
		}
	}
	
	public void resetSwarm(){
		for(int i=0;i<swarm.size();i++){
			TopDownShooter.playground.getChildren().remove(swarm.get(i));
		}
		swarm = new ArrayList<Mob>();
	}
	
	//TODO
	/* Knocks back all mobs in the direction opposite of what they are facing with magnitude m
	 * @input: double n
	*/
	public void knockbackMobs(Player p, double m){
		for(Mob mob : swarm){
			if(mob.getKnockback()){
				mob.knockback(p.getLocX(), p.getLocY(), m);
			}
		}
	}
	
	public void knockbackMobsAnimated(Player p, double m){
		Timeline delay = new Timeline(new KeyFrame(Duration.millis(5),ae -> knockbackMobs(p, m/50)));
		delay.setCycleCount(50);
		delay.play();
	}
	
	
	
	private void checkCollisions(){
		checkRoomBounds();
		checkMobCollisions();
	}
	
	private void checkRoomBounds(){
		for(Mob m : swarm){
			//used to be //Bounds b = m.getBody().localToScene(m.getBoundsInLocal());
			Bounds b = m.getBody().localToScene(m.getBody().getBoundsInLocal());
			if(b.getMaxX() >= playground.getPrefWidth()){
				m.move(-1,0);
			}
			else if(b.getMinX() <= 0){
				m.move(1,0);
			}
			else if(b.getMaxY()+m.getBodyHeight() >= playground.getPrefHeight()){
				m.move(0,-1);
			}
			else if(b.getMinY() <= 0){
				m.move(0,1);
			}
		}
	}
	
	private void checkMobCollisions(){
		for(int a = 0; a < swarm.size(); a++){
			for (int b = a+1; b < swarm.size(); b++){
				Mob m1 = swarm.get(a);
				Mob m2 = swarm.get(b);
				Shape s1 = m1.getBody(); //eventually should be Polygon
				Shape s2 = m2.getBody();
				Bounds b1 = s1.localToScene(s1.getBoundsInLocal());
				Bounds b2 = s2.localToScene(s2.getBoundsInLocal());
				if(b1.intersects(b2)){
					if(b1.getMaxX() > b2.getMaxX() && b1.getMaxX()-b2.getMaxX() <= m1.getBodyWidth()){
						swarm.get(a).move(1,0);
						swarm.get(b).move(-1,0);
					}
					if(b1.getMaxX() < b2.getMaxX() && b2.getMaxX()-b1.getMaxX() <= m1.getBodyWidth()){
						swarm.get(a).move(-1,0);
						swarm.get(b).move(1,0);
					}
					if(b1.getMaxY() > b2.getMaxY() && b1.getMaxY()-b2.getMaxY() <= m1.getBodyHeight()){
						swarm.get(a).move(0,1);
						swarm.get(b).move(0,-1);
					}
					if(b1.getMaxY() < b2.getMaxY() && b2.getMaxY()-b1.getMaxY() <= m1.getBodyHeight()){
						swarm.get(a).move(0,-1);
						swarm.get(b).move(0,1);
					}
				}
			}
		}
	}
	
}