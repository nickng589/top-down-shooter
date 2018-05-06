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

public class TopDownShooter{
	Player player;
	BorderPane screen;
	public static Pane playground;
	Scene mainGame;
	Stage stage;
	boolean goNorth, goSouth, goEast, goWest, leftClick, rightClick;
	double mouseY, mouseX;
	Timeline mouseTimer, shootTimer, collision;
	boolean delayOff;
	Swarm mobs;
	UserInterface ui;
	ParticleEffects pe;
	VBox devTools;
	Status stats;
	Timeline mobMovement;
	
	private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0 ;
    private boolean arrayFilled = false ;
	
	TopDownShooter(Stage s,Button main){
		delayOff=true;
		stage=s;
		
		screen=new BorderPane();
		
		devTools = new VBox();
		devTools.setStyle("-fx-background-color: black");
		devTools.setPrefWidth(220);
		String cssLayout = "-fx-border-color: pink;\n" +
                   "-fx-border-insets: 5 ;\n" +
                   "-fx-border-width: 5;\n" +
                   "-fx-border-style: solid;\n"+
                   "-fx-background-color: black;";
		
		devTools.setStyle(cssLayout);
		devTools.setSpacing(15);
		
        Label label = new Label();
        AnimationTimer frameRateMeter = new AnimationTimer() {

            @Override
            public void handle(long now) {
                long oldFrameTime = frameTimes[frameTimeIndex] ;
                frameTimes[frameTimeIndex] = now ;
                frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length ;
                if (frameTimeIndex == 0) {
                    arrayFilled = true ;
                }
                if (arrayFilled) {
                    long elapsedNanos = now - oldFrameTime ;
                    long elapsedNanosPerFrame = elapsedNanos / frameTimes.length ;
                    double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame ;
                    label.setText(String.format("Current frame rate: %.3f", frameRate));
                }
            }
        };
        frameRateMeter.start();
		label.setTextFill(Color.web("#FFFFFF"));
		devTools.getChildren().add(label);
		
		Font f1 = Font.loadFont(getClass().getResourceAsStream("ARCADECLASSIC.ttf"),20);
		Text devTitle = new Text("Dev Tools");
		devTitle.setFont(f1);
		devTitle.setFill(Color.WHITE);
		devTools.getChildren().add(devTitle);

		playground = new Pane();
		playground.setPrefWidth(1000);
		playground.setPrefHeight(1000);
		screen.setCenter(playground);
		
		pe=new ParticleEffects(playground);
		
		ui=new UserInterface(playground,main);
		
		player = new Player(playground);
		playground.getChildren().addAll(player);
			
		screen.setRight(devTools);
		
		//health
		stats = new Status(playground,player);
		playground.getChildren().addAll(stats);
		
		mainGame = new Scene(screen);
		
		shootTimer= new Timeline(new KeyFrame(Duration.millis(100), ae -> player.getGun().shoot(player,mouseX,mouseY)));			
		shootTimer.setCycleCount(Animation.INDEFINITE);
					
		//checks if mob collides with bullet
		/*
		collision = new Timeline(new KeyFrame(Duration.millis(40), ae -> collisionChecker()));			
		collision.setCycleCount(Animation.INDEFINITE);
		collision.play();
		*/
		
		AnimationTimer collision= new AnimationTimer(){
			public void handle(long l){
				collisionChecker();
			}
		};
		collision.start();
		
		mainGame.setOnKeyPressed(new EventHandler<KeyEvent>() {
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
            }
        });
		
		mainGame.setOnKeyReleased(new EventHandler<KeyEvent>() {
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
                int dx = 0, dy = 0;
				
				if(player.getLayoutY()>0){
					if (goNorth) dy -= 2;
				}
				if(player.getLayoutY()<playground.getWidth()){
					if (goSouth) dy += 2;
				}
				if(player.getLayoutX()<playground.getHeight()){
					if (goEast)  dx += 2;
				}
				if(player.getLayoutX()>0){
					if (goWest)  dx -= 2;
				}

                player.move(dx, dy);
				player.rotate(mouseX,mouseY);
				//left mousekey is held down shoot, else stop
				if(leftClick){
					shoot();
				}
            }
        };
        timer.start();
		
		mainGame.setOnMouseMoved(new EventHandler<MouseEvent>() {
		  @Override public void handle(MouseEvent event) {
			mouseX = event.getX();
			mouseY = event.getY();
		  }
		});		
		
		//when holding down and moving mouse 
		mainGame.setOnMouseDragged(new EventHandler<MouseEvent>() {
		  @Override public void handle(MouseEvent event) {
			mouseY = event.getY();
			mouseX = event.getX();
		  }
		});
		
		//left click to shoot a bullet
		mainGame.setOnMousePressed(new EventHandler<MouseEvent>() {
			
		  @Override public void handle(MouseEvent event){
			if(event.isPrimaryButtonDown()){
				leftClick = true;
			}
			if(event.isSecondaryButtonDown()){
				rightClick = true;
			}
		  }
		});		
		
		mainGame.setOnMouseReleased(new EventHandler<MouseEvent>() {
			
		  @Override public void handle(MouseEvent event){
			if(!event.isPrimaryButtonDown()){
				leftClick = false;

			}
			if(!event.isSecondaryButtonDown()){
				rightClick = false;
			}
		  }
		});
		
		//create swarm and test it
		mobs = new Swarm(4);
		mobMovement = new Timeline(new KeyFrame(Duration.millis(20),ae -> mobs.swarmPlayer(player)));
		mobMovement.setCycleCount(Animation.INDEFINITE);
		Button spawnBtn = new Button();
		devTools.getChildren().add(spawnBtn);
		spawnBtn.setText("Spawn round");
		spawnBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				mobs.spawnSwarm(playground);				
				mobMovement.play();
			}
		});
		
		//test knockback
		Button knockBtn = new Button();
		devTools.getChildren().add(knockBtn);
		knockBtn.setLayoutX(100);
		knockBtn.setText("knockback");
		knockBtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				knockBackMobs();
			}
		});
	}
	
	private void knockBackMobs(){
		mobMovement.pause();
		mobs.knockbackMobsAnimated(player, 300);
		mobMovement.play();
	}
	
	public void play(){
		stage.setScene( mainGame );
		player.setLayoutX(playground.getWidth()/2);
		player.setLayoutY(playground.getHeight()/2);
	}
	

	private void shoot(){
		if(delayOff){
			player.getGun().shoot(player,mouseX,mouseY);
			delayOff=false;
			Timeline delay = new Timeline(new KeyFrame(Duration.millis(100),ae -> delayOff=true));
			delay.play();
		}
	}
	
	public void updateMouse(double x, double y){
		mouseX = x;
		mouseY = y;
	}
	public void collisionChecker(){
		//if there are mobs, check for each mob if they collided with bullet 
		if(mobs.getSwarm().size() > 0){
			for(int i = 0; i < mobs.getSwarm().size(); i++){
				if(player.collideWithMob(mobs.getSwarm(i), stats)){
					knockBackMobs();
				}
				//colldeWithBullet is in Mob class
				mobs.getSwarm(i).collideWithBullet(player);
				if(mobs.getSwarm(i).getHealth() < 0){
					pe.RectExplosion(mobs.getSwarm(i).getAbsoluteMiddleX(),mobs.getSwarm(i).getAbsoluteMiddleY());
					playground.getChildren().remove(mobs.getSwarm(i));
					mobs.getSwarm().remove(mobs.getSwarm(i));
				}
				//checks if player is getting hit by any MobProjectile
				if(mobs.getSwarm().get(i).getAttacks()){
					for(MobProjectile p : mobs.getSwarm().get(i).getProjectiles()){
						if(player.collideWithProjectile(p, stats)){
							//TODO: grant player temporary invincibility (instead of knocking back mobs)
						}
					}
				}
			}
		}
		
	
	}
	public void reset(){
		player.reset();
		mobs.resetSwarm();
	}
}
