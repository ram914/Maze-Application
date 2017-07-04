/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mazeapplication;

import java.awt.Color;

/**
 *
 * @author ram bablu
 */
public class Cell{

    public Cell neighbors[] = new Cell[]{null,null,null,null};
    
    public static final int TOP = 0;
    public static final int LEFT = 1;
    public static final int BOTTOM = 2;
    public static final int RIGHT = 3;
    
    // 0-Top    1-Left  2-Bottom    3-Right
    private final boolean walls[] = new boolean[4];   
    
    private boolean visited = false;
    
    // x,y are coordinates of this cell
    int x, y;

    /**
     * Constructor for Cell class   <br>
     *  <em>x</em> : x coordinate on the canvas <br>
     *  <em></em>  : y coordinate on the canvas <br>
     * 
     * @param i
     * @param j 
     */
    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        
        
    }
    
    /**
     * Empty constructor
     */
    Cell() {
    
    }
    
    
    /**
     * This method sets neighbors of a cell.
     *  sets <em>top</em> as cell above the current cell
     *  sets <em>bottom</em> as cell below the current cell
     *  sets <em>left</em> as cell left to the current cell
     *  sets <em>right</em> as cell right to the current cell
     * 
     * @param top
     * @param bottom
     * @param left
     * @param right 
     */
    void setNeighbors(Cell top, Cell bottom, Cell left, Cell right) {
        if(top != null){
            this.neighbors[0] = top;
        }
        if(bottom != null){
            this.neighbors[2] = bottom;
        }
        if(left != null) {
            this.neighbors[1] = left;
        }
        if(right != null) {
            this.neighbors[3] = right;
        }
    }
    
    
    /**
     * This method draws a square for this Cell in the grid
     *  Coordinates for the cell are determined by <em>x</em> <em>y</em> fields <br>
     *  of the current Cell Object  <br>
     * 
     * @param width
     * @param color
     * @param thickness 
     */
    void drawCell(Color color, double pensize){
        GUIHandler.setPenColor(color);
        GUIHandler.setPenRadius(pensize);
        
        double x_c = this.x;
        double y_c = this.y;
        
        if(!this.isWallExist(TOP) && this.neighbors[TOP] != null)
            paintTop(x_c,y_c);
        
        if(!this.isWallExist(LEFT) && this.neighbors[LEFT] != null)
            paintLeft(x_c,y_c);
        
        if(!this.isWallExist(BOTTOM) && this.neighbors[BOTTOM] != null)
            paintBottom(x_c,y_c);
        
        if(!this.isWallExist(RIGHT) && this.neighbors[RIGHT] != null)
            paintRight(x_c,y_c);
    }

    private void paintBottom(double x, double y) {
        GUIHandler.line(x+1, y+1, x+2, y+1);
        setWall(BOTTOM,true);
        if(this.neighbors[BOTTOM] != null ) {
            this.neighbors[BOTTOM].setWall(TOP, true);
        }
    }

    private void paintTop(double x, double y) {
        GUIHandler.line(x+1, y+2, x+2, y+2);
        setWall(TOP,true);
        if(this.neighbors[TOP] != null ) {
            this.neighbors[TOP].setWall(BOTTOM, true);
        }
    }

    private void paintLeft(double x, double y) {
        GUIHandler.line(x+1, y+2, x+1, y+1);
        setWall(LEFT,true);
        
        if( this.neighbors[LEFT] != null ) {
            this.neighbors[LEFT].setWall(RIGHT, true);
        }
    }

    private void paintRight(double x, double y) {
        GUIHandler.line(x+2, y+1, x+2, y+2);
        setWall(RIGHT,true);
        if(this.neighbors[RIGHT] != null ) {
            this.neighbors[RIGHT].setWall(LEFT, true);
        }
    }
    
    public boolean isVisited() {
        return visited;
    }
    
    public void setVisited() {
        this.visited = true;
    }

    public void setWall(int side, boolean value) {
        if(side < 0 || side > 3) throw new ArrayIndexOutOfBoundsException();
        walls[side] = value;
    }
    
    public boolean isWallExist(int side) {
        if(side < 0 || side > 3) throw new ArrayIndexOutOfBoundsException();
        return walls[side];
    }

    public void markAsCurrent() {
        GUIHandler.filledCircle(this.x+1.5, this.y+1.5, 0.2,GUIHandler.GREEN);
        
        drawCell(GUIHandler.BLACK,GUIHandler.DEFAULT_PEN_RADIUS);
    }
    
    public void fill(String text,Color color) {
        GUIHandler.setPenColor(color);
        GUIHandler.text(this.x+1.3f, this.y+1.3f, text,color, 2.9555);
//        GUIHandler.filledSquare(this.x+1, this.y+2,1);
    }

    void markAsPrevious() {
        GUIHandler.filledCircle(this.x+1.5, this.y+1.5, 0.255,GUIHandler.WHITE);
        drawCell(GUIHandler.WHITE,GUIHandler.DEFAULT_PEN_RADIUS+0.00169);
    }
    
    @Override
    public boolean equals(Object obj) {
        if( obj == null ) return false;
        if( obj instanceof Cell) {
            Cell other = (Cell) obj;
            return (this.x == other.x) && (this.y == other.y);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.x;
        hash = 79 * hash + this.y;
        return hash;
    }

}