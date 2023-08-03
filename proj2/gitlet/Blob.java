package gitlet;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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

    public static void mergeBlob(Blob currentBlob, Blob givenBlob, String fileName) {
        String string1 = "<<<<<<< HEAD\n";
        String string2 = "\n=======\n";
        String string3 = "\n>>>>>>>";
        byte[] array1 = string1.getBytes();
        byte[] array2 = string2.getBytes();
        byte[] array3 = string3.getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(array1);
            outputStream.write(currentBlob.getFileContent());
            outputStream.write(array2);
            outputStream.write(givenBlob.getFileContent());
            outputStream.write(array3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = Utils.join(CWD, fileName);
        Utils.writeContents(file, outputStream.toByteArray());

    }



}
