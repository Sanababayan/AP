import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main
{

    public static void main(String[] args)
    {
        Pattern pattern = Pattern.compile("\\s*[a-zA-Z0-9.]((\"[^\"]*\")|[^;|])*\\s*[;|]");
        Scanner scanner = new Scanner(System.in);
        StringBuilder voroodi = new StringBuilder();
        while (scanner.hasNextLine())
            voroodi.append(scanner.nextLine());


        Matcher matcher = pattern.matcher(voroodi.toString());
//        Matcher matcher = pattern.matcher("echo \"first > message\" > \"file1.;txt;\"; cat \"file1.;txt;\"; echo \"second;; | message >>\" >> \"file1.;txt;\"; cat \"file1.;txt;\"; echo \" > third message\" >> \"file2.md\"; cat \"file2.md\"; exit;");
        int index = 1;
        ArrayList<String> pipeLog = new ArrayList<>();
        HashMap<String, String> files = new HashMap<>();
        StringBuilder output;


        while (matcher.find())
        {
            String kamel = matcher.group();
            if (Pattern.matches("\\s*exit\\s*;", kamel))
                break;
            output = new StringBuilder();
            Pattern CS = Pattern.compile("^\\s*(?<com>[0-9a-zA-Z.]+)(\\s+(?<sub>[0-9a-zA-Z.]+))?");
            Matcher commandSubcommand = CS.matcher(kamel);
            commandSubcommand.find();
            String part = kamel.substring(commandSubcommand.end(), kamel.length() - 1);
            output.append(("command ")).append(index).append("\n");
            output.append(("command: ")).append(commandSubcommand.group("com")).append("\n");
            boolean catFlag = commandSubcommand.group("com").equals("cat");
            if (commandSubcommand.group("sub") != null)
                output.append("subcommand: ").append(commandSubcommand.group("sub")).append("\n");


            Pattern optionsPattern = Pattern.compile("\\s+-(?<all>[.a-zA-Z0-9]*)(?<op>[.a-zA-Z0-9])(\\s+(?<arg>\"[^\"]+\"))?");
            Matcher optionsMatcher = optionsPattern.matcher(part);
            int opIndex = 1;
            while (optionsMatcher.find())
            {
                boolean notInArg = true;
                for (int i = 0; i < optionsMatcher.start(); i++)
                    if (part.charAt(i) == '\"')
                        notInArg = !notInArg;
                if (notInArg)
                {
                    for (int i = 0; i < optionsMatcher.group("all").length(); i++)
                    {
                        output.append(("option ")).append(opIndex).append(": ").append(optionsMatcher.group("all").charAt(i)).append("\n");
                        opIndex++;
                    }
                    if (optionsMatcher.group("arg") != null)
                        output.append(("option ")).append(opIndex).append(": ").append(optionsMatcher.group("op").charAt(0)).append(" = ").append(optionsMatcher.group("arg")).append("\n");
                    else
                        output.append(("option ")).append(opIndex).append(": ").append(optionsMatcher.group("op").charAt(0)).append("\n");
                    opIndex++;
                }
            }

            Pattern flagPattern = Pattern.compile("\\s+--(?<flag>[.a-zA-Z0-9]+)");
            Matcher flagMatcher = flagPattern.matcher(part);
            int flagIndex = 1;
            while (flagMatcher.find())
            {
                boolean notInArg = true;
                for (int i = 0; i < flagMatcher.start(); i++)
                    if (part.charAt(i) == '\"')
                        notInArg = !notInArg;
                if (notInArg)
                {
                    output.append("flag ").append(flagIndex).append(": ").append(flagMatcher.group("flag")).append("\n");
                    flagIndex++;
                }
            }

            String file;
            String lastArg = "";
            Pattern fileName = Pattern.compile(".*(\"[^\"]*\"|.)*\\s+>\\s+(?<file>\"[^\"]+\").*");
            Pattern fileNameAppend = Pattern.compile(".*(\"[^\"]*\"|.)*\\s+>>\\s+(?<file>\"[^\"]+\").*");
            Matcher save = fileName.matcher(part);
            Matcher append = fileNameAppend.matcher(part);
//            System.out.println(part);
            if (append.find())
            {
                file = append.group("file");
                part = part.substring(0, part.lastIndexOf(file) - 1);
                Pattern arg = Pattern.compile("\\s(?<argument>(\"[^\"]+\")|[a-zA-Z0-9.]+)");
                Matcher argMatch = arg.matcher(part);
                int argIndex = 1;
                while (argMatch.find())
                {
                    lastArg = argMatch.group("argument");
                    output.append(("argument ")).append(argIndex).append(": ").append(lastArg).append("\n");
                    argIndex++;
                }
                for (int i = 0; i < pipeLog.size(); i++)
                {
                    output.append(("input ")).append(i + 1).append(": ").append(pipeLog.get(i)).append("\n");
                }
                System.out.println(output);
                if (files.containsKey(file))
                    files.replace(file, files.get(file) + output + "\n");
                else
                    files.put(file, output + "\n");
            }
            else if (save.find())
            {
                file = save.group("file");
                part = part.substring(0, part.lastIndexOf(file) - 1);
                Pattern arg = Pattern.compile("\\s(?<argument>(\"[^\"]+\")|[a-zA-Z0-9.]+)");
                Matcher argMatch = arg.matcher(part);
                int argIndex = 1;
                while (argMatch.find())
                {
                    lastArg = argMatch.group("argument");
                    output.append(("argument ")).append(argIndex).append(": ").append(lastArg).append("\n");
                    argIndex++;
                }
                for (int i = 0; i < pipeLog.size(); i++)
                {
                    output.append(("input ")).append(i + 1).append(": ").append(pipeLog.get(i)).append("\n");
                }
                System.out.println(output);
                files.remove(file);
                files.put(file, output + "\n");
            }
            else
            {

                Pattern arg = Pattern.compile("\\s(?<argument>(\"[^\"]+\")|[a-zA-Z0-9.]+)");
                Matcher argMatch = arg.matcher(part);
                int argIndex = 1;
                while (argMatch.find())
                {
                    lastArg = argMatch.group("argument");
                    output.append(("argument ")).append(argIndex).append(": ").append(lastArg).append("\n");
                    argIndex++;
                }
                for (int i = 0; i < pipeLog.size(); i++)
                {
                    output.append(("input ")).append(i + 1).append(": ").append(pipeLog.get(i)).append("\n");
                }
                System.out.println(output);
            }
            if (catFlag)
            {
                System.out.print(files.get(lastArg));
            }
            if (matcher.group().charAt(matcher.group().length() - 1) == '|')
                pipeLog.add("command " + index);
            else
                pipeLog.clear();
            index++;
        }
    }
}