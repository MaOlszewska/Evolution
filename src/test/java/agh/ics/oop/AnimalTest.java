package agh.ics.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    @Test
    public void moveTest(){
        AbstractWorldMap map = new RightMap(5, 5, 0.3f,10,false);
        AnimalGenes genes = new AnimalGenes();
        Animal animal = new Animal(new Vector2d(1,1),10,map,genes);

        animal.move(2);
        assertEquals(animal.getPosition(), new Vector2d(1,1));
        animal.move(7);
        assertEquals(animal.getPosition(), new Vector2d(1,1));
        animal.move(4);
        assertNotEquals(animal.getPosition(), new Vector2d(1,1));
    }

    @Test
    public void getImageTest(){
        AbstractWorldMap map = new RightMap(5, 5, 0.3f,10,false);
        AnimalGenes genes = new AnimalGenes();
        Animal animal = new Animal(new Vector2d(1,1),10,map,genes);
        assertEquals(animal.getImage(), 4);
        animal.substractEnergy(7);
        assertEquals(animal.getImage(),2);
    }

    @Test
    public void newBornTest(){
        AbstractWorldMap map = new RightMap(5, 5, 0.3f,10,false);
        AnimalGenes genes = new AnimalGenes();
        Animal animal1 = new Animal(new Vector2d(1,1),100,map,genes);
        Animal animal2 = new Animal(new Vector2d(1,1),200,map,genes);
        Animal newAnimal = animal1.newBornAnimal(animal2);

        assertEquals(newAnimal.getPosition(), new Vector2d(1,1));
        assertEquals(newAnimal.getEnergy(),25 + 50);
        assertEquals(newAnimal.getImage(),4);
        assertEquals(animal1.getNumberOfChildren(), 1);
        assertEquals(animal2.getNumberOfChildren(), 1);
    }
}
