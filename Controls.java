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
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;

public class Controls{
	boolean delayOff;
	boolean delayOffBlink;
	boolean goNorth, goSouth, goEast, goWest, leftClick, rightClick;
	double mouseY, mouseX;
	Scene mainGame;
	Player player;
	Pane playground;
	boolean isPaused;
	UserInterface ui;
	
	public Controls(Scene mg, Player p, Pane pg,UserInterface userint){
		delayOff=true;
		delayOffBlink=true;
		isPaused=false;
		
		mainGame = mg;
		
		ui=userint;
		
		player = p;
		
		playground = pg;
					
		mg.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:    goNorth = true;
						break;
                    case S:  goSouth = true; 
						break;
                    case A:  goWest  = true; 
						break;
                    case D: goEast  = true; 
						break;
                }
				//keys 1, 2, 3, 4, 5 changes weapons
				//TODO: FINISH MORE GUNS TO ASSIGN FOR DIGIT CLICKED
				if(!isPaused){
					if(event.getCode()==KeyCode.DIGIT1||event.getCode()==KeyCode.DIGIT2||event.getCode()==KeyCode.DIGIT3||event.getCode()==KeyCode.DIGIT4||event.getCode()==KeyCode.DIGIT5||event.getCode()==KeyCode.DIGIT6){
						player.changeGun(Integer.parseInt(event.getText())-1,ui);
					}
				}
            }
        });
		
		mg.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case W:    goNorth = false; 
						break;
                    case S:  goSouth = false; 
						break;
                    case A:  goWest  = false; 
						break;
                    case D: goEast  = false; 
						break;
                }
            }
        });
		
		AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
				if(!isPaused){
					int dx = 0, dy = 0;
					
					if(player.getLayoutY()-player.getBody().getRadius()>0){
						if (goNorth) dy -= player.getSpeed();
					}
					if(player.getLayoutY()+player.getBody().getRadius()<playground.getWidth()){
						if (goSouth) dy += player.getSpeed();
					}
					if(player.getLayoutX()+player.getBody().getRadius()<playground.getHeight()){
						if (goEast)  dx += player.getSpeed();
					}
					if(player.getLayoutX()-player.getBody().getRadius()>0){
						if (goWest)  dx -= player.getSpeed();
					}

					player.move(dx, dy);
					player.rotate(mouseX,mouseY);
					//left mousekey is held down shoot, else stop
					if(leftClick){
						shoot();
					}
				}
            }
        };
        timer.start();
		
		mg.setOnMouseMoved(new EventHandler<MouseEvent>() {
		  @Override public void handle(MouseEvent event) {
			mouseX = event.getX();
			mouseY = event.getY();
		  }
		});		
		
		//when holding down and moving mouse 
		mg.setOnMouseDragged(new EventHandler<MouseEvent>() {
		  @Override public void handle(MouseEvent event) {
			mouseY = event.getY();
			mouseX = event.getX();
		  }
		});
		
		//left click to shoot a bullet
		mg.setOnMousePressed(new EventHandler<MouseEvent>() {
			
		  @Override public void handle(MouseEvent event){
			if(event.isPrimaryButtonDown()){
				leftClick = true;
			}
			if(event.isSecondaryButtonDown()){
				rightClick = true;
				player.blink(mouseX,mouseY);
			}
		  }
		});		
		
		mg.setOnMouseReleased(new EventHandler<MouseEvent>() {
			
		  @Override public void handle(MouseEvent event){
			if(!event.isPrimaryButtonDown()){
				leftClick = false;

			}
			if(!event.isSecondaryButtonDown()){
				rightClick = false;
			}
		  }
		});
		
		
	}
		
	public void updateMouse(double x, double y){
		mouseX = x;
		mouseY = y;
	}
	
	private void shoot(){
		if(delayOff){
			player.getGun().shoot(player, mouseX, mouseY);
			ui.getAmmoCounter().setAmmoNum(player.getGun().getAmmo());
			delayOff=false;
			Timeline delay = new Timeline(new KeyFrame(Duration.millis(player.getGun().getFireRate()*1000),ae -> delayOff=true));
			delay.play();
		}
	}
	/*
	private void blink(){
		if(delayOffBlink){
			double sideA = mouseX - player.getLayoutX();
			double sideB = mouseY - player.getLayoutY();
			double sideC = Math.sqrt(Math.pow(sideA,2) + Math.pow(sideB,2));
			player.setLayoutX(player.getLayoutX()+sideA/sideC*100);
			player.setLayoutY(player.getLayoutY()+sideB/sideC*100);
			delayOffBlink=false;
			Timeline delay = new Timeline(new KeyFrame(Duration.seconds(1.5),ae -> delayOffBlink=true));
			delay.play();
		}
	}
	*/
	public void pause(){
		isPaused=true;
	}
	
	public void play(){
		isPaused=false;
	}

}