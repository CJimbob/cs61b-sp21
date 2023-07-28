package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

import static gitlet.Repository.HEAD_POINTER;


public class Branches implements Serializable {
    private static final File BRANCH_FILE = Repository.BRANCH_FILE;
    public static String currentBranch = Utils.readContentsAsString(HEAD_POINTER);
    public static void newBranches() {
        Utils.writeObject(BRANCH_FILE, branches);
    }

    /** Key: Branch Name, Value: Hash Code of Commit */
    private static HashMap<String, String> branches = new HashMap<>();
    public static void updateBranches(String branchName, Commit branch) {
        branches = Utils.readObject(BRANCH_FILE, HashMap.class);
        branches.put(branchName, branch.getHashCode());
        Utils.writeObject(BRANCH_FILE, branches);
    }

    public static Commit getCommit(String branchName) {
        branches = Utils.readObject(BRANCH_FILE, HashMap.class);
        String hashCode = branches.get(branchName);
        if (hashCode == null) {
            return null;
        } else {
            return Commit.getCommit(hashCode);
        }

    }

    public static HashMap getBranches() {
        return Utils.readObject(BRANCH_FILE, HashMap.class);
    }




}
