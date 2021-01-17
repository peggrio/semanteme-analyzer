import java.io.File;//以字节为单位进行处理
import java.io.FileNotFoundException;
import java.io.FileWriter;//以字符为单位进行处理
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;
public class Analyzer {
    protected File inputFile;
    protected File outputFile;
    protected String fileContent;
    protected static ArrayList<Word> list=new ArrayList<>();

    public Analyzer(String input,String output){
        inputFile =new File(input);
        outputFile=new File(output);
    }

    //从指定文件中读取源程序内容

    public String getContent(){
        StringBuilder StringBuilder=new StringBuilder();
        try(Scanner reader=new Scanner(inputFile)){
            while(reader.hasNextLine()){
                String line=reader.nextLine();
                StringBuilder.append(line+"\n");
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }return fileContent=StringBuilder.toString();
    }
    //先将源程序中的注释和换行替换成空串
    //然后扫描程序，在程序结束前将扫描到的词添加到list中
    //最后把扫描结果保存到指定的文件中

    public void analyze(String fileContent) {
        int over = 1;
        Word word ;
        WordsScanner scanner = new WordsScanner(fileContent.toCharArray());
        while (over != 0) {
            word = scanner.scan();
            list.add(word);
            over = word.getTypenum();//直到读到最后一个字符#
        }
        saveResult();
    }
    /*将结果写入到到指定文件中
    如果文件不存在，则创建一个新的文件
    用一个foreach循环将list中的项变成字符串写入到文件中*/

    public void saveResult() {
        if (!outputFile.exists())
            try {
                outputFile.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        try (Writer writer = new FileWriter(outputFile)) {
            for (Word word : list) {
                writer.write("(" + word.getTypenum() + "," + word.getWord() + ")\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        //System.out.println("说明：除了书上的单词符号种别码外，将单行注释的种别码设为30，多行注释的种别码设为31");
            Analyzer analyzer=new Analyzer("src/input.txt","output.txt");//文件放在当前文件夹下
        analyzer.analyze((analyzer.getContent()));
    }
}
