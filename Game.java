

import ecs100.UI;

import java.util.*;

/**
 * This class is where we control all the functionality that has to do with
 * the game "Cluedo" itself. Cluedo and all it's rules and player options will
 * be controlled through this class.
 * <p>
 * The Game class will contain collections of all the deck and players in the game
 * <p>
 * Created by Jack on 19/07/2016.
 */
public class Game {

    private Board[][] board = new Board[27][27];

    private List<Card> deck = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    private Set<Card> solution = new HashSet<>(); // contains cards to win the game
    private List<Card> allCards;


    public Game() {
        createCards();
        createRooms();
        allCards = new ArrayList<>(deck);
        setSolution();

    }

    public void draw(){
        UI.clearGraphics();
        for (int i=0; i<26; i++){
            for (int j=0; j<26; j++){
                if (board[i][j] != null){
                    board[i][j].draw();
                }
            }
        }
    }

    /**
     * Here we set up the locations of the rooms on the board, we also
     * set the door locations. A room is a location on the board as well
     * as a Card
     */
    public void createRooms(){
        Room kitchen = new Room("Kitchen", 1, 1, 7, 4);
        Room ball = new Room("Ball Room", 10, 1, 7, 7);
        Room conservatory = new Room("Conservatory", 19, 1, 7, 4);
        Room lounge = new Room("Lounge", 1, 19, 5, 7);
        Room hall = new Room("Hall", 8, 17, 9, 9);
        Room study = new Room("Study", 19, 21, 7, 5);
        Room dining = new Room("Dining Room", 1, 7, 5, 9);
        Room library = new Room("Library", 20, 14, 6, 5);
        Room billiard = new Room("Billiard Room", 21, 6, 5, 6);

        // Set the door locations for each room
        kitchen.setDoorLocation(6,4);
        ball.setDoorLocation(11,7);
        conservatory.setDoorLocation(19,4);
        lounge.setDoorLocation(5,19);
        hall.setDoorLocation(12,17);
        study.setDoorLocation(19,21);
        dining.setDoorLocation(5,8);
        library.setDoorLocation(20,16);
        billiard.setDoorLocation(21,8);//20/8

        // Assign the location for each room to the board
        kitchen.setStartPosition(board);
        ball.setStartPosition(board);
        conservatory.setStartPosition(board);
        lounge.setStartPosition(board);
        hall.setStartPosition(board);
        study.setStartPosition(board);
        dining.setStartPosition(board);
        library.setStartPosition(board);
        billiard.setStartPosition(board);

        // add the rooms to the deck (as they are cards as well
        deck.add(kitchen);
        deck.add(ball);
        deck.add(conservatory);
        deck.add(lounge);
        deck.add(hall);
        deck.add(study);
        deck.add(dining);
        deck.add(library);
        deck.add(billiard);
    }

    /**
     * Here we set up our characters and weapons for our deck of playing
     * deck which our players will keep in their inventory
     */
    public void createCards(){
        // characters
        Character scarlett = new Character("Miss Scarlett");
        Character mustard = new Character("Colonel Mustard");
        Character green = new Character("Mr Green");
        Character peacock = new Character("Mrs Peacock");
        Character white = new Character("Mrs White");
        Character plum = new Character("Professor Plum");

        // weapons
        Weapon rope = new Weapon("Rope");
        Weapon dagger = new Weapon("Dagger");
        Weapon candle = new Weapon("Candlestick");
        Weapon pipe = new Weapon("Lead Pipe");
        Weapon spanner = new Weapon("Spanner");
        Weapon gun = new Weapon("Revolver");

        // add the cards to the deck
        deck.add(scarlett);
        deck.add(mustard);
        deck.add(green);
        deck.add(peacock);
        deck.add(white);
        deck.add(plum);
        deck.add(rope);
        deck.add(dagger);
        deck.add(candle);
        deck.add(pipe);
        deck.add(spanner);
        deck.add(gun);
    }

    /**
     * Here we set up our player with their initial position on where they will be
     * starting on the board, each player is assigned a character in which they will
     * play throughout the game. This does not necessarily mean that they will have
     * that character card in their deck.
     *
     * @param name
     * @param token
     */
    public Player addPlayer(String name, Player.Token token){
        switch (token){
            case MissScarlett://1,17
                Player p1 = new Player(name,token,1,17);
                players.add(p1);
                return p1;
            case ProfessorPlum://6,25
                Player p2 = new Player(name, token,6,25);
                players.add(p2);
                return p2;
            case MrsWhite://25/19
                Player p3 = new Player(name,token,25,19);
                players.add(p3);
                return p3;
            case MrsPeacock:
                Player p4 = new Player(name,token,25,5);
                players.add(p4);
                return p4;
            case MrGreen:
                Player p5 = new Player(name,token,17,1);
                players.add(p5);
                return p5;
            case ColonelMustard:
                Player p6 = new Player(name,token,8,1);
                players.add(p6);
                return p6;
            default:
                throw new IllegalArgumentException("Player selection was abnormally exited");
        }
    }

    /**
     * Adds a list of players to the board
     */
    public void addPlayersToBoard(List<Player> players){
        for (Player p: players)
            p.setStartPosition(board);
    }

    /**
     * Get the room by string name;
     * @param roomName
     * @return
     */
    public Room getRoom(String roomName){
        for (Card c: allCards){
            if (c.getName().toLowerCase().contains(roomName.toLowerCase()) && c instanceof Room)
                return (Room)c;
        }
        throw new IllegalArgumentException("Room name is incorrect");
    }

    /**
     * Get the room the player is currently in
     * @param player
     * @return
     */
    public Room getRoom(Player player){
        if (player.getRoom() != null)
            return player.getRoom();
        throw new IllegalArgumentException("Player is not in a room");
    }

    /**
     * Checks if a door is adjacent to our Position then return
     * a door
     * @param pos
     * @return
     */
    public Door isDoor(Position pos){
        // check north
        if (board[pos.x][pos.y-1] instanceof Door)
            return (Door)board[pos.x][pos.y-1];
        // south
        if (board[pos.x][pos.y+1] instanceof Door)
            return (Door)board[pos.x][pos.y+1];
        // east
        if (board[pos.x+1][pos.y] instanceof Door)
            return (Door)board[pos.x+1][pos.y];
        // west
        if (board[pos.x-1][pos.y] instanceof Door)
            return (Door)board[pos.x-1][pos.y];
        return null;
    }

    /**
     * Retrieves the room by position of Door
     *
     * @param pos
     * @return
     */
    public Room getRoomByDoor(Position pos){
        if (board[pos.x][pos.y] instanceof Door) {
            Door door = (Door) board[pos.x][pos.y];
            return door.getRoom();
        }
        return null;
    }

    /**
     * Checks if current position is a Room
     * @return
     */
    public boolean isRoom(Position p){
        if (board[p.x][p.y] instanceof Room)
            return false;
        return true;
    }

    /**
     * Define our solution cards which are randomly chosen and stored in a set.
     * The solution contains 1 Character, 1 Weapon, and 1 Room
     */
    public void setSolution(){
        // probably not the most ideal way to do this
        Collections.shuffle(deck); //randomise our selection
        for (int i=0; i<deck.size(); i++)
            if (deck.get(i) instanceof Room) {
                solution.add(deck.get(i));
                deck.remove(i);
                break;
            }
        for (int i=0; i<deck.size(); i++)
            if (deck.get(i) instanceof Character) {
                solution.add(deck.get(i));
                deck.remove(i);
                break;
            }
        for (int i=0; i<deck.size(); i++)
            if (deck.get(i) instanceof Weapon) {
                solution.add(deck.get(i));
                deck.remove(i);
                break;
            }

    }

    /**
     * Here we attempt to move the player n amount of steps (according to the dice roll),
     * In the future we will implement this move function with more logic. We will attempt
     * to move the player in a A* fashion towards a room/door of the players choosing
     * @param player
     * @param nmoves
     * @param room - this is the destination we want to get to
     */
    public void movePlayer(Player player, int nmoves, Room room){

        /** notes:
         * because we want to move in a A* fashion towards a room, there will be no need
         * to record the direction in which we travel.
         * The user can specify a certain amount of steps they wish the player to move,
         * dependant on the dice roll.
         */

        // compute A*

        // current position of player
        Node start = new Node(null,player.getPos());
        Node end = new Node(null,room.getDoor().getPos());
        start.setGoal(end);
        end.setGoal(end);

        Node path = Astar(start,end);
        if (path == null) //break out and report an error
            throw new IllegalArgumentException("Path finder has failed");
        List<Position> pathToFollow = new ArrayList<>();
        addPath(pathToFollow, path);

        int i = pathToFollow.size()-1;
        while (nmoves != 0){ // this logic needs to be complex

            if (i < 0) break; //safe guard against out-of-bounds

            // Check if there is a door in our surrounding position
            // check if new position is a door
            Door door = isDoor(pathToFollow.get(i));
            if (door != null){
                Room r = door.getRoom();
                String inp = TextClient.inputString("Would you like to enter room "+r.getName()+": yes/no?");
                if (inp.contains("y")){
                    // put the player in the room and update position
                    player.enterRoom(r);
                    player.move(board,pathToFollow.get(i));
                    draw();
                    return;
                }else{ // player said no, so if room we want to travel to
                    // is equal to the room we are at, we end our turn
                    if (room.equals(r)) {
                        player.move(board, pathToFollow.get(i));
                        draw();
                        return;
                    }//else carry on with the program
                }
            }

            // move the player
            player.move(board,pathToFollow.get(i));

            UI.sleep(100);
            draw();
            i--;
            nmoves--;
        }
    }

    /**
     * Helper method for adding our shortest path to the list in
     * which we will use to move our player
     */
    public void addPath(List<Position> positions, Node path){
        while (path.parent != null){
            positions.add(new Position(path.getPos().x,path.getPos().y));
            path = path.parent;
        }
    }

    /**
     * Algorithm for Astar search which will return a node containing
     * the shortest path to our destination. Should never return null.
     * @param start
     * @param end
     * @return
     */
    public Node Astar(Node start, Node end){
        Queue<Node> order = new PriorityQueue<>();
        Set<Node> visited = new HashSet<>();

        order.add(start); // add our start node to the queue

        while (!order.isEmpty()){
            // do Astar LOGIC
            Node n = order.poll();
            visited.add(n);
            if (n.equals(end)) return n; // found our path
            else {
                for (Position p: n.getPos().getNeighbours()){
                    Node neighbour = new Node(n,p);
                    neighbour.setGoal(end);
                    if (!visited.contains(neighbour) && isRoom(p))
                        order.add(neighbour);
                }
            }
        }
        return null; // we haven't found a path
                    // we should never get here but we will account for it
    }



    /**
     * Return the specified card using a string
     * @param name
     * @return
     */
    public Card getCard(String name){
        for (Card c: allCards){
            if (c.getName().equals(name))
                return c;
        }
        return null; // does not contain the card
    }

    /**
     * Each player is dealt a set of cards. Each card dealt to a player
     * will come from a shuffled deck
     */
    public void dealCards(List<Player> players){
        while (!deck.isEmpty())
            for (Player p : players) {
                if (deck.isEmpty()) break; // this ensures our random doesn't throw an illegalArg.
                Card c = randomCard();
                p.addCardToInventory(c);
                // removes a card from the deck, ensuring that we cannot select the same card again
                deck.remove(c);

                // Todo: remove this code, it is for debugging
                //System.out.println("Card removed = " + c.toString()+" belongs to "+p.toString());
            }
    }

    /**
     * Helper method for getting a random card from the deck
     * @return
     */
    public Card randomCard(){
        int rnd = new Random().nextInt(deck.size());
        return deck.get(rnd);
    }

    /**
     * A method used for testing, Prints all the cards that are
     * currently in the deck. However once the game has commenced, there
     * should not be a card left in the deck
     */
    public void printCards(){
        for (Card c: deck)
            System.out.println(c.toString());
    }

    public String toString(){
        return String.format(board.toString());
    }

}
