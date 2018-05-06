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
import javafx.geometry.Point2D;

public class ShotGun extends Gun{
//	double locX; 
//	double locY;
//	Rectangle body;
//	Rectangle tip;
//	ArrayList<Bullet> bullets= new ArrayList<Bullet>();
	
	//TODO: NEEDS TO CHECK FOR AMMO LEFT 
	ShotGun(){
		super();
		fireRate=.5;
		tip=new Rectangle(10,10);
		tip.setFill(Color.RED);
		body=new Rectangle(10,60);
		body.setFill(Color.GREEN);
		getChildren().addAll(body,tip);
		tip.setLayoutY(body.getHeight());
	}
	
	public void shoot(Player p, double mouseLocX, double mouseLocY){
		Bounds boundsInScene = tip.localToScene(tip.getBoundsInLocal());
		if(mouseLocX!=boundsInScene.getMinX()&&mouseLocY!=boundsInScene.getMinY()){
		//	double radius = 10;
		//	double sideA = mouseLocX - boundsInScene.getMinX()-radius/2;
		//	double sideB = mouseLocY - boundsInScene.getMinY()-radius/2;
		
			double sideA = mouseLocX - boundsInScene.getMinX();
			double sideB = mouseLocY - boundsInScene.getMinY();
			double sideC = Math.sqrt(Math.pow(sideA,2) + Math.pow(sideB,2));
			//TODO: BULLETS SHOULD SPAWN AND GO SLIGHTLY ANGLED TOWARDS A DIRECTION(3 BULLETS - 1 MIDDLE - 1 LOWER - 1 HIGHER) AND CURRENTLY DOESNT WORK
			ShotGunBullet b = new ShotGunBullet(sideA/sideC*7,sideB/sideC*7);
			ShotGunBullet b1 = new ShotGunBullet(sideA/sideC*7,sideB/sideC*7);
			ShotGunBullet b2 = new ShotGunBullet(sideA/sideC*7,sideB/sideC*7);
			
			b.setLocation(boundsInScene.getMinX()-b.getRadius()/2,boundsInScene.getMinY()-b.getRadius()/2);
			b1.setLocation(boundsInScene.getMinX()-b.getRadius()/2,boundsInScene.getMinY()-b.getRadius()/2);
			b2.setLocation(boundsInScene.getMinX()-b.getRadius()/2,boundsInScene.getMinY()-b.getRadius()/2);

			bullets.add(b);
			bullets.add(b1);
			bullets.add(b2);
		
			TopDownShooter.playground.getChildren().addAll(b,b1,b2);

		}
		
	}
	
}