import java.util.ArrayList;

public class Analyzer_3 extends Analyzer_2 {
    //构造函数
    public Analyzer_3(String input, String output) {
        super(input, output);
    }
    protected ArrayList<Word> list3=new ArrayList<>();

    //改写analyze方法
    @Override
    public void analyze(String fileContent) {

        int over = 1;
        Word word3 = new Word();
        WordsScanner scanner = new WordsScanner(fileContent.toCharArray());

        while (over != 0) {
            word3 = scanner.scan();
            list3.add(word3);
            over = word3.getTypenum();//直到读到最后一个字符#
        }
        int[] aim=new int[list3.size()];
        int k=0;
            for(Word i:list3){aim[k]=i.getTypenum();k++;}
            SemanticScanner semanticScanner = new SemanticScanner(list3);
            semanticScanner.lrparser();
        }

    public static void main(String[] args) {
        Analyzer_3 analyzer3 = new Analyzer_3("src/input1.txt", "output2.txt");//文件放在当前文件夹下，实际上没有用到output2
        analyzer3.analyze((analyzer3.getContent()));
    }
}
