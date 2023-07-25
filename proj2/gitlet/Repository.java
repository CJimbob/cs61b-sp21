package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");


    public static final File STAGING_FILE = join(GITLET_DIR, "staging");

    public static final File COMMIT_FILE = join(GITLET_DIR, "commit");

    public static final File BLOB_DIR = join(GITLET_DIR, "blobs");

    private static Commit headPointer;

    /* TODO: fill in the rest of this class. */

    public static void init() {

        GITLET_DIR.mkdir();
        BLOB_DIR.mkdir();
        try {
            COMMIT_FILE.createNewFile();
            STAGING_FILE.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Commit c = new Commit("Initialise Gitlet", null);
        Commit.writeCommit(c);
        headPointer = c;
    }

    public static void add(String fileName) {
        Map<String, Blob> map = StagingArea.getStagingAreaMap();

        if (!map.containsKey(fileName)) {
            Blob newBlob = new Blob(fileName);
            newBlob.addBlobToBLOB_DIR();
            StagingArea.addBlobToMap(newBlob);
        } else {
            Blob blobInMap = map.get(fileName);

            String fileContent = Utils.readContentsAsString(blobInMap.workingFile);
            if (!fileContent.equals(blobInMap.getFileContent())  && !blobInMap.isCommited()) {
                blobInMap.updateContent();
                System.out.println("File Changed");
            }

        }

    }

    public static void commit(String message) {
        Commit lastCommit = readObject(COMMIT_FILE, Commit.class);
        Commit c = new Commit(message, lastCommit);
        Commit.writeCommit(c);

        headPointer = c;
    }



}
