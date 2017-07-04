/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeapplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * @author ram bablu
 */
public class MazeCreater {
    static HashSet<Cell> path = new HashSet<>();
    static LinkedList<Cell> pathList = new LinkedList<>();
    static Cell start;
    static Cell end;
    
    private boolean pathCompleted;
    
    static void cellClicked(Cell clickedCell) {
        
        System.out.println(path.size());
        if(!path.contains(clickedCell) || !pathList.element().equals(clickedCell)) {
            clickedCell.fill("X",GUIHandler.RED);
            return;
        }
        if(clickedCell.equals(end)) {
            clickedCell.fill("\u2713",GUIHandler.GREEN);
           GUIHandler.completeDialog(0);
        } else {
            clickedCell  = pathList.remove();
            Cell neighbor = pathList.element();
            System.out.println("["+clickedCell.x+","+clickedCell.y+"] is in the path");
            String text = "";
            if( neighbor.equals(clickedCell.neighbors[Cell.TOP]) ) {
                text = "\u261D";
            } else if( neighbor.equals(clickedCell.neighbors[Cell.LEFT]) ) {
                text = "\u261C";
            } else if ( neighbor.equals(clickedCell.neighbors[Cell.BOTTOM]) ) {
                text = "\u261F";
            } else if( neighbor.equals(clickedCell.neighbors[Cell.RIGHT]) ) {
                text = "\u261E";
            }
            clickedCell.fill(text,GUIHandler.GOLD); //2620
        } 
    }
    private int visitedCells = 0;
    private int totalCells = 0;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        MazeCreater mc = new MazeCreater();
        GUIHandler.getSize();
        mc.setUpGrid1(GUIHandler.getGridLength());
        
        int cell_count = mc.read();
        System.out.println(cell_count);
        
        Cell cells[][] = new Cell[cell_count][cell_count];
        
        
        mc.setUpGrid2(cells,cell_count);
        
        mc.createMaze(cells);
        
        GUIHandler.setPenColor(GUIHandler.WHITE);
        GUIHandler.setPenRadius(0.00755);
        GUIHandler.line(2, 2, 2, 3);
        GUIHandler.line(cell_count+2, cell_count+2, cell_count+2, cell_count+1);
        GUIHandler.text(cell_count+2.3f, cell_count+1.3f, "\u2620",GUIHandler.BLUE, 2.9555);
        GUIHandler.text(1.3f, 1+1.3f, "\u2603",GUIHandler.BLUE, 2.9555);
        
    }
    
    private void setUpGrid2(Cell[][] cells, int cell_count) {
        
        // i - y axis         j - x axis
        for(int i = 0; i < cell_count;i++) {
            for( int j = 0; j < cell_count; j++ ) {
                cells[i][j] = new Cell(j+1,i+1);
            }
        }
        
        // Set neighbours for 4 corner cells
        cells[0][0].setNeighbors(cells[1][0], null , null,cells[0][1]  );
        cells[cell_count-1][0].setNeighbors( null, cells[cell_count-2][0],null, cells[cell_count-1][1] );
        cells[0][cell_count-1].setNeighbors( cells[1][cell_count-1],null,  cells[0][cell_count-2],null);
        cells[cell_count-1][cell_count-1].setNeighbors(null,cells[cell_count-2][cell_count-1], cells[cell_count-1][cell_count-2], null);
       
        // Set neighbours for all edge Cells
        for(int i = 1; i < cell_count-1;i++) {
            cells[0][i].setNeighbors( cells[1][i],null, cells[0][i-1],cells[0][i+1]); // top most row
            cells[cell_count-1][i].setNeighbors( null,cells[cell_count-2][i], cells[cell_count-1][i-1], cells[cell_count-1][i+1]); // bottom most row
            cells[i][0].setNeighbors(cells[i+1][0], cells[i-1][0] ,null, cells[i][1]); // left most column
            cells[i][cell_count-1].setNeighbors(cells[i+1][cell_count-1], cells[i-1][cell_count-1], cells[i][cell_count-2],null); // right mose column
        }
        
        // Set neighbours for All middle cells
        for(int i = 1; i < cell_count-1;i++) {
            for( int j = 1; j < cell_count-1; j++ ) {
                cells[i][j].setNeighbors(cells[i+1][j], cells[i-1][j], cells[i][j-1], cells[i][j+1]);
            }
        }
    }
    
    private void setUpGrid1( int len) {
        
        GUIHandler.setCanvasSize(128);
        // if canvas is a palane bottom left point will be (1,1) top right point will be (len+1,len+1)
        GUIHandler.setPenColor(GUIHandler.BLACK);
        GUIHandler.setScale(1, len+3);
        GUIHandler.startProgressBar();
        
        GUIHandler.square(2, len+2, len);
        
        
    }

    private void createMaze(Cell[][] cells) {
        LinkedList<Cell> dfsList = new LinkedList<>();
        
        // starting at the middle cell
        //dfs(cells[cells.length/2][cells.length/2],dfsList);
        
        // selecting the destination cells
        start = cells[0][0];
        end   = cells[cells.length-1][cells.length-1];
        
        dfs(cells[0][0],dfsList);
        
        GUIHandler.square(2, cells.length+2, cells.length);
    }

    private void dfs(Cell current, LinkedList<Cell> dfsList)    {
        if( current == null ) { return; }
        
        // logic to update progressBar
        if(!current.isVisited()) {
            visitedCells ++;
            GUIHandler.updateProgressBar((int)( (visitedCells * 100) / totalCells) );
        }
        
        current.markAsCurrent();
        current.setVisited();
        
        if(!pathCompleted && !path.contains(current)) {
            path.add(current);
            pathList.add(current);
            System.out.println(pathList.size());
        }
        if(current.equals(end)) {
            pathCompleted = true;
        }
        
        Cell next = getNextCell(current);
        if(next != null) {
            dfsList.add(current);
            
            current.markAsPrevious();
            dfs(next,dfsList);
            
        } else {
            // when current doesnt have neighbors it is clear that 
            // it will not be in the path
            if(!pathCompleted) {
                path.remove(current);
                pathList.remove(current);
            }
                
            if(dfsList.size() > 0){
                next = dfsList.removeLast();
            }
            current.markAsPrevious(); // if this line is not there green dots will pop up entire screen
            dfs(next,dfsList);
        }
        
    }

    private Cell getNextCell(Cell cell) {
        ArrayList<Cell> neighbors = new ArrayList<>();
        if( cell.neighbors[Cell.TOP] != null && !cell.neighbors[Cell.TOP].isVisited()) {
            neighbors.add(cell.neighbors[Cell.TOP]);
       }
        if( cell.neighbors[Cell.LEFT] != null && !cell.neighbors[Cell.LEFT].isVisited() ) {
            neighbors.add(cell.neighbors[Cell.LEFT]);
        }
        if( cell.neighbors[Cell.BOTTOM] != null && !cell.neighbors[Cell.BOTTOM].isVisited() ) {
            neighbors.add(cell.neighbors[Cell.BOTTOM]);
        }
        if( cell.isWallExist(Cell.RIGHT) && !cell.neighbors[Cell.RIGHT].isVisited()) {
            neighbors.add(cell.neighbors[Cell.RIGHT]);
        }
        if(neighbors.size() > 0) {
            int ind = (int)System.currentTimeMillis();
            ind = Math.abs(ind % neighbors.size());
            Collections.shuffle(neighbors);
            Cell next = neighbors.get(ind);
            //System.out.println("Selected :["+next.x+","+next.y+"] ");
            if(next.equals(cell.neighbors[Cell.TOP])) {
                cell.setWall(Cell.TOP, false);
                next.setWall(Cell.BOTTOM, false);
            } else if( next.equals(cell.neighbors[Cell.LEFT]) ) {
                cell.setWall(Cell.LEFT, false);
                next.setWall(Cell.RIGHT, false);
            } else if( next.equals(cell.neighbors[Cell.BOTTOM]) ) {
                cell.setWall(Cell.BOTTOM, false);
                next.setWall(Cell.TOP, false);
            } else if( next.equals(cell.neighbors[Cell.RIGHT]) ){
                cell.setWall(Cell.RIGHT, false);
                next.setWall(Cell.LEFT, false);
            }
            return next;
        } else {
            return null;
        }
    }
    
    private int read() {
          int c = GUIHandler.getGridLength();
          totalCells = (int)Math.pow( c, 2);
          return c;
    }
    
}

/**
 *  Grid will be like
 *  6X6 grid example
 *     __________________
 *    |                  |
 *    |  6 _ _ _ _ _ _   |
 *    |  5 _ _ _ _ _ _   |
 *    |  4 _ _ _ _ _ _   |
 *    |  3 _ _ _ _ _ _   |
 *    |  2 _ _ _ _ _ _   |
 *    |  1 _ _ _ _ _ _   |
 *    |   1 2 3 4 5 6    |
 *    --------------------
 * 
 */




