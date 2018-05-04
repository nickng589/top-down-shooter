import java.util.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.* ; 
import javafx.scene.input.* ;
import javafx.scene.layout.*;
import javafx.event.EventHandler;
import javafx.animation.AnimationTimer;
import javafx.animation.Timeline;
import javafx.animation.*;
import javafx.animation.KeyFrame;
import javafx.scene.shape.*;
import javafx.util.Duration;
import javafx.geometry.*;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.transform.Rotate;

public class Player extends Pane{
	Circle body;
	Circle eye;
	Gun gun;
	double angle;
	Pane Playground;
	int health, score;
	
	Player(Pane p){
		//setPrefSize(100,100);
		health = 1000;
		score = 0;
		Playground=p;
		body = new Circle(50);
		body.setFill(Color.BLACK);
		eye = new Circle(5);
		eye.setFill(Color.BLUE);
		eye.setLayoutY(getLayoutY()+40);
		//body.setCenterX(getPrefWidth()/2);
		//System.out.println(getPrefWidth() + " " + getPrefHeight());
		//body.setCenterY(getPrefHeight()/2);
		//setStyle("-fx-background-color: blue;");
		
		getChildren().addAll(body,eye);
		gun = new Gun();
		gun.setLayoutX(-50);
		getChildren().add(gun);
	}
	
	public int getHealth(){
		return health;
	}
	public int getScore(){
		return score;
	}
	public double getAngle(){
		return angle;
	}
	public Gun getGun(){
		return gun;
	}
	public double getLocX(){
		return getLayoutX();
	}
	public double getLocY(){
		return getLayoutY();
	}
	
	public void move(int x, int y){
		setLayoutX(getLayoutX()+x);
		setLayoutY(getLayoutY()+y);
		gun.setLocX(getLayoutX()+x+50);
		gun.setLocY(getLayoutY()+y);
	}

	public void rotate(double x, double y){
			
		angle=Math.atan2(getLayoutY()-y,getLayoutX()-x);
		double gunAngle=Math.atan2(getLayoutY()-y-Math.sin(angle+Math.PI/2)*50,getLayoutX()-x-Math.cos(angle+Math.PI/2)*50)-angle-Math.PI/2;
		
		getTransforms().clear();
		
		getTransforms().add(new Rotate(Math.toDegrees(angle)+90));
		gun.rotate(gunAngle);
	}
	
	public void reset(){
		gun.clearBullets();
	}
	
	public void collideWithMob(Mob m, Status s){
		Bounds b1 = m.getBody().localToScene(body.getBoundsInLocal());
		Bounds b2 = body.localToScene(body.getBoundsInLocal());
		//Bug: HITS THE PLAYER BEFORE ACTUALLY TOUCHING
		if(b1.intersects(b2)){
			health--;
			s.setHealthTxt(health);
			System.out.println(health);
			
		}
		
	}
	
	public void addToScore(int i){
		score += score;
	}
	
	
}