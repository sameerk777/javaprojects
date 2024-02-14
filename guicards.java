// name is sameer kulkarni, student id 20477716
package application;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;

public class Foothill extends Application {
       public static void main(String[] args) 
       {
          launch(args);
       }
       // two members for this class
       int[] fourCardValue = new int[4];
       int[] expressionNumbers = new int[4];
       // displays four cards on the screen, called when program is run and every time it gets shuffled
       private void displayFourCards(Queue<Card> cardQueue, FlowPane playedPane) {
         playedPane.getChildren().clear();
         Iterator<Card> queueIterator = cardQueue.iterator();
         if(!queueIterator.hasNext()) {
             playedPane.getChildren().add(new Label("Game over, ran out of cards"));
         }
         // moves through the queue and gets the value as an int
         for(int i = 0; i < 4 && queueIterator.hasNext(); i++) {
                 Card thisCard = queueIterator.next();   
                 fourCardValue[i] = GUICard.valueAsInt(thisCard)+1;
                 queueIterator.remove();
                 Image cardImage = GUICard.getImage(thisCard);
                 ImageView cardImageView = new ImageView(cardImage);
                 playedPane.getChildren().add(cardImageView);
             }
         }
       // checks if expression matches the card numbers
       boolean isMatching() {
           Arrays.sort(fourCardValue);
           Arrays.sort(expressionNumbers);
           int [] sortedExpressionNumbers = new int[4];
           int [] fourCardValueSorted = new int[4];
           System.arraycopy(expressionNumbers, 0, sortedExpressionNumbers,
                   0, expressionNumbers.length);
           System.arraycopy(fourCardValue, 0, fourCardValueSorted, 0, 
                   fourCardValue.length);
           if(expressionNumbers.length != fourCardValue.length) {
               return false;
           }
           // error check if the user inputs more or less than 4 operands
           for(int i = 0; i < expressionNumbers.length; i++) {
               if (sortedExpressionNumbers[i] != fourCardValueSorted[i]) { 
                   return false;
               }
           }
           return true;
       }
       // checks if user entered valid numbers
       boolean isValid(String expression) {
           if (expression.matches("^[ 0-9+/*()-]+$")){
               return true;
           }
           return false;           
       }
       // splits the numbers in the expression based on operators and converts to integers
       public boolean expressionCheck(String expression) {        
           String[] arrayOfStrings = expression.split("[+-/*() ]+");
           if (arrayOfStrings[0].equals("")) {
               arrayOfStrings = Arrays.copyOfRange(arrayOfStrings, 1, arrayOfStrings.length);
           }
           if (arrayOfStrings.length != expressionNumbers.length) {
               return false;
           }
           for(int i = 0; i < fourCardValue.length; i++) {
               String a = arrayOfStrings[i];          
               expressionNumbers[i] = Integer.parseInt(a);
           }
           return true;
       }
       public static String infixToPostfix(String expression) {
           // create stacks for operators and operands
           Stack<Character> operatorStack = new Stack<>();
           Stack<String> operandStack = new Stack<>();

           // loop through the expression character by character
           for (int i = 0; i < expression.length(); i++) {
               char c = expression.charAt(i);
               if (Character.isDigit(c)) {
                   // if the current character is a digit, get the entire 
                   // number and push it onto the operand stack
                   int j = i;
                   while (j < expression.length() && Character.isDigit
                           (expression.charAt(j))) {
                       j++;
                   }
                   operandStack.push(expression.substring(i, j));
                   i = j - 1;
               } 
               else if (isOperator(c)) {
                   // If the current character is an operator
                   while (!operatorStack.empty() && hasHigherPrecedence(
                           operatorStack.peek(), c)) {
                       // Pop operators from the operator stack and push them
                       // onto the operand stack
                       operandStack.push(operatorStack.pop().toString());
                   }
                   operatorStack.push(c);
               } 
               else if (c == '(') {
                   // if the current character is a left parenthesis, push it
                   // onto the operator stack
                   operatorStack.push(c);
               }
               else if (c == ')') {
                   // if the current character is a right parenthesis
                   while (!operatorStack.empty() && operatorStack.peek() != '(') {
                       // pop operators from the operator stack and push them 
                       // onto the operand stack until a left parenthesis occurs
                       operandStack.push(operatorStack.pop().toString());
                   }
                   operatorStack.pop(); // discard the left parenthesis
               }
           }

           while (!operatorStack.empty()) {
               // pop any remaining operators from the operator stack and push
               // them onto the operand stack
               operandStack.push(operatorStack.pop().toString());
           }

           // the expression been processed, the operand stack has the postfix 
           // expression now
           return String.join(" ", operandStack);
       }
       // checks for operators
       private static boolean isOperator(char c) {
           return c == '+' || c == '-' || c == '*' || c == '/';
       }
       // looks at precedence and determines which has higher precedence
       private static boolean hasHigherPrecedence(char op1, char op2) {
           if (op1 == '(' || op2 == ')') {
               // parentheses have the highest precedence
               return false;
           } 
           else if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'))
           {
               // multiplication and division have higher precedence than
               // addition and subtraction, pemdas
               return true;
           } 
           else {
               return false;
           }
       }
       // evaluates the postfix expression found in the last method
       public static double evaluatePostfixExpression(String postfix) throws
       IllegalArgumentException {
           Stack<Double> stack = new Stack<>();
           try {
               for (int i = 0; i < postfix.length(); i++) {
                   char c = postfix.charAt(i);
                   // skip whitespace
                   if (Character.isWhitespace(c)) {
                       continue; 
                   } 
                   else if (Character.isDigit(c)) {
                       double num = 0;
                       while (Character.isDigit(c)) {
                           num = num * 10 + (c - '0');
                           i++;
                           if (i >= postfix.length()) break;
                           c = postfix.charAt(i);
                       }
                       stack.push(num);
                   } 
                   
                   else if (c == '+' || c == '-' || c == '*' || c == '/') {
                       double num2 = stack.pop();
                       double num1 = stack.pop();
                       switch (c) {
                           case '+':
                               stack.push(num1 + num2);
                               break;
                           case '-':
                               stack.push(num1 - num2);
                               break;
                           case '*':
                               stack.push(num1 * num2);
                               break;
                           case '/':
                               stack.push(num1 / num2);
                               break;
                           default:
                               throw new IllegalArgumentException("Invalid "
                                       + "operator: " + c);
                       }
                   } 
                   else {
                       throw new IllegalArgumentException("Invalid character: "
                               + "" + c);
                   }
               }
               if (stack.size() != 1) {
                   throw new IllegalArgumentException("Invalid expression");
               }
               return stack.pop();
           } 
           // checks if the stack is empty before starting
           catch (EmptyStackException e) {
               throw new IllegalArgumentException("Invalid expression");
           }
       }

       public void start(Stage primaryStage)
       {
           // queue which holds our card objects
           java.util.Queue<Card> cardQueue = new java.util.LinkedList<>();
           Deck newDeck = new Deck();
           newDeck.shuffle();
           Card newCard;
           // if theres no more cards left, leave the loop and stop adding 
           // to the queue
           while (true) {
               if ((newCard = newDeck.dealCard()) == null) 
                   break;
               cardQueue.offer(newCard);
           }
           // create the scene and put it in the stage
          BorderPane pane = new BorderPane();    
          Scene scene = new Scene(pane, 800, 600);
          primaryStage.setTitle("Card Table");
          primaryStage.setScene(scene);
          
          // creating hbox for human
          HBox humanPane = new HBox(15);
          humanPane.setPadding(new Insets(15,50,15,50));
          // creating hbox for computer
          HBox computerPane = new HBox(15);
          computerPane.setPadding(new Insets(15,100,15,100));
          // creating the central played pane
          FlowPane playedPane = new FlowPane(150,15);
          // setting padding, alignment and style for central pane
          playedPane.setPadding(new Insets(100, 200, 100, 200));
          playedPane.setAlignment(Pos.CENTER);
          playedPane.setStyle("-fx-border-color: blue");
          // creating shuffle button and adding to pane
          Button shuffleButton = new Button("Shuffle");
          computerPane.getChildren().add(shuffleButton);
          // adding label and text field and buttons to the pane
          Label baseLabel = new Label("");
          computerPane.getChildren().add(baseLabel);
          TextField textField = new TextField();
          Button verifyButton = new Button("Verify");
          humanPane.getChildren().addAll(textField, verifyButton);
          // event handler for the verify button
          verifyButton.setOnAction(e -> {
              String theText = textField.getText();
              // if there is an invalid character, change label
              if (isValid(theText) == false) {
                  baseLabel.setText("Invalid character entered");
                  return;
              }
              // if there is the wrong amount of numbers in the expression, 
              // change the label
              if (!expressionCheck(theText)) {
                  baseLabel.setText("Use four numbers");
                  return;
              }
              // if the numbers dont match the card values, use this label
              if(isMatching() == false) {
                 // computerPane.getChildren().clear();
                  baseLabel.setText("Numbers don't match card values");
                  return;
              }
              // after hitting verify, put the expression into postfix form and
              // evaluate
              String postfix = infixToPostfix(theText);
              double result = evaluatePostfixExpression(postfix);
              // checking results of the expression
              if (result == 24) {
                  baseLabel.setText("Correct!");
              }
              else baseLabel.setText("Incorrect, does not equal 24");
          });
          // event handler for shuffle button
          shuffleButton.setOnAction(e -> {      
              baseLabel.setText("");
              textField.clear();
              displayFourCards(cardQueue, playedPane);
          });
          // add the computer played and human pane to the main stage
          displayFourCards(cardQueue, playedPane);
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
    private static Image[][] imageCards = new Image[4][13];   
    private static ImageView[][] imageCardViews = new ImageView[4][13];
    private static Image imageBack;
    private static ImageView imageCardBack;
    private static boolean imagesLoaded = false; 
    // these strings and this static array assist with conversions 
    private static String cardValsConvertAssist = "A23456789TJQK";
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
    public static int valueAsInt(Card card) {
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
class Deck{
    // all constants and members used in this class
    private static final int MAX_PACKS = 6;
    private static final int NUM_CARDS_PER_PACK = 52;
    private static Card[] masterPack = new Card[NUM_CARDS_PER_PACK];
    private int numPacks;
    private int topCard; 
    private Card[] cards;
    // assigns masterpack to cards elements, populates arrays, and initialize
    public Deck(int numberOfPacks) {
        allocateMasterPack();
        cards = new Card[NUM_CARDS_PER_PACK * numberOfPacks];
        boolean packInitializer = initializePack(numberOfPacks);
        if(packInitializer == false) {
            numPacks = 1;
            topCard = (numPacks * NUM_CARDS_PER_PACK) - 1;
            initializePack(numPacks);
        }
    }
    // default constructor, sets everything to 1
    public Deck() {
        this(1);
    }
    // repopulates the cards array with 52 * numPacks cards
    public boolean initializePack(int numberOfPacks) {
        if(numberOfPacks < 1 || numberOfPacks > MAX_PACKS) {
            return false;
        }
        int indexNumber = 0;
        for(int packNumber = 0; packNumber < numberOfPacks; packNumber++) {
            for(int i = 0; i < NUM_CARDS_PER_PACK; i++) {
                cards[indexNumber] = masterPack[i];
                indexNumber++;
            }
        }
        numPacks = numberOfPacks;
        topCard = (numPacks * NUM_CARDS_PER_PACK) - 1;
                return true;
    }
    // shuffles the deck of cards 
    public void shuffle() {
        Random card = new Random();
        for(int i = 0; i < cards.length; i++) {
            int theRandomIndex = card.nextInt(cards.length);
            Card tempCard = cards[i];
            cards[i] = cards[theRandomIndex];
            cards[theRandomIndex] = tempCard;
        }
    }
    // deals a card to an available hand object and returns copies of cards
    public Card dealCard() {
        if (topCard >= 0) {      
            Card theTop = cards[topCard];
            char cardOneValue = theTop.getValue();
            CardIdentity.Suit cardOneSuit = theTop.getSuit();
            Card cardOne = new Card(cardOneValue, cardOneSuit);
            topCard--;
            return cardOne;
            
        }
        else return null;
    }
    // accessor for top card
    public int getTopCard(){
        return topCard;
    }
    // this method holds and allocates the master pack to deck objects
    private static void allocateMasterPack() {
        CardIdentity.Suit cardSuit[] = {CardIdentity.Suit.Spades, 
                CardIdentity.Suit.Clubs, CardIdentity.Suit.Hearts, 
                CardIdentity.Suit.Diamonds};
        char cardValues[] = {'A', '2', '3','4','5','6','7','8','9','T','J','Q',
                'K'};
        int theIndex = 0;
        boolean haveIBeenRunBefore = false;
        if(haveIBeenRunBefore == true) 
            return;
        for (int theSuit = 0; theSuit < cardSuit.length; theSuit++) {
            for(int theValue = 0; theValue < cardValues.length; theValue++) {
                Card masterCard = new Card(cardValues[theValue], 
                        cardSuit[theSuit]);
                masterPack[theIndex] = masterCard;
                theIndex++;   
            }
        }
    }
    // returns a copy of each card, returns a bad card as well if parameters bad
    public Card inspectCard(int k) {
        try {           
            Card cardArray = cards[k];
            char cardValue = cardArray.getValue();
            CardIdentity.Suit cardSuit = cardArray.getSuit();
            Card cardCopy = new Card(cardValue, cardSuit);
            return cardCopy;
        }
        catch(IndexOutOfBoundsException ex) {
            Card badCard = new Card('P', CardIdentity.Suit.Hearts);
            System.out.println(badCard.getCardError());
            return badCard;
            
        }
    }
}
