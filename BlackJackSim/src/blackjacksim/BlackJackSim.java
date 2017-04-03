package blackjacksim;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class BlackJackSim {
    
    public static int getNextCard(int isUsed[]) {
        //We need a random gen
        Random randomGenerator = new Random();
        int card;
        do {
                card = 8 + randomGenerator.nextInt(52);
            } while (isUsed[card] != 0);
        isUsed[card] = 1;
        return card;
    }
    
    public static int getPlayerTotal(Player x) {
        int card, total = 0, numberOfAces = 0;
        for(int i = 0; i < x.getCardsNumber(); ++i) {
            card = x.getCard(i);
            card = card / 4;
            if(card <= 10)
                total += card;
            else if(card <= 13)
                total += 10;
            else {
                total += 11;
                numberOfAces++;
            }
        }
        while(total > 21 && numberOfAces > 0) {
            total -=10;
            numberOfAces--;
        }
        return total;
    }
    
    public static void main(String[] args) throws IOException {
        
        //to Asign the CardType
        HashMap<Integer, String> cardTypes = new HashMap<>();
        cardTypes.put(0, "Hearts");
        cardTypes.put(1, "Spades");
        cardTypes.put(2, "Clubs");
        cardTypes.put(3, "Diamonds");
        
        //to Asign the CardNumber/Value
        HashMap<Integer, String> cardNumber = new HashMap<>();
        for(int i = 2; i <= 10; ++i) {
            cardNumber.put(i, "" + i);
        }
        cardNumber.put(11, "J");
        cardNumber.put(12, "Q");
        cardNumber.put(13, "K");
        cardNumber.put(14, "A");
        
        //to Make each card unique
        int []isUsed = new int[60]; 
        for(int i = 0; i <= 59; i++)
            isUsed[i] = 0;
        
        //Let's create a player and a computer
        Player player = new Player();
        Player dealer = new Player();
        
        
        //Start the game
        System.out.println("Initial Draw");
        int card;
        String output;
        
        //Draw two cards for dealer
        card = getNextCard(isUsed);
        dealer.addCard(card);
        card = getNextCard(isUsed);
        dealer.addCard(card);
        
        output = "Dealer Draw: ";
        card = (int) dealer.getCard(0);
        output += cardNumber.get(card/4) + " ";
        output += cardTypes.get(card%4);
        output += ", Hidden";
        System.out.println(output);
        
        //Draw two cards for player
        for(int i = 0; i <= 1; ++i) {
            card = getNextCard(isUsed);
            player.addCard(card);
        }
        
        //Get the initial output for the player
        output = "Player Draw(1): ";
        for(int i = 0; i <= 1; ++i) {
            card = (int) player.getCard(i);
            output += cardNumber.get(card/4) + " ";
            output += cardTypes.get(card%4);
            if(i < 1)
                output += ", ";
        }
        output += ".";
        System.out.println(output);
        output = "Do you want to draw another card? (Y/N)";
        System.out.println(output);
        
        //Game flow
        int state = 0, round = 1;    
        do {                         
            char c = (char) System.in.read();
            char dummy = (char) System.in.read();
            //study all posibilities
            switch(c) {
                case 'N':
                    state = 1;
                    break;
                    
                case 'Y':
                    //we give player a new card
                    round++;
                    card = getNextCard(isUsed);
                    player.addCard(card);
                    output = "Player Draw(" + round + "): ";
                    output += cardNumber.get(card/4) + " ";
                    output += cardTypes.get(card%4) + ".";
                    System.out.println(output);
                    //we check the total
                    int playerTotal = getPlayerTotal(player);
                    //test the score
                    if(playerTotal > 21) {
                        output = "Player Hand values ";
                        output += playerTotal;
                        output += ".";
                        System.out.println(output);
                        System.out.println("Player lost!!!");
                        return;
                    }
                    output = "Do you want to draw another card? (Y/N)";
                    System.out.println(output);
                    break;
                    
                default:
                    System.out.println("Please, give a valid input: Y/N");
                    break;
            }
            
        } while(state == 0);
        
        
        //show dealer card + calculate the dealer score
        card = (int)dealer.getCard(1);
        output = "Dealer Hidden Card Was: " + cardNumber.get(card/4) +
                " " + cardTypes.get(card%4)+ ". ";
        int playerScore = getPlayerTotal(player);
        
        //Let's calculate the dealer score
        int dealerScore = getPlayerTotal(dealer);
        output += "Dealer hand is " + dealerScore + ".";
        System.out.println(output);
        
        //check if player and/or dealer has blackjack
        if(playerScore == 21)
            if(dealerScore == 21) {
                System.out.println("TIE, player and dealer have blackjack!");
                return;
            }
            else {
                System.out.println("Player has blackjack. Player won!!!");
                return;
            }
        
        round = 0;
        while(dealerScore < 17) {
            card = getNextCard(isUsed);
            dealer.addCard(card);
            dealerScore = getPlayerTotal(dealer);
            round++;
            output = "Dealer Draw(" + round + "):" + cardNumber.get(card / 4);
            output += " " + cardTypes.get(card % 4) + ".";
            output += " Dealer hand is " + dealerScore + ".";
            System.out.println(output);
        }
        System.out.println("Dealer stops!");
        //if dealer has over 21 he loose.
        
        if(dealerScore > 21) {
            System.out.println("Dealer lost!");
            return;
        }
        
        output = "Player score is " + playerScore + ".";
        output += " Dealer score is " + dealerScore + ".";
        System.out.println(output);
        
        //print the result
        if(playerScore > dealerScore)
            System.out.println("Player wins!!!");
        else if(dealerScore > playerScore) 
            System.out.println("Dealer wins!!!");
        else 
            System.out.println("It's a draw");
    }
}
