import java.util.*;
import javafx.geometry.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
public class PistolBullet extends Bullet{
	
	public PistolBullet(double xS, double yS){
		super(xS, yS);
		speedMultiplier = .4;
		damage = 3;
		shell = new Circle(4);
		shell.setFill(Color.BLACK);
		getChildren().add(shell);
		setPrefSize(10,10);
		shell.setCenterX(10);
		shell.setCenterY(10);
		
		explosionTimer = null;
	}

	

}