/*种别码：
main---1
int----2
char---3
if-----4
else---5
for----6
while--7
letter(letter|digit)---10
digit-11
!=----12
+-----13
------14
*-----15
/-----16
:-----17
:=----18
<>----19
<-----20
==----21
<=----22
>-----23
>=----24
=-----25
;-----26
(-----27
)-----28
[-----29
]-----30
{-----31
}-----32
//----33     识别单行换行
/*----34     识别多行换行
\0----1000   程序结束标志
*/

import java.util.ArrayList;

public class WordScanner {
//数据域
        protected static String _KEY_WORD_END="end string of string";
        protected int charNum=0;

        protected char[] input=new char[255];
        protected char[] token=new char[255];
        protected int p_input=0;
        protected int p_token=0;

        protected char ch;

        private int head=1;
        private int kk;
        private int mm=1;
        private int i=0;
        private ArrayList<Word> saveResultList;
        private int zuokuohao=0;
        private int ii=0;

        protected String[] keyWord={"main","int","char","if","else","for","while","",_KEY_WORD_END};
        //构造函数
        public WordScanner(char[] input) {
            this.input = input;
        }

        //取下一个字符

        public char m_getch(){
            if(p_input<input.length){
                ch=input[p_input];
                p_input++;
            }
            return ch;
        }
        //如果是标识符或者空白符就取下一个字符

        public void getbc(){
            while((ch==' '||ch=='\t')&&p_input<input.length){
                ch=input[p_input];
                p_input++;
            }
        }

        //把当前字符和原有字符串连接

        public void concat(){
            token[p_token]=ch;
            p_token++;
            token[p_token]='\0';
        }
        //判断是否是字母
        public boolean letter(){
            if(ch>='a'&&ch<='z'||ch>='A'&&ch<='Z')
                return true;
            else
                return false;
        }
        //判断是否是数字
        public boolean digit(){
            if(ch>='0'&&ch<='9') {
                return true;
            }
            return false;
        }
        //判断是否是小数点
        public boolean dot(){
            if(ch=='.'){
                return true;
            }
            return false;
        }

        //回退一个字符

        public void retract(){
            p_input--;
        }

        //查看token中的字符串是否是关键字，是的话返回关键字种别编码，否则返回10

        public int reserve() {
            int  i=0;
            while(keyWord[i].compareTo(_KEY_WORD_END)!=0) {
                if(keyWord[i].compareTo(new String(token).trim()) == 0) {
                    return i+1;
                }
                i++;
            }
            return 10;
        }

        //分析器的词法扫描部分
        public Word scan() {
            token = new char[255];
            Word myWord = new Word();
            myWord.setTypenum(10);
            myWord.setWord("");

            p_token = 0;
            m_getch();
            getbc();
            if (letter()) {
                while (letter() || digit()) {//如果字符的后面是数字或字符
                    concat();
                    m_getch();
                }
                retract();
                myWord.setTypenum(reserve());
                myWord.setWord(new String(token).trim());//输出token中的数字串字符形式
                return myWord;
            }else if(digit()) {
                boolean dotOnlyOnce=true;
                while(digit()||(dot()&&dotOnlyOnce)) {//如果数字的后面还是数字,或小数点
                    concat();
                    if (dot()) {//如果数字的后面是小数点,那么只允许出现一次
                        dotOnlyOnce = false;
                    }
                    m_getch();
                }
                retract();
                myWord.setTypenum(11);
                myWord.setWord(new String(token).trim());	//输出token中的数字串字符形式
                return myWord;
            } else
                switch (ch) {
                    case '=':
                        m_getch();
                        if(ch=='='){
                            myWord.setTypenum(21);
                            myWord.setWord("==");
                            return myWord;
                        }
                        retract();
                        myWord.setTypenum(25);
                        myWord.setWord("=");
                        return myWord;
                    case '+':
                        myWord.setTypenum(13);
                        myWord.setWord("+");
                        return myWord;
                    case '-':
                        myWord.setTypenum(14);
                        myWord.setWord("-");
                        return myWord;
                    case '*':
                        myWord.setTypenum(15);
                        myWord.setWord("*");
                        return myWord;
                    case '/':    //识别换行，单行注释，多行注释
                        //换行的种别码：30
                        //多行的种别码：31
                        m_getch();
                        //识别单行注释
                        if (ch == '/') {
                            String string = "";
                            while (m_getch() != '\n') {
                                retract();
                                string += ch;
                                m_getch();
                            }
                            myWord.setTypenum(33);
                            myWord.setWord(string);
                            return myWord;
                        }
                        //识别多行注释
                        if (ch == '*') {
                            String string = "";
                            int hang=0;
                            while (true) {
                                m_getch();
                                if (ch == '*') {//注释的结尾
                                    if (m_getch() == '/') {
                                        myWord.setTypenum(34);
                                        myWord.setWord(string);
                                        return myWord;
                                    }
                                    retract();
                                }else {
                                    if(ch=='\n'){continue;}else{string += ch;}
                                }
                            }
                        }
                        retract();
                        myWord.setTypenum(16);
                        myWord.setWord("/");
                        return myWord;
                    case ':':
                        m_getch();
                        if (ch == '=') {
                            myWord.setTypenum(18);
                            myWord.setWord(":=");
                            return myWord;
                        }
                        retract();
                        myWord.setTypenum(17);
                        myWord.setWord(":");
                        return myWord;
                    case '<':
                        m_getch();
                        if (ch == '=') {
                            myWord.setTypenum(22);
                            myWord.setWord("<=");
                            return myWord;
                        } else if (ch == '>') {
                            myWord.setTypenum(19);
                            myWord.setWord("<>");
                            return myWord;
                        }
                        retract();
                        myWord.setTypenum(20);
                        myWord.setWord("<");
                        return myWord;
                    case '>':
                        m_getch();
                        if (ch == '=') {
                            myWord.setTypenum(24);
                            myWord.setWord(">=");
                            return myWord;
                        }
                        retract();
                        myWord.setTypenum(23);
                        myWord.setWord(">");
                        return myWord;
                    case ';':
                        myWord.setTypenum(26);
                        myWord.setWord(";");
                        return myWord;
                    case '(':
                        myWord.setTypenum(27);
                        myWord.setWord("(");
                        return myWord;
                    case ')':
                        myWord.setTypenum(28);
                        myWord.setWord(")");
                        return myWord;
                    case'[':
                        myWord.setTypenum(29);
                        myWord.setWord("[");
                        return myWord;
                    case']':
                        myWord.setTypenum(30);
                        myWord.setWord("]");
                        return myWord;
                    case'{':
                        myWord.setTypenum(31);
                        myWord.setWord("{");
                        return myWord;
                    case'}':
                        myWord.setTypenum(32);
                        myWord.setWord("}");
                        return myWord;
                    case '\n':
                        myWord.setTypenum(33);
                        myWord.setWord("\\n");
                        return myWord;
                    case '\0':
                        myWord.setTypenum(1000);
                        myWord.setWord("\\0");
                        return myWord;
                    //报错
                    default:
                        concat();
                        myWord.setTypenum(-1);
                        myWord.setWord("ERROR INFO: WORD = \"" + new String(token).trim() + "\"");
                        return myWord;
                }
        }
    //分析器的词法扫描部分

//生成一个三地址语句送到四元式表中
        public void emit(String result, String ag1, String op, String ag2) {
            System.out.print("("+head+")\t");
            System.out.println(result+"="+ag1+op+ag2);
            head++;
            /*String temp="("+head+")\t"+result+"="+ag1+op+ag2;
            saveResultList.add(temp);
            head++;*/
        }

    public ArrayList<Word> getSaveResultList() {
        return saveResultList;
    }

    //回送一个新的临时变量名，临时变量名产生顺序为t1,t2,……
        public String newtemp() {
            String t="t"+mm;
            mm++;
            return t;
        }

        //程序主方法
        public int lrparser(ArrayList<Word> list) {
            this.saveResultList=list;
            int schain = 0;
            kk = 0;
            if (list.get(i).getTypenum() == 1) {
                i++;
                if (list.get(i).getTypenum() != 27 || list.get(i + 1).getTypenum() != 28) {
                    System.out.println("main方法入口错误");
                    kk = 1;
                }
            } else {
                System.out.println("找不到main方法");
            }
            i+=2;
            if (list.get(i).getTypenum() == 31) {//语句块{的入口
                schain = yucu(list);
                i++;
                zuokuohao++;
                if (list.get(i).getTypenum() == 1000) {
                    if (kk == 0) {
                        System.out.println("success!");
                    }
                } else {
                    if (kk == 0) {
                        System.out.println("程序以不正确的姿势结束，sad~");
                        kk = 1;
                    }
                }
            }else {
                System.out.println("程序没有方法体");
                kk = 1;
            }
                return schain;
            }

    public int yucu(ArrayList<Word> list){//判断{}括起来的语句块
        int schain=0;
        while (list.get(i).getTypenum() == 31) {//如果遇到左括号{
            i++;
            zuokuohao++;
            while(list.get(i).getTypenum()==33 || list.get(i).getTypenum() == 34) {//如果是换行
                i++;
            }
            yucu_2(list);
            while(list.get(i).getTypenum()==33 || list.get(i).getTypenum() == 34) {//如果是换行
                i++;
            }
            if (list.get(i).getTypenum() == 32) {//如果遇到右括号}
                zuokuohao--;
                checkPairs(zuokuohao);
                i++;
            }
            while(list.get(i).getTypenum()==33 || list.get(i).getTypenum() == 34) {//如果是换行
                i++;
            }
            schain = statement(list);
        }
        return schain;
    }

        public int yucu_2(ArrayList<Word> list) {//判断由分号分割的语句块
            int schain = 0;
            int temp=i;
            schain = statement(list);
            if(i==temp){//如果执行不了statement，说明这是一个条件状语
conditions(list);
            }
            while (list.get(i).getTypenum() == 26) {//如果遇到分号
                i++;
                while (list.get(i).getTypenum() == 33 || list.get(i).getTypenum() == 34) {//如果是换行
                    i++;
                }
                if (list.get(i).getTypenum() == 7 || list.get(i).getTypenum() == 4) {//如果是while\if引导的条件语句块，则要另外判断
                    conditions(list);
                } else {
                    schain = statement(list);
                }
            }
            return schain;
        }


        //判断左右括号是否匹配
        public void checkPairs(int zuokuohao){
            if(zuokuohao<0){
                System.out.println("右括号输多了");
                kk=1;
            }
        }
        public int conditions(ArrayList<Word> list){
            int schain=0;
            i++;
            ArrayList<Word> list_1=new ArrayList<>();
            if(list.get(i).getTypenum()==27){
                i++;
                while(list.get(i).getTypenum()!=28){
                    list_1.add(list.get(i));
                    i++;
                }
                statement_2_forCondition(list_1);
            }else{
                System.out.println("条件语句没有条件");
            }
            return schain;
        }

        public int statement(ArrayList<Word> list) {
            String tt,eplace;
            int schain = 0;
            switch (list.get(i).getTypenum()) {
                case 10: {//赋值语句
                    tt = list.get(i).getWord();
                    i++;
                    if (list.get(i).getTypenum() == 25) {//如果这个字符是=
                        i++;
                        eplace = expression(list);
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

        public String expression(ArrayList<Word> list) {
            String tp,tt, ep2, eplace;
            eplace = term(list);
            while (list.get(i).getTypenum()== 13 ||list.get(i).getTypenum() == 14) {//操作符为+/-
                tt=list.get(i).getWord();
                i++;
                ep2 = term(list);
                tp = newtemp();//调用newtemp产生临时变量tp存储计算结果
                emit(tp, eplace, tt, ep2);//生成四元式送入四元表
                eplace = tp;//将计算结果作为下一次表达式计算的第一项eplace
            }
            return eplace;
        }

        public String term(ArrayList<Word> list) {
            String  ep2, eplace, tt,tp;
            eplace = factor(list);
            while (list.get(i).getTypenum() == 15 || list.get(i).getTypenum()== 16) {//操作符为*/
                tt=list.get(i).getWord();
                i++;
                ep2 = factor(list);
                tp = newtemp();//调用newtemp产生临时变量tp存储计算结果
                emit(tp, eplace, tt, ep2);//生成四元式送入四元表
                eplace = tp;//将计算结果作为下一次表达式计算的第一项eplace
            }
            return eplace;
        }

        public String factor(ArrayList<Word> list) {
            String fplace ="";
            if (list.get(i).getTypenum() == 10) {
                fplace = list.get(i).getWord();
                i++;
            } else if (list.get(i).getTypenum() == 11) {
                fplace = list.get(i).getWord();
                i++;
            } else if (list.get(i).getTypenum() == 27) {
                i++;
                fplace = expression(list);
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

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void emit_2(String result, String ag1, String op, String ag2) {
        System.out.print("("+head+")\t");
        System.out.print(result+"\t"+ag1+op+ag2+"\t");
        head++;
            /*String temp="("+head+")\t"+result+"="+ag1+op+ag2;
            saveResultList.add(temp);
            head++;*/
    }
    public int statement_2_forCondition(ArrayList<Word> list) {
        ii=0;
        String tt,eplace;
        int schain = 0;
        switch (list.get(ii).getTypenum()) {
            case 10:
            case 11:{//赋值语句
                tt = list.get(ii).getWord();
                ii++;
                int temp=list.get(ii).getTypenum();
                if (temp == 19||temp==20||temp==22||temp == 23||temp==24||temp==12) {//如果这个字符是==、！=、>、<、<=、>=
                    String op = list.get(ii).getWord();
                    ii++;
                    if (list.get(ii).getTypenum() == 10 || list.get(ii).getTypenum() == 11) {
                        emit_2("if", tt, op, list.get(ii).getWord());//生成四元式送入四元表
                        schain = 0;
                        i++;
                        gotoCheck(saveResultList);
                    }
                }else {
                    System.out.println("赋值号错误");
                    kk = 1;
                }
                break;
            }
        }
        return schain;
    }

    public void gotoCheck(ArrayList<Word> saveResultList){
        System.out.println("goto");///////////////////
        yucu(saveResultList);
        return;
    }

    public String expression_2(ArrayList<Word> list) {
        String tp,tt, ep2, eplace;
        eplace = term_2(list);
        while (list.get(ii).getTypenum()== 13 ||list.get(ii).getTypenum() == 14) {//操作符为+/-
            tt=list.get(ii).getWord();
            ii++;
            ep2 = term_2(list);
            tp = newtemp();//调用newtemp产生临时变量tp存储计算结果
            emit(tp, eplace, tt, ep2);//生成四元式送入四元表
            eplace = tp;//将计算结果作为下一次表达式计算的第一项eplace
        }
        return eplace;
    }

    public String term_2(ArrayList<Word> list) {
        String  ep2, eplace, tt,tp;
        eplace = factor_2(list);
        while (list.get(ii).getTypenum() == 15 || list.get(ii).getTypenum()== 16) {//操作符为*/
            tt=list.get(ii).getWord();
            ii++;
            ep2 = factor_2(list);
            tp = newtemp();//调用newtemp产生临时变量tp存储计算结果
            emit(tp, eplace, tt, ep2);//生成四元式送入四元表
            eplace = tp;//将计算结果作为下一次表达式计算的第一项eplace
        }
        return eplace;
    }

    public String factor_2(ArrayList<Word> list) {
        String fplace ="";
        if (list.get(ii).getTypenum() == 10) {
            fplace = list.get(ii).getWord();
            ii++;
        } else if (list.get(ii).getTypenum() == 11) {
            fplace = list.get(ii).getWord();
            ii++;
        } else if (list.get(ii).getTypenum() == 27) {
            ii++;
            fplace = expression_2(list);
            if (list.get(ii).getTypenum() == 28) {
                ii++;
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
