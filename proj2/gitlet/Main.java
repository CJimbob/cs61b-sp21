package gitlet;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */

    public static final Path GITLET_PATH = Paths.get(".gitlet");
    public static void main(String[] args) {

        if (args.length == 0) {
            Utils.exitWithError("Please enter a command");
        }

        String firstArg = args[0];
        switch(firstArg) {
            case "init":

                if (Files.exists(GITLET_PATH)) {
                    Utils.exitWithError("A Gitlet version-control system already exists in the current directory.");
                } else {
                    Repository.init();
                }
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                validateNumArgs("add", args, 2);
                String fileName = args[1];
                Path filePath = Paths.get(fileName);
                if (Files.exists(filePath)) {
                    Repository.add(fileName);
                } else {
                    Utils.exitWithError("File does not exist.");
                }
                break;
            // TODO: FILL THE REST IN
            case "commit":
                validateNumArgs("commit", args, 2);
                String message = args[1];
                Repository.commit(message);
                break;
            case "rm":
                break;
            case "log":
                break;
            default:
                Utils.exitWithError("No command with that name exists");

        }


    }


    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (args.length != n) {
            throw new RuntimeException(
                    String.format("Invalid number of arguments for: %s.", cmd));
        }
    }

}
