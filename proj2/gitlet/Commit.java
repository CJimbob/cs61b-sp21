package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static gitlet.Utils.join;

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
    private Date date;

    public static final File COMMIT_FILE = Repository.COMMIT_FILE;

    /** The message of this Commit. */
    private String message;

    /* TODO: fill in the rest of this class. */

    private Commit parentCommit;
    private String hashCode;


    /** Contains the set in Staging Area */
    private Map<String, String> Blobs;

    public Commit(String message, Commit parentCommit) {
        this.parentCommit = parentCommit;
        this.message = message;
        if (parentCommit != null) {
            Blobs = StagingArea.copyStagingAreaMap();
        }
        this.setHashCode();
    }


    public void setHashCode() {
        hashCode = Utils.sha1(Utils.readContents(COMMIT_FILE));
    }
    public String getHashCode() {
        return hashCode;
    }
    public static void writeCommit(Commit c) {
        Utils.writeObject(COMMIT_FILE, c);
    }

}
