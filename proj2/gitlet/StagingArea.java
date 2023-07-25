package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StagingArea implements Serializable {

    private static boolean mapWrittenInFile = false;
    public static final File STAGING_File = Repository.STAGING_FILE;
    /** Stores blobs' hashCode */

    public static final Map<String, Blob> addedBlobs = Utils.readObject(STAGING_File, HashMap.class);
    public static void startStagingArea() {
        Map<String, Blob> addedBlobs = new HashMap<>();
        Utils.writeObject(STAGING_File, (Serializable) addedBlobs);
    }


    /** Add blob to staging area */
    public static void addBlobToMap (Blob blob) {
        addedBlobs.put(blob.getFileName(), blob);
        Utils.writeObject(STAGING_File, (Serializable) addedBlobs);
    }
    public static void clear() {
        addedBlobs.clear();
        Utils.writeObject(STAGING_File, (Serializable) addedBlobs);
    }

    public static Map getStagingAreaMap() {
        return Utils.readObject(STAGING_File, HashMap.class);
    }

    public static Map copyStagingAreaMap() {
        Map<String, Blob> copiedMap = new HashMap<>();
        copiedMap.putAll(addedBlobs);
        return copiedMap;
    }

    public static void removeBlobFromMap(String fileName) {
        addedBlobs.remove(fileName);
        Utils.writeObject(STAGING_File, (Serializable) addedBlobs);
    }




}
