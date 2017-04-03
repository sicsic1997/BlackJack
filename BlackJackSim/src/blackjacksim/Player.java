
package blackjacksim;

import java.util.ArrayList;

public class Player {
    private ArrayList cards = new ArrayList();
    
    public void addCard(int x) {
        cards.add(x);
    }
    
    public int getCard(int x) {
        return (int)cards.get(x);
    }
    
    public int getCardsNumber() {
        return cards.size();
    }
}
