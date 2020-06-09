package rs.edu.raf.mtomic.paclike.agent.player;

import rs.edu.raf.mtomic.paclike.Direction;
import rs.edu.raf.mtomic.paclike.GameState;
import rs.edu.raf.mtomic.paclike.agent.genetic.Gene;

import java.util.ArrayList;

public class PlayerOne extends Player {

    private int moveIndex;
    private ArrayList<Integer> moves;
    private boolean test = true;

    private Runnable nextMove = this::goLeft;
    private Runnable lMove = this::goLeft;
    private int lastMove = 0;
    private Runnable lastRMove = null;
    private Gene gene;
    private int count = 0;

    public PlayerOne(GameState gameState, Gene gene) {
        super(gameState);
        this.gene = gene;
        this.moves = gene.getChromosome();
        this.moveIndex = 0;
    }



    @Override
    protected boolean checkAvailableCoords(int centerX, int centerY) {
        return super.checkAvailableCoords(centerX, centerY);
    }

//    @Override
//    protected Runnable generateNextMove() {
//
//        if((moveIndex == moves.size() || moveIndex == moves.size()+1) && test) {
//            test = false;
//            return this::goUp;
//        } else if((moveIndex == moves.size() || moveIndex == moves.size()+1) && !test){
//            test = true;
//            return this::goDown;
//        }
//
//        int bit1 = moves.get(moveIndex);
//        int bit2 = moves.get(moveIndex+1);
//
//        if(bit1 == 0 && bit2 == 0){
//            if(checkAvailableCoords(getSpriteTopX()+3,getSpriteTopY()+7)){
//                nextMove = this::goLeft;
//                lastMove = 0;
//            }else {
//                nextMove = getLastMove(lastMove);
//            }
//        }else if(bit1 == 0 && bit2 == 1){
//            if(checkAvailableCoords(getSpriteTopX()+11, getSpriteTopY())){
//                nextMove = this::goRight;
//                lastMove = 1;
//            }else {
//                nextMove = getLastMove(lastMove);
//            }
//        }else if(bit1 == 1 && bit2 == 0){
//            if(checkAvailableCoords(getSpriteTopX()+3, getSpriteTopY())){
//                nextMove = this::goUp;
//                lastMove = 2;
//            }else{
//                nextMove = getLastMove(lastMove);
//            }
//        }else if(bit1 == 1 && bit2 == 1){
//            if(checkAvailableCoords(getSpriteTopX()+7, getSpriteTopY()+11)){
//                nextMove = this::goDown;
//                lastMove = 3;
//            }else {
//                nextMove = getLastMove(lastMove);
//            }
//        }
//
//        lMove = nextMove;
//        moveIndex+=2;
//        return nextMove;
//    }

    @Override
    protected Runnable generateNextMove() {
//      We increase "moveIndex" every time generateNextMove() is called.
//      If "moveIndex" is greater then moves.size() (chromosome length), the PacMan will go up and down until "dead"

        if((moveIndex == moves.size() || moveIndex == moves.size()+1) && test) {
            test = false;
            return this::goUp;
        } else if((moveIndex == moves.size() || moveIndex == moves.size()+1) && !test){
            test = true;
            return this::goDown;
        }

        int bit1 = moves.get(moveIndex);
        int bit2 = moves.get(moveIndex+1);

//      Here we get the bit value from moves and decode so we know where the PacMan is supposed to go

        int tmp = 40;
        if(count != 0){
            count = (count+1)%tmp;
            return lastRMove;
        }
        count = (count+1)%tmp;

        if(bit1 == 0 && bit2 == 0){
//          If the move is illegal it will not be executed and a legal move will be found calling the getAvailable() method.

            if(checkAvailableCoords(getSpriteTopX()+3,getSpriteTopY()+7)){
                nextMove = this::goLeft;
                lastMove = 0;
            }else {
                nextMove = getAvailable();
            }
        }else if(bit1 == 0 && bit2 == 1){
            if(checkAvailableCoords(getSpriteTopX()+11, getSpriteTopY())){
                nextMove = this::goRight;
                lastMove = 1;
            }else {
                nextMove = getAvailable();
            }
        }else if(bit1 == 1 && bit2 == 0){
            if(checkAvailableCoords(getSpriteTopX()+3, getSpriteTopY())){
                nextMove = this::goUp;
                lastMove = 2;
            }else{
                nextMove = getAvailable();
            }
        }else if(bit1 == 1 && bit2 == 1){
            if(checkAvailableCoords(getSpriteTopX()+7, getSpriteTopY()+11)){
                nextMove = this::goDown;
                lastMove = 3;
            }else {
                nextMove = getAvailable();
            }
        }

        lastRMove = nextMove;
        lMove = nextMove;
        moveIndex+=2;
        return nextMove;
    }

    private Runnable getAvailable(){
//      This is for testing purposes
        boolean bool = true;

//      We find the first legal move

        if(checkAvailableCoords(getSpriteTopX()+3,getSpriteTopY()+7))
            return setBetterDirection(Direction.LEFT, bool);
        else if(checkAvailableCoords(getSpriteTopX()+11, getSpriteTopY()))
            return setBetterDirection(Direction.RIGHT, bool);
        else if(checkAvailableCoords(getSpriteTopX()+3, getSpriteTopY()))
            return setBetterDirection(Direction.UP, bool);
        else
            return setBetterDirection(Direction.DOWN, bool);
    }

    private Runnable setBetterDirection(Direction dir, boolean bool){
        if(bool == false){
            if(dir == Direction.LEFT)
                return this::goLeft;
            if(dir == Direction.RIGHT)
                return this::goRight;
            if(dir == Direction.UP)
                return this::goUp;
            if(dir == Direction.DOWN)
                return this::goDown;
            return null;
        } else {

//          When the first legal move is found we have to change the chromosome so it matches the move.
//          Once that is done, we tell the game our next move

            if(dir == Direction.LEFT){
                this.gene.getChromosome().set(moveIndex, 0);
                this.gene.getChromosome().set(moveIndex+1, 0);
                return this::goLeft;
            } else if(dir == Direction.RIGHT){
                this.gene.getChromosome().set(moveIndex, 0);
                this.gene.getChromosome().set(moveIndex+1, 1);
                return this::goRight;
            } else if(dir == Direction.UP){
                this.gene.getChromosome().set(moveIndex, 1);
                this.gene.getChromosome().set(moveIndex+1, 0);
                return this::goUp;
            } else {
                this.gene.getChromosome().set(moveIndex, 1);
                this.gene.getChromosome().set(moveIndex+1, 1);
                return this::goDown;
            }
        }
    }

    private Runnable getLastMove(int lastMove){
        if(this.moveIndex == 0){
            return this::goLeft;
        }
        if(lastMove == 0){
            return this::goRight;
        }
        if(lastMove == 1){
            return this::goLeft;
        }
        if(lastMove == 2){
            return this::goDown;
        }
        if(lastMove == 3){
            return this::goUp;
        }
        return null;
    }
}
