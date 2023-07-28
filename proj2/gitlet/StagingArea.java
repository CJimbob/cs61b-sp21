package gitlet;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StagingArea implements Serializable {

    private static final File STAGING_FILE = Repository.STAGING_FILE;
    private static final File REMOVED_FILE = Repository.REMOVED_FILE;

    private static final File CWD = Repository.CWD;

    /** Key: File Name, Value: HashCode */
    private static HashMap<String, String> addedBlobs;
    /** Key: File Name, Value: HashCode */
    private static HashMap<String, String> removedBlobs;

    public static void startStagingArea() {
        addedBlobs = new HashMap<>();
        Utils.writeObject(STAGING_FILE, addedBlobs);
        removedBlobs = new HashMap<>();
        Utils.writeObject(REMOVED_FILE, removedBlobs);
    }

    /** Add blob to staging area */
    public static void addBlobToMap (Blob Blob) {
        addedBlobs = Utils.readObject(STAGING_FILE, HashMap.class);
        addedBlobs.put(Blob.getFileName(), Blob.getHashCode());
        Utils.writeObject(STAGING_FILE, addedBlobs);

        removedBlobs = Utils.readObject(REMOVED_FILE, HashMap.class);
        removedBlobs.remove(Blob.getFileName());
        Utils.writeObject(REMOVED_FILE, removedBlobs);
    }

    public static void removeBlobFromMap(String fileName) {
        /** Unstage the file if it is currently staged for addition. */
        addedBlobs = Utils.readObject(STAGING_FILE, HashMap.class);
        String hashCode = addedBlobs.remove(fileName);
        Utils.writeObject(STAGING_FILE, addedBlobs);

        Commit headPointer = Branches.getCommit("headPointer");
        Map<String, String> Blobs = headPointer.getBlobs();
        if (!Blobs.containsKey(fileName) && hashCode == null) {
            System.out.println("No reason to remove the file");
        } else {
            removedBlobs = Utils.readObject(REMOVED_FILE, HashMap.class);
            removedBlobs.put(fileName, hashCode);
            Utils.writeObject(REMOVED_FILE, removedBlobs);
            Path path = Paths.get(fileName);
            if (Files.exists(path) && Blobs.containsKey(fileName)) {
                File file = Utils.join(CWD, fileName);
                file.delete();
            }
        }
    }
    public static void clear() {
        addedBlobs.clear();
        removedBlobs.clear();
        Utils.writeObject(REMOVED_FILE, removedBlobs);
        Utils.writeObject(STAGING_FILE, addedBlobs);
    }

    public static boolean isClear() {
        addedBlobs = Utils.readObject(STAGING_FILE, HashMap.class);
        removedBlobs = Utils.readObject(REMOVED_FILE, HashMap.class);
        if (!addedBlobs.isEmpty() || !removedBlobs.isEmpty()) {
            return false;
        }
        return true;
    }
    public static HashMap getStagingAreaMap() {
        addedBlobs = Utils.readObject(STAGING_FILE, HashMap.class);
        return addedBlobs;
    }

    public static HashMap getRemovedMap() {
        removedBlobs= Utils.readObject(REMOVED_FILE, HashMap.class);
        return removedBlobs;
    }

}
