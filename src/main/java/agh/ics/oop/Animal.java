package agh.ics.oop;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Animal implements IMapElement{
    private Vector2d position;
    private MapDirection orientation;
    private final IWorldMap map;
    private int energy;
    private final AnimalGenes genes;
    private int numberOfChildren;
    private int numberOfDays;
    private ArrayList<IPositionChangeObserver> observers;
    private ArrayList<IEnergyChangeObserver> energyObservers;


    public Animal(Vector2d initialPosition, int initialEnergy, IWorldMap map, AnimalGenes genes){
        this.orientation = MapDirection.randomOrientation();
        this.position = initialPosition;
        this.energy = initialEnergy;
        this.map = map;
        this.genes = genes;
        this.numberOfChildren = 0;
        this.numberOfDays = 0;
        this.observers = new ArrayList<>();
        this.energyObservers = new ArrayList<>();
    }
    public void addOneDay(){this.numberOfDays += 1;}
    public int getNumberOfDays(){return numberOfDays;}
    public void addChild(){this.numberOfChildren += 1;}
    public int getNumberOfChildren(){return numberOfChildren;}
    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public int getEnergy(){
        return this.energy;
    }

    public AnimalGenes getGenes(){return this.genes;}

    public void substractEnergy(int i ){
        this.energy -= i;
    }

    public void addEnergy(int i){
        this.energy += i;
    }

    public void addObserver(IPositionChangeObserver observer){
        this.observers.add(observer);
    }

    public void removeObserver (IPositionChangeObserver observer){
        this.observers.remove(observer);
    }

//    public void addEnergyObserver(IEnergyChangeObserver observer){
//        this.energyObservers.add(observer);
//    }
//
//    public void removeEnergyObserver (IEnergyChangeObserver observer){
//        this.energyObservers.remove(observer);
//    }


    public void move(int movement, int energy){
        Vector2d newPosition;
        //System.out.println(this.getEnergy());

        switch (movement) {
            case 0 :
                newPosition = this.position.add(this.orientation.toUnitVector());
                if(this.map.canMoveTo(newPosition)){
                    Vector2d oldPosition = this.position;
                    this.position = newPosition;
                    positionChanged(oldPosition,newPosition);
                }
            case 4:
                newPosition = this.position.subtract(this.orientation.toUnitVector());
                if(this.map.canMoveTo(newPosition)){
                    Vector2d oldPosition = this.position;
                    this.position = newPosition;
                    positionChanged(oldPosition,newPosition);
                }
                break;
            default: rotate(movement);

        }
    }

//    private void energyChanged(){
//        for(IEnergyChangeObserver observer : this.energyObservers){observer.energyChanged(this);}
//    }
    private void positionChanged(Vector2d oldPosition, Vector2d newPosition) {
        for (IPositionChangeObserver observer : this.observers){observer.positionChanged(this, oldPosition, newPosition);}
    }
    private void rotate(int movement){
        for(int i = 1; i <= movement; i++ ){if(i != 4){this.orientation = this.orientation.next();}}
    }

    @Override
    public String getPath(IMapElement object) {
        Animal animal = (Animal) object;
        MapDirection orientation = animal.orientation;
        switch (orientation) {
            case NORTH -> {return "src/main/resources/North.png";}
            case EAST -> {return "src/main/resources/East.png";}
            case SOUTH -> {return "src/main/resources/South.png";}
            case WEST -> {return "src/main/resources/WEST.png";}
            default -> {return "src/main/resources/WEST.png";}
        }
    }

    public int selectMovement(){return genes.selectMovemnet();}

    public Animal newBornAnimal( Animal dad){
        int newBornEnergy = this.getEnergy()/4 + dad.getEnergy()/4;
        Vector2d newBornPosition = this.getPosition();
        AnimalGenes newBornGenes = createNewBornGenes(dad);
        this.substractEnergy(this.getEnergy()/4);
        dad.substractEnergy(dad.getEnergy()/4);
        return new Animal( newBornPosition,newBornEnergy, map, newBornGenes);
    }

    public AnimalGenes createNewBornGenes(Animal secondParent) {
        int[] newBornGenes = new int[32];
        int fisrtAnimalEnergy = this.getEnergy();
        int secondAnimalEnergy = secondParent.getEnergy();
        int[] firstAnimalGenes = this.genes.getGenes();
        int[] secondAnimalGenes = secondParent.genes.getGenes();
        int div = (int) ((((float)(fisrtAnimalEnergy)/(fisrtAnimalEnergy + secondAnimalEnergy))) * 32 - 1);

        if(fisrtAnimalEnergy >= secondAnimalEnergy){ // dziecko dostaje wiecej genów mamy
            Random random = new Random();// true-lewa false-prawa
            if(random.nextBoolean()){newBornGenes = leftSide(div, secondAnimalGenes, firstAnimalGenes);}
            else{newBornGenes = rightSide(div, secondAnimalGenes, firstAnimalGenes);}//WYlosowano stronę prawą
        }
        else{
            Random random = new Random();// true-lewa false-prawa
            if(random.nextBoolean()){newBornGenes = leftSide(div, secondAnimalGenes, firstAnimalGenes);}
            else{newBornGenes = rightSide(div, secondAnimalGenes, firstAnimalGenes);}//WYlosowano stronę prawą
        }
        Arrays.sort(newBornGenes);
        for(int i = 0; i< 32; i++){
            System.out.print(newBornGenes[i]);
        }
        return new AnimalGenes(newBornGenes);
    }

    private int[] rightSide(int div, int[] firstAnimalGenes, int[] secondAnimalGenes ){
        int[] newBornGenes = new int[32];
        for(int i = 32 - div - 1; i < 32; i++){newBornGenes[i] = secondAnimalGenes[i];}
        for(int i = 0; i < 32 - div - 1; i++){newBornGenes[i] = firstAnimalGenes[i];}
        return newBornGenes;
    }
    private int[] leftSide(int div, int[] firstAnimalGenes, int[] secondAnimalGenes){
        int[] newBornGenes = new int[32];
        for(int i = 0  ; i <= div; i++){newBornGenes[i] = secondAnimalGenes[i];}
        for(int i = div + 1; i < 32; i++){newBornGenes[i] = firstAnimalGenes[i];}
        return newBornGenes;
    }
}
