import java.util.*;
import javafx.geometry.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import javafx.animation.*;
import javafx.scene.transform.Rotate;

public class Boss extends Mob implements TrueBounds{
	
	//fields
	boolean spawned;
	UserInterface ui;
	
	//constructor
	public Boss(UserInterface i){
		ui=i;
		spawned = false;
		knockback = false;
		attacks = true;
		shooting = false;
		double speedModifier = 0;
		isBoss = true;
	}
	
	//methods
	public void spawn(Player p){
		spawned = true;
		knockbackPlayer(p);
	}
	
	public void knockbackPlayer(Player p){
		p.animatedKnockback(getLayoutX() + getBodyWidth()/2, getLayoutY() + getBodyHeight()/2, 100);
	}
	
	public double getBodyHeight(){
		return 0;
	}
	
	public double getBodyWidth(){
		return 0;
	}
	
	//need a separate method, to subtract hp from the healthbar
	public void collideWithBullet(Player p){ //NOTE: now takes a player instead of gun
		Gun g = p.getGun();
		ArrayList<Bullet> b = g.getBullets();
		if(b.size() > 0){
			Bounds b1 = body.localToScene(body.getBoundsInLocal());
			for(int i = b.size()-1; i >= 0; i--){
				Bounds b2 = g.getBullets().get(i).localToScene(g.getBullets().get(i).getBoundsInLocal());
				if(b1.intersects(b2)){
					if(knockback){
						double temp = speedModifier;
						speedModifier = 0;
						animatedKnockback(p.getLocX(), p.getLocY(), b.get(i).getKnockBack()); //last number should eventually be p.getGun().getKnockback()
						speedModifier = temp;
					}
					health = health - b.get(i).getDamage();
					g.removeBullet(b.get(i));
					ui.setBossHP(health);
				}
			}
		}
	}
}