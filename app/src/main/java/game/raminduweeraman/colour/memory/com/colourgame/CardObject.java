package game.raminduweeraman.colour.memory.com.colourgame;

import android.widget.Button;
/**
 * Created by Ramindu.WEERAMAN on 3/1/2016.
 */

public class CardObject {

	public int x;
	public int y;
	public Button button;
	
	public CardObject(Button button, int x, int y) {
		this.x = x;
		this.y=y;
		this.button=button;
	}
	

}
