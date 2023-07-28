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
            case "commit":
                validateNumArgs("commit", args, 2);
                String message = args[1];
                if (message.isEmpty()) {
                    System.out.println("Please enter a commit message");
                    break;
                }
                Repository.commit(message);
                break;
            case "rm":
                validateNumArgs("rm", args, 2);
                String fileName1 = args[1];
                Repository.rm(fileName1);
                break;
            case "log":
                validateNumArgs("log", args, 1);
                Repository.log();
                break;
            case "global-log":
                validateNumArgs("global-log", args, 1);
                Repository.global_log();
                break;
            case "status":
                validateNumArgs("status", args, 1);
                Repository.status();
                break;
            case "find":
                validateNumArgs("find", args, 2);
                Repository.find(args[1]);
                break;
            case "checkout":

                if (args.length == 3) {
                    /** java gitlet.Main checkout -- [file name] */
                    Repository.checkout(args[2], 1);

                } else if (args.length == 2) {
                    /** java gitlet.Main checkout [branch name] */
                    Repository.checkout(args[1], 2);
                }
                else if (args.length == 4) {
                    /** java gitlet.Main checkout [commit id] -- [file name] */
                    Repository.checkout(args[1], args[3]);
                } else {
                    Utils.exitWithError("No command with that name exist");
                }
                break;
            case "branch":
                validateNumArgs("branch", args, 2);
                String branchName = args[1];
                Repository.branch(branchName);
                break;
            case "rm-branch":
                validateNumArgs("rm-branch", args, 2);
                Repository.rm_branch(args[1]);
                break;
            case "merge":
                validateNumArgs("merge", args, 2);
                Repository.merge(args[1]);
                break;
            case "reset":
                validateNumArgs("reset", args, 2);
                Repository.reset(args[1]);
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
