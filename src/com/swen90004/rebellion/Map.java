package com.swen90004.rebellion;

import java.util.ArrayList;
import java.util.List;

public class Map {
    private ArrayList<Interactable> map;
    private int size;

    public Map(int size){
        this.size = size;
    }

    public Map(int size, ArrayList<Interactable> map ){
        this.size = size;
        this.map = map;
    }

    public void initialiseBoard() {
        int initialCopDensity = Configuration.getInt("initialCopDensity");
        int initialCitizenDensity = Configuration.getInt("initialCitizenDensity");

        // convert densities to number of citizens, cops and empty spaces
        int numOfCops = (int)Math.floor( initialCopDensity/100 * math.pow(this.size,2) ) ;
        int numOfCitizens = (int)Math.floor( initialCitizenDensity/100 * math.pow(this.size,2) );
        int numOfEmpty = math.pow(this.size,2) - numOfCops - numOfCitizens;

        // If the sum of the initialCopDensity and initialCitizenDensity is greater
        // then 100% then we cannot place all the interactables on the map
        if( initialCopDensity + initialCitizenDensity > 100 ){
            throw new java.lang.Error("Error: initialCopDensity + initialCitizenDensity must be less than 100");
        }

        // iterate through all board positions and randomly pick: a citizen, cop or empty spot
        for (int x = 0; x < this.size; x++){
            for (int y = 0; y < this.size; y++) {
                Position position = new Position(x,y);
                while(true) {
                    int choice = random.nextInt(3);
                    if(choice == 0 && numOfCops > 0){
                        this.map.add(new Cop(this,position));
                        numOfCops--;
                        break;
                    }
                    else if(choice == 1 && numOfCitizens > 0){
                        this.map.add(new Citizen(this,position));
                        numOfCitizens--;
                        break;
                    }
                    else if(choice == 2 && numOfEmpty > 0){
                        numOfEmpty--;
                        break;
                    }
                }
            }

        }
    }

    public Map getNeighbourhood(Position position, int vision) {
        ArrayList cells = new ArrayList();
        Map neighbourhood;

        xDist =  math.abs(interactable.getposition().x - position.x);
        yDist =  math.abs(interactable.getposition().y - position.y);

        for( Interactable interactable : this.map ){
            if(xDist <= vision && yDist <= vision){
                cells.append(interactable);
            }
        }
        // size is 2*vision + 1 since the pattern for vision to map size is:
        // vision : 0,1,2,3
        // size   : 1,3,5,7
        neighbourhood = new Map(2*vision+1,cells);
        return neighbourhood;
    }

    public Position getEmptyPosition() {

    }

    public List<Citizen> getActiveCitizens() {
        List<Citizen> activeCitizens = new  ArrayList();
        for(Interactable interactable: map){
            if(interactable instanceof Citizen && ((Citizen) interactable).isRebelling()){
               activeCitizens.append(interactable);
            }
        }
        return activeCitizens;
    }

    public List<Cop> getCops() {
        List<Cop> cops = new  ArrayList();
        for(Interactable interactable: map){
            if(interactable instanceof Cop){
                cops.append(interactable);
            }
        }
        return cops;
    }
}
