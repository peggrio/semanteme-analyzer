import java.util.ArrayList;

public class SemanticScanner {
    //数据域
    protected ArrayList<Word> list;
    private int i = 0;
    private int kk;
    private int mm=1;
    private int head=1;
    Word word3=new Word();


    //构造函数
    public SemanticScanner(ArrayList<Word> list) {
        this.list = list;
    }

    //生成一个三地址语句送到四元式表中
    public void emit(String result, String ag1, String op, String ag2) {
        System.out.print("("+head+")\t");
        System.out.println(result+"="+ag1+op+ag2);
        head++;
    }

    //回送一个新的临时变量名，临时变量名产生顺序为t1,t2,……
    public String newtemp() {
        String t="t"+mm;
        mm++;
        return t;
    }

    //程序主方法
    public int lrparser() {
        int schain = 0;
        kk = 0;
        if (list.get(i).getTypenum() == 1) {
            i++;
        }else{System.out.println("begin错误");
            kk = 1;}
            schain = yucu();
            if (list.get(i).getTypenum() == 6) {
                i++;
                if (list.get(i).getTypenum() == 0 && kk == 0) {
                    System.out.println("success!");
                }
            } else {
                if (kk != 1) {
                    System.out.println("缺end");
                    kk = 1;
                }
            }

        return schain;
    }

    public int yucu() {
        int schain = 0;
        while(list.get(i).getTypenum() == 30||list.get(i).getTypenum() == 31){//如果分号后面是换行或者注释
            i++;
        }
        schain = statement();
        while (list.get(i).getTypenum() == 26) {//如果遇到分号
            i++;
            while(list.get(i).getTypenum() == 30||list.get(i).getTypenum() == 31){//如果分号后面是换行或者注释
                i++;
            }
            schain = statement();

        }
        return schain;
    }

    public int statement() {
        String tt,eplace;
        int schain = 0;
        switch (list.get(i).getTypenum()) {
            case 10: {//赋值语句
                tt = list.get(i).getWord();
                i++;
                if (list.get(i).getTypenum() == 18) {//如果这个字符是:=
                    i++;
                    eplace = expression();
                    emit(tt, eplace, "", "");//生成四元式送入四元表
                    schain = 0;
                } else {
                    System.out.println("缺少赋值号");
                    kk = 1;
                }
                break;
            }
        }
        return schain;
    }

    public String expression() {
        String tp,tt, ep2, eplace;
        eplace = term();
        while (list.get(i).getTypenum()== 13 ||list.get(i).getTypenum() == 14) {//操作符为+/-
           tt=list.get(i).getWord();
            i++;
            ep2 = term();
            tp = newtemp();//调用newtemp产生临时变量tp存储计算结果
            emit(tp, eplace, tt, ep2);//生成四元式送入四元表
            eplace = tp;//将计算结果作为下一次表达式计算的第一项eplace
        }
        return eplace;
    }

    public String term() {
        String  ep2, eplace, tt,tp;
        eplace = factor();
        while (list.get(i).getTypenum() == 15 || list.get(i).getTypenum()== 16) {//操作符为*/
            tt=list.get(i).getWord();
            i++;
            ep2 = factor();
            tp = newtemp();//调用newtemp产生临时变量tp存储计算结果
            emit(tp, eplace, tt, ep2);//生成四元式送入四元表
            eplace = tp;//将计算结果作为下一次表达式计算的第一项eplace
        }
        return eplace;
    }

    public String factor() {
        String fplace ="";
        if (list.get(i).getTypenum() == 10) {
            fplace = list.get(i).getWord();
            i++;
        } else if (list.get(i).getTypenum() == 11) {
            fplace = list.get(i).getWord();
            i++;
        } else if (list.get(i).getTypenum() == 27) {
            i++;
            fplace = expression();
            if (list.get(i).getTypenum() == 28) {
                i++;
            } else {
                System.out.println("缺）");
                kk = 1;
            }
        } else {
            System.out.println("缺（");
            kk = 1;
        }
        return fplace;
    }
}
