import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Analyzer {
        protected File inputFile;
        protected File outputFile1;
        protected String fileContent;
        protected static ArrayList<Word> list = new ArrayList<>();

        public Analyzer(String input, String output1) {

            inputFile = new File(input);
            outputFile1 = new File(output1);
        }
        //从指定文件中读取源程序内容

        public String getContent() {
            StringBuilder StringBuilder = new StringBuilder();
            try (Scanner reader = new Scanner(inputFile)) {
                while (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    StringBuilder.append(line + "\n");
                }
                StringBuilder.append("\0");//在文件的最后添加一个终止符
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return fileContent = StringBuilder.toString();
        }
        //先将源程序中的注释和换行替换成空串
        //然后扫描程序，在程序结束前将扫描到的词添加到list中
        //最后把扫描结果保存到指定的文件中

        public void analyze(String fileContent) throws IOException {

            int over = 1;
            Word word;
            WordScanner scanner = new WordScanner(fileContent.toCharArray());
            while (over != 1000) {
                word = scanner.scan();
                list.add(word);
                over = word.getTypenum();//直到读到最后一个字符#
            }
            saveResult_1();
        }
    /*将结果写入到到指定文件中
    如果文件不存在，则创建一个新的文件
    用一个foreach循环将list中的项变成字符串写入到文件中*/

    //装词法扫描的结果
        public void saveResult_1() {
            if (!outputFile1.exists())
                try {
                    outputFile1.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            try (Writer writer = new FileWriter(outputFile1)) {
                for (Word word : list) {
                    writer.write("(" + word.getTypenum() + "," + word.getWord() + ")\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void main(String[] args) throws IOException {
            Analyzer analyzer=new Analyzer("src/file.txt","output1.txt");//文件放在当前文件夹下
            analyzer.analyze((analyzer.getContent()));
            //创建一个result.txt文档装入语义分析的结果
            var fis=new File("result.txt");
            if(!fis.exists()){
                try {
                    fis.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Semanteme semanteme=new Semanteme("result.txt",list);
            semanteme.lrparse();
        }
    }
