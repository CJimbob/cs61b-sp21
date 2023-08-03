package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

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


    public static final File STAGING_FILE = join(GITLET_DIR, "staging.txt");
    public static final File REMOVED_FILE = join(GITLET_DIR, "removed.txt");
    public static final File BRANCH_FILE = join(GITLET_DIR, "branch.txt");
    public static final File BLOB_DIR = join(GITLET_DIR, "blobs");

    public static final File COMMIT_DIR = join(GITLET_DIR, "commits");
    public static final File HASHCODE_COMMIT_MAPPING = join(GITLET_DIR, "hashCodeCommit.txt");
    public static final File HEAD_POINTER = join(GITLET_DIR, "headPointer.txt");
    private static Commit headPointer;


    /* TODO: fill in the rest of this class. */

    public static void init() {

        GITLET_DIR.mkdir();
        BLOB_DIR.mkdir();
        COMMIT_DIR.mkdir();
        try {
            HEAD_POINTER.createNewFile();
            STAGING_FILE.createNewFile();
            REMOVED_FILE.createNewFile();
            BRANCH_FILE.createNewFile();
            HASHCODE_COMMIT_MAPPING.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        StagingArea.startStagingArea();

        Commit c = new Commit("initial commit", null);
        File commitFile = Utils.join(COMMIT_DIR, c.getHashCode());
        Utils.writeObject(commitFile, c);
        headPointer = c;
        initialiseHeadPointer();
        Branches.newBranches();
        Branches.updateBranches("headPointer", headPointer);
        Branches.updateBranches(Branches.currentBranch, headPointer);
    }
    private static void initialiseHeadPointer() {
        Utils.writeContents(HEAD_POINTER, "master");
    }


    public static void add(String fileName) {
        Blob newBlob = new Blob(fileName);
        newBlob.addBlobToBLOB_DIR();
        StagingArea.addBlobToMap(newBlob);
    }

    public static void commit(String message) {
        Commit lastCommit = Branches.getCommit("headPointer");
        Commit c = new Commit(message, lastCommit);
        if (!c.getChanged()) {
            System.out.println("No changes added to the commit.");
            return;
        }
        StagingArea.clear();

        headPointer = c;
        File commitFile = Utils.join(COMMIT_DIR, c.getHashCode());
        Utils.writeObject(commitFile, c);
        Branches.updateBranches("headPointer", headPointer);
        Branches.updateBranches(Branches.currentBranch, headPointer);
    }

    public static void rm(String fileName) {
        /** If the file is tracked in the current commit, stage it for removal and remove the file from
         * the working directory if the user has not already done so
         * (do not remove it unless it is tracked in the current commit). */
        StagingArea.removeBlobFromMap(fileName);
    }


    public static void log() {

        Commit commit = Branches.getCommit("headPointer");
        String parentHashCode;
        System.out.println("===");
        System.out.println("commit " + commit.getHashCode());
        System.out.println("Date: " + commit.getDate());
        System.out.println(commit.getMessage());
        System.out.println();
        while (commit.hasParentCommit()) {
            parentHashCode = commit.getParentCommit();
            commit = Commit.getCommit(parentHashCode);
            System.out.println("===");
            System.out.println("commit " + commit.getHashCode());
            System.out.println("Date: " + commit.getDate());
            System.out.println(commit.getMessage());
            System.out.println();
        }
    }

    public static void global_log() {
        String[] pathNames = COMMIT_DIR.list();
        for (String pathName : pathNames) {
            Commit commit = Commit.getCommit(pathName);
            System.out.println("===");
            System.out.println("commit " + commit.getHashCode());
            System.out.println("Date: " + commit.getDate());
            System.out.println(commit.getMessage());
            System.out.println();
        }
    }

    public static void find(String commit_message) {
        HashMap<String, String> branches = Branches.getBranches();
        boolean flag = false;
        for (Map.Entry<String, String> entry : branches.entrySet()) {
            String branchName = entry.getKey();
            if (branchName.equals("headPointer")) continue;
            String hashCode = entry.getValue();
            Commit commit = Commit.getCommit(hashCode);
            if (commit.getMessage().equals(commit_message)) {
                System.out.println(commit.getHashCode());
                flag = true;
            }
        }
        if (!flag) {
            System.out.println("Found no commit with that message");
        }

    }


    public static void status() {
        HashMap<String, String> branches = Branches.getBranches();
        System.out.println("=== Branches ===");
        for (Map.Entry<String, String> entry : branches.entrySet()) {
            String branchName = entry.getKey();
            if (branchName.equals("headPointer")) continue;
            if (branchName.equals(Branches.currentBranch)) {
                System.out.print("*");
            }
            System.out.println(branchName);
        }

        System.out.println();

        HashMap<String, String> addedBlobs = StagingArea.getStagingAreaMap();
        System.out.println("=== Staged Files ===");
        for (Map.Entry<String, String> entry : addedBlobs.entrySet()) {
            String fileName = entry.getKey();
            System.out.println(fileName);
        }

        System.out.println();

        HashMap<String, String> removedMap = StagingArea.getRemovedMap();
        System.out.println("=== Removed Files ===");
        for (Map.Entry<String, String> entry : removedMap.entrySet()) {
            String fileName = entry.getKey();
            System.out.println(fileName);
        }

        System.out.println();
        System.out.println("=== Modifications Not Stage For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");

        System.out.println();

    }


    public static void checkout (String name, int operation) {
        if (operation == 1) {
            Commit commit = Branches.getCommit("headPointer");
            HashMap<String, String> Blobs = commit.getBlobs();
            String hashCode = Blobs.get(name);
            if (hashCode == null) {
                System.out.println("File does not exist in that commit.");
                return;
            }
            File blobFile = Utils.join(BLOB_DIR, hashCode);
            Blob blob = Utils.readObject(blobFile, Blob.class);
            File file = Utils.join(CWD, name);
            Utils.writeContents(file, blob.getFileContent());
        } else if (operation == 2){
            Commit commit = Branches.getCommit(name);
            if (commit == null) {
                System.out.println("No such branch exists.");
                return;
            } else if (Branches.currentBranch.equals(name)) {
                System.out.println("No need to checkout the current branch.");
                return;
            }
            HashMap<String, String> Blobs = commit.getBlobs();
            for (Map.Entry<String, String> entry : Blobs.entrySet()) {
                String fileName = entry.getKey();
                String hashCode = entry.getValue();
                Blob blob = Blob.getBlob(hashCode);
                File file = Utils.join(CWD, fileName);
                Utils.writeContents(file, blob.getFileContent());
            }
            Utils.writeContents(HEAD_POINTER, name);
            Branches.updateBranches("headPointer", commit);
        }
    }

    public static void checkout (String commitID, String fileName) {
        Commit commit = Commit.getCommit(commitID);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            return;
        }
        HashMap<String, String> Blobs = commit.getBlobs();
        String hashCode = Blobs.get(fileName);
        if (hashCode == null) {
            System.out.println("File does not exist in that commit.");
            return;
        }
        File blobFile = Utils.join(BLOB_DIR, hashCode);
        Blob blob = Utils.readObject(blobFile, Blob.class);
        File file = Utils.join(CWD, fileName);
        Utils.writeContents(file, blob.getFileContent());
    }

    public static void branch(String branchName) {
        if (Branches.getBranches().containsKey(branchName)) {
            System.out.println("A branch with that name already exists");
            return;
        }
        headPointer = Branches.getCommit("headPointer");
        Utils.writeContents(HEAD_POINTER, branchName);
        Branches.updateBranches(branchName, headPointer);
    }

    public static void rm_branch(String branchName) {
        HashMap<String, String> branches = Branches.getBranches();
        if (!branches.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist");
            return;
        }

        if (Branches.currentBranch.equals(branchName)) {
            System.out.println("Cannot remove the current branch");
        } else {
            branches.remove(branchName);
            Utils.writeObject(BRANCH_FILE, branches);
        }
    }


    public static void reset(String commitId) {
        Commit commit = Commit.getCommit(commitId);
        if (commit == null) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Branches.updateBranches(Branches.currentBranch, commit);
        checkout(Branches.currentBranch, 2);
    }

    public static void merge(String branchName) {
        if (!StagingArea.isClear()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        Commit temp = Branches.getCommit(branchName);
        if (temp == null) {
            System.out.println("A branch with that name does not exist.");
            return;
        }

        Commit splitPointer = findSplitPointer(branchName);
        HashMap<String, String> splitBlobs = splitPointer.getBlobs();
        Commit givenCommit = Branches.getCommit(branchName);
        HashMap<String, String> givenBlobs = givenCommit.getBlobs();
        Commit currentCommit = Branches.getCommit(Branches.currentBranch);
        HashMap<String, String> currentBlobs = currentCommit.getBlobs();

        if (branchName.equals(Branches.currentBranch)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        } else if (splitPointer.getHashCode().equals(givenCommit.getHashCode())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        } else if (splitPointer.getHashCode().equals(currentCommit.getHashCode())) {
            checkout(branchName, 2);
            System.out.println("Current branch fast-forwarded.");
            return;
        }

        HashMap<String, boolean[]> checkMap= new HashMap<>();
        for (Map.Entry<String, String> entry : splitBlobs.entrySet()) {
            String fileName = entry.getKey();
            boolean[] check = {true, false, false};

            if (!checkMap.containsKey(fileName)) {
                checkMap.put(fileName, check);
            }

        }
        for (Map.Entry<String, String> entry : currentBlobs.entrySet()) {
            String fileName = entry.getKey();
            boolean[] check = {false, true, false};

            if (!checkMap.containsKey(fileName)) {
                checkMap.put(fileName, check);
            } else {
                check = new boolean[]{true, true, false};
                checkMap.put((fileName), check);
            }

        }
        for (Map.Entry<String, String> entry : givenBlobs.entrySet()) {
            String fileName = entry.getKey();
            boolean[] check = {false, false, true};

            if (!checkMap.containsKey(fileName)) {
                checkMap.put(fileName, check);
            } else {
                check = checkMap.remove(fileName);
                check[2] = true;
                checkMap.put(fileName, check);
            }
        }
        boolean mergeConflict = false;
        for (Map.Entry<String, boolean[]> entry : checkMap.entrySet()) {
            String fileName = entry.getKey();
            boolean[] check = entry.getValue();
            String currentHashCode = currentBlobs.remove(fileName);
            String splitHashCode = splitBlobs.remove(fileName);
            String givenHashCode = givenBlobs.remove(fileName);
            if (check[0] == false && check[1] == false && check[2] == true) {
                /** add a new file */
                Blob blob = Blob.getBlob(givenHashCode);
                StagingArea.addBlobToMap(blob);
            } else if (check[0] == false && check[1] == true && check[2] == false) {
                /** add file to stagingArea */
                Blob blob = Blob.getBlob(currentHashCode);
                StagingArea.addBlobToMap(blob);
            } else if (check[0] == true && check[1] == false && check[2] == true) {
                if (givenHashCode.equals(splitHashCode)) {
                    Blob blob = Blob.getBlob(givenHashCode);
                    StagingArea.addBlobToMap(blob);
                }
                /** check if file is different
                 * Yes: make change
                 * No: no change
                 * */
            } else if (check[0] == true && check[1] == true && check[2] == false) {
                /** check if file is now absent in given branch
                 * Yes: remove the origin file
                 * No: no change
                 */
                if (currentHashCode.equals(splitHashCode)) {
                    StagingArea.removeBlobFromMap(fileName);
                }
            }  else if (check[0] == true && check[1] == true && check[2] == true) {
                /** need compare two files */
                if (currentHashCode.equals(splitHashCode) && !currentHashCode.equals(givenHashCode)) {
                    Blob blob = Blob.getBlob(givenHashCode);
                    StagingArea.addBlobToMap(blob);
                }  else if (givenHashCode.equals(splitHashCode) && !currentHashCode.equals(givenHashCode)) {
                    Blob blob = Blob.getBlob(currentHashCode);
                    StagingArea.addBlobToMap(blob);
                }
            }  else if (check[1] == true && check[2] == true) {
                if (!currentHashCode.equals(splitHashCode) && currentHashCode.equals(givenHashCode)){
                    Blob blob = Blob.getBlob(givenHashCode);
                    StagingArea.addBlobToMap(blob);
                } else if (!givenHashCode.equals(splitHashCode) && !currentHashCode.equals(givenHashCode)) {
                    /** conflict resolve */
                    Blob currentBlob = Blob.getBlob(currentHashCode);
                    Blob givenBlob = Blob.getBlob(givenHashCode);
                    Blob.mergeBlob(currentBlob, givenBlob, fileName);
                    Blob newBlob = new Blob(fileName);
                    newBlob.addBlobToBLOB_DIR();
                    StagingArea.addBlobToMap(newBlob);
                    mergeConflict = true;
                }
            }
        }
        String message = "Merged " + branchName + " into " + Branches.currentBranch;
        commit(message);
        checkoutCurrentBranch();
        if (mergeConflict) {
            System.out.println("Encountered a merge conflict.");
        }
    }
    private static Commit findSplitPointer(String branchName) {
        Commit givenBranchCommit = Branches.getCommit(branchName);
        Commit commit = givenBranchCommit;
        /** Contains hashCode of all commits in given branch */
        HashSet<String> hashcodeSet = new HashSet<>();
        hashcodeSet.add(commit.getHashCode());
        while (commit.hasParentCommit()) {
            commit = Commit.getCommit(commit.getParentCommit());
            hashcodeSet.add(commit.getHashCode());
        }
        commit = Branches.getCommit(Branches.currentBranch);
        while (!hashcodeSet.contains(commit.getHashCode())) {
            commit  = Commit.getCommit(commit.getParentCommit());
        }
        return commit;
        /** 'commit' is now the split pointer commit */

    }

    private static void checkoutCurrentBranch() {
        Commit commit = Branches.getCommit(Branches.currentBranch);
        HashMap<String, String> Blobs = commit.getBlobs();
        for (Map.Entry<String, String> entry : Blobs.entrySet()) {
            String fileName = entry.getKey();
            String hashCode = entry.getValue();
            Blob blob = Blob.getBlob(hashCode);
            File file = Utils.join(CWD, fileName);
            Utils.writeContents(file, blob.getFileContent());
        }
    }
}
