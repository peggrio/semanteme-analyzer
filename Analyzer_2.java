import java.util.ArrayList;
import java.util.Scanner;

public class Analyzer_2 extends Analyzer {
    public Analyzer_2(String input, String output) {
        super(input, output);
    }
    protected ArrayList<Integer> list2=new ArrayList<>();

    //改写analyze方法
    @Override
    public void analyze(String fileContent) {

        int over = 1;
        Word word2 = new Word();
        WordsScanner scanner = new WordsScanner(fileContent.toCharArray());

        while (over != 0) {
            word2 = scanner.scan();
            list2.add(word2.getTypenum());
            over = word2.getTypenum();//直到读到最后一个字符#
        }

        int[] aim=new int[list2.size()];
        int k=0;
        for(int i:list2){aim[k]=i;k++;}
        GrammerScanner grammerScanner = new GrammerScanner(aim);
        grammerScanner.lrparser();
    }

    public static void main(String[] args) {
        Analyzer_2 analyzer2 = new Analyzer_2("src/input.txt", "output2.txt");//文件放在当前文件夹下，实际上没有用到output2
        analyzer2.analyze((analyzer2.getContent()));
    }
}