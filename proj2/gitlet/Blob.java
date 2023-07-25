package gitlet;



import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    /** CWD is also the working directory */
    public static final File CWD = Repository.CWD;
    public static final File BLOB_DIR = Repository.BLOB_DIR;

    private String fileContent;

    private String fileName;
    private String hashCode;

    private boolean commited = false;

    /** File in working directory */
    public File workingFile;

    public Blob(String fileName) {
        this.fileName = fileName;
        workingFile = Utils.join(CWD, fileName);
        fileContent = Utils.readContentsAsString(workingFile);
        this.setHashCode();

    }
    /** Hash Code Generation */
    public void setHashCode() {
        hashCode = Utils.sha1(Utils.readContents(workingFile));
    }
    public String getHashCode() {
        return hashCode;
    }

    /** Write blob into a file in BLOB_DIR */
    public void addBlobToBLOB_DIR() {
        File blobFile = Utils.join(BLOB_DIR, this.getHashCode());
        Utils.writeObject(blobFile, this);
    }

    public String getFileName() {
        return this.fileName;
    }

    public static Blob readBlobFromFile(String hashCode) {
        File blobFile = Utils.join(BLOB_DIR, hashCode);
        return Utils.readObject(blobFile, Blob.class);
    }

    public void updateContent() {
        fileContent = Utils.readContentsAsString(workingFile);
    }

    public String getFileContent() {
        return fileContent;
    }

    public boolean isCommited() {
        return commited;
    }

    public void setCommited() {
        commited = true;
    }


}
