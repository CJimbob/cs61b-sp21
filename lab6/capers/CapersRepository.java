package capers;

import java.io.File;
import java.io.IOException;

import static capers.Utils.*;

/** A repository for Capers 
 * @author TODO
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = Utils.join(".capers"); // TODO Hint: look at the `join`
                                            //      function in Utils
    static final File DOGS_FOLDER = Utils.join(".capers", "dogs");

    static final File STORY_FILE = Utils.join(".capers", "story");

    static final File DOG_NAME_FILE = Utils.join(DOGS_FOLDER, "dogNames");

    public static void setupPersistence() {

        CAPERS_FOLDER.mkdir();
        DOGS_FOLDER.mkdir();

        try {
            STORY_FILE.createNewFile();
            DOG_NAME_FILE.createNewFile();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writeStory(String text) {
        String outputString = readContentsAsString(STORY_FILE);
        Utils.writeContents(STORY_FILE, outputString, text, "\n");
        System.out.println(outputString + text + "\n");
    }


    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) {
        // TODO
        Dog dog = new Dog(name, breed, age);
        dog.saveDog();


    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        // TODO
        Dog dog = Dog.fromFile(name);
        dog.haveBirthday();
    }
}


