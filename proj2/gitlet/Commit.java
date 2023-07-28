package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class

import java.util.HashMap;
import java.util.Map;

import static gitlet.Utils.join;
import static gitlet.Utils.serialize;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    private static final File COMMIT_DIR = Repository.COMMIT_DIR;
    private Date date;


    /** The message of this Commit. */
    private String message;

    /* TODO: fill in the rest of this class. */
    /** Hashcode of parent commit */
    private String parentCommit;
    private String hashCode;

    private boolean changed = false;

    /** Contains the set in Staging Area */
    private HashMap<String, String> Blobs;

    public Commit(String message, Commit parentCommit) {

        this.message = message;
        if (parentCommit != null) {
            this.parentCommit = parentCommit.getHashCode();
            Blobs = new HashMap<>(parentCommit.Blobs);
            updateBlobs();
            removeBlobs();
        } else {
            Blobs = new HashMap<>();
        }
        date = new Date();
        this.setHashCode();
    }


    public boolean getChanged() {
        return changed;
    }
    private void updateBlobs() {
        Map<String, String> blobsInStagingArea = StagingArea.getStagingAreaMap();
        for (Map.Entry<String, String> entry : blobsInStagingArea.entrySet()) {
            String fileName = entry.getKey();
            String hashCode = entry.getValue();
            Blobs.put(fileName, hashCode);
            changed = true;
        }
    }

    private void removeBlobs() {
        Map<String, String> removedMap = StagingArea.getRemovedMap();
        for (Map.Entry<String, String> entry : removedMap.entrySet()) {
            String fileName = entry.getKey();
            Blobs.remove(fileName);
            changed = true;
        }
    }


    public void setHashCode() {
        hashCode = Utils.sha1(serialize(this));
    }
    public String getHashCode() {
        return hashCode;
    }

    public String getDate() {
        return date.toString();
    }

    public String getMessage() {
        return message;
    }

    public String getParentCommit() {
        return parentCommit;
    }

    public boolean hasParentCommit() {
        if (parentCommit == null) {
            return false;
        }
        return true;
    }

    public HashMap<String, String> getBlobs() {
        return Blobs;
    }

    public static Commit getCommit(String hashCode) {
        File commitFile = Utils.join(COMMIT_DIR, hashCode);
        return Utils.readObject(commitFile, Commit.class);
    }
}
