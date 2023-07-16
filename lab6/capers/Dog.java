package capers;

import java.io.File;
import java.io.Serializable;
import static capers.Utils.*;

/** Represents a dog that can be serialized.
 * @author TODO
*/
public class Dog implements Serializable { // TODO

    /** Folder that dogs live in. */
    static final File DOG_FOLDER = Utils.join(".capers","dogs"); // TODO (hint: look at the `join`
                                         //      function in Utils)
    static final File DOG_NAME_FILE = Utils.join(DOG_FOLDER, "dogNames");

    /** Age of dog. */
    private int age;
    /** Breed of dog. */
    private String breed;
    /** Name of dog. */
    private String name;

    /** Number of dogs. */
    private int numOfDogs;

    /**
     * Creates a dog object with the specified parameters.
     * @param name Name of dog
     * @param breed Breed of dog
     * @param age Age of dog
     */
    public Dog(String name, String breed, int age) {
        this.age = age;
        this.breed = breed;
        this.name = name;
    }

    /**
     * Reads in and deserializes a dog from a file with name NAME in DOG_FOLDER.
     *
     * @param name Name of dog to load
     * @return Dog read from file
     */
    public static Dog fromFile(String name) {
        // TODO (hint: look at the Utils file)
        File dogFile = Utils.join(DOG_FOLDER, name);
        Dog dog = readObject(dogFile, Dog.class);

        return dog;
    }

    /**
     * Increases a dog's age and celebrates!
     */
    public void haveBirthday() {
        age += 1;
        System.out.println(this);
        System.out.println("Happy birthday! Woof! Woof!");

        File thisDog = Utils.join(DOG_FOLDER, name);
        writeObject(thisDog, this);


    }

    /**
     * Saves a dog to a file for future use.
     */
    public void saveDog() {
        // TODO (hint: don't forget dog names are unique)
        String names = readContentsAsString(DOG_NAME_FILE);
        String[] nameArray = names.split(" ", -1);
        boolean flag = true;

        for (int i = 0; i < nameArray.length; i++) {
            if (nameArray[i].equals(name)) {
                flag = false;
                break;
            }
        }
        if (flag == true) {
            File thisDog = Utils.join(DOG_FOLDER, name);
            writeObject(thisDog, this);
            writeContents(DOG_NAME_FILE, names, name, " ");
            System.out.println(this);
        } else {
            System.out.println("This name is taken");
        }

    }

    @Override
    public String toString() {
        return String.format(
            "Woof! My name is %s and I am a %s! I am %d years old! Woof!",
            name, breed, age);
    }

}
