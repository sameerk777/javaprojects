// name is sameer kulkarni, student id 20477716
// second submission is because I forgot the label
package application;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;

    public class Foothill extends Application 
    {
       final int NUM_CARDS_PER_HAND = 7;
       final int NUM_PLAYERS = 2;
       Image[] humanImages = new Image[NUM_CARDS_PER_HAND];
       ImageView[] humanViews = new ImageView[NUM_CARDS_PER_HAND];
       Image[] computerImages = new Image[NUM_CARDS_PER_HAND];
       ImageView[] computerViews = new ImageView[NUM_CARDS_PER_HAND];
       Image[] playedImages = new Image[NUM_CARDS_PER_HAND];
       ImageView[] playedViews = new ImageView[NUM_CARDS_PER_HAND];
       Label[] playLabelText = new Label[NUM_PLAYERS];
       static String cardlValsConvertAssist = "23456789TJQKAX";
       static String suitValsConvertAssist = "CDHS";
       
       public static void main(String[] args) 
       {
          launch(args);
       }
       
       public void start(Stage primaryStage)
       {
          // Create the scene and place it in the stage
          BorderPane pane = new BorderPane();    
          Scene scene = new Scene(pane, 800, 600);
          primaryStage.setTitle("Card Table");
          primaryStage.setScene(scene);
          
          // creating hbox for human
          HBox humanPane = new HBox(15);
          humanPane.setPadding(new Insets(15,100,15,100));
          // creating hbox for computer
          HBox computerPane = new HBox(15);
          computerPane.setPadding(new Insets(15,100,15,100));
          // creating the central played pane
          FlowPane playedPane = new FlowPane(150,15);
          // setting padding, alignment and style for central pane
          playedPane.setPadding(new Insets(100, 200, 100, 200));
          playedPane.setAlignment(Pos.CENTER);
          playedPane.setStyle("-fx-border-color: blue");
          
          // creating the computers cards
          for (int i = 0; i < NUM_CARDS_PER_HAND; i++) {
              computerImages[i] = new Image("file:images/BK.gif");
              computerViews[i] = new ImageView(computerImages[i]);
              computerPane.getChildren().add(computerViews[i]);
          }
          // creating humans cards
          for (int i = 0; i < NUM_CARDS_PER_HAND; i++) {
              Card card = generateRandomCard();
              humanImages[i] = GUICard.getImage(card);
              humanViews[i] = new ImageView(humanImages[i]);
              humanPane.getChildren().add(humanViews[i]);

          }
          // adding two cards to the center position of the playing pane
          Card card = generateRandomCard();
          Image newImage = GUICard.getImage(card);
          ImageView imageView = new ImageView(newImage);
          playedPane.getChildren().add(imageView);
          
          Card cardTwo = generateRandomCard();
          Image newImageTwo = GUICard.getImage(cardTwo);
          ImageView imageViewTwo = new ImageView(newImageTwo);
          playedPane.getChildren().add(imageViewTwo);
          Label computerLabel = new Label("computer");
          playedPane.getChildren().add(computerLabel);
          Label humanLabel = new Label("you");
          playedPane.getChildren().add(humanLabel);
          // add the computer played and human pane to the main stage
          pane.setTop(computerPane);
          pane.setCenter(playedPane);  
          pane.setBottom(humanPane); 
          primaryStage.show();
       }
       // method using math.random to generate a random card
       public static Card generateRandomCard() {
           int suit = (int)(Math.random() * 3);
           int value = (int)(Math.random() * 13 + 1);
           return new Card(GUICard.turnIntIntoCardValueChar(value), 
                   GUICard.turnIntIntoSuit(suit));
       }
}
class GUICard{
    // 14 = A thru K (+ joker)
    private static Image[][] imageCards = new Image[4][14];   
    private static ImageView[][] imageCardViews = new ImageView[4][14];
    private static Image imageBack;
    private static ImageView imageCardBack;
    private static boolean imagesLoaded = false; 
    // these strings and this static array assist with conversions 
    private static String cardValsConvertAssist = "23456789TJQKAX";
    private static String suitValsConvertAssist  = "CDHS";
    private static Card.Suit suitConvertAssist[] = 
    {
       Card.Suit.Clubs,
       Card.Suit.Diamonds,
       Card.Suit.Hearts,
       Card.Suit.Spades
    };
    // loads card images 
    static void loadCardImages() {
        int suitIndex;
        int valueIndex;
        String fileName; 
    // if this method has been called before, we prevent it from loading twice
        if(imagesLoaded == true) {
            return;
        }
        else {
            // use these for loops to travel through the array
            for(suitIndex = 0; suitIndex < suitValsConvertAssist.length();
                    suitIndex++) {
                for(valueIndex = 0; valueIndex < 
                        cardValsConvertAssist.length(); valueIndex++) {
                    // call these methods to get chars from the indexes
                    char suit = turnIntIntoCardSuitChar(suitIndex);
                    char value = turnIntIntoCardValueChar(valueIndex);
                    // set up the file structure and fill it up with all cards
                    fileName = "file:images/" + value + suit +".gif";
                    imageCards[suitIndex][valueIndex] = new Image(fileName);
                    imageCardViews[suitIndex][valueIndex] = new ImageView
                            (imageCards[suitIndex][valueIndex]);
                }
            }
            imagesLoaded = true;
        }
    }
    // converts an int into a card value like ace, 2,3,4 etc, returns char
    static char turnIntIntoCardValueChar(int k)
    {
    
       if ( k < 0 || k > 13)
          return '?'; 
       return cardValsConvertAssist.charAt(k);
    }
    
    // turns 0 - 3 into 'C', 'D', 'H', 'S'
    public static char turnIntIntoCardSuitChar(int k)
    {
       if ( k < 0 || k > 3)
          return '?'; 
       return suitValsConvertAssist.charAt(k);
    }
    // retrieves a specific image from loadcardimages()
    static public Image getImage(Card card)
    {
       loadCardImages(); // will not load twice, so no worries.
       return imageCards[suitAsInt(card)][valueAsInt(card)];
    }
    // basically converts the char value to an int 
    static int valueAsInt(Card card) {
        int k;
        char theCard;
        for(k = 0; k < cardValsConvertAssist.length(); k++) {
            theCard = cardValsConvertAssist.charAt(k);
            if (theCard == card.getValue()) {
                return k;
            }
        }
        return -1;
    }
    // same thing as above method, except for suit
    static int suitAsInt(Card card) {
        int j;
        int theSuit = 0; 
        for(j = 0; j < suitConvertAssist.length; j++) {
            if(card.getSuit() == suitConvertAssist[j]) {
                return j;
            }
        }
        return -1;
            
    }
    // returns the back of card image 
    static public Image getBackCardImage() {
        String theFile = "file:images/BK.gif";
        imageBack = new Image(theFile);
        imageCardBack = new ImageView(imageBack);
        return imageBack;
    }
    // turns an int value into the suit, super easy method
    static Card.Suit turnIntIntoSuit(int k){
        return suitConvertAssist[k];
    }
}


class CardIdentity{
    // defining private members
    private char value;
    private Suit suit;
    // defining the suit enumeration
    public enum Suit{
        Clubs, Diamonds, Hearts, Spades
    }
    // sets default card values to the ace of spades
    CardIdentity(){
        value = 'A';
        suit = Suit.Spades;
    }
    // mutator for both members, uses isvalid method as parameter
    boolean set(char value, Suit suit) {
        if(isValid(value, suit)) {
            this.value = value;
            this.suit = suit;
            return true; 
        }
        else {
            return false;
        }
    }
    // this method checks if the card is valid, if it has the right values
    private static boolean isValid(char value, Suit suit){
        if(value == 'A' || value == 'K' || value == 'Q' || value == 'J'
                || value == 'T' || (value >= '2' && value <= '9')) {
            return true;
        }
        else {
            return false;
            
        }
    }
    // accessors for the suit as well as the value of the card
    protected Suit getSuit() {
        return suit;
    }
    protected char getValue(){
        return value;
    }
    
}

// subclass of cardidentity superclass
class Card extends CardIdentity {
    // instantiating private member
    private boolean cardError;
    // default constructor for card; uses superclass constructor
    public Card(){
        super();
        cardError = false;
    }
    // card constructor that takes value and suit, calls superclass constructor
    public Card(char value, Suit suit){
        super();
        if(!set(value, suit)) {
            super.set('A', Suit.Spades);           
            cardError = true;
        }
        else cardError = false;
    }
    // mutator for the carderror member and overrides cardidentity mutator
    boolean set(char value, Suit suit) {
        if(super.set(value, suit)) {
            cardError = false; 
            return true;
        }
        else {
            cardError = true;
            return false;
        }
    }
    // returns a string version of card, if it is a bad card, return invalid
    public String toString() {
        if(cardError) {
            return "Invalid card";
        }
        else {
            return getValue() + " of " + getSuit();
        }
    }
    // accessor for carderror
    boolean getCardError(){
        return cardError;
    }
    // if all of the members are identical, return true
    boolean equals(Card card) {
        if(this.getValue() == card.getValue() && this.getSuit() == card.getSuit() 
                && this.getCardError() == card.getCardError()) {
            return true;
        }
        else return false;
    }
}
