package gitlet;



import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    /** CWD is also the working directory */
    public static final File CWD = Repository.CWD;
    public static final File BLOB_DIR = Repository.BLOB_DIR;

    private byte[] fileContent;

    private String fileName;
    private String hashCode;


    /** File in working directory */
    public File workingFile;

    public Blob(String fileName) {
        this.fileName = fileName;
        workingFile = Utils.join(CWD, fileName);
        fileContent = Utils.readContents(workingFile);
        this.setHashCode();

    }
    /** Hash Code Generation */
    public void setHashCode() {
        hashCode = Utils.sha1(Utils.serialize(this));
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

    public byte[] getFileContent() {
        return fileContent;
    }

    public static Blob getBlob(String hashCode) {
        File blobFile = Utils.join(BLOB_DIR, hashCode);
        return Utils.readObject(blobFile, Blob.class);
    }

    public void mergeBlob(Blob currentBlob, Blob givenBlob) {
        String string1 = "<<<<<<< HEAD\n";
        String string2 = "=======\n";
        String string3 = ">>>>>>>";
        byte[] array1 = string1.getBytes();
        byte[] array2 = string2.getBytes();
        byte[] array3 = string3.getBytes();
        this.fileContent = new byte[array1.length + currentBlob.getFileContent().length + array2.length + givenBlob.getFileContent().length + array3.length];

    }



}
